<%@page import="com.sits.rsrch.research_patent.ResearchPatentManager"%>
<%@page import="com.sits.rsrch.research_patent.ResearchPatentModel"%>
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
	<script type="text/javascript" src="../js/commonDropDown.js"></script>
    <script type="text/javascript" src="../js/research/research-patent.js"></script>
    
	 
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
  	
  	</style>
</head>
<%			
	int k=0;
	String fstatus = General.checknull(request.getParameter("fstatus"));
	String Xpat_type = General.checknull(request.getParameter("Xpat_type"));
	String Xpat_status = General.checknull(request.getParameter("Xpat_status"));
	String location = General.checknull(request.getParameter("location"));
	String ddo = General.checknull(request.getParameter("ddo"));
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	String OPT_VALUE = "", id="",disabled="",fin_year="",pat_publ_grnt_num="", app_name="", pat_title="", pat_app_num="",pat_awd_by="",pat_grnt_date="", 
			sub_category="",pat_type="",ass_name="", upload="",locationCode="",ddoCode="", pat_status="", category="", url="", resh_category="",
			filing_date="",pat_cat="",deptid="",PiName="";
	ArrayList<ResearchPatentModel> arrayList = new ArrayList<ResearchPatentModel>();  
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E") || fstatus.trim().equals("R")){
		OPT_VALUE = ApplicationConstants.EDIT;
		id					=General.checknull(request.getParameter("id"));
		arrayList 			= ResearchPatentManager.getEditList(id);
		pat_cat				=General.checknull(arrayList.get(0).getPat_cat());
		pat_type			=General.checknull(arrayList.get(0).getPat_type());
		pat_status			=General.checknull(arrayList.get(0).getPat_status());
		locationCode  		=General.checknull(arrayList.get(0).getLocation());
		ddoCode				=General.checknull(arrayList.get(0).getDdo());
		app_name			=General.checknull(arrayList.get(0).getApp_name());
		pat_title			=General.checknull(arrayList.get(0).getPat_title());
		pat_app_num			=General.checknull(arrayList.get(0).getPat_app_num());
		filing_date			=General.checknull(arrayList.get(0).getFiling_date());
		resh_category		=General.checknull(arrayList.get(0).getResh_category());
		sub_category		=General.checknull(arrayList.get(0).getSub_category());
		url					=General.checknull(arrayList.get(0).getUrl());
		pat_awd_by			=General.checknull(arrayList.get(0).getPat_awd_by());
		pat_grnt_date		=General.checknull(arrayList.get(0).getPat_grnt_date());
		pat_publ_grnt_num	=General.checknull(arrayList.get(0).getPat_publ_grnt_num());
		ass_name			=General.checknull(arrayList.get(0).getAss_name());
		upload				=General.checknull(arrayList.get(0).getUpload());
		deptid				=General.checknull(arrayList.get(0).getDeptid());
		PiName				=General.checknull(arrayList.get(0).getPiName());
		fin_year=General.checknull(arrayList.get(0).getFin_yr());
	}else if (fstatus.trim().equals("S")){
		OPT_VALUE = ApplicationConstants.SEARCH;
	}
	
	 locationCode= locationCode.equals("")?loct_id:locationCode;
	 ddoCode= ddoCode.equals("")?ddo_id:ddoCode;
	 if(user_status.equals("U")){
			disabled="disabled";
		}
	 fin_year= fin_year.equals("")?seCurrentFinancialYearId:fin_year;
%>
<body onload="getFinanceYrDropdwn('<%=fin_year%>','fin_yr');getDptByEmployee('<%=sess_emp_id%>','<%=user_status%>','<%=deptid%>');getSearchList('<%=fstatus%>','<%=Xpat_type%>','<%=Xpat_status%>','<%=location%>','<%=ddo%>');
getRsrchCategory('<%=resh_category%>','rsrchcat');getRsrchSubCategory('<%=category%>','<%=sub_category%>','rsrchsubcat');
geteditDD('E', '<%=category%>','<%=sub_category%>','<%=locationCode%>','<%=ddoCode %>');
getLocationDetail('<%=locationCode%>','',''); getEmployeeName('','','<%=PiName%>','list','PiName');
 ">
<script type="text/javascript">
var user_status='<%=user_status%>';
   $(document).ready(function() { 
		$("#filing_date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#filing_date").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#pat_publ_grntd_date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#pat_publ_grntd_date").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
 });
 </script>
<div class="container-fluid">
<div id="" class="page-header"><h4><%=ReadProps.getkeyValue("research_patent.header","sitsResource") %>
     <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
	 </span> 
	 </h4>
 </div>
    <form class="form-horizontal" name="frmF1ProjectSubmissionE" id="frmF1ProjectSubmissionE" action="" method="post" autocomplete="off" target="" enctype="multipart/form-data">
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left required-field" for="">Form Category</label>
							<div class="col-sm-4">
								<select class="form-control" id="pat_cat" name="pat_cat">
								<option value="">Select Category</option>
								<option value="PA"<%=pat_cat.equals("PA")?"selected":""%>>Patents</option>
								<option value="DR"<%=pat_cat.equals("DR")?"selected":""%>>Design Registration</option>
								<option value="PC"<%=pat_cat.equals("PC")?"selected":""%>>Patent Copyright</option>
								<option value="TR"<%=pat_cat.equals("TR")?"selected":""%>>Trademark</option>
								</select>
				           </div>
				           
				           <label class="col-sm-2 col-form-label text-left required-field" for="">Type</label>
							<div class="col-sm-4">
								<select class="form-control" id="pat_type" name="pat_type">
								<option value="">Select Type</option>
								<option value="N"<%=pat_type.equals("N")?"selected":""%>>National</option>
								<option value="I"<%=pat_type.equals("I")?"selected":""%>>International</option>
								</select>
				           </div>
				           
				       </div>
				</div>
			</div>
			
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
						
					<label class="col-sm-2 col-form-label text-left required-field" for="">Status</label>
							<div class="col-sm-4">
								<select class="form-control" id="pat_status" name="pat_status">
									<option value="">Select Status</option>
									<option value="P" <%=pat_status.equals("P")?"selected":""%>>Published </option>
									<option value="G" <%=pat_status.equals("G")?"selected":""%>>Granted </option>
									<option value="A" <%=pat_status.equals("A")?"selected":""%>>Applied </option>
									</select>
				           </div>
				           
		           <label for="location" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Location</label>
	      				<div class="col-sm-4 ">
									<select class="form-control" id="Xlocation" name="Xlocation" <%=disabled %> onchange=getDdoDetailbyLocation(this.value);>
										<option value="">Select Location</option>
									</select>
			           	</div>
			       </div>
				</div>
			</div>
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
      				
						
					<label for="ddo" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo"<%=disabled %> onchange="getEmployeeName('','','<%=sess_emp_id%>','list','PiName');" >
      						 <option value="">Select DDO</option>
      						</select>      				
      				</div>
      				<label class="col-sm-2 col-form-label required-field">Financial Year</label>
					<div class="col-sm-4">
					 <select class="form-control" id="fin_yr"name="fin_yr">
						<option value="">Select Financial Year</option>
					</select> 
					</div>
      				 
							<%-- <div class="col-sm-4 ">
								<select class="form-control" id="PiName" name="PiName"  <%=disabled %>>
									<option value="">Select Principal Investigator</option>
								</select>
			           		</div>  --%>
				</div>
				</div>
				</div>
				
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row" >
					<label class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Inventor Name</label>
						<div id="non_ret_pi">
					   <div class="col-sm-4" id="PiName" >
								<ul class="form-control"
									style="height: 85px; padding-top: 0px;resize: vertical;overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;">
									<li>List Of Inventor</li>
							</ul>
						</div> 
						</div>
						<label for="" class="col-sm-2 col-form-label text-left ">Department</label>
      					<div class="col-sm-4"><select class="form-control ddo" id="deptId" name="deptId" >
      						 <option value="">Select Department</option>
      						</select>
				       </div>
				     </div>
			  </div>
			  </div>
			
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row" >
					 <label class="col-sm-2 col-form-label text-left required-field" for="">Applicant name</label>
						<div class="col-sm-4">
						<input type="text" maxlength="30" class="form-control" id="app_name" name="app_name" value="<%=app_name %>" onblur="Alpha(this);"  placeholder="Enter Applicant name" >
				       </div>
      				<label for="location" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Title</label>
      				<div class="col-sm-4 ">
							<input type="text" maxlength="255" class="form-control" id="pat_title" name="pat_title" value="<%=pat_title %>" onblur="isAlphaNumeric(this,'pat_title');"   placeholder="Enter Title" >
				          </div>
				       </div>
				     </div>
				  </div>
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
			 <label for="ddo" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Application Number</label>
      				<div class="col-sm-4">
      				<input type="text" maxlength="30" class="form-control" id="pat_app_num" name="pat_app_num" value="<%=pat_app_num %>"  onblur="isAlphaNumeric(this,'pat_app_num');"  placeholder="Enter Application Number" >
      				</div>  
				           <label class="col-sm-2 col-form-label text-left required-field" for="">Date Filing date</label>
							<div class="col-sm-4 ">
								<div class="input-group date" id="msg-proj_start_date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
									id="filing_date" name="filing_date" placeholder="DD/MM/YYYY" value="<%=filing_date %>" >
								</div>								
				           </div>
					   </div>
				   </div>
				</div> 
				<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
					 	<label class="col-sm-2 col-form-label text-left required-field" for="">Category:</label>
							<div class="col-sm-4">
								<select class="form-control" id="rsrchcat" name="rsrchcat" onchange="getRsrchSubCategory(this.value,'<%=sub_category%>','rsrchsubcat');" >
								<option value="">Select Category</option>
								</select>
				           </div>
						<label class="col-sm-2 col-form-label text-left required-field" for="">Sub Category:</label>
							<div class="col-sm-4">
								<select class="form-control" id="rsrchsubcat" name="rsrchsubcat">
								</select>
				           </div>
				       </div>
				</div>
			</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						 <label class="col-sm-2 col-form-label text-left " for="">URL link(only for granted)</label>
							<div class="col-sm-4 ">
								<input type="text" maxlength="255" class="form-control" id="url" name="url" value="<%=url %>"  placeholder="Enter Url link of patent( only for granted patent)" >
							<div class="col-sm-1"></div>
					   		</div>
				           <label class="col-sm-2 col-form-label text-left " for="">Awarded by(only for granted)</label>
							<div class="col-sm-4 ">
									<input type="text" maxlength="30" class="form-control" id="pat_awrd_by" name="pat_awrd_by" value="<%=pat_awd_by%>"  placeholder="Enter awarded by(only for granted patent)" >
				           </div>
					   
					    
						</div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >	
						 <label class="col-sm-2 col-form-label text-left required-field" for="">Published/ granted date</label>
								<div class="col-sm-4 ">
								<div class="input-group date" id="msg-pat_start_date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
									id="pat_publ_grntd_date" name="pat_publ_grntd_date" placeholder="DD/MM/YYYY" value="<%=pat_grnt_date %>" >
								</div>
								</div>					
				           <label class="col-sm-2 col-form-label text-left " for="">Publication/ granted Number</label>
							<div class="col-sm-4 ">
									<input type="text" maxlength="30" class="form-control" id="pat_publn_grntd_num" name="pat_publn_grntd_num" value="<%=pat_publ_grnt_num%>"  placeholder="Enter publication/ granted Number" >
				           </div>
				   		</div>
				</div> 
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						<label class="col-sm-2 col-form-label text-left " for="">Assignees name(Institute affiliation at the time of application) </label>
							<div class="col-sm-4 ">
									<input type="text" maxlength="30" class="form-control" id="ass_name" name="ass_name" value="<%=ass_name %>"  placeholder="Enter Assignees name(Institute affiliation at the time of application)" >
				           </div>
		   					<label class="col-sm-2 col-form-label required-field" for="documnet_upload">Upload Certificate :(only for granted)</label>
							 <div class="col-sm-4">
										<input type="file" id="XUPLDOC"  name="XUPLDOC" class="">
									<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
									<br> (*Note: Only .pdf, .jpg, .png, & doc files will be allowed. Max Size Can't Exceed 20MB.)</span>
									<a target="_blank" href="../downloadfile?filename=<%=upload%>&folderName=/RSRCH/PATENT/<%=id%>/&fstatus=dwnFileFrmDir"><br>
										<u><%=upload%></u></a>
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
			
			<div class="col-md-12 text-center m-t-20">
					<%if(fstatus.trim().equals("E")) {%>
							<button type="button" class="btn btn-view" id="btnUpdate" name="btnUpdate" onclick="save();">Update</button>
							<button type="button" class="btn btn-view" id="btnBack" name="btnBack" onclick="vldBack('<%=Xpat_type%>','<%=Xpat_status%>','<%=location%>','<%=ddo%>');" >Back</button>
					<%}else{%>
						<button type="button" class="btn btn-view" id="btnSave" name="btnSave" onclick="save();">Save</button>
					<%}%>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				<input type="hidden" name="fstatus" id="fstatus" value="<%=fstatus%>">
				<input type="hidden" name="mid" id="mid" value="<%=id%>">
				<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
			</div>
			</div>
		</div>
   </form>
   <form class="form-horizontal" name="btmfrmPatentD" id="btmfrmPatentD" action="" method="post" autocomplete="off" target="">
    <div class="modal fade recomond-btn" id="myModal" tabindex="-1" role="dialog" aria-labelledby="openModal" aria-hidden="true" style="display: none;">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header"> <button type="button" style="color:#d71414 !important; opacity:.5 ;" class="close" data-dismiss="modal" aria-hidden="true">X</button>
			</div>
			
			  <div class="modal-body">
			   <div class="panel panel-default">
                <div class="panel-heading"><h3 class="panel-title text-right">Searching Criteria</h3></div>
     			<div class="panel-body">
		<!-- -------------------------------Write content inside this modal------------------------------------------- -->
				
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left " for="">Type</label>
							<div class="col-sm-4">
								<select class="form-control" id="Xpat_type" name="Xpat_type">
								<option value="">Select Patent Type</option>
								<option value="N"<%=Xpat_type.equals("N")?"selected":""%>>National</option>
								<option value="I"<%=Xpat_type.equals("I")?"selected":""%>>International</option>
								</select>
				           </div>
					<label class="col-sm-2 col-form-label text-left " for="">Status</label>
							<div class="col-sm-4">
								<select class="form-control" id="Xpat_status" name="Xpat_status">
									<option value="">Select Status</option>
									<option value="P" <%=Xpat_status.equals("P")?"selected":""%>>Published </option>
									<option value="G" <%=Xpat_status.equals("G")?"selected":""%>>Granted </option>
									<option value="A" <%=Xpat_status.equals("A")?"selected":""%>>Applied </option>
									</select>
				           </div>
				       </div>
				</div>
			</div>
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
      				<label for="location" class="col-sm-2 col-form-label text-left">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="location" name="location"  onchange=getDdoDetailbyLocation(this.value);>
									<option value="">Select Location</option>
								</select>
				           </div>
					<label for="ddo" class="col-sm-2 col-form-label text-left" >DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="ddo" name="ddo" >
      						 <option value="">Select DDO</option>
      						</select>      				
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
					<button type="button" class="btn btn-view" id="btnSearch" data-dismiss="modal" onclick="btnSearch">Search</button>
					<button type="button" class="btn btn-view" id="xbtnReset" onclick="">Reset</button> 
				</div>
	
			<!-- -------------------------------End Here (Write content inside this modal)------------------------------------------- -->
			</div> <!-- End of panal body -->
			</div> <!-- End of panal default -->
		   </div> <!-- End of modal body -->
				 
			</div> <!-- End of modal content -->
		</div> <!-- End of modal-dialog -->
	  </div> <!-- End of modal class/id at which modal will be open -->
	  
	</form>
	<%if(!fstatus.equals("E")){ %>
<iframe class="embed-responsive-item" onload="resizeIframe(this)" src="research_patent_mast_l.jsp" name="btmfrmPatentE" id="btmfrmPatentE" frameborder="0" scrolling="no" width="100%" height=""></iframe>

 </div>
  <%} %>
</body>
<script type="text/javascript">
function resizeIframe(iframe) {
    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
    window.requestAnimationFrame(() => resizeIframe(iframe));
}
</script>
</html>
 