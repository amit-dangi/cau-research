<%@page import="com.sits.patent.commercialization_licensing.CommercializationLicensingManager"%>
<%@page import="com.sits.patent.commercialization_licensing.CommercializationLicensingModel"%>
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
    <script type="text/javascript" src="../js/patent/commercialization-licensing.js"></script>
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
	String Xtype = General.checknull(request.getParameter("Xtype"));
	String Xstatus = General.checknull(request.getParameter("Xstatus"));
	String location = General.checknull(request.getParameter("location"));
	String ddo = General.checknull(request.getParameter("ddo"));
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	String OPT_VALUE = "", comm_type="",id="",disabled="",to_date="", app_no="", title="", amt_of_comm="",cau_comm_num="",comm_no="", 
			sub_category="",type="",duration="", upload="",locationCode="",ddoCode="", status="",sec_party="", category="", resh_category="", from_date="";
	ArrayList<CommercializationLicensingModel> arrayList = new ArrayList<CommercializationLicensingModel>();  
	String comm_code = CommercializationLicensingManager.getComm_code();

	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E") || fstatus.trim().equals("R")){
		OPT_VALUE = ApplicationConstants.EDIT;
		id					=General.checknull(request.getParameter("id"));
		arrayList 			= CommercializationLicensingManager.getEditList(id);
		type				=General.checknull(arrayList.get(0).getTyp());
		locationCode  		=General.checknull(arrayList.get(0).getLocation());
		ddoCode				=General.checknull(arrayList.get(0).getDdo());
		app_no				=General.checknull(arrayList.get(0).getApp_no());
		title				=General.checknull(arrayList.get(0).getTitle());
		from_date			=General.checknull(arrayList.get(0).getDate());
		to_date				=General.checknull(arrayList.get(0).getEnd_date());
		duration			=General.checknull(arrayList.get(0).getDuration());
		amt_of_comm			=General.checknull(arrayList.get(0).getAmt_of_comm());
		cau_comm_num		=General.checknull(arrayList.get(0).getCau_comm_num());
		comm_no				=General.checknull(arrayList.get(0).getComm_no());
		sec_party			=General.checknull(arrayList.get(0).getSec_party());
		resh_category		=General.checknull(arrayList.get(0).getResh_category());
		sub_category		=General.checknull(arrayList.get(0).getSub_category());
		comm_type			=General.checknull(arrayList.get(0).getComm_type());
		upload				=General.checknull(arrayList.get(0).getUpload());
	}else if (fstatus.trim().equals("S")){
		OPT_VALUE = ApplicationConstants.SEARCH;
	}
	
	 locationCode= locationCode.equals("")?loct_id:locationCode;
	 ddoCode= ddoCode.equals("")?ddo_id:ddoCode;
	 if(user_status.equals("U")){
			disabled="disabled";
		}
%>
<body onload="getSearchList('<%=fstatus%>','<%=Xtype%>','<%=Xstatus%>','<%=location%>','<%=ddo%>');getRsrchCategory('<%=resh_category%>','rsrchcat');getRsrchSubCategory('<%=category%>','<%=sub_category%>','rsrchsubcat');geteditDD('E', '<%=category%>','<%=sub_category%>','<%=locationCode%>','<%=ddoCode %>');getLocationDetail('<%=locationCode%>','','');">
<script type="text/javascript">
var user_status='<%=user_status%>';
   $(document).ready(function() { 
		$("#date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#date").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		$("#end_date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#end_date").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
 });
 </script>
<div class="container-fluid">
<div id="" class="page-header"><h4><%=ReadProps.getkeyValue("commercialization_licensing.header","sitsResource") %>
     <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
	 </span> 
	 </h4>
 </div>
    <form class="form-horizontal" name="frmCommlicensing" id="frmCommlicensing" action="" method="post" autocomplete="off" target="" enctype="multipart/form-data">
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left required-field" for="">Type</label>
							<div class="col-sm-4">
								<select class="form-control" id="type" name="type">
								<option value="">Select Type</option>
								<option value="N"<%=type.equals("N")?"selected":""%>>National</option>
								<option value="I"<%=type.equals("I")?"selected":""%>>International</option>
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
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo"<%=disabled %> >
      						 <option value="">Select DDO</option>
      						</select>      				
      				</div>     
      				<label class="col-sm-2 col-form-label text-left required-field" for="">Application no</label>
						<div class="col-sm-4">
						<input type="text" maxlength="100" class="form-control" id="app_no" name="app_no" value="<%=app_no %>" onblur="Alpha(this);"  placeholder="Enter Applicant no" >
				       </div> 						
				</div>
				</div>
				</div>
			<div class="form-group" >
			<div class="col-md-12">
					<div class="row" >
      				<label for="location" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Title</label>
      				<div class="col-sm-4 ">
							<input type="text" maxlength="255" class="form-control" id="title" name="title" value="<%=title %>" onblur="isAlphaNumeric(this,'title');"   placeholder="Enter Title" >
				          </div>
				          <label for="ddo" class="col-sm-2 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">CAU Commercialization no</label>
				          <div class="col-sm-4">
			 <% if(fstatus.equals("N")){%>
      				<input type="text" maxlength="30" class="form-control" id="cau_comm_num" name="cau_comm_num" value="<%=comm_code %>" disabled="disabled" onblur="isAlphaNumeric(this,'cau_comm_num');"  placeholder="Enter CAU Commercialization no" >
      				<%}else{ %>
				<input type="text" maxlength="30" class="form-control" id="cau_comm_num" name="cau_comm_num" value="<%=cau_comm_num %>" disabled="disabled" onblur="isAlphaNumeric(this,'cau_comm_num');"  placeholder="Enter CAU Commercialization no" >
      				<%} %> 
 					</div>
				       </div>
				     </div>
				  </div>
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
				           <label class="col-sm-2 col-form-label text-left required-field" for="">Commercialization no</label>
							<div class="col-sm-4 ">
								<input type="text" maxlength="100" class="form-control" id="comm_no" name="comm_no" value="<%=comm_no %>"  onblur="isAlphaNumeric(this,'comm_no');"  placeholder="Enter Commercialization no" >								
				           </div>
				           <label for="ddo" class="col-sm-2 col-form-label text-left">Commercialization type</label>
						<div class="col-sm-4" >
						<select class="form-control" id="comm_type" name="comm_type">
							<option value="">Select Commercialization type</option>
							<option value="i"<%if(comm_type.equals("i")){ %>selected="selected"<%} %>>inclusive</option>
							<option value="e"<%if(comm_type.equals("e")){ %>selected="selected"<%} %>>exclusive</option>
						</select>
						</div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
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
				       <div class="form-group" >
				<div class="col-md-12">
					<div class="row">
						
				            <label class="col-sm-2 col-form-label text-left " for="">Second party</label>
							<div class="col-sm-4 ">
								<input type="text" maxlength="100" class="form-control" id="sec_party" name="sec_party" value="<%=sec_party %>"  placeholder="Enter Second party" >
					   </div>
					   <label class="col-sm-2 col-form-label text-left required-field" for="">Date of MoU of Commercialization</label>
								<div class="col-sm-4 ">
								<div class="input-group date" id="msg-start_date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
									id="date" name="date" placeholder="DD/MM/YYYY" value="<%=from_date %>" >
								</div>
								</div>	
				       </div>
				</div>
			</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
				           
								<label class="col-sm-2 col-form-label text-left required-field" for="">MoU Ending date</label>
								<div class="col-sm-4 ">
								<div class="input-group date" id="msg-start_date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
									id="end_date" name="end_date"  placeholder="DD/MM/YYYY" value="<%=to_date %>" onChange="CalculateDays();" >
								</div>
								</div>		
								<label class="col-sm-2 col-form-label text-left " for="">Duration(In days)</label>
							<div class="col-sm-4 ">
									<input type="text" maxlength="30" class="form-control" id="duration" name="duration" disabled="disabled" value="<%=duration%>"  placeholder="Enter Duration(In days)" >
				           </div>
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >						
				            <label class="col-sm-2 col-form-label text-left " for="">Amount of Commercialization</label>
							<div class="col-sm-4 ">
									<input type="text" maxlength="6" class="form-control" id="amt_of_comm" name="amt_of_comm" value="<%=amt_of_comm %>"  placeholder="Enter Amount of Commercialization" >
				           </div>
				           <label class="col-sm-2 col-form-label required-field" for="documnet_upload">Upload MoU</label>
							 <div class="col-sm-4">
										<input type="file" id="XUPLDOC"  name="XUPLDOC" class="">
									<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
									<br> (*Note: Only .pdf files will be allowed. Max Size Can't Exceed 20MB.)</span>
									<a target="_blank" href="../downloadfile?filename=<%=upload%>&folderName=/RSRCH/Commercialization/<%=id%>/&fstatus=dwnFileFrmDir"><br>
										<u><%=upload%></u></a>
							  </div>  
				   </div>
				</div> 
				</div>
				<% String uploaded[]=upload.split("_") ;
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
							<button type="button" class="btn btn-view" id="btnBack" name="btnBack" onclick="vldBack('<%=Xtype%>','<%=Xstatus%>','<%=location%>','<%=ddo%>');" >Back</button>
					<%}else{%>
						<button type="button" class="btn btn-view" id="btnSave" name="btnSave" onclick="save();">Save</button>
					<%}%>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				<input type="hidden" name="fstatus" id="fstatus" value="<%=fstatus%>">
				<input type="hidden" name="mid" id="mid" value="<%=id%>">
				<input type="hidden" name="upl_id" id="upl_id" value="<%=uploaded[0]%>">
				<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
			</div>
			</div>
		</div>
   </form>
   <form class="form-horizontal" name="frmCommlicensingD" id="frmCommlicensingD" action="" method="post" autocomplete="off" target="">
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
								<select class="form-control" id="Xtype" name="Xtype">
								<option value="">Select Patent Type</option>
								<option value="N"<%=Xtype.equals("N")?"selected":""%>>National</option>
								<option value="I"<%=Xtype.equals("I")?"selected":""%>>International</option>
								</select>
				           </div>
					<label for="location" class="col-sm-2 col-form-label text-left">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="location" name="location"  onchange=getDdoDetailbyLocation(this.value);>
									<option value="">Select Location</option>
								</select>
				           </div>
				       </div>
				</div>
			</div>
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
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
<iframe class="embed-responsive-item" onload="resizeIframe(this)" src="commercialization_licensing_l.jsp" name="frmCommlicensingE" id="frmCommlicensingE" frameborder="0" scrolling="no" width="100%" height=""></iframe>

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
 