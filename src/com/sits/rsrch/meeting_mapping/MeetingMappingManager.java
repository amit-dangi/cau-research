package com.sits.rsrch.meeting_mapping;

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
import java.util.Iterator;
import java.util.List;

public class MeetingMappingManager {
	static Logger l = Logger.getLogger("exceptionlog CAU Research Patent"); 
	/* MouMoaform added by Amit Dangi 
	 * This static MeetingMappingModel type method is used for save  data .
	 * In this method suppose to pass 5 parameters(model,items,user_id,machine,mode) 
	 * and will return MeetingMappingModel object */
	public static MeetingMappingModel save(MeetingMappingModel model,  List<FileItem> items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0",mid="";
		int chk=0,i=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			qry="INSERT INTO rsrch_meeting_mapping (LOCATION_CODE,DDO_ID,meeting_type_id,others,fin_yr, meeting_date,"
					+ "CREATED_BY, CREATED_MACHINE, CREATED_DATE) VALUES (?,?,?, ?, ?,  str_to_date(?,'%d/%m/%Y'), "
					+ "?, ?,now())";			
		
			psmt = conn.prepareStatement(qry,psmt.RETURN_GENERATED_KEYS);
			
			psmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
			psmt.setString(++i, General.checknull(model.getDDO_ID()));
			psmt.setString(++i, General.checknull(model.getMeeting_type_id()));
			psmt.setString(++i, General.checknull(model.getOthers()));
			psmt.setString(++i, General.checknull(model.getFin_yr()));
			psmt.setString(++i, General.checknull(model.getMeeting_date()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			int cnt = psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				mid = rs.getString(1);
			}
			if (cnt > 0){
				if(items!=null){
					Iterator<FileItem> iter = items.iterator();
						while (iter.hasNext()) {
							FileItem fileItem = iter.next();
							saveDoc(machine, mid, user_id,conn, fileItem);	
												}
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
			l.fatal(Logging.logException("MeetingMappingManager[save]", e.getMessage().toString()));
			System.out.println("Error in MeetingMappingManager[save] : " + e.getMessage());
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
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/MEETING/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in MeetingMappingManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("MeetingMappingManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}
	
	//This method is used for save attachment name in file_attachment table
	public static synchronized String saveFileattachment(String machine, FileItem fileItem, String id, String userid, Connection conn){ 
		PreparedStatement psmt = null;
		String qry = "";
		String attachid="",file_type="";
		int count=0;
		try{
			qry="INSERT INTO file_attachment ( file_name, file_type, table_name, reference_id, CREATED_BY, CREATED, MACHINE) VALUES "
			+ "( ? , ?, 'rsrch_meeting_mapping', ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			 if(fileItem.getFieldName().equals("upload_doc")){
					file_type="upload_agenda";
				}else if(fileItem.getFieldName().equals("upload_doc2")){
					file_type="upload_Proceedings";
				}
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, file_type);
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
			System.out.println("EXCEPTION IS CAUSED BY: MeetingMappingManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("MeetingMappingManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
	//This method is used for get List on l page
	public static ArrayList<MeetingMappingModel> getList(String m_id,String x_location,String x_meeting_type_id,String x_ddo){
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
			query = "select distinct m_id,LOCATION_CODE ,DDO_ID , (select TYPE from rsrch_meeting_type_mast where TYPE_ID=meeting_type_id) as meeting_type,"
					+ "meeting_type_id,others,fin_yr,date_format(meeting_date,'%d/%m/%Y') as meeting_date "
					+ ",(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_meeting_mapping' and file_type='upload_agenda' "
					+ "and reference_id=rmf.m_id order by CREATED desc limit 1) as upld,(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where "
					+ "table_name='rsrch_meeting_mapping' and file_type='upload_Proceedings' "
					+ "and reference_id=rmf.m_id order by CREATED desc limit 1) as upld2 from rsrch_meeting_mapping rmf WHERE 1=1 ";
			if(!m_id.trim().equals("")) {
				query+=" AND rmf.m_id= '"+m_id+"' ";
			} if(!x_location.trim().equals("") ) {
				query+=" AND LOCATION_CODE ='" + x_location+"' ";
			} if(!x_meeting_type_id.trim().equals("") ) {
				query+=" AND meeting_type_id ='" + x_meeting_type_id+"' ";
			}if(!x_ddo.trim().equals("") ) {
				query+=" AND DDO_ID ='" + x_ddo+"' ";
			}
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			while (rst.next()) {
				MeetingMappingModel model = new MeetingMappingModel();
				
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
				
				model.setM_id(General.checknull(rst.getString("m_id")));
				model.setLOCATION_CODE(General.checknull(rst.getString("LOCATION_CODE")));
				model.setDDO_ID(General.checknull(rst.getString("DDO_ID")));
				model.setMeeting_type(General.checknull(rst.getString("meeting_type")));
				model.setMeeting_type_id(General.checknull(rst.getString("meeting_type_id")));
				model.setOthers(General.checknull(rst.getString("others")));
				model.setFin_yr(General.checknull(rst.getString("fin_yr")));
				model.setMeeting_date(General.checknull(rst.getString("meeting_date")));
				model.setUploaded_file(General.checknull(rst.getString("upld")));
				model.setUploaded_proceeding(General.checknull(rst.getString("upld2")));
				al.add(model);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("MeetingMappingManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("MeetingMappingManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	/*This method will be used to delete data form 
	file_attachment & rsrch_mou_moa_form from database
	as well as delete the whole folder for the given id*/
	public static MeetingMappingModel delete(String id,String fname,String user_id,String ip) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "",str="";
		int count = 0,cntTable=0;
		MeetingMappingModel model=new MeetingMappingModel();
		try {
			
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			conn = General.updtDeletedData("rsrch_meeting_mapping", "m_id", "", "", "", "","",
		    		id, "", "", "","", "", ip, user_id,"",conn);
			if (conn != null) {
			query = "delete from file_attachment where table_name='rsrch_meeting_mapping' AND reference_id='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			
			}if(count > 0){	
				psmt=null;query="";
				query = "delete from rsrch_meeting_mapping where m_id='"+id+"'";
				psmt = conn.prepareStatement(query);
				cntTable = psmt.executeUpdate();
			}if(cntTable>0){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/MEETING/"+id/*+"/"+fname*/;
				
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
			l.fatal(Logging.logException("MeetingMappingManager[delete]", e.getMessage().toString()));
			System.out.println("Error in MeetingMappingManager[delete] : " + e.getMessage());
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
	In this method passing 5 parameters(model,items,user_id,machine,mode) and will return MeetingMappingModel*/
	public static MeetingMappingModel Update(MeetingMappingModel model, List<FileItem> items, String user_id, String machine, String mode) {
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
			
			qry="UPDATE rsrch_meeting_mapping SET LOCATION_CODE=?,DDO_ID=?, meeting_type_id=?, others=?, fin_yr=?, "
					+ "meeting_date=str_to_date(?,'%d/%m/%Y'),"
					+ "UPDATED_BY=?, UPDATED_MACHINE=?,UPDATED_DATE=now() where m_id=?  ";
			
			psmt = conn.prepareStatement(qry);
			psmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
			psmt.setString(++i, General.checknull(model.getDDO_ID()));
			psmt.setString(++i, General.checknull(model.getMeeting_type_id()));
			psmt.setString(++i, General.checknull(model.getOthers()));
			psmt.setString(++i, General.checknull(model.getFin_yr()));
			psmt.setString(++i, General.checknull(model.getMeeting_date()));
			
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			psmt.setString(++i, model.getM_id());
			
			int cnt = psmt.executeUpdate();
			if (cnt > 0){
				if(items!=null){
					Iterator<FileItem> iter = items.iterator();
						while (iter.hasNext()) {
							FileItem fileItem = iter.next();
							saveDoc(machine, mid, user_id,conn, fileItem);	
								}
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
		l.fatal(Logging.logException("MeetingMappingManager[update]", e.getMessage().toString()));
		System.out.println("Error in MeetingMappingManager[update] : " + e.getMessage());
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