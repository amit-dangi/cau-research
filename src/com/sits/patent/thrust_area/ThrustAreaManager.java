/**
 * @ AUTHOR TANUJALA
 */
package com.sits.patent.thrust_area;

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

public class ThrustAreaManager {
	static Logger log = Logger.getLogger("exceptionlog");

	@SuppressWarnings("unchecked")
	public static JSONObject save(ThrustAreaModel faModel, String machine, String user_id,String year) {
		Connection conn = null;
		ResultSet rst = null;
		PreparedStatement psmt = null;		
		String query = "", id="";
		int count = 0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			query = "select LPAD(CONVERT(IFNULL(MAX(SUBSTR(THRUST_AREA_ID ,4,6)),0)+1,SIGNED INTEGER),4,'0') from rsrch_thrust_area_mast";

			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			if (rst.next()) {
				id = General.checknull(rst.getString(1)).trim();
			}

			query = "";psmt = null;
			query = "INSERT INTO rsrch_thrust_area_mast (THRUST_AREA_ID, `THRUST_AREA`, `created_by`, `created_machine`, `created_date`) VALUES (?, ?, ?, ?, now())";
			
			psmt = conn.prepareStatement(query);
			psmt.setString(1, General.checknull("TA"+id));
			psmt.setString(2, General.checknull(faModel.getThrust_area()).trim().replaceAll("\\s+", " "));
			psmt.setString(3, General.checknull(user_id));
			psmt.setString(4, General.checknull(machine));
			
			count = psmt.executeUpdate();
			
			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Record Saved Successfully");
				jSonDataFinalObj.put("flag", "Y");
			 	}
			 else {
				conn.rollback();				
				jSonDataFinalObj.put("status", ApplicationConstants.FAIL);
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
			System.out.println("EXCEPTION CAUSED BY ThrustAreaManager :" + " " + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ThrustAreaManager[save]", e.toString()));
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
				System.out.println("EXCEPTION IN ThrustAreaManager CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());
			}
		}
		return jSonDataFinalObj;
	}

	public static ArrayList<ThrustAreaModel> getList(String headName) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();

			query = "SELECT THRUST_AREA_ID, THRUST_AREA FROM rsrch_thrust_area_mast WHERE 1=1 ";
			
			if(!General.checknull(headName).trim().equals("")){
				query+=" and THRUST_AREA like '%"+General.checknull(headName)+"%' ";
			}
			
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			
			while (rst.next()) {
				ThrustAreaModel faModel = new ThrustAreaModel();
				faModel.setThrust_area(General.checknull(rst.getString("THRUST_AREA")));
				faModel.setThrustId(General.checknull(rst.getString("THRUST_AREA_ID")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			log.fatal(Logging.logException("ThrustAreaManager[getList]", e.toString()));
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
				log.fatal(Logging.logException("ThrustAreaManager[getList]", sql.toString()));
			}
		}
		return al;
	}

	public static JSONObject update(ThrustAreaModel faModel, String machine, String user_id, String year) {
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

			query = "UPDATE rsrch_thrust_area_mast SET THRUST_AREA=?,  updated_by=?, updated_machine=?, updated_date=now() WHERE THRUST_AREA_ID=?";
			
			psmt = conn.prepareStatement(query);
			psmt.setString(1, faModel.getThrust_area().trim().replaceAll("\\s+", " "));
			psmt.setString(2, user_id);
			psmt.setString(3, machine);
			psmt.setString(4, faModel.getThrustId());
			count = psmt.executeUpdate();
			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Record Updated Successfully");
				jSonDataFinalObj.put("flag", "Y");
			} else {
				conn.rollback();
				jSonDataFinalObj.put("status", ApplicationConstants.FAIL);
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
			log.fatal(Logging.logException("ThrustAreaManager[update]", e.toString()));

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
			query = "DELETE FROM rsrch_thrust_area_mast WHERE THRUST_AREA_ID=?";
			psmt = conn.prepareStatement(query);
			psmt.setString(1, id);
			count = psmt.executeUpdate();
			
			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("status", "Record Deleted Successfully");
				jSonDataFinalObj.put("flag", "Y");
			 	}
			  else {
				conn.rollback();
				jSonDataFinalObj.put("status", ApplicationConstants.FAIL);
				jSonDataFinalObj.put("flag", "N");
			}
		} catch (Exception e) {
			if (e.toString().contains("REFERENCES")){
				jSonDataFinalObj.put("status", ApplicationConstants.DELETE_FORIEGN_KEY);
				jSonDataFinalObj.put("flag", "N");
			}
			System.out.println("EXCEPTION CAUSED BY ThrustAreaManager [delete]:"+ e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ThrustAreaManager [delete]", e.toString()));

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