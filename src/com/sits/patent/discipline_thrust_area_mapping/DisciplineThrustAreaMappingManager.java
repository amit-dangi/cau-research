package com.sits.patent.discipline_thrust_area_mapping;

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

public class DisciplineThrustAreaMappingManager {
	static Logger log = Logger.getLogger("exceptionlog");

	public static JSONObject save(DisciplineThrustAreaMappingModel model, String user_id, String ip) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String query = "";
		int count = 0, i=0;
		JSONObject jSonDataFinalObj = new JSONObject();
		try {
			System.out.println("psmt manager");
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			query = "INSERT INTO discipline_thrust_area_mapping (CR_ID,THRUST_AREA_ID,SUB_THRUST_AREA_ID,created_by,"
					+ " CREATED_DATE,created_machine)"
					+ " values(?,?,?,?,now(),?)";
			psmt = conn.prepareStatement(query);
			psmt.setString(++i, General.checknull(model.getDiscipline()));
			psmt.setString(++i, General.checknull(model.getThrust_area()));
			psmt.setString(++i, General.checknull(model.getSub_thrust_area()));
			psmt.setString(++i, user_id);
			psmt.setString(++i, ip);
			System.out.println("psmt"+psmt);
			count = psmt.executeUpdate();

			if (count > 0) {
				conn.commit();
				jSonDataFinalObj.put("errMsg", ApplicationConstants.SAVE);
				jSonDataFinalObj.put("flg", "Y");

			} else {
				conn.rollback();
				jSonDataFinalObj.put("errMsg", ApplicationConstants.EXCEPTION_MESSAGE);
			}
		} 
	catch (Exception e) {

	if (e.toString().contains("Duplicate"))
		jSonDataFinalObj.put("errMsg", ApplicationConstants.UNIQUE_CONSTRAINT);

	if (e.toString().contains("REFERENCES"))
		jSonDataFinalObj.put("status", ApplicationConstants.EXCEPTION_MESSAGE);
	System.out.println("EXCEPTION CAUSED BY:" + " " + e.getMessage().toUpperCase());
	log.fatal(Logging.logException("DisciplineThrustAreaMappingManager[saveRecord]", e.toString()));

} 
	
	 finally {
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

	public static ArrayList<DisciplineThrustAreaMappingModel> getList(DisciplineThrustAreaMappingModel model) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList<DisciplineThrustAreaMappingModel> al = new ArrayList<DisciplineThrustAreaMappingModel>();
		String query = "";
		DisciplineThrustAreaMappingModel model1 = null;
		try {
			conn = DBConnection.getConnection();
			query = "select id,(select course_name from cau_iums.academic_degree_desc_mast adm where a.CR_ID=adm.CR_ID)as dis,"
					+ " (select SUB_THRUST_AREA from rsrch_SUB_thrust_area_mast m where a.SUB_THRUST_AREA_ID=m.SUB_THRUST_AREA_ID)as subthrustarea "
					+ " ,(select THRUST_AREA from rsrch_thrust_area_mast m where a.THRUST_AREA_ID=m.THRUST_AREA_ID)as thrustarea "
					+ "  FROM discipline_thrust_area_mapping a where 1=1 ";
			if (!General.checknull(model.getDiscipline()).equals("")) {
				query += "  and a.CR_ID = '" + model.getDiscipline() + "' ";
			}
			if (!General.checknull(model.getThrust_area()).equals("")) {
				query += "  and a.THRUST_AREA_ID='" + model.getThrust_area() + "' ";
			}
			if (!General.checknull(model.getSub_thrust_area()).equals("")) {
				query += " AND a.SUB_THRUST_AREA_ID='" + model.getSub_thrust_area() + "' ";
			}
			System.out.println("query"+query);
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			while (rst.next()) {
				model1 = new DisciplineThrustAreaMappingModel();
				model1.setDiscipline(General.checknull(rst.getString("dis")));
				model1.setThrust_area(General.checknull(rst.getString("thrustarea")));
				model1.setSub_thrust_area(General.checknull(rst.getString("subthrustarea")));
				model1.setId(General.checknull(rst.getString("id")));
				al.add(model1);
			}
		} catch (Exception e) {
			System.out.println("EXCEPTION CAUSED BY:" + " " + e.getMessage().toUpperCase());
			log.fatal(Logging.logException("DisciplineThrustAreaMappingManager[getList]", e.toString()));
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
			}
		}

		return al;
	}

	public static DisciplineThrustAreaMappingModel EditRecord(String id) {
		DisciplineThrustAreaMappingModel model = new DisciplineThrustAreaMappingModel();
		String cSql = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int i = 1;
		try {

			conn = DBConnection.getConnection();
			cSql = "select CR_ID,THRUST_AREA_ID,SUB_THRUST_AREA_ID  from discipline_thrust_area_mapping where id=? ";
			pstmt = conn.prepareStatement(cSql);
			pstmt.setString(i++, id.trim());
			System.out.println("pstmt viewrecord"+pstmt);
			rst = pstmt.executeQuery();
			if (rst.next() == false) {
			} else {
				do {
					model.setDiscipline(General.checknull(rst.getString("CR_ID")));
					model.setThrust_area(General.checknull(rst.getString("THRUST_AREA_ID")));
					model.setSub_thrust_area(General.checknull(rst.getString("SUB_THRUST_AREA_ID")));
					model.setId(General.checknull(rst.getString("id")));
				} while (rst.next());
			}
		} catch (Exception e) {
			System.out.println("Error in DisciplineThrustAreaMappingManager[EditRecord] : " + e.getMessage());
			log.fatal(Logging.logException("DisciplineThrustAreaMappingManager[EditRecord]", e.getMessage().toString()));
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
		return model;
	}

	public static JSONObject update(DisciplineThrustAreaMappingModel model, String user_id, String ip) {

		String  qry = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int count = 0, count1 = 0, count2 = 0,i=0;
		JSONObject jsonObject = new JSONObject();
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			qry = " update discipline_thrust_area_mapping set CR_ID=?, THRUST_AREA_ID=?,SUB_THRUST_AREA_ID=?";
			
			qry +=" ,UPDATED_BY=?, UPDATED_DATE=now(), UPDATED_MACHINE=? ";
			qry +=" where id=? ";
				pstmt = conn.prepareStatement(qry);
				pstmt.setString(++i, General.checknull(model.getDiscipline().trim()));
				pstmt.setString(++i, General.checknull(model.getThrust_area().trim()));
				pstmt.setString(++i, General.checknull(model.getSub_thrust_area()));
				pstmt.setString(++i, General.checknull(user_id));
				pstmt.setString(++i, General.checknull(ip));
				pstmt.setString(++i, General.checknull(model.getId().trim()));
				System.out.println("pstmt"+pstmt);
				count1 = pstmt.executeUpdate();
				if (count1 > 0) {
							conn.commit();
							jsonObject.put("errMsg", ApplicationConstants.UPDATED);
							jsonObject.put("flg", "Y");

						} else {
							conn.rollback();
							jsonObject.put("errMsg", ApplicationConstants.EXCEPTION_MESSAGE);
						}
		} catch (Exception e) {
			System.out.println("EXCEPTION CAUSED BY :" + " " + e.getMessage().toUpperCase() + "" + "|" + e);
			log.fatal(Logging.logException("DisciplineThrustAreaMappingManager[update]", e.toString()));

		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println(
						"EXCEPTION IN CLOSING CONNECTION IS CAUSED BY:" + " " + e.getMessage().trim().toUpperCase());
			}
		}
		return jsonObject;
	}

	public static JSONObject deleteRecord(String id, String user_id, String ip) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int  count = 0;
		String qry = "", status = "", str = "", flg = "N";
		JSONObject obj = new JSONObject();

		try {

			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
				qry = "delete from  discipline_thrust_area_mapping  where id='" + id + "'";
				pstmt = conn.prepareStatement(qry);
				count = pstmt.executeUpdate();

			if (count > 0) {
				conn.commit();
				status = ApplicationConstants.DELETED;
				flg = "Y";
			} else {
				if (conn != null)
					conn.rollback();
				status = ApplicationConstants.FAIL;
			}

			obj.put("errMsg", status);
			obj.put("flg", flg);
		} catch (Exception e) {
			str = e.getMessage().toString();
			if (str.indexOf("Duplicate entry") != -1)
				obj.put("errMsg", ApplicationConstants.DUPLICATE);
			else
				obj.put("errMsg", ApplicationConstants.EXCEPTION_MESSAGE);
			System.out.println("Exception in DisciplineThrustAreaMappingManager[delete]" + e.getMessage());
			log.fatal(Logging.logException("Error in DisciplineThrustAreaMappingManager[delete]", e.getMessage().toString()));
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return obj;
	}
}
