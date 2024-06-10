/**
 * @ AUTHOR Chetan Sharma
 */
package com.sits.rsrch.research_activity.research_head;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.sits.commonApi.commonAPI;
import com.sits.conn.DBConnection;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;

public class ResearchHeadManager {
	static Logger log = Logger.getLogger("exceptionlog");

	@SuppressWarnings("unchecked")
	public static JSONObject save(ResearchHeadModel faModel, String machine, String user_id,String year) {
		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;		
		String query = "", id="";
		int count = 0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			query = "select LPAD(CONVERT(IFNULL(MAX(SUBSTR(head_id ,4,6)),0)+1,SIGNED INTEGER),4,'0') from rsrch_research_head";

			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			if (rst.next()) {
				id = General.checknull(rst.getString(1)).trim();
			}

			query = "";psmt = null;
			query = "INSERT INTO rsrch_research_head (head_id, `head_name`, `is_active`, `created_by`, `created_machine`, `created_date`) VALUES (?, ?, ?, ?, ?, now())";
			
			psmt = conn.prepareStatement(query);
			psmt.setString(1, General.checknull("RH"+id));
			psmt.setString(2, General.checknull(faModel.getHeadName()).trim().replaceAll("\\s+", " "));
			psmt.setString(3, General.checknull(faModel.getHeadType()).trim());
			psmt.setString(4, General.checknull(user_id));
			psmt.setString(5, General.checknull(machine));
			
			count = psmt.executeUpdate();
			
			if (count > 0) {
				/* 19/05/2023 New json added after save in any Head General Leadger
				 *  that will use in indent Purchase Indent Finance Approve/Reject page
				 *  data will save through api in GL_MAST & ACC_CURR_BAL_COST
				*/
				
				/*Code commented as per 12 dec Mail by cau Removed dependancy from Finance Leadger
				JSONObject headObject = new JSONObject(); 
				headObject.put("DDO", "");
				headObject.put("LOCATION", "");
				headObject.put("YEAR", General.checknull(year));
				headObject.put("LOGIN_ID", General.checknull(user_id));
				headObject.put("IP", General.checknull(machine));
				headObject.put("LEDGER_NAME", General.checknull(faModel.getHeadName()).trim().replaceAll("\\s+", " "));
				headObject.put("LEDGER_CODE", "RH"+General.checknull(id));
				headObject.put("BALANCE_SHEET_GROUP", "RESEARCH_HEAD");
				headObject.put("BANK", "B");
				headObject.put("SUB_LEDGER", "N");
				headObject.put("OPENING_BALANCE", "0");
				headObject.put("FSTATUS", "N");
				System.out.println("save research Head headObject|||"+headObject);
				
				JSONObject jsonobj1 = commonAPI.getDropDownByWebService("rest/FinanceApiServices/saveUpdateGeneralLeadger", headObject);
				headObject.clear();
				String flg=jsonobj1.get("flag").toString();
				System.out.println("flg:"+flg);
			 	if(flg.equals("Y")){
				conn.commit();
				jSonDataFinalObj.put("status", "Record Saved Successfully");
				jSonDataFinalObj.put("flag", "Y");
			 	}
			 	else if(flg.equals("N")){
			 		jSonDataFinalObj.put("status", jsonobj1.get("errmsg").toString());
					jSonDataFinalObj.put("flag", "N");
			 		conn.rollback();
			 	}*/
				
			 	conn.commit();
				jSonDataFinalObj.put("status", "Record Saved Successfully");
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
			System.out.println("EXCEPTION CAUSED BY ResearchHeadManager :" + " " + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ResearchHeadManager[save]", e.toString()));
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
				System.out.println("EXCEPTION IN ResearchHeadManager CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}

	public static ArrayList<ResearchHeadModel> getList(String headName, String status) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();

			query = "SELECT head_id, head_name, is_active FROM rsrch_research_head WHERE 1=1 ";
			
			if(!General.checknull(headName).trim().equals("")){
				query+=" and head_name like '%"+General.checknull(headName)+"%' ";
			}
			if(!General.checknull(status).trim().equals("")){
				query+=" and is_active = '"+General.checknull(status)+"' ";
			}
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				ResearchHeadModel faModel = new ResearchHeadModel();
				faModel.setHeadId(General.checknull(rst.getString("head_id")));
				faModel.setHeadName(General.checknull(rst.getString("head_name")));
				faModel.setHeadType(General.checknull(rst.getString("is_active")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			log.fatal(Logging.logException("ResearchHeadManager[getList]", e.toString()));
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
				log.fatal(Logging.logException("ResearchHeadManager[getList]", sql.toString()));
			}
		}
		return al;
	}

	public static JSONObject update(ResearchHeadModel faModel, String machine, String user_id, String year) {
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

			query = "UPDATE rsrch_research_head SET head_name=?, is_active=?, updated_by=?, updated_machine=?, updated_date=now() WHERE head_id=?";
			
			psmt = conn.prepareStatement(query);
			psmt.setString(1, faModel.getHeadName().trim().replaceAll("\\s+", " "));
			psmt.setString(2, faModel.getHeadType().trim().replaceAll("\\s+", " "));
			psmt.setString(3, user_id);
			psmt.setString(4, machine);
			psmt.setString(5, faModel.getHeadId());
			count = psmt.executeUpdate();
			if (count > 0) {

				/* 19/05/2023 New json added after Update in any Head General Leadger
				 *  that will use in indent Purchase Indent Finance Approve/Reject page
				 *  data will save through api in GL_MAST & ACC_CURR_BAL_COST
				 */
				
				/*Code commented as per 12 dec Mail by cau Removed dependancy from Finance Leadger
				 * JSONObject headObject = new JSONObject(); 
				headObject.put("DDO", "");
				headObject.put("LOCATION", "");
				headObject.put("YEAR", General.checknull(year));
				headObject.put("LOGIN_ID", General.checknull(user_id));
				headObject.put("IP", General.checknull(machine));
				headObject.put("LEDGER_NAME", General.checknull(faModel.getHeadName()).trim().replaceAll("\\s+", " "));
				headObject.put("LEDGER_CODE", General.checknull(faModel.getHeadId()));
				headObject.put("BALANCE_SHEET_GROUP", "RESEARCH_HEAD");
				headObject.put("BANK", "B");
				headObject.put("SUB_LEDGER", "N");
				headObject.put("OPENING_BALANCE", "0");
				headObject.put("FSTATUS", "E");
				
				JSONObject jsonobj1 = commonAPI.getDropDownByWebService("rest/FinanceApiServices/saveUpdateGeneralLeadger", headObject);
				headObject.clear();
				String flg=jsonobj1.get("flag").toString();
			 	if(flg.equals("Y")){
				conn.commit();
				jSonDataFinalObj.put("status", "Record Saved Successfully");
				jSonDataFinalObj.put("flag", "Y");
			 	}
			 	else if(flg.equals("N")){
			 		jSonDataFinalObj.put("status", jsonobj1.get("errmsg").toString());
					jSonDataFinalObj.put("flag", "N");
			 		conn.rollback();
			 	}*/
			 	
			 	conn.commit();
				jSonDataFinalObj.put("status", "Record Updated Successfully");
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
			log.fatal(Logging.logException("ResearchHeadManager[update]", e.toString()));

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
				System.out.println("EXCEPTION IN CLOSING CONNECTION IS CAUSED BY :" + " " + e.getMessage().trim().toUpperCase());
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
			query = "DELETE FROM rsrch_research_head WHERE head_id=?";
			psmt = conn.prepareStatement(query);
			psmt.setString(1, id);
			count = psmt.executeUpdate();
			
			if (count > 0) {
				/*19/05/2023 New json added before delete any Head General Leadger
				 *  that will use in indent Purchase Indent Finance Approve/Reject page
				 *  data will delete through api from GL_MAST & ACC_CURR_BAL_COST
				 */
				
			/*	Code commented as per 12 dec Mail by cau Removed dependancy from Finance Leadger
			 * JSONObject headObject = new JSONObject(); 
				headObject.put("LEDGER_CODE", General.checknull(id));
				
				JSONObject jsonobj1 = commonAPI.getDropDownByWebService("rest/FinanceApiServices/deleteGeneralLeadger", headObject);
				headObject.clear();
				String flg=jsonobj1.get("flag").toString();
				System.out.println("flg:"+flg);
			 	if(flg.equals("Y")){
				conn.commit();
				jSonDataFinalObj.put("status", "Record Deleted Successfully");
				jSonDataFinalObj.put("flag", "Y");
			 	}
			 	else if(flg.equals("N")){
			 		jSonDataFinalObj.put("status", jsonobj1.get("errmsg").toString());
					jSonDataFinalObj.put("flag", "N");
			 		conn.rollback();
			 	}
			 	*/
			 	conn.commit();
				jSonDataFinalObj.put("status", "Record Deleted Successfully");
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
			System.out.println("EXCEPTION CAUSED BY ResearchHeadManager [delete]:"+ e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ResearchHeadManager [delete]", e.toString()));

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