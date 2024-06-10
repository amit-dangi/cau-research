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
	<script type="text/javascript" src="../js/aes/AesUtil.js"></script>
	<script type="text/javascript" src="../js/aes/crypto.js"></script>
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
    <script type="text/javascript" src="../js/patent/thrust-area.js"></script>    	
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
	String sel= "", fstatus="", OPT_VALUE = "";
	fstatus = General.checknull(request.getParameter("fstatus"));
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	
	String id = General.checknull(request.getParameter("id"));
	String thrust_area = General.checknull(request.getParameter("thrust_area"));
	
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E")) {
		OPT_VALUE = ApplicationConstants.EDIT;
	}
%>

<body>
<div class="container-fluid">
 <div id="" class="page-header"><h4 style="position: absolute;"><%=ReadProps.getkeyValue("thrust_area.header","sitsResource") %></h4>
  <span id="" class="text-right ">
	 <h4><a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"><span>Advance Search</span>
	 	<i class="fa fa-search"></i></a>
	 </h4>
	 </span> 
 </div>
    
    <form class="form-horizontal" name="frmThrustAreaE" id="frmThrustAreaE" action="" method="post" autocomplete="off" >
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			
             <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="" class="col-sm-2 col-form-label required-field">Thrust Area</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" maxlength="2555" id="thrust_area" name="thrust_area" placeholder="Enter Head Name" value="<%=thrust_area%>">
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
					<button type="button" class="btn btn-view" id="btnSave">Save</button>
				<%}else if(fstatus.trim().equals("E")) {%>
					<button type="button" class="btn btn-view" id="btnUpdate">Update</button>
					<button type="button" class="btn btn-view" id="btnBack">Back</button>
				<%} %>
					<button type="button" class="btn btn-view" id="btnReset">Reset</button> 
					<input type="hidden" name="id" id="id" value="<%=id%>">
					<input type= "hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
				</div>
			</div>
		</div>
   </form>
   
   <form class="form-horizontal" name="frmThrustAreaD" id="frmThrustAreaD" action="" method="post" autocomplete="off" >
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
						<label for="" class="col-sm-2 col-form-label">Thrust Area</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="thrust_areaS" name="thrust_areaS" placeholder="Enter Head Name" value="">
						</div>
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
<iframe class="embed-responsive-item" src="thrust_area_l.jsp" name="frmThrustAreaD" id="frmThrustAreaD" 
onload="resizeIframe(this)" src="" frameborder="0" scrolling="no" width="100%" height=""></iframe>
</div>

</body>
<script type="text/javascript">
      function resizeIframe(iframe) {
	    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
	    window.requestAnimationFrame(() => resizeIframe(iframe));
	}
  </script>
</html>