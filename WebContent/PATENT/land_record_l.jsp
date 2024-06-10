<%@page import="com.sits.patent.land_record.LandRecordModel"%>
<%@page import="com.sits.patent.land_record.LandRecordManager"%>
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
	<link href="../sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
	<script src="../js/jquery-3.2.1.js"></script>
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
	<script type="text/javascript" src="../js/patent/land-record.js"></script>
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	
</head>
<body>
<form name="landRecordFormL" id="landRecordFormL" action="" method="post" autocomplete="off" target="">
  <% 
	String x_patta="", pat_status="", fstatus="",x_location="",ld_id="",upl_doc="";
      ld_id=General.checknull(request.getParameter("ld_id"));
	  x_patta=General.checknull(request.getParameter("X_PATTA"));
	  x_location = General.checknull(request.getParameter("x_location"));
	//  System.out.println("L page x_location||"+x_location+"||x_mou_type||"+x_patta);
	ArrayList<LandRecordModel> arrayList=LandRecordManager.getList(ld_id,x_location,x_patta);
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
				<div class="col-sm-4 repTitle h5 ">List of MOU/MOA Form(s)</div>
				<div class="col-sm-4 repTitle h6 text-center">Run Date : <%=General.currdate_time() %>&nbsp;</div>
			</div>
    <table id="searchTable" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th class="text-center">S.No.</th>
					<th class="text-center">Location</th>
					<th class="text-center">Patta No</th>
					<th class="text-center">Remarks</th>
				<!-- 	<th class="text-center">Uploaded Document</th> -->
					<th class="text-center">Edit</th>
					<th class="text-center">Delete</th>
			</thead>	
			<tbody>
			<% 
			int i=0;
			if(arrayList.size()>0){
			for(LandRecordModel landlist: arrayList){
				upl_doc=General.checknull(landlist.getUploaded_file());
				ld_id=General.checknull(landlist.getLd_id());
			%>
                <tr>
					<td style="text-align:center; width:5%;"><%=++i %></td>
					<td style="text-align:center; width:15%;"><%=General.checknull(landlist.getLocation())%></td>
					<td style="text-align:center; width:15%;"><%=General.checknull(landlist.getPatta_no())%></td>
					<td style="text-align:center; width:10%;"><%=General.checknull(landlist.getOther())%></td>
					<%-- <td style="text-align:center;width:15%;">
					  <a href="../downloadfile?filename=<%=upl_doc %>&folderName=RSRCH/LAND_RECORD/<%=ld_id%>&fstatus=dwnFileFrmDir">
         			  <u><%=upl_doc %></u></a> --%>
					<td class="text-center colr-blue-p"  style="width:7%;">
						<span id="EDIT_RECORD_<%= i%>" onclick="editRecord('<%=ld_id%>','E','<%=x_location%>','<%=x_patta%>');">
	   				<i class="fa fa-edit p-l-3"></i>Edit</span>
	   				</td>
      				<td class="text-center colr-red-p" style="width:7%;">
      					<span id="DELETE_RECORD_<%= i%>"  onClick="deleteRecord('<%=ld_id%>');">
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
   	                	filename: 'Land Record Form',
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