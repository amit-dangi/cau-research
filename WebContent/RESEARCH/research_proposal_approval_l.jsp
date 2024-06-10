<%@include file="../myCks.jsp"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.sits.rsrch.research_proposal_approval.ResearchProposalApprovalModel" %>
<%@ page import="com.sits.rsrch.research_proposal_approval.ResearchProposalApprovalManager" %>
<!DOCTYPE html> 
<html lang="en">
<head>
    <meta  name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="-1" />

	<link href="../bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/aes/AesUtil.js"></script>
	<script type="text/javascript" src="../js/aes/crypto.js"></script>
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
	<script type="text/javascript" src="../js/research/research-proposal-approval.js"></script>
	
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<link href="../sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<style>
 .fade{background: #ffffff;}
 .point{cursor: pointer; color:#ec6c10; }
 .point:hover {
  color: blue;
  text-decoration:underline;
}
</style>
</head>
<body>
<form name="frmRsrchPropAppL" id="frmRsrchPropAppL" class="frmRsrchPropAppLT" action="" method="post" autocomplete="off">
  <% 
	String XFROMDATES="", XTODATES="", statusS="", fstatus="", pageType="", pageName="";
  	int i=0;
  	XFROMDATES=General.checknull(request.getParameter("XFROMDATES"));
	XTODATES=General.checknull(request.getParameter("XTODATES"));
	statusS=General.checknull(request.getParameter("statusS"));	
	/* if(statusS.equals("")){
		statusS="A";
	} */
	fstatus = General.checknull(request.getParameter("fstatus"));
	pageType = General.checknull(request.getParameter("pageType"));
	loct_id = General.checknull(request.getParameter("Xlocation"));
	ddo_id = General.checknull(request.getParameter("Xddo"));
	ArrayList<ResearchProposalApprovalModel> arrayList= new ArrayList<ResearchProposalApprovalModel>();  
	ArrayList<ResearchProposalApprovalModel> arrayList1= new ArrayList<ResearchProposalApprovalModel>();
	ArrayList<String> arrayList2= new ArrayList<String>();
	
	if(pageType.trim().equals("HD")){
		pageName="(HOD/DEAN)";
		//if(General.checknull(is_dept_head).trim().equals("Y")){ Commented due to this page will be used only for hod user wise as suggested by amit kumar
			arrayList=ResearchProposalApprovalManager.getList(XFROMDATES, XTODATES, statusS, pageType, dept_id,loct_id,ddo_id);
			arrayList2=ResearchProposalApprovalManager.getList1(XFROMDATES, XTODATES, statusS, pageType);	
		//}
	}else if(pageType.trim().equals("RP")){
		pageName="RP Cell";
		arrayList=ResearchProposalApprovalManager.getList(XFROMDATES, XTODATES, statusS, pageType, "",loct_id,ddo_id);
		arrayList2=ResearchProposalApprovalManager.getList1(XFROMDATES, XTODATES, statusS, pageType);		
	}else if(pageType.trim().equals("RR")){
		pageName="Registrar";
		arrayList=ResearchProposalApprovalManager.getList(XFROMDATES, XTODATES, statusS, pageType, "",loct_id,ddo_id);
		arrayList2=ResearchProposalApprovalManager.getList1(XFROMDATES, XTODATES, statusS, pageType);	
	}else if(pageType.trim().equals("VC")){
		pageName="VC";
		arrayList=ResearchProposalApprovalManager.getList(XFROMDATES, XTODATES, statusS, pageType, "",loct_id,ddo_id);
		arrayList2=ResearchProposalApprovalManager.getList1(XFROMDATES, XTODATES, statusS, pageType);
	}else if(pageType.trim().equals("DDR")){
		pageName="DDR";
		arrayList=ResearchProposalApprovalManager.getList(XFROMDATES, XTODATES, statusS, pageType, "",loct_id,ddo_id);
		arrayList2=ResearchProposalApprovalManager.getList1(XFROMDATES, XTODATES, statusS, pageType);
	}
	else{
		pageName="";
	}
	
%>  
  <div id="searchingTable1" class="scr0">
				<!-- <div class="col-sm-12">
					<div class="row text-center">
						<div class="errmessage1" id="errMsg1"></div>
					</div>
				</div>
				<div class="col-sm-12">
					<div class="row text-center">
						<div class="errmessage2" id="errMsg2"></div>
					</div>
				</div> -->
			<div class="page-header-1">
				<div class="col-sm-4 repTitle h5 ">List of <%=ReadProps.getkeyValue("res_prop_app.header","sitsResource") %> (s)</div>
				<div class="col-sm-4 repTitle h6 text-center">Run Date : <%=General.currdate_time() %>&nbsp;</div>
			</div>
    <table id="searchTable" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th class="text-center">S.No.</th>
					<th class="text-center"><input type="checkbox" class="" id="checkAll" style="margin:0px;"> All</th>
					<!-- <th class="text-center">Location Name</th>
					<th class="text-center">DDO Name</th> -->
					<th class="text-center">Project Proposal Id</th>
					<th class="text-center">Title of Project Proposal</th>
					<th class="text-center">Proposal Submission Date</th>
					<th class="text-center">View Form</th>
					<th class="text-center">Status</th>
					<th class="text-center">Approve/ Reject Date</th>
					<th class="text-center">Reason</th>
					<%if(pageType.trim().equals("RR")){%> 
						<th class="text-center">Is VC Approval Required</th>
					<%} %>
			</thead>	
			<tbody>
			<%
			if(arrayList.size()>0){
				int a=0;
			for(ResearchProposalApprovalModel bhm: arrayList){
				a=++i;
			%>
			
			<% if(arrayList2.contains(bhm.getPsId())){
				arrayList1=ResearchProposalApprovalManager.getList2(bhm.getPsId(), pageType);
				
				String selP="", selR="", selA="", val = arrayList1.get(0).getStatus(), chk="",selDisabled="disabled";
				if(val.equals("P")){
					selP="selected";
					selDisabled="";
				}else if(val.equals("R")){
					selR="selected";
				}else{
					selA="selected";
				}
				
				if(arrayList1.get(0).getIsAppReq().equals("Y")){
					chk="checked";
				}else{
					chk="";
				}
				
			%>
				<tr>
					<td style="text-align:center; width:3%;"><%=a %></td>
					<td style="text-align:center;width:5%;">
					<%if(val.equals("P")){%>
					<input type="checkbox" class="checkBoxClass" name="chkBox" id="chkBox_<%=a %>">
					<%} %>
					</td>
					<td style="text-align:center;width:8%;"><%=bhm.getPpId() %></td>
					<input type="hidden" name="psId_<%=a%>" id="psId_<%=a%>" value="<%=bhm.getPsId()%>"/>
						<input type="hidden" name="ppId_<%=a%>" id="ppId_<%=a%>" value="<%=bhm.getPpId()%>"/>
						<input type="hidden" name="previousstatus_<%=a%>" id="previousstatus_<%=a%>" value="<%=val%>"/>
						
					<td style="text-align:center;width:10%;" id="tittle_<%=a%>"><%=bhm.getTitle_pp() %></td>
					<td style="text-align:center;width:10%;" id="psDt_<%=a%>"><%=bhm.getPs_date() %></td>
					<%-- <span onclick="editRecord('<%=bhm.getPsId()%>', '<%=pageType%>')"><i class="fa fa-address-card-o p-l-3"></i>View Form</span> --%>
					<%-- <td class="text-center colr-blue-p"  style="width:6%;">						
						<div id="" class="recommend-btn" onclick="OpenModel('<%=bhm.getPsId()%>', '<%=pageType%>');"><span class="point">View Form</span></div>
					</td>
					 --%>
					<td class="text-center colr-blue-p"  style="width:6%;">
						<% if(pageType.trim().equals("1HD")){%>			
							<div id="" class="recommend-btn" onclick="OpenModelHD('<%= bhm.getPsId()%>');">
								<span class="point">View Form</span>
							</div>
						<% }else if(pageType.trim().equals("1RP")){%>
							<div id="" class="recommend-btn" onclick="OpenModelRP('<%= bhm.getPsId()%>');">
								<span class="point">View Form</span>
							</div>	
						<% }else if(pageType.trim().equals("1DDR")){%>
							<div id="" class="recommend-btn" onclick="OpenModelRR('<%= bhm.getPsId()%>');">
								<span class="point">View Form</span>
							</div>	
						<% }else if(pageType.trim().equals("1RR")){%>
						<div id="" class="recommend-btn" onclick="OpenModelRR('<%= bhm.getPsId()%>');">
						<span class="point">View Form</span>
						</div>	
						<% }else if(pageType.trim().equals("1VC")){%>
							<div id="" class="recommend-btn" onclick="OpenModelVC('<%= bhm.getPsId()%>');">
								<span class="point">View Form</span>
							</div>
						<% }
						else{%>
							<div id="" class="recommend-btn" onclick="OpenModel('<%= bhm.getPsId()%>');">
								<span class="point">View Form</span>
							</div>					
						<% }%>
					</td>
					
					<td style="text-align:center;width:6%;">
						<select class="form-control" id="status_<%=a%>" name="status_<%=a%>" onchange="text('<%=a%>');" <%=selDisabled%>>
							<option value="">Select Status</option>
							<option <%=selP%> value="P">Pending</option>
							<option <%=selA%> value="A">Approved</option>
							<option <%=selR%> value="R">Reject</option>
						</select>
					</td>
					<td style="text-align:center;width:8%;"><%=arrayList1.get(0).getAppRejDt()%></td>
					<td style="text-align:center;width:12%;">
						<textarea id="txtArea_<%=a%>" name="txtArea_<%=a%>" style="height:50px ;width:100%;" maxlength="254" disabled="disabled"><%=General.checknull(arrayList1.get(0).getReason())%></textarea>
					</td>
					<%if(pageType.trim().equals("RR")){%> 
						<td style="text-align:center;width:5%;">
							<input type="checkbox" class="" name="appReq" id="appReq_<%=a %>" <%=chk %> disabled="disabled">
						</td>
					<%} %>
				 </tr>
               
				 <%}else{%> 
					  <tr>
					<td style="text-align:center; width:3%;"><%=a %></td>
					<td style="text-align:center;width:5%;"><input type="checkbox" class="checkBoxClass" name="chkBox" id="chkBox_<%=a %>"></td>
					
					<td style="text-align:center;width:8%;">
						<input type="hidden" name="psId_<%=a%>" id="psId_<%=a%>" value="<%=bhm.getPsId()%>"/>
						<input type="hidden" name="ppId_<%=a%>" id="ppId_<%=a%>" value="<%=bhm.getPpId()%>"/>
						<input type="hidden" name="previousstatus_<%=a%>" id="previousstatus_<%=a%>" value=""/>
						<%=bhm.getPpId() %>
					</td>
					<td style="text-align:center;width:10%;" id="tittle_<%=a%>"><%=bhm.getTitle_pp() %></td>
					<td style="text-align:center;width:10%;" id="psDt_<%=a%>"><%=bhm.getPs_date() %></td>
					<td class="text-center colr-blue-p"  style="width:6%;">
						 <div id="" class="recommend-btn" onclick="OpenModel('<%= bhm.getPsId()%>');"><span class="point">View Form</span></div> 
					</td>
					
					<td style="text-align:center;width:6%;">
						<select class="form-control" id="status_<%=a%>" name="status_<%=a%>" onchange="text('<%=a%>');">
							<option value="">Select Status</option>
							<option value="P">Pending</option>
							<option value="A">Approved</option>
							<option value="R">Reject</option>
						</select>
					</td>
					<td style="text-align:center;width:8%;"></td>
					
					<td style="text-align:center;width:12%;">
						<textarea id="txtArea_<%=a%>" name="txtArea_<%=a%>" style="height:50px ;width:100%;" maxlength="254" ></textarea>
					</td>
					<%if(pageType.trim().equals("RR")){%> 
						<td style="text-align:center;width:5%;">
							<input type="checkbox" class="" name="appReq" id="appReq_<%=a %>" style="display: show();">
						</td>
					<%} %>
				 </tr> 
					<%}}}%>
			</tbody>
		</table> 
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
	<div class="col-md-12 text-center m-b-15" style="padding-bottom:20px;">
		<button type="button" class="btn btn-view" id="btnSubmit">Submit</button> 
	</div>
	
	<input type="hidden" name="totRow" id="totRow" value="<%=i%>" style="width:10px;" readOnly/>
	<input type="hidden" name="pTyp" id="pTyp" value="<%=pageType%>" style="width:10px;" readOnly/>
	<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
</form>
 
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
		dom :"<'row'<'col-sm-4 text-left'.h5><'col-sm-4 text-center'.h6> <'col-sm-4 text-right'B>>" 
			+"<'row'<'col-sm-6'l><'col-sm-6'>>" 
			+"<'row'<'col-sm-12'tr>>" 
			+"<'row'<'col-sm-5'i><'col-sm-7'p>>",   
		buttons: [{
			extend		: 'excelHtml5',
			title		: 'List of Research Proposal Approval',
			filename	: 'Research Proposal Approval',
			className	: 'btn btn-view',
			text		:'Download',
			exportOptions: {
               	columns: [ 0,2,3,4,5,6,7,8]
           	},
		}],
		columnDefs: [{ orderable: false, 
			"targets"	: [0,2,8]},],
			"order"		: [[ 2, 'asc' ]]
		} );
		
		t.on( 'order.dt search.dt', function () {
			t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
				cell.innerHTML = i+1;
				t.cell(cell).invalidate('dom');
			});
		}).draw();
});     

</script>
</body>
</html>