/**
 * @ AUTHOR Chetan Sharma
 */
package com.sits.rsrch.funding_agency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.sits.conn.DBConnection;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;

public class FundingAgencyManager {
	static Logger log = Logger.getLogger("exceptionlog");

	public static JSONObject save(FundingAgencyModel faModel, String machine, String user_id) {
		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;		
		String query = "";
		int count = 0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			query = "INSERT INTO RSRCH_FUNDING_AGENCY (`fa_Name`, `fa_Type`, `fa_Contact_No`, `fa_Web_url`, `fa_Address`, `fa_Detail`,`fundedby`, "
					+ "`created_by`, `created_machine`, `created_date`) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, now())";
			psmt = conn.prepareStatement(query);
			
			psmt.setString(1, General.checknull(faModel.getFaName()).trim().replaceAll("\\s+", " "));
			psmt.setString(2, General.checknull(faModel.getFaType()).trim());
			psmt.setString(3, General.checknull(faModel.getFaMobNo()).trim().replaceAll("\\s+", " "));
			psmt.setString(4, General.checknull(faModel.getFaUrl()).trim().replaceAll("\\s+", " "));
			psmt.setString(5, General.checknull(faModel.getFaAddr()).trim().replaceAll("\\s+", " "));
			psmt.setString(6, General.checknull(faModel.getFaDetail()).trim().replaceAll("\\s+", " "));
			psmt.setString(7, General.checknull(faModel.getFundedby()).trim().replaceAll("\\s+", " "));
			psmt.setString(8, General.checknull(user_id));
			psmt.setString(9, General.checknull(machine));
			
			count = psmt.executeUpdate();
			
			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Funding Agency Saved Successfully");
				jSonDataFinalObj.put("flag", "Y");
			} else {
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
			System.out.println("EXCEPTION CAUSED BY FundingAgencyManager :" + " " + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundingAgencyManager[save]", e.toString()));
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
				System.out.println("EXCEPTION IN FundingAgencyManager CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}

	public static ArrayList<FundingAgencyModel> getList(FundingAgencyModel fam) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();
			query = "SELECT fa_id, fa_Name, fa_Type, fa_Contact_No, fa_Web_url, fa_Address, fa_Detail,fundedby "
					+ "FROM RSRCH_FUNDING_AGENCY WHERE 1=1 ";
			if(!General.checknull(fam.getFaName()).trim().equals("")){
				query+=" and fa_Name like '%"+General.checknull(fam.getFaName())+"%' ";
			}
			if(!General.checknull(fam.getFaType()).trim().equals("")){
				query+=" and fa_Type = '"+General.checknull(fam.getFaType())+"' ";
			}
			if(!General.checknull(fam.getFaMobNo()).trim().equals("")){
				query+=" and fa_Contact_No like '%"+General.checknull(fam.getFaMobNo())+"%' ";
			}
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				FundingAgencyModel faModel = new FundingAgencyModel();
				faModel.setFaId(General.checknull(rst.getString("fa_id")));
				faModel.setFaName(General.checknull(rst.getString("fa_Name")));
				faModel.setFaType(General.checknull(rst.getString("fa_Type")));
				faModel.setFaMobNo(General.checknull(rst.getString("fa_Contact_No")));
				faModel.setFaUrl(General.checknull(rst.getString("fa_Web_url")));
				faModel.setFaAddr(General.checknull(rst.getString("fa_Address")));
				faModel.setFaDetail(General.checknull(rst.getString("fa_Detail")));
				faModel.setFundedby(General.checknull(rst.getString("fundedby")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			log.fatal(Logging.logException("FundingAgencyManager[getList]", e.toString()));
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
				log.fatal(Logging.logException("FundingAgencyManager[getList]", sql.toString()));
			}
		}
		return al;
	}

	
	public static JSONObject update(FundingAgencyModel faModel, String machine, String user_id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String query = "";
		int count = 0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			psmt = null;

			query = "UPDATE RSRCH_FUNDING_AGENCY SET fa_Name=?, fa_Type=?, fa_Contact_No=?, fa_Web_url=?, fa_Address=?, "
					+ "fa_Detail=?, updated_by=?, updated_machine=?, updated_date=now() WHERE fa_id=?";
			
			psmt = conn.prepareStatement(query);
			psmt.setString(1, faModel.getFaName().trim().replaceAll("\\s+", " "));
			psmt.setString(2, faModel.getFaType().trim().replaceAll("\\s+", " "));
			psmt.setString(3, faModel.getFaMobNo().trim().replaceAll("\\s+", " "));
			psmt.setString(4, faModel.getFaUrl().trim().replaceAll("\\s+", " "));		
			psmt.setString(5, faModel.getFaAddr().trim().replaceAll("\\s+", " "));
			psmt.setString(6, faModel.getFaDetail().trim().replaceAll("\\s+", " "));
			psmt.setString(7, user_id);
			psmt.setString(8, machine);
			psmt.setString(9, faModel.getFaId());
			count = psmt.executeUpdate();
			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Funding Agency Updated Successfully");
				jSonDataFinalObj.put("flag", "Y");
			} else {
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
			System.out.println("EXCEPTION CAUSED BY:" + " " + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundingAgencyManager[update]", e.toString()));

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
				System.out.println(
						"EXCEPTION IN CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());

			}
		}
		return jSonDataFinalObj;
	}

	public static JSONObject delete(String id, String machine, String user_id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String query = "";
		int count = 0;
		JSONObject jSonDataFinalObj = new JSONObject();
		
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			psmt = null;
			query = "DELETE FROM RSRCH_FUNDING_AGENCY WHERE fa_id=?";
			psmt = conn.prepareStatement(query);
			psmt.setString(1, id);
			count = psmt.executeUpdate();
			
			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Funding Agency Deleted Successfully");
				jSonDataFinalObj.put("flag", "Y");
			} else {
				conn.rollback();
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
		} catch (Exception e) {
			if (e.toString().contains("REFERENCES")){
				jSonDataFinalObj.put("status", ApplicationConstants.DELETE_FORIEGN_KEY);
				jSonDataFinalObj.put("flag", "N");
			}
			System.out.println("EXCEPTION CAUSED BY FundingAgencyManager :"+ e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundingAgencyManager[deleteGovtBudgetHead]", e.toString()));

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
				System.out.println("EXCEPTION IN CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());

			}
		}
		return jSonDataFinalObj;
	}
}