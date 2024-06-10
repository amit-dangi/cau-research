<%@page import="com.sits.rsrch.student_research_project_form.StudentResearchProjectFormManager"%>
<%@page import="com.sits.rsrch.student_research_project_form.StudentResearchProjectFormModel"%>
<%@include file="../myCks.jsp"%>
<%@ page import="java.util.ArrayList" %>
<%@page import="com.sits.general.ApplicationConstants"%>
<%@page import="com.sits.general.QueryUtil"%>
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
    <script type="text/javascript" src="../js/patent/student_research_project_form.js"></script>
    <script type="text/javascript" src="../js/commonDropDown.js"></script>
	<script type="text/javascript" src="../js/hrmsCommonDropDown.js"></script>
	 
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
	String location = General.checknull(request.getParameter("location"));
	String  x_mou_type=General.checknull(request.getParameter("x_mou_type"));
	String  x_location = General.checknull(request.getParameter("x_location"));
	
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	String OPT_VALUE = "", fin_year="",stu_status="",m_id="",disabled="",locationCode="",ddoCode="",mou_type="" ,inst_name="" ,coll_type="" ,coll_area="" ,
			signed_on="",validity_period="" ,valid_upto="" ,signed_by="" ,pi_name="" ,m_status="",Uploaded_file=""
					,start_year="",end_year="",stu_name="",ICAR_USID="",cau_regno="",course="",discipline="",research_thrust_area=""
					,research_sub_thrust_area="",proj_type="",proj_title="",objective="",guide_name="",
							external_guide_name="",proj_id=""
			;
	ArrayList<StudentResearchProjectFormModel> arrayList = new ArrayList<StudentResearchProjectFormModel>(); 
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E") || fstatus.trim().equals("R")){
		OPT_VALUE = ApplicationConstants.EDIT;
		m_id				=General.checknull(request.getParameter("m_id"));
		arrayList 			= StudentResearchProjectFormManager.getList(m_id,x_location,"","","",user_status,user_id);
		locationCode		=General.checknull(arrayList.get(0).getLOCATION_CODE());
		ddoCode				=General.checknull(arrayList.get(0).getDDO_ID());
		start_year			=General.checknull(arrayList.get(0).getStart_year());
		end_year			=General.checknull(arrayList.get(0).getEnd_year());
		stu_name			=General.checknull(arrayList.get(0).getStu_name());
		ICAR_USID			=General.checknull(arrayList.get(0).getICAR_USID());
		cau_regno			=General.checknull(arrayList.get(0).getCau_regno());
		discipline			=General.checknull(arrayList.get(0).getDiscipline());
		course				=General.checknull(arrayList.get(0).getCourse());
		proj_type			=General.checknull(arrayList.get(0).getProj_type());
		proj_title			=General.checknull(arrayList.get(0).getProj_title());
		objective			=General.checknull(arrayList.get(0).getObjective());
		guide_name			=General.checknull(arrayList.get(0).getGuide_name());
		external_guide_name =General.checknull(arrayList.get(0).getExternal_guide_name());
		Uploaded_file		=General.checknull(arrayList.get(0).getUploaded_file());
		stu_status			=General.checknull(arrayList.get(0).getStu_status());
		fin_year			=General.checknull(arrayList.get(0).getFin_yr());
		research_thrust_area=General.checknull(arrayList.get(0).getResearch_thrust_area());
		research_sub_thrust_area=General.checknull(arrayList.get(0).getResearch_sub_thrust_area());
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
<body onload="getProgramByDepartment('','<%=course %>','course');getAcademicSession('<%=start_year %>','start_year');getAcademicSession('<%=end_year %>','end_year');getFinanceYrDropdwn('<%=fin_year%>','fin_yr');<%if(fstatus.equals("E")){%>getSubjectByProgram('<%=course%>','<%=discipline%>','','discipline');getSubThrustAreaByThrustArea('<%=research_thrust_area%>','<%=research_sub_thrust_area%>','research_sub_thrust_area','list');getThrustAreaByDiscipline('<%=discipline%>','<%=research_thrust_area%>','research_thrust_area');<%}%>
getSearchList('<%=fstatus%>','<%=x_location%>','<%=x_mou_type%>'); geteditDD('E', '','','<%=locationCode%>','<%=ddoCode%>');getLocationDetail('<%=locationCode%>','Xlocation'); getLocationDetail('<%=x_location%>','x_location');
">
<script type="text/javascript">
var user_status='<%=user_status%>';
   $(document).ready(function() { 
		$("#signed_on").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#signed_on").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
 });
 </script>
<div class="container-fluid">
<div id="" class="page-header"><h4>Students Research Project
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
      				<label for="location" class="col-sm-2 col-form-label text-left required-field">Location</label>
      				<div class="col-sm-4 ">
							<select class="form-control" id="Xlocation" name="Xlocation" <%=disabled %> onchange=getDdoDetailbyLocation(this.value);>
								<option value="">Select Location</option>
							</select>
		             </div>
		             
		              <label for="ddo" class="col-sm-2 col-form-label text-left required-field">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo"<%=disabled %> >
      						 <option value="">Select DDO</option>
      						</select>      				
      				</div>  
		             
				</div>
				</div>
				</div>
				<div class="form-group" >
			<div class="col-md-12">
					<div class="row" >
				<label class="col-sm-2 col-form-label required-field">Start Year</label>
					<div class="col-sm-4">
					<select class="form-control" id="start_year" name="start_year">
						<option value="">Select Start Year</option>
					<%--  <select class="form-control" id="fin_yr"name="fin_yr">
						<option value="">Select Financial Year</option>
					</select> 
					</div> --%>
					</select> 
					</div>
				<label class="col-sm-2 col-form-label required-field">End Year</label>
					<div class="col-sm-4">
					<select class="form-control" id="end_year" name="end_year">
						<option value="">Select End Year</option>
					</select> 
					</div>
					</div>
					</div>
					</div>
					
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row" >
						<label for="" class="col-sm-2 col-form-label text-left required-field">Status</label>
	      				<div class="col-sm-4">
								<select class="form-control" id="status" name="status">
								<option value="">Select Status</option>
								<option value="Ongoing"<%if(stu_status.equals("Ongoing")){ %>selected="selected"<%} %>>Ongoing</option>
								<option value="Completed"<%if(stu_status.equals("Completed")){%>selected="selected"<%}%>>Completed</option>
								</select>
								
			           </div>
					 	<label class="col-sm-2 col-form-label text-left required-field" for="">Student Name</label>
						<div class="col-sm-4">
						<input type="text" maxlength="100" class="form-control" id="stu_name" name="stu_name" value="<%=stu_name%>" onblur="Alpha(this);"  placeholder="Enter Student Name" >
				       	</div>
					
				       </div>
				     </div>
				  </div>
				  
				  
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
			<label class="col-sm-2 col-form-label text-left " for="">ICAR USID</label>
						<div class="col-sm-4">
						<input type="text" maxlength="100" class="form-control" id="ICAR_USID" name="ICAR_USID" value="<%=ICAR_USID%>"   placeholder="Enter ICAR USID" >
				       	</div>
					   <label class="col-sm-2 col-form-label text-left " for="">CAU Registration no.</label>
						<div class="col-sm-4">
						<input type="text" maxlength="100" class="form-control" id="cau_regno" name="cau_regno" value="<%=cau_regno%>"   placeholder="Enter CAU Registration no." >
				       	</div>
					   </div>
				   </div>
				</div> 
				<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
					<label for="" class="col-sm-2 col-form-label text-left ">Course/Degree</label>
	      				<div class="col-sm-4">
								<select class="form-control" id="course" name="course" onchange="getSubjectByProgram(this.value,'<%=discipline%>','','discipline');">
								</select>
			           </div>
						<label for="" class="col-sm-2 col-form-label text-left ">Discipline/Subject</label>
	      				<div class="col-sm-4">
								<select class="form-control" id="discipline" name="discipline" onchange="getThrustAreaByDiscipline(this.value,'<%=research_thrust_area%>','research_thrust_area');">
								<option value="">Select Discipline</option>
								</select>
			            </div>
				       </div>
				</div>
			</div>
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						<label for="" class="col-sm-2 col-form-label text-left ">Research Thrust Area</label>
	      				<div class="col-sm-4">
								<select class="form-control" id="research_thrust_area" name="research_thrust_area" onchange="getSubThrustAreaByThrustArea(this.value,'<%=research_sub_thrust_area%>','research_sub_thrust_area','list');">
								<option value="">Select Research Thrust Area</option>
								</select>
			            </div>
			         	  <label for="" class="col-sm-2 col-form-label text-left ">Research Sub Thrust Area</label>
										<div class="col-sm-4">
											<div class="" id="research_sub_thrust_area">
												<ul class="form-control checkBoxClass"
													style="height: 85px; padding-top: 0px; overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;">
													<li>List Of Research Sub Thrust Area(s)</li>
												</ul>
											</div>
										</div>
					   </div>
				   </div>
				</div>
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						<label for="" class="col-sm-2 col-form-label text-left ">Project type</label>
	      				<div class="col-sm-4">
								<select class="form-control" id="proj_type" name="proj_type">
								<option value="">Select Project type</option>
								<option value="ICAR Education Division Project"<%=proj_type.equals("ICAR Education Division Project")?"selected":""%>>ICAR Education Division Project</option>
								<option value="Others"<%=proj_type.equals("Others")?"selected":""%>>Others</option>
								</select>
			            </div>
					  <label class="col-sm-2 col-form-label text-left " for="">Project Title</label>
						<div class="col-sm-4">
						<input type="text" maxlength="255" class="form-control" id="proj_title" name="proj_title" value="<%=proj_title%>"   placeholder="Enter Project Title" >
				       	</div>
					   </div>
				   </div>
			</div>
			
			<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						<label class="col-sm-2 col-form-label text-left " for="">Objective</label>
						<div class="col-sm-4">
						<textarea type="text" maxlength="500" class="form-control" id="objective" name="objective" value="<%=objective%>" style="max-height:120px;resize:vertical;height:40px;"  placeholder="Enter Objective" ></textarea>
				       	</div>
					  <label class="col-sm-2 col-form-label text-left " for="">Guide Name</label>
						<div class="col-sm-4">
						<input type="text" maxlength="255" class="form-control" id="guide_name" name="guide_name" value="<%=guide_name%>"   placeholder="Enter Guide Name" >
				       	</div>
					   </div>
				   </div>
			</div>
			
			<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						 <label class="col-sm-2 col-form-label text-left " for="">External Guide Name</label>
						<div class="col-sm-4">
						<input type="text" maxlength="255" class="form-control" id="external_guide_name" name="external_guide_name" value="<%=external_guide_name%>"   placeholder="Enter External Guide Name" >
				       	</div>
					      <label class="col-sm-2 col-form-label " for="documnet_upload">Upload Abstract/Achievements(PDF) </label>
							 <div class="col-sm-4">
										<input type="file" id="XUPLDOC"  name="XUPLDOC" class="">
									<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
									<br> (*Note: Only .pdf files will be allowed. Max Size Cant Exceed 20MB.)</span>
									<a target="_blank" href="../downloadfile?filename=<%=Uploaded_file%>&folderName=/RSRCH/STUFORM/<%=m_id%>/&fstatus=dwnFileFrmDir"><br>
										<u><%=Uploaded_file%></u></a>
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
							<button type="button" class="btn btn-view" id="btnBack" name="btnBack" onclick="vldBack('<%=x_location%>','<%=x_mou_type%>');" >Back</button>
					<%}else{%>
						<button type="button" class="btn btn-view" id="btnSave" name="btnSave" onclick="save();">Save</button>
					<%}%>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				<input type="hidden" name="fstatus" id="fstatus" value="<%=fstatus%>">
				<input type="hidden" name="m_id" id="m_id" value="<%=m_id%>">
				<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
			</div>
			</div>
		</div>
   </form>
   <form class="form-horizontal" name="moufrmModal" id="moufrmModal" action="" method="post" autocomplete="off" target="">
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
      				<label for="location" class="col-sm-2 col-form-label text-left">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="x_location" name="x_location" onchange="getDdoDetailbyLocation(this.value,'','x_ddo');">
									<option value="">Select Location</option>
								</select>
				           </div>
					  <label for="ddo" class="col-sm-2 col-form-label text-left required-field">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="x_ddo" name="x_ddo">
      						 <option value="">Select DDO</option>
      						</select>      				
      				</div>  
		             
				</div>
				</div>
				</div>
				
				
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row" >
						
					 	<label class="col-sm-2 col-form-label text-left required-field" for="">Student Name</label>
						<div class="col-sm-4">
						<input type="text" maxlength="100" class="form-control" id="x_stu_name" name="x_stu_name" onblur="Alpha(this);"  placeholder="Enter Student Name" >
				       	</div>
				       	
				       	<label class="col-sm-2 col-form-label text-left required-field" for="">ICAR USID</label>
						<div class="col-sm-4">
						<input type="text" maxlength="100" class="form-control" id="x_ICAR_USID" name="x_ICAR_USID"    placeholder="Enter ICAR USID" >
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
					<button type="button" class="btn btn-view" id="btnSearch" data-dismiss="modal" onclick="getSearchList('<%=fstatus%>','<%=x_location%>','<%=x_mou_type%>');">Search</button>
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
<iframe class="embed-responsive-item" onload="resizeIframe(this)" src="" name="mouMoaFormLFrame" id="mouMoaFormLFrame" frameborder="0" scrolling="no" width="100%" height=""></iframe>

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
 