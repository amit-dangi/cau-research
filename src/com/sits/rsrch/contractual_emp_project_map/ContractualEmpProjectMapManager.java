package com.sits.rsrch.contractual_emp_project_map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.text.log.SysoLogger;
import com.sits.commonApi.commonAPI;
import com.sits.conn.DBConnection;
import com.sits.general.General;
import com.sits.general.Logging;

public class ContractualEmpProjectMapManager {
	static Logger l = Logger.getLogger("exceptionlog");

	public static String save(String projId, JSONArray consobj, String user_id, String machine,String locationCode,String ddoCode,String natureType) throws SQLException {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0", id="", empId="";
		int cnt=0;
		int chk=consobj.size();
		
		try {
			for (int i=0; i<chk; i++) {
				empId+= consobj.get(i).toString();
				if(i != chk-1){
					empId+= ",";
				}
			}
			
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			String query = "SELECT cem_id FROM RSRCH_CONTRACTUAL_EMP_PROJECT_MAP where proj_id='"+projId+"'";
			psmt= conn.prepareStatement(query);
			rst = psmt.executeQuery();
			if(rst.next()) {
				id = General.checknull(rst.getString(1));
			}
	        
			psmt=null;
	        
			if(General.checknull(id).equals("")){
			
			String Qry1 = "SELECT concat('CE', LPAD(CONVERT(IFNULL(MAX(SUBSTR(cem_id,3,6)),0)+1,SIGNED INTEGER),4,'0')) FROM RSRCH_CONTRACTUAL_EMP_PROJECT_MAP";
			psmt = conn.prepareStatement(Qry1);
			rst = psmt.executeQuery();
			if (rst.next()) {
				id = General.checknull(rst.getString(1));
			}
			
			//for (int i=0; i<chk; i++) {
				//JSONObject obj = (JSONObject) consobj.get(i);
				qry="INSERT INTO RSRCH_CONTRACTUAL_EMP_PROJECT_MAP (`cem_id`,`proj_id`, `emp_id`, `CREATED_BY`, `CREATED_MACHINE`,`LOCATION_CODE`,`DDO_ID`, `nature_type`,`CREATED_DATE`) "
						+ "VALUES (?, ?, ?, ?, ?,?,?,?, now())";
				psmt = conn.prepareStatement(qry);				
				
				psmt.setString(1, General.checknull(id.toString()));
				psmt.setString(2, General.checknull(projId.toString()));
				psmt.setString(3, General.checknull(empId.toString()));
				psmt.setString(4, General.checknull(user_id));
				psmt.setString(5, General.checknull(machine));
				psmt.setString(6, General.checknull(locationCode));
				psmt.setString(7, General.checknull(ddoCode));
				psmt.setString(8, General.checknull(natureType));
				//psmt.addBatch();
				System.out.println("Emp Project Mapping Save "+psmt);
				cnt = psmt.executeUpdate();
			//}
			}else{
				qry="UPDATE RSRCH_CONTRACTUAL_EMP_PROJECT_MAP SET emp_id=?, updated_by=?, updated_machine=?, updated_date=now() where cem_id=?";
				psmt = conn.prepareStatement(qry);				
				
				psmt.setString(1, General.checknull(empId.toString()));
				psmt.setString(2, General.checknull(user_id));
				psmt.setString(3, General.checknull(machine));
				psmt.setString(4, General.checknull(id));
				cnt = psmt.executeUpdate();
			}
			if (cnt > 0){
				msg="1";
				conn.commit();
			} else {
				msg="0";
				conn.rollback();
			}
		} catch (Exception e) {
			if(e.getMessage().toString().indexOf("Duplicate entry") != -1){				
				msg="5";
				conn.rollback();
			}else{ 
				msg="2";
				conn.rollback();
			}
			
			System.out.println("Exception in ContractualEmpProjectMapManager[SAVE]" + " " + e.getMessage());
			l.fatal(Logging.logException("ContractualEmpProjectMapManager[SAVE]", e.toString()));
		} finally {
			try {
				conn.close();
				psmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return msg;
	}
	
	public static ArrayList<ContractualEmpProjectMapModel> getList(String proj, String emp,String loct_id,String ddo_id,String user_status,String sess_emp_id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		JSONObject finalObject=new JSONObject();
		try {
			conn = DBConnection.getConnection();
			query = "SELECT a.DDO_ID,a.LOCATION_CODE,a.proj_id as projID, b.PS_TITTLE_PROJ as projN, group_concat(a.emp_id) as empId, cem_id as id,nature_type "
					+ "FROM RSRCH_CONTRACTUAL_EMP_PROJECT_MAP a, rsrch_form1_mast b where a.proj_id=b.PS_MID";
			
			if(!General.checknull(proj).trim().equals("")){
				query+=" and proj_id = '"+General.checknull(proj)+"' ";
			}
			if(!General.checknull(loct_id).equals("") || !General.checknull(ddo_id).equals("")){
				query+=" and a.LOCATION_CODE = '"+General.checknull(loct_id)+"' and a.DDO_ID = '"+General.checknull(ddo_id)+"' ";
			} if((!sess_emp_id.equals("")) && (user_status.equals("U"))){
				query+=" and a.created_by like '"+sess_emp_id+"' ";
			}
			/*if(!General.checknull(emp).trim().equals("")){
				query+=" and emp_id = '"+General.checknull(emp)+"' ";
			}*/
			query+=" group by PS_MID order by b.PS_TITTLE_PROJ asc ";
			//System.out.println("getList( -"+query);
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			JSONObject obj = new JSONObject();
			finalObject.put("tablename", "employee_mast");
			finalObject.put("columndesc", "concat(employeeName,' (',employeeCodeM,')')");
			finalObject.put("id", "employeeId");
			obj = commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObject);
			JSONArray item = (JSONArray) obj.get("commondata");
			
			
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
			
			
			while (rst.next()) {
				ContractualEmpProjectMapModel faModel = new ContractualEmpProjectMapModel();
				String name="";
				faModel.setId(General.checknull(rst.getString("id")));
				faModel.setProjId(General.checknull(rst.getString("projID")));
				faModel.setProjName(General.checknull(rst.getString("projN")));
				faModel.setEmpId(General.checknull(rst.getString("empId")));
				faModel.setNature_type(General.checknull(rst.getString("nature_type")));
				faModel.setDdo_id(General.checknull(rst.getString("DDO_ID")));
				faModel.setLocation_code(General.checknull(rst.getString("LOCATION_CODE")));
				
				if(!General.checknull(rst.getString("empId")).equals("")){
					
					String a[]=General.checknull(rst.getString("empId").trim()).split(",");
					
					for (int n = 0; n < item.size(); n++) {
						JSONObject jsn = (JSONObject) item.get(n);
						
						for (int k = 0; k < a.length; k++) {
							if (General.checknull(jsn.get("id").toString()).equals(General.checknull(a[k]))) {								
								name+=General.checknull(jsn.get("desc").toString());
								if(k != a.length-1){
									 name+= ", ";
								}
							}	
						}
					}
				}
				
				faModel.setEmpName(General.checknull(name));
				
				for(int i=0; i<designationlocationarr.size(); i++){
					JSONObject jsn=	(JSONObject) designationlocationarr.get(i);
					if(jsn.get("id").equals(rst.getString("LOCATION_CODE")))
					{
						faModel.setLocationName( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				for(int i=0; i<designationDtoarr.size(); i++){
					JSONObject jsn=	(JSONObject) designationDtoarr.get(i);
					if(jsn.get("id").equals(rst.getString("ddo_id")))
					{
						faModel.setDdoName( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ContractualEmpProjectMapManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("ContractualEmpProjectMapManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static String delete(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "", al="";
		int count = 0;
		try {
			conn = DBConnection.getConnection();
			
			query = "delete from RSRCH_CONTRACTUAL_EMP_PROJECT_MAP where cem_id='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			if(count > 0){
				al="1";
			}else{
				al="0";	
			}			
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectSubmissionManager[getList]", e.toString()));
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
				l.fatal(Logging.logException("ProjectSubmissionManager[getList]", sql.toString()));
			}
		}
		return al;
	}

	public static String Update(String proj, JSONArray consobj, String id, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0", empId="";
		int chk=consobj.size();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			for (int i=0; i<chk; i++) {
				empId+= consobj.get(i).toString();
				if(i != chk-1){
					empId+= ",";
				}
			}
			
			qry="UPDATE RSRCH_CONTRACTUAL_EMP_PROJECT_MAP SET proj_id=?, emp_id=?, updated_by=?, updated_machine=?, updated_date=now() where cem_id=?";
			psmt = conn.prepareStatement(qry);				
			
			psmt.setString(1, General.checknull(proj.toString()));
			psmt.setString(2, General.checknull(empId.toString()));
			psmt.setString(3, General.checknull(user_id));
			psmt.setString(4, General.checknull(machine));
			psmt.setString(5, General.checknull(id));
			int cnt = psmt.executeUpdate();

			if (cnt > 0){
				msg="1";
				conn.commit();
			} else {
				msg="0";
				conn.rollback();
			}
		} catch (Exception e) {
			if(e.getMessage().toString().indexOf("Duplicate entry") != -1)				
				msg="5";
			else 
				msg="2";
			
			System.out.println("Exception in ContractualEmpProjectMapManager[SAVE]" + " " + e.getMessage());
			l.fatal(Logging.logException("ContractualEmpProjectMapManager[SAVE]", e.toString()));
		} finally {
			try {
				conn.close();
				psmt.close();
				rst.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return msg;
	}
	
	public static JSONObject getSavedData(String id) {
		PreparedStatement pst = null;
		Connection conn=null;
		ResultSet rst = null;
		JSONObject objectJson=new JSONObject();
		String query = "SELECT emp_id FROM RSRCH_CONTRACTUAL_EMP_PROJECT_MAP where proj_id='"+id+"'";
        try {
        	conn = DBConnection.getConnection();
        	pst = conn.prepareStatement(query);
        	rst = pst.executeQuery();
        	while (rst.next()) {
        		objectJson.put("eid", General.checknull(rst.getString("emp_id")));
        	}
        } catch (Exception e) {
        	System.out.println("FileName=[ContractualEmpProjectMapManager] MethodName=[getSavedData()] :"+ e.getMessage().toString());
        	l.fatal(Logging.logException("FileName=[ContractualEmpProjectMapManager] MethodName=[getSavedData()] :", e.getMessage().toString()));
        }finally {
        	try {
        		if (rst != null) {
        			rst.close();
        			rst = null;
        		}
        		if (pst != null) {
        			pst.close();
        			pst = null;
        		}
        		if (conn != null) {
        			conn.close();
        			conn = null;
        		}
        	} catch (final Exception e) {
        		l.fatal(Logging.logException("FileName=[ContractualEmpProjectMapManager],MethodName=[getSavedData()]", e.getMessage().toString()));
        	}
        }
        return objectJson;
	}
	
	public static JSONObject getPorojectsSavedData(String locationCode,String ddoCode,HttpServletRequest request) {
		PreparedStatement pst = null;
		Connection conn=null;
		ResultSet rst = null;
		JSONObject objectJsonfinal=new JSONObject();
		JSONArray jsonArray = new JSONArray();
		String user_id= General.checknull((String) request.getSession().getAttribute("user_id"));
		String user_status= General.checknull((String) request.getAttribute("user_status"));
		String query = "SELECT distinct a.PS_MID,a.PS_TITTLE_PROJ FROM rsrch_form1_mast a, rsrch_research_prop_approval b where a.PS_MID=b.PS_MID and a.is_form_submittd='Y' and a.submitted_date is not null and b.RPA_STATUS='A' and RPA_TYPE='RR' ";
		if(!General.checknull(locationCode).equals("") || !General.checknull(ddoCode).equals("")){
			query+=" and a.LOCATION_CODE = '"+General.checknull(locationCode)+"' and a.DDO_ID = '"+General.checknull(ddoCode)+"' ";
		}
		if(!user_status.equals("A")){
			query+=" and a.CREATED_BY='"+user_id+"' " ; 
		 }
		try {
        	conn = DBConnection.getConnection();
        	pst = conn.prepareStatement(query);
        	rst = pst.executeQuery();
        	while (rst.next()) {
        		JSONObject objectJson=new JSONObject();
        		objectJson.put("PS_MID", General.checknull(rst.getString("PS_MID")));
        		objectJson.put("PS_TITTLE_PROJ", General.checknull(rst.getString("PS_TITTLE_PROJ")));
        		jsonArray.add(objectJson);
        	}
        	objectJsonfinal.put("projectdropdown", jsonArray);
        	System.out.print("objectJsonfinal>>>");
        	System.out.println(objectJsonfinal.toString());
        } catch (Exception e) {
        	System.out.println("FileName=[ContractualEmpProjectMapManager] MethodName=[getPorojectsSavedData()] :"+ e.getMessage().toString());
        	l.fatal(Logging.logException("FileName=[ContractualEmpProjectMapManager] MethodName=[getPorojectsSavedData()] :", e.getMessage().toString()));
        }finally {
        	try {
        		if (rst != null) {
        			rst.close();
        			rst = null;
        		}
        		if (pst != null) {
        			pst.close();
        			pst = null;
        		}
        		if (conn != null) {
        			conn.close();
        			conn = null;
        		}
        	} catch (final Exception e) {
        		l.fatal(Logging.logException("FileName=[ContractualEmpProjectMapManager],MethodName=[getPorojectsSavedData()]", e.getMessage().toString()));
        	}
        }
        return objectJsonfinal;
	}
	
}