<%@page import="com.sits.rsrch.research_activity.create_manage_expense.CreateManageExpenseManager"%>
<%@page import="com.sits.rsrch.research_activity.create_manage_expense.CreateManageExpenseModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sits.general.QueryUtil"%>
<%@page import="com.sits.general.ApplicationConstants"%>
<%@include file="../myCks.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Expires" content="-1" />
 	
 	<link rel="stylesheet" href="../assets/sits/bootstrap/3.3.7/css/bootstrap.min.css"  type="text/css">
	<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
	<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/aes/AesUtil.js"></script>
	<script type="text/javascript" src="../js/aes/crypto.js"></script>
	<script type="text/javascript" src="../js/common.js"></script>	
	<script type="text/javascript" src="../js/common/common-utilities.js"></script>
	<script type="text/javascript" src="../js/common/validations.js"></script>
	<script type="text/javascript" src="../js/gen.js"></script>
    <script type="text/javascript" src="../js/research/create-and-manage-expense-details.js"></script>
    <script type="text/javascript" src="../js/datepicker/bootstrap-datepicker.js"></script> 
    <script>
 	$(document).ready(function() {
 	$("a").tooltip();
 	});
 	</script>   	
	<%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", -1); //prevents caching at the proxy
	%>
	<link rel="stylesheet" href="../css/datepicker/datepicker3.css"  type="text/css">
	<link href="../css/themes/cuk.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
  	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
  	<style>
  	table.tableGrid th { background-color: #bb9c2b;
	 /*    border: 1px solid #1ea2cd !important; */
	    color: #0c426f;
     }
     .close {
    	opacity: 1 !important;
    	color: red !important;
	 }
	 .dataEntryDiv {
    overflow-y: auto;
    min-height: 80px;
    max-height: unset !important;
    width: 100%;
    }
  	
  	</style>
</head>
<%	
	ArrayList<CreateManageExpenseModel> arrayList = new ArrayList<CreateManageExpenseModel>();
	ArrayList<CreateManageExpenseModel> saveData = new ArrayList<CreateManageExpenseModel>();
	String fstatus="", piId="", finYrId="", resPrps="", date="",locationCode="",ddoCode="",installId="";
	int rBlnc=0;boolean checkRecord=false;
	int remainingBlnc=0;
	fstatus = General.checknull(request.getParameter("fstatus"));
	piId = General.checknull(request.getParameter("piId"));
	finYrId = General.checknull(request.getParameter("finYrId"));
	resPrps = General.checknull(request.getParameter("resPrps"));
	date 	= General.checknull(request.getParameter("date"));
	locationCode = General.checknull(request.getParameter("locationCode"));
	ddoCode 	= General.checknull(request.getParameter("ddoCode"));
	installId 	= General.checknull(request.getParameter("installId"));
	
	checkRecord=CreateManageExpenseManager.getCheck(finYrId,installId, resPrps,"2");
	//Getting the head list from this method on the bases of installId
	boolean check=CreateManageExpenseManager.getCheck(finYrId,installId, resPrps,"1");
if(!check){ 
	 %>
	  <script type="text/javascript">
	        window.parent.responseBack();
	    </script>
	  <%}else{
	arrayList=CreateManageExpenseManager.getListView(resPrps,finYrId);
	if(arrayList.size()==0){
		 %>
		  <script type="text/javascript">
		        window.parent.responseBack1();
		    </script>
		  <%
	}
	//System.out.println("subHead||"+subHead.toString());
	saveData=CreateManageExpenseManager.getSaveData( resPrps, finYrId, date);
	ArrayList<CreateManageExpenseModel> SaveDataForManageClosingblnc=CreateManageExpenseManager.getSaveDataForManageClosingblnc(resPrps, finYrId, date);

%>

<body>
     <%  if(arrayList.size()!=0){%>
    <form class="form-horizontal" name="frmCreateManageExpE" id="frmCreateManageExpE" action="" method="post" autocomplete="off" target="">
    <div class="panel panel-default" Style="display: show;" id="CreateManageExp">
     <div class="panel-heading"><h3 class="panel-title text-right">View Head Sub Detail</h3></div>
			<div class="panel-body" style="padding: 10px;">
            	
			<div class="col-md-12" style="padding: 0px;">
				<div id="" class="" style="overflow-y: scroll; min-height: 100px; max-height: 400px;">
				  <table border="1" cellspacing="1" cellpadding="1" width="100%" class="tableGrid " >
				  <thead>
					<tr>
					  <th style="text-align:center; width:3%;">S.No.</th>
				  	  <th style="text-align:center; width:9%;">Head Name</th>
				  	   <th style="text-align:center; width:9%;">Sub Head Name</th>
					  <th style="text-align:center; width:11%;">Total Allocated Amount (Rs.)</th>
					  <th style="text-align:center; width:11%;">Balance Amount (Rs.)</th>
					  <th style="text-align:center; width:11%;">Approval Date</th>
					  <th style="text-align:center; width:9%;">Expense Amount (Rs.)</th>
					  <th style="text-align:center; width:9%;">Particulars/Purpose</th>
					  <th style="text-align:center; width:11%;">Sanction/ Order No</th>
					  <th style="text-align:center; width:9%; z-index:1;">Sanctioned Order 
					  	<!-- <p class="errMsg" style="color:red;">Max File Size 20 MB (Note: Only .pdf, .jpg, .png, & doc files will be allowed for uploading)</p> -->
					  	<!-- <span>(File size should be 10KB to 3MB.)</span> -->
					  	<span><a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="auto left" style="color:#2b3e1f;"
    	                    title="" data-original-title="File Format should be JPG/PNG/DOC/PDF and File size should be 10kb to 2 MB."></a></span>
					  </th>
					</tr>
				 </thead>
					<tbody id="stable">
					
					<%int i=0, rowIndex=0; %>
					
					<%if(saveData.size()>0 && saveData.get(0).getFlg().equals("Y")){ 
						for(CreateManageExpenseModel bhm: saveData){%>
						<tr>
					  		<td style="text-align:center; width:3%;"><%=++i %></td>															
							<td style="text-align:center; width:9%;" >
								<input type="hidden" id="headId_<%=i%>" name="headId_<%=i%>" value="<%=bhm.getHeadId()%>" readonly="readonly"><%=General.checknull(bhm.getHeadName())%>
							</td>
							<td style="text-align:center; width:9%;" valign="top">
								<table style="width:100%">
									<tr>
										<td>
											<%=bhm.getSubHeadName()%>
											<input type="hidden" id="subHeadId_<%=i%>" name="subHeadId_<%=i%>" value="<%=bhm.getSubHeadId()%>">																
										</td>
									</tr>
										
									<%
								%>
								</table>
								<input class="form-control"  type="hidden" id="subHeadIndex_<%=i%>" name="subHeadIndex_<%=i%>" value="<%=rowIndex%>">
							</td>
							<td style="text-align:center; width:11%;">
								<input class="form-control" type="text" id="appAmt_<%=i%>" name="appAmt_<%=i%>" value="<%=bhm.getBlncAmt()%>" readonly="readonly" style="text-align:right;">
							</td>
							<td style="text-align:center; width:11%;">
								<input class="form-control"  type="text" id="blncAmt_<%=i%>" name="blncAmt_<%=i%>" value="<%=bhm.getRemainingAmt()%>" readonly="readonly" style="text-align:right;">
							</td>
							<td style="text-align:center; width:11%;" id="allDt">
								<input class="form-control"  type="text" id="allDt_<%=i%>" name="allDt1_<%=i%>" class="allDt" readonly="readonly" value="<%=bhm.getAppDate()%>">
							</td>
							
							<td style="text-align:center; width:9%;">
						    	<div valign="top" style="margin-bottom:2px;">
						    		<input type="text" id="amt_<%=i%>" name="amt_<%=i%>" onblur=" IsValidAmt(this.value, <%=i%>, <%=bhm.getSubHeadAmt()%>); numberOrDecimalNumber(this);" value="<%=bhm.getSubHeadAmt() %>" onkeypress="if ( isNaN(this.value + String.fromCharCode(event.keyCode) )) return false;" onCopy="return false" onCut="return false" maxlength="10" style="text-align:right;">								    		
						    	</div>    								  	
						  	<input type="hidden" id="amtIndex_<%=i%>" name="amtIndex_<%=i%>" value="<%=bhm.getSubHeadId()%>">	
							</td>
						
							<%
							//String directoryName="RSRCH/EXPENSE/"+resPrps+"/"+piId+"/"+bhm.getHeadId()+"/"; 							
							String directoryName="RSRCH/EXPENSE/"+resPrps+"/"+bhm.getHeadId()+"/";
							//String doc []= General.checknull(bhm.getDocName()).split(",");%>
								<td style="text-align:center; width:9%;">
						    	<div valign="top" style="margin-bottom:2px;">
						    		<input type="text" maxlength="50" class="form-control" id="purpose_<%=i%>" name="purpose_<%=i%>" value="<%=bhm.getPurpose()%>"  placeholder="Enter Particulars/Purpose" >
						    	</div>    								  	
							</td>
							
							<td style="text-align:center; width:11%;">
						    	<div valign="top" style="margin-bottom:2px;">
						    		<input type="text" maxlength="50" class="form-control" id="order_no_<%=i%>" name="order_no_<%=i%>" value="<%=bhm.getOrder_no()%>"  placeholder="Enter Sanction/ Order No" >
						    	</div>    								  	
							</td>
							
							<td style="text-align:center; width:12%;">
							<table style="width:100%">
							<% 
						//	for(int k=0; k<doc.length; k++){ 
							System.out.println("docName"+bhm.getDocName().toString());
							String docName[]=bhm.getDocName().split("~");
							String did[]=bhm.getDid().split(",");
							for(int kk=0; kk<did.length; kk++){
							boolean flag=true;
							//System.out.println("docName value||"+did[kk]);	
							if(!bhm.getDocName().equals("")){  flag=false;%>
								<div id="saveUpload_<%=bhm.getHeadId() %>">
								<tr>
								<td style="text-align:center; width:15%; z-index:1;">
								<a target="_blank" href="../downloadfile?filename=<%=docName[2]+"_"+docName[1] %>&folderName=<%=directoryName%>">									
									<u><%=bhm.getDocName()%></u>
								</a>&ensp;															
								 <span style="text-align:center; width:10%;color:red;cursor:pointer;"
						   	 		onclick="deleteuploadfile('<%=docName[2]%>', '<%=directoryName+"/"+docName[2]+"_"+docName[1]%>', '<%=bhm.getHeadId() %>');" 
						   	 		>
						   	 	<!-- 	<i class="fa fa-times-circle fa-2x"></i> -->
						   	 	</span>
						   	 	<%-- <input type="hidden" id="attchIndex_<%=i%>" name="attchIndex_<%=i%>" value="<%=cnt4%>"> --%>	
						   	 	</td>
						   	 	<tr>
						   	 	</div>	
							<%/* }else{ */
								if(flag){
							%>
							<div id="chooseUpload_<%=bhm.getHeadId() %>">
								<tr>
							<td style="text-align:center; width:9%; z-index:1;">
							<%-- <% int cnt1=0; for(int kk=0; kk<rowIndex; kk++){ cnt1=kk; cnt1++;%> --%>										 
						    	<div valign="top" style="margin-bottom:2px;">
						    		<div class="row">
						    			<div class="col-sm-12">
											<input type="file" id="upldoc_<%=i%>"  name="upldoc_<%=i%>" class="" onchange="filechk(this.id);" onblur="filechk(this.id);" 
												style="width: 150px;">
										</div>
						    		</div>   
						    	</div> 								  	
						  	<%-- <%} %> --%>									  	
														
						</td>
						</tr>
						</div>
							<%
								}
							}
							
							
							}
							//}
							%>
							<%if(bhm.getDocName().toString().equals("")){ %>
									<div valign="top" style="margin-bottom:2px;">
							    		<div class="row">
							    			<div class="col-sm-12">
												<input type="file" id="upldoc_<%=i%>"  name="upldoc_<%=i%>" class="" onchange="filechk(this.id);" onblur="filechk(this.id);" 
													style="width: 150px;">
											</div>
							    		</div>   
							    	</div>
							<%} %>
							<input type="hidden" id="attchIndex_<%=i%>" name="attchIndex_<%=i%>" value="">
							</table>
							
							</td>	</tr>					
					<%}
					
						}else{ %>
					
					
					<% 	if(arrayList.size()>0){
						String preSubHeadValue="",preHeadValue="";
						for(CreateManageExpenseModel bhm: arrayList){
							// System.out.println("bhm.getHeadId() arrayList.size()||"+arrayList.size());
						%>
			                <tr>
								<td style="text-align:center; width:3%;"><%=++i %></td>															
								<td style="text-align:center; width:9%;" >
									<input class=""  type="hidden" id="headId_<%=i%>" name="headId_<%=i%>" value="<%=bhm.getHeadId()%>" readonly="readonly"><%=bhm.getHeadName()%>
								</td>
								<td style="text-align:center; width:9%;" valign="top">
									<table style="width:100%">
											<!-- <td style="text-align:center; width:15%;"> -->
														<tr>
															<td>
															<%-- <%=sval[aa]%> --%>
															<%=bhm.getSubHeadName()%>
															<input class="form-control"  type="hidden" id="subHeadId_<%=i%>" name="subHeadId_<%=i%>" value="<%=bhm.getSubHeadId()%>">																
															<%-- <%=subHead.get(a).getHeadName() %> --%>
															<!-- </td> -->
															</td>
														</tr>
									</table>
									<input class="form-control"  type="hidden" id="subHeadIndex_<%=i%>" name="subHeadIndex_<%=i%>" value="<%=rowIndex%>">
								</td>
								<td style="text-align:center; width:11%;">
									<input class="form-control"  type="text" id="appAmt_<%=i%>" name="appAmt_<%=i%>" value="<%=bhm.getHeadAmt()%>" readonly="readonly" style="text-align:right;">
								</td>								
											
										
											
										<%
												if(checkRecord){
													 remainingBlnc=CreateManageExpenseManager.getRemainingBlnc(resPrps, finYrId, "'"+bhm.getHeadId()+"'",bhm.getSubHeadId());
												 /* if(remainingBlnc > 0){  */
														rBlnc=remainingBlnc;
												/* 	} */
												}else { 
														rBlnc=Integer.parseInt(bhm.getHeadAmt());
													}
											%> 
													
										<td style="text-align:center; width:11%;">
										<input class="form-control"  type="text" id="blncAmt_<%=i%>" name="blncAmt_<%=i%>" value="<%=rBlnc%>" readonly="readonly" style="text-align:right;">
														
								<td style="text-align:center; width:11%;" id="allDt">
									<input class="form-control"  type="text" id="allDt_<%=i%>" name="allDt1_<%=i%>" class="allDt" readonly="readonly" value="<%=bhm.getAppDate()%>">
								</td>
								
								<td style="text-align:center; width:9%;">
									<%
									if(rowIndex==0){
										rowIndex=1;
									}
									 %>										 
								    	<div valign="top" style="margin-bottom:2px;">
								    		<input class="form-control"  type="text" id="amt_<%=i%>" name="amt_<%=i%>" onblur=" IsValidAmt(this.value, <%=i%>); numberOrDecimalNumber(this);" onkeypress="if ( isNaN(this.value + String.fromCharCode(event.keyCode) )) return false;" onCopy="return false" onCut="return false" value="0" maxlength="10" style="text-align:right;">								    		
								    	</div>    								  	
								<input type="hidden" id="amtIndex_<%=i%>" name="amtIndex_<%=i%>" value="">							
								</td>
								<td style="text-align:center; width:9%;">
						    	<div valign="top" style="margin-bottom:2px;">
						    		<input type="text" maxlength="50" class="form-control" id="purpose_<%=i%>" name="purpose_<%=i%>" value=""  placeholder="Enter Particulars/Purpose" >
						    	</div>    								  	
							</td>
							
							<td style="text-align:center; width:11%;">
						    	<div valign="top" style="margin-bottom:2px;">
						    		<input type="text" maxlength="50" class="form-control" id="order_no_<%=i%>" name="order_no_<%=i%>" value=""  placeholder="Enter Sanction/ Order No" >
						    	</div>    								  	
							</td>
								<td style="text-align:center; width:9%; z-index:1;">
									<% int cnt1=0; for(int kk=0; kk<rowIndex; kk++){ cnt1=kk; cnt1++;%>										 
								    	<div valign="top" style="margin-bottom:2px;">
								    		<div class="row">
								    			<div class="col-sm-12">
													<input type="file" id="upldoc_<%=i%>"  name="upldoc_<%=i%>" class="" onchange="filechk(this.id);" onblur="filechk(this.id);" 
														style="width: 150px;">
												</div>
								    		</div>   
								    	</div> 								  	
								  	<%} %>									  	
								<input type="hidden" id="attchIndex_<%=i%>" name="attchIndex_<%=i%>" value="<%=cnt1%>">							
								</td>								
							 </tr> 
							 <%	
					}%>
					<%} }%>
					</tbody>
						<input type="hidden" id="index" name="index" value="<%=i%>">
				 </table>				 	
				 </div> 
				</div>
			</div>
		</div>
		  <div class="panel panel-default" id="manageClosingblnc" Style="display: none;">
     		<div class="panel-heading"><h3 class="panel-title text-right">View Head Wise Amount Detail</h3></div>
			<div class="panel-body" style="padding: 10px;">
            	
			<div class="col-md-12" style="padding: 0px;">
				<div id="" class="dataEntryDiv" style="overflow: unset;">
				  <table border="1" cellspacing="1" cellpadding="1" width="100%" class="tableGrid " >
				  <thead>
					<tr>
					  <th style="text-align:center; width:3%;">S.No.</th>
				  	  <th style="text-align:center; width:15%;">Head Name</th>
				  	   <th style="text-align:center; width:15%;">Sub Head Name</th>
					  <th style="text-align:center; width:10%;">Total Allocated Amount (Rs.)</th>
					  <th style="text-align:center; width:10%;" title="Expenses Amount for date <%=date%>">Total Expenses Amount (Rs.)</th>
					  <th style="text-align:center; width:10%;">Total Balance Amount (Rs.)</th>
					  <th style="text-align:center; width:10%;">Interest (Rs.)</th>
					  <th style="text-align:center; width:7%;">Total (Rs.)</th>
					</tr>
				 </thead>
				
				 <tbody id="blncmanage">
					
					<%int m=0;%>
					
					<%if(SaveDataForManageClosingblnc.size() > 0){ 
						System.out.println("size---"+SaveDataForManageClosingblnc.size());
						for(CreateManageExpenseModel Closinglist: SaveDataForManageClosingblnc){%>
						<tr>
					  		<td style="text-align:center; width:3%;"><%=++m %></td>															
							<td style="text-align:center; width:15%;" >
								<input type="hidden" id="headId_<%=m%>" name="headId_<%=m%>" value="<%=Closinglist.getHeadId()%>" readonly="readonly"><%=Closinglist.getHeadId()%>
							</td>
							<td style="text-align:center; width:15%;" >
								<input type="hidden" id="subHeadId_<%=m%>" name="subHeadId_<%=m%>" value="<%=Closinglist.getHeadId()%>" readonly="readonly"><%=Closinglist.getSubHeadName()%>
							</td>
							<td style="text-align:right; width:10%;"><%=Closinglist.getTotalAllocated()%></td>
							<td style="text-align:right; width:10%;"><%=Closinglist.getTotalExpenses()%></td>
							<td style="text-align:right; width:10%;"><%=Closinglist.getBlncAmt()%></td>
							<input type="hidden" id="balance_<%=m%>" name="balance_<%=m%>" value="<%=Closinglist.getBlncAmt()%>" >
							<%Integer intrest=(Integer.parseInt(Closinglist.getTotalAmt())-Integer.parseInt(Closinglist.getBlncAmt())); %>
							<td style="text-align:right; width:10%;" >
								<input type="text" id="intrest_<%=m%>" name="intrest_<%=m%>" value="<%=intrest.toString().contains("-")?"":intrest%>" onblur="IsInteger(this); calcIntTotal(<%=m%>);" style="text-align:right;">
							</td>
							<td style="text-align:right; width:17%;" >
								<input type="text" id="total_<%=m%>" name="total_<%=m%>" value="<%=Closinglist.getTotalAmt()%>" readonly style="text-align:right;">
							</td>
							
							</tr>
							<%System.out.println("total alloted---"+Closinglist.getTotalAllocated()+"expence--"+Closinglist.getTotalExpenses()+"balance amt"+Closinglist.getBlncAmt()+"intrest---"+intrest);}%>
							
						<%}%>
						<input type="hidden" id="totalindex" name="totalindex" value="<%=m%>" >
							<input type="hidden" id="installId" name="installId" value="<%=installId%>" >
							</tbody>
							 </table>
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
				<div class="col-md-12 text-center m-t-20 m-b-15 btndiv">
					<%if(General.checknull(fstatus.trim()).equals("E")) { %>
						<!-- <button type="button" class="btn btn-view" id="btnUpdate" >Update</button> -->
						<button type="button" class="btn btn-view" id="btnBack1" >Back</button>
					<%}else{%>
					<button type="button" class="btn btn-view" id="btnSave" onclick="saveData('INSERT_DELETE');">Save & Update</button>
					<%if(checkRecord) {%>
					<!-- <button type="button" class="btn btn-view" id="btnshowdiv" onclick="showdiv();">Click here to manage closing balance</button> -->
					<button type="button" class="btn btn-view" id="btnDelete" onclick="saveData('DELETE');">Delete Expense (<%=date%>)</button>
					
					<%}}%>
					<button type="button" class="btn btn-view" id="btnReset" >Reset</button>
					<%if(m>0){ %>
					<button type="button" class="btn btn-view" id="btnSavehead" onclick="saveheadTotal();" Style="display: none;">Save Closing Balance</button>
					<%} %>
					
					<input type="hidden" id="finYrId" name="finYrId" value="<%=finYrId%>">
					<input type="hidden" id="install_Id" name="install_Id" value="<%=installId%>">
					<input type="hidden" id="resPrps" name="resPrps" value="<%=resPrps%>">
					<input type="hidden" id="piId" name="piId" value="<%=piId%>">
					<input type="hidden" id="EXPDATE" name="EXPDATE" value="<%=date%>">
					<input type="hidden" id="locationCode" name="locationCode" value="<%=locationCode%>">
					<input type="hidden" id="ddoCode" name="ddoCode" value="<%=ddoCode%>">
					<input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">				
					<input type="hidden" id="AESKey" value="<%=session.getAttribute("AESUniqueKey")%>" />
				</div>
   </form>
   <%} %>
 
</body>
</html>
 <%}%>