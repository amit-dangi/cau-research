/**
 * 
 */

 function funcgrid(x,g) {
	 x2=x.name;
	 i=x2.lastIndexOf("_");
	 no_rec=x2.substring(i+1);
	 document.forms[0].elements[g].value=no_rec;
	 i=no_rec;
 }
 
//------------------------ DEPENDENT GRID ------------------------------
 function executeDependentGrid(xmlHttp1,url,grid) {
 	xmlHttp1.onreadystatechange = function() {
 		if((xmlHttp1.readyState==0)||(xmlHttp1.readyState==1)||(xmlHttp1.readyState==3)){  
 			document.getElementById(grid).innerHTML="<BR><BR><p align='center'><img  border='0' src=images/ajax-loader.gif></images><br><B>Please Wait..</B></p>";
 		}
 		if (xmlHttp1.readyState == 4 && xmlHttp1.status == 200 ) {
 			var dobj = document.getElementById(grid);
 		    x=xmlHttp1.responseText;
 		    y=x.indexOf("[Val:");
 		    z=x.lastIndexOf("]");
 		    if ((y)>-1) {
 		    	v=x.substring(y+5,z);
 		        x=x.substring(0,y);
 		    }
 		    dobj.innerHTML = trim(x);
 		    document.getElementById("totDependentGrid").value=v;
 		}
 	}
 	xmlHttp1.open("POST",url,true);
 	xmlHttp1.send(url);
 }
  
 function showDependentGrid(grid,v,pt,frm_type) {
 	xmlH1=ajaxFunction();
     v1=parseInt(v)+1;
     s="emp_dependent_grid.jsp?cont="+v1+"&no_grids=0&emp_code="+pt+"&rt=0&opt_type="+frm_type;
     executeDependentGrid(xmlH1,s,grid+v1);
 }

 function addDependentGrid(grid,v,pt,frm_type) {
     k1=document.getElementById("totDependentGrid").value;
     xmlH1=ajaxFunction();
     v1=parseInt(v)+1;
     s="emp_dependent_grid.jsp?cont="+v1+"&no_grids=0&emp_code="+pt+"&rt=0&opt_type="+frm_type;
     executeDependentGrid(xmlH1,s,grid+v1);
 }

 function clearDependentGrid() {
     n=parseInt(document.getElementById("dgRec").value);
     t=parseInt(document.getElementById("totDependentGrid").value);
     //alert(n+"|"+t);
     if (n==1 && t==1) {
       return;
     }
     var j=0;
     for (i=n;i<t;i++) {
       j=parseInt(i)+1;
       document.getElementById("DEPENDENT_ID_"+i).value=document.getElementById("DEPENDENT_ID_"+j).value;
       document.getElementById("ED_NAME_"+i).value=document.getElementById("ED_NAME_"+j).value;
       document.getElementById("ED_GENDER_"+i).value=document.getElementById("ED_GENDER_"+j).value;
       document.getElementById("ED_DOB_"+i).value=document.getElementById("ED_DOB_"+j).value;
       document.getElementById("ED_RELATION_"+i).value=document.getElementById("ED_RELATION_"+j).value;
     }
     if (t==n) {
     	document.getElementById("dgRec").value=(t-1);
     }
     var dobj = document.getElementById("dg"+(t));
     x="<div id='dg"+(--t)+"'>";
     x=x+"</div>";
     dobj.innerHTML = trim(x);
     document.getElementById("totDependentGrid").value=t;
 }
 
 
//------------------------ EDUCATION GRID ------------------------------
 function executeEduGrid(xmlHttp1,url,grid) {
 	xmlHttp1.onreadystatechange = function() {//alert(xmlHttp1.readyState);alert(xmlHttp1.status);
 		if((xmlHttp1.readyState==0)||(xmlHttp1.readyState==1)||(xmlHttp1.readyState==3)){  
 			document.getElementById(grid).innerHTML="<BR><BR><p align='center'><img  border='0' src=images/ajax-loader.gif></images><br><B>Please Wait..</B></p>";
 		}
 		if (xmlHttp1.readyState == 4 && xmlHttp1.status == 200 ) {
 			var dobj = document.getElementById(grid);
 		    x=xmlHttp1.responseText;
 		    y=x.indexOf("[Val:");
 		    z=x.lastIndexOf("]");
 		    if ((y)>-1) {
 		    	v=x.substring(y+5,z);
 		        x=x.substring(0,y);
 		    }
 		    dobj.innerHTML = trim(x);
 		    document.getElementById("totEducationGrid").value=v;
 		}
 	}
 	xmlHttp1.open("POST",url,true);
 	xmlHttp1.send(url);
 }
  
 function showEducationGrid(grid,v,pt,frm_type) {//alert("1");
 	xmlH1=ajaxFunction();
     v1=parseInt(v)+1;
     s="emp_edu_grid.jsp?cont="+v1+"&no_grids=0&emp_code="+pt+"&rt=0&opt_type="+frm_type;
     executeEduGrid(xmlH1,s,grid+v1);
 }

 function addEducationGrid(grid,v,pt,frm_type) {
     k1=document.getElementById("totEducationGrid").value;
     xmlH1=ajaxFunction();
     v1=parseInt(v)+1;
     s="emp_edu_grid.jsp?cont="+v1+"&no_grids=0&emp_code="+pt+"&rt=0&opt_type="+frm_type;
     executeEduGrid(xmlH1,s,grid+v1);
 }

 function clearEducationGrid() {
     n=parseInt(document.getElementById("egRec").value);
     t=parseInt(document.getElementById("totEducationGrid").value);
     //alert(n+"|"+t);
     if (n==1 && t==1) {
       return;
     }
     var j=0;
     for (i=n;i<t;i++) {
       j=parseInt(i)+1;
       document.getElementById("EDU_ID_"+i).value=document.getElementById("EDU_ID_"+j).value;
       document.getElementById("EXAM_PASSED_"+i).value=document.getElementById("EXAM_PASSED_"+j).value;
       document.getElementById("EDU_TYPE_"+i).value=document.getElementById("EDU_TYPE_"+j).value;
       document.getElementById("DURR_FROM_DT_"+i).value=document.getElementById("DURR_FROM_DT_"+j).value;
       document.getElementById("DURR_TO_DT_"+i).value=document.getElementById("DURR_TO_DT_"+j).value;
       document.getElementById("UNIVERSITY_"+i).value=document.getElementById("UNIVERSITY_"+j).value;
       document.getElementById("COLLEGE_"+i).value=document.getElementById("COLLEGE_"+j).value;
       document.getElementById("DIVISION_"+i).value=document.getElementById("DIVISION_"+j).value;
     }
     if (t==n) {
     	document.getElementById("egRec").value=(t-1);
     }
     var dobj = document.getElementById("eg"+(t));
     x="<div id='eg"+(--t)+"'>";
     x=x+"</div>";
     dobj.innerHTML = trim(x);
     document.getElementById("totEducationGrid").value=t;
 }
 
 
//------------------------ PREVIOUS EMPLOYMENT GRID ------------------------------
 function executePrevEmpGrid(xmlHttp1,url,grid) {
 	xmlHttp1.onreadystatechange = function() {//alert(xmlHttp1.readyState);alert(xmlHttp1.status);
 		if((xmlHttp1.readyState==0)||(xmlHttp1.readyState==1)||(xmlHttp1.readyState==3)){  
 			document.getElementById(grid).innerHTML="<BR><BR><p align='center'><img  border='0' src=images/ajax-loader.gif></images><br><B>Please Wait..</B></p>";
 		}
 		if (xmlHttp1.readyState == 4 && xmlHttp1.status == 200 ) {
 			var dobj = document.getElementById(grid);
 		    x=xmlHttp1.responseText;
 		    y=x.indexOf("[Val:");
 		    z=x.lastIndexOf("]");
 		    if ((y)>-1) {
 		    	v=x.substring(y+5,z);
 		        x=x.substring(0,y);
 		    }
 		    dobj.innerHTML = trim(x);
 		    document.getElementById("totPrevEmpGrid").value=v;
 		}
 	}
 	xmlHttp1.open("POST",url,true);
 	xmlHttp1.send(url);
 }
  
 function showPrevEmpGrid(grid,v,pt,frm_type) {//alert("1");
 	xmlH1=ajaxFunction();
     v1=parseInt(v)+1;
     s="emp_prev_emp_grid.jsp?cont="+v1+"&no_grids=0&emp_code="+pt+"&rt=0&opt_type="+frm_type;
     executePrevEmpGrid(xmlH1,s,grid+v1);
 }

 function addPrevEmpGrid(grid,v,pt,frm_type) {
     k1=document.getElementById("totPrevEmpGrid").value;
     xmlH1=ajaxFunction();
     v1=parseInt(v)+1;
     s="emp_prev_emp_grid.jsp?cont="+v1+"&no_grids=0&emp_code="+pt+"&rt=0&opt_type="+frm_type;
     executePrevEmpGrid(xmlH1,s,grid+v1);
 }

 function clearPrevEmpGrid() {
     n=parseInt(document.getElementById("pegRec").value);
     t=parseInt(document.getElementById("totPrevEmpGrid").value);
     //alert(n+"|"+t);
     if (n==1 && t==1) {
       return;
     }
     var j=0;
     for (i=n;i<t;i++) {
       j=parseInt(i)+1;
       document.getElementById("PREV_EMP_ID_"+i).value=document.getElementById("PREV_EMP_ID_"+j).value;
       document.getElementById("EMPLOYER_NAME_"+i).value=document.getElementById("EMPLOYER_NAME_"+j).value;
       document.getElementById("DOJ_"+i).value=document.getElementById("DOJ_"+j).value;
       document.getElementById("DOR_"+i).value=document.getElementById("DOR_"+j).value;
       document.getElementById("DESIGNATION_"+i).value=document.getElementById("DESIGNATION_"+j).value;
       document.getElementById("REPORTING_HEAD_"+i).value=document.getElementById("REPORTING_HEAD_"+j).value;
       document.getElementById("CTC_"+i).value=document.getElementById("CTC_"+j).value;
       document.getElementById("REASON_LEAVING_"+i).value=document.getElementById("REASON_LEAVING_"+j).value;
     }
     if (t==n) {
     	document.getElementById("pegRec").value=(t-1);
     }
     var dobj = document.getElementById("peg"+(t));
     x="<div id='peg"+(--t)+"'>";
     x=x+"</div>";
     dobj.innerHTML = trim(x);
     document.getElementById("totPrevEmpGrid").value=t;
 }