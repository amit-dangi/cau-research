package com.sits.patent.species_breed_variety;

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

public class SpeciesBreedVarietyManager {
	static Logger l = Logger.getLogger("exceptionlog CAU Research Patent"); 
	/* SpeciesBreedVarietyManager added by Amit Dangi 
	 * This static SpeciesBreedVarietyModel type method is used for save  data .
	 * In this method suppose to pass 5 parameters(model,items,user_id,machine,mode) 
	 * and will return MeetingMappingModel object */
	public static SpeciesBreedVarietyModel save(SpeciesBreedVarietyModel model,  List<FileItem> items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0",sid="";
		int chk=0,i=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			qry="INSERT INTO rsrch_species_breed_variety_mast (LOCATION_CODE,DDO_ID,status,fn_agency,accession_number, relese_date,"
					+ "CREATED_BY, CREATED_MACHINE, CREATED_DATE) VALUES (?,?,?,?, ?,  str_to_date(?,'%d/%m/%Y'), "
					+ "?, ?,now())";			
		
			psmt = conn.prepareStatement(qry,psmt.RETURN_GENERATED_KEYS);
			
			psmt.setString(++i, General.checknull(model.getLocation()));
			psmt.setString(++i, General.checknull(model.getDdo()));
			psmt.setString(++i, General.checknull(model.getSts()));
			psmt.setString(++i, General.checknull(model.getFn_agency()));
			psmt.setString(++i, General.checknull(model.getRelsing_no()));
		//	psmt.setString(++i, General.checknull(model.getFin_yr()));
			psmt.setString(++i, General.checknull(model.getRel_date()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			int cnt = psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				sid = rs.getString(1);
			}
			if (cnt > 0){
				if(items!=null){
					Iterator<FileItem> iter = items.iterator();
						while (iter.hasNext()) {
							FileItem fileItem = iter.next();
							saveDoc(machine,"", sid, user_id,conn, fileItem);	
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
			l.fatal(Logging.logException("SpeciesBreedVarietyManager[save]", e.getMessage().toString()));
			System.out.println("Error in SpeciesBreedVarietyManager[save] : " + e.getMessage());
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
	public static boolean saveDoc(String machine,String upl_id, String id, String userid,Connection conn, FileItem fileItem){		
		java.io.File file;
		try{
			String attachid=saveFileattachment(machine, fileItem, id, userid, conn);
			deletePreviousattchments(upl_id,conn);
			if(!attachid.equals("")){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/SPECIES/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in SpeciesBreedVarietyManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("SpeciesBreedVarietyManager[saveDoc]", e.toString()));
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
			+ "( ? , ?, 'rsrch_species_breed_variety_mast', ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			 if(fileItem.getFieldName().equals("upload_doc")){
					file_type="upload_agenda";
				}/*else if(fileItem.getFieldName().equals("upload_doc2")){
					file_type="upload_Proceedings";
				}*/
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
			System.out.println("EXCEPTION IS CAUSED BY: SpeciesBreedVarietyManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("SpeciesBreedVarietyManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
	//This method is used for get List on l page
	public static ArrayList<SpeciesBreedVarietyModel> getList(String s_id,String x_location,String x_ddo){
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
			query = "select distinct s_id,LOCATION_CODE ,DDO_ID ,status,"
					+ "fn_agency,accession_number,date_format(relese_date,'%d/%m/%Y') as relese_date "
					+ ",(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_species_breed_variety_mast' and file_type='upload_agenda' "
					+ "and reference_id=rmf.s_id order by CREATED desc limit 1) as upld from rsrch_species_breed_variety_mast rmf WHERE 1=1 ";
			
			if(!s_id.trim().equals("")) {
				query+=" AND rmf.s_id= '"+s_id+"' ";
			} if(!x_location.trim().equals("") ) {
				query+=" AND LOCATION_CODE ='" + x_location+"' ";
			}if(!x_ddo.trim().equals("") ) {
				query+=" AND DDO_ID ='" + x_ddo+"' ";
			}
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			while (rst.next()) {
				SpeciesBreedVarietyModel model = new SpeciesBreedVarietyModel();
				
				for(int i=0; i<designationlocationarr.size(); i++){
					JSONObject jsn=	(JSONObject) designationlocationarr.get(i);
					if(jsn.get("id").equals(rst.getString("LOCATION_CODE")))
					{
						model.setLocation_id( General.checknull(jsn.get("desc").toString()));
					}
				}
				for(int i=0; i<designationDtoarr.size(); i++){
					JSONObject jsn=	(JSONObject) designationDtoarr.get(i);
					if(jsn.get("id").equals(rst.getString("ddo_id")))
					{
						model.setDdo_id( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				model.setS_id(General.checknull(rst.getString("s_id")));
				model.setLocation(General.checknull(rst.getString("LOCATION_CODE")));
				model.setDdo(General.checknull(rst.getString("DDO_ID")));
				model.setSts(General.checknull(rst.getString("status")));
				model.setFn_agency(General.checknull(rst.getString("fn_agency")));
				model.setRelsing_no(General.checknull(rst.getString("accession_number")));
				model.setRel_date(General.checknull(rst.getString("relese_date")));
				model.setUploaded_file(General.checknull(rst.getString("upld")));
				al.add(model);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("SpeciesBreedVarietyManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("SpeciesBreedVarietyManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	/*This method will be used to delete data form 
	file_attachment & rsrch_mou_moa_form from database
	as well as delete the whole folder for the given id*/
	public static SpeciesBreedVarietyModel delete(String id,String fname) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "",str="";
		int count = 0,cntTable=0;
		SpeciesBreedVarietyModel model=new SpeciesBreedVarietyModel();
		try {
			
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			query = "delete from file_attachment where table_name='rsrch_species_breed_variety_mast' AND reference_id='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			
			if(count > 0){	
				psmt=null;query="";
				query = "delete from rsrch_species_breed_variety_mast where s_id='"+id+"'";
				psmt = conn.prepareStatement(query);
				cntTable = psmt.executeUpdate();
			}if(cntTable>0){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/SPECIES/"+id/*+"/"+fname*/;
				
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
			l.fatal(Logging.logException("SpeciesBreedVarietyManager[delete]", e.getMessage().toString()));
			System.out.println("Error in SpeciesBreedVarietyManager[delete] : " + e.getMessage());
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
	public static SpeciesBreedVarietyModel Update(SpeciesBreedVarietyModel model,String upl_id, List<FileItem> items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0";
		int chk=0,i=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String sid=General.checknull(model.getS_id());
			
			qry="UPDATE rsrch_species_breed_variety_mast SET LOCATION_CODE=?,DDO_ID=?,status=?, fn_agency=?, accession_number=?, "
					+ "relese_date=str_to_date(?,'%d/%m/%Y'),"
					+ "UPDATED_BY=?, UPDATED_MACHINE=?,UPDATED_DATE=now() where s_id=? ";
			
			psmt = conn.prepareStatement(qry);
			psmt.setString(++i, General.checknull(model.getLocation()));
			psmt.setString(++i, General.checknull(model.getDdo()));
			psmt.setString(++i, General.checknull(model.getSts()));
			psmt.setString(++i, General.checknull(model.getFn_agency()));
			psmt.setString(++i, General.checknull(model.getRelsing_no()));
			psmt.setString(++i, General.checknull(model.getRel_date()));
			
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			psmt.setString(++i, model.getS_id());
			int cnt = psmt.executeUpdate();
			if (cnt > 0){
				if(items!=null){
					
					Iterator<FileItem> iter = items.iterator();
						while (iter.hasNext()) {
							FileItem fileItem = iter.next();
							saveDoc(machine,upl_id, sid, user_id,conn, fileItem);	
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
		l.fatal(Logging.logException("SpeciesBreedVarietyManager[update]", e.getMessage().toString()));
		System.out.println("Error in SpeciesBreedVarietyManager[update] : " + e.getMessage());
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
	/*This static method will override the Previous attchments from table
	as well as delete attachment from the directory*/
	public static String deletePreviousattchments(String id,Connection conn) {
		l = Logger.getLogger("exceptionlog");
		String cSql="",msg="";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int count=0;
		try {
			cSql ="delete from file_attachment where file_attachment_id=?";
			pstmt = conn.prepareStatement(cSql);
			pstmt.setString(1, id.trim());
			count = pstmt.executeUpdate();
			if(count>0){
				msg="File Deleted Successfully";
			}		
		} catch (Exception e) {
			System.out.println("Error in SpeciesBreedVarietyManager[deletePreviousattchments] : "+e.getMessage());
			l.fatal(Logging.logException("SpeciesBreedVarietyManager[deletePreviousattchments]", e.getMessage().toString()));
		} finally {
			try {
				if(pstmt != null) pstmt = null;
				if(rst != null) rst = null;
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return msg;
	}	
	
	}