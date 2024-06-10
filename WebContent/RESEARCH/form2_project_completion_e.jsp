<%@include file="../myCks.jsp"%>
<%@ page import="java.util.ArrayList" %>
<%@page import="com.sits.general.ApplicationConstants"%>
<%@page import="com.sits.general.QueryUtil"%>
<%@ page import="com.sits.rsrch.form2_project_completion.*" %>
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
    <script type="text/javascript" src="../js/research/form2-project-completion.js"></script>
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
</head>
<%			
	String fstatus = General.checknull(request.getParameter("fstatus"));
	if(General.checknull(fstatus.trim()).equals("")) {
		fstatus="N";
	}
	String OPT_VALUE = "", f_agn="", blncRefFundAgn="", submToFundAgn="", submToProjCell="", 
			piAccClose="", Dept="", Desg="", blncRefFundAgnC="", submToFundAgnC="", submToProjCellC="", 
			piAccCloseC="", date="", RelvInfo="", id="", techRepsubmToFundAgn="", techRepsubmToFundAgnC="",
			dis="" , locationCode=""  , ddoCode="" ,disabled="",projPropsalIdManual="",durPropProjYear="",project="";
	
	ArrayList<ProjectCompletionModel> arrayList = new ArrayList<ProjectCompletionModel>();  
	ArrayList<ProjectCompletionModel> fileList = new ArrayList<ProjectCompletionModel>();
	
	if(fstatus.trim().equals("N")) {
		OPT_VALUE = ApplicationConstants.NEW;
		f_agn=sess_emp_id;
	}else if (fstatus.trim().equals("E")){
		OPT_VALUE = ApplicationConstants.EDIT;
		id=General.checknull(request.getParameter("id"));
		project=General.checknull(request.getParameter("project"));
		arrayList = ProjectCompletionManager.getEditList(id);
		fileList = ProjectCompletionManager.getEditFileList(id);

		if(General.checknull(arrayList.get(0).getPiAccClose()).equals("Y")){
			piAccCloseC="checked";
		}
		if(General.checknull(arrayList.get(0).getBlncRefFundAgn()).equals("Y")){
			blncRefFundAgnC="checked";
		}
		if(General.checknull(arrayList.get(0).getSubmToFundAgn()).equals("Y")){
			submToFundAgnC="checked";
		}
		if(General.checknull(arrayList.get(0).getSubmToProjCell()).equals("Y")){
			submToProjCellC="checked";
		}
		if(General.checknull(arrayList.get(0).getTechRepsubmToFundAgn()).equals("Y")){
			techRepsubmToFundAgnC="checked";
		}
		
		f_agn=General.checknull(arrayList.get(0).getPiId());
		Dept=General.checknull(arrayList.get(0).getDept()); 
		Desg=General.checknull(arrayList.get(0).getDesg());
		date=General.checknull(arrayList.get(0).getXTODATE());
		RelvInfo=General.checknull(arrayList.get(0).getRelvInfo());
	}else if (fstatus.trim().equals("S")){
		OPT_VALUE = ApplicationConstants.SEARCH;
	}
	
	if(user_status.equals("A")){
		dis="";
		disabled="";
	}else{
		dis="";
		disabled="disabled";
	}
	 locationCode= locationCode.equals("")?loct_id:locationCode;
	 ddoCode= ddoCode.equals("")?ddo_id:ddoCode;
%>
 <body onload="getEmpFromForm1('<%=f_agn%>'); getLocationDetail('<%=locationCode%>'); getDdoDetailbyLocation('<%=locationCode%>','<%=ddoCode%>'); <%if(fstatus.trim().equals("E")){ %> getDptByEmp('<%=f_agn%>', '<%=Dept%>'); getDesignByEmployeeAndDept('<%=f_agn%>', '<%=Dept%>', '<%=Desg%>');<%}%>">

<script type="text/javascript">
   $(document).ready(function() { 
		$("#XTODATE").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#XTODATE").datepicker("setDate", '');
		}).on('clearDate', function(selected) {
		});
 });
 </script>
<div class="container-fluid">
<div id="" class="page-header"><h4><%=ReadProps.getkeyValue("form2_proj_comp.header","sitsResource") %></h4> </div>
    <form class="form-horizontal" name="frmF1ProjectSubmissionE" id="frmF1ProjectSubmissionE" action="" method="post" autocomplete="off" target="" enctype="multipart/form-data">
    <div class="panel panel-default">
     <div class="panel-heading"><h3 class="panel-title text-right"><%=OPT_VALUE %></h3></div>
			<div class="panel-body">
			
			<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  				
      				<label for="location" class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%=dis %><%=disabled %> onchange=getDdoDetailbyLocation(this.value);>
									<option value="">Select Location</option>
								</select>
				           </div>				        						
				</div>
				
				</div>
				</div>
				 <div class="form-group" >
			<div class="col-md-12">
			<div class="row">
	  								
					<label for="ddo" class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo" <%=dis %> <%=disabled %> onchange="getEmpFromForm1('<%=f_agn%>');">
      						 <option value="">Select DDO</option>
      						</select>      				
      				</div>      						
				</div>
				
				</div>
				</div>
			
              <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Name of the Principal Investigator/Project</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="piId" name="piId">
									<option value="">Select Principal Investigator</option>
								</select>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				 
				 <div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
					
				           <label class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Department</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="deptId" name="deptId" <%=dis %>>
									<option value="">Select Department</option>
								</select>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Designation</label>
							<div class="col-sm-4 ">
								<select class="form-control" id="desId" name="desId" <%=dis %>>
									<option value="">Select Designation</option>
								</select>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				<%if(fstatus.trim().equals("N") || fstatus.trim().equals("E")) { %>
				
				<%if(fstatus.trim().equals("E")) { %>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >						
				           <label class="col-sm-7 col-form-label text-left"  for="">Title Of the project proposal</label>
							
							<div class="col-sm-4 ">
								<div class="form-control" ><%=project%>
								</div>								
				           </div>
				           
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				<%} %>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >						
				           <label class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Date of Completion</label>
							
							<div class="col-sm-4 ">
								<div class="input-group date" id="msg-XTODATE">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
									id="XTODATE" name="XTODATE" placeholder="DD/MM/YYYY" value="<%=date %>">
								</div>								
				           </div>
				           
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left " for="">Whether PI Account Is Closed</label>
							<div class="col-sm-4">
								<input type="Checkbox" id="piAccClose" name="piAccClose" value="<%=piAccClose %>" <%=piAccCloseC %>>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left " for="">Whether balance amount refund to funding agency</label>
							<div class="col-sm-4 ">
								<input type="Checkbox" value="<%=blncRefFundAgn %>" id="blncRefFundAgn" name="blncRefFundAgn" <%=blncRefFundAgnC %>>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left " for="">Whether Consolidated SE & UC has been Submitted to funding agency</label>
							<div class="col-sm-4 ">
								<input type="Checkbox" value="<%=submToFundAgn %>" id="submToFundAgn" name="submToFundAgn" <%=submToFundAgnC %>>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left " for="">Whether list of equipment purchased along with stock entry details duly endorsed by HOD is submitted to project cell</label>
							<div class="col-sm-4 ">
								<input type="Checkbox" value="<%=submToProjCell %>" id="submToProjCell" name="submToProjCell" <%=submToProjCellC %>>
				           </div>
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
							<label class="col-sm-7 col-form-label text-left " for="">Whether final technical report to the project work submitted to the funding agency</label>
							<div class="col-sm-4 ">
								<input type="Checkbox" value="<%=techRepsubmToFundAgn %>" id="techRepsubmToFundAgn" name="techRepsubmToFundAgn" <%=techRepsubmToFundAgnC %>>
				           </div>
							<div class="col-sm-1"></div>					
					   </div>
				   </div>
				</div>
				<div class="form-group " >
					<div class="col-md-12 ">
						<div class="row " >
						
				           <label class="col-sm-7 col-form-label text-left <%if(!fstatus.trim().equals("S")){ %> required-field <%} %>" for="">Any other relevent information</label>
							<div class="col-sm-4 ">
								<textarea style="height: 65px;" rows="15" cols="60" placeholder="Enter Details" id="RelvInfo" name="RelvInfo" maxlength="254"><%=RelvInfo %></textarea>						
				           </div>
				           
							<div class="col-sm-1"></div>
					   </div>
				   </div>
				</div> 
				
		   	<div class="page-header-1">
				<div class="col-sm-4 repTitle h5 ">Document Upload (Attachment)</div>
			</div>

			<div id="dydiv" style="width:100%;overflow: auto;">
      			<div id="" style="padding:8px 0px;display:flex;width:100%;">
					<table border="1" cellspacing="1" cellpadding="1" width="100%" class="tableGrid" >
					<tr>
						<th width="30%" style="text-align:center;">Document Title</th>
					  	<th width="30%" style="text-align:center;">Upload Document <br>
					  		<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">
					  			(Max. Size Cannot Exceed 20MB) <br> (*Note: Only .pdf, .jpg, .png, & doc files will be allowed for uploading.)
					  		</span>
					  	</th>
					  	<th width="20%" style="text-align:center;">Delete</th>
					</tr>
					
					<tbody id="searchTable" >
					<%int i=0, k=0;
					if(arrayList.size()>0){
					for(ProjectCompletionModel bhm: fileList){
						k=++i;
					%>				
				    <tr>
				    	
				    	<td class="text-center"><input type="text" class="form-control" id="doctitle<%=k%>" name="doctitle<%=k%>" maxlength="100" placeholder="Enter Document Title" value="<%=bhm.getDoc_name()%>">
							<input type="hidden" class="form-control" id="hfname<%=k%>" name="hfname<%=k%>">
							<input type="hidden" class="form-control" id="fsize<%=k%>" name="fsize<%=k%>">
						</td>
						
						<td class="text-center">
						<a target="_blank" href="../downloadfile?filename=<%=bhm.getDid()%>_<%=bhm.getFile_name()%>&folderName=RSRCH/FORM1/<%=bhm.getfId()%>">
							<u><%=bhm.getFile_name()%></u>
						</a><%-- <input type="file" id="upldoc<%=k%>" onchange="data(<%=k%>)" name="upldoc<%=k%>" class=""> --%>
						</td>						
						<td style="text-align:center;color:RED;cursor:pointer; width:5%;"
							 onclick="deletesavefile('<%=bhm.getDid()%>','<%=bhm.getDid()+"_"+bhm.getFile_name()%>','<%=bhm.getfId()%>');"
						><i class="fa fa-trash-o"></i>Delete</td>
					</tr>
					<%} }else{%>
					<tr>
				    	<td class="text-center"><input type="text" class="form-control" id="doctitle1" name="doctitle1" maxlength="100" placeholder="Enter Document Title" value="">
							<input type="hidden" class="form-control" id="hfname1" name="hfname1">
							<input type="hidden" class="form-control" id="fsize1" name="fsize1">
						</td>						
						<td class="text-center"><input type="file" id="upldoc1" onchange="data(1)" name="upldoc1" class=""></td>						
						<td style="text-align:center;color:RED;cursor:pointer; width:5%;" onclick="deletedata(1,this)"><i class="fa fa-trash-o"></i> Delete</td>
					</tr>
					<%} %>
 					</tbody>
					</table>
					<div class="text-right"style="margin-top:18px;padding:8px;">
					<%if(General.checknull(fstatus).equals("E") || General.checknull(fstatus).equals("N")){ %>
						<div class="colr-blue-p" id="addmore">
							<span id="" onClick=""><i class="fa fa-plus-circle fa-2x" style="font-size: 20px;"></i></span>
						</div>
					<%} %>
				</div>					
				</div>
			</div>		
			<%}%>
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
				<%if(fstatus.trim().equals("S")) {%>
					<button type="button" class="btn btn-view" id="btnSearch1" name="btnSearch1">Search</button>
					<button type="button" class="btn btn-view" id="btnNew" name="btnNew">New</button>
				<%}else{%>
					<button type="button" class="btn btn-view" id="btnSearch" name="btnSearch">Search</button>
					<%if(fstatus.trim().equals("E")) {%>
							<button type="button" class="btn btn-view" id="btnUpdate" name="btnUpdate" onclick="saveAndSubmit('U');">Update</button>
					<%}else{%>
						<button type="button" class="btn btn-view" id="btnSave" name="btnSave" onclick="saveAndSubmit('S');">Save</button>
					<%}%>
				<%}%>
				<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
				<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				<input type="hidden" name="fstatus" id="fstatus" value="<%=General.checknull(fstatus) %>">
				<input type="hidden" name=index id="index" value="1">
				<input type="hidden" name="mid" id="mid" value="<%=id%>">
				<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
			</div>
				
			</div>
		</div>
   </form>
   <%if (fstatus.trim().equals("S")){ %>
  		<iframe class="embed-responsive-item" src="form2_project_completion_l.jsp" name="btmfrmF1ProjectSubmissionE" id="btmfrmF1ProjectSubmissionE" frameborder="0" scrolling="no" width="100%" height="480px"></iframe>
  	<%} %>
   </div> 
</body>
</html>