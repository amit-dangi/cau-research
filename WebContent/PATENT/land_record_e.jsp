<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.sits.patent.land_record.*" %>
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
    <script type="text/javascript" src="../js/patent/land-record.js"></script>
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
	     
		<style>
  	table.tableGrid th { background-color: #bb9c2b !important;
	 /*    border: 1px solid #1ea2cd !important; */
	    color: #0c426f;
     }
    .dataEntryDiv{
    overflow-y: unset;
  min-height: unset; 
     max-height: unset; 
    width: 100%;
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
  	<script type="text/javascript">

   $(document).ready(function() { 
		$(".payment_date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$(this).datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
		
 });
 </script>
</head>
<%			
	int k=0;
	String fstatus = General.checknull(request.getParameter("fstatus"));
	String location = General.checknull(request.getParameter("location"));
	String  x_patta=General.checknull(request.getParameter("x_patta"));
	String  x_location = General.checknull(request.getParameter("x_location"));
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	String OPT_VALUE = "", ld_id="",dept="",disabled="",locationCode="",ddoCode="",patta_no="",other="",Uploaded_file="";
	ArrayList<LandRecordModel> arrayList = new ArrayList<LandRecordModel>(); 
	JSONArray json=	 new JSONArray(); 
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E") || fstatus.trim().equals("R")){
		OPT_VALUE = ApplicationConstants.EDIT;
		ld_id				=General.checknull(request.getParameter("ld_id"));
		
		json=LandRecordManager.viewRecordDetails(ld_id);
		arrayList = LandRecordManager.getList(ld_id,x_location,x_patta);
		locationCode=	General.checknull(arrayList.get(0).getLOCATION_CODE());
		ddoCode=	General.checknull(arrayList.get(0).getDDO_ID());
		patta_no=	General.checknull(arrayList.get(0).getPatta_no());
		other=	General.checknull(arrayList.get(0).getOther());
		Uploaded_file=General.checknull(arrayList.get(0).getUploaded_file());
		dept=General.checknull(arrayList.get(0).getDept());
	}else if (fstatus.trim().equals("S")){
		OPT_VALUE = ApplicationConstants.SEARCH;
	}
	
	 locationCode= locationCode.equals("")?loct_id:locationCode;
	 ddoCode= ddoCode.equals("")?ddo_id:ddoCode;
	 if(user_status.equals("U")){
			disabled="disabled";
		}
%>
<body onload="getDptByEmployee('<%=sess_emp_id%>','<%=user_status%>','<%=dept%>');getSearchList('<%=x_location %>','<%=fstatus%>'); geteditDD('E', '','','<%=locationCode%>','<%=ddoCode%>');getLocationDetail('<%=locationCode%>','Xlocation',''); getLocationDetail('<%=x_location%>','x_location');">
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
<div id="" class="page-header"><h4>Land Records
     <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
	 </span> 
	 </h4>
 </div>
    <form class="form-horizontal" name="frmLandRecordE" id="frmLandRecordE" action="" method="post" autocomplete="off" target="" enctype="multipart/form-data">
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
      				<label for="location" class="col-sm-2 col-form-label text-left required-field">Location</label>
      				<div class="col-sm-4 ">
							<select class="form-control" id="Xlocation" name="Xlocation" <%=disabled %> onchange=getDdoDetailbyLocation(this.value);>
								<option value="L">Select Location</option>
							</select>
		             </div>
		             
		              <label for="ddo" class="col-sm-2 col-form-label text-left required-field">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo"<%=disabled %> >
      						 <option value="D">Select DDO</option>
      						</select>      				
      				</div>  
		             
				</div>
				</div>
				</div>
				
					<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						 <label for="" class="col-sm-2 col-form-label text-left required-field">Department</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="deptId" name="deptId"<%=disabled %> >
      						 <option value="">Select Department</option>
      						</select>      				
      				</div>  
							<label class="col-sm-2 col-form-label text-left required-field" for="">Patta No.</label>
							<div class="col-sm-4">
							<input type="text" maxlength="200" class="form-control" id="XPATTA" name="XPATTA" value="<%=patta_no %>"  placeholder="Enter Patta No">
				           </div> 
				              
					   </div>
				   </div>
				</div>
				           <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
							<label class="col-sm-2 col-form-label text-left" for="">Others</label>
							<div class="col-sm-4">
							<input type="text" maxlength="200" class="form-control" id="XOTHER" name="XOTHER" value="<%=other %>"  placeholder="Others" >
				           </div>
				           
					   </div>
				   </div>
				</div>
				
						<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						 <label class="col-sm-2 col-form-label required-field" for="documnet_upload">Upload Document </label>
							<div class="col-sm-4">
                <%if(fstatus.equals("N")) { %> 
                   <input type="file" class="" id="XUPLDOC" name="XUPLDOC" value="" placeholder=""> 
                   
                  <%}else{ %>						
						<span class="fileUpload" id="file_Upload">
							<a target="_blank" href="../downloadfile?filename=<%=Uploaded_file%>&folderName=RSRCH/LAND_RECORD/<%=ld_id%>"> 
									<u><%=Uploaded_file%></u> </a>&ensp; 
									<% if(!ld_id.equals("")&&(!Uploaded_file.equals(""))){  %>
									<span style="color: red;" id="" onclick="deletesavefile('<%=ld_id%>','<%=Uploaded_file%>');"> <i class="fa fa-times-circle"></i>
									</span> 
									<%}else{ %>							
										<input type="file" class="form-control" id="XUPLDOC" name="XUPLDOC" value="<%=Uploaded_file%>" onblur="imgcheck(this.id)">
											<%} %>
								<%} %>
								<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
							(*Note: Only .pdf files will be allowed. Max Size Cant Exceed 20MB.)</span>	
               </div>
					   </div>
				   </div>
				</div>
		<div class="col-md-12 table-responsive" style="padding: 0px;">
	<div id="" class="dataEntryDiv" style="padding: 0px;  overflow-y: unset !important; 
           width:100%;">
	<table id="searchTable1" border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " >
	<thead class="theadnew">
		<tr>
			<th class="text-center" style="text-align:center;width:1%;">S.no</th>
			<th class="text-center" style="text-align:center;width:4%;">Revenue Payment Date</th>
 			<th class="text-center" style="text-align:center;width:4%;">Upload Payment Receipt</th>
 			<th class="text-center" style="text-align:center;width:4%;">Delete</th>
 			<!-- <th class="text-center" style="text-align:center;width:4%;">Remarks</th>
 			 -->
	 </tr>
	</thead>
		<tbody id="END1" class="tbodynew landRecord">
		<%if(fstatus.trim().equals("N")||json.size()==0) {%>
		<tr>
		 <td  class="text-center w-10" id="landRecord_1" name="landRecord_1">1</td>
			  	<td  class="text-center"><div class="input-group date" id="msg-proj_start_date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar payment_date" 
									id="payment_date_1" name="payment_date_1" placeholder="DD/MM/YYYY" value="" >
								</div>	</td> 
 			    <td  class="text-center"> <input type="file" class="" id="XUPLDOC_1" name="XUPLDOC_1" value="" placeholder=""> </td>
			  	<td class="colr-red-p text-center" style="width:5%;color:red;"><span id="" onclick="deleteDetailsdata(-1,1,this,'itm' );"><i class="fa fa-trash p-l-3"></i>Delete</span>
			  <input type="hidden" id="detId_1" value=""/>
			  	</td>
		</tr>
		<%} %>
		</tbody>
	</table>
	</div>	
	</div>
	<div class="col-md-12 text-right m-t-20">
				<button type="button" class="btn btn-view" id="addLandRecord" onclick="">Add More</button>
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
							<button type="button" class="btn btn-view" id="btnBack" name="btnBack" onclick="vldBack('<%=x_location%>','<%=x_patta%>');" >Back</button>
					<%}else{%>
						<button type="button" class="btn btn-view" id="btnSave" name="btnSave" onclick="save();">Save</button>
					<%}%>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				<input type="hidden" name="fstatus" id="fstatus" value="<%=fstatus%>">
				<input type="hidden" name="ld_id" id="ld_id" value="<%=ld_id%>">
				<input type="hidden" name="count" id="count" value="1">
				<input type='hidden' id="jsonddata" name="jsonddata" value='<%=json%>' />
				<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
			</div>
			</div>
		</div>
   </form>
   <form class="form-horizontal" name="frmLandRecordD" id="frmLandRecordD" action="" method="post" autocomplete="off" target="">
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
								<select class="form-control" id="x_location" name="x_location" >
									<option value="">Select Location</option>
								</select>
				           </div>
					  	<label class="col-sm-2 col-form-label text-left required-field" for="">Patta No.</label>
							<div class="col-sm-4">
							<input type="text" class="form-control" id="X_PATTA" name="X_PATTA" placeholder="Enter Patta No">
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
					<button type="button" class="btn btn-view" id="X_BTNSEARCH" name="X_BTNSEARCH" data-dismiss="modal" onclick="">Search</button>
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

<iframe class="embed-responsive-item" onload="resizeIframe(this)" src="" name="LandRecordFrame" id="LandRecordFrame" frameborder="0" scrolling="no" width="100%" height=""></iframe>

 </div>
 
</body>
<script type="text/javascript">
function resizeIframe(iframe) {
    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
    window.requestAnimationFrame(() => resizeIframe(iframe));
}
</script>
</html>
 