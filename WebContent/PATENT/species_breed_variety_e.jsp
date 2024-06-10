<%@page import="com.sits.patent.species_breed_variety.*"%>
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
    <script type="text/javascript" src="../js/patent/species-breed-variety.js"></script>
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
	//String  x_mou_type=General.checknull(request.getParameter("x_mou_type"));
	String  x_location = General.checknull(request.getParameter("x_location"));
	//String  x_meeting_type_id = General.checknull(request.getParameter("x_meeting_type_id"));
	String  x_ddo = General.checknull(request.getParameter("x_ddo"));
	
	
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	String OPT_VALUE = "", s_id="",disabled="",ddo="",
			rel_date="",sts="",relsing_no="",fn_agency="",
					Uploaded_file="",
			signed_on="",pi_name="" ,m_status="";
	ArrayList<SpeciesBreedVarietyModel> arrayList = new ArrayList<SpeciesBreedVarietyModel>(); 
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E") || fstatus.trim().equals("R")){
		OPT_VALUE = ApplicationConstants.EDIT;
		s_id	=General.checknull(request.getParameter("m_id"));
		
		arrayList = SpeciesBreedVarietyManager.getList(s_id,x_location,x_ddo);
		s_id =	General.checknull(arrayList.get(0).getS_id());
	//	System.out.println("s_id----"+s_id);
		location =	General.checknull(arrayList.get(0).getLocation());
		ddo =	General.checknull(arrayList.get(0).getDdo());
		sts =	General.checknull(arrayList.get(0).getSts());
		fn_agency =	General.checknull(arrayList.get(0).getFn_agency());
		
		relsing_no =	General.checknull(arrayList.get(0).getRelsing_no());
		rel_date =	General.checknull(arrayList.get(0).getRel_date());
		Uploaded_file =	General.checknull(arrayList.get(0).getUploaded_file());
		//uploaded_proceeding =	General.checknull(arrayList.get(0).getUploaded_proceeding());
		
		
	}else if (fstatus.trim().equals("S")){
		OPT_VALUE = ApplicationConstants.SEARCH;
	}
	
	 location= location.equals("")?loct_id:location;
	 ddo= ddo.equals("")?ddo_id:ddo;
	 if(user_status.equals("U")){
			disabled="disabled";
		}
%>
<body onload="getSearchList('<%=fstatus%>','<%=x_location%>'); 
	geteditDD('E', '','','<%=location%>','<%=ddo%>');getLocationDetail('<%=location%>','Xlocation',''); 
	getLocationDetail('<%=location%>','x_location');">

<script type="text/javascript">
var user_status='<%=user_status%>';
   $(document).ready(function() { 
		$("#rel_date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#rel_date").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
 });
 </script>
<div class="container-fluid">
 <div id="" class="page-header"><h4><%=ReadProps.getkeyValue("species_variety_released.header","sitsResource") %>
     <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
	 </span> 
	 </h4>
 </div>
    <form class="form-horizontal" name="frmSpeciesE" id="frmSpeciesE" action="" method="post" autocomplete="off" target="" enctype="multipart/form-data">
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
						<label class="col-sm-2 col-form-label text-left required-field" for="">Status</label>
							<div class="col-sm-4">
								<select class="form-control" id="status" name="status" onchange="">
								<option value="">Select Status</option>
								<option value="Applied"<%if(sts.equals("Applied")){ %>selected<%} %>>Applied</option>
								<option value="Granted"<%if(sts.equals("Granted")){ %>selected<%} %>>Granted</option>
							
								
								</select>
				           </div>
					 	<label class="col-sm-2 col-form-label text-left required-field" for="">Funding Agency</label>
							<div class="col-sm-4">
								<select class="form-control" id="fn_agency" name="fn_agency"  >
									<option value="">Select Funding Agency</option>
									<%=QueryUtil.getComboOption("rsrch_funding_agency", "fa_id", "concat(fa_name,' ~ ',fundedby)", fn_agency,"", "fa_name") %>
								</select>
	           				</div> 
					
				       </div>
				     </div>
				  </div>
				  
				  
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
					<label class="col-sm-2 col-form-label">Releasing Number</label>
					<div class="col-sm-4">
					 	<input type="text" maxlength="100" class="form-control" id="rele_no" name="rele_no" onblur="isAlphaNumeric(this,'inst_name');" value="<%=relsing_no%>" onblur="Alpha(this);"  placeholder="Releasing Number" > 
					</div>
					
					<label class="col-sm-2 col-form-label text-left required-field" for="">Release Date</label>
						<div class="col-sm-4 ">
							<div class="input-group date" id="msg-proj_start_date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
								id="rel_date" name="rel_date" placeholder="DD/MM/YYYY" value="<%=rel_date%>" >
							</div>								
			           </div>
		   </div>
				   </div>
				</div> 
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-2 col-form-label required-field" for="documnet_upload">Release/Notification Order Upload</label>
							 <div class="col-sm-4">
										<input type="file" id="XUPLDOC"  name="XUPLDOC" class="">
									<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
									<br> (*Note: Only .pdf files will be allowed. Max Size Can't Exceed 20MB.)</span>
									<a target="_blank" href="../downloadfile?filename=<%=Uploaded_file%>&folderName=/RSRCH/SPECIES/<%=s_id%>/&fstatus=dwnFileFrmDir"><br>
										<u><%=Uploaded_file%></u></a>
							  </div>
							  
					   </div>
				   </div>
				</div>
				<% String uploaded[]=Uploaded_file.split("_") ;
				%>
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
							<button type="button" class="btn btn-view" id="btnBack" name="btnBack" onclick="vldBack('<%=x_location%>');" >Back</button>
					<%}else{%>
						<button type="button" class="btn btn-view" id="btnSave" name="btnSave" onclick="save();">Save</button>
					<%}%>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				<input type="hidden" name="fstatus" id="fstatus" value="<%=fstatus%>">
				<input type="hidden" name="s_id" id="s_id" value="<%=s_id%>">
				<input type="hidden" name="upl_id" id="upl_id" value="<%=uploaded[0]%>">
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
								<select class="form-control" id="x_location" name="x_location" <%=disabled %>>
									<option value="">Select Location</option>
								</select>
				           </div>
				            <label for="ddo" class="col-sm-2 col-form-label text-left required-field">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="x_ddo" name="x_ddo"<%=disabled %> >
      						 <option value="">Select DDO</option>
      						</select>      				
      				</div>
      				</div>
				           </div>
				           </div>
				 <%--    <div class="form-group" >
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
				</div> --%>
						
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
					<button type="button" class="btn btn-view" id="btnSearch" data-dismiss="modal" onclick="getSearchList('<%=fstatus%>','<%=x_location%>');">Search</button>
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
<iframe class="embed-responsive-item" onload="resizeIframe(this)" src="" name="frmSpeciesFrame" id="frmSpeciesFrame" frameborder="0" scrolling="no" width="100%" height=""></iframe>

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
 