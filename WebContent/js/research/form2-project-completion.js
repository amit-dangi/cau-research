$(document).ready(function(){
	$("#btnReset").click(function(){
		location.reload();
	});
	
	$("#btnSearch").click(function(){
		document.location.href="form2_project_completion_e.jsp?fstatus=S";			
	});	
	
	$("#piId").change(function(){		
		var emp = $(this).find('option:selected').val();
		var id = emp.slice(0, 4);
		if(emp!='' && id != 'Reti'){
			getDptByEmp(id, "");
		}
		if(id=='Reti'){
			getDptByEmp("", "");
		}
	});
	
	$("#deptId").change(function(){			
		var deptId = $(this).find('option:selected').val();
		var emp = $("#piId").val().slice(0, 4);		
		if(emp!='' && deptId!='' && emp != 'Reti'){
			getDesignByEmployeeAndDept(emp, deptId, "");
			}
		if(emp=='Reti'){
			getDesignByEmployeeAndDept("%", deptId, "");
		}
	});
	
	
	$("#btnSearch1").click(function(){
		var locationCode=$("#Xlocation").val();
		var ddoCode=$("#Xddo").val();
		var piId=$("#piId").val();
		var deptId=$("#deptId").val();
		var desId=$("#desId").val();	
		
		$("#frmF1ProjectSubmissionE").attr("target", "btmfrmF1ProjectSubmissionE");
		$("#frmF1ProjectSubmissionE").attr("action", 'form2_project_completion_l.jsp?fstatus=V&piId='+piId+'&deptId='+deptId+'&desId='+desId+"&locationCode="+locationCode+"&ddoCode="+ddoCode);
		$("#frmF1ProjectSubmissionE" ).submit();		
	});	
	
	$('#btnNew').click(function(){		  
		 document.location.href="form2_project_completion_e.jsp?fstatus=N";
	 });
	
	$("#addmore").click(function(){
		var index=parseInt($('#index').val());
		if($('#doctitle'+index).val()==''){
			displaySuccessMessages("errMsg2", "Document Title is required", "");
			clearSuccessMessageAfterFiveSecond("errMsg2");
			
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
				if($.inArray(ext, ['pdf','png','jpg','jpeg','doc']) == -1) {
					x=1;
					$('#upldoc'+index).focus();
					alert("Note: Only .pdf, .jpg, .png, & doc files will be allowed for uploading.!");
					return false;
				}
				
				var	fsize=$('#upldoc'+index)[0].files[0].size;
				var file = Math.round((fsize / 1024));
				if (parseInt(file) > 20480 || parseInt(file) < 100) {
					x=1;
					$('#upldoc'+index).focus();
					alert("File size should be greater then 100 kb & less then 20 MB!");
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
					document.getElementById('dydiv').style.height = "190px";
				}
			}
		}
	});
	
});

function geteditDD(status, piName, Dept, Desg, locationCode,ddoCode){
	try {
		//alert(status);
		if(status=="E" || status=="R"){
			getEmployee(piName);
			getDptByEmp(piName, Dept);
			getDesignByEmployeeAndDept(piName, Dept, Desg);
			getDdoDetailbyLocation(locationCode,ddoCode);
		}else if(user_status=="U"){
			getDdoDetailbyLocation(locationCode,ddoCode);
		}
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

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
		}else if($('#piId').val()==''){
			$('#piId').focus();
			showerr($("#piId")[0], "Principal Investigator is required!","block");			
			return false;
		}else if($('#deptId').val()==''){
			$('#deptId').focus();
			showerr($("#deptId")[0], "Department is required!","block");			
			return false;
		}else if($('#desId').val()==''){
			$('#desId').focus();
			showerr($("#desId")[0], "Designation is required!","block");			
			return false;
		}else if($('#XTODATE').val()==''){
			$('#XTODATE').focus();
			showerr($("#XTODATE")[0], "Date of Completion is required!","block");			
			return false;
		}else if($('#RelvInfo').val()==''){
			$('#RelvInfo').focus();
			showerr($("#RelvInfo")[0], "Details is required!","block");			
			return false;
		}
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function saveAndSubmit(val){
	try {
		if(validateFormFields()){
			var form_data = new FormData();
			var workarray = [];
			var exp_index = $("#index").val();
				
			for(i=1; i<=parseInt(exp_index); i++){
				if($("#upldoc"+i).val()!="" && $("#upldoc"+i).val()!=undefined)
				{						
					var ext = $("#upldoc"+i).val().split('.').pop().toLowerCase();
					if($.inArray(ext, ['pdf','png','jpg','jpeg','doc']) == -1) {						
						$('#upldoc'+i).focus();
						alert("Note: Only .pdf, .jpg, .png, & doc files will be allowed for uploading.!");
						return false;
					}
					var	fsize=$('#upldoc'+i)[0].files[0].size;
					var file = Math.round((fsize / 1024));
					if (parseInt(file) > 20480 || parseInt(file) < 100) {
						$('#upldoc'+i).focus();
						alert("File size should be greater then 100 kb & less then 20 MB!");
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
				
			var piId = $('#piId').val();			
			var	dept = $('#deptId').val();
			var desg = $('#desId').val();
			var XTODATE = $('#XTODATE').val();
			var	RelvInfo = $('#RelvInfo').val();
			var id = $('#mid').val();
			var locationCode = $('#Xlocation').val();
			var ddoCode = $('#Xddo').val();
			
			var piAccClose ="", blncRefFundAgn="", submToFundAgn="", submToProjCell="", techRepsubmToFundAgn="";
			if ($('#piAccClose').prop("checked")){
				piAccClose="Y";
			}else{
				piAccClose="N";
			}
			if ($('#blncRefFundAgn').prop("checked")){
				blncRefFundAgn="Y";
			}else{
				blncRefFundAgn="N";
			}
			if ($('#submToFundAgn').prop("checked")){
				submToFundAgn="Y";
			}else{
				submToFundAgn="N";
			}
			if ($('#submToProjCell').prop("checked")){
				submToProjCell="Y";
			}else{
				submToProjCell="N";
			}
			if ($('#techRepsubmToFundAgn').prop("checked")){
				techRepsubmToFundAgn="Y";
			}else{
				techRepsubmToFundAgn="N";
			}
				
			var jsonObject={"piId":piId, "dept":dept, "desg":desg, "XTODATE":XTODATE, "RelvInfo":RelvInfo, "piAccClose":piAccClose, 
					"blncRefFundAgn":blncRefFundAgn, "submToFundAgn":submToFundAgn,"submToProjCell":submToProjCell, 
					"techRepsubmToFundAgn":techRepsubmToFundAgn, "workarray": JSON.stringify(workarray), "mode":val, "fId":id,"locationCode":locationCode,"ddoCode":ddoCode};
			console.log("Project Completion save json");
			console.log(jsonObject);
			var encData=encAESData($("#AESKey").val(), jsonObject);	
			
			$("#btnSave").hide();
			$("#btnReset").hide();
			$("#btnSearch").hide();
			$("#btnUpdate").hide();
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../ProjectCompletionService?fstatus=S", true);			    
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
							$("#btnSearch").show();
							$("#btnReset").show();
							$("#btnUpdate").show();
							if(data.mode=='S'){
								setTimeout(function() {
									location.reload();
								}, 2000);	
							}else{
								setTimeout(function() {
									document.location.href="form2_project_completion_e.jsp?fstatus=S";
								}, 4000);
							}
						}else{
							displaySuccessMessages("errMsg2", data.msg, "");
							clearSuccessMessageAfterFiveSecond("errMsg2");
							$("#btnSave").show();
							$("#btnSearch").show();
							$("#btnReset").show();
							$("#btnUpdate").show();
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
				url: "../ProjectCompletionService?fstatus=D",
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

function editRecord(id,project){
	try {
		$('#frmF1ProjectSubmissionL').attr('action', 'form2_project_completion_e.jsp?fstatus=E&id='+id+"&project="+project);
		$("#frmF1ProjectSubmissionL").attr("target", '_parent');
		$("#frmF1ProjectSubmissionL" ).submit();
	} catch (err) {
		alert("ERROR :"+err);
	}
}

function getDptByEmp(emp, deptId){
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
}

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

function deletesavefile(attid, fliename, mastid, id,name){
	
	var retVal = confirm("File will be deleted permanently. \nAre you sure you want to remove this file?");
	if(retVal == true) {
		try{
			$.ajax({
				type: "POST",
				async: false,
				url: "../ProjectCompletionService",
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

function getEmpFromForm1(empid){
	try {
		var locationCode = $('#Xlocation').val();
		var ddoCode = $('#Xddo').val();
		$.ajax({
			type: "POST",
			url: "../ProjectCompletionService",
			data:{"fstatus":"3","location":locationCode,"ddo":ddoCode,"empid":empid},
			async: false,
			success: function (response){
				var employee=response.employee;
				console.log("response.employee");
				console.log(employee);
				var emp="<option value=''>Select Principal Investigator</option>";
				$.each(employee, function(index, employee) {
					if(empid!=""){
						var emptest=employee.employeeId;
						if (emptest && typeof emptest === 'string') {
						if(emptest.split("~")[0]==empid){
							emp+="<option value='"+employee.employeeId+"'> "+employee.employeeName+"</option>";
						}else{
							emp+="<option  value='"+employee.employeeId+"'> "+employee.employeeName+"</option>";
						}
						 }
					}else{
						emp+="<option  value='"+employee.employeeId+"'> "+employee.employeeName+"</option>";
					}
				});
				$("#piId").html(emp);
			}
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

function setOrder(id) {
	try {
		alert(id);
		alert(id.value);
		var select = $('#piId');
		select.html(select.find('option').sort(function(x, y) {
			// to change to descending order switch "<" for ">"
			return $(x).text() > $(y).text() ? 1 : -1;
		}));
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e)
	}
	
}