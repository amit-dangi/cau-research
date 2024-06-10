<%@page import="com.sits.rsrch.patent_mou_moa_form.MouMoaformManager"%>
<%@page import="com.sits.rsrch.patent_mou_moa_form.MouMoaformModel"%>
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
    <script type="text/javascript" src="../js/patent/mou_moa_form.js"></script>
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
	 .boxAlign{
		margin: 5px 0 0 !important;
		margin-right: 5px !important;
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
	String OPT_VALUE = "",academic="",research="",extension="",Other="", m_id="",disabled="",locationCode="",ddoCode="",mou_type="" ,inst_name="" ,coll_type="" ,coll_area="" ,
			signed_on="",validity_period="" ,valid_upto="" ,signed_by="" ,pi_name="" ,m_status="",Uploaded_file="";
	ArrayList<MouMoaformModel> arrayList = new ArrayList<MouMoaformModel>(); 
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
	}else if (fstatus.trim().equals("E") || fstatus.trim().equals("R")){
		OPT_VALUE = ApplicationConstants.EDIT;
		m_id				=General.checknull(request.getParameter("m_id"));
		arrayList = MouMoaformManager.getList(m_id,x_location,x_mou_type);
		locationCode=	General.checknull(arrayList.get(0).getLOCATION_CODE());
		ddoCode=	General.checknull(arrayList.get(0).getDDO_ID());
		mou_type=	General.checknull(arrayList.get(0).getMou_type());
		coll_type=	General.checknull(arrayList.get(0).getColl_type());
	 if(coll_type.contains("Academic")){
		 academic="checked";
		 }
		 if(coll_type.contains("Research")){
			 research="checked";
		 }
		 if(coll_type.contains("Extension")){
			 extension="checked";
		 }
		 if(coll_type.contains("Other")){
			 Other="checked";
		 } 
		m_status=	General.checknull(arrayList.get(0).getM_status());
		inst_name=	General.checknull(arrayList.get(0).getInst_name());
		coll_area=	General.checknull(arrayList.get(0).getColl_area());
		signed_on=	General.checknull(arrayList.get(0).getSigned_on());
		validity_period=General.checknull(arrayList.get(0).getValidity_period());
		valid_upto=	General.checknull(arrayList.get(0).getValid_upto());
		signed_by=	General.checknull(arrayList.get(0).getSigned_by());
		pi_name=	General.checknull(arrayList.get(0).getPi_name());
		Uploaded_file=General.checknull(arrayList.get(0).getUploaded_file());
		
	}else if (fstatus.trim().equals("S")){
		OPT_VALUE = ApplicationConstants.SEARCH;
	}
	
	 locationCode= locationCode.equals("")?loct_id:locationCode;
	 ddoCode= ddoCode.equals("")?ddo_id:ddoCode;
	 if(user_status.equals("U")){
			disabled="disabled";
		}
%>
<body onload="getSearchList('<%=fstatus%>','<%=x_location%>','<%=x_mou_type%>'); geteditDD('E', '','','<%=locationCode%>','<%=ddoCode%>');getLocationDetail('<%=locationCode%>','Xlocation',''); getLocationDetail('<%=x_location%>','x_location');">
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
<div id="" class="page-header"><h4><%=ReadProps.getkeyValue("research_mou_moa_form.header","sitsResource") %>
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
						<label class="col-sm-2 col-form-label text-left required-field" for="">Type of MOU/MOA</label>
							<div class="col-sm-4">
								<select class="form-control" id="mou_type" name="mou_type">
								<option value="">Select MOU/MOA Type</option>
								<option value="National"<%=mou_type.equals("National")?"selected":""%>>National</option>
								<option value="International"<%=mou_type.equals("International")?"selected":""%>>International</option>
								</select>
				           </div>
					 	<label class="col-sm-2 col-form-label text-left required-field" for="">Name of Institute/University/Company/others</label>
						<div class="col-sm-4">
						<input type="text" maxlength="100" class="form-control" id="inst_name" name="inst_name" onblur="isAlphaNumeric(this,'inst_name');" value="<%=inst_name%>" onblur="Alpha(this);"  placeholder="Name of Institute/University/Company/others" >
				       	</div>
					
				       </div>
				     </div>
				  </div>
				  
				  
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
					<%-- <label class="col-sm-2 col-form-label text-left required-field" for="">Type of collaboration</label>
							<div class="col-sm-4">
								<select class="form-control" id="coll_type" name="coll_type">
								<option value="">Select collaboration Type</option>
								<option value="Academic"<%=coll_type.equals("Academic")?"selected":""%>>Academic</option>
								<option value="Research"<%=coll_type.equals("Research")?"selected":""%>>Research</option>
								<option value="Extension"<%=coll_type.equals("Extension")?"selected":""%>>Extension</option>
								<option value="Other"<%=coll_type.equals("Other")?"selected":""%>>Other</option>
								</select>
				           </div> --%>
				           	<label class="col-sm-2 col-form-label text-left required-field" for="coll_type">Type of collaboration</label>
							<div class="col-sm-4">
                    <div class="coll_type" >
								<ul class="form-control" style="height: 85px; padding-top:0px;overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;">
								      <option value="">Select collaboration Type</option>
									 <li><input type="checkbox" class="boxAlign" name="boxAlign1" id="coll_type" value="Academic"<%=academic%>>Academic</li>
									 <li><input type="checkbox" class="boxAlign" name="boxAlign1" id="coll_type" value="Research"<%=research%>>Research</li>
									 <li><input type="checkbox" class="boxAlign" name="boxAlign1" id="coll_type" value="Extension"<%=extension%>>Extension</li>
									 <li><input type="checkbox" class="boxAlign" name="boxAlign1" id="coll_type" value="Other"<%=Other%>>Other</li>
								</ul>
							</div>	  
			</div>
				           
			 			<label for="" class="col-sm-2 col-form-label text-left required-field">Area of Collaboration</label>
	      				<div class="col-sm-4">
	      				<input type="text" maxlength="250" class="form-control" id="coll_area" name="coll_area" onblur="isAlphaNumeric(this,'coll_area');" value="<%=inst_name%>" value="<%=coll_area%>"  placeholder="Area of Collaboration" >
	      				</div>
					   </div>
				   </div>
				</div> 
				<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left required-field" for="">Signed on</label>
						<div class="col-sm-4 ">
							<div class="input-group date" id="msg-proj_start_date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
								id="signed_on" name="signed_on" placeholder="DD/MM/YYYY" value="<%=signed_on%>" >
							</div>								
			           </div>
						<label class="col-sm-2 col-form-label text-left required-field" for="">Validity period (Years)</label>
						<div class="col-sm-4">
						<input type="number" min="0" onkeypress="if (this.value.length > 1) return false;" class="form-control" id="validity_period" name="validity_period" value="<%=validity_period%>" onblur="getvalidityupto(this.value);" placeholder="Validity period in years" >
			           </div>
				       </div>
				</div>
			</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
							<label class="col-sm-2 col-form-label text-left required-field" for=" ">Valid Upto</label>
							<div class="col-sm-4">
							<input type="text"  min="0"  class="form-control" id="valid_upto" name="valid_upto" value="<%=valid_upto%>"  placeholder="Validity Upto" readonly>
				           </div> 
							<label class="col-sm-2 col-form-label text-left required-field" for="">Signed by</label>
							<div class="col-sm-4">
							<input type="text" maxlength="100" class="form-control" id="signed_by" name="signed_by" value="<%=signed_by%>"  placeholder="Signed by" >
				           </div>
				           
					   </div>
				   </div>
				</div>
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           	<label class="col-sm-2 col-form-label text-left" for="">PI Name</label>
							<div class="col-sm-4">
							<input type="text" maxlength="100" class="form-control" id="pi_name" name="pi_name" value="<%=pi_name%>" onblur="isAlphaNumeric(this,'pi_name');" placeholder="PI Name" >
				           	</div>
				           <label class="col-sm-2 col-form-label required-field" for="documnet_upload">Upload Signed MOU/MOA (PDF) </label>
							 <div class="col-sm-4">
										<input type="file" id="XUPLDOC"  name="XUPLDOC" class="">
									<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
									<br> (*Note: Only .pdf files will be allowed. Max Size Cant Exceed 20MB.)</span>
									<a target="_blank" href="../downloadfile?filename=<%=Uploaded_file%>&folderName=/RSRCH/MOU/<%=m_id%>/&fstatus=dwnFileFrmDir"><br>
										<u><%=Uploaded_file%></u></a>
							  </div>
					   </div>
				   </div>
				</div>
				
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
							  <label class="col-sm-2 col-form-label text-left required-field" for="">Status</label>
							<div class="col-sm-4">
								<select class="form-control" id="m_status" name="m_status">
								<option value="">Select Status</option>
								<option value="Initiated"<%=m_status.equals("Initiated")?"selected":""%>>Initiated</option>
								<option value="Completed"<%=m_status.equals("Completed")?"selected":""%>>Completed</option>
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
								<select class="form-control" id="x_location" name="x_location" <%=disabled %>>
									<option value="">Select Location</option>
								</select>
				           </div>
					   <label class="col-sm-2 col-form-label text-left " for="">Type of MOU/MOA</label>
							<div class="col-sm-4">
								<select class="form-control" id="x_mou_type" name="x_mou_type">
								<option value="">Select MOU/MOA Type</option>
								<option value="National"<%=x_mou_type.equals("National")?"selected":""%>>National</option>
								<option value="International"<%=x_mou_type.equals("International")?"selected":""%>>International</option>
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
 