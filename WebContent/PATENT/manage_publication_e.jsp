<%@page import="com.sits.patent.manage_publication.ManagePublicationManager"%>
<%@page import="com.sits.patent.manage_publication.ManagePublicationModel"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sits.general.ApplicationConstants"%>
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
	<script type="text/javascript" src="../js/patent/manage-publication.js"></script>
	
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
	 <link rel="stylesheet" href="../css/datepicker/datepicker3.css"  type="text/css">
	<script src="../js/datepicker/bootstrap-datepicker.js"></script> 
</head>
<script type="text/javascript">

   $(document).ready(function() { 
		$("#symposium_date").datepicker({
			format : 'dd/mm/yyyy',
			autoclose : true,
		}).on('click', function() {
			$("#symposium_date").datepicker("setDate", 'today');
		}).on('clearDate', function(selected) {
		});
		
 });
 </script>

	<%
	
	String OPT_VALUE="", OPT_TYP = "";
	int cnt=0;
	String location="",ddo="",publication="",author_name="",publ_year_article="",title="",
			journal_name="",volno="",issno="",pages="",naas_rat="",doi_no="",year_publ="",
			symposium_name="",pagination="",place="",symposium_date="",magazine="",id="",
			placePublication="",name_publ="",connWord="",hostItems="",edition_no="",later_edition="",
			college_name="";
 	OPT_TYP = General.checknull(request.getParameter("opt_typ"));
 	id = General.checknull(request.getParameter("id"));
 	ManagePublicationModel Model =ManagePublicationManager.EditRecord(id);

if(OPT_TYP.equals("")) {
		cnt=1;
		OPT_TYP = "N";
		
		OPT_VALUE = ApplicationConstants.NEW;
		location= location.equals("")?loct_id:location;
		ddo= ddo.equals("")?ddo_id:ddo;
	}else if(OPT_TYP.equals("E")) {
		
		OPT_VALUE = ApplicationConstants.EDIT;
		try {
			location =General.checknull(Model.getLOCATION_CODE());
			ddo=General.checknull(Model.getDDO_ID());
			publication =General.checknull(Model.getPub_id());
			author_name= General.checknull(Model.getAuthor_name());
			publ_year_article= General.checknull(Model.getPublication_year_article());
			title= General.checknull(Model.getPaper_tittle());
			journal_name=General.checknull(Model.getJournal_name());
			volno=General.checknull(Model.getVolume_no());
			issno=General.checknull(Model.getIssue_no());
			pages=General.checknull(Model.getPages());
			naas_rat=General.checknull(Model.getNaas_rating());
			doi_no=General.checknull(Model.getDoi_number());
			year_publ=General.checknull(Model.getYear_publication());
			symposium_name=General.checknull(Model.getSymposium_name());
			pagination=General.checknull(Model.getPagination());
			place=General.checknull(Model.getPlace());
			symposium_date=General.checknull(Model.getSymposium_date());
			magazine=General.checknull(Model.getName_of_magazine());
			id=General.checknull(Model.getId());
			placePublication=General.checknull(Model.getPlace_of_publication());
			name_publ=General.checknull(Model.getName_publisher());
			connWord=General.checknull(Model.getConnecting_word());
			hostItems=General.checknull(Model.getHost_document_items());
			edition_no=General.checknull(Model.getEdition_no());
			later_edition=General.checknull(Model.getLater_edition());
			college_name=General.checknull(Model.getCollege_name());
			//System.out.println("Error in location.jsp"+location);
			//System.out.println("Error in ddo.jsp"+ddo);
			
		}catch(Exception e){System.out.println("Error in manage_publication_e.jsp"+e.getMessage());	
	}}
%>

<body onload="showHide(<%=publication%>);getLocationDetail('<%=location%>','Xlocation');getDdoDetailbyLocation('<%=location%>','<%=ddo%>','Xddo');getLocationDetail('','location');">
<div class="container-fluid">
<div id="" class="page-header"><h4><%=ReadProps.getkeyValue("manage_publications.header","sitsResource") %>
     <span style="float:right">
	  <a href="" data-toggle="modal" data-target=".recomond-btn" style="text-decoration:none;"> <span>Advance Search</span>
	 	<i class="fa fa-search"></i>
	  </a>
	 </span> 
	 </h4>
 </div>
   <form class="form-horizontal" name="managePublE" id="managePublE" method="post" autocomplete="off">
	    <div class="panel panel-default">
   <% if(OPT_TYP.equals("N")){%>
     <div class="panel-heading"><h3 class="panel-title text-right">New Record</h3></div>
     <%}else{ %>
       <div class="panel-heading"><h3 class="panel-title text-right">Edit Record</h3></div>
   <%} %>
	<div class="panel-body">
		   <div class="form-group" >
			<div class="col-md-12">
			<div class="row">
      				<label for="location" class="col-sm-7 col-form-label text-left ">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="Xlocation" name="Xlocation" <%if(user_status.equals("U")){ %> disabled <%} %>  onchange="getDdoDetailbyLocation(this.value,'<%=ddo%>');">
									<option value="">Select Location</option>
								</select>
				           </div>
						</div>
				</div>
				</div>
				<div class="form-group" >
			<div class="col-md-12">
			<div class="row">
					<label for="ddo" class="col-sm-7 col-form-label text-left  required-field">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="Xddo" name="Xddo" <%if(user_status.equals("U")){ %> disabled <%} %>>
      						 <option value="">Select DDO</option>
      						</select>      				
      				</div>      						
				</div>
				</div>
				</div>
		     <div class="form-group">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-7 col-form-label required-field" for="">publication</label>
							  <div class="col-sm-4" >
								<select class="form-control"  <%if(OPT_TYP.equals("E")){ %>disabled<%} %>  id="publication" name="publication" onchange="showHide(this.value);">
								 	<option value="">Select publication</option>	
									 <%=QueryUtil.getComboOption("RSRCH_publications", "PUB_ID", "PUB_TYP ",publication,"", "PUB_ID") %> 			    
								</select>
						   </div>
					</div>
		        </div>
		    </div> 
		     <div class="form-group" id="author_nameS" style="display:none">
			  <div class="col-sm-12">
			    <div class="row" >
						   	<label class="col-sm-7 col-form-label text-left " for="">Author name</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="author_name" name="author_name" value="<%=author_name %>" onblur=""  placeholder="Enter Author name" >
				       </div>
				      </div>
		        </div>
		    </div> 
		    <div class="form-group" id="publication_year_articleS" style="display:none">
			  <div class="col-sm-12">
			    <div class="row" >
				        	<label class="col-sm-7 col-form-label text-left " for="">Publication year article</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="publication_year_article" name="publication_year_article" value="<%=publ_year_article %>" onblur=""  placeholder="Enter Publication year article" >
				       </div>
				       </div> 
				      </div>
		        </div>
				  <div class="form-group" id="bulletin_titleS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" >
						   	<label class="col-sm-7 col-form-label text-left " for="">Title of the Bulletin</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="bulletin_title" name="bulletin_title" value="<%=title %>" onblur=""  placeholder="Enter Article title" >
				       </div>
					</div>
		        </div>
		    </div> 
		     <div class="form-group"  id="article_titleS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" >
						   	<label class="col-sm-7 col-form-label text-left " for="">Article title</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="article_title" name="article_title" value="<%=title %>" onblur=""  placeholder="Enter Article title" >
				       </div>
					</div>
		        </div>
		    </div> 
		     <div class="form-group" id="journal_nameS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" >
				        	<label class="col-sm-7 col-form-label text-left " for="">Journal name</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="journal_name" name="journal_name" value="<%=journal_name %>" onblur=""  placeholder="Enter Journal name" >
				       </div>
				       </div>
		        </div>
		    </div> 
		   <div class="form-group" id="volume_noS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Volume no</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="volume_no" name="volume_no" value="<%=volno %>" onblur=""  placeholder="Enter Volume no" >
				       </div>
					</div>
		        </div>
		    </div>
		     <div class="form-group" id="issue_noS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				        	<label class="col-sm-7 col-form-label text-left " for="">Issue no</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="issue_no" name="issue_no" value="<%=issno %>" onblur=""  placeholder="Enter Issue no" >
				       </div>
					</div>
		        </div>
		    </div>
		    <div class="form-group" id="pagesS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Pages</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="pages" name="pages" value="<%=pages%>" onblur=""  placeholder="Enter Pages" >
				       </div>
					</div>
		        </div>
		    </div>
		     <div class="form-group" id="naas_ratingS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				        	<label class="col-sm-7 col-form-label text-left " for="">Naas rating</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="naas_rating" name="naas_rating" value="<%=naas_rat%>" onblur=""  placeholder="Enter Naas rating" >
				       </div>
				       </div>
		        </div>
		    </div>
		    <div class="form-group" id="year_publication"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Year publication</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="year_publication" name="year_publication" value="<%=year_publ %>" onblur=""  placeholder="Enter Year publication" >
				       </div>
					</div>
		        </div>
		    </div>
		     <div class="form-group" id="doi_numberS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						  	<label class="col-sm-7 col-form-label text-left " for="">Doi number</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="doi_number" name="doi_number" value="<%=doi_no %>" onblur=""  placeholder="Enter Doi number" >
				       </div> 
					</div>
		        </div>
		    </div>
		    <div class="form-group" id="paper_titleS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-7 col-form-label text-left " for="">Paper title</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="paper_title" name="paper_title" value="<%=title %>" onblur=""  placeholder="Enter Paper title" >
				       </div>
					</div>
		        </div>
		    </div>
		     <div class="form-group" id="symposium_nameS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Symposium name</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="symposium_name" name="symposium_name" value="<%=symposium_name %>" onblur=""  placeholder="Enter Symposium name" >
				       </div>
					</div>
		        </div>
		    </div>
				 <div class="form-group" id="paginationS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-7 col-form-label text-left " for="">Pagination</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="pagination" name="pagination" value="<%=pagination %>" onblur=""  placeholder="Enter Pagination" >
				       </div>
					</div>
		        </div>
		    </div>	
		    	 <div class="form-group"  id="placeS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Place</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="place" name="place" value="<%=place %>" onblur=""  placeholder="Enter Place" >
				       </div>
				       </div>
		        </div>
		    </div>	
		     <div class="form-group" id="research_paper_titleS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-7 col-form-label text-left " for="">Research paper title</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="research_paper_title" name="research_paper_title" value="<%=title %>" onblur=""  placeholder="Enter Research paper title" >
				       </div>
					</div>
		        </div>
		    </div>	
		      <div class="form-group" id="symposium_dateS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Symposium date</label>
						<div class="col-sm-4">
				       <div class="input-group date" id="msg-proj_start_date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar" 
									id="symposium_date" name="symposium_date" placeholder="DD/MM/YYYY" value="<%=symposium_date %>" >
								</div>	
				       </div>
				       
					</div>
		        </div>
		    </div>	
		    <div class="form-group" id="name_of_magazineS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-7 col-form-label text-left " for="">Name of magazine</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="name_of_magazine" name="name_of_magazine" value="<%=magazine %>" onblur=""  placeholder="Enter Name of magazine" >
				       </div>
					</div>
		        </div>
		    </div>	
		      <div class="form-group" id="book_titleS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Book title</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="book_title" name="book_title" value="<%=title %>" onblur=""  placeholder="Enter Book title" >
				       </div>
					</div>
		        </div>
		    </div>	
		     <div class="form-group" id="place_of_publication"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-7 col-form-label text-left " for="">Place of publication</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="place_of_publication" name="place_of_publication" value="<%=placePublication %>" onblur=""  placeholder="Enter Place of publication" >
				       </div>
				       </div>
		        </div>
		    </div>	
		    <div class="form-group" id="name_publisherS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Name publisher</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="name_publisher" name="name_publisher" value="<%=name_publ %>" onblur=""  placeholder="Enter Name publisher" >
				       </div>
				       </div>
		        </div>
		    </div>	
		    <div class="form-group" id="contribution_titleS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-7 col-form-label text-left " for="">Contribution title</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="contribution_title" name="contribution_title" value="<%=title %>" onblur=""  placeholder="Enter Contribution title" >
				       </div>
					</div>
		        </div>
		    </div>	
		    <div class="form-group" id="connecting_wordS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Connecting word</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="connecting_word" name="connecting_word" value="<%=connWord %>" onblur=""  placeholder="Enter Connecting word" >
				       </div>
				       </div>
		        </div>
		    </div>	
		     <div class="form-group" id="host_document_itemsS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-7 col-form-label text-left " for="">Host document items</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="host_document_items" name="host_document_items" value="<%=hostItems %>" onblur=""  placeholder="Enter Host document items" >
				       </div>
					</div>
		        </div>
		    </div>	
		    <div class="form-group"id="edition_noS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">Edition no</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="edition_no" name="edition_no" value="<%=edition_no %>" onblur=""  placeholder="Enter Edition no" >
				       </div>
				       </div>
					</div>
		    </div>	
		      <div class="form-group"  id="later_editionS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-7 col-form-label text-left " for="">Later edition</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="later_edition" name="later_edition" value="<%=later_edition %>" onblur=""  placeholder="Enter Later edition" >
				       </div>
					</div>
		        </div>
		    </div>
		    <div class="form-group" id="college_nameS"style="display:none">
			  <div class="col-sm-12">
			    <div class="row" > 
						   	<label class="col-sm-7 col-form-label text-left " for="">College name</label>
						<div class="col-sm-4">
						<input type="text" maxlength="150" class="form-control" id="college_name" name="college_name" value="<%=college_name %>" onblur=""  placeholder="Enter College name" >
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
			 <input type="hidden" name=hrmsApi id="hrmsApi" value="<%=General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource")) %>">
				<button type="button" class="btn btn-view" id="btnReset" onclick="">Reset</button> 			
				<input type= "hidden" id="id" value="<%=id %>"  /> 
				<input type= "hidden" id="fstatus" name="fstatus" value="<%=OPT_TYP %>"  /> 
				 <input type= "hidden" id="AESKey" value="<%= session.getAttribute("AESUniqueKey")%>" style="width:0px;" readOnly/> 
<!--  				 	<input type= "hidden" id="AESKey" value="123" style="width:0px;" readOnly/>  
 -->		
				</div>
				       </div>
				    </div>
				    </div>
	      </div><!-- End panel-default -->
        </form>
        <form class="form-horizontal" name="managePublicationE" id="managePublicationE" action="" method="post" autocomplete="off">
      <div class="modal fade recomond-btn" id="myModal" tabindex="-1" role="dialog" aria-labelledby="openModal" aria-hidden="true" style="display: none;">
	    <div class="modal-dialog modal-lg">
		  <div class="modal-content">
			<div class="modal-header"> <button type="button" style="color:#d71414 !important; opacity:.5 ;" class="close" data-dismiss="modal" aria-hidden="true">X</button></div>
			  <div class="modal-body">
			   <div class="panel panel-default">
                <div class="panel-heading"><h3 class="panel-title text-right">Searching Criteria</h3></div>
     			<div class="panel-body">
		<!-- -------------------------------Write content inside this modal------------------------------------------- -->
			  <div class="form-group" >
			<div class="col-md-12">
			<div class="row">
      				<label for="location" class="col-sm-2 col-form-label text-left ">Location</label>
      				<div class="col-sm-4 ">
								<select class="form-control" id="location" name="location"  onchange="getDdoDetailbyLocation(this.value,'','ddo');">
									<option value="">Select Location</option>
								</select>
				           </div>
						
					<label for="ddo" class="col-sm-2 col-form-label text-left  ">DDO</label>
      				<div class="col-sm-4"><select class="form-control ddo" id="ddo" name="ddo" >
      						 <option value="">Select DDO</option>
      						</select>      				
      				</div>      						
				</div>
				</div>
				</div>
		     <div class="form-group">
			  <div class="col-sm-12">
			    <div class="row" > 
				       <label class="col-sm-2 col-form-label " for="">publication</label>
							  <div class="col-sm-4" >
								<select class="form-control" id="Xpublication" name="Xpublication" >
								 	<option value="">Select publication</option>	
									 <%=QueryUtil.getComboOption("RSRCH_publications", "PUB_ID", "PUB_TYP ","","", "PUB_ID") %> 			    
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
	<iframe class="embed-responsive-item " onload="resizeIframe(this)"  src="manage_publication_l.jsp" name="managePublicationD" id="managePublicationD"   frameborder="0" scrolling="no" width="100%" height=""></iframe>
    </div> <!-- End container-fluid -->
  </body>
  <script type="text/javascript">
function resizeIframe(iframe) {
    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
    window.requestAnimationFrame(() => resizeIframe(iframe));
}
		</script>
</html>
    