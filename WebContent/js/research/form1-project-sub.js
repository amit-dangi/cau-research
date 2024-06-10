$(document).ready(function(){
	$("#det2").hide();
	$("#btnReset").click(function(){
		location.reload();
	});
	$("#btnReset1").click(function(){
		location.reload();
	});
	
	$("#PiName").change(function(){			
		var emp = $(this).find('option:selected').val();
		if(emp!='')
			getDptByEmp(emp, "");
	});
	$("#XISRETIRE").click(function(){
	if($("#XISRETIRE").prop("checked") == true) {
		$("#PiName").val('');
		$("#non_ret_pi").hide();
		$("#ret_pi").show();
		getDptByEmp('', "");
	}else{
		$("#non_ret_pi").show();
		$("#ret_pi").hide();
	}});
	$("#deptId").change(function(){			
		var deptId = $(this).find('option:selected').val();
		var emp = $("#PiName").val();
		if(emp!='' && deptId!=''){
			getDesignByEmployeeAndDept(emp, deptId, "");
		}else{
			getDesignByEmployeeAndDept("%", deptId, "");
		}
	});	
	
	$("#btnSearch").click(function(){
		
		
		var locationCode=$("#Xlocation").val();
		var ddoCode=$("#Xddo").val();
		//alert("locationCode="+locationCode+" ddoCode="+ddoCode);
		document.location.href="form1_project_sub_e.jsp?fstatus=S";
				//&locationCode="+locationCode+"&ddoCode="+ddoCode;
		/*var PiName=$("#PiName").val();
		var deptId=$("#deptId").val();
		var desId=$("#desId").val();	
		
		$("#frmF1ProjectSubmissionE").attr("target", "btmfrmF1ProjectSubmissionE");
		$("#frmF1ProjectSubmissionE").attr("action", 'form1_project_sub_l.jsp?fstatus=V&PiName='+PiName+'&deptId='+deptId+'&desId='+desId);
		$("#frmF1ProjectSubmissionE" ).submit();*/		
	});	
	
	$("#btnSearch1").click(function(){
		var PiName=$("#PiName").val();
		var deptId=$("#deptId").val();
		var desId=$("#desId").val();	
		var locationCode=$("#Xlocation").val();
		var ddoCode=$("#Xddo").val();
		
		var projtype=$("#projtype").val();
		var erptype=$("#erptype").val();
		var projtermshort=$("#projtermshort").val();
		$("#frmF1ProjectSubmissionE").attr("target", "btmfrmF1ProjectSubmissionE");
		$("#frmF1ProjectSubmissionE").attr("action", 'form1_project_sub_l.jsp?fstatus=V&PiName='+PiName+'&deptId='+deptId+'&desId='+desId+"&locationCode="+locationCode+"&ddoCode="+ddoCode+"&projtype="+projtype+"&erptype="+erptype+"&projtermshort="+projtermshort);
		$("#frmF1ProjectSubmissionE" ).submit();		
	});	
	
	 $('#finCommitUni').click(function(){
		 if ($('#finCommitUni').prop("checked")){
			 $("#det2").show();
		 }else{
			 $("#det2").hide();
		 }
	 });
	
	$('#btnNew').click(function(){		  
		 document.location.href="form1_project_sub_e.jsp?fstatus=N";
	 });
	
	$("#addmore").click(function(){
		var index=parseInt($('#index').val());
		if($('#doctitle'+index).val()==''){
			/*displaySuccessMessages("errMsg2", "Document Title is required", "");
			clearSuccessMessageAfterFiveSecond("errMsg2");
			*/
			$('#doctitle'+index).focus();
			showerr($("#doctitle"+index)[0], "Document Title is required!","block");
			return false;
		}else if($('#upldoc'+index).val()==''){
			displaySuccessMessages("errMsg2", "Upload Document is required", "");
			clearSuccessMessageAfterFiveSecond("errMsg2");
			return false;
		}else if($('#upldoc'+index).val()!=''){
			var x=0;
			if($('#upldoc'+index).val()!=undefined){
				var ext = $("#upldoc"+index).val().split('.').pop().toLowerCase();
				if($.inArray(ext, ['pdf','png','jpg','jpeg','doc','docx']) == -1) {	
					x=1;
					$('#upldoc'+index).focus();
					alert("Note: Only .pdf, .jpg, .png & doc files will be allowed for uploading.!");
					return false;
				}
				
				var	fsize=$('#upldoc'+index)[0].files[0].size;
				var file = Math.round((fsize / 1024));
				if (parseInt(file) > 20480 || parseInt(file) < 100) {
					x=1;
					$('#upldoc'+index).focus();
					alert("File size should be greater than 100 kb & less than 20 MB!");
					return false;
				}
			}else{
				x=0;
			}
			if(x==0){
				index=index+1;
				var newRow = $("<tr>");	 
				var cols = '<tr >'
					+'<td class="text-center"><input type="text" class="form-control" id="doctitle'+index+'" name="doctitle'+index+'"' 
					+' maxlength="100" onchange="namechange('+index+',this.value)"; placeholder="Enter Document Title" >'
					+'<input type="hidden" class="form-control" id="hfname'+index+'" name="hfname'+index+'">'
					+'<input type="hidden" class="form-control" id="fsize'+index+'" name="fsize'+index+'">'
					+'</td>'
					+'<td id='+index+' class="text-center"><input type="file" id="upldoc'+index+'" onchange="data('+index+');" name="upldoc'+index+'" class=" data" ></td>'
					+'<td style="text-align:center;color:RED;cursor:pointer; width:5%; " onclick="deletedata('+index+',this)"><i class="fa fa-trash-o"></i> Delete</td>';
				$('#searchTable').append(cols);
				$("#index").val(index);
				var rowcountAfterDelete = document.getElementById("searchTable").rows.length;
				if(rowcountAfterDelete==5){
					document.getElementById('dydiv').style.height = "auto";
				}
			}
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
	
	$("#savedmanualproj").change(function(){	
		var savedmanualproj = $('#savedmanualproj').val();
		$('#projPropsalIdManual').val(savedmanualproj);
		//$('#savedmanualproj').val('');
	});
	
	
	
	$("#projtype").change(function(){	
		var projtype = $('#projtype').val();
		if(projtype=='AICRP'){
			 $('#erptypediv').show();
		}
		else{
			$('#erptypediv').hide();
			$("#erptype").val('');
		}
	});
});
$(document).off('change', '#PiName').on('change', '#PiName',function() {
	var PiName = $(this).find('option:selected').val();
	if($("#previous_pi_Name").val()!==PiName){
	$("#applied_pi_date").show();
	}else{
		$("#applied_pi_date").hide();
	}
});

function getDepartment(dptId){
	var hrmsApi = $("#hrmsApi").val().trim();
	var json={userId:$("#user_id").val()};  	
	var sel="";
	try {
		$.ajax({	
			type: "GET",
			url: hrmsApi+"rest/apiServices/getDepartmentDetail",
			jsonp: "parseResponse",
			dataType: "jsonp",
			data:json,
			success: function (response){	
				var department=response.department;
				var dropdown="<option value=''>Select Department</option>";
				$.each(department, function(index, department) {
					if(department.departmentId==dptId) {
						sel="selected";	
					} else {
						sel="";
					}
					dropdown+="<option  value='" + department.departmentId + "'"+sel+">" + department.departmentName + "</option>";					
				});
				
				$("#deptId").html(dropdown);
			}
		});	
	} catch (e) {
		// TODO: handle exception
		alert(e);
	}
}

function getDesignation(desId){
	var hrmsApi = $("#hrmsApi").val().trim();
	var json={userId:$("#user_id").val()};
	var sel="";
	try {
		$.ajax({
			type: "GET",
			url: hrmsApi+"rest/apiServices/getDesignationDetail",
			jsonp: "parseResponse",
			dataType: "jsonp",
			data:json,
			success: function (response){
				var designation=response.designationDetail;
				var dropdown="<option value=''>Select Designation</option>";
				$.each(designation, function(index, designation) {
					if(designation.designationID==desId){
						sel="selected";
					} else {
						sel="";
					}
					dropdown+="<option  value='" + designation.designationID + "'"+sel+">" + designation.designationName + "</option>";					
				});
				
				$("#desId").html(dropdown);
			}
		});	
	} catch (e) {
		// TODO: handle exception
		alert(e);
	}
}

function validateFormFields(){
	try{
		var projterm = $('input[name="projterm"]:checked').val();
		if($('#projtype').val()==''){
			$('#projtype').focus();
			showerr($("#projtype")[0], "Project Type is required!","block");			
			return false;
		}else if($('#erptype').val()=='' && $('#projtype').val()=='ERP'){
			$('#erptype').focus();
			showerr($("#erptype")[0], "ERP type is required!","block");			
			return false;
		}else if($('#fn_agency').val()=='' ){
			$('#fn_agency').focus();
			showerr($("#fn_agency")[0], "Funding Agency is required!","block");			
			return false;
		}else if($('#Xlocation').val()==''){
			$('#Xlocation').focus();
			showerr($("#Xlocation")[0], "Location is required!","block");			
			return false;
		}else if($('#Xddo').val()==''){
			$('#Xddo').focus();
			showerr($("#Xddo")[0], "DDO is required!","block");			
			return false;
		}/*else if($('#PiName').val()==''){
			$('#PiName').focus();
			showerr($("#PiName")[0], "Name of the Principal Investigator is required!","block");			
			return false;
		}*/else if(($('#previous_pi_Name').val()!==$('#PiName').val())&&($('#applied_date').val()=='')){
			$('#applied_date').focus();
			showerr($("#applied_date")[0], "Applied date is required!","block");			
			return false;
		}else if($('#deptId').val()==''){
			$('#deptId').focus();
			showerr($("#deptId")[0], "Department is required!","block");			
			return false;
		}else if($('#desId').val()==''){
			$('#desId').focus();
			showerr($("#desId")[0], "Designation is required!","block");			
			return false;
		}else if($('#projPropsal').val()==''){
			$('#projPropsal').focus();
			showerr($("#projPropsal")[0], "Title Of the project proposal is required!","block");			
			return false;
		}else if($('#durPropProjYear').val()=='' & projterm=='ShortTerm'){
			$('#durPropProjYear').focus();
			showerr($("#durPropProjYear")[0], "Year is required!","block");			
			return false;
		}else if($('#durPropProj').val()=='' & projterm=='ShortTerm'){
			$('#durPropProj').focus();
			showerr($("#durPropProj")[0], "Month is required!","block");			
			return false;
		}else if($('#totalBudgProp').val()=='' & projterm=='ShortTerm'){
			$('#totalBudgProp').focus();
			showerr($("#totalBudgProp")[0], "Total Budget Proposed is required!","block");			
			return false;
		}else if($('#proj_start_date').val()==''){
			$('#proj_start_date').focus();
			showerr($("#proj_start_date")[0], "Project Start Date is required!","block");			
			return false;
		}/*else if($('#FundAgency').val()==''){
			$('#FundAgency').focus();
			showerr($("#FundAgency")[0], "Fund Agency is required!","block");			
			return false;
		}else if($('#nameAddrCoPi').val()==''){
			$('#nameAddrCoPi').focus();
			showerr($("#nameAddrCoPi")[0], "Name and address of Co-PI's is required!","block");			
			return false;
		}else if($('#XTODATE').val()==''){
			$('#XTODATE').focus();
			showerr($("#XTODATE")[0], "Deadline For the submission of the project proposal is required!","block");			
			return false;
		}*/else if($('#finCommitUni').prop("checked") == true){
			if($('#finCommitUniDetails').val()==''){
				$('#finCommitUniDetails').focus();
				showerr($("#finCommitUniDetails")[0], "Details is required!","block");			
				return false;	
			}
		}
		
	    /*else if($('#nonRecCost').val()==''){
			$('#nonRecCost').focus();
			showerr($("#nonRecCost")[0], "Non-recurring(Equipment cost) is required!","block");			
			return false;
		}else if($('#chemAndCon').val()==''){
			$('#chemAndCon').focus();
			showerr($("#chemAndCon")[0], "Chemicals and consumables is required!","block");			
			return false;
		}else if($('#manpower').val()==''){
			$('#manpower').focus();
			showerr($("#manpower")[0], "Manpower is required!","block");			
			return false;
		}else if($('#contingency').val()==''){
			$('#contingency').focus();
			showerr($("#contingency")[0], "Contingency is required!","block");			
			return false;
		}else if($('#travel').val()==''){
			$('#travel').focus();
			showerr($("#travel")[0], "Travel is required!","block");			
			return false;
		}else if($('#outSourcingCharge').val()==''){
			$('#outSourcingCharge').focus();
			showerr($("#outSourcingCharge")[0], "Any other charges/outsourcing charges towards technical services is required!","block");			
			return false;
		}else if($('#overCharg').val()==''){
			$('#overCharg').focus();
			showerr($("#overCharg")[0], "Overhead charges is required!","block");			
			return false;
		}else if($('#durPropProj').val()!=''){
			if($('#durPropProj').val().length > 2){
				$('#durPropProj').focus();	
				showerr($("#durPropProj")[0], "Invalid Duration Of the proposed Project!","block");
				return false;	
			}
		}*/
	   var index=parseInt($('#index').val());
		if($('#upldoc'+index).val()!=''){
			if($('#doctitle'+index).val()==''){
			$('#doctitle'+index).focus();
			showerr($("#doctitle"+index)[0], "Document Title is required!","block");
			return false;
			}
		}else if($('#doctitle'+index).val()!=''){
			if($('#upldoc'+index).val()==''){
			displaySuccessMessages("errMsg2", "Upload Document is required", "");
			clearSuccessMessageAfterFiveSecond("errMsg2");
			return false;
			}
		}
	   
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function IsIntegerVal(sText) {
	var ValidChars = "0123456789";
	var IsNumber=true;
	var v = 0;
	var Char;
	
	for (i = 0; i < sText.value.length && IsNumber == true; i++) { 
		Char = sText.value.charAt(i); 
		if (ValidChars.indexOf(Char) == -1) {
			showerr(sText,"Value must be numeric.","block");
			sText.focus();
			return false;
		}
	}
	v = sText.value;
	var durPropProj = $("#durPropProj").val();
	var extdurPropProjMonth = $("#extdurPropProjMonth").val();
	if(v!="") {
		if(parseInt(durPropProj) >12 || parseInt(extdurPropProjMonth)>12 ){
			showerr(sText,"Max Month avail is 12.","block");
			sText.focus();
			return false;
		}
		
		if (v > 0) {
			return IsNumber;
		}
		else {
			/*showerr(sText,"Value must be greater than zero(s)","block");
			sText.focus();
			return false;*/
		}
	}  
}

function deletedata(id,did){	
	var rowcountAfterDelete = document.getElementById("searchTable").rows.length;
	if(rowcountAfterDelete!=1){
		var filesize=$('#filesize').val();
		var fsize=$('#fsize'+id).val();
		if(fsize=='')
			fsize="0.00";
		filesize=parseFloat(filesize)-parseFloat(fsize).toString();
		$('#filesize').val(filesize);
		$(did).parents("tr").remove();
	}else{
		displaySuccessMessages("errMsg2", "At least 1 rows should be present in the table", "");
		clearSuccessMessageAfterFiveSecond("errMsg2");
	}
	if(rowcountAfterDelete==4){
		document.getElementById('dydiv').style.height = "auto";
	}
}

function saveAndSubmit(val){
	try {
		if(validateFormFields()){
			var form_data = new FormData();
			var workarray = [];
			var exp_index = $("#index").val();
			
			for(i=1; i<=parseInt(exp_index); i++){
				if($("#upldoc"+i).val()!="" && $("#upldoc"+i).val()!=undefined){
					var ext = $("#upldoc"+i).val().split('.').pop().toLowerCase();
					
					if(ext !=""){
						if($.inArray(ext, ['pdf','png','jpg','jpeg','doc','docx']) == -1) {						
							$('#upldoc'+i).focus();
							alert("Note: Only .pdf, .jpg, .png & doc files will be allowed for uploading.!");
							return false;
						}
					}
					var	fsize=$('#upldoc'+i)[0].files[0].size;
					var file = Math.round((fsize / 1024));
					if (parseInt(file) > 20480 || parseInt(file) < 100) {
						$('#upldoc'+i).focus();
						alert("File size should be greater than 100 kb & less than 20 MB!");
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
			//alert("DATA :"+JSON.stringify(workarray));
			var projtype = $('#projtype').val();
			var	projunder = $('#projunder').val();
			var	erptype = $('#erptype').val();
			var projterm = $('input[name="projterm"]:checked').val();
			var IsApprovalReq = $('input[name="IsApprovalReq"]:checked').val();
			var PiName = $('#PiName').val();			
			var	dept = $('#deptId').val();
			var desg = $('#desId').val();
			var	projPropsal	= $('#projPropsal').val();
			var durPropProj = $('#durPropProj').val();
			var totalBudgProp = $('#totalBudgProp').val();
			var FundAgency = $('#FundAgency').val();
			var nameAddrCoPi = $('#nameAddrCoPi').val();
			var XTODATE = $('#XTODATE').val();
			var proj_start_date = $('#proj_start_date').val();
			//alert("check------ "+proj_start_date);
			var nonRecCost = $('#nonRecCost').val();
			var chemAndCon = $('#chemAndCon').val();
			var manpower = $('#manpower').val();
			var contingency = $('#contingency').val();
			var travel = $('#travel').val();
			var outSourcingCharge = $('#outSourcingCharge').val();
			var overCharg = $('#overCharg').val();
			var id = $('#mid').val();
			var PPID = $('#projPropsalId').val();
			var locationCode = $('#Xlocation').val();
			var ddoCode = $('#Xddo').val();
			var projPropsalIdManual = $('#projPropsalIdManual').val();
			var durPropProjYear = $('#durPropProjYear').val();
			
			var fn_agency = $('#fn_agency').val();
			var proj_obj = $('#proj_obj').val();
			var thrust_area = $('#thrust_area').val();
			 var  sub_thrust_area = [];
			  	$(".checkBoxClass:checked").each(function() {
			  		sub_thrust_area.push(this.value);
			  	});
			  	var subThrustArea = sub_thrust_area.toString();
			  	if((subThrustArea==""||subThrustArea==null) && thrust_area!="" && sub_thrust_area.length>0)
			  	{
			  	  $(".checkBoxClass").focus();
			  	  showerr($(".checkBoxClass")[0],"atleast 1 sub thrust area should be checked","block");
			  	  return false;
			  	}
			var capital= $('#capital').val();
			var general= $('#general').val();
			var revenue= $('#revenue').val();
			var lumpsum= $('#lumpsum').val();
			if(capital==""||capital==null){
				capital=0;
			}if(general==""||general==null){
				general=0;
			}if(revenue==""||revenue==null){
				revenue=0;
			}if(lumpsum==""||lumpsum==null){
				lumpsum=0;
			}
			var budgetHeads=capital+"~"+general+"~"+revenue+"~"+lumpsum;
			
			var projPropSub ="", projPropClear="", necClearObt="", finCommitUni="", AttchCertif="", finCommitUniDetails="";
			if ($('#projPropSub').prop("checked")){
				projPropSub="Y";
			}else{
				projPropSub="N";
			}
			if ($('#projPropClear').prop("checked")){
				projPropClear="Y";
			}else{
				projPropClear="N";
			}
			if ($('#necClearObt').prop("checked")){
				necClearObt="Y";
			}else{
				necClearObt="N";
			}
			if ($('#finCommitUni').prop("checked")){
				finCommitUniDetails=$("#finCommitUniDetails").val();
				finCommitUni="Y";
			}else{
				finCommitUniDetails="";
				finCommitUni="N";
			}
			if ($('#AttchCertif').prop("checked")){
				AttchCertif="Y";
			}else{
				AttchCertif="N";
			}
			
			var inst_charges = $('#inst_charges').val();
			
			var jsonObject={"projtype":projtype,"projunder":projunder,"erptype":erptype,"projterm":projterm,"IsApprovalReq":IsApprovalReq, "PiName":PiName,"retirePiName":$("#RETIRE_PS").val(), "dept":dept,"locationCode":locationCode,"ddoCode":ddoCode, "desg":desg, "projPropsal":projPropsal, "durPropProj":durPropProj,
					"totalBudgProp":totalBudgProp, "FundAgency":FundAgency, "nameAddrCoPi":nameAddrCoPi, "XTODATE":XTODATE,"proj_start_date":proj_start_date, 		
					"nonRecCost":nonRecCost, "chemAndCon":chemAndCon, "manpower":manpower, "contingency":contingency, 
					"travel":travel, "outSourcingCharge":outSourcingCharge, "overCharg":overCharg, "projPropSub": projPropSub,
					"projPropClear": projPropClear, "necClearObt": necClearObt, "finCommitUni": finCommitUni, "AttchCertif": AttchCertif, 
					"workarray": JSON.stringify(workarray), "mode":val, "fId":id, "ppId":PPID, "finCommitUniDetails":finCommitUniDetails,
					"durPropProjYear":durPropProjYear,"projPropsalIdManual":projPropsalIdManual,"fn_agency":fn_agency, "proj_obj":proj_obj,
					"thrust_area":thrust_area,"sub_thrust_area":subThrustArea,"budgetHeads":budgetHeads,"inst_charges":inst_charges,"previous_pi_Name":$('#previous_pi_Name').val(),"applied_date":$('#applied_date').val()};			
			var encData=encAESData($("#AESKey").val(), jsonObject);	
			
			/*$("#btnSave").hide();
			$("#btnSubmit").hide();
			$("#btnReset").hide();
			
			$("#btnUpdate").hide();
			$("#btnSubmit1").hide();*/
			
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../ProjectSubmissionService?fstatus=S", true);			    
			xmlHttp.setRequestHeader("encData", encData);
			//xmlHttp.setRequestHeader("fstatus", "S");
			    
			xmlHttp.send(form_data);
			try{
				xmlHttp.onreadystatechange = function() {
					if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
						var data=JSON.parse(this.responseText);
						if(data.status=='1'){
							displaySuccessMessages("errMsg1", data.msg, "");
							clearSuccessMessageAfterFiveSecond("errMsg1");
							
							$("#btnSave").show();
							$("#btnSubmit").show();
							$("#btnReset").show();
							$("#btnUpdate").show();
							$("#btnSubmit1").show();
							
							setTimeout(function() {
								location.reload();
							}, 2000);
						}else{
							displaySuccessMessages("errMsg2", data.msg, "");
							clearSuccessMessageAfterFiveSecond("errMsg2");
							$("#btnSave").show();
							$("#btnSubmit").show();
							$("#btnReset").show();							
							$("#btnUpdate").show();
							$("#btnSubmit1").show();
						}
					}
				}
			} catch (err){
				alert(err);
			}
		}else{
			return false;
		}
	} catch (e) {
		alert("ERROR :"+e);
	}
}

function deleteRecord(id){	
	try {	
		var r = confirm("Are You Sure!");
		if (r == true){ 		
			var jsonObject={"id":id};   	 	
			$.ajax({
				type: "POST",
				url: "../ProjectSubmissionService?fstatus=D",
				data: {jsonObject: JSON.stringify(jsonObject) },
				dataType: "json",
				success: function (data) {		         
					var tRec = $("#totRow").val();
					for(var i=1;i<=tRec;i++){
						$("#EDIT_RECORD_"+i).wrap('<td style="display:none"/>');
						$("#DELETE_RECORD_"+i).wrap('<td style="display:none"/>');		            			
					}
					displaySuccessMessages("errMsg1", data.msg, "");
					clearSuccessMessageAfterFiveSecond("errMsg1");

					setTimeout(function() {
						location.reload();
					}, 2000);
				},
				error: function (data) {
					alert("Error");
				}
			}); 			    			 
		}		
	} catch (err) {
		alert(err);
	}
}

function editRecord(id){
	try {
		$('#frmF1ProjectSubmissionL').attr('action', 'form1_project_sub_e.jsp?fstatus=E&id='+id);
		$("#frmF1ProjectSubmissionL").attr("target", '_parent');
		$("#frmF1ProjectSubmissionL" ).submit();
	} catch (err) {
		alert("ERROR :"+err);
	}
}

function getData(fstatus) {
	try {
		if(fstatus=="E"){
			if($('#finCommitUni').prop("checked") == true){
				$("#det2").show();
			}
		}
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

function getEmployee(empid){
	try {
		var hrmsApi = $("#hrmsApi").val().trim();
		var locationCode = $('#Xlocation').val();
		var ddoCode = $('#Xddo').val();
		var json={"location":locationCode,"ddo":ddoCode};
		$.ajax({
			type: "GET",
			url: hrmsApi+"rest/apiServices/getEmployee",
			jsonp: "parseResponse",
			dataType: "jsonp",
			async: false,
			data:json,
			success: function (response){
				var employee=response.employee;
				var emp="<option value=''>Select Principal Investigator</option>";
				$.each(employee, function(index, employee) {
				
					if(employee.employeeId==empid){
					
						emp+="<option selected value='"+employee.employeeId+"'> "+employee.employeeName+"</option>";
					}else{
						emp+="<option  value='"+employee.employeeId+"'> "+employee.employeeName+"</option>";
					}
				});
				$("#PiName").html(emp);
			}
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

/*function getDptByEmp(emp, deptId){
	try{		
		var json={empId:emp};  		
		var sel="";
		var hrmsApi=$("#hrmsApi").val().trim();
		$.ajax({	
			type: "GET",
			url: hrmsApi+"rest/apiServices/getDeptByEmpId",
			jsonp: "parseResponse",
			dataType: "jsonp",
			async: false,
			data:json,
			success: function (response){
				console.log("get getDptByEmp");
				console.log(response);
				var dept=response.deptDetail;
				var count=0;
				var dropdown="<option value=''>Select Department</option>";
				$.each(dept, function(index, dept) {
					++count;
					if(dept.ID==deptId){
						dropdown+="<option selected value='" + dept.ID + "'"+sel+">" + dept.Name + "</option>";
					}else{
						dropdown+="<option  value='" + dept.ID + "'"+sel+">" + dept.Name + "</option>";
					}
				});
				$("#deptId").html(dropdown);				
			}
		});

	}catch(err){
		alert("Error :"+err);
	}
}*/

function getDesignByEmployeeAndDept(empId, deptId, des){
	var hrmsApi = $("#hrmsApi").val().trim();
	var json={"empid":empId, "deptid":deptId};  
	var sel="";
	
	$.ajax({
		type: "GET",
		url: hrmsApi+"rest/apiServices/getdsigbyempanddept",
		jsonp: "parseResponse",
		dataType: "jsonp",
		async: false,
		data:json,
		success: function (response){
			var employee=response.designation;
			var desgination="";
			desgination+="<option  value=''>Select Designation</option>";
			
			$.each(employee, function(index, employee) {
				if(employee.id==des){
					desgination+="<option selected value='" + employee.id + "'"+sel+">" + employee.name + "</option>";
				}else{
					desgination+="<option value='" + employee.id + "'"+sel+">" + employee.name + "</option>";
				}				
			});
			$("#desId").html(desgination);			
		}
	});
}

function geteditDD(status, piName, Dept, Desg, locationCode,ddoCode,sess_emp_id,dept_id,Desg){
	try {
		//alert(status);
		if(status=="E" || status=="R"){
			getEmployee(piName);
			getDptByEmp(piName, Dept);
			if(piName==""||piName==null){
				getDesignByEmployeeAndDept("%", Dept, Desg);
			}else{
			getDesignByEmployeeAndDept(piName, Dept, Desg);
			}
			getDdoDetailbyLocation(locationCode,ddoCode);
		}else if(user_status=="U"){
			getLocationDetail(locationCode); 
			getDdoDetailbyLocation(locationCode,ddoCode);
			getEmployee(sess_emp_id);
			getDptByEmp(sess_emp_id, dept_id);
			getDesignByEmployeeAndDept(sess_emp_id, dept_id, Desg);
		}
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

function deletesavefile(attid, fliename, mastid, id,name){
	
	var retVal = confirm("File will be deleted permanently. \nAre you sure you want to remove this file?");
	if(retVal == true) {
		try{
			$.ajax({
				type: "POST",
				async: false,
				url: "../ProjectSubmissionService",
				data:{"fstatus":"2", "attid":attid, "filename":fliename,"mastid":mastid},
				success: function (data){
					displaySuccessMessages("errMsg1", data.msg, "");
					clearSuccessMessageAfterFiveSecond("errMsg1");
					setTimeout(function() {
						location.reload();
					}, 2000);
				}
			});
		}catch(err){
			alert(err);
		}
		} else {
			return false;
		}
}


function saveExtension(psmid){	
	try {
		 if($('#projET').val()==''){
				$('#projET').focus();
				showerr($("#projET")[0], "Extension Type is required!","block");			
				return false;
			}else if($('#extdurPropProjYear').val()==''){
				$('#extdurPropProjYear').focus();
				showerr($("#extdurPropProjYear")[0], "Extension Year is required!","block");			
				return false;
			}else if($('#extdurPropProjMonth').val()==''){
				$('#extdurPropProjMonth').focus();
				showerr($("#extdurPropProjMonth")[0], "Extension Month is required!","block");			
				return false;
			}
			else if(parseInt($("#extdurPropProjMonth").val())>12 ){
				$('#extdurPropProjMonth').focus();
				showerr(sText,"Max Month avail is 12.","block");
				return false;
			}
		
		var r = confirm("Are You Sure for Project Extension!");
		if (r == true){ 		
			var jsonObject={"id":psmid,"projET":$('#projET').val(),"extdurPropProjYear":$('#extdurPropProjYear').val(),"extdurPropProjMonth":$('#extdurPropProjMonth').val()};   	 	
			console.log("saveExtension obj");
			console.log(jsonObject);
			$.ajax({
				type: "POST",
				url: "../ProjectSubmissionService?fstatus=EXT",
				data: {jsonObject: JSON.stringify(jsonObject) },
				dataType: "json",
				success: function (data) {
					console.log("saveExtension data");
					console.log(data);
					
					displaySuccessMessages("errMsgnew", data.msg, "");
					//clearSuccessMessageAfterFiveSecond("errMsg1");

					setTimeout(function() {
						location.reload();
					}, 2000);
				},
				error: function (data) {
					alert("Error");
				}
			}); 			    			 
		}		
	} catch (err) {
		alert(err);
	}
}
function checkSavedSubThrustArea(){
	if($("#chkAllSavedSubThrustArea").is(':checked')){
		$(".checkBoxClass").prop('checked', true);	
	}else{
		$(".checkBoxClass").prop('checked', false);
		     
	} 
}
