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
	
	//getLocationDetail('','Xlocation');
});

function validateFormFields(){
	try{
		var start_year = $("#start_year option:selected").text();
		var end_year = $("#end_year option:selected").text();
		if($('#Xlocation').val().trim()==''){
			$('#Xlocation').focus();
			showerr($("#Xlocation")[0], "Location is required!","block");			
			return false;
		}
		if($('#Xddo').val().trim()==''){
			$('#Xddo').focus();
			showerr($("#Xddo")[0], "Ddo is required!","block");			
			return false;
		}if($('#start_year').val()==''){
			$('#start_year').focus();
			showerr($("#start_year")[0], "Start Year  is required!","block");			
			return false;
		}if($('#end_year').val()==''){
			$('#end_year').focus();
			showerr($("#end_year")[0], "End Year  is required!","block");			
			return false;
		} if(start_year>end_year){
		$('#end_year').focus();
		showerr($("#end_year")[0], "End year can not less than start year!","block");			
		return false;
		}/*else if($('#fin_yr').val().trim()==''){
			$('#fin_yr').focus();
			showerr($("#fin_yr")[0], "Financial Year is required!","block");			
			return false;
		}*/else if($('#status').val().trim()==''){
			$('#status').focus();
			showerr($("#status")[0], "Status is required!","block");			
			return false;
		}else if($('#stu_name').val().trim()==''){
			$('#stu_name').focus();
			showerr($("#stu_name")[0], "Student Name is required!","block");			
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
		
		 var  sub_thrust_area = [];
		  	$(".checkBoxClass:checked").each(function() {
		  		sub_thrust_area.push(this.value);
		  	});
		  	var subThrustArea = sub_thrust_area.toString();
		  	/*if(subThrustArea==""||subThrustArea==null)
		  	{
		  	  $(".checkBoxClass").focus();
		  	  showerr($(".checkBoxClass")[0],"atleast 1 book should be checked","block");
		  	  return false;
		  	}*/
			var form_data = new FormData();
			var workarray = [];
			
				if($("#XUPLDOC").val()!="" && $("#XUPLDOC").val()!=undefined){
					var ext = $("#XUPLDOC").val().split('.').pop().toLowerCase();
					
					if(ext !=""){
						if($.inArray(ext, ['pdf']) == -1) {						
							$('#XUPLDOC').focus();
							alert("Note: Only .pdf files will be allowed for Abstract/Achievements!");
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
				 LOCATION_CODE 	  		: $('#Xlocation').val(),
				 DDO_ID 	  	  		: $('#Xddo').val(),
				 start_year 	  	  		: $('#start_year').val(),
				 end_year 	  	  		: $('#end_year').val(),
				 stu_name 		  		: $('#stu_name').val(),
				 ICAR_USID 		  		: $('#ICAR_USID').val(),
				 cau_regno		  		: $('#cau_regno').val(),
				 discipline	  	  		: $('#discipline').val(),
				 course	  	  			: $('#course').val(),
				 research_thrust_area 	: $('#research_thrust_area').val(),
				 research_sub_thrust_area  : subThrustArea,
				 proj_type 	  			: $('#proj_type').val(),
				 proj_title 		  	: $('#proj_title').val(),
				 objective 	  	  		: $('#objective').val(),
				 guide_name    	  		: $('#guide_name').val(),
				 external_guide_name    : $('#external_guide_name').val(),
				 proj_id			  	: $('#m_id').val(),
				// fin_yr					: $('#fin_yr').val(),
				 stu_status				: $('#status').val(),
				 m_id			  		: $('#m_id').val(),
			};			
			//console.log("send jsonObject------ "+JSON.stringify(jsonObject));
			var encData=encAESData($("#AESKey").val(), jsonObject);
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../StudentResearchProjectFormService");			    
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
								 document.location.href = 'student_research_project_form_e.jsp';
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
			xmlHttp.open("POST", "../StudentResearchProjectFormService", true);			    
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
		document.mouMoaFormL.action = "student_research_project_form_e.jsp?fstatus=E&m_id="+m_id+"&x_location="+x_location;
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
		document.moufrmModal.action="student_research_project_form_e.jsp";
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
				document.moufrmModal.action="student_research_project_form_l.jsp" ;
						/*"?x_location="
													+x_location+"&x_mou_type="+x_mou_type;*/
				document.moufrmModal.submit();
			}
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	function checkSavedSubThrustArea(){
		if($("#chkAllSavedSubThrustArea").is(':checked')){
			$(".checkBoxClass").prop('checked', true);	
			 
		}else{
			$(".checkBoxClass").prop('checked', false);
			     
		} 
	}
	