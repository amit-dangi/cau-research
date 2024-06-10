<%@page import="com.sits.rsrch.research_activity.research_sub_head.ResearchSubHeadManager"%>
<%@page import="com.sits.rsrch.research_activity.research_sub_head.ResearchSubHeadModel"%>
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
	<script type="text/javascript" src="../js/research/research-sub-head.js"></script>
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<link href="../sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	
</head>
<body>
<form name="frmResSubHeadL" id="frmResSubHeadL" action="" method="post" autocomplete="off">
  <% 
	String headName="", status="", fstatus="", SubheadName="",locatSelL="",locatSelO="",locationTyp="";

  	SubheadName=General.checknull(request.getParameter("SubHeadName"));
	headName=General.checknull(request.getParameter("headName"));
	status=General.checknull(request.getParameter("status"));	
	locationTyp=General.checknull(request.getParameter("locationTyp"));	
	fstatus = General.checknull(request.getParameter("fstatus"));
	
	ArrayList<ResearchSubHeadModel> arrayList=ResearchSubHeadManager.getList(headName, SubheadName, status,locationTyp);
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
				<div class="col-sm-4 repTitle h5 ">List of <%=ReadProps.getkeyValue("res_head.header","sitsResource") %> (s)</div>
				<div class="col-sm-4 repTitle h6 text-center">Run Date : <%=General.currdate_time() %>&nbsp;</div>
			</div>
           <table id="searchTable" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th class="text-center">S.No.</th>
					<th class="text-center"> Head Name</th>
					<th class="text-center"> Sub Head Name</th>
					<th class="text-center">Status</th>					
					<th class="text-center">Edit</th>
					<th class="text-center">Delete</th>
			</thead>	
			<tbody>
			<% 
			int i=0;
			if(arrayList.size()>0){
			for(ResearchSubHeadModel bhm: arrayList){
				String val = "";
				String subHeadName =bhm.getSubHeadName();
				String locatSel = General.checknull(bhm.getLocation());
				
				if(General.checknull(bhm.getSubHeadType()).trim().equals("Y")){
					val = "Active";
				}else if(General.checknull(bhm.getSubHeadType()).trim().equals("N")){
					val = "In-Active";
				}
				if (locatSel.equals("L"))
			    	locatSelL = "Local";
			    if (locatSel.equals("O"))
					locatSelL = "Others";

			%>
		
                <tr>
					<td style="text-align:center; width:5%;"><%=++i %></td>
					<td style="text-align:center; width:15%;"><%=bhm.gethName()%></td>
					<% if(bhm.getSubHeadName().equals("") || bhm.getSubHeadName().equals(null) ){	 %>
					<td style="text-align:center; width:15%;"><%=locatSelL%></td>
					<%}else{ %>
					<td style="text-align:center; width:15%;"><%=bhm.getSubHeadName()%></td>
					<%} %>
					<td style="text-align:center; width:10%;"><%=val%></td>
					<td class="text-center colr-blue-p"  style="width:7%;">
					<span id="EDIT_RECORD_<%= i%>" onclick="editRecord('<%=bhm.getSubHeadId() %>','<%=bhm.gethId() %>','<%=bhm.getSubHeadName() %>','<%=bhm.getSubHeadType() %>','<%=bhm.getLocation()%>','<%=bhm.getLocation_name()%>','<%=fstatus %>')">
	   				<i class="fa fa-edit p-l-3"></i>Edit</span>
	   				</td>
      				<td class="text-center colr-red-p" style="width:7%;">
      				<span id="DELETE_RECORD_<%= i%>"  onClick="deleteRecord('<%=bhm.getSubHeadId() %>');">
      				<i class="fa fa-trash p-l-3"></i>Delete</span>
      				</td>	    
				 </tr>
			<%}}%>
			</tbody>
		</table>
		
		<input type="hidden" name="XhId" id="XhId" value="">
		<input type="hidden" name="XhName" id="XhName" value="">
		<input type="hidden" name="XSubhName" id="XSubhName" value="">
   		<input type="hidden" name="XhType" id="XhType" value=""> 
   		<input type="hidden" name="Xlocation" id="Xlocation" value=""> 
   		<input type="hidden" name="XlocationName" id="XlocationName" value=""> 		
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
   	                	filename: 'List of Create & Manage Research Head',
   	                	title:'',
   	                	className: 'btn btn-view',
   	                	text	:'Download',
   	                	exportOptions: {
   	                    	columns: [ 0,1,2,3]
   	                	},
   	           		 }
	        	],
    	        columnDefs: [{ orderable: false, "targets": [0,3,4,5] },
   				  
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