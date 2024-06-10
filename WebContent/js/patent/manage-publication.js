
$(document).ready(function(){
	$('#btnReset').click(function(){    // call on Reset  button
		document.location.reload();
	});
	$('#btnback').click(function(){   
		document.location.href="manage_publication_e.jsp";
	});
		 $("#xbtnReset").click(function(){
				   	document.getElementById("frmdismasterd").reset();
});

	
});
function showHide(publications) {
	//var publications = $("#publication").val();
	if(publications=='1'){
		$("#author_nameS").show();   				
		$("#publication_year_articleS").show();
		$("#article_titleS").show();
		$("#journal_nameS").show();
		$("#volume_noS").show();
		$("#issue_noS ").show();
		$("#pagesS").show();
		$("#naas_ratingS").show();
		
		$("#doi_numberS").hide();
		$("#year_publicationS").hide();
		$("#paper_titleS").hide();
		$("#symposium_nameS").hide();
		$("#paginationS").hide();
		$("#placeS").hide();
		$("#research_paper_titleS").hide();
		$("#symposium_dateS").hide();
		$("#name_of_magazineS").hide();
		$("#book_titleS").hide();
		$("#place_of_publicationS").hide();
		$("#name_publisherS").hide();
		$("#contribution_titleS").hide();
		$("#connecting_wordS").hide();
		$("#host_document_itemsS").hide();
		$("#edition_noS").hide();
		$("#later_editionS").hide();
		$("#college_nameS").hide();
		$("#bulletin_titleS").hide();
	}if(publications=='2'){
		$("#author_nameS").show();				
		$("#publication_year_articleS").show();
		$("#article_titleS").show();
		$("#journal_nameS").show();
		$("#doi_numberS").show();

		$("#volume_noS").hide();
		$("#issue_noS ").hide();
		$("#pagesS").hide();
		$("#naas_ratingS").hide();
		$("#year_publicationS").hide();
		$("#paper_titleS").hide();
		$("#symposium_nameS").hide();
		$("#paginationS").hide();
		$("#placeS").hide();
		$("#research_paper_titleS").hide();
		$("#symposium_dateS").hide();
		$("#name_of_magazineS").hide();
		$("#book_titleS").hide();
		$("#place_of_publicationS").hide();
		$("#name_publisherS").hide();
		$("#contribution_titleS").hide();
		$("#connecting_wordS").hide();
		$("#host_document_itemsS").hide();
		$("#edition_noS").hide();
		$("#later_editionS").hide();
		$("#college_nameS").hide();
		$("#bulletin_titleS").hide();
	}if(publications=='3'){
		$("#author_nameS").show()   				
		$("#year_publicationS").show()
		$("#paper_titleS").show()
		$("#symposium_nameS").show()
		$("#paginationS").show()
		$("#symposium_dateS").show()
		$("#placeS").show()

		$("#publication_year_articleS").hide();
		$("#article_titleS").hide();
		$("#journal_nameS").hide();
		$("#volume_noS").hide();
		$("#issue_noS ").hide();
		$("#pagesS").hide();
		$("#naas_ratingS").hide();
		$("#doi_numberS").hide();
		$("#research_paper_titleS").hide();
		$("#name_of_magazineS").hide();
		$("#book_titleS").hide();
		$("#place_of_publicationS").hide();
		$("#name_publisherS").hide();
		$("#contribution_titleS").hide();
		$("#connecting_wordS").hide();
		$("#host_document_itemsS").hide();
		$("#edition_noS").hide();
		$("#later_editionS").hide();
		$("#college_nameS").hide();
			$("#bulletin_titleS").hide();
	}if(publications=='4'){
		$("#author_nameS").show()   				
		$("#publication_year_articleS").show()
		$("#research_paper_titleS").show()
		$("#symposium_nameS").show()
		$("#paginationS").show()
		$("#symposium_dateS").show()
		$("#placeS").show()

		$("#article_titleS").hide();
		$("#journal_nameS").hide();
		$("#volume_noS").hide();
		$("#issue_noS ").hide();
		$("#pagesS").hide();
		$("#naas_ratingS").hide();
		$("#doi_numberS").hide();
		$("#year_publicationS").hide();
		$("#paper_titleS").hide();
		$("#name_of_magazineS").hide();
		$("#book_titleS").hide();
		$("#place_of_publicationS").hide();
		$("#name_publisherS").hide();
		$("#contribution_titleS").hide();
		$("#connecting_wordS").hide();
		$("#host_document_itemsS").hide();
		$("#edition_noS").hide();
		$("#later_editionS").hide();
		$("#college_nameS").hide();
			$("#bulletin_titleS").hide();
	}if(publications=='5'){
		$("#author_nameS").show()   				
		$("#publication_year_articleS").show()
		$("#article_titleS").show()
		$("#name_of_magazineS").show()
		$("#volume_noS").show()
		$("#issue_noS").show()
		$("#pagesS").show()

		$("#journal_nameS").hide();
		$("#naas_ratingS").hide();
		$("#doi_numberS").hide();
		$("#year_publicationS").hide();
		$("#paper_titleS").hide();
		$("#symposium_nameS").hide();
		$("#paginationS").hide();
		$("#placeS").hide();
		$("#research_paper_titleS").hide();
		$("#symposium_dateS").hide();
		$("#book_titleS").hide();
		$("#place_of_publicationS").hide();
		$("#name_publisherS").hide();
		$("#contribution_titleS").hide();
		$("#connecting_wordS").hide();
		$("#host_document_itemsS").hide();
		$("#edition_noS").hide();
		$("#later_editionS").hide();
		$("#college_nameS").hide();
			$("#bulletin_titleS").hide();
	}if(publications=='6'){
		$("#author_nameS").show()   				
		$("#year_publicationS").show()
		$("#book_titleS").show()
		$("#volume_noS").show()
		$("#place_of_publicationS").show()
		$("#name_publisherS").show()
		$("#paginationS").show()

		$("#publication_year_articleS").hide();
		$("#article_titleS").hide();
		$("#journal_nameS").hide();
		$("#issue_noS ").hide();
		$("#pagesS").hide();
		$("#naas_ratingS").hide();
		$("#doi_numberS").hide();
		$("#paper_titleS").hide();
		$("#symposium_nameS").hide();
		$("#placeS").hide();
		$("#research_paper_titleS").hide();
		$("#symposium_dateS").hide();
		$("#name_of_magazineS").hide();
		$("#contribution_titleS").hide();
		$("#connecting_wordS").hide();
		$("#host_document_itemsS").hide();
		$("#edition_noS").hide();
		$("#later_editionS").hide();
		$("#college_nameS").hide();
		$("#bulletin_titleS").hide();
	}if(publications=='7'){
		$("#author_nameS").show()   				
		$("#year_publicationS").show()
		$("#contribution_titleS").show()
		$("#connecting_wordS").show()
		$("#host_document_itemsS").show()
		$("#volume_noS").show()
		$("#edition_noS").show()
		$("#later_editionS").show()
		$("#place_of_publicationS").show()
		$("#name_publisherS").show()
		$("#paginationS").show()

		$("#author_nameS").hide();   				
		$("#article_titleS").hide();
		$("#journal_nameS").hide();
		$("#issue_noS ").hide();
		$("#pagesS").hide();
		$("#naas_ratingS").hide();
		$("#doi_numberS").hide();
		$("#paper_titleS").hide();
		$("#symposium_nameS").hide();
		$("#placeS").hide();
		$("#research_paper_titleS").hide();
		$("#symposium_dateS").hide();
		$("#name_of_magazineS").hide();
		$("#book_titleS").hide();
		$("#college_nameS").hide();
		$("#bulletin_titleS").hide();
	}if(publications=='8'){
		$("#author_nameS").show()   				
		$("#year_publicationS").show()
		$("#bulletin_titleS").show()
		$("#college_nameS").show();
		$("#pagesS").show()

		$("#publication_year_articleS").hide();
		$("#article_titleS").hide();
		$("#journal_nameS").hide();
		$("#volume_noS").hide();
		$("#issue_noS ").hide();
		$("#naas_ratingS").hide();
		$("#doi_numberS").hide();
		$("#paper_titleS").hide();
		$("#symposium_nameS").hide();
		$("#paginationS").hide();
		$("#placeS").hide();
		$("#research_paper_titleS").hide();
		$("#symposium_dateS").hide();
		$("#name_of_magazineS").hide();
		$("#book_titleS").hide();
		$("#place_of_publicationS").hide();
		$("#name_publisherS").hide();
		$("#contribution_titleS").hide();
		$("#connecting_wordS").hide();
		$("#host_document_itemsS").hide();
		$("#edition_noS").hide();
		$("#later_editionS").hide();
	}if(publications=='9'){
		$("#author_nameS").show()   				

		$("#publication_year_articleS").hide();
		$("#article_titleS").hide();
		$("#journal_nameS").hide();
		$("#volume_noS").hide();
		$("#issue_noS ").hide();
		$("#pagesS").hide();
		$("#naas_ratingS").hide();
		$("#doi_numberS").hide();
		$("#year_publicationS").hide();
		$("#paper_titleS").hide();
		$("#symposium_nameS").hide();
		$("#paginationS").hide();
		$("#placeS").hide();
		$("#research_paper_titleS").hide();
		$("#symposium_dateS").hide();
		$("#name_of_magazineS").hide();
		$("#book_titleS").hide();
		$("#place_of_publicationS").hide();
		$("#name_publisherS").hide();
		$("#contribution_titleS").hide();
		$("#connecting_wordS").hide();
		$("#host_document_itemsS").hide();
		$("#edition_noS").hide();
		$("#later_editionS").hide();
		$("#college_nameS").hide();   
			$("#bulletin_titleS").hide();
	}
}
function vldSearch(){

	document.managePublicationE.target = "managePublicationD";
	document.managePublicationE.action = "manage_publication_l.jsp";
	document.managePublicationE.submit();
	document.managePublicationE.reset();
}

function saveRecord(fstatus){
	if($('#Xlocation').val() == ''){
		showerr($("#Xlocation")[0], "Location Name is required!", "block");
		$('#Xlocation').focus();
		return false;
    }
	else if($('#Xddo').val() == ''){
		showerr($("#Xddo")[0], "Ddo Area is required!", "block");
		$('#Xddo').focus();
		return false;
    }else if($('#publications').val() == ''){
		showerr($("#publications")[0], "Publications is required!", "block");
		$('#publications').focus();
		return false;
    }else{
		try{
			
			let objjson={
			"LOCATION_CODE":$("#Xlocation").val(),
			 "pub_id":$("#publication").val(),
			 "DDO_ID":$("#Xddo").val(),
			 "author_name":$("#author_name").val(),  				
			 "publication_year_article":$("#publication_year_article").val(),
			 "article_title":$("#article_title").val(),
			"journal_name":$("#journal_name").val(),
			"volume_no":$("#volume_no").val(),
			"issue_no" :$("#issue_no").val(),
			"pages":$("#pages").val(),
			"naas_rating":$("#naas_rating").val(),
			"doi_number":$("#doi_number").val(),
			"year_publication":$("#year_publication").val(),
			"paper_title":$("#paper_title").val(),
			"symposium_name":$("#symposium_name").val(),
			"pagination":$("#pagination").val(),
			"place":$("#place").val(),
			"research_paper_title":$("#research_paper_title").val(),
			"symposium_date":$("#symposium_date").val(),
			"name_of_magazine":$("#name_of_magazine").val(),
			"book_title":$("#book_title").val(),
			"place_of_publication":$("#place_of_publication").val(),
			"name_publisher":$("#name_publisher").val(),
			"contribution_title":$("#contribution_title").val(),
			"connecting_word":$("#connecting_word").val(),
			"host_document_items":$("#host_document_items").val(),
			"edition_no":$("#edition_no").val(),
			"later_edition":$("#later_edition").val(),
			"college_name":$("#college_name").val(),
			"bulletin_title":$("#bulletin_title").val(),
			"id":$("#id").val(),
			"fstatus":$("#fstatus").val(),
	
			};
			//alert("wdwdw"+JSON.stringify(objjson));
			var encData=encAESData($("#AESKey").val(), objjson);
			//alert("encData"+encData)
			$.ajax({
				type : "POST",
				url : '../ManagePublicationService',
				data : {		    	
			    	encData : encData,
			    	fstatus :$("#fstatus").val(),
			    },success : function(response) {
			    	var result=decAESData($("#AESKey").val(), response)
			    	if(result.flg == "Y"){
			    		displaySuccessMessages("errMsg1", result.errMsg, "");
			    		clearSuccessMessageAfterTwoSecond("errMsg1");
			    		setTimeout(function () {
			    			document.location.href="manage_publication_e.jsp";
			    		}, 5000);			    		
			    	  }else{
			    		    displaySuccessMessages("errMsg2", result.errMsg, "");
				    		clearSuccessMessageAfterTwoSecond("errMsg2");
			    	  }
			        
			    },
				error : function(xhr, status, error) {
					alert("error");
				}
			});
															
		}catch(e){
			alert("view: "+e);
		}		
     
 
				
	}	 			 

	
}

function editRecord(lid,typ) {
	//alert(lid+"typ"+typ);
	try {

		 if(typ=="D"){
			var del=confirm("Are You Sure?")
		    if(del==true){
		    	$.ajax({
		    		type : "POST",
		    		url : '../ManagePublicationService',
		    		data: 'fstatus=D&id='+lid,	
		    		success : function(response){
				    	if(response.flg == "Y"){
				    		displaySuccessMessages("errMsg1", response.errMsg, "");
				    		clearSuccessMessageAfterTwoSecond("errMsg1");
				    		setTimeout(function () {
				    			location.reload();
				    		}, 5000);			    		
				    	  }else{
				    		    displaySuccessMessages("errMsg2", response.errMsg, "");
					    		clearSuccessMessageAfterTwoSecond("errMsg2");
				    	  }
				        
				    }
		    	});
		    }
		}else {
			document.getElementById("id").value=lid;
			document.getElementById("opt_typ").value=typ;
			document.ManagePublicationL.target = "_parent";
			document.ManagePublicationL.action = "manage_publication_e.jsp";
			document.ManagePublicationL.submit();
		}
	} catch (err) {alert(err);}
}
