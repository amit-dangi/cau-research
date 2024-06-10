/**
 * @author Amit dangi
 */
$(document).ready(function(){
	$(".hidediv").hide();
	$("#btnReset, #btnReset1, #btnReset2").click(function(){
		location.reload();
	});
	
	//Function for upload the Target/Achivements certificate on bases of selected reserach proposal id
 	$('#btnSave').click(function(){
 		if(validateFormFields()){
 				var form_data = new FormData();
 				var workarray = [];
 				var exp_index = "13";
 				var is_upload_file="N";
 				var resPrpsId=$('#resPrps').val();
 				var TA_ID=$('#TA_ID').val();
 				if($("#upldoc1").val()==""  
 						/*&& $("#upldoc3").val()=="" && $("#upldoc4").val()==""
 					&& $("#upldoc5").val()=="" && $("#upldoc6").val()=="" && $("#upldoc7").val()=="" && $("#upldoc8").val()==""*/
 						&& $("#upldoc9").val()=="" && $("#upldoc10").val()=="" && $("#upldoc11").val()=="" && $("#upldoc12").val()=="" && $("#upldoc13").val()==""){
 						displaySuccessMessages("errMsg2", "At least one document should be uploaded", "");
				    	clearSuccessMessageAfterThreeSecond("errMsg2"); 
						return false;
 				}
 				
 				for(i=1; i<=parseInt(exp_index); i++){
 					if($("#upldoc"+i).val()!="" && $("#upldoc"+i).val()!=undefined){
 						is_upload_file="Y";
 						var ext = $("#upldoc"+i).val().split('.').pop().toLowerCase();
 						
 						if(ext !=""){
 							if($.inArray(ext, ['pdf','png','jpg','jpeg','doc','docx']) == -1) {
 								if(i==7||i==8||i==11||i==12||i==13){
 									$('#upldoc'+i).focus();
 									alert("Note: Only .jpg, .png files will be allowed. Max Size Cant Exceed 20MB.)!");
 	 								return false;
 								}else{
 								$('#upldoc'+i).focus();
 								alert("Note: Only .pdf, .jpg, .png & doc & docx files will be allowed for uploading.!");
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
 				
 				var TA_ID=$('#TA_ID').val();
 				var resPrpsId=$('#resPrps').val();
 				var finYr=$('#finYr').val();
 				var quarter=$('#quarter').val();
 				var XFROMDATE=$('#XFROMDATE').val();
 				var XTODATE=$("#XTODATE").val();
 				var rsrchcatQ=$('#rsrchcatQ').val();
 				var rsrchsubcatQ=$('#rsrchsubcatQ').val();
 				var rsrchcatY=$('#rsrchcatY').val();
 				var rsrchsubcatY=$('#rsrchsubcatY').val();
 				
 				/*Getting the values for achievements*/
 				var acv_quarter=$('#acv_quarter').val();
 				var XFROMDATE_ACV=$('#XFROMDATE_ACV').val();
 				var XTODATE_ACV=$("#XTODATE_ACV").val();
 				var acv_rsrchcatQ=$('#acv_rsrchcatQ').val();
 				var acv_rsrchsubcatQ=$('#acv_rsrchsubcatQ').val();
 				var acv_rsrchcatY=$('#acv_rsrchcatY').val();
 				var acv_rsrchsubcatY=$('#acv_rsrchsubcatY').val();
 				
 				var jsonObject={"TA_ID":TA_ID,"resPrpsId":resPrpsId,"finYr":finYr,"quarter":quarter,"XFROMDATE":XFROMDATE,
 								"XTODATE":XTODATE,"rsrchcatQ":rsrchcatQ,"rsrchsubcatQ":rsrchsubcatQ,"rsrchcatY":rsrchcatY,
 								"rsrchsubcatY":rsrchsubcatY,"acv_quarter":acv_quarter,"XFROMDATE_ACV":XFROMDATE_ACV,"XTODATE_ACV":XTODATE_ACV,
 								"acv_rsrchcatQ":acv_rsrchcatQ,"acv_rsrchsubcatQ":acv_rsrchsubcatQ,
 								"acv_rsrchcatY":acv_rsrchcatY,"acv_rsrchsubcatY":acv_rsrchsubcatY};
 				jsonobj=JSON.stringify(jsonObject);
 				var encData=encAESData($("#AESKey").val(), jsonObject);
 				console.log("Target/ Objective jsonobj");
 				console.log(jsonobj);
 				var xmlHttp = new XMLHttpRequest();
 				xmlHttp.open("POST", "../TargetAchievementService?fstatus=UPLOAD", true);
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

/*
 * code commented as per new format design share by client new function is written below.
 * function getTargetAchievementDetail(){
	var id = $("#resPrps").val();
	var hrmsApi = $("#hrmsApi").val().trim();
	var finYrId = $("#finYr").val()+"-"+(parseInt($("#finYr").val())+1);
	
	//Validate file as click on view
	if($("#Xlocation").val() == ""){ 
 		$('#Xlocation').focus();	
			showerr($("#Xlocation")[0], "Location is required.","block");
			return false;	
	}
	else if($("#finYr").val() == ""){ 
 		$('#finYr').focus();	
			showerr($("#finYr")[0], "Financial Year is required.","block");
			return false;	
	}
	else if($("#Xddo").val() == ""){ 
 		$('#Xddo').focus();	
			showerr($("#Xddo")[0], "DDO is required.","block");
			return false;	
	}else if(id == ""){ 
 		$('#resPrps').focus();	
			showerr($("#resPrps")[0], "Research Proposal is required.","block");
			return false;	
	}
	try {
		$.ajax({
			type: "POST",
			url: "../TargetAchievementService",
			data:{"fstatus":"DETAILS", "pi_id":id},
			async: false,
			success: function (response){
				$('#stable').html("");
				console.log("response");
				console.log(response.piIdlist);
				var details=response.piIdlist[0];
				
				var cols ='<div style="padding:15px;"><div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Name of the project :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.PS_TITTLE_PROJ+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Name of the PI :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.PS_PRINCIPAL+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Funding Agency :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.fn_agency+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Fund available :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.fundavail+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Project Start Date :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.proj_start_date+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Project End Date  :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.proj_end_date+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div id="tab_info">'
				+'<div class="form-group row" style="margin:0px;padding:0px;">'
				+' <label id="ftb1" class="col-sm-1 col-form-label fileTabs" style="width: 15%;" onClick="showTable('+"'"+''+"1"+''+"'"+');">Target Quarterly</label>'
				+' <label id="ftb2" class="col-sm-1 col-form-label fileTabs" style="width: 15%;" onClick="showTable('+"'"+''+"2"+''+"'"+');">Target Yearly</label>'
				+' <label id="ftb3" class="col-sm-1 col-form-label fileTabs" style="width: 15%;" onClick="showTable('+"'"+''+"3"+''+"'"+');">Acheivements Quarterly</label>'
				+' <label id="ftb4" class="col-sm-1 col-form-label fileTabs" style="width: 15%;" onClick="showTable('+"'"+''+"4"+''+"'"+');">Acheivements Yearly</label>'
				+' </div>'
				+'</div>'
				Target Quarterly Details
				+'<div id="tab1" class="tab_details">'
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label required-field">Quarter:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="quarter" name="quarter" >'
				+'<option value="Q1"' +(details.quarter=='Q1'?"selected":"")+'>Q1</option>'
				+'<option value="Q2"' +(details.quarter=='Q2'?"selected":"")+'>Q2</option>'
				+'<option value="Q3"' +(details.quarter=='Q3'?"selected":"")+'>Q3</option>'
				+'<option value="Q4"' +(details.quarter=='Q4'?"selected":"")+'>Q4</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="" >From Date:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;" >'
				+'<div class="input-group date" id="msg-XODATE">'
				+'<div class="input-group-addon"><i class="fa fa-calendar"></i></div>'
				+'<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XFROMDATE" name="XFROMDATE" placeholder="DD/MM/YYYY" value="'+details.from_date+'">'
				+'</div>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="" >To Date:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;" >'
				+'<div class="input-group date" id="msg-XTODATE">'
				+'<div class="input-group-addon"><i class="fa fa-calendar"></i></div>'
				+'<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XTODATE" name="XTODATE" placeholder="DD/MM/YYYY" value="'+details.to_date+'">'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label required-field">Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="rsrchcatQ" name="rsrchcatQ" onchange="getRsrchSubCategory(this.value,'+"'"+details.sub_category_qr+"'"+','+"'"+'rsrchsubcatQ'+"'"+');">'
				+'<option value="">Select Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">Sub Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="rsrchsubcatQ" name="rsrchsubcatQ">'
				+'<option value="">Select Sub Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label">Financial Year:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;"><b>'+finYrId+'</b></div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">Upload Targets:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc1" name="upldoc1" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Target_Major_Doc_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Target_Major_Doc_Qr+'</u></a>'
				+'</div>' 
				+'<label style="display:none;" class="col-sm-2 col-form-label text-left required-field" for="">Upload Minor Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:none;">'
				+'<input type="file" id="upldoc2" name="upldoc2" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Target_Minor_Doc_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Target_Minor_Doc_Qr+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				//New upload div added for photograph
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">Upload Photograph:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc13" name="upldoc13" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Target_Photograph_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Target_Photograph_Qr+'</u></a>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'</div>'
				Target Yearly Details
				+'<div id="tab2" class="tab_details">'
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label required-field">Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="rsrchcatY" name="rsrchcatY" onchange="getRsrchSubCategory(this.value,'+"'"+details.sub_category_yr+"'"+','+"'"+'rsrchsubcatY'+"'"+');">'
				+'<option value="">Select Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">Sub Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="rsrchsubcatY" name="rsrchsubcatY">'
				+'<option value="">Select Sub Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label">Financial Year:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;"><b>'+finYrId+'</b></div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label text-left" for="">Upload Major Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc3" name="upldoc3" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Target_Major_Doc_Yr+'&folderName=RSRCH/UC/'+id+'">'
				+'<u>'+details.Target_Major_Doc_Yr+'</u></a>'
				+'</div>' 
				+'<label class="col-sm-2 col-form-label text-left" for="">Upload Minor Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc4" name="upldoc4" class=""style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Target_Minor_Doc_Yr+'&folderName=RSRCH/UC/'+id+'">'
				+'<u>'+details.Target_Minor_Doc_Yr+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				Tab 2 End here
				
				Tab 3 For Quarterly Acheivement 
				+'<div id="tab3" class="tab_details">'
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label required-field">Quarter:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="acv_quarter" name="acv_quarter" >'
				+'<option value="Q1"' +(details.acv_quarter=='Q1'?"selected":"")+'>Q1</option>'
				+'<option value="Q2"' +(details.acv_quarter=='Q2'?"selected":"")+'>Q2</option>'
				+'<option value="Q3"' +(details.acv_quarter=='Q3'?"selected":"")+'>Q3</option>'
				+'<option value="Q4"' +(details.acv_quarter=='Q4'?"selected":"")+'>Q4</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">From Date:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<div class="input-group date" id="msg-XODATE_ACV">'
				+'<div class="input-group-addon"><i class="fa fa-calendar"></i></div>'
				+'<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XFROMDATE_ACV" name="XFROMDATE_ACV" placeholder="DD/MM/YYYY" value="'+details.acv_from_date+'">'
				+'</div>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">To Date:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;" >'
				+'<div class="input-group date" id="msg-XTODATE_ACV">'
				+'<div class="input-group-addon"><i class="fa fa-calendar"></i></div>'
				+'<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XTODATE_ACV" name="XTODATE_ACV" placeholder="DD/MM/YYYY" value="'+details.acv_to_date+'">'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label required-field">Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="acv_rsrchcatQ" name="acv_rsrchcatQ" onchange="getRsrchSubCategory(this.value,'+"'"+details.acv_sub_category_qr+"'"+','+"'"+'acv_rsrchsubcatQ'+"'"+');">'
				+'<option value="">Select Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">Sub Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="acv_rsrchsubcatQ" name="acv_rsrchsubcatQ">'
				+'<option value="">Select Sub Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label">Financial Year:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;"><b>'+finYrId+'</b></div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left " for="">Upload Major Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc5" name="upldoc5" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Achievements_Major_Doc_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Major_Doc_Qr+'</u></a>'
				+'</div>' 
				+'<label class="col-sm-2 col-form-label text-left" for="">Upload Minor Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc6" name="upldoc6" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Achievements_Minor_Doc_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Minor_Doc_Qr+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left" for="">Original high quality photographs used in Major Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px; display:flex !important;">'
				+'<input type="file" id="upldoc7" name="upldoc7" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only jpg. files will be allowed. Max Size Cant Exceed 20MB.)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Achievements_Major_Photo_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Major_Photo_Qr+'</u></a>'
				+'</div>' 
				+'<label class="col-sm-2 col-form-label text-left" for="">Original high quality photographs used in Minor Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc8" name="upldoc8" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only jpg. files will be allowed. Max Size Cant Exceed 20MB.)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Achievements_Minor_Photo_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Minor_Photo_Qr+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				Tab 3 End here
				
				Tab 4 Achievements Yearly Details
				+'<div id="tab4" class="tab_details">'
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label">Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="acv_rsrchcatY" name="acv_rsrchcatY" onchange="getRsrchSubCategory(this.value,'+"'"+details.acv_sub_category_yr+"'"+','+"'"+'acv_rsrchsubcatY'+"'"+');">'
				+'<option value="">Select Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left " for="">Sub Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="acv_rsrchsubcatY" name="acv_rsrchsubcatY">'
				+'<option value="">Select Sub Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label">Financial Year:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;"><b>'+finYrId+'</b></div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left" for="">Upload Major Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px; display:flex !important;">'
				+'<input type="file" id="upldoc9" name="upldoc9" class=""style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Achievements_Major_Doc_Yr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Major_Doc_Yr+'</u></a>'
				+'</div>' 
				+'<label class="col-sm-2 col-form-label text-left" for="">Upload Minor Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc10" name="upldoc10" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Achievements_Minor_Doc_Yr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Minor_Doc_Yr+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left" for="">Original high quality photographs used in Major Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px; display:flex !important;">'
				+'<input type="file" id="upldoc11" name="upldoc11" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only jpg. files will be allowed. Max Size Cant Exceed 20MB.)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Achievements_Major_Photo_Yr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Major_Photo_Yr+'</u></a>'
				+'</div>' 
				+'<label class="col-sm-2 col-form-label text-left" for="">Original high quality photographs used in Minor Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc12" name="upldoc12" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only jpg. files will be allowed. Max Size Cant Exceed 20MB.)"></a>'
				+'</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.Achievements_Minor_Photo_Yr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Minor_Photo_Yr+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'</div>'
				Tab 4 End here
				
				+'</div>'
				$('#stable').append(cols);
				showTable(1);
				
				$("a").tooltip();
				
				$(".hidediv").show();
				getRsrchCategory(details.category_qr,'rsrchcatQ');
				getRsrchCategory(details.category_yr,'rsrchcatY');
				getRsrchCategory(details.acv_category_qr,'acv_rsrchcatQ');
				getRsrchCategory(details.acv_category_yr,'acv_rsrchcatY');
				
				getRsrchSubCategory(details.category_qr,details.sub_category_qr,'rsrchsubcatQ');
				getRsrchSubCategory(details.category_yr,details.sub_category_yr,'rsrchsubcatY');
				getRsrchSubCategory(details.acv_category_qr,details.acv_sub_category_qr,'acv_rsrchsubcatQ');
				getRsrchSubCategory(details.acv_category_yr,details.acv_sub_category_yr,'acv_rsrchsubcatY');
				$("#detailstr").show();
			}
			
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
	//Calenders for from date and to date
	$("#XFROMDATE").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
	}).on('click', function() {
		$("#XFROMDATE").datepicker("setDate", '');
	}).on('clearDate', function(selected) {
	});
	
	$("#XTODATE").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
	}).on('click', function() {
		$("#XTODATE").datepicker("setDate", '');
	}).on('clearDate', function(selected) {
	});
	
	$("#XFROMDATE_ACV").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
	}).on('click', function() {
		$("#XFROMDATE").datepicker("setDate", '');
	}).on('clearDate', function(selected) {
	});
	
	$("#XTODATE_ACV").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
	}).on('click', function() {
		$("#XTODATE").datepicker("setDate", '');
	}).on('clearDate', function(selected) {
	});
}*/

function showTable(i) {
	try {
		 for(k=1;k<=2;k++){
	        document.getElementById("ftb"+k).className="fileTabs";
	        document.getElementById("tab"+k).style.display="none";
	        document.getElementById("ftb"+k).style.background = "#0A819C";
	    } 
	    document.getElementById("ftb"+i).className="fileTabs";
	    document.getElementById("tab"+i).style.display="";
	    document.getElementById("ftb"+i).style.background = "#ff8c00";
	    
	    $("#currenttab").val(i);
	} catch (err) {
	    alert(err.message);
	} 
}

function ishowTable(i){
	document.getElementById("ftb"+i).className="fileTabs_show";
}
 document.onkeydown = function (evt) {
	  var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
	  if (keyCode == 123){ 
		  return false;
	  } else if(keyCode == 17) {
		  return false;
	  } else {
	    return true;
	  }
};

function validateFormFields(){
	try{
		 var currenttab=$("#currenttab").val();
		//Validate file as click on Upload button
		//Validation for target Monthly
		 var selectedrsrchsubcatQ = $("#rsrchsubcatQ option:selected").text();
		 var selectedrsrchsubcatY = $("#rsrchsubcatY option:selected").text();
		 var selectedacv_rsrchsubcatQ = $("#acv_rsrchsubcatQ option:selected").text();
		 var selectedacv_rsrchsubcatQ = $("#acv_rsrchsubcatQ option:selected").text();
		 var selectedacv_rsrchsubcatY = $("#acv_rsrchsubcatY option:selected").text();
		 var from_date=$("#XFROMDATE").val();
		 var to_date=$("#XTODATE").val();
		 var fd = from_date.substring(6,10) + from_date.substring(3,5) + from_date.substring(0,2);
		 var td = to_date.substring(6,10) + to_date.substring(3,5) + to_date.substring(0,2);
		 
		/* var XFROMDATE_ACV=$("#XFROMDATE_ACV").val();
		 var XTODATE_ACV=$("#XTODATE_ACV").val();
		 var fd_acv = XFROMDATE_ACV.substring(6,10) + XFROMDATE_ACV.substring(3,5) + XFROMDATE_ACV.substring(0,2);
		 var td_acv = XTODATE_ACV.substring(6,10) + XTODATE_ACV.substring(3,5) + XTODATE_ACV.substring(0,2);
		*/
		 
		 if($("#quarter").val() == "" && currenttab=="1"){ 
	 		$('#quarter').focus();	
				showerr($("#quarter")[0], "Target Quarter is required.","block");
				return false;	
		}
		else if(from_date == "" && currenttab=="1"){ 
	 		$('#XFROMDATE').focus();	
				showerr($("#XFROMDATE")[0], "Target FROM DATE is required.","block");
				displaySuccessMessages("errMsg2", "Target FROM DATE is required.", "");
		    	clearSuccessMessageAfterThreeSecond("errMsg2"); 
				return false;	
		}
		else if(to_date == "" && currenttab=="1"){ 
	 		$('#XTODATE').focus();	
				showerr($("#XTODATE")[0], "Target TODATE is required.","block");
				return false;	
		}
		else if(fd>td && currenttab=="1"){
			showerr($("#XFROMDATE")[0], "Target From Date should be less than To date", "block");
			displaySuccessMessages("errMsg2", "Target From Date should be less than To date", "");
	    	clearSuccessMessageAfterThreeSecond("errMsg2"); 
			return false;
		}
		else if($("#rsrchcatQ").val() == "" && currenttab=="1"){ 
	 		$('#rsrchcatQ').focus();	
				showerr($("#rsrchcatQ")[0], "Target Quarterly Category is required.","block");
				return false;	
		}
		else if($("#rsrchsubcatQ").val() == "" && selectedrsrchsubcatQ != "NA" && currenttab=="1"){ 
	 		$('#rsrchsubcatQ').focus();	
				showerr($("#rsrchsubcatQ")[0], "Target Quarterly Sub Category is required.","block");
				return false;	
		}
		/*//Validation for target yearly
		else if($("#rsrchcatY").val() == "" && currenttab=="2"){ 
	 		$('#rsrchcatY').focus();	
				showerr($("#rsrchcatY")[0], "Target Yearly Category is required.","block");
				return false;	
		}
		else if($("#rsrchsubcatY").val() == "" && selectedrsrchsubcatY != "NA" && currenttab=="2"){ 
	 		$('#rsrchsubcatY').focus();	
				showerr($("#rsrchsubcatY")[0], "Target Yearly Sub Category is required.","block");
				return false;	
		}
		//validation for achievement quarterly
		if($("#acv_quarter").val() == "" && currenttab=="3"){ 
	 		$('#acv_quarter').focus();	
				showerr($("#acv_quarter")[0], "Acheivements Quarter is required.","block");
				return false;	
		}
		else if($("#XFROMDATE_ACV").val() == "" && currenttab=="3"){ 
	 		$('#XFROMDATE_ACV').focus();	
				showerr($("#XFROMDATE_ACV")[0], "Acheivements FROM DATE is required.","block");
				return false;	
		}
		else if($("#XTODATE_ACV").val() == "" && currenttab=="3"){ 
	 		$('#XTODATE_ACV').focus();	
				showerr($("#XTODATE_ACV")[0], "Acheivements TODATE is required.","block");
				return false;	
		}
		else if(fd_acv>td_acv && currenttab=="3"){
			showerr($("#XFROMDATE_ACV")[0], "Acheivements From Date should be less than To date", "block");
			displaySuccessMessages("errMsg2", "Acheivements From Date should be less than To date", "");
	    	clearSuccessMessageAfterThreeSecond("errMsg2"); 
			return false;
		}
		else if($("#acv_rsrchcatQ").val() == "" && currenttab=="3"){ 
	 		$('#acv_rsrchcatQ').focus();	
				showerr($("#acv_rsrchcatQ")[0], "Acheivements Quarterly Category is required.","block");
				return false;	
		}
		else if($("#acv_rsrchsubcatQ").val() == "" && selectedacv_rsrchsubcatQ != "NA" && currenttab=="3"){ 
	 		$('#acv_rsrchsubcatQ').focus();	
				showerr($("#acv_rsrchsubcatQ")[0], "Acheivements Quarterly Sub Category is required.","block");
				return false;	
		}*/
		//validation for achievement Yearly
		else if($("#acv_rsrchcatY").val() == "" && currenttab=="2"){ 
	 		$('#acv_rsrchcatY').focus();	
				showerr($("#acv_rsrchcatY")[0], "Acheivements Yearly Category is required.","block");
				return false;	
		}
		else if($("#acv_rsrchsubcatY").val() == "" && selectedacv_rsrchsubcatY != "NA" && currenttab=="2"){ 
	 		$('#acv_rsrchsubcatY').focus();	
				showerr($("#acv_rsrchsubcatY")[0], "Acheivements Yearly Sub Category is required.","block");
				return false;	
		}
		
		}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function getTargetAchievementDetail(){
	var id = $("#resPrps").val();
	var hrmsApi = $("#hrmsApi").val().trim();
	var finYrId = $("#finYr").val()+"-"+(parseInt($("#finYr").val())+1);
	
	//Validate file as click on view
	if($("#Xlocation").val() == ""){ 
 		$('#Xlocation').focus();	
			showerr($("#Xlocation")[0], "Location is required.","block");
			return false;	
	}
	else if($("#finYr").val() == ""){ 
 		$('#finYr').focus();	
			showerr($("#finYr")[0], "Financial Year is required.","block");
			return false;	
	}
	else if($("#Xddo").val() == ""){ 
 		$('#Xddo').focus();	
			showerr($("#Xddo")[0], "DDO is required.","block");
			return false;	
	}else if(id == ""){ 
 		$('#resPrps').focus();	
			showerr($("#resPrps")[0], "Research Proposal is required.","block");
			return false;	
	}
	try {
		$.ajax({
			type: "POST",
			url: "../TargetAchievementService",
			data:{"fstatus":"DETAILS", "pi_id":id,"finYr":$("#finYr").val()},
			async: false,
			success: function (response){
				$('#stable').html("");
				console.log("response");
				console.log(response.piIdlist);
				var details=response.piIdlist[0];
				var id=details.TA_ID;
				var cols ='<div style="padding:15px;"><div class="form-group" >'
				+'<input type="hidden" id="TA_ID" value="'+details.TA_ID+'">'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Name of the project :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.PS_TITTLE_PROJ+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Name of the PI :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.PS_PRINCIPAL+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Funding Agency :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.fn_agency+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Fund available :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.fundavail+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Project Start Date :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.proj_start_date+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Project End Date  :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.proj_end_date+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'<div class="form-group">'
				+'<div class="col-sm-12">'
				+'<label for="" class="col-sm-20 col-form-label text-left l-title" ><b><a href="../resources/file/target_acheivement_format.docx" target="_blank" download>Click here to Download the Target/ Objective and Achievement form</a></b></label>'
				+'</div>'
				+'</div>'
				+'<div id="tab_info">'
				+'<div class="form-group row" style="margin:0px;padding:0px;">'
				+' <label id="ftb1" class="col-sm-1 col-form-label fileTabs" style="width: 15%;" onClick="showTable('+"'"+''+"1"+''+"'"+');">Target/Acheivements Quarterly</label>'
				/*+' <label id="ftb2" class="col-sm-1 col-form-label fileTabs" style="width: 15%;" onClick="showTable('+"'"+''+"2"+''+"'"+');">Target Yearly</label>'
				+' <label id="ftb3" class="col-sm-1 col-form-label fileTabs" style="width: 15%;" onClick="showTable('+"'"+''+"3"+''+"'"+');">Acheivements Quarterly</label>'*/
				+' <label id="ftb2" class="col-sm-1 col-form-label fileTabs" style="width: 15%;" onClick="showTable('+"'"+''+"2"+''+"'"+');">Target/Acheivements Yearly</label>'
				+' </div>'
				+'</div>'
				/*Target Quarterly Details*/
				+'<div id="tab1" class="tab_details">'
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label required-field">Quarter:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="quarter" name="quarter" >'
				+'<option value="Q1"' +(details.quarter=='Q1'?"selected":"")+'>Q1</option>'
				+'<option value="Q2"' +(details.quarter=='Q2'?"selected":"")+'>Q2</option>'
				+'<option value="Q3"' +(details.quarter=='Q3'?"selected":"")+'>Q3</option>'
				+'<option value="Q4"' +(details.quarter=='Q4'?"selected":"")+'>Q4</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="" >From Date:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;" >'
				+'<div class="input-group date" id="msg-XODATE">'
				+'<div class="input-group-addon"><i class="fa fa-calendar"></i></div>'
				+'<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XFROMDATE" name="XFROMDATE" placeholder="DD/MM/YYYY" value="'+details.from_date+'">'
				+'</div>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="" >To Date:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;" >'
				+'<div class="input-group date" id="msg-XTODATE">'
				+'<div class="input-group-addon"><i class="fa fa-calendar"></i></div>'
				+'<input  data-field-id=" " type="text" class="form-control datecalendar jOpen_date" id="XTODATE" name="XTODATE" placeholder="DD/MM/YYYY" value="'+details.to_date+'">'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label required-field">Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="rsrchcatQ" name="rsrchcatQ" onchange="getRsrchSubCategory(this.value,'+"'"+details.sub_category_qr+"'"+','+"'"+'rsrchsubcatQ'+"'"+');">'
				+'<option value="">Select Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">Sub Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="rsrchsubcatQ" name="rsrchsubcatQ">'
				+'<option value="">Select Sub Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label">Financial Year:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;"><b>'+finYrId+'</b></div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">Upload Targets:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc1" name="upldoc1" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" style="word-break: break-all;" href="../downloadfile?filename='+details.Target_Major_Doc_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Target_Major_Doc_Qr+'</u></a>'
				+'</div>' 
				+'<label style="display:none;" class="col-sm-2 col-form-label text-left required-field" for="">Upload Minor Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:none;">'
				+'<input type="file" id="upldoc2" name="upldoc2" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" style="word-break: break-all;" href="../downloadfile?filename='+details.Target_Minor_Doc_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Target_Minor_Doc_Qr+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				//New upload div added for photograph
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left required-field" for="">Upload Photograph:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc13" name="upldoc13" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" style="word-break: break-all;" href="../downloadfile?filename='+details.Target_Photograph_Qr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Target_Photograph_Qr+'</u></a>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'</div>'
				
				/*Tab 2 and tab 3 removed for reference check above function with same name
				 *  Tab 4 Achievements Yearly Details*/
				+'<div id="tab2" class="tab_details">'
				+'<div class="form-group" >'
				+'<div class="col-md-12">'
				+'<div class="row">'
				+'<label class="col-sm-2 col-form-label">Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="acv_rsrchcatY" name="acv_rsrchcatY" onchange="getRsrchSubCategory(this.value,'+"'"+details.acv_sub_category_yr+"'"+','+"'"+'acv_rsrchsubcatY'+"'"+');">'
				+'<option value="">Select Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label text-left " for="">Sub Category:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'
				+'<select class="form-control" id="acv_rsrchsubcatY" name="acv_rsrchsubcatY">'
				+'<option value="">Select Sub Category</option>'
				+'</select>'
				+'</div>'
				+'<label class="col-sm-2 col-form-label">Financial Year:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;"><b>'+finYrId+'</b></div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left" for="">Upload Major Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px; display:flex !important;">'
				+'<input type="file" id="upldoc9" name="upldoc9" class=""style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" style="word-break: break-all;" href="../downloadfile?filename='+details.Achievements_Major_Doc_Yr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Major_Doc_Yr+'</u></a>'
				+'</div>' 
				+'<label class="col-sm-2 col-form-label text-left" for="">Upload Minor Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc10" name="upldoc10" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only .doc & docx files will be allowed for uploading.!)"></a>'
				+'</span>'
				+'<br><a target="_blank" style="word-break: break-all;" href="../downloadfile?filename='+details.Achievements_Minor_Doc_Yr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Minor_Doc_Yr+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<div class="row " >'
				+'<label class="col-sm-2 col-form-label text-left" for="">Original high quality photographs used in Major Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px; display:flex !important;">'
				+'<input type="file" id="upldoc11" name="upldoc11" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only jpg. files will be allowed. Max Size Cant Exceed 20MB.)"></a>'
				+'</span>'
				+'<br><a target="_blank" style="word-break: break-all;" href="../downloadfile?filename='+details.Achievements_Major_Photo_Yr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Major_Photo_Yr+'</u></a>'
				+'</div>' 
				+'<label class="col-sm-2 col-form-label text-left" for="">Original high quality photographs used in Minor Achievements:</label>'
				+'<div class="col-sm-4" style="padding-top:7px;display:flex !important;">'
				+'<input type="file" id="upldoc12" name="upldoc12" class="" style="width:57% !important;">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<a class="fa fa-info-circle fa-lg" data-toggle="tooltip" data-placement="left" style="color:#f16a07;"title="" data-original-title="(*Note: Only jpg. files will be allowed. Max Size Cant Exceed 20MB.)"></a>'
				+'</span>'
				+'<br><a target="_blank" style="word-break: break-all;" href="../downloadfile?filename='+details.Achievements_Minor_Photo_Yr+'&folderName=RSRCH/TARGET/'+id+'">'
				+'<u>'+details.Achievements_Minor_Photo_Yr+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'</div>'
				/*Tab 4 End here*/
				
				+'</div>'
				$('#stable').append(cols);
				showTable(1);
				
				$("a").tooltip();
				
				$(".hidediv").show();
				getRsrchCategory(details.category_qr,'rsrchcatQ');
				getRsrchCategory(details.category_yr,'rsrchcatY');
				getRsrchCategory(details.acv_category_qr,'acv_rsrchcatQ');
				getRsrchCategory(details.acv_category_yr,'acv_rsrchcatY');
				
				getRsrchSubCategory(details.category_qr,details.sub_category_qr,'rsrchsubcatQ');
				getRsrchSubCategory(details.category_yr,details.sub_category_yr,'rsrchsubcatY');
				getRsrchSubCategory(details.acv_category_qr,details.acv_sub_category_qr,'acv_rsrchsubcatQ');
				getRsrchSubCategory(details.acv_category_yr,details.acv_sub_category_yr,'acv_rsrchsubcatY');
				$("#detailstr").show();
			}
			
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
	//Calenders for from date and to date
	$("#XFROMDATE").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
	}).on('click', function() {
		$("#XFROMDATE").datepicker("setDate", '');
	}).on('clearDate', function(selected) {
	});
	
	$("#XTODATE").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
	}).on('click', function() {
		$("#XTODATE").datepicker("setDate", '');
	}).on('clearDate', function(selected) {
	});
	
	$("#XFROMDATE_ACV").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
	}).on('click', function() {
		$("#XFROMDATE").datepicker("setDate", '');
	}).on('clearDate', function(selected) {
	});
	
	$("#XTODATE_ACV").datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
	}).on('click', function() {
		$("#XTODATE").datepicker("setDate", '');
	}).on('clearDate', function(selected) {
	});
}