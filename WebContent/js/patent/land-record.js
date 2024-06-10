/**
 * @author Amit dangi
 */
$(document).ready(function(){
	$("#btnReset").click(function(){
		location.reload();
	});
	$("#btnReset1").click(function(){
		location.reload();
	});
	$("#xbtnReset").click(function(){
		document.getElementById("frmLandRecordD").reset();
	});
	
	$("#X_BTNSEARCH").click(function() {
		$('#frmLandRecordD').attr('action', 'land_record_l.jsp');
		$("#frmLandRecordD").attr("target", 'LandRecordFrame');
		$("#frmLandRecordD").submit();
	});
	   
	
	$("#Xlocation").change(function(){	
		var locationcode = $('#Xlocation').val();
		getDdoDetailbyLocation(locationcode);
	});
	$("#location").change(function(){	
		var location = $('#location').val();
		getDdoDetailbyLocation(location,'','ddo');
	});
	
	//getLocationDetail('','Xlocation');
	//click of Add more button new row will be create 
	  $("#addLandRecord").click(function(){
			var index=parseInt($('#count').val());
				var text="";
				for(var i=index;i>=0;i--)
				{
					if($('#XUPLDOC_'+index).val()!=""&& $('#XUPLDOC_'+index).val()!=null){
					if(($('#payment_date_'+index).val()=="" || $('#payment_date_'+index).val()==null) && $('#payment_date_'+index).val()!=undefined)
					{
						   $('#payment_date_'+index).focus();
						   showerr($('#payment_date_'+index)[0],"Payment date is required","block");
						   return false;
				    }}if($('#payment_date_'+index).val()!=""&& $('#payment_date_'+index).val()!=null){
						if(($('#XUPLDOC_'+index).val()=="" || $('#XUPLDOC_'+index).val()==null) && $('#XUPLDOC_'+index).val()!=undefined)/*&&($("#fstatus").val()=="N")*/
						{
							   $('#XUPLDOC_'+index).focus();
							   showerr($('#XUPLDOC_'+index)[0],"Upload Payment Receipt is required","block");
							   return false;
					    }}
				    
					if($("#landRecord_"+i).text()!=undefined && $("#landRecord_"+i).text()!="")
					{	
						text=$("#landRecord_"+i).text();
						if(text!='')
							break;
					}
				}
				index=index+1;
				var newRow = $("<tr>");	 
				var cols = '<tr >'
						+' 	<td  class="text-center w-10" id="landRecord_'+index+'" >'+(parseInt(text)+1)+'</td>'
						+'	<td  class="text-center"><div class="input-group date" id="msg-proj_start_date">'
						+' 	<div class="input-group-addon">'
						+'	<i class="fa fa-calendar"></i>'
						+'	</div>'
						+'	<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar payment_date" '
						+'	id="payment_date_'+index+'" name="payment_date_'+index+'" placeholder="DD/MM/YYYY" value="" >'
						+'	</div>	</td> '
						+'	<td  class="text-center"> <input type="file" class="" id="XUPLDOC_'+index+'" name="XUPLDOC_'+index+'" value="" placeholder=""> </td>'
					  	+' 	<td id="s_id_'+index+'" style="text-align:center;color:RED;cursor:pointer; width:6%;" onclick="deleteDetailsdata(-1,'+index+',this,'+"'"+''+"landRecord"+''+"'"+',"")" > <i class="fa fa-trash p-l-3"></i>Delete'
						+' 	<input type="hidden" id="detId_'+index+'" value=""/>'
						+'	</td>'
						
						+'</tr>';		
					$('.landRecord').append(cols);
					$("#count").val(index);
					
					$(".payment_date").datepicker({
						format : 'dd/mm/yyyy',
						autoclose : true,
					}).on('click', function() {
						$(this).datepicker("setDate", '');
					}).on('clearDate', function(selected) {
					});
		});	
	  var fstatus=$("#fstatus").val();
	  if(fstatus=='E'){
 		  var jsondata=$("#jsonddata").val();  
 		  var obj = jQuery.parseJSON(jsondata); 
		  $.each(obj, function (index, value) {
			 des="";
			 
			  index=index+1;
			  if(index==1){
				  des="disabled";
			  }if(index==''){
				  index=1;
			  }
 			  var det_id=value["det_id"].toString();
			  var ld_id=value["ld_id"].toString();
			  var file_name=value["file_name"].toString();
			  var REV_PAY_DATE=value["REV_PAY_DATE"].toString();
			  var newRow = $("<tr>");	 
				var cols = '<tr >'
					+' 	<td  class="text-center w-10" id="landRecord_'+index+'" >'+(parseInt(index))+'</td>'
					+'	<td  class="text-center"><div class="input-group date" id="msg-proj_start_date">'
					+' 	<div class="input-group-addon">'
					+'	<i class="fa fa-calendar"></i>'
					+'	</div>'
					+'	<input readonly="readonly" data-field-id=" " type="text" class="form-control datecalendar payment_date" '
					+'	id="payment_date_'+index+'" name="payment_date_'+index+'" placeholder="DD/MM/YYYY" value="'+REV_PAY_DATE+'" >'
					+'	</div>	</td> '
					+'<td>'
					+'<span class="fileUpload" id="file_Upload_'+index+'">'
					+'<a target="_blank" href="../downloadfile?filename='+file_name+'&folderName=RSRCH/LAND_RECORD/'+ld_id+'">' 
					+'	<u>'+file_name+'</u> </a>&ensp; '
					if(ld_id!=""&&(file_name!="")){ 
						cols+='<input type="hidden" class="form-control" id="savedDoc_'+index+'" name="savedDoc_'+index+'" value="'+file_name+'" onblur="imgcheck(this.id)">'
					}else{							
						cols+='<input type="file" class="form-control" id="XUPLDOC_'+index+'" name="XUPLDOC_'+index+'" value="'+file_name+'" onblur="imgcheck(this.id)">'
					}
				cols+=' <td id="cmd_'+index+'" style="text-align:center;color:RED;cursor:pointer; width:6%;" onclick="deleteDetailsdata('+"'"+''+''+ld_id+''+"'"+','+index+',this,'+"'"+''+"landRecord"+''+"'"+','+"'"+''+''+file_name+''+"'"+','+"'"+''+''+det_id+''+"'"+')"><i class="fa fa-trash p-l-3"></i>Delete'
					+' <input type="hidden" id="detId_'+index+'" value="'+det_id+'"/>'					
					+'</td>'
					+'</tr>'; 
					$('.landRecord').append(cols);
					$("#count").val(index);
					$(".payment_date").datepicker({
						format : 'dd/mm/yyyy',
						autoclose : true,
					}).on('click', function() {
						$(this).datepicker("setDate", '');
					}).on('clearDate', function(selected) {
					});
			}); 
	  }
});
function validateFormFields(){
	try{
	
		if($('#Xlocation').val().trim()==''){
			$('#Xlocation').focus();
			showerr($("#Xlocation")[0], "Location is required!","block");			
			return false;
		}else if($('#Xddo').val()==''){
				$('#Xddo').focus();
				showerr($("#Xddo")[0], "DDO is required!","block");			
				return false;
		}else if($('#deptId').val()==''){
			$('#deptId').focus();
			showerr($("#deptId")[0], "Department is required!","block");			
			return false;
		}else if($('#XPATTA').val()==''){
			$('#XPATTA').focus();
			showerr($("#XPATTA")[0], "Patta No is required!","block");			
			return false;
	}else if($('#fstatus').val().trim()=='N' && $('#XUPLDOC').val().trim()==''){
		$('#XUPLDOC').focus();
		showerr($("#XUPLDOC")[0], "Upload Document is required!","block");			
		return false;	
}
	}catch(e){
		alert("ERROR :"+e);
	}
	return true;
}

function save(){
	if(validateFormFields()){
	try {
			var form_data = new FormData();
			var multiple_form_data = new FormData();
			var workarray = [];
			
				if($("#XUPLDOC").val()!="" && $("#XUPLDOC").val()!=undefined){
					var ext = $("#XUPLDOC").val().split('.').pop().toLowerCase();
					
					if(ext !=""){
						if($.inArray(ext, ['pdf']) == -1) {						
							$('#XUPLDOC').focus();
							alert("Note: Only .pdf files will be allowed!");
							return false;
						}
					}
					var	fsize=$('#XUPLDOC')[0].files[0].size;
					var file = Math.round((fsize / 1024));
					if (parseInt(file) > 20480 || parseInt(file) < 100) {
						$('#XUPLDOC').focus();
						alert("File size should be greater than 100 kb & less than 20 MB!");
						return false;
					}
					if ($("#XUPLDOC").val()!='' && $("#XUPLDOC").val()!=undefined){
						var fileCount = document.getElementById("XUPLDOC").files.length;
							form_data.append("upload_doc", document.getElementById("XUPLDOC").files[0]);
					}	
				}	
				
			var addData=[];
			 var cnt = parseInt($("#count").val()); 
			 if (cnt > 0) {
					for (var i = 1; i <= cnt; i++) {
						if($('#XUPLDOC_'+i).val()!=""&& $('#XUPLDOC_'+i).val()!=null){
							if(($('#payment_date_'+i).val()=="" || $('#payment_date_'+i).val()==null) && $('#payment_date_'+i).val()!=undefined)
							{
								   $('#payment_date_'+i).focus();
								   showerr($('#payment_date_'+i)[0],"Payment date is required","block");
								   return false;
						    }}if($('#payment_date_'+i).val()!=""&& $('#payment_date_'+i).val()!=null){
								if(($('#XUPLDOC_'+i).val()=="" || $('#XUPLDOC_'+i).val()==null) && $('#payment_date_'+i).val()!=undefined&&($("#fstatus").val()=="N"))
								{
									   $('#XUPLDOC_'+i).focus();
									   showerr($('#XUPLDOC_'+i)[0],"Upload PaymentReceipt is required","block");
									   return false;
							    }}
						if ($("#payment_date_" + i).val()!='' && $("#payment_date_" + i).val()!=undefined){
						addData.push({
							"revenue_date" 	        : $("#payment_date_" + i).val(),
							"item_des" 		        : $("#savedDoc_" + i).val(),
							"detail_id" 		: $("#detId_" + i).val()
						});
						if ($("#XUPLDOC_" + i).val()!='' && $("#XUPLDOC_" + i).val()!=undefined){
							form_data.append("upload_documents", document.getElementById("XUPLDOC_" + i).files[0]);
						}}
					}
					
					}
			var jsonObject={
				 LOCATION_CODE 	  	: $('#Xlocation').val(),
				 dept				: $('#deptId').val(),
				 DDO_ID 	 	 	: $('#Xddo').val(),
				 patta_no			:$('#XPATTA').val(),
				 other				:$('#XOTHER').val(),
				 ld_id				:$('#ld_id').val(),
				 list:addData
			};			
			//alert("send jsonObject------ "+JSON.stringify(jsonObject));
			var encData=encAESData($("#AESKey").val(), jsonObject);
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../LandRecordService");			    
			xmlHttp.setRequestHeader("encData", encData);
			xmlHttp.setRequestHeader("fstatus", $('#fstatus').val());
			xmlHttp.send(form_data);
			//xmlHttp.send(multiple_form_data);
			
			
			try{
				xmlHttp.onreadystatechange = function() {
					if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
						var data=JSON.parse(this.responseText);
						var result=decAESData($("#AESKey").val(), data)
						if(result.flg=="Y"){
							displaySuccessMessages("errMsg1", result.errMsg, "");
							clearSuccessMessageAfterFiveSecond("errMsg1");
							setTimeout(function() {
								 document.location.href = 'land_record_e.jsp';
							}, 2000);
						}else{
							displaySuccessMessages("errMsg2", result.errMsg, "");
							clearSuccessMessageAfterFiveSecond("errMsg2");
							setTimeout(function () {
		    					 //location.reload();
		    				    }, 3000);
						}
					}
				}
			} catch (err){
				alert(err);
			}
	} catch (e) {
		alert("ERROR :"+e);
	}
	}else{
		return false
	}
}

function deleteRecord(id,fname){	
		var r = confirm("Are You Sure!");
		if (r == true){ 
			var xmlHttp = new XMLHttpRequest();
			xmlHttp.open("POST", "../LandRecordService", true);			    
			xmlHttp.setRequestHeader("id", id);
			xmlHttp.setRequestHeader("fname", fname);
			xmlHttp.setRequestHeader("fstatus", "D");
	    	xmlHttp.send();
	   
	    try{
	    	xmlHttp.onreadystatechange = function() {
	    		//alert(xmlHttp.readyState+"|"+xmlHttp.status);
	    		if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
					var data=JSON.parse(this.responseText);
					var tRec = $("#totRow").val();
					for(var i=1;i<=tRec;i++){
						$("#EDIT_RECORD_"+i).wrap('<td style="display:none"/>');
						$("#DELETE_RECORD_"+i).wrap('<td style="display:none"/>');		            			
					}
					if(data.flg=="Y"){
					displaySuccessMessages("errMsg1", data.errMsg, "");
					clearSuccessMessageAfterFiveSecond("errMsg1");
					}else{
						displaySuccessMessages("errMsg2", data.errMsg, "");
						clearSuccessMessageAfterFiveSecond("errMsg2");
					}

					setTimeout(function() {
						location.reload();
					}, 2000);
	    		}
		}	
	} catch (err) {
		alert(err);
	}
}
}

function editRecord(ld_id,fstatus,x_location,x_patta){
	try {
		document.landRecordFormL.target = "_parent";
		document.landRecordFormL.action = "land_record_e.jsp?fstatus=E&ld_id="+ld_id+"&x_location="+x_location+
								"&x_patta="+x_patta;
		document.landRecordFormL.submit();
	} catch (err) {
		alert("ERROR :"+err);
	}
}


function geteditDD(status, category,sub_category,locationCode,ddoCode){
	try {
		if(status=="E" || status=="N"){
			getLocationDetail(locationCode,'Xlocation');
			getDdoDetailbyLocation(locationCode,ddoCode,'Xddo');
		}else{
			getLocationDetail(locationCode,'Xlocation');
			getDdoDetailbyLocation(locationCode,ddoCode,'Xddo');
		}
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}
	//On the click of back button from the edit page , searched value will appned to the e page 
	function vldBack(x_location,x_patta) {
		try {
		document.frmLandRecordD.action="land_record_e.jsp";
		document.frmLandRecordD.submit();
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
	}
	function getSearchList(val, status) {//alert("val----"+val+"status----"+status)
		try {
			if(status!="E"){
				$('#frmLandRecordE').attr('action', 'land_record_l.jsp');
				$("#frmLandRecordE").attr("target", 'LandRecordFrame');
				$("#frmLandRecordE").submit();
			}
		} catch (e) {
			// TODO: handle exception
			alert("ERROR IN getSearch() -:"+e);
		}
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
	
	//delete addmore row
	function deleteDetailsdata(id,index,s_id,typ,file_name,ld_id)
	{	
		var rowcountAfterDelete = document.getElementById("END1").rows.length; 
		 var rowcountAfterDelete1 = parseInt($("#count").val());
		if((rowcountAfterDelete!=1))//||(rowcountAftrDlt!=1)
		{  
			var chk=0;
			if(id!='-1'&& id!=undefined )
			{
				if(!deletemastdata(id,file_name,ld_id))
					chk=1;
			}	
			/*if(chk==0)
			{*/
				var x=parseInt($("#"+typ+"_"+index).text());
				for(var i=parseInt(index);i<=rowcountAfterDelete1;i++)
				{
					if($("#"+typ+"_"+(i+1)).text()!=undefined && $("#"+typ+"_"+(i+1)).text()!="" )
					{	
						$("#"+typ+"_"+(i+1)).text(x);
						++x;
					}
				}
				$(s_id).parents("tr").remove();
			//}
			//$("#count").val(index-1);
		}
		else
		{ 
				displaySuccessMessages("errMsg2", "At least 1 rows should be present in the table", "");
				clearSuccessMessageAfterFiveSecond("errMsg2");
		}
		
	}
	function deleteuploadedfile(id,AttachName) {
		var xmlHttp = new XMLHttpRequest();
	    xmlHttp.open("POST", "../LandRecordService", true);
	    xmlHttp.setRequestHeader("fstatus","UD");
	    xmlHttp.setRequestHeader("upld_name",AttachName);
	    xmlHttp.setRequestHeader("upld_id",id);	  
	    	xmlHttp.send();   
	    try{
	    	xmlHttp.onreadystatechange = function() {
	    		if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
					var data=JSON.parse(this.responseText);
					cons='	<input type="file" id="XUPLDOC"  name="XUPLDOC" class="" onblur="filechk1(this.id)">'
						+'</font>';	
					$('#file_Upload').html(cons);
					$('#XUPLDOC').val('');
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

	//delete addmore row from database
	function deletemastdata(id,file_name,ld_id) {
		var retVal = confirm("Data will be deleted permanently. \nAre you sure you want to remove this Data?");
		if (retVal == true) {
			try {
			
				
				
				var xmlHttp = new XMLHttpRequest();
			    xmlHttp.open("POST", "../LandRecordService", true);
			    xmlHttp.setRequestHeader("fstatus","DetailD");
			    xmlHttp.setRequestHeader("id",id);	
			    xmlHttp.setRequestHeader("file_name",file_name);	
			    xmlHttp.setRequestHeader("ld_id",ld_id);	
			    	xmlHttp.send();
			    	xmlHttp.onreadystatechange = function() {
			    	
			    	}
				
			} catch (err) {
				alert(err);
			}
			return true;
		} else {
			return false;
		}
	} 
function getDptByEmployee(emp,user_status,dept) {
	if(user_status=="A"){
		getDptByEmp('',dept,"deptId");
	}else{
		getDptByEmp(emp,dept,"deptId");
	}
}	
	