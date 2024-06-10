package com.sits.common;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import com.sits.conn.DBConnection;
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.common.ZipUtils;
import com.sits.general.ReadProps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
/**
 *
 * @author amit dangi 25 Oct
 * rsrchFilesDownload used for get the zip file for all the uploaded attachment
 * by a user for the given primry key and their table form file_attachment.
 * Work process is like first create a dynamic attachments folder then convert it 
 * to zip folder , after convert the zip folder delete their relevent folder and 
 * after download the zip to browser it will delete the zip folder also in parallel.
 */

public class rsrchFilesDownload {

	static Logger LOGGER = Logger.getLogger(rsrchFilesDownload.class);
	
	public static String downloadUploadedDoc(String FOLDER_NAME,String Xlocation,String Xddo,String param1,String param2,String param3,String param4,
											String param5,String param6,String param7,String param8){
		
		 String PROJECT_FOLDER="RSRCH/";
		 String directory_path  =ReadProps.getkeyValue("document.path", "sitsResource")+PROJECT_FOLDER+FOLDER_NAME+"/";
		 String OUTPUT_ZIP_FILE =ReadProps.getkeyValue("document.path", "sitsResource")+PROJECT_FOLDER+FOLDER_NAME+"/attachments.zip";
		 String SOURCE_FOLDER   =ReadProps.getkeyValue("document.path", "sitsResource")+PROJECT_FOLDER+FOLDER_NAME+"/attachments"; // SourceFolder path
		 
		 //System.out.println("------------"+directory_path+"SOURCE_FOLDER----------"+SOURCE_FOLDER);
        Connection conn = null;
        PreparedStatement pstmt=null;
        ResultSet rs = null;
        String sql="";
        try{
        	CreateFolder(directory_path,""); //Parent Folder created
        	conn = DBConnection.getConnection();
        	
        	if(FOLDER_NAME.equals("MOU")){
        	//Params for mou locationCode,ddoCode,inst_name,coll_type,mou_type,m_status
             sql = "select m_id,(select concat(file_attachment_id,'_',file_name) as file_name from"
            		+ " file_attachment where table_name='rsrch_mou_moa_form' and file_type='mou_moa_document'"
            		+ " and reference_id=rmf.m_id order by CREATED desc limit 1) as file_name from rsrch_mou_moa_form rmf"
            		+ " where 1=1";
            		
            		if(!General.checknull(Xlocation).equals("")){
            			sql+=" and rmf.location_code= '"+General.checknull(Xlocation)+"'";
            		}
            		if(!General.checknull(Xddo).equals("")){
            			sql+=" and rmf.ddo_id= '"+General.checknull(Xddo)+"'";
            		}
            		if(!General.checknull(param1).equals("%")){
            			sql+=" and rmf.inst_name= '"+General.checknull(param1)+"'";
            		}
            		if(!General.checknull(param2).equals("%")){
            			sql+=" and rmf.coll_type= '"+General.checknull(param2)+"'";
            		}
            		if(!General.checknull(param3).equals("%")){
            			sql+=" and rmf.mou_type= '"+General.checknull(param3)+"'";
            		}
            		if(!General.checknull(param4).equals("%")){
            			sql+=" and rmf.m_status= '"+General.checknull(param4)+"'";
            		}
            		if(!General.checknull(param5).equals("%") && !General.checknull(param5).equals("")){
            			sql+=" and date_format(rmf.signed_on,'%Y')= '"+General.checknull(param5)+"'";
            		}
        	}else if(FOLDER_NAME.equals("MEETING")){
        		//Params for mou locationCode,ddoCode,meeting_type_id
                sql = "select rmf.m_id,concat(file_attachment_id,'_',file_name) as file_name from file_attachment,rsrch_meeting_mapping rmf"
                		+ " where table_name='rsrch_meeting_mapping' and FIND_IN_SET(file_type,'upload_agenda,upload_Proceedings')"
                		+ " and reference_id=rmf.m_id";
               		
               		if(!General.checknull(Xlocation).equals("") && !General.checknull(Xlocation).equals("%")){
               			sql+=" and rmf.location_code= '"+General.checknull(Xlocation)+"'";
               		}
               		if(!General.checknull(Xddo).equals("") && !General.checknull(Xddo).equals("%")){
               			sql+=" and rmf.ddo_id= '"+General.checknull(Xddo)+"'";
               		}
               		if(!General.checknull(param1).equals("%")){
               			sql+=" and rmf.meeting_type_id= '"+General.checknull(param1)+"'";
               		}
               		if(!General.checknull(param2).equals("All")){
               			sql+=" and rmf.others= '"+General.checknull(param1)+"'";
               		}
               		if(!General.checknull(param3).equals("%")){
               			sql+=" and rmf.fin_yr= '"+General.checknull(param3)+"'";
               		}
        	}else if(FOLDER_NAME.equals("STUFORM")){
        		//Params for student_research_project_form_report locationCode,ddoCode,stu_name,course,discipline,research_thrust_area
                sql = "select rmf.proj_id as m_id,concat(file_attachment_id,'_',file_name) as file_name from file_attachment,student_research_project_form rmf"
                		+ " where table_name='student_research_project_form' and FIND_IN_SET(file_type,'student_research_project_doc')"
                		+ " and reference_id=rmf.proj_id ";
               		
               		if(!General.checknull(Xlocation).equals("") && !General.checknull(Xlocation).equals("%")){
               			sql+=" and rmf.location_code= '"+General.checknull(Xlocation)+"'";
               		}
               		if(!General.checknull(Xddo).equals("") && !General.checknull(Xddo).equals("%")){
               			sql+=" and rmf.ddo_id= '"+General.checknull(Xddo)+"'";
               		}
               		if(!General.checknull(param1).equals("%") && !General.checknull(param1).equals("")){
               			sql+=" and rmf.stu_name= '"+General.checknull(param1)+"'";
               		}
               		if(!General.checknull(param2).equals("%") && !General.checknull(param2).equals("")){
               			sql+=" and rmf.course= '"+General.checknull(param2)+"'";
               		}
               		if(!General.checknull(param3).equals("%") && !General.checknull(param3).equals("")){
               			sql+=" and rmf.discipline= '"+General.checknull(param3)+"'";
               		}
               		if(!General.checknull(param4).equals("%") && !General.checknull(param4).equals("")){
               			sql+=" and rmf.research_thrust_area= '"+General.checknull(param4)+"'";
               		}
               		if(!General.checknull(param5).equals("%") && !General.checknull(param5).equals("")){
               			sql+=" and rmf.fin_year= '"+General.checknull(param5)+"'";
               		}
               		if(!General.checknull(param6).equals("%") && !General.checknull(param6).equals("")){
               			sql+=" and rmf.status= '"+General.checknull(param6)+"'";
               		}
        	}else if(FOLDER_NAME.equals("PATENT")){
        		//Params for student_research_project_form_report locationCode,ddoCode,stu_name,course
                sql = "select rmf.pat_id as m_id,concat(file_attachment_id,'_',file_name) as file_name from file_attachment,rsrch_patent_mast rmf"
                		+ " where table_name='rsrch_patent_mast' and FIND_IN_SET(file_type,'Patent_Certificate')"
                		+ " and reference_id=rmf.pat_id ";
               		
               		if(!General.checknull(Xlocation).equals("") && !General.checknull(Xlocation).equals("%")){
               			sql+=" and rmf.location_code= '"+General.checknull(Xlocation)+"'";
               		}
               		if(!General.checknull(Xddo).equals("") && !General.checknull(Xddo).equals("%")){
               			sql+=" and rmf.ddo_id= '"+General.checknull(Xddo)+"'";
               		}
               		if(!General.checknull(param1).equals("%")){
               			sql+=" and rmf.pat_status= '"+General.checknull(param1)+"'";
               		}
               		if(!General.checknull(param2).equals("%")){
               			sql+=" and rmf.applicant_name= '"+General.checknull(param2)+"'";
               		}
               		if(!General.checknull(param3).equals("%")){
               			sql+=" and rmf.pat_title= '"+General.checknull(param3)+"'";
               		}
               		if(!General.checknull(param4).equals("%")){
               			sql+=" and rmf.pat_cat= '"+General.checknull(param4)+"'";
               		}
               		if(!General.checknull(param5).equals("%")){
               			sql+=" and rmf.pat_type= '"+General.checknull(param5)+"'";
               		}
               		if(!General.checknull(param6).equals("%") && !General.checknull(param6).equals("")){
               			sql+=" and rmf.fin_yr= '"+General.checknull(param6)+"'";
               		}
        	}else if(FOLDER_NAME.equals("TARGET")){
        		//Params for student_research_project_form_report locationCode,ddoCode,stu_name,course
        		  sql = "select reference_id as m_id,max(CREATED) ,concat(file_attachment_id,'_',file_name) as file_name from file_attachment fa,rsrch_form1_mast rfm "
        		  		+ "left join rsrch_target_objective_achievement_details c on rfm.PS_MID=c.PS_MID  where "
        		  		+ "table_name='rsrch_target_objective_achievement_details' and fa.reference_id=c.TA_ID  ";
                 		
  	                if(!General.checknull(Xlocation).equals("") && !General.checknull(Xlocation).equals("%")){
  	           			sql+=" and rfm.location_code= '"+General.checknull(Xlocation)+"'";
  	           		}
  	           		if(!General.checknull(Xddo).equals("") && !General.checknull(Xddo).equals("%")){
  	           			sql+=" and rfm.ddo_id= '"+General.checknull(Xddo)+"'";
  	           		}
                 		if(!General.checknull(param1).equals("%") && !General.checknull(param1).equals("")){
                 			sql+=" and date_format(rfm.submitted_date,'%Y')= '"+General.checknull(param1)+"'";
                 		}
                 		if(!General.checknull(param2).equals("%") && !General.checknull(param2).equals("")){
                 			sql+=" and rfm.projtype= '"+General.checknull(param2)+"'";
                 		}
                 		if(!General.checknull(param3).equals("%") && !General.checknull(param3).equals("")){
                 			sql+=" and rfm.fn_agency= '"+General.checknull(param3)+"'";
                 		}
                 		if(!General.checknull(param4).equals("%") && !General.checknull(param4).equals("")){
                 			sql+=" and rfm.PS_MID= '"+General.checknull(param4)+"'";
                 		}
                 		
                 		sql +=" group by reference_id,file_type";
                 		
        	}else if(FOLDER_NAME.equals("UC")){
        		/*Params FOLDER_NAME,locationCode,ddoCode,fin_yr,projtype,fn_agency,"","","","","" 
        		for utilization_certificate_status_report */
                sql = "select reference_id as m_id,max(CREATED) ,concat(file_attachment_id,'_',file_name) as file_name "
                		+ "from file_attachment fa,rsrch_form1_mast rfm where table_name='utilization_certificate' and FIND_IN_SET(file_type,'UC_Certificate,AUC_Certificate') "
                		+ "and fa.reference_id=rfm.PS_MID ";
               		
	                if(!General.checknull(Xlocation).equals("") && !General.checknull(Xlocation).equals("%")){
	           			sql+=" and rfm.location_code= '"+General.checknull(Xlocation)+"'";
	           		}
	           		if(!General.checknull(Xddo).equals("") && !General.checknull(Xddo).equals("%")){
	           			sql+=" and rfm.ddo_id= '"+General.checknull(Xddo)+"'";
	           		}
               		if(!General.checknull(param1).equals("%") && !General.checknull(param1).equals("")){
               			sql+=" and date_format(rfm.submitted_date,'%Y')= '"+General.checknull(param1)+"'";
               		}
               		if(!General.checknull(param2).equals("%") && !General.checknull(param2).equals("")){
               			sql+=" and rfm.projtype= '"+General.checknull(param2)+"'";
               		}
               		if(!General.checknull(param3).equals("%") && !General.checknull(param3).equals("")){
               			sql+=" and rfm.fn_agency= '"+General.checknull(param3)+"'";
               		}
               		
               		if(!General.checknull(param4).equals("%") && !General.checknull(param4).equals("")){
               			sql+=" and rfm.PS_MID= '"+General.checknull(param4)+"'";
               		}
               		sql +=" group by reference_id,file_type";	
        	}else if(FOLDER_NAME.equals("LAND_RECORD")){
        		/*Params FOLDER_NAME,locationCode,ddoCode,deptId,projtype,fn_agency,"","","","","" 
        		for land_record_report used table rsrch_land_record & rsrch_land_record_detail */
                sql = "select m_id,file_name from (select LOCATION_CODE,DDO_ID,DEPT_ID,reference_id as m_id,max(CREATED) ,file_name from file_attachment fa,rsrch_land_record rld "
            		+ "where table_name='rsrch_land_record' and fa.reference_id=rld.ld_id  group by reference_id "
            		+ "union "
            		+ "select LOCATION_CODE,DDO_ID,DEPT_ID,rlr.ld_id as m_id,'' ,file_name from rsrch_land_record rlr,rsrch_land_record_detail rlrd where  rlr.ld_id=rlrd.ld_id ) as details where 1=1 ";
               		
	                if(!General.checknull(Xlocation).equals("") && !General.checknull(Xlocation).equals("%")){
	           			sql+=" and location_code= '"+General.checknull(Xlocation)+"'";
	           		}
	           		if(!General.checknull(Xddo).equals("") && !General.checknull(Xddo).equals("%")){
	           			sql+=" and ddo_id= '"+General.checknull(Xddo)+"'";
	           		}
               		if(!General.checknull(param1).equals("%") && !General.checknull(param1).equals("")){
               			sql+=" and DEPT_ID= '"+General.checknull(param1)+"'";
               		}
        	}else if(FOLDER_NAME.equals("SPECIES")){
        		/*Params FOLDER_NAME,locationCode,ddoCode,,"","","","","" 
        		for rsrch_species_breed_variety_mast used table rsrch_land_record & rsrch_land_record_detail */
                sql = "select rmf.s_id as m_id,concat(file_attachment_id,'_',file_name) as file_name from file_attachment,rsrch_species_breed_variety_mast rmf "
                		+ "where table_name='rsrch_species_breed_variety_mast' and reference_id=rmf.s_id ";
               		
	                if(!General.checknull(Xlocation).equals("") && !General.checknull(Xlocation).equals("%")){
	           			sql+=" and rmf.location_code= '"+General.checknull(Xlocation)+"'";
	           		}
	           		if(!General.checknull(Xddo).equals("") && !General.checknull(Xddo).equals("%")){
	           			sql+=" and rmf.ddo_id= '"+General.checknull(Xddo)+"'";
	           		}
	           		if(!General.checknull(param1).equals("") && !General.checknull(param1).equals("%")){
	           			sql+=" and rmf.fn_agency= '"+General.checknull(param1)+"'";
	           		}
	           		if(!General.checknull(param2).equals("") && !General.checknull(param2).equals("%")){
	           			sql+=" and rmf.relese_date= '"+General.checknull(param2)+"'";
	           		}
	           		if(!General.checknull(param3).equals("") && !General.checknull(param3).equals("%")){
	           			sql+=" and rmf.status= '"+General.checknull(param3)+"'";
	           		}
        	}else if(FOLDER_NAME.equals("Commercialization")){
        		/*Params FOLDER_NAME,locationCode,ddoCode,type,comm_type,rsrchcat,rsrchsubcat
        		for Commercialization_Licensing_mast used table Commercialization_Licensing_mast */
                sql = "select rmf.id as m_id,concat(file_attachment_id,'_',file_name) as file_name from file_attachment,Commercialization_Licensing_mast rmf "
                		+ "where table_name='Commercialization_Licensing_mast' and reference_id=rmf.id ";
               		
	                if(!General.checknull(Xlocation).equals("") && !General.checknull(Xlocation).equals("%")){
	           			sql+=" and rmf.location_code= '"+General.checknull(Xlocation)+"'";
	           		}
	           		if(!General.checknull(Xddo).equals("") && !General.checknull(Xddo).equals("%")){
	           			sql+=" and rmf.ddo_id= '"+General.checknull(Xddo)+"'";
	           		}
	           		if(!General.checknull(param1).equals("") && !General.checknull(param1).equals("%")){
	           			sql+=" and rmf.type= '"+General.checknull(param1)+"'";
	           		}
	           		if(!General.checknull(param2).equals("") && !General.checknull(param2).equals("%")){
	           			sql+=" and rmf.comm_type= '"+General.checknull(param2)+"'";
	           		}
	           		if(!General.checknull(param3).equals("") && !General.checknull(param3).equals("%")){
	           			sql+=" and rmf.rsrchcat= '"+General.checknull(param3)+"'";
	           		}
	           		if(!General.checknull(param4).equals("") && !General.checknull(param4).equals("%")){
	           			sql+=" and rmf.rsrchsubcat= '"+General.checknull(param3)+"'";
	           		}
        	}else if(param1.equals("fund_utilization_report")){
        		/*Params FOLDER_NAME,locationCode,ddoCode gettting the 
        		 * sanction orders uploaded in table rsrch_funding_agency_approval */
                sql = "select PS_MID as m_id,concat(PS_MID,'_',uploadedfile) as file_name from ("
                		+ "select fin_yr,faa_id,a.LOCATION_CODE,a.ddo_id,PS_TITTLE_PROJ,a.PS_MID,fa_id,(select "
                		+ "file_name from file_attachment where table_name='rsrch_funding_agency_approval' and reference_id=a.PS_MID AND "
                		+ "date_format(CREATED,'%m-%d-%Y-%h-%i')=date_format(a.CREATED_DATE,'%m-%d-%Y-%h-%i') order by CREATED desc limit 1) as uploadedfile from "
                		+ "rsrch_funding_agency_approval a ,rsrch_form1_mast b WHERE 1=1 and a.PS_MID=b.PS_MID and faa_id in (select max(faa_id) AS maxfaa_id from "
                		+ "rsrch_funding_agency_approval group by location_code,ddo_id,PS_MID,fin_yr)  order by a.CREATED_DATE desc "
                		+ ") as trgtable where uploadedfile is not null ";
               		
	                if(!General.checknull(Xlocation).equals("") && !General.checknull(Xlocation).equals("%")){
	           			sql+=" and trgtable.location_code= '"+General.checknull(Xlocation)+"'";
	           		}
	           		if(!General.checknull(Xddo).equals("") && !General.checknull(Xddo).equals("%")){
	           			sql+=" and trgtable.ddo_id= '"+General.checknull(Xddo)+"'";
	           		}
	           		if(!General.checknull(param2).equals("%") && !General.checknull(param2).equals("")){
               			sql+=" and trgtable.fin_yr= '"+General.checknull(param2)+"'";
               		}
        	}
             pstmt = conn.prepareStatement(sql);
             //System.out.println("pstmt-"+pstmt);
             rs = pstmt.executeQuery();
             if(rs.next()){
            	 do{
            	String reference_id=rs.getString("m_id");
            	String file_name=rs.getString("file_name");
            	//System.out.println("file_name--------"+file_name);
                if(CreateFolder(directory_path,"attachments")){
                	 Path sourcePath = Paths.get(directory_path+"/"+reference_id+"/"+file_name);
                     Path destinationPath = Paths.get(directory_path+"/attachments/"+file_name);
                	 try {
                         // Use the Files.copy method to copy the file
                         Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                         //System.out.println("File copied successfully.");
                     } catch (IOException e) {
                         System.err.println("Error copying the file: " + e.getMessage());
                     }
	            }  
	            else{
	           	 System.out.println("Unable to copy files.");	
	           }
            } while (rs.next()); 
             //System.out.println("SOURCE_FOLDER||"+SOURCE_FOLDER);
          //Create a zip file by ZipUtils require the two string OUTPUT_ZIP_FILE where to create zip ,SOURCE_FOLDER from which to create zip
           	ZipUtils zu=new ZipUtils();
            SOURCE_FOLDER =	SOURCE_FOLDER.replaceAll("[/\\\\]+", "/");
           	zu.generateFileList(new File(SOURCE_FOLDER),SOURCE_FOLDER);
           	zu.zipIt(OUTPUT_ZIP_FILE,SOURCE_FOLDER);
           	
           	// Delete the download folder after zip
           	zu.deleteFolder(new File(SOURCE_FOLDER));
             }else{
            	 OUTPUT_ZIP_FILE="Doucments not found for selected criteria."; 
             }
        } catch (Exception e) {
			System.out.println("Error in rsrchFilesDownload[downloadUploadedDoc] : "+e.getMessage());
			LOGGER.fatal(Logging.logException("rsrchFilesDownload[downloadUploadedDoc]", e.getMessage().toString()));
		} finally {
				try {
					if(pstmt != null) pstmt = null;
					if(rs != null) rs = null;
					if(conn != null) conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}			
		}
			return OUTPUT_ZIP_FILE;
		
	}
	
	public static boolean CreateFolder(String directory_path,String foldername) {
 	   String folderPath = directory_path+foldername;
        File folder = new File(folderPath);

        if (!folder.exists()) {
            boolean success = folder.mkdir();
            if (success) {
                //System.out.println("Folder created successfully!"+folderPath);
                return true;
            } else {
                //System.out.println("Failed to create folder!"+folderPath);
                return false;
            }
            
        } else {
            //System.out.println("Folder already exists!");
            return true;
        }
 	
	}
	
}
