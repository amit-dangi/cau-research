package com.sits.rsrch.research_patent;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

public class ResearchPatentManager {
	static Logger l = Logger.getLogger("exceptionlog CAU Research"); 
	/*This method is used for save  data .In this function passing 5 parameters(model,items,user_id,machine,mode) and will return String*/
	public static ResearchPatentModel save(ResearchPatentModel model,  FileItem items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0",mid="";
		int chk=0,i=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			qry="INSERT INTO rsrch_patent_mast (pat_type,pat_status,applicant_name,pat_title, pat_app_num, date_filing_date, category,"
					+ " sub_category, url_link, pat_awrd_by, pat_publ_granted_date, pat_publ_granted_num,assignees_name,LOCATION_CODE,DDO_ID,fin_yr,"
					+ "`CREATED_BY`, `CREATED_MACHINE`, `CREATED_DATE`,pat_cat,PiName,DEPT_ID) VALUES (?,?,?, ?, ?, ?, ?, ?, ?, "//+ "?, "
					+ "?, ?, ?, ?, ?, ?, ?,?,?,now(),?,?,?)";			
		
			psmt = conn.prepareStatement(qry,psmt.RETURN_GENERATED_KEYS);
			
			psmt.setString(++i, General.checknull(model.getPat_type()));
			psmt.setString(++i, General.checknull(model.getPat_status()));
			psmt.setString(++i, General.checknull(model.getApp_name()));
			psmt.setString(++i, General.checknull(model.getPat_title()));
			psmt.setString(++i, General.checknull(model.getPat_app_num()));
			psmt.setString(++i, General.formatDate(model.getFiling_date()));
			psmt.setString(++i, (model.getResh_category().equals("")?null:model.getResh_category()));
			
			psmt.setString(++i, General.checknull(model.getSub_category()));	
			psmt.setString(++i, General.checknull(model.getUrl()));
			psmt.setString(++i, General.checknull(model.getPat_awd_by()));
			psmt.setString(++i, General.formatDate(model.getPat_grnt_date()));
			psmt.setString(++i, General.checknull(model.getPat_publ_grnt_num()));
			psmt.setString(++i, General.checknull(model.getAss_name()));	
			psmt.setString(++i, General.checknull(model.getLocation()));
			psmt.setString(++i, General.checknull(model.getDdo()));
			psmt.setString(++i, General.checknull(model.getFin_yr()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			psmt.setString(++i, General.checknull(model.getPat_cat()));
			psmt.setString(++i, General.checknull(model.getPiName()));
			psmt.setString(++i, General.checknull(model.getDeptid()));
			//System.out.println("ResearchPatentManager save- "+psmt);
			int cnt = psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				mid = rs.getString(1);
			}
			if (cnt > 0){
				if(items!=null ){
						saveDoc(machine, mid, user_id,conn, items);						
						chk++;
					}
				conn.commit();
					model.setErrMsg(ApplicationConstants.SAVE);
				model.setValid(true);
			 } else {
				conn.rollback();
				model.setErrMsg(ApplicationConstants.FAIL);
				model.setValid(false);
			}
		} catch (Exception e) {
			msg = e.getMessage().toString();
			if (msg.indexOf("Duplicate entry") != -1)
				msg = ApplicationConstants.UNIQUE_CONSTRAINT;
			else
				msg = ApplicationConstants.EXCEPTION_MESSAGE;
			model.setErrMsg(msg);
			model.setValid(false);
			l.fatal(Logging.logException("ResearchPatentManager[save]", e.getMessage().toString()));
			System.out.println("Error in ResearchPatentManager[save] : " + e.getMessage());
		} finally {
			try {
				if (psmt != null)
					psmt.close();
				if (rst != null)
					rst.close();
				conn.close();
				msg = "";
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return model;
	}
	//This function is using for make directory using mkdirs() and save the document in that directory
	public static boolean saveDoc(String machine, String id, String userid,Connection conn, FileItem fileItem){		
		java.io.File file;
		try{
			String attachid=saveFileattachment(machine, fileItem, id, userid, conn);
			
			if(!attachid.equals("")){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/PATENT/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in ResearchPatentManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("ResearchPatentManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}
	//This function is used for save attachment name in file_attachment table
	public static String saveFileattachment(String machine, FileItem fileItem, String id, String userid, Connection conn){ 
		PreparedStatement psmt = null;
		String qry = "";
		String attachid="";
		int count=0;
		try{
			qry="INSERT INTO file_attachment ( file_name, file_type, table_name, reference_id, CREATED_BY, CREATED, MACHINE) VALUES "
			+ "( ? , ?, 'rsrch_patent_mast', ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, "Patent_Certificate");
			psmt.setString(3, id);
			psmt.setString(4, userid);
			psmt.setString(5, machine);
			count= psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				attachid = rs.getString(1);
			}
			 
		}
		catch(Exception e)
		{
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: ResearchPatentManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("ResearchPatentManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	//This function is used for get List on l page
	public static ArrayList<ResearchPatentModel> getList(String pat_type,String pat_status,String location,String ddo,String user_id){
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
			query = "select pat_id,(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_patent_mast' and file_type='Patent_Certificate' and reference_id=a.pat_id order by CREATED desc limit 1) as upld,pat_type, pat_status,category,applicant_name,pat_title,LOCATION_CODE,DDO_ID from rsrch_patent_mast a WHERE 1=1 ";
			if(!pat_type.trim().equals("")) {
				query+=" AND pat_type= '"+pat_type+"' ";
			}if(!pat_status.trim().equals("")) {
				query+=" AND pat_status ='"+pat_status+"' ";
			} if(!location.trim().equals("")){
				query+=" AND LOCATION_CODE ='" + location+"' ";
			} if(!ddo.trim().equals("")) {
				query+=" AND DDO_ID = '" + ddo +"'";
			}if(!user_id.trim().equals("")) {
				query+=" AND CREATED_BY ='" + user_id+"' ";
			}
			psmt = conn.prepareStatement(query);
			// System.out.println("ResearchPatent pstmt-"+psmt);
			rst = psmt.executeQuery();
			while (rst.next()) {
				ResearchPatentModel model = new ResearchPatentModel();
			
				for(int i=0; i<designationlocationarr.size(	); i++){
					JSONObject jsn=	(JSONObject) designationlocationarr.get(i);
					if(jsn.get("id").equals(rst.getString("LOCATION_CODE")))
					{
						model.setLocation( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				for(int i=0; i<designationDtoarr.size(); i++){
					JSONObject jsn=	(JSONObject) designationDtoarr.get(i);
					if(jsn.get("id").equals(rst.getString("ddo_id")))
					{
						model.setDdo( General.checknull(jsn.get("desc").toString()));
					}
				}
				model.setId(General.checknull(rst.getString("pat_id")));
				model.setPat_type(General.checknull(rst.getString("pat_type")));
				model.setPat_status(General.checknull(rst.getString("pat_status")));
				model.setPat_title(General.checknull(rst.getString("pat_title")));
				model.setApp_name(General.checknull(rst.getString("applicant_name")));
				model.setUpload(General.checknull(rst.getString("upld")));
				model.setDdo_id(General.checknull(rst.getString("DDO_ID")));
				model.setLocation_id(General.checknull(rst.getString("LOCATION_CODE")));
				al.add(model);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ResearchPatentManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("ResearchPatentManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	//this method is using to delete from database
	public static ResearchPatentModel delete(String id,String fname) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "",str="";
		int count = 0,cntTable=0;
		ResearchPatentModel model=new ResearchPatentModel();
		try {
			
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			query = "delete from file_attachment where table_name='rsrch_patent_mast' and file_type='Patent_Certificate' AND reference_id='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			
			if(count > 0){	
				psmt=null;query="";
				query = "delete from rsrch_patent_mast where pat_id='"+id+"'";
				psmt = conn.prepareStatement(query);
				cntTable = psmt.executeUpdate();
			}if(cntTable>0){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/PATENT/"+id+"/"+fname;
				File file = new File(directoryName);
				if(file.exists()){
					file.delete();
				}
				conn.commit();
				model.setErrMsg(ApplicationConstants.DELETED);
				model.setValid(true);
			} else {
				conn.rollback();
				model.setErrMsg(ApplicationConstants.FAIL);
				model.setValid(false);
			}
		} catch (Exception e) {
			str = e.getMessage().toString();
			if (str.indexOf("foreign key constraint fails") != -1)
				str = ApplicationConstants.DELETE_FORIEGN_KEY;
			else
				str = ApplicationConstants.EXCEPTION_MESSAGE;
			model.setErrMsg(str);
			model.setValid(false);
			l.fatal(Logging.logException("BulkIndentCreationManager[delete]", e.getMessage().toString()));
			System.out.println("Error in BulkIndentCreationManager[delete] : " + e.getMessage());
	} finally {
			try {
				if (psmt != null)
					psmt.close();
				if (rst != null)
					rst.close();
				conn.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return model;
	}
	
	//This function is using for view the data during edit record
	public static ArrayList<ResearchPatentModel> getEditList(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();			
			query = "SELECT *, (select concat(file_attachment_id,'_',file_name) as file_name from file_attachment "
					+ "where table_name='rsrch_patent_mast' and file_type='Patent_Certificate' and reference_id=a.pat_id "
					+ "order by CREATED desc limit 1) as upld,date_format(date_filing_date, '%d/%m/%Y') as filing_date,"
					+ "date_format(pat_publ_granted_date, '%d/%m/%Y') as granted_date FROM rsrch_patent_mast a "
					+ "WHERE pat_id='"+id+"' ";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {	
				ResearchPatentModel model = new ResearchPatentModel();
				model.setId(General.checknull(rst.getString("pat_id")));
				model.setLocation(rst.getString("LOCATION_CODE"));
				model.setDdo(rst.getString("DDO_ID"));
				model.setPat_type(General.checknull(rst.getString("pat_type")));
				model.setPat_status(General.checknull(rst.getString("pat_status")));
				model.setApp_name(General.checknull(rst.getString("applicant_name")));
				model.setPat_title(General.checknull(rst.getString("pat_title")));
				model.setPat_app_num(General.checknull(rst.getString("pat_app_num")));
				model.setFiling_date(General.checknull(rst.getString("filing_date")));
				model.setResh_category(General.checknull(rst.getString("category")));
				model.setSub_category(rst.getString("sub_category"));
				model.setUrl(General.checknull(rst.getString("url_link")));
				model.setPat_awd_by(General.checknull(rst.getString("pat_awrd_by")));
				model.setPat_grnt_date(General.checknull(rst.getString("granted_date")));
				model.setPat_publ_grnt_num(General.checknull(rst.getString("pat_publ_granted_num")));
				model.setAss_name(General.checknull(rst.getString("assignees_name")));
				model.setUpload(General.checknull(rst.getString("upld")));
				model.setFin_yr(General.checknull(rst.getString("fin_yr")));
				model.setPat_cat(General.checknull(rst.getString("pat_cat")));
				model.setPiName(General.checknull(rst.getString("PiName")));
				model.setDeptid(General.checknull(rst.getString("DEPT_ID")));
				al.add(model);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ResearchPatentManager[getEditList]", e.toString()));
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
				l.fatal(Logging.logException("ResearchPatentManager[getEditList]", sql.toString()));
			}
		}
		return al;
	}
	//This function is using to update data into database.In this function passing 5 parameters(model,items,user_id,machine,mode) and will return String*/
	public static ResearchPatentModel Update(ResearchPatentModel model, FileItem items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0";
		int chk=0,i=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String mid=General.checknull(model.getId());
			
			qry="UPDATE rsrch_patent_mast SET pat_type=?, pat_status=?, applicant_name=?, pat_title=?, pat_app_num=?, "//+ "`PS_FUND_AGNCY`=?, "
					+ "date_filing_date=?, category=?,sub_category=?, "
					+ "url_link=?, pat_awrd_by=?, pat_publ_granted_date=?, pat_publ_granted_num=?,assignees_name=? "
					//+ "`PS_NON_REC_COST`=?, `PS_CHEMICALS_COST`=?, `PS_MANPOWER`=?, `PS_CONTINGENCY`=?, `PS_TRAVEL`=?, `PS_OTHER_CHARGES`=?, `PS_OVERHEAD_CHARGES`=?, "
					+ ",LOCATION_CODE=?,DDO_ID=?,fin_yr=?,`UPDATED_BY`=?, `UPDATED_MACHINE`=?,`UPDATED_DATE`=now(),pat_cat=?,PiName=?,DEPT_ID=? ";
			
			qry+= "WHERE `pat_id`=?";
			
			psmt = conn.prepareStatement(qry);
	//	System.out.println("=model.getXTODATE()="+model.getXTODATE());
			psmt.setString(++i, General.checknull(model.getPat_type()));
			psmt.setString(++i, General.checknull(model.getPat_status()));
			psmt.setString(++i, General.checknull(model.getApp_name()));
			psmt.setString(++i, General.checknull(model.getPat_title()));
			psmt.setString(++i, General.checknull(model.getPat_app_num()));
			psmt.setString(++i, General.formatDate(model.getFiling_date()));
			psmt.setString(++i, (model.getResh_category().equals("")?null:model.getResh_category()));
			
			psmt.setString(++i, General.checknull(model.getSub_category()));	
			psmt.setString(++i, General.checknull(model.getUrl()));
			psmt.setString(++i, General.checknull(model.getPat_awd_by()));
			psmt.setString(++i, General.formatDate(model.getPat_grnt_date()));
			psmt.setString(++i, General.checknull(model.getPat_publ_grnt_num()));
			psmt.setString(++i, General.checknull(model.getAss_name()));	
			psmt.setString(++i, General.checknull(model.getLocation()));
			psmt.setString(++i, General.checknull(model.getDdo()));
			psmt.setString(++i, General.checknull(model.getFin_yr()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			psmt.setString(++i, General.checknull(model.getPat_cat()));
			psmt.setString(++i, General.checknull(model.getPiName()));
			psmt.setString(++i, General.checknull(model.getDeptid()));
			
			psmt.setString(++i, General.checknull(model.getId()));
			int cnt = psmt.executeUpdate();
			//System.out.println("check ---- "+psmt);
			if (cnt > 0){
				if(items!=null && (!items.getName().equals(""))){
						saveDoc(machine, mid, user_id, conn, items);						
						chk++;
					}
				conn.commit();
				model.setErrMsg(ApplicationConstants.UPDATED);
			model.setValid(true);
		 } else {
			conn.rollback();
			model.setErrMsg(ApplicationConstants.FAIL);
			model.setValid(false);
		}
	} catch (Exception e) {
		msg = e.getMessage().toString();
		if (msg.indexOf("Duplicate entry") != -1)
			msg = ApplicationConstants.UNIQUE_CONSTRAINT;
		else
			msg = ApplicationConstants.EXCEPTION_MESSAGE;
		model.setErrMsg(msg);
		model.setValid(false);
		l.fatal(Logging.logException("ResearchPatentManager[update]", e.getMessage().toString()));
		System.out.println("Error in ResearchPatentManager[update] : " + e.getMessage());
	} finally {
		try {
			if (psmt != null)
				psmt.close();
			if (rst != null)
				rst.close();
			conn.close();
			msg = "";
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	return model;
}
	}