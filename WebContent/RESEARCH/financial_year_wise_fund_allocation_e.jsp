<%@page import="com.sits.rsrch.research_activity.fin_year_wise_fund_allocation.FundAllocationManager"%>
<%@page import="com.sits.rsrch.research_activity.fin_year_wise_fund_allocation.FundAllocationModel"%>
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
    <script type="text/javascript" src="../js/research/financial-year-wise-fund-allocation.js"></script>
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
	 /*    border: 1px solid #1ea2cd !important; */
	    color: #0c426f;
     }
     
     p11 {
  padding-top: 50px;
}
	</style>
</head>
<script>
$(document).ready(function() {
	$("#XALLDATE").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
	}).on('click', function() {
		$("#XALLDATE").datepicker("setDate", 'today');
	}).on('clearDate', function(selected) {
	});
});
</script>
<%	
	ArrayList<FundAllocationModel> arrayList = new ArrayList<FundAllocationModel>();
	ArrayList<FundAllocationModel> editList = new ArrayList<FundAllocationModel>();
	ArrayList<FundAllocationModel> editListD = new ArrayList<FundAllocationModel>();
	String sel= "", fstatus="", OPT_VALUE = "", faid="", piid="",disabled="",dis="";
	String Faid="", FinYr="", fAgency="", ResPrps="", Date="", Remark="",locationCode=""  , ddoCode="";
	int index=0;
	fstatus = General.checknull(request.getParameter("fstatus"));
	user_status		= General.checknull((String)session.getAttribute("user_status"));
	faid = General.checknull(request.getParameter("faid"));
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
		 index=0;
		//arrayList=FundAllocationManager.getHeadList();
	}/* else if(General.checknull(fstatus.trim()).equals("V")) {
		arrayList=FundAllocationManager.getListView(faid);
	}else if(General.checknull(fstatus.trim()).equals("E")) {
		editList=FundAllocationManager.getEditList(faid);
		
		Faid=General.checknull(editList.get(0).getFaid());
		FinYr=General.checknull(editList.get(0).getFinYr());
		fAgency=General.checknull(editList.get(0).getfAgency());
		ResPrps=General.checknull(editList.get(0).getResPrps());
		Date=General.checknull(editList.get(0).getDate());
		Remark=General.checknull(editList.get(0).getRemark());
		piid=General.checknull(editList.get(0).getPiid());
		//System.out.println("faid-"+faid);
		editListD=FundAllocationManager.getEditListDetail(Faid);
		
	} */
	
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E")) {
		OPT_VALUE = ApplicationConstants.EDIT;
	}
	//getProjectDetailbyLocation
%>

<body onload="getResearchProposal('<%=loct_id%>','<%=ddo_id%>'); unhide('<%=fstatus%>', '<%=piid%>'); getLocationDetail('<%=loct_id%>'); getDdoDetailbyLocation('<%=loct_id%>','<%=ddo_id%>');getFinanceYrDropdwn('<%=session.getAttribute("seCurrentFinancialYearId")%>','fin_yr'); ">
<div class="container-fluid">
    <%if(!General.checknull(fstatus.trim()).equals("V")) { %>
    <div id="" class="page-header"><h4 ><%=ReadProps.getkeyValue("res_fin_fund_allocation.header","sitsResource") %></h4>
   <!--  <span id="" class="text-right ">
	 <h4><a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"><span>Advance Search</span>
	 	<i class="fa fa-search"></i></a>
	 </h4>
	 </span> -->
 </div>
 
    <form class="form-horizontal" name="frmfundAllocationE" id="frmfundAllocationE" action="" method="post" autocomplete="off" target="">
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
							<%-- <input readonly="readonly" type="text" class="form-control" id="fin_yr" name="fin_yr" placeholder="Financial Year" value="<%=currentFinancialYear%>">
							<input readonly="readonly" type="hidden" class="form-control" id="finYrId" name="finYrId" value="<%=seCurrentFinancialYearId%>">
						 --%>
						  <select class="form-control" id="fin_yr"name="fin_yr">
						<option value="">Select Financial Year</option>
					</select> 
						 </div>
						<%-- <label class="col-sm-2 col-form-label required-field" for="">Funding Agency</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="FundAgency" name="FundAgency">
									<option value="">Select Fund Agency</option>
									<%=QueryUtil.getComboOption("RSRCH_FUNDING_AGENCY", "fa_id", "fa_Name", fAgency, "", "fa_name") %>
								</select>
				           </div> --%>
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
						<label class="col-sm-2 col-form-label required-field" for="">Installment Date</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="installId" name="installId">
									<option value="">Select Installment Date</option>
								</select>
				           </div>
						
					</div>
				</div>
			</div>
			<!--  <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label required-field" for="">Purchase Indent</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="PiId" name="PiId">
									<option value="">Select Purchase Indent</option>
								</select>
				           </div>
						 
					</div>
				</div>
			</div> -->
			  
			<%-- <div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="" class="col-sm-2 col-form-label">Remarks</label>
						<div class="col-sm-4">
							  <textarea id="remark" name="remark" placeholder=" Enter Remarks" style="width:100%; height:60px;"><%=Remark %></textarea> 
						</div>
					</div>
				</div>
			</div> --%>
			
			<div class="form-group">
				<div class="col-md-12">
					<div class="row">
					<label for="" class="col-sm-4 col-form-label required-field"><b>Total Available Amount for Selected Installment Date:</b></label>
						<div class="p11" id="installAmt" style="font-weight:bold;">
						</div>  
					</div>
				</div>
			</div>
				 
				 
				<div class="col-md-12 text-center m-b-15">
					<!-- <button type="button" class="btn btn-view" id="btnView">View</button>					
					<button type="button" class="btn btn-view" id="btnReset1" >Reset</button>
					 --><input type="hidden" id="propsalId" value="<%=ResPrps%>" />
					 <input type="hidden" id="installAmt1" value="" />
					 
				</div>
				
				<%} %>
				
				<%if(!General.checknull(fstatus.trim()).equals("V") ) { %>
					<div class="hidediv">
				<%}else{ %>
					<div>
				<%}%>
				
			<div class="col-md-12 table-responsive" style="padding: 0px;">
				<div id="" class="dataEntryDiv" style="padding: 0px;  overflow-y: auto; 
	 	          min-height: 60px; max-height: 300px; width:100%;">
				  <table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id ="tableHide" style="display: none" >
				  <thead>
					<tr>
					  <th style="text-align:center width:3%;">S.No.</th> 
					  <th style="text-align:center width:10%;">Head Name</th>
					   <th style="text-align:center width:10%;">Sub Head Name</th>
					  <th style="text-align:center width:10%;">Amount (Rs.)</th>
					   <th style="text-align:center width:10%;">Delete</th>
					</tr>
				 </thead>
					 
					 <tbody id="stable" class="dataEntryDiv slist" > 
 					 
					<%--  <tr class="Hhide">
					<td style="text-align:center; width:3%;" id="srn_<%=index%>">1</td> 
				     <td style="text-align:center; width:10%;">
				     <select class="form-control headclass" id="head_<%=index%>" name="head_<%=index%>">
				     <option >select Head Name </option>
				      <%=QueryUtil.getComboOption("rsrch_research_head", "head_id", "head_Name", "", "is_active='Y'", "head_Name") %>
				     </select>
 				    </td>
				    <td style="text-align:center; width:10%;">
				      <select class="form-control" id="shead_<%=index%>" name="shead_<%=index%>">
				     <option >select Sub Head Name </option>
				     </select>
				    </td>
				    <td style="text-align:center; width:10%;">
				   	<input type="text" class="form-control" id="amount_<%=index%>" name="amount_<%=index%>" placeholder="Enter Amount"  value="">
				   </td>
				    <td class="colr-red-p text-center" style="width:5%;color:red;"><span id="" onClick=""><i class="fa fa-trash p-l-3"></i>Delete</span></td>
					<input type ="hidden" id="detId_<%=index%>" value="" />
				  </tr> --%>
						<!-- <input type="hidden" id="index" name="index" value=""> -->
				 </tbody>
 				 </table>
							 	
				 </div> 
				 
				 	<div class="col-sm-12" style="text-align:right;" id="totaldata">
					</div>
					
				</div>
				
				</div>
					<div class="col-md-12 text-right m-t-20">
						<button type="button" class="btn btn-view" id="addRow"  >Add More</button>
					</div> 
					<div class="col-md-8 text-right m-t-20">
						<div style="font-weight:bold;" id="headwisetotal"></div>
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
						 <button type="button" class="btn btn-view" id="btnUpdate" >Update</button> 
						 <button type="button" class="btn btn-view" id="btnBack1" >Back</button> 
					<%}else{%>
						 <button type="button" class="btn btn-view" id="btnSave" onclick="">Save</button>
					<%}%>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
					
					
					<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">				
					<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
					<!-- <input type="text" id="pId" value="">
					<input type="text" id="prpsl" value=""> -->
				</div>
			</div>
		</div>
		<input type="hidden" name=count id="count" value="<%=index%>">
   </form>
    
   <form class="form-horizontal" name="frmfundAllocationD" id="frmfundAllocationD" action="" method="post" autocomplete="off" >
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
 
 <div class="modal fade recomond-btn-list" id="reportDiolog" tabindex="-1" role="dialog" aria-labelledby="open-Modal" aria-hidden="true" style="margin-top: -27px;">
		<div class="modal-dialog" style="width:85%;">
			<div class="modal-content">
				<div class="modal-header text-center">
					<span class="close" data-dismiss="modal" aria-label="Close close-cross" aria-hidden="true" style="margin-top: 0px !important;">&times;</span>
					<h4 class="modal-title">Allocated Amount</h4>
				</div>
				
				<div class="modal-body" style="height: 400px"> <!--Start Modal Body-->
					  <form name="pdfForm" id="pdfForm" method="get" autocomplete="off" style="padding: 0px !important;">
						<iframe class="embed-responsive-item" name="1_Report" id="1_Report" width="100%;" height="390px" src="" frameborder="0" scrolling="yes"></iframe>							
					</form>
				</div>
				<!--End Modal Body-->
				
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
   <%if(!General.checknull(fstatus.trim()).equals("V")) { %>
<!-- <iframe class="embed-responsive-item" src="financial_year_wise_fund_allocation_l.jsp" name="btmfrmfundAllocationE" id="btmfrmfundAllocationE" frameborder="0" scrolling="no" width="100%" height="480px"></iframe> -->
<%} %>
</div>
</body>
</html>
 