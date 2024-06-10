package com.sits.rsrch.research_activity.fin_year_wise_fund_allocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sits.commonApi.commonAPI;
import com.sits.conn.DBConnection;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;

public class FundAllocationManager {
	static Logger l = Logger.getLogger("exceptionlog");

	public static String save(FundAllocationModel model, JSONArray consobj, String user_id, String machine) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		String qry = "", msg="0";
		String mid="",deleteDetail="",deleteMast="";
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			String Qry1 = "SELECT concat('FA', LPAD(CONVERT(IFNULL(MAX(SUBSTR(fund_allc_mid,3,6)),0)+1,SIGNED INTEGER),4,'0')) FROM rsrch_fund_allocation_mast";
			 psmt= conn.prepareStatement(Qry1);
			ResultSet rst1 = psmt.executeQuery();
			if (rst1.next()) {
				mid = General.checknull(rst1.getString(1));
			}
			psmt=null;
			deleteDetail="delete from rsrch_fund_allocation_detail where fund_allc_mid in "
					+ "(SELECT fund_allc_mid FROM rsrch_fund_allocation_mast where install_id='"+model.getInstalId()+"' and PS_MID='"+model.getResPrps()+"')";
			psmt=conn.prepareStatement(deleteDetail);
			psmt.executeUpdate();
		//	System.out.println("psmt delete rsrch_fund_allocation_detail"+psmt);
			psmt=null;
			deleteMast="delete from rsrch_fund_allocation_mast where install_id='"+model.getInstalId()+"' and PS_MID='"+model.getResPrps()+"'";
			psmt=conn.prepareStatement(deleteMast);
			psmt.executeUpdate();
			//System.out.println("psmt delete rsrch_fund_allocation_mast"+psmt);
			
			qry="INSERT INTO rsrch_fund_allocation_mast (fund_allc_mid, pi_id, PS_MID, fund_allc_dt, ";
			if(General.checknull(model.getRemark()).trim().equals("")){
				qry += "remarks, ";
			}
			qry += "created_by, created_machine, created_date, financial_year,LOCATION_CODE,DDO_ID,install_id ) VALUES (?, ?, ?, str_to_date(?,'%d/%m/%Y'), ";
			if(General.checknull(model.getRemark()).trim().equals("")){
				qry += "'"+General.checknull(model.getRemark()).trim()+"', ";
			}
			qry += "?, ?, now(), ?,?,?,?)";
			psmt=null;
			psmt = conn.prepareStatement(qry);		
			psmt.setString(1, General.checknull(mid).trim());
			psmt.setString(2, General.checknull(model.getPiid()).trim());
			psmt.setString(3, General.checknull(model.getResPrps()));
			psmt.setString(4, General.checknull(model.getDate()));
			psmt.setString(5, General.checknull(user_id));
			psmt.setString(6, General.checknull(machine));
			psmt.setString(7, General.checknull(model.getFinYr()));
			psmt.setString(8, General.checknull(model.getLocationCode()));
			psmt.setString(9, General.checknull(model.getDdoCode()));
			psmt.setString(10, General.checknull(model.getInstalId())); 
			int cnt = psmt.executeUpdate();
			if (cnt > 0){
				qry = ""; psmt=null;
				int cnt1=0;
				for(int i=0; i<consobj.size(); i++){
					JSONObject obj = (JSONObject) consobj.get(i);
					
					qry="INSERT INTO rsrch_fund_allocation_detail (fund_allc_mid, head_id, amount, created_by, created_machine, created_date,subhead_id) "
							+ "VALUES (?, ?, ?, ?, ?, now(),?)";
					psmt = conn.prepareStatement(qry);	
					psmt.setString(1, General.checknull(mid).trim());
					psmt.setString(2, General.checknull(obj.get("headId").toString()));
					psmt.setString(3, General.checknull(obj.get("amt").toString()));
					psmt.setString(4, General.checknull(user_id));
					psmt.setString(5, General.checknull(machine));
					psmt.setString(6, General.checknull(obj.get("sheadId").toString()).equals("NA")?null: General.checknull(obj.get("sheadId").toString()));
					cnt1 = psmt.executeUpdate();
				}
				
				if (cnt1 > 0){
					msg="1";
					conn.commit();
				}else{
					msg="0";
					conn.rollback();
				}
			} else {
				msg="0";
				conn.rollback();
			}
		} catch (Exception e) {
			msg="0";
			System.out.println("Exception in FundAllocationManager[SAVE]" + " " + e.getMessage());
			l.fatal(Logging.logException("FundAllocationManager[SAVE]", e.toString()));
		} finally {
			try {
				conn.close();
				psmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return msg;
	}
	
	public static ArrayList<FundAllocationModel> getList(String piId) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();
			query = "select fund_allc_mid, (select fa_Name from rsrch_funding_agency where fa_id=a.fa_id) agency, "
					+ "(select PS_TITTLE_PROJ from rsrch_form1_mast where PS_MID=a.PS_MID) proposal, fund_allc_dt, financial_year, pi_id "
					+ "from rsrch_fund_allocation_mast a where 1=1";
			if(!General.checknull(piId).trim().equals("")){
				query+=" and a.PS_MID = '"+General.checknull(piId)+"' ";
			}
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				FundAllocationModel faModel = new FundAllocationModel();
				faModel.setFinYr(General.checknull(rst.getString("financial_year")));
				faModel.setFaid(General.checknull(rst.getString("fund_allc_mid")));
				faModel.setResPrps(General.checknull(rst.getString("proposal")));
				faModel.setPiid(General.checknull(rst.getString("pi_id")));				
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("FundAllocationManager[getList]", e.toString()));
		} finally {
			try {
				if (rst != null)
					rst.close();
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sql) {
				sql.printStackTrace();
				l.fatal(Logging.logException("FundAllocationManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static String delete(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;		
		String query = "", chk="";
		int count = 0;
		try {
			conn = DBConnection.getConnection();
			
			query = "delete from rsrch_fund_allocation_detail where fund_allc_mid='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			
			if(count > 0){				
				psmt=null;query="";
				query = "delete from rsrch_fund_allocation_mast where fund_allc_mid='"+id+"'";
				psmt = conn.prepareStatement(query);
				count = psmt.executeUpdate();
				chk="1";
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("FundAllocationManager[delete]", e.toString()));
		} finally {
			try {
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sql) {
				sql.printStackTrace();
				l.fatal(Logging.logException("FundAllocationManager[delete]", sql.toString()));
			}
		}
		return chk;
	}
	public static ArrayList<FundAllocationModel> getEditList(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();			
			query = "select fund_allc_mid, financial_year, pi_id, fa_id, PS_MID, date_format(fund_allc_dt, '%d/%m/%Y') dt, remarks from rsrch_fund_allocation_mast where 1=1";
			if(!General.checknull(id).trim().equals("")){
				query+=" and fund_allc_mid = '"+General.checknull(id)+"' ";
			}
			//System.out.println("query-"+query);
			psmt = conn.prepareStatement(query);
			
			rst = psmt.executeQuery();
			while (rst.next()) {
				FundAllocationModel faModel = new FundAllocationModel();
				faModel.setFaid(General.checknull(rst.getString("fund_allc_mid")));
				faModel.setFinYr(General.checknull(rst.getString("financial_year")));				
				faModel.setfAgency(General.checknull(rst.getString("fa_id")));
				faModel.setResPrps(General.checknull(rst.getString("PS_MID")));
				faModel.setDate(General.checknull(rst.getString("dt")));
				faModel.setRemark(General.checknull(rst.getString("remarks")));
				faModel.setPiid(General.checknull(rst.getString("pi_id")));
				
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("FundAllocationManager[getEditList]", e.toString()));
		} finally {
			try {
				if (rst != null)
					rst.close();
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sql) {
				sql.printStackTrace();
				l.fatal(Logging.logException("FundAllocationManager[getEditList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static JSONObject getResearchProposal(String locationCode,String ddoCode,HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
        PreparedStatement pst = null;
        Connection conn=null;
        ResultSet rst = null;
        JSONObject objectJson=new JSONObject();
        String user_id=General.checknull(request.getSession().getAttribute("user_id").toString());
        String user_status=General.checknull(request.getSession().getAttribute("user_status").toString());
        
        String query="select distinct a.PS_MID id, PS_TITTLE_PROJ name from rsrch_form1_mast a, rsrch_research_prop_approval b "
        		+ "where a.PS_MID=b.PS_MID and a.LOCATION_CODE='"+locationCode+"' and a.DDO_ID='"+ddoCode+"' and a.is_form_submittd='Y' and b.RPA_STATUS='A' and b.RPA_STATUS = all "
        		+ "(SELECT RPA_STATUS FROM rsrch_research_prop_approval WHERE RPA_TYPE in ('HD', 'RR', 'RP', 'VC') and RPA_STATUS='A') " ;
        		if(!user_status.equals("A")){
        			query+=" and a.CREATED_BY='"+user_id+"' " ; 
        		 }
        		query += " union "
        		+ " select distinct a.PS_MID id, PS_TITTLE_PROJ name from rsrch_form1_mast a where IsApprovalReq='No' and a.is_form_submittd='Y'"
        		+ " and a.LOCATION_CODE='"+locationCode+"' and a.DDO_ID='"+ddoCode+"' ";
        		if(!user_status.equals("A")){
        			query+=" and a.CREATED_BY='"+user_id+"' " ; 
        		 }
	         try {
	        	 conn = DBConnection.getConnection();
	        	 pst = conn.prepareStatement(query);
	        	 //System.out.println("getResearchProposal || "+pst);
	        	 rst = pst.executeQuery();
	        	 while (rst.next()) {
	        		 JSONObject json = new JSONObject();
	        		 json.put("employeeId",  General.checknull(rst.getString("id").toString()));
	        		 json.put("employeeName", General.checknull(rst.getString("name").toString()));
	        		 jsonArray.add(json);
	              }
	              objectJson.put("employee", jsonArray);
	          } catch (Exception e) {
	           System.out.println("FileName=[FundAllocationManager] MethodName=[getEmployee()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[FundAllocationManager] MethodName=[getEmployee()] :", e.getMessage().toString()));
	          }finally {
	              try {
	                  if (rst != null) {
	                      rst.close();
	                      rst = null;
	                  }
	                  if (pst != null) {
	                      pst.close();
	                      pst = null;
	                  }
	                  if (conn != null) {
	                      conn.close();
	                      conn = null;
	                  }
	              } catch (final Exception e) {
	            	  l.fatal(Logging.logException("FileName=[FundAllocationManager],MethodName=[getEmployee()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }

	public static ArrayList<FundAllocationModel> getHeadList() {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();
			query = "select head_id id, head_name name from rsrch_research_head where is_active='Y'";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			while (rst.next()) {
				FundAllocationModel faModel = new FundAllocationModel();
				faModel.setHeadId(General.checknull(rst.getString("ID")));
				faModel.setHeadName(General.checknull(rst.getString("NAME")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("FundAllocationManager[getHeadList]", e.toString()));
		} finally {
			try {
				if (rst != null)
					rst.close();
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sql) {
				sql.printStackTrace();
				l.fatal(Logging.logException("FundAllocationManager[getHeadList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static ArrayList<FundAllocationModel> getListView(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		ArrayList<FundAllocationModel> arrayList = new ArrayList<FundAllocationModel>();
		String query = "";
		JSONObject finalObject=new JSONObject();
		try {
			conn = DBConnection.getConnection();
			query = "select fam.financial_year, fam.PS_MID, fm.PS_TITTLE_PROJ, fad.head_id, fad.amount amt, SUBSTR(fad.head_id, 1, 2) as chk "//rh.head_name,
					+ "from rsrch_fund_allocation_mast fam, rsrch_fund_allocation_detail fad, rsrch_form1_mast fm "// rsrch_research_head rh,
					+ "where fam.fund_allc_mid=fad.fund_allc_mid and fam.PS_MID=fm.PS_MID ";//and fad.head_id=rh.head_id			
			if(!General.checknull(id).trim().equals("")){
				query+=" and fam.fund_allc_mid = '"+General.checknull(id)+"' ";
			}
			query+=" order by fad.head_id";
			psmt = conn.prepareStatement(query);
			//System.out.println("psmt-"+psmt);
			rst = psmt.executeQuery();
			
			arrayList=FundAllocationManager.getHeadList(); 
			
			
			JSONObject obj = new JSONObject();
			finalObject.put("tableName", "type_mast");
			finalObject.put("coldesc1", "TYPE_NAME");
			finalObject.put("id", "TYPE_ID");
			obj = commonAPI.getDropDownByWebService("rest/snpApiService/masterdetails", finalObject);
			JSONArray item = (JSONArray) obj.get("commondata");
			
			while (rst.next()) {
				FundAllocationModel faModel = new FundAllocationModel();
				String name="";
				faModel.setFinYr(General.checknull(rst.getString("financial_year")));
				faModel.setResPrps(General.checknull(rst.getString("PS_TITTLE_PROJ")));
				
				if(General.checknull(rst.getString("chk")).equals("RH")){
					for(int k=0; k<arrayList.size(); k++){
						String hid = arrayList.get(k).getHeadId();
						String hname = arrayList.get(k).getHeadName();	        				 
						if(General.checknull(hid).equals(rst.getString("head_id"))){
							name=hname;
							break;
						}else{
							name="";
						}						
					}
				}else{
					for (int n = 0; n < item.size(); n++) {
						JSONObject jsn = (JSONObject) item.get(n);
						if (General.checknull(jsn.get("id").toString()).equals(General.checknull(rst.getString("head_id")))) {
							name=General.checknull(jsn.get("desc").toString());
						}
					}
				}
				
				faModel.setHeadName(General.checknull(name));				
				faModel.setAmt(General.checknull(rst.getString("amt")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("FundAllocationManager[getListView]", e.toString()));
		} finally {
			try {
				if (rst != null)
					rst.close();
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sql) {
				sql.printStackTrace();
				l.fatal(Logging.logException("FundAllocationManager[getListView]", sql.toString()));
			}
		}
		return al;
	}

	public static ArrayList<FundAllocationModel> getEditListDetail(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();			
			query = "select fam.PS_MID, fm.PS_TITTLE_PROJ, fam.fa_id, fa.fa_Name, date_format(fam.fund_allc_dt, '%d/%m/%Y') dt, "
					+ "fad.head_id, rh.head_name, fad.amount amt from rsrch_fund_allocation_mast fam, rsrch_fund_allocation_detail fad, "
					+ "rsrch_research_head rh, rsrch_form1_mast fm, rsrch_funding_agency fa where fam.fund_allc_mid=fad.fund_allc_mid and "
					+ "fad.head_id=rh.head_id and fam.PS_MID=fm.PS_MID and fam.fa_id=fa.fa_id ";
			if(!General.checknull(id).trim().equals("")){
				query+=" and fam.fund_allc_mid = '"+General.checknull(id)+"' ";
			}
			query+=" order by fad.head_id";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				FundAllocationModel faModel = new FundAllocationModel();		
				faModel.setfAgency(General.checknull(rst.getString("fa_Name")));
				faModel.setResPrps(General.checknull(rst.getString("PS_TITTLE_PROJ")));
				faModel.setDate(General.checknull(rst.getString("dt")));
				faModel.setHeadId(General.checknull(rst.getString("head_id")));
				faModel.setHeadName(General.checknull(rst.getString("head_name")));
				faModel.setAmt(General.checknull(rst.getString("amt")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("FundAllocationManager[getEditList]", e.toString()));
		} finally {
			try {
				if (rst != null)
					rst.close();
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sql) {
				sql.printStackTrace();
				l.fatal(Logging.logException("FundAllocationManager[getEditList]", sql.toString()));
			}
		}
		return al;
	}

	public static JSONObject getHeadNameList(String id) {
        PreparedStatement pst = null;
        Connection conn=null;
        ResultSet rst = null,rst1=null;
        JSONObject objectJson=new JSONObject();        
        JSONObject finalObject=new JSONObject();
        JSONObject jsonobj=new JSONObject();
        JSONArray jsonobjNew = new JSONArray();
        JSONArray jsonArray = new JSONArray();
        String qry="",query="",EXP_HEAD_MID="";
        ArrayList<FundAllocationModel> arrayList = new ArrayList<FundAllocationModel>();
	         try {
	        	 conn = DBConnection.getConnection();
	        	 
	        	 query = "select EXP_HEAD_MID as id from rsrch_manage_exp_mast  where  PUR_IND='"+General.checknull(id)+"' ";
	        	 pst=conn.prepareStatement(query);
	 			rst = pst.executeQuery();	
	 			while (rst.next()) {
	 				EXP_HEAD_MID=(General.checknull(rst.getString("id")));
	 			}
	 			//System.out.println("EXP_HEAD_MID"+EXP_HEAD_MID);
	 			pst=null;
	        	 qry=" select head_id,(SELECT head_name FROM rsrch_research_head WHERE head_id=d.head_id) name,amount from rsrch_fund_allocation_detail d,rsrch_fund_allocation_mast m where m.fund_allc_mid=d.fund_allc_mid and m.pi_id='"+id+"'";
		        	pst=conn.prepareStatement(qry);
		        	rst1 = pst.executeQuery();
		        	while(rst1.next()){
		        		 JSONObject json = new JSONObject();
		        	json.put("id", General.checknull(rst1.getString("head_id")));
					json.put("amount", General.checknull(rst1.getString("amount"))); 
					json.put("name", General.checknull(rst1.getString("name")));
					 json.put("EXP_HEAD_MID", General.checknull(EXP_HEAD_MID));
					jsonArray.add(json);
		        	}
		        	
		        	if(jsonArray.size()==0){
	        	 finalObject.put("id", id);
	        	 jsonobj= commonAPI.getDropDownByWebService("rest/snpApiService/getResearchHeadList", finalObject);
	        	 jsonobjNew=(JSONArray) jsonobj.get("piIdlist");
	        	 
	        	 if(!jsonobj.isEmpty()){
	        		 arrayList=FundAllocationManager.getHeadList(); 	
	        	 }
	        	 
	        	 for(int i=0; i<jsonobjNew.size(); i++){
	        		 JSONObject jsonobjNew1 = new JSONObject();
	        		 JSONObject json = new JSONObject();
	        		 jsonobjNew1=(JSONObject) jsonobjNew.get(i);
	        		 
	        		 String val=jsonobjNew1.get("name").toString();
	        		 String name="";
	        		 if(General.checknull(val).equals("RH")){
	        			 for(int k=0; k<arrayList.size(); k++){
	        				 String hid = arrayList.get(k).getHeadId();
	        				 String hname = arrayList.get(k).getHeadName();	        				 
	        				 if(General.checknull(hid).equals(jsonobjNew1.get("id").toString())){
	        					 name=hname;
	        					 break;
	        				 }else{
	        					name="";
	        				 }
	        				 
	        			 }
	        		 }else{
	        			 name=jsonobjNew1.get("name").toString();
	        		 }
	        		 
	        		 json.put("id", General.checknull(jsonobjNew1.get("id").toString()));
					 json.put("name", General.checknull(name));
					 json.put("amount", General.checknull(jsonobjNew1.get("amount").toString()));
					 json.put("EXP_HEAD_MID", General.checknull(EXP_HEAD_MID));
					 
					 jsonArray.add(json);
	        	 }
		        	}
	        	 objectJson.put("piIdlist", jsonArray);
	        	 
	          } catch (Exception e) {
	           System.out.println("FileName=[FundAllocationManager] MethodName=[getHeadNameList()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[FundAllocationManager] MethodName=[getHeadNameList()] :", e.getMessage().toString()));
	          }finally {
	              try {
	                  if (rst != null) {
	                      rst.close();
	                      rst = null;
	                  }
	                  if (pst != null) {
	                      pst.close();
	                      pst = null;
	                  }
	                  if (conn != null) {
	                      conn.close();
	                      conn = null;
	                  }
	              } catch (final Exception e) {
	            	  l.fatal(Logging.logException("FileName=[FundAllocationManager],MethodName=[getHeadName()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }
	
	 
	
	public static JSONObject getInsallMentAmt(String id) {
        PreparedStatement pst = null;
        Connection conn=null;
        ResultSet rst = null,rst1=null;
        JSONObject objectJson=new JSONObject();        
          
          String qry="";
 	         try {
	        	 conn = DBConnection.getConnection();
	         
	        	 qry=" select distinct alloted_amount ,(select if(count(*)>0,'Y','N') as flg from rsrch_fund_allocation_mast where install_id= faa_id) as flg from rsrch_funding_agency_approval where faa_id='"+id+"' ";
		        	pst=conn.prepareStatement(qry);
		        	rst1 = pst.executeQuery();
		        	//System.out.println("getInsallMentAmt pst --"+pst);
		        	while(rst1.next()){
		        		objectJson.put("amt", General.checknull(rst1.getString("alloted_amount")));
		        		objectJson.put("flg", General.checknull(rst1.getString("flg")));
		        		 
		        	}  
	        	 
	          } catch (Exception e) {
	           System.out.println("FileName=[FundAllocationManager] MethodName=[getInsallMentAmt()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[FundAllocationManager] MethodName=[getInsallMentAmt()] :", e.getMessage().toString()));
	          }finally {
	              try {
	                  if (rst != null) {
	                      rst.close();
	                      rst = null;
	                  }
	                  if (pst != null) {
	                      pst.close();
	                      pst = null;
	                  }
	                  if (conn != null) {
	                      conn.close();
	                      conn = null;
	                  }
	              } catch (final Exception e) {
	            	  l.fatal(Logging.logException("FileName=[FundAllocationManager],MethodName=[getSubHeadNameList()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }
	
	
	public static JSONObject getListDetails(String resp,String instid) { //HERE CODE CHANGE
        PreparedStatement pst = null;
        Connection conn=null;
        ResultSet rst = null,rst1=null;
        JSONObject objectJson=new JSONObject();        
          JSONArray jsonArray = new JSONArray();
          String qry="";
 	         try {
	        	 conn = DBConnection.getConnection();
 	        	 qry="select a.fund_allc_did,a.head_id,a.subhead_id,a.amount ,"
 	        	 		+ " (select if(count(*)>0,'Y','N') from rsrch_manage_exp_mast c,rsrch_manage_exp_detail d where "
 	        	 		+ " c.EXP_HEAD_MID=d.EXP_HEAD_MID and c.RSRCH_PROP=b.PS_MID "
 	        	 		+ " and if(d.EXP_SUB_HEAD_ID!=null ,d.EXP_SUB_HEAD_ID=a.subhead_id and  d.HEAD_ID=a.head_id, d.HEAD_ID=a.head_id )) as flag"
 	        			+ " from rsrch_fund_allocation_detail a , "
 	        	 		+ " rsrch_fund_allocation_mast b where a.fund_allc_mid=b.fund_allc_mid and PS_MID='"+resp+"' "
 	        	 		+ " and install_id='"+instid+"'";
		        	pst=conn.prepareStatement(qry);
		        	//System.out.println("getListDetails pst -   "+pst);
		        	rst1 = pst.executeQuery();
		        	while(rst1.next()){
		        	JSONObject json = new JSONObject();
		        	json.put("fund_allc_did", General.checknull(rst1.getString("fund_allc_did")));
					json.put("head_id", General.checknull(rst1.getString("head_id"))); 
					json.put("subhead_id", General.checknull(rst1.getString("subhead_id"))); 
					json.put("amount", General.checknull(rst1.getString("amount"))); 
					json.put("flag", General.checknull(rst1.getString("flag")));  
					jsonArray.add(json);
		        	}  
	        	 objectJson.put("fundinglist", jsonArray);
	        	 
	          } catch (Exception e) {
	           System.out.println("FileName=[FundAllocationManager] MethodName=[getSubHeadNameList()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[FundAllocationManager] MethodName=[getSubHeadNameList()] :", e.getMessage().toString()));
	          }finally {
	              try {
	                  if (rst != null) {
	                      rst.close();
	                      rst = null;
	                  }
	                  if (pst != null) {
	                      pst.close();
	                      pst = null;
	                  }
	                  if (conn != null) {
	                      conn.close();
	                      conn = null;
	                  }
	              } catch (final Exception e) {
	            	  l.fatal(Logging.logException("FileName=[FundAllocationManager],MethodName=[getSubHeadNameList()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }	
	public static String deleteDetailRecord(String id) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String cInsert = "", st = "";
		int updtCnt = 0;
		String str = "";
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			cInsert = "delete from rsrch_fund_allocation_detail ";
			cInsert += "WHERE fund_allc_did=?";  
			pstmt = conn.prepareStatement(cInsert);
			pstmt.setString(1, id.trim());
			updtCnt = pstmt.executeUpdate();
			// System.out.println("delete :"+pstmt);
			if (updtCnt > 0) {
				conn.commit();
				st = ApplicationConstants.DELETED;
			} else {
				conn.rollback();
				st = ApplicationConstants.FAIL;
			}
		} catch (Exception e) {
			str = e.getMessage().toString();
			if (str.indexOf("foreign key constraint fails") != -1)
				str = ApplicationConstants.DELETE_FORIEGN_KEY;
			else
				str = ApplicationConstants.EXCEPTION_MESSAGE; 
			l.fatal(Logging.logException("FundAllocationManager[deleteDetailRecord]", e.getMessage().toString()));
			System.out.println("Error in FundAllocationManager[deleteDetailRecord] : " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rst != null)
					rst.close();
				conn.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return st;
	}	
}