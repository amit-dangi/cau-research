/*
 * Created by Rajendra on 28-05-2020
 * to get the subject on change of Degree
 * */
function getSubjectByProgram(programme,sel_sub, is_common, obj) {
	var hrmsApi = $("#hrmsApi").val().trim();
	xmlHttp1=ajaxFunction();
	url = hrmsApi +'exam_common_dropdown.jsp?action=getSubjectByProgram&programme=' + programme+'&is_common=' + is_common+'&sel_sub='+sel_sub;
	xmlHttp1.onreadystatechange = function() {
		if (xmlHttp1.readyState == 4 && xmlHttp1.status == 200 ) {
			if(obj!=undefined && obj!=""){
				var dobj = document.getElementById(obj);
			}else{
				var dobj = document.getElementById("sel_sub");
			}
			//var dobj = document.getElementById("sel_sub");
			x=xmlHttp1.responseText;
			dobj.innerHTML = trim("<option value=''>Select Course</option>"+x);
		}
	}
	xmlHttp1.open("POST",url,false);
	xmlHttp1.send(url);
}
/*
 * 28-november-2023 Function to get the get Academic Session
 */	
function getAcademicSession(session_id,obj){
		try{		
		var sel="";
		var hrmsApi=$("#hrmsApi").val().trim();
		$.ajax({	
			type: "GET",
			url: hrmsApi+"rest/apiServices/getAcademicSession",
			jsonp: "parseResponse",
			dataType: "jsonp",
			async: false,
			//data:json,
			success: function (response){				
				var data=response.academicDropDown;
				var count=0;
				var dropdown="<option value=''>Select Year</option>";
				$.each(data, function(index, data) {
					++count;
					if(data.id==session_id){
						dropdown+="<option selected value='" + data.id + "'"+sel+">" + data.session + "</option>";
					}else{
						dropdown+="<option  value='" + data.id + "'"+sel+">" + data.session + "</option>";
					}
				});
				if(obj!=undefined && obj!=""){
	            	$("#"+obj).html(dropdown);
	            }else{
	            	$("#session_id").html(dropdown);
	            }				
			}
		});

	}catch(err){
		alert("Error :"+err);
	}
}

function getProgramByDepartment(dept, progId, obj) {
	var hrmsApi = $("#hrmsApi").val().trim();
	$.ajax({
		type : "POST",
		dataType : "json",
		url : hrmsApi + "rest/apiServices/getProgramByDept/"+encodeURIComponent(dept),
		success : function(response) {
			var moduleHtml = "<option value=''>Select Programme</option>";
			if (typeof response.program != 'undefined'
					&& response.program.length > 0) {
				var employee = response.program;
				$.each(response.program, function(key, val) {
					var widgetKey = val.programId;
					var widgetValue = val.programName;
					if (progId == widgetKey) {
						sel = "selected";
						moduleHtml += "<option value='" + widgetKey + "'" + sel
								+ ">" + widgetValue + "</option>";
					} else {
						sel = "";
						moduleHtml += "<option value='" + widgetKey + "'" + sel
								+ ">" + widgetValue + "</option>";
					}
				});
			}
			$("#" + obj).html(moduleHtml);
		}
	});
};