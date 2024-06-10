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
    <script type="text/javascript" src="../js/research/funding-agency.js"></script>    	
	<%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
		
		//check testing git
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
	String id = General.checknull(request.getParameter("XfaId"));
	String faName = General.checknull(request.getParameter("XfaName"));
	String faType = General.checknull(request.getParameter("XfaType"));
	String faMobNo = General.checknull(request.getParameter("XfaMobNo"));
	String faUrl = General.checknull(request.getParameter("XfaUrl"));
	String faAddr = General.checknull(request.getParameter("XfaAddr"));
	String faDetail = General.checknull(request.getParameter("XfaDetail"));
	String fundedby = General.checknull(request.getParameter("Xfundedby"));
	
	String OPT_VALUE = "";	
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E")) {
		OPT_VALUE = ApplicationConstants.EDIT;
	}
	
	String selG = "", selS = "", selN = "";
	
	if(!General.checknull(faType).trim().equals("")){
		if(General.checknull(faType).trim().equals("G")){
			selG = "checked";
		}else if(General.checknull(faType).trim().equals("S")){
			selS = "checked";
		}else if(General.checknull(faType).trim().equals("N")){
			selN = "checked";
		}	
	}else{
		selG = "checked";
	}
%>

<body>
<div class="container-fluid">
 <div id="" class="page-header"><h4><%=ReadProps.getkeyValue("fund_agnc.header","sitsResource") %>
   <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
	 </span> 
	 </h4>
 </div>
  <form class="form-horizontal" name="frmFundingAgencyE" id="frmFundingAgencyE" action="" method="post" autocomplete="off" >
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			
             <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="" class="col-sm-2 col-form-label required-field">Funding Agency Name</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="faName" name="faName" placeholder="Enter Funding Agency Name" value="<%=faName%>" maxlength="100">
						</div>
						<label for="" class="col-sm-2 col-form-label required-field">Funding Agency Type</label>
						<div class="col-sm-4">
							<input type="radio" id="govt" name="faType" value="G"  <%=selG %>> Govt. &emsp;&emsp;
							<input type="radio" id="Sgovt" name="faType" value="S" <%=selS %>> Semi Govt. &emsp;&emsp;
							<input type="radio" id="Ngovt" name="faType" value="N" <%=selN %>> Non Govt.
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="" class="col-sm-2 col-form-label required-field">Contact No.</label>
						<div class="col-sm-4">
						<input type="text" class="form-control" id="faMobNo" name="faMobNo" maxlength="11" placeholder="Enter Contact No." value="<%=faMobNo%>" onblur="IsInteger(this);">
						</div>
						<label for="" class="col-sm-2 col-form-label ">Funded by</label>
						<div class="col-sm-4">
						<select class="form-control" id="fundedby" name="fundedby">
				      		<option value="">Select Funded by</option>
				      		<option value='CAU' <%=fundedby.equals("CAU")?"Selected":"" %>>CAU</option>
				      		<option value='ICAR' <%=fundedby.equals("ICAR")?"Selected":"" %>>ICAR</option>
				      		<option value='Others' <%=fundedby.equals("Others")?"Selected":"" %>>Others</option>				      		
				      	</select>
				      	</div>
						</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="" class="col-sm-2 col-form-label">Funding Agency Address</label>
						<div class="col-sm-4">
							 <textarea id="faAddr" name="faAddr" value="" maxlength="255" placeholder="Enter Funding Agency Address" style="width:100%;height:80px;"><%=faAddr%></textarea> 
						</div>
						<label for="" class="col-sm-2 col-form-label">Funding Agency Details (Ex: Project Coordinator Name)</label>
						<div class="col-sm-4">
						  <textarea id="faDetail" name="faDetail" value="" maxlength="255" placeholder="Enter Details" style="width:100%;height:80px;"><%=faDetail%></textarea> 
						</div>
					</div>
					<div class="row">
					<label for="" class="col-sm-2 col-form-label">Website URL</label>
						<div class="col-sm-4">
						<input type="text" class="form-control" maxlength="100" id="faUrl" name="faUrl" placeholder="Enter Website URL" onblur="validURL(this);" value="<%=faUrl%>">
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
   
   <form class="form-horizontal" name="frmFundingAgencyD" id="frmFundingAgencyD" action="" method="post" autocomplete="off" >
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
						<label for="" class="col-sm-2 col-form-label">Funding Agency Name</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="faNameS" name="faNameS" placeholder="Enter Funding Agency Name">
						</div>
						<label for="" class="col-sm-2 col-form-label">Funding Agency Type</label>
						<div class="col-sm-4">
						<select class="form-control" id="faTypeS" name="faTypeS">
				      		<option value="">Select Funding Agency Type</option>
				      		<option value='G'>Govt.</option>
				      		<option value='S'>Semi Govt.</option>
				      		<option value='N'>Non Govt.</option>				      		
				      	</select>
					  </div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="" class="col-sm-2 col-form-label">Contact No.</label>
						<div class="col-sm-4">
						<input type="text" class="form-control" id="faMobNoS" name="faMobNoS" placeholder="Enter Contact No.">
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
	<%if (!fstatus.trim().equals("E")) { %>
<iframe class="embed-responsive-item" src="funding_agency_l.jsp" name="btmfrmFundingAgencyD" id="btmfrmFundingAgencyD" 
onload="resizeIframe(this)" src="" frameborder="0" scrolling="no" width="100%" height=""></iframe>
<%} %>
</div>
</body>
<script type="text/javascript">
      function resizeIframe(iframe) {
	    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
	    window.requestAnimationFrame(() => resizeIframe(iframe));
  }
 </script>
</html>