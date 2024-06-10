/**
 * @ AUTHOR Chetan Sharma
 */
package com.sits.rsrch.form1_project_submission;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sits.commonApi.commonAPI;
import com.sits.conn.DBConnection;
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.general.ReadProps;

import bwmorg.LOG;

public class ProjectSubmissionManager {
	static Logger l = Logger.getLogger("exceptionlog"); 
	public static String save(ProjectSubmissionModel model, JSONArray consobj, List<FileItem> items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0";
		int chk=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String mid="";
			
			String Qry1 = "SELECT concat('PS', LPAD(CONVERT(IFNULL(MAX(SUBSTR(PS_MID,3,6)),0)+1,SIGNED INTEGER),4,'0')) FROM RSRCH_FORM1_MAST";
			PreparedStatement psmt5= conn.prepareStatement(Qry1);
			ResultSet rst1 = psmt5.executeQuery();
			if (rst1.next()) {
				mid = General.checknull(rst1.getString(1));
			}
			
			qry="INSERT INTO RSRCH_FORM1_MAST (`PS_MID`,`LOCATION_CODE`,`DDO_ID`, `PS_PRINCIPAL`, `PS_DEPT`, `PS_DESG`, `PS_TITTLE_PROJ`, `PS_DUR_PROJECT`, `PS_TOT_BUDGET`, "//+ "`PS_FUND_AGNCY`, "
					+ "`PS_NAME_AND_ADD_CO_PI`, `PS_DEADLINE_SUBMISSION`, `PS_PROJ_PROPOSAL_SUB`, `PS_PROJ_PROPOSAL_REQ_CLEARANCE`, `PS_CLEARANCE_BEEN_OBTAINED`, "
					+ "`PS_ANY_FIN_COMMITMENT`, `PS_DECLARATION_CERTIFICATE`,`ppld_manual`,`PS_DUR_PROJECT_YEAR`, " //+ "`PS_NON_REC_COST`, `PS_CHEMICALS_COST`, `PS_MANPOWER`, `PS_CONTINGENCY`, `PS_TRAVEL`, `PS_OTHER_CHARGES`, `PS_OVERHEAD_CHARGES`, "
					+ "`projtype`,`erptype`,`projterm`,`IsApprovalReq`,`fn_agency`,`proj_obj`,`thrust_area`,`sub_thrust_area`,RETIRE_PS_PRINCIPAL,BUDGET_HEADS,`CREATED_BY`, `CREATED_MACHINE`, `CREATED_DATE`,`proj_start_date` ";
			
			if(mode.trim().equals("SS")){
				qry+= ", is_form_submittd, submitted_date";
			}
			if(General.checknull(model.getFinCommitUni()).equals("Y")){
				qry+= ", PS_ANY_FIN_COMMITMENT_DTLS ";
			}
			qry+= ", ppId,inst_charges,projunder) VALUES (?,?,?, ?, ?, ?, ?, ?, ?, "//+ "?, "
					+ "?, ?, ?, ?, ?, ?, ?,?,?, ?,?,?,?, "//+ "?, ?, ?, ?, ?, ?, ?, "					
					+ "?,?,?, ?,?,?,?,?, now(),?";			
			
			if(mode.trim().equals("SS")){
				qry+= ", 'Y', date(now())";
			}
			if(General.checknull(model.getFinCommitUni()).equals("Y")){
				qry+= ", '"+General.checknull(model.getFinCommitUniDetails())+"' ";
			}
			
			qry+= ", ?,?,?)";
			
			psmt = conn.prepareStatement(qry);
			psmt.setString(1, General.checknull(mid).trim());
			psmt.setString(2, General.checknull(model.getLocationCode()));
			psmt.setString(3, General.checknull(model.getDdoCode()));
			psmt.setString(4, General.checknull(model.getPiName()).trim());
			psmt.setString(5, General.checknull(model.getDept()));
			psmt.setString(6, General.checknull(model.getDesg()));
			psmt.setString(7, General.checknull(model.getProjPropsal()));
			psmt.setString(8, (model.getDurPropProj().equals("")?null:model.getDurPropProj()));
			
			psmt.setString(9, (model.getTotalBudgProp().equals("")?null:model.getTotalBudgProp()));
			///psmt.setString(8, General.checknull(model.getFundAgency()));			
			psmt.setString(10, General.checknull(model.getNameAddrCoPi()));	
			if(!model.getXTODATE().equals("")){
			psmt.setString(11, General.formatDate(model.getXTODATE()));//?null:"str_to_date("+model.getXTODATE()+",'%d/%m/%Y')");			
			}else{
				psmt.setString(11, null);
			}
			psmt.setString(12, General.checknull(model.getProjPropSub()));
			
			psmt.setString(13, General.checknull(model.getProjPropClear()));
			psmt.setString(14, General.checknull(model.getNecClearObt()));
			psmt.setString(15, General.checknull(model.getFinCommitUni()));
			psmt.setString(16, General.checknull(model.getAttchCertif()));	
			
			psmt.setString(17, General.checknull(model.getProjPropsalIdManual()));
			psmt.setString(18, (model.getDurPropProjYear().equals("")?null:model.getDurPropProjYear()));	
			//psmt.setString(15, General.checknull(model.getNonRecCost()));
			//psmt.setString(16, General.checknull(model.getChemAndCon()));			
			//psmt.setString(17, General.checknull(model.getManpower()));
			//psmt.setString(18, General.checknull(model.getContingency()));
			//psmt.setString(19, General.checknull(model.getTravel()));
			//psmt.setString(20, General.checknull(model.getOutSourcingCharge()));
			//psmt.setString(21, General.checknull(model.getOverCharg()));			
			
			psmt.setString(19, model.getProjtype());
			psmt.setString(20, model.getErptype());
			psmt.setString(21, model.getProjterm());
			psmt.setString(22, model.getIsApprovalReq());
			
			psmt.setString(23, model.getFn_agency());
			psmt.setString(24, model.getProj_obj());
			
			psmt.setString(25, model.getThrust_area());
			psmt.setString(26, model.getSub_thrust_area());
			psmt.setString(27, model.getRetirePiName());
			psmt.setString(28, model.getBudgetHeads());
			
			psmt.setString(29, General.checknull(user_id));
			psmt.setString(30, General.checknull(machine));
			
			if(!model.getProj_start_date().equals("")){
				psmt.setString(31, General.formatDate(model.getProj_start_date()));			
				}else{
					psmt.setString(31, null);
				}
			psmt.setString(32, General.checknull(model.getPpId()));
			psmt.setString(33, General.checknull(model.getInst_charges()));
			psmt.setString(34, General.checknull(model.getProjunder()));
			
			//System.out.println("ProjectSubmissionManager save- "+psmt);
			int cnt = psmt.executeUpdate();
			if (cnt > 0){
				getSequenceNoforChallan("RSRCH_FORM1_MAST", "N", "U");
				if(items!=null){
					Iterator<FileItem> iter = items.iterator();
					while (iter.hasNext()) {
						FileItem fileItem = iter.next();
						JSONObject obj = (JSONObject) consobj.get(chk);
						String docName="";
						if(obj.get("doctitle")!=null)
							docName=obj.get("doctitle").toString();
						
						saveDoc(machine, mid, user_id, conn, fileItem, consobj, docName);						
						chk++;
					}
				}
				msg="1";
				conn.commit();
			} else {
				msg="0";
				conn.rollback();
			}
		} catch (Exception e) {
			msg=e.getMessage();
			System.out.println("Exception in ProjectSubmissionManager[SAVE]" + " " + e.getMessage());
			l.fatal(Logging.logException("ProjectSubmissionManager[SAVE]", e.toString()));
		} finally {
			try {
				if (rst != null)
					rst.close();
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return msg;
	}
	
	public static boolean saveDoc(String machine, String id, String userid, Connection conn, FileItem fileItem, JSONArray consobj, String docName){		
		java.io.File file;
		try{
			String attachid=filedetailsave(machine, fileItem.getName().replace("&", "and"), id, userid, fileItem.getSize(), conn, docName);
			
			if(!attachid.equals("")){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/FORM1/"+id+"/";
				File directory = new File(directoryName);
				if (!directory.isDirectory()){
					directory.mkdirs();
				}
				file = new File(directoryName+attachid+"_"+fileItem.getName().replace("&", "and"));
				fileItem.write(file);	
				//file2.transferTo(file);
			}
	  	}catch(Exception e){
	  		System.out.println("Error in ProjectSubmissionManager[saveDoc] : "+e.getMessage());
	  		l.fatal(Logging.logException("ProjectSubmissionManager[saveDoc]", e.toString()));
	  		return false;
	  	}
		return true;
	}
	
	public static String filedetailsave(String machine, String name, String id, String userid, long size, Connection conn, String docName){ 
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";
		String attachid="";
		int count=0;
		try{
			String Qry = "SELECT IFNULL(MAX(PS_DID)+1,1) FROM RSRCH_FORM1_ATTACH where 1=1"; 
			psmt = conn.prepareStatement(Qry);
			rst= psmt.executeQuery();
			if(rst.next())
				attachid=General.checknull(rst.getString(1));
			
			qry="INSERT INTO RSRCH_FORM1_ATTACH (PS_DID, PS_MID, PS_DOC_NAME, PS_FILE_NAME, UPLOADED_DT, UPLOADED_BY, UPLOADED_MACHINE) "
					+ "VALUES (?, ?, ?, ?, now(), ?, ?)";
			psmt = conn.prepareStatement(qry);
			psmt.setString(1, attachid);
			psmt.setString(2, id);
			psmt.setString(3, docName);
			psmt.setString(4, name);
			psmt.setString(5, userid);
			psmt.setString(6, machine);
			count= psmt.executeUpdate();
			if(count==0)
				attachid="";
		}
		catch(Exception e)
		{
			attachid="";
			System.out.println("EXCEPTION IS CAUSED BY: ProjectSubmissionManager" + "[filedetailsave]" + " " + e.getMessage().trim().toUpperCase());
			l.fatal(Logging.logException("ProjectSubmissionManager [filedetailsave]", e.toString()));
			return attachid;
		}
		return attachid;
	}
	
	public static ArrayList<ProjectSubmissionModel> getList(ProjectSubmissionModel fam,String sess_emp_id,String user_status) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		//JSONObject finalObject=new JSONObject();
		try {
			JSONObject jsonobj=new JSONObject();
			JSONObject finalObject=new JSONObject();
			finalObject.put("tablename", "employee_mast");
			finalObject.put("columndesc","concat(employeeName,' (',employeeCodeM,')')");
			finalObject.put("id", "employeeId");
			jsonobj= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObject);
			JSONArray deparr = (JSONArray) jsonobj.get("commondata");
			
			JSONObject jsonobj1=new JSONObject();
			JSONObject finalObject1=new JSONObject();
			finalObject1.put("tablename", "department_mast");
			finalObject1.put("columndesc","DEPARTMENT");
			finalObject1.put("id", "DEPT_ID");
			jsonobj1= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObject1);
			JSONArray departmentarr = (JSONArray) jsonobj1.get("commondata");
			
			
			JSONObject jsonobj2=new JSONObject();
			JSONObject finalObject2=new JSONObject();
			finalObject2.put("tablename", "designation_mast");
			finalObject2.put("columndesc","DESIGNATION");
			finalObject2.put("id", "DESIGNATION_ID");
			jsonobj2= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObject2);
			JSONArray designationarr = (JSONArray) jsonobj2.get("commondata");
			
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
			query = "SELECT concat(projtype,case when erptype='' then '' else concat('-',erptype) end) as projtype,projterm,LOCATION_CODE,ddo_id,PS_TITTLE_PROJ,PS_MID, PS_PRINCIPAL,RETIRE_PS_PRINCIPAL, PS_DEPT, PS_DESG, is_form_submittd,is_project_extension,fn_agency,proj_obj FROM rsrch_form1_mast b WHERE 1=1 ";
			
			if(!General.checknull(fam.getProjtype()).trim().equals("")){
				
				query+=" and projtype = '"+General.checknull(fam.getProjtype())+"' ";
			}
			
            if(!General.checknull(fam.getErptype()).trim().equals("")){
				
				query+=" and erptype = '"+General.checknull(fam.getErptype())+"' ";
			}
            
           /*if(!General.checknull(fam.getProjterm()).trim().equals("")){
				
				query+=" and projterm = '"+General.checknull(fam.getProjterm())+"' ";
			}*/
            
			if(!(sess_emp_id.equals("")) && (user_status.equals("U"))){
				query+=" and PS_PRINCIPAL like '"+sess_emp_id+"' ";
			}
			else{
			    query+=" and PS_PRINCIPAL like '%"+General.checknull(fam.getPiName())+"%' ";
			}
			if(!General.checknull(fam.getDept()).trim().equals("")){
				query+=" and PS_DEPT = '"+General.checknull(fam.getDept())+"' ";
			}
			if(!General.checknull(fam.getDesg()).trim().equals("")){
				query+=" and PS_DESG = '"+General.checknull(fam.getDesg())+"' ";
			}
			if(!General.checknull(fam.getLocationCode()).trim().equals("")){
				query+=" and LOCATION_CODE = '"+General.checknull(fam.getLocationCode())+"' ";
			}
			if(!General.checknull(fam.getDdoCode()).trim().equals("")){
				query+=" and ddo_id = '"+General.checknull(fam.getDdoCode())+"' ";
			}
			
			//System.out.println("getList(ProjectSubmissionModel fam)-"+query);
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				ProjectSubmissionModel faModel = new ProjectSubmissionModel();
				
				for(int i=0; i<deparr.size(); i++){ 
					JSONObject jsn= (JSONObject) deparr.get(i);
					if(jsn.get("id").equals(rst.getString("PS_PRINCIPAL")))
					{
						faModel.setPiNameDesc( General.checknull(jsn.get("desc").toString()));
					}else if(General.checknull(rst.getString("PS_PRINCIPAL")).equals("")){
						faModel.setPiNameDesc( General.checknull(rst.getString("RETIRE_PS_PRINCIPAL")));
					}
				}
				
				for(int i=0; i<departmentarr.size(); i++){
					JSONObject jsn=	(JSONObject) departmentarr.get(i);
					if(jsn.get("id").equals(rst.getString("PS_DEPT")))
					{
						faModel.setDeptName( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				for(int i=0; i<designationarr.size(); i++){
					JSONObject jsn=	(JSONObject) designationarr.get(i);
					if(jsn.get("id").equals(rst.getString("PS_DESG")))
					{
						faModel.setDesgName( General.checknull(jsn.get("desc").toString()));
					}
				}
				
				
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
				
				
				
				faModel.setfId(General.checknull(rst.getString("PS_MID")));
				faModel.setDept(General.checknull(rst.getString("PS_DEPT")));
				faModel.setDesg(General.checknull(rst.getString("PS_DESG")));
				faModel.setIsSubmit(General.checknull(rst.getString("is_form_submittd")));
				faModel.setLocationCode(General.checknull(rst.getString("LOCATION_CODE")));
				faModel.setPiName(General.checknull(rst.getString("PS_TITTLE_PROJ")));
				faModel.setProjET(General.checknull(rst.getString("is_project_extension")));
				faModel.setProjtype(General.checknull(rst.getString("projtype")));
				al.add(faModel);
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

	public static String delete(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "", al="";
		int count = 0;
		try {
			conn = DBConnection.getConnection();
			
			String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/FORM1/"+id+"/";
			File directory = new File(directoryName);
			
			query = "delete from rsrch_form1_attach where PS_MID='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			
			if(count > 0){				
				deleteFile(directory);
				al="1";
			}
			psmt=null;rst=null;query="";
			query = "delete from rsrch_form1_mast where PS_MID='"+id+"'";
			psmt = conn.prepareStatement(query);
			count = psmt.executeUpdate();
			al="1";
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

	public static void deleteFile(File directory) {
		if (directory.isDirectory()) {
			File[] children = directory.listFiles();
			for (File child : children) {
				child.getAbsoluteFile().delete();	
			}
		}
	}
	
	public static ArrayList<ProjectSubmissionModel> getEditList(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		JSONObject finalObject=new JSONObject();
		try {
			conn = DBConnection.getConnection();			
			query = "SELECT *, date_format(PS_DEADLINE_SUBMISSION, '%d/%m/%Y') as dt,date_format(proj_start_date, '%d/%m/%Y') as str_dt FROM rsrch_form1_mast WHERE PS_MID='"+id+"' ";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			//System.out.println("psmt"+psmt);
			while (rst.next()) {	
				ProjectSubmissionModel faModel = new ProjectSubmissionModel();
				faModel.setfId(General.checknull(rst.getString("PS_MID")));
				faModel.setLocationCode(rst.getString("LOCATION_CODE"));
				faModel.setDdoCode(rst.getString("DDO_ID"));
				faModel.setPiName(General.checknull(rst.getString("PS_PRINCIPAL")));
				faModel.setRetirePiName(General.checknull(rst.getString("RETIRE_PS_PRINCIPAL")));
				faModel.setBudgetHeads(General.checknull(rst.getString("BUDGET_HEADS")));
				faModel.setDept(General.checknull(rst.getString("PS_DEPT")));
				faModel.setDesg(General.checknull(rst.getString("PS_DESG")));
				faModel.setProjPropsal(General.checknull(rst.getString("PS_TITTLE_PROJ")));
				faModel.setDurPropProj(General.checknull(rst.getString("PS_DUR_PROJECT")));
				faModel.setTotalBudgProp(General.checknull(rst.getString("PS_TOT_BUDGET")));
				faModel.setFundAgency(General.checknull(rst.getString("PS_FUND_AGNCY")));
				faModel.setNameAddrCoPi(General.checknull(rst.getString("PS_NAME_AND_ADD_CO_PI")));
				faModel.setXTODATE(General.checknull(rst.getString("dt")));
				faModel.setProj_start_date(General.checknull(rst.getString("str_dt")));
				faModel.setProjPropSub(General.checknull(rst.getString("PS_PROJ_PROPOSAL_SUB")));
				faModel.setProjPropClear(General.checknull(rst.getString("PS_PROJ_PROPOSAL_REQ_CLEARANCE")));
				faModel.setNecClearObt(General.checknull(rst.getString("PS_CLEARANCE_BEEN_OBTAINED")));
				faModel.setFinCommitUni(General.checknull(rst.getString("PS_ANY_FIN_COMMITMENT")));
				faModel.setAttchCertif(General.checknull(rst.getString("PS_DECLARATION_CERTIFICATE")));
				faModel.setNonRecCost(General.checknull(rst.getString("PS_NON_REC_COST")));
				faModel.setChemAndCon(General.checknull(rst.getString("PS_CHEMICALS_COST")));
				faModel.setManpower(General.checknull(rst.getString("PS_MANPOWER")));
				faModel.setContingency(General.checknull(rst.getString("PS_CONTINGENCY")));
				faModel.setTravel(General.checknull(rst.getString("PS_TRAVEL")));
				faModel.setOutSourcingCharge(General.checknull(rst.getString("PS_OTHER_CHARGES")));
				faModel.setOverCharg(General.checknull(rst.getString("PS_OVERHEAD_CHARGES")));
				faModel.setIsSubmit(General.checknull(rst.getString("is_form_submittd")));
				faModel.setPpId(General.checknull(rst.getString("ppId")));
				faModel.setFinCommitUniDetails(General.checknull(rst.getString("PS_ANY_FIN_COMMITMENT_DTLS")));
				faModel.setProjPropsalIdManual(rst.getString("ppld_manual"));
				faModel.setDurPropProjYear(rst.getString("PS_DUR_PROJECT_YEAR"));
				
				faModel.setProjtype(rst.getString("projtype"));
				faModel.setErptype(rst.getString("erptype"));
				faModel.setProjterm(rst.getString("projterm"));
				faModel.setIsApprovalReq(rst.getString("IsApprovalReq"));
				
				faModel.setFn_agency(General.checknull(rst.getString("fn_agency")));
				faModel.setProj_obj(General.checknull(rst.getString("proj_obj")));
				
				faModel.setThrust_area(General.checknull(rst.getString("thrust_area")));
				faModel.setSub_thrust_area(General.checknull(rst.getString("sub_thrust_area")));
				
				faModel.setInst_charges(General.checknull(rst.getString("inst_charges")));
				faModel.setProjunder(General.checknull(rst.getString("projunder")));
				
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectSubmissionManager[getEditList]", e.toString()));
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
				l.fatal(Logging.logException("ProjectSubmissionManager[getEditList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static ArrayList<ProjectSubmissionModel> getEditFileList(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();			
			query = "SELECT * FROM rsrch_form1_attach WHERE PS_MID='"+id+"' ";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				ProjectSubmissionModel faModel = new ProjectSubmissionModel();
				
				faModel.setDid(General.checknull(rst.getString("PS_DID")));
				faModel.setfId(General.checknull(rst.getString("PS_MID")));
				faModel.setDoc_name(General.checknull(rst.getString("PS_DOC_NAME")));
				faModel.setFile_name(General.checknull(rst.getString("PS_FILE_NAME")));
					
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectSubmissionManager[getEditFileList]", e.toString()));
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
				l.fatal(Logging.logException("ProjectSubmissionManager[getEditFileList]", sql.toString()));
			}
		}
		return al;
	}

	public static String Update(ProjectSubmissionModel model, JSONArray consobj, List<FileItem> items, String user_id, String machine, String mode) {
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "",query="", msg="0";
		int chk=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			String mid=General.checknull(model.getfId());
			
			if(!model.getPiName().equals(model.getPrevious_pi_Name())){
				query="INSERT INTO RSRCH_FORM1_PI_HISTORY( PS_MID,PS_PRINCIPAL,PS_END_DATE,CREATED_BY,CREATED_MACHINE,CREATED_DATE)VALUES(?,?,?,?,?,now())";
				psmt = conn.prepareStatement(query);
				
				psmt.setString(1, General.checknull(model.getfId()).trim());
				psmt.setString(2, General.checknull(model.getPrevious_pi_Name()).trim());
				psmt.setString(3, General.formatDate(model.getApplied_date()));
				psmt.setString(4, General.checknull(user_id));
				psmt.setString(5, General.checknull(machine));
				psmt.executeUpdate();
				//System.out.println("cnt"+cnt+"psmt"+psmt);
			}
			
			
			qry="UPDATE RSRCH_FORM1_MAST SET PS_PRINCIPAL=?, `PS_DEPT`=?, `PS_DESG`=?, `PS_TITTLE_PROJ`=?, `PS_DUR_PROJECT`=?, `PS_TOT_BUDGET`=?, "//+ "`PS_FUND_AGNCY`=?, "
					+ "`PS_NAME_AND_ADD_CO_PI`=?, `PS_DEADLINE_SUBMISSION`=?, `PS_PROJ_PROPOSAL_SUB`=?, "
					+ "`PS_PROJ_PROPOSAL_REQ_CLEARANCE`=?, `PS_CLEARANCE_BEEN_OBTAINED`=?, `PS_ANY_FIN_COMMITMENT`=?, `PS_DECLARATION_CERTIFICATE`=?, "
					//+ "`PS_NON_REC_COST`=?, `PS_CHEMICALS_COST`=?, `PS_MANPOWER`=?, `PS_CONTINGENCY`=?, `PS_TRAVEL`=?, `PS_OTHER_CHARGES`=?, `PS_OVERHEAD_CHARGES`=?, "
					+ "`UPDATED_BY`=?, `UPDATED_MACHINE`=?,`LOCATION_CODE`=?,`DDO_ID`=?,`ppld_manual`=?,`PS_DUR_PROJECT_YEAR`=?,`projtype`=?,"
					+ " `erptype`=?,`projterm`=?,`IsApprovalReq`=?,`proj_start_date`=?,`fn_agency`=?,`proj_obj`=?,`thrust_area`=?,`sub_thrust_area`=?,RETIRE_PS_PRINCIPAL=?,BUDGET_HEADS=?,"
					+ " inst_charges=?,projunder=?, `UPDATED_DATE`=now()";
			if(mode.trim().equals("US")){
				qry+= ", is_form_submittd='Y', submitted_date=date(now()) ";
			}else{
				qry+= ", is_form_submittd='N' ";
			}
			if(General.checknull(model.getFinCommitUni()).equals("Y")){
				qry+= ", PS_ANY_FIN_COMMITMENT_DTLS='"+General.checknull(model.getFinCommitUniDetails())+"' ";
			}else{
				qry+= ", PS_ANY_FIN_COMMITMENT_DTLS=null ";
			}
			qry+= "WHERE `PS_MID`=?";
			psmt=null;
			psmt = conn.prepareStatement(qry);
	//	System.out.println("=model.getXTODATE()="+model.getXTODATE());
			psmt.setString(1, General.checknull(model.getPiName()).trim());
			psmt.setString(2, General.checknull(model.getDept()));
			psmt.setString(3, General.checknull(model.getDesg()));
			psmt.setString(4, General.checknull(model.getProjPropsal()));
			psmt.setString(5, General.checknull(model.getDurPropProj()));
			
			psmt.setString(6, (model.getTotalBudgProp().equals("")?null:model.getTotalBudgProp()));
			//psmt.setString(7, General.checknull(model.getFundAgency()));			
			psmt.setString(7, General.checknull(model.getNameAddrCoPi()));	
			if(!model.getXTODATE().equals("")){
				psmt.setString(8, General.formatDate(model.getXTODATE()));
			}else{
				psmt.setString(8, null);
			}
			
			//psmt.setString(8, model.getXTODATE().equals("")?null:"str_to_date("+model.getXTODATE()+",'%d/%m/%Y')");			
			psmt.setString(9, General.checknull(model.getProjPropSub()));
			
			psmt.setString(10, General.checknull(model.getProjPropClear()));
			psmt.setString(11, General.checknull(model.getNecClearObt()));
			psmt.setString(12, General.checknull(model.getFinCommitUni()));
			
			psmt.setString(13, General.checknull(model.getAttchCertif()));			
			/*psmt.setString(15, General.checknull(model.getNonRecCost()));
			psmt.setString(16, General.checknull(model.getChemAndCon()));
			
			psmt.setString(17, General.checknull(model.getManpower()));
			psmt.setString(18, General.checknull(model.getContingency()));
			psmt.setString(19, General.checknull(model.getTravel()));
			psmt.setString(20, General.checknull(model.getOutSourcingCharge()));
			psmt.setString(21, General.checknull(model.getOverCharg()));*/			
			psmt.setString(14, General.checknull(user_id));
			psmt.setString(15, General.checknull(machine));
			psmt.setString(16, General.checknull(model.getLocationCode()));
            psmt.setString(17, General.checknull(model.getDdoCode()));
            psmt.setString(18, General.checknull(model.getProjPropsalIdManual()));
            psmt.setString(19, General.checknull(model.getDurPropProjYear()));
            
			//new fields added 22 feb meeting
            psmt.setString(20, model.getProjtype());
			psmt.setString(21, model.getErptype());
			psmt.setString(22, model.getProjterm());
			psmt.setString(23, model.getIsApprovalReq());
			//new fields added 19 jun  meeting
			
			if(!model.getProj_start_date().equals("")){
				psmt.setString(24, General.formatDate(model.getProj_start_date()));
			}else{
				psmt.setString(24, null);
			}
			
			psmt.setString(25, General.checknull(model.getFn_agency()));
			psmt.setString(26, General.checknull(model.getProj_obj()));
			
			psmt.setString(27, General.checknull(model.getThrust_area()));
			psmt.setString(28, General.checknull(model.getSub_thrust_area()));
			psmt.setString(29, General.checknull(model.getRetirePiName()));
			psmt.setString(30, General.checknull(model.getBudgetHeads()));
			psmt.setString(31, General.checknull(model.getInst_charges()));
			psmt.setString(32, General.checknull(model.getProjunder()));
			psmt.setString(33, General.checknull(model.getfId()));
			//System.out.println("In Form 1 Update Mode-"+psmt);
			int cnt = psmt.executeUpdate();
			//System.out.println("check ---- "+psmt);
			if (cnt > 0){
				if(items!=null){
					Iterator<FileItem> iter = items.iterator();
					while (iter.hasNext()) {
						FileItem fileItem = iter.next();
						JSONObject obj = (JSONObject) consobj.get(chk);
						String docName="";
						if(obj.get("doctitle")!=null)
							docName=obj.get("doctitle").toString();						
						saveDoc(machine, mid, user_id, conn, fileItem, consobj, docName);						
						chk++;
					}
				}
				msg="1";
				conn.commit();
			} else {
				msg="0";
				conn.rollback();
			}
		} catch (Exception e) {
			msg=e.getMessage();
			System.out.println("Exception in ProjectSubmissionManager[Update]" + " " + e.getMessage());
			l.fatal(Logging.logException("ProjectSubmissionManager[Update]", e.toString()));
		} finally {
			try {System.out.println("msg||"+msg);
				if (rst != null)
					rst.close();
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return msg;
	}
	
	public static String getSequenceNoforChallan(String serail, String param, String val){
		l = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String qry="", seqNoWithDate="", scid="", chk[]=null;
		int seq_no=0, i=0;
		try{
			conn = DBConnection.getConnection();
			qry="select concat(ifnull(max(seq_no),0)+1 ,'~', concat('PP/',lpad(ifnull(max(seq_no),0)+1,4,0))) from generate_sequence_no "
					+ "where serial = '"+serail+"' and param='"+param+"'";
			psmt = conn.prepareStatement(qry);
			rst= psmt.executeQuery();
			if(rst.next()){
				scid=General.checknull(rst.getString(1));	
			}
					
			chk = scid.split("~");
			seq_no=Integer.parseInt(chk[0]);
			seqNoWithDate=chk[1];
			
			qry=""; psmt=null;
			if(seq_no == 1){
				qry=" insert into generate_sequence_no (serial, date, seq_no, param) values(?, date_format(now(),'%Y-%m-%d'),?, ?)";
				psmt = conn.prepareStatement(qry);
				psmt.setString(1, serail);
				psmt.setInt(2, seq_no);
				psmt.setString(3, param);
				i = psmt.executeUpdate();
			} else {
				if(!val.equals("V")){
					qry=" update generate_sequence_no set seq_no = ? where serial =? and param =? ";
					psmt = conn.prepareStatement(qry);				
					psmt.setInt(1, seq_no);
					psmt.setString(2, serail);
					psmt.setString(3, param);
					i = psmt.executeUpdate();	
				}
			}
		} catch (final Exception se) {
			System.out.println("Exception in ProjectSubmissionManager [getSequenceNo]:"+se.getMessage());
		}finally {
			try {
				if (rst != null)
					rst.close();
				if (psmt != null)
					psmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return seqNoWithDate;
	}

	public static String deletattchdata(String aid, String fname,String mastid) {
		l = Logger.getLogger("exceptionlog");
		String cSql="",msg="";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int count=0;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			cSql ="delete from rsrch_form1_attach where PS_DID=? ";
			pstmt = conn.prepareStatement(cSql);
			pstmt.setString(1, aid.trim());
			count = pstmt.executeUpdate();
			if(count>0){
				String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/FORM1/"+mastid+"/"+fname;
				File file = new File(directoryName);
				file.delete();
				msg="File Deleted Successfully";
				conn.commit();
			}		
		} catch (Exception e) {
			System.out.println("Error in ProjectSubmissionManager[deletattchdata] : "+e.getMessage());
			l.fatal(Logging.logException("ProjectSubmissionManager[deletattchdata]", e.getMessage().toString()));
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
	
	
	@SuppressWarnings({ "null", "resource" })
	public static String saveExtensionrequest(ProjectSubmissionModel model,String user_id,String machine) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;		
		String query = "", al="";
		int count = 0;
		try {
			conn = DBConnection.getConnection();
			
			/*String directoryName = ReadProps.getkeyValue("document.path", "sitsResource")+"RSRCH/FORM1/"+id+"/";
			File directory = new File(directoryName);
			*/
			query = "INSERT INTO rsrch_form1_proj_extension( PS_MID, EXTENSIONTYPE, PS_DUR_PROJECT_YEAR, PS_DUR_PROJECT_MONTH, CREATED_BY, CREATION_DATE, CREATION_MACHINE) VALUES (?, ?,?, ?, ?,NOW() ,?);";
			psmt = conn.prepareStatement(query);
			psmt.setString(1, model.getfId());
			psmt.setString(2, model.getProjET());
			psmt.setString(3, model.getExtdurPropProjYear());
			psmt.setString(4, model.getExtdurPropProjMonth());
			psmt.setString(5, user_id);
			psmt.setString(6, machine);
			//System.out.println("saveExtensionrequest >>>"+psmt);
			count = psmt.executeUpdate();
			
			if(count > 0){				
				psmt=null;rst=null;query="";
				query = "UPDATE rsrch_form1_mast set is_project_extension='Y' where PS_MID='"+model.getfId()+"';";
				psmt = conn.prepareStatement(query);
				//System.out.println("update rsrch_form1_mast >>>"+psmt);
				count = psmt.executeUpdate();
				al="1";
			}
			
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectSubmissionManager[saveExtensionrequest]", e.toString()));
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
				l.fatal(Logging.logException("ProjectSubmissionManager[saveExtensionrequest]", sql.toString()));
			}
		}
		return al;
	}
	
	public static ArrayList<ProjectSubmissionModel> getProjExtList(String id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();			
			query = "SELECT (case when extensiontype='wf' then 'With Financial Support' when extensiontype='wo' then 'Without Financial Support' else '' end) as extensiontype,PS_DUR_PROJECT_YEAR,PS_DUR_PROJECT_MONTH,DATE_FORMAT(CREATION_DATE, '%d %M %Y') as CREATION_DATE FROM rsrch_form1_proj_extension WHERE PS_MID='"+id+"' order by CREATION_DATE desc";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				ProjectSubmissionModel faModel = new ProjectSubmissionModel();
				
				faModel.setProjET(rst.getString("extensiontype"));
				faModel.setExtdurPropProjYear(rst.getString("PS_DUR_PROJECT_YEAR"));
				faModel.setExtdurPropProjMonth(rst.getString("PS_DUR_PROJECT_MONTH"));
				faModel.setCreateDate(rst.getString("CREATION_DATE"));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			l.fatal(Logging.logException("ProjectSubmissionManager[getEditFileList]", e.toString()));
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
				l.fatal(Logging.logException("ProjectSubmissionManager[getEditFileList]", sql.toString()));
			}
		}
		return al;
	}
}