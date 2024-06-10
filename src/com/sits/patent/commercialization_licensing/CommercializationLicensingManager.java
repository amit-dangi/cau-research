package com.sits.patent.commercialization_licensing;

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

public class CommercializationLicensingManager {
	static Logger l = Logger.getLogger("exceptionlog CAU Research"); 
	/*This method is used for save  data .In this function passing 5 parameters(model,items,user_id,machine,mode) and will return String*/
	public static CommercializationLicensingModel save(CommercializationLicensingModel model,  FileItem items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0",mid="";
		int chk=0,i=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			qry="INSERT INTO Commercialization_Licensing_mast (type,application_no,title, cau_comm_num, comm_no, comm_type,category,"
					+ " sub_category, sec_party, from_date, to_date, duration,amt_of_comm,LOCATION_CODE,DDO_ID,"
					+ "`CREATED_BY`, `CREATED_MACHINE`, `CREATED_DATE`) VALUES (?,?, ?, ?, ?, ?, ?, ?, "//+ "?, "
					+ "?, ?, ?, ?, ?, ?, ?,?,?,now())";			
		
			psmt = conn.prepareStatement(qry,psmt.RETURN_GENERATED_KEYS);
			
			psmt.setString(++i, General.checknull(model.getTyp()));
			psmt.setString(++i, General.checknull(model.getApp_no()));
			psmt.setString(++i, General.checknull(model.getTitle()));
			psmt.setString(++i, General.checknull(model.getCau_comm_num()));
			psmt.setString(++i, General.checknull(model.getComm_no()));
			psmt.setString(++i, General.checknull(model.getComm_type()));
			psmt.setString(++i, (model.getResh_category().equals("")?null:model.getResh_category()));
			psmt.setString(++i, General.checknull(model.getSub_category()));
			psmt.setString(++i, General.checknull(model.getSec_party()));
			psmt.setString(++i, General.formatDate(model.getDate()));
			psmt.setString(++i, General.formatDate(model.getEnd_date()));
			psmt.setString(++i, General.checknull(model.getDuration()));
			psmt.setString(++i, General.checknull(model.getAmt_of_comm()));
			psmt.setString(++i, General.checknull(model.getLocation()));
			psmt.setString(++i, General.checknull(model.getDdo()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			//System.out.println("CommercializationLicensingManager save- "+psmt);
			int cnt = psmt.executeUpdate();
			ResultSet rs = psmt.getGeneratedKeys();

			if (rs.next()) {
				mid = rs.getString(1);
			}
			if (cnt > 0){
				if(items!=null ){
						saveDoc(machine,"", mid, user_id,conn, items);						
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
			l.fatal(Logging.logException("CommercializationLicensingManager[save]", e.getMessage().toString()));
			System.out.println("Error in CommercializationLicensingManager[save] : " + e.getMessage());
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
	//This function is using for make directory using mkdirs() and save the document in that directory
	public static boolean saveDoc(String machine,String upl_id,  String id, String userid,Connection conn, FileItem fileItem){		
		java.io.File file;
		try{
			String attachid=saveFileattachment(machine, fileItem, id, userid, conn);
			deletePreviousattchments(upl_id,conn);
			if(!attachid.equals("")){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/Commercialization/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
			}
	  	}catch(Exception e){
	  		System.out.println("Error in CommercializationLicensingManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("CommercializationLicensingManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}
	//This function is used for save attachment name in file_attachment table
	public static String saveFileattachment(String machine, FileItem fileItem, String id, String userid, Connection conn){ 
		PreparedStatement psmt = null;
		String qry = "";
		String attachid="";
		int count=0;
		try{
			qry="INSERT INTO file_attachment ( file_name, file_type, table_name, reference_id, CREATED_BY, CREATED, MACHINE) VALUES "
			+ "( ? , ?, 'Commercialization_Licensing_mast', ?, ?,now(), ?); ";
			psmt = conn.prepareStatement(qry, psmt.RETURN_GENERATED_KEYS);
			psmt.setString(1, fileItem.getName());
			psmt.setString(2, "Upload_Commerc");
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
			System.out.println("EXCEPTION IS CAUSED BY: CommercializationLicensingManager" + "[saveFileattachment]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("CommercializationLicensingManager [saveFileattachment]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	//This function is used for get List on l page
	public static ArrayList<CommercializationLicensingModel> getList(String type,String location,String ddo,String user_id){
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		//JSONObject finalObject=new JSONObject();
		
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
			query = "select id,(select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='Commercialization_Licensing_mast' and file_type='Upload_Commerc' and reference_id=a.id order by CREATED desc limit 1) as upld,type, category,application_no,title,LOCATION_CODE,DDO_ID from Commercialization_Licensing_mast a WHERE 1=1 ";
			if(!type.trim().equals("")) {
				query+=" AND type= '"+type+"' ";
			}else if(!location.trim().equals("")) {
				query+=" AND LOCATION_CODE ='" + location+"' ";
			}else if(!ddo.trim().equals("")){
				query+="AND DDO_ID = '" + ddo +"'";
			}else if(!user_id.trim().equals("ADMIN")) {
				query+=" AND CREATED_BY ='" + user_id+"' ";
			}
			psmt = conn.prepareStatement(query);
			//System.out.println("psmt"+psmt);
			rst = psmt.executeQuery();
			while (rst.next()) {
				CommercializationLicensingModel model = new CommercializationLicensingModel();
			
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
				model.setId(General.checknull(rst.getString("id")));
				model.setTyp(General.checknull(rst.getString("type")));
				model.setTitle(General.checknull(rst.getString("title")));
				model.setApp_no(General.checknull(rst.getString("application_no")));
				model.setUpload(General.checknull(rst.getString("upld")));
				model.setDdo_id(General.checknull(rst.getString("DDO_ID")));
				model.setLocation_id(General.checknull(rst.getString("LOCATION_CODE")));
				al.add(model);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("CommercializationLicensingManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("CommercializationLicensingManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	
	//This method is using to delete from database
	public static CommercializationLicensingModel delete(String id,String fname) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "",str="";
		int count = 0,cntTable=0;
		CommercializationLicensingModel model=new CommercializationLicensingModel();
		try {
			
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			query = "delete from file_attachment where table_name='Commercialization_Licensing_mast' and file_type='Upload_Commerc' AND reference_id='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			
			if(count > 0){	
				psmt=null;query="";
				query = "delete from Commercialization_Licensing_mast where id='"+id+"'";
				psmt = conn.prepareStatement(query);
				cntTable = psmt.executeUpdate();
			}if(cntTable>0){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/Commercialization/"+id+"/"+fname;
				File file = new File(directoryName);
				if(file.exists()){
					file.delete();
				}
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
			l.fatal(Logging.logException("CommercializationLicensingManager[delete]", e.getMessage().toString()));
			System.out.println("Error in CommercializationLicensingManager[delete] : " + e.getMessage());
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
	
	//This function is using for view the data during edit record
	public static ArrayList<CommercializationLicensingModel> getEditList(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();			
			query = "SELECT *, (select concat(file_attachment_id,'_',file_name) as file_name from file_attachment where table_name='Commercialization_Licensing_mast' and file_type='Upload_Commerc' and reference_id=a.id order by CREATED desc limit 1) as upld,date_format(from_date, '%d/%m/%Y') as date,date_format(to_date, '%d/%m/%Y') as end_date  FROM Commercialization_Licensing_mast a WHERE id='"+id+"' ";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			//System.out.println("psmt DURING EDIT"+psmt);
			while (rst.next()) {	
				CommercializationLicensingModel model = new CommercializationLicensingModel();
				model.setId(General.checknull(rst.getString("id")));
				model.setLocation(rst.getString("LOCATION_CODE"));
				model.setDdo(rst.getString("DDO_ID"));
				model.setTyp(General.checknull(rst.getString("type")));
				model.setApp_no(General.checknull(rst.getString("application_no")));
				model.setTitle(General.checknull(rst.getString("title")));
				model.setDate(General.checknull(rst.getString("date")));
				model.setEnd_date(General.checknull(rst.getString("end_date")));
				model.setDuration(General.checknull(rst.getString("duration")));
				model.setAmt_of_comm(General.checknull(rst.getString("amt_of_comm")));
				model.setResh_category(General.checknull(rst.getString("category")));
				model.setSub_category(rst.getString("sub_category"));
				model.setSec_party(rst.getString("sec_party"));
				model.setCau_comm_num(General.checknull(rst.getString("cau_comm_num")));
				model.setComm_no(General.checknull(rst.getString("comm_no")));
				model.setComm_type(General.checknull(rst.getString("comm_type")));
				model.setUpload(General.checknull(rst.getString("upld")));
				
				al.add(model);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("CommercializationLicensingManager[getEditList]", e.toString()));
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
				l.fatal(Logging.logException("CommercializationLicensingManager[getEditList]", sql.toString()));
			}
		}
		return al;
	}
	//This function is using to update data into database.In this function passing 5 parameters(model,items,user_id,machine,mode) and will return String*/
	public static CommercializationLicensingModel Update(CommercializationLicensingModel model, String upl_id, FileItem items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0";
		int chk=0,i=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String mid=General.checknull(model.getId());
			
			qry="UPDATE Commercialization_Licensing_mast SET type=?,  application_no=?, title=?, cau_comm_num=?, comm_no=?, comm_type=?,category=?,"
					+ " sub_category=?, sec_party=?, from_date=?, to_date=?, duration=?,amt_of_comm=?,LOCATION_CODE=?,DDO_ID=?,`UPDATED_BY`=?, `UPDATED_MACHINE`=?,`UPDATED_DATE`=now()";
			
			qry+= "WHERE `id`=?";
			
			psmt = conn.prepareStatement(qry);
	//	System.out.println("=model.getXTODATE()="+model.getXTODATE());
			psmt.setString(++i, General.checknull(model.getTyp()));
			psmt.setString(++i, General.checknull(model.getApp_no()));
			psmt.setString(++i, General.checknull(model.getTitle()));
			psmt.setString(++i, General.checknull(model.getCau_comm_num()));
			psmt.setString(++i, General.checknull(model.getComm_no()));
			psmt.setString(++i, General.checknull(model.getComm_type()));
			psmt.setString(++i, (model.getResh_category().equals("")?null:model.getResh_category()));
			psmt.setString(++i, General.checknull(model.getSub_category()));
			psmt.setString(++i, General.checknull(model.getSec_party()));
			psmt.setString(++i, General.formatDate(model.getDate()));
			psmt.setString(++i, General.formatDate(model.getEnd_date()));
			psmt.setString(++i, General.checknull(model.getDuration()));
			psmt.setString(++i, General.checknull(model.getAmt_of_comm()));
			psmt.setString(++i, General.checknull(model.getLocation()));
			psmt.setString(++i, General.checknull(model.getDdo()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, machine);
			psmt.setString(++i, General.checknull(model.getId()));
			int cnt = psmt.executeUpdate();
			//System.out.println("check ---- "+psmt);
			if (cnt > 0){
				if(items!=null && (!items.getName().equals(""))){
						saveDoc(machine,upl_id, mid, user_id, conn, items);						
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
		l.fatal(Logging.logException("CommercializationLicensingManager[update]", e.getMessage().toString()));
		System.out.println("Error in CommercializationLicensingManager[update] : " + e.getMessage());
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
	
	
	 public static String CalculateDays(final String fdDate, final String day) {
	        Connection conn = null;
	        PreparedStatement psmt = null;
	        ResultSet rst = null;
	        String qry = "";
	        String date = "";
	        try {
	            conn = DBConnection.getConnection();
	            qry = "SELECT DATEDIFF(str_to_date(?,'%d/%m/%Y'), (str_to_date(?,'%d/%m/%Y'))) AS DAYDIFF ";
	            psmt = conn.prepareStatement(qry);
	            psmt.setString(1, General.checknull(day));
	            psmt.setString(2, General.checknull(fdDate));
	            rst = psmt.executeQuery();
	            if (rst.next()) {
	                date = General.checknull(rst.getString("DAYDIFF"));
	            }
	        }
	        catch (Exception e) {
	        l.fatal((Object)Logging.logException("CommercializationLicensingManager[CalculateDays]", e.toString()));
	            return date;
	        }
	        finally {
	            try {
	                conn.close();
	                psmt.close();
	            }
	            catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	        try {
	            conn.close();
	            psmt.close();
	        }
	        catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return date;
	    }
			// genrate getComm_code LPAD:-
			public static String getComm_code() {
				String cSql = "", msg = "";
				Connection conn = null;
				PreparedStatement pstmt = null;
				String comm_code = "";
				ResultSet rst = null;
				int count = 0;
				try {
					conn = DBConnection.getConnection();
					conn.setAutoCommit(false);
					cSql = " SELECT LPAD(CONVERT(IFNULL(MAX(SUBSTR(cau_comm_num,11,19)),0)+1,SIGNED INTEGER),3,'0') FROM Commercialization_Licensing_mast";
					pstmt = conn.prepareStatement(cSql);
					//System.out.println("pstmt"+pstmt);
					rst = pstmt.executeQuery();
					if (rst.next()) {
						comm_code = General.checknull(rst.getString(1)).trim();
					}
					comm_code = "CAU/COM/" + comm_code;
					conn.commit();
				} catch (Exception e) {
					System.out.println("Error in CommercializationLicensingManager[getComm_code] : " + e.getMessage());
					l.fatal(Logging.logException("CommercializationLicensingManager[getComm_code]", e.getMessage().toString()));
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
				return comm_code;
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
					System.out.println("Error in CommercializationLicensingManager[deletePreviousattchments] : "+e.getMessage());
					l.fatal(Logging.logException("CommercializationLicensingManager[deletePreviousattchments]", e.getMessage().toString()));
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