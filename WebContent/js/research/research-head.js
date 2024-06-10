$(document).ready(function(){
	$("#btnReset").click(function(){
		location.reload();
	});
	
	$("#btnReset1").click(function(){
		document.getElementById("frmResHeadD").reset();
	});
	
	$("#btnSearch").click(function(){
		var headName=$("#headNameS").val();
		var status=$("#statusS").val();
		
		$("#frmResHeadE").attr("target", "btmfrmResHeadD");
		$("#frmResHeadE").attr("action", 'research_head_l.jsp?fstatus=V&headName='+headName+'&status='+status);
		$("#frmResHeadE" ).submit();
	});	
	
	$('#btnBack').click(function(){		  
		document.location.href="research_head_e.jsp?fstatus=N";
	});
	
	$('#btnSave').click(function(){
		if(validateFormFields()){
			var name = $("#headName").val();
			var status = "";
			
			if($("#active").prop("checked") == true){ 
				status = "Y";
			}else { 
				status = "N"; 
			}
			
			try {
				var jsonObject={"headName":name, "headType":status};
				var encData=encAESData($("#AESKey").val(), jsonObject);				
				$('#btnSave').hide();
				$('#btnReset').hide();
				$.ajax({
					type: "POST",
					url: "../ResearchHeadService",
					data: {encData: encData, fstatus: "N"},
					dataType: "json",
					success: function (response) {
						var data= decAESData($("#AESKey").val(), response);
						$('#btnSave').show();
						$('#btnReset').show();
						
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
								location.reload();
							}, 5000);
						}
					},
					error: function(xhr, status, error) {
						alert(xhr.responseText);
						alert(status);
						alert("error :"+error);
						
						$('#btnSave').show();
						$('#btnReset').show();
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
			var status = "", id = $("#id").val();
			
			if($("#active").prop("checked") == true){ 
				status = "Y";
			}else { 
				status = "N"; 
			}
			
			try {
				var jsonObject={"headName":name, "headType":status, "headId":id};
				var encData=encAESData($("#AESKey").val(), jsonObject);
				$('#btnUpdate').hide();
				$('#btnReset').hide();
				$.ajax({
					type: "POST",
					url: "../ResearchHeadService",
					//data: {jsonObject: JSON.stringify(jsonObject)},
					data: {encData: encData, fstatus: "E"},
					dataType: "json",
					success: function (response) {
						var data= decAESData($("#AESKey").val(), response);
						$('#btnUpdate').show();
						$('#btnReset').show();
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
								document.location.href="research_head_e.jsp?fstatus=N";
							}, 5000);
						}
					},
					error: function(xhr, status, error) {
						alert(xhr.responseText);
						alert(status);
						alert("error :"+error);
						
						$('#btnUpdate').show();
						$('#btnReset').show();
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
		if($('#headName').val()==''){
			$('#headName').focus();
			showerr($("#headName")[0], "Head Name is required!","block");			
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
				url: "../ResearchHeadService",
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

function editRecord(id, name, type){
	try {		
		$("#XhId").val(id);	
		$("#XhName").val(name);	
		$("#XhType").val(type);	
	
		$('#frmResHeadL').attr('action', 'research_head_e.jsp?fstatus=E');
		$("#frmResHeadL").attr("target", '_parent');
		$("#frmResHeadL" ).submit();		
	} catch (err) {
		alert(error);
	}
}