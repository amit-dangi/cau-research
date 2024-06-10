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
	<script type="text/javascript" src="../js/research/research-proposal-details-report.js"></script>	
	<script type="text/javascript" src="../js/datepicker/bootstrap-datepicker.js"></script>
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
	});
</script>
<%			
	String OPT_VALUE="", pageName="", fstatus="";	
	fstatus = General.checknull(request.getParameter("fstatus"));
	
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.SEARCH;
	}
%>
<body onload="getEmployee(); ">
<div class="container-fluid">
<div id="" class="page-header"><h4><%=ReadProps.getkeyValue("res_prop_det_rep.header","sitsResource") %></h4> </div>
	<form class="form-horizontal" name="frmRsrchPropReportD" id="frmRsrchPropReportD" action="" method="post" autocomplete="off" >
    	<div class="panel panel-default">
     	<div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
		<div class="panel-body">
			
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
			
			 <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
				           <label class="col-sm-2 col-form-label text-left required-field" for="">Name of the Principal Investigator</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="PiName" name="PiName">
									<option value="">Select Principal Investigator</option>
								</select>
				           </div>
							<label class="col-sm-2 col-form-label text-left required-field" for="">Department</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="deptId" name="deptId">
									<option value="">Select Department</option>
								</select>
			           		</div>
					   	</div>
				   	</div>
				</div>
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
							<label class="col-sm-2 col-form-label text-left required-field" for="">Designation</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="desId" name="desId">
									<option value="">Select Designation</option>
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
			
			<%if(fstatus.trim().equals("N")) { %>
				<div class="col-md-12 text-center m-t-20">
					<button type="button" class="btn btn-view" id="btnPdf">View PDF</button>
					<button type="button" class="btn btn-view" id="btnReset">Reset</button>
					<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
					<%-- <input type= "hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" /> --%>
					
					<input type="hidden" name="fstatus" id="fstatus" value="<%=General.checknull(fstatus) %>">
					<input type="hidden" name="fDate" id="fDate" />
		 			<input type="hidden" name="tDate" id="tDate" />
	 				<input type="hidden" name="empId" id="empId" />
	 				<input type="hidden" name="dept" id="dept" />
	 				<input type="hidden" name="des" id="des" />
				</div>
			<%} %>
		</div>
		</div>
	</form>

<iframe class="embed-responsive-item" name="btmfrmRsrchPropReportD" id="btmfrmRsrchPropReportD" src="" frameborder="0" 
onload="resizeIframe(this)" scrolling="no" width="100%" height=""></iframe>
</div>
</body>
<script type="text/javascript">
      function resizeIframe(iframe) {
	    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
	    window.requestAnimationFrame(() => resizeIframe(iframe));
	}
  </script>
</html>