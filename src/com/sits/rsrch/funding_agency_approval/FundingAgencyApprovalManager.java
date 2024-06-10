/**
 * @ AUTHOR Amit Dangi
 */
package com.sits.rsrch.funding_agency_approval;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sits.commonApi.commonAPI;
import com.sits.conn.DBConnection;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.general.ReadProps;


public class FundingAgencyApprovalManager {
	static Logger l = Logger.getLogger("exceptionlog"); 
	
	public static JSONObject save(FundingAgencyApprovalModel faModel, String machine, String user_id,List<FileItem> items) {
		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;		
		String query = "";
		int count = 0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			query = "INSERT INTO rsrch_funding_agency_approval ( location_code, ddo_id, PS_MID, fa_id, approved_amount, "
					+ " alloted_amount, alloted_date, remaining_amount, is_upload_file, remarks, CREATED_BY, "
					+ "CREATED_MACHINE, CREATED_DATE,sanction_orderno,sanction_orderdate,fin_yr,is_opening_blnc,opening_blnc,received_date) "
					+ "VALUES ( ?,?,?,?,?,?,str_to_date(?,'%d/%m/%Y'),?,?,?,?,?, now(),?,?,?,?,?,?);";
			psmt = conn.prepareStatement(query);
			
			psmt.setString(1, General.checknull(faModel.getLocation_code()));
			psmt.setString(2, General.checknull(faModel.getDdo_id()).trim());
			psmt.setString(3, General.checknull(faModel.getPS_MID()).trim());
			psmt.setString(4, General.checknull(faModel.getFa_id()).trim());
			psmt.setString(5, General.checknull(faModel.getApproved_amount()).trim());
			psmt.setString(6, General.checknull(faModel.getAlloted_amount()).trim());
			psmt.setString(7, General.checknull(faModel.getAll_date()).trim());
			psmt.setString(8, General.checknull(faModel.getRemaining_amount()).trim());
			psmt.setString(9, General.checknull(faModel.getIs_upload_file()).trim());
			psmt.setString(10, General.checknull(faModel.getRemarks()).trim());
			psmt.setString(11, General.checknull(user_id));
			psmt.setString(12, General.checknull(machine));
			psmt.setString(13, General.checknull(faModel.getSanction_orderno()).trim());
			psmt.setString(14, General.checknull(faModel.getSanction_orderdate()).equals("") ? null:General.formatDate(faModel.getSanction_orderdate()));
			psmt.setString(15, General.checknull(faModel.getFin_yr()));
			psmt.setString(16, General.checknull(faModel.getIs_opening_blnc()));
			psmt.setString(17, General.checknull(faModel.getOpening_blnc()));
			psmt.setString(18, (faModel.getReceived_date()).equals("") ? null:General.formatDate(faModel.getReceived_date()));
			System.out.println("FundingAgencyApproval Insert"+psmt);
			count = psmt.executeUpdate();
			
			if (count > 0) {
				if(items!=null){
					Iterator<FileItem> iter = items.iterator();
					while (iter.hasNext()) {
						FileItem fileItem = iter.next();
						//JSONObject obj = (JSONObject) consobj.get(chk);
						String docName="";
					
						saveDoc(machine, faModel.getPS_MID(), user_id, conn, fileItem, docName);
					}
				}
				conn.commit();
				jSonDataFinalObj.put("status", "Funding Agency Approval Saved Successfully");
				jSonDataFinalObj.put("flag", "Y");
			} else {
				conn.rollback();				
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
		} catch (Exception e) {
			if (e.toString().contains("Duplicate")){
				jSonDataFinalObj.put("status", ApplicationConstants.UNIQUE_CONSTRAINT + " On Alloted Date : "+General.checknull(faModel.getAll_date()).trim());
				jSonDataFinalObj.put("flag", "N");
			}
			if (e.toString().contains("REFERENCES")){
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
			System.out.println("EXCEPTION CAUSED BY FundingAgencyApprovalManager :" + " " + e.getMessage().toUpperCase());
			l.fatal(Logging.logException("FundingAgencyApprovalManager[save]", e.toString()));
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println("EXCEPTION IN FundingAgencyApprovalManager CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}
	
	public static boolean saveDoc(String machine, String id, String userid, Connection conn, FileItem fileItem, String docName){		
		java.io.File file;
		try{
			String attachid=saveFileattachment(machine, fileItem.getName().replace("&", "and"), id, userid, conn);
			
			if(!attachid.equals("")){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
				//file2.transferTo(file);
			}
	  	}catch(Exception e){
	  		System.out.println("Error in FundingAgencyApprovalManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("FundingAgencyApprovalManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}
	
	public static String saveFileattachment(String machine, String name, String id, String userid, Connection conn){ 
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";
		String attachid="";
		int count=0;
		try{
			/*String Qry = "SELECT IFNULL(MAX(faa_id)+1,1) FROM rsrch_funding_agency_approval where 1=1"; 
			psmt = conn.prepareStatement(Qry);
			rst= psmt.executeQuery();
			if(rst.next())
				attachid=General.checknull(rst.getString(1));*/
			
			qry="INSERT INTO file_attachment ( file_name, file, file_type, table_name, reference_id, CREATED_BY, CREATED, MACHINE) VALUES "
			+ "( ?, ?, ?, 'rsrch_funding_agency_approval', ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry);
			String splitedfilename[]=name.split("\\.(?=[^\\.]+$)");
			psmt.setString(1, name);
			psmt.setString(2, splitedfilename[0]);
			psmt.setString(3, splitedfilename[1]);
			psmt.setString(4, id);
			psmt.setString(5, userid);
			psmt.setString(6, machine);
			//System.out.println("saveFileattachment Insert FundingAgencyApprovalManager>>"+psmt);
			count= psmt.executeUpdate();
			if(count!=0)
				attachid=id;
		}
		catch(Exception e)
		{
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: FundingAgencyApprovalManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("FundingAgencyApprovalManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
	
	
	
	public static ArrayList<FundingAgencyApprovalModel> getList(FundingAgencyApprovalModel fam,String mode,String user_id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		//JSONObject finalObject=new JSONObject();
		try {
			
			JSONObject jsonobjlocation=new JSONObject();
			JSONObject finalObjectlocation=new JSONObject();
			finalObjectlocation.put("tablename", "leave_location_mast");
			finalObjectlocation.put("columndesc","LOCATION_NAME");
			finalObjectlocation.put("id", "LOCATION_CODE");
			jsonobjlocation= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObjectlocation);
			JSONArray designationlocationarr = (JSONArray) jsonobjlocation.get("commondata");
			
			JSONObject jsonobjDdo=new JSONObject();
			JSONObject finalObjectDdo=new JSONObject();
			finalObjectDdo.put("tablename", "ddo");
			finalObjectDdo.put("columndesc","DDONAME");
			finalObjectDdo.put("id", "DDO_ID");
			jsonobjDdo= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObjectDdo);
			JSONArray designationDtoarr = (JSONArray) jsonobjDdo.get("commondata");
			
	
			
			
			
			conn = DBConnection.getConnection();
			if(mode.equals("delete")){
				//System.out.println("getProjTitle:"+fam.getPS_MID());
				query = "select fin_yr,faa_id,a.LOCATION_CODE,a.ddo_id,PS_TITTLE_PROJ,a.PS_MID,fa_id,date_format(alloted_date,'%d/%m/%Y') alloted_date1,GETFundingAgencyname(fa_id) as agencyname,approved_amount,alloted_amount,date_format(alloted_date, '%d/%m/%Y') as alloted_date,"
						+ "remaining_amount,is_upload_file,remarks,sanction_orderno,date_format(sanction_orderdate,'%d/%m/%Y') as sanction_orderdate,date_format(received_date,'%d/%m/%Y') as received_date,"
						+ "(select file_name from file_attachment where  table_name='rsrch_funding_agency_approval' and reference_id=a.PS_MID AND date_format(CREATED,'%m-%d-%Y-%h-%i')=date_format(a.CREATED_DATE,'%m-%d-%Y-%h-%i') "
						+ "order by CREATED desc limit 1) as uploadedfile,(select (case when install_id is not null then 'Y' else 'N' END) from rsrch_fund_allocation_mast "
						+ "where install_id=a.faa_id) as isfundallocated,is_opening_blnc,opening_blnc from rsrch_funding_agency_approval a ,rsrch_form1_mast b WHERE 1=1 and a.PS_MID=b.PS_MID";
			
				if(!General.checknull(fam.getFaaId()).trim().equals("")){
					query+=" and fa_id in (select fa_id from rsrch_funding_agency_approval where faa_id='"+General.checknull(fam.getFaaId())+"') ";
				}
				if(!General.checknull(fam.getPS_MID()).equals("")){		
					query += " and a.PS_MID='"+General.checknull(fam.getPS_MID())+"' ";
				}
				if(!General.checknull(fam.getFin_yr()).equals("")){		
					query += " and a.fin_yr='"+fam.getFin_yr()+"'";
					
				}
				
			}else{
				query="select fin_yr,faa_id,a.LOCATION_CODE,a.ddo_id,PS_TITTLE_PROJ,a.PS_MID,fa_id,GETFundingAgencyname(fa_id) as agencyname,date_format(alloted_date,'%d/%m/%Y') alloted_date1,"
					+ "approved_amount,(select sum(alloted_amount) from rsrch_funding_agency_approval  where ddo_id=a.ddo_id and PS_MID=a.PS_MID and fa_id=a.fa_id and fin_yr=a.fin_yr) as alloted_amount,"
					+ "date_format(alloted_date, '%d/%m/%Y') as alloted_date,remaining_amount,is_upload_file,remarks,sanction_orderno,date_format(sanction_orderdate,'%d/%m/%Y') as sanction_orderdate,date_format(received_date,'%d/%m/%Y') as received_date,"
					+ "(select file_name from file_attachment where  table_name='rsrch_funding_agency_approval' and reference_id=a.PS_MID AND date_format(CREATED,'%m-%d-%Y-%h-%i')=date_format(a.CREATED_DATE,'%m-%d-%Y-%h-%i') order by CREATED desc limit 1) as uploadedfile,"
					+ "(select (case when install_id is not null then 'Y' else 'N' END) from rsrch_fund_allocation_mast where install_id=a.faa_id) as isfundallocated,is_opening_blnc,opening_blnc from rsrch_funding_agency_approval a ,rsrch_form1_mast b"
					+ " WHERE 1=1 and a.PS_MID=b.PS_MID and faa_id in (select max(faa_id) AS maxfaa_id from rsrch_funding_agency_approval group by location_code,ddo_id,PS_MID,fin_yr) ";
			
			if(!General.checknull(fam.getFaaId()).trim().equals("")){
				query+=" and faa_id = '"+General.checknull(fam.getFaaId())+"' ";
			}
			if(!General.checknull(fam.getProjTitle()).equals("")){		
				query += " and b.PS_MID='"+General.checknull(fam.getProjTitle())+"'";
			}if(!General.checknull(fam.getFunding()).equals(""))	{	
				query += " and a.fa_id='"+General.checknull(fam.getFunding())+"'";
			
			}if(!General.checknull(user_id).equals("ADMIN")){		
			query += " and a.CREATED_BY='"+user_id+"'";
			
			}
			if(!General.checknull(fam.getFin_yr()).equals("")){		
				query += " and a.fin_yr='"+fam.getFin_yr()+"'";
				
				}
			}
			/*
			if(!General.checknull(fam.getDdoCode()).trim().equals("")){
				query+=" and ddo_id = '"+General.checknull(fam.getDdoCode())+"' ";
			}*/
			
			query+=" order by a.CREATED_DATE desc";
			System.out.println("getList(FundingAgencyApprovalManager fam) mode-"+mode+"||"+query);
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				FundingAgencyApprovalModel faModel = new FundingAgencyApprovalModel();
				
				for(int i=0; i<designationlocationarr.size(); i++){
					JSONObject jsn=	(JSONObject) designationlocationarr.get(i);
					if(jsn.get("id").equals(rst.getString("LOCATION_CODE")))
					{
						faModel.setLocationName( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				for(int i=0; i<designationDtoarr.size(); i++){
					JSONObject jsn=	(JSONObject) designationDtoarr.get(i);
					if(jsn.get("id").equals(rst.getString("ddo_id")))
					{
						faModel.setDdoName( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				faModel.setFa_id(General.checknull(rst.getString("fa_id")));
				faModel.setFaaId(General.checknull(rst.getString("faa_id")));
				faModel.setLocation_code(General.checknull(rst.getString("LOCATION_CODE")));
				faModel.setDdo_id(General.checknull(rst.getString("ddo_id")));
				faModel.setProjTitle(General.checknull(rst.getString("PS_TITTLE_PROJ")));
				faModel.setPS_MID(General.checknull(rst.getString("PS_MID")));
				faModel.setFagencyName(General.checknull(rst.getString("agencyname")));
				faModel.setApproved_amount(General.checknull(rst.getString("approved_amount")));
				faModel.setAlloted_amount(General.checknull(rst.getString("alloted_amount")));
				faModel.setRemaining_amount(General.checknull(rst.getString("remaining_amount")));
				faModel.setAlloted_date(General.checknull(rst.getString("alloted_date")));
				faModel.setIs_upload_file(General.checknull(rst.getString("is_upload_file")));
				faModel.setRemarks(General.checknull(rst.getString("remarks")));
				faModel.setUploadedFile(General.checknull(rst.getString("uploadedfile")));
				faModel.setSanction_orderno(General.checknull(rst.getString("sanction_orderno")));
				faModel.setSanction_orderdate(General.checknull(rst.getString("sanction_orderdate")));
				faModel.setAlloted_date(General.checknull(rst.getString("alloted_date1")));
				faModel.setIsfundallocated(General.checknull(rst.getString("isfundallocated")));
				faModel.setFin_yr(General.checknull(rst.getString("fin_yr")));
				faModel.setIs_opening_blnc(General.checknull(rst.getString("is_opening_blnc")));
				faModel.setOpening_blnc(General.checknull(rst.getString("opening_blnc")));
				faModel.setReceived_date(General.checknull(rst.getString("received_date")));
				al.add(faModel);
				
				
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("FundingAgencyApprovalManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("FundingAgencyApprovalManager[getList]", sql.toString()));
			}
		}
		return al;
	}

	public static String delete(String id,String user_id,String machine) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "", al="";
		int count = 0;
		try {
			conn = DBConnection.getConnection();
			
			String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/"+id+"/";
			File directory = new File(directoryName);
			/*conn = General.updtDeletedData("rsrch_funding_agency_approval", "faa_id", "", "", "", "","",
		    		id, "", "", "","", "", machine, user_id,"",conn);
			if (conn != null) {*/
			query = "delete from rsrch_funding_agency_approval where faa_id='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			
			/*}*/if(count > 0){				
				//deleteFile(directory);
				al="1";
			}
		} catch (Exception e) {
			if (e.toString().contains("REFERENCES")) {
				al="2";
			}
			System.out.println(e);
			l.fatal(Logging.logException("FundingAgencyApprovalManager[delete]", e.toString()));
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
				l.fatal(Logging.logException("FundingAgencyApprovalManager[delete]", sql.toString()));
			}
		}
		return al;
	}

	
/*	  public static void deleteFile(File directory) {
		if (directory.isDirectory()) {
			File[] children = directory.listFiles();
			for (File child : children) {
				child.getAbsoluteFile().delete();	
			}
		}
	}*/
	
	
	
	public static JSONObject GetProjectTenurebyApprovedProjects(String projectId) {
		PreparedStatement pst = null;
		Connection conn=null;
		ResultSet rst = null;
		JSONObject objectJsonfinal=new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		/*String query = "SELECT distinct a.PS_MID,PS_TITTLE_PROJ,concat(PS_DUR_PROJECT_YEAR,' Year ',PS_DUR_PROJECT,' Months') as projduration from rsrch_form1_mast a, rsrch_research_prop_approval b where a.PS_MID=b.PS_MID and a.is_form_submittd='Y' and a.submitted_date is not null ";*/
		//Above query discard as per the new changes for approvel process skip or not 
		
		String query="select distinct a.PS_MID id, PS_TITTLE_PROJ name,concat(PS_DUR_PROJECT_YEAR,' Year ',PS_DUR_PROJECT,' Months') as projduration from rsrch_form1_mast a, rsrch_research_prop_approval b "
	        		+ "where a.PS_MID=b.PS_MID and a.ps_mid='"+projectId+"' and a.is_form_submittd='Y' and b.RPA_STATUS='A' and b.RPA_STATUS = all "
	        		+ "(SELECT RPA_STATUS FROM rsrch_research_prop_approval WHERE RPA_TYPE in ('HD', 'RR', 'RP', 'VC') and RPA_STATUS='A') " ;
					 if(!General.checknull(projectId).equals("")){
							query+=" and a.ps_mid = '"+General.checknull(projectId)+"'";
						}
	        		query += " union "
	        		+ " select distinct a.PS_MID id, PS_TITTLE_PROJ name,concat(PS_DUR_PROJECT_YEAR,' Year ',PS_DUR_PROJECT,' Months') as projduration from rsrch_form1_mast a where IsApprovalReq='No' and a.is_form_submittd='Y'"
	        		+ " and a.ps_mid='"+projectId+"' ";
		try {
        	conn = DBConnection.getConnection();
        	pst = conn.prepareStatement(query);
        	//System.out.println("GetProjectTenurebyApprovedProjects pst||"+pst);
        	rst = pst.executeQuery();
        	while (rst.next()) {
        		JSONObject objectJson=new JSONObject();
        		objectJson.put("PS_MID", General.checknull(rst.getString("id")));
        		objectJson.put("PS_TITTLE_PROJ", General.checknull(rst.getString("name")));
        		objectJson.put("projduration", General.checknull(rst.getString("projduration")));

        		jsonArray.add(objectJson);
        	}
        	objectJsonfinal.put("projectdropdown", jsonArray);
        	//System.out.print("objectJsonfinal>>>");
        	//System.out.println(objectJsonfinal.toString());
        } catch (Exception e) {
        	System.out.println("FileName=[FundingAgencyApprovalManager] MethodName=[GetProjectTenurebyApprovedProjects()] :"+ e.getMessage().toString());
        	l.fatal(Logging.logException("FileName=[FundingAgencyApprovalManager] MethodName=[GetProjectTenurebyApprovedProjects()] :", e.getMessage().toString()));
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
        		l.fatal(Logging.logException("FileName=[FundingAgencyApprovalManager],MethodName=[GetProjectTenurebyApprovedProjects()]", e.getMessage().toString()));
        	}
        }
        return objectJsonfinal;
	}
	
	/*New Method added for update the approved amount
	Only ‘Total Approved Amount’ will always greater than ‘Total Allocated Amount’ 
	for any particular Financial Year and for a funding agency*/	
	public static JSONObject UpdateAmt(FundingAgencyApprovalModel faModel, String machine, String user_id) {
		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;		
		String query = "";
		int count = 0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			query = "update rsrch_funding_agency_approval set approved_amount=?,remaining_amount=(remaining_amount-?),UPDATED_BY=?,UPDATED_MACHINE=?,UPDATED_DATE=now()"
					+ "  where fin_yr=? and PS_MID=? and fa_id=? ";
			psmt = conn.prepareStatement(query);
			
			psmt.setString(1, General.checknull(faModel.getApproved_amount()).trim());
			psmt.setString(2, General.checknull(faModel.getAmount_diff()).trim());
			psmt.setString(3, General.checknull(user_id));
			psmt.setString(4, General.checknull(machine));
			psmt.setString(5, General.checknull(faModel.getFin_yr()).trim());
			psmt.setString(6, General.checknull(faModel.getPS_MID()).trim());
			psmt.setString(7, General.checknull(faModel.getFa_id()).trim());
			
			System.out.println("FundingAgencyApproval amount update ::"+psmt);
			count = psmt.executeUpdate();
			
			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Funding Agency Amount Calculation Update Successfully");
				jSonDataFinalObj.put("flag", "Y");
			} else {
				conn.rollback();				
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
		} catch (Exception e) {
			if (e.toString().contains("Duplicate")){
				jSonDataFinalObj.put("status", ApplicationConstants.UNIQUE_CONSTRAINT);
				jSonDataFinalObj.put("flag", "N");
			}
			if (e.toString().contains("REFERENCES")){
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
			System.out.println("EXCEPTION CAUSED BY FundingAgencyApprovalManager :" + " " + e.getMessage().toUpperCase());
			l.fatal(Logging.logException("FundingAgencyApprovalManager[UpdateAmt]", e.toString()));
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println("EXCEPTION IN FundingAgencyApprovalManager CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}
}