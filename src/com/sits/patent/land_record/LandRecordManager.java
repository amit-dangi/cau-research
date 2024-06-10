package com.sits.patent.land_record;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

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


public class LandRecordManager {
	static Logger l = Logger.getLogger("exceptionlog CAU Research Patent"); 
	public static LandRecordModel save(LandRecordModel model,  List<FileItem> items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null,pstmt=null,ustPstmt=null,updetPstmt=null;
		ResultSet rst = null;
		String qry = "", msg="0",lid="",uplqry="",detqry="";
		int chk[]=null,i=0,cnt1=0,cnt=0, k=0,p=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String qry1 = "SELECT LPAD(CONVERT(IFNULL(MAX(SUBSTR(ld_id,3,6)),0)+1,SIGNED INTEGER),4,'0') FROM rsrch_land_record";
			psmt = conn.prepareStatement(qry1);
			rst=psmt.executeQuery();
			if(rst.next()){
				lid="LR"+General.checknull(rst.getString(1));
			}
			qry="INSERT INTO rsrch_land_record (ld_id,LOCATION_CODE,DDO_ID,DEPT_ID,PATTA_NO,OTHERS,CREATED_BY, CREATED_DATE,CREATED_MACHINE) VALUES (?,?,?,?,?,?,?,now(),?)";			
			psmt=null;
			psmt = conn.prepareStatement(qry);
			
			uplqry="UPDATE rsrch_land_record SET LOCATION_CODE=?,DDO_ID=?,DEPT_ID=?, PATTA_NO=?, "
					+ "OTHERS=?,UPDATED_BY=?, UPDATED_MACHINE=?,UPDATED_DATE=now() where ld_id=?  ";
			ustPstmt = conn.prepareStatement(uplqry);
			if(mode.equals("N")){
				psmt.setString(++i, General.checknull(lid));
				psmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
				psmt.setString(++i, General.checknull(model.getDDO_ID()));
				psmt.setString(++i, General.checknull(model.getDept()));
				psmt.setString(++i, General.checknull(model.getPatta_no()));
				psmt.setString(++i, General.checknull(model.getOther()));
				psmt.setString(++i, user_id);
				psmt.setString(++i, machine);
				//System.out.println("psmt"+psmt);
				 cnt = psmt.executeUpdate();
			}else{
			ustPstmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
			ustPstmt.setString(++i, General.checknull(model.getDDO_ID()));
			ustPstmt.setString(++i, General.checknull(model.getDept()));
			ustPstmt.setString(++i, General.checknull(model.getPatta_no()));
			ustPstmt.setString(++i, General.checknull(model.getOther()));
			ustPstmt.setString(++i, user_id);
			ustPstmt.setString(++i, machine);
			ustPstmt.setString(++i, model.getLd_id());
			cnt = ustPstmt.executeUpdate();
			
			}
		 //System.out.println("cnt"+cnt);
			if (cnt > 0){

				ArrayList<LandRecordModel> al = (ArrayList<LandRecordModel>) model.getList();
				String query = "";
				query="insert into rsrch_land_record_detail (ld_id,REV_PAY_DATE,file_name,CREATED_BY, CREATED_DATE,CREATED_MACHINE) VALUES (?,?,?,?,now(),?)";			
				pstmt = conn.prepareStatement(query);
				detqry="update  rsrch_land_record_detail set REV_PAY_DATE=?,";
							detqry+="file_name=?,";
						detqry+="UPDATED_BY=?, UPDATED_MACHINE=?,UPDATED_DATE=now() where det_id=?  ";
				updetPstmt = conn.prepareStatement(detqry);
				
				for (int t = 0; t < al.size(); t++) {
					LandRecordModel mdl = (LandRecordModel) al.get(t); 
				if (mdl.getDetail_id().equals("")) {
					i=0;
					if (mode.equals("N"))
						
						pstmt.setString(++i,  lid);
					else
				pstmt.setString(++i,model.getLd_id());
				pstmt.setString(++i, General.formatDate(mdl.getRevenue_date()).equals("") ? null:General.formatDate(mdl.getRevenue_date()));
				if(items!=null ){
					if(mode.equals("E")){
						k=t-p;
						System.out.println("k"+k);
					}else{
					 k=t+1;}
					if(items.get(k).getName()!=null){
					pstmt.setString(++i, items.get(k).getName());				
				}}
				pstmt.setString(++i, user_id);
				pstmt.setString(++i, machine);
				cnt1 = pstmt.executeUpdate();
				
				}else{
					i=0;
					++p;
					updetPstmt.setString(++i, General.formatDate(mdl.getRevenue_date()).equals("") ? null:General.formatDate(mdl.getRevenue_date()));
					if(items!=null && items.equals("")){
						//System.out.println("fileItem"+	items.get(t).getName());
						updetPstmt.setString(++i, items.get(t).getName());				
					}else{
						updetPstmt.setString(++i, mdl.getItem_des());	
					}
					updetPstmt.setString(++i, user_id);
					updetPstmt.setString(++i, machine);
					updetPstmt.setString(++i,mdl.getDetail_id());
					cnt1 = updetPstmt.executeUpdate();
				}
				}
				}
				if(cnt1>0||cnt > 0){
				if(items!=null){
					Iterator<FileItem> iter = items.iterator();
						while (iter.hasNext()) {
							FileItem fileItem = iter.next();
							saveDoc(machine, lid, user_id,conn, fileItem);	
												}
							}
				conn.commit();
				if (mode.equals("N"))
					model.setErrMsg(ApplicationConstants.SAVE);
				else
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
			l.fatal(Logging.logException("LandRecordManager[save]", e.getMessage().toString()));
			System.out.println("Error in LandRecordManager[save] : " + e.getMessage());
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
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/LAND_RECORD/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+"/"+"" +fileItem.getName());
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in LandRecordManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("LandRecordManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}
	
	//This method is used for save attachment name in file_attachment table
	public static synchronized String saveFileattachment(String machine, FileItem fileItem, String id, String userid, Connection conn){ 
		PreparedStatement psmt = null;
		String qry = "";
		String attachid="",file_type="",tableName="";
		int count=0;
		try{
			qry="INSERT INTO file_attachment ( file_name, file_type, table_name, reference_id, CREATED_BY, CREATED, MACHINE) VALUES "
			+ "( ? , ?, ?, ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			 if(fileItem.getFieldName().equals("upload_doc")){
					file_type="land_record_document";
					tableName="rsrch_land_record";
				}else if(fileItem.getFieldName().equals("upload_documents")){
					file_type="land_record_payment_receipt";
					tableName="rsrch_land_record_detail";
				}
			
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, file_type);
			psmt.setString(3, tableName);
			psmt.setString(4, id);
			psmt.setString(5, userid);
			psmt.setString(6, machine);
			count= psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				attachid = rs.getString(1);
			}
			 
		}
		catch(Exception e)
		{
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: LandRecordManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("LandRecordManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
	//This method is used for get List on l page
		public static ArrayList<LandRecordModel> getList(String ld_id,String x_location,String x_patta){
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
				query = "select ld_id,LOCATION_CODE,DDO_ID,DEPT_ID,PATTA_NO,OTHERS,(select distinct file_name from file_attachment where reference_id=ld_id and table_name='rsrch_land_record')as upl_doc from rsrch_land_record where 1=1";
				if(!ld_id.trim().equals("")) {
					query+=" AND ld_id= '"+ld_id+"' ";
				}else if(!x_location.trim().equals("") ) {
					query+=" AND LOCATION_CODE ='" + x_location+"' ";
				}else if(!x_patta.trim().equals("")) {
					query+=" AND PATTA_NO ='" + x_patta+"' ";
				}
				psmt = conn.prepareStatement(query);
				//System.out.println("query---------------"+psmt);
				rst = psmt.executeQuery();
				while (rst.next()) {
					LandRecordModel model = new LandRecordModel();
					
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
					
					model.setLd_id(General.checknull(rst.getString("ld_id")));
					model.setLOCATION_CODE(General.checknull(rst.getString("LOCATION_CODE")));
					model.setDDO_ID(General.checknull(rst.getString("DDO_ID")));
					model.setDept(General.checknull(rst.getString("DEPT_ID")));
					model.setPatta_no(General.checknull(rst.getString("PATTA_NO")));
					model.setOther(General.checknull(rst.getString("OTHERS")));
					model.setUploaded_file(General.checknull(rst.getString("upl_doc")));
					al.add(model);
				}
			} catch (Exception e) {
				System.out.println(e);
				l.fatal(Logging.logException("LandRecordManager[getList]", e.toString()));
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
					l.fatal(Logging.logException("LandRecordManager[getList]", sql.toString()));
				}
			}
			return al;
		}
		
		/*This method will be used to delete data form 
		file_attachment & rsrch_mou_moa_form from database
		as well as delete the whole folder for the given id*/
		@SuppressWarnings("resource")
		public static LandRecordModel delete(String id,String fname,String user_id,String ip) {
			Connection conn = null;
			PreparedStatement psmt = null,pstmt=null;
			ResultSet rst = null;		
			String query = "",str="",qry="",ld_id="";
			int count = 0,cntTable=0,cnt=0,chk=0;
			LandRecordModel model=new LandRecordModel();
			try {
				
				conn = DBConnection.getConnection();
				conn.setAutoCommit(false);
				
				qry="select ld_id from  rsrch_land_record_detail where ld_id='"+id+"' ";
				pstmt = conn.prepareStatement(qry);
				//System.out.println("pstmt"+pstmt);
				rst= pstmt.executeQuery();
				while (rst.next()) {
					ld_id= General.checknull(rst.getString(1));
				}
				//System.out.println("chk in chk"+chk);
				if(!ld_id.equals("")){
				conn = General.updtDeletedData("rsrch_land_record_detail", "ld_id", "", "", "", "","",
			    		id, "", "", "","", "", ip, user_id,"",conn);
				}if (conn != null) {
				query = "delete from file_attachment where table_name='rsrch_land_record' and file_type='land_record_document' AND reference_id='"+id+"'";
				psmt = conn.prepareStatement(query);
				count = psmt.executeUpdate();
				//System.out.println("1 in manager"+count);
					if(count > 0){	
						qry="";pstmt=null;rst=null;
					qry="delete from  rsrch_land_record_detail  where  ld_id='"+id+"'";
					pstmt = conn.prepareStatement(qry);
					//System.out.println("pstmt"+pstmt);
					cnt= pstmt.executeUpdate();
					//System.out.println("2 in manager"+cnt);
					}
					}
				//if(cnt>0){
						conn = General.updtDeletedData("rsrch_land_record", "ld_id", "", "", "", "","",
					    		id, "", "", "","", "", ip, user_id,"",conn);
						if (conn != null) {
					psmt=null;query="";
					query = "delete from rsrch_land_record where ld_id='"+id+"'";
					psmt = conn.prepareStatement(query);
					cntTable = psmt.executeUpdate();
					System.out.println("3 in manager"+cntTable);
				//}
						}if(cntTable>0){
					String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/LAND_RECORD/"+id;
					
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
				l.fatal(Logging.logException("LandRecordManager[delete]", e.getMessage().toString()));
				System.out.println("Error in LandRecordManager[delete] : " + e.getMessage());
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
		//method to delete attachment
				public static String deleteDetailData(String id,String file_name,String det_id,String user_id, String ip) {
				String cSql="",msg="",sql="";
				Connection conn = null;
				PreparedStatement pstmt = null,psmt=null;
				
				ResultSet rst = null;
				int count=0,cnt=0;
				try {
					conn = DBConnection.getConnection();
					conn.setAutoCommit(false);
					conn = General.updtDeletedData("rsrch_land_record_detail", "det_id", "", "", "", "","",
							det_id, "", "", "","", "", ip, user_id,"",conn);
					if (conn != null) {
					sql="delete from rsrch_land_record_detail where det_id='"+det_id+"' and file_name='"+file_name+"'";
					psmt = conn.prepareStatement(sql);
					cnt= psmt.executeUpdate();
					System.out.println("cpsmtnt"+psmt);
					}
					if(cnt>0)
					{
						String directoryName=ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/LAND_RECORD"+"/"+id+"/"+file_name+"";
						//System.out.println("directoryName:::"+directoryName);
						File file = new File(directoryName);
						file.delete();
						msg=ApplicationConstants.DELETED;
						conn.commit();
					}		
					
				} catch (Exception e) {
					System.out.println("Error in LandRecordManager[deletattchdata] : "+e.getMessage());
					l.fatal(Logging.logException("LandRecordManager[deletattchdata]", e.getMessage().toString()));
				} finally {
					try {
						if(pstmt != null) pstmt = null;
						if(rst != null) rst = null;
						if(conn != null) conn.close();
						if(cSql!=null)
							cSql=null;
					} catch (Exception e) {
						e.printStackTrace();
					}			
				}
				return msg;
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
						String directoryName=ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/LAND_RECORD"+"/"+upload_id+"/"+file_name+"";
						//System.out.println("directoryName:::"+directoryName);
						File file = new File(directoryName);
						file.delete();
						msg=ApplicationConstants.DELETED;
						conn.commit();
					}		
					
				} catch (Exception e) {
					System.out.println("Error in LandRecordManager[deletattchdata] : "+e.getMessage());
					l.fatal(Logging.logException("LandRecordManager[deletattchdata]", e.getMessage().toString()));
				} finally {
					try {
						if(pstmt != null) pstmt = null;
						if(rst != null) rst = null;
						if(conn != null) conn.close();
						if(cSql!=null)
							cSql=null;
					} catch (Exception e) {
						e.printStackTrace();
					}			
				}
				return msg;
			}
				@SuppressWarnings("unchecked")
				public static JSONArray viewRecordDetails(String ld_id) {
					JSONArray arr = new JSONArray();
					String cSql = "";
					Connection conn = null;
					PreparedStatement pstmt = null;
					ResultSet rst = null;
					try {
						conn = DBConnection.getConnection();
						cSql = "select det_id,ld_id,date_format(REV_PAY_DATE,'%d/%m/%Y')AS REV_PAY_DATE,file_name "
								+ " FROM rsrch_land_record_detail d WHERE ld_id=? ";
						pstmt = conn.prepareStatement(cSql);
						pstmt.setString(1, ld_id.toUpperCase().trim());
						//System.out.println("detail FIRST:: "+pstmt);
						rst = pstmt.executeQuery();
						if (rst.next() == false) {
						} else {
							do { 
								JSONObject obj = new JSONObject();
								obj.put("det_id", General.checknull(rst.getString("det_id")));
								obj.put("ld_id", General.checknull(rst.getString("ld_id")));
								obj.put("file_name", General.checknull(rst.getString("file_name")));
								obj.put("REV_PAY_DATE", General.checknull(rst.getString("REV_PAY_DATE")));
								arr.add(obj);
							} while (rst.next());
						}
					} catch (Exception e) {
						System.out.println("Error in LandRecordManager[viewRecordDetails] : " + e.getMessage());
						l.fatal(Logging.logException("LandRecordManager[viewRecordDetails]", e.getMessage().toString()));
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
					return arr;
				} 
		
}
