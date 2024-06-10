package com.sits.rsrch.form2_project_completion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sits.commonApi.commonAPI;
import com.sits.conn.DBConnection;
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.general.ReadProps;

public class ProjectCompletionManager {
	static Logger l = Logger.getLogger("exceptionlog");

	public static String save(ProjectCompletionModel model, JSONArray consobj, List<FileItem> items, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0";
		int chk=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String mid="",empId="",RetiredempId="";
			
			String Qry1 = "SELECT concat('PC', LPAD(CONVERT(IFNULL(MAX(SUBSTR(PC_MID,3,6)),0)+1,SIGNED INTEGER),4,'0')) FROM RSRCH_FORM2_MAST";
			PreparedStatement psmt5= conn.prepareStatement(Qry1);
			ResultSet rst1 = psmt5.executeQuery();
			if (rst1.next()) {
				mid = General.checknull(rst1.getString(1));
			}
			
			qry="INSERT INTO RSRCH_FORM2_MAST (PC_MID, PS_MID, PC_DEPT, PC_DESG, PC_COMMENCEMENT, PC_PI_ACC_CLOSE, PC_BLNC_REF_FUND_AGN, "
					+ "PC_SUBM_TO_FUND_AGN, PC_SUBM_TO_PROJ_CELL, PC_TECH_REP_SUBM_TO_FUND_AGN, PC_REL_INFO, CREATED_BY, CREATED_MACHINE, "
					+ "CREATED_DATE, PS_PRINCIPAL,LOCATION_CODE,DDO_ID,RETIRE_PS_PRINCIPAL) VALUES (?, ?, ?, ?, str_to_date(?,'%d/%m/%Y'), ?, ?, ?, ?, ?, ?, ?, ?, now(), ?,?,?,?)";
			psmt = conn.prepareStatement(qry);
			if(model.getPiId().split("~")[0].contains("Retired")){
				RetiredempId= General.checknull(model.getPiId().split("~")[0]).trim();
				}else{
				empId= General.checknull(model.getPiId().split("~")[0]).trim();	
					}
			
			psmt.setString(1, General.checknull(mid).trim());
			psmt.setString(2, General.checknull(model.getPiId().split("~")[1]).trim());
			psmt.setString(3, General.checknull(model.getDept()));
			psmt.setString(4, General.checknull(model.getDesg()));
			psmt.setString(5, General.checknull(model.getXTODATE()));
			psmt.setString(6, General.checknull(model.getPiAccClose()));
			psmt.setString(7, General.checknull(model.getBlncRefFundAgn()));
			psmt.setString(8, General.checknull(model.getSubmToFundAgn()));			
			psmt.setString(9, General.checknull(model.getSubmToProjCell()));			
			psmt.setString(10, General.checknull(model.getTechRepsubmToFundAgn()));
			psmt.setString(11, General.checknull(model.getRelvInfo()));
			psmt.setString(12, General.checknull(model.getUser_id()));			
			psmt.setString(13, General.checknull(model.getMachine()));
			psmt.setString(14, empId);
			psmt.setString(15, General.checknull(model.getLocationCode()));
			psmt.setString(16, General.checknull(model.getDdoCode()));
			psmt.setString(17, RetiredempId);
			
			System.out.println("form 2 save psmt||"+psmt);
			int cnt = psmt.executeUpdate();
			
			if (cnt > 0){
				if(items!=null){
					Iterator<FileItem> iter = items.iterator();
					while (iter.hasNext()) {
						FileItem fileItem = iter.next();
						JSONObject obj = (JSONObject) consobj.get(chk);
						String docName="";
						if(obj.get("doctitle")!=null)
							docName=obj.get("doctitle").toString();
						
						saveDoc(model.getMachine(), mid, model.getUser_id(), conn, fileItem, consobj, docName);						
						chk++;
					}
				}
				msg="1";
				conn.commit();
			} else {
				msg="0";
				conn.rollback();
			}
		} catch (Exception e) {
			msg="2";
			System.out.println("Exception in ProjectCompletionManager[SAVE]" + " " + e.getMessage());
			l.fatal(Logging.logException("ProjectCompletionManager[SAVE]", e.toString()));
		} finally {
			try {
				conn.close();
				psmt.close();
				rst.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return msg;
	}
	
	public static boolean saveDoc(String machine, String id, String userid, Connection conn, FileItem fileItem, JSONArray consobj, String docName){		
		java.io.File file;
		try{
			String attachid=filedetailsave(machine, fileItem.getName(), id, userid, fileItem.getSize(), conn, docName);
	  		
			if(!attachid.equals("")){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/FORM2/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName());
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in ProjectCompletionManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("ProjectCompletionManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}

	public static String filedetailsave(String machine, String name, String id, String userid, long size, Connection conn, String docName){ 
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";
		String attachid="";
		int count=0;
		try{
			String Qry = "SELECT IFNULL(MAX(PC_DID)+1,1) FROM RSRCH_FORM2_ATTACH"; 
			psmt = conn.prepareStatement(Qry);
			rst= psmt.executeQuery();
			if(rst.next())
				attachid=General.checknull(rst.getString(1));
			
			qry="INSERT INTO RSRCH_FORM2_ATTACH (PC_DID, PC_MID, PC_DOC_NAME, PC_FILE_NAME, UPLOADED_DT, UPLOADED_BY, UPLOADED_MACHINE) VALUES (?, ?, ?, ?, now(), ?, ?)";
			psmt = conn.prepareStatement(qry);
			psmt.setString(1, attachid);
			psmt.setString(2, id);
			psmt.setString(3, docName);
			psmt.setString(4, name);
			psmt.setString(5, userid);
			psmt.setString(6, machine);
			count= psmt.executeUpdate();
			
			if(count==0)
				attachid="";
			
		}catch(Exception e){
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: ProjectCompletionManager" + "[filedetailsave]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("ProjectCompletionManager [filedetailsave]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
	public static ArrayList<ProjectCompletionModel> getListold(String piId, String dept, String desg,String locationCode,String ddoCode) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		JSONObject finalObject=new JSONObject();
		try {
			conn = DBConnection.getConnection();
			query = "SELECT b.LOCATION_CODE,b.DDO_ID,PC_MID, a.PS_PRINCIPAL, b.PS_PRINCIPAL, PC_DEPT, PC_DESG FROM RSRCH_FORM2_MAST a, rsrch_form1_mast b "
					+ "where a.PS_MID=b.PS_MID";
			if(!General.checknull(piId).trim().equals("")){
				query+=" and a.PS_PRINCIPAL = '"+General.checknull(piId.split("~")[0])+"' ";
			}
			if(!General.checknull(dept).trim().equals("")){
				query+=" and PC_DEPT = '"+General.checknull(dept)+"' ";
			}
			if(!General.checknull(desg).trim().equals("")){
				query+=" and PC_DESG = '"+General.checknull(desg)+"' ";
			}
			if(!General.checknull(locationCode).trim().equals("")){
				query+=" and LOCATION_CODE = '"+General.checknull(locationCode)+"' ";
			}
			if(!General.checknull(ddoCode).trim().equals("")){
				query+=" and ddo_id = '"+General.checknull(ddoCode)+"' ";
			}
			System.out.println("getList(ProjectCompletionManager)-"+query);
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				ProjectCompletionModel faModel = new ProjectCompletionModel();
				JSONObject jsonobj=new JSONObject();
				faModel.setId(General.checknull(rst.getString("PC_MID")));
				
				finalObject.put("tablename", "employee_mast");
				finalObject.put("columndesc", "concat(employeeName, ' (', employeeCodeM ,')' )");
				finalObject.put("id", "employeeId");
				finalObject.put("idvalue", General.checknull(rst.getString("PS_PRINCIPAL")));
				jsonobj = commonAPI.getDropDownByWebService("rest/apiServices/commonApi", finalObject);
				if (!jsonobj.isEmpty())
					faModel.setPiName(General.checknull(jsonobj.get("display_name").toString()));
				finalObject.clear();
				
				//faModel.setPiName(General.checknull(rst.getString("PS_PRINCIPAL")));
				faModel.setDept(General.checknull(rst.getString("PC_DEPT")));
				
				finalObject.put("tablename", "department_mast");
				finalObject.put("columndesc", "DEPARTMENT");
				finalObject.put("id", "DEPT_ID");
				finalObject.put("idvalue", General.checknull(rst.getString("PC_DEPT")));
				jsonobj = commonAPI.getDropDownByWebService("rest/apiServices/commonApi", finalObject);
				if (!jsonobj.isEmpty())
					faModel.setDeptName(General.checknull(jsonobj.get("display_name").toString()));
				finalObject.clear();
				
				faModel.setDesg(General.checknull(rst.getString("PC_DESG")));
				
				finalObject.put("tablename", "designation_mast");
				finalObject.put("columndesc", "DESIGNATION");
				finalObject.put("id", "DESIGNATION_ID");
				finalObject.put("idvalue", rst.getString("PC_DESG"));
				jsonobj = commonAPI.getDropDownByWebService("rest/apiServices/commonApi", finalObject);
				if (!jsonobj.isEmpty())
					faModel.setDesgName(General.checknull(jsonobj.get("display_name").toString()));
				finalObject.clear();

				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectCompletionManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("ProjectCompletionManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	
	
	public static ArrayList<ProjectCompletionModel> getList(String piId, String dept, String desg,String locationCode,String ddoCode,String user_status) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		//JSONObject finalObject=new JSONObject();
		try {
			JSONObject jsonobj=new JSONObject();
			JSONObject finalObject=new JSONObject();
			finalObject.put("tablename", "employee_mast");
			finalObject.put("columndesc","concat(employeeName,' (',employeeCodeM,')')");
			finalObject.put("id", "employeeId");
			jsonobj= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObject);
			JSONArray deparr = (JSONArray) jsonobj.get("commondata");
			
			JSONObject jsonobj1=new JSONObject();
			JSONObject finalObject1=new JSONObject();
			finalObject1.put("tablename", "department_mast");
			finalObject1.put("columndesc","DEPARTMENT");
			finalObject1.put("id", "DEPT_ID");
			jsonobj1= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObject1);
			JSONArray departmentarr = (JSONArray) jsonobj1.get("commondata");
			
			
			JSONObject jsonobj2=new JSONObject();
			JSONObject finalObject2=new JSONObject();
			finalObject2.put("tablename", "designation_mast");
			finalObject2.put("columndesc","DESIGNATION");
			finalObject2.put("id", "DESIGNATION_ID");
			jsonobj2= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObject2);
			JSONArray designationarr = (JSONArray) jsonobj2.get("commondata");
			
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
			query = "SELECT b.LOCATION_CODE,b.DDO_ID,PC_MID, a.PS_PRINCIPAL, b.PS_PRINCIPAL, PC_DEPT, PC_DESG,PC_TECH_REP_SUBM_TO_FUND_AGN,b.PS_TITTLE_PROJ FROM RSRCH_FORM2_MAST a, rsrch_form1_mast b "
					+ "where a.PS_MID=b.PS_MID";
			
			if(!user_status.equals("A")){
				query+=" and a.PS_PRINCIPAL = '"+General.checknull(piId.split("~")[0])+"' ";
			}
			if(!General.checknull(dept).trim().equals("")){
				query+=" and PC_DEPT = '"+General.checknull(dept)+"' ";
			}
			if(!General.checknull(desg).trim().equals("")){
				query+=" and b.PC_DESG = '"+General.checknull(desg)+"' ";
			}
			if(!General.checknull(locationCode).trim().equals("")){
				query+=" and b.LOCATION_CODE = '"+General.checknull(locationCode)+"' ";
			}
			if(!General.checknull(ddoCode).trim().equals("")){
				query+=" and b.ddo_id = '"+General.checknull(ddoCode)+"' ";
			}
			System.out.println("getList(ProjectCompletionManager)-"+query);
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				ProjectCompletionModel faModel = new ProjectCompletionModel();
				
				
				for(int i=0; i<deparr.size(); i++){
					JSONObject jsn= (JSONObject) deparr.get(i);
					if(jsn.get("id").equals(rst.getString("PS_PRINCIPAL")))
					{
						faModel.setPiNameDesc( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				for(int i=0; i<departmentarr.size(); i++){
					JSONObject jsn=	(JSONObject) departmentarr.get(i);
					if(jsn.get("id").equals(rst.getString("PC_DEPT")))
					{
						faModel.setDeptName( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				for(int i=0; i<designationarr.size(); i++){
					JSONObject jsn=	(JSONObject) designationarr.get(i);
					if(jsn.get("id").equals(rst.getString("PC_DESG")))
					{
						faModel.setDesgName( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				
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
				
				
				
				faModel.setfId(General.checknull(rst.getString("PC_MID")));
				faModel.setDept(General.checknull(rst.getString("PC_DEPT")));
				faModel.setDesg(General.checknull(rst.getString("PC_DESG")));
				faModel.setIsSubmit(General.checknull(rst.getString("PC_TECH_REP_SUBM_TO_FUND_AGN")));
				faModel.setLocationCode(General.checknull(rst.getString("LOCATION_CODE")));
				faModel.setPiName(General.checknull(rst.getString("PS_TITTLE_PROJ")));    
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectCompletionManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("ProjectCompletionManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static String delete(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "", al="";
		int count = 0;
		try {
			conn = DBConnection.getConnection();
			
			String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/FORM2/"+id+"/";
			File directory = new File(directoryName);
			
			query = "delete from rsrch_form2_attach where PC_MID='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			
			if(count > 0){				
				deleteFile(directory);
				al="1";
			}
			psmt=null;rst=null;query="";
			query = "delete from rsrch_form2_mast where PC_MID='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			al="1";
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectCompletionManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("ProjectCompletionManager[getList]", sql.toString()));
			}
		}
		return al;
	}

	public static void deleteFile(File directory) {
		if (directory.isDirectory()) {
			File[] children = directory.listFiles();
			for (File child : children) {
				child.getAbsoluteFile().delete();	
			}
		}
	}
	
	public static ArrayList<ProjectCompletionModel> getEditList(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		JSONObject finalObject=new JSONObject();
		try {
			conn = DBConnection.getConnection();			
			query = "SELECT *, date_format(PC_COMMENCEMENT, '%d/%m/%Y') as dt FROM RSRCH_FORM2_MAST WHERE PC_MID='"+id+"' ";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				ProjectCompletionModel faModel = new ProjectCompletionModel();
				faModel.setId(General.checknull(rst.getString("PC_MID")));
				faModel.setXTODATE(General.checknull(rst.getString("dt")));				
				faModel.setPiId(General.checknull(rst.getString("PS_PRINCIPAL")));
				faModel.setDept(General.checknull(rst.getString("PC_DEPT")));
				faModel.setDesg(General.checknull(rst.getString("PC_DESG")));
				faModel.setPiAccClose(General.checknull(rst.getString("PC_PI_ACC_CLOSE")));
				faModel.setBlncRefFundAgn(General.checknull(rst.getString("PC_BLNC_REF_FUND_AGN")));
				faModel.setSubmToFundAgn(General.checknull(rst.getString("PC_SUBM_TO_FUND_AGN")));
				faModel.setSubmToProjCell(General.checknull(rst.getString("PC_SUBM_TO_PROJ_CELL")));
				faModel.setTechRepsubmToFundAgn(General.checknull(rst.getString("PC_TECH_REP_SUBM_TO_FUND_AGN")));
				faModel.setRelvInfo(General.checknull(rst.getString("PC_REL_INFO")));
				
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectCompletionManager[getEditList]", e.toString()));
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
				l.fatal(Logging.logException("ProjectCompletionManager[getEditList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static ArrayList<ProjectCompletionModel> getEditFileList(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();			
			query = "SELECT * FROM RSRCH_FORM2_ATTACH WHERE PC_MID='"+id+"' ";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				ProjectCompletionModel faModel = new ProjectCompletionModel();
				faModel.setDid(General.checknull(rst.getString("PC_DID")));
				faModel.setfId(General.checknull(rst.getString("PC_MID")));
				faModel.setDoc_name(General.checknull(rst.getString("PC_DOC_NAME")));
				faModel.setFile_name(General.checknull(rst.getString("PC_FILE_NAME")));
				
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectCompletionManager[getEditFileList]", e.toString()));
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
				l.fatal(Logging.logException("ProjectCompletionManager[getEditFileList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static String Update(ProjectCompletionModel model, JSONArray consobj, List<FileItem> items, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0";
		int chk=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String mid="";
			
			qry="UPDATE RSRCH_FORM2_MAST SET PS_MID=?, `PC_DEPT`=?, `PC_DESG`=?, `PC_COMMENCEMENT`=str_to_date(?,'%d/%m/%Y'), `PC_PI_ACC_CLOSE`=?, "
					+ "`PC_BLNC_REF_FUND_AGN`=?, `PC_SUBM_TO_FUND_AGN`=?, `PC_SUBM_TO_PROJ_CELL`=?, `PC_TECH_REP_SUBM_TO_FUND_AGN`=?, `PC_REL_INFO`=?, "
					+ "`UPDATED_BY`=?, `UPDATED_MACHINE`=?, `UPDATED_DATE`=now(), PS_PRINCIPAL=?,`LOCATION_CODE`=?,`DDO_ID`=? WHERE `PC_MID`=?";

			psmt = conn.prepareStatement(qry);
			
			psmt.setString(1, General.checknull(model.getPiId().split("~")[1]).trim());
			psmt.setString(2, General.checknull(model.getDept()));
			psmt.setString(3, General.checknull(model.getDesg()));
			psmt.setString(4, General.checknull(model.getXTODATE()));
			psmt.setString(5, General.checknull(model.getPiAccClose()));
			psmt.setString(6, General.checknull(model.getBlncRefFundAgn()));
			psmt.setString(7, General.checknull(model.getSubmToFundAgn()));			
			psmt.setString(8, General.checknull(model.getSubmToProjCell()));			
			psmt.setString(9, General.checknull(model.getTechRepsubmToFundAgn()));
			psmt.setString(10, General.checknull(model.getRelvInfo()));
			psmt.setString(11, General.checknull(model.getUser_id()));			
			psmt.setString(12, General.checknull(model.getMachine()));
			psmt.setString(13, General.checknull(model.getPiId().split("~")[0]).trim());
			psmt.setString(14, General.checknull(model.getLocationCode()));
			psmt.setString(15, General.checknull(model.getDdoCode()));
			psmt.setString(16, General.checknull(model.getId()));
			int cnt = psmt.executeUpdate();
		
			if (cnt > 0){
				if(items!=null){
					Iterator<FileItem> iter = items.iterator();
					while (iter.hasNext()) {
						FileItem fileItem = iter.next();
						JSONObject obj = (JSONObject) consobj.get(chk);
						String docName="";
						if(obj.get("doctitle")!=null)
							docName=obj.get("doctitle").toString();
						
						saveDoc(model.getMachine(), mid, model.getUser_id(), conn, fileItem, consobj, docName);						
						chk++;
					}
				}
				msg="1";
				conn.commit();
			} else {
				msg="0";
				conn.rollback();
			}
		} catch (Exception e) {
			msg="2";
			System.out.println("Exception in ProjectCompletionManager[Update]" + " " + e.getMessage());
			l.fatal(Logging.logException("ProjectCompletionManager[Update]", e.toString()));
		} finally {
			try {
				conn.close();
				psmt.close();
				rst.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return msg;
	}
	
	public static String deletattchdata(String aid, String fname,String mastid) {
		l = Logger.getLogger("exceptionlog");
		String cSql="",msg="";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int count=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			cSql ="delete from rsrch_form2_attach where PC_DID=? ";
			pstmt = conn.prepareStatement(cSql);
			pstmt.setString(1, aid.trim());
			count = pstmt.executeUpdate();
			if(count>0){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/FORM2/"+mastid+"/"+fname;
				File file = new File(directoryName);
				file.delete();
				msg="File Deleted Successfully";
				conn.commit();
			}		
		} catch (Exception e) {
			System.out.println("Error in ProjectCompletionManager[deletattchdata] : "+e.getMessage());
			l.fatal(Logging.logException("ProjectCompletionManager[deletattchdata]", e.getMessage().toString()));
		} finally {
			try {
				if(pstmt != null) pstmt = null;
				if(rst != null) rst = null;
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return msg;
	}
	
	public static JSONObject getEmployee(HttpServletRequest request) {
		org.codehaus.jettison.json.JSONArray jsonArray = new org.codehaus.jettison.json.JSONArray();
        PreparedStatement pst = null;
        Connection conn=null;
        ResultSet rst = null;
        String empid=General.checknull(request.getParameter("empid"));
        JSONObject objectJson=new JSONObject();
        ArrayList<String> eid = new ArrayList<String>();
        String query="select distinct PS_PRINCIPAL, PS_MID,PS_TITTLE_PROJ from rsrch_form1_mast where 1=1  and is_form_submittd='Y' and PS_PRINCIPAL<>'' " ;
        if(!General.checknull(request.getParameter("location")).equals("")){
			query+=" and LOCATION_CODE='"+ General.checknull(request.getParameter("location"))+"' ";
		}
        if(!General.checknull(request.getParameter("ddo")).equals("")){
			query+=" and DDO_ID='"+ General.checknull(request.getParameter("ddo"))+"' ";
		}
        String user_status=(String) request.getSession().getAttribute("user_status");
        if(!empid.equals("") && user_status.equals("U")){
			query+=" and PS_PRINCIPAL = '"+empid+"' ";
		}
        //Added union for get the reitred pis
        query+="Union select distinct concat('Retired PI -',RETIRE_PS_PRINCIPAL) as PS_PRINCIPAL, PS_MID, "
        		+ "PS_TITTLE_PROJ from rsrch_form1_mast where 1=1  and is_form_submittd='Y' and RETIRE_PS_PRINCIPAL<>'' " ;
        if(!General.checknull(request.getParameter("location")).equals("")){
			query+=" and LOCATION_CODE='"+ General.checknull(request.getParameter("location"))+"' ";
		}
        if(!General.checknull(request.getParameter("ddo")).equals("")){
			query+=" and DDO_ID='"+ General.checknull(request.getParameter("ddo"))+"' ";
		}
        if(!empid.equals("") && user_status.equals("U")){
			query+=" and PS_PRINCIPAL = '"+empid+"' ";
		}
	         try {
	        	 JSONObject obj2 = new JSONObject();
	        	 JSONObject finalObject2 = new JSONObject();
	        	 
	        	 finalObject2.put("tablename", "employee_mast");
	        	 finalObject2.put("columndesc", "concat(employeeName, ' (', employeeCodeM ,')' )");
	        	 finalObject2.put("columndesc2", " employeeCodeM ");
	        	 finalObject2.put("id", "employeeId");
	        	 obj2 = commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObject2);
	        	 JSONArray empl = (JSONArray) obj2.get("commondata"); 

	        	 for(int n=0; n<empl.size(); n++) {
	        		 JSONObject obj = (JSONObject) empl.get(n);
	        		 eid.add(General.checknull(obj.get("id").toString())); 
	        	 }
	        	 conn = DBConnection.getConnection();
	        	 pst = conn.prepareStatement(query);
	        	 System.out.println("ProjectCompletionManager getEmployee query||"+query);
	        	 rst = pst.executeQuery();
	        	 while (rst.next()) {
	        		 JSONObject json = new JSONObject();
	        		if(eid.contains(General.checknull(rst.getString("PS_PRINCIPAL")))){
	        			 int a= eid.indexOf(General.checknull(rst.getString("PS_PRINCIPAL")));
	        			 JSONObject jsonobj = (JSONObject) empl.get(a);
	        			 
	        			 json.put("employeeId",  General.checknull(jsonobj.get("id").toString()+"~"+rst.getString("PS_MID")));
        				 json.put("employeeName", General.checknull(jsonobj.get("desc").toString()+"~"+rst.getString("PS_TITTLE_PROJ")));
	        		 }
	        		if(General.checknull(rst.getString("PS_PRINCIPAL")).contains("Retired")){
	        			json.put("employeeId", General.checknull(rst.getString("PS_PRINCIPAL"))+"~"+rst.getString("PS_MID"));
       				 	json.put("employeeName", General.checknull(rst.getString("PS_PRINCIPAL"))+"~"+rst.getString("PS_TITTLE_PROJ"));
	        		}
	        		 jsonArray.put(json);
	              }
	              objectJson.put("employee", jsonArray);
	             System.out.println("objectJson||"+objectJson.toJSONString());
	          } catch (Exception e) {
	           System.out.println("FileName=[ProjectCompletionManager] MethodName=[getEmployee()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[ProjectCompletionManager] MethodName=[getEmployee()] :", e.getMessage().toString()));
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
	            	  l.fatal(Logging.logException("FileName=[ProjectCompletionManager],MethodName=[getEmployee()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }
	
}