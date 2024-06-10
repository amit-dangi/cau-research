package com.sits.rsrch.reports.research_proposal_report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.sits.commonApi.commonAPI;
import com.sits.conn.DBConnection;
import com.sits.general.General;
import com.sits.general.Logging;

public class ResearchProposalReportManager {
	static Logger log = Logger.getLogger("exceptionlog");
	
	public  static synchronized ArrayList<ResearchProposalReportModel> getList(String fstatus, String  fd, String td, String empId, String deptId, String desId){
		Connection conn=null;
		PreparedStatement psmt=null;
		ResultSet rst=null;
		String qry="";
		JSONObject finalObject=new JSONObject();
		ArrayList<ResearchProposalReportModel> list=new ArrayList<ResearchProposalReportModel>();
		try {
			conn = DBConnection.getConnection();
			
			qry="select distinct rm.PS_TITTLE_PROJ, rm.PS_PRINCIPAL as empId, "
					//+ "(select employeeName from "+dbName+".employee_mast where employeeId=rm.PS_PRINCIPAL) empName, "
					+ "rm.PS_FUND_AGNCY, fa.fa_Name, date_format(rm.submitted_date, '%d/%m/%Y') as submitted_date, rpa.RPA_TYPE, rpa.RPA_STATUS "
					+ "from rsrch_form1_mast rm, rsrch_research_prop_approval rpa, rsrch_funding_agency fa "
					+ "where rm.PS_MID=rpa.PS_MID and fa.fa_id=rm.PS_FUND_AGNCY and rm.PS_PRINCIPAL=? and rm.PS_DEPT=? and rm.PS_DESG=? "
					+ "and rpa.CREATED_DATE = (select max(cc.CREATED_DATE) from rsrch_research_prop_approval cc where cc.PS_MID=PS_MID) order by rpa.PS_MID";
			
			psmt=conn.prepareStatement(qry);
			psmt.setString(1, empId);
			psmt.setString(2, deptId);
			psmt.setString(3, desId);
			//psmt.setString(4,session);
			rst=psmt.executeQuery();
			while(rst.next()){
				JSONObject jsonobj=new JSONObject();
				ResearchProposalReportModel model=new ResearchProposalReportModel();
				model.setEmpId(General.checknull(rst.getString("empId")));
				
				finalObject.put("tablename", "employee_mast");
				finalObject.put("columndesc", "concat(employeeName,' (',employeeCodeM,')')");
				finalObject.put("id", "employeeId");
				finalObject.put("idvalue", rst.getString("empId"));
				jsonobj = commonAPI.getDropDownByWebService("rest/apiServices/commonApi", finalObject);
				finalObject.clear();
				
				if(!jsonobj.isEmpty())
					model.setEmpName(General.checknull(jsonobj.get("display_name").toString()));
				else
					model.setEmpName(General.checknull(""));
				
				//model.setEmpName(General.checknull(rst.getString("empName")));				
				model.setFa_Name(General.checknull(rst.getString("fa_Name")));
				model.setTproj(General.checknull(rst.getString("PS_TITTLE_PROJ")));
				model.setSubmitted_date(General.checknull(rst.getString("submitted_date")));
				
				String name="", val="";
				if(General.checknull(rst.getString("RPA_TYPE")).equals("HD")){
					name="HOD/DEAN";
				}else if(General.checknull(rst.getString("RPA_TYPE")).equals("RP")){
					name="RP Cell";
				}else if(General.checknull(rst.getString("RPA_TYPE")).equals("RR")){
					name="Registrar";
				}else if(General.checknull(rst.getString("RPA_TYPE")).equals("VC")){
					name="VC Office";
				}else{
					name="";
				}
				
				if(General.checknull(rst.getString("RPA_STATUS")).equals("R")){
					val="Rejected By "+name;
				}else if(General.checknull(rst.getString("RPA_STATUS")).equals("P")){
					val="Pending By "+name;
				}else if(General.checknull(rst.getString("RPA_STATUS")).equals("A")){
					val="Approved";
				}else{
					val="";
				}
				
				model.setStatus(General.checknull(val));
				list.add(model);
			}
		}catch (Exception e) {
			
			System.out.println("EXCEPTION CAUSED ResearchProposalReportManager[findAmountByRules]" + " " + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ResearchProposalReportManager[findAmountByRules]", e.toString()));
		} 
		finally {
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
		return list ;
	}	
	
}
