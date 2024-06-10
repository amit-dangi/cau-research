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
    <script type="text/javascript" src="../js/research/staff-position-filled-post.js"></script>
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
	}
		.border-space{border:unset !important;}
		
	
 	  .dataEntryDiv{ 
 	    overflow-y: auto; 
 	    min-height: 50px; 
 	    max-height: 500px; 
 	   }
	  .dataEntryDiv thead tr th { 
	     position: sticky; 
	     top: -1px;
         border-top: 0px solid #62b762 !important;  
         box-shadow: 1px 0 0 1px #62b762 !important;
         outline: #4CAF50 solid 1px;
         outline-offset: -1px;  
     }
		table  { border-collapse: collapse; width: 100%; }
		th, td { padding: 4px 4px; }
		th     { background:#eee; }
 
 	 table.tableGrid thead tr th {
        background-color: #bb9c2b;
          border: 0px solid #439653 !important;  
        color:#0c426f;
     } 
   .page-header-1 .repTitle {
    font-family: Calibri, verdana;
    font-size: 16px;
    font-weight: bold;
    padding: 10px 0px;
    color: #0a151e !important;
    width: 100%;
    }
 </style>
	
</head>

<%	
	String ResPrps="",locationCode=""  , ddoCode="",fstatus="",disabled="",dis="";
	fstatus = General.checknull(request.getParameter("fstatus"));

%>
<!-- commonDropDown.js will be used for call the 
getResearchProposal,getRsrchCategory,getRsrchSubCategory -->

<body onload="getEmployee('','PiName');getResearchProposal('<%=loct_id%>','<%=ddo_id%>','');  getLocationDetail('<%=loct_id%>'); getDdoDetailbyLocation('<%=loct_id%>','<%=ddo_id%>'); getFinanceYrDropdwn('<%=seCurrentFinancialYearId%>','finYr');">
	<div class="container-fluid">
	    <div id="" class="page-header"><h4 ><%=ReadProps.getkeyValue("staff_position_filled_post.header","sitsResource") %></h4>
	 </div>
 
    <form class="form-horizontal" name="frmUtilizationCertificateE" id="frmUtilizationCertificateE" action="" method="post" autocomplete="off" target="">
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right">Staff Position Filled Post</h3></div>
			<div class="panel-body">
			
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  				
      					<label for="location" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%if(user_status.equals("U")){ %> disabled <%} %> onchange="getDdoDetailbyLocation(this.value);">
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
						  
				           <label class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Name of the PI/Scientist In-Charge</label>
							<div class="col-sm-4 ">
								<%-- <input class="form-control" placeholder="Enter PI Name" value="<%=piName %>" type="text" id="PiName" name="PiName""> --%>
								<select class="form-control" id="PiName" name="PiName" <%=dis %> <%=disabled %>>
									<option value="">Select Principal Investigator</option>
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
							<label class="col-sm-2 col-form-label text-left required-field" for="">Funding Agency</label>
							<div class="col-sm-4">
								
								<select class="form-control" id="fn_agency" name="fn_agency"  <%=disabled %>>
									<option value="">Select Funding Agency</option>
									<%=QueryUtil.getComboOption("rsrch_funding_agency", "fa_id", "concat(fa_name,' ~ ',fundedby)", "","", "fa_name") %>
								</select>
		           </div> 
					</div>	
				</div>
			</div>
			
				<div class="col-md-12 text-center m-b-15">
					<button type="button" class="btn btn-view" id="btnView" onclick="getStaffPositionDetail();">View</button>					
					<button type="button" class="btn btn-view" id="btnReset1" >Reset</button>
					<input type="hidden" id="propsalId" value="<%=ResPrps%>" />
					<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">				
					<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
				</div>
			<div>
			
		   <div id="co_pi_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of Co-PI:(<span id="co_pi_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="co_pi" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="scientist_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of scientist:(<span id="scientist_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="scientist" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="ra_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of RA:(<span id="ra_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="ra" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="srf_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of SRF:(<span id="srf_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="srf" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="jrf_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of JRF:(<span id="jrf_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="jrf" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="tec_ass_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of Technical Assistant:(<span id="tec_ass_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="tec_ass" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="fil_ass_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of Field Assistant:(<span id="fil_ass_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="fil_ass" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="la_fi_att_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of Lab/Field Attendant:(<span id="la_fi_att_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="la_fi_att" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="fil_man_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of Fieldman:(<span id="fil_man_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="fil_man" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="yp_i_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of YP-I:(<span id="yp_i_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="yp_i" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="yp_ii_hide" style="display:none">
			<div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Number of YP-II:(<span id="yp_ii_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Appointment Letter</th>
					  <th style="text-align:center;">Clear</th>
					</tr>
				 </thead>
					<tbody id="yp_ii" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>     
			</div>
			
			<div id="other_hide" style="display:none">
			<!-- <div class="page-header-1 ">
			   <div class="col-sm-4 repTitle h5 ">Others (Please specify):(<span id="other_post"></span>)</div>
		    </div>
			<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">
				<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >
				  <thead>
					<tr>
					  <th style="text-align:center;">S.No.</th>
					  <th style="text-align:center;">Name</th>
					  <th style="text-align:center;">Upload Docs.</th>
					  <th style="text-align:center;">Delete</th>
					</tr>
				 </thead>
					<tbody id="other" class="dataEntryDiv" >
					
					</tbody>
				  </table>
				</div> 
			 </div>      -->
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
					 <button type="button" class="btn btn-view" id="btnSave" onclick="">Save</button>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				</div>
				
				<input type="hidden"  id="co_pi_count" name="" value="">
				<input type="hidden"  id="scientist_count" name="" value="">
				<input type="hidden"  id="ra_count" name="" value="">
				<input type="hidden"  id="srf_count" name="" value="">
				<input type="hidden"  id="jrf_count" name="" value="">
				<input type="hidden"  id="tech_asisstant_count" name="" value="">
				<input type="hidden"  id="field_asisstant_count" name="" value="">
				<input type="hidden"  id="lab_attendant_count" name="" value="">
				<input type="hidden"  id="fieldman_count" name="" value="">
				<input type="hidden"  id="yp1_count" name="" value="">
				<input type="hidden"  id="yp2_count" name="" value="">
				<input type="hidden"  id="others_count" name="" value="">
				
			
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
 