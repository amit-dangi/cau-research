$(document).ready(function(){ 
	
		$("#btnReset").click(function() {
		location.reload();
		}); 
		
		$("#X_BTNSEARCH").click(function() {
		x_research=$("#X_RESEARCH").val();
		x_fdate=$("#X_FDATE").val();
		x_tdate=$("#X_TDATE").val();
		
			document.FrmResearchProposalD.target = "objAchievementE";
			document.FrmResearchProposalD.action = 'objective_achievement_details_l.jsp?x_research='+x_research+"&x_fdate="+x_fdate+"&x_tdate="+x_tdate;
			document.FrmResearchProposalD.submit();
			document.getElementById("FrmResearchProposalD").reset();
		});
		$("#X_BTNRESET").click(function() {
			$("#X_RESEARCH").val("");
			$("#X_FDATE").val("");
			$("#X_TDATE").val("");
		});  
		$("#btnBack").click(function() {
			window.location.href = "objective_achievement_details_e.jsp";
		});
		
	  });

//to save record  
function executeSaveRecord() {
	try {
		var fstatus	         =$("#fstatus").val();
		var oa_id	         =$("#oa_id").val();
		var rsrch_proposal	 =$("#rsrch_proposal").val();
		var from_date		 =$("#XSTARTDATE").val();
		var to_date			 =$("#XENDDATE").val();
		var achievement_det	 =$("#achievement_det").val();
		var upldoc			 =$("#upldoc").val();
		
	var	fd = from_date.substring(6,10) + from_date.substring(3,5) + from_date.substring(0,2);
	var	td = to_date.substring(6,10) + to_date.substring(3,5) + to_date.substring(0,2);
		
		if(upldoc!= "") {
			is_doc_uploaded = "Y" ;
		} else { 
			is_doc_uploaded = "N" ;
		}
		
		 if (rsrch_proposal ==""  || rsrch_proposal  == null){
				$("#rsrch_proposal").val("");
				$("#rsrch_proposal").focus();
				showerr($("#rsrch_proposal")[0], "Research Proposal is required", "block");
				return false;
			}
		 if (from_date ==""  || from_date  == null){
				$("#XSTARTDATE").val("");
				$("#XSTARTDATE").focus();
				showerr($("#XSTARTDATE")[0], "From Date is required", "block");
				return false;
			}
		 if (to_date ==""  || to_date  == null){
				$("#XENDDATE").val("");
				$("#XENDDATE").focus();
				showerr($("#XENDDATE")[0], "To Date is required", "block");
				return false;
			}
		 if(fd>td){
				showerr($("#msg-XSTARTDATE")[0], "From Date is less than To date", "block");
				return false;
		 }
		 if (achievement_det ==""  || achievement_det  == null){
				$("#achievement_det").val("");
				$("#achievement_det").focus();
				showerr($("#achievement_det")[0], "Progress Details is required", "block");
				return false;
			}
		
		 var form_data = new FormData();
			if(upldoc!=''&& upldoc!=undefined){
				form_data.append('upl_file', $('#upldoc')[0].files[0]);
				 var signCount = document.getElementById("upldoc").files.length;
			}  
 			var objjson = { 
				"rsrch_proposal" 	: $("#rsrch_proposal").val(), 
				"from_date" 		: $("#XSTARTDATE").val(),
				"to_date" 			: $("#XENDDATE").val(),
				"achievement_det" 	: $("#achievement_det").val(),	
				"is_doc_uploaded"	:is_doc_uploaded,
				"oa_id"				:oa_id,
			};
 		//	alert("json:" + JSON.stringify(objjson));
			var encData = encAESData($("#AESKey").val(), objjson);
				
				var xmlHttp = new XMLHttpRequest();
				xmlHttp.open("POST", "ObjectiveAchievementService", true);			    
				xmlHttp.setRequestHeader("encData", encData);
				xmlHttp.setRequestHeader("fstatus",fstatus);
				xmlHttp.send(form_data);
				try{
					xmlHttp.onreadystatechange = function()
						{
						if (xmlHttp.readyState === 4 && xmlHttp.status === 200)
						{
							var data=JSON.parse(this.responseText);
							saveOrUpdateCommonFunctionInUploadDoc(decAESData($("#AESKey").val(), data));
						}
						}
					} catch (err){
					alert(err);
				}
	} catch (err) {
		alert(err);
	}
}

function saveOrUpdateCommonFunctionInUploadDoc(data) {
		if (data.fstatus == "S") hideElement("btnSave");
		if (data.fstatus == "E") hideElement("btnUpdate");
		var tRec = $("#totRow").val();
		setTimeout(function () {
			if (data.fstatus == "D"){
				for(var i=1;i<=tRec;i++){
					$("#EDIT_RECORD_"+i).wrap('<td style="display:none"/>');
					$("#DELETE_RECORD_"+i).wrap('<td style="display:none"/>');
				}
			}
			if(data.flg=="V") {
				displaySuccessMessages("errMsg1", data.errMsg, "");
				clearSuccessMessageAfterFiveSecond("errMsg1");
		    	if (data.fstatus == "N") reloadWinAfterFiveSecond();
		        else if (data.fstatus == "E") showWinAfterFiveSecond();
		        else if (data.fstatus == "D") reloadWinAfterFiveSecond();
		        else if (data.fstatus == "UD") reloadWinAfterFiveSecond();
				
			} else {
				displaySuccessMessages("errMsg2", data.errMsg, "");
				clearSuccessMessageAfterFiveSecond("errMsg2");
		    	if (data.fstatus == "N") showElementAfterFiveSecond("btnSave");
		        else if (data.fstatus == "E") showElementAfterFiveSecond("btnUpdate");
			}
		}, 1000);
	}
//for reload form after 4 sec
function reloadWinAfterFiveSecond() {
    setTimeout(function () {
    	location.reload();
    }, 4000);
}
function showWinAfterFiveSecond() {
	 setTimeout(function () {
	window.location.href = "objective_achievement_details_e.jsp";
	    }, 4000);
}

//function to delete saved attachment 
function deletesavefile(id,AttachName) 
{
	var retVal =""; 
		retVal = confirm("Uploaded File will be deleted permanently. \nAre you sure you want to remove this file?");
	if(retVal == true) {
		deleteuploadedfile(id,AttachName) 
		} else {
			return false;
		}
}

function deleteuploadedfile(id,AttachName) {
	var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST", "ObjectiveAchievementService", true);
    xmlHttp.setRequestHeader("fstatus","UD");
    xmlHttp.setRequestHeader("upld_name",AttachName);
    xmlHttp.setRequestHeader("upld_id",id);	  
    	xmlHttp.send();   
    try{
    	xmlHttp.onreadystatechange = function() {
    		if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
				var data=JSON.parse(this.responseText);
				//saveOrUpdateCommonFunctionInUpload(data);
				cons='	<input type="file" id="upldoc"  name="upldoc" class="" onblur="filechk1(this.id)">'
					+'</font>';	
				$('#file_Upload').html(cons);
				$('#upldoc').val('');
    		}
        }
    } catch (err){
    	alert(err);
    	showElementAfterFiveSecond("btnSave");
		showElementAfterFiveSecond("btnBack");
		showElementAfterFiveSecond("btnReset");
    }
    return true;
}

function editRow(oa_id,typ,r_id,upl_doc) {
	try {
		if(typ=="D"){
			var del=confirm("Are You Sure?")
		    if(del==true){
		    	var xmlHttp = new XMLHttpRequest();
			    xmlHttp.open("POST", "ObjectiveAchievementService", true);
			    xmlHttp.setRequestHeader("oa_id", oa_id);
			    xmlHttp.setRequestHeader("fstatus",typ);
			    xmlHttp.setRequestHeader("upl_doc",upl_doc);
			    xmlHttp.send();
			   
			    try{
			    	xmlHttp.onreadystatechange = function() {
			    		if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
							var data=JSON.parse(this.responseText);
							saveOrUpdateCommonFunctionInUploadDoc(data);
			    		}
			        }
			    } catch (err){
			    	alert(err);
			    	showElementAfterFiveSecond("btnSave");
					showElementAfterFiveSecond("btnBack");
					showElementAfterFiveSecond("btnReset");
			    }
			    return true;
			}
 
		} else {
			document.getElementById("oa_id").value = oa_id;
 			document.getElementById("opt_typ").value = typ;
 			document.getElementById("r_id").value = r_id;
			
			document.frmObjAchievementL.target = "_parent";
 			document.frmObjAchievementL.action = "objective_achievement_details_e.jsp";
			document.frmObjAchievementL.submit();
		}
	} catch (err) {
		alert(err);
	}
} 

function filechk1(id){ 
	 var ext = $('#'+id).val().split('.').pop().toLowerCase();
		if(ext !=""){
			if($.inArray(ext, ['pdf','png','jpg','jpeg','doc','docx']) == -1) {						
				$('#'+id).focus();
				alert("Note: Only .pdf, .jpg, .png, .doc & .docx files will be allowed for uploading.!");
				$('#'+id).val("");
				return false;
			}
		}
		var	fsize=parseInt(($("#"+id)[0].files[0].size / 1024));
		if (fsize > 20480 ) {
			$('#'+id).focus();
			alert("File size should be less than 20 MB!");
			$('#'+id).val("");
			return false;
		}
}
function imgcheck(id){
var ext = $('#'+id).val().split('.').pop().toLowerCase();
	if(ext !=""){
		if($.inArray(ext, ['pdf','png','jpg','jpeg','doc','docx']) == -1) {		
			$('#'+id).focus();
			alert("Note: Only .pdf, .jpg, .png, .doc & .docx files will be allowed for uploading.!");
			$('#'+id).val("");
			return false;
		}
	}
	var	fsize=parseInt(($("#"+id)[0].files[0].size / 1024));
	if (fsize > 20480 ) {
		$('#'+id).focus();
		alert("File size should be less than 20 MB!");
		$('#'+id).val("");
		return false;
	}
}