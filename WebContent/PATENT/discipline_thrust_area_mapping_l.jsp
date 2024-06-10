 <%@page import="com.sits.patent.discipline_thrust_area_mapping.DisciplineThrustAreaMappingManager"%>
<%@page import="com.sits.patent.discipline_thrust_area_mapping.DisciplineThrustAreaMappingModel"%>
<%@page import="com.sits.general.General"%>
<%@page import="java.util.ArrayList"%>
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
  
  <link rel="stylesheet" href="../assets/sits/bootstrap/3.3.7/css/bootstrap.css"  type="text/css">
  <link href="../assets/sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet"> 
  <%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
	%>
  
  <script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
    <script type="text/javascript" src="../js/common/common-utilities.js"></script>
  <script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/patent/discipline-thrust-area-mapping.js"></script>
	
  <link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
  <link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
  <link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
  <link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
 
</head>
 <body>
 <% 
 String act="";
ArrayList<DisciplineThrustAreaMappingModel> arrayList=null;
String discipline                      =General.checknull(request.getParameter("xdiscipline"));
String thrust_area                    =General.checknull(request.getParameter("xthrust_area"));
String sub_thrust_area                       =General.checknull(request.getParameter("xsub_thrust_area"));
 DisciplineThrustAreaMappingModel model=new DisciplineThrustAreaMappingModel();
model.setDiscipline(discipline);
model.setThrust_area(thrust_area);
model.setSub_thrust_area(sub_thrust_area);

arrayList=DisciplineThrustAreaMappingManager.getList(model); 

%>
 
	<form name="DisciplineThrustAreaL" id="DisciplineThrustAreaL" action="" method="post" autocomlete="off">
	<div id="searchingTable" class=" scr0">
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
		    <div class="col-sm-4 repTitle ">List of Discipline (s)</div>
		    <div class="col-sm-4 repTitle text-center">Run Date :  <%=General.currdate_time() %></div>	
	   </div>
    
	<div class="responsive">
	<table id="searchTable" class="table table-striped table-bordered table-hover display nowrap" cellspacing="0" width="100%">
			<thead class="theadnew" >
				<tr>
					<th class="text-center">S.No.</th>
					<th class="text-center">Discipline</th>
					<th class="text-center">Thrust Area</th>
					<th class="text-center">Sub Thrust Area</th>
					<th class="text-center">Edit</th>
					<th class="text-center">Delete</th>
				</tr>
			</thead>	
			<tbody>
			 <% 
	int i=0;
	if(arrayList.size()>0){
	for(DisciplineThrustAreaMappingModel model1: arrayList){
		i++;
		%>
			
				 <tr>
					  <td style="text-align:center;width:5%;"><%= i%></td>
				     <td style="text-align:center;width:15%;"><%= model1.getDiscipline()%></td>
				     <td style="text-align:center;width:15%;"><%= model1.getThrust_area()%></td>
					 <td style="text-align:center;width:15%;"><%= model1.getSub_thrust_area()%></td>
					 <td style="text-align:center;width:10%;"><a href="#"  id="EDIT_RECORD_" class="" style="color:blue;cursor: pointer;width:20%" onclick="editRecord('<%=model1.getId()%>','E')"><i class="fa fa-edit"></i>Edit</a></td>
		             <td style="text-align:center;width:10%;"><a href="#"  id="DELETE_RECORD_" class="" style="color:red;cursor: pointer;width:20%" onclick="editRecord('<%=model1.getId()%>','D')"><i class="fa fa-trash"></i>Delete</a></td> 
				 </tr>  
				 <%} 
	}%>  
			
			</tbody>
	</table>
	</div>
	<input type="hidden" name="cr_id" id="cr_id" style="width:0px;" readOnly/>
    <input type="hidden" name="opt_typ" id="opt_typ" style="width:0px;" readOnly/>
	</div>  
	 
	
	
	<script src="../assets/sits/DataTable/1.10.15/js/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.js"></script>
<script src="../assets/sits/DataTable/1.10.15/js/jquery.dataTables.js"></script>
<script src="../assets/sits/DataTable/1.10.15/js/dataTables.bootstrap.js"></script>
<script src="../assets/sits/DataTable/1.4.2/js/buttons.flash.min.js"></script>
<script src="../assets/sits/DataTable/1.4.2/js/buttons.modell5.min.js"></script>
<script src="../assets/sits/DataTable/1.4.2/js/buttons.print.min.js"></script>
<script src="../assets/sits/DataTable/1.4.2/js/dataTables.buttons.min.js"></script>
<script src="../assets/sits/DataTable/1.4.2/js/vfs_fonts.js"></script> 
<script src="../assets/sits/DataTable/1.4.2/js/jszip.min.js"></script>
	
	<script>
		       var t = $('#searchTable').DataTable({
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
				extend: 'excelmodell5',
				filename: 'discipline_master',
				className: 'btn btn-view',
				text	:'Download',
				title:  'Dr.B.R. Ambedkar Open University',
				exportOptions: {
				columns: [ 0,1,2,3,4]
				},
				},
				],                      	       	
			     	columnDefs: [
			     		{ orderable: false, "targets": [0,4,5,6]} 
								],
					 }); 
			        t.on( 'order.dt search.dt', function () {
			     	t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
			         cell.innermodelL = i+1;
			         t.cell(cell).invalidate('dom');
			     	});
			 	}).draw();
	    </script>   
	   </form>
	   
	    <form name="edit" id="edit" method="post" >
	   <input type="hidden" name="totRow" id="totRow" value="${count}" style="width:10px;" readOnly/>
	    <input type="hidden" id="CR_ID" name="CR_ID"  value=""/> 
	   </form>
	   
	   
	   
	</body>
</modell> 