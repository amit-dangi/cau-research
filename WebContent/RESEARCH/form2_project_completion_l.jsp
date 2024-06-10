<%@include file="../myCks.jsp"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.sits.rsrch.form2_project_completion.*"%>
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
	<script type="text/javascript" src="../js/research/form2-project-completion.js"></script>
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<link href="../sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	
</head>
<body>
<form name="frmF1ProjectSubmissionL" id="frmF1ProjectSubmissionL" action="" method="post" autocomplete="off">
  <% 
	String piId="", dept="", desg="";

piId=General.checknull(request.getParameter("piId"));
dept=General.checknull(request.getParameter("deptId"));
desg=General.checknull(request.getParameter("desId"));
String locationCode=General.checknull(request.getParameter("locationCode"));
String ddoCode = General.checknull(request.getParameter("ddoCode"));
piId=piId.equals("")?sess_emp_id:piId; 
ArrayList<ProjectCompletionModel> arrayList = ProjectCompletionManager.getList(piId, dept, desg,locationCode,ddoCode,user_status);
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
				<div class="col-sm-4 repTitle h5 ">List of <%=ReadProps.getkeyValue("form2_proj_comp.header","sitsResource") %> (s)</div>
				<div class="col-sm-4 repTitle h6 text-center">Run Date : <%=General.currdate_time() %>&nbsp;</div>
			</div>
    <table id="searchTable" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th class="text-center">S.No.</th>
					<th class="text-center">Location Name</th>
					<th class="text-center">DDO Name</th>
					<th class="text-center">Title Of the Project Proposal</th>
					<th class="text-center">Name of the Principal Investigator</th>
					<th class="text-center">Department</th>
					<th class="text-center">Designation</th>
					<th class="text-center">Project Status</th>
					<th class="text-center">Edit</th>
					<th class="text-center">Delete</th>
			</thead>	
			<tbody>
			<% 
			int i=0;
			if(arrayList.size()>0){
			for(ProjectCompletionModel bhm: arrayList){
			%>
                <tr>
					<td style="text-align:center; width:5%;"><%=++i %></td>
					<td style="text-align:center; width:15%;"><%=General.checknull(bhm.getLocationName()) %></td>
					<td style="text-align:center; width:15%;"><%=General.checknull(bhm.getDdoName()) %></td>
					<td style="text-align:center; width:15%;"><%=General.checknull(bhm.getPiName()) %></td>
					<td style="text-align:center; width:15%;"><%=General.checknull(bhm.getPiNameDesc())%></td>
					<td style="text-align:center; width:10%;"><%=General.checknull(bhm.getDeptName())%></td>
					<td style="text-align:center; width:10%;"><%=General.checknull(bhm.getDesgName())%></td>
					<td style="text-align:center; width:10%;"><%=bhm.getIsSubmit().equals("Y")?"Completed":"Ongoing" %></td>
					<td class="text-center colr-blue-p"  style="width:7%;">
						<span id="EDIT_RECORD_<%= i%>"  onclick="editRecord('<%=bhm.getfId()%>','<%=General.checknull(bhm.getPiName()) %>')">
	   				<i class="fa fa-edit p-l-3"></i>Edit</span>
	   				</td>
      				<td class="text-center colr-red-p" style="width:7%;">
      					<span id="DELETE_RECORD_<%= i%>"  onClick="deleteRecord('<%=bhm.getfId()%>');">
      					<i class="fa fa-trash p-l-3"></i>Delete</span>
      				</td> 
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
<script>      
$(document).ready(function() {
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
   	                	filename: 'Form 2 Project Completion',
   	                	className: 'btn btn-view',
   	                	text	:'Download',
   	                	exportOptions: {
   	                    	columns: [ 0,1,2,3]
   	                	},
   	           		 }
	        	],
    	        columnDefs: [{ orderable: false, "targets": [0,1,4,5] },
   				  
   				 ],
    	        "order": [[ 2, 'asc' ]]
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