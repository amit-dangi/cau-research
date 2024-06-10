package com.sits.rsrch.research_activity.objective_achievement_details;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.JSONException;
import org.json.simple.JSONArray;

import com.sits.conn.DBConnection;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.general.ReadProps;

public class ObjectiveAchievementManager {
	static Logger log = Logger.getLogger("exceptionlog");
	//method to save record
	public static ObjectiveAchievementModel save(ObjectiveAchievementModel Model,String user_id,String ip,List<FileItem> fs) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String cInsert="";
		int updtCnt=0,i=0;
		String attachid="";
		String str="";
		try 
		{
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			pstmt=null;
			rst=null;
			cInsert =" INSERT INTO rsrch_objective_achievement_details ( ";
			cInsert+=" PS_MID,from_date, to_date,achievement_details,is_uploaded_doc,CREATED_DATE,CREATED_BY, CREATED_MACHINE ";
			cInsert+=" ) VALUES (?,str_to_date(?,'%d/%m/%Y'),str_to_date(?,'%d/%m/%Y'),?,?,now(), ?, ?";
			cInsert+=" )";
		//	pstmt = conn.prepareStatement(cInsert);
			pstmt = conn.prepareStatement(cInsert, pstmt.RETURN_GENERATED_KEYS);
			pstmt.setString(++i, (General.checknull( Model.getRsrch_proposal())));
			pstmt.setString(++i, (General.checknull(Model.getFrom_date())));
			pstmt.setString(++i, (General.checknull( Model.getTo_date())));
			pstmt.setString(++i, General.checknull(Model.getAchievement_det()));
			pstmt.setString(++i, General.checknull(Model.getIs_doc_uploaded()));
			pstmt.setString(++i, user_id.trim());
			pstmt.setString(++i, ip.trim());
			updtCnt = pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
		if (rs.next()) {
				attachid = rs.getString(1);
			}
		//	 System.out.println("pstmt manager"+pstmt);
			 if(updtCnt>0){
			  Iterator<FileItem> iter = fs.iterator(); 
          while (iter.hasNext()) {
          	FileItem fileItem = iter.next();
             		saveDoc(fileItem,attachid,user_id,ip);
          }
        				conn.commit();
        				Model.setErrMsg("Record Saved Successfully");
        				Model.setValid(true);
        
              	}
		else {
				conn.rollback();
				Model.setErrMsg(ApplicationConstants.FAIL);
				Model.setValid(false);					
			}	
         
		}catch (Exception e) {
			str=e.getMessage().toString() ;
			if(str.indexOf("Duplicate entry") != -1)
				str = ApplicationConstants.UNIQUE_CONSTRAINT;
			else 
				str = ApplicationConstants.EXCEPTION_MESSAGE;
			Model.setErrMsg(str);
			Model.setValid(false);
			log.fatal(Logging.logException("ObjectiveAchievementManager[save]", e.getMessage().toString()));
			System.out.println("Error in ObjectiveAchievementManager[save] : "+e.getMessage());			
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(rst != null) rst.close();
				
				conn.close();
				str ="";
			} catch (Exception e1) {
				e1.printStackTrace();
			}						
		}
		return Model;
	}
	
	//method to save attachment in directory
	public static boolean saveDoc(FileItem fileItem,String id,String user_id,String ip){
		Logger log = Logger.getLogger("exceptionlog");
	  	java.io.File file;
		String str="";
	  	//java.io.File file1;
	  	
	  	try{
	 
	  		String attachid=saveFileattachment(fileItem,id,user_id,ip);
	  		if(!attachid.equals("")){
	  		
			String directoryName=ReadProps.getkeyValue("document.path", "sitsResource")+"/"+"RESEARCH"+"/"+"PROJ_DETAIL"+"/"+id+"";
			File directory = new File(directoryName);
			
			if (!directory.isDirectory()){
				
	 			directory.mkdirs();
	 		}
		    
			file = new File(directoryName+"/"+"" +fileItem.getName());
			fileItem.write(file);
	  		}
	  	}catch(Exception e){
	  		str=e.getMessage().toString() ;
			if(str.indexOf("Duplicate entry") != -1)
				str = ApplicationConstants.UNIQUE_CONSTRAINT;
	  		System.out.println("Error in ObjectiveAchievementManager[saveDoc] : "+e.getMessage());
	  		log.fatal(Logging.logException("ObjectiveAchievementManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}
	
	public static ArrayList<ObjectiveAchievementModel> searchRecord(ObjectiveAchievementModel mModel,String user_id) {
		ArrayList<ObjectiveAchievementModel> list = new ArrayList<ObjectiveAchievementModel>();
		String cSql = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		try {
			conn = DBConnection.getConnection();
			cSql = "select OA_ID, PS_MID,achievement_details,date_format(from_date, '%d/%m/%Y') as from_date ,date_format(to_date, '%d/%m/%Y') as to_date ,"
					+ "(select PS_TITTLE_PROJ from rsrch_form1_mast where PS_MID= a.PS_MID) "
					+ "as rsrch_proposal,(SELECT file_name FROM file_attachment WHERE reference_id=OA_ID limit 1) "
					+ "as upl_doc from rsrch_objective_achievement_details a where 1=1 ";
			if(!user_id.equals("ADMIN")){
				cSql += "AND CREATED_BY='"+user_id+"'";
			}
			 if (!mModel.getRsrch_proposal_id().trim().equals("")) {
					cSql += " AND PS_MID = '"+General.checknull(mModel.getRsrch_proposal_id())+"'";
				}
			 if (!mModel.getFrom_date().equals("") && !mModel.getTo_date().equals("")) {
					cSql += " and from_date between str_to_date('"+mModel.getFrom_date()+"','%d/%m/%Y')  and str_to_date('"+mModel.getTo_date()+"','%d/%m/%Y')  ";
				}
			pstmt = conn.prepareStatement(cSql);
			rst = pstmt.executeQuery();
		//	System.out.println("pstmt   SEARCH======"+pstmt);
			if (rst.next() == false) {
			} else {
				do {
					ObjectiveAchievementModel mm = new ObjectiveAchievementModel();
					mm.setOa_id(General.checknull(rst.getString("OA_ID")));
					mm.setRsrch_proposal_id(General.checknull(rst.getString("PS_MID")));
					mm.setFrom_date(General.checknull(rst.getString("from_date")));
					mm.setTo_date(General.checknull(rst.getString("to_date")));
					mm.setRsrch_proposal(General.checknull(rst.getString("rsrch_proposal")));
					mm.setUpl_doc(General.checknull(rst.getString("upl_doc")));
					mm.setAchievement_det (General.checknull(rst.getString("achievement_details")));
					list.add(mm);
				} while (rst.next());
			}
		} catch (Exception e) {
			System.out.println("Error in ObjectiveAchievementManager[searchRecord] : " + e.getMessage());
			log.fatal(Logging.logException("ObjectiveAchievementManager[searchRecord]", e.getMessage().toString()));
		} finally {
			try {
				if (pstmt != null)
					pstmt = null;
				if (rst != null)
					rst = null;
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static ObjectiveAchievementModel viewRecordDetails(String oa_id) {
		String cSql = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		ObjectiveAchievementModel sm = new ObjectiveAchievementModel();
		try {
			conn = DBConnection.getConnection();
			cSql = "select PS_MID,date_format(from_date, '%d/%m/%Y') as from_date ,"
					+ "date_format(to_date, '%d/%m/%Y') as to_date,achievement_details,(select file_name from "
					+ "file_attachment where reference_id='"+oa_id+"')as attach from"
					+ " rsrch_objective_achievement_details where OA_ID='"+oa_id+"'";
			pstmt = conn.prepareStatement(cSql);
			rst = pstmt.executeQuery();
		//	System.out.println("pstmt view>>>>>>>>"+pstmt);
			if (rst.next() == false) {
			} else {
				do {
					sm.setRsrch_proposal_id(General.checknull(rst.getString("PS_MID")));
					sm.setFrom_date(General.checknull(rst.getString("from_date")));
					sm.setTo_date(General.checknull(rst.getString("to_date")));
					sm.setAchievement_det(General.checknull(rst.getString("achievement_details")));
					sm.setAttach_id(General.checknull(rst.getString("attach")));
				
				} while (rst.next());
			}
		} catch (Exception e) {
			System.out.println("Error in ObjectiveAchievementManager[viewRecordDetails] : " + e.getMessage());
			log.fatal(Logging.logException("ObjectiveAchievementManager[viewRecordDetails]", e.getMessage().toString()));
		} finally {
			try {
				if (pstmt != null)
					pstmt = null;
				if (rst != null)
					rst = null;
				if (conn != null)
					conn.close();
				} catch (Exception e)
				{
				e.printStackTrace();
				}
		}
		return sm;
	}

	//method to save attachment in database
	public static String saveFileattachment( FileItem fileItem,String id,String user_id,String machine){ 
		PreparedStatement psmt = null;
		String qry = "";
		String fileattachid="";
		int count=0;
		Connection conn = null;
		
		try{
			conn = DBConnection.getConnection();
			qry="INSERT INTO file_attachment ( file_name, table_name, reference_id, CREATED_BY, CREATED, MACHINE) VALUES "
			+ "( ? , 'rsrch_objective_achievement_details', ?, ?,now(), ?); ";
		
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, id);
			psmt.setString(3, user_id);
			psmt.setString(4, machine);
		//	System.out.println("saveFileattachment >>"+psmt);
			count= psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				fileattachid = rs.getString(1);
			}
			 
		}
		catch(Exception e)
		{
			fileattachid="";
			System.out.println("EXCEPTION IS CAUSED BY: ObjectiveAchievementManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			log.fatal(Logging.logException("ObjectiveAchievementManager [saveFileattachment]", e.toString()));
			return fileattachid;
		}
		return fileattachid;
	}

	//method to delete attachment
	public static String deletattchdata(String upload_id,String file_name) {
	String cSql="",msg="";
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	ResultSet rst = null;
	int count=0;
	try {
		conn = DBConnection.getConnection();
		conn.setAutoCommit(false);
		cSql="DELETE FROM  file_attachment where reference_id=?";
		pstmt = conn.prepareStatement(cSql);
		pstmt.setString(1, General.checknull(upload_id).trim()); 
		count = pstmt.executeUpdate();
		
		if(count>0)
		{
			String directoryName=ReadProps.getkeyValue("document.path", "sitsResource")+"RESEARCH"+"/"+"PROJ_DETAIL"+"/"+upload_id+"/"+file_name+"";
			//System.out.println("directoryName:::"+directoryName);
			File file = new File(directoryName);
			file.delete();
			msg=ApplicationConstants.DELETED;
			conn.commit();
		}		
		
	} catch (Exception e) {
		System.out.println("Error in ObjectiveAchievementManager[deletattchdata] : "+e.getMessage());
		log.fatal(Logging.logException("ObjectiveAchievementManager[deletattchdata]", e.getMessage().toString()));
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

	//method to update record in database
	public static ObjectiveAchievementModel updateRecord(ObjectiveAchievementModel iModel, String user_id, String ip,List<FileItem> fs) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String cInsert="";
		int updtCnt=0,i=0;
		String str=""; 
		FileItem fileItem = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			cInsert="UPDATE rsrch_objective_achievement_details SET  PS_MID=?,from_date=str_to_date(?,'%d/%m/%Y'),"
					+ "to_date=str_to_date(?,'%d/%m/%Y'),achievement_details=?,is_uploaded_doc=?,";
			cInsert += " UPDATED_BY=?, UPDATED_DATE=now(), UPDATED_MACHINE=?";
			cInsert += " WHERE OA_ID=?";
			pstmt = conn.prepareStatement(cInsert);
			
				
			pstmt.setString(++i,General.checknull(iModel.getRsrch_proposal()));
			pstmt.setString(++i,General.checknull( iModel.getFrom_date()));
			pstmt.setString(++i,General.checknull(iModel.getTo_date()));
			pstmt.setString(++i,General.checknull(iModel.getAchievement_det()));
			pstmt.setString(++i,General.checknull(iModel.getIs_doc_uploaded()));
		
			pstmt.setString(++i, user_id.trim());
			pstmt.setString(++i, ip.trim());
			pstmt.setString(++i, iModel.getOa_id());
		//	System.out.println("update query in mnger"+pstmt);
			
			updtCnt = pstmt.executeUpdate();
			if(updtCnt>0){
				 Iterator<FileItem> itr = fs.iterator(); 
				    while (itr.hasNext()){ 
				    	 fileItem = itr.next();
			    saveDoc(fileItem,iModel.getOa_id(),user_id,ip);
				    } 
				    conn.commit();
					iModel.setErrMsg("Record Updated Successfully");
					iModel.setValid(true);
				}
					}
			 catch (Exception e) {
			str=e.getMessage().toString() ;
			if(str.indexOf("Duplicate entry") != -1)
				str = ApplicationConstants.UNIQUE_CONSTRAINT;
			else 
				str = ApplicationConstants.EXCEPTION_MESSAGE;
			iModel.setErrMsg(str);
			iModel.setValid(false);
			log.fatal(Logging.logException("ObjectiveAchievementManager[updateRecord]", e.getMessage().toString()));
			System.out.println("Error in ObjectiveAchievementManager[updateRecord] : "+e.getMessage());			
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(rst != null) rst.close();
				conn.close();str="";
			} catch (Exception e1) {
				e1.printStackTrace();
			}						
		}
		return iModel;		
	}
	public static ObjectiveAchievementModel deleteRecord(String id,String upl_doc) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String cInsert="",cDelete="";
		int updtCnt=0,updtCnt1=0;
		String str ="";
		ObjectiveAchievementModel iModel = new ObjectiveAchievementModel();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			cInsert="delete from rsrch_objective_achievement_details ";
			cInsert+="WHERE OA_ID=?";
			pstmt = conn.prepareStatement(cInsert);
			pstmt.setString(1, id.trim());
			
			updtCnt = pstmt.executeUpdate();
			pstmt=null;
			 if(!upl_doc.equals("")){
			cDelete="delete from file_attachment where reference_id=? ";
			pstmt = conn.prepareStatement(cDelete);
			pstmt.setString(1, id.trim());
			updtCnt1 = pstmt.executeUpdate();
			 }
			if(updtCnt1>0) {
				String directoryName=ReadProps.getkeyValue("document.path", "sitsResource")+"RESEARCH"+"/"+"PROJ_DETAIL"+"/"+id+"/"+upl_doc;
				File file = new File(directoryName);
				if(file.exists()){
				file.delete();
			}
			}if(updtCnt>0 || updtCnt1>0){
				conn.commit();
				iModel.setErrMsg("Record Deleted Successfully");
				iModel.setValid(true);
			} else {
				conn.rollback();
				iModel.setErrMsg(ApplicationConstants.FAIL);
				iModel.setValid(false);
			}
		//	}
		} catch (Exception e) {
			str=e.getMessage().toString() ;
			if(str.indexOf("foreign key constraint fails") != -1)
				str = ApplicationConstants.DELETE_FORIEGN_KEY;
			else 
				str = ApplicationConstants.EXCEPTION_MESSAGE;
			iModel.setErrMsg(str);
			iModel.setValid(false);
			log.fatal(Logging.logException("ObjectiveAchievementManager[deleteRecord]", e.getMessage().toString()));
			System.out.println("Error in ObjectiveAchievementManager[deleteRecord] : "+e.getMessage());		
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(rst != null) rst.close();
				conn.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}						
		}
		return iModel;		
	}	

	//method to check already record exists between applied dates
	public static boolean getRecordExist(ObjectiveAchievementModel iModel) {
		String cSql="";
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean flag=false;
		ResultSet rst = null;
		try {
			conn = DBConnection.getConnection();
			cSql="select * from rsrch_objective_achievement_details"
					+ " where PS_MID=? and '"+General.formatDate(iModel.getFrom_date()).trim()+"'"
					+ " between from_date and to_date";
			pstmt = conn.prepareStatement(cSql);
			pstmt.setString(1, General.checknull(iModel.getRsrch_proposal()).trim()); 
			rst = pstmt.executeQuery();
		//	System.out.println("pstmt record exist-------------"+pstmt);
			while (rst.next()){
				flag=true;
			}		
			
		} catch (Exception e) {
			System.out.println("Error in ObjectiveAchievementManager[getRecordExist] : "+e.getMessage());
			log.fatal(Logging.logException("ObjectiveAchievementManager[getRecordExist]", e.getMessage().toString()));
		} finally {
			try {
				if(pstmt != null) pstmt = null;
				if(rst != null) rst = null;
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return flag;
	}
}
