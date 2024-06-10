/**
 * @author Amit Dangi
 */
	
//This function is used to get finacial year
function getFinanceYrDropdwn(fin_yr_id, obj) { 
	var hrmsApi = $("#hrmsApi").val().trim(); 
	var sel = "";
 	$.ajax({
		type : "POST",
		url : hrmsApi+ "rest/FinanceApiServices/getfinancialYrDropdown",
		jsonp : "parseResponse",
		dataType : "jsonp",
		success : function(response) {
			var moduleHtml = "<option value=''>Select Financial Year</option>";
			if (typeof response.currentFinYear != 'undefined'
					&& response.currentFinYear.length > 0) {
				$.each(response.currentFinYear, function(key, val) { 
					var widgetKey = val.finyearId;
					var widgetValue = val.finyear;
					if (fin_yr_id == widgetKey) {
						sel = "selected";
						moduleHtml += "<option value='" + widgetKey + "'" + sel + ">" + widgetValue
								+ "</option>";
					} else {
						sel = "";
						moduleHtml += "<option value='" + widgetKey + "'" + sel + ">" + widgetValue
								+ "</option>";
					}
				});
			} 
			$("#" + obj).html(moduleHtml);
		}
	}); 
};

/*
 * Created by Amit Dangi on 11-03-2022
 * to get the location Details 
 * */
function getLocationDetail(locationCode,obj){
	var hrmsApi = $("#hrmsApi").val().trim();
	var json={userId:$("#user_id").val()};  	
	var sel="";
	try {
		$.ajax({	
			type: "GET",
			url: hrmsApi+"rest/apiServices/getLocationDetail",
			jsonp: "parseResponse",
			dataType: "jsonp",
			data:json,
			success: function (response){
				console. log("getLocationDetail response");
				console.log(response);
				var department=response.locationDetail;
				var dropdown="<option value=''>Select Location</option>";
				$.each(department, function(index, department) {
				if(department.locationId==locationCode) {
						sel="selected";	
					} else {
						sel="";
					}
					dropdown+="<option  value='" + department.locationId + "'"+sel+">" + department.locationName + "</option>";					
				});	
				if(obj == null && obj == undefined){
					$("#Xlocation").html(dropdown);
				}
				else
					$("#"+obj).html(dropdown);
			}
		});	
	} catch (e) {
		// TODO: handle exception
		alert(e);
	}
}

/*
 * Created by Amit Dangi on 11-03-2022
 * to get the ddo Details by locationcode
 * */
function getDdoDetailbyLocation(locationCode,ddoCode,obj){
	var hrmsApi = $("#hrmsApi").val().trim();
	var json={userId:$("#user_id").val(),"locationCode":locationCode};  	
	var sel="";
	
	try {
		$.ajax({	
			type: "GET",
			url: hrmsApi+"rest/apiServices/getDdoDetailbyLocation",
			jsonp: "parseResponse",
			dataType: "jsonp",
			data:json,
			success: function (response){
				console. log("getDdoDetailbyLocation response");
				console.log(response);
				var department=response.DTODetail;
				var dropdown="<option value=''>Select DDO</option>";
				$.each(department, function(index, department) {
					if(department.DTOId==ddoCode) {
						sel="selected";	
					} else {
						sel="";
					}
					dropdown+="<option  value='" + department.DTOId + "'"+sel+">" + department.DTOName + "</option>";					
				});
				if(obj == null && obj == undefined){
					$("#Xddo").html(dropdown);	
				}
				else
				$("#"+obj).html(dropdown);
				
			}
		});	
	} catch (e) {
		// TODO: handle exception
		alert(e);
	}
}

/*
 * Created by Amit Dangi on 06-12-2022
 * to get the Project Details by locationcode
 * */

function getProjectDetailbyLocation(pId,locationCode,ddoCode,obj){
	try {
		if(locationCode==''){var locationCode = $('#Xlocation').val(); var ddoCode = $('#Xddo').val();}
		var data={"fstatus": "GETPROJECTS","locationCode":locationCode,"ddoCode":ddoCode};
		console.log("getProjectDetailbyLocation data");
		console.log(data);
		$.ajax({
			type: "POST",
			url: "../ContractualEmpProjectMapService",
			data:data,
			async: false,
			success: function (response){
				console. log("getProjectDetailbyLocation response common");
				console.log(response);
				var department=response.projectdropdown;
				var dropdown="<option value=''>Select Project</option>";
				$.each(department, function(index, department) {
					if(department.PS_MID==pId) {
						sel="selected";	
					} else {
						sel="";
					}
					dropdown+="<option  value='" + department.PS_MID + "'"+sel+">" + department.PS_TITTLE_PROJ + "</option>";					
				});
				if(obj == null && obj == undefined){
					$("#proj").html(dropdown);
				}
				else
				$("#" + obj).html(dropdown);
			}
		});
	
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+err);
	}
}

/*
 * Created by Amit Dangi on 08-09-2023
 * Function to get the research propsal as per the selected location, ddo & projtype
 * */
function getResearchProposal(locationCode,ddoCode){
	try {
		if(locationCode==''){var locationCode = $('#Xlocation').val(); var ddoCode = $('#Xddo').val();}
		var id=$("#propsalId").val();
		var projtype=$("#projtype").val();
		$(".resPrps").html("");
		$.ajax({
			type: "POST",
			url: "../common/rsrchdropdowndata",
			data:{"fstatus":"RSCHPROPOSAL","locationCode":locationCode,"ddoCode":ddoCode,"projtype":projtype},
			async: false,
			success: function (response){
				console.log("response.employee");
				console.log(response.employee);
				var emp="<option value=''>Select Research Proposal</option>";
				var employee=response.employee;
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

/*
 * Created by Amit Dangi on 08-09-2023
 * Function to get the research Category as per the selected Cat
 * */
function getRsrchCategory(SavedCat,obj)
{console.log("common getRsrchCategory obj "+obj);
	$.ajax({
        type: 'POST',
        url:  '../common/rsrchdropdowndata',
        data:{"fstatus":"getRsrchCategory"},
        async: false,
        success: function (response)
        {
        	console.log("getRsrchCategory response ");
        	console.log(response.catList);
        	var moduleHtml = "<option value=''>Select Category</option>";
        	if (typeof response.catList != 'undefined' && response.catList.length > 0)
            {
                $.each(response.catList, function (key, val) {
                    var widgetKey = val.id;
                    var widgetValue = val.desc;
                    var sel="";
                    if(SavedCat == widgetKey) sel="selected";
                    moduleHtml += "<option value='" + widgetKey + "' "+ sel +">" + widgetValue + "</option>";
                });

            }
        	if(obj!=undefined && obj!=""){
            	$("#"+obj).html(moduleHtml);
            }
        },
         error:function(){
        	 alert("err");
         }
    });
};

/*
 * Created by Amit Dangi on 08-09-2023
 * Function to get the research Category as per the selected SubCat
 * */
function getRsrchSubCategory(param,SavedSubCat,obj)
{
	$.ajax({
        type: 'POST',
        url:  '../common/rsrchdropdowndata',
        data:{"fstatus":"getRsrchSubCategory","CATEGORY_ID":param},
        async: false,
        success: function (response)
        {
        	console.log("getRsrchSubCategory response ");
        	console.log(response.subcatList);
        	if(response.subcatList.length=='0'){
				var moduleHtml="<option value=''>NA</option>";
			}else{
				var moduleHtml = "<option value=''>Select Sub Category</option>";
			}
        	
        	if (typeof response.subcatList != 'undefined' && response.subcatList.length > 0)
            {
                $.each(response.subcatList, function (key, val) {
                    var widgetKey = val.id;
                    var widgetValue = val.desc;
                    var sel="";
                    if(SavedSubCat == widgetKey) sel="selected";
                    moduleHtml += "<option value='" + widgetKey + "' "+ sel +">" + widgetValue + "</option>";
                });

            }
        	if(obj!=undefined && obj!=""){
            	$("#"+obj).html(moduleHtml);
            }
        },
         error:function(){
        	 alert("err");
         }
    });
};

/*
 * Created by TANUJALA on 21-09-2023
 * Function to get the Sub Thrust Area as per the selected Thrust Area
 * */
function getSubThrustAreaByThrustArea(param,SavedSubThrustArea,obj,type){debugger;
	var hrmsApi = $("#hrmsApi").val();
		$.ajax({	
			type: 'POST',
	        url:  '../common/rsrchdropdowndata',
	        data:{"fstatus":"getSubThrustAreaByThrustArea","Thrust_Area_Id":param},
	        async: false,
			success: function (response){debugger;
				
				if(type=='list'){
	         		 var moduleHtml = '<ul class="form-control" style="height: 85px; padding-top:0px;overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;">';
	               moduleHtml +='<li ><input type="checkbox" name="chkAllSavedSubThrustArea" id="chkAllSavedSubThrustArea" class="chkAllSavedSubThrustArea" onclick="checkSavedSubThrustArea()" > ALL </li>';
	         	 }else{
	         		if(response.subThrustAreaList.length=='0'){
	    				var moduleHtml="<option value=''>NA</option>";
	    			}else{
	    				var moduleHtml = "<option value=''>Select Sub Thrust Area</option>";
	    			}
	         	 	}				
		        	if (typeof response.subThrustAreaList != 'undefined' && response.subThrustAreaList.length > 0)
		            { 
		                $.each(response.subThrustAreaList, function (key, val) {debugger;
		                    var widgetKey = val.id;
		                    var widgetValue = val.desc;
		                    if(type=='list'){
		                    if((type=='list')&&(SavedSubThrustArea.includes(widgetKey))){	
		                    	moduleHtml +='<li><input type="checkbox"  id="checkBoxClass" class="checkBoxClass"  value="'+widgetKey+'" checked> '+widgetValue+'</li>';
		                    }else{
		                    	moduleHtml +='<li><input type="checkbox"  id="checkBoxClass" class="checkBoxClass"  value="'+widgetKey+'" > '+widgetValue+'</li>';
							}
		                    }
		                    if(type!='list'){
		                    	if(widgetKey==SavedSubThrustArea){
		                    		moduleHtml += "<option selected value='" + widgetKey + "'>" + widgetValue + "</option>";
		                    	}else{
		                    		moduleHtml += "<option value='" + widgetKey + "'>" + widgetValue + "</option>";
		                    	}
		                    }
		                });
		                
		                if(type=='list')
		                	$("#"+obj).html(moduleHtml+'</ul>');
		                else
		            	   $("#"+obj).html(moduleHtml);
		           }
		           else
		           {
		           	if(type=='list')
		           		$("#"+obj).html('<ul class="form-control" style="height: 85px; padding-top:0px;overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;"><li>List of Research Sub Thrust Area(s)</li></ul>');
		               else
		            	   $("#"+obj).html(moduleHtml);
		           }
		        }
				
		    });
		};


/*
 * Created by TANUJALA on 21-09-2023
 * Function to get the  Thrust Area as per the selected Discipline
 * */
function getThrustAreaByDiscipline(param,SavedThrustArea,obj)
{
	$.ajax({
        type: 'POST',
        url:  '../common/rsrchdropdowndata',
        data:{"fstatus":"getThrustAreaByDiscipline","discipline_id":param},
        async: false,
        success: function (response)
        {
        	console.log("getSubThrustArea response ");
        	console.log(response.thrustAreaList);
        	if(response.thrustAreaList.length=='0'){
				var moduleHtml="<option value=''>NA</option>";
			}else{
				var moduleHtml = "<option value=''>Select Sub Thrust Area</option>";
			}
        	
        	if (typeof response.thrustAreaList != 'undefined' && response.thrustAreaList.length > 0)
            {
                $.each(response.thrustAreaList, function (key, val) {
                    var widgetKey = val.id;
                    var widgetValue = val.desc;
                    var sel="";
                    if(SavedThrustArea == widgetKey) sel="selected";
                    moduleHtml += "<option value='" + widgetKey + "' "+ sel +">" + widgetValue + "</option>";
                });

            }
        	if(obj!=undefined && obj!=""){
            	$("#"+obj).html(moduleHtml);
            }
        },
         error:function(){
        	 alert("err");
         }
    });
};


/*
 * Created by TANUJALA on 29-09-2023
 * Function to get the  Discipline as per the selected Course
 * */
function getDisciplineByCourse(id,discipline,obj){
	var hrmsApi = $("#hrmsApi").val();
	try {
		$.ajax({	
			type: "GET",
			url: hrmsApi+"rest/apiServices/getDisciplineByProgYrType",
			jsonp: "parseResponse",
			dataType: "jsonp",
			data:{"pgmYrTyp":id},
			success: function (response){
				console.log("getDisciplineByProgYrType response ");
	        	console.log(response);
				if (typeof response.discipline != 'undefined'
					&& response.discipline.length > 0) {
				var moduleHtml = "<option value=''>Select Discipline</option>";
				$.each(response.discipline, function(key, val) { 
					var widgetKey = val.id;
					var widgetValue = val.course_name;
					if (discipline == widgetKey) {
						sel = "selected";
						moduleHtml += "<option value='" + widgetKey + "'" + sel + ">" + widgetValue
								+ "</option>";
					} else {
						sel = "";
						moduleHtml += "<option value='" + widgetKey + "'" + sel + ">" + widgetValue
								+ "</option>";
					}
				});
				$("#" + obj).html(moduleHtml);
			} else {
				$("#" + obj).html(
								"<option value=''>Select Discipline</option>");
			}
		},
        error: function(xhr, status, error, response) {
            alert("xhr : "+xhr.responseText);
          }

        });
	} catch (e) {
		// TODO: handle exception
		alert(e);
	}
}
/*
 * Created by TANUJALA on 29-09-2023
 * Function to get the Course
 * */
function getCourse(course,obj){
	var hrmsApi = $("#hrmsApi").val();
	try {
		$.ajax({	
			type: "GET",
			url: hrmsApi+"rest/apiServices/getCourse",
			jsonp: "parseResponse",
			dataType: "jsonp",
			success: function (response){
				console.log("getCourse response ");
	        	console.log(response);
				if (typeof response.courseList != 'undefined'
					&& response.courseList.length > 0) {
				var moduleHtml = "<option value=''>Select Course</option>";
				$.each(response.courseList, function(key, val) { 
					var widgetKey = val.id;
					var widgetValue = val.course_name;
					if (course == widgetKey) {
						sel = "selected";
						moduleHtml += "<option value='" + widgetKey + "'" + sel + ">" + widgetValue
								+ "</option>";
					} else {
						sel = "";
						moduleHtml += "<option value='" + widgetKey + "'" + sel + ">" + widgetValue
								+ "</option>";
					}
				});
				$("#" + obj).html(moduleHtml);
			} else {
				$("#" + obj).html(
								"<option value=''>Select Course</option>");
			}
		},
        error: function(xhr, status, error, response) {
            alert("xhr : "+xhr.responseText);
          }

        });
	} catch (e) {
		// TODO: handle exception
		alert(e);
	}
}

/*
 * Created by Amit Dangi on 13-Oct-2023
 * Function to get the get Funding Agency record as per the selected proposal
 * */
function getFundingAgency(projId,SavedFAgency,obj)
{console.log("common getFundingAgency obj "+obj);
	$.ajax({
        type: 'POST',
        url:  '../common/rsrchdropdowndata',
        data:{"fstatus":"getFundingAgency","projId":projId},
        async: false,
        success: function (response)
        {
        	console.log("getFundingAgency response ");
        	console.log(response.List);
        	var moduleHtml = "<option value=''>Select Funding Agency</option>";
        	if (typeof response.List != 'undefined' && response.List.length > 0)
            {
                $.each(response.List, function (key, val) {
                    var widgetKey = val.id;
                    var widgetValue = val.desc;
                    var sel="";
                    if(SavedFAgency == widgetKey || response.List.length==1) sel="selected";
                    moduleHtml += "<option value='" + widgetKey + "' "+ sel +">" + widgetValue + "</option>";
                });

            }
        	if(obj!=undefined && obj!=""){
            	$("#"+obj).html(moduleHtml);
            }
        },
         error:function(){
        	 alert("err");
         }
    });
};

/*
 * Created by Amit Dangi on 18-Oct-2023
 * Function to get the get Hrms Employee name as per the selected location and ddo
 * */

function getEmployeeName(locationCode,ddoCode,empid,type,obj){
	try {
		var hrmsApi = $("#hrmsApi").val().trim();
		console.log("hrmsApi||"+hrmsApi+"|locationCode|"+locationCode);
		if(locationCode==''){var locationCode = $('#Xlocation').val(); var ddoCode = $('#Xddo').val();}
		var json={"location":locationCode,"ddo":ddoCode};
		console.log("json||");
		console.log(json);
		$.ajax({
			type: "GET",
			url: hrmsApi+"rest/apiServices/getEmployee",
			jsonp: "parseResponse",
			dataType: "jsonp",
			async: false,
			data:json,
			success: function (response){
				var employee=response.employee;
				console.log("employee||"+employee);
				if(type=='list'){
	         		 var moduleHtml = '<ul class="form-control" style="height: 85px; padding-top:0px;overflow: auto; border: 1px solid #B2BABB; border-radius: 9px !important; list-style-type: none;">';
	               moduleHtml +='<li ><input type="checkbox" name="chkAllSavedemp" id="chkAllSavedemp" class="chkAllSavedemp" onclick="checkAll()" > ALL </li>';
	         	 }else{
				var emp="<option value=''>Select Principal Investigator</option>";
	         	 }
				$.each(employee, function(index, employee) {
					
					if(type=='list'){
	                    if(empid.includes(employee.employeeId)){	
	                    	moduleHtml +='<li><input type="checkbox"  id="checkBoxClass" class="checkBoxClass"  value="'+employee.employeeId+'" checked> '+employee.employeeName+'</li>';
	                    }else{
	                    	moduleHtml +='<li><input type="checkbox"  id="checkBoxClass" class="checkBoxClass"  value="'+employee.employeeId+'" > '+employee.employeeName+'</li>';
						}
	                    }
					if(type!='list'){
					if(employee.employeeId==empid){
						emp+="<option selected value='"+employee.employeeId+"'> "+employee.employeeName+"</option>";
					}else{
						emp+="<option  value='"+employee.employeeId+"'> "+employee.employeeName+"</option>";
					}
					}
				});
				if(type=='list')
                	$("#"+obj).html(moduleHtml+'</ul>');
                else
				$("#"+obj).html(emp);
			}
		});
	} catch (e) {
		// TODO: handle exception
		alert("ERROR :"+e);
	}
}

/*
 * 18-Oct-2023 Function to get the get Hrms Employee Department
 *  as per the login employee
 */	
function getDptByEmp(emp, deptId,obj){
	try{		
		var json={empId:emp};  		
		var sel="";
		var hrmsApi=$("#hrmsApi").val().trim();
		$.ajax({	
			type: "GET",
			url: hrmsApi+"rest/apiServices/getDeptByEmpId",
			jsonp: "parseResponse",
			dataType: "jsonp",
			async: false,
			data:json,
			success: function (response){				
				var dept=response.deptDetail;
				var count=0;
				var dropdown="<option value=''>Select Department</option>";
				$.each(dept, function(index, dept) {
					++count;
					if(dept.ID==deptId){
						dropdown+="<option selected value='" + dept.ID + "'"+sel+">" + dept.Name + "</option>";
					}else{
						dropdown+="<option  value='" + dept.ID + "'"+sel+">" + dept.Name + "</option>";
					}
				});
				if(obj!=undefined && obj!=""){
	            	$("#"+obj).html(dropdown);
	            }else{
	            	$("#deptId").html(dropdown);
	            }				
			}
		});

	}catch(err){
		alert("Error :"+err);
	}
}

/*
 * Created by Amit dangi on 12-07-2023
 * Function to get the  Subject/Discipline as per the selected Course/Degree
 * */
function getSubjectByCourse(id,discipline,obj){
	var hrmsApi = $("#hrmsApi").val();
	try {
		$.ajax({
			type: 'POST',
	        url:  '../common/rsrchdropdowndata',
	        data:{"fstatus":"getSubjectByCourse","courseId":id},
	        async: false,
			success: function (response){
				console.log("getSubjectByCourse response ");
	        	console.log(response.List);
				if (typeof response.List != 'undefined' && response.List.length > 0) {
				var moduleHtml = "<option value=''>Select Discipline</option>";
				$.each(response.List, function(key, val) { 
					var widgetKey = val.id;
					var widgetValue = val.desc;
					if (discipline == widgetKey) {
						sel = "selected";
						moduleHtml += "<option value='" + widgetKey + "'" + sel + ">" + widgetValue
								+ "</option>";
					} else {
						sel = "";
						moduleHtml += "<option value='" + widgetKey + "'" + sel + ">" + widgetValue
								+ "</option>";
					}
				});
				$("#" + obj).html(moduleHtml);
			} else {
				$("#" + obj).html(
								"<option value=''>Select Discipline</option>");
			}
		},
        error: function(xhr, status, error, response) {
            alert("xhr : "+xhr.responseText);
          }

        });
	} catch (e) {
		// TODO: handle exception
		alert(e);
	}
}

/*
 * Created by Sachin on 12-12-2023
 * Function to get the fund allotmentdate as per from head subheadwise 
 * fund allocation page
 * */

function getInstallMentDate(Id,installId,obj,fin_yr) {
	$.ajax({ 
    	type: "POST",
    	url:  '../common/rsrchdropdowndata',
	data:{"fstatus":"instalmentlDetails","Id":Id,"fin_yr":fin_yr},
    async: false,
    success: function (response)
    { 		 
            if (typeof response.instList != 'undefined' && response.instList.length > 0)
        { 
            var moduleHtml = "<option value=''>Select Installment Date</option>";
            $.each(response.instList, function (key, val) {
            	 
            	var widgetKey = val.id;
                var widgetValue = val.name;
                 if(widgetKey==installId)
                	 moduleHtml += "<option value='" + widgetKey + "'selected>" + widgetValue + "</option>";
                 else
                moduleHtml += "<option value='" + widgetKey + "'>" + widgetValue + "</option>";
                 
             });
            $("#"+obj).html(moduleHtml);
        }else
        {
         $("#"+obj).html("<option value=''>Select Installment Date</option>");
        }
    }
});
}

function getHeadNameList(id,index,obj) { 
  	    $.ajax({  
    	   type: "POST",
    	   url:  '../common/rsrchdropdowndata',
	    	data:{"fstatus":"FHead"},
	    	async: false,
        success: function (response)
        { 		
	            if (typeof response.headlist != 'undefined' && response.headlist.length > 0)
            {
                var moduleHtml = "<option value=''>Select Head</option>";
                $.each(response.headlist, function (key, val) {
                    var widgetKey = val.id;
                    var widgetValue = val.name;
                    if(widgetKey==id){
                    	moduleHtml += "<option value='" + widgetKey + "'selected>" + widgetValue + "</option>";
                    }else{
                    	moduleHtml += "<option value='" + widgetKey + "'>" + widgetValue + "</option>";
                    }
                 });
                $("#"+obj+"_"+index).html(moduleHtml);
            }else
            {
             $("#"+obj+"_"+index).html("<option value=''>Select Head</option>");
            }
        }
    });
	}
function getSubHeadNameList(headId, index,subheadId,obj) {
   	    $.ajax({  
  	    	type: "POST",
  	    	 url:  '../common/rsrchdropdowndata',
 			data:{"fstatus":"FSubHead","hId":headId},
 			async: false,
	        success: function (response)
	        { 		
 	            if (typeof response.sheadlist != 'undefined' && response.sheadlist.length > 0)
	            {
	               var moduleHtml = "<option value=''>Select Sub Head</option>";
	                $.each(response.sheadlist, function (key, val) {
	                    var widgetKey = val.id;
	                    var widgetValue = val.name;
	                    if(widgetKey==subheadId){
	                    	moduleHtml += "<option value='" + widgetKey + "'selected>" + widgetValue + "</option>";
	                    }else{
	                    	moduleHtml += "<option value='" + widgetKey + "'>" + widgetValue + "</option>";
	                    }
	                 });
	                $("#"+obj+"_"+index).html(moduleHtml);
	            }else
	            {
	             $("#"+obj+"_"+index).html("<option value='NA'>NA</option>");
	            }
	        }
	    });
 	}

