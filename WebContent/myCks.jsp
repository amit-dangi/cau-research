<%@page import="com.sits.general.General"%>
<%@page import="com.sits.general.ReadProps"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader ("Expires", -1);
	response.addHeader("Cache-Control", "post-check=0, pre-check=0");
	String user_id="", user_name="", user_status="", user_style="", user_dashboard="", soft_ver="", 
			menuRights="", session_id="", dept_id="", is_dept_head="",csession_id="", sess_emp_id="", 
			ip="", login_id="", puser_id="", curr_academic_year="", suser_type="", loct_id="", 
			ddo_id="", ddoLocatId="", base_url="", currentFinancialYear="", seCurrentFinancialYearId="";
	try {
		ddo_id			= General.checknull((String)session.getAttribute("ddo_id"));
		loct_id			= General.checknull((String)session.getAttribute("loct_id"));
		ddoLocatId		= General.checknull((String)session.getAttribute("ddoLocatId"));		
		user_id			= General.checknull((String)session.getAttribute("user_id"));
		puser_id		= General.checknull((String)session.getAttribute("puser_id"));
		login_id		= General.checknull((String)session.getAttribute("login_id"));
		user_name		= General.checknull((String)session.getAttribute("user_name"));
		sess_emp_id		= General.checknull((String)session.getAttribute("employee_id"));
		user_status		= General.checknull((String)session.getAttribute("user_status"));
		user_style		= General.checknull((String)session.getAttribute("user_style"));
		user_dashboard	= General.checknull((String)session.getAttribute("dashboard"));
		soft_ver		= General.checknull((String)session.getAttribute("soft_ver"));
		menuRights		= General.checknull((String)session.getAttribute("menuRights"));
		session_id		= General.checknull((String)session.getAttribute("session_id"));
		csession_id 	= General.checknull((String)session.getId());
		ip 				= General.checknull((String)session.getAttribute("ip"));
		dept_id 		= General.checknull((String)session.getAttribute("depratment_id")).trim();
		is_dept_head 	= General.checknull((String)session.getAttribute("is_dept_head")).trim();
		currentFinancialYear = General.checknull((String)session.getAttribute("seCurrentFinancialYear")).trim();
		seCurrentFinancialYearId=General.checknull((String)session.getAttribute("seCurrentFinancialYearId")).trim();
		curr_academic_year = General.checknull((String)session.getAttribute("curr_academic_year")).trim();
		suser_type		= General.checknull((String)session.getAttribute("user_type"));
		base_url	 	= ReadProps.getkeyValue("root.url", "sitsResource")+"/SessionExp.jsp?x=y";
		//session.setAttribute("AESUniqueKey", "CHETAN");
		System.out.println(": RSRCH :"+ "user_id :"+user_id+" || user_name :"+user_name+" || user_status :"+user_status+" || user_style :"+user_style
				+" || user_dashboard :"+user_dashboard+" || soft_ver :"+soft_ver+" || menuRights :"+menuRights+" || session_id :"+session_id
				+" || dept_id :"+dept_id+" || is_dept_head :"+is_dept_head+" || csession_id :"+csession_id+" || sess_emp_id :"+sess_emp_id
				+" || ip :"+ip+" || login_id :"+login_id+" || puser_id :"+puser_id+" || curr_academic_year :"+curr_academic_year+" || suser_type :"+suser_type);
		
		/* session.setAttribute("user_id", "JNAFAU");
		session.setAttribute("s_user_id", "CHETAN");
		session.setAttribute("ddo_id", "DDO001");
		session.setAttribute("loct_id", "LC0003");
		session.setAttribute("user_status", "A");
		session.setAttribute("ddoLocatId", "A");
		session.setAttribute("csession_id", "2019");
		session.setAttribute("employee_id", "0001");
		session.setAttribute("seCurrentFinancialYearId", "2019");
		session.setAttribute("seCurrentFinancialYear", "01/04/2019~31/03/2020"); 
		session.setAttribute("s_ip", "12345");*/
		
		if((user_id.trim().equals("")) || (user_status.trim().equals("")) /* ||- (!session_id.equals(csession_id)) */) {
			response.sendRedirect(base_url);
	 	}%>
<%
	} catch (Exception e) {
		System.out.println("Error in myCks.jsp : "+e);
%>
	<jsp:forward page="SessionExp.jsp" >
		<jsp:param name="x" value="y" />
	</jsp:forward>
<%	}
%>