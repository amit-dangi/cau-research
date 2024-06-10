$(document).ready(function(){
	$("#btnReset").click(function(){
		location.reload();
	});
	
	$("#PiName").change(function(){			
		var emp = $(this).find('option:selected').val();
		if(emp!='')
			getDptByEmp(emp, "");
	});
	
	$("#deptId").change(function(){			
		var deptId = $(this).find('option:selected').val();
		var emp = $("#PiName").val();
		if(emp!='' && deptId!='')
			getDesignByEmployeeAndDept(emp, deptId, "");
	});	
	
	$('#btnPdf').click(function() {
		try {
			var empId = $("#PiName").val();
			if (empId.trim() == "" || empId.trim() == null) {
				$("#PiName").focus();
				showerr($("#PiName")[0], "Name of the Principal Investigator is required", "block");
				return false;
			}else if($('#deptId').val()==''){
				$('#deptId').focus();
				showerr($("#deptId")[0], "Department is required!","block");			
				return false;
			}else if($('#desId').val()==''){
				$('#desId').focus();
				showerr($("#desId")[0], "Designation is required!","block");			
				return false;
			}
			
			$("#fDate").val($("#XFROMDATE").val());
			$("#tDate").val($("#XTODATE").val());
			$("#empId").val($("#PiName option:selected").val());
			$("#dept").val($("#deptId option:selected").val());
			$("#des").val($("#desId option:selected").val());
			
			$("#frmRsrchPropReportD").attr("target", "btmfrmRsrchPropReportD");
			$("#frmRsrchPropReportD").attr("action", "research_proposal_details_report_2pdf.jsp");
			$("#frmRsrchPropReportD").submit();
			
		} catch (e) {
			// TODO: handle exception
			alert("ERROR :"+e);
		}
		
		//$('#loading').show();
		 /* $("#college_name").val($("#sel_college option:selected").text());
		  $("#semester").val($("#sel_pgm_cyl option:selected").text());
		  $("#programme_name").val($("#sel_pgm option:selected").text());
		  $("#year").val($("#sel_session option:selected").text());
		  $("#sel_exam_config_name").val($("#sel_exam_config option:selected").text());
		  $("#regulation").val($("#sel_reg_id option:selected").text());
		  $("#hallTicket").val();

		  if (validateForm()) {
		  $("#frmadmitcardD").attr("action", "admit_card_report.jsp");
		  $("#frmadmitcardD").submit();
		  }*/
	});
	
});

function getEmployee(empid){
	try {
		var hrmsApi = $("#hrmsApi").val().trim();
		var json={};		
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

function revertBack(){
	setTimeout(function () {
		displaySuccessMessages("errMsg", "Sorry! No Record Found for the selected combination.", "");
    }, 1000);
	clearSuccessMessageAfterFiveSecond("errMsg");
}