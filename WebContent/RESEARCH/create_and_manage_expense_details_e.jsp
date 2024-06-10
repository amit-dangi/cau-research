<%@page import="java.util.ArrayList"%>
<%@page import="com.sits.general.QueryUtil"%>
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
    <script type="text/javascript" src="../js/research/create-and-manage-expense-details.js"></script>
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
$(document).ready(function() {	
	$("#EXPDATE").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
		"setDate": new Date(),
	}).on('click', function() {
		$("#EXPDATE").datepicker("setDate", '');
	}).on('clearDate', function(selected) {
	});
});
function responseBack(){
	 revertBack();
	}
function responseBack1(){
	 revertBack1();
	}

</script>
<%	
	String sel= "", fstatus="", OPT_VALUE = "", faid="", piid="";
	String Faid="", FinYr="", fAgency="", ResPrps="", Date="", Remark="",locationCode=""  , ddoCode="";
	fstatus = General.checknull(request.getParameter("fstatus"));
	user_status		= General.checknull((String)session.getAttribute("user_status"));
	
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E")) {
		OPT_VALUE = ApplicationConstants.EDIT;
	}
	
%>

<body onload="
getFinanceYrDropdwn('<%=session.getAttribute("seCurrentFinancialYearId")%>','finYrId');getResearchProposal('<%=loct_id%>','<%=ddo_id%>'); getLocationDetail('<%=loct_id%>'); getDdoDetailbyLocation('<%=loct_id%>','<%=ddo_id%>');">
<div class="container-fluid">
<div id="" class="page-header"><h4><%=ReadProps.getkeyValue("res_create_mang_exp_head.header","sitsResource") %></h4>
  </div>
    <form class="form-horizontal" name="frmCreateManageExpE" id="frmCreateManageExpE" action="" method="post" autocomplete="off" target="">
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  				
      					<label for="location" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%if(user_status.equals("U")){ %> disabled <%} %> onchange="getDdoDetailbyLocation1(this.value);">
									<option value="">Select Location</option>
								</select>
				           </div>
				           
				           <label for="ddo" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo" <%if(user_status.equals("U")){ %> disabled <%} %> onchange="getResearchProposal('<%=locationCode%>','<%=ddoCode %>');">
      						 <option value="">Select DDO</option>
      						</select>      				
      					</div> 				        						
				</div>
				
				</div>
				</div>
				
             <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="" class="col-sm-2 col-form-label required-field">Financial Year</label>
						<div class="col-sm-4">
						<select class="form-control" id="finYrId"name="finYrId">
						<option value="">Select Financial Year</option>
					</select> 
						</div>
				          <label for="" class="col-sm-2 col-form-label required-field">Research Proposal</label>
							<div class="col-sm-4">
								<select class="form-control resPrps" id="resPrps" name="resPrps">
									<option value="">Select Research Proposal</option>
								</select>
							</div>
					</div>
				</div>
			</div>
			 <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<!-- <label class="col-sm-2 col-form-label required-field" for="">Purchase Indent</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="PiId" name="PiId">
									<option value="">Select Purchase Indent</option>
								</select>
				           </div> -->
				          <label class="col-sm-2 col-form-label required-field" for="">Expense Date</label>
							<div class="col-sm-4">
								<div class="input-group date" id="msg-EXPDATE">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text"
										class="form-control datecalendar" id="EXPDATE"
										name="EXPDATE" placeholder="DD/MM/YYYY" value="">
								</div>
							</div>
					</div>
				</div>
			</div>
			 <%--  <div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  				
      					<label for="location" class="col-sm-2 col-form-label text-left required-field" >Installment Date</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="installId" name="installId" <%if(user_status.equals("U")){ %> disabled <%} %> >
										<option value="">Select Installation Date</option>
								</select>
				           </div>
				      </div>
				</div>
			</div> --%>     
				<div class="col-md-12 text-center m-b-15">
					<button type="button" class="btn btn-view" id="btnView">View</button>					
					<button type="button" class="btn btn-view" id="btnReset1" >Reset</button>
					<input type="hidden" id="propsalId" value="<%=ResPrps%>" />
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
				<div class="col-md-12 text-center m-t-20 m-b-15 hidediv">
					<%if(General.checknull(fstatus.trim()).equals("E")) { %>
						<!-- <button type="button" class="btn btn-view" id="btnUpdate" >Update</button> -->
						<button type="button" class="btn btn-view" id="btnBack1" >Back</button>
					<%}else{%>
						<button type="button" class="btn btn-view" id="btnSave" onclick="">Save</button>
					<%}%>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
					
					<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">				
					<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />				
				</div>
			</div>
		</div>
   </form>
   
   <form class="form-horizontal" name="frmCreateManageExpD" id="frmCreateManageExpD" action="" method="post" autocomplete="off" >
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
						<label for="" class="col-sm-2 col-form-label required-field">Research Proposal</label>
						<div class="col-sm-4">
							<select class="form-control resPrps" id="resPrpsS" name="resPrpsS">
								<option value="">Select Research Proposal</option>
							</select>
						</div>
					</div>
				</div>
			</div>
				 
			    <div class="col-md-12 text-center m-t-20">
					<button type="button" class="btn btn-view" id="btnSearch" data-dismiss="modal" onclick=" ">Search</button>
					<button type="button" class="btn btn-view" id="btnReset2" >Reset</button> 
					
				</div>				
								<!-- -------------------------------End Here (Write content inside this modal)------------------------------------------- -->
			</div> <!-- End of panal body -->
			</div> <!-- End of panal default -->
		   </div> <!-- End of modal body -->
				 
			</div> <!-- End of modal content -->
		</div> <!-- End of modal-dialog -->
	</div> <!-- End of modal class/id at which modal will be open -->
	</form>

<iframe class="embed-responsive-item" src="" name="btmfrmCreateManageExpE" id="btmfrmCreateManageExpE" frameborder="0" 
 onload="resizeIframe(this)" scrolling="no" width="100%" height="500px"></iframe>
</div>
</body>
<script type="text/javascript">
      function resizeIframe(iframe) {
	    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
	    window.requestAnimationFrame(() => resizeIframe(iframe));
  }
 </script>
</html>
 