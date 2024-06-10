$(document).ready(function(){
	
	$('#checkAll').click(function() {
		if ($("#checkAll").is(':checked'))
			$(".checkBoxClass").prop('checked', true);
		else
			$(".checkBoxClass").prop('checked', false);
	});
	
	$('.checkBoxClass').change(function() {
		$("#checkAll").prop('checked', false);
	});
	
	$("#btnReset").click(function(){
		location.reload();
	});
	
	$("#btnView").click(function(){
		
		var typ=$("#typ").val();
		var XFROMDATES=$("#XFROMDATE").val();
		var XTODATES=$("#XTODATE").val();
		var statusS=$("#status").val();
		if($("#Xlocation").val().trim()==""){ 
			$("#Xlocation").focus();
			showerr($("#Xlocation")[0], "Location is required!","block");
			return false;
		}
		if($("#Xddo").val().trim()==""){ 
			$("#Xddo").focus();
			showerr($("#Xddo")[0], "DDO is required!","block");
			return false;
		}
		
		var xa= $("#XFROMDATE").val().substring(6,10)+$("#XFROMDATE").val().substring(3,5)+$("#XFROMDATE").val().substring(0,2);
		 var xe= $("#XTODATE").val().substring(6,10)+$("#XTODATE").val().substring(3,5)+$("#XTODATE").val().substring(0,2);
		 if(parseInt(xa)>parseInt(xe))
		  {	
	    	 showerr($("#msg-XTODATE")[0], "From Date should be greater than To Date!", "block");
		       return false;
		   } 
		
		$('#frmRsrchPropAppE').attr('target', 'btmfrmRsrchPropAppD');
		$('#frmRsrchPropAppE').attr('action', 'research_proposal_approval_l.jsp?fstatus=V&Xlocation='+$("#Xlocation").val()+'&Xddo='+$("#Xddo").val()+'&XFROMDATES='+XFROMDATES+'&XTODATES='+XTODATES+'&statusS='+statusS+"&pageType="+typ);
		$("#frmRsrchPropAppE" ).submit();
	});

	$("#btnSubmit").click(function(){
		try {
			var index=parseInt($("#totRow").val());
			var checkboxes = $('[name="chkBox"]:checked').length;
			var xjsonArray = [];
			if(checkboxes > 0){
				for(var j=1; j<=index; j++){
					if($("#chkBox_"+j).prop('checked')==true){
						if($("#status_"+j).val()=="" || $("#status_"+j).val() == null){
							$("#status_"+j).focus();
							showerr($("#status_"+j)[0], "Status is required!","block");
							return false;
						}
						if($("#status_"+j).val()=="P" || $("#status_"+j).val() == "R"){
							if($("#txtArea_"+j).val()=="" || $("#txtArea_"+j).val() == null){
								$("#txtArea_"+j).focus();
								showerr($("#txtArea_"+j)[0], "Reason is required!","block");
								return false;
							}
						}						
						var vcApp="N";
						if($("#appReq_"+j).prop('checked')==true){
							vcApp="Y";
						}else{
							vcApp="N";
						}
						
						var mapedJson={psId :$("#psId_"+j).val(), ppId :$("#ppId_"+j).val(), status :$("#status_"+j).val(), text :$("#txtArea_"+j).val(), isReq :vcApp,previousstatus :$("#previousstatus_"+j).val()}
						xjsonArray.push(mapedJson);
					}
				}
				
				if(xjsonArray==""||xjsonArray==null){
					setTimeout(function () {
						displaySuccessMessages("errMsg",'Please Select Atleast One Checkbox', "");
					}, 1000);
					clearSuccessMessageAfterFiveSecond("errMsg");
					return false;
				}
				//alert("DATA ::"+JSON.stringify(xjsonArray));
				try {
					var jsonObject={"list": JSON.stringify(xjsonArray), "type": $("#pTyp").val()};	
					console.log("btnSubmit Research Proposal Approval");
					console.log(jsonObject);
					var encData=encAESData($("#AESKey").val(), jsonObject);
					$('#btnSubmit').hide();
					$.ajax({
						type: "POST",
						url: "../ResearchProposalApprovalService",
						data: {encData: encData, fstatus: "S"},
						dataType: "json",
						success: function (response) {
							var data= decAESData($("#AESKey").val(), response);
							$('#btnSubmit').show();
							if(data.status=="1"){
								setTimeout(function() {
									displaySuccessMessages("errMsg1", data.msg, "");							
								}, 3000);
								clearSuccessMessageAfterFiveSecond("errMsg1");
							}else{
								setTimeout(function() {
									displaySuccessMessages("errMsg2", data.msg, "");							
								}, 3000);
								clearSuccessMessageAfterFiveSecond("errMsg2");							
							}
													
							if(data.status=="1"){
								setTimeout(function() {
									location.reload();
									//document.location.href="research_proposal_approval_e.jsp?fstatus=N&pageType="+data.type;
								}, 5000);
							}
						},
						error: function(xhr, status, error) {
							alert(xhr.responseText);
							alert(status);
							alert("error :"+error);
							
							$('#btnSubmit').show();
						}
					});	
				} catch (e) {
					// TODO: handle exception
					alert("ERROR :"+e);
				}
			}else{
				displaySuccessMessages("errMsg2", "At least 1 rows should be checked in the table", "");
				clearSuccessMessageAfterFiveSecond("errMsg2");
			}
		} catch (e) {
			// TODO: handle exception
			alert("ERROR :"+e);
		}
	});
	
	$("#Xlocation").change(function(){	
		var locationcode = $('#Xlocation').val();
		getDdoDetailbyLocation(locationcode);
	});
});

function text(a) {
	try {
		var val = $("#status_"+a+" option:selected").val();
		if(val=="P"  || val=="R"){
			$("#txtArea_"+a).prop( "disabled", false );
			$("#appReq_"+a).hide();
			$("#appReq_"+a).prop('checked', false);
		}else{
			$("#txtArea_"+a).prop( "disabled", true );
			$("#appReq_"+a).show();
			$("#appReq_"+a).prop( "disabled", false );
		}		
	} catch (e) {
		// TODO: handle exception
		alert(e);
	}
}

function OpenModel(id) {
	try {
		//$('#reportDiolog', window.parent.document).modal('show');
		$("#frmRsrchPropAppL").attr("action", "form1_project_sub_e.jsp?fstatus=R&id="+id);
		$("#frmRsrchPropAppL").attr("target", "_blank");
		//$("#pdfForm").attr("method", "POST");
		$("#frmRsrchPropAppL").submit();	  	
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+err);
	}
}