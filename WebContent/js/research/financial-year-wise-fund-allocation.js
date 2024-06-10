 
$(document).ready(function(){
	//$(".hidediv").hide();
	$("#btnSave").hide();
 $("#tableHide").hide();
	$("#addRow").hide();
	$('#addRow').on('click', function (e) {
		addRow();
	}); 
	$("#btnReset, #btnReset1, #btnReset2").click(function(){
		location.reload();
	});

	$("#btnBack, #btnBack1").click(function(){
		document.location.href = "financial_year_wise_fund_allocation_e.jsp";
	});
	
	$("#resPrps").change(function(){			
		var resPrps = $(this).find('option:selected').val();
		if(resPrps!='')
			//getPurchaseIndent(resPrps, "");
		var fin_yr= $("#fin_yr").val();
		getInstallMentDate(resPrps,'','installId',fin_yr);
	});
	$("#installId").change(function(){
		$("#headwisetotal").text("");
		$("#fin_yr").prop('disabled', true);
		$('#resPrps').prop('disabled', true);
		$("#btnSave").show();
		var instId = $(this).find('option:selected').val();
		getInstallMentAmount(instId);
	});
	
	$("#btnView1").click(function(){
		if(validateFormFields()){
			$(".hidediv").show();
			//$("#btnView").hide();
			$("#btnReset1").hide(); 
			var res=$("#resPrps option:selected" ).text();
			var dt=$("#XALLDATE").val();
			var piid = $("#PiId").val(); 
			if(piid !='')
				getHeadDataList(piid, res, dt);			
		}
	});
	
	$("#btnSearch").click(function(){
		var resPrps=$("#resPrpsS").val();
		
		$("#frmfundAllocationE").attr("target", "btmfrmfundAllocationE");
		$("#frmfundAllocationE").attr("action", 'financial_year_wise_fund_allocation_l.jsp?fstatus=V&resPrps='+resPrps);
		$("#frmfundAllocationE" ).submit();
	});
	
	$("#btnSave").click(function(){
		if(validateFormFields()){
			try {
				var locationCode = $('#Xlocation').val(); 
				var ddoCode = $('#Xddo').val();
				var workarray = [];
				var headSubHead = [];
				var isContainsHeadSubHead = [];
				var index=parseInt($('#count').val());
				var InAmount=parseInt($('#installAmt1').val());
				var TotalAmt=0;
				for(i=1; i<=parseInt(index); i++){
				
					 if(($('#head_'+i).val()=="" || $('#head_'+i).val()==null) && $('#head_'+i).val()!=undefined)
						{
							   $('#head_'+i).focus();
							   showerr($('#head_'+i)[0],"Head is required","block");
							   return false;
						}   if(($('#shead_'+i).val()=="" || $('#shead_'+i).val()==null) && $('#shead_'+i).val()!=undefined)
						{
							   $('#shead_'+i).focus();
							   showerr($('#shead_'+i)[0],"Sub Head is required","block");
							   return false;
						}    if(($('#amount_'+i).val()=="" || $('#amount_'+i).val()==null) && $('#amount_'+i).val()!=undefined)
						{
							   $('#amount_'+i).focus();
							   showerr($('#amount_'+i)[0],"Amount is required","block");
							   return false;
							    
						}  
					
						headSubHead.push($('#head_'+i).val()+"_"+$('#shead_'+i).val())
					/*if($('#amt'+i).val()==''){
						displaySuccessMessages("errMsg2", "Amount is required", "");
						clearSuccessMessageAfterFiveSecond("errMsg2");
						
						$('#amt'+i).focus();
						showerr($("#amt"+i)[0], "Amount  Title is required!","block");
						return false;
					}*/
				 if(($('#head_'+i).val()!="" || $('#head_'+i).val()!=null) && $('#head_'+i).val()!=undefined)
				 {
					 amount=	parseInt($('#amount_'+i).val());   
					 TotalAmt=TotalAmt+amount;	 
					 var mapedJson={
							amt		: $("#amount_"+i).val(), 
							headId	: $("#head_"+i).val(),
							sheadId	: $("#shead_"+i).val()
							}
					workarray.push(mapedJson);
				}
				}
				for(var k=0; k<=headSubHead.length; k++){
					
					//alert(headSubHead[k])
					if(isContainsHeadSubHead.includes(headSubHead[k])){
						displaySuccessMessages("errMsg2", "more then one Sub Heads can not map with same Head", "");
						clearSuccessMessageAfterFiveSecond("errMsg2");
						return false;
					}
					isContainsHeadSubHead.push(headSubHead[k]);
				}
				
				if(InAmount<TotalAmt){
					displaySuccessMessages("errMsg2", "Sum of Heads wise Amount is grater than Total Available Amount for selected Installment Date", "");
					clearSuccessMessageAfterFiveSecond("errMsg2");	
					return false;
				}
				if(InAmount>TotalAmt){
					displaySuccessMessages("errMsg2", "Heads wise Allocation is not equall to Total Available Amount for selected Installment Date", "");
					clearSuccessMessageAfterFiveSecond("errMsg2");	
					return false;
				}
				//alert("TotalAmt --"+TotalAmt);
				var jsonObject={locationCode:locationCode,ddoCode:ddoCode,
						"resPrps":$("#resPrps").val(), "PiId":$("#PiId").val(), //"FundAgency":$("#FundAgency").val(), 
						"XALLDATE":$("#XALLDATE").val(), "remark":$("#remark").val(), "finYr": $("#fin_yr").val(),"installId":$("#installId").val(), 
						"workarray": JSON.stringify(workarray), mode: "S"};
				
				//console.log("Sending jsonObject :"+JSON.stringify(jsonObject));				
				var encData=encAESData($("#AESKey").val(), jsonObject);	
				
				$('#btnSave').hide();
				$('#btnReset').hide();
				
				$.ajax({
					type: "POST",
					url: "../FundAllocationService",
					data: {encData: encData, fstatus: "S"},
					dataType: "json",
					success: function (response) {
						var data= decAESData($("#AESKey").val(), response);
						$('#btnSave').show();
						$('#btnReset').show();
						if(data.flag=="Y"){
							setTimeout(function() {
								displaySuccessMessages("errMsg1", data.status, "");							
							}, 2000);
							clearSuccessMessageAfterFiveSecond("errMsg1");
						}else{
							setTimeout(function() {
								displaySuccessMessages("errMsg2", data.status, "");							
							}, 2000);
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
						
						$('#btnSave').show();
						$('#btnReset').show();
					}
				});	
			} catch (e) {
				// TODO: handle exception
				alert("error :"+e);
			}
		}
	});
	
	$("#Xlocation").change(function(){	
		var locationcode = $('#Xlocation').val();
		getDdoDetailbyLocation(locationcode);
	});

	$(document.body).on('change',".headclass",function (e) { 
		var hId = $(this).find('option:selected').val();
	 	var indexval=this.id;
 		const myArray = indexval.split("_");
		let index = myArray[1]; 
		getSubHeadNameList(hId, index,'','shead');
		//getSubHead(hId, index);
	});
});



function getInstallMentAmount(Id) {
	    $.ajax({ 
    	type: "POST",
	url: "../FundAllocationService",
	data:{"fstatus":"amt","Id":Id},
    async: false,
    success: function (response)
    { 		
         $("#installAmt").text(response.amt);
         $("#installAmt1").val(response.amt);
         var flg=response.flg;
          var index=0;
         $(".slist").html('');
        if(flg=='Y'){ 
        	 $.ajax({ 
     	    	type: "POST",
     		url: "../FundAllocationService",
     		data:{"fstatus":"getList","Id":Id,"resPrps":$("#resPrps").val()},
             async: false,
             success: function (response)
             { 		
            	 $("#tableHide").show();
            	$("#addRow").show();
      	            if (typeof response.fundinglist != 'undefined' && response.fundinglist.length > 0)
                 {
                     $.each(response.fundinglist, function (key, val) {
                         var fund_allc_did 	= val.fund_allc_did;
                         var head_id 		= val.head_id;
                         var subhead_id 	= val.subhead_id;
                         var amount 		= val.amount;
                          var flag = val.flag;
                         index=index+1;
                         var disabled=(flag=='Y'?"disabled":"")
                         var newRow = $("<tr>");	 
             			var cols = '<tr >'
             				
             				    +' <td style="text-align:center; width:3%;" id="srn_'+index+'">'+index+'</td>'
             				
             					+' <td style="text-align:center; width:10%;">'
             					+' <select '+disabled+' class="form-control headclass" id="head_'+index+'" name="head_'+index+'"onchange="getsubHead11(this.value, '+index+')">'
             					+' <option value="">Select Head Name</option>'
             					//+ ' QueryUtil.getComboOption("rsrch_research_head", "head_id", "head_Name", "", "is_active='Y'", "head_Name")  
             					+' </select>'
             					+'</td>'
             					
             					+' <td style="text-align:center; width:10%;">'
             					+' <select '+disabled+' class="form-control" id="shead_'+index+'" name="shead_'+index+'" >'
             					+' <option value="">Select Sub Head Name</option>'
             					+' </select>'
             					+'</td>'
             					+' <td style="text-align:center; width:10%;">'
             					+' <input type="text" class="form-control" id="amount_'+index+'" name="amount_'+index+'" placeholder="Enter Amount"  value ='+amount+' onblur="calcAllocation();"></td>'
             					
             					+' <td id="pc_id_'+index+'"  style="text-align:center;color:RED;cursor:pointer; width:6%;" onclick="deleteDetailsdata('+fund_allc_did+','+index+',this,'+"'"+''+""+flag+''+"'"+')"><i class="fa fa-trash p-l-3"> Delete' //
//             					+' <span onclick="deleteDetailsdata('+"''"+','+index+',this)">'
             		//			+' <i class="fa fa-trash-o"></i> Delete</span>'
             					+' <input type="hidden" id="detId_'+index+'" value=""/>'
             					+'</td>'
             					
             					+'</tr>';		
             				$('.slist').append(cols);
             				$("#count").val(index);
              				/*if(flag=='Y'){
              					$("#btnSave").hide();
              				}*/
             				//getHead(index,head_id);  
             				getHeadNameList(head_id,index,'head');
             				getSubHeadNameList(head_id, index,subhead_id,'shead');
             				//getSubHead(head_id, index,subhead_id);
                         
                         
                      });
                  } else{
                	  $("#count").val(0);
                  }
             }
         });
        }else{
       // $(".Hhide").hide();	
        	 $("#tableHide").show();
        	$("#addRow").show();
        	  index=index+1;
        	 var newRow = $("<tr>");	 
  			var cols = '<tr >'
  				
  				    +' <td style="text-align:center; width:3%;" id="srn_'+index+'">'+index+'</td>'
  				
  					+' <td style="text-align:center; width:10%;">'
  					+' <select class="form-control headclass" id="head_'+index+'" name="head_'+index+'"onchange="getsubHead11(this.value, '+index+')">'
  					+' <option value="">Select Head Name</option>'
  					//+ ' QueryUtil.getComboOption("rsrch_research_head", "head_id", "head_Name", "", "is_active='Y'", "head_Name")  
  					+' </select>'
  					+'</td>'
  					
  					+' <td style="text-align:center; width:10%;">'
  					+' <select class="form-control" id="shead_'+index+'" name="shead_'+index+'" >'
  					+' <option value="">Select Sub Head Name</option>'
  					+' </select>'
  					+'</td>'
  					+' <td style="text-align:center; width:10%;">'
  					+' <input type="text" class="form-control" id="amount_'+index+'" name="amount_'+index+'" placeholder="Enter Amount" onblur="calcAllocation();"></td>'
  					
  					+' <td id="pc_id_'+index+'"  style="text-align:center;color:RED;cursor:pointer; width:6%;" onclick="deleteDetailsdata(-1,'+index+',this)"><i class="fa fa-trash p-l-3"> Delete'
//  					+' <span onclick="deleteDetailsdata('+"''"+','+index+',this)">'
  		//			+' <i class="fa fa-trash-o"></i> Delete</span>'
  					+' <input type="hidden" id="detId_'+index+'" value=""/>'
  					+'</td>'
  					
  					+'</tr>';		
  				$('.slist').append(cols);
  				$("#count").val(index); 
  				//getHead(index,'');
  				getHeadNameList('',index,'head');
        } 
         
    }
});
}

function getResearchProposal(locationCode,ddoCode){
	try {
		if(locationCode==''){var locationCode = $('#Xlocation').val(); var ddoCode = $('#Xddo').val();}
		var id=$("#propsalId").val();
		$.ajax({
			type: "POST",
			url: "../FundAllocationService",
			data:{"fstatus":"RP","locationCode":locationCode,"ddoCode":ddoCode},
			async: false,
			success: function (response){
				console.log("response.employee");
				console.log(response.employee);
				var employee=response.employee;
				console.log(employee);
				console.log("employee employeeId||"+employee[0].employeeId);
				console.log("employee employeeName||"+employee[0].employeeName);
				var emp="<option value=''>Select Research Proposal</option>";
				$.each(employee, function(index, employee) {					
					if(employee.employeeId==id){
						emp+="<option selected value='"+employee.employeeId+"'> "+employee.employeeName+"</option>";
					}else{
						emp+="<option  value='"+employee.employeeId+"'> "+employee.employeeName+"</option>";
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
		}else if($('#fin_yr').val()==''){
			$('#fin_yr').focus();
			showerr($("#fin_yr")[0], "Financial Year is required!","block");			
			return false;
		}/*else if($('#FundAgency').val()==''){
			$('#FundAgency').focus();
			showerr($("#FundAgency")[0], "Fund Agency is required!","block");			
			return false;
		}*/else if($('#resPrps').val()==''){
			$('#resPrps').focus();
			showerr($("#resPrps")[0], "Research Proposal is required!","block");			
			return false;
		}else if($('#PiId').val()==''){
			$('#PiId').focus();
			showerr($("#PiId")[0], "Purchase Indent is required!","block");			
			return false;
		}/*else if($('#XALLDATE').val()==''){
			$('#XALLDATE').focus();
			showerr($("#XALLDATE")[0], "Allocation Date is required!","block");			
			return false;
		}*/
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function deleteRecord(id){	
	try {	
		var r = confirm("Are You Sure!");
		if (r == true){ 		
			var jsonObject={"id":id};   	 	
			$.ajax({
				type: "POST",
				url: "../FundAllocationService?fstatus=D",
				data: {jsonObject: JSON.stringify(jsonObject) },
				dataType: "json",
				success: function (data) {		         
					var tRec = $("#totRow").val();
					for(var i=1;i<=tRec;i++){
						$("#VIEW_RECORD_"+i).wrap('<td style="display:none"/>');
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

function OpenModel(id) {
	try {
		$('#reportDiolog', window.parent.document).modal('show');
		$("#frmfundAllocationL").attr("action", "financial_year_wise_fund_allocation_e.jsp?fstatus=V&faid="+id);
		$("#frmfundAllocationL").attr("target", "1_Report");
		$("#frmfundAllocationL").submit();
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+err);
	}
}

function editRecord(id){
	try {
		$('#frmfundAllocationL').attr('action', 'financial_year_wise_fund_allocation_e.jsp?fstatus=E&faid='+id);
		$("#frmfundAllocationL").attr("target", '_parent');
		$("#frmfundAllocationL" ).submit();
	} catch (err) {
		alert("ERROR :"+err);
	}
}

function unhide(a, id){
	try{
		if(a=="E"){
			var resPrps = $("#resPrps").val();
			if(resPrps!='')
				getPurchaseIndent(resPrps, id);
			
			setTimeout(function() {
				$("#btnView").click();							
			}, 500);			
		}
	} catch (err) {
		alert("ERROR :"+err);
	}
}

function getPurchaseIndent(resPrps, id){
	var hrmsApi = $("#hrmsApi").val().trim();
	//alert("hrmsApi---- "+hrmsApi);
	var json={userId:$("#user_id").val(), "resPrps":resPrps};
	console.log("hrmsApi-"+hrmsApi);
	console.log(json);
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

/*function getHeadDataList(id, res, dt){
	var hrmsApi = $("#hrmsApi").val().trim();
	var json={userId:$("#user_id").val(), "piId":id};
	var sel="", index=0;
	try {
		$.ajax({
			type: "GET",
			url: hrmsApi+"rest/snpApiService/getResearchHeadList",
			jsonp: "parseResponse",
			dataType: "jsonp",
			data:json,
			async: false,
			success: function (response){
				var headList=response.piIdlist;
				$.each(headList, function(index, headList) {
					var name="";
					if(headList.name=="1"){
						name=getHeadName(headList.id);
					}else{
						name=headList.name;
					}					
					index=index+1;
					var newRow = $("<tr>");			
					var cols = '<tr >'
						+'<td style="text-align:center; width:5%;">'+index+'</td>'
						+'<td style="text-align:center; width:20%;"><input type="hidden" class="form-control" id="proName1" name="proName1" class="proName" value="'+res+'">'+res+'</td>'
						+'<td style="text-align:center; width:10%;"><input type="hidden" class="form-control" id="allDt1" name="allDt1" class="allDt" value="'+dt+'">'+dt+'</td>'
						+'<td style="text-align:center; width:15%;"><input type="hidden" class="form-control" id="headId_'+index+'" name="headId_'+index+'" value="'+headList.id+'">'+name+'</td>'
						+'<td style="text-align:center; width:15%;"><input type="text" class="form-control" id="amt_'+index+'" name="amt_'+index+'" onblur="IsInteger(this);" value="'+headList.amount+'"></td>'						
						+'</tr>'
					$('#stable').append(cols);
					$('#index').val(index)
			});
				//$("#PiId").html(dropdown);
			}
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}*/

function getHeadName(hid){
	try {		
		$.ajax({
			type: "POST",
			url: "../FundAllocationService",
			data:{"fstatus":"HN", "h_id":hid},
			async: false,
			success: function (response){
				var headName=response.headName;
					//alert("headName :"+JSON.stringify(headName));
			}
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

function getHeadDataList(id, res, dt){
	var hrmsApi = $("#hrmsApi").val().trim();
	var json={userId:$("#user_id").val(), "piId":id};
	var sel="", index=0;
	try {
		$.ajax({
			type: "POST",
			url: "../FundAllocationService",
			data:{"fstatus":"HL", "pi_id":id},
			async: false,
			success: function (response){
				
				var headList=response.piIdlist;
				var gtotal=0;
				$('#stable').html("");
				$('#totaldata').html("");	
				$.each(headList, function(index, headList) {
					var name="";$("#btnSave").show()
					//alert(headList.EXP_HEAD_MID);
					if(!headList.EXP_HEAD_MID==""||!headList.EXP_HEAD_MID==null){
						$("#btnSave").hide()
					}
					if(headList.name=="1"){
						name=getHeadName(headList.id);
					}else{
						name=headList.name;
					}					
					index=index+1;
					var newRow = $("<tr>");	
					gtotal=parseFloat(gtotal)+parseFloat(headList.amount);
					var cols = '<tr >'
						+'<td style="text-align:center; width:5%;">'+index+'</td>'
						/*+'<td style="text-align:center; width:20%;"><input type="hidden" readonly="readonly" class="form-control" id="proName1" name="proName1" class="proName" value="'+res+'">'+res+'</td>'*/
						//+'<td style="text-align:center; width:10%;"><input type="hidden" class="form-control" id="allDt1" name="allDt1" class="allDt" value="'+dt+'">'+dt+'</td>'
						+'<td style="text-align:center; width:15%;"><input type="hidden" readonly="readonly" class="form-control" id="headId_'+index+'" name="headId_'+index+'" value="'+headList.id+'">'+name+'</td>'
						+ '<td style="text-align:center; width:15%;"></td>'
						+'<td style="text-align:center; width:15%;"><input type="text" class="form-control" onchange="totalVal();" maxlength="10" id="amt_'+index+'" name="amt_'+index+'" onblur="numberOrDecimalNumber(this);" value="'+headList.amount+'" style="text-align:right;"></td>'						
						+'</tr>'
					$('#stable').append(cols);
					$('#index').val(index)
			});
				
				var totalcols ='<div class="col-sm-12" id="totaldata">'
								+'<div class="col-sm-2"></div>'
								+'<div class="col-sm-4" style="text-align:center;"><b>Head Total</b></div>'
								+'<div  class="col-sm-6" style="text-align:right;"><b>'+gtotal.toFixed(2)+'</b></div></div>'	
								$('#totaldata').append(totalcols);	
				
				/*var totalcols = '<tr>'
					+'<td style="text-align:center; width:5%;">-</td>'
					+'<td style="text-align:center; width:15%;"><b>Head Total</b></td>'
					+'<td style="text-align:right; width:15%;"><b>'+gtotal+'</td></b></tr>'
					$('#stable').append(totalcols);*/
				//$("#PiId").html(dropdown);
			}
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}
function totalVal(){
	
	var index=$("#index").val();
	var gtotal=0;
	$('#totaldata').html("");	
	for(var i=1;i<=index;i++){
		gtotal=parseFloat(gtotal)+parseFloat($("#amt_"+i).val());
		//alert("functigtotalon"+gtotal);
	}
	var totalcols ='<div class="col-sm-12" id="totaldata">'
		+'<div class="col-sm-2"></div>'
		+'<div class="col-sm-4" style="text-align:center;"><b>Head Total</b></div>'
		+'<div  class="col-sm-6" style="text-align:right;"><b>'+gtotal.toFixed(2)+'</b></div></div>'	
		$('#totaldata').append(totalcols);	
	//$('#totaldata').html(gtotal);	
}

function addRow(){   
	//$("#addModel").click(function(){
		var index=parseInt($('#count').val());
		//alert("index:"+index);
	 	 if(($('#head_'+index).val()=="" || $('#head_'+index).val()==null) && $('#head_'+index).val()!=undefined)
		{
			   $('#head_'+index).focus();
			   showerr($('#head_'+index)[0],"Head is required","block");
			   return false;
		}   if(($('#shead_'+index).val()=="" || $('#shead_'+index).val()==null) && $('#shead_'+index).val()!=undefined)
		{
			   $('#shead_'+index).focus();
			   showerr($('#shead_'+index)[0],"Sub Head is required","block");
			   return false;
		}    if(($('#amount_'+index).val()=="" || $('#amount_'+index).val()==null) && $('#amount_'+index).val()!=undefined)
		{
			   $('#amount_'+index).focus();
			   showerr($('#amount_'+index)[0],"Amount is required","block");
			   return false;
		}  
			var text="";
			for(var i=index;i>=0;i--)
			{
				if($("#srn_"+i).text()!=undefined && $("#srn_"+i).text()!='')
				{	
					text=$("#srn_"+i).text();
					if(text!='')
						break;
				}else{
					text=0;	
				}
			}
			index=index+1;
			//alert("index :: "+index);
			var newRow = $("<tr>");	 
			var cols = '<tr >'
				
				    +' <td style="text-align:center; width:3%;" id="srn_'+index+'">'+(parseInt(text)+1)+'</td>'
				
					+' <td style="text-align:center; width:10%;">'
					+' <select class="form-control headclass" id="head_'+index+'" name="head_'+index+'"onchange="getsubHeadq(this.value, '+index+')">'
					+' <option value="">Select Head Name</option>'
					//+ ' QueryUtil.getComboOption("rsrch_research_head", "head_id", "head_Name", "", "is_active='Y'", "head_Name")  
					+' </select>'
					+'</td>'
					
					+' <td style="text-align:center; width:10%;">'
					+' <select class="form-control" id="shead_'+index+'" name="shead_'+index+'" >'
					+' <option value="">Select Sub Head Name</option>'
					+' </select>'
					+'</td>'
					+' <td style="text-align:center; width:10%;">'
					+' <input type="text" class="form-control" id="amount_'+index+'" name="amount_'+index+'" placeholder="Enter Amount" onblur="calcAllocation();"></td>'
					
					+' <td id="pc_id_'+index+'"  style="text-align:center;color:RED;cursor:pointer; width:6%;" onclick="deleteDetailsdata(-1,'+index+',this)"><i class="fa fa-trash p-l-3"> Delete'
//					+' <span onclick="deleteDetailsdata('+"''"+','+index+',this)">'
		//			+' <i class="fa fa-trash-o"></i> Delete</span>'
					+' <input type="hidden" id="detId_'+index+'" value=""/>'
					+'</td>'
					
					+'</tr>';		
				$('.slist').append(cols);
				$("#count").val(index);
	     	
				//getHead(index,'');
				getHeadNameList('',index,'head');
}
function deleteDetailsdata(id,index,model_id,flag)
{  
	if(flag!='Y'){
	var rowcountAfterDelete = document.getElementById("stable").rows.length; 
	var rowcountAfterDelete1 = parseInt($("#count").val());
	if(true)
	{  
		var chk=0;
		if(id!='-1'&& id!=undefined)
		{
			if(!deletemastdata(id))
				chk=1;
		}	
		/*if(chk==0)
		{*/
			var x=parseInt($("#srn_"+index).text());
			for(var i=parseInt(index);i<=rowcountAfterDelete1;i++)
			{
				if($("#srn_"+(i+1)).text()!=undefined && $("#srn_"+(i+1)).text()!="" )
				{	
					$("#srn_"+(i+1)).text(x);
					++x;
				}
			}
			$(model_id).parents("tr").remove();
		//}
		//$("#count").val(index-1);
	}
	else
	{ 
		displaySuccessMessages("errMsg2", "At least 1 rows should be present in the table", "");
		clearSuccessMessageAfterFiveSecond("errMsg2"); 
	}
	}else{
		displaySuccessMessages("errMsg2", "Amount can't be delete as Expense is already saved For this head", "");
		clearSuccessMessageAfterFiveSecond("errMsg2"); 
	}
}
function deletemastdata(id) {
	var retVal = confirm("Data will be deleted permanently. \nAre you sure you want to remove this Data?");
	if (retVal == true) {
		try {
			$.ajax({
				type : "POST",
				//url : "StockInwardBatchService",
				url: "../FundAllocationService",
				data : {
					id : id,
					fstatus : 'RD',
				},
				success : function(data) {
					//reloadWinAfterFiveSecond();
				},
				error : function() {
					alert("Error");
				}
			});
		} catch (err) {
			alert(err);
		}
		return true;
	} else {
		return false;
	}
}
 
function calcAllocation(){
	var index=parseInt($('#count').val());
	var InAmount=parseInt($('#installAmt1').val());
	var TotalAmt=0;
	for(i=1; i<=parseInt(index); i++){
	 if(($('#head_'+i).val()!="" || $('#head_'+i).val()!=null) && $('#head_'+i).val()!=undefined)
					{
						 amount=	parseInt($('#amount_'+i).val());   
						 TotalAmt=TotalAmt+amount;
					}
		}
	$('#headwisetotal').text("Total :"+TotalAmt);
}