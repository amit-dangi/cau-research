<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@include file="../myCks.jsp"%>
<%@ page import="java.util.ArrayList" %>
<%@page import="com.sits.general.ApplicationConstants"%>
<%@page import="com.sits.general.QueryUtil"%>
<%@ page import="com.sits.rsrch.form1_project_submission.*" %>
<%@ page import="com.sits.rsrch.funding_agency_approval.*" %>

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
 	<link href="../assets/sits/bootstrap/3.3.7/css/bootstrap.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
	<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/aes/AesUtil.js"></script>
	<script type="text/javascript" src="../js/aes/crypto.js"></script>	
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
    <script type="text/javascript" src="../js/research/funding-agency-approval.js"></script>
    <script type="text/javascript" src="../js/commonDropDown.js"></script>
	<%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
	%>
	<link href="../assets/sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
  	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="../css/datepicker/datepicker3.css"  type="text/css">
	<script src="../js/datepicker/bootstrap-datepicker.js"></script>
</head>
<%			
	int k=0;
	String fstatus = General.checknull(request.getParameter("fstatus"));
	
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	String OPT_VALUE = "", cnfrm="" , locationCode=""  , ddoCode="" ,disabled="",
					PS_MID="",Fa_id="",Approved_amount="",Alloted_amount="0",Remaining_amount="",
					allocationamoutheading="Allocation Amount for Active Financial Year",directoryName="",
					sanction_orderno="",sanction_orderdate="",is_opening_blnc="",opening_blnc="",sel="",
					received_date="",alloted_date="";

	if(fstatus.trim().equals("E")){
		disabled="disabled";
	}else{
		disabled="";
	}
	 
	 //System.out.println("locationCode-"+locationCode+" ddoCode="+ddoCode+" user_status="+user_status);
	  	String faaid = General.checknull(request.getParameter("faaid"));
	  	String fin_yr = General.checknull(request.getParameter("fin_yr"));
	  	FundingAgencyApprovalModel fam = new FundingAgencyApprovalModel();
	  	fam.setFaaId(faaid);
	  	fam.setFin_yr(fin_yr);
	  	//System.out.println("Getting the funding approval List in Edit mode with faaid -"+faaid);
		ArrayList<FundingAgencyApprovalModel> ApprovedDetailslist = new ArrayList<FundingAgencyApprovalModel>();
		ApprovedDetailslist=FundingAgencyApprovalManager.getList(fam,"",user_id);
		ArrayList<FundingAgencyApprovalModel> ApprovedDetailslistfordelete = new ArrayList<FundingAgencyApprovalModel>();
		
		if(!ApprovedDetailslist.isEmpty() && fstatus.trim().equals("E")){
		seCurrentFinancialYearId=ApprovedDetailslist.get(0).getFin_yr();
		PS_MID=ApprovedDetailslist.get(0).getPS_MID();
		Fa_id=ApprovedDetailslist.get(0).getFa_id();
		Approved_amount=ApprovedDetailslist.get(0).getApproved_amount();
		Alloted_amount=ApprovedDetailslist.get(0).getAlloted_amount();
		Remaining_amount=ApprovedDetailslist.get(0).getRemaining_amount();
		allocationamoutheading="Available Amount for Selected Financial Year (Previous total Allotment <b>"+Alloted_amount+"<b> )";
		locationCode=	ApprovedDetailslist.get(0).getLocation_code();
		ddoCode=	ApprovedDetailslist.get(0).getDdo_id();
		sanction_orderno=	ApprovedDetailslist.get(0).getSanction_orderno();
		sanction_orderdate=	General.checknull(ApprovedDetailslist.get(0).getSanction_orderdate());
		alloted_date=	General.checknull(ApprovedDetailslist.get(0).getAlloted_date());
		received_date=	General.checknull(ApprovedDetailslist.get(0).getReceived_date());
		is_opening_blnc	=General.checknull(ApprovedDetailslist.get(0).getIs_opening_blnc());
		opening_blnc	=General.checknull(ApprovedDetailslist.get(0).getOpening_blnc());
		sel=is_opening_blnc.equals("Y")?"Checked":"";
		fam.setPS_MID(PS_MID);
		ApprovedDetailslistfordelete=FundingAgencyApprovalManager.getList(fam,"delete",user_id);
		directoryName = "RSRCH/"+PS_MID+"/";
		}
		else{
			  locationCode= locationCode.equals("")?loct_id:locationCode;
			 ddoCode= ddoCode.equals("")?ddo_id:ddoCode; 
		}
		
	
%>
<body onload="geteditDD('<%=fstatus%>', '<%=PS_MID%>', '', '','<%=locationCode%>','<%=ddoCode %>'); getLocationDetail('<%=locationCode%>'); 
getFinanceYrDropdwn('<%=seCurrentFinancialYearId%>','fin_yr'); 
getFundingAgency('<%=PS_MID%>','<%=Fa_id%>','fnagency'); ">
<script type="text/javascript">
var user_status='<%=user_status%>';
   $(document).ready(function() { 
		$("#sanction_orderdatediv").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#sanction_orderdatediv").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#allot_orderdatediv").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#allot_orderdatediv").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#received_orderdatediv").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#received_orderdatediv").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
 });
 </script>
<div class="container-fluid">
 
  <div id="" class="page-header"><h4>Funding Agency Approval
  <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
  </span>
 </h4>
 </div>
<!-- <div id="" class="page-header"><h4>Funding agency approval</h4> </div> -->
    <form class="form-horizontal" name="frmFundingAgencyApprovalE" id="frmFundingAgencyApprovalE" action="" method="post" autocomplete="off" target="" enctype="multipart/form-data">
    
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  				
      					<label for="location" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%=disabled %> <%if(user_status.equals("U")){ %> disabled <%} %> onchange=getDdoDetailbyLocation(this.value);>
									<option value="">Select Location</option>
								</select>
				           </div>
				           
				           <label for="ddo" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo"  <%=disabled %> <%if(user_status.equals("U")){ %> disabled <%} %> onchange="getResearchProposal('','<%=locationCode%>','<%=ddoCode %>');">
      						 <option value="">Select DDO</option>
      						</select>      				
      					</div> 				        						
				</div>
				
				</div>
				</div>
			<div class="form-group " >
			 <div class="col-md-12 ">
						<div class="row " >
				<label class="col-sm-2 col-form-label text-left required-field" for="">Project</label>
							<div class="col-sm-4">
								
								<select class="resPrps form-control" id="proj" name="proj" <%=disabled %> onchange="getFundingAgency(this.value,'<%=Fa_id%>','fnagency'); getProjectTenurebyApprovedProjects(this.value);" >
									<option value="">Select Project</option>
								</select>
				           </div>
			<label class="col-sm-2 col-form-label">Financial Year</label>
					<div class="col-sm-4">
					 <select class="form-control" id="fin_yr"name="fin_yr" <%=disabled %>>
						<option value="">Select Financial Year</option>
					</select> 
					</div>
				           
						   </div>
			</div>
			</div>
			
			<div class="form-group " >
			 <div class="col-md-12 ">
						<div class="row " >
				<label class="col-sm-2 col-form-label text-left required-field" for="">Funding Agency</label>
							<div class="col-sm-4">
								
								<select class="form-control" id="fnagency" name="fnagency"  <%=disabled %>>
									<option value="">Select Funding Agency</option>
									<%-- <%=QueryUtil.getComboOption("rsrch_funding_agency", "fa_id", "concat(fa_name,' ~ ',fundedby)", Fa_id,"", "fa_name") %> --%>
								</select>
				           </div> 
			<label class="col-sm-2 col-form-label">Project Tenure</label>
					 <div class="col-sm-4">
					 <input type="text" class="form-control" id="proj_duration" name="proj_duration" value="" disabled>
					</div> 
				           
						   </div>
			</div>
			</div>
			
			   <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="" class="col-sm-2 col-form-label">Is opening balance carryforward</label>
							<div class="col-sm-1">
								<input type="checkbox" id="is_opening_blnc" name="is_opening_blnc"  <%=sel%> <%=!opening_blnc.equals("")?"disabled":""%> onkeypress="if ( isNaN(this.value + String.fromCharCode(event.keyCode) )) return false;">
							 </div>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="opening_blnc" name="opening_blnc" placeholder="Enter Opening Balance" value="<%=opening_blnc%>" disabled onclick="getRemainingAmout();">
						</div>
					</div>
				</div>
			</div>
			
			<div class="form-group " >
			 <div class="col-md-12 ">
						<div class="row " >
				<label class="col-sm-2 col-form-label text-left required-field" for="">Total Approved Amount(Rs.)</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="approvedAmount" name="approvedAmount" value="<%=Approved_amount%>" placeholder="Total Approved Amount" <%=disabled %> onkeypress="if ( isNaN(this.value + String.fromCharCode(event.keyCode) )) return false;"  style="text-align:right;" maxlength="9">
					<%if(!Approved_amount.equals("")){ %>
					<button type="button" class="btn btn-view m-t-20" id="editamount" style="display: show;">Edit Amount</button>
					<button type="button" class="btn btn-view m-t-20" id="updateamount" style="display: none;" onclick="updateAmount();">Update Amount</button>
					<%} %>
					</div>
					<input type="hidden" class="form-control" id="previous_approvedAmount" value="<%=Approved_amount%>" > 
				<label class="col-sm-2 col-form-label"><%=allocationamoutheading%></label>
					 <div class="col-sm-2">
					 <input type="text" class="form-control" id="allotedAmount" name="allotedAmount" value="<%=Remaining_amount %>" placeholder="Alloted Amount" onblur="getRemainingAmout();" onkeypress="if ( isNaN(this.value + String.fromCharCode(event.keyCode) )) return false;" style="text-align:right;" maxlength="9">
					</div>
					<input type="hidden" id="alloted_amount_sum" value="<%=Alloted_amount%>"></input>
					<label class="col-sm-1 col-form-label">Balance</label> 
					<div class="col-sm-1" style="padding-left:0px; padding-right:10px;">
					 <input type="text" class="form-control" id="remainingAmount" name="remainingAmount" value="0" placeholder="Remaining Amount" disabled style="text-align:right;">
					</div> 
				       <input type="hidden" class="form-control" id="lastallotedAmount" name="lastallotedAmount" value="<%=Alloted_amount%>">
					    
						   </div>
			</div>
			</div>
			
			<div class="form-group " >
			 <div class="col-md-12 ">
						<div class="row " >
					<label class="col-sm-2 col-form-label">Remarks</label>
					 <div class="col-sm-4">
					 <textarea style="height: 65px;" rows="15" cols="50" placeholder="Enter Details" id="remarks" name="remarks" maxlength="254"></textarea>
					</div> 
					<label class="col-sm-2 col-form-label">Sanction Order No.</label>
					 <div class="col-sm-4">
					 <input class="form-control" type="text" maxlength="100" name="sanction_orderno" id="sanction_orderno" value="<%=sanction_orderno%>" placeholder="Enter Sanction Order No."/>
					</div> 
				           
						   </div>
			</div>
			</div>
			
			<div class="form-group " >
			 <div class="col-md-12 ">
						<div class="row " >
				<label class="col-sm-2 col-form-label text-left" for="">Upload Details (If Any)</label>
							<div class="col-sm-4">
								<input type="file" id="upldoc1" name="upldoc1" class="">
								<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
					  			<br> (*Note: Only .pdf, .jpg, .png, & doc files will be allowed. Max Size Can't Exceed 20MB.)
					  		</span>
							</div>
					<label class="col-sm-2 col-form-label text-left" for="">Sanction Order Date</label>
						<div class="col-sm-4 ">
							<div class="input-group date" id="sanction_orderdatediv">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
								id="sanction_orderdate" name="sanction_orderdate" placeholder="DD/MM/YYYY" value="<%=sanction_orderdate%>" >
							</div>								
			           </div> 
						   </div>
			</div>
			</div>
			
			
				<div class="form-group " >
			 <div class="col-md-12 ">
						<div class="row " > 
					<label class="col-sm-2 col-form-label text-left required-field" for="">Alloted Date</label>
						<div class="col-sm-4 ">
							<div class="input-group date" id="allot_orderdatediv">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
								id="all_date" name="all_date" placeholder="DD/MM/YYYY" value="<%=alloted_date%>" >
							</div>								
			            </div>
			            <label class="col-sm-2 col-form-label text-left " for="">Received Date</label>
						<div class="col-sm-4 ">
							<div class="input-group date" id="received_orderdatediv">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
								id="received_date" name="received_date" placeholder="DD/MM/YYYY" value="<%=received_date%>" >
							</div>								
			            </div> 
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
			<div class="col-md-12 text-center m-t-20">
				<button type="button" class="btn btn-view" id="btnSave" name="btnSave" onclick="save('S');">Save</button>
				<%if (fstatus.trim().equals("E")){ %>
				<button type="button" class="btn btn-view" id="btnReset" >Back</button>	
				<% }else{%>
				<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				<%} %>			
				<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				<input type="hidden" name="fstatus" id="fstatus" value="<%=General.checknull(fstatus) %>">
				<input type="hidden" name=index id="index" value="<%=k%>">
				<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
				<input type="hidden" id="propsalId" value="<%=PS_MID%>" />
				<input type="hidden" id="faaid" value="<%=faaid%>" />
			</div>
				
			</div>
		</div>
		  <%if (fstatus.trim().equals("E")){ %> 
		 <!--  <div class="page-header-1">
				<div class="col-sm-4 repTitle h5 ">Approval History (Only latest entry will be delete once.)</div>
			</div> -->
			
			<div class="page-header-1">
				<div class="col-sm-4 repTitle h5 ">Approval History (Only latest entry will be delete once.)</div>
				<div class="col-sm-4 repTitle h6 text-center">Run Date : <%=General.currdate_time() %>&nbsp;</div>
			</div>
		<table id="searchTable" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th class="text-center">S.No.</th>
					<!-- <th class="text-center">Location</th>
					<th class="text-center">DDO</th>
					 --><th class="text-center">Approved Proposal</th>
					<th class="text-center">Approved Amount</th>
					<th class="text-center">Alloted Amount</th>
					<th class="text-center">Alloted Date</th>
					<th class="text-center">Sanction Date</th>
					<th class="text-center">Received Date</th>
					<th class="text-center">Remarks</th>
					<th class="text-center">Uploaded Attachment</th>
					<th class="text-center">Delete</th> 
			</thead>	
			<tbody>
			<% 
			int i=0;
			if(ApprovedDetailslistfordelete.size()>0){
			for(FundingAgencyApprovalModel list: ApprovedDetailslistfordelete){
				String val = "";
				
			%>
                <tr>
					<td style="text-align:center; width:5%;"><%=++i %></td>
					<%-- <td style="text-align:center; width:15%;"><%=list.getLocationName()%></td>
					<td style="text-align:center; width:10%;"><%=list.getDdoName()%></td>
					 --%><td style="text-align:center; width:10%;"><%=list.getProjTitle()%></td>
					<td style="text-align:center; width:10%;"><%=list.getApproved_amount()%></td>
					<td style="text-align:center; width:8%;"><%=list.getAlloted_amount()%></td>
					<td style="text-align:center; width:8%;"><%=list.getAlloted_date()%></td>
					<td style="text-align:center; width:8%;"><%=list.getSanction_orderdate()%></td>
					<td style="text-align:center; width:8%;"><%=list.getReceived_date()%></td>
					<td style="text-align:center; width:8%;"><%=list.getRemarks()%></td>
      				<td style="text-align:center; width:8%;"><a target="_blank" href="../downloadfile?filename=<%=list.getPS_MID()%>_<%=list.getUploadedFile()%>&folderName=RSRCH/<%=list.getPS_MID()%>/">
							<u><%=list.getUploadedFile()%></u></a>
						</td>
      				<td class="text-center colr-red-p" style="width:7%;">
      				<%if(i==1 && (list.getIsfundallocated().equals("N") || list.getIsfundallocated().equals(""))){ %>
      					<span id="DELETE_RECORD_<%= i%>"  onClick="deleteRecord('<%=list.getFaaId() %>');">
      					<i class="fa fa-trash p-l-3"></i>Delete</span>
      					<%} %>
      					<%if(list.getIsfundallocated().equals("Y")){ %>
      					<span><b>Fund Allocated</b></span>
      					<%} %>
      				</td>	    
				 </tr>
			<%	}}%>
			</tbody>
		</table>
		<%} %>
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
							<div class="form-group " >
			 <div class="col-md-12 ">
						<div class="row " >
				<label class="col-sm-2 col-form-label text-left" for="">Project</label>
							<div class="col-sm-4">
								
								<select class="form-control resPrps" id="Sproj" name="Sproj" <%=disabled %> onchange="getProjectTenurebyApprovedProjects(this.value);" >
									<option value="">Select Project</option>
								</select>
				           </div>
			<label class="col-sm-2 col-form-label text-left" for="">Funding Agency</label>
							<div class="col-sm-4">
								
								<select class="form-control" id="Snagency" name="Snagency"  <%=disabled %>>
									<option value="">Select Funding Agency</option>
									<%=QueryUtil.getComboOption("rsrch_funding_agency", "fa_id", "concat(fa_name,' ~ ',fundedby)", Fa_id,"", "fa_name") %>
								</select>
				           </div> 
				           
						   </div>
			</div>
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
					<button type="button" class="btn btn-view" id="btnReseT" >Reset</button> 
				</div>				
				
			<!-- -------------------------------End Here (Write content inside this modal)------------------------------------------- -->
			</div> <!-- End of panal body -->
			</div> <!-- End of panal default -->
		   </div> <!-- End of modal body -->
				 
			</div> <!-- End of modal content -->
		</div> <!-- End of modal-dialog -->
	</div> <!-- End of modal class/id at which modal will be open -->
	</form>
   
   
    <%if (!fstatus.trim().equals("E")){ %> 
  		<iframe class="embed-responsive-item" src="funding_agency_approval_research_l.jsp" name="frmFundingAgencyApprovalE" id="frmFundingAgencyApprovalE" frameborder="0" scrolling="no" width="100%" height="480px"></iframe>
  	 <%} %>  
   </div> 
   
   <!-- <script src="../sits/DataTable/1.10.15/js/jquery.js"></script> -->
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.js"></script>

<script src="../sits/DataTable/1.10.15/js/jquery.dataTables.js"></script>
<script src="../sits/DataTable/1.10.15/js/dataTables.bootstrap.js"></script>
<script src="../sits/DataTable/1.4.2/js/buttons.flash.min.js"></script>
<script src="../sits/DataTable/1.4.2/js/buttons.html5.min.js"></script>
<script src="../sits/DataTable/1.4.2/js/buttons.print.min.js"></script>
<script src="../sits/DataTable/1.4.2/js/dataTables.buttons.min.js"></script>
<script src="../sits/DataTable/1.4.2/js/vfs_fonts.js"></script> 
<script src="../sits/DataTable/1.4.2/js/jszip.min.js"></script>
<script>      $(document).ready(function() {
    	    var t = $('#searchTable').DataTable( {
    	    	"lengthMenu": [[-1,10, 25, 50, 100, 250, 500], ['All',10, 25, 50, 100, 250, 500]],
    	    	"searching" : false,
       			"scrollY": "250px",
                "scrollX": true,
                "scrollCollapse": true,
                "paging": true,
    	    	"columnDefs": [ {
    	            "searchable": false,
    	            "orderable": false,
    	            "targets": 0
    	        } ],
    	         dom :"<'row'<'col-sm-4 text-left'.h5><'col-sm-4 text-center'.h6> <'col-sm-4 text-right'B>>" +
            	"<'row'<'col-sm-6'l><'col-sm-6'>>" +
       		    "<'row'<'col-sm-12'tr>>" +
        	    "<'row'<'col-sm-5'i><'col-sm-7'p>>",   
	        	buttons: [
	            	{	
   	                	extend: 'excelHtml5',
   	                	title:'Funding Agency Approval History',
   	                	filename: 'Funding Agency Approval History',
   	                	className: 'btn btn-view',
   	                	text	:'Download',
   	                	exportOptions: {
   	                    	columns: [ 0,1,2,3,4,5,6,7,8]
   	                	},
   	           		 }
	        	],
    	        columnDefs: [{ orderable: false, "targets": [0,3,4,5,6] },
   				  
   				 ],
    	        "order": [[ 1, 'asc' ]]
    	    } );
    	 
    	    t.on( 'order.dt search.dt', function () {
    	        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
    	            cell.innerHTML = i+1;
    	            t.cell(cell).invalidate('dom');
    	        } );
    	    } ).draw();
    	} );
      
</script>
</body>
</html>