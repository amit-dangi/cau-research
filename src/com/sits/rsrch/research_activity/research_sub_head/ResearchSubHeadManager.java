/**
 * @ AUTHOR Chetan Sharma
 */
package com.sits.rsrch.research_activity.research_sub_head;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.sits.conn.DBConnection;
import com.sits.rsrch.research_activity.research_sub_head.ResearchSubHeadManager;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;

public class ResearchSubHeadManager {
	static Logger log = Logger.getLogger("exceptionlog");

	public static JSONObject save(ResearchSubHeadModel faModel, String machine, String user_id) {

		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;
		String query = "", id = "";
		int count = 0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			query = "select LPAD(CONVERT(IFNULL(MAX(SUBSTR(sub_head_id ,4,6)),0)+1,SIGNED INTEGER),4,'0') from rsrch_research_sub_head";

			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			if (rst.next()) {
				id = General.checknull(rst.getString(1)).trim();
			}

			query = "";
			psmt = null;

			query = "INSERT INTO rsrch_research_sub_head (`sub_head_id`,`head_id`,`location_typ`,`location_name`, `sub_head_name` ,`is_active`, `created_by`, `created_machine`, `created_date`) "
					+ "VALUES (?, ?, ?, ?,?, ?, ?, ?, now())";

			psmt = conn.prepareStatement(query);
			psmt.setString(1, General.checknull("RS" + id));
			psmt.setString(2, General.checknull(faModel.gethId()).trim().replaceAll("\\s+", " "));
			if (faModel.getLocation().equals("") || faModel.getLocation().equals(null)) {
				psmt.setString(3, null);
			} else {
				psmt.setString(3, faModel.getLocation().trim().replaceAll("\\s+", " "));
			}
			if (faModel.getLocation_name().equals("") || faModel.getLocation_name().equals(null)) {
				psmt.setString(4, null);
			} else {
				psmt.setString(4, faModel.getLocation_name().trim().replaceAll("\\s+", " "));
			}
			if (faModel.getSubHeadName().equals("") || faModel.getSubHeadName().equals(null)) {
				psmt.setString(5, null);
			} else {
				psmt.setString(5, faModel.getSubHeadName().trim().replaceAll("\\s+", " "));
			}
			psmt.setString(6, General.checknull(faModel.getSubHeadType()).trim());
			psmt.setString(7, General.checknull(user_id));
			psmt.setString(8, General.checknull(machine));

			count = psmt.executeUpdate();

			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Record Saved Successfully");
				jSonDataFinalObj.put("flag", "Y");
			} else {
				conn.rollback();
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
		} catch (Exception e) {
			if (e.toString().contains("Duplicate")) {
				jSonDataFinalObj.put("status", ApplicationConstants.UNIQUE_CONSTRAINT);
				jSonDataFinalObj.put("flag", "N");
			}
			if (e.toString().contains("REFERENCES")) {
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
			System.out.println("EXCEPTION CAUSED BY ResearchSubHeadManager :" + " " + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ResearchSubHeadManager[save]", e.toString()));
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
				System.out.println("EXCEPTION IN ResearchSubHeadManager CLOSING CONNECTION IS CAUSED BY:" + " "
						+ e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}

	public static ArrayList<ResearchSubHeadModel> getList(String headName, String subHeadName, String status,
			String locationTyp) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();

			query = "SELECT sub_head_id, head_id,location_typ,location_name, sub_head_name, is_active, (select head_name from rsrch_research_head where head_id=a.head_id) as head_name "
					+ "FROM rsrch_research_sub_head a WHERE 1=1 ";

			if (!General.checknull(headName).trim().equals("")) {
				query += " and head_id = '" + General.checknull(headName) + "' ";
			}
			if (!General.checknull(subHeadName).trim().equals("")) {
				query += " and sub_head_name like '%" + General.checknull(subHeadName) + "%' ";
			}
			if (!General.checknull(status).trim().equals("")) {
				query += " and is_active = '" + General.checknull(status) + "' ";

			}
			if (!General.checknull(locationTyp).trim().equals("")) {
				query += " and location_typ = '" + General.checknull(locationTyp) + "' ";
			}

			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();

			while (rst.next()) {
				ResearchSubHeadModel faModel = new ResearchSubHeadModel();
				faModel.setSubHeadId(General.checknull(rst.getString("sub_head_id")));
				faModel.setLocation(General.checknull(rst.getString("location_typ")));
				faModel.setLocation_name(General.checknull(rst.getString("location_name")));
				faModel.sethId(General.checknull(rst.getString("head_id")));
				faModel.sethName(General.checknull(rst.getString("head_name")));
				faModel.setSubHeadName(General.checknull(rst.getString("sub_head_name")));
				faModel.setSubHeadType(General.checknull(rst.getString("is_active")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			log.fatal(Logging.logException("ResearchSubHeadManager[getList]", e.toString()));
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
				log.fatal(Logging.logException("ResearchSubHeadManager[getList]", sql.toString()));
			}
		}
		return al;
	}

	public static JSONObject update(ResearchSubHeadModel faModel, String machine, String user_id) {
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

			query = "UPDATE rsrch_research_sub_head SET location_typ=?,location_name=?,sub_head_Name=?, head_id=?, is_active=?, "
					+ "updated_by=?, updated_machine=?, updated_date=now() WHERE sub_head_id=?";

			psmt = conn.prepareStatement(query);
			if (faModel.getLocation().equals("") || faModel.getLocation().equals(null)) {
				psmt.setString(1, null);
			} else {
				psmt.setString(1, faModel.getLocation().trim().replaceAll("\\s+", " "));
			}
			if (faModel.getLocation_name().equals("") || faModel.getLocation_name().equals(null)) {
				psmt.setString(2, null);
			} else {
				psmt.setString(2, faModel.getLocation_name().trim().replaceAll("\\s+", " "));
			}
			if (faModel.getSubHeadName().equals("") || faModel.getSubHeadName().equals(null)) {
				psmt.setString(3, null);
			} else {
				psmt.setString(3, faModel.getSubHeadName().trim().replaceAll("\\s+", " "));
			}
			psmt.setString(4, faModel.gethId().trim().replaceAll("\\s+", " "));
			psmt.setString(5, faModel.getSubHeadType().trim().replaceAll("\\s+", " "));
			psmt.setString(6, user_id);
			psmt.setString(7, machine);
			psmt.setString(8, faModel.getSubHeadId());
			count = psmt.executeUpdate();
			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Record Updated Successfully");
				jSonDataFinalObj.put("flag", "Y");
			} else {
				conn.rollback();
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
		} catch (Exception e) {
			if (e.toString().contains("Duplicate")) {
				jSonDataFinalObj.put("status", ApplicationConstants.UNIQUE_CONSTRAINT);
				jSonDataFinalObj.put("flag", "N");
			}
			if (e.toString().contains("REFERENCES")) {
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
			System.out.println("EXCEPTION CAUSED BY:" + " " + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ResearchSubHeadManager[update]", e.toString()));

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
						"EXCEPTION IN CLOSING CONNECTION IS CAUSED BY :" + " " + e.getMessage().trim().toUpperCase());
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
			query = "DELETE FROM rsrch_research_sub_head WHERE sub_head_id=?";
			psmt = conn.prepareStatement(query);
			psmt.setString(1, id);
			count = psmt.executeUpdate();

			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Record Deleted Successfully");
				jSonDataFinalObj.put("flag", "Y");
			} else {
				conn.rollback();
				jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
				jSonDataFinalObj.put("flag", "N");
			}
		} catch (Exception e) {
			if (e.toString().contains("REFERENCES")) {
				jSonDataFinalObj.put("status", ApplicationConstants.DELETE_FORIEGN_KEY);
				jSonDataFinalObj.put("flag", "N");
			}
			System.out.println("EXCEPTION CAUSED BY ResearchSubHeadManager [delete]:" + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ResearchSubHeadManager [delete]", e.toString()));

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

}