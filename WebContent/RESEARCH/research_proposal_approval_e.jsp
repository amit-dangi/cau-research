<%@page import="com.sits.general.ApplicationConstants"%>
<%@include file="../myCks.jsp"%>
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
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
	<script type="text/javascript" src="../js/research/research-proposal-approval.js"></script>	
	<script type="text/javascript" src="../js/datepicker/bootstrap-datepicker.js"></script>
	  <script type="text/javascript" src="../js/commonDropDown.js"></script>
	<%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
	%>
	<link rel="stylesheet" href="../css/datepicker/datepicker3.css"  type="text/css">
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
  	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
</head>
 <script>
 var user_status='<%=user_status%>';
	$(document).ready(function() {
		$("#XFROMDATE").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#XFROMDATE").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		
		$("#XTODATE").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#XTODATE").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#XFROMDATES").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#XFROMDATES").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		
		$("#XTODATES").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#XTODATES").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
	});
</script>
<%	
	String pageType = General.checknull(request.getParameter("pageType"));
	String OPT_VALUE = "", pageName="", dis="" , locationCode=""  , ddoCode="" ,disabled="";

	String fstatus = General.checknull(request.getParameter("fstatus")); 
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.SEARCH;
	}else if (fstatus.trim().equals("E")) {
		OPT_VALUE = ApplicationConstants.EDIT;
	}
	
	if(pageType.trim().equals("HD")){
		pageName="CRAC";
	}else if(pageType.trim().equals("RP")){
		pageName="Dean";
	}else if(pageType.trim().equals("RR")){
		pageName="Director of Research(DOR)";
	}else if(pageType.trim().equals("VC")){
		pageName="VC";
	}else if(pageType.trim().equals("DDR")){
		pageName="Deputy Director(DDR)";
	}
	else{
		pageName="";
	}
	if(user_status.equals("A")){
		dis="";
		disabled="";
	}else{
		//dis="disabled";
		dis="";
		disabled="disabled";
	}
	 locationCode= locationCode.equals("")?loct_id:locationCode;
	 ddoCode= ddoCode.equals("")?ddo_id:ddoCode;
	// System.out.println("locationCode-"+locationCode+" ddoCode="+ddoCode+" user_status="+user_status);
%>
<body onload="getDdoDetailbyLocation('<%=locationCode%>','<%=ddoCode %>');  getLocationDetail('<%=locationCode%>');">

<div class="container-fluid">
 <%-- <div id="" class="page-header"><h4 style="position: absolute;">Research Proposal Approval <%=pageName %></h4>
  <span id="" class="text-right ">
	 <h4><a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i></a>
	 </h4>
	 </span> 
 </div> --%>
    <div id="" class="page-header"><h4><%=ReadProps.getkeyValue("res_prop_app.header","sitsResource") %> <%=pageName %></h4> </div>
    <form class="form-horizontal" name="frmRsrchPropAppE" id="frmRsrchPropAppE" action="" method="post" autocomplete="off" >
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
	   <div class="panel-body">
		<div class="form-group">
		<div class="col-md-12">
				<div class="row">
					<label for="location" class="col-sm-2 col-form-label <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%=dis %><%=disabled %> onchange="getDdoDetailbyLocation(this.value,'');">
									<option value="">Select Location</option>
								</select>
				           </div>
				           <label for="ddo" class="col-sm-2 col-form-label <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo" <%=dis %> <%=disabled %>>
      						</select>      				
      				</div>  
				</div>
				
			</div>
		</div>
		
		<div class="form-group">
			<div class="col-md-12">
				<div class="row">
					<label class="col-sm-2 col-form-label" for="">From Date</label>
					<div class="col-sm-4" >
						<div class="input-group date" id="msg-XODATE">
						<div class="input-group-addon"><i class="fa fa-calendar"></i></div>
							<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XFROMDATE" name="XFROMDATE" placeholder="DD/MM/YYYY" value="">
					 	 </div>
					</div>
					<label class="col-sm-2 col-form-label" for="">To Date</label>
					<div class="col-sm-4" >
						<div class="input-group date" id="msg-XTODATE">
						<div class="input-group-addon"><i class="fa fa-calendar"></i></div>
							<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XTODATE" name="XTODATE" placeholder="DD/MM/YYYY" value="">
					 	 </div>
					</div>
					
				</div>
			</div>
	   </div>
		   <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for	="" class="col-sm-2 col-form-label">Status</label>
						<div class="col-sm-4">
							<select class="form-control" id="status" name="status">
								<option value="">All</option>
								<option value="P">Pending</option>
								<option value="A">Approved</option>
								<option value="R">Reject</option>
							</select>
						</div>
					</div>
				</div>
			</div>
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
					<button type="button" class="btn btn-view" id="btnView">Search</button>
				<%} %>
					<button type="button" class="btn btn-view" id="btnReset">Reset</button> 
					<input type="hidden" name="typ" id="typ" value="<%=pageType %>">					
					<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
					<%-- <input type= "hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" /> --%>
				</div>
			</div>
			</div>
   </form>
	
	<!-- <div class="modal fade recomond-btn" id="reportDiologHD" tabindex="-1" role="dialog" aria-labelledby="open-Modal" aria-hidden="true" style="margin-top: -27px;">
		<div class="modal-dialog" style="width:85%;">
			<div class="modal-content">
				<div class="modal-header text-center">
					<span class="close" data-dismiss="modal" aria-label="Close close-cross" aria-hidden="true" style="margin-top: 0px !important;">&times;</span>
					<h4 class="modal-title">Preview File</h4>
				</div>
				
				<div class="modal-body" style="height: 400px"> Start Modal Body
					  <form name="pdfFormHD" id="pdfFormHD" method="get" autocomplete="off" style="padding: 0px !important;">
						<iframe class="embed-responsive-item" name="1_ReportHD" id="1_ReportHD" width="100%;" height="390px" src="" frameborder="0" scrolling="yes"></iframe>							
					</form>
				</div>
				End Modal Body
				
				<div class="modal-footer">
					<div class="row">
						<div class="col-md-12 text-center">
							<button type="button" class="btn btn-warning" data-dismiss="modal">Back</button>
						</div>
					</div>	
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade recomond-btn" id="reportDiologRP" tabindex="-1" role="dialog" aria-labelledby="open-Modal" aria-hidden="true" style="margin-top: -27px;">
		<div class="modal-dialog" style="width:85%;">
			<div class="modal-content">
				<div class="modal-header text-center">
					<span class="close" data-dismiss="modal" aria-label="Close close-cross" aria-hidden="true" style="margin-top: 0px !important;">&times;</span>
					<h4 class="modal-title">Preview File</h4>
				</div>
				
				<div class="modal-body" style="height: 400px"> Start Modal Body
					  <form name="pdfFormRP" id="pdfFormRP" method="get" autocomplete="off" style="padding: 0px !important;">
						<iframe class="embed-responsive-item" name="1_ReportRP" id="1_ReportRP" width="100%;" height="390px" src="" frameborder="0" scrolling="yes"></iframe>							
					</form>
				</div>
				End Modal Body
				
				<div class="modal-footer">
					<div class="row">
						<div class="col-md-12 text-center">
							<button type="button" class="btn btn-warning" data-dismiss="modal">Back</button>
						</div>
					</div>	
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade recomond-btn" id="reportDiologRR" tabindex="-1" role="dialog" aria-labelledby="open-Modal" aria-hidden="true" style="margin-top: -27px;">
		<div class="modal-dialog" style="width:85%;">
			<div class="modal-content">
				<div class="modal-header text-center">
					<span class="close" data-dismiss="modal" aria-label="Close close-cross" aria-hidden="true" style="margin-top: 0px !important;">&times;</span>
					<h4 class="modal-title">Preview File</h4>
				</div>
				
				<div class="modal-body" style="height: 400px">
					  <form name="pdfFormRR" id="pdfFormRR" method="get" autocomplete="off" style="padding: 0px !important;">
						<iframe class="embed-responsive-item" name="1_ReportRR" id="1_ReportRR" width="100%;" height="390px" src="" frameborder="0" scrolling="yes"></iframe>							
					</form>
				</div>
				
				<div class="modal-footer">
					<div class="row">
						<div class="col-md-12 text-center">
							<button type="button" class="btn btn-warning" data-dismiss="modal">Back</button>
						</div>
					</div>	
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade recomond-btn" id="reportDiologVC" tabindex="-1" role="dialog" aria-labelledby="open-Modal" aria-hidden="true" style="margin-top: -27px;">
		<div class="modal-dialog" style="width:85%;">
			<div class="modal-content">
				<div class="modal-header text-center">
					<span class="close" data-dismiss="modal" aria-label="Close close-cross" aria-hidden="true" style="margin-top: 0px !important;">&times;</span>
					<h4 class="modal-title">Preview File</h4>
				</div>
				
				<div class="modal-body" style="height: 400px">
					  <form name="pdfFormVC" id="pdfFormVC" method="get" autocomplete="off" style="padding: 0px !important;">
						<iframe class="embed-responsive-item" name="1_ReportVC" id="1_ReportVC" width="100%;" height="390px" src="" frameborder="0" scrolling="yes"></iframe>							
					</form>
				</div>
				
				<div class="modal-footer">
					<div class="row">
						<div class="col-md-12 text-center">
							<button type="button" class="btn btn-warning" data-dismiss="modal">Back</button>
						</div>
					</div>	
				</div>
			</div>
		</div>
	</div> -->
	
   <%-- <form class="form-horizontal" name="frmRsrchPropAppD" id="frmRsrchPropAppD" action="" method="post" autocomplete="off" >
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
					<label class="col-sm-2 col-form-label" for="">From Date</label>
					<div class="col-sm-4" >
						<div class="input-group date" id="msg-XODATE">
						<div class="input-group-addon"><i class="fa fa-calendar"></i></div>
							<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XFROMDATES" name="XFROMDATES" placeholder="DD/MM/YYYY" value="">
					 	 </div>
					</div>
					<label class="col-sm-2 col-form-label" for="">To Date</label>
					<div class="col-sm-4" >
						<div class="input-group date" id="msg-XTODATE">
						<div class="input-group-addon"><i class="fa fa-calendar"></i></div>
							<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XTODATES" name="XTODATES" placeholder="DD/MM/YYYY" value="">
					 	 </div>
					</div>
					
				</div>
			</div>
	   </div>
		   <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for	="" class="col-sm-2 col-form-label">Status</label>
						<div class="col-sm-4">
							<select class="form-control" id="statusS" name="statusS">
								<option value="">Select Status</option>
								<option value="P">Pending</option>
								<option value="A">Approved</option>
								<option value="R">Reject</option>
							</select>
						</div>
					</div>
				</div>
			</div>
				 
			    <div class="col-md-12 text-center m-t-20">
					<button type="button" class="btn btn-view" id="btnSearch" >Search</button>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button> 

				</div>				
			<!-- -------------------------------End Here (Write content inside this modal)------------------------------------------- -->
			</div> <!-- End of panal body -->
			</div> <!-- End of panal default -->
		    </div> <!-- End of modal body -->
				 
			</div> <!-- End of modal content -->
		</div> <!-- End of modal-dialog -->
	</div> <!-- End of modal class/id at which modal will be open -->
	</form> --%>
<iframe class="embed-responsive-item" src="research_proposal_approval_l.jsp?fstatus=V&pageType=<%=pageType %>"
 onload="resizeIframe(this)" name="btmfrmRsrchPropAppD" id="btmfrmRsrchPropAppD" src="" frameborder="0" scrolling="no" width="100%" height=""></iframe>
</div>
<script type="text/javascript">
      function resizeIframe(iframe) {
	    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
	    window.requestAnimationFrame(() => resizeIframe(iframe));
  }
 </script>
</body>
</html>