$(document).ready(function(){
	$("#btnReset").click(function(){
		location.reload();
	});
	
	$("#btnReset1").click(function(){
		document.getElementById("frmFundingAgencyD").reset();
	});

	 $('#btnBack').click(function(){		  
		 document.location.href="funding_agency_e.jsp?fstatus=N";
	 });
	
	$("#btnSearch").click(function(){
		var name=$("#faNameS").val();
		var type=$("#faTypeS").val();
		var mob=$("#faMobNoS").val();	
		
		$('#frmFundingAgencyE').attr('target', 'btmfrmFundingAgencyD');
		$('#frmFundingAgencyE').attr('action', 'funding_agency_l.jsp?fstatus=V&faNameS='+name+'&faTypeS='+type+'&faMobNoS='+mob);
		$("#frmFundingAgencyE" ).submit();		
	});	
	
	$('#btnSave').click(function(){
		if(validateFormFields()){
			var Name = $("#faName").val();
			var type = $('input[name="faType"]:checked').val();
			var mob = $("#faMobNo").val();
			var url = $("#faUrl").val();
			var addr = $("#faAddr").val();
			var detail = $("#faDetail").val();
			var fundedby = $("#fundedby").val();
			
			try {
				var jsonObject={"faName":Name, "faType":type, "faMobNo":mob, "faUrl":url, "faAddr":addr, "faDetail":detail, "fundedby":fundedby};
				var encData=encAESData($("#AESKey").val(), jsonObject);	
				$('#btnSave').hide();
				$('#btnReset').hide();
				$.ajax({
					type: "POST",
					url: "../FundingAgencyService",
					//data: {jsonObject: JSON.stringify(jsonObject)},
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
			var Name = $("#faName").val();
			var type = $('input[name="faType"]:checked').val();
			var mob = $("#faMobNo").val();
			var url = $("#faUrl").val();
			var addr = $("#faAddr").val();
			var detail = $("#faDetail").val();
			var id = $("#id").val();
			
			try {
				var jsonObject={"faName":Name, "faType":type, "faMobNo":mob, "faUrl":url, "faAddr":addr, "faDetail":detail, "faId":id};
				var encData=encAESData($("#AESKey").val(), jsonObject);
				$('#btnUpdate').hide();
				$('#btnReset').hide();
				$.ajax({
					type: "POST",
					url: "../FundingAgencyService",
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
								document.location.href="funding_agency_e.jsp?fstatus=N";
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
		if($('#faName').val()==''){
			$('#faName').focus();
			showerr($("#faName")[0], "Funding Agency Name is required!","block");			
			return false;
		}else if($('#faMobNo').val()==''){
			$('#faMobNo').focus();
			showerr($("#faMobNo")[0], "Contact No. is required!","block");			
			return false;
		}/*else if($('#fundedby').val() == ''){
			$('#fundedby').focus();
			showerr($("#fundedby")[0], "Funded by is required!","block");			
			return false;
		}*/else if($('#faMobNo').val()!=''){
			if($('#faMobNo').val().length < 10){
				$('#faMobNo').focus();	
				showerr($("#faMobNo")[0], "Please Enter Valid Contact No.!","block");
				return false;	
			}
		}
		
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function deleteRecord(Id){	
	try {	
		$("#XfaId").val(Id);
		var r = confirm("Are You Sure!");
		if (r == true){
			//var jsonObject={"faId":Id, "fstatus":"D"};
			$.ajax({
				type: "POST",
				url: "../FundingAgencyService",
				data: {"faId":Id, "fstatus":"D"},
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

function editRecord(id, name, type, mob, det, addr, url, fstatus,fundedby){
	try {
		$("#XfaId").val(id);	
		$("#XfaName").val(name);	
		$("#XfaType").val(type);
		$("#XfaMobNo").val(mob);
		$("#XfaUrl").val(url);
		$("#XfaAddr").val(addr);		
		$("#XfaDetail").val(det);	
		$("#Xfundedby").val(fundedby);
	
		$('#frmFundingAgencyL').attr('action', 'funding_agency_e.jsp?fstatus=E');
		$("#frmFundingAgencyL").attr("target", '_parent');
		$("#frmFundingAgencyL" ).submit();		
	} catch (err) {
		alert(error);
	}
}

function validURL(sText) {
	if(sText.value!="") {
		/*if(!isEmail(sText.value)) {
			showerr(sText,"Website URL should be proper.","block");  
			email.focus();
			return false;
		}*/
		//var pattern = "/^([a-z]([a-z]|\d|\+|-|\.)*):(\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?((\[(|(v[\da-f]{1,}\.(([a-z]|\d|-|\.|_|~)|[!\$&'\(\)\*\+,;=]|:)+))\])|((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=])*)(:\d*)?)(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*|(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)|((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)|((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)){0})(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i";
		//var pattern = "/^(http|https)?:\/\/[a-zA-Z0-9-\.]+\.[a-z]{2,4}/";
		
		/*if(!/\/[a-zA-Z0-9-\.]+\.[a-z]{2,4}/.test(sText.value)) {
			alert("qqqqqq")
			showerr(sText,"Website URL should be proper.","block");
			return false;
		}*/

	}
}