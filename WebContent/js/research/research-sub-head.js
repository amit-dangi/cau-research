$(document).ready(function(){
	
	 $(document).off('change', '#headName').on('change', '#headName',
			 function() {
				 var headName =	$("#headName :selected").text();
				 
				if(headName == "Travel"){
					    $("#LocName").show();
					    $("#optHide").show();
						$("#txtHide").hide();
						$("#SubHeadName" ).val( "" );
					
				 }else{
					    $("#LocName").hide();
					    $("#optHide").hide();
						$("#txtHide").show(); 
						$("#location").val( "" );
				 }
			});

	 $(document).off('change', '#headNameS').on('change', '#headNameS',
	 		 function() {
	 			 var headName =	$("#headNameS :selected").text();
	 			 
	 			if(headName == "Travel"){
	 				$("#SubHeadNameS" ).val( "" );
	 				$("#locType").show();
	 				$("#subHeadName").hide();
	 			 }else{
	 				$("#locType").hide();
	 				$("#subHeadName").show(); 
	 				$("#locationTyp").val( "" );
	 					
	 			 }
	 		});
	 
	$("#btnReset").click(function(){
		location.reload();
	});
	
	$("#btnReset1").click(function(){
 		document.getElementById("frmResSubHeadD").reset();
	});
	
	$("#btnSearch").click(function(){
		var SubHeadName=$("#SubHeadNameS").val();
		var headName=$("#headNameS").val();
		var status=$("#statusS").val();
		var locationTyp=$("#locationTyp").val();
		$("#frmResSubHeadE").attr("target", "btmfrmResSubHeadD");
		$("#frmResSubHeadE").attr("action", 'research_sub_head_l.jsp?fstatus=V&SubHeadName='+SubHeadName+'&headName='+headName+'&status='+status+'&locationTyp='+locationTyp);
		$("#frmResSubHeadE" ).submit();
	});	
	
	$('#btnBack').click(function(){		  
		document.location.href="research_sub_head_e.jsp?fstatus=N";
	});
	
	$('#btnSave').click(function(){
		if(validateFormFields()){
			var name = $("#headName").val();
			var sub_name = $("#SubHeadName").val();
			var location = $("#location").val();
			var locationName = $("#locationName").val();
			var status = "";
			
			if($("#active").prop("checked") == true){ 
				status = "Y";
			}else { 
				status = "N"; 
			}
			
			try {
				var jsonObject={"subHeadName":sub_name, "Location":location,"hId":name, "subHeadType":status,"locationName":locationName, "hName":$("#headName :selected").text()};
				var encData=encAESData($("#AESKey").val(), jsonObject);				
				$('#btnSave').hide();
				$('#btnReset').hide();
				$.ajax({
					type: "POST",
					url: "../ResearchSubHeadService",
					data: {encData: encData, fstatus: "N"},
					dataType: "json",
					success: function (response) {
						var data= decAESData($("#AESKey").val(), response);
					
						if(data.flag=="Y"){
							setTimeout(function() {
								displaySuccessMessages("errMsg1", data.status, "");							
							}, 3000);
							clearSuccessMessageAfterFiveSecond("errMsg1");
						}else{
							setTimeout(function() {
								displaySuccessMessages("errMsg2", data.status, "");							
							}, 3000);
							clearSuccessMessageAfterFiveSecond("errMsg2");							
						}
												
						if(data.flag=="Y"){
							setTimeout(function() {
							document.location.href="research_sub_head_e.jsp";
							}, 5000);
						}
					},
					error: function(xhr, status, error) {
						alert(xhr.responseText);
						alert(status);
						alert("error :"+error);
						
					}
				});	
			} catch (e) {
				// TODO: handle exception
				alert("ERROR :"+e);
			}
		}else{
			return false;
		}	
	});
	
	$('#btnUpdate').click(function(){
		if(validateFormFields()){
			var name = $("#headName").val();
			var sub_name = $("#SubHeadName").val();
			var location = $("#location").val();
			var locationName = $("#locationName").val();
			var status = "", id = $("#id").val();
			
			if($("#active").prop("checked") == true){ 
				status = "Y";
			}else { 
				status = "N"; a
			}
			
			try {
				var jsonObject={"subHeadName":sub_name, "hId":name, "subHeadType":status, "subHeadId":id,"Location":location,"locationName":locationName,"hName":$("#headName :selected").text()};
				var encData=encAESData($("#AESKey").val(), jsonObject);
				$('#btnUpdate').hide();
				$('#btnReset').hide();
				$.ajax({
					type: "POST",
					url: "../ResearchSubHeadService",
					//data: {jsonObject: JSON.stringify(jsonObject)},
					data: {encData: encData, fstatus: "E"},
					dataType: "json",
					success: function (response) {
						var data= decAESData($("#AESKey").val(), response);
					
						if(data.flag=="Y"){
							setTimeout(function() {
								displaySuccessMessages("errMsg1", data.status, "");							
							}, 3000);
							clearSuccessMessageAfterFiveSecond("errMsg1");
						}else{
							setTimeout(function() {
								displaySuccessMessages("errMsg2", data.status, "");							
							}, 3000);
							clearSuccessMessageAfterFiveSecond("errMsg2");							
						}
												
						if(data.flag=="Y"){
							setTimeout(function() {
								document.location.href="research_sub_head_e.jsp?fstatus=N";
							}, 5000);
						}
					},
					error: function(xhr, status, error) {
						alert(xhr.responseText);
						alert(status);
						alert("error :"+error);
					
					}
				});	
			} catch (e) {
				// TODO: handle exception
				alert("ERROR :"+e);
			}
		}else{
			return false;
		}	
	});
	
});

function validateFormFields(){
	try{
		 var headName =	$("#headName :selected").text();
		 if($('#location').val()=='' && $("#headName :selected").text() == 'Travel'){
			$('#location').focus();
			showerr($("#location")[0], "Location is required!","block");			
			return false;
		
			} else if($('#headName').val()==''){
			$('#headName').focus();
			showerr($("#headName")[0], "Head Name is required!","block");			
			return false;
		
	} else if ( $("#headName :selected").text() !== 'Travel' && $('#SubHeadName').val()=='' ){
		$('#SubHeadName').focus();
		showerr($("#SubHeadName")[0], "Sub Head Name is required!","block");			
		return false;
	}
		
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function deleteRecord(Id){	
	try {	
		$("#XhId").val(Id);
		var r = confirm("Are You Sure!");
		if (r == true){
			$.ajax({
				type: "POST",
				url: "../ResearchSubHeadService",
				data: {"headId":Id, "fstatus":"D"},
				dataType: "json",
				success: function (data) {
					var tRec = $("#totRow").val();
					for(var i=1;i<=tRec;i++){
						$("#EDIT_RECORD_"+i).wrap('<td style="display:none"/>');
						$("#DELETE_RECORD_"+i).wrap('<td style="display:none"/>');		            			
					}
					if(data.flag=="Y"){
						displaySuccessMessages("errMsg1", data.status, "");													
					}else{
						displaySuccessMessages("errMsg2", data.status, "");
					}
					setTimeout(function () {
						location.reload();
					}, 5000);
				},
				error: function (data) {
					alert("Error");
				}
			}); 			    			 
		}		
	} catch (err) {
		alert("ERROR :"+err);
	}
}

function editRecord(id,head_id,name,type,location,locationName,status){
	try {		
		
		$("#XhId").val(id);	
		$("#XhName").val(head_id);
		$("#XSubhName").val(name);
		$("#XhType").val(type);
		$("#Xlocation").val(location);
		$("#XlocationName").val(locationName);
		
		$('#frmResSubHeadL').attr('action', 'research_sub_head_e.jsp?fstatus=E');
		$("#frmResSubHeadL").attr("target", '_parent');
		$("#frmResSubHeadL" ).submit();		
	} catch (err) {
		alert(error);
	}
}

function hideShowLocation(hName){
	 var headName =	$("#headName :selected").text();
	 if(headName == "Travel"){
		    $("#optHide").show();
			$("#txtHide").hide();
		    $("#LocName").show();
	 }else{
		    $("#optHide").hide();
			$("#txtHide").show(); 
		    $("#LocName").hide();
	 }
}
