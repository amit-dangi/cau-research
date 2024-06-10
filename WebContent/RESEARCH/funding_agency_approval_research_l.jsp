<%@include file="../myCks.jsp"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.sits.rsrch.funding_agency_approval.FundingAgencyApprovalModel" %>
<%@ page import="com.sits.rsrch.funding_agency_approval.FundingAgencyApprovalManager" %>
<!DOCTYPE html> 
<html lang="en">
<head>
    <meta  name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="-1" />
	<title></title>
	<link href="../bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<script src="../js/jquery-3.2.1.js"></script>
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
	<script type="text/javascript" src="../js/research/funding-agency-approval.js"></script>
	 <script type="text/javascript" src="../js/commonDropDown.js"></script>
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<link href="../sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	
</head>
<body>
<form name="frmFundingAgencyApprovalL" id="frmFundingAgencyApprovalL" action="" method="post" autocomplete="off">
  <% 
	String  fstatus="" ,project="", funding="";

fstatus = General.checknull(request.getParameter("fstatus"));

project= General.checknull(request.getParameter("Sproj"));
funding= General.checknull(request.getParameter("Snagency"));
 
FundingAgencyApprovalModel fam = new FundingAgencyApprovalModel();
fam.setProjTitle(project);
fam.setFunding(funding);

	ArrayList<FundingAgencyApprovalModel> ApprovedDetailslist = new ArrayList<FundingAgencyApprovalModel>();
	ApprovedDetailslist=FundingAgencyApprovalManager.getList(fam,"",user_id);
	
	
%>
  
  <div id="searchingTable1" class="scr0">
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
			<div class="page-header-1">
				<div class="col-sm-4 repTitle h5 ">List of <%=ReadProps.getkeyValue("fund_agnc.header","sitsResource") %> (s)</div>
				<div class="col-sm-4 repTitle h6 text-center">Run Date : <%=General.currdate_time() %>&nbsp;</div>
			</div>
    <table id="searchTable" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th class="text-center">S.No.</th>
					<th class="text-center">Location</th>
					<th class="text-center">DDO</th>
					<th class="text-center">Financial Year</th>
					<th class="text-center">Approved Proposal</th>
					<th class="text-center">Funding Agency</th>
					<th class="text-center">Remaining Amount</th>
					<th class="text-center">Edit</th>
			</thead>	
			<tbody>
			<% 
			int i=0;
			if(ApprovedDetailslist.size()>0){
			for(FundingAgencyApprovalModel bhm: ApprovedDetailslist){
				String val = "";
				
			%>
                <tr>
					<td style="text-align:center; width:5%;"><%=++i %></td>
					<td style="text-align:center; width:10%;"><%=bhm.getLocationName()%></td>
					<td style="text-align:center; width:10%;"><%=bhm.getDdoName()%></td>
					<td style="text-align:center; width:5%;"><%=bhm.getFin_yr()%></td>
					<td style="text-align:center; width:10%;"><%=bhm.getProjTitle()%></td>
					<td style="text-align:center; width:10%;"><%=bhm.getFagencyName()%></td>
					<td style="text-align:center; width:8%;"><%=bhm.getRemaining_amount()%></td>
					<td class="text-center colr-blue-p"  style="width:7%;">
						<span id="EDIT_RECORD_<%= i%>"  onclick="editRecord('<%=bhm.getFaaId() %>','<%=bhm.getFin_yr()%>')">
	   				<i class="fa fa-edit p-l-3"></i>View/Edit</span>
	   				</td>
      				<%-- <td class="text-center colr-red-p" style="width:7%;">
      					<span id="DELETE_RECORD_<%= i%>"  onClick="deleteRecord('<%=bhm.getFaaId() %>');">
      					<i class="fa fa-trash p-l-3"></i>Delete</span>
      				</td> --%>	    
				 </tr>
			<%	}}%>
			</tbody>
		</table>
		
		<input type="hidden" name="XfaId" id="XfaId" value="">
		<input type="hidden" name="XfaName" id="XfaName" value="">
   		<input type="hidden" name="XfaType" id="XfaType" value="">
   		<input type="hidden" name="XfaMobNo" id="XfaMobNo" value="">
   		<input type="hidden" name="XfaUrl" id="XfaUrl" value="">        
   		<input type="hidden" name="XfaAddr" id="XfaAddr" value="">   
   		<input type="hidden" name="XfaDetail" id="XfaDetail" value="">
   		<input type="hidden" name="Xfundedby" id="Xfundedby" value=""> 
	</div>
	<input type="hidden" name="totRow" id="totRow" value="<%= i%>" style="width:10px;" readOnly/>
</form>
<script src="../sits/DataTable/1.10.15/js/jquery.js"></script>
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
   	                	title:'Funding Agency Approval',
   	                	filename: 'Funding Agency Approval',
   	                	className: 'btn btn-view',
   	                	text	:'Download',
   	                	exportOptions: {
   	                    	columns: [ 0,1,2,3,4,5]
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