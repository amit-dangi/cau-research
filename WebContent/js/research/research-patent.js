$(document).ready(function(){
	$("#btnReset").click(function(){
		location.reload();
	});
	$("#btnReset1").click(function(){
		location.reload();
	});
	$("#xbtnReset").click(function(){
		document.getElementById("btmfrmPatentD").reset();
	});
	   
	$("#btnSearch").click(function(){
		document.btmfrmPatentD.target="btmfrmPatentE";
		document.btmfrmPatentD.action="research_patent_mast_l.jsp";
		document.btmfrmPatentD.submit();
		//document.btmfrmPatentD.reset();
	});
	/*$('#btnBack').click(function(){		  
		 document.location.href="research_patent_mast_e.jsp";
	 });*/
	
	
	 $('#finCommitUni').click(function(){
		 if ($('#finCommitUni').prop("checked")){
			 $("#det2").show();
		 }else{
			 $("#det2").hide();
		 }
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
	$("#rsrchcat").change(function(){	
		var rsrchcat = $('#rsrchcat').val();
		getRsrchSubCategory(rsrchcat,'','rsrchsubcat');
	});
	$("#savedmanualproj").change(function(){	
		var savedmanualproj = $('#savedmanualproj').val();
		$('#projPropsalIdManual').val(savedmanualproj);
		$('#savedmanualproj').val('');
	});
	
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
	getLocationDetail('','location'); 
});
function validateFormFields(){
	try{
	
		if($('#pat_cat').val().trim()==''){
			$('#pat_cat').focus();
			showerr($("#pat_cat")[0], "Category is required!","block");			
			return false;
		}else if($('#pat_type').val().trim()==''){
			$('#pat_type').focus();
			showerr($("#pat_type")[0], "Patent Type is required!","block");			
			return false;
		}else if($('#pat_status').val().trim()==''||$('#pat_status').val().trim()==null ){
			$('#pat_status').focus();
			showerr($("#pat_status")[0], "Patent Status is required!","block");			
			return false;
		}else if($('#Xlocation').val().trim()==''){
			$('#Xlocation').focus();
			showerr($("#Xlocation")[0], "Location is required!","block");			
			return false;
		}else if($('#Xddo').val().trim()==''){
			$('#Xddo').focus();
			showerr($("#Xddo")[0], "DDO is required!","block");			
			return false;
		}else if($('#app_name').val().trim()==''||$('#app_name').val().trim()==undefined){
			$('#app_name').focus();
			showerr($("#app_name")[0], "Applicant name is required!","block");			
			return false;
		}else if($('#pat_title').val().trim()==''){
			$('#pat_title').focus();
			showerr($("#pat_title")[0], "Patent Title is required!","block");			
			return false;
		}else if($('#pat_app_num').val().trim()==''){
			$('#pat_app_num').focus();
			showerr($("#pat_app_num")[0], "Patent Application Number is required!","block");			
			return false;
		}else if($('#filing_date').val().trim()==''){
			$('#filing_date').focus();
			showerr($("#filing_date")[0], "Date Filing date is required!","block");			
			return false;
		}else if($('#rsrchcat').val().trim()=='' ){
			$('#rsrchcat').focus();
			showerr($("#rsrchcat")[0], "Category is required!","block");			
			return false;
		}else if($('#rsrchsubcat').val().trim()=='' && $("#rsrchsubcat option:selected").text().trim()!= "NA" ){
			$('#rsrchsubcat').focus();
			showerr($("#rsrchsubcat")[0], "Sub Category is required!","block");			
			return false;
		}else if($('#pat_publ_grntd_date').val()==''){
			$('#pat_publ_grntd_date').focus();
			showerr($("#pat_publ_grntd_date")[0], "Patent published/ granted date is required!","block");			
			return false;
		}else if($('#fstatus').val().trim()=='N'&&$('#XUPLDOC').val().trim()==''){
				$('#XUPLDOC').focus();
				showerr($("#XUPLDOC")[0], "Upload Certificate :(only for granted patent) is required!","block");			
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
						if($.inArray(ext, ['pdf','png','jpg','jpeg','doc','docx']) == -1) {						
							$('#XUPLDOC').focus();
							alert("Note: Only .pdf, .jpg, .png & doc files will be allowed for uploading.!");
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
				
				var  PiName = [];
			  	$(".checkBoxClass:checked").each(function() {
			  		PiName.push(this.value);
			  	});
			  	if((PiName==""||PiName==null)  && PiName.length==0)
			  	{
			  	  displaySuccessMessages("errMsg2", "atleast 1 Inventor Name should be checked", "");
			  	  clearSuccessMessageAfterFiveSecond("errMsg2");
			  	  return false;
			  	}	
			var jsonObject={
					 pat_cat  	 	  : $('#pat_cat').val(),
					 pat_type  	 	  : $('#pat_type').val(),		
					 pat_status 	  : $('#pat_status').val(),
					 location 		  : $('#Xlocation').val(),			
					 ddo 			  : $('#Xddo').val(),
					 app_name 		  : $('#app_name').val(),
					 pat_title		  : $('#pat_title').val(),
					 pat_app_num	  : $('#pat_app_num').val(),
					 filing_date 	  : $('#filing_date').val(),
					 resh_category    : $('#rsrchcat').val(),
					 sub_category 	  : $('#rsrchsubcat').val(),
					 url 			  : $('#url').val(),
					 pat_awd_by 	  : $('#pat_awrd_by').val(),
					 pat_grnt_date    : $('#pat_publ_grntd_date').val(),
					 pat_publ_grnt_num: $('#pat_publn_grntd_num').val(),
					 ass_name		  : $('#ass_name').val(),
					 id				  : $('#mid').val(),
					 fin_yr			  : $('#fin_yr').val(),
					 PiName			  : PiName.toString(),	
					 deptId			  : $('#deptId').val(), 	
			};			
			//console.log("save jsonObject------ "+JSON.stringify(jsonObject));
			var encData=encAESData($("#AESKey").val(), jsonObject);
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../ResearchPatentService");			    
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
								 document.location.href = 'research_patent_mast_e.jsp';
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
			xmlHttp.open("POST", "../ResearchPatentService", true);			    
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
function editRecord(id,Xpat_type,Xpat_status,location,ddo,fstatus){
	try {
		$('#researchPatentL').attr('action', 'research_patent_mast_e.jsp?fstatus=E&id='+id+'&Xpat_type='+Xpat_type+'&Xpat_status='+Xpat_status+'&location='+location+'&ddo='+ddo);
		$("#researchPatentL").attr("target", '_parent');
		$("#researchPatentL" ).submit();
	} catch (err) {
		alert("ERROR :"+err);
	}
}


function geteditDD(status, category,sub_category,locationCode,ddoCode){
	try {
		//alert(status);
		if(status=="E" || status=="N"){
			getDdoDetailbyLocation(locationCode,ddoCode);
			getLocationDetail(locationCode); 
			//getRsrchSubCategory('rsrchsubcat',category,sub_category);
		}else{
			getLocationDetail(locationCode,'location'); 
			getDdoDetailbyLocation(locationCode,ddoCode,'ddo');
		}
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}
	//On the click of back button from the edit page , searched value will appned to the e page 
	function vldBack(Xpat_type,Xpat_status,location,ddo) {
		try {
		document.btmfrmPatentD.action="research_patent_mast_e.jsp?Xpat_type="
													+Xpat_type+"&Xpat_status="+Xpat_status+"&location="+location+"&ddo="+ddo;
		document.btmfrmPatentD.submit();
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	function getSearchList(status,Xpat_type,Xpat_status,location,ddo) {
		try {
			//alert("in||"+Xpat_type+"||"+Xpat_status+"||"+location+"||"+ddo);
			if(status!="E" ){
				document.btmfrmPatentD.target="btmfrmPatentE";
				document.btmfrmPatentD.action="research_patent_mast_l.jsp?Xpat_type="
													+Xpat_type+"&Xpat_status="+Xpat_status+"&location="+location+"&ddo="+ddo;
				document.btmfrmPatentD.submit();
			}
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	
	
	function checkAll(){
		if($("#chkAllSavedemp").is(':checked')){
			$(".checkBoxClass").prop('checked', true);	
		}else{
			$(".checkBoxClass").prop('checked', false);
			     
		} 
	}
	
	function getDptByEmployee(emp,user_status,dept) {
		if(user_status=="A"){
			getDptByEmp('',dept,"deptId");
		}else{
			getDptByEmp(emp,dept,"deptId");
		}
	}	
