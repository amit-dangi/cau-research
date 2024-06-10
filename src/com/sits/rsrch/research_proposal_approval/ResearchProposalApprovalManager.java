package com.sits.rsrch.research_proposal_approval;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sits.conn.DBConnection;
import com.sits.general.General;
import com.sits.general.Logging;

public class ResearchProposalApprovalManager {
	static Logger log = Logger.getLogger("exceptionlog");

	public static ArrayList<ResearchProposalApprovalModel> getList(String XFROMDATE, String XTODATE, String status, String pageType, String dept_id,String loct_id,String ddo_id) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();
			
			if(General.checknull(pageType).trim().equals("HD")){
				//This condition add for get the Previous page from 1 submitted proposal that is pending for current pageType
				query = "SELECT ppId, PS_MID, PS_TITTLE_PROJ, date_format(submitted_date, '%d/%m/%Y') as date FROM rsrch_form1_mast "
						+ "where is_form_submittd='Y' and submitted_date is not null and IsApprovalReq='Yes' ";
				if(!General.checknull(XFROMDATE).trim().equals("") || !General.checknull(XFROMDATE).trim().equals("")){
					query+="and submitted_date between str_to_date('"+XFROMDATE+"', '%d/%m/%Y') and str_to_date('"+XTODATE+"', '%d/%m/%Y')";
				}
				if(!General.checknull(loct_id).trim().equals("") || !General.checknull(ddo_id).trim().equals("")){
					query+=" and LOCATION_CODE='"+loct_id+"' and DDO_ID='"+ddo_id+"' ";
				}
				if(!General.checknull(status).trim().equals("")){
					query+=" AND PS_MID IN (select distinct b.PS_MID from rsrch_research_prop_approval b where b.RPA_STATUS='"+status+"' ) ";
				}
				if(General.checknull(status).trim().equals("P")){
				//This condition use for get the current pageType proposal as per selected pending status from dropdown 	
					query+= "UNION SELECT ppId, PS_MID, PS_TITTLE_PROJ, date_format(submitted_date, '%d/%m/%Y') as date FROM rsrch_form1_mast "
							+ "where is_form_submittd='Y' and submitted_date is not null and IsApprovalReq='Yes' ";
					if(!General.checknull(XFROMDATE).trim().equals("") || !General.checknull(XFROMDATE).trim().equals("")){
						query+="and submitted_date between str_to_date('"+XFROMDATE+"', '%d/%m/%Y') and str_to_date('"+XTODATE+"', '%d/%m/%Y')";
					}
					if(!General.checknull(loct_id).trim().equals("") || !General.checknull(ddo_id).trim().equals("")){
						query+=" and LOCATION_CODE='"+loct_id+"' and DDO_ID='"+ddo_id+"' ";
					}
					if(!General.checknull(status).trim().equals("")){
						query+=" AND PS_MID NOT IN (select distinct b.PS_MID from rsrch_research_prop_approval b where b.RPA_STATUS='A' ) ";
					}
					
				}
			}else if(General.checknull(pageType).trim().equals("RP") || General.checknull(pageType).trim().equals("RR") || General.checknull(pageType).trim().equals("VC") || General.checknull(pageType).trim().equals("DDR")){
				String typ="";
				//This condition add for get the current pageType proposal as per selected status from dropdown 
				query = "SELECT a.ppId, a.PS_MID, a.PS_TITTLE_PROJ, date_format(a.submitted_date, '%d/%m/%Y') as date "
						+ "FROM rsrch_form1_mast a, rsrch_research_prop_approval b "
						+ "where a.PS_MID=b.PS_MID and a.is_form_submittd='Y' and a.submitted_date is not null "
						+ "and RPA_TYPE='"+pageType+"' and a.IsApprovalReq='Yes' ";
				
				/*if(General.checknull(pageType).trim().equals("VC")){
					query+=" and IsVC_AppReq='Y' ";
				}*/
				if(!General.checknull(XFROMDATE).trim().equals("") || !General.checknull(XFROMDATE).trim().equals("")){
					query+="and a.submitted_date between str_to_date('"+XFROMDATE+"', '%d/%m/%Y') and str_to_date('"+XTODATE+"', '%d/%m/%Y')";
				}
				if(!General.checknull(loct_id).trim().equals("") || !General.checknull(ddo_id).trim().equals("")){
					query+=" and a.LOCATION_CODE='"+loct_id+"' and a.DDO_ID='"+ddo_id+"' ";
				}
				if(!General.checknull(status).trim().equals("")){
					query+=" and b.RPA_STATUS='"+status+"' ";
				}
				//New condition add for get the Previous pageType approved proposal that is pending for current level 
				if(General.checknull(status).trim().equals("P") || General.checknull(status).trim().equals("")){
				
				if(General.checknull(pageType).trim().equals("RP")){
					typ="HD";
				}
				if(General.checknull(pageType).trim().equals("DDR")){
					typ="RP";
				}
				if(General.checknull(pageType).trim().equals("RR")){
					typ="DDR";
				}
				if(General.checknull(pageType).trim().equals("VC")){
					typ="RR";
				}
				query += "UNION "
						+ "SELECT a.ppId, a.PS_MID, a.PS_TITTLE_PROJ, date_format(a.submitted_date, '%d/%m/%Y') as date "
						+ "FROM rsrch_form1_mast a, rsrch_research_prop_approval b "
						+ "where a.PS_MID=b.PS_MID and a.is_form_submittd='Y' and a.submitted_date is not null "
						+ "and RPA_TYPE='"+typ+"' and a.IsApprovalReq='Yes' and b.RPA_STATUS='A' "
						+ "and b.PS_MID NOT IN (select distinct b.PS_MID from rsrch_research_prop_approval b where RPA_TYPE='"+pageType+"' and b.RPA_STATUS='A') ";
				
				if(General.checknull(pageType).trim().equals("VC")){
					query+=" and IsVC_AppReq='Y' ";
				}
				if(!General.checknull(XFROMDATE).trim().equals("") || !General.checknull(XFROMDATE).trim().equals("")){
					query+="and a.submitted_date between str_to_date('"+XFROMDATE+"', '%d/%m/%Y') and str_to_date('"+XTODATE+"', '%d/%m/%Y')";
				}
				if(!General.checknull(loct_id).trim().equals("") || !General.checknull(ddo_id).trim().equals("")){
					query+=" and a.LOCATION_CODE='"+loct_id+"' and a.DDO_ID='"+ddo_id+"' ";
				}
				}
			}
			
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();
			System.out.println("getList psmt :"+pageType+":"+psmt);
			while (rst.next()) {
				ResearchProposalApprovalModel faModel = new ResearchProposalApprovalModel();
				faModel.setPpId(General.checknull(rst.getString("ppId")));
				faModel.setPsId(General.checknull(rst.getString("PS_MID")));
				faModel.setTitle_pp(General.checknull(rst.getString("PS_TITTLE_PROJ")));
				faModel.setPs_date(General.checknull(rst.getString("date")));				
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			log.fatal(Logging.logException("ResearchProposalApprovalManager[getList]", e.toString()));
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
				log.fatal(Logging.logException("ResearchProposalApprovalManager[getList]", sql.toString()));
			}
		}
		return al;
	}

	public static ArrayList<String> getList1(String XFROMDATE, String XTODATE, String status, String pageType) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList<String> al = new ArrayList<String>();
		String query = "";
		try {
			conn = DBConnection.getConnection();
			query = "select PS_MID from rsrch_research_prop_approval where 1=1 and RPA_TYPE='"+pageType+"' ";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();			
			while (rst.next()) {
				String val = General.checknull(rst.getString("PS_MID"));
				al.add(val);
			}
		} catch (Exception e) {
			System.out.println(e);
			log.fatal(Logging.logException("ResearchProposalApprovalManager[getList]", e.toString()));
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
				log.fatal(Logging.logException("ResearchProposalApprovalManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static ArrayList<ResearchProposalApprovalModel> getList2(String id, String pageType) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {
			conn = DBConnection.getConnection();
			query = "select PS_MID, RPA_STATUS, date_format(RPA_APP_REJ, '%d/%m/%Y') as RPA_APP_REJ, IsVC_AppReq,RPA_REASON "
					+ "from rsrch_research_prop_approval where 1=1 and RPA_TYPE='"+pageType+"' and PS_MID = '"+id+"'";
			psmt = conn.prepareStatement(query);
			rst = psmt.executeQuery();			
			while (rst.next()) {
				ResearchProposalApprovalModel faModel = new ResearchProposalApprovalModel();
				faModel.setPsId(General.checknull(rst.getString("PS_MID")));
				faModel.setStatus(General.checknull(rst.getString("RPA_STATUS")));
				faModel.setAppRejDt(General.checknull(rst.getString("RPA_APP_REJ")));
				faModel.setIsAppReq(General.checknull(rst.getString("IsVC_AppReq")));
				faModel.setReason(General.checknull(rst.getString("RPA_REASON")));
				al.add(faModel);
			}
		} catch (Exception e) {
			System.out.println(e);
			log.fatal(Logging.logException("ResearchProposalApprovalManager[getList]", e.toString()));
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
				log.fatal(Logging.logException("ResearchProposalApprovalManager[getList]", sql.toString()));
			}
		}
		return al;
	}
	
	public static String save(JSONArray consobj, String user_id, String machine, String mode) {
		log = Logger.getLogger("exceptionlog");
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "", msg="0";
		int chk=consobj.size();
		int[] cnt=null;
		try{
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			
			for (int i=0; i<chk; i++) {
				JSONObject obj = (JSONObject) consobj.get(i);
				if(General.checknull(obj.get("previousstatus").toString()).equals("P")){
					 qry = "update rsrch_research_prop_approval set RPA_STATUS=?,RPA_REASON=?,UPDATED_BY=?,UPDATED_MACHINE=?, UPDATED_DATE=NOW() where PS_MID='"+obj.get("psId").toString()+"' and RPA_TYPE='"+General.checknull(mode)+"' and RPA_STATUS='P'; ";
					 	psmt = conn.prepareStatement(qry);
						psmt.setString(1, General.checknull(obj.get("status").toString()));
						psmt.setString(2, General.checknull(obj.get("text").toString()));
						psmt.setString(3, General.checknull(user_id));
						psmt.setString(4, General.checknull(machine));
						
						psmt.addBatch();
						cnt = psmt.executeBatch();
						//System.out.println(psmt); 
				}
				else{
				qry=" INSERT INTO rsrch_research_prop_approval (`PS_MID`, `RPA_STATUS`, `RPA_APP_REJ`, ";
				if(!General.checknull(obj.get("text").toString()).equals("")){
					qry+="`RPA_REASON`, ";
				}
				if(!General.checknull(obj.get("isReq").toString()).equals("")){
					qry+="`IsVC_AppReq`, ";
				}
				qry+= "`RPA_TYPE`, `CREATED_BY`, `CREATED_MACHINE`, `CREATED_DATE`) VALUES (?, ?, DATE(now()),";
				if(!General.checknull(obj.get("text").toString()).equals("")){
					qry+=" '"+General.checknull(obj.get("text").toString())+"', ";
				}
				if(!General.checknull(obj.get("isReq").toString()).equals("")){
					qry+=" '"+General.checknull(obj.get("isReq").toString())+"', ";
				}
				qry+= "?, ?, ?, now())";
				
				psmt = conn.prepareStatement(qry);
				
				psmt.setString(1, General.checknull(obj.get("psId").toString()));
				psmt.setString(2, General.checknull(obj.get("status").toString()));
				psmt.setString(3, General.checknull(mode));
				psmt.setString(4, General.checknull(user_id));
				psmt.setString(5, General.checknull(machine));
				
				psmt.addBatch();
				cnt = psmt.executeBatch();
				//System.out.println(psmt);
				}
			}
			if (cnt.length > 0){
				msg="1";
				conn.commit();	
			}else {
				msg="0";
				conn.rollback();
			}
			
		} catch (Exception e) {
			if(e.getMessage().toString().indexOf("Duplicate entry") != -1)				
				msg="3";
			else 
				msg="2";
			System.out.println("Exception in ResearchProposalApprovalManager[SAVE]" + " " + e.getMessage());
			log.fatal(Logging.logException("ResearchProposalApprovalManager[SAVE]", e.toString()));
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
	
}