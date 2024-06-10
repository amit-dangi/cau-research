
$(document).ready(function(){
	$('#btnReset').click(function(){    // call on Reset  button
		document.location.reload();
	});
	$('#btnback').click(function(){   
		document.location.href="discipline_thrust_area_mapping_e.jsp";
	});
		 $("#xbtnReset").click(function(){
				   	document.getElementById("frmdismasterd").reset();
});
});

function vldSearch(){

	document.DisciplineThrustAreaE.target = "DisciplineThrustAreaD";
	document.DisciplineThrustAreaE.action = "discipline_thrust_area_mapping_l.jsp";
	document.DisciplineThrustAreaE.submit();
	document.DisciplineThrustAreaE.reset();
}

function saveRecord(fstatus){
	if($('#discipline').val() == ''){
		showerr($("#discipline")[0], "Discipline Name is required!", "block");
		$('#discipline').focus();
		return false;
    }
	else if($('#thrust_area').val() == ''){
		showerr($("#thrust_area")[0], "Thrust Area is required!", "block");
		$('#thrust_area').focus();
		return false;
    }else if($('#sub_thrust_area').val() == ''&& $("#sub_thrust_area option:selected").text().trim()!= "NA" ){
		showerr($("#sub_thrust_area")[0], "Sub Thrust Area is required!", "block");
		$('#sub_thrust_area').focus();
		return false;
    }else{
		try{
			
			let objjson={
					"discipline":$("#discipline").val(),
					"thrust_area":$("#thrust_area").val(),						
					"sub_thrust_area":$("#sub_thrust_area").val(),
					"cr_id":$("#cr_id").val(),
					"fstatus":$("#fstatus").val(),
	
			};
			
			var encData=encAESData($("#AESKey").val(), objjson);
			$.ajax({
				type : "POST",
				url : 'DisciplineThrustAreaMappingService',
				data : {		    	
			    	encData : encData,
			    	fstatus :fstatus
			    },success : function(response) {
			    	if(response.flg == "Y"){
			    		displaySuccessMessages("errMsg1", response.errMsg, "");
			    		clearSuccessMessageAfterTwoSecond("errMsg1");
			    		setTimeout(function () {
			    			document.location.href="discipline_thrust_area_mapping_e.jsp";
			    		}, 5000);			    		
			    	  }else{
			    		    displaySuccessMessages("errMsg2", response.errMsg, "");
				    		clearSuccessMessageAfterTwoSecond("errMsg2");
			    	  }
			        
			    },
				error : function(xhr, status, error) {
					alert("error");
				}
			});
															
		}catch(e){
			alert("view: "+e);
		}		
     
 
				
	}	 			 

	
}

function editRecord(lid,typ) {
	try {

		 if(typ=="D"){
			var del=confirm("Are You Sure?")
		    if(del==true){
		    	$.ajax({
		    		type : "POST",
		    		url:  'DisciplineThrustAreaMappingService',
		    		data: 'fstatus=D&cr_id='+lid,	
		    		success : function(response){
				    	if(response.flg == "Y"){
				    		displaySuccessMessages("errMsg1", response.errMsg, "");
				    		clearSuccessMessageAfterTwoSecond("errMsg1");
				    		setTimeout(function () {
				    			location.reload();
				    		}, 5000);			    		
				    	  }else{
				    		    displaySuccessMessages("errMsg2", response.errMsg, "");
					    		clearSuccessMessageAfterTwoSecond("errMsg2");
				    	  }
				        
				    }
		    	});
		    }
		}else {
			document.getElementById("cr_id").value=lid;
			document.getElementById("opt_typ").value=typ;
			document.DisciplineThrustAreaL.target = "_parent";
			document.DisciplineThrustAreaL.action = "discipline_thrust_area_mapping_e.jsp";
			document.DisciplineThrustAreaL.submit();
		}
	} catch (err) {alert(err);}
}

function checkUncheck(flag){ //debugger;
	try{
		if(flag=="Y"){
			$('#is_active').prop('checked', true);
		} else if(flag=="N") {
			$('#is_active').prop('checked', false);
		} else{
			
		}
	}catch(err){
		alert("Error Caused by checkUncheck() in [course-master.js]: "+err);
	}
}