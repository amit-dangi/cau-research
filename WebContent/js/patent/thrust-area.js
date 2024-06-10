$(document).ready(function(){
	$("#btnReset").click(function(){
		location.reload();
	});
	
	$("#btnReset1").click(function(){
		document.getElementById("frmResHeadD").reset();
	});
	
	$("#btnSearch").click(function(){
		var thrust_area=$("#thrust_areaS").val();
		
		$("#frmThrustAreaE").attr("target", "frmThrustAreaD");
		$("#frmThrustAreaE").attr("action", 'thrust_area_l.jsp?fstatus=V&thrust_area='+thrust_area);
		$("#frmThrustAreaE" ).submit();
	});	
	
	$('#btnBack').click(function(){		  
		document.location.href="thrust_area_e.jsp?fstatus=N";
	});
	
	$('#btnSave').click(function(){
		if(validateFormFields()){
			var name = $("#thrust_area").val();
			var status = "";
			
			
			try {
				var jsonObject={"thrust_area":name,"thrustId":$("#id").val()};
				var encData=encAESData($("#AESKey").val(), jsonObject);				
				$.ajax({
					type: "POST",
					url: "../ThrustAreaService",
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
			var name = $("#thrust_area").val();
			
			
			try {
				var jsonObject={"thrust_area":name,"thrustId":$("#id").val()};
				var encData=encAESData($("#AESKey").val(), jsonObject);
				$.ajax({
					type: "POST",
					url: "../ThrustAreaService",
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
								document.location.href="thrust_area_e.jsp?fstatus=N";
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
		if($('#thrust_area').val()==''){
			$('#thrust_area').focus();
			showerr($("#thrust_area")[0], "Thrust Area is required!","block");			
			return false;
		}
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function deleteRecord(Id){	
	try {	
		$("#id").val(Id);
		var r = confirm("Are You Sure!");
		if (r == true){
			$.ajax({
				type: "POST",
				url: "../ThrustAreaService",
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
		$("#id").val(id);	
		$("#thrust_area").val(name);	
	
		$('#frmThrustAreaL').attr('action', 'thrust_area_e.jsp?fstatus=E');
		$("#frmThrustAreaL").attr("target", '_parent');
		$("#frmThrustAreaL" ).submit();		
	} catch (err) {
		alert(error);
	}
}