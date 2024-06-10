/**
 * @author Amit dangi
 */
$(document).ready(function(){
	$(".hidediv").hide();
	$("#btnReset, #btnReset1, #btnReset2").click(function(){
		location.reload();
	});
	
	//Function for upload the UC/AUC certificate on bases of selected reserach proposal id
 	$('#btnSave').click(function(){
 				var form_data = new FormData();
 				var workarray = [];
 				var exp_index = "2";
 				var is_upload_file="N";
 				var resPrpsId=$('#resPrps').val();
 				if($("#upldoc1").val()=="" && $("#upldoc2").val()==""){
 						$('#btnSave').focus();	
						showerr($("#btnSave")[0], "Please Upload UC/AUC Certificate ","block");
						return false;	
 				}
 				for(i=1; i<=parseInt(exp_index); i++){
 					if($("#upldoc"+i).val()!="" && $("#upldoc"+i).val()!=undefined){
 						is_upload_file="Y";
 						var ext = $("#upldoc"+i).val().split('.').pop().toLowerCase();
 						
 						if(ext !=""){
 							if($.inArray(ext, ['pdf']) == -1) {						
 								$('#upldoc'+i).focus();
 								alert("Note: Only .pdf files will be allowed for uploading.!");
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
 				var uc_certname=$('#uc_certname').val();
 				var auc_certname=$('#auc_certname').val();
 				var finYr=$('#finYr').val();
 				var uc_id=$('#uc_id').val();
 				var xmlHttp = new XMLHttpRequest();
 				xmlHttp.open("POST", "../UtilizationCertificateService?fstatus=UPLOAD&resPrpsId="+resPrpsId+
 						"&uc_certname="+uc_certname+"&auc_certname="+auc_certname+"&finYr="+finYr+"&uc_id="+uc_id, true);
 				xmlHttp.setRequestHeader("jsonobj", "");
 				xmlHttp.send(form_data);
 				try{
 					xmlHttp.onreadystatechange = function() {
 						if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
 							var data=JSON.parse(this.responseText);
 							console.log("Response");
 							console.log(data);
 							    displaySuccessMessages("errMsg1", data.status, "");
 								$("#btnSave").hide();
 								$("#btnfinalsubmit").hide();
 								$("#btnReseT").hide();
 								
 								setTimeout(function() {
 									location.reload();
 								}, 3000); 							
 						}
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


function getUtilizationCertificateDetail(){
	var id = $("#resPrps").val();
	var hrmsApi = $("#hrmsApi").val().trim();
	var finYrId = $("#finYr").val()+"-"+(parseInt($("#finYr").val())+1);
	//Validate file as click on view
	if($("#Xlocation").val() == ""){ 
 		$('#Xlocation').focus();	
			showerr($("#Xlocation")[0], "Location is required.","block");
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
			url: "../UtilizationCertificateService",
			data:{"fstatus":"DETAILS", "pi_id":id,"fin_year":$("#finYr").val()},
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
				+'<div class="col-sm-2"style="padding-top:7px;">'+details.fundavail+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Project Start Date :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.proj_start_date+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for=""><b>Project End Date  :</b></label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+details.proj_end_date+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				
				+'<div id="tab_info">'
				+'<div class="form-group row" style="margin:0px;padding:0px;">'
				+' <label id="ftb1" class="col-sm-1 col-form-label fileTabs" style="width: 10%;" onClick="showTable('+"'"+''+"1"+''+"'"+');">UC</label>'
				+' <label id="ftb2" class="col-sm-1 col-form-label fileTabs" style="width: 10%;" onClick="showTable('+"'"+''+"2"+''+"'"+');">AUC</label>'
				+' </div>'
				+'</div>'
				+'<div id="tab1" class="tab_details">'
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<label class="col-sm-2 col-form-label">Financial Year:</label>'
				+'<div class="col-sm-2" style="padding-top:7px;">'+finYrId+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for="">Upload UC:</label>'
				+'<div class="col-sm-6" style="padding-top:7px;">'
				+'<input type="file" id="upldoc1" name="upldoc1" class="">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'(*Note: Only .pdf will be allowed. Max Size Cant Exceed 20MB.)</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.uc_certname+'&folderName=RSRCH/UC/'+details.uc_id+'">'
				+'<u>'+details.uc_certname+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				+'<div id="tab2" class="tab_details">'
				+'<div class="form-group " >'
				+'<div class="col-md-12 ">'
				+'<label class="col-sm-2 col-form-label">Financial Year:</label>'
				+'<div class="col-sm-2">'+finYrId+'</div>'
				+'<label class="col-sm-2 col-form-label text-left" for="">Upload AUC:</label>'
				+'<div class="col-sm-6" style="padding-top:7px;">'
				+'<input type="file" id="upldoc2" name="upldoc2" class="">'
				+'<span class="" style="font-size: 13px !important; font-weight: bold; color: red;">'
				+'<br> (*Note: Only .pdf will be allowed. Max Size Cant Exceed 20MB.)</span>'
				+'<br><a target="_blank" href="../downloadfile?filename='+details.auc_certname+'&folderName=RSRCH/UC/'+details.uc_id+'">'
				+'<u>'+details.auc_certname+'</u></a>'
				+'</div>' 
				+'</div>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'<input type="hidden" id="uc_certname" name="uc_certname" value="'+details.uc_certname+'">'
				+'<input type="hidden" id="auc_certname" name="auc_certname" value="'+details.auc_certname+'">'
				+'<input type="hidden" id="uc_id" name="uc_id" value="'+details.uc_id+'">'
				$('#stable').append(cols);
				showTable(1);
				$(".hidediv").show();
				$("#detailstr").show();
			}
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

function showTable(i) {
	try {
		 for(k=1;k<=2;k++){
	        document.getElementById("ftb"+k).className="fileTabs";
	        document.getElementById("tab"+k).style.display="none";
	        document.getElementById("ftb" + k).style.background = "#0A819C";
	    } 
	    document.getElementById("ftb"+i).className="fileTabs";
	    document.getElementById("tab"+i).style.display="";
	    document.getElementById("ftb" + i).style.background = "#ff8c00";  
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
