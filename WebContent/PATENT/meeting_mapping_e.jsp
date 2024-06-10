<%@page import="com.sits.rsrch.meeting_mapping.MeetingMappingManager"%>
<%@page import="com.sits.rsrch.meeting_mapping.MeetingMappingModel"%>
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
    <script type="text/javascript" src="../js/patent/meeting-mapping.js"></script>
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
	String  x_meeting_type_id = General.checknull(request.getParameter("x_meeting_type_id"));
	String  x_ddo = General.checknull(request.getParameter("x_ddo"));
	
	
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	String OPT_VALUE = "", m_id="",disabled="",locationCode="",ddoCode="",
			meeting_type_id="",others="",fin_yr="",meeting_date="",
					Uploaded_file="",uploaded_proceeding="",
			
			//not used
			mou_type="" ,inst_name="" ,coll_type="" ,coll_area="" ,
			signed_on="",validity_period="" ,valid_upto="" ,signed_by="" ,pi_name="" ,m_status="";
	ArrayList<MeetingMappingModel> arrayList = new ArrayList<MeetingMappingModel>(); 
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E") || fstatus.trim().equals("R")){
		OPT_VALUE = ApplicationConstants.EDIT;
		m_id				=General.checknull(request.getParameter("m_id"));
		arrayList = MeetingMappingManager.getList(m_id,x_location,x_meeting_type_id,x_ddo);
		locationCode=	General.checknull(arrayList.get(0).getLOCATION_CODE());
		ddoCode=	General.checknull(arrayList.get(0).getDDO_ID());
		meeting_type_id=	General.checknull(arrayList.get(0).getMeeting_type_id());
		others=	General.checknull(arrayList.get(0).getOthers());
		fin_yr=	General.checknull(arrayList.get(0).getFin_yr());
		meeting_date=	General.checknull(arrayList.get(0).getMeeting_date());
		Uploaded_file=	General.checknull(arrayList.get(0).getUploaded_file());
		uploaded_proceeding=	General.checknull(arrayList.get(0).getUploaded_proceeding());
		
		
	}else if (fstatus.trim().equals("S")){
		OPT_VALUE = ApplicationConstants.SEARCH;
	}
	
	 locationCode= locationCode.equals("")?loct_id:locationCode;
	 ddoCode= ddoCode.equals("")?ddo_id:ddoCode;
	 if(user_status.equals("U")){
			disabled="disabled";
		}
%>
<body onload="getSearchList('<%=fstatus%>','<%=x_location%>','<%=x_mou_type%>','<%=x_meeting_type_id%>'); 
	geteditDD('E', '','','<%=locationCode%>','<%=ddoCode%>');getLocationDetail('<%=locationCode%>','Xlocation',''); 
	getLocationDetail('<%=x_location%>','x_location');getFinanceYrDropdwn('<%=fin_yr%>','fin_yr');">

<script type="text/javascript">
var user_status='<%=user_status%>';
   $(document).ready(function() { 
		$("#meeting_date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#meeting_date").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
 });
 </script>
<div class="container-fluid">
<div id="" class="page-header"><h4>Meeting Mapping 
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
						<label class="col-sm-2 col-form-label text-left required-field" for="">Meeting type</label>
							<div class="col-sm-4">
								<select class="form-control" id="meeting_type_id" name="meeting_type_id" onchange="showothers(this.value);">
								<%=QueryUtil.getComboOption("rsrch_meeting_type_mast", "type_id", "type",meeting_type_id,"", "type_id") %>
								</select>
				           </div>
					 	<label class="col-sm-2 col-form-label text-left " for="">If Others type</label>
						<div class="col-sm-4">
						<input type="text" maxlength="100" class="form-control" id="others" name="others" value="<%=others%>" onblur="Alpha(this);"  placeholder="Enter others" readonly>
				       	</div>
					
				       </div>
				     </div>
				  </div>
				  
				  
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
					<label class="col-sm-2 col-form-label required-field">Financial Year</label>
					<div class="col-sm-4">
					 <select class="form-control" id="fin_yr"name="fin_yr" >
						<option value="">Select Financial Year</option>
					</select> 
					</div>
					
					<label class="col-sm-2 col-form-label text-left required-field" for="">Meeting Date</label>
						<div class="col-sm-4 ">
							<div class="input-group date" id="msg-proj_start_date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
								id="meeting_date" name="meeting_date" placeholder="DD/MM/YYYY" value="<%=meeting_date%>" >
							</div>								
			           </div>
		   </div>
				   </div>
				</div> 
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-2 col-form-label required-field" for="documnet_upload">Upload Agenda Items/Notes </label>
							 <div class="col-sm-4">
										<input type="file" id="XUPLDOC"  name="XUPLDOC" class="">
									<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
									<br> (*Note: Only .pdf files will be allowed. Max Size Can't Exceed 20MB.)</span>
									<a target="_blank" href="../downloadfile?filename=<%=Uploaded_file%>&folderName=/RSRCH/MEETING/<%=m_id%>/&fstatus=dwnFileFrmDir"><br>
										<u><%=Uploaded_file%></u></a>
							  </div>
							  
							  <label class="col-sm-2 col-form-label required-field" for="documnet_upload">Upload Proceedings</label>
							 <div class="col-sm-4">
										<input type="file" id="XUPLDOC2"  name="XUPLDOC2" class="">
									<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
									<br> (*Note: Only .pdf files will be allowed. Max Size Can't Exceed 20MB.)</span>
									<a target="_blank" href="../downloadfile?filename=<%=Uploaded_file%>&folderName=/RSRCH/MEETING/<%=m_id%>/&fstatus=dwnFileFrmDir"><br>
										<u><%=uploaded_proceeding%></u></a>
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
							<button type="button" class="btn btn-view" id="btnBack" name="btnBack" onclick="vldBack('<%=x_location%>','<%=x_mou_type%>','<%=x_meeting_type_id%>');" >Back</button>
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
								<select class="form-control" id="x_location" name="x_location">
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
					<div class="row"> 
					   <label class="col-sm-2 col-form-label text-left " for="">Meeting type</label>
							<div class="col-sm-4">
								<select class="form-control" id="x_meeting_type_id" name="x_meeting_type_id" onchange="showothers(this.value);">
								 <option value="">Select Meeting type</option>
								<%=QueryUtil.getComboOption("rsrch_meeting_type_mast", "type_id", "type","","", "type_id") %>
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
					<button type="button" class="btn btn-view" id="btnSearch" data-dismiss="modal" onclick="getSearchList('<%=fstatus%>','<%=x_location%>','<%=x_mou_type%>','<%=x_meeting_type_id%>',);">Search</button>
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
 