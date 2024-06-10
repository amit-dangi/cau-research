$(document).ready(function(){
	$(".hidediv").hide();
	$("#btnReset, #btnReset1, #btnReset2").click(function(){
		location.reload();
	});

	$("#btnBack, #btnBack1").click(function(){
		document.location.href = "create_and_manage_expense_details_e.jsp";
	});
	
	$("#resPrps").change(function(){			
		var resPrps = $(this).find('option:selected').val();
		if(resPrps!='')
			getInstallMentDate(resPrps,'','installId');
			//getPurchaseIndent(resPrps, "");
		
	});

	$("#btnView").click(function(){
		if(validateFormFields()){
			//$("#btnView").hide();
			//$("#btnReset1").hide();
			
			$("#finYrId").prop('disabled', true);
			//$("#PiId").prop('disabled', true);
			$("#resPrps").prop('disabled', true);
			//$("#EXPDATE").prop('disabled', true);
			
			var locationCode = $('#Xlocation').val(); 
			var ddoCode = $('#Xddo').val();
			var piid = $("#PiId").val();
			var finYrId = $("#finYrId").val();
			var resPrps = $("#resPrps").val();
			var date	= $("#EXPDATE").val();
			var installId	= $("#installId").val();
			$("#frmCreateManageExpE").attr("target", "btmfrmCreateManageExpE");
			$("#frmCreateManageExpE").attr("action", 'create_and_manage_expense_details_view.jsp?fstatus=V&piId='+piid+"&finYrId="+finYrId+"&resPrps="+resPrps+"&date="+date+"&locationCode="+locationCode+"&ddoCode="+ddoCode+"&installId="+installId);
			$("#frmCreateManageExpE" ).submit();
			
		}
	});
	
	$("#Xlocation").change(function(){	
		var locationcode = $('#Xlocation').val();
		getDdoDetailbyLocation(locationcode);
	});
	
});

function getResearchProposal(locationCode,ddoCode){
	try {
		if(locationCode==''){var locationCode = $('#Xlocation').val(); var ddoCode = $('#Xddo').val();}
		var id=$("#propsalId").val();
		$.ajax({
			type: "POST",
			url: "../RESEARCH/CreateManageExpenseService",
			data:{"fstatus":"RP","locationCode":locationCode,"ddoCode":ddoCode},
			async: false,
			success: function (response){
				var rpData=response.rpData;
				var emp="<option value=''>Select Research Proposal</option>";
				$.each(rpData, function(index, rpData) {					
					if(rpData.RId==id){
						emp+="<option selected value='"+rpData.RId+"'> "+rpData.employeeName+"</option>";
					}else{
						emp+="<option  value='"+rpData.RId+"'> "+rpData.RName+"</option>";
					}
				});
				$(".resPrps").html(emp);
			}
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

function getPurchaseIndent(resPrps, id){
	var hrmsApi = $("#hrmsApi").val().trim();
	var json={userId:$("#user_id").val(), "resPrps":resPrps};
	var sel="";
	try {
		$.ajax({
			type: "GET",
			url: hrmsApi+"rest/snpApiService/getPurchaseIndent",
			jsonp: "parseResponse",
			dataType: "jsonp",
			data:json,
			async: false,
			success: function (response){
				var piId=response.piIdlist;
				var dropdown="<option value=''>Select Purchase Indent</option>";
				$.each(piId, function(index, piId) {
					if(piId.id==id){
						sel="selected";
					} else {
						sel="";
					}
					dropdown+="<option  value='"+piId.id+"'"+sel+">"+piId.name+"</option>";
				});
				$("#PiId").html(dropdown);
			}
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
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
		}else if($('#finYrId').val()==''){
			$('#finYrId').focus();
			showerr($("#finYrId")[0], "Financial Year is required!","block");			
			return false;
		}else if($('#resPrps').val()==''){
			$('#resPrps').focus();
			showerr($("#resPrps")[0], "Research Proposal is required!","block");			
			return false;
		}/*else if($('#PiId').val()==''){
			$('#PiId').focus();
			showerr($("#PiId")[0], "Purchase Indent is required!","block");			
			return false;
		}*/else if($('#EXPDATE').val()==''){
			$('#EXPDATE').focus();
			showerr($("#EXPDATE")[0], "Expense Date is required!","block");			
			return false;
		}/*else if($('#installId').val()==''){
			$('#installId').focus();
			showerr($("#installId")[0], "Installation Date is required!","block");			
			return false;
		}*/
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function IsValidAmt(amt, i, cnt){
	if(amt==''){
		$("#amt_"+i).focus();
		showerr($("#amt_"+i)[0], "Amount is required!","block");			
		return false;
	}
	/*Condition commented as per 21Feb2024 mail by client 
		As per now system will accept negative amount during transaction/expense.*/
	
	/*else if(amt !='' || amt > 0){
		if(cnt>1){
		var rowcnt=	$("#amtIndex_"+i).val();
		var subheadtotal=0;
		for(j=0; j<=rowcnt; j++){
			var ab = $("#amt_"+i).val();
			if(ab == ""  || ab == undefined){
				ab=0;
			}
		subheadtotal =	subheadtotal+parseInt(ab);
		}	
		var appAmt = $("#blncAmt_"+i).val();
		if(parseInt(subheadtotal) >  parseInt(appAmt)){
			$("#amt_"+i).focus();
			showerr($("#amt_"+i)[0], "Sum of SubHead Cannot be greater then Approval Amount!","block");
			return false;
		}			
		}
		else{
		var appAmt = $("#blncAmt_"+i).val();
		if(parseInt(amt) >  parseInt(appAmt)){
			$("#amt_"+i).focus();
			showerr($("#amt_"+i)[0], "Amount Cannot be greater then Approval Amount!","block");
			return false;
		}
			}
	}*/
	
	//remainingAmt(amt, i, cnt);
	
}

function validateFormData(){
	try{
		var indexTotal = $("#index").val();		
		for (var i=0; i<indexTotal; i++) {
			var a=i, aa=0, finalAmt=0;
			aa=++a;
			var indexRow = $("#amtIndex_"+aa).val();
			
			for (var ii=0; ii<indexRow; ii++) {
				var b=ii, bb=0;
				bb=++b;
				var totalAmt = $("#amt_"+aa+"_"+bb).val();				
				finalAmt=parseInt(finalAmt)+parseInt(totalAmt);
				if((totalAmt>0&&($("#blncAmt_"+bb).val()==0))||totalAmt==0&&($("#blncAmt_"+bb).val()==0)){
					displaySuccessMessages("errMsg2","Balance amount is zero", "");
			    	clearSuccessMessageAfterFiveSecond("errMsg2"); 
					return false;
					break;
				}
				if(totalAmt==''){
					$("#amt_"+aa+"_"+bb).focus();
					showerr($("#amt_"+aa+"_"+bb)[0], "Amount is required!","block");			
					return false;
					break;
				}else if(totalAmt !='' || totalAmt > 0){
					var appAmt = $("#blncAmt_"+aa).val();
					/*if(parseInt(finalAmt) >  parseInt(appAmt)){
						$("#amt_"+aa+"_"+bb).focus();
						showerr($("#amt_"+aa+"_"+bb)[0], "Amount Cannot be greator then Approval Amount!","block");
						return false;
						break;
					}*/
				}
			}
		}
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function saveData(savemode){
	try {
		if(validateFormData()){
			executeRecord(savemode);
		}
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

function executeRecord(savemode){
	var locationCode=$('#locationCode').val();
	var ddoCode=$('#ddoCode').val();
	var headList=[];
	var subHeadArray=[];
	var form_data = new FormData(); 
	var amt	=0;
		try{
			var index=$("#index").val();
			for(var i=1; i<=index; i++){				 	
				/*headList.push({					
							
				});*/
				amt=parseInt(amt)+parseInt($("#amt_"+i).val());
				var subHeadIndex=$("#subHeadIndex_"+i).val();
				if(subHeadIndex>0){
					subHeadArray.push({
						"docCount"	:	i,
						"subHeadId" : $("#subHeadId_"+i).val(),
						"subHeadAmt" : $("#amt_"+i).val(),
						 
						"headId"    : $("#headId_"+i).val(),
						"headAmt"    : $("#appAmt_"+i).val(),
						"order_no"  : $("#order_no_"+i).val(),
						"purpose" : $("#purpose_"+i).val(),
						
						"docName" : $("#upldoc_"+i).val()
					});
					
					var hid = $("#headId_"+i).val();
					var sid = $("#subHeadId_"+i).val();
					
					if($("#upldoc_"+i).val()!='' && $("#upldoc_"+i).val()!=undefined){					
						var fileCount = document.getElementById("upldoc_"+i).files.length;
						for (j = 0; j < fileCount; j++) {
							form_data.append("uploadDoc_"+hid+"_"+sid, document.getElementById("upldoc_"+i).files[j]);
						}
					}
			}else{
				subHeadArray.push({
					"docCount"	:	i,
					"subHeadId" : $("#subHeadId_"+i).val(),
					"subHeadAmt" : $("#amt_"+i).val(),
					
					"headId"    : $("#headId_"+i).val(),
					"headAmt"    : $("#appAmt_"+i).val(),
					"order_no"  : $("#order_no_"+i).val(),
					"purpose" : $("#purpose_"+i).val(),
					
					"docName" : $("#upldoc_"+i).val()
				});
				
				var hid = $("#headId_"+i).val();
				var sid = $("#subHeadId_"+i).val();
				if($("#upldoc_"+i).val()!='' && $("#upldoc_"+i).val()!=undefined){					
					var fileCount = document.getElementById("upldoc_"+i).files.length;
					for (j = 0; j < fileCount; j++) {
						form_data.append("uploadDoc_"+hid+"_"+sid, document.getElementById("upldoc_"+i).files[j]);
					}
				}
			}
				console.log("subHeadArray||"+JSON.stringify(subHeadArray));
				var objjson={
					locationCode:locationCode,
					ddoCode:ddoCode,	
					finYrId:$("#finYrId").val(),
					resPrps:$("#resPrps").val(),
					piId:"",
					expDate:$("#EXPDATE").val(),
					appDate : $("#allDt_1").val(),
					install_Id : $("#install_Id").val(),
					//headList:headList,
					subHeadArray:subHeadArray,
					savemode:savemode
				}
			}// 1st for loop 
			if(amt==0 && savemode=='DELETE'){
				displaySuccessMessages("errMsg2", "No Data found for selected Expanse Date", "");
				clearSuccessMessageAfterFiveSecond("errMsg2");
				return false;
				//break;
			}else if(amt==0){
				displaySuccessMessages("errMsg2", "Total Expanse Amount should be greater than is Zero", "");
				clearSuccessMessageAfterFiveSecond("errMsg2");
				return false;
			}
			var encData=encAESData($("#AESKey").val(), objjson);  				     
			console.log("DATA :"+JSON.stringify(objjson));
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "CreateManageExpenseService?fstatus=S", true);
			xmlHttp.setRequestHeader("fstatus", "S");
			xmlHttp.setRequestHeader("encData", encData); 
			xmlHttp.send(form_data);
			
			try{
				xmlHttp.onreadystatechange = function() {
					//$(".btndiv").hide();
		    		if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
						var data1=JSON.parse(this.responseText);
						data=decAESData($("#AESKey").val(), data1);//data fetch from server 
		    			if(data.flg=="V"){
								setTimeout(function () {
									displaySuccessMessages("errMsg1", data.errMsg, "");
								}, 1000);
								    	clearSuccessMessageAfterFiveSecond("errMsg1");
								setTimeout(function(){
									//document.location.href = "create_and_manage_expense_details_e.jsp";
									$("#frmCreateManageExpE").hide();
								}, 3000);
							}else{
								setTimeout(function () {
									displaySuccessMessages("errMsg1", data.errMsg, "");
								    }, 1000);
								    	clearSuccessMessageAfterFiveSecond("errMsg1"); 
								   	setTimeout(function(){ 
				    		    		$(".btndiv").show();
				    	    		}, 5000);
							} 	 								 
		    		}  
				}					
		    } catch (err){
		    	alert(err);
		    }
			
		}catch(err){
			alert("ERROR :"+err);
		}		
}

function filechk(id){
	if($('#'+id).val()!=''){
		var photoSize  = parseInt(($("#"+id)[0].files[0].size / 1024));
		if(!(isJpg($('#'+id).val())||isJpeg($('#'+id).val())||isPng($('#'+id).val())||isPdf($('#'+id).val()))){
			$('#'+id).focus();
			$('#'+id).val('');
			alert("Upload Documents File must be .JPG,.JPEG,.PNG,.PDF!");
			return false;
		}
		else if((photoSize>3072 || photoSize<10)){
			$('#'+id).focus();
			$('#'+id).val('');
			alert("Upload Documents File size should be greater than 10KB and less than 3MB!");
			return false;
		} 
	}
}

var isJpg = function (name) {
    return name.match(/jpg$/i);
};
var isJpeg = function (name) {
	return name.match(/jpeg$/i);
};
var isPng = function (name) {
	return name.match(/png$/i);
};
var isPdf = function (name) {
	return name.match(/pdf$/i);
};


function remainingAmt(amt, i){
	try {
		var rAmt="";
		var rb = parseInt($("#blncAmt_"+i).val());
		var appAmt = parseInt($("#appAmt_"+i).val());
		if(rb!="" || rb!=null){
			rAmt = rb;
		}else{
			rAmt = appAmt;
		}				
		var remianBlnc= parseInt(rAmt)-parseInt(amt);
		$("#blncAmt_"+i).val(remianBlnc);
	} catch (e) {
		// TODO: handle exception
		alert("ERROR ::"+e);
	}
}

//delete upload file
function deleteuploadfile(id,flg,fldr, hid, sid){ 
	var del=confirm("files will be deleted permanently. \nAre you sure you want to remove this file?");
	 	if(del){
			deleteFile(id, flg, fldr, hid, sid); 
		 }
}

function deleteFile(id, fldr, HeadId, hid, sid){
	try{
		
		var xmlHttp = new XMLHttpRequest();
		xmlHttp.open("POST", "CreateManageExpenseService?fstatus=DELETE", true);
		xmlHttp.setRequestHeader("fileFolderName", fldr);
		xmlHttp.setRequestHeader("id", id);
		xmlHttp.send();
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
				var data=JSON.parse(this.responseText);
				
				if(data.flg=="Y"){					
					$("#saveUpload_"+hid+"_"+sid).hide();
					
					setTimeout(function(){ 
						$("#chooseUpload_"+hid+"_"+sid).show();
    	    		}, 2000);
				}
				
			}
		} 
	}catch(e){
		alert(e);
	}
}


function calcIntTotal(rowno){
	
	var totalblnc=$("#balance_"+rowno).val();
	var intrest=$("#intrest_"+rowno).val();
	var gtotal=parseInt(totalblnc==""?"0":totalblnc)+parseInt(intrest==""?"0":intrest);
	$("#total_"+rowno).val(gtotal);
	
}

//**this method is for save head wise total after interest*
function saveheadTotal(){
	var HeadArray=[];
	var form_data = new FormData(); 	
		try{
			var totalindex=$("#totalindex").val();
				for(var m=1; m<=totalindex; m++){
				
					if($("#intrest_"+m).val()==''){
						$("#intrest_"+m).focus();
						showerr($("#intrest_"+m)[0], "Intrest Amount is required!","block");			
						return false;
					}
					if($("#total_"+m).val().includes('-')){
						$("#total_"+m).focus();
						showerr($("#total_"+m)[0], "Negative amount has been entered, which is not allowed while closing!","block");			
						return false;
					}
					
					HeadArray.push({
						"headId" : $("#headId_"+m).val(),
						"subHeadId" : $("#subHeadId_"+m).val(),
						"totalAmt" : $("#total_"+m).val()
					});
				}
				
				var objjson={
					finYrId:$("#finYrId").val(),
					resPrps:$("#resPrps").val(),
					piId:"",
					install_Id:$("#installId").val(),
					headList:HeadArray	
				}
			//console.log("json saveheadTotal");
			//console.log(objjson);
			
			var encData=encAESData($("#AESKey").val(), objjson);  				     
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "CreateManageExpenseService?fstatus=SAVECLOSINGAMT", true);
			xmlHttp.setRequestHeader("fstatus", "S");
			xmlHttp.setRequestHeader("encData", encData); 
			xmlHttp.send(form_data);
			
			try{
				xmlHttp.onreadystatechange = function() {
					/*$(".btndiv").hide();*/
		    		if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
						var data1=JSON.parse(this.responseText);
						data=decAESData($("#AESKey").val(), data1);//data fetch from server 
		    			if(data.flg=="V"){
								setTimeout(function () {
									displaySuccessMessages("errMsg1", data.errMsg, "");
								}, 1000);
								    	clearSuccessMessageAfterFiveSecond("errMsg1");
								setTimeout(function(){
									window.parent.location = "create_and_manage_expense_details_e.jsp";
								}, 3000);
							}else{
								setTimeout(function () {
									displaySuccessMessages("errMsg2", data.errMsg, "");
								    }, 1000);
								    	clearSuccessMessageAfterFiveSecond("errMsg2"); 
								   	setTimeout(function(){ 
				    		    		$(".btndiv").show();
				    	    		}, 5000);
							} 	 								 
		    		}  
				}					
		    } catch (err){
		    	alert(err);
		    }
			
		}catch(err){
			alert("ERROR :"+err);
		}		
}

function showdiv(){
	$("#btnshowdiv").hide();
	$("#manageClosingblnc").show();
	$("#btnSavehead").show();
	$("#CreateManageExp").hide();
	$("#btnSave").hide();
	
}

function IsInteger(sText) {
    var ValidChars = "0123456789";
    var IsNumber=true;
    var v = 0;
    var Char;
    for (i = 0; i < sText.value.length && IsNumber == true; i++) { 
      Char = sText.value.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) {
        //alert("Value must be numeric");
        showerr(sText,"Value must be numeric.","block");
        sText.focus();
        return false;
      }
    }
    v = sText.value;
    if(v!="") {
      if (v => 0) {
        return IsNumber;
      } else {
        //alert("Value must be greater than zero(s)");
        showerr(sText,"Value must be greater than zero(s)","block");
        sText.focus();
        return false;
      }
    }  
  }
function revertBack(){
	displaySuccessMessages("errMsg2", "Entry not found in Head and SubHead Wise Fund Allocation Page", "");
	clearSuccessMessageAfterFiveSecond("errMsg2"); 
	}
function revertBack1(){
	displaySuccessMessages("errMsg2", "Fund not Alloted in this selected Financial year", "");
	clearSuccessMessageAfterFiveSecond("errMsg2"); 
	}
