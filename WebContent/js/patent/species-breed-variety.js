/**
 * @author Amit dangi
 */
$(document).ready(function(){
	$("#btnReset").click(function(){
		location.reload();
	});
	$("#btnReset1").click(function(){
		location.reload();
	});
	$("#xbtnReset").click(function(){
		document.getElementById("moufrmModal").reset();
	});
	   
	var status=$("#fstatus").val();
	if(status=="E"){
		getData(status);	
	}
	
	$("#Xlocation").change(function(){	
		var locationcode = $('#Xlocation').val();
		getDdoDetailbyLocation(locationcode);
	});
	$("#x_location").change(function(){	
		var locationcode = $('#x_location').val();
		getDdoDetailbyLocation(locationcode,'','x_ddo');
	});
	$("#location").change(function(){	
		var location = $('#location').val();
		getDdoDetailbyLocation(location,'','ddo');
	});
	
	//getLocationDetail('','Xlocation');
});
function validateFormFields(){
	try{
	
		if($('#Xlocation').val().trim()==''){
			$('#Xlocation').focus();
			showerr($("#Xlocation")[0], "Location is required!","block");			
			return false;
		}else if($('#Xddo').val().trim()==''){
			$('#Xddo').focus();
			showerr($("#Xddo")[0], "Ddo is required!","block");			
			return false;
		}else if($('#status').val().trim()==''){
			$('#status').focus();
			showerr($("#status")[0], "Status is required!","block");			
			return false;
		}else if($('#fn_agency').val().trim()==''){
			$('#fn_agency').focus();
			showerr($("#fn_agency")[0], "funding Agency is required!","block");			
			return false;
		}else if($('#rel_date').val().trim()==''){
			$('#rel_date').focus();
			showerr($("#rel_date")[0], "Releasing Date is required!","block");			
			return false;
		}else if($('#fstatus').val().trim()=='N' && $('#XUPLDOC').val().trim()==''){
				$('#XUPLDOC').focus();
				showerr($("#XUPLDOC")[0], "Upload Agenda Items/Notes  is required!","block");			
				return false;	
		}/*else if($('#fstatus').val().trim()=='N' && $('#XUPLDOC2').val().trim()==''){
			$('#XUPLDOC2').focus();
			showerr($("#XUPLDOC2")[0], "Upload Proceedings  is required!","block");			
			return false;	
	}*/
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}


function save(){
	if(validateFormFields()){
	try {
			var form_data = new FormData();
			var workarray = [];
			
				if($("#XUPLDOC").val()!="" && $("#XUPLDOC").val()!=undefined){
					var ext = $("#XUPLDOC").val().split('.').pop().toLowerCase();
					
					if(ext !=""){
						if($.inArray(ext, ['pdf']) == -1) {						
							$('#XUPLDOC').focus();
							alert("Note: Only .pdf files will be allowed for Upload Agenda Items/Notes!");
							return false;
						}
					}
					var	fsize=$('#XUPLDOC')[0].files[0].size;
					var file = Math.round((fsize / 1024));
					if (parseInt(file) > 20480 || parseInt(file) < 100) {
						$('#XUPLDOC').focus();
						alert("File size should be greater than 100 kb & less than 20 MB!");
						return false;
					}
					if ($("#XUPLDOC").val()!='' && $("#XUPLDOC").val()!=undefined){
						var fileCount = document.getElementById("XUPLDOC").files.length;
							form_data.append("upload_doc", document.getElementById("XUPLDOC").files[0]);
					}	
				}
				//alert("form_data---------"+form_data);
				
			var jsonObject={
					location 	  	: $('#Xlocation').val(),
					ddo 	  	  	: $('#Xddo').val(),
					 sts		    : $('#status').val(),
					 fn_agency		: $('#fn_agency').val(),
					 relsing_no  	: $('#rele_no').val(),
					 rel_date	  	: $('#rel_date').val(),
					 s_id			: $('#s_id').val(),
					 upl_id			: $('#upl_id').val(),
			};	
			//alert("json:" + JSON.stringify(jsonObject));
			//console.log("send jsonObject------ "+JSON.stringify(jsonObject));
			var encData=encAESData($("#AESKey").val(), jsonObject);
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../SpeciesBreedVarietyService");			    
			xmlHttp.setRequestHeader("encData", encData);
			xmlHttp.setRequestHeader("fstatus", $('#fstatus').val());
			xmlHttp.send(form_data);
			
			try{
				xmlHttp.onreadystatechange = function() {
					if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
						var data=JSON.parse(this.responseText);
						var result=decAESData($("#AESKey").val(), data)
						if(result.flg=="Y"){
							displaySuccessMessages("errMsg1", result.errMsg, "");
							clearSuccessMessageAfterFiveSecond("errMsg1");
							setTimeout(function() {
								 document.location.href = 'species_breed_variety_e.jsp';
							}, 2000);
						}else{
							displaySuccessMessages("errMsg2", result.errMsg, "");
							clearSuccessMessageAfterFiveSecond("errMsg2");
							setTimeout(function () {
		    					 location.reload();
		    				    }, 3000);
						}
					}
				}
			} catch (err){
				alert(err);
			}
	} catch (e) {
		alert("ERROR :"+e);
	}
	}else{
		return false
	}
}

function deleteRecord(id,fname){	
		var r = confirm("Are You Sure!");
		if (r == true){ 
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../SpeciesBreedVarietyService", true);			    
			xmlHttp.setRequestHeader("id", id);
			xmlHttp.setRequestHeader("fname", fname);
			xmlHttp.setRequestHeader("fstatus", "D");
	    	xmlHttp.send();
	   
	    try{
	    	xmlHttp.onreadystatechange = function() {
	    		//alert(xmlHttp.readyState+"|"+xmlHttp.status);
	    		if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
					var data=JSON.parse(this.responseText);
					var tRec = $("#totRow").val();
					for(var i=1;i<=tRec;i++){
						$("#EDIT_RECORD_"+i).wrap('<td style="display:none"/>');
						$("#DELETE_RECORD_"+i).wrap('<td style="display:none"/>');		            			
					}
					if(data.flg=="Y"){
					displaySuccessMessages("errMsg1", data.errMsg, "");
					clearSuccessMessageAfterFiveSecond("errMsg1");
					}else{
						displaySuccessMessages("errMsg2", data.errMsg, "");
						clearSuccessMessageAfterFiveSecond("errMsg2");
					}

					setTimeout(function() {
						location.reload();
					}, 2000);
	    		}
		}	
	} catch (err) {
		alert(err);
	}
}
}

function editRecord(m_id,fstatus){
	try {
		document.mouMoaFormL.target = "_parent";
		document.mouMoaFormL.action = "species_breed_variety_e.jsp?fstatus=E&m_id="+m_id;
		document.mouMoaFormL.submit();
	} catch (err) {
		alert("ERROR :"+err);
	}
}


function geteditDD(status, category,sub_category,locationCode,ddoCode){
	try {
		//alert(status);
		if(status=="E" || status=="N"){
			getLocationDetail(locationCode);
			getDdoDetailbyLocation(locationCode,ddoCode);
			getDdoDetailbyLocation(locationCode,ddoCode,'x_ddo');
		}else{
			getLocationDetail(locationCode,'Xlocation');
			getDdoDetailbyLocation(locationCode,ddoCode,'Xddo');
		}
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}
	//On the click of back button from the edit page , searched value will appned to the e page 
	function vldBack(x_location) {
		try {
		document.moufrmModal.action="species_breed_variety_e.jsp";
		document.moufrmModal.submit();
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	function getSearchList(status,x_location) {
		try {
			if(status!="E" ){
				document.moufrmModal.target="frmSpeciesFrame";
				document.moufrmModal.action="species_breed_variety_l.jsp" ;
				document.moufrmModal.submit();
			}
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	
