/**
 * AMIT DANGI
 */
$(document).ready(function(){
	$(".hidediv").hide();
	$("#btnReset, #btnReset1, #btnReset2").click(function(){
		location.reload();
	});
	
	//Function for upload the Target/Achivements certificate on bases of selected reserach proposal id
 	$('#btnSave').click(function(){
 		if(true){
 				var form_data = new FormData();
 				var workarray = [];
 				var exp_index = "0";
 				var is_upload_file="N";
 				var resPrpsId=$('#resPrps').val();
 			/*	if($("#upldoc1").val()=="" && $("#upldoc2").val()=="" && $("#upldoc3").val()=="" && $("#upldoc4").val()==""
 					&& $("#upldoc5").val()=="" && $("#upldoc6").val()=="" && $("#upldoc7").val()=="" && $("#upldoc8").val()==""
 						&& $("#upldoc9").val()=="" && $("#upldoc10").val()=="" && $("#upldoc11").val()=="" && $("#upldoc12").val()==""){
 						displaySuccessMessages("errMsg2", "At least one document should be uploaded", "");
				    	clearSuccessMessageAfterThreeSecond("errMsg2"); 
						return false;
 				}*/
 				
 				for(i=1; i<=parseInt(exp_index); i++){
 					if($("#upldoc"+i).val()!="" && $("#upldoc"+i).val()!=undefined){
 						is_upload_file="Y";
 						var ext = $("#upldoc"+i).val().split('.').pop().toLowerCase();
 						
 						if(ext !=""){
 							if($.inArray(ext, ['jpg','doc','docx']) == -1) {
 								if(i==7||i==8||i==11||i==12){
 									$('#upldoc'+i).focus();
 									alert("Note: Only jpg. files will be allowed. Max Size Cant Exceed 20MB.)!");
 	 								return false;
 								}else{
 								$('#upldoc'+i).focus();
 								alert("Note: Only .doc & docx files will be allowed for uploading.!");
 								return false;
 							}}
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
 				
 				 if($('#co_pi_count').val()>20){
 					$('#co_pi_count').focus();
 					showerr($("#co_pi_count")[0], "Number of Co-PI max value is 20 !","block");			
 					return false;
 				 }if($('#scientist_count').val()>20){
 					$('#scientist_count').focus();
 					showerr($("#scientist_count")[0], "Number of scientist max value is 20 !","block");			
 					return false;
 				 }if($('#ra_count').val()>20){
 					$('#ra_count').focus();
 					showerr($("#ra_count")[0], "Number of RA max value is 20 !","block");			
 					return false;
 				 }if($('#srf_count').val()>20){
 					$('#srf_count').focus();
 					showerr($("#srf_count")[0], "Number of SRF max value is 20 !","block");			
 					return false;
 				 }if($('#jrf_count').val()>20){
 					$('#jrf_count').focus();
 					showerr($("#jrf_count")[0], "Number of JRF max value is 20 !","block");			
 					return false;
 				 }if($('#tech_asisstant_count').val()>20){
 					$('#tech_asisstant_count').focus();
 					showerr($("#tech_asisstant_count")[0], "Number of Technical Assistant max value is 20 !","block");			
 					return false;
 				 }if($('#field_asisstant_count').val()>20){
 					$('#field_asisstant_count').focus();
 					showerr($("#field_asisstant_count")[0], "Number of Field Assistant max value is 20 !","block");			
 					return false;
 				 }if($('#lab_attendant_count').val()>20){
 					$('#lab_attendant_count').focus();
 					showerr($("#lab_attendant_count")[0], "Number of Lab/Field Attendant max value is 20 !","block");			
 					return false;
 				 }if($('#fieldman_count').val()>20){
 					$('#fieldman_count').focus();
 					showerr($("#fieldman_count")[0], "Number of Fieldman max value is 20 !","block");			
 					return false;
 				 }if($('#yp1_count').val()>20){
 					$('#yp1_count').focus();
 					showerr($("#yp1_count")[0], "Number of YP-I max value is 20 !","block");			
 					return false;
 				 }if($('#yp2_count').val()>20){
 					$('#yp2_count').focus();
 					showerr($("#yp2_count")[0], "Number of YP-II max value is 20 !","block");			
 					return false;
 				 }
 				var location_code=$('#Xlocation').val();
 				var ddo_id=$('#Xddo').val();
 				var ps_mid=$('#resPrps').val();
 				var ps_principal=$("#PiName").val();
 				var co_pi_count=$('#co_pi_count').val();
 				var scientist_count=$('#scientist_count').val();
 				var ra_count=$('#ra_count').val();
 				var srf_count=$('#srf_count').val();
 				
 				var jrf_count=$('#jrf_count').val();
 				var tech_asisstant_count=$('#tech_asisstant_count').val();
 				var field_asisstant_count=$("#field_asisstant_count").val();
 				var lab_attendant_count=$('#lab_attendant_count').val();
 				var fieldman_count=$('#fieldman_count').val();
 				var yp1_count=$('#yp1_count').val();
 				var yp2_count=$('#yp2_count').val();
 				var others_count=$('#others_count').val();
 				
 				var Details = [];
 				var grantreqrow = $("#grantreqrow").val();
 				for(var i = 1; i <= grantreqrow; i++){
 					if($("#field_count_" + i).val()>20){
 	 					$("#field_count_" + i).focus();
 	 					showerr($("#field_count_" + i)[0], "max value is 20 !","block");			
 	 					return false;
 	 				 }
 				if ($("#field_name_" +i).val() != "" && $("#field_name_" + i).val() != undefined) {
 					var arr = {
 							field_name   	: $("#field_name_" + i).val(), 
 							field_count  	: $("#field_count_" + i).val()
 					}
 				}
 				Details.push(arr);
 				}
 				
 				var jsonObject={"location_code":location_code,"ddo_id":ddo_id,"ps_mid":ps_mid,"ps_principal":ps_principal,
 								"ps_principal":ps_principal,"co_pi_count":co_pi_count,"scientist_count":scientist_count,"ra_count":ra_count,
 								"srf_count":srf_count,"jrf_count":jrf_count,"tech_asisstant_count":tech_asisstant_count,
 								"field_asisstant_count":field_asisstant_count,"lab_attendant_count":lab_attendant_count,
 								"fieldman_count":fieldman_count,"yp1_count":yp1_count,"yp2_count":yp2_count,
 								"others_count":others_count,"Details":Details};
 				
 				jsonobj=JSON.stringify(jsonObject);
 				var encData=encAESData($("#AESKey").val(), jsonObject);
 				console.log("jsonobj");
 				console.log(jsonobj);
 				var xmlHttp = new XMLHttpRequest();
 				xmlHttp.open("POST", "../StaffPositionSanctionPostService?fstatus=SAVE", true);
 				xmlHttp.setRequestHeader("encData", encData);
 				xmlHttp.send(form_data);
 				try{
 					xmlHttp.onreadystatechange = function() {
 						if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
 							
 							var data=JSON.parse(this.responseText);
 							var result=decAESData($("#AESKey").val(), data)
 							console.log("Response");
 							console.log(data);
 							    displaySuccessMessages("errMsg1", result.status, "");
 								$("#btnReseT").hide();
 								$("#btnSave").hide();
 								 setTimeout(function() {
 									location.reload();
 								}, 3000); 						
 						}
 					}
 				} catch (e) {
 					// TODO: handle exception
 					alert("ERROR :"+e);
 				}
 			}
 		});
	
	$("#Xlocation").change(function(){	
		var locationcode = $('#Xlocation').val();
		getDdoDetailbyLocation(locationcode);
	});
	
});

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
function getStaffPositionDetail(){
	var id = $("#resPrps").val();
	var hrmsApi = $("#hrmsApi").val().trim();
	
	//Validate file as click on view
	if($("#Xlocation").val() == ""){ 
 		$('#Xlocation').focus();	
			showerr($("#Xlocation")[0], "Location is required.","block");
			return false;	
	}else if($("#Xddo").val() == ""){ 
 		$('#Xddo').focus();	
			showerr($("#Xddo")[0], "DDO is required.","block");
			return false;	
	}else if($("#PiName").val() == ""){ 
 		$('#PiName').focus();	
		showerr($("#PiName")[0], "Name of the Principal Investigator is required.","block");
		return false;	
	}
	
	else if(id == ""){ 
 		$('#resPrps').focus();	
			showerr($("#resPrps")[0], "Project title is required.","block");
			return false;	
	}
	var selectedPiName = $("#PiName option:selected").text();
	try {
		$.ajax({
			type: "POST",
			url: "../StaffPositionSanctionPostService",
			data:{"fstatus":"DETAILS", "pi_id":id},
			async: false,
			success: function (response){
				$('#stable').html("");
				console.log("response");
				console.log(response.piIdlist);
				var details=response.piIdlist[0];
				var otrdetails=response.piIdlist[1];
				console.log("otrdetails||"+otrdetails.field_name_1);
				var cols ='<div style="padding:15px;">'
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Name of the project :</b></label>'
				+'<div class="col-sm-4" style="padding-top:7px;">'+details.PS_TITTLE_PROJ+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Name of the PI/Scientist In-Charge :</b></label>'
				+'<div class="col-sm-4" style="padding-top:7px;">'+selectedPiName+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of Co-PI:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="co_pi_count" name="co_pi_count" value="'+details.co_pi_count+'"  placeholder="Number of Co-PI" >'
				+'</div>' 
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of scientist:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="scientist_count" name="scientist_count" value="'+details.scientist_count+'"  placeholder="Number of scientist" >'
				+'</div>'
				
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of RA:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="ra_count" name="ra_count" value="'+details.ra_count+'"  placeholder="Number of RA" >'
				+'</div>' 
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of SRF:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="srf_count" name="srf_count" value="'+details.srf_count+'"  placeholder="Number of SRF" >'
				+'</div>'
				
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of JRF:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="jrf_count" name="jrf_count" value="'+details.jrf_count+'"  placeholder="Number of JRF" >'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of Technical Assistant:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="tech_asisstant_count" name="tech_asisstant_count" value="'+details.tech_asisstant_count+'"  placeholder="Number of Technical Assistant" >'
				+'</div>'
				
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of Field Assistant:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="field_asisstant_count" name="field_asisstant_count" value="'+details.field_asisstant_count+'"  placeholder="Number of Field Assistant" >'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of Lab/Field Attendant:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="lab_attendant_count" name="lab_attendant_count" value="'+details.lab_attendant_count+'"  placeholder="Number of Lab/Field Attendant" >'
				+'</div>'
				
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of Fieldman:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="fieldman_count" name="fieldman_count" value="'+details.fieldman_count+'"  placeholder="Number of Fieldman" >'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of YP-I:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="yp1_count" name="yp1_count" value="'+details.yp1_count+'"  placeholder="Number of YP-I" >'
				+'</div>'
				
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				
				+'<label class="col-sm-2 col-form-label text-left" for="">Number of YP-II:</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="2" class="form-control" id="yp2_count" name="yp2_count" value="'+details.yp2_count+'"  placeholder="Number of YP-II" >'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for="">Others (Please specify):</label>'
				+'<div class="col-sm-4">'
				+'<input type="text" maxlength="1" class="form-control" id="others_count" name="others_count" value="'+details.others_count+'" onblur="otherdetails(this.value);" placeholder="Others (Please specify)" >'
				+'</div>'
				
				+'</div>'
				+'</div>'
				+'</div>'
				+'<input type="hidden" id="grantreqrow" name="grantreqrow" value="" />'
				+'<div><tbody id="END1" class="dataEntryDiv grantsrequestedtable"></tbody></div>'
				
				+'</div>'
				$('#stable').append(cols);
				
				$(".hidediv").show();
				$("#detailstr").show();
				$("#detailstr2").show();
				$("#othersdet").show();
				
				otherdetails(details.others_count,otrdetails);
			}
			
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}


//add more Details of education details
	function otherdetails(rows,otrdetails) {
		console.log("otrdetails||"+JSON.stringify(otrdetails));
		$('#stable2').html("");
	    var counter	= parseInt(rows);
	    var DetTabData="";
	    for (j = 1; j <= counter; j++) {
	    	if(JSON.stringify(otrdetails)==undefined){
	    		DetTabData+='<tr id="rowid_'+j+'">'
				DetTabData+='<td style="text-align:center; width:5%; id="rowId_'+j+'">'+j+'</td>'
				DetTabData+='<td style="text-align:center; width:10%;"><input type="text" class="form-control" id="field_name_'+j+'" placeholder="Enter Name" value="" maxlength="25"></td>'
				DetTabData+='<td style="text-align:center; width:5%;"><input type="text" class="form-control" id="field_count_'+j+'" placeholder="Enter Number" value="" maxlength="2"></td>'
				DetTabData+='</tr>'	
	    	}else{
	    		var field_name=otrdetails["field_name_"+j];
				DetTabData+='<tr id="rowid_'+j+'">'
				DetTabData+='<td style="text-align:center; width:5%; id="rowId_'+j+'">'+j+'</td>'
				DetTabData+='<td style="text-align:center; width:10%;"><input type="text" class="form-control" id="field_name_'+j+'" placeholder="Enter Name" value="'+otrdetails["field_name_"+j]+'" maxlength="25"></td>'
				DetTabData+='<td style="text-align:center; width:5%;"><input type="text" class="form-control" id="field_count_'+j+'" placeholder="Enter Number" value="'+otrdetails["field_count_"+j]+'" maxlength="2"></td>'
				DetTabData+='</tr>'	
	    	}
	    	
	    }
	    console.log(DetTabData);
	    		$("#grantreqrow").val(counter);	
				$("#stable2").append(DetTabData);
	} 