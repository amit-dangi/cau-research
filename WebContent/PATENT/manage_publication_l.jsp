 <%@page import="com.sits.patent.manage_publication.ManagePublicationManager"%>
<%@page import="com.sits.patent.manage_publication.ManagePublicationModel"%>
<%@page import="com.sits.general.General"%>
<%@page import="java.util.ArrayList"%>
<%@include file="../myCks.jsp"%>
<%-- <%@page import="com.sits.examination.common_master.discipline_course_master.CourseMasterManager"%>
<%@page import="com.sits.examination.common_master.discipline_course_master.CourseMasterModel"%> --%>
<%@page import="java.util.Iterator"%>
<!DOCTYPE modell> 
<modell lang="en">
<head>
<meta  name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' />
<meta http-equiv="Content-Type" content="text/modell; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="-1" />
 
  <%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
	%>
 
 <link href="../bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<script src="../js/jquery-3.2.1.js"></script>
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
	  <script type="text/javascript" src="../js/patent/manage-publication.js"></script>
	
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<link href="../sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	
 
 
</head>
 <body>
 <% 
 String act="";
ArrayList<ManagePublicationModel> arrayList=null;
String ddo                      	=General.checknull(request.getParameter("ddo"));
String location                     =General.checknull(request.getParameter("location"));
String pub_id                       =General.checknull(request.getParameter("Xpublication"));
 //System.out.println("ddo"+ddo+location+"pub_id"+pub_id);
ManagePublicationModel model=new ManagePublicationModel();
model.setDDO_ID(ddo);
model.setLOCATION_CODE(location);
model.setPub_id(pub_id);

arrayList=ManagePublicationManager.getList(model,user_status,user_id); 

%>
 
	<form name="ManagePublicationL" id="ManagePublicationL" action="" method="post" autocomlete="off">
	<div id="searchingTable1" class=" scr0">
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
		    <div class="col-sm-4 repTitle ">List of Publication(s)</div>
		    <div class="col-sm-4 repTitle text-center">Run Date :  <%=General.currdate_time() %></div>	
	   </div>
    
	<!-- <div class="responsive"> -->
	<table id="searchTable" class="table table-striped table-bordered table-hover display nowrap" cellspacing="0" width="100%">
			<thead class="theadnew" >
				<tr>
					<th class="text-center">S.No.</th>
					<th class="text-center">Location</th>
					<th class="text-center">Ddo</th>
					<th class="text-center">Publications</th>
					<th class="text-center">Edit</th>
					<th class="text-center">Delete</th>
				</tr>
			</thead>	
			<tbody>
			 <% 
	int i=0;
	if(arrayList.size()>0){
	for(ManagePublicationModel model1: arrayList){
		i++;
		%>
			
				 <tr>
					  <td style="text-align:center;width:5%;"><%= i%></td>
				     <td style="text-align:center;width:15%;"><%= model1.getLOCATION_CODE()%></td>
				     <td style="text-align:center;width:15%;"><%= model1.getDDO_ID()%></td>
					 <td style="text-align:center;width:15%;"><%= model1.getPub_id()%></td>
					 <td style="text-align:center;width:10%;"><a href="#"  id="EDIT_RECORD_" class="" style="color:blue;cursor: pointer;width:20%" onclick="editRecord('<%=model1.getId()%>','E')"><i class="fa fa-edit"></i>Edit</a></td>
		             <td style="text-align:center;width:10%;"><a href="#"  id="DELETE_RECORD_" class="" style="color:red;cursor: pointer;width:20%" onclick="editRecord('<%=model1.getId()%>','D')"><i class="fa fa-trash"></i>Delete</a></td> 
				 </tr>  
				 <%} 
	}%>  
			
			</tbody>
	</table>
	</div>
	<!-- </div>   -->
	<input type="hidden" name="id" id="id" style="width:0px;" readOnly/>
    <input type="hidden" name="opt_typ" id="opt_typ" style="width:0px;" readOnly/>
	
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
   	                	filename: 'Manage Publication',
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
	 
	   
	    <form name="edit" id="edit" method="post" >
	   <input type="hidden" name="totRow" id="totRow" value="${count}" style="width:10px;" readOnly/>
	    <input type="hidden" id="id" name="id"  value=""/> 
	   </form>
	</body>
</modell> 