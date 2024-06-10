package com.sits.rsrch.patent_mou_moa_form;

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

public class MouMoaformManager {
	static Logger l = Logger.getLogger("exceptionlog CAU Research Patent"); 
	/* MouMoaform added by Amit Dangi 
	 * This static MouMoaformModel type method is used for save  data .
	 * In this method suppose to pass 5 parameters(model,items,user_id,machine,mode) 
	 * and will return MouMoaformModel object */
	public static MouMoaformModel save(MouMoaformModel model,  FileItem items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0",mid="";
		int chk=0,i=0;
		try {
			System.out.println("in manager");
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			qry="INSERT INTO rsrch_mou_moa_form (LOCATION_CODE,DDO_ID,mou_type,inst_name,coll_type, coll_area, signed_on, validity_period,"
					+ " valid_upto, signed_by, pi_name, m_status,"
					+ "CREATED_BY, CREATED_MACHINE, CREATED_DATE) VALUES (?,?,?, ?, ?, ?, str_to_date(?,'%d/%m/%Y'), ?, str_to_date(?,'%d/%m/%Y'), "
					+ "?, ?,?,?,?,now())";			
		
			psmt = conn.prepareStatement(qry,psmt.RETURN_GENERATED_KEYS);
			
			psmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
			psmt.setString(++i, General.checknull(model.getDDO_ID()));
			psmt.setString(++i, General.checknull(model.getMou_type()));
			psmt.setString(++i, General.checknull(model.getInst_name()));
			psmt.setString(++i, General.checknull(model.getColl_type()));
			psmt.setString(++i, General.checknull(model.getColl_area()));
			
			psmt.setString(++i, General.checknull(model.getSigned_on()));
			psmt.setString(++i, General.checknull(model.getValidity_period()));
			psmt.setString(++i, General.checknull(model.getValid_upto()));
			
			psmt.setString(++i, General.checknull(model.getSigned_by()));
			psmt.setString(++i, General.checknull(model.getPi_name()));
			psmt.setString(++i, General.checknull(model.getM_status()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
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
			l.fatal(Logging.logException("MouMoaformManager[save]", e.getMessage().toString()));
			System.out.println("Error in MouMoaformManager[save] : " + e.getMessage());
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
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/MOU/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in MouMoaformManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("MouMoaformManager[saveDoc]", e.toString()));
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
			+ "( ? , ?, 'rsrch_mou_moa_form', ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, "mou_moa_document");
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
			System.out.println("EXCEPTION IS CAUSED BY: MouMoaformManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("MouMoaformManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	//This method is used for get List on l page
	public static ArrayList<MouMoaformModel> getList(String m_id,String x_location,String x_mou_type){
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
			query = "select m_id,LOCATION_CODE ,DDO_ID ,mou_type ,inst_name ,coll_type ,coll_area ,date_format(signed_on,'%d/%m/%Y') as signed_on,"
					+ "validity_period ,date_format(valid_upto,'%d/%m/%Y') as valid_upto ,signed_by ,pi_name ,m_status "
					+ ",(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_mou_moa_form' and file_type='mou_moa_document' "
					+ "and reference_id=rmf.m_id order by CREATED desc limit 1) as upld from rsrch_mou_moa_form rmf WHERE 1=1 ";
			if(!m_id.trim().equals("")) {
				query+=" AND rmf.m_id= '"+m_id+"' ";
			}else if(!x_location.trim().equals("") ) {
				query+=" AND LOCATION_CODE ='" + x_location+"' ";
			}else if(!x_mou_type.trim().equals("")) {
				query+=" AND mou_type ='" + x_mou_type+"' ";
			}
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			System.out.println("psmt11----"+psmt);
			while (rst.next()) {
				MouMoaformModel model = new MouMoaformModel();
				
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
				model.setMou_type(General.checknull(rst.getString("mou_type")));
				model.setInst_name(General.checknull(rst.getString("inst_name")));
				model.setColl_type(General.checknull(rst.getString("coll_type")));
				model.setColl_area(General.checknull(rst.getString("coll_area")));
				model.setSigned_on(General.checknull(rst.getString("signed_on")));
				model.setValidity_period(General.checknull(rst.getString("validity_period")));
				model.setValid_upto(General.checknull(rst.getString("valid_upto")));
				model.setSigned_by(General.checknull(rst.getString("signed_by")));
				model.setPi_name(General.checknull(rst.getString("pi_name")));
				model.setM_status(General.checknull(rst.getString("m_status")));
				model.setUploaded_file(General.checknull(rst.getString("upld")));
				al.add(model);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("MouMoaformManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("MouMoaformManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	/*This method will be used to delete data form 
	file_attachment & rsrch_mou_moa_form from database
	as well as delete the whole folder for the given id*/
	public static MouMoaformModel delete(String id,String fname,String user_id,String ip) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "",str="";
		int count = 0,cntTable=0;
		MouMoaformModel model=new MouMoaformModel();
		try {
			
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			conn = General.updtDeletedData("rsrch_mou_moa_form", "m_id", "", "", "", "","",
		    		id, "", "", "","", "", ip, user_id,"",conn);
			if (conn != null) {
			query = "delete from file_attachment where table_name='rsrch_mou_moa_form' and file_type='mou_moa_document' AND reference_id='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			}
			if(count > 0){	
				psmt=null;query="";
				query = "delete from rsrch_mou_moa_form where m_id='"+id+"'";
				psmt = conn.prepareStatement(query);
				cntTable = psmt.executeUpdate();
			}if(cntTable>0){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/MOU/"+id/*+"/"+fname*/;
				
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
			l.fatal(Logging.logException("MouMoaformManager[delete]", e.getMessage().toString()));
			System.out.println("Error in MouMoaformManager[delete] : " + e.getMessage());
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
	In this method passing 5 parameters(model,items,user_id,machine,mode) and will return MouMoaformModel*/
	public static MouMoaformModel Update(MouMoaformModel model, FileItem items, String user_id, String machine, String mode) {
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
			
			qry="UPDATE rsrch_mou_moa_form SET LOCATION_CODE=?,DDO_ID=?, mou_type=?, inst_name=?, coll_type=?, "
					+ "coll_area=?, signed_on=str_to_date(?,'%d/%m/%Y'),validity_period=?,valid_upto=str_to_date(?,'%d/%m/%Y'),"
					+ " signed_by=?, pi_name=?, m_status=? ,UPDATED_BY=?, UPDATED_MACHINE=?,UPDATED_DATE=now() where m_id=?  ";
			
			psmt = conn.prepareStatement(qry);
			psmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
			psmt.setString(++i, General.checknull(model.getDDO_ID()));
			psmt.setString(++i, General.checknull(model.getMou_type()));
			psmt.setString(++i, General.checknull(model.getInst_name()));
			psmt.setString(++i, General.checknull(model.getColl_type()));
			psmt.setString(++i, General.checknull(model.getColl_area()));
			
			psmt.setString(++i, General.checknull(model.getSigned_on()));
			psmt.setString(++i, General.checknull(model.getValidity_period()));
			psmt.setString(++i, General.checknull(model.getValid_upto()));
			
			psmt.setString(++i, General.checknull(model.getSigned_by()));
			psmt.setString(++i, General.checknull(model.getPi_name()));
			psmt.setString(++i, General.checknull(model.getM_status()));
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
		l.fatal(Logging.logException("MouMoaformManager[update]", e.getMessage().toString()));
		System.out.println("Error in MouMoaformManager[update] : " + e.getMessage());
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