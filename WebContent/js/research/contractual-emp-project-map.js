//ContEmpProjMap
$(document).ready(function(){
	$("#btnReset").click(function(){
		location.reload();
	});
	
	$("#btnReset1").click(function(){
		document.getElementById("frmContEmpProjMapD").reset();
	});

	$('#btnBack').click(function(){		  
		document.location.href="contractual_emp_project_map_e.jsp?fstatus=N";
	});
	
	$("#Xlocation").change(function(){	
		var locationcode = $('#Xlocation').val();
		getDdoDetailbyLocation(locationcode);
	});
	
	$("#btnSearch").click(function(){
		var proj=$("#projS").val();
		var emp=$("#CntEmpS").val();	
		var locationCode = $('#Slocation').val(); var ddoCode = $('#Sddo').val();
		$('#frmContEmpProjMapE').attr('target', 'btmfrmContEmpProjMapD');
		$('#frmContEmpProjMapE').attr('action', 'contractual_emp_project_map_l.jsp?fstatus=V&proj='+proj+'&emp='+emp+'&locationCode='+locationCode+'&ddoCode='+ddoCode);
		$("#frmContEmpProjMapE" ).submit();		
	});	

	$("#addMore").click(function(){
		if(validateFormFields()){
			var proj = $("#proj option:selected").val();
			var projName = $("#proj option:selected").text();
			var sel_emp = [], sel_empN = [];
			
			$(".emp:checked").each(function(){
				sel_emp.push(this.value);
				sel_empN.push($('input[id=empId_'+this.value+']').parent().text());
			});
			
			/*$(".emp:checked").each(function(){

				alert("test 1 :"+JSON.stringify(this.value));
				alert("test 2 :"+JSON.stringify(document.getElementById('empId_'+this.value).childNodes));
				alert("test 3 :"+JSON.stringify(document.getElementById('empId_'+this.value).childNodes.item(0)));				
				alert("test 4 :"+document.getElementById('empId_'+this.value).childNodes.item(0).nodeValue);

				alert("test 5 :"+
						$("input[id=empId_"'"'+this.value+'"'"]:checked")
						);
				//alert("test 5 :"+$("input[id=empId_"'"'+this.value+'"'"]:checked").val());
				//alert("test 5 :"+$("input[id=empId_"'"'+this.value+'"'"]:checked").val());

				//alert("test 5 :"+$("input[class=emp]:checked").parent().text());
				//alert("test 5 :"+$("#empId_'+this.value+':checked").val()); empId_
				//alert("test 5 :"+$("input[id=empId_"'"'+this.value+'"'"]:checked").val());
			});
*/			
			/*alert("test 1 :"+sel_emp.length);
			var cEmp = sel_emp.toString();
			var cEmpName = sel_emp.toString();
			alert("test 1 :"+cEmp);*/			
			//var cEmp = $("#CntEmp option:selected").val();
			//var cEmpName = $("#CntEmp option:selected").text();
			
			var index=parseInt($('#index').val());
			
			
			for (var i = 1; i < index; i++) {
				if(proj==$('#projId'+i).val()){
					if(sel_emp.length > 0){
						for(var k=0; k<sel_emp.length; k++){
							if(sel_emp[k]==$('#empId'+i).val()){
								displaySuccessMessages("errMsg2", "Employee	:"+sel_empN[k]+" Already Exist", "");
								clearSuccessMessageAfterFiveSecond("errMsg2");
								return false;
								break;
							}
						}
					}
				}
			}			
			if(sel_emp.length > 0){
				for(var k=0; k<sel_emp.length; k++){
					index=index+1;
					var newRow = $("<tr>");			
					var cols = '<tr >'
						+'<td class="text-center">'+index+'</td>'
						+'<td class="text-center"><input type="hidden" class="form-control" id="projId'+index+'" name="projId'+index+'" value="'+proj+'">'+projName+'</td>'
						+'<td class="text-center"><input type="hidden" class="form-control" id="empId'+index+'" name="empId'+index+'" value="'+sel_emp[k]+'">'+sel_empN[k]+'</td>'
						+'<td style="text-align:center; color:RED; cursor:pointer; width:5%;" onclick="deletedata('+index+',this)"><i class="fa fa-trash-o"></i>Delete</td>';
					$('#searchTable').append(cols);
				}
			}
			
			//var proj = $("#proj").val("");
			//var cEmp = $("#CntEmp").val("");
			$('.emp').prop('checked', false); // Unchecks it
			$("#index").val(index);
			
			var rowcountAfterDelete = document.getElementById("searchTable").rows.length;
			if(rowcountAfterDelete==5){
				document.getElementById('dydiv').style.height = "190px";
			}
		
		}else{
			return false;
		}
	});
	
	$('#btnSave').click(function(){
		if(validateFormFields()){
			//var x = parseInt($('#index').val());
			//if(x >= 1 ){
				try {
					/*var workarray = [];
					for (var a=1; a<=x; a++) {
						var proj = $("#projId"+a).val();
						var cEmp = $("#empId"+a).val();
						
						var mapedJson={projId: proj, cEmpId: cEmp}
						workarray.push(mapedJson);
					}*/
					
					var proj = $("#proj option:selected").val();
					var sel_emp = [];
					$(".emp:checked").each(function(){
						sel_emp.push(this.value);
					});
					var locationCode = $('#Xlocation').val();
					var ddoCode = $('#Xddo').val();
					var natureType = $('#XnatureType').val();
					var jsonObject={"workarray": JSON.stringify(sel_emp), mode: "S", "proj": proj,"locationCode":locationCode,"ddoCode":ddoCode,"natureType":natureType};
					console. log("jsonObject for save");
					console.log(jsonObject);
					var encData=encAESData($("#AESKey").val(), jsonObject);	
					$('#addMore').hide();
					$('#btnSave').hide();
					$('#btnReset').hide();
					
					$.ajax({
						type: "POST",
						url: "../ContractualEmpProjectMapService",
						data: {encData: encData, fstatus: "S"},
						dataType: "json",
						success: function (response) {
							var data= decAESData($("#AESKey").val(), response);
							$('#btnSave').show();
							$('#btnReset').show();
							$('#addMore').show();
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
							
							$('#addMore').show();
							$('#btnSave').show();
							$('#btnReset').show();
						}
					});
				} catch (e) {
					// TODO: handle exception
					alert("ERROR :"+e);
				}
			/*}else{
				displaySuccessMessages("errMsg2", "At least 1 rows should be Add in the table", "");
				clearSuccessMessageAfterFiveSecond("errMsg2");
				return false;
			}*/
		}
	});
	
	$('#btnUpdate').click(function(){
		if(validateFormFields()){
			try {			
				
				var sel_emp = [];
				$(".emp:checked").each(function(){
					sel_emp.push(this.value);
				});
				
				var jsonObject={"workarray": JSON.stringify(sel_emp), "proj": $("#proj").val(), "id": $("#id").val(), mode: "U","natureType":$('#XnatureType').val()};
				console.log("jsonObject while update >>>>");
				console.log(jsonObject);
				var encData=encAESData($("#AESKey").val(), jsonObject);
				$('#addMore').hide();
				$('#btnSave').hide();
				$('#btnReset').hide();
				$('#btnUpdate').hide();
				
				$.ajax({
					type: "POST",
					url: "../ContractualEmpProjectMapService",
					data: {encData: encData, fstatus: "S"},
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
								//location.reload();
								document.location.href="contractual_emp_project_map_e.jsp?fstatus=N";
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
				alert("ERROR :"+e);
			}
		}
	});

});

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
		}
		else if($('#proj').val()==''){
			$('#proj').focus();
			showerr($("#proj")[0], "Project is required!","block");			
			return false;
		}else if($('#XnatureType').val()==''){
			$('#XnatureType').focus();
			showerr($("#XnatureType")[0], "Nature Type is required!","block");			
			return false;
		}else if(!$('.emp [type="checkbox"]').is(':checked')){
			showerr($(".emp")[0], "Please Check atleast one CheckBox!", "block");
			return false;
		}
		
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function deletedata(id,did){	
	var rowcountAfterDelete = document.getElementById("searchTable").rows.length;
	if(rowcountAfterDelete!=1){
		$(did).parents("tr").remove();
		$("#index").val($("#index").val()-1);
	}else{
		displaySuccessMessages("errMsg2", "At least 1 rows should be present in the table", "");
		clearSuccessMessageAfterFiveSecond("errMsg2");
	}
	if(rowcountAfterDelete==4){
		document.getElementById('dydiv').style.height = "auto";
	}
}

function getEmployee(empid){
	var hrmsApi = $("#hrmsApi").val().trim();
	var sel="", chk="", dis="";
	try {
		$.ajax({
			type: "GET",
			url: hrmsApi+"rest/apiServices/getEmployee",
			jsonp: "parseResponse",
			dataType: "jsonp",
			success: function (response){
				console.log("getEmployee response");
				console.log(response);
				var employee=response.employee;
				//var dropdown="<option value=''>Select Employee</option>";
				var dropdown = '<ul class="form-control" style="height: 85px; padding-top:0px;overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;"> <li>List Of Employee(s)</li>';
				$.each(employee, function(index, employee) {
					
					if(empid.indexOf(employee.employeeId) !== -1) {
						chk="checked";
						dis="disabled";
					} else {
						chk="";
						dis="";
					}
					
					/*if(employee.employeeId==empid){
						sel="selected";
						chk="checked";
					} else {
						sel="";
						chk="";
					}*/					
					dropdown +='<li><input type="checkbox"  id="empId_'+employee.employeeId+'" class="emp" value="'+employee.employeeId+'" '+chk+' '+dis+'> '+employee.employeeName+'</li>';	                  
					//dropdown+="<option  value='" + employee.employeeId + "'"+sel+">" + employee.employeeName+"</option>";
				});
				
				$(".emp").html(dropdown+'</ul>');
			}
		});	
	} catch (e) {
		alert(e);
	}
}

function deleteRecord(id){	
	try {	
		var r = confirm("Are You Sure!");
		if (r == true){
			var jsonObject={"id":id};
			$.ajax({
				type: "POST",
				url: "../ContractualEmpProjectMapService?fstatus=D",
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

function editRecord(id, pId, eId,fstatus,nature_type,locationcode,ddoid){
	try {
		$('#frmContEmpL').attr('action', 'contractual_emp_project_map_e.jsp?fstatus=E&id='+id+"&pId="+pId+"&eId="+eId+"&nType="+nature_type+"&locationCode="+locationcode+"&ddoCode="+ddoid);
		$("#frmContEmpL").attr("target", '_parent');
		$("#frmContEmpL" ).submit();
	} catch (err) {
		alert("ERROR :"+err);
	}
}

function getDropDown(val){
	try {
		var data="";
		
		$.ajax({
			type: "POST",
			url: "../ContractualEmpProjectMapService",
			data:{"fstatus": "RP", "id": val},
			async: false,
			success: function (response){
				if (typeof response.eid != 'undefined' && response.eid.length > 0){
					data = response.eid;	
                }else{
                	data = "";
                }
			}
		});
		if(data!="")
			getEmployee(data);
		else
			getEmployee("");
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+err);
	}
}
function getEmployeeByLocDDo(empid,locationCode,ddoCode,naturetype){
	var hrmsApi = $("#hrmsApi").val().trim();
	if(locationCode==''){var locationCode = $('#Xlocation').val(); var ddoCode = $('#Xddo').val();}
	var sel="", chk="", dis="";
	var json={"location":locationCode,"ddo":ddoCode,"naturetype":naturetype};
	console.log("json");
	console.log(json);
	try {
		$.ajax({
			type: "GET",
			url: hrmsApi+"rest/apiServices/getEmployee",
			jsonp: "parseResponse",
			dataType: "jsonp",
			data: json,
			success: function (response){
				console.log("getEmployeeByLocDDo response");
				console.log(response);
				var employee=response.employee;
				//var dropdown="<option value=''>Select Employee</option>";
				var dropdown = '<ul class="form-control" style="height: 85px; padding-top:0px;overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;"> <li>List Of Employee(s)</li>';
				$.each(employee, function(index, employee) {
					
					if(empid.indexOf(employee.employeeId) !== -1) {
						chk="checked";
						dis="disabled";
					} else {
						chk="";
						dis="";
					}				
					dropdown +='<li><input type="checkbox"  id="empId_'+employee.employeeId+'" class="emp" value="'+employee.employeeId+'" '+chk+' '+dis+'> '+employee.employeeName+'</li>';	                  
					//dropdown+="<option  value='" + employee.employeeId + "'"+sel+">" + employee.employeeName+"</option>";
				});
				
				$(".emp").html(dropdown+'</ul>');
			}
		});	
	} catch (e) {
		alert(e);
	}
}



function getProjectDetailbyLocationmain(pId,locationCode,ddoCode,obj){
		if(obj=='projS'){
			var locationCode = $('#Slocation').val(); var ddoCode = $('#Sddo').val();
			}else{
			var locationCode = $('#Xlocation').val(); var ddoCode = $('#Xddo').val();	
			}
		/*getProjectDetailbyLocation(pId,locationCode,ddoCode,obj);*/
		getResearchProposal(locationCode,ddoCode);
}

/*
 * Created by Amit Dangi on 23-11-2022
 * to get the Project Details by locationcode
 * */

function getNatureDetail(naturetypeid){
	try {
		var hrmsApi = $("#hrmsApi").val().trim();
		var sel="", chk="", dis="";
		var json={};
		console.log("getNatureDetail");
		console.log(json);
		
		$.ajax({
				type: "GET",
				url: hrmsApi+"rest/apiServices/getNatureDetail",
				jsonp: "parseResponse",
				dataType: "jsonp",
				data: json,
				success: function (response){
					console.log("getNatureDetail response");
					console.log(response);
					var employee=response.NatureDetail;
					var dropdown="<option value=''>Select Nature Type</option>";
					
					$.each(employee, function(index, employee) {
						
						if(naturetypeid==employee.nature_id) {
							sel="selected";
							dis="disabled";
						} else {
							sel="";
							dis="";
						}				
						dropdown+="<option  value='" + employee.nature_id + "'"+sel+">" + employee.nature+"</option>";
					});
					
					$("#XnatureType").html(dropdown);
				}
			});	
		} catch (e) {
			alert(e);
		}
}
