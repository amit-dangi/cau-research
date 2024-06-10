<%@page import="com.sits.rsrch.research_activity.objective_achievement_details.ObjectiveAchievementModel"%>
<%@page import="com.sits.rsrch.research_activity.objective_achievement_details.ObjectiveAchievementManager"%>
<%@page import="com.sits.general.General"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@include file="../myCks.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="-1" />
<link rel="stylesheet" href="../assets/sits/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css">
<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="../assets/sits/DataTable/1.10.15/css/dataTables.bootstrap.css"rel="stylesheet">
<link rel="stylesheet" href="../assets/plugins/datepicker/datepicker3.css" type="text/css">
<script type="text/javascript" src="../assets/plugins/datepicker/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="../js/aes/AesUtil.js"></script>
<script type="text/javascript" src="../js/aes/crypto.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/common/common-utilities.js"></script>
<script type="text/javascript" src="../js/common/validations.js"></script>
<script type="text/javascript" src="../js/gen.js"></script>
 <script type="text/javascript" src="../js/research/objective-achievement-details.js"></script>

	<%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
	%>
<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<%
		ArrayList<ObjectiveAchievementModel> list = null;
		int i = 0;
		String x_research = General.checknull(request.getParameter("x_research"));
		String x_fdate = General.checknull(request.getParameter("x_fdate"));
		String x_tdate = General.checknull(request.getParameter("x_tdate"));

		// System.out.println("user_id-- L :"+user_id);
	%>
	<form name="frmObjAchievementL" id="frmObjAchievementL" action="" method="post" autocomplete="off">
		<div id="searchingTable1" class=" scr0">
			<div class="col-sm-12">
				<div class="row text-center">
					<div class="errmessage1" id="errMsg1"></div>
				</div>
			</div>
			<div class="page-header-1">
				<div class="col-sm-4 repTitle ">List of Objective and Achievement Detail(s)</div>
				<div class="col-sm-4 repTitle text-center">
					Run Date :
					<%=General.currdate_time()%>&nbsp;
				</div>
			</div>
			<table id="searchTable"
				class="table table-striped table-bordered table-hover display nowrap"
				cellspacing="0" width="100%">
				<thead class="theadnew">
					<tr>
						<th class="text-center">S.No.</th>
						<th class="text-center">Research Proposal</th>
						<th class="text-center">From Date</th>
						<th class="text-center">To Date</th>
						<th class="text-center" style=display:none;">Achievement Details</th>
						<th class="text-center">Edit</th>
						<th class="text-center">Delete</th>
					</tr>
				</thead>
				<%
					 String active = "";
					ObjectiveAchievementModel mm = new ObjectiveAchievementModel();
					mm.setRsrch_proposal_id(x_research);
					mm.setFrom_date(x_fdate);
					mm.setTo_date(x_tdate);

					try {
						list = ObjectiveAchievementManager.searchRecord(mm,user_id);
						if (list.size() > 0) {
							Iterator itr = list.iterator();
							while (itr.hasNext()) {
								ObjectiveAchievementModel mdl = (ObjectiveAchievementModel) itr.next();
								++i; 
								String A=mdl.getUpl_doc();
				%>
				<tr>
					<td style="text-align: center; width: 3%"><%=i %></td>
					<td style="text-align: center; width: 30%"><%=mdl.getRsrch_proposal() %></td>
					<td style="text-align: center; width: 10%"><%=mdl.getFrom_date() %></td>
					<td style="text-align: center; width: 10%"><%=mdl.getTo_date() %></td>
					<td style="text-align: center; width: 10% ; display:none; "><%=mdl.getAchievement_det() %></td> 
					<td class="colr-blue-p text-center" style="width: 6%; color: blue;"><span id="EDIT_RECORD_<%=i%>"onClick="editRow('<%=mdl.getOa_id()%>','E','<%=mdl.getRsrch_proposal_id()%>')"><i class="fa fa-edit p-l-3"></i>Edit</span></td>
					<td class="colr-red-p text-center" style="width: 6%; color: red;"><span id="DELETE_RECORD_<%=i%>"onClick="editRow('<%=mdl.getOa_id()%>','D','','<%=mdl.getUpl_doc()%>')"><i class="fa fa-trash p-l-3"></i>Delete</span></td>
				</tr>
				<% 
					}

						}
					} catch (Exception e) {
						System.out.println("Error in printing_course_material_l.jsp" + e.getMessage());
					} finally {
						list.clear();
					} 
				%>
			</table>
		</div>
		<input type="hidden" name="oa_id" id="oa_id" style="width: 0px;" readOnly />
		<input type="hidden" name="opt_typ" id="opt_typ" style="width: 0px;"readOnly />
		<input type="hidden" name="r_id" id="r_id" style="width: 0px;" readOnly />


	</form>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="../assets/sits/DataTable/1.10.15/js/jquery.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.js"></script>
	<script src="../assets/sits/DataTable/1.10.15/js/jquery.dataTables.js"></script>
	<script src="../assets/sits/DataTable/1.10.15/js/dataTables.bootstrap.js"></script>
	<script src="../assets/sits/DataTable/1.4.2/js/buttons.flash.min.js"></script>
	<script src="../assets/sits/DataTable/1.4.2/js/buttons.html5.min.js"></script>
	<script src="../assets/sits/DataTable/1.4.2/js/buttons.print.min.js"></script>
	<script src="../assets/sits/DataTable/1.4.2/js/dataTables.buttons.min.js"></script>
	<script src="../assets/sits/DataTable/1.4.2/js/vfs_fonts.js"></script>
	<script src="../assets/sits/DataTable/1.4.2/js/jszip.min.js"></script>
	<script>
		var t = $('#searchTable')
				.DataTable(
						{
							"searching" : false,
							"lengthMenu" : [ [ -1, 10, 25, 50, 100, 250, 500 ],
									[ 'All', 10, 25, 50, 100, 250, 500 ] ],
							//"scrollY": "200px",
							"scrollX" : true,
							"scrollCollapse" : true,
							"paging" : true,

							"columnDefs" : [ {
								"searchable" : false,
								"orderable" : false,
								"targets" : 0
							} ],
							"order" : [ [ 1, 'asc' ] ],

							dom : "<'row'<'col-sm-4 text-left'.h5><'col-sm-4 text-center'.h6> <'col-sm-4 text-right'B>>"
									+ "<'row'<'col-sm-6'l><'col-sm-6'>>"
									+ "<'row'<'col-sm-12'tr>>"
									+ "<'row'<'col-sm-5'i><'col-sm-7'p>>",
							buttons : [ {
								extend : 'excelHtml5',
								title : 'Central Agricultural University',
								filename : 'Objective Achievement Details',
								className : 'btn btn-view',
								text : 'Download',
								exportOptions : {
									columns : [ 0, 1 ,2,3,4]
								},
							}, ],
							columnDefs : [ {
								orderable : false,
								"targets" : [ 0,2,3,4,5 ]
							} ],
						});
		t.on('order.dt search.dt', function() {
			t.column(0, {
				search : 'applied',
				order : 'applied'
			}).nodes().each(function(cell, i) {
				cell.innerHTML = i + 1;
				t.cell(cell).invalidate('dom');
			});
		}).draw();
	</script>

	<form name="actionForm" id="actionForm" method="post" action="" target="_parent">
	<input type="hidden" id="id" name="id" value=""> <input type="hidden" id="fstatus" name="fstatus" />
	</form>
	<iframe class="embed-responsive-item"name="btmfrmPrintingCourseMastReppdf"id="btmfrmPrintingCourseMastReppdf" onload="resizeIframe(this)" src="" frameborder="0" scrolling="no" width="100%" height=""></iframe>

</body>
</html>