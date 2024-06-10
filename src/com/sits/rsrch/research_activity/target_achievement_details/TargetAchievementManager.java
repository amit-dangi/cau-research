/**
 * @ AUTHOR Amit Dangi
 */
package com.sits.rsrch.research_activity.target_achievement_details;

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

public class TargetAchievementManager {
	static Logger l = Logger.getLogger("exceptionlog research activity TargetAchievementManager");

	/*Static Method to get the Target & Achievement saved details if any or
	get the selected proposal details from Project Proposal Submission form as per proposal id
	Return type is object Json*/
	public static JSONObject getTargetAchievementDetail(String p_id,String finYr) {
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
	        	 query="SELECT TA_ID,PS_TITTLE_PROJ ,PS_PRINCIPAL,(select fa_name from rsrch_funding_agency where fn_agency=fa_id) as fn_agency,'1.capital 2.General' as " 
	     				+ "fundavail,date_format(proj_start_date, '%d/%m/%Y') as proj_start_date,date_format(PS_DEADLINE_SUBMISSION, '%d/%m/%Y') as " 
	     				+ "PS_DEADLINE_SUBMISSION,projtype,projterm,LOCATION_CODE,ddo_id,is_form_submittd,is_project_extension,"
	     				+ "quarter,date_format(from_date, '%d/%m/%Y') as from_date,date_format(to_date, '%d/%m/%Y') as to_date,category_qr,sub_category_qr,category_yr,sub_category_yr,"
	     				+ "acv_quarter,date_format(acv_from_date, '%d/%m/%Y') as acv_from_date,date_format(acv_to_date, '%d/%m/%Y') as acv_to_date,acv_category_qr,"
	     				+ "acv_sub_category_qr,acv_category_yr,acv_sub_category_yr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Target_Major_Doc_Qr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Target_Major_Doc_Qr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Target_Minor_Doc_Qr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Target_Minor_Doc_Qr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and"
	     				+ " file_type='Target_Major_Doc_Yr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Target_Major_Doc_Yr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Target_Minor_Doc_Yr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Target_Minor_Doc_Yr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Achievements_Major_Doc_Qr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Achievements_Major_Doc_Qr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Achievements_Minor_Doc_Qr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Achievements_Minor_Doc_Qr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Achievements_Major_Photo_Qr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Achievements_Major_Photo_Qr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Achievements_Minor_Photo_Qr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Achievements_Minor_Photo_Qr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and"
	     				+ " file_type='Achievements_Major_Doc_Yr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Achievements_Major_Doc_Yr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Achievements_Minor_Doc_Yr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Achievements_Minor_Doc_Yr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Achievements_Major_Photo_Yr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Achievements_Major_Photo_Yr,"
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Achievements_Minor_Photo_Yr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Achievements_Minor_Photo_Yr, "
	     				+ "(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='rsrch_target_objective_achievement_details' and" 
	     				+ " file_type='Target_Photograph_Qr' and reference_id=c.TA_ID order by CREATED desc limit 1) as Target_Photograph_Qr "
	     				+ "FROM rsrch_form1_mast b left join rsrch_target_objective_achievement_details c on b.PS_MID=c.PS_MID and c.fin_year = '"+General.checknull(finYr)+"' "
 						+ "WHERE is_form_submittd='Y' and b.PS_MID = '"+General.checknull(p_id)+"' ";
	     	 			psmt = conn.prepareStatement(query);
	 			System.out.println("getTargetAchievementDetail psmt||"+psmt);
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
	 				//Getting submission form needed detail
	 				
	 				 json.put("TA_ID",General.checknull(rst.getString("TA_ID")));
	        		 json.put("PS_TITTLE_PROJ",General.checknull(rst.getString("PS_TITTLE_PROJ")));
					 json.put("fn_agency",General.checknull(rst.getString("fn_agency")));
					 json.put("fundavail",General.checknull(rst.getString("fundavail")));
					 json.put("proj_start_date",General.checknull(rst.getString("proj_start_date")));
					 json.put("proj_end_date",General.checknull(rst.getString("PS_DEADLINE_SUBMISSION")));
					 
					//puting target json
					 json.put("quarter",General.checknull(rst.getString("quarter")));
					 json.put("from_date",General.checknull(rst.getString("from_date")));
					 json.put("to_date",General.checknull(rst.getString("to_date")));
					 json.put("category_qr",General.checknull(rst.getString("category_qr")));
					 json.put("sub_category_qr",General.checknull(rst.getString("sub_category_qr")));
					 json.put("category_yr",General.checknull(rst.getString("category_yr")));
					 json.put("sub_category_yr",General.checknull(rst.getString("sub_category_yr")));
					 
					 //puting Achivements json
					 json.put("acv_quarter",General.checknull(rst.getString("acv_quarter")));
					 json.put("acv_from_date",General.checknull(rst.getString("acv_from_date")));
					 json.put("acv_to_date",General.checknull(rst.getString("acv_to_date")));
					 json.put("acv_category_qr",General.checknull(rst.getString("acv_category_qr")));
					 json.put("acv_sub_category_qr",General.checknull(rst.getString("acv_sub_category_qr")));
					 json.put("acv_category_yr",General.checknull(rst.getString("acv_category_yr")));
					 json.put("acv_sub_category_yr",General.checknull(rst.getString("acv_sub_category_yr")));
					 
					 //Target Uploaded document json
					 json.put("Target_Major_Doc_Qr",General.checknull(rst.getString("Target_Major_Doc_Qr")));
					 json.put("Target_Minor_Doc_Qr",General.checknull(rst.getString("Target_Minor_Doc_Qr")));
					 json.put("Target_Major_Doc_Yr",General.checknull(rst.getString("Target_Major_Doc_Yr")));
					 json.put("Target_Minor_Doc_Yr",General.checknull(rst.getString("Target_Minor_Doc_Yr")));
					 
					//Achivements Uploaded document Quarterly json
					 json.put("Achievements_Major_Doc_Qr",General.checknull(rst.getString("Achievements_Major_Doc_Qr")));
					 json.put("Achievements_Minor_Doc_Qr",General.checknull(rst.getString("Achievements_Minor_Doc_Qr")));
					 json.put("Achievements_Major_Photo_Qr",General.checknull(rst.getString("Achievements_Major_Photo_Qr")));
					 json.put("Achievements_Minor_Photo_Qr",General.checknull(rst.getString("Achievements_Minor_Photo_Qr")));
					 //yearly json
					 json.put("Achievements_Major_Doc_Yr",General.checknull(rst.getString("Achievements_Major_Doc_Yr")));
					 json.put("Achievements_Minor_Doc_Yr",General.checknull(rst.getString("Achievements_Minor_Doc_Yr")));
					 json.put("Achievements_Major_Photo_Yr",General.checknull(rst.getString("Achievements_Major_Photo_Yr")));
					 json.put("Achievements_Minor_Photo_Yr",General.checknull(rst.getString("Achievements_Minor_Photo_Yr")));
					 
					 //New filed adeed photograph in targetquarter
					 json.put("Target_Photograph_Qr",General.checknull(rst.getString("Target_Photograph_Qr")));
					 
					 jsonArray.add(json);
	        	 }
	        	 
	        	 objectJson.put("piIdlist", jsonArray);
	        	 
	          } catch (Exception e) {
	           System.out.println("FileName=[TargetAchievementManager] MethodName=[getTargetAchievementDetail()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[TargetAchievementManager] MethodName=[getTargetAchievementDetail()] :", e.getMessage().toString()));
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
	            	  l.fatal(Logging.logException("FileName=[TargetAchievementManager],MethodName=[getUtilizationCertificateList()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }
	
/*	This Static method for upload the attach document at list one doc should be there 
	will take the TargetAchievementModel as for the params
	and return jSonDataFinalObj i.e sucussfully uploaded or not*/
	public static JSONObject TargetAchievementUpload(TargetAchievementModel TAmodel, String machine, String user_id,List<FileItem> items,String resPrpsId) {
		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;		
		String query = "",ta_id="";
		int count = 0;
		int delcount=0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			System.out.println("TAmodel ::"+TAmodel);
			ta_id=General.checknull(TAmodel.getTA_ID());
			System.out.println("before ta_id||"+ta_id);
			if(ta_id.equals("")){
				System.out.println("in");
			String Qry1 = "SELECT concat('TA', LPAD(CONVERT(IFNULL(MAX(SUBSTR(TA_ID,3,6)),0)+1,SIGNED INTEGER),4,'0')) FROM rsrch_target_objective_achievement_details";
			PreparedStatement psmt5= conn.prepareStatement(Qry1);
			ResultSet rst1 = psmt5.executeQuery();
			if (rst1.next()) {
				ta_id = General.checknull(rst1.getString(1));
			}
			}
			System.out.println("after ta_id||"+ta_id);
			String delqry = "DELETE FROM rsrch_target_objective_achievement_details where ta_id='"+ta_id+"' and fin_year='"+General.checknull(TAmodel.getFinYr())+"' ";
			PreparedStatement psmtdel= conn.prepareStatement(delqry);
			delcount=psmtdel.executeUpdate();

			query = "INSERT INTO rsrch_target_objective_achievement_details ( PS_MID, fin_year, quarter, from_date, "
					+ "to_date, category_qr, sub_category_qr, category_yr, sub_category_yr, is_uploaded_doc,"
					+"acv_quarter,acv_from_date,acv_to_date,acv_category_qr,acv_sub_category_qr,acv_category_yr,acv_sub_category_yr,"
					+ " CREATED_BY,CREATED_MACHINE, CREATED_DATE,TA_ID) "
				+ "VALUES 	(?,?,?,?,?,?,?,?,?,'Y',?,?,?,?,?,?,?,?,?,NOW(),?);";
			
			psmt = conn.prepareStatement(query,psmt.RETURN_GENERATED_KEYS);
			psmt.setString(1, General.checknull(resPrpsId));
			psmt.setString(2, General.checknull(TAmodel.getFinYr()));
			psmt.setString(3, General.checknull(TAmodel.getQuarter()));
			psmt.setString(4, TAmodel.getXFROMDATE().equals("") ? null:General.formatDate(TAmodel.getXFROMDATE()));
			psmt.setString(5, TAmodel.getXTODATE().equals("") ? null:General.formatDate(TAmodel.getXTODATE()));
			psmt.setString(6, General.checknull(TAmodel.getRsrchcatQ()));
			psmt.setString(7, General.checknull(TAmodel.getRsrchsubcatQ()));
			psmt.setString(8, General.checknull(TAmodel.getRsrchcatY()));
			psmt.setString(9, General.checknull(TAmodel.getRsrchsubcatY()));
			
			psmt.setString(10, General.checknull(TAmodel.getAcv_quarter()));
			psmt.setString(11, null/*TAmodel.getXFROMDATE_ACV().equals("") ? null:General.formatDate(TAmodel.getXFROMDATE_ACV())*/);
			psmt.setString(12, null/*TAmodel.getXTODATE_ACV().equals("") ? null:General.formatDate(TAmodel.getXTODATE_ACV())*/);
			psmt.setString(13, General.checknull(TAmodel.getAcv_rsrchcatQ()));
			psmt.setString(14, General.checknull(TAmodel.getAcv_rsrchsubcatQ()));
			psmt.setString(15, General.checknull(TAmodel.getAcv_rsrchcatY()));
			psmt.setString(16, General.checknull(TAmodel.getAcv_rsrchsubcatY()));
			
			psmt.setString(17, General.checknull(user_id));
			psmt.setString(18, General.checknull(machine));
			psmt.setString(19, ta_id);
			System.out.println("TargetAchievementManager Insert"+psmt);
			count = psmt.executeUpdate();
			
			if (count > 0) {
					if(items!=null){
						Iterator<FileItem> iter = items.iterator();
							while (iter.hasNext()) {
								FileItem fileItem = iter.next();
								saveDoc(machine,ta_id, user_id,"", conn, fileItem);
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
			l.fatal(Logging.logException("TargetAchievementManager[save]", e.toString()));
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
			System.out.println("Error in TargetAchievementManager[deletePreviousattchments] : "+e.getMessage());
			l.fatal(Logging.logException("TargetAchievementManager[deletePreviousattchments]", e.getMessage().toString()));
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
	  		System.out.println("Error in TargetAchievementManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("TargetAchievementManager[saveDoc]", e.toString()));
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
			}else if(fileItem.getFieldName().equals("upload_doc13")){
				file_type="Target_Photograph_Qr";
			}else {
				file_type="NA";
			}
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, file_type);
			psmt.setString(3, id);
			psmt.setString(4, userid);
			psmt.setString(5, machine);
			System.out.println("saveFileattachment Insert TargetAchievementManager>>"+psmt);
			count= psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				attachid = rs.getString(1);
			}
			 
		}
		catch(Exception e)
		{
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: TargetAchievementManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("TargetAchievementManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
}