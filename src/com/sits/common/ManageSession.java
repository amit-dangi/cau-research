package com.sits.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sits.general.General;

/**
 * Servlet implementation class Chkpass
 */
@WebServlet(description = "Add Session Attribute", urlPatterns = { "/ManageSession" })
// @WebServlet("/ManageSession")
public class ManageSession extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManageSession() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = General.check_null(request.getParameter("action"));
		//System.out.println("RSRCH : "+action);
		switch (action) {
		case "1":
			processReguest(request, response);
			break;
		case "2":
			rmvSessData(request, response);
			break;
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doGet");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void processReguest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();

		JSONObject userData = null;
		JSONObject userObj = null;
		JSONObject jsonObj = null;
		try {
//			System.out.println("RSRCH : " + (String) request.getParameter("userData"));
			userData = (JSONObject) new JSONParser().parse((String) request.getParameter("userData"));
			userObj = (JSONObject) userData.get("sUser");
			jsonObj = new JSONObject();

			HttpSession session = request.getSession();
//			System.out.println("RSRCH user_id : " + (String) userObj.get("user_id"));
			session.setAttribute("user_id", (String) userObj.get("user_id"));
			session.setAttribute("user_name", (String) userObj.get("user_name"));
			session.setAttribute("user_status", (String) userObj.get("s_user_status"));
			session.setAttribute("employee_id", (String) userObj.get("employee_id"));
			session.setAttribute("loct_id", "LC0012");
			session.setAttribute("ddo_id", "DDO001");
			session.setAttribute("ddoLocatId", (String) userObj.get("ddoLocatId"));
			session.setAttribute("seCurrentFinancialYearId", (String) userObj.get("seCurrentFinancialYearId"));
			session.setAttribute("seCurrentFinancialYear", (String) userObj.get("seCurrentFinancialYear"));
			session.setAttribute("depratment_id", (String) userObj.get("depratment_id"));
			session.setAttribute("AESUniqueKey", (String)userObj.get("AESUniqueKey"));
			
			//System.out.println("RSRCH depratment_id : " + (String) userObj.get("depratment_id"));
			//session.setAttribute("is_dept_head", (String) userObj.get("is_depratment_head"));
			//change by chetan 25-may-2021
			//System.out.println("department head = "+(String)userObj.get("is_depratment_head"));
			
			if(General.checknull((String)userObj.get("is_depratment_head")).equals("")){				
				session.setAttribute("is_dept_head", "N");
			}else{
				session.setAttribute("is_dept_head", (String)userObj.get("is_depratment_head"));	
			}
			
			jsonObj.put("tablename", "employee_mast");
			jsonObj.put("columndesc", "department");
			jsonObj.put("id", "employeeId");
			jsonObj.put("idvalue", (String) userObj.get("employee_id"));

			/*
			 * jsonObj =
			 * commonAPI.getDropDownByWebService("rest/hrmsApiService/commonApi"
			 * ,jsonObj);
			 * 
			 * session.setAttribute("depratment_id",
			 * General.checknull(jsonObj.get("display_name").toString()));
			 */
			if (session.getAttribute("user_id") != null) {
				jsonObj.put("flag", "Y");
			}
			out.print(jsonObj);
			//System.out.println("department head=" + (String) userObj.get("is_depratment_head"));
		} catch (Exception e) {
			System.out.println("RSRCH[ManageSession] Exception : " + e);
		}
	}

	protected void rmvSessData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject userData = null;
		JSONObject userObj = null;
		try {
			// System.out.println("rmvSessData : ");
			HttpSession session = request.getSession(true);
			session.setAttribute("user_id", "");
			session.setAttribute("user_name", "");
			session.setAttribute("user_status", "");
			session.setAttribute("employee_id", "");
			session.setAttribute("csession_id", "");
			session.setAttribute("loct_id", "");
			session.setAttribute("ddo_id", "");
			session.setAttribute("ddoLocatId", "");
			session.invalidate();

		} catch (Exception e) {
			System.out.println("RSRCH[rmvSessData] Exception : " + e);
		}
	}
}
