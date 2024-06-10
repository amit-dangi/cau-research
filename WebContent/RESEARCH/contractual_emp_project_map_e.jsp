<%@include file="../myCks.jsp"%>
<%@page import="com.sits.general.ApplicationConstants"%>
<%@page import="com.sits.general.QueryUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Expires" content="-1" />
 	
 	<link rel="stylesheet" href="../assets/sits/bootstrap/3.3.7/css/bootstrap.min.css"  type="text/css">
	<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
	<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/aes/AesUtil.js"></script>
	<script type="text/javascript" src="../js/aes/crypto.js"></script>
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
    <script type="text/javascript" src="../js/research/contractual-emp-project-map.js"></script>  
    <script type="text/javascript" src="../js/commonDropDown.js"></script>  	
	<%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
	%>
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
  	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
</head>
<%			
	String fstatus = General.checknull(request.getParameter("fstatus"));
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}	
	String id="", pId="", eId="", dis="" , locationCode=""  , ddoCode="" ,disabled="",nType="";
	
	String OPT_VALUE = "";	
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E")) {
		dis="disabled";
		OPT_VALUE = ApplicationConstants.EDIT;
		id=General.checknull(request.getParameter("id"));
		pId=General.checknull(request.getParameter("pId"));
		eId=General.checknull(request.getParameter("eId"));
		nType=General.checknull(request.getParameter("nType"));
		locationCode=General.checknull(request.getParameter("locationCode"));
		ddoCode=General.checknull(request.getParameter("ddoCode"));
	}else{
		OPT_VALUE="";
	}
	if(user_status.equals("A")){
		//dis="";
		disabled="";
	}else{
		//dis="disabled";
		dis="";
		disabled="disabled";
	}
	if(!user_status.equals("A")){
	 locationCode= locationCode.equals("")?loct_id:locationCode;
	 ddoCode= ddoCode.equals("")?ddo_id:ddoCode;
	}
%>
<body onload="getDdoDetailbyLocation('<%=locationCode%>','<%=ddoCode %>','Xddo');  getLocationDetail('<%=locationCode%>','Xlocation'); getEmployeeByLocDDo('<%=eId %>','<%=locationCode%>','<%=ddoCode %>','<%=nType%>'); 
 			getNatureDetail('<%=nType%>'); getDdoDetailbyLocation('<%=locationCode%>','<%=ddoCode %>','Sddo');  getLocationDetail('<%=locationCode%>','Slocation'); getResearchProposal('<%=locationCode%>','<%=ddoCode %>','<%=pId%>','proj');">
<div class="container-fluid">
 <div id="" class="page-header"><h4><%=ReadProps.getkeyValue("emp_proj_map.header","sitsResource") %>
  <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
  </span>
 </h4>
 </div>
 <form class="form-horizontal" name="frmContEmpProjMapE" id="frmContEmpProjMapE" action="" method="post" autocomplete="off" >
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			<div class="form-group">
		<div class="col-md-12">
				<div class="row">
					<label for="location" class="col-sm-2 col-form-label <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%=dis %><%=disabled %> onchange="getDdoDetailbyLocation(this.value,'<%=ddoCode %>','Xddo');">
									<option value="">Select Location</option>
								</select>
				           </div>
				           <label for="ddo" class="col-sm-2 col-form-label <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo" <%=dis %> <%=disabled %>  onchange="getProjectDetailbyLocationmain('<%=pId%>','<%=locationCode%>','<%=ddoCode %>','proj');">
      						</select>      				
      				</div>  
				</div>
				
			</div>
		</div>
             <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left required-field" for="">Project</label>
							<div class="col-sm-4">
								
								<%-- <select class="form-control" id="proj" name="proj" <%=dis %> onchange="getDropDown(this.value);"> --%> 
								<select class="form-control resPrps" id="proj" name="proj" <%if(fstatus.trim().equals("E") ) { %> disabled <%} %> >
									<option value="">Select Project</option>
								<%-- 	<%=QueryUtil.getComboOption("rsrch_form1_mast", "PS_MID", "PS_TITTLE_PROJ", ""+pId+"", "is_form_submittd='Y' and LOCATION_CODE='"+locationCode+"' and DDO_ID='"+ddoCode+"'", "PS_TITTLE_PROJ") %>
								 --%></select>
				           </div>
				           
				            <label for="natureType" class="col-sm-2 col-form-label text-left required-field">Nature Type</label>
      			  	   <div class="col-sm-4"><select class="form-control" id="XnatureType" name="XnatureType" <%=fstatus.equals("E")?"disabled":""%> onchange="getEmployeeByLocDDo('<%=eId %>','<%=locationCode%>','<%=ddoCode %>',this.value);">
	      						<option value="">Select Nature Type</option>
	      						
	      						<%-- <%=QueryUtil.getComboOption("nature_mast", "NATURE_ID", "NATURE", "" , "1=1", "NATURE") %>
	      						 --%></select>
      				</div>
      				 		 		
						
				         						
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<div class="row">
			<label class="col-sm-2 col-form-label text-left required-field" for="">Employee</label>
							<div class="col-sm-4">
								<!-- <select class="form-control emp" id="CntEmp" name="CntEmp">
									<option value="">Select Employee</option>
								</select> -->
								<div class="emp">
									<ul class="form-control" style="height: 85px; padding-top:0px;overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;">
										<li>List Of Employee(s)</li>								
									</ul>
								</div>
				           </div>
				           </div>
				           </div>
				           </div>
			
			<%if (fstatus.trim().equals("K")) { %>
			<div class="col-md-12 text-center m-t-20">
				<button type="button" class="btn btn-view" id="addMore">Add</button>
			</div>
			
			<div class="page-header-1">
				<div class="col-sm-4 repTitle h5 ">List of Mapping(s)</div>
			</div>
			
			<div id="dydiv" style="width:100%;overflow: auto;">
      			<div id="" style="padding:8px 0px;display:flex;width:100%;">
					<table border="1" cellspacing="1" cellpadding="1" width="100%" class="tableGrid" >
						<tr>
							<th width="10%" style="text-align:center;">S.No.</th>
							<th width="30%" style="text-align:center;">Project</th>
						  	<th width="30%" style="text-align:center;">Employee</th>
						  	<th width="20%" style="text-align:center;">Delete</th>
						</tr>
						<tbody id="searchTable" ></tbody>
					</table>					
				</div>
			</div>		
			<%} %>
			
		 		<div class="col-sm-12">
					<div class="row text-center">
						<div class="errmessage" id="errMsg"></div>
					</div>
				</div>
				<div class="col-sm-12">
					<div class="row text-center">
						<div class="errmessage1" id="errMsg1"></div>
					</div>
				</div>
                <div class="col-sm-12">
				        <div class="row text-center">
				            <div class="errmessage2" id="errMsg2"></div>
				        </div>
		            </div>
				<div class="col-md-12 text-center m-t-20">
				<%if(fstatus.trim().equals("N")) { %>
					<button type="button" class="btn btn-view" id="btnSave">Save</button>
				<%}else if(fstatus.trim().equals("E")) {%>
					<button type="button" class="btn btn-view" id="btnUpdate">Update</button>
					<button type="button" class="btn btn-view" id="btnBack">Back</button>
				<%} %>
					<button type="button" class="btn btn-view" id="btnReset">Reset</button> 
					<input type="hidden" name="id" id="id" value="<%=id%>">
					<input type="hidden" name="propsalId" id="propsalId" value="<%=pId%>">
					<input type="hidden" name=index id="index" value="0">
					<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">					
					<input type= "hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
				</div>
			</div>
		</div>
   </form>
   
   <form class="form-horizontal" name="frmContEmpProjMapD" id="frmContEmpProjMapD" action="" method="post" autocomplete="off" >
    <div class="modal fade recomond-btn" id="myModal" tabindex="-1" role="dialog" aria-labelledby="openModal" aria-hidden="true" style="display: none;">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button></div>
			  <div class="modal-body">
			   <div class="panel panel-default">
                <div class="panel-heading"><h3 class="panel-title text-right"><%=ApplicationConstants.SEARCH %></h3></div>
     			<div class="panel-body">
 
				<!-- -------------------------------Write content inside this modal------------------------------------------- -->
				 <div class="form-group">
		<div class="col-md-12">
				<div class="row">
					<label for="location" class="col-sm-2 col-form-label">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Slocation" name="Slocation" <%=dis %><%=disabled %> onchange="getDdoDetailbyLocation(this.value,'<%=ddoCode %>','Sddo');">
									<option value="">Select Location</option>
								</select>
				           </div>
				           <label for="ddo" class="col-sm-2 col-form-label">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Sddo" name="Sddo" <%=dis %> <%=disabled %>  onchange="getProjectDetailbyLocationmain('<%=pId%>','<%=locationCode%>','<%=ddoCode %>','projS');">
      						</select>      				
      				</div>  
				</div>
				
			</div>
		</div>
				 <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left" for="">Project</label>
							<div class="col-sm-4 ">
								<select class="form-control resPrps" id="projS" name="projS">
									<option value="">Select Project</option>
									<%-- <%=QueryUtil.getComboOption("rsrch_form1_mast a, rsrch_research_prop_approval b", "a.PS_MID", "a.PS_TITTLE_PROJ", "", " a.PS_MID=b.PS_MID and a.is_form_submittd='Y' and a.submitted_date is not null and b.RPA_STATUS='A' and RPA_TYPE='RR' and IsVC_AppReq='Y' ", "a.PS_TITTLE_PROJ") %> --%>
								</select>
				           </div>					
						<!-- <label class="col-sm-2 col-form-label text-left" for="">Employee</label>
							<div class="col-sm-4 ">
								<select class="form-control emp" id="CntEmpS" name="CntEmpS">
									<option value="">Select Employee</option>
								</select>
				           </div> -->					
					</div>
				</div>
			</div>
							 
			    <div class="col-md-12 text-center m-t-20">
					<button type="button" class="btn btn-view" id="btnSearch" data-dismiss="modal" onclick=" ">Search</button>
					<button type="button" class="btn btn-view" id="btnReset1" >Reset</button> 
				</div>				
				
			<!-- -------------------------------End Here (Write content inside this modal)------------------------------------------- -->
			</div> <!-- End of panal body -->
			</div> <!-- End of panal default -->
		   </div> <!-- End of modal body -->
				 
			</div> <!-- End of modal content -->
		</div> <!-- End of modal-dialog -->
	</div> <!-- End of modal class/id at which modal will be open -->
	</form>
<iframe class="embed-responsive-item" src="contractual_emp_project_map_l.jsp" name="btmfrmContEmpProjMapD" 
	onload="resizeIframe(this)" id="btmfrmContEmpProjMapD" src="" frameborder="0" scrolling="no" width="100%" height=""></iframe>
</div>
</body>
<script type="text/javascript">
      function resizeIframe(iframe) {
	    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
	    window.requestAnimationFrame(() => resizeIframe(iframe));
	}
  </script>
</html>
 