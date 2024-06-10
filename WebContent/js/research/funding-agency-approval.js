$(document).ready(function(){
	$("#btnReset").click(function(){
		redirect();
	});
	
	$("#btnReseT").click(function(){
		document.getElementById("frmContEmpProjMapD").reset();
	});
	

	 $('#btnBack').click(function(){		  
		 document.location.href="funding_agency_e.jsp?fstatus=N";
	 });
	 
	 
	 $('#editamount').click(function(){		  
		 document.getElementById('approvedAmount').removeAttribute('disabled');
		 $("#editamount").hide();
		 $("#updateamount").show();
		 $("#btnSave").hide();
	 });
	
	$("#btnSearch").click(function(){
/*		var name=$("#faNameS").val();
		var type=$("#faTypeS").val();
		var mob=$("#faMobNoS").val();*/	
		var project = $("#Sproj").val();
		var funding = $("#Snagency").val();
		//alert("funding - "+funding);
		
		$('#frmContEmpProjMapD').attr('target', 'frmFundingAgencyApprovalE');
		$('#frmContEmpProjMapD').attr('action', 'funding_agency_approval_research_l.jsp?fstatus=V&Sproj='+project+'&Snagency='+funding);
		$("#frmContEmpProjMapD" ).submit();		
	});
	
	 $('input[type="checkbox"]').click(function(){
         if($(this).prop("checked") == true){
        	 $('#opening_blnc').prop('disabled', false);
     		//document.getElementById('opening_blnc').removeAttribute('disabled');
         }
         else if($(this).prop("checked") == false){
        	 $('#opening_blnc').prop('disabled', true);
        	 $('#opening_blnc').val("");
         }
     });
	
	
	$('#btnSave').click(function(){
	if(validateFormFields()){
		if("1"=="1"){	
			var form_data = new FormData();
			var workarray = [];
			var exp_index = "1";
			var is_upload_file="N";
			
			for(i=1; i<=parseInt(exp_index); i++){
				if($("#upldoc"+i).val()!="" && $("#upldoc"+i).val()!=undefined){
					is_upload_file="Y";
					var ext = $("#upldoc"+i).val().split('.').pop().toLowerCase();
					
					if(ext !=""){
						if($.inArray(ext, ['pdf','png','jpg','jpeg','doc','docx']) == -1) {						
							$('#upldoc'+i).focus();
							alert("Note: Only .pdf, .jpg, .png, .doc & doc files will be allowed for uploading.!");
							return false;
						}
					}
					var	fsize=$('#upldoc'+i)[0].files[0].size;
					var file = Math.round((fsize / 1024));
					if (parseInt(file) > 20480 ) {
						$('#upldoc'+i).focus();
						alert("File size should be less than 20 MB!");
						return false;
					}
					
					var mapedJson={doctitle: $("#doctitle"+i).val()}
					workarray.push(mapedJson);
					if ($("#upldoc"+i).val()!='' && $("#upldoc"+i).val()!=undefined){
						var fileCount = document.getElementById("upldoc"+i).files.length;
						for (j = 0; j < fileCount; j++) {
							form_data.append("upload_doc"+i, document.getElementById("upldoc"+i).files[j]);
						}
					}	
				}
				
			}	
			
			var location_code = $('#Xlocation').val();
			var ddo_id = $("#Xddo").val();
			var PS_MID = $("#proj").val();
			var fa_id = $("#fnagency").val();
			var approved_amount = $("#approvedAmount").val();
			var alloted_amount = $("#allotedAmount").val();
			var remarks= $("#remarks").val();
			var remaining_amount=$('#remainingAmount').val();
			var sanction_orderno=$('#sanction_orderno').val();
			var sanction_orderdate=$('#sanction_orderdate').val();
			var all_date=$('#all_date').val();
			var fin_yr = $("#fin_yr").val();
			var is_opening_blnc="N";
				if($("#is_opening_blnc").prop("checked") == true){
				 is_opening_blnc = "Y";
					}
			var opening_blnc = $("#opening_blnc").val();
			var received_date= $("#received_date").val();
			var jsonObject={"location_code":location_code, "ddo_id":ddo_id, "PS_MID":PS_MID, "fa_id":fa_id,
					"approved_amount":approved_amount, "alloted_amount":alloted_amount, "is_upload_file":is_upload_file,
					"remarks":remarks,"remaining_amount":remaining_amount,"sanction_orderno":sanction_orderno,
					"sanction_orderdate":sanction_orderdate,"all_date":all_date,"fin_yr":fin_yr,
					"is_opening_blnc":is_opening_blnc,"opening_blnc":opening_blnc,"received_date":received_date};
				console.log("Funding agency approval Save Json Object");
				console.log(jsonObject);
				var encData=encAESData($("#AESKey").val(), jsonObject);	
				
			
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../FundingAgencyApprovalService?fstatus=SAVE", true);			    
			xmlHttp.setRequestHeader("encData", encData);
			xmlHttp.send(form_data);
			try{
				xmlHttp.onreadystatechange = function() {
					if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
						var data=JSON.parse(this.responseText);
						console.log("Response");
						console.log(data);
							if(data.flag=="Y"){
								setTimeout(function() {
									displaySuccessMessages("errMsg1", data.status, "");							
								}, 1000);
								
							}else{
								setTimeout(function() {
									displaySuccessMessages("errMsg2", data.status, "");							
								}, 3000);
								clearSuccessMessageAfterFiveSecond("errMsg2");							
							}
							$("#btnSave").show();
							$("#btnReset").show();
							if(data.flag=="Y"){
								setTimeout(function() {
									redirect();
								}, 2000);
							}
						
					}
				}
			} catch (e) {
				// TODO: handle exception
				alert("ERROR :"+e);
			}
		}else{
			return false;
		}
	}
	});	
	
	$("#Xlocation").change(function(){	
		var locationcode = $('#Xlocation').val();
		getDdoDetailbyLocation(locationcode);
	});
});

function redirect(fstatus,faaid,fin_yr){
	$('#frmFundingAgencyApprovalE').attr('action', 'funding_agency_approval_research_e.jsp?fstatus='+fstatus+'&faaid='+faaid+'&fin_yr='+fin_yr);
	$("#frmFundingAgencyApprovalE" ).submit();
}

function geteditDD(status, PS_MID, Dept, Desg, locationCode,ddoCode){
	try {
		//alert(status);
		/*if(status=="E" || status=="R"){*/
			getDdoDetailbyLocation(locationCode,ddoCode);
			/*getProjectDetailbyLocation(PS_MID,locationCode,ddoCode);*/
			getResearchProposal(locationCode,ddoCode);
			var proj=$('#proj').val();
			getProjectTenurebyApprovedProjects(PS_MID);
		/*}else if(user_status=="U"){
			getDdoDetailbyLocation(locationCode,ddoCode);
			getProjectDetailbyLocation(PS_MID,locationCode,ddoCode);
			var proj=$('#proj').val();
			getProjectTenurebyApprovedProjects(PS_MID);
		}*/
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

function getRemainingAmout(){
	let appramt=$('#approvedAmount').val().trim()==""?"0":$('#approvedAmount').val().trim();
	let opening_blnc=$('#opening_blnc').val().trim()==""?"0":$('#opening_blnc').val().trim();
		appramt=parseInt(appramt)+parseInt(opening_blnc);
	let allotedamt=$('#allotedAmount').val().trim()==""?"0":$('#allotedAmount').val().trim();
	let lastallotedAmount=$('#lastallotedAmount').val().trim()==""?"0":$('#lastallotedAmount').val().trim();
	let finalallotedamt=parseInt(lastallotedAmount)+parseInt(allotedamt);
	let remamt= parseInt(appramt)-finalallotedamt;
	//alert(appramt);
	if(remamt<0 || isNaN(remamt)){
		showerr($("#allotedAmount")[0], "Invalid Amount Entered!","block");
		$('#remainingAmount').val("0");
		$('#allotedAmount').val("0");
		//$('#approvedAmount').val("0");
		$('#allotedAmount').focus();
		//$('#btnSave').hide();
		return false;
	}else
	$('#remainingAmount').val(remamt);
	$('#btnSave').show();
	
}

function validateFormFields(){
	try{
		if($('#Xlocation').val()==''){
			$('#Xlocation').focus();
			showerr($("#Xlocation")[0], "Location is required!","block");			
			return false;
		}else if($('#Xddo').val()==''){
			$('#Xddo').focus();
			showerr($("#Xddo")[0], "DDO is required!","block");			
			return false;
		}else if($('#proj').val()==''){
			$('#proj').focus();
			showerr($("#proj")[0], "Approved Project is required!","block");			
			return false;
		}else if($('#fnagency').val() == ''){
			$('#fnagency').focus();
			showerr($("#fnagency")[0], "Funding Agency is required!","block");			
			return false;
		}else if($('#approvedAmount').val()==''){
			$('#approvedAmount').focus();	
				showerr($("#approvedAmount")[0], "Please Enter Total Approved Amount","block");
				return false;	
		}else if($('#allotedAmount').val()==''){
			$('#allotedAmount').focus();	
			showerr($("#allotedAmount")[0], "Please Enter Allocation Amount","block");
			return false;	
		}
		else if($('#remainingAmount').val()==''){
			$('#remainingAmount').focus();	
			showerr($("#remainingAmount")[0], "Remaining Amount is Required.","block");
			return false;	
		}else if($('#all_date').val()==''){
			$('#all_date').focus();	
			showerr($("#all_date")[0], "Fund Allocation Date is Required.","block");
			return false;	
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
			$.ajax({
				type: "POST",
				url: "../FundingAgencyApprovalService",
				data: {"faaId":Id, "fstatus":"DELETE"},
				dataType: "json",
				success: function (data) {
					
					if(data.flg=="Y"){
					displaySuccessMessages("errMsg1", data.msg, "");
					}else{
						displaySuccessMessages("errMsg2", data.msg, "");	
					}												
					setTimeout(function () {
						redirect();
					}, 1000);
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

function editRecord(id,fin_yr){
	try {
		$('#frmFundingAgencyApprovalL').attr('action', 'funding_agency_approval_research_e.jsp?fstatus=E&faaid='+id+'&fin_yr='+fin_yr);
		$("#frmFundingAgencyApprovalL").attr("target", '_parent');
		$("#frmFundingAgencyApprovalL" ).submit();		
	} catch (err) {
		alert(error);
	}
}

/*
 * Created by Amit Dangi on 11-03-2022
 * to get the Get Project Tenure by Approved Projects
 * */

function getProjectTenurebyApprovedProjects(projectId){
	try {
		if(projectId==''){var projectId = $('#proj').val();}
		var data={"fstatus": "GETPROJECTSTENURE","projectId":projectId};
		$.ajax({
			type: "POST",
			url: "../FundingAgencyApprovalService",
			data:data,
			async: false,
			success: function (response){
				console. log("getProjectTenurebyApprovedProjects response");
				console.log(response);
				var department=response.projectdropdown;
				$.each(department, function(index, department) {
					$('#proj_duration').val(department.projduration);
				});
				
			}
		});
	
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+err);
	}
}

function calculateblnc(){
	var approved_amount = $("#approvedAmount").val();
	var alloted_amount_sum = $("#alloted_amount_sum").val();
	
	//alert("approved_amount||"+approved_amount);
	try{
	/*if(parseInt(approved_amount)<parseInt(alloted_amount_sum)){
			$('#approvedAmount').focus();	
			showerr($("#approvedAmount")[0], "Total Approved Amount cann't be less than Sum of Previous total Allotments","block");
			return false;	
	}*/
	var availamt=parseInt(approved_amount)-(alloted_amount_sum);
	$("#allotedAmount").val(availamt);
	getRemainingAmout();
	}
	catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

/*New function added for update the approved amount
Only ‘Total Approved Amount’ will always greater than ‘Total Allocated Amount’ 
for any particular Financial Year and for a funding agency*/
function updateAmount(){
	
	var form_data = new FormData();
	var fin_yr = $("#fin_yr").val();
	var PS_MID = $("#proj").val();
	var fa_id = $("#fnagency").val();
	
	var previous_approvedAmount = $("#previous_approvedAmount").val();
	var approved_amount = $("#approvedAmount").val();
	var alloted_amount_sum = $("#alloted_amount_sum").val();
	try{
	if(parseInt(approved_amount)<parseInt(alloted_amount_sum)){
			$('#approvedAmount').focus();	
			showerr($("#approvedAmount")[0], "Total Approved Amount cann't be less than Sum of Previous total Allotments","block");
			return false;	
	}
	}
	catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
	var amount_diff=parseInt(previous_approvedAmount)-parseInt(approved_amount);
	//alert("amount_diff::"+amount_diff);
	var jsonObject={"fin_yr":fin_yr,"PS_MID":PS_MID, "fa_id":fa_id,
					"approved_amount":approved_amount,"amount_diff":amount_diff};
		console.log("Funding agency update Approved Amount Json Object");
		console.log(jsonObject);
		var encData=encAESData($("#AESKey").val(), jsonObject);	
	
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("POST", "../FundingAgencyApprovalService?fstatus=UpdateAmt", true);			    
	xmlHttp.setRequestHeader("encData", encData);
	xmlHttp.send(form_data);
	try{
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
				var data=JSON.parse(this.responseText);
				console.log("Response");
				console.log(data);
				    displaySuccessMessages("errMsg1", data.status, "");
					clearSuccessMessageAfterFiveSecond("errMsg1");
					
					$("#btnSave").show();
					$("#btnReset").show();
					
					setTimeout(function() {
						redirect('E',$("#faaid").val(),fin_yr);
					}, 1000);
				
			}
		}
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}

}