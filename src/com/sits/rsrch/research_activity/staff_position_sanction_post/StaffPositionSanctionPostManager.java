/**
 * @ AUTHOR Amit Dangi
 */
package com.sits.rsrch.research_activity.staff_position_sanction_post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
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
import java.io.File;
import org.apache.commons.fileupload.FileItem;
import com.sits.general.ReadProps;

public class StaffPositionSanctionPostManager {
	static Logger l = Logger.getLogger("exceptionlog research activity StaffPositionSanctionPostManager");

	/*Static Method to get the Target & Achievement saved details if any or
	get the selected proposal details from Project Proposal Submission form as per proposal id
	Return type is object Json*/
	public static JSONObject getSanctionPostDetail(String p_id) {
        PreparedStatement psmt = null,psmt2 = null;
        Connection conn=null;
        ResultSet rst = null,rst2 = null;
        String query="";
        JSONObject objectJson=new JSONObject();        
        JSONArray jsonArray = new JSONArray();
	         try {
				 
	        	conn = DBConnection.getConnection();
	        	query="SELECT fm.PS_MID,PS_TITTLE_PROJ ,fm.PS_PRINCIPAL,pm.sp_id, pm.co_pi_count, pm.scientist_count, pm.ra_count, pm.srf_count, pm.jrf_count, " 
        			+"pm.tech_asisstant_count, pm.field_asisstant_count, pm.lab_attendant_count, pm.fieldman_count, pm.yp1_count, pm.yp2_count, pm.others_count "
        			+"FROM rsrch_form1_mast fm left join rsrch_sanction_post_mast pm "
        			+"on fm.PS_MID=pm.PS_MID  WHERE is_form_submittd='Y' and fm.PS_MID = '"+General.checknull(p_id)+"'";
	 			psmt = conn.prepareStatement(query);
	 			System.out.println("getSanctionPostDetail psmt||"+psmt);
	 			rst = psmt.executeQuery();
	 			
	 			while (rst.next()) {
	 				JSONObject json= new JSONObject();
	 				
	 				//Getting submission form needed detail
 					 json.put("PS_MID",General.checknull(rst.getString("PS_MID")));
	        		 json.put("PS_TITTLE_PROJ",General.checknull(rst.getString("PS_TITTLE_PROJ")));
					 json.put("PS_PRINCIPAL",General.checknull(rst.getString("PS_PRINCIPAL")));
					 
					 
					//puting rsrch_sanction_post_mast json
					 json.put("sp_id",General.checknull(rst.getString("sp_id")));
					 json.put("co_pi_count",General.checknull(rst.getString("co_pi_count")));
					 json.put("scientist_count",General.checknull(rst.getString("scientist_count")));
					 json.put("ra_count",General.checknull(rst.getString("ra_count")));
					 json.put("srf_count",General.checknull(rst.getString("srf_count")));
					 json.put("jrf_count",General.checknull(rst.getString("jrf_count")));
					 json.put("tech_asisstant_count",General.checknull(rst.getString("tech_asisstant_count")));
					 
					 
					 json.put("field_asisstant_count",General.checknull(rst.getString("field_asisstant_count")));
					 json.put("lab_attendant_count",General.checknull(rst.getString("lab_attendant_count")));
					 json.put("fieldman_count",General.checknull(rst.getString("fieldman_count")));
					 json.put("yp1_count",General.checknull(rst.getString("yp1_count")));
					 json.put("yp2_count",General.checknull(rst.getString("yp2_count")));
					 json.put("others_count",General.checknull(rst.getString("others_count")));
					 
					 jsonArray.add(json);
	        	 }
	 			String dtilsquery="select field_name,field_count from rsrch_sanction_post_others_detail where sp_id = '"+General.checknull(p_id)+"'";
		 			psmt2 = conn.prepareStatement(dtilsquery);
		 			System.out.println("getSanctionPostDetail dtilsquery||"+dtilsquery);
		 			rst2 = psmt2.executeQuery(); int i=0;
		 			JSONObject detiljson= new JSONObject();
		 			while (rst2.next()) { i++;
		 				
		 				detiljson.put("field_name_"+i,General.checknull(rst2.getString("field_name")));
		 				detiljson.put("field_count_"+i,General.checknull(rst2.getString("field_count")));
		 			} 
		 			jsonArray.add(detiljson);
	 				objectJson.put("piIdlist", jsonArray);
	 				System.out.println("objectJson||"+objectJson.toJSONString());
	          } catch (Exception e) {
	           System.out.println("FileName=[StaffPositionSanctionPostManager] MethodName=[getSanctionPostDetail()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[StaffPositionSanctionPostManager] MethodName=[getSanctionPostDetail()] :", e.getMessage().toString()));
	          }finally {
	              try {
	                  if (rst != null) {
	                      rst.close();
	                      rst = null;
	                  }
	                  if (psmt != null) {
	                	  psmt.close();
	                	  psmt = null;
	                  }
	                  if (conn != null) {
	                      conn.close();
	                      conn = null;
	                  }
	              } catch (final Exception e) {
	            	  l.fatal(Logging.logException("FileName=[StaffPositionSanctionPostManager],MethodName=[getUtilizationCertificateList()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }
	
/*	This Static method for upload the attach document at list one doc should be there 
	will take the StaffPositionSanctionPostModel as for the params
	and return jSonDataFinalObj i.e sucussfully save or not*/
	public static JSONObject StaffPositionSanctionSave(StaffPositionSanctionPostModel TAmodel, String machine, String user_id,List<FileItem> items,String resPrpsId,JSONArray OtherDetailslist) {
		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;		
		String query = "";
		int count = 0,i=0;
		int delcount=0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			String delqry = "DELETE FROM rsrch_sanction_post_mast where ps_mid='"+resPrpsId+"'";
			PreparedStatement psmtdel= conn.prepareStatement(delqry);
			delcount=psmtdel.executeUpdate();

			query = "INSERT INTO rsrch_sanction_post_mast ( sp_id, location_code, ddo_id, ps_mid, "
					+ "ps_principal, co_pi_count, scientist_count, ra_count, srf_count, jrf_count,"
					+"tech_asisstant_count,field_asisstant_count,lab_attendant_count,fieldman_count,"
					+ "yp1_count,yp2_count,others_count,"
					+ " CREATED_BY,CREATED_MACHINE, CREATED_DATE) "
				+ "VALUES 	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW());";
			
			psmt = conn.prepareStatement(query);
			psmt.setString(++i, General.checknull(resPrpsId));
			psmt.setString(++i, General.checknull(TAmodel.getLocation_code()));
			psmt.setString(++i, General.checknull(TAmodel.getDdo_id()));
			psmt.setString(++i, General.checknull(TAmodel.getPs_mid()));
			psmt.setString(++i, General.checknull(TAmodel.getPs_principal()));
			psmt.setString(++i, TAmodel.getCo_pi_count().equals("")?"0":TAmodel.getCo_pi_count());
			psmt.setString(++i, TAmodel.getScientist_count().equals("")?"0":TAmodel.getScientist_count());
			psmt.setString(++i, TAmodel.getRa_count().equals("")?"0":TAmodel.getRa_count());
			psmt.setString(++i, TAmodel.getSrf_count().equals("")?"0":TAmodel.getSrf_count());
			
			psmt.setString(++i, TAmodel.getJrf_count().equals("")?"0":TAmodel.getJrf_count());
			psmt.setString(++i, TAmodel.getTech_asisstant_count().equals("")?"0":TAmodel.getTech_asisstant_count());
			psmt.setString(++i, TAmodel.getField_asisstant_count().equals("")?"0":TAmodel.getField_asisstant_count());
			psmt.setString(++i, TAmodel.getLab_attendant_count().equals("")?"0":TAmodel.getLab_attendant_count());
			
			psmt.setString(++i, TAmodel.getFieldman_count().equals("")?"0":TAmodel.getFieldman_count());
			psmt.setString(++i, TAmodel.getYp1_count().equals("")?"0":TAmodel.getYp1_count());
			psmt.setString(++i, TAmodel.getYp2_count().equals("")?"0":TAmodel.getYp2_count());
			psmt.setString(++i, TAmodel.getOthers_count().equals("")?"0":TAmodel.getOthers_count());
			
			psmt.setString(++i, General.checknull(user_id));
			psmt.setString(++i, General.checknull(machine));
			
			
			
			System.out.println("StaffPositionSanctionPostManager Insert"+psmt);
			count = psmt.executeUpdate();
		
			if (count > 0) {
				saveOtherDetailslist(conn,OtherDetailslist,resPrpsId,user_id,machine);
					/*if(items!=null){
						Iterator<FileItem> iter = items.iterator();
							while (iter.hasNext()) {
								FileItem fileItem = iter.next();
								saveDoc(machine,resPrpsId, user_id,"", conn, fileItem);
					}
				}*/
				conn.commit();
				jSonDataFinalObj.put("status", "Record Saved Successfully");
				jSonDataFinalObj.put("flag", "Y");
				}else {
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
			System.out.println("EXCEPTION CAUSED BY StaffPositionSanctionPostManager :" + " " + e.getMessage().toUpperCase());
			l.fatal(Logging.logException("StaffPositionSanctionPostManager[save]", e.toString()));
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
				System.out.println("EXCEPTION IN StaffPositionSanctionPostManager CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}
	
	
	public static String saveOtherDetailslist(Connection conn,JSONArray OtherDetailslist, String resPrpsId,
												String user_id,String machine) {
		
		PreparedStatement psmt = null;		
		String query = "";
		int[] cnt = null;
		String status=null;
		query="INSERT INTO rsrch_sanction_post_others_detail(sp_id, field_name, field_count, CREATED_BY, CREATED_DATE) "
				+ "VALUES ('"+resPrpsId+"', ?, ?, '"+user_id+"', NOW());";
		try {
			String delqry = "DELETE FROM rsrch_sanction_post_others_detail where sp_id='"+resPrpsId+"'";
			PreparedStatement psmtdel= conn.prepareStatement(delqry);
			psmtdel.executeUpdate();
			psmt = conn.prepareStatement(query);
			
			 		for(int t=0; t<OtherDetailslist.size(); t++){
	            	JSONObject reqobj = (JSONObject) OtherDetailslist.get(t);
	            	if(reqobj != null){
	            	psmt.setString(1, General.checknull(reqobj.get("field_name").toString()));
					psmt.setString(2, General.checknull(reqobj.get("field_count").toString()));
	            	}
					psmt.addBatch();
			    }
	            
				System.out.println("saveOtherDetailslist Insert"+psmt);
			
			cnt = psmt.executeBatch();
			if (cnt.length>0){
				status="Inserted";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	return status;	
}
	
	/*This static method will override the Previous attchments from table
	as well as delete attachment from the directory*/
	public static String deletePreviousattchments(String reference_id,String ftype, String fname,Connection conn) {
		l = Logger.getLogger("exceptionlog");
		String cSql="",msg="";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int count=0;
		try {
			cSql ="delete from file_attachment where reference_id=? and table_name='rsrch_target_objective_achievement_details' and file_type='"+ftype+"' ";
			pstmt = conn.prepareStatement(cSql);
			pstmt.setString(1, reference_id.trim());
			count = pstmt.executeUpdate();
			if(count>0){
				msg="File Deleted Successfully";
			}		
		} catch (Exception e) {
			System.out.println("Error in StaffPositionSanctionPostManager[deletePreviousattchments] : "+e.getMessage());
			l.fatal(Logging.logException("StaffPositionSanctionPostManager[deletePreviousattchments]", e.getMessage().toString()));
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
	
	public static boolean saveDoc(String machine, String resPrpsId, String userid,String file_type, Connection conn, FileItem fileItem){		
		java.io.File file;
		try{
			
			String attachid=saveFileattachment(machine, fileItem, resPrpsId, userid,file_type, conn);
			if(!attachid.equals("")){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/TARGET/"+resPrpsId+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in StaffPositionSanctionPostManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("StaffPositionSanctionPostManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}
	
	public static String saveFileattachment(String machine, FileItem fileItem, String id, String userid,String file_type, Connection conn){ 
		PreparedStatement psmt = null;
		String qry = "";
		String attachid="";
		int count=0;
		try{
			qry="INSERT INTO file_attachment ( file_name, file_type, table_name, reference_id, CREATED_BY, CREATED, MACHINE) VALUES "
			+ "( ? , ?, 'rsrch_target_objective_achievement_details', ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			if(fileItem.getFieldName().equals("upload_doc1")){
				file_type="Target_Major_Doc_Qr";
			}else if(fileItem.getFieldName().equals("upload_doc2")){
				file_type="Target_Minor_Doc_Qr";
			}else if(fileItem.getFieldName().equals("upload_doc3")){
				file_type="Target_Major_Doc_Yr";
			}else if(fileItem.getFieldName().equals("upload_doc4")){
				file_type="Target_Minor_Doc_Yr";
			}else if(fileItem.getFieldName().equals("upload_doc5")){
				file_type="Achievements_Major_Doc_Qr";
			}else if(fileItem.getFieldName().equals("upload_doc6")){
				file_type="Achievements_Minor_Doc_Qr";
			}else if(fileItem.getFieldName().equals("upload_doc7")){
				file_type="Achievements_Major_Photo_Qr";
			}else if(fileItem.getFieldName().equals("upload_doc8")){
				file_type="Achievements_Minor_Photo_Qr";
			}else if(fileItem.getFieldName().equals("upload_doc9")){
				file_type="Achievements_Major_Doc_Yr";
			}else if(fileItem.getFieldName().equals("upload_doc10")){
				file_type="Achievements_Minor_Doc_Yr";
			}else if(fileItem.getFieldName().equals("upload_doc11")){
				file_type="Achievements_Major_Photo_Yr";
			}else if(fileItem.getFieldName().equals("upload_doc12")){
				file_type="Achievements_Minor_Photo_Yr";
			}else {
				file_type="NA";
			}
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, file_type);
			psmt.setString(3, id);
			psmt.setString(4, userid);
			psmt.setString(5, machine);
			System.out.println("saveFileattachment Insert StaffPositionSanctionPostManager>>"+psmt);
			count= psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				attachid = rs.getString(1);
			}
			 
		}
		catch(Exception e)
		{
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: StaffPositionSanctionPostManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("StaffPositionSanctionPostManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
}