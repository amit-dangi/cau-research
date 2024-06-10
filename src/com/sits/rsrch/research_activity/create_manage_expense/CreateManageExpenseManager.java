package com.sits.rsrch.research_activity.create_manage_expense;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sits.commonApi.commonAPI;
import com.sits.conn.DBConnection;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.general.ReadProps;

import org.apache.commons.fileupload.FileItem;

public class CreateManageExpenseManager {
	static Logger l = Logger.getLogger("exceptionlog");

	public static JSONObject getResearchProposal(String locationCode,String ddoCode,HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
		PreparedStatement pst = null;
		Connection conn = null;
		ResultSet rst = null;
		JSONObject objectJson = new JSONObject();
		JSONObject finalObject = new JSONObject();
		ArrayList<String> rid = new ArrayList<String>();
		try {
	        String user_id=General.checknull(request.getSession().getAttribute("user_id").toString());
	        String user_status=General.checknull(request.getSession().getAttribute("user_status").toString());
			JSONObject obj = commonAPI.getDropDownByWebService("rest/snpApiService/getResearchProposalId", finalObject);
			finalObject.clear();
			JSONArray item = (JSONArray) obj.get("rpId");

			for (int n = 0; n < item.size(); n++) {
				JSONObject jsn = (JSONObject) item.get(n);
				rid.add(General.checknull(jsn.get("id").toString()));
			}
			
			if (rid.size() > 0) {
				String query = "select distinct PS_MID id, PS_TITTLE_PROJ name from rsrch_form1_mast where LOCATION_CODE='"+locationCode+"' and DDO_ID='"+ddoCode+"' ";
						if(!user_status.equals("A")){
		        			query+=" and CREATED_BY='"+user_id+"' " ; 
		        		 }
				query+= "and PS_MID in (";
				for (int ii = 0; ii < rid.size(); ii++) {
					query += "'"+rid.get(ii)+"'";
					if (ii != rid.size() - 1) {
						query += ",";
					}
				}
				query+= ") group by PS_MID";
				conn = DBConnection.getConnection();
	            pst = conn.prepareStatement(query);
	            
	            rst = pst.executeQuery();
	            while (rst.next()) {
	            	JSONObject json = new JSONObject();
	            	json.put("RId",  General.checknull(rst.getString("id").toString()));
	            	json.put("RName", General.checknull(rst.getString("name").toString()));
	            	jsonArray.add(json);
	            }	            
			}
			objectJson.put("rpData", jsonArray);

		} catch (Exception e) {
			System.out.println("FileName=[CreateManageExpenseManager] MethodName=[getEmployee()] :" + e.getMessage().toString());
			l.fatal(Logging.logException("FileName=[CreateManageExpenseManager] MethodName=[getEmployee()] :",e.getMessage().toString()));
		} finally {
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
				l.fatal(Logging.logException("FileName=[CreateManageExpenseManager],MethodName=[getEmployee()]",e.getMessage().toString()));
			}
		}
		return objectJson;
	}

	public static ArrayList<CreateManageExpenseModel> getListView(String resPrps,String finYrId) {		
		String query="";
		ArrayList al=new ArrayList();
		PreparedStatement psmt=null;
		ResultSet rst=null;
		Connection conn=null;
		try {
			conn = DBConnection.getConnection();
			query="select fad.subhead_id sid,fam.financial_year, h.head_id ,date_format(fam.created_date, '%d/%m/%Y')as DATE , sum(amount) as APP_AMT , "
					+ "coalesce((select sub_head_name from rsrch_research_sub_head sh where sh.sub_head_id=fad.subhead_id),'NA') name,h.head_name from"
					+ "   rsrch_fund_allocation_detail fad,rsrch_fund_allocation_mast fam, rsrch_research_head h where  fad.head_id=h.head_id and fam.fund_allc_mid=fad.fund_allc_mid   "
					+ "  and fam.PS_MID='"+resPrps+"' and fam.financial_year='"+finYrId+"' group by head_id,sid order by head_name,name";
			psmt = conn.prepareStatement(query);
			System.out.println("psmt getListView||"+psmt);
			rst = psmt.executeQuery();
			while (rst.next()) {
				CreateManageExpenseModel faModel = new CreateManageExpenseModel();
				faModel.setHeadId(General.checknull(rst.getString("head_id")));
				faModel.setHeadName(General.checknull(rst.getString("head_name")));
				faModel.setSubHeadId(General.checknull(rst.getString("sid")));
				faModel.setSubHeadName(General.checknull(rst.getString("name")));
				faModel.setHeadAmt(General.checknull(rst.getString("APP_AMT")));
				faModel.setAppDate(General.checknull(rst.getString("DATE")));
				faModel.setFinYrId(General.checknull(rst.getString("financial_year")));
				al.add(faModel);
				
			}
							
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("CreateManageExpenseManager[getListView]", e.toString())); 
		}
		return al;
	}
	
	public static ArrayList<CreateManageExpenseModel> getSubHeadList(String piId) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		JSONObject finalObject=new JSONObject();
		finalObject.put("idvalue",piId);
		try {
			conn = DBConnection.getConnection();
			query = "select a.sub_head_id sid, a.head_id id, group_concat(if(a.sub_head_name is null, concat(if(location_typ ='O', 'Other Traveler', "
					+ "'Local Traveler'), if(location_name is null, ' ', concat(' (',location_name,')')),'#',sub_head_id), "
					+ "concat(sub_head_name,'#',sub_head_id))) name "
					+ "from rsrch_research_sub_head a,  rsrch_fund_allocation_detail b where is_active='Y' and b.subhead_id=a.sub_head_id "
					+ " group by a.head_id";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			while (rst.next()) {
				CreateManageExpenseModel faModel = new CreateManageExpenseModel();
				faModel.setHeadId(General.checknull(rst.getString("ID")));
				faModel.setHeadName(General.checknull(rst.getString("NAME")));
				al.add(faModel);
			}
			//System.out.println("sub head list is>>"+al.toString());
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("CreateManageExpenseManager[getSubHeadList]", e.toString()));
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
				l.fatal(Logging.logException("CreateManageExpenseManager[getSubHeadList]", sql.toString()));
			}
		}
		return al;
	}

	public static CreateManageExpenseModel save(CreateManageExpenseModel cModel, String login_id, String machine, List<FileItem> fileNameList) {
		Connection conn = null;
		PreparedStatement psmt=null, psmt1=null,psmtdel=null,delqryDtl=null;
		ResultSet rst = null;	
		String qry = "", qry1 = "", id="", finyr="", rsrchProp="", purInd="", date="",locationCode="",ddoCode="";
		int fileCnt=0,delcnt=0;
		 
		finyr=General.checknull(cModel.getFinYrId());
		rsrchProp=General.checknull(cModel.getResPrps());
		purInd=General.checknull(cModel.getPiId());
		date=General.checknull(cModel.getExpDate());
		locationCode=General.checknull(cModel.getLocationCode());
		ddoCode=General.checknull(cModel.getDdoCode());
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			qry = "SELECT concat('ME', LPAD(CONVERT(IFNULL(MAX(SUBSTR(EXP_HEAD_MID,3,8)),0)+1,SIGNED INTEGER),8,'0')) FROM RSRCH_MANAGE_EXP_MAST";
			psmt = conn.prepareStatement(qry);
			rst = psmt.executeQuery();
			if (rst.next()) {
				id = rst.getString(1);
			}
			qry=""; psmt=null;
			
			/*Delete functionality added as per client changes 
			as per the savemode it will work for DELETE button it will delete only data 
			and for Save and update mode it will delete the data first then in insert in batch with transactional*/
			
			String delqry = "delete from rsrch_manage_exp_detail where EXP_HEAD_MID in (select EXP_HEAD_MID from "
					+ "rsrch_manage_exp_mast where RSRCH_PROP='"+rsrchProp+"' and exp_date=str_to_date('"+date+"','%d/%m/%Y'));";
			psmtdel= conn.prepareStatement(delqry);
			psmtdel.executeUpdate();
			
			String delqry2 = "delete from rsrch_manage_exp_mast where RSRCH_PROP='"+rsrchProp+"' and exp_date=str_to_date('"+date+"','%d/%m/%Y');";
			delqryDtl= conn.prepareStatement(delqry2);
			delcnt=delqryDtl.executeUpdate();
			//System.out.println("Save Mode ||"+cModel.getSavemode());
			if(General.checknull(cModel.getSavemode()).equals("DELETE")){
				if(delcnt > 0){
				conn.commit();
				cModel.setErrMsg(date+" Expense Details Delete Successfully");			
				cModel.setValid(false);
				}else{
					conn.rollback();
					cModel.setErrMsg(ApplicationConstants.FAIL);
					cModel.setValid(false);
				}
			}else{
				qry = "insert into RSRCH_MANAGE_EXP_MAST(EXP_HEAD_MID,LOCATION_CODE,DDO_ID, FIN_YEAR, RSRCH_PROP, PUR_IND, "
						//+ "HEAD_ID, BLNC_AMT, "
						+ "APP_DATE, EXP_DATE,install_id, CREATED_BY, CREATED_DATE, CREATED_MACHINE) values (?,?,?,?,?,?,"
						//+ "?,?,"
						+ "str_to_date(?,'%d/%m/%Y'),str_to_date(?,'%d/%m/%Y'),?,?,now(),?)";
				psmt = conn.prepareStatement(qry);
				
				psmt.setString(1, id.trim());
				
				psmt.setString(2, General.checknull(locationCode).trim());
				psmt.setString(3, General.checknull(ddoCode).trim());
				psmt.setString(4, General.checknull(finyr).trim());
				psmt.setString(5, General.checknull(rsrchProp).trim());
				psmt.setString(6, General.checknull(purInd).trim());
				psmt.setString(7, General.checknull(cModel.getAppDate()).trim());
				psmt.setString(8, General.checknull(date).trim());
				psmt.setString(9, ""/*General.checknull(cModel.getInstall_Id())*/);
				psmt.setString(10, General.checknull(login_id).trim());
				
				psmt.setString(11, General.checknull(machine).trim());				
				int cnt = psmt.executeUpdate();
				//qry=""; psmt=null;
				
				if(cnt > 0){
					qry1 = "insert into RSRCH_MANAGE_EXP_DETAIL(EXP_HEAD_MID, EXP_SUB_HEAD_ID, SUB_HEAD_AMT, purpose,order_no,"
							+ "CREATED_BY, CREATED_DATE, CREATED_MACHINE, HEAD_ID, BLNC_AMT) values (?,?,coalesce(?,'0'),?,?,?,now(),?,?,?)";
					psmt1 = conn.prepareStatement(qry1, psmt1.RETURN_GENERATED_KEYS);
					for(CreateManageExpenseModel sdata:cModel.getSubHeadArray()){
						psmt1.setString(1, id.trim());
						psmt1.setString(2, General.checknull(sdata.getSubHeadId()).equals("") ? null:General.checknull(sdata.getSubHeadId()));
						psmt1.setString(3, sdata.getSubHeadAmt());
						psmt1.setString(4, sdata.getPurpose());
						psmt1.setString(5, sdata.getOrder_no());
						psmt1.setString(6, General.checknull(login_id).trim());
						psmt1.setString(7, General.checknull(machine).trim());			
						psmt1.setString(8, General.checknull(sdata.getHeadId()).trim());
						psmt1.setString(9, General.checknull(sdata.getHeadAmt()).trim());
						int cnt1 = psmt1.executeUpdate();
						ResultSet tableKeys = psmt1.getGeneratedKeys();
						tableKeys.next();
						
						int autoGeneratedID = tableKeys.getInt(1);						
						fileCnt++;			
						
						if(cnt1 > 0 ){
							for(int k=0; k<fileNameList.size(); k++){
								FileItem fileItem = fileNameList.get(k);
								String tst= fileNameList.get(k).getFieldName().replace("undefined", "");								
								if(tst.equals("uploadDoc_"+sdata.getHeadId()+"_"+General.checknull(sdata.getSubHeadId()))){								
									saveDoc(machine, "RSRCH_MANAGE_EXP_DETAIL", String.valueOf(autoGeneratedID), General.checknull(sdata.getHeadId()), login_id, conn, fileItem, "", "RMED", rsrchProp, purInd);								
								}
							}
								conn.commit();
								cModel.setErrMsg("Create & Manage Expense Details Saved Successfully");			
								cModel.setValid(true);
						}else{
							conn.rollback();
							cModel.setErrMsg(ApplicationConstants.FAIL);
							cModel.setValid(false);
						}
					}	
				}else{
					conn.rollback();
					cModel.setErrMsg(ApplicationConstants.FAIL);
					cModel.setValid(false);
				}
		}		
		} catch (Exception e) {
			String str=e.getMessage().toString() ;
			if(str.indexOf("Duplicate entry") != -1)
				str = ApplicationConstants.DUPLICATE;
			else 
				str = ApplicationConstants.EXCEPTION_MESSAGE;
			
				cModel.setErrMsg(str);
				cModel.setValid(false);
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.out.println("Error in CreateManageExpenseManager.java(save) : " + e.getMessage());
		} finally {
			try {
				if (psmt != null){
					psmt.clearBatch();
					psmt.close();
				}
				
				if (conn != null) conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return cModel;
	}
	
	public static boolean saveDoc(String machine,String tablename,String mastid,String cand_id,String userid,
			Connection conn, FileItem fileItem,String attid,String desc, String rsrchProp, String purInd){
	  	java.io.File file;
	  	try{
	  		
	  		//System.out.println("machine :"+machine+"  | tablename :"+tablename+"  | mastid :"+mastid+"  | cand_id :"+cand_id+"  | userid :"+userid+"  | fileItem :"+fileItem+"  | attid :"+attid+"  | desc :"+desc);	  		
	  		String attachid=attid;
	  		if(attachid.equals(""))
	  			attachid=filedetailsave(machine, fileItem.getName(), mastid, userid, fileItem.getSize(), tablename, conn,desc);
	  		else
	  			filedetailupdate(machine, fileItem.getName(), mastid, userid, fileItem.getSize(), tablename, attid, conn,desc);
			
	  		//String directoryName=ReadProps.getkeyValue("document.path", "sitsResource")+"/"+cand_id+"/";
	  		String directoryName=ReadProps.getkeyValue("document.path", "sitsResource")+"/"+"RSRCH/EXPENSE/"+rsrchProp+"/"+purInd+"/"+cand_id+"/";
	  		//System.out.println("directoryName1   :"+directoryName);
	  		
	  		File directory = new File(directoryName);
    		if (!directory.isDirectory()){
     			directory.mkdirs();
     		}
    		//RESEARCH/EXPENSE/PROPOSAL_ID/PI_ID/fileAttachmentId_fileName.jpg
    		file = new File(directoryName +attachid+"_"+fileItem.getName());
    		fileItem.write(file);
	  	}catch(Exception e){
	  		System.out.println("Error in CreateManageExpenseManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("CreateManageExpenseManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}	
	
	public static String filedetailsave(String machine, String name,String mastid,String userid, long size,String tablename,Connection conn,String desc){ 
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";
		String attachid="";
		int count=0;
		try{
			String Qry = "SELECT IFNULL(MAX(file_attachment_id)+1,1) FROM file_attachment"; 
			psmt = conn.prepareStatement(Qry);
			rst= psmt.executeQuery();
			//System.out.println(psmt);
			if(rst.next())
				attachid=General.checknull(rst.getString(1));
			
			qry="INSERT INTO file_attachment (file_attachment_id,file_name,file_type,table_name,"
					+ "reference_id,CREATED_BY,CREATED,MACHINE) VALUES (?, ?,?,?, ?,?,now(),?)";
			psmt = conn.prepareStatement(qry);
			psmt.setString(1, attachid);
			psmt.setString(2, name);
			psmt.setString(3, desc);
			psmt.setString(4, tablename);
			psmt.setString(5, mastid);
			psmt.setString(6, userid);
			psmt.setString(7, machine);
			//System.out.println("filedetailsave||"+psmt);
			count= psmt.executeUpdate();
			//System.out.println(psmt);
			if(count==0)
				attachid="";
		}
		catch(Exception e)
		{
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: CreateManageExpenseManager" + "[filedetailsave]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("CreateManageExpenseManager [filedetailsave]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
	public static void filedetailupdate(String machine, String name,String mastid,String userid, long size,String tablename,String attid,Connection conn,String desc){ 
		PreparedStatement psmt = null;
		String qry = "";
		int count=0;
		try{
			//System.out.println("reference_id||"+mastid);
			//System.out.println("attid||"+attid);
			qry="UPDATE file_attachment SET file_name=?, filesize=? WHERE file_attachment_id =? and table_name= ? and reference_id=? ";
			psmt = conn.prepareStatement(qry);
			psmt.setString(1, name);
			psmt.setString(2, size+"");
			psmt.setString(3, attid);
			psmt.setString(4, tablename);
			psmt.setString(5, mastid);
			//System.out.println("filedetailupdate|| "+psmt);
			count= psmt.executeUpdate();
			//System.out.println(psmt);
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION IS CAUSED BY: CreateManageExpenseManager" + "[filedetailupdate]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("CreateManageExpenseManager [filedetailupdate]", e.toString()));
		}
	  	
	}
	
	public static ArrayList<CreateManageExpenseModel> getSaveData(String resPrps, String finYrId, String dt){		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";		
		try {
			conn = DBConnection.getConnection();
			/*About Query
			 *Through amountdetails table we are getting the sum of balance amount for given propsal & fin year and through filedetails getting the 
			file,purpose,orderno and saved expense amount for the selected date*/
			
			/*query="select (select if(count(*)>0,'Y','N') as flg from RSRCH_MANAGE_EXP_mast em where em.EXP_DATE=str_to_date('"+General.checknull(dt)+"', '%d/%m/%Y') and em.FIN_YEAR='"+General.checknull(finYrId)+"' and em.RSRCH_PROP='"+General.checknull(resPrps)+"' ) as flg,"
					+ "filedetails.purpose,filedetails.order_no,amountdetails.EXP_HEAD_MID,head_name,coalesce(sub_head_name,'NA') AS sub_head_name,amountdetails.HEAD_ID,amountdetails.EXP_SUB_HEAD_ID,BLNC_AMT,APP_DATE,blnc,EXP_HEAD_DID,SUB_HEAD_AMT,coalesce(fname,'')as fname "
					+ "from (select med.purpose,med.order_no,mem.EXP_HEAD_MID,(SELECT sub_head_name FROM rsrch_research_sub_head WHERE sub_head_id=med.EXP_SUB_HEAD_ID) as sub_head_name,(SELECT head_name FROM rsrch_research_head WHERE head_id=med.HEAD_ID) as "
					+ "head_name,med.HEAD_ID, med.BLNC_AMT, date_format(mem.APP_DATE, '%d/%m/%Y') APP_DATE, med.BLNC_AMT-sum(med.SUB_HEAD_AMT) as blnc, (med.EXP_HEAD_DID) EXP_HEAD_DID, coalesce(med.EXP_SUB_HEAD_ID,'') EXP_SUB_HEAD_ID "
					+ "from RSRCH_MANAGE_EXP_mast mem, RSRCH_MANAGE_EXP_DETAIL med where mem.EXP_HEAD_MID=med.EXP_HEAD_MID and mem.FIN_YEAR='"+General.checknull(finYrId)+"' and mem.RSRCH_PROP='"+General.checknull(resPrps)+"'  group by  HEAD_ID,EXP_SUB_HEAD_ID order by head_name,sub_head_name) as "
					+ "amountdetails left join (select med.purpose,med.order_no,mem.EXP_HEAD_MID, HEAD_ID,coalesce(EXP_SUB_HEAD_ID,'') as EXP_SUB_HEAD_ID,(med.SUB_HEAD_AMT) SUB_HEAD_AMT,(select concat(concat(fa.reference_id,'~',fa.file_name,'~',file_attachment_id)) from file_attachment fa where"
					+ " table_name='RSRCH_MANAGE_EXP_DETAIL' and file_type='RMED' and find_in_set(fa.reference_id,(med.EXP_HEAD_DID))) as fname from RSRCH_MANAGE_EXP_mast mem, RSRCH_MANAGE_EXP_DETAIL med where mem.EXP_HEAD_MID=med.EXP_HEAD_MID and "
					+ "mem.FIN_YEAR='"+General.checknull(finYrId)+"' and mem.RSRCH_PROP='"+General.checknull(resPrps)+"' and "
					+ "mem.EXP_DATE=str_to_date('"+General.checknull(dt)+"', '%d/%m/%Y')) as filedetails"
					+ " on  amountdetails.HEAD_ID=filedetails.HEAD_ID and amountdetails.EXP_SUB_HEAD_ID=filedetails.EXP_SUB_HEAD_ID";*/
			
			
			
				query="select (select if(count(*)>0,'Y','N') as flg from RSRCH_MANAGE_EXP_mast em where em.EXP_DATE=str_to_date('"+General.checknull(dt)+"', '%d/%m/%Y') and em.FIN_YEAR='"+General.checknull(finYrId)+"' and em.RSRCH_PROP='"+General.checknull(resPrps)+"' ) as flg,filedetails.purpose,filedetails.order_no,amountdetails.EXP_HEAD_MID,head_name,"
						+ "coalesce(sub_head_name,'NA') AS sub_head_name,amountdetails.HEAD_ID,amountdetails.EXP_SUB_HEAD_ID,fundallocsum as BLNC_AMT,APP_DATE,(fundallocsum-headexpnse) as blnc,EXP_HEAD_DID,SUB_HEAD_AMT,coalesce(fname,'')as fname from ("
						+ " select med.purpose,med.order_no,mem.EXP_HEAD_MID,(SELECT sub_head_name FROM rsrch_research_sub_head WHERE sub_head_id=med.EXP_SUB_HEAD_ID) as sub_head_name,(SELECT head_name FROM rsrch_research_head WHERE head_id=med.HEAD_ID) as head_name,"
						+ "med.HEAD_ID, date_format(mem.APP_DATE, '%d/%m/%Y') APP_DATE,"
					+ " (select coalesce(sum(amount),0) as APP_AMT from rsrch_fund_allocation_detail fad,rsrch_fund_allocation_mast fam, rsrch_research_head h where fad.head_id=h.head_id and fam.fund_allc_mid=fad.fund_allc_mid and fam.PS_MID='"+General.checknull(resPrps)+"' and fam.financial_year='"+General.checknull(finYrId)+"'"
					+ " and fad.head_id=med.HEAD_ID "
					+ " and coalesce(fad.subhead_id,'')=coalesce(med.EXP_SUB_HEAD_ID,'')) as fundallocsum"
					+ " , sum(med.SUB_HEAD_AMT) as headexpnse, (med.EXP_HEAD_DID) EXP_HEAD_DID, coalesce(med.EXP_SUB_HEAD_ID,'') EXP_SUB_HEAD_ID from RSRCH_MANAGE_EXP_mast mem, RSRCH_MANAGE_EXP_DETAIL med where mem.EXP_HEAD_MID=med.EXP_HEAD_MID and mem.FIN_YEAR='"+General.checknull(finYrId)+"'"
					+ " and mem.RSRCH_PROP='"+General.checknull(resPrps)+"'  group by  HEAD_ID,EXP_SUB_HEAD_ID order by head_name,sub_head_name"
					+ " ) as amountdetails left join ("
					+ " select med.purpose,med.order_no,mem.EXP_HEAD_MID, HEAD_ID,coalesce(EXP_SUB_HEAD_ID,'') as EXP_SUB_HEAD_ID,(med.SUB_HEAD_AMT) SUB_HEAD_AMT,(select concat(concat(fa.reference_id,'~',fa.file_name,'~',file_attachment_id)) from file_attachment fa"
					+ " where table_name='RSRCH_MANAGE_EXP_DETAIL' and file_type='RMED' and find_in_set(fa.reference_id,(med.EXP_HEAD_DID)) limit 1) as fname from RSRCH_MANAGE_EXP_mast mem, RSRCH_MANAGE_EXP_DETAIL med where mem.EXP_HEAD_MID=med.EXP_HEAD_MID and mem.FIN_YEAR='"+General.checknull(finYrId)+"'"
					+ " and mem.RSRCH_PROP='"+General.checknull(resPrps)+"' and mem.EXP_DATE=str_to_date('"+General.checknull(dt)+"', '%d/%m/%Y')"
					+ " ) as filedetails on  amountdetails.HEAD_ID=filedetails.HEAD_ID and amountdetails.EXP_SUB_HEAD_ID=filedetails.EXP_SUB_HEAD_ID";

			psmt = conn.prepareStatement(query);
			//System.out.println("getSaveData>>>>"+psmt);
			rst = psmt.executeQuery();				
			while (rst.next()) {
				CreateManageExpenseModel faModel = new CreateManageExpenseModel();				
				faModel.setMid(General.checknull(rst.getString("EXP_HEAD_MID")));
				faModel.setBlncAmt(General.checknull(rst.getString("BLNC_AMT")));
				faModel.setRemainingAmt(General.checknull(rst.getString("blnc")));
				faModel.setDid(General.checknull(rst.getString("EXP_HEAD_DID")));
				faModel.setHeadId(General.checknull(rst.getString("HEAD_ID")));
				faModel.setHeadName(General.checknull(rst.getString("head_name")));
				faModel.setSubHeadId(General.checknull(rst.getString("EXP_SUB_HEAD_ID")));				
				faModel.setSubHeadAmt(General.checknull(rst.getString("SUB_HEAD_AMT")));
				faModel.setAppDate(General.checknull(rst.getString("APP_DATE")));
				faModel.setSubHeadName(General.checknull(rst.getString("sub_head_name")));
				faModel.setPurpose(General.checknull(rst.getString("purpose")));
				faModel.setOrder_no(General.checknull(rst.getString("order_no")));
				faModel.setDocName(General.checknull(rst.getString("fname")));
				faModel.setFlg(General.checknull(rst.getString("flg")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("CreateManageExpenseManager[getSaveData]", e.toString()));
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
				l.fatal(Logging.logException("CreateManageExpenseManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static Integer getRemainingBlnc( String resPrps, String finYrId, String hid,String shid){ 
		PreparedStatement psmt = null;
		ResultSet rst = null;
		Connection conn = null;
		int famt=0;
		try{
			conn = DBConnection.getConnection();
			String sub_head=General.checknull(shid);
			String Qry = "select ifnull((select coalesce(sum(amount),0) as APP_AMT from rsrch_fund_allocation_detail fad,rsrch_fund_allocation_mast fam, "
					+ "rsrch_research_head h where fad.head_id=h.head_id and fam.fund_allc_mid=fad.fund_allc_mid and fam.PS_MID='"+General.checknull(resPrps) +"' and"
					+ " fam.financial_year='"+General.checknull(finYrId) +"' and fad.head_id="+General.checknull(hid)+" ";
			if(!sub_head.equals("")){
				Qry+=" and fad.subhead_id= '"+sub_head+"' ";
			}
			Qry+=")-ifnull(sum(med.SUB_HEAD_AMT), 0),0) as blnc"
					+ " from RSRCH_MANAGE_EXP_mast mem, RSRCH_MANAGE_EXP_DETAIL med  "
					+ "where mem.EXP_HEAD_MID=med.EXP_HEAD_MID  and mem.FIN_YEAR='"+General.checknull(finYrId)+"' "
					+ "and mem.RSRCH_PROP='"+General.checknull(resPrps) +"' ";
					if(!hid.equals("")){
						Qry+= "and med.HEAD_ID in ("+General.checknull(hid)+")";
					}if(!sub_head.equals("")){
						Qry+= "and med.EXP_SUB_HEAD_ID ='"+sub_head+"' ";
					}
			psmt = conn.prepareStatement(Qry);
			//System.out.println("getRemainingBlnc ||"+psmt);
			rst= psmt.executeQuery();
			if(rst.next()){
				famt=rst.getInt(1);
			}
			
		}
		catch(Exception e){
			System.out.println("EXCEPTION IS CAUSED BY: CreateManageExpenseManager" + "[getRemainingBlnc]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("CreateManageExpenseManager [getRemainingBlnc]", e.toString()));
			return famt;
		}
		return famt;
	}
	
	public static JSONObject deleteFiles(String file, String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		String qry = "", errMsg = "", flg = "";
		int cnt = 0;
		boolean flag = false;
		JSONObject obj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			if(!General.checknull(id).equals("")){ 
				qry = "delete from file_attachment where file_attachment_id="+General.checknull(id)+" and table_name='RSRCH_MANAGE_EXP_DETAIL'";				
				psmt = conn.prepareStatement(qry);
				cnt = psmt.executeUpdate();
				String x_folderName =ReadProps.getkeyValue("document.path", "sitsResource")+"/"+file;				
				///File bDirectory = new File(x_folderName+id.split("#")[1]);
				File bDirectory = new File(x_folderName);
				flag=bDirectory.delete();
			}
			

			if (cnt>0 && flag) {
				conn.commit();
				errMsg = ApplicationConstants.DELETED;
				//System.out.println(errMsg);
				flg = "Y";
			} else {
				conn.rollback();
				errMsg = ApplicationConstants.FAIL;
				flg = "N";
			}

		} catch (Exception e) {
			if (e.getMessage().contains("foreign key constraint fails")) {
				errMsg = ApplicationConstants.DELETE_FORIEGN_KEY;
			} else {
				errMsg = ApplicationConstants.FAIL;
			}
			System.out.println("Exception in CreateManageExpenseManager[deleteFiletrain]" + e.getMessage());
			l.fatal(Logging.logException("Error in CreateManageExpenseManager[deleteFiletrain]", e.getMessage().toString()));
		} finally {
			try {
				if (psmt != null) psmt.close();
				if (conn != null) conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		obj.put("errMsg", errMsg);
		obj.put("flg", flg);
		return obj;
	}
	
	
	public static ArrayList<CreateManageExpenseModel> getSaveDataForManageClosingblnc( String resPrps, String finYrId, String dt){		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";		
		try {
			conn = DBConnection.getConnection();
			
			
			query = "select EXP_HEAD_MID,HEAD_ID,totolallocated,totalexpenses,totolallocated-(totalexpenses) as   blnc,closing_blnc,APP_DATE,EXP_HEAD_DID,EXP_SUB_HEAD_ID ,SUB_HEAD_AMT,sub_head_name from ("
					+ "select mem.EXP_HEAD_MID, (SELECT head_name FROM rsrch_research_head WHERE head_id=med.HEAD_ID)  as HEAD_ID,  (SELECT sub_head_name FROM rsrch_research_sub_head WHERE sub_head_id"
					+ "=med.EXP_SUB_HEAD_ID)  as sub_head_name,"
					+ "(select sum(amount) FROM rsrch_fund_allocation_detail d,rsrch_fund_allocation_mast m where m.fund_allc_mid=d.fund_allc_mid and m.PS_MID=mem.RSRCH_PROP and m.PS_MID='"+General.checknull(resPrps)+"' and financial_year='"+General.checknull(finYrId)+"' and d.head_id=med.HEAD_ID and coalesce(d.subhead_id,'')=coalesce(med.EXP_SUB_HEAD_ID,'')) as totolallocated,"

					+ "sum(med.SUB_HEAD_AMT) as totalexpenses,coalesce(med.closing_blnc,0) as closing_blnc, date_format(mem.APP_DATE, '%d/%m/%Y') APP_DATE, (med.EXP_HEAD_DID) EXP_HEAD_DID,  (med.EXP_SUB_HEAD_ID) EXP_SUB_HEAD_ID, (med.SUB_HEAD_AMT) SUB_HEAD_AMT "
					+ "from RSRCH_MANAGE_EXP_mast mem,  RSRCH_MANAGE_EXP_DETAIL med where mem.EXP_HEAD_MID=med.EXP_HEAD_MID and mem.FIN_YEAR='"+General.checknull(finYrId)+"' and  mem.RSRCH_PROP='"+General.checknull(resPrps)+"' group by  med.HEAD_ID,med.EXP_SUB_HEAD_ID"
					+ ") as closingamt order by head_id,sub_head_name ";
			System.out.println("getSaveDataForManageClosingblnc-"+query);
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();				
			while (rst.next()) {
				CreateManageExpenseModel faModel = new CreateManageExpenseModel();				
				faModel.setMid(General.checknull(rst.getString("EXP_HEAD_MID")));
				faModel.setHeadId(General.checknull(rst.getString("HEAD_ID")));
				faModel.setTotalAllocated(General.checknull(rst.getString("totolallocated")));
				faModel.setTotalExpenses(General.checknull(rst.getString("totalexpenses")));
				faModel.setBlncAmt(General.checknull(rst.getString("blnc")));
				faModel.setDid(General.checknull(rst.getString("EXP_HEAD_DID")));
				faModel.setSubHeadId(General.checknull(rst.getString("EXP_SUB_HEAD_ID")));				
				faModel.setSubHeadAmt(General.checknull(rst.getString("SUB_HEAD_AMT")));
				faModel.setAppDate(General.checknull(rst.getString("APP_DATE")));
				faModel.setTotalAmt(General.checknull(rst.getString("closing_blnc")));
				faModel.setSubHeadName(General.checknull(rst.getString("sub_head_name")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("CreateManageExpenseManager[getSaveDataForManageClosingblnc]", e.toString()));
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
				l.fatal(Logging.logException("CreateManageExpenseManager[getSaveDataForManageClosingblnc]", sql.toString()));
			}
		}
		return al;
	}
	
	
	public static CreateManageExpenseModel saveClosingBlnc(CreateManageExpenseModel cModel, String login_id, String machine) {
		
		Connection conn = null;
		PreparedStatement psmt = null;
		String qry = "";
		int[] cnt = null;
		
		String purInd=General.checknull(cModel.getResPrps());
		/*String install_Id=General.checknull(cModel.getInstall_Id());*/
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			for(CreateManageExpenseModel sdata:cModel.getHeadList()){
				String headid=General.checknull((sdata.getHeadId()).trim());
				String totalAmt=General.checknull((sdata.getTotalAmt()));
				String subHeadId=General.checknull((sdata.getSubHeadId()));
				
				qry = "update rsrch_manage_exp_detail set CLOSING_BLNC='"+totalAmt+"' where head_id='"+headid+"' ";
						if(!General.checknull(subHeadId).equals("")){
							qry+= " and EXP_SUB_HEAD_ID='"+subHeadId+"'  ";
						}
						qry+= " and EXP_HEAD_MID in (select distinct EXP_HEAD_MID from RSRCH_MANAGE_EXP_MAST "
						+ "where RSRCH_PROP='"+purInd+"' ) ";	
				
				//System.out.println("qry updates"+qry);
				psmt = conn.prepareStatement(qry);
				psmt.addBatch();
				cnt = psmt.executeBatch();
			}

			if (cnt.length>0){
				conn.commit();
				cModel.setErrMsg("Closing Balance Save Successfully");			
				cModel.setValid(true);
			} else {
				conn.rollback();
				cModel.setErrMsg(ApplicationConstants.FAIL);
				cModel.setValid(false);
			}

		}  catch (Exception e) {
			String str=e.getMessage().toString() ;
			if(str.indexOf("Duplicate entry") != -1)
				str = ApplicationConstants.DUPLICATE;
			else 
				str = ApplicationConstants.EXCEPTION_MESSAGE;
			
				cModel.setErrMsg(str);
				cModel.setValid(false);
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.out.println("Error in CreateManageExpenseManager.java(saveClosingBlnc) : " + e.getMessage());
		} finally {
			try {
				if (psmt != null){
					psmt.clearBatch();
					psmt.close();
				}
				
				if (conn != null) conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return cModel;
	}
	public static boolean  getCheck(String finYear,String installId, String resPrps,String str){		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String query="",id="";		
		boolean flg = false;
		try {
			conn = DBConnection.getConnection();
			if(str.equals("1")){
			query = "select fund_allc_mid as id from rsrch_fund_allocation_mast fam where  "
					+ "fam.PS_MID='"+General.checknull(resPrps)+"' "
					/*+ "and fam.install_id='"+General.checknull(installId)+"' "*/
			       /* + "and fam.financial_year='"+finYear+"'"*/;
			}else{
				query = "select EXP_HEAD_MID as id from RSRCH_MANAGE_EXP_mast where "
						+ "  RSRCH_PROP='"+General.checknull(resPrps)+"' "
						/*+ "and install_id='"+General.checknull(installId)+"' "*/
						/*+ "and FIN_YEAR='"+finYear+"'"*/;
			}
			psmt = conn.prepareStatement(query);
			System.out.println("getSave  Data>>>>"+psmt);
			rst = psmt.executeQuery();	
			while (rst.next()) {
				id=(General.checknull(rst.getString("id")));
			}
				if(!id.equals("")){
					flg=true;
				}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("CreateManageExpenseManager[getSaveData]", e.toString()));
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
				l.fatal(Logging.logException("CreateManageExpenseManager[getList]", sql.toString()));
			}
		}
		//System.out.println("flg"+flg);
		return flg;
	}
	
}