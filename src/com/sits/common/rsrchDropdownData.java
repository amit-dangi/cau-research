/**
 * @ AUTHOR Amit Dangi
 */
package com.sits.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.sits.common.AesUtil;
import com.sits.conn.DBConnection;
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.rsrch.research_activity.target_achievement_details.TargetAchievementManager;

/**
 * Servlet implementation class rsrchdropdowndata
 */

@WebServlet("/common/rsrchdropdowndata")
public class rsrchDropdownData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger("exceptionlog research activity rsrchdropdowndata");
	AesUtil aesUtil = new AesUtil(128,10);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			processRequest(request, response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().append("Served at rsrchdropdowndata: ").append(request.getContextPath());
		}		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		switch (General.checknull(request.getParameter("fstatus"))){
		case "getRsrchCategory":
			getRsrchCategory(request, response);
			break;
		case "getRsrchSubCategory":
			getRsrchSubCategory(request, response);
			break;
		case "getSubThrustAreaByThrustArea":
			getSubThrustAreaByThrustArea(request, response);
			break;
		case "getThrustAreaByDiscipline":
			getThrustAreaByDiscipline(request, response);
			break;
		case "RSCHPROPOSAL":
			getResearchPropsal(request, response);
			break;
		case "getFundingAgency":
			getFundingAgency(request, response);
			break;
		case "getSubjectByCourse":
			getSubjectByCourse(request, response);
			break;
		case "FSubHead":
			getSubHeadList(request, response);
			break;
		case "FHead":
			getHeadNameList(request, response);
			break;
		case "instalmentlDetails":
			getInsatllmetDate(request, response);
			break;
		default: System.out.println("Invalid grade rsrchdropdowndata");
		}
	}
	
	
	private void getRsrchCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONObject finalResult = new JSONObject();

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";

		/*String param1 = General.checknull(request.getParameter("param")).trim() ;*/
		JSONArray arr = new JSONArray();
		try {
			conn = DBConnection.getConnection();
			qry="select CATEGORY_ID,CATEGORY from rsrch_category_mast order by category ";
			psmt = conn.prepareStatement(qry);
			rst = psmt.executeQuery();
			while (rst.next()) {
				JSONObject obj = new JSONObject();
				obj.put("id", General.checknull(rst.getString("CATEGORY_ID")));
				obj.put("desc", General.checknull(rst.getString("CATEGORY")));
				arr.add(obj);
			}
			finalResult.put("catList", arr);
			//System.out.println("rsrchdropdowndata finalResult||"+finalResult.toString());
	        response.setContentType("application/json");
	        response.setHeader("Cache-Control", "no-store");
	        out.print(finalResult);
	        
		} catch (Exception e) {
			System.out.println("Exception is rsrchdropdowndata [getRsrchCategory] " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rst != null) {
					rst.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void getRsrchSubCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONObject finalResult = new JSONObject();

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";

		String CATEGORY_ID = General.checknull(request.getParameter("CATEGORY_ID"));
		JSONArray arr = new JSONArray();
		try {
			conn = DBConnection.getConnection();
			qry="select SUB_CATEGORY_ID,SUB_CATEGORY from rsrch_sub_category_mast where 1=1 ";
					if(!CATEGORY_ID.equals("")){
						qry+=" and CATEGORY_ID='"+CATEGORY_ID+"' " ; 
	        		 }	
			qry+= "order by SUB_CATEGORY ";
			psmt = conn.prepareStatement(qry);
			rst = psmt.executeQuery();
			while (rst.next()) {
				JSONObject obj = new JSONObject();
				obj.put("id", General.checknull(rst.getString("SUB_CATEGORY_ID")));
				obj.put("desc", General.checknull(rst.getString("SUB_CATEGORY")));
				arr.add(obj);
			}
			finalResult.put("subcatList", arr);
			//System.out.println("rsrchdropdowndata subcat finalResult||"+finalResult.toString());
	        response.setContentType("application/json");
	        response.setHeader("Cache-Control", "no-store");
	        out.print(finalResult);
	        
		} catch (Exception e) {
			System.out.println("Exception is rsrchdropdowndata [getRsrchCategory] " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rst != null) {
					rst.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	private void getSubThrustAreaByThrustArea(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONObject finalResult = new JSONObject();

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";

		String thrust_area_id = General.checknull(request.getParameter("Thrust_Area_Id"));
		JSONArray arr = new JSONArray();
		try {
			conn = DBConnection.getConnection();
			qry="select SUB_THRUST_AREA_ID,SUB_THRUST_AREA from rsrch_sub_thrust_area_mast where 1=1 ";
					//if(!thrust_area_id.equals("")){
						qry+=" and THRUST_AREA_ID='"+thrust_area_id+"' " ; 
	        		 //}	
			qry+= "order by SUB_THRUST_AREA ";
			psmt = conn.prepareStatement(qry);
			rst = psmt.executeQuery();
			while (rst.next()) {
				JSONObject obj = new JSONObject();
				obj.put("id", General.checknull(rst.getString("SUB_THRUST_AREA_ID")));
				obj.put("desc", General.checknull(rst.getString("SUB_THRUST_AREA")));
				arr.add(obj);
			}
			finalResult.put("subThrustAreaList", arr);
	        response.setContentType("application/json");
	        response.setHeader("Cache-Control", "no-store");
	        out.print(finalResult);
	        
		} catch (Exception e) {
			System.out.println("Exception is rsrchdropdowndata [getSubThrustAreaByThrustArea] " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rst != null) {
					rst.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	private void getThrustAreaByDiscipline(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONObject finalResult = new JSONObject();

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";

		String discipline_id = General.checknull(request.getParameter("discipline_id"));
		JSONArray arr = new JSONArray();
		try {
			conn = DBConnection.getConnection();
			qry="select distinct THRUST_AREA_ID,(select THRUST_AREA from rsrch_thrust_area_mast where THRUST_AREA_ID=dm.THRUST_AREA_ID) as THRUST_AREA from discipline_thrust_area_mapping dm where 1=1 ";
					if(!discipline_id.equals("")){
						qry+=" and dm.CR_ID='"+discipline_id+"' " ; 
	        		 }	
			qry+= "order by THRUST_AREA ";
			psmt = conn.prepareStatement(qry);
			rst = psmt.executeQuery();
			while (rst.next()) {
				JSONObject obj = new JSONObject();
				obj.put("id", General.checknull(rst.getString("THRUST_AREA_ID")));
				obj.put("desc", General.checknull(rst.getString("THRUST_AREA")));
				arr.add(obj);
			}
			finalResult.put("thrustAreaList", arr);
			//System.out.println("rsrchdropdowndata subcat finalResult||"+finalResult.toString());
	        response.setContentType("application/json");
	        response.setHeader("Cache-Control", "no-store");
	        out.print(finalResult);
	        
		} catch (Exception e) {
			System.out.println("Exception is rsrchdropdowndata [getThrustAreaByDiscipline] " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rst != null) {
					rst.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public synchronized void getResearchPropsal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String locationCode = General.checknull(request.getParameter("locationCode"));
			String ddoCode = General.checknull(request.getParameter("ddoCode"));
			
			finalResult = getResearchProposal(locationCode,ddoCode,request);
			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: rsrchdropdowndata [getResearchPropsal]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("rsrchdropdowndata [getResearchPropsal]", e.toString()));
		}
	}
	
	public static JSONObject getResearchProposal(String locationCode,String ddoCode,HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
        PreparedStatement pst = null;
        Connection conn=null;
        ResultSet rst = null;
        JSONObject objectJson=new JSONObject();
        String user_id=General.checknull(request.getSession().getAttribute("employee_id").toString());
        String user_status=General.checknull(request.getSession().getAttribute("user_status").toString());
        String projtype = General.checknull(request.getParameter("projtype"));
        String query="select distinct a.PS_MID id, PS_TITTLE_PROJ name from rsrch_form1_mast a, rsrch_research_prop_approval b "
        		+ "where a.PS_MID=b.PS_MID and a.LOCATION_CODE='"+locationCode+"' and a.DDO_ID='"+ddoCode+"' and a.is_form_submittd='Y' and b.RPA_STATUS='A' and b.RPA_STATUS = all "
        		+ "(SELECT RPA_STATUS FROM rsrch_research_prop_approval WHERE RPA_TYPE in ('HD', 'RR', 'RP', 'VC') and RPA_STATUS='A') " ;
		        if(!projtype.equals("")){
					query+=" and a.projtype='"+projtype+"' " ; 
				 }		
		        if(!user_status.equals("A")){
        			query+=" and a.PS_PRINCIPAL='"+user_id+"' " ; 
        		 }
        		query += " union "
        		+ " select distinct a.PS_MID id, PS_TITTLE_PROJ name from rsrch_form1_mast a where IsApprovalReq='No' and a.is_form_submittd='Y'"
        		+ " and a.LOCATION_CODE='"+locationCode+"' and a.DDO_ID='"+ddoCode+"' ";
        		if(!projtype.equals("")){
        			query+=" and a.projtype='"+projtype+"' " ; 
        		 }
        		if(!user_status.equals("A")){
        			query+=" and a.PS_PRINCIPAL='"+user_id+"' " ; 
        		 }
	         try {
	        	 conn = DBConnection.getConnection();
	        	 pst = conn.prepareStatement(query);
	        	 rst = pst.executeQuery();
	        	 while (rst.next()) {
	        		 JSONObject json = new JSONObject();
	        		 json.put("employeeId",  General.checknull(rst.getString("id").toString()));
	        		 json.put("employeeName", General.checknull(rst.getString("name").toString()));
	        		 jsonArray.add(json);
	              }
	              objectJson.put("employee", jsonArray);
	          } catch (Exception e) {
	           System.out.println("FileName=[TargetAchievementManager] MethodName=[getResearchProposal()] :"+ e.getMessage().toString());
	           log.fatal(Logging.logException("FileName=[TargetAchievementManager] MethodName=[getResearchProposal()] :", e.getMessage().toString()));
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
	            	  log.fatal(Logging.logException("FileName=[TargetAchievementManager],MethodName=[getResearchProposal()]", e.getMessage().toString()));
	              }
	          }
	         return objectJson;
	    }
	
	private void getFundingAgency(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONObject finalResult = new JSONObject();

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";

		String projId = General.checknull(request.getParameter("projId"));
		JSONArray arr = new JSONArray();
		try {
			conn = DBConnection.getConnection();
			qry="select fa_id,concat(fa_name,' ~ ',fundedby) as name from rsrch_funding_agency where "
					+ "fa_id=(select fn_agency from rsrch_form1_mast where PS_MID='"+projId+"') order by fa_name ";
			psmt = conn.prepareStatement(qry);
			rst = psmt.executeQuery();
			while (rst.next()) {
				JSONObject obj = new JSONObject();
				obj.put("id", General.checknull(rst.getString("fa_id")));
				obj.put("desc", General.checknull(rst.getString("name")));
				arr.add(obj);
			}
			finalResult.put("List", arr);
	        response.setContentType("application/json");
	        response.setHeader("Cache-Control", "no-store");
	        out.print(finalResult);
	        
		} catch (Exception e) {
			System.out.println("Exception is rsrchdropdowndata [getFundingAgency] " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rst != null) {
					rst.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void getSubjectByCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONObject finalResult = new JSONObject();

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";

		String courseId = General.checknull(request.getParameter("courseId"));
		JSONArray arr = new JSONArray();
		try {
			conn = DBConnection.getConnection();
			qry="select a.SU_ID as ID, subject_name as DESCP from rsrch_subject_disc_mast a,rsrch_subject_disc_detail b "
					+ " where a.SU_ID=b.SU_ID ";
	    	if(!courseId.equals("")){
	    		qry+= " and b.degree_id ='"+courseId+"'";
	    	}
	    	qry+= " order by DESCP ";
    		psmt = conn.prepareStatement(qry);
			//System.out.println("getSubjectByCourse psmt||"+qry);
			rst = psmt.executeQuery();
			while (rst.next()) {
				JSONObject obj = new JSONObject();
				obj.put("id", General.checknull(rst.getString("ID")));
				obj.put("desc", General.checknull(rst.getString("DESCP")));
				arr.add(obj);
			}
			finalResult.put("List", arr);
			System.out.println("rsrchdropdowndata getSubjectByCourse finalResult||"+finalResult.toString());
	        response.setContentType("application/json");
	        response.setHeader("Cache-Control", "no-store");
	        out.print(finalResult);
	        
		} catch (Exception e) {
			System.out.println("Exception is rsrchdropdowndata [getSubjectByCourse] " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rst != null) {
					rst.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void getInsatllmetDate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONObject finalResult = new JSONObject();

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";
		String id=General.checknull(request.getParameter("Id"));
		String fin_yr=General.checknull(request.getParameter("fin_yr"));
		//System.out.println("id  ---"+id);
 		JSONArray arr = new JSONArray();
		try {
			conn = DBConnection.getConnection();
			qry=" select distinct faa_id,date_format(alloted_date,'%d/%m/%Y') as date from rsrch_funding_agency_approval where PS_MID='"+id+"' and fin_yr='"+fin_yr+"' ";
			psmt = conn.prepareStatement(qry);
			rst = psmt.executeQuery();
			while (rst.next()) {
				JSONObject json = new JSONObject();
	        	json.put("id", General.checknull(rst.getString("faa_id")));
				json.put("name", General.checknull(rst.getString("date"))); 
				arr.add(json);
			}
 			finalResult.put("instList", arr);
			System.out.println("rsrchdropdowndata getInsatllmetDate||"+finalResult.toString());
	        response.setContentType("application/json");
	        response.setHeader("Cache-Control", "no-store");
	        out.print(finalResult);
	        
		} catch (Exception e) {
			System.out.println("Exception is rsrchdropdowndata [getHeadNameList] " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rst != null) {
					rst.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}	
	private void getSubHeadList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONObject finalResult = new JSONObject();

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = "";
		String id=General.checknull(request.getParameter("hId"));
		System.out.println("id  ---"+id);
		/*String param1 = General.checknull(request.getParameter("param")).trim() ;*/
		JSONArray arr = new JSONArray();
		try {
			conn = DBConnection.getConnection();
       	 qry=" select sub_head_id,sub_head_name from rsrch_research_sub_head where is_active='Y' and head_id='"+id+"' ";
			psmt = conn.prepareStatement(qry);
			rst = psmt.executeQuery();
			while (rst.next()) {
				JSONObject json = new JSONObject();
	        	json.put("id", General.checknull(rst.getString("sub_head_id")));
				json.put("name", General.checknull(rst.getString("sub_head_name"))); 
				arr.add(json);
			}
 			finalResult.put("sheadlist", arr);
			//System.out.println("rsrchdropdowndata finalResult||"+finalResult.toString());
	        response.setContentType("application/json");
	        response.setHeader("Cache-Control", "no-store");
	        out.print(finalResult);
	        
		} catch (Exception e) {
			System.out.println("Exception is rsrchdropdowndata [getHeadNameList] " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rst != null) {
					rst.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}	
	
	private void getHeadNameList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONObject finalResult = new JSONObject();
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rst = null;
		String qry = ""; 
		JSONArray arr = new JSONArray();
		try {
			conn = DBConnection.getConnection();
			qry="select head_id,head_name from rsrch_research_head where is_active='Y'";
			psmt = conn.prepareStatement(qry);
			rst = psmt.executeQuery();
			while (rst.next()) {
				JSONObject obj = new JSONObject();
				obj.put("id", General.checknull(rst.getString("head_id")));
				obj.put("name", General.checknull(rst.getString("head_name")));
				arr.add(obj);
			}
			finalResult.put("headlist", arr);
 	        response.setContentType("application/json");
	        response.setHeader("Cache-Control", "no-store");
	        out.print(finalResult);
	        
		} catch (Exception e) {
			System.out.println("Exception is rsrchdropdowndata [getHeadNameList] " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rst != null) {
					rst.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}	
}