<%@include file="../myCks.jsp"%>
<%@ page import="java.util.ArrayList" %>
<%@page import="com.sits.general.ApplicationConstants"%>
<%@page import="com.sits.general.QueryUtil"%>
<%@ page import="com.sits.rsrch.form1_project_submission.*" %>
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
 	<link href="../assets/sits/bootstrap/3.3.7/css/bootstrap.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
	<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/aes/AesUtil.js"></script>
	<script type="text/javascript" src="../js/aes/crypto.js"></script>	
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
    <script type="text/javascript" src="../js/research/form1-project-sub.js"></script>
    <script type="text/javascript" src="../js/commonDropDown.js"></script>
	 
	<%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
	%>
	<link href="../assets/sits/DataTable/1.10.15/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
  	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="../css/datepicker/datepicker3.css"  type="text/css">
	<script src="../js/datepicker/bootstrap-datepicker.js"></script>
	     <script>
 	$(document).ready(function() {
 	$("a").tooltip();
 	});
 	</script>   	
	
		<style>
  	table.tableGrid th { background-color: #bb9c2b !important;
	 /*    border: 1px solid #1ea2cd !important; */
	    color: #0c426f;
     }
     table.tableGrid th {
    background-color:#bb9c2b !important;
    border: 1px solid #439653 !important;
    color: #0c426f;
     .close {
    	opacity: 1 !important;
    	color: red !important;
	 }
  	.previewbtn{color: #ffffff;
    background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#1e4636), color-stop(100%,#c3cbc8));
    box-shadow: 2px 2px 1px #07680a;}
  
	</style>
</head>
<%			
	int k=0;
	String fstatus = General.checknull(request.getParameter("fstatus"));
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	String OPT_VALUE = "", f_agn="", ProjPropSub="", NecClearObt="", FinCommitUni="", AttchCertif="", 
			ProjPropClear="", Dept="", Desg="", piName="", cnfrm="", ProjPropsal="", DurPropProj="", 
			TotalBudgProp="", NameAddrCoPi="", ProjPropSubC="", NecClearObtC="", FinCommitUniC="", 
			AttchCertifC="", ProjPropClearC="", date="",str_date="", NonRecCost="", ChemAndCon="", Manpower="", 
			Contingency="", Travel="", OutSourcingCharge="", OverCharg="", id="", ProjPropsalId="", 
			FinCommitUniDetails="", pageType="", dis="" , locationCode=""  , ddoCode="" ,disabled="",
			projPropsalIdManual="",durPropProjYear="",projtype="",erptype="",projterm="",IsApprovalReq="",
			erptypediv="none",fn_agency="",proj_obj="",thrust_area="",sub_thrust_area="",retire_ps_pri="",
			budget_head="",inst_charges="",projunder="";
	
	ArrayList<ProjectSubmissionModel> arrayList = new ArrayList<ProjectSubmissionModel>();  
	ArrayList<ProjectSubmissionModel> fileList = new ArrayList<ProjectSubmissionModel>();
	ArrayList<ProjectSubmissionModel> extList = new ArrayList<ProjectSubmissionModel>(); 
	
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
		ProjPropsalId = ProjectSubmissionManager.getSequenceNoforChallan("RSRCH_FORM1_MAST", "N", "V");
	}else if (fstatus.trim().equals("E") || fstatus.trim().equals("R")){
		pageType=General.checknull(request.getParameter("pageType"));
		OPT_VALUE = ApplicationConstants.EDIT;
		id=General.checknull(request.getParameter("id"));

		arrayList = ProjectSubmissionManager.getEditList(id);
		fileList = ProjectSubmissionManager.getEditFileList(id);
		
		//Mehod for find the project extended details
		extList = ProjectSubmissionManager.getProjExtList(id);

		if(General.checknull(arrayList.get(0).getProjPropSub()).equals("Y")){
			ProjPropSubC="checked";
		}
		if(General.checknull(arrayList.get(0).getNecClearObt()).equals("Y")){
			NecClearObtC="checked";
		}
		if(General.checknull(arrayList.get(0).getFinCommitUni()).equals("Y")){
			FinCommitUniC="checked";
		}
		if(General.checknull(arrayList.get(0).getAttchCertif()).equals("Y")){
			AttchCertifC="checked";
		}
		if(General.checknull(arrayList.get(0).getProjPropClear()).equals("Y")){
			ProjPropClearC="checked";
		}
		f_agn=General.checknull(arrayList.get(0).getFundAgency());
		cnfrm=General.checknull(arrayList.get(0).getIsSubmit());
		Dept=General.checknull(arrayList.get(0).getDept()); 
		Desg=General.checknull(arrayList.get(0).getDesg());
		piName=General.checknull(arrayList.get(0).getPiName());
		
		ProjPropsal=General.checknull(arrayList.get(0).getProjPropsal());
		DurPropProj=General.checknull(arrayList.get(0).getDurPropProj());
		TotalBudgProp=General.checknull(arrayList.get(0).getTotalBudgProp());
		NameAddrCoPi=General.checknull(arrayList.get(0).getNameAddrCoPi());
		ProjPropSub=General.checknull(arrayList.get(0).getProjPropSub());
		NecClearObt=General.checknull(arrayList.get(0).getNecClearObt());
		FinCommitUni=General.checknull(arrayList.get(0).getFinCommitUni());
		AttchCertif=General.checknull(arrayList.get(0).getAttchCertif());
		ProjPropClear=General.checknull(arrayList.get(0).getProjPropClear());
		date=General.checknull(arrayList.get(0).getXTODATE());
		str_date=General.checknull(arrayList.get(0).getProj_start_date());
		NonRecCost=General.checknull(arrayList.get(0).getNonRecCost());
		ChemAndCon=General.checknull(arrayList.get(0).getChemAndCon());
		Manpower=General.checknull(arrayList.get(0).getManpower());
		Contingency=General.checknull(arrayList.get(0).getContingency());
		Travel=General.checknull(arrayList.get(0).getTravel());
		OutSourcingCharge=General.checknull(arrayList.get(0).getOutSourcingCharge());
		OverCharg=General.checknull(arrayList.get(0).getOverCharg());
		FinCommitUniDetails=General.checknull(arrayList.get(0).getFinCommitUniDetails());
		ProjPropsalId=General.checknull(arrayList.get(0).getPpId());
		locationCode=General.checknull(arrayList.get(0).getLocationCode());
		ddoCode=General.checknull(arrayList.get(0).getDdoCode());
		
		projPropsalIdManual=General.checknull(arrayList.get(0).getProjPropsalIdManual());
		durPropProjYear =General.checknull(arrayList.get(0).getDurPropProjYear());
		
		projtype=General.checknull(arrayList.get(0).getProjtype());
		projunder=General.checknull(arrayList.get(0).getProjunder());
		erptype =General.checknull(arrayList.get(0).getErptype());
		projterm=General.checknull(arrayList.get(0).getProjterm());
		
		IsApprovalReq =General.checknull(arrayList.get(0).getIsApprovalReq());
		// New coloums added
		fn_agency	   =General.checknull(arrayList.get(0).getFn_agency());
		proj_obj	   =General.checknull(arrayList.get(0).getProj_obj());
		
		thrust_area	   =General.checknull(arrayList.get(0).getThrust_area());
		sub_thrust_area=General.checknull(arrayList.get(0).getSub_thrust_area());
		retire_ps_pri  =General.checknull(arrayList.get(0).getRetirePiName());
		budget_head    =General.checknull(arrayList.get(0).getBudgetHeads());
		inst_charges=General.checknull(arrayList.get(0).getInst_charges());
		
		if(projtype.trim().equals("AICRP")){
			erptypediv="show";
		}
		if(General.checknull(cnfrm).trim().equals("Y")){
			dis="disabled";
		}
	
	}else if (fstatus.trim().equals("S")){
		OPT_VALUE = ApplicationConstants.SEARCH;
	}
	if(user_status.equals("A")){
		//dis="";
		disabled="";
	}else{
		//dis="disabled";
		//dis="";
		disabled="disabled";
	}
	 locationCode= locationCode.equals("")?loct_id:locationCode;
	 ddoCode= ddoCode.equals("")?ddo_id:ddoCode;
	 
	 String suser_id = General.checknull((String) request.getSession().getAttribute("user_id"));
%>
<body onload="getEmployee('');geteditDD('<%=fstatus%>', '<%=piName%>', '<%=Dept%>', '<%=Desg %>','<%=locationCode%>','<%=ddoCode %>','<%=sess_emp_id%>','<%=dept_id%>','<%=Desg%>');
			 getLocationDetail('<%=locationCode%>');
			<%if(fstatus.equals("E")){%> getSubThrustAreaByThrustArea('<%=thrust_area%>','<%=sub_thrust_area%>','sub_thrust_area','list');<%}%>">
<script type="text/javascript">
var user_status='<%=user_status%>';
   $(document).ready(function() { 
		$("#XTODATE").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#XTODATE").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#proj_start_date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#proj_start_date").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#applied_date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#applied_date").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
 });
 </script>
<div class="container-fluid">
<div id="" class="page-header"><h4><%=ReadProps.getkeyValue("form1_proj_sub.header","sitsResource") %></h4> </div>
    <form class="form-horizontal" name="frmF1ProjectSubmissionE" id="frmF1ProjectSubmissionE" action="" method="post" autocomplete="off" target="" enctype="multipart/form-data">
    
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
				 <div class="col-sm-12">
				<div class="row text-center">
					<div class="errmessage1" id="errMsgnew"></div>
				</div>
			</div>
            <div class="col-sm-12">
				<div class="row text-center">
					<div class="errmessage2" id="errMsgnew"></div>
				</div>
		    </div>
				<div class="col-md-12">
				<div class="row " >
							<label class="col-sm-7 col-form-label text-left " for="">Project Extension Required -<b><%=ProjPropsal%></b></label>
							<label class="col-sm-5 col-form-label text-left" for="">Proposal Id -<b><%=ProjPropsalId%></b></label>
							
					   </div>
			</div>
			</div>
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left" for="">Extension Type</label>
							<div class="col-sm-3 ">
								<select class="form-control" id="projET" name="projET">
									<option value="">Select Type</option>
									<option value="wf">With Financial Support</option>
									<option value="wo">Without Financial Support</option>
									</select>
				           </div>
				             <label class="col-sm-2 col-form-label text-left required-field" for="">Extension Duration</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" placeholder="Enter Year" maxlength="2" value="" id="extdurPropProjYear" name="extdurPropProjYear" onblur="IsIntegerVal(this);">
								<span style="color: red; text-align:center;">(Year)</span>
				           </div>
				           <!-- <div class="col-sm-1 col-form-label" style="color: red;">(Year)</div> -->
							 <div class="col-sm-2">
								<input type="text" class="form-control" placeholder="Enter Month" maxlength="2" value="" id="extdurPropProjMonth" name="extdurPropProjMonth" onblur="IsIntegerVal(this);">
								<span style="color: red; text-align:center;">(Month)</span>
				           </div>
				           <!-- <div class="col-sm-1 col-form-label" style="color: red;"> (Month)</div> -->					
					</div>
					
				</div>
			</div>
				
				<div class="form-group">
				<div class="col-md-12">
					<div class="row">
					
					</div>
					</div>
				</div>			 
			    <div class="col-md-12 text-center m-t-20">
			    <%-- <%if(cnfrm.equals("Y")){ %> --%>
					<button type="button" class="btn btn-view" id="btnExtsave" onclick="saveExtension('<%=id%>')">Save</button>
				<%-- 	<%}%> --%>
					<button type="button" class="btn btn-view" id="btnReset1" >Reset</button> 
				</div>	
				
				<div class="form-group">
				<div class="col-md-12">
					<div class="row">
					
					</div>
					</div>
				</div>				
				<table id="searchTable11" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th class="text-center">S.No.</th>
					<th class="text-center">Extension Type</th>
					<th class="text-center">Extension Duration Yr/Mon</th>
					<th class="text-center">Creation Date</th>
				</tr>	
			</thead>	
			<tbody>
			<% 
			int m=0;
			if(extList.size()>0){
			for(ProjectSubmissionModel bhm: extList){
			%>
                <tr>
					<td style="text-align:center; width:5%;"><%=++m %></td>
					<td style="text-align:center; width:15%;"><%=bhm.getProjET() %></td>
					<td style="text-align:center; width:15%;"><%=bhm.getExtdurPropProjYear() %> Yr/<%=bhm.getExtdurPropProjMonth() %> Mon</td>
					<td style="text-align:center; width:15%;"><%=bhm.getCreateDate() %></td>
					
				 </tr>
			<%	}}else{%>
			</tbody>
			<tr>
					<td style="text-align:center; width:5%;"></td>
					<td style="text-align:center; width:15%;"></td>
					<td style="text-align:center; width:15%;"><b>No Extension History Found</b></td>
					<td style="text-align:center; width:15%;"></td>
					 </tr>
			<%} %>
			
		</table>
		
			<!-- -------------------------------End Here (Write content inside this modal)------------------------------------------- -->
			</div> <!-- End of panal body -->
			</div> <!-- End of panal default -->
		   </div> <!-- End of modal body -->
				 
			</div> <!-- End of modal content -->
		</div> <!-- End of modal-dialog -->
	</div> <!-- End of modal class/id at which modal will be open -->
  
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
						<label class="col-sm-7 col-form-label text-left required-field" for="">Project Type</label>
							<div class="col-sm-4">
								<select class="form-control" id="projtype" name="projtype">
								<option value="">Select Project Type</option>
								<%=QueryUtil.getComboOption("rsrch_form1_project_type", "type", "type", projtype ,"", "type_id") %>
								<%-- 	<option value="IRP" <%=projtype.equals("IRP")?"selected":""%>>Intramural Research Project(IRP)</option>
									<option value="ERP" <%=projtype.equals("ERP")?"selected":""%>>Extramural Research Project(ERP)</option> --%>
								</select>
				           </div>
				       </div>
				</div>
			</div>
			
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
						<label class="col-sm-7 col-form-label text-left" for="">Project Under</label>
							<div class="col-sm-4">
								<select class="form-control" id="projunder" name="projunder">
									<option value="">Select Project Under</option>
									<option value="DOR" <%=projunder.equals("DOR")?"selected":""%>>Directorate of Research</option>
									<option value="DEE" <%=projunder.equals("DEE")?"selected":""%>>Directorate of Extension Education</option>
									</select>
				           </div>
				       </div>
				</div>
			</div>
			
			<div class="form-group" id="erptypediv" style="display:<%=erptypediv%>;" >
			<div class="col-md-12">
					<div class="row" >
						<label class="col-sm-7 col-form-label text-left required-field" for="">AICRP Type</label>
							<div class="col-sm-4">
								<select class="form-control" id="erptype" name="erptype">
									<option value="">Select AICRP Type</option>
									<option value="REC" <%=erptype.equals("REC")?"selected":""%>>Regular Centre</option>
									<option value="SUC" <%=erptype.equals("SUC")?"selected":""%>>Sub Centre</option>
									<option value="VOC" <%=erptype.equals("VOC")?"selected":""%>>Voluntary Centre</option>
									</select>
				           </div>
				       </div>
				</div>
			</div>
			
			<%if(!fstatus.trim().equals("S")) { %>
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row" >
						<label class="col-sm-7 col-form-label text-left required-field" for="">Project Term</label>
						        <div class="col-sm-2">
								     <input type="radio" class="test" id="projtermshort" name="projterm" value="LongTerm" <%=projterm.equals("LongTerm")?"checked":projterm.equals("")?"checked":"" %>>&ensp;Long Term Project
							   </div>
							   <div class="col-sm-2">
								     <input type="radio" class="test" id="projterlong" name="projterm" value="ShortTerm"  <%=projterm.equals("ShortTerm")?"checked":"" %>>&ensp;Short Term Project
							   </div>	
				       </div>
				</div>
			</div>
			<%}%>
			
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row" >
				<label class="col-sm-7 col-form-label text-left required-field" for="">Funding Agency</label>
							<div class="col-sm-4">
								
								<select class="form-control" id="fn_agency" name="fn_agency"  >
									<option value="">Select Funding Agency</option>
									<%=QueryUtil.getComboOption("rsrch_funding_agency", "fa_id", "concat(fa_name,' ~ ',fundedby)", fn_agency,"", "fa_name") %>
								</select>
		           </div> 
		             </div>
				</div>
			</div>
			
			<%if(!fstatus.trim().equals("S")) { %>
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row" >
						<label class="col-sm-7 col-form-label text-left required-field" for="">Is Approval required?</label>
						        <div class="col-sm-2">
								     <input type="radio" class="test" id="IsApprovalReqYes" name="IsApprovalReq" value="Yes" <%=IsApprovalReq.equals("Yes")?"checked":"" %> <%=fstatus.equals("E")?"disabled":"" %>>&ensp;Yes
							   </div>
							   <div class="col-sm-2">
								     <input type="radio" class="test" id="IsApprovalReqNo" name="IsApprovalReq" value="No"  <%=IsApprovalReq.equals("No")?"checked":IsApprovalReq.equals("")?"checked":"" %> <%=fstatus.equals("E")?"disabled":"" %>>&ensp;Not required (Skip)
							   </div>	
				       </div>
				</div>
			</div>
			<%} %>
			
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  				
      				<label for="location" class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%=dis %><%=disabled %> onchange=getDdoDetailbyLocation(this.value);>
									<option value="">Select Location</option>
								</select>
				           </div>
      	<%-- 			<div class="col-sm-4"><select class="form-control" id="Xlocation" name="Xlocation" onchange="getddo(this.value)" <%=dis %>>
				      						<option value="">Select Location</option>
				      						<%if(user_status.equals("A")){ %>
				      							<%=QueryUtil.getComboOption("leave_location_mast l, ddolocationmapping dlm" , "dlm.location_code", "l.location_name", "", "dlm.LOCATION_CODE=l.LOCATION_CODE", "l.location_name") %>
				      						<%}else{ %>
				      							<%=QueryUtil.getComboOption("ddolocationmapping m, leave_location_mast l", "l.LOCATION_CODE", "l.LOCATION_NAME", ddoLocation, "m.LOCATION_CODE = l.LOCATION_code and m.DDO_ID ='"+ddo+"'", "l.LOCATION_NAME") %>
				      						<%} %>
				      						</select></div> --%>
				        						
				</div>
				
				</div>
				</div>
				 <div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  								
					<label for="ddo" class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo" <%=dis %> <%=disabled %> onchange="getEmployee('<%=sess_emp_id%>');";>
      						 <option value="">Select DDO</option>
      						<%--<%if(user_status.equals("A")){ }
      						else{%>
      							<%=QueryUtil.getComboOption("DDO", "DDO_ID", "DDONAME", ddo , "ddo_id='"+ddo+"'", "DDONAME") %>
      						<%} %> --%>
      						</select>      				
      				</div>      						
			</div>
			</div>
			
				</div>
				<% 
				if(!fstatus.trim().equals("S") && !user_status.equals("U")) { %>
				 <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
			<label for="" class="col-sm-2 col-form-label ">Is Retire</label>
						<div class="col-sm-4">
							<td class="text-center"><input type="checkbox"  id="XISRETIRE" name="XISRETIRE" <%if(General.checknull(fstatus).equals("E") &&!retire_ps_pri.equals("")){ %>checked<%} %>/></td>
					    </div>
					    </div>
					    </div>
					    </div>
		    <%}%>
             <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						 <label class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Name of the Principal Investigator</label>
						<div id="non_ret_pi">
							<div class="col-sm-4 ">
								<%-- <input class="form-control" placeholder="Enter PI Name" value="<%=piName %>" type="text" id="PiName" name="PiName""> --%>
								<select class="form-control" id="PiName" name="PiName" <%if(fstatus.equals("E") &&(!retire_ps_pri.equals(""))){ %>style="display: none;"<%} %> <%=disabled %>>
									<option value="">Select Principal Investigator</option>
								</select>
				           </div>
				           </div>
				           <%if(!fstatus.trim().equals("S")) { %>
				           <div class="col-sm-4 " id="ret_pi" <%if(fstatus.equals("N")||!piName.equals("")){ %>style="display: none;"<%} %>>
								<%-- <input class="form-control" placeholder="Enter PI Name" value="<%=piName %>" type="text" id="PiName" name="PiName""> --%>
									<input type="text" class="form-control"   value="<%=retire_ps_pri%>" placeholder="Enter Retire Principal Investigator" id="RETIRE_PS" name="RETIRE_PS" maxlength="99">
				           </div>
				            <%}%>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				<%if(fstatus.trim().equals("E")){ %>
				  <div id="applied_pi_date" style="display: none;">
				  <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						 <label class="col-sm-7 col-form-label text-left  required-field "  >Applied date</label>
						<div class="col-sm-4 ">
								<div class="input-group date" id="msg-proj_start_date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
									id="applied_date" name="applied_date" placeholder="DD/MM/YYYY" value="" >
								</div>								
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				</div>
				<%} %>
				 <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
					
				           <label class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Department</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="deptId" name="deptId" >
									<option value="">Select Department</option>
								</select>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
					
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Designation</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="desId" name="desId" >
									<option value="">Select Designation</option>
								</select>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				
				<!-- New fileds added thrust are and sub thrust area -->
					<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
					<label for="" class="col-sm-7 col-form-label text-left">Research Thrust Area</label>
	      				<div class="col-sm-4">
								<select class="form-control" id="thrust_area" name="thrust_area" onchange="getSubThrustAreaByThrustArea(this.value,'<%=sub_thrust_area%>','sub_thrust_area','list');">
									<option value="">Select Thrust Area </option>	
									 <%=QueryUtil.getComboOption("rsrch_thrust_area_mast", "THRUST_AREA_ID", "THRUST_AREA ",thrust_area,"", "THRUST_AREA") %> 			    
								</select>
			            </div>
			            </div>
	            </div>
	            </div>
			            
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
				 	<label for="" class="col-sm-7 col-form-label text-left">Research Sub Thrust Area</label>
	      				<div class="col-sm-4">
							<div class="" id="sub_thrust_area">
								<ul class="form-control"
									style="height: 85px; padding-top: 0px;resize: vertical;overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;">
									<li>List Of Research Sub Thrust Area(s)</li>
							</ul>
						</div>
			           </div>
			          </div>
		            </div>
		           </div>
				<%if(fstatus.trim().equals("N") || fstatus.trim().equals("E") || fstatus.trim().equals("R")) { %>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				          <label class="col-sm-7 col-form-label text-left required-field" for="">Title Of the project proposal (Please attach One page Project Summary)</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" value="<%=ProjPropsal %>" placeholder="Enter Title Of the project proposal" id="projPropsal" name="projPropsal" maxlength="250">
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				
					<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				          <label class="col-sm-7 col-form-label text-left" for="">Objective(s) of the Project</label>
							<div class="col-sm-4">
								 <textarea style="height: 100px; resize:vertical;width:100%;" rows="25" cols="50" placeholder="Enter Details" id="proj_obj" name="proj_obj" maxlength="3000"><%=proj_obj %></textarea>
				           </div>
				           <div class="col-sm-1"><a href="javascript:void(0)" class="previewbtn" data-toggle="modal" data-target=".recomond-btnTxt" onclick="getData1()" style="text-decoration:none;"> <span class="previewbtn">Preview</span>
	 	  					 </a></div>
					   </div>
				   </div>
				</div>
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
							<label class="col-sm-7 col-form-label text-left required-field" for="">Project Proposal Id</label>
							<div class="col-sm-4">
								<input type="text" class="form-control"  <%=dis %> value="<%=ProjPropsalId %>" placeholder="Enter project proposal Id" id="projPropsalId" name="projPropsalId" disabled="disabled">
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
					<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
							<label class="col-sm-7 col-form-label text-left" for="">Manual Project Proposal Id/Project Code</label>
							<span class="col-sm-7 col-form-label text-left " style="font-size: 13px !important; font-weight: bold; color: green;">
					  			(Select if one Project is running simultaneously in multiple location/college.)
					  		</span>
							<div class="col-sm-2">
								<input type="text" class="form-control"   value="<%=projPropsalIdManual%>" placeholder="Enter Manual project proposal Id" id="projPropsalIdManual" name="projPropsalIdManual"  maxlength="24">
				           </div>
	      				<div class="col-sm-2" title="Available projects">
	      						<select class="form-control ddo" id="savedmanualproj" name="savedmanualproj">
	      						 	<option value="Available project Ids">Available project Ids</option>
	      							<%=QueryUtil.getComboOption("rsrch_form1_mast", "ppId", "concat(PS_TITTLE_PROJ, concat('-',ppId))", " " , " ", "ppld_manual") %>
	      						</select>      				
	      				</div>
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				          <label class="col-sm-7 col-form-label text-left " for="">Duration Of the proposed Project</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" placeholder="Enter Duration Year" maxlength="2" value="<%=durPropProjYear%>" id="durPropProjYear" name="durPropProjYear" onblur="IsIntegerVal(this);">
				           <span style="color: red;">(Year)</span>
				           </div>
				           <!-- <div class="col-sm-1 col-form-label" style="color: red;">(Year)</div> -->
							
				           <div class="col-sm-2">
								<input type="text" class="form-control" placeholder="Enter Duration Month" maxlength="2" value="<%=DurPropProj %>" id="durPropProj" name="durPropProj" onblur="IsIntegerVal(this);">
				           <span style="color: red;">(Month)</span>
				           </div>
				           <!-- <div class="col-sm-1 col-form-label" style="color: red;"> (Month)</div> -->
				           
				          <%--  <%if(cnfrm.equals("Y") & fstatus.trim().equals("E")){ %> --%> 
							 <div class="col-sm-1"><a href="" data-toggle="modal" data-target="#myModal">Is Extension Required</a></div> 
						<%-- 	 <%} %> --%>
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				          <label class="col-sm-7 col-form-label text-left " for="">Total Budget Proposed (Including overhead charges) </label>
							<div class="col-sm-4">
								<input type="text" class="form-control" placeholder="Enter Total Budget Proposed" value="<%=TotalBudgProp %>" id="totalBudgProp" name="totalBudgProp" onblur="IsIntegerVal(this);"  maxlength="10">
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				<%
				if((fstatus.equals("E"))&&!budget_head.equals("")){
					String headArr[]=budget_head.split("~");
				%>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				          <label class="col-sm-7 col-form-label text-left " for="">Heads </label>
							<label class="col-sm-1 col-form-label text-left " for="">Capital </label>
							<div class="col-sm-1">
								<input type="text" style="padding-left:0px;padding-right:0px;"class="form-control"  placeholder="Enter Capital" value="<%=headArr[0]%>" id="capital" name="capital" onblur="IsIntegerVal(this);"  maxlength="9">
				           </div>
				           <label class="col-sm-1 col-form-label text-left " for="">General </label>
				           <div class="col-sm-1">
								<input type="text" style="padding-left:0px;padding-right:0px;" class="form-control"  placeholder="Enter General" value="<%=headArr[1]%>" id="general" name="general" onblur="IsIntegerVal(this);"  maxlength="9">
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						<label class="col-sm-7 col-form-label text-left " for=""> </label>
				<label class="col-sm-1 col-form-label text-left " for=""> Revenue</label>
				<div class="col-sm-1">
								<input type="text" style="padding-left:0px;padding-right:0px;" class="form-control" placeholder="Enter Revenue" value="<%=headArr[2]%>" id="revenue" name="revenue" onblur="IsIntegerVal(this);"  maxlength="9">
				           </div>
				           <label class="col-sm-1 col-form-label text-left " for="">Lumpsum</label>
				            <div class="col-sm-1">
								<input type="text" style="padding-left:0px;padding-right:0px;" class="form-control"   placeholder="Enter Lumpsum" value="<%=headArr[3]%>" id="lumpsum" name="lumpsum" onblur="IsIntegerVal(this);"  maxlength="9">
				           </div>
				           </div>
				           </div>
				           </div>
				           <%
				} %>
				<%if(fstatus.equals("N")){ %>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				          <label class="col-sm-7 col-form-label text-left " for="">Heads </label>
							<label class="col-sm-1 col-form-label text-left " for="">Capital </label>
							<div class="col-sm-1">
								<input type="text" style="padding-left:0px;padding-right:0px;" class="form-control"  placeholder="Enter Capital" value="" id="capital" name="capital" onblur="IsIntegerVal(this);"  maxlength="9">
				           </div>
				           <label class="col-sm-1 col-form-label text-left " for="">General </label>
				           <div class="col-sm-1">
								<input type="text"  style="padding-left:0px;padding-right:0px;" class="form-control"  placeholder="Enter General" value="" id="general" name="general" onblur="IsIntegerVal(this);"  maxlength="9">
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						<label class="col-sm-7 col-form-label text-left " for=""> </label>
				<label class="col-sm-1 col-form-label text-left " for=""> Revenue</label>
				<div class="col-sm-1">
								<input type="text" style="padding-left:0px;padding-right:0px;" class="form-control" placeholder="Enter Revenue" value="" id="revenue" name="revenue" onblur="IsIntegerVal(this);"  maxlength="9">
				           </div>
				           <label class="col-sm-1 col-form-label text-left " for=""> Lumpsum</label>
				            <div class="col-sm-1">
								<input type="text" style="padding-left:0px;padding-right:0px;" class="form-control"   placeholder="Enter Lumpsum" value="" id="lumpsum" name="lumpsum" onblur="IsIntegerVal(this);"  maxlength="9">
				           </div>
				           </div>
				           </div>
				           </div>
				           <%} %>
				<%-- <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
					
				           <label class="col-sm-7 col-form-label text-left required-field" for="">Name of External Fund Agency</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="FundAgency" name="FundAgency" <%=dis %>>
									<option value="">Select Fund Agency</option>
									<!-- 1 table , 2 id , 3 name , 4 for slect, 5th where clse, orderby -->
									<%=QueryUtil.getComboOption("RSRCH_FUNDING_AGENCY", "fa_id", "fa_Name", ""+f_agn+"", "", "fa_name") %>
								</select>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> --%>
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left" for="">Institutional Charges(Rs)</label>
							<div class="col-sm-4 ">
								<input type="text" class="form-control" placeholder="Enter Institutional Charges(Rs)" value="<%=inst_charges%>" id="inst_charges" name="inst_charges" onblur="IsIntegerVal(this);" maxlength="9">
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left" for="">Name and address of Co-PI's(if any)</label>
							<div class="col-sm-4 ">
								<input type="text" class="form-control" placeholder="Enter Name and address of Co-PI's" value="<%=NameAddrCoPi %>" id="nameAddrCoPi" name="nameAddrCoPi" maxlength="1000">
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
					
				           <label class="col-sm-7 col-form-label text-left " for="">Is there project Proposal submitted in response of the call for proposal by the agency?.</label>
							<div class="col-sm-4 ">
								<input type="Checkbox" value="<%=ProjPropSub %>" id="projPropSub" name="projPropSub" <%=ProjPropSubC %>>								
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
					<!-- 	///////////////////////// -->
					<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left required-field" for="">Project Start Date </label>
							
							<div class="col-sm-4 ">
								<div class="input-group date" id="msg-proj_start_date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
									id="proj_start_date" name="proj_start_date" placeholder="DD/MM/YYYY" value="<%=str_date %>" >
								</div>								
				           </div>
							<div class="col-sm-1"></div>
					   </div>
					  
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left " for="">Deadline For the submission of the project proposal</label>
							
							<div class="col-sm-4 ">
								<div class="input-group date" id="msg-XTODATE">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
									id="XTODATE" name="XTODATE" placeholder="DD/MM/YYYY" value="<%=date%>" >
								</div>								
				           </div>
				           
							<div class="col-sm-1"></div>
					   </div>
					  
				   </div>
				</div> 
				
		
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left " for="">Do the project Proposal requires clearance form IBSC/Animal Ethics Committee/Human Ethics Committee/Stem sell Committee or any other regulatory agency,at the time of submission of the proposal?. please specify</label>
							<div class="col-sm-4">
								<input type="Checkbox" id="projPropClear" name="projPropClear" value="<%=ProjPropClear %>" <%=ProjPropClearC %> >
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left " for="">If yes, has the necessary Clearance been obtained?</label>
							<div class="col-sm-4 ">
								<input type="Checkbox" value="<%=NecClearObt %>" id="necClearObt" name="necClearObt" <%=NecClearObtC %> >
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left " for="">If there any financial Commitment in the part of university, if the project its implemented? if yes, please provide the details</label>
							<div class="col-sm-4 ">
								<input type="Checkbox" value="<%=FinCommitUni %>" id="finCommitUni" name="finCommitUni" <%=FinCommitUniC %> >
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				
				<div class="form-group" id="det2">
					<div class="col-md-12 ">
						<div class="row " >						
				           <label class="col-sm-7 col-form-label text-left required-field" for="">Details</label>
							<div class="col-sm-4 ">
								<textarea id="finCommitUniDetails" name="finCommitUniDetails" style="height:50px ;width:100%;" maxlength="254"><%=FinCommitUniDetails %></textarea>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left " for="">Attach copy of declaration/Endorsement certificate</label>
							<div class="col-sm-4 ">
								<input type="Checkbox" value="<%=AttchCertif %>" id="AttchCertif" name="AttchCertif" <%=AttchCertifC %> >
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				   
				   <span class="col-sm-7 col-form-label text-left " style="font-size: 13px !important; font-weight: bold; color: red;">
					  			 Note: *Duration can be extended only for Created/Submitted Project proposal.
					  		</span>
				</div> 
				
				<%-- <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
							<label class="col-sm-7 col-form-label text-left " for=""><h5><b>Break-up of budget</b></h5></label>						
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left required-field" for="">A. Non-recurring(Equipment cost)</label>
							<div class="col-sm-4 ">
								<input type="text" class="form-control" <%=dis %> placeholder="Enter Equipment Cost" value="<%=NonRecCost %>" id="nonRecCost" name="nonRecCost" onblur="IsIntegerVal(this);">								
				           </div>
				           
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
							<label class="col-sm-7 col-form-label text-left required-field" for=""><b>B. Recurring</b></label>							
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >						
				           <label class="col-sm-7 col-form-label text-left required-field" for="">B1. Chemicals and consumables</label>
							<div class="col-sm-4 ">
								<input type="text" class="form-control" <%=dis %> placeholder="Enter Chemicals and consumables" value="<%=ChemAndCon %>" id="chemAndCon" name="chemAndCon" onblur="IsIntegerVal(this);">
								
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left required-field" for="">B2. Manpower</label>
							<div class="col-sm-4 ">
								<input type="text" class="form-control" <%=dis %> placeholder="Enter Manpower" value="<%=Manpower %>" id="manpower" name="manpower" onblur="IsIntegerVal(this);">							
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left required-field" for="">B3. Contingency</label>
							<div class="col-sm-4 ">
								<input type="text" class="form-control" <%=dis %> placeholder="Enter Contingency" value="<%=Contingency %>" id="contingency" name="contingency" onblur="IsIntegerVal(this);">
								
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left required-field" for="">B4. Travel</label>
							<div class="col-sm-4 ">
								<input type="text" class="form-control" <%=dis %> placeholder="Enter Travel" value="<%=Travel %>" id="travel" name="travel" onblur="IsIntegerVal(this);">
								
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left required-field" for="">B5. Any other charges/outsourcing charges towards technical services, if applicable</label>
							<div class="col-sm-4 ">
								<input type="text" class="form-control" <%=dis %> placeholder="Enter charges" value="<%=OutSourcingCharge %>" id="outSourcingCharge" name="outSourcingCharge" onblur="IsIntegerVal(this);">
								
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left required-field" for="">B6. Overhead charges</label>
							<div class="col-sm-4 ">
								<input type="text" class="form-control" <%=dis %> placeholder="Enter Overhead charges" value="<%=OverCharg %>" id="overCharg" name="overCharg" onblur="IsIntegerVal(this);">
								
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> --%> 
			  
		   	<div class="page-header-1">
				<div class="col-sm-4 repTitle h5 ">Document Upload (Attachment)</div>
			</div>

			<div id="dydiv" style="width:100%;overflow: ; height:unset !important;">
      			<div id="" style="padding:8px 0px;display:flex;width:100%;">
					<table border="1" cellspacing="1" cellpadding="1" width="100%" class="tableGrid" >
					<tr>
						<th width="30%" style="text-align:center;">Document Title</th>
					  	<th width="30%" style="text-align:center;">Upload Document 
					  		<!--<span><a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="auto left" style="color:#2b3e1f;font-size: 13px !important;"
					  			title=""data-original-title="(Max. Size Cannot Exceed 20MB) <br> (*Note: Only .pdf, .jpg, .png, & doc files will be allowed for uploading.)"></a>
					  		</span>
							<span><a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="auto left" style="color:#2b3e1f;"
    	                    title="" data-original-title="File Format should be JPG/PNG/DOC/PDF and File size should be 100kb to 2 MB."></a></span>-->
							<span><a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="auto left" style="color:#2b3e1f;"
    	                    title="" data-original-title="(Max. Size Cannot Exceed 20MB) (*Note: Only .pdf, .jpg, .png, & doc files will be allowed for uploading.)"></a></span>
					  	</th>
					  	<th width="20%" style="text-align:center;">Delete</th>
					</tr>
					
					<tbody id="searchTable" >
					<%int i=0;
					if(arrayList.size()>0){
					for(ProjectSubmissionModel bhm: fileList){
						k=++i;
					%>				
				    <tr>
				    	
				    	<td class="text-center"><input type="text" class="form-control" id="doctitle<%=k%>" name="doctitle<%=k%>" maxlength="100 placeholder="Enter Document Title" value="<%=bhm.getDoc_name()%>" maxlength="251">
							<input type="hidden" class="form-control" id="hfname<%=k%>" name="hfname<%=k%>">
							<input type="hidden" class="form-control" id="fsize<%=k%>" name="fsize<%=k%>">
						</td>
						
						<td class="text-center">
						<a target="_blank" href="../downloadfile?filename=<%=bhm.getDid()%>_<%=bhm.getFile_name()%>&folderName=RSRCH/FORM1/<%=bhm.getfId()%>">
							<u><%=bhm.getFile_name()%></u>
						</a><%-- <input type="file" id="upldoc<%=k%>" onchange="data(<%=k%>)" name="upldoc<%=k%>" class=""> --%>
						</td>		
						
						<%if(cnfrm.trim().equals("N") || cnfrm.trim().equals("Y")) {%>
							<%-- <td style="text-align:center;color:RED;cursor:pointer; width:5%;" onclick="deletedata(<%=k%>, this)"><i class="fa fa-trash-o"></i>Delete</td> --%>
							
							<td style="text-align:center;color:RED;cursor:pointer; width:5%;"
							 onclick="deletesavefile('<%=bhm.getDid()%>','<%=bhm.getDid()+"_"+bhm.getFile_name()%>','<%=bhm.getfId()%>');"
							 ><i class="fa fa-trash-o"></i>Delete</td>
						<%}else{ %>
							<td style="text-align:center;color:RED;cursor:pointer; width:5%;"></td>
						<%}%>	
					</tr>
					<%} }else{
						k=1;
					%>
					<tr>
				    	<td class="text-center"><input type="text" class="form-control" id="doctitle1" name="doctitle1" maxlength="100" placeholder="Enter Document Title" value="">
							<input type="hidden" class="form-control" id="hfname1" name="hfname1">
							<input type="hidden" class="form-control" id="fsize1" name="fsize1">
						</td>						
						<td class="text-center"><input type="file" id="upldoc1" onchange="data(1)" name="upldoc1" class=""></td>						
						<td style="text-align:center;color:RED;cursor:pointer; width:5%;" onclick="deletedata(1,this)"><i class="fa fa-trash-o"></i> Delete</td>
					</tr>
					<%} %>
 					</tbody>
					</table>
					<div class="text-right"style="margin-top:18px;padding:8px;">
					<% if(General.checknull(fstatus).equals("E") || General.checknull(fstatus).equals("N")){ %>
					<%-- 	<%if(cnfrm.trim().equals("N") || cnfrm.trim().equals("")) {%> --%>
						<div class="colr-blue-p" id="addmore">
							<span id="" onClick=""><i class="fa fa-plus-circle fa-2x" style="font-size: 20px;"></i></span>
						</div>
					<%-- 	<%} %> --%>
					<%} %>
				</div>					
				</div>
			</div>		
			<%}%>
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
			
			<div class="col-md-12 text-center m-t-20">
				<%if(fstatus.trim().equals("R")) {%>
					<%-- <button type="button" class="btn btn-view" id="btnPrevPage" name="btnPrevPage" onclick="PrevPage('<%=pageType %>')" >Back</button> --%>
				<%}else{%>
				<%if(fstatus.trim().equals("S")) {%>
					<button type="button" class="btn btn-view" id="btnSearch1" name="btnSearch1">Search</button>
					<button type="button" class="btn btn-view" id="btnNew" name="btnNew">New</button>
				<%}else{%>
					<button type="button" class="btn btn-view" id="btnSearch" name="btnSearch">Search</button>
					<%if(fstatus.trim().equals("E")) {%>
						<%if(cnfrm.trim().equals("N")) {%>
							<button type="button" class="btn btn-view" id="btnUpdate" name="btnUpdate" onclick="saveAndSubmit('U');">Update</button>
							<button type="button" class="btn btn-view" id="btnSubmit1" name="btnSubmit1" onclick="saveAndSubmit('US');">Approve & Submit</button>
						<%}else if(cnfrm.trim().equals("Y")) {%>
							<button type="button" class="btn btn-view" id="btnSubmit1" name="btnSubmit1" title="Multiple update allowed after submit" onclick="saveAndSubmit('US');">Update & Submit</button>
						<%} %>
					<%}else{%>
							<button type="button" class="btn btn-view" id="btnSave" name="btnSave" onclick="saveAndSubmit('S');">Save</button>
						<!-- <button type="button" class="btn btn-view" id="btnSubmit" name="btnSubmit" onclick="saveAndSubmit('SS');">Save & Submit</button> -->
					<%}%>
				<%}%>
						<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				<%}%>				
				<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				<input type="hidden" name="fstatus" id="fstatus" value="<%=General.checknull(fstatus) %>">
				<input type="hidden" name=index id="index" value="<%=k%>">
				<input type="hidden" name="mid" id="mid" value="<%=id%>">
				<input type="hidden" name=emp_id id="emp_id" value="<%=sess_emp_id%>">
				<input type="hidden" name="deptId" id="deptId" value="<%=dept_id%>">
				<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
				<input type="hidden" name="previous_pi_Name" id="previous_pi_Name" value="<%=piName%>">
				
			</div>
				
			</div>
		</div>
   </form>
  <!--  New model added for Objective(s) of the Project preview button -->
   <form action="">
		<div class="modal fade recomond-btnTxt" id="myModal1" tabindex="-1"
			role="dialog" aria-labelledby="openModal" aria-hidden="true"
			style="display: none;">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
					</div>
					<div class="modal-body">
							<textarea id="modalArea" style="height:70vh; width: 100%;" readonly>
							</textarea>
					</div>
				</div>
			</div>
		</div>
	</form>
   
   <%if (fstatus.trim().equals("S")){ %>
  		<iframe class="embed-responsive-item" onload="resizeIframe(this)" src="form1_project_sub_l.jsp" name="btmfrmF1ProjectSubmissionE" id="btmfrmF1ProjectSubmissionE" frameborder="0" scrolling="no" width="100%" height=""></iframe>
  	<%} %>
   </div> 
</body>
 <script type="text/javascript">
function resizeIframe(iframe) {
    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
    window.requestAnimationFrame(() => resizeIframe(iframe));
}

function getData1(){ 
	var data=document.getElementById("proj_obj").value;
	document.getElementById("modalArea").innerHTML=data;
		
	}
</script>
</html>