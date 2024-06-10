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
		document.frmCommlicensingD.target="frmCommlicensingE";
		document.frmCommlicensingD.action="commercialization_licensing_l.jsp";
		document.frmCommlicensingD.submit();
		//document.btmfrmPatentD.reset();
	});
	/*$('#btnBack').click(function(){		  
		 document.location.href="commercialization_licensing_e.jsp";
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
	
		if($('#type').val().trim()==''){
			$('#type').focus();
			showerr($("#type")[0], "Type is required!","block");			
			return false;
		}else if($('#Xlocation').val().trim()==''){
			$('#Xlocation').focus();
			showerr($("#Xlocation")[0], "Location is required!","block");			
			return false;
		}else if($('#Xddo').val().trim()==''){
			$('#Xddo').focus();
			showerr($("#Xddo")[0], "DDO is required!","block");			
			return false;
		}else if($('#app_no').val().trim()==''||$('#app_no').val().trim()==undefined){
			$('#app_no').focus();
			showerr($("#app_no")[0], "Application no is required!","block");			
			return false;
		}else if($('#title').val().trim()==''){
			$('#title').focus();
			showerr($("#title")[0], "Title is required!","block");			
			return false;
		}else if($('#comm_no').val().trim()=='' ){
			$('#comm_no').focus();
			showerr($("#comm_no")[0], "Commercialization no is required!","block");			
			return false;
		}else if($('#rsrchcat').val().trim()=='' ){
			$('#rsrchcat').focus();
			showerr($("#rsrchcat")[0], "Category is required!","block");			
			return false;
		}else if($('#rsrchsubcat').val().trim()=='' && $("#rsrchsubcat option:selected").text().trim()!= "NA" ){
			$('#rsrchsubcat').focus();
			showerr($("#rsrchsubcat")[0], "Sub Category is required!","block");			
			return false;
		}else if($('#date').val().trim()==''){
			$('#date').focus();
			showerr($("#date")[0], "Date Filing date is required!","block");			
			return false;
		}else if($('#end_date').val()==''){
			$('#end_date').focus();
			showerr($("#end_date")[0], "Patent published/ granted date is required!","block");			
			return false;
		}else if($('#fstatus').val().trim()=='N'&&$('#XUPLDOC').val().trim()==''){
				$('#XUPLDOC').focus();
				showerr($("#XUPLDOC")[0], "Upload MOU is required!","block");			
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
							alert("Note: Only .pdf files will be allowed for uploading.!");
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
					 typ  	 	  	  : $('#type').val(),		
					 location 		  : $('#Xlocation').val(),			
					 ddo 			  : $('#Xddo').val(),
					 app_no 		  : $('#app_no').val(),
					 title		  	  : $('#title').val(),
					 cau_comm_num	  : $('#cau_comm_num').val(),
					 comm_no	  	  : $('#comm_no').val(),
					 comm_type  	  : $('#comm_type').val(),
					 resh_category    : $('#rsrchcat').val(),
					 sub_category 	  : $('#rsrchsubcat').val(),
					 sec_party 	  	  : $('#sec_party').val(),
					 date 	  		  : $('#date').val(),
					 end_date    	  : $('#end_date').val(),
					 duration 		  : $('#duration').val(),
					 amt_of_comm 	  : $('#amt_of_comm').val(),
					 id				  : $('#mid').val(),
					 upl_id			: $('#upl_id').val(),
			};			
			//alert("check------ "+JSON.stringify(jsonObject));
			var encData=encAESData($("#AESKey").val(), jsonObject);
			//var encData=encAESData($("#AESKey").val(), jsonObject);
			//alert("check---encData--- "+encData);
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../CommercializationLicensingService");			    
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
								 document.location.href = 'commercialization_licensing_e.jsp';
							}, 2000);
						}else{
							displaySuccessMessages("errMsg2", result.errMsg, "");
							clearSuccessMessageAfterFiveSecond("errMsg2");
							 setTimeout(function () {
		    					 //location.reload();
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
			xmlHttp.open("POST", "../CommercializationLicensingService", true);			    
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
function editRecord(id,Xtype,location,ddo,fstatus){
	try {
		$('#frmCommlicensingL').attr('action', 'commercialization_licensing_e.jsp?fstatus=E&id='+id+'&Xtype='+Xtype+'&location='+location+'&ddo='+ddo);
		$("#frmCommlicensingL").attr("target", '_parent');
		$("#frmCommlicensingL" ).submit();
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
	function vldBack(Xtype,Xstatus,location,ddo) {
		try {
		document.frmCommlicensingD.action="commercialization_licensing_e.jsp?Xtype="
													+Xtype+"&Xstatus="+Xstatus+"&location="+location+"&ddo="+ddo;
		document.frmCommlicensingD.submit();
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	function getSearchList(status,Xtype,Xstatus,location,ddo) {
		try {
			//alert("in||"+Xtype+"||"+Xstatus+"||"+location+"||"+ddo);
			if(status!="E" ){
				document.frmCommlicensingD.target="frmCommlicensingE";
				document.frmCommlicensingD.action="commercialization_licensing_l.jsp?Xtype="
													+Xtype+"&Xstatus="+Xstatus+"&location="+location+"&ddo="+ddo;
				document.frmCommlicensingD.submit();
			}
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	
	    	
	function CalculateDays(){
        var days=$("#date").val();
		var fdDate=$("#end_date").val();
		var xmlHttp = new XMLHttpRequest();
		xmlHttp.open("POST", "../CommercializationLicensingService", true);	
		  xmlHttp.setRequestHeader("day",days);
		  xmlHttp.setRequestHeader("end_date",fdDate);
		  xmlHttp.setRequestHeader("fstatus","Day");
		  xmlHttp.send();
		  xmlHttp.onreadystatechange = function() {
	    		//alert(xmlHttp.readyState+"|"+xmlHttp.status);
	    		if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
					var data=JSON.parse(this.responseText);
				  $('#duration').val(data.days);
			  }
		  }
	}