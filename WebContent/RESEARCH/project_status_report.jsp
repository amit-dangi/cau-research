<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@page import="java.util.ArrayList" %>
<%@page import="com.sits.general.*"%>
<%@include file="../myCks.jsp"%>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Expires" content="-1" />
 	<link rel="stylesheet" href="../assets/sits/bootstrap/3.3.7/css/bootstrap.min.css"  type="text/css">
	<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
	<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../assets/plugins/datepicker/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	 <script type="text/javascript" src="../js/commonDropDown.js"></script>
    <link rel="stylesheet" href="../assets/plugins/datepicker/datepicker3.css"  type="text/css">
 	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
  	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
  		<style type="text/css">
.container {
  position: relative;
  overflow: hidden;
  width: 100%;
  padding-top: 56.25%; /* 16:9 Aspect Ratio (divide 9 by 16 = 0.5625) */
}

/* Then style the iframe to fit in the container div with full height and width */
.responsive-iframe {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  width: 100%;
  height: 100%;
}
		</style>
</head>
 <%	
	String locationCode=""  , ddoCode="",dis="",disabled="";
	user_status		= General.checknull((String)session.getAttribute("user_status"));
	
%>
 
<body onload="downloadJasperReport(); getLocationDetailwithall('<%=loct_id%>'); 
	<%if(user_status.equals("U")){%> getDdoDetailbyLocation('<%=loct_id%>','<%=ddo_id%>');<%} %> 
		getFinanceYrDropdwn('','fin_yr'); getEmployeeName('<%=loct_id%>','<%=ddo_id%>','','','PiName');">
<div class="container-fluid">
  <div id="" class="page-header"><h4>Project Status Report</h4>	
 </div>	
 
		
	<script type="text/javascript">
		$(document).ready(function(){
			 setTimeout(function() {
				    var select = document.getElementById('fin_yr');
			        var firstOption = select.options[0];
			        // Change the text of the first option to "All"
			        firstOption.textContent = 'All Financial Year';
			        document.getElementById('PiName').options[0].textContent='All';
				}, 500);
			$("#btnreset").click(function(){
				window.location.href="project_status_report.jsp"
			});
		});
	
		var tibcoserverUrl = '<%=General.checknull(ReadProps.getkeyValue("jasper.url", "sitsResource")) %>';
		var tibcoserverUsername='<%=General.checknull(ReadProps.getkeyValue("jasper.username", "sitsResource")) %>';
		var tibcoserverPassword ='<%=General.checknull(ReadProps.getkeyValue("jasper.password", "sitsResource")) %>';
		var folder_name ='<%=General.checknull(ReadProps.getkeyValue("jasper.report_folder_name", "sitsResource")) %>';
		var jaspername='';
		
		function downloadJasperReport()
		{
			 	var locationCode=$("#Xlocation").val();
			 	if(locationCode==""){locationCode='%';}
			    var ddoCode=$("#Xddo").val();
			    if(ddoCode==""){ddoCode='%';}
			    if($("#reportType").val()=='count'){
			    	jaspername='project_status_count_report';	
			    }else{
			    	jaspername='project_status_report';
			    	}
			    var fin_yr=$("#fin_yr").val();
			    if(fin_yr==""){fin_yr='%';}
			    var projtype=$("#projtype").val();
   			    var erptype=$("#erptype").val();
   			 	var projStatus=$("#projStatus").val();
   			 	var fn_agency=$("#fn_agency").val();
   			 
	   			    if(erptype==null){erptype='%';}
			    var downloadtype=$("#downloadtype").val();
			    var projunder=$("#projunder").val();
			    var PiName=$("#PiName").val();
			    if(PiName==null || PiName==""){PiName='%';}
			    var url=tibcoserverUrl+"?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2Freports&reportUnit="+folder_name
							 		+jaspername+"&standAlone=true&output="+downloadtype+"&j_username="
							 		+tibcoserverUsername+"&j_password="+tibcoserverPassword+"&locationCode="+locationCode
							 		+"&ddoCode="+ddoCode+"&projectType="+projtype+"&erpType="+erptype
							 		+"&fin_yr="+fin_yr+"&projStatus="+projStatus+"&fn_agency="+fn_agency+
							 		"&projunder="+projunder+"&PiName="+PiName;
				console.log("JasperUrl>>>>>>");
				console.log(url);
				$("#reportframe").show();
				document.getElementById("myFrame").src = url;		
		}
		
	</script>
	
 <form class="form-horizontal" name="frmTotalTailwiseReportD" id="frmTotalTailwiseReportD" action="" method="get" autocomplete="off">
  <div class="panel panel-default">
   <div class="panel-heading"><h3 class="panel-title text-right">New Report</h3></div>
	<div class="panel-body">
		
		
		<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  				
      					<label for="location" class="col-sm-2 col-form-label text-left ">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%if(user_status.equals("U")){ %> disabled <%} %> onchange=getDdoDetailbyLocation(this.value);>
									<option value="">All</option>
								</select>
				           </div>
				           
				           <label for="ddo" class="col-sm-2 col-form-label text-left ">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo" <%if(user_status.equals("U")){ %> disabled <%} %> onchange="getEmployeeName('<%=loct_id%>','<%=ddo_id%>','','','PiName');">
      						 <option value="">All</option>
      						</select>      				
      					</div>        						
				</div>
				
				</div>
				</div>
				
				<div class="form-group">
			<div class="col-md-12">
				<div class="row">
							<label class="col-sm-2 col-form-label">Financial Year</label>
					<div class="col-sm-4">
					 <select class="form-control" id="fin_yr" name="fin_yr" >
						<option value="%">All</option>
					</select> 
					</div>		 		      		
       <label class="col-sm-2 col-form-label" for="">Download type</label>
		  	<div class="col-sm-4"> 
		  		
		<select class="form-control" id="downloadtype" name="downloadtype">
									 <option value="pdf">PDF</option>
									 <option value="docx">MS word</option>
									 <option value="xlsx">Excel</option>
									<!--  <option value="docx">Doc</option>
									 <option value="pptx">PPTx</option> -->
																	
								</select>
								</div>
           		
					</div>
				</div>
				</div>
				
			<div class="form-group" >
				<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left" for="">Project Status</label>
							<div class="col-sm-4">
								<select class="form-control" id="projStatus" name="projStatus">
									<option value="%">All</option>
									<option value="Completed" >Completed</option>
									<option value="Ongoing" >Ongoing</option>
									<option value="Newly Sanctioned">Newly Sanctioned</option>
									<option value="Project Proposed">Project Proposed</option>
								</select>
				           </div>
				           <label class="col-sm-2 col-form-label text-left" for="">Report Type</label>
							<div class="col-sm-4">
								<select class="form-control" id="reportType" name="reportType">
									<option value="detail" >Project Status Detail Report</option>
									<option value="count" >Project Status Count Report</option>
								</select>
				           </div>
				       </div>
				</div>
			</div>
				
			<div class="form-group" >
				<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left " for="">Project Type</label>
							<div class="col-sm-4">
								<select class="form-control" id="projtype" name="projtype">
									<option value="%">All</option>
									<%=QueryUtil.getComboOption("rsrch_form1_project_type", "type", "type", "" ,"", "type_id") %>
									<!-- <option value="IRP" >Intramural Research Project(IRP)</option>
									<option value="ERP" >Extramural Research Project(ERP)</option> -->
									</select>
				           </div>
				       
				       <label class="col-sm-2 col-form-label text-left " for="">Funding Agency</label>
							<div class="col-sm-4">
								
								<select class="form-control" id="fn_agency" name="fn_agency"  <%=disabled %>>
									<option value="%">All</option>
									<%=QueryUtil.getComboOption("rsrch_funding_agency", "fa_id", "concat(fa_name,' ~ ',fundedby)", "","", "fa_name") %>
								</select>
		           			</div> 
		           
				      <!--  <div id="erptypediv" style="display: none;">
						<label class="col-sm-2 col-form-label text-left" for="">ERP Type</label>
							<div class="col-sm-4">
								<select class="form-control" id="erptype" name="erptype">
									<option value="%">All</option>
									<option value="AICRP" >All India Co-ordinated Research Projects (AICRP)</option>
									<option value="ADHOC" >Adhoc (NEH, TSP, SC, SP etc.)</option>
									<option value="OTHERS">Others (DBT, DST, ICAR, ICCSR etc.)</option>
								</select>
				           </div>
				       </div> -->
				       </div>
				</div>
			</div>
		<div class="form-group" >
			<div class="col-md-12">
					<div class="row">
						<label class="col-sm-2 col-form-label text-left" for="">Project Under</label>
							<div class="col-sm-4">
								<select class="form-control" id="projunder" name="projunder">
									<option value="%">All</option>
									<option value="DOR" >DOR</option>
									<option value="DEE" >DEE</option>
									</select>
				           </div>
				       
						<label class="col-sm-2 col-form-label text-left  " for="">Name of the PI/Scientist In-Charge</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="PiName" name="PiName" >
									<option value="">All</option>
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
		<button type="button" class="btn btn-view" id="XPdf" onclick='downloadJasperReport();'>Generate Report</button>
			<button type="button" class="btn btn-view" id="btnreset">Reset</button> 
		</div>
	</div> <!-- End of panal body -->
  </div> <!-- End of panal default -->
  <input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				
</form>
 </div>
   <div class="container" id="reportframe" style="display: none;">
<iframe id="myFrame"  src=""  class="responsive-iframe"  width="100%" height="" title="Iframe Example"></iframe>
</div>

 <script>
 function getLocationDetailwithall(locationCode){
		var hrmsApi = $("#hrmsApi").val().trim();
		var json={userId:$("#user_id").val()};  	
		var sel="";
		//alert(hrmsApi+" locationCode ="+locationCode);
		try {
			$.ajax({	
				type: "GET",
				url: hrmsApi+"rest/apiServices/getLocationDetail",
				jsonp: "parseResponse",
				dataType: "jsonp",
				data:json,
				success: function (response){
					console. log("response");
					console.log(response);
					var department=response.locationDetail;
					var dropdown="<option value='%'>All</option>";
					$.each(department, function(index, department) {
					if(department.locationId==locationCode) {
							sel="selected";	
						} else {
							sel="";
						}
						dropdown+="<option  value='" + department.locationId + "'"+sel+">" + department.locationName + "</option>";					
					});	
					$("#Xlocation").html(dropdown);
				}
			});	
		} catch (e) {
			// TODO: handle exception
			alert(e);
		}
	}
 
 $("#projtype").change(function(){	
		var projtype = $('#projtype').val();
		if(projtype=='ERP'){
			$("#erptype").val('%');
			 $('#erptypediv').show();
		}
		else{
			$('#erptypediv').hide();
			$("#erptype").val('');
		}
	});
 </script>

</body>
</html>