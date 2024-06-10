<html lang="en">
<%@page import="com.sits.rsrch.research_activity.objective_achievement_details.ObjectiveAchievementManager"%>
<%@page import="com.sits.rsrch.research_activity.objective_achievement_details.ObjectiveAchievementModel"%>
<%@page import="com.sits.general.QueryUtil"%>
<%@page import="com.sits.general.General"%>
<%@page import="com.sits.general.ReadProps"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.sits.general.ApplicationConstants"%>
<%@include file="../myCks.jsp"%>

<head>
<meta  name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="-1" />
  
  <link rel="stylesheet" href="../assets/sits/bootstrap/3.3.7/css/bootstrap.css"  type="text/css">
  <script type="text/javascript" src="../js/common.js"></script>
  <script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
  <script type="text/javascript" src="../assets/sits/bootstrap/3.3.7/js/bootstrap.js"></script>
  <script type="text/javascript" src="../js/aes/AesUtil.js"></script>
  <script type="text/javascript" src="../js/aes/crypto.js"></script>	
  <script type="text/javascript" src="../js/common/common-utilities.js"></script>
  <script type="text/javascript" src="../js/common/validations.js"></script>
  <script type="text/javascript" src="../js/gen.js"></script>
  <script type="text/javascript" src="../js/examCommonDropDown.js"></script>
  
  <link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
  <link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
  <link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
  <link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
  <link rel="stylesheet" href="../assets/plugins/datepicker/datepicker3.css"  type="text/css">
  <script type="text/javascript" src="../assets/plugins/datepicker/bootstrap-datepicker.js"></script>
  <script type="text/javascript" src="../js/research/objective-achievement-details.js"></script>
</head>
<script type="text/javascript">
   $(document).ready(function() { 
		$("#XSTARTDATE").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#XSTARTDATE").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#XENDDATE").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#XENDDATE").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#X_FDATE").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#X_FDATE").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#X_TDATE").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#X_TDATE").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
 });
 </script>

<%
String OPT_TYP = "" , OPT_VALUE = "", oa_id="" ,r_id="",rsrch_proposal_id="",f_date="",t_date="",achievement_det="",attach="";
int index=0;
JSONArray json=	 new JSONArray();
OPT_TYP = General.checknull(request.getParameter("opt_typ"));
if(OPT_TYP.equals("")) {
	OPT_TYP = "N";
	OPT_VALUE = ApplicationConstants.NEW;
	index=1;
}else{
	OPT_TYP = "E";
	OPT_VALUE = ApplicationConstants.EDIT; 
	oa_id = General.checknull(request.getParameter("oa_id"));
	r_id = General.checknull(request.getParameter("r_id"));
	ObjectiveAchievementModel smodel = ObjectiveAchievementManager.viewRecordDetails(oa_id);
	rsrch_proposal_id= General.checknull(smodel.getRsrch_proposal_id()).trim();
	f_date= General.checknull(smodel.getFrom_date()).trim();
	t_date= General.checknull(smodel.getTo_date()).trim();
	achievement_det= General.checknull(smodel.getAchievement_det()).trim();
	attach= General.checknull(smodel.getAttach_id()).trim();
}   
%>
<body onload="">
<body>
	<div class="container-fluid">
	<div id="" class="page-header"><h4><%=ReadProps.getkeyValue("objective_achievement.header","sitsResource") %>
   <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"><span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
	 </span> 
	 </h4>
 </div>
		<form class="form-horizontal" name="update_document_detail" id="update_document_detail" method="post" autocomplete="off">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title text-right" ><%=OPT_VALUE%></h3>
				</div>
				<div class="panel-body">
				<div class="form-group"  >
			<div class="col-md-12">
			<div class="row">
	  				
      				<label for="location" class="col-sm-2 col-form-label required-field ">Research Proposal</label>
      				<div class="col-sm-4 "><select class="form-control" id="rsrch_proposal" name="rsrch_proposal"  >
   						<option value="">Select Research Proposal</option>
   						<% if(!user_id.equals("ADMIN")) { %>
   					<%=QueryUtil.getComboOption("rsrch_form1_mast", "PS_MID", "PS_TITTLE_PROJ", rsrch_proposal_id,"is_form_submittd='Y' and PS_PRINCIPAL='"+sess_emp_id+"'", "PS_TITTLE_PROJ") %>
   						<%}else{ %>	
   					<%=QueryUtil.getComboOption("rsrch_form1_mast", "PS_MID", "PS_TITTLE_PROJ", rsrch_proposal_id,"is_form_submittd='Y'", "PS_TITTLE_PROJ") %>
   					<%} %>
   						</select></div>
   						
   						<label for="" class="col-sm-2 col-form-label required-field ">From Date</label>
								<div class="col-sm-4">
									<div class="input-group date" id="msg-XSTARTDATE">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input readonly="readonly" data-field-id=" " type="text"
											class="form-control datecalendar from_date" id="XSTARTDATE"
											name="XSTARTDATE" placeholder="DD/MM/YYYY" value="<%=f_date%>">
									</div>
								</div>
   						
			     						
				</div>
				</div>
				</div>
					<div class="form-group">
						<div class="col-md-12">
							<div class="row">
								
								<label for="" class="col-sm-2 col-form-label required-field">To Date</label>
								<div class="col-sm-4">
									<div class="input-group date" id="msg-XENDDATE">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input readonly="readonly" data-field-id="9" type="text"
											class="form-control datecalendar" id="XENDDATE"
											name="XENDDATE" placeholder="DD/MM/YYYY" value="<%=t_date%>">
									</div>
								</div>
								<div class="row " >
				<label class="col-sm-2 col-form-label text-left" for="">Upload Details (If Any)</label>
							<div class="col-sm-4">
								<%if(OPT_TYP == "N"){ %>
								<input type="file" id="upldoc" name="upldoc" class="" onblur="imgcheck(this.id)">
								<%}else{ %>						
									<span class="fileUpload" id="file_Upload">
								
							<a target="_blank" href="../downloadfile?filename=<%=attach%>&folderName=/RESEARCH/PROJ_DETAIL/<%=oa_id%>"> 
									<u><%=attach%></u> </a>&ensp; 
									<% if(!oa_id.equals("")&&(!attach.equals(""))){%>
									<span style="color: red;" id="" onclick="deletesavefile('<%=oa_id%>','<%=attach%>');"> <i class="fa fa-times-circle"></i>
									</span> 
									<%}else{ %>							
										<input type="file" class="form-control" id="upldoc" name="upldoc" value="<%=attach%>" onblur="imgcheck(this.id)">
						
							<%}
									} %>
							</span>
								<div class="instruc_text">
									<span class="" style="color:red; font-size: 13px !important; font-weight: bold">(Max size can not exceed 20MB)<br>(Only .pdf, .jpg, .png, .doc, .docx  files will be allowed for uploading.)</span> 
								</div>
					</div> 
								
							</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-12">
							<div class="row">
								<label class="col-sm-2 col-form-label required-field " for="">Project Progress Details</label>
								<div class="col-sm-10">
						<textarea type="text" class="form-control" id="achievement_det" maxlength="1000" style="max-height:120px;height:60px;resize:vertical;" name="achievement_det" placeholder="Enter Project Progress Details" ><%=achievement_det %></textarea>
								</div>
							</div>
						</div>
					</div>
										
					<div class="col-md-12 text-center">
						<div class="row">
							<div class="errmessage" id="errMsg"></div>
						</div>
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

					<div class="form-group m-t-25 m-b-5" style="margin-top:20px;">
						<div class="col-md-12 text-center">
							<div class="row">
							<% if (OPT_TYP.equals("N")) {%>
								<button type="button" class="btn btn-view" id="btnSave" onclick="executeSaveRecord();">Save</button>
								<%} %>
								<% if (OPT_TYP.equals("E")) {%>
								<button type="button" class="btn btn-view" id="btnUpdate" onclick="executeSaveRecord();">Update</button>
								<button type="button" class="btn btn-view" id="btnBack">Back</button>
								<%} %>
								<button type="button" class="btn btn-view" id="btnReset">Reset</button>
							</div>
						</div>
						<input type= "hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" readOnly/>	
						<input type= "hidden" id="oa_id" name="oa_id"  value="<%=oa_id%>" />	
						<input type= "hidden" name="fstatus" id="fstatus" value="<%=OPT_TYP%>"/>
					</div>
				
				<!-- End panel-body -->
			</div>
			</div>
			<!-- End panel-default -->
		</form>
		   <form class="form-horizontal" name="FrmResearchProposalD" id="FrmResearchProposalD" action="" method="post" autocomplete="off" target="">
    <div class="modal fade recomond-btn" id="myModal" tabindex="-1" role="dialog" aria-labelledby="openModal" aria-hidden="true" style="display: none;">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="color:red;">×</button>
			</div>
			  <div class="modal-body">
			   <div class="panel panel-default">
                <div class="panel-heading"><h3 class="panel-title text-right">Searching Criteria</h3></div>
     			<div class="panel-body">
		<!-- -------------------------------Write content inside this modal------------------------------------------- -->
			<div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="location" class="col-sm-2 col-form-label  ">Research Proposal</label>
      				<div class="col-sm-4"><select class="form-control" id="X_RESEARCH" name="X_RESEARCH"  >
   						<option value="">Select Research Proposal</option>
   					<% if(!user_id.equals("ADMIN")) { %>
   					<%=QueryUtil.getComboOption("rsrch_form1_mast", "PS_MID", "PS_TITTLE_PROJ", rsrch_proposal_id,"is_form_submittd='Y' and PS_PRINCIPAL='"+sess_emp_id+"'", "PS_TITTLE_PROJ") %>
   						<%}else{ %>	
   					<%=QueryUtil.getComboOption("rsrch_form1_mast", "PS_MID", "PS_TITTLE_PROJ", rsrch_proposal_id,"is_form_submittd='Y'", "PS_TITTLE_PROJ") %>
   					<%} %>
   						</select></div>
						<label for="" class="col-sm-2 col-form-label">From Date</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="X_FDATE" name="X_FDATE" placeholder="DD/MM/YYYY" value="">
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<div class="row">
						<label for="" class="col-sm-2 col-form-label">To Date</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="X_TDATE" name="X_TDATE" placeholder="DD/MM/YYYY" value="">
						</div>
					</div>
				</div>
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
			
			    <div class="col-md-12 text-center">
					<button type="button" class="btn btn-view" id="X_BTNSEARCH" data-dismiss="modal" onclick=" ">Search</button>
					<button type="button" class="btn btn-view" id="X_BTNRESET" onclick="">Reset</button> 
				</div>
			<!-- -------------------------------End Here (Write content inside this modal)------------------------------------------- -->
			</div> <!-- End of panal body -->
			</div> <!-- End of panal default -->
		   </div> <!-- End of modal body -->
				 
			</div> <!-- End of modal content -->
		</div> <!-- End of modal-dialog -->
	</div> <!-- End of modal class/id at which modal will be open -->
	</form>
	<%if(OPT_TYP == "N"){ %>
		<iframe class="embed-responsive-item" name="objAchievementE"  onload="resizeIframe(this)" id="objAchievementE" width="100%;" height="" src="objective_achievement_details_l.jsp" frameborder="0" scrolling="no" style="">
		</iframe>
		<%} %>
	</div>
	<!-- End container-fluid -->
</body>
<script type="text/javascript">
      function resizeIframe(iframe) {
	    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
	    window.requestAnimationFrame(() => resizeIframe(iframe));
	}
  </script>

</html>
    