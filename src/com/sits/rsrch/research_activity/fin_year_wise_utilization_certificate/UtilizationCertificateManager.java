/**
 * @ AUTHOR Amit Dangi
 */
package com.sits.rsrch.research_activity.fin_year_wise_utilization_certificate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.sits.rsrch.research_activity.target_achievement_details.TargetAchievementModel;

public class UtilizationCertificateManager {
	static Logger l = Logger.getLogger("exceptionlog research activity UtilizationCertificateManager");

	/*Static Method to get the utilization certificate saved details if any or
	get the selected proposal details from Project Proposal Submission form as per proposal id
	Return type is object Json*/
	public static JSONObject getUtilizationCertificateDetail(String p_id,String fin_year) {
        PreparedStatement psmt = null;
        Connection conn=null;
        ResultSet rst = null;
        String query="";
        JSONObject objectJson=new JSONObject();        
        JSONArray jsonArray = new JSONArray();
	         try {
	        	 //Json used to get the employee name from hrms database
	 			 JSONObject jsonobj=new JSONObject();
				 JSONObject finalObject=new JSONObject();
				 finalObject.put("tablename", "employee_mast");
				 finalObject.put("columndesc","concat(employeeName,' (',employeeCodeM,')')");
				 finalObject.put("id", "employeeId");
				 jsonobj= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObject);
				 JSONArray deparr = (JSONArray) jsonobj.get("commondata");
				 
	        	 conn = DBConnection.getConnection();
	        	/* Query change as per added new table dependency
	        	 *  query = "SELECT PS_TITTLE_PROJ ,PS_PRINCIPAL,(select fa_name from rsrch_funding_agency where fn_agency=fa_id) as fn_agency,'1.capital 2.General' as fundavail,date_format(proj_start_date, '%d/%m/%Y') as proj_start_date,"
	        	 		+ "date_format(PS_DEADLINE_SUBMISSION, '%d/%m/%Y') as PS_DEADLINE_SUBMISSION,projtype,projterm,LOCATION_CODE,ddo_id,is_form_submittd,"
	        	 		+ "is_project_extension,(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment "
	        	 		+ "where table_name='utilization_certificate' and file_type='UC_Certificate' and reference_id=b.PS_MID "
	        	 		+ "order by CREATED desc limit 1) as uc_certname,(select concat(file_attachment_id,'_',file_name) as file_name "
	        	 		+ "from file_attachment where table_name='utilization_certificate' and file_type='AUC_Certificate' and "
	        	 		+ "reference_id=b.PS_MID order by CREATED desc limit 1) as auc_certname FROM rsrch_form1_mast b WHERE is_form_submittd='Y'  ";
	 			if(!General.checknull(p_id).trim().equals("")){
	 				query+=" and PS_MID = '"+General.checknull(p_id)+"' ";
	 			}*/
	        	 query="SELECT uc_id,PS_TITTLE_PROJ ,PS_PRINCIPAL,(select fa_name from rsrch_funding_agency where fn_agency=fa_id) as fn_agency,'1.capital 2.General' as "
	        	 		+ "fundavail,date_format(proj_start_date, '%d/%m/%Y') as proj_start_date,date_format(PS_DEADLINE_SUBMISSION, '%d/%m/%Y') as "
	        	 		+ "PS_DEADLINE_SUBMISSION,projtype,projterm,LOCATION_CODE,ddo_id,is_form_submittd,is_project_extension,(select "
	        	 		+ "concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_uc_auc_certificate_details' and "
	        	 		+ "file_type='UC_Certificate' and reference_id=ucd.UC_ID order by CREATED desc limit 1) as uc_certname,(select "
	        	 		+ "concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_uc_auc_certificate_details' and "
	        	 		+ "file_type='AUC_Certificate' and reference_id=ucd.UC_ID order by CREATED desc limit 1) as auc_certname FROM "
	        	 		+"rsrch_form1_mast b left join rsrch_uc_auc_certificate_details ucd on b.ps_mid=ucd.ps_mid "
	        	 		+ "and ucd.fin_year='"+General.checknull(fin_year)+"' where is_form_submittd='Y' and b.PS_MID='"+General.checknull(p_id)+"'";
	        	 		
	 			psmt = conn.prepareStatement(query);
	 			System.out.println("getUtilizationCertificateDetail psmt||"+psmt);
	 			rst = psmt.executeQuery();
	 			
	 			while (rst.next()) {
	 				JSONObject json= new JSONObject();
	 				for(int i=0; i<deparr.size(); i++){ 
						JSONObject jsn= (JSONObject) deparr.get(i);
						if(jsn.get("id").equals(rst.getString("PS_PRINCIPAL")))
						{
						json.put("PS_PRINCIPAL", General.checknull(jsn.get("desc").toString()));
						}
					}
	 				
	 				 json.put("uc_id",General.checknull(rst.getString("uc_id")));
	        		 json.put("PS_TITTLE_PROJ",General.checknull(rst.getString("PS_TITTLE_PROJ")));
					 json.put("fn_agency",General.checknull(rst.getString("fn_agency")));
					 
					 json.put("fundavail",General.checknull(rst.getString("fundavail")));
					 json.put("proj_start_date",General.checknull(rst.getString("proj_start_date")));
					 json.put("proj_end_date",General.checknull(rst.getString("PS_DEADLINE_SUBMISSION")));
					 json.put("uc_certname",General.checknull(rst.getString("uc_certname")));
					 json.put("auc_certname",General.checknull(rst.getString("auc_certname")));
					 jsonArray.add(json);
	        	 }
	        	 
	        	 objectJson.put("piIdlist", jsonArray);
	        	 
	          } catch (Exception e) {
	           System.out.println("FileName=[UtilizationCertificateManager] MethodName=[getUtilizationCertificateDetail()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[UtilizationCertificateManager] MethodName=[getUtilizationCertificateDetail()] :", e.getMessage().toString()));
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
	            	  l.fatal(Logging.logException("FileName=[UtilizationCertificateManager],MethodName=[getUtilizationCertificateList()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }
	
/*	This Static method for upload the attach document in rsrch_uc_auc_certificate_details
 *  at list one doc should be there will take the TargetAchievementModel as for the params
	and return jSonDataFinalObj i.e sucussfully uploaded or not*/
	public static JSONObject UtilizationCertificateUpload(UtilizationCertificateModel UCmodel, String machine, String user_id,List<FileItem> items) {
		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;		
		String query = "",attachid="",file_type="",fname="";
		int count = 0;
		int delcount=0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String uc_id=General.checknull(UCmodel.getUc_id());
			if(uc_id==""){
			String delqry = "DELETE FROM rsrch_uc_auc_certificate_details where PS_MID='"+General.checknull(UCmodel.getResPrpsId())+"' and fin_year='"+General.checknull(UCmodel.getFin_year())+"' ";
			PreparedStatement psmtdel= conn.prepareStatement(delqry);
			delcount=psmtdel.executeUpdate();

			query = "INSERT INTO rsrch_uc_auc_certificate_details ( PS_MID, fin_year,CREATED_BY,CREATED_MACHINE, CREATED_DATE) "
				+ "VALUES 	(?,?,?,?,NOW());";
			
			psmt = conn.prepareStatement(query,psmt.RETURN_GENERATED_KEYS);
			psmt.setString(1, General.checknull(UCmodel.getResPrpsId()));
			psmt.setString(2, General.checknull(UCmodel.getFin_year()));
			psmt.setString(3, General.checknull(user_id));
			psmt.setString(4, General.checknull(machine));
			System.out.println("rsrch_uc_auc_certificate_details Insert"+psmt);
			
			count = psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();
			if (rs.next()) {
				attachid = rs.getString(1);
			}
			}
			else{
				attachid=uc_id;
			}
			System.out.println("Generated key||"+attachid);
		
			if (count > 0 || attachid!="") {
			
					if(items!=null){
						Iterator<FileItem> iter = items.iterator();
							while (iter.hasNext()) {
								FileItem fileItem = iter.next();
								if(fileItem.getFieldName().equals("upload_doc1")){
									file_type="UC_Certificate";
									fname=UCmodel.getUc_certname();
								}
								else if(fileItem.getFieldName().equals("upload_doc2")){
									file_type="AUC_Certificate";
									fname=UCmodel.getAuc_certname();
								}else{
									file_type=fileItem.getFieldName();
								}
								saveDoc(machine,attachid, user_id,file_type,fname, conn, fileItem);
					}
				}
				conn.commit();
				jSonDataFinalObj.put("status", "Details Saved Successfully");
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
			System.out.println("EXCEPTION CAUSED BY TargetAchievementManager :" + " " + e.getMessage().toUpperCase());
			l.fatal(Logging.logException("TargetAchievementManager[UtilizationCertificateUpload]", e.toString()));
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
				System.out.println("EXCEPTION IN TargetAchievementManager CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}
	
	
	/*Static method for upload the attach document if any 
	will take the UtilizationCertificateModel as for the params
	and return jSonDataFinalObj i.e sucussfully uploaded or not*/
	/*public static JSONObject Uploaddoc(String machine, UtilizationCertificateModel UCmodel, String user_id, FileItem fileItem) {
		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;
		String file_type="",fname="";
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			if(fileItem.getFieldName().equals("upload_doc1")){
				file_type="UC_Certificate";
				fname=UCmodel.getUc_certname();
			}
			else if(fileItem.getFieldName().equals("upload_doc2")){
				file_type="AUC_Certificate";
				fname=UCmodel.getAuc_certname();
			}else{
				file_type=fileItem.getFieldName();
			}
				deletePreviousattchments(UCmodel.getResPrpsId(),file_type,fname,conn);
				if(UtilizationCertificateManager.saveDoc(machine, UCmodel.getResPrpsId(), user_id,file_type, conn, fileItem)){
				conn.commit();
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/UC/"+UCmodel.getResPrpsId()+"/"+fname;
				File file = new File(directoryName);
				file.delete();
				jSonDataFinalObj.put("status", "Certificate Upload Successfully");
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
			System.out.println("EXCEPTION CAUSED BY UtilizationCertificateManager :" + " " + e.getMessage().toUpperCase());
			l.fatal(Logging.logException("UtilizationCertificateManager[getUtilizationCertificateList]", e.toString()));
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
				System.out.println("EXCEPTION IN UtilizationCertificateManager CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}*/
	
	/*This static method will override the Previous attchments from table
	as well as delete attachment from the directory*/
	public static String deletePreviousattchments(String reference_id,String ftype, String fname,Connection conn) {
		l = Logger.getLogger("exceptionlog");
		String cSql="",msg="";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int count=0;
		try {
			cSql ="delete from file_attachment where reference_id=? and table_name='rsrch_uc_auc_certificate_details' and file_type='"+ftype+"' and concat(file_attachment_id,'_',file_name)='"+fname+"' ";
			pstmt = conn.prepareStatement(cSql);
			pstmt.setString(1, reference_id.trim());
			count = pstmt.executeUpdate();
			if(count>0){
				msg="File Deleted Successfully";
			}		
		} catch (Exception e) {
			System.out.println("Error in UtilizationCertificateManager[deletePreviousattchments] : "+e.getMessage());
			l.fatal(Logging.logException("UtilizationCertificateManager[deletePreviousattchments]", e.getMessage().toString()));
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
	
	public static boolean saveDoc(String machine, String uc_id, String userid,String file_type,String fname, Connection conn, FileItem fileItem){		
		java.io.File file;
		try{
			deletePreviousattchments(uc_id,file_type,fname,conn);
			String attachid=saveFileattachment(machine, fileItem, uc_id, userid,file_type, conn);
			
			if(!attachid.equals("")){
				String deldirectory = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/UC/"+uc_id+"/"+fname;
				file = new File(deldirectory);
				file.delete();
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/UC/"+uc_id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in UtilizationCertificateManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("UtilizationCertificateManager[saveDoc]", e.toString()));
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
			+ "( ? , ?, 'rsrch_uc_auc_certificate_details', ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, file_type);
			psmt.setString(3, id);
			psmt.setString(4, userid);
			psmt.setString(5, machine);
			System.out.println("saveFileattachment Insert UtilizationCertificateManager>>"+psmt);
			count= psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				attachid = rs.getString(1);
			}
			 
		}
		catch(Exception e)
		{
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: UtilizationCertificateManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("UtilizationCertificateManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
}