<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@page import="java.util.ArrayList" %>
<%@page import="com.sits.general.*"%>
<%@include file="../myCks.jsp"%>
<%@page import="com.sits.common.ZipUtils" %>
<%@page import="java.io.File" %>
<%@ page import="java.io.*" %>
<%@ page import="com.sits.common.rsrchFilesDownload" %>
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
	String disabled="",fin_yr="",mode="",returnurl="",locationCode="",ddoCode="",projtype="",fn_agency="",resPrps="";
	user_status		= General.checknull((String)session.getAttribute("user_status"));
	mode=General.checknull(request.getParameter("mode"));
	locationCode=General.checknull(request.getParameter("Xlocation"));
	ddoCode=General.checknull(request.getParameter("Xddo"));
	projtype=General.checknull(request.getParameter("projtype"));
	fn_agency=General.checknull(request.getParameter("fn_agency"));
	fin_yr=General.checknull(request.getParameter("fin_yr"));
	resPrps=General.checknull(request.getParameter("resPrps"));
	if (user_status.equals("U")){
		disabled="disabled";
	}
	ZipUtils zu=new ZipUtils();
	String FOLDER_NAME=  mode;
/* 	System.out.println("mode||"+mode+"||locationCode||"+locationCode+"||ddoCode||"+ddoCode+"|fin_yr|"+fin_yr
			+"|projtype||"+projtype+"|fn_agency|"+fn_agency); */
	if(mode.equals("UC")){
	 returnurl=rsrchFilesDownload.downloadUploadedDoc(FOLDER_NAME,locationCode,ddoCode,fin_yr,
			 projtype,fn_agency,resPrps,"","","","");
	}
	//System.out.println("returnurl- returnurl"+returnurl);
		try{
	 	if(returnurl.contains(".zip")){
			File directory=null;
			String filename=returnurl.split("/")[4]; 
			directory   = new File(returnurl);
			FileInputStream inStream = new FileInputStream(directory);
			ServletContext context = getServletContext();

			// gets MIME type of the file
			String mimeType = context.getMimeType(returnurl);
			if (mimeType == null) {        
		   		mimeType = "application/octet-stream";
			}
			
			response.setContentType(mimeType);
			response.setContentLength((int) directory.length());
			response.setHeader("Content-Disposition","attachment; filename=\""+FOLDER_NAME+"_"+filename);
			OutputStream outStream = response.getOutputStream();
			byte[] buffer1 = new byte[5120000];
			int bytesRead = -1;
			while ((bytesRead = inStream.read(buffer1)) != -1){
		   		outStream.write(buffer1, 0, bytesRead);
			}
			inStream.close();
			outStream.close(); 
			response.setContentType("application/zip");
			
	     	// Delete the download folder after zip
	     	zu.deleteFolder(new File(returnurl));
			response.flushBuffer();
			out.clear(); 
			out = pageContext.pushBody();
		}
	 	}catch (Exception e) {
			System.out.println("utilization_certificate_status_report.jsp :"+e);
		}
	 	
%>
 
<body onload="getLocationDetailwithall('<%=loct_id%>'); <%if(user_status.equals("U")){%> getDdoDetailbyLocation('<%=loct_id%>','<%=ddo_id%>');<%} %> getFinanceYrDropdwn('<%=session.getAttribute("seCurrentFinancialYearId")%>','fin_yr');" >
<div class="container-fluid">
  <div id="" class="page-header"><h4>Utilization Certificate Status Report	</h4>	
 </div>	
 
		
	<script type="text/javascript">
		$(document).ready(function(){
			$("#btnreset").click(function(){
				window.location.href="utilization_certificate_status_report.jsp"
			});
			
			 setTimeout(function() {
				  
					document.getElementById('resPrps').options[0].textContent='All';
				 }, 500);
			
		});
	
		var tibcoserverUrl = '<%=General.checknull(ReadProps.getkeyValue("jasper.url", "sitsResource")) %>';
		var tibcoserverUsername='<%=General.checknull(ReadProps.getkeyValue("jasper.username", "sitsResource")) %>';
		var tibcoserverPassword ='<%=General.checknull(ReadProps.getkeyValue("jasper.password", "sitsResource")) %>';
		var folder_name ='<%=General.checknull(ReadProps.getkeyValue("jasper.report_folder_name", "sitsResource")) %>';
		var jaspername='utilization_certificate_report';
		function downloadJasperReport()
		{
			 	var locationCode=$("#Xlocation").val();
			    var ddoCode=$("#Xddo").val();
			    if(ddoCode==""){ddoCode='%';}
			    var fin_yr=$("#fin_yr").val();
			    var projtype=$("#projtype").val();
			    if(projtype==""){projtype='%';}
			    var erptype=$("#erptype").val();
			    if(erptype==null){erptype='%';}
			    var resPrps=$("#resPrps").val();
			    if(resPrps==""){resPrps='%';}
			    
			    var downloadtype=$("#downloadtype").val();
			    var url=tibcoserverUrl+"?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2Freports&reportUnit="+folder_name
							 		+jaspername+"&standAlone=true&output="+downloadtype+"&j_username="
							 		+tibcoserverUsername+"&j_password="+tibcoserverPassword
							 		+"&locationCode="+locationCode+"&ddoCode="+ddoCode+"&projectType="+projtype+"&erpType="+erptype+"&fin_yr="+fin_yr+"&resPrps="+resPrps;
				console.log("JasperUrl>>>>>>");
				console.log(url);
				$("#reportframe").show();
				document.getElementById("myFrame").src = url;		
		}
		function downloadUploadedDoc(){
			document.frmTotalTailwiseReportD.action="utilization_certificate_status_report.jsp?mode=UC" ;
			document.frmTotalTailwiseReportD.submit();
		}
	</script>
	
 <form class="form-horizontal" name="frmTotalTailwiseReportD" id="frmTotalTailwiseReportD" action="" method="post" autocomplete="off">
  <div class="panel panel-default">
   <div class="panel-heading"><h3 class="panel-title text-right">New Report</h3></div>
	<div class="panel-body">
		
		
		<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  				
      					<label for="location" class="col-sm-2 col-form-label text-left ">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%if(user_status.equals("U")){ %> disabled <%} %> onchange=getDdoDetailbyLocation(this.value);>
									<option value="%">All</option>
								</select>
				           </div>
				           
				           <label for="ddo" class="col-sm-2 col-form-label text-left ">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo" <%if(user_status.equals("U")){ %> disabled <%} %> onchange="getResearchProposal('<%=locationCode%>','<%=ddoCode %>');">
      						 <option value="%">All</option>
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
					 <select class="form-control" id="fin_yr"name="fin_yr" >
						<option value=""> Financial Year</option>
					</select> 
					</div>		 		      		
       <label class="col-sm-2 col-form-label" for="">Download type</label>
		  	<div class="col-sm-4"> 
		  		
		<select class="form-control" id="downloadtype" name="downloadtype">
									 <option value="pdf">PDF</option>
									 <option value="docx">MS word</option>
									 <option value="xlsx">Excel</option>
									<!--  <option value="pptx">PPTx</option> -->
																	
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
								<select class="form-control" id="projtype" name="projtype" onchange="getResearchProposal('<%=locationCode%>','<%=ddoCode %>');">
									<option value="">All</option>
									<%=QueryUtil.getComboOption("rsrch_form1_project_type", "type", "type", "" ,"", "type_id") %>
									</select>
				           </div>
				       
				      
				       
				        <label class="col-sm-2 col-form-label text-left " for="">Funded by</label>
							<div class="col-sm-4">
								
								<select class="form-control" id="fn_agency" name="fn_agency"  <%=disabled %>>
									<option value="%">All</option>
									<%=QueryUtil.getComboOption("rsrch_funding_agency", "fa_id", "concat(fa_name,' ~ ',fundedby)", "","", "fa_name") %>
								</select>
		           			</div> 
		           			
				       </div>
				</div>
			</div>
			
			
				<div class="form-group" >
			<div class="col-md-12">
			      	<div class="row"> 
					<input readonly="readonly" type="hidden" class="form-control" id="finYrId" name="finYrId" value="<%=seCurrentFinancialYearId%>">
				          <label for="" class="col-sm-2 col-form-label ">Project title</label>
							<div class="col-sm-4">
								<select class="form-control resPrps" id="resPrps" name="resPrps">
									<option value="">Select Research Proposal</option>
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
		<button type="button" class="btn btn-view" id="XPdf" onclick='downloadUploadedDoc();'>Download Related Attachments</button>
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
