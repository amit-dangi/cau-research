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
	
	getLocationDetail('','Xlocation');
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
		}else if($('#fin_yr').val().trim()==''){
			$('#fin_yr').focus();
			showerr($("#fin_yr")[0], "Financial Year is required!","block");			
			return false;
		}else if($('#meeting_date').val().trim()==''){
			$('#meeting_date').focus();
			showerr($("#meeting_date")[0], "Meeting Date is required!","block");			
			return false;
		}else if($('#fstatus').val().trim()=='N' && $('#XUPLDOC').val().trim()==''){
				$('#XUPLDOC').focus();
				showerr($("#XUPLDOC")[0], "Upload Agenda Items/Notes  is required!","block");			
				return false;	
		}else if($('#fstatus').val().trim()=='N' && $('#XUPLDOC2').val().trim()==''){
			$('#XUPLDOC2').focus();
			showerr($("#XUPLDOC2")[0], "Upload Proceedings  is required!","block");			
			return false;	
	}
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
				
				
				if($("#XUPLDOC2").val()!="" && $("#XUPLDOC2").val()!=undefined){
					var ext = $("#XUPLDOC2").val().split('.').pop().toLowerCase();
					
					if(ext !=""){
						if($.inArray(ext, ['pdf']) == -1) {						
							$('#XUPLDOC2').focus();
							alert("Note: Only .pdf files will be allowed for Upload Proceedings !");
							return false;
						}
					}
					var	fsize=$('#XUPLDOC2')[0].files[0].size;
					var file = Math.round((fsize / 1024));
					if (parseInt(file) > 20480 || parseInt(file) < 100) {
						$('#XUPLDOC2').focus();
						alert("File size should be greater than 100 kb & less than 20 MB!");
						return false;
					}
					if ($("#XUPLDOC2").val()!='' && $("#XUPLDOC2").val()!=undefined){
						var fileCount = document.getElementById("XUPLDOC2").files.length;
							form_data.append("upload_doc2", document.getElementById("XUPLDOC2").files[0]);
					}	
				}
				
			
				
			var jsonObject={
				 LOCATION_CODE 	  	: $('#Xlocation').val(),
				 DDO_ID 	  	  	: $('#Xddo').val(),
				 meeting_type_id  	: $('#meeting_type_id').val(),
				 others 		    : $('#others').val(),
				 fin_yr		  		: $('#fin_yr').val(),
				 meeting_date	  	: $('#meeting_date').val(),
				 m_id			  	: $('#m_id').val(),
			};			
			console.log("send jsonObject------ "+JSON.stringify(jsonObject));
			var encData=encAESData($("#AESKey").val(), jsonObject);
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../MeetingMappingService");			    
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
								 document.location.href = 'meeting_mapping_e.jsp';
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
			xmlHttp.open("POST", "../MeetingMappingService", true);			    
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

function editRecord(m_id,fstatus,x_location){
	try {
		document.mouMoaFormL.target = "_parent";
		document.mouMoaFormL.action = "meeting_mapping_e.jsp?fstatus=E&m_id="+m_id+"&x_location="+x_location;
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
	function vldBack(x_location,x_mou_type) {
		try {
		document.moufrmModal.action="meeting_mapping_e.jsp";
		document.moufrmModal.submit();
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	function getSearchList(status,x_location,x_mou_type) {
		try {
			if(status!="E" ){
				document.moufrmModal.target="mouMoaFormLFrame";
				document.moufrmModal.action="meeting_mapping_l.jsp" ;
				document.moufrmModal.submit();
			}
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	
	function showothers(meetingid) {
		var selectedmetting = $("#meeting_type_id option:selected").text();
		if(selectedmetting=='Others'){
			 $("#others").attr("readonly", false); 
		}else{
			$("#others").val("");
			 $("#others").attr("readonly", true); 
		}
	}