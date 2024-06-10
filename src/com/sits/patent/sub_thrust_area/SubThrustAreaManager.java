/**
 * @ AUTHOR TANUJALA
 */
package com.sits.patent.sub_thrust_area;

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

public class SubThrustAreaManager {
	static Logger log = Logger.getLogger("exceptionlog");

	public static JSONObject save(SubThrustAreaModel faModel, String machine, String user_id) {

		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;
		String query = "", id = "";
		int count = 0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			query = "select LPAD(CONVERT(IFNULL(MAX(SUBSTR(SUB_THRUST_AREA_ID ,4,6)),0)+1,SIGNED INTEGER),3,'0') from rsrch_SUB_thrust_area_mast";

			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			if (rst.next()) {
				id = General.checknull(rst.getString(1)).trim();
			}

			query = "";
			psmt = null;

			query = "INSERT INTO rsrch_SUB_thrust_area_mast (`SUB_THRUST_AREA_ID`,`THRUST_AREA_ID`,`SUB_THRUST_AREA` ,`created_by`, `created_machine`, `created_date`) "
					+ "VALUES (?, ?, ?, ?,?,  now())";

			psmt = conn.prepareStatement(query);
			psmt.setString(1, General.checknull("TSA" + id));
			psmt.setString(2, General.checknull(faModel.getThrust_area()).trim());
			psmt.setString(3, General.checknull(faModel.getSub_thrust_area()).trim());
			psmt.setString(4, General.checknull(user_id));
			psmt.setString(5, General.checknull(machine));
			//System.out.println("psmt"+psmt);
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
			System.out.println("EXCEPTION CAUSED BY SubThrustAreaManager :" + " " + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("SubThrustAreaManager[save]", e.toString()));
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
				System.out.println("EXCEPTION IN SubThrustAreaManager CLOSING CONNECTION IS CAUSED BY:" + " "
						+ e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}

	public static ArrayList<SubThrustAreaModel> getList(String thrust_area, String sub_thrust_area) {
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();

			query = "SELECT SUB_THRUST_AREA_ID, THRUST_AREA_ID, SUB_THRUST_AREA,(select THRUST_AREA from rsrch_thrust_area_mast where THRUST_AREA_ID=a.THRUST_AREA_ID) as thrust_area "
					+ "FROM rsrch_SUB_thrust_area_mast a WHERE 1=1 ";

			if (!General.checknull(thrust_area).trim().equals("")) {
				query += " and THRUST_AREA_ID = '" + General.checknull(thrust_area) + "' ";
			}
			if (!General.checknull(sub_thrust_area).trim().equals("")) {
				query += " and sub_THRUST_AREA like '%" + General.checknull(sub_thrust_area) + "%' ";
			}

			psmt = conn.prepareStatement(query);
			//System.out.println("psmt"+psmt);
			rst = psmt.executeQuery();

			while (rst.next()) {
				SubThrustAreaModel faModel = new SubThrustAreaModel();
				faModel.setId(General.checknull(rst.getString("SUB_THRUST_AREA_ID")));
				faModel.setThrust_area_id(General.checknull(rst.getString("THRUST_AREA_ID")));
				faModel.setThrust_area(General.checknull(rst.getString("THRUST_AREA")));
				faModel.setSub_thrust_area(General.checknull(rst.getString("sub_THRUST_AREA")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			log.fatal(Logging.logException("SubThrustAreaManager[getList]", e.toString()));
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
				log.fatal(Logging.logException("SubThrustAreaManager[getList]", sql.toString()));
			}
		}
		return al;
	}

	public static JSONObject update(SubThrustAreaModel faModel, String machine, String user_id) {
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

			query = "UPDATE rsrch_SUB_thrust_area_mast SET sub_THRUST_AREA=?, THRUST_AREA_ID=?, "
					+ "updated_by=?, updated_machine=?, updated_date=now() WHERE SUB_THRUST_AREA_ID=?";

			psmt = conn.prepareStatement(query);
			
			psmt.setString(1, faModel.getSub_thrust_area().trim().replaceAll("\\s+", " "));
			psmt.setString(2, faModel.getThrust_area());
			psmt.setString(3, user_id);
			psmt.setString(4, machine);
			psmt.setString(5, faModel.getId());
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
			log.fatal(Logging.logException("SubThrustAreaManager[update]", e.toString()));

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
			query = "DELETE FROM rsrch_SUB_thrust_area_mast WHERE SUB_THRUST_AREA_ID=?";
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
			System.out.println("EXCEPTION CAUSED BY SubThrustAreaManager [delete]:" + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("SubThrustAreaManager [delete]", e.toString()));

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