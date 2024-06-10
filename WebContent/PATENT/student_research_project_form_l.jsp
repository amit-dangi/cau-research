<%@page import="com.sits.rsrch.student_research_project_form.*"%>
<%@include file="../myCks.jsp"%>
<%@ page import="java.util.ArrayList" %>
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
	    <script type="text/javascript" src="../js/patent/student_research_project_form.js"></script>
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<link href="../sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	
</head>
<body>
<form name="mouMoaFormL" id="mouMoaFormL" action="" method="post" autocomplete="off">
  <% 
	String x_mou_type="", x_ICAR_USID="",x_ddo="",x_stu_name="",pat_status="", fstatus="",x_location="",m_id="";
	  m_id=General.checknull(request.getParameter("m_id"));
	  x_mou_type=General.checknull(request.getParameter("x_mou_type"));
	  x_location = General.checknull(request.getParameter("x_location"));
	  x_ICAR_USID = General.checknull(request.getParameter("x_ICAR_USID"));
	  x_ddo = General.checknull(request.getParameter("x_ddo"));
	  x_stu_name = General.checknull(request.getParameter("x_stu_name"));
	  //System.out.println("L page x_location||"+x_location+"||x_ICAR_USID||"+x_mou_type);
	ArrayList<StudentResearchProjectFormModel> arrayList=StudentResearchProjectFormManager.getList(m_id,x_location,x_ICAR_USID,x_ddo,x_stu_name,user_status,user_id);
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
				<div class="col-sm-4 repTitle h5 ">List of Students Research Project</div>
				<div class="col-sm-4 repTitle h6 text-center">Run Date : <%=General.currdate_time() %>&nbsp;</div>
			</div>
    <table id="searchTable" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th class="text-center">S.No.</th>
					<th class="text-center">Location</th>
					<th class="text-center">Student Name</th>
					<th class="text-center">ICAR USID</th>
					<th class="text-center">CAU Registration no.</th>
					<th class="text-center">Edit</th>
					<th class="text-center">Delete</th>
			</thead>	
			<tbody>
			<% 
			int i=0;
			if(arrayList.size()>0){
			for(StudentResearchProjectFormModel moulist: arrayList){
			%>
                <tr>
					<td style="text-align:center; width:5%;"><%=++i %></td>
					<td style="text-align:center; width:15%;"><%=General.checknull(moulist.getLocation())%></td>
					<td style="text-align:center; width:10%;"><%=General.checknull(moulist.getStu_name())%></td>
					<td style="text-align:center; width:15%;"><%=General.checknull(moulist.getICAR_USID())%></td>
					<td style="text-align:center; width:10%;"><%=General.checknull(moulist.getCau_regno())%></td>
					<td class="text-center colr-blue-p"  style="width:7%;">
						<span id="EDIT_RECORD_<%= i%>" onclick="editRecord('<%=moulist.getM_id()%>','E','<%=x_location%>');">
	   				<i class="fa fa-edit p-l-3"></i>Edit</span>
	   				</td>
      				<td class="text-center colr-red-p" style="width:7%;">
      					<span id="DELETE_RECORD_<%= i%>"  onClick="deleteRecord('<%=moulist.getM_id()%>');">
      					<i class="fa fa-trash p-l-3"></i>Delete</span>
      				</td>	    
				 </tr>
			<%}}%>
			</tbody>
		</table>
		
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
    	    	"searching" : false,
				//"scrollY": "500px",
				"ordering": false,
				"scrollX": true,
				"scrollCollapse": true,
				"paging": true,
				//"lengthMenu": [[10, 25, 50,100,200,500, -1], [10, 25, 50,100,200,500, "All"]],
				"lengthMenu": [[ -1,10, 25, 50, 100, 250, 500], ['All',10, 25, 50, 100, 250, 500]],
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
   	                	filename: 'MOU/MOA Form',
   	                	title: 'Central Agricultural University',
   	                	className: 'btn btn-view',
   	                	text	:'Download',
   	                	exportOptions: {
   	                    	columns: [ 0,1,2,3,4]
   	                	},
   	           		 }
	        	],
    	        columnDefs: [{ orderable: false, "targets": [0,3,4] },
   				  
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