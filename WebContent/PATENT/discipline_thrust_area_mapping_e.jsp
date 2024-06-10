<%@page import="com.sits.patent.discipline_thrust_area_mapping.DisciplineThrustAreaMappingManager"%>
<%@page import="com.sits.patent.discipline_thrust_area_mapping.DisciplineThrustAreaMappingModel"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sits.general.ApplicationConstants"%>
<%-- <%@page import="com.sits.examination.common_master.discipline_course_master.CourseMasterManager"%>
<%@page import="com.sits.examination.common_master.discipline_course_master.CourseMasterModel"%> --%>
<%@page import="com.sits.general.ReadProps"%>
<%@page import="com.sits.general.QueryUtil"%>
<%@page import="com.sits.general.General"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../myCks.jsp"%>  
<!DOCTYPE html> 
<html lang="en">
<head>
	
  <meta name="viewport" content="width=device-width, initial-scale=1">	
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="Expires" content="-1" />
  <title></title>
 
	<title><%=ReadProps.getkeyValue("welcome.header", "sitsResource") %></title>
	<link rel="stylesheet" href="../assets/sits/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css">
	<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
	<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  	

	<script type="text/javascript" src="../js/aes/AesUtil.js"></script>
	<script type="text/javascript" src="../js/aes/crypto.js"></script>
	<script type="text/javascript" src="../js/commonDropDown.js"></script>
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
	<script type="text/javascript" src="../js/patent/discipline-thrust-area-mapping.js"></script>
	
	<%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
	%>
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />	
	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	  
</head>

	<%
	
	String OPT_VALUE="", OPT_TYP = "",cr_id="";
	int cnt=0;
	String discipline="",thrust_area="",sub_thrust_area="",pgm="",pgmlist="";
 	OPT_TYP = General.checknull(request.getParameter("opt_typ"));
 	cr_id = General.checknull(request.getParameter("cr_id"));
 
if(OPT_TYP.equals("")) {
		cnt=1;
		OPT_TYP = "N";
		
		OPT_VALUE = ApplicationConstants.NEW;
	}else if(OPT_TYP.equals("E")) {
		
		OPT_VALUE = ApplicationConstants.EDIT;
		try {
			 DisciplineThrustAreaMappingModel Model =DisciplineThrustAreaMappingManager.EditRecord(cr_id);
			discipline = Model.getDiscipline();
			thrust_area=Model.getThrust_area();
			sub_thrust_area = Model.getSub_thrust_area();
			
		}catch(Exception e){System.out.println("Error in discipline_course_master_e.jsp"+e.getMessage());	
		}
	}
%>
<body onload="getDisciplineByCourse('','<%=discipline %>','discipline'); <%if(OPT_TYP.equals("E")){%>getSubThrustAreaByThrustArea('<%=thrust_area%>','<%=sub_thrust_area%>','sub_thrust_area');<%}%>">
<div class="container-fluid">
 <%--  <div id="" class="page-header"><h4><%=ReadProps.getkeyValue("discipline_thrust_area_mapping.header","sitsResource") %>
    --%>
     <div id="" class="page-header"><h4>Discipline Thrust Area Mapping
   
    <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
	 </span> 
	 </h4>
 </div>
   <form class="form-horizontal" name="courseMasterE" id="courseMasterE" method="post" autocomplete="off">
	    <div class="panel panel-default">
   <% if(OPT_TYP.equals("N")){%>
     <div class="panel-heading"><h3 class="panel-title text-right">New Record</h3></div>
     <%}else{ %>
       <div class="panel-heading"><h3 class="panel-title text-right">Edit Record</h3></div>
   <%} %>
	<div class="panel-body">
		    <div class="form-group">
		     <div class="col-md-12">
		         <div class="row">
					  <label class="col-sm-2 col-form-label required-field" for="">Discipline</label>
							  <div class="col-sm-4" >
								<select class="form-control" id="discipline" name="discipline">
									<option value="">Select Discipline </option>	
								<%-- 	 <%=QueryUtil.getComboOption("cau_iums.academic_degree_desc_mast", "CR_ID", "course_name ",discipline,"", "course_name") %> 			    
									 --%>
								</select>
						   </div>
				        
				       <label class="col-sm-2 col-form-label required-field" for="">Thrust Area</label>
							  <div class="col-sm-4" >
								<select class="form-control" id="thrust_area" name="thrust_area" onchange="getSubThrustAreaByThrustArea(this.value,'<%=sub_thrust_area%>','sub_thrust_area');">
								<option value="<%=sub_thrust_area %>">Select Thrust Area</option>
							    <%=QueryUtil.getComboOption("rsrch_thrust_area_mast", "THRUST_AREA_ID", "THRUST_AREA", thrust_area, "1=1", "THRUST_AREA") %> 
								
						 	</select>
						   </div>
					</div>
		        </div>
		    </div> 
		     <div class="form-group">
			  <div class="col-sm-12">
			    <div class="row" > 
				        <label for="" class="col-sm-2 col-form-label text-left required-field">Research Sub Thrust Area</label>
										<div class="col-sm-4">
												<select class="form-control" id="sub_thrust_area" name="sub_thrust_area" >
													<option value="">Select Sub Thrust Area </option>	
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
					 <div class="form-group m-t-25 m-b-5">
						<div class="col-md-12 text-center">
						    <div class="row">
						  
	
	         <%if(OPT_TYP.equals("N")){ %>
				<button type="button" class="btn btn-view" id="" name="" OnClick="saveRecord('N');">Save</button>
			 <%}else if(OPT_TYP.equals("E")){ %>
			   <button type="button" class="btn btn-view" id="updtBtn" name="updtBtn" onClick="saveRecord('E');">Update</button>
			 	<button type="button" class="btn btn-view" id="btnback" >Back</button>		 
			 <%} %>
				<button type="button" class="btn btn-view" id="btnReset" onclick="">Reset</button> 			
				<input type= "hidden" id="cr_id" value="<%=cr_id %>"  /> 
				<input type= "hidden" id="fstatus" name="fstatus" value="<%=OPT_TYP %>"  /> 
				<input type= "hidden" id="AESKey" value="<%= session.getAttribute("AESUniqueKey")%>" style="width:0px;" readOnly/> 
				<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				</div>
				       </div>
				    </div>
				    </div>
	      </div><!-- End panel-default -->
        </form>
        <form class="form-horizontal" name="DisciplineThrustAreaE" id="DisciplineThrustAreaE" action="" method="post" autocomplete="off">
      <div class="modal fade recomond-btn" id="myModal" tabindex="-1" role="dialog" aria-labelledby="openModal" aria-hidden="true" style="display: none;">
	    <div class="modal-dialog modal-lg">
		  <div class="modal-content">
			<div class="modal-header"> <button type="button" style="color:#d71414 !important; opacity:.5 ;" class="close" data-dismiss="modal" aria-hidden="true">X</button></div>
			  <div class="modal-body">
			   <div class="panel panel-default">
                <div class="panel-heading"><h3 class="panel-title text-right">Searching Criteria</h3></div>
     			<div class="panel-body">
		<!-- -------------------------------Write content inside this modal------------------------------------------- -->
			  <div class="form-group">
		     <div class="col-md-12">
		         <div class="row">
					  <label class="col-sm-2 col-form-label required-field" for="">Discipline</label>
							  <div class="col-sm-4" >
								<select class="form-control" id="xdiscipline" name="xdiscipline" onchange="getThrustAreaByDiscipline(this.value,'','xthrust_area');">
									<option value="">Select Discipline </option>	
									 <%=QueryUtil.getComboOption("cau_iums.academic_degree_desc_mast", "CR_ID", "course_name ",discipline,"", "course_name") %> 			    
									
								</select>
						   </div>
				        
				       <label class="col-sm-2 col-form-label required-field" for="">Thrust Area</label>
							  <div class="col-sm-4" >
								<select class="form-control" id="xthrust_area" name="xthrust_area" onchange="getSubThrustAreaByThrustArea(this.value,'<%=sub_thrust_area%>','xsub_thrust_area');">
									<option value="">Select Thrust Area </option>	
									<%--  <%=QueryUtil.getComboOption("rsrch_thrust_area_mast", "THRUST_AREA_ID", "THRUST_AREA ",thrust_area,"", "THRUST_AREA") %> 			    
									 --%>
									
								</select>
						   </div>
					</div>
		        </div>
		    </div> 
		     <div class="form-group">
			  <div class="col-sm-12">
			    <div class="row" > 
		           
				       <label class="col-sm-2 col-form-label required-field" for="">Sub Thrust Area</label>
							  <div class="col-sm-4" >
								<select class="form-control" id="xsub_thrust_area" name="xsub_thrust_area">
								 	<option value="">Select Sub Thrust Area </option>	
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
					<button type="button" class="btn btn-view" id="btnSearch" data-dismiss="modal" onclick="vldSearch();">Search</button>
					<button type="button" class="btn btn-view" id="xbtnReset" onclick="">Reset</button> 
				</div>
			</div> <!-- End of panal body -->
			</div> <!-- End of panal default -->
		   </div> <!-- End of modal body -->
				 
			</div> <!-- End of modal content -->
		</div> <!-- End of modal-dialog -->
	</div> <!-- End of modal class/id at which modal will be open -->
	</form>
	<iframe class="embed-responsive-item " onload="resizeIframe(this)"  src="discipline_thrust_area_mapping_l.jsp" name="DisciplineThrustAreaD" id="DisciplineThrustAreaD"   frameborder="0" scrolling="no" width="100%" height=""></iframe>
    </div> <!-- End container-fluid -->
  </body>
  <script type="text/javascript">
function resizeIframe(iframe) {
    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
    window.requestAnimationFrame(() => resizeIframe(iframe));
}
		</script>
</html>
    