package com.sits.rsrch.student_research_project_form;

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

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

public class StudentResearchProjectFormManager {
	static Logger l = Logger.getLogger("exceptionlog CAU Research Patent"); 
	/* MouMoaform added by Amit Dangi 
	 * This static StudentResearchProjectFormModel type method is used for save  data .
	 * In this method suppose to pass 5 parameters(model,items,user_id,machine,mode) 
	 * and will return StudentResearchProjectFormModel object */
	public static StudentResearchProjectFormModel save(StudentResearchProjectFormModel model,  FileItem items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0",mid="";
		int chk=0,i=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			qry="INSERT INTO student_research_project_form (LOCATION_CODE,DDO_ID,stu_name,ICAR_USID,cau_regno,"
					+ " course,discipline,research_thrust_area, research_sub_thrust_area,proj_type,proj_title,"
					+ " objective, guide_name, external_guide_name,fin_year,status,"
					+ "CREATED_BY, CREATED_MACHINE, CREATED_DATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,now())";			
		
			psmt = conn.prepareStatement(qry,psmt.RETURN_GENERATED_KEYS);
			
			psmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
			psmt.setString(++i, General.checknull(model.getDDO_ID()));
			psmt.setString(++i, General.checknull(model.getStu_name()));
			psmt.setString(++i, General.checknull(model.getICAR_USID()));
			psmt.setString(++i, General.checknull(model.getCau_regno()));
			psmt.setString(++i, General.checknull(model.getCourse()));
			psmt.setString(++i, General.checknull(model.getDiscipline()));
			
			psmt.setString(++i, General.checknull(model.getResearch_thrust_area()));
			psmt.setString(++i, General.checknull(model.getResearch_sub_thrust_area()));
			psmt.setString(++i, General.checknull(model.getProj_type()));
			
			psmt.setString(++i, General.checknull(model.getProj_title()));
			psmt.setString(++i, General.checknull(model.getObjective()));
			psmt.setString(++i, General.checknull(model.getGuide_name()));
			psmt.setString(++i, General.checknull(model.getExternal_guide_name()));
			psmt.setString(++i, General.checknull(model.getFin_yr()));
			psmt.setString(++i, General.checknull(model.getStu_status()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			//System.out.println("StudentResearchProjectFormManager save|| "+psmt);
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
			l.fatal(Logging.logException("StudentResearchProjectFormManager[save]", e.getMessage().toString()));
			System.out.println("Error in StudentResearchProjectFormManager[save] : " + e.getMessage());
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
	//This method is using for make directory using mkdirs() and save the document in that directory
	public static boolean saveDoc(String machine, String id, String userid,Connection conn, FileItem fileItem){		
		java.io.File file;
		try{
			String attachid=saveFileattachment(machine, fileItem, id, userid, conn);
			
			if(!attachid.equals("")){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/STUFORM/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in StudentResearchProjectFormManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("StudentResearchProjectFormManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}
	
	//This method is used for save attachment name in file_attachment table
	public static synchronized String saveFileattachment(String machine, FileItem fileItem, String id, String userid, Connection conn){ 
		PreparedStatement psmt = null;
		String qry = "";
		String attachid="";
		int count=0;
		try{
			qry="INSERT INTO file_attachment ( file_name, file_type, table_name, reference_id, CREATED_BY, CREATED, MACHINE) VALUES "
			+ "( ? , ?, 'student_research_project_form', ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, "student_research_project_doc");
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
			System.out.println("EXCEPTION IS CAUSED BY: StudentResearchProjectFormManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("StudentResearchProjectFormManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	//This method is used for get List on l page
	public static ArrayList<StudentResearchProjectFormModel> getList(String proj_id,String x_location,String x_ICAR_USID,String x_ddo,String x_stu_name,String user_status,String user_id){
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String query = "";
		ArrayList al = new ArrayList();
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
			query = "select LOCATION_CODE ,DDO_ID ,proj_id ,fin_year,status,LOCATION_CODE ,DDO_ID ,stu_name ,ICAR_USID ,cau_regno ,"
					+ "course ,discipline ,research_thrust_area ,research_sub_thrust_area ,proj_type ,proj_title ,"
					+ "objective ,guide_name ,external_guide_name  "
					+ ",(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='student_research_project_form'  "
					+ "and reference_id=rmf.proj_id order by CREATED desc limit 1) as upld from student_research_project_form rmf WHERE 1=1 ";
			if(!proj_id.trim().equals("")) {
				query+=" AND rmf.proj_id= '"+proj_id+"' ";
			}else if(!x_location.trim().equals("") ) {
				query+=" AND LOCATION_CODE ='" + x_location+"' ";
			}if(!General.checknull(x_ddo).equals("")){		
				query += " and DDO_ID='"+x_ddo+"'";
				
			}if(!General.checknull(user_status).equals("A")){		
				query += " and CREATED_BY='"+user_id+"'";
				
			}if(!General.checknull(x_ICAR_USID).equals("")){		
				query += " and ICAR_USID='"+x_ICAR_USID+"'";
				
			}if(!General.checknull(x_stu_name).equals("")){		
				query += " and stu_name='"+x_stu_name+"'";
				
			}
			psmt = conn.prepareStatement(query);
			//System.out.println("query:|getlist |"+psmt);
			rst = psmt.executeQuery();
			while (rst.next()) {
				StudentResearchProjectFormModel model = new StudentResearchProjectFormModel();
				
				for(int i=0; i<designationlocationarr.size(); i++){
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
				
				model.setM_id(General.checknull(rst.getString("proj_id")));
				model.setLOCATION_CODE(General.checknull(rst.getString("LOCATION_CODE")));
				model.setDDO_ID(General.checknull(rst.getString("DDO_ID")));
				model.setStu_name(General.checknull(rst.getString("stu_name")));
				model.setICAR_USID(General.checknull(rst.getString("ICAR_USID")));
				model.setCau_regno(General.checknull(rst.getString("cau_regno")));
				model.setCourse(General.checknull(rst.getString("course")));
				model.setDiscipline(General.checknull(rst.getString("discipline")));
				model.setResearch_thrust_area(General.checknull(rst.getString("research_thrust_area")));
				model.setResearch_sub_thrust_area(General.checknull(rst.getString("research_sub_thrust_area")));
				model.setProj_type(General.checknull(rst.getString("proj_type")));
				model.setProj_title(General.checknull(rst.getString("proj_title")));
				model.setObjective(General.checknull(rst.getString("objective")));
				model.setGuide_name(General.checknull(rst.getString("guide_name")));
				model.setExternal_guide_name(General.checknull(rst.getString("external_guide_name")));
				model.setStu_status(General.checknull(rst.getString("status")));
				model.setFin_yr(General.checknull(rst.getString("fin_year")));
				model.setUploaded_file(General.checknull(rst.getString("upld")));
				al.add(model);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("StudentResearchProjectFormManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("StudentResearchProjectFormManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	/*This method will be used to delete data form 
	file_attachment & rsrch_mou_moa_form from database
	as well as delete the whole folder for the given id*/
	public static StudentResearchProjectFormModel delete(String id,String fname) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "",str="";
		int count = 0,cntTable=0;
		StudentResearchProjectFormModel model=new StudentResearchProjectFormModel();
		try {
			
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			query = "delete from file_attachment where table_name='student_research_project_form'  AND reference_id='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			//System.out.println("id"+id);
			if(count > 0){	
				psmt=null;query="";
				query = "delete from student_research_project_form where proj_id='"+id+"'";
				psmt = conn.prepareStatement(query);
				cntTable = psmt.executeUpdate();
			}if(cntTable>0){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/STUFORM/"+id/*+"/"+fname*/;
				
				 Path directoryToDelete = Paths.get(directoryName);
		            Files.walkFileTree(directoryToDelete, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
		                @Override
		                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		                    Files.delete(file); // Delete the file
		                    return FileVisitResult.CONTINUE;
		                }

		                @Override
		                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		                    // Handle the failure to delete a file (optional)
		                    return FileVisitResult.CONTINUE;
		                }

		                @Override
		                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		                    Files.delete(dir); // Delete the directory
		                    return FileVisitResult.CONTINUE;
		                }
		            });
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
			l.fatal(Logging.logException("StudentResearchProjectFormManager[delete]", e.getMessage().toString()));
			System.out.println("Error in StudentResearchProjectFormManager[delete] : " + e.getMessage());
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
	
	/*This static method is use to update data into database.
	In this method passing 5 parameters(model,items,user_id,machine,mode) and will return StudentResearchProjectFormModel*/
	public static StudentResearchProjectFormModel Update(StudentResearchProjectFormModel model, FileItem items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0";
		int chk=0,i=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String mid=General.checknull(model.getM_id());
			
			qry="UPDATE student_research_project_form SET LOCATION_CODE=?,DDO_ID=?, stu_name=?, ICAR_USID=?, cau_regno=?, "
					+ "course=?, discipline=?,research_thrust_area=?,research_sub_thrust_area=?,"
					+ " proj_type=?, proj_title=?, objective=? ,guide_name=?,external_guide_name=?,fin_year=?,"
					+ "status=?,UPDATED_BY=?, UPDATED_MACHINE=?,UPDATED_DATE=now() where proj_id=?  ";
			
			psmt = conn.prepareStatement(qry);
			psmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
			psmt.setString(++i, General.checknull(model.getDDO_ID()));
			psmt.setString(++i, General.checknull(model.getStu_name()));
			psmt.setString(++i, General.checknull(model.getICAR_USID()));
			psmt.setString(++i, General.checknull(model.getCau_regno()));
			
			psmt.setString(++i, General.checknull(model.getCourse()));
			psmt.setString(++i, General.checknull(model.getDiscipline()));
			psmt.setString(++i, General.checknull(model.getResearch_thrust_area()));
			psmt.setString(++i, General.checknull(model.getResearch_sub_thrust_area()));
			
			psmt.setString(++i, General.checknull(model.getProj_type()));
			psmt.setString(++i, General.checknull(model.getProj_title()));
			psmt.setString(++i, General.checknull(model.getObjective()));
			psmt.setString(++i, General.checknull(model.getGuide_name()));
			psmt.setString(++i, General.checknull(model.getExternal_guide_name()));
			psmt.setString(++i, General.checknull(model.getFin_yr()));
			psmt.setString(++i, General.checknull(model.getStu_status()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			psmt.setString(++i, model.getM_id());
			
			int cnt = psmt.executeUpdate();
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
		l.fatal(Logging.logException("StudentResearchProjectFormManager[update]", e.getMessage().toString()));
		System.out.println("Error in StudentResearchProjectFormManager[update] : " + e.getMessage());
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