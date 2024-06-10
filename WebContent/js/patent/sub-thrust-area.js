$(document).ready(function(){
		$("#btnReset").click(function(){
		location.reload();
	});
	
	$("#btnReset1").click(function(){
 		document.getElementById("frmSubThrustAreaE").reset();
	});
	
	$("#btnSearch").click(function(){
		var sub_thrust_area=$("#Xsub_thrust_area").val();
		var thrust_area=$("#Xthrust_area").val();
		$("#frmSubThrustAreaE").attr("target", "frmSubThrustAreaD");
		$("#frmSubThrustAreaE").attr("action", 'sub_thrust_area_l.jsp?fstatus=V&sub_thrust_area='+sub_thrust_area+'&thrust_area='+thrust_area);
		$("#frmSubThrustAreaE" ).submit();
	});	
	
	$('#btnBack').click(function(){		  
		document.location.href="sub_thrust_area_e.jsp?fstatus=N";
	});
	
	$('#btnSave').click(function(){
		if(validateFormFields()){
			var thrust_area = $("#thrust_area").val();
			var sub_thrust_area = $("#sub_thrust_area").val();
			
			
			try {
				var jsonObject={"sub_thrust_area":sub_thrust_area, "thrust_area":thrust_area,"id":$("#id").val()};
				var encData=encAESData($("#AESKey").val(), jsonObject);				
				$.ajax({
					type: "POST",
					url: "../SubThrustAreaService",
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
							document.location.href="sub_thrust_area_e.jsp";
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
			var thrust_area = $("#thrust_area").val();
			var sub_thrust_area = $("#sub_thrust_area").val();
			
			
			try {
				var jsonObject={"sub_thrust_area":sub_thrust_area, "thrust_area":thrust_area,"id": $("#id").val()};
				var encData=encAESData($("#AESKey").val(), jsonObject);
				$.ajax({
					type: "POST",
					url: "../SubThrustAreaService",
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
								document.location.href="sub_thrust_area_e.jsp?fstatus=N";
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
		 if($('#thrust_area').val()==''&& $('#thrust_area').val()==null ){
			$('#thrust_area').focus();
			showerr($("#thrust_area")[0], "Thrust Area is required!","block");			
			return false;
		
	} else if ( $("#sub_thrust_area").val()== '' && $('#sub_thrust_area').val()==null ){
		$('#sub_thrust_area').focus();
		showerr($("#sub_thrust_area")[0], "Sub Thrust Area is required!","block");			
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
				url: "../SubThrustAreaService",
				data: {"id":Id, "fstatus":"D"},
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

function editRecord(id,thrust_area,sub_thrust_area){
	try {		
		
		$("#id").val(id);	
		$("#sub_thrust_area").val(sub_thrust_area);
		$("#thrust_area").val(thrust_area);
		
		$('#frmSubThrustAreaL').attr('action', 'sub_thrust_area_e.jsp?fstatus=E');
		$("#frmSubThrustAreaL").attr("target", '_parent');
		$("#frmSubThrustAreaL" ).submit();		
	} catch (err) {
		alert(error);
	}
}
