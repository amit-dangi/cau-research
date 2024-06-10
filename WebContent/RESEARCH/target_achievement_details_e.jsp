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
    <script type="text/javascript" src="../js/research/target-achievement-details.js"></script>
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
	<style>
	table.tableGrid th { background-color: #bb9c2b;
	    color: #0c426f;
     }
     .tab_details{padding: 15px;}
	 .tooltip{
	margin-top: 10px !important;
	</style>
	
</head>

<%	
	String ResPrps="",locationCode=""  , ddoCode="",fstatus="",disabled="",dis="";
	fstatus = General.checknull(request.getParameter("fstatus"));
%>
<!-- commonDropDown.js will be used for call the 
getResearchProposal,getRsrchCategory,getRsrchSubCategory -->

<body onload="getResearchProposal('<%=loct_id%>','<%=ddo_id%>','');  getLocationDetail('<%=loct_id%>'); getDdoDetailbyLocation('<%=loct_id%>','<%=ddo_id%>'); getFinanceYrDropdwn('<%=seCurrentFinancialYearId%>','finYr');">
	<div class="container-fluid">
	    <div id="" class="page-header"><h4 ><%=ReadProps.getkeyValue("res_target_achievement_details.header","sitsResource") %></h4>
	 </div>
 
    <form class="form-horizontal" name="frmUtilizationCertificateE" id="frmUtilizationCertificateE" action="" method="post" autocomplete="off" target="">
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right">UC/AUC</h3></div>
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
					 <select class="form-control" id="finYr"name="finYr" >
						<option value=""> Financial Year</option>
					</select> 
					</div>
					<label class="col-sm-2 col-form-label text-left required-field" for="">Project Type</label>
						<div class="col-sm-4">
							<select class="form-control" id="projtype" name="projtype" onchange="getResearchProposal('<%=loct_id%>','<%=ddo_id%>',this.value);">
							<option value="">All</option>
							<%=QueryUtil.getComboOption("rsrch_form1_project_type", "type", "type", "" ,"", "type_id") %>
							</select>
			           </div>
					</div>
				</div>
			</div>
			
			<div class="form-group" >
			<div class="col-md-12">
			      	<div class="row"> 
					<input readonly="readonly" type="hidden" class="form-control" id="finYrId" name="finYrId" value="<%=seCurrentFinancialYearId%>">
				          <label for="" class="col-sm-2 col-form-label required-field">Project title</label>
							<div class="col-sm-4">
								<select class="form-control resPrps" id="resPrps" name="resPrps">
									<option value="">Select Research Proposal</option>
								</select>
							</div>
					</div>	
				</div>
			</div>
			
				<div class="col-md-12 text-center m-b-15">
					<button type="button" class="btn btn-view" id="btnView" onclick="getTargetAchievementDetail();">View</button>					
					<button type="button" class="btn btn-view" id="btnReset1" >Reset</button>
					<input type="hidden" id="propsalId" value="<%=ResPrps%>" />
					<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">				
					<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
					<input type="hidden" id="currenttab" name="currenttab" value="" />
				</div>
			<div>
			<div class="col-md-12 table-responsive" style="padding: 0px;">
				<div id="" class="dataEntryDiv" style="padding: 0px;  overflow-y: unset; 
	 	          min-height: 60px; max-height: unset; width:100%;overflow-x:hidden;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " >
				  <thead>
					<tr style="display: none;" id="detailstr">
					  <th style="text-align:right; width:5%;">Details</th>
					</tr>
				 </thead>
					<tbody id="stable">
					</tbody>
				 </table>
							 	
				 </div> 
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
				<div class="col-md-12 text-center m-t-20 m-b-15 hidediv">
					 <button type="button" class="btn btn-view" id="btnSave" onclick="">Upload</button>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				</div>
			</div>
		</div>
   </form>

</div>
</body>
<script>
 	$(document).ready(function() {
 	$("a").tooltip();
 	});
 	</script>
 
</html>
 