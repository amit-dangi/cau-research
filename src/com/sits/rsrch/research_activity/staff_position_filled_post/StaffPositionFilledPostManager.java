package com.sits.rsrch.research_activity.staff_position_filled_post;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sits.conn.DBConnection;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.general.ReadProps;

public class StaffPositionFilledPostManager {
	static Logger l = Logger.getLogger("exceptionlog research activity StaffPositionFilledPostManager");

	
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
	 		//	System.out.println("getSanctionPostDetail dtilsquery||"+dtilsquery);
	 			rst2 = psmt2.executeQuery(); int i=0;
	 			JSONObject detiljson= new JSONObject();
	 			while (rst2.next()) { i++;
	 				
	 				detiljson.put("field_name_"+i,General.checknull(rst2.getString("field_name")));
	 				detiljson.put("field_count_"+i,General.checknull(rst2.getString("field_count")));
	 			} 
	 			jsonArray.add(detiljson);
 				objectJson.put("piIdlist", jsonArray);
	        	 
	          } catch (Exception e) {
	           System.out.println("FileName=[StaffPositionFilledPostManager] MethodName=[getTargetAchievementDetail()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[StaffPositionFilledPostManager] MethodName=[getTargetAchievementDetail()] :", e.getMessage().toString()));
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
	            	  l.fatal(Logging.logException("FileName=[StaffPositionFilledPostManager],MethodName=[getUtilizationCertificateList()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }


	public synchronized static JSONObject StaffPositionFilledSave(StaffPositionFilledPostModel  cModel, String login_id, String machine,List<FileItem> fileNameList) {
		Connection conn = null;
		PreparedStatement  psmt=null;
        ResultSet rst = null;
		String qry = "",file_name="";
		int pf_id=0;
		int count[] = null;
        JSONObject objectJson=new JSONObject();        

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			String delqry = "DELETE FROM rsrch_position_filled_post where ps_mid='"+cModel.getPs_mid()+"'";
			PreparedStatement psmtdel= conn.prepareStatement(delqry);
			int a =psmtdel.executeUpdate();
			System.out.println(a);
			
			String delqry1 = "DELETE FROM file_attachment where file_type='"+cModel.getPs_mid()+"' and table_name='rsrch_position_filled_post'";
			PreparedStatement psmtdel1= conn.prepareStatement(delqry1);
			int b =psmtdel1.executeUpdate();
			
			System.out.println(b);

			
			
		    qry="select if(max(pf_id) != '',max(pf_id)+1,'1')as pf_id from rsrch_position_filled_post";
		    psmt = conn.prepareStatement(qry);	
		  //System.out.println("psmt:::"+psmt);
 			rst = psmt.executeQuery();
 			if (rst.next()) {
 				pf_id=rst.getInt("pf_id");
 			}

 			qry="";
 			psmt=null;
			qry =" insert into rsrch_position_filled_post (ps_mid,type_name,type,file_name,"
				+" CREATED_DATE,CREATED_BY,CREATED_MACHINE,pf_id)"
				+" values(?,?,?,?,now(),?,?,?)";
			psmt = conn.prepareStatement(qry);
			//Number of Co-PI: array
			int check1=1;
			for(StaffPositionFilledPostModel coPiList :cModel.getCoPiList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(coPiList.getCo_pi()));
				psmt.setString(3, General.checknull(coPiList.getCo_pi_type()));
				if(!coPiList.getCo_pi_file_type().equals("") && !coPiList.getCo_pi_file_type().equals(null)){
					 file_name=coPiList.getCo_pi_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("co_pi_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post", pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),coPiList.getCo_pi_type());
	                	    file_name=fileItem.getName();
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			//Number of scientist array
			check1=1;
			for(StaffPositionFilledPostModel Scientist :cModel.getScientistList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(Scientist.getScientist()));
				psmt.setString(3, General.checknull(Scientist.getScientist_type()));
				System.out.println("Scientist.getScientist_file_type()::"+Scientist.getScientist_file_type());
				if(!Scientist.getScientist_file_type().equals("") && !Scientist.getScientist_file_type().equals(null)){
					 file_name=Scientist.getScientist_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("scientist_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post", pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),Scientist.getScientist_type());
	                	    file_name=fileItem.getName();
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			 //Number of RA: array
			check1=1;
			for(StaffPositionFilledPostModel raList :cModel.getRaList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(raList.getRa()));
				psmt.setString(3, General.checknull(raList.getRa_type()));
				if(!raList.getRa_file_type().equals("") && !raList.getRa_file_type().equals(null)){
					 file_name=raList.getRa_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("ra_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post", pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),raList.getRa_type());
	                		file_name=fileItem.getName();	
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			 //Number of SRF array
			check1=1;
			for(StaffPositionFilledPostModel SrfList :cModel.getSrfList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(SrfList.getSrf()));
				psmt.setString(3, General.checknull(SrfList.getSrf_type()));
				if(!SrfList.getSrf_file_type().equals("") && !SrfList.getSrf_file_type().equals(null)){
					 file_name=SrfList.getSrf_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("srf_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post", pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),SrfList.getSrf_type());
	                		file_name=fileItem.getName();	
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			//Number of JRF array  
			check1=1;
			for(StaffPositionFilledPostModel jrfList :cModel.getJrfList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(jrfList.getJrf()));
				psmt.setString(3, General.checknull(jrfList.getJrf_type()));
				if(!jrfList.getJrf_file_type().equals("") && !jrfList.getJrf_file_type().equals(null)){
					 file_name=jrfList.getJrf_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("jrf_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post", pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),jrfList.getJrf_type());
	                		file_name=fileItem.getName();	
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			//Number of Technical Assistant: array
			check1=1;
			for(StaffPositionFilledPostModel TechAsisstantList :cModel.getTechAsisstantList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(TechAsisstantList.getTech_asisstant()));
				psmt.setString(3, General.checknull(TechAsisstantList.getTech_asisstant_type()));
				if(!TechAsisstantList.getTech_asisstant_file_type().equals("") && !TechAsisstantList.getTech_asisstant_file_type().equals(null)){
					 file_name=TechAsisstantList.getTech_asisstant_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("tech_asisstant_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post",pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),TechAsisstantList.getTech_asisstant_type());
	                	    file_name=fileItem.getName();
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			//Number of Field Assistant: array
			check1=1;
			for(StaffPositionFilledPostModel FieldAsisstantList :cModel.getFieldAsisstantList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(FieldAsisstantList.getField_asisstant()));
				psmt.setString(3, General.checknull(FieldAsisstantList.getField_asisstant_type()));
				if(!FieldAsisstantList.getField_asisstant_file_type().equals("") && !FieldAsisstantList.getField_asisstant_file_type().equals(null)){
					 file_name=FieldAsisstantList.getField_asisstant_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("field_asisstant_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post", pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),FieldAsisstantList.getField_asisstant_type());
	                	    file_name=fileItem.getName();	
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			//Number of Lab/Field Attendant: array
			check1=1;
			for(StaffPositionFilledPostModel LabAttendantList :cModel.getLabAttendantList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(LabAttendantList.getLab_attendant()));
				psmt.setString(3, General.checknull(LabAttendantList.getLab_attendant_type()));
				if(!LabAttendantList.getLab_attendant_file_type().equals("") && !LabAttendantList.getLab_attendant_file_type().equals(null)){
					 file_name=LabAttendantList.getLab_attendant_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("lab_attendant_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post",pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),LabAttendantList.getLab_attendant_type());
	                	    file_name=fileItem.getName();
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			//Number of Fieldman array
			check1=1;
			for(StaffPositionFilledPostModel FieldmanList :cModel.getFieldmanList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(FieldmanList.getFieldman()));
				psmt.setString(3, General.checknull(FieldmanList.getFieldman_type()));
				if(!FieldmanList.getFieldman_file_type().equals("") && !FieldmanList.getFieldman_file_type().equals(null)){
					 file_name=FieldmanList.getFieldman_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("fieldman_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post",pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),FieldmanList.getFieldman_type());
	                		file_name=fileItem.getName();
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			// Number of YP-I array 
			check1=1;
			for(StaffPositionFilledPostModel YpiList :cModel.getYpiList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(YpiList.getYp1()));
				psmt.setString(3, General.checknull(YpiList.getYp1_type()));
				if(!YpiList.getYp1_file_type().equals("") && !YpiList.getYp1_file_type().equals(null)){
					 file_name=YpiList.getYp1_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("yp1_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post", pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),YpiList.getYp1_type());
	                		file_name=fileItem.getName();	
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			// Number of YP-II array
			check1=1;
			for(StaffPositionFilledPostModel YpiiList :cModel.getYpiiList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(YpiiList.getYp2()));
				psmt.setString(3, General.checknull(YpiiList.getYp2_type()));
				if(!YpiiList.getYp2_file_type().equals("") && !YpiiList.getYp2_file_type().equals(null)){
					 file_name=YpiiList.getYp2_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("yp2_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post", pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),YpiiList.getYp2_type());
	                		file_name=fileItem.getName();
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			
			//Other array
			check1=1;
			for(StaffPositionFilledPostModel OtherList :cModel.getOtherList()){
				psmt.setString(1, General.checknull(cModel.getPs_mid()));
				psmt.setString(2, General.checknull(OtherList.getOthers()));
				psmt.setString(3, General.checknull(OtherList.getOthers_type()));
				if(!OtherList.getOthers_file_type().equals("") && !OtherList.getOthers_file_type().equals(null)){
					 file_name=OtherList.getOthers_file_type();
			  		String	ab=filedetailsave(machine, file_name, pf_id, login_id, 0, "rsrch_position_filled_post", conn,cModel.getPs_mid());

				}else{
					Iterator<FileItem> iter = fileNameList.iterator(); 
	                while (iter.hasNext()) {
	                	FileItem fileItem = iter.next();
	                	//System.out.println("  file name1 "+fileItem.getFieldName()); 
	                	if(fileItem.getFieldName().equals("others_file_"+(check1))){
	                		saveDoc(machine, "rsrch_position_filled_post",pf_id,cModel.getPs_mid(), login_id, conn, fileItem,"",cModel.getPs_mid(),OtherList.getOthers_type());
	                		file_name=fileItem.getName();
	                	}
	                }
				}
                ++check1;
                psmt.setString(4, General.checknull(file_name));
				psmt.setString(5, General.checknull(login_id));
				psmt.setString(6, General.checknull(machine));
				psmt.setInt(7, pf_id);
				psmt.addBatch();
				++pf_id;
			}
			count=psmt.executeBatch();
			
			if(count.length >0){
				conn.commit();
				objectJson.put("status", "Record Saved Successfully");
				objectJson.put("flag", "Y");
				}else {
					conn.rollback();				
					objectJson.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
					objectJson.put("flag", "N");
			}
			
		} catch (Exception e) {
			if (e.toString().contains("Duplicate")){
				objectJson.put("status", ApplicationConstants.UNIQUE_CONSTRAINT);
				objectJson.put("flag", "N");
			}
			if (e.toString().contains("REFERENCES")){
				objectJson.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				objectJson.put("flag", "N");
			}

	           System.out.println("FileName=[StaffPositionFilledPostManager] MethodName=[StaffPositionFilledSave()] :"+ e.getMessage().toString());
	           l.fatal(Logging.logException("FileName=[StaffPositionFilledPostManager] MethodName=[StaffPositionFilledSave()] :", e.getMessage().toString()));
	          } finally {
			try {
				if (psmt != null){
					psmt.clearBatch();
					psmt.close();
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		 return objectJson;
	}
	
	public static boolean saveDoc(String machine,String tablename,int mastid,String ps_id,String userid,
			Connection conn, FileItem fileItem,String attid,String desc,String type){
	  	java.io.File file;
	  	try{
	  		//System.out.println("ds::"+mastid);
	  		String attachid=attid;
	  		if(attachid.equals(""))
	  			attachid=filedetailsave(machine, fileItem.getName(), mastid, userid, fileItem.getSize(), tablename, conn,desc);
	  		else
	  			filedetailupdate(machine, fileItem.getName(), mastid, userid, fileItem.getSize(), tablename, attid, conn,desc);
			
	  		String directoryName=ReadProps.getkeyValue("document.path", "sitsResource")+"/RSRCH/STAFF_POSITION/"+ps_id+"/"+type+"/";
//    		System.out.println("directoryName1:"+directoryName);
	  		File directory = new File(directoryName);
    		if (!directory.isDirectory()){
     			directory.mkdirs();
     		}
    		//file = new File(directoryName +attachid+"_"+fileItem.getName());
    		file = new File(directoryName +fileItem.getName());
    		fileItem.write(file);
	  	}catch(Exception e){
	  		System.out.println("Error in StaffPositionFilledPostManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("StaffPositionFilledPostManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}	
	
	public static String filedetailsave(String machine, String name,int mastid,String userid, long size,String tablename,Connection conn,String desc){ 
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";
		String attachid="";
		int count=0;
		try{
			String Qry = "SELECT IFNULL(MAX(file_attachment_id)+1,1) FROM file_attachment"; 
			psmt = conn.prepareStatement(Qry);
			rst= psmt.executeQuery();
			if(rst.next())
				attachid=General.checknull(rst.getString(1));
			
			qry="INSERT INTO file_attachment"
					+ " (file_attachment_id,file_name,file_type,table_name,reference_id,CREATED_BY,CREATED,MACHINE) "
					+ " VALUES (?, ?,?,?, ?,?,now(),?)";
			psmt = conn.prepareStatement(qry);
			psmt.setString(1, attachid);
			psmt.setString(2, name);
			psmt.setString(3, desc);
			psmt.setString(4, tablename);
			psmt.setInt(5, mastid);
			psmt.setString(6, userid);
			psmt.setString(7, machine);
			count= psmt.executeUpdate();
			System.out.println("  file attachid "+psmt);
			if(count==0)
				attachid="";
		}
		catch(Exception e)
		{
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: StaffPositionFilledPostManager" + "[filedetailsave]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("StaffPositionFilledPostManager [filedetailsave]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
	public static void filedetailupdate(String machine, String name,int mastid,String userid, long size,String tablename,String attid,Connection conn,String desc){ 
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";
		int count=0;
		try{
			qry="UPDATE file_attachment SET file_name=?, filesize=? WHERE "
					+ " file_attachment_id =? and table_name= ? and reference_id=? ";
			psmt = conn.prepareStatement(qry);
			psmt.setString(1, name);
			psmt.setString(2, size+"");
			psmt.setString(3, attid);
			psmt.setString(4, tablename);
			psmt.setInt(5, mastid);
			count= psmt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION IS CAUSED BY: StaffPositionFilledPostManager" + "[filedetailupdate]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("StaffPositionFilledPostManager [filedetailupdate]", e.toString()));
		}
	  	
	}


	public static JSONArray getSaveDetail( String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		JSONArray arr = new JSONArray();

		try {
				conn = DBConnection.getConnection();
				String query = " select type,pf_id,file_name as file,concat(ps_mid,'/',type,'/')as ps_mid,type_name ,(select concat(b.file_name)"
					+ " from file_attachment b where reference_id=pf_id and b.file_name=a.file_name limit 1)as file_name"
					+ " from rsrch_position_filled_post a where ps_mid=? ";			
				psmt = conn.prepareStatement(query);
				psmt.setString(1, General.checknull(id));
				System.out.println("psmt:::"+psmt);
				rst = psmt.executeQuery();
			while (rst.next()) {
				//System.out.println("psmt:::"+rst.getString("type"));

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("pf_id", rst.getString("pf_id"));
				jsonObject.put("type", rst.getString("type"));
				jsonObject.put("ps_mid", rst.getString("ps_mid"));
				jsonObject.put("type_name", rst.getString("type_name"));
				jsonObject.put("file_name", rst.getString("file_name"));
				jsonObject.put("file", rst.getString("file"));
				arr.add(jsonObject);
			}
		} catch (Exception e) {
			System.out.println("Exception is: StaffPositionFilledPostManager[getSaveDetail]" + e.getMessage());
			l.fatal(Logging.logException("StaffPositionFilledPostManager[getSaveDetail]", e.toString()));

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
			}
		}
		return arr;
	}

}
