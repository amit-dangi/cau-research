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
	String locationCode=""  , ddoCode="",dis="",disabled="",mode="",returnurl="",
			inst_name="",coll_type="",mou_type="",m_status="",fin_yr="";
	user_status		= General.checknull((String)session.getAttribute("user_status"));
	mode=General.checknull(request.getParameter("mode"));
	locationCode=General.checknull(request.getParameter("locationCode"));
	ddoCode=General.checknull(request.getParameter("ddoCode"));
	inst_name=General.checknull(request.getParameter("inst_name"));
	coll_type=General.checknull(request.getParameter("coll_type"));
	mou_type=General.checknull(request.getParameter("mou_type"));
	m_status=General.checknull(request.getParameter("m_status"));
	fin_yr=General.checknull(request.getParameter("fin_yr"));
	
/* 	  Create a zip file by ZipUtils require the two string OUTPUT_ZIP_FILE where to create zip ,
	  SOURCE_FOLDER from which to create zip */
   	ZipUtils zu=new ZipUtils();
  String FOLDER_NAME=  "MOU";
  if(mode.equals("Attach")){
   returnurl=rsrchFilesDownload.downloadUploadedDoc(FOLDER_NAME,locationCode,ddoCode,inst_name,
		   						coll_type,mou_type,m_status,fin_yr,"","","");
  }
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
		out.clear(); // where out is a JspWriter
		out = pageContext.pushBody();  
	}
	}catch (Exception e) {
		System.out.println("mou_moa_details_report.jsp :"+e);
	}
	
%>
 
<body onload="downloadJasperReport(); getLocationDetailwithall('<%=loct_id%>'); <%if(user_status.equals("U")){%> getDdoDetailbyLocation('<%=loct_id%>','<%=ddo_id%>');<%} %> getFinanceYrDropdwn('','fin_yr');">
<div class="container-fluid">
  <div id="" class="page-header"><h4>MOU/MOA Details Report</h4>	
 </div>	
 
		
	<script type="text/javascript">
		$(document).ready(function(){
			 setTimeout(function() {
				    var select = document.getElementById('fin_yr');
			        var firstOption = select.options[0];
			        // Change the text of the first option to "All"
			        firstOption.textContent = 'All Financial Year';
				}, 500);
			$("#btnreset").click(function(){
				window.location.href="mou_moa_details_report.jsp"
			});
		});
	
		var tibcoserverUrl = '<%=General.checknull(ReadProps.getkeyValue("jasper.url", "sitsResource")) %>';
		var tibcoserverUsername='<%=General.checknull(ReadProps.getkeyValue("jasper.username", "sitsResource")) %>';
		var tibcoserverPassword ='<%=General.checknull(ReadProps.getkeyValue("jasper.password", "sitsResource")) %>';
		var folder_name ='<%=General.checknull(ReadProps.getkeyValue("jasper.report_folder_name", "sitsResource")) %>';
		var jaspername='mou_moa_details_report';
		
		function downloadJasperReport()
		{
			 	var locationCode=$("#Xlocation").val();
			    var ddoCode=$("#Xddo").val();
			    if(ddoCode==""){ddoCode='%';}
			    
			    var inst_name=$("#inst_name").val();
			    var coll_type=$("#coll_type").val();
			    
			    var mou_type=$("#mou_type").val();
			    var m_status=$("#m_status").val();
			    var fin_yr=$("#fin_yr").val();
			    if(fin_yr==""){fin_yr='%';}
			   
			    var downloadtype=$("#downloadtype").val();
			    var url=tibcoserverUrl+"?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2Freports&reportUnit="+folder_name
							 		+jaspername+"&standAlone=true&output="+downloadtype+"&j_username="
							 		+tibcoserverUsername+"&j_password="+tibcoserverPassword+"&locationCode="+locationCode
							 		+"&ddoCode="+ddoCode+"&inst_name="+inst_name+"&coll_type="+coll_type
							 		+"&mou_type="+mou_type+"&m_status="+m_status+"&fin_yr="+fin_yr;
							 		;
				console.log("JasperUrl>>>>>>");
				console.log(url);
				$("#reportframe").show();
				document.getElementById("myFrame").src = url;
		}
		
		function downloadUploadedDoc(){
			/* alert("in"); */
			document.frmTotalTailwiseReportD.action="mou_moa_details_report.jsp?mode=Attach" ;
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
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo" <%if(user_status.equals("U")){ %> disabled <%} %>>
      						 <option value="%">All</option>
      						</select>      				
      					</div> 				        						
				</div>
				
				</div>
				</div>
				
				<div class="form-group">
			<div class="col-md-12">
				<div class="row">
				<label class="col-sm-2 col-form-label text-left required-field" for="">Name of Institute/University/Company/others</label>
						<div class="col-sm-4">
						<select class="form-control" id="inst_name" name="inst_name">
						<option value="%">All</option>
				       	<%=QueryUtil.getComboOption("rsrch_mou_moa_form", "inst_name", "inst_name ","","", "inst_name") %>
				       	</select>
				       	</div>			 		      		
				<label class="col-sm-2 col-form-label text-left required-field" for="">Type of collaboration</label>
							<div class="col-sm-4">
								<select class="form-control" id="coll_type" name="coll_type">
								<option value="%">All</option>
								<option value="Academic">Academic</option>
								<option value="Research">Research</option>
								<option value="Extension">Extension</option>
								<option value="Other">Other</option>
								</select>
				           </div>           		
					</div>
				</div>
				</div>
				
				
				<div class="form-group">
			<div class="col-md-12">
				<div class="row">
				
			    <label class="col-sm-2 col-form-label text-left required-field" for="">Type of MOU/MOA</label>
					<div class="col-sm-4">
						<select class="form-control" id="mou_type" name="mou_type">
						<option value="%">All</option>
						<option value="National">National</option>
						<option value="International">International</option>
						</select>
		           </div>   	
				  <label class="col-sm-2 col-form-label text-left required-field" for="">Action Taken</label>
							<div class="col-sm-4">
								<select class="form-control" id="m_status" name="m_status">
								<option value="%">All</option>
								<option value="Initiated">Initiated</option>
								<option value="Completed">Completed</option>
								</select>
				           </div>         		
					</div>
				</div>
				</div>
				
				
				<div class="form-group">
			<div class="col-md-12">
				<div class="row">
				 	<label class="col-sm-2 col-form-label ">Financial Year</label>
						<div class="col-sm-4">
						 <select class="form-control" id="fin_yr"name="fin_yr">
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
 
 </script>

</body>
</html>