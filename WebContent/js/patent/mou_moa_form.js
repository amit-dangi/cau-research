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
		}else if($('#mou_type').val().trim()==''){
			$('#mou_type').focus();
			showerr($("#mou_type")[0], "Type of MOU/MOA is required!","block");			
			return false;
		}else if($('#inst_name').val().trim()==''){
			$('#inst_name').focus();
			showerr($("#inst_name")[0], "Name of Institute/University/Company/others is required!","block");			
			return false;
		}else if(!$('.coll_type [type="checkbox"]').is(':checked')){
			showerr($(".coll_type")[0], "Please Check atleast one CheckBox!", "block");
			return false;
		}else if($('#coll_type').val()==''){
			$('#coll_type').focus();
			showerr($("#coll_type")[0], "Type of collaboration is required!","block");			
			return false;
		}else if($('#coll_area').val().trim()==''){
			$('#coll_area').focus();
			showerr($("#coll_area")[0], "Area of Collaboration is required!","block");			
			return false;
		}else if($('#signed_on').val().trim()==''){
			$('#signed_on').focus();
			showerr($("#signed_on")[0], "Signed on date is required!","block");			
			return false;
		}else if($('#validity_period').val().trim()=='' ){
			$('#validity_period').focus();
			showerr($("#validity_period")[0], "Validity period required!","block");			
			return false;
		}else if($('#valid_upto').val().trim()=='' ){
			$('#valid_upto').focus();
			showerr($("#valid_upto")[0], "Valid Upto is required!","block");			
			return false;
		}else if($('#signed_by').val()==''){
			$('#signed_by').focus();
			showerr($("#signed_by")[0], "Signed by is required!","block");			
			return false;
		}/*else if($('#pi_name').val()==''){
			$('#pi_name').focus();
			showerr($("#pi_name")[0], "PI Name is required!","block");			
			return false;
		}*/else if($('#fstatus').val().trim()=='N' && $('#XUPLDOC').val().trim()==''){
				$('#XUPLDOC').focus();
				showerr($("#XUPLDOC")[0], "Upload Signed MOU/MOA (PDF)  is required!","block");			
				return false;	
		}else if($('#m_status').val()==''){
				$('#m_status').focus();
				showerr($("#m_status")[0], "Status is required!","block");			
				return false;
		}
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function getvalidityupto(validity_period) {
	 let signed_on=$('#signed_on').val();
	 if(validity_period.includes("-") || validity_period.includes(".")){
	 showerr($("#validity_period")[0], "Invalid Validity year Entered !","block");
		$('#validity_period').val("0");
		$('#valid_upto').val("");
		$('#validity_period').focus();
		return false;
	 }else{
	 let signed_on_year = signed_on.substring(6,10);
	 let total_yr=parseInt(validity_period)+parseInt(signed_on_year);
	 let final_valid_upto= signed_on.substring(0,2)+'/'+signed_on.substring(3,5) +'/'+total_yr;
	/* alert("final_valid_upto date ||"+final_valid_upto);*/
	 $('#valid_upto').val(final_valid_upto);
	 }
}

function save(){
	if(validateFormFields()){
	try {
			var form_data = new FormData();
			var workarray = [];
			 var sel_sessions = [];
			   var str_sel_sess="";
			   sel_sessions = [];
			   	$.each($("input[name='boxAlign1']:checked"), function(){
			   		sel_sessions.push($(this).val());
			     });
			   	str_sel_sess = sel_sessions.toString();
				if($("#XUPLDOC").val()!="" && $("#XUPLDOC").val()!=undefined){
					var ext = $("#XUPLDOC").val().split('.').pop().toLowerCase();
					
					if(ext !=""){
						if($.inArray(ext, ['pdf']) == -1) {						
							$('#XUPLDOC').focus();
							alert("Note: Only .pdf files will be allowed for upload signed MOU/MOA .!");
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
				
			
				
			var jsonObject={
				coll_type         : str_sel_sess,
				 LOCATION_CODE 	  : $('#Xlocation').val(),
				 DDO_ID 	  : $('#Xddo').val(),
				 mou_type 		  : $('#mou_type').val(),
				 inst_name 		  : $('#inst_name').val(),
				// coll_type		  : $('#coll_type').val(),
				 coll_area	  	  : $('#coll_area').val(),
				 signed_on 	      : $('#signed_on').val(),
				 validity_period  : $('#validity_period').val(),
				 valid_upto 	  : $('#valid_upto').val(),
				 signed_by 		  : $('#signed_by').val(),
				 pi_name 	  	  : $('#pi_name').val(),
				 m_status    	  : $('#m_status').val(),
				 m_id			  : $('#m_id').val(),
			};	
			//alert("json---"+JSON.stringify(jsonObject));
			console.log("send jsonObject------ "+JSON.stringify(jsonObject));
			var encData=encAESData($("#AESKey").val(), jsonObject);
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../MouMoaformService");			    
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
								 document.location.href = 'mou_moa_form_e.jsp';
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
			xmlHttp.open("POST", "../MouMoaformService", true);			    
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

function editRecord(m_id,fstatus,x_location,x_mou_type){
	try {
		document.mouMoaFormL.target = "_parent";
		document.mouMoaFormL.action = "mou_moa_form_e.jsp?fstatus=E&m_id="+m_id+"&x_location="+x_location+
								"&x_mou_type="+x_mou_type;
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
		document.moufrmModal.action="mou_moa_form_e.jsp";
		document.moufrmModal.submit();
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	function getSearchList(status,x_location,x_mou_type) {
		try {
			//alert("x_location||"+x_location);
			if(status!="E" ){
				document.moufrmModal.target="mouMoaFormLFrame";
				document.moufrmModal.action="mou_moa_form_l.jsp" ;
						/*"?x_location="
													+x_location+"&x_mou_type="+x_mou_type;*/
				document.moufrmModal.submit();
			}
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}