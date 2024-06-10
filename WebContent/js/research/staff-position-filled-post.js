/**
 * AMIT DANGI
 */
$(document).ready(function(){
	$(".hidediv").hide();
	$("#btnReset, #btnReset1, #btnReset2").click(function(){
		location.reload();
	});
	
	$("#Xddo").change(function(){	
		var locationcode = $('#Xlocation').val();
		getResearchProposal(locationcode,this.value);
		getEmployee('','PiName');
	});
	
	//Function for upload the Target/Achivements certificate on bases of selected reserach proposal id
 	$('#btnSave').click(function(){debugger;
 		if(true){
 				var form_data = new FormData();
 				var coPiArray=[];
 				var scientistArray=[];
 				var raArray=[];
 				var srfArray=[];
 				var jrfArray=[];
 				var techAsisstantArray=[];
 				var fieldAsisstantArray=[];
 				var labAttendantArray=[];
 				var fieldmanArray=[];
 				var ypiArray=[];
 				var ypiiArray=[];
 				var otherArray=[];
 				
 				var coPiTotCnt=$("#co_pi_count").val();
 				var scientistTotCnt=$("#scientist_count").val();
 				var raTotCnt=$("#ra_count").val();
 				var srfTotCnt=$("#srf_count").val();
 				var jrfTotCnt=$("#jrf_count").val();
 				var techAsisstantTotCnt=$("#tech_asisstant_count").val();
 				var fieldAsisstantTotCnt=$("#field_asisstant_count").val();
 				var labAttendantTotCnt=$("#lab_attendant_count").val();
 				var fieldmanTotCnt=$("#fieldman_count").val();
 				var ypiTotCnt=$("#yp1_count").val();
 				var ypiiTotCnt=$("#yp2_count").val();
 				var otherTotCnt=$("#others_count").val();
 				
 				
 				var x=1;
 			//Number of Co-PI: array
 			   if(coPiTotCnt>0)
 				for(var i=1; i<=coPiTotCnt; i++){
					if($("#co_pi_"+i).val()!=''&& $("#co_pi_"+i).val()!=undefined){	
					  if(($("#co_pi_file_"+i).val()!='' && $("#co_pi_file_"+i).val()!=undefined) || ( 
						$("#co_pi_file_type_"+i).val() !='' && $("#co_pi_file_type_"+i).val()!=undefined) ){
						  var co_pi_file_type="";
						  if($("#co_pi_file_type_"+i).val() !='' && $("#co_pi_file_type_"+i).val()!=undefined)
							  co_pi_file_type=$("#co_pi_file_type_"+i).val();
						coPiArray.push({
							"co_pi"		    	: 		$("#co_pi_"+i).val(),
							"co_pi_type"		: 		$("#co_pi_type_"+i).val(),						                         
							"co_pi_file_type"	: 		co_pi_file_type,						                         
					    });
					
						if ($("#co_pi_file_"+i).val()!='' && $("#co_pi_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("co_pi_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("co_pi_file_"+x, document.getElementById("co_pi_file_"+i).files[j]);
							}
					    }
						++x;
				     }else{
				    	 $('#co_pi_file_'+i).focus();
						 showerr($("#co_pi_file_"+i)[0], "Document is required!","block");			
						 return false; 
				     }
				   }
			     }
 				
 			   x=1;
 			//Number of scientist array
 			   if(scientistTotCnt>0)
 				for(var i=1; i<=scientistTotCnt; i++){
					if($("#scientist_"+i).val()!=''&& $("#scientist_"+i).val()!=undefined){	
					 if(($("#scientist_file_"+i).val()!=''&& $("#scientist_file_"+i).val()!=undefined) || ( 
								$("#scientist_file_type_"+i).val() !='' && $("#scientist_file_type_"+i).val()!=undefined) ){
						  var scientist_file_type="";
						  if($("#scientist_file_type_"+i).val() !='' && $("#scientist_file_type_"+i).val()!= undefined)
							  scientist_file_type=$("#scientist_file_type_"+i).val();	
						scientistArray.push({
							"scientist"		    	: 		$("#scientist_"+i).val(),
							"scientist_type"		: 		$("#scientist_type_"+i).val(),
							"scientist_file_type"   :       scientist_file_type,
					    });
					
						if ($("#scientist_file_"+i).val()!='' && $("#scientist_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("scientist_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("scientist_file_"+x, document.getElementById("scientist_file_"+i).files[j]);
							}
					   }
						++x;
					 }else{
				    	 $('#scientist_file_'+i).focus();
						 showerr($("#scientist_file_"+i)[0], "Document is required!","block");			
						 return false; 
				     }
				    }
			     }
 			  x=1;
 			 //Number of RA: array
 			   if(raTotCnt>0)
 				for(var i=1; i<=raTotCnt; i++){
					if($("#ra_"+i).val()!='' && $("#ra_"+i).val()!=undefined){	
					  if(($("#ra_file_"+i).val()!=''&& $("#ra_file_"+i).val()!=undefined) || ( 
								$("#ra_file_type_"+i).val() !='' && $("#ra_file_type_"+i).val()!=undefined) ){
						  var ra_file_type="";
						  if($("#ra_file_type_"+i).val() !='' && $("#ra_file_type_"+i).val()!=undefined)
							  ra_file_type=$("#ra_file_type_"+i).val();	
						raArray.push({
							"ra"		    	: 		$("#ra_"+i).val(),
							"ra_type"			: 		$("#ra_type_"+i).val(),	
							"ra_file_type"      :       ra_file_type
					    });
					
						if ($("#ra_file_"+i).val()!='' && $("#ra_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("ra_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("ra_file_"+x, document.getElementById("ra_file_"+i).files[j]);
							}
					   }
						++x;
					  }else{
				    	 $('#ra_file_'+i).focus();
						 showerr($("#ra_file_"+i)[0], "Document is required!","block");			
						 return false; 
					  }
				    }
			     }
 			  x=1;
 			 //Number of SRF array
 			   if(srfTotCnt>0)
 				for(var i=1; i<=srfTotCnt; i++){
					if($("#srf_"+i).val()!='' && $("#srf_"+i).val()!=undefined){
					  if(($("#srf_file_"+i).val()!=''&& $("#srf_file_"+i).val()!=undefined) || ( 
								$("#srf_file_type_"+i).val() !='' && $("#srf_file_type_"+i).val()!=undefined) ){
						  var srf_file_type="";
						  if($("#srf_file_type_"+i).val() !='' && $("#srf_file_type_"+i).val()!=undefined)
							  srf_file_type=$("#srf_file_type_"+i).val();
						srfArray.push({
							"srf"		    	: 		$("#srf_"+i).val(),
							"srf_type"		    : 		$("#srf_type_"+i).val(),	
							"srf_file_type"     :       srf_file_type,
					    });
					
						if ($("#srf_file_"+i).val()!='' && $("#srf_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("srf_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("srf_file_"+x, document.getElementById("srf_file_"+i).files[j]);
							}
					   }
						++x;
					  }else{
					    	 $('#srf_file_'+i).focus();
							 showerr($("#srf_file_"+i)[0], "Document is required!","block");			
							 return false; 
					   }
				    }
			     }
 			  x=1;  
 			//Number of JRF array   
 			   if(jrfTotCnt>0)
 				for(var i=1; i<=jrfTotCnt; i++){
					if($("#jrf_"+i).val()!='' && $("#jrf_"+i).val()!=undefined){
					  if(($("#jrf_file_"+i).val()!=''&& $("#jrf_file_"+i).val()!=undefined) || ( 
								$("#jrf_file_type_"+i).val() !='' && $("#jrf_file_type_"+i).val()!=undefined) ){
						  var jrf_file_type="";
						  if($("#jrf_file_type_"+i).val() !='' && $("#jrf_file_type_"+i).val()!=undefined)
							  jrf_file_type=$("#jrf_file_type_"+i).val();
						jrfArray.push({
							"jrf"		    	: 		$("#jrf_"+i).val(),
							"jrf_type"			: 		$("#jrf_type_"+i).val(),
							"jrf_file_type"		:       jrf_file_type,
					    });
					
						if ($("#jrf_file_"+i).val()!='' && $("#jrf_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("jrf_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("jrf_file_"+x, document.getElementById("jrf_file_"+i).files[j]);
							}
					   }
						++x;
					  }else{
					    	 $('#jrf_file_'+i).focus();
							 showerr($("#jrf_file_"+i)[0], "Document is required!","block");			
							 return false; 
					   }
				    }
			     }
 			  x=1;
 			 //Number of Technical Assistant: array  
 			   if(techAsisstantTotCnt>0)
 				for(var i=1; i<=techAsisstantTotCnt; i++){
					if($("#tech_asisstant_"+i).val()!='' && $("#tech_asisstant_"+i).val()!=undefined){
					  if(($("#tech_asisstant_file_"+i).val()!=''&& $("#tech_asisstant_file_"+i).val()!=undefined) || ( 
								$("#tech_asisstant_file_type_"+i).val() !='' && $("#tech_asisstant_file_type_"+i).val()!=undefined) ){
						  var tech_asisstant_file_type="";
						  if($("#tech_asisstant_file_type_"+i).val() !='' && $("#tech_asisstant_file_type_"+i).val()!=undefined)
							  tech_asisstant_file_type=$("#tech_asisstant_file_type_"+i).val();	
						techAsisstantArray.push({
							"tech_asisstant"		    	: 		$("#tech_asisstant_"+i).val(),
							"tech_asisstant_type"		    : 		$("#tech_asisstant_type_"+i).val(),		
							"tech_asisstant_file_type"		:       tech_asisstant_file_type,
					    });
					
						if ($("#tech_asisstant_file_"+i).val()!='' && $("#tech_asisstant_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("tech_asisstant_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("tech_asisstant_file_"+x, document.getElementById("tech_asisstant_file_"+i).files[j]);
							}
					   }
						++x;
					  }else{
					    	 $('#tech_asisstant_file_'+i).focus();
							 showerr($("#tech_asisstant_file_"+i)[0], "Document is required!","block");			
							 return false; 
					   }
				    }
			     }
 			  x=1;
 			  //Number of Field Assistant: array 
 			   if(fieldAsisstantTotCnt>0)
 				for(var i=1; i<=fieldAsisstantTotCnt; i++){
					if($("#field_asisstant_"+i).val()!='' && $("#field_asisstant_"+i).val()!=undefined){
					  if(($("#field_asisstant_file_"+i).val()!=''&& $("#field_asisstant_file_"+i).val()!=undefined) || ( 
								$("#field_asisstant_file_type_"+i).val() !='' && $("#field_asisstant_file_type_"+i).val()!=undefined) ){
						  var field_asisstant_file_type="";
						  if($("#field_asisstant_file_type_"+i).val() !='' && $("#field_asisstant_file_type_"+i).val()!=undefined)
							  field_asisstant_file_type=$("#field_asisstant_file_type_"+i).val();	

						fieldAsisstantArray.push({
							"field_asisstant"		    	: 		$("#field_asisstant_"+i).val(),
							"field_asisstant_type"		    : 		$("#field_asisstant_type_"+i).val(),
							"field_asisstant_file_type"		:       field_asisstant_file_type,
					    });
					
						if ($("#field_asisstant_file_"+i).val()!='' && $("#field_asisstant_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("field_asisstant_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("field_asisstant_file_"+x, document.getElementById("field_asisstant_file_"+i).files[j]);
							}
					   }
						++x;
					  }else{
					    	 $('#field_asisstant_file_'+i).focus();
							 showerr($("#field_asisstant_file_"+i)[0], "Document is required!","block");			
							 return false; 
					   }
				    }
			     }
 			  x=1;	
 			//Number of Lab/Field Attendant: array   
 			   if(labAttendantTotCnt>0)
 				for(var i=1; i<=labAttendantTotCnt; i++){
					if($("#lab_attendant_"+i).val()!='' && $("#lab_attendant_"+i).val()!=undefined){
					  if(($("#lab_attendant_file_"+i).val()!=''&& $("#lab_attendant_file_"+i).val()!=undefined) || ( 
								$("#lab_attendant_file_type_"+i).val() !='' && $("#lab_attendant_file_type_"+i).val()!=undefined) ){
						  var lab_attendant_file_type="";
						  if($("#lab_attendant_file_type_"+i).val() !='' && $("#lab_attendant_file_type_"+i).val()!=undefined)
							  lab_attendant_file_type=$("#lab_attendant_file_type_"+i).val();

						labAttendantArray.push({
							"lab_attendant"		    	: 		$("#lab_attendant_"+i).val(),
							"lab_attendant_type"		: 		$("#lab_attendant_type_"+i).val(),
							"lab_attendant_file_type"	:       lab_attendant_file_type,
					    });
					
						if ($("#lab_attendant_file_"+i).val()!='' && $("#lab_attendant_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("lab_attendant_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("lab_attendant_file_"+x, document.getElementById("lab_attendant_file_"+i).files[j]);
							}
					   }
						++x;
					  }else{
					    	 $('#lab_attendant_file_'+i).focus();
							 showerr($("#lab_attendant_file_"+i)[0], "Document is required!","block");			
							 return false; 
					   }
				    }
			     }
 			  x=1;
 			//Number of Fieldman array   
 			   if(fieldmanTotCnt>0)
 				for(var i=1; i<=fieldmanTotCnt; i++){
					if($("#fieldman_"+i).val()!='' && $("#fieldman_"+i).val()!=undefined){
					  if(($("#fieldman_file_"+i).val()!=''&& $("#fieldman_file_"+i).val()!=undefined) || ( 
								$("#fieldman_file_type_"+i).val() !='' && $("#fieldman_file_type_"+i).val()!=undefined) ){
						  var fieldman_file_type="";
						  if($("#fieldman_file_type_"+i).val() !='' && $("#fieldman_file_type_"+i).val()!=undefined)
							  fieldman_file_type=$("#fieldman_file_type_"+i).val();	

						fieldmanArray.push({
							"fieldman"		    	: 		$("#fieldman_"+i).val(),
							"fieldman_type"		    : 		$("#fieldman_type_"+i).val(),
							"fieldman_file_type"	:       fieldman_file_type,
					    });
					
						if ($("#fieldman_file_"+i).val()!='' && $("#fieldman_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("fieldman_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("fieldman_file_"+x, document.getElementById("fieldman_file_"+i).files[j]);
							}
					   }
						++x;
					  }else{
					    	 $('#fieldman_file_'+i).focus();
							 showerr($("#fieldman_file_"+i)[0], "Document is required!","block");			
							 return false; 
					   }
				    }
			     }
 			  x=1;
 			// Number of YP-I array  
 			   if(ypiTotCnt>0)
 				for(var i=1; i<=ypiTotCnt; i++){
					if($("#yp1_"+i).val()!='' && $("#yp1_"+i).val()!=undefined){
				      if(($("#yp1_file_"+i).val()!=''&& $("#yp1_file_"+i).val()!=undefined) || ( 
								$("#yp1_file_type_"+i).val() !='' && $("#yp1_file_type_"+i).val()!=undefined) ){
						  var yp1_file_type="";
						  if($("#yp1_file_type_"+i).val() !='' && $("#yp1_file_type_"+i).val()!=undefined)
							  yp1_file_type=$("#yp1_file_type_"+i).val();		

						ypiArray.push({
							"yp1"		    	: 		$("#yp1_"+i).val(),
							"yp1_type"		    : 		$("#yp1_type_"+i).val(),
							"yp1_file_type"		:       yp1_file_type,
					    });
					
						if ($("#yp1_file_"+i).val()!='' && $("#yp1_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("yp1_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("yp1_file_"+x, document.getElementById("yp1_file_"+i).files[j]);
							}
					   }
						++x;
				      }else{
					    	 $('#yp1_file_'+i).focus();
							 showerr($("#yp1_file_"+i)[0], "Document is required!","block");			
							 return false; 
					   }
				    }
			     }
 			  x=1;  
 			// Number of YP-II array
 			  if(ypiiTotCnt>0)
 				for(var i=1; i<=ypiiTotCnt; i++){
					if($("#yp2_"+i).val()!='' && $("#yp2_"+i).val()!=undefined){
					   if(($("#yp2_file_"+i).val()!=''&& $("#yp2_file_"+i).val()!=undefined) || ( 
								$("#yp2_file_type_"+i).val() !='' && $("#yp2_file_type_"+i).val()!=undefined) ){
							  var yp2_file_type="";
							  if($("#yp2_file_type_"+i).val() !='' && $("#yp2_file_type_"+i).val()!=undefined)
								  yp2_file_type=$("#yp2_file_type_"+i).val();		

						ypiiArray.push({
							"yp2"		    	: 		$("#yp2_"+i).val(),
							"yp2_type"			: 		$("#yp2_type_"+i).val(),
							"yp2_file_type"		:       yp2_file_type,
					    });
					
						if ($("#yp2_file_"+i).val()!='' && $("#yp2_file_"+i).val()!=undefined){
							var fileCount = document.getElementById("yp2_file_"+i).files.length;
							for (j = 0; j < fileCount; j++) {
								form_data.append("yp2_file_"+x, document.getElementById("yp2_file_"+i).files[j]);
							}
					   }
						++x;
					   }else{
					    	 $('#yp2_file_'+i).focus();
							 showerr($("#yp2_file_"+i)[0], "Document is required!","block");			
							 return false; 
					   }
				    }
			     }
 			 x=1;
 			 var l=1;
 			  //Other array
 			  if(otherTotCnt>0)
 				for(var i=1; i<=otherTotCnt; i++){
 					
 					var other_total = $("#others_detail_count_"+i).val();
 					if(other_total>0){
 					for(var j=1;j<=other_total;j++){
							if($("#others_"+i+"_"+j).val()!='' && $("#others_"+i+"_"+j).val()!=undefined){	
								   if(($("#others_file_"+i+"_"+j).val()!=''&& $("#others_file_"+i+"_"+j).val()!=undefined) || ( 
											$("#others_file_type_"+i+"_"+j).val() !='' && $("#others_file_type_"+i+"_"+j).val()!=undefined) ){
										  var others_file_type="";
										  if($("#yp2_file_type_"+i).val() !='' && $("#others_file_type_"+i+"_"+j).val()!=undefined)
											  others_file_type=$("#others_file_type_"+i+"_"+j).val();	

								otherArray.push({
									"others"		    	: 		$("#others_"+i+"_"+j).val(),
									"others_type"		    : 		$("#others_type_"+i+"_"+j).val(),
									"others_file_type"		:       others_file_type,
							    });
							
								if ($("#others_file_"+i+"_"+j).val()!='' && $("#others_file_"+i+"_"+j).val()!=undefined){
									var fileCount = document.getElementById("others_file_"+i+"_"+j).files.length;
									for (k = 0; k < fileCount; k++) {
										form_data.append("others_file_"+x, document.getElementById("others_file_"+i+"_"+j).files[k]);
									}
							   }
								++x;
						   }else{
						    	 $("#others_file_"+i+"_"+j).focus();
								 showerr($("#others_file_"+i+"_"+j)[0], "Document is required!","block");			
								 return false; 
						   }
								
						    }
 					    }
 					   
 					 }
 					
			     }

 			  
 				var jsonObject={
 						ps_mid             	 	:		$('#resPrps').val(),
 						coPiList				:		coPiArray,
 						scientistList			:		scientistArray,
 						raList					:		raArray,
 						srfList					:		srfArray,
 						jrfList					:		jrfArray,
 						techAsisstantList		:		techAsisstantArray,
 						fieldAsisstantList		:		fieldAsisstantArray,
 						labAttendantList		:		labAttendantArray,
 						fieldmanList			:		fieldmanArray,
 						ypiList					:		ypiArray,
 						ypiiList				:		ypiiArray,
 						otherList				:		otherArray,
 					};
 				
 				
 				jsonobj=JSON.stringify(jsonObject);
 				var encData=encAESData($("#AESKey").val(), jsonObject);
 				console.log("jsonobj");
 				console.log(jsonobj);
 				var xmlHttp = new XMLHttpRequest();
 				xmlHttp.open("POST", "../StaffPositionFilledPostService?fstatus=SAVE", true);
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

function getEmployee(empid,obj){
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
				$("#"+obj).html(emp);
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
			url: "../StaffPositionFilledPostService",
			data:{"fstatus":"DETAILS", "pi_id":id},
			async: false,
			success: function (response){
				
				console.log("response");

				console.log(response.piIdlist);
				var details=response.piIdlist[0];
				var otrdetails=response.piIdlist[1];
				console.log("otrdetails||"+otrdetails.field_name_1);
				var co_pi_count= details.co_pi_count;
				var scientist_count=details.scientist_count;
				var ra_count=details.ra_count;
				var srf_count=details.srf_count;
				var jrf_count=details.jrf_count;
				var tech_asisstant_count=details.tech_asisstant_count;
				var field_asisstant_count=details.field_asisstant_count;
				var lab_attendant_count=details.lab_attendant_count;
				var fieldman_count=details.fieldman_count;
				var yp1_count=details.yp1_count;
				var yp2_count=details.yp2_count;
				var others_count=details.others_count;
				  $('#co_pi').html("");
				  $('#scientist').html("");
				  $('#ra').html("");
				  $('#srf').html("");
				  $('#jrf').html("");
				  $('#tec_ass').html("");
				  $('#fil_ass').html("");
				  $('#la_fi_att').html("");
				  $('#fil_man').html("");
				  $('#yp_i').html("");
				  $('#yp_ii').html("");
				  $('#other').html("");
				  $('#other_hide').html("");  
				  
				var directoryName=  $("#directoryName").val();
				var cols="";
				var prpf_id="";
				var type="";
				var type_name="";
				var file_name="";
				var ps_mid="";
				var array=[];
				var file="";
				if(co_pi_count >0 ){
				  $("#co_pi_hide").show();
				  for(var i=1;i<=co_pi_count;i++){
					  
					  for(var j=0;j<response.list.length;j++){
			                 if(response.list[j].type == 'co_pi_count' && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
			                	 type = response.list[j].type;
			                	 type_name = response.list[j].type_name;
			                	 prpf_id =response.list[j].pf_id;
			                	 file_name=response.list[j].file_name;
			                	 ps_mid=response.list[j].ps_mid;
			                	 file=response.list[j].file;			                	 
			                	 array.push(
			                			 prpf_id
			                	 );
			                	 break;
			                 }else{
			                	 type="";
			                 }
		                }
					 if(type == 'co_pi_count'){
						 
						cols +='<tr>'
						cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
						cols +='<td style="text-align:center; width:15%;"><select class="form-control" id="co_pi_'+i+'" name="">'
						cols +='<option value="">Select Principal Investigator</option></select>'
						cols +='<input type="hidden" id="co_pi_type_'+i+'" value="co_pi_count"></td>'
						cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload_'+i+'">'
						cols +='<input type="hidden" id="co_pi_file_type_'+i+'" value="'+file+'">'
						var co_pi_file_="'co_pi_file_'";
						var co_pi_type="'co_pi_'";
						var file_upload="'file_upload_'";
						cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
						cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
					    cols +='</tr>';
						$('#co_pi').append(cols);
						getEmployee(type_name,'co_pi_'+i);
						cols ="";
					 }else{
						cols +='<tr>'
						cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
						cols +='<td style="text-align:center; width:15%;"><select class="form-control" id="co_pi_'+i+'" name="">'
						cols +='<option value="">Select Principal Investigator</option></select>'
						cols +='<input type="hidden" id="co_pi_type_'+i+'" value="co_pi_count"></td>'
						cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
						cols +='<input type="file" class="form-control" id="co_pi_file_'+i+'" name=""></td>'
						cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
					    cols +='</tr>';
						$('#co_pi').append(cols);
						getEmployee('','co_pi_'+i);
						cols =""; 
				     }
				   }
				  $('#co_pi_post').text('Santioned Post Count-'+co_pi_count);
				}
				
				type="";
				type_name="";
				array=[];
				file_name="";
				prpf_id="";
				cols="";
				if(scientist_count >0 ){
					  $("#scientist_hide").show();
					  var pf_id="";
					  if(scientist_count >0 )
					  for(var i=1;i<=scientist_count;i++){
						  for(var j=0;j<response.list.length;j++){
				                 if(response.list[j].type == 'scientist_count' && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 type = response.list[j].type;
				                	 type_name = response.list[j].type_name;
				                	 file_name=response.list[j].file_name;
				                	 ps_mid=response.list[j].ps_mid;
				                	 file=response.list[j].file;			                	 
				                	 prpf_id =response.list[j].pf_id;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";
				                 }
			                }
						      if(type == 'scientist_count'){
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;"><select class="form-control" id="scientist_'+i+'" name="">'
									cols +='<option value="">Select Department</option></select>'
									cols +='<input type="hidden" id="scientist_type_'+i+'" value="scientist_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload1_'+i+'">' 
									cols +='<input type="hidden" id="scientist_file_type_'+i+'" value="'+file+'">'
									var co_pi_file_="'scientist_file_'";
									var file_upload="'file_upload1_'";
									var co_pi_type="'scientist_'";
									cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
									$('#scientist').append(cols);
									getEmployee(type_name,'scientist_'+i);
									cols ="";
							  }else{
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;"><select class="form-control" id="scientist_'+i+'" name="">'
									cols +='<option value="">Select Department</option></select>'
									cols +='<input type="hidden" id="scientist_type_'+i+'" value="scientist_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
									cols +='<input type="file" class="form-control" id="scientist_file_'+i+'" name=""></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
									$('#scientist').append(cols);
									getEmployee('','scientist_'+i);
									cols ="";
						       }
					  }
					  $('#scientist_post').text('Santioned Post Count-'+scientist_count);
				}
				
				type="";
				type_name="";
				file_name="";
				array=[];
				prpf_id="";
				cols="";
				if(ra_count >0 ){
					  $("#ra_hide").show();
					  for(var i=1;i<=ra_count;i++){
						  for(var j=0;j<response.list.length;j++){
				                 if(response.list[j].type == 'ra_count' && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 type = response.list[j].type;
				                	 prpf_id =response.list[j].pf_id;
				                	 file=response.list[j].file;			                	 
				                	 ps_mid=response.list[j].ps_mid;
				                	 type_name = response.list[j].type_name;
				                	 file_name=response.list[j].file_name;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";
				                 }
			                }
						   if(type == 'ra_count'){
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;">'
									cols +='<input type="text" class="form-control" id="ra_'+i+'" name="" value="'+type_name+'">'
									cols +='<input type="hidden" class="form-control" id="ra_type_'+i+'" value="ra_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload2_'+i+'">' 
									cols +='<input type="hidden" id="ra_file_type_'+i+'" value="'+file+'">'
									var co_pi_file_="'ra_file_'";
									var file_upload="'file_upload2_'";
									var co_pi_type="'ra_'";
									cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
						   }else{
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="ra_'+i+'" name="">'
								cols +='<input type="hidden" class="form-control" id="ra_type_'+i+'" value="ra_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
								cols +='<input type="file" class="form-control" id="ra_file_'+i+'" name=""></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						  }
					  }
					  $('#ra').append(cols);
					  $('#ra_post').text('Santioned Post Count-'+ra_count);
				}
				
				type="";
				type_name="";
				cols="";
				file_name="";
				prpf_id="";
				array = [];
				if(srf_count >0 ){
					  $("#srf_hide").show();
					  for(var i=1;i<=srf_count;i++){
						  
						  for(var j=0;j<response.list.length;j++){				 			 
				                 if(response.list[j].type.includes('srf_count')  && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 ps_mid=response.list[j].ps_mid;
				                	 type = response.list[j].type;
				                	 file_name=response.list[j].file_name;
				                	 file=response.list[j].file;			                	 
				                	 type_name = response.list[j].type_name;
				                	 prpf_id =response.list[j].pf_id;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";	 
				                 }
						     }
						   if(type == 'srf_count'){
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;">'
									cols +='<input type="text" class="form-control" id="srf_'+i+'" name="" value="'+type_name+'">'
									cols +='<input type="hidden" class="form-control" id="srf_type_'+i+'" value="srf_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload3_'+i+'">' 
									cols +='<input type="hidden" id="srf_file_type_'+i+'" value="'+file+'">'
									var co_pi_file_="'srf_file_'";
									var file_upload="'file_upload3_'";
									var co_pi_type="'srf_'";
									cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
						   }else{
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="srf_'+i+'" name="">'
								cols +='<input type="hidden" class="form-control" id="srf_type_'+i+'" value="srf_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
								cols +='<input type="file" class="form-control" id="srf_file_'+i+'" name=""></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						   }
					  }
					  $('#srf').append(cols);
					  $('#srf_post').text('Santioned Post Count-'+srf_count);
				}
				
				type="";
				type_name="";
				file_name="";
				array=[];
				prpf_id="";
				cols="";
				if(jrf_count >0 ){
					  $("#jrf_hide").show();
					  for(var i=1;i<=jrf_count;i++){
						  for(var j=0;j<response.list.length;j++){
				                 if(response.list[j].type == 'jrf_count'  && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 file_name=response.list[j].file_name;
				                	 type = response.list[j].type;
				                	 file=response.list[j].file;			                	 
				                	 prpf_id =response.list[j].pf_id;
				                	 ps_mid=response.list[j].ps_mid;
				                	 type_name = response.list[j].type_name;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";
				                 }
			                }
						      if(type == 'jrf_count'){
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;">'
									cols +='<input type="text" class="form-control" id="jrf_'+i+'" name="" value="'+type_name+'">'
									cols +='<input type="hidden" class="form-control" id="jrf_type_'+i+'" value="jrf_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload4_'+i+'">' 
									cols +='<input type="hidden" id="jrf_file_type_'+i+'" value="'+file+'">'
									var co_pi_file_="'jrf_file_'";
									var file_upload="'file_upload4_'";
									var co_pi_type="'jrf_'";
									cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
						   }else{
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="jrf_'+i+'" name="">'
								cols +='<input type="hidden" class="form-control" id="jrf_type_'+i+'" value="jrf_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
								cols +='<input type="file" class="form-control" id="jrf_file_'+i+'" name=""></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						   }
					  }
					  $('#jrf').append(cols);
					  $('#jrf_post').text('Santioned Post Count-'+jrf_count);
				}
				
				type="";
				type_name="";
				file_name="";
				array=[];
				prpf_id="";
				cols="";
				if(tech_asisstant_count >0 ){
					  $("#tec_ass_hide").show();
					  for(var i=1;i<=tech_asisstant_count;i++){
						  for(var j=0;j<response.list.length;j++){
				                 if(response.list[j].type == 'tech_asisstant_count' && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 prpf_id =response.list[j].pf_id;
				                	 file_name=response.list[j].file_name;
				                	 type = response.list[j].type;
				                	 ps_mid=response.list[j].ps_mid;
				                	 file=response.list[j].file;			                	 
				                	 type_name = response.list[j].type_name;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";
				                 }
			                }
						      if(type == 'tech_asisstant_count'){
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;">'
									cols +='<input type="text" class="form-control" id="tech_asisstant_'+i+'" name="" value="'+type_name+'">'
									cols +='<input type="hidden" class="form-control" id="tech_asisstant_type_'+i+'" value="tech_asisstant_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload5_'+i+'">' 
									cols +='<input type="hidden" id="tech_asisstant_file_type_'+i+'" value="'+file+'">'
									var co_pi_file_="'tech_asisstant_file_'";
									var file_upload="'file_upload5_'";
									var co_pi_type="'tech_asisstant_'";
									cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
						   }else{
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="tech_asisstant_'+i+'" name="">'
								cols +='<input type="hidden" class="form-control" id="tech_asisstant_type_'+i+'" value="tech_asisstant_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
								cols +='<input type="file" class="form-control" id="tech_asisstant_file_'+i+'" name=""></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						   }
					  }
					  $('#tec_ass').append(cols);
					  $('#tec_ass_post').text('Santioned Post Count-'+tech_asisstant_count);
				}
				
				type="";
				type_name="";
				file_name="";
				array=[];
				prpf_id="";
				cols="";
				if(field_asisstant_count >0 ){
					  $("#fil_ass_hide").show();
					  for(var i=1;i<=field_asisstant_count;i++){
						  for(var j=0;j<response.list.length;j++){
				                 if(response.list[j].type == 'field_asisstant_count' && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 type = response.list[j].type;
				                	 ps_mid=response.list[j].ps_mid;
				                	 file_name=response.list[j].file_name;
				                	 file=response.list[j].file;			                	 
				                	 prpf_id =response.list[j].pf_id;
				                	 type_name = response.list[j].type_name;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";
				                 }
			                }
						    if(type == 'field_asisstant_count'){
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="field_asisstant_'+i+'" name="" value="'+type_name+'">'
								cols +='<input type="hidden" class="form-control" id="field_asisstant_type_'+i+'" value="field_asisstant_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload6_'+i+'">' 
								cols +='<input type="hidden" id="field_asisstant_file_type_'+i+'" value="'+file+'">'
								var co_pi_file_="'field_asisstant_file_'";
								var file_upload="'file_upload6_'";
								var co_pi_type="'field_asisstant_'";
								cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						   }else{
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="field_asisstant_'+i+'" name="">'
								cols +='<input type="hidden" class="form-control" id="field_asisstant_type_'+i+'" value="field_asisstant_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
								cols +='<input type="file"  class="form-control" id="field_asisstant_file_'+i+'" name=""></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						   }
					  }
					  $('#fil_ass').append(cols);
					  $('#fil_ass_post').text('Santioned Post Count-'+field_asisstant_count);
				}
				
				type="";
				type_name="";
				array=[];
				file_name="";
				prpf_id="";
				cols="";
				if(lab_attendant_count >0 ){
					  $("#la_fi_att_hide").show();
					  for(var i=1;i<=lab_attendant_count;i++){
						  for(var j=0;j<response.list.length;j++){
				                 if(response.list[j].type == 'lab_attendant_count' && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 type = response.list[j].type;
				                	 ps_mid=response.list[j].ps_mid;
				                	 file=response.list[j].file;			                	 
				                	 file_name=response.list[j].file_name;
				                	 prpf_id =response.list[j].pf_id;
				                	 type_name = response.list[j].type_name;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";
				                 }
			                }
						    if(type == 'lab_attendant_count'){
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;">'
									cols +='<input type="text" class="form-control" id="lab_attendant_'+i+'" name="" value="'+type_name+'">'
									cols +='<input type="hidden" class="form-control" id="lab_attendant_type_'+i+'" value="lab_attendant_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload7_'+i+'">' 
									cols +='<input type="hidden" id="lab_attendant_file_type_'+i+'" value="'+file+'">'
									var co_pi_file_="'lab_attendant_file_'";
									var file_upload="'file_upload7_'";
									var co_pi_type="'lab_attendant_'";
									cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
						   }else{
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="lab_attendant_'+i+'" name="">'
								cols +='<input type="hidden" class="form-control" id="lab_attendant_type_'+i+'" value="lab_attendant_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
								cols +='<input type="file" class="form-control" id="lab_attendant_file_'+i+'" name=""></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						   }
					  }
					  $('#la_fi_att').append(cols);
					  $('#la_fi_att_post').text('Santioned Post Count-'+lab_attendant_count);
				}
				
				type="";
				type_name="";
				array=[];
				file_name="";
				prpf_id="";
				cols="";
				if(fieldman_count >0 ){
					  $("#fil_man_hide").show();
					  for(var i=1;i<=fieldman_count;i++){
						  for(var j=0;j<response.list.length;j++){
				                 if(response.list[j].type == 'fieldman_count' && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 ps_mid=response.list[j].ps_mid;
				                	 type = response.list[j].type;
				                	 file_name=response.list[j].file_name;
				                	 prpf_id =response.list[j].pf_id;
				                	 file=response.list[j].file;			                	 
				                	 type_name = response.list[j].type_name;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";
				                 }
			                }
						    if(type == 'fieldman_count'){
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;">'
									cols +='<input type="text" class="form-control" id="fieldman_'+i+'" name="" value="'+type_name+'">'
									cols +='<input type="hidden" class="form-control" id="fieldman_type_'+i+'" value="fieldman_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload8_'+i+'">' 
									cols +='<input type="hidden" id="fieldman_file_type_'+i+'" value="'+file+'">'
									var co_pi_file_="'fieldman_file_'";
									var file_upload="'file_upload8_'";
									var co_pi_type="'fieldman_'";
									cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
						   }else{
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="fieldman_'+i+'" name="">'
								cols +='<input type="hidden" class="form-control" id="fieldman_type_'+i+'" value="fieldman_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
								cols +='<input type="file" class="form-control" id="fieldman_file_'+i+'" name=""></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						   }
					  }
					  $('#fil_man').append(cols);
					  $('#fil_man_post').text('Santioned Post Count-'+fieldman_count);
				}
				
				file_name="";
				type="";
				type_name="";
				array=[];
				prpf_id="";
				cols="";
				if(yp1_count >0 ){
					  $("#yp_i_hide").show();
					  for(var i=1;i<=yp1_count;i++){
						  for(var j=0;j<response.list.length;j++){
				                 if(response.list[j].type == 'yp1_count' && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 type = response.list[j].type;
				                	 ps_mid=response.list[j].ps_mid;
				                	 prpf_id =response.list[j].pf_id;
				                	 file=response.list[j].file;			                	 
				                	 file_name=response.list[j].file_name;
				                	 type_name = response.list[j].type_name;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";
				                 }
			                }
						    if(type == 'yp1_count'){
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;">'
									cols +='<input type="text" class="form-control" id="yp1_'+i+'" name="" value="'+type_name+'">'
									cols +='<input type="hidden" class="form-control" id="yp1_type_'+i+'" value="yp1_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload9_'+i+'">' 
									cols +='<input type="hidden" id="yp1_file_type_'+i+'" value="'+file+'">'
									var co_pi_file_="'yp1_file_'";
									var file_upload="'file_upload9_'";
									var co_pi_type="'yp1_'";
									cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
						   }else{
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="yp1_'+i+'" name="">'
								cols +='<input type="hidden" class="form-control" id="yp1_type_'+i+'" value="yp1_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
								cols +='<input type="file" class="form-control" id="yp1_file_'+i+'" name=""></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						   }
					  }
					  $('#yp_i').append(cols);
					  $('#yp_i_post').text('Santioned Post Count-'+yp1_count);
				}
				
				type="";
				type_name="";
				array=[];
				file_name="";
				prpf_id="";
				cols="";
				if(yp2_count >0 ){
					  $("#yp_ii_hide").show();
					  for(var i=1;i<=yp2_count;i++){
						  for(var j=0;j<response.list.length;j++){
				                 if(response.list[j].type == 'yp2_count' && !JSON.stringify(array).includes(response.list[j].pf_id)){			                	 
				                	 prpf_id =response.list[j].pf_id;
				                	 file_name=response.list[j].file_name;
				                	 file=response.list[j].file;			                	 
				                	 type = response.list[j].type;
				                	 ps_mid=response.list[j].ps_mid;
				                	 type_name = response.list[j].type_name;
				                	 array.push(
				                			 prpf_id
				                	 );
				                	 break;
				                 }else{
				                	 type="";
				                 }
			                }
						    if(type == 'yp2_count'){
									cols +='<tr>'
									cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
									cols +='<td style="text-align:center; width:15%;">'
									cols +='<input type="text" class="form-control" id="yp2_'+i+'" name="" value="'+type_name+'">'
									cols +='<input type="hidden" class="form-control" id="yp2_type_'+i+'" value="yp2_count"></td>'
									cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload10_'+i+'">' 
									cols +='<input type="hidden" id="yp2_file_type_'+i+'" value="'+file+'">'
									var co_pi_file_="'yp2_file_'";
									var file_upload="'file_upload10_'";
									var co_pi_type="'yp2_'";
									cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
									cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+i+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
								    cols +='</tr>';
						   }else{
								cols +='<tr>'
								cols +='<td style="text-align:center; width:5%;">'+i+'</td>'
								cols +='<td style="text-align:center; width:15%;">'
								cols +='<input type="text" class="form-control" id="yp2_'+i+'" name="">'
								cols +='<input type="hidden" class="form-control" id="yp2_type_'+i+'" value="yp2_count"></td>'
								cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
								cols +='<input type="file" class="form-control" id="yp2_file_'+i+'" name=""></td>'
								cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
							    cols +='</tr>';
						   }
					  }
					  $('#yp_ii').append(cols);
					  $('#yp_ii_post').text('Santioned Post Count-'+yp2_count);
				}
				
				cols="";
				if(others_count >0 ){
					  $("#other_hide").show();
					  for(var i=1;i<=others_count;i++){
						  cols +='<div class="page-header-1 ">'
						  cols +='<div class="col-sm-4 repTitle h5 ">Other:(Number of '+otrdetails["field_name_"+i]+'-'+otrdetails["field_count_"+i]+')</div>'
						  cols +='</div>'
						  cols +='<div class="col-md-12 table-responsive" style="padding: 0px;display: inline-block;">'
						  cols +='<div id="" class="dataEntryDiv" style="padding: 0px;display: inline-block;">'
						  cols +='<table border="1" cellspacing="1" cellpadding="1" width="98%" class="tableGrid " id="MyTable" >'
						  cols +='<thead>'
						  cols +='<tr>'
						  cols +='<th style="text-align:center;">S.No.</th>'
						  cols +='<th style="text-align:center;">Name</th>'
						  cols +='<th style="text-align:center;">Upload Docs.</th>'
						  cols +='<th style="text-align:center;">Clear</th>'
						  cols +='</tr>'
						  cols +='</thead>'
						  cols +='<tbody id="" class="dataEntryDiv" >'
							type="";
							type_name="";
							array=[];
							file_name="";
							prpf_id="";
							  for(var j =1;j<=otrdetails["field_count_"+i];j++){
								  
								  for(var h=0;h<response.list.length;h++){
						                 if(response.list[h].type == otrdetails["field_name_"+i] && !JSON.stringify(array).includes(response.list[h].pf_id)){			                	 
						                	 type = response.list[h].type;
						                	 prpf_id =response.list[h].pf_id;
						                	 file=response.list[j].file;			                	 
						                	 ps_mid=response.list[h].ps_mid;
						                	 type_name = response.list[h].type_name;
						                	 file_name=response.list[h].file_name;
						                	 array.push(
						                			 prpf_id
						                	 );
						                	 break;
						                 }else{
						                	 type="";
						                 }
					                }
								    if(type == otrdetails["field_name_"+i]){
								    	cols +='<tr>'
											cols +='<td style="text-align:center; width:5%;">'+j+'</td>'
											cols +='<td style="text-align:center; width:15%;">'
											cols +='<input type="text" class="form-control" id="others_'+i+'_'+j+'" name="" value="'+type_name+'">'
											cols +='<input type="hidden" class="form-control" id="others_type_'+i+'_'+j+'" value="'+otrdetails["field_name_"+i]+'"></td>'
											cols +='<td class="addDocs" style="text-align:left; width:15%;"><div id="file_upload11_'+i+'_'+j+'">' 
											cols +='<input type="hidden" id="others_file_type_'+i+'_'+j+'" value="'+file+'">'
											var co_pi_file_="'others_file_"+i+"_'";
											var file_upload="'file_upload11_"+i+"_'";
											var co_pi_type="'others_"+i+"_'";
											cols +='<a target="_blank" class="form-control" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/STAFF_POSITION/'+ps_mid+'">'+file_name+'</a></div></td>'
											cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick="deleteFile('+j+','+co_pi_file_+','+file_upload+','+co_pi_type+');"><i class="fa "></i>Clear</span></td>'
										    cols +='</tr>';
								    }else{
								  
										cols +='<tr>'
										cols +='<td style="text-align:center; width:5%;">'+j+'</td>'
										cols +='<td style="text-align:center; width:15%;">'
										cols +='<input type="text" class="form-control" id="others_'+i+'_'+j+'" name="">'
										cols +='<input type="hidden" class="form-control" id="others_type_'+i+'_'+j+'" value="'+otrdetails["field_name_"+i]+'"></td>'
										cols +='<td class="addDocs" style="text-align:left; width:15%;">' 
										cols +='<input type="file" class="form-control" id="others_file_'+i+'_'+j+'" name=""></td>'
										cols +='<td class="colr-red-p" style="width:8%;"><span id="" onClick=""><i class="fa "></i>Clear</span></td>'
									    cols +='</tr>';
								    }
							  }	  
						  cols +='</tbody></table>'
						  cols +='<input type="hidden" id="others_detail_count_'+i+'" value="'+otrdetails["field_count_"+i]+'"></div></div>' 
					      $('#other_hide').append(cols);  
						  cols ="";
					  }
				}
					    $('#co_pi_count').val(co_pi_count);
					    $('#scientist_count').val(scientist_count);
					    $('#ra_count').val(ra_count);
					    $('#srf_count').val(srf_count);
					    $('#jrf_count').val(jrf_count);
					    $('#tech_asisstant_count').val(tech_asisstant_count);
					    $('#field_asisstant_count').val(field_asisstant_count);
					    $('#lab_attendant_count').val(lab_attendant_count);
					    $('#fieldman_count').val(fieldman_count);
					    $('#yp1_count').val(yp1_count);
					    $('#yp2_count').val(yp2_count);
					    $('#others_count').val(others_count);
					    $(".hidediv").show();
					//  $('#other_post').text('Santioned Post Count-'+others_count);
				
				
				
			}
			
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}
function deleteFile(id,index,file_upload,name){
		$("#"+name+id).val("");
		$("#"+file_upload+id).empty();
		$("#"+file_upload+id).append('<input type="file"  class="form-control"    id="'+index+id+'" title="Upload here" />');		

}