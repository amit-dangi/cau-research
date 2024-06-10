<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="../assets/sits/bootstrap/3.3.7/css/bootstrap.min.css"  type="text/css">
	<script type="text/javascript" src="../js/jquery-3.2.1.js"></script>
	<script src="../assets/sits/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
	 
 	<link rel="stylesheet" href="../assets/plugins/datepicker/datepicker3.css"  type="text/css">
    <script type="text/javascript" src="../assets/plugins/datepicker/bootstrap-datepicker.js"></script>
	<link href="../css/themes/blue.css" rel="stylesheet" type="text/css" />
	<link href="../css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<link href="../css/themes/hrm-fym.css" rel="stylesheet" type="text/css" />
<link href="../css/themes/responsive.css" rel="stylesheet" type="text/css" />
</head>
<body>
 <div class="container-fluid">
<div id="ftitleHeader" class="page-header"><h4>Utilization Certificate(for Recurring & Non Recurring)</h4></div>
 
	<form class="form-horizontal" name="frmHRFinYearD" id="myForm" action="hr_fin_mast_l.jsp" target="btmfrmHRFinYearD" method="post">
	<div class="panel panel-default">
		<div class="panel-heading"><h3 class="panel-title text-right">Searching Criteria</h3></div>
		<div class="panel-body"> <!-- style="margin:0px;padding-top:5px;"> -->
		
		<div class="form-group">
			<div class="col-md-12">
			<div class="row">
			 <label for="" class="col-sm-2 col-form-label required-field">Financial Year</label>
               <div class="col-sm-4">
                   <select class="form-control" id="" name="">
                          <option value=""> Select Financial Year</option>
						  <option value="">A1</option>
						  <option value="">B1</option>
                    </select> 
              </div>
			  
			   <label for="" class="col-sm-2 col-form-label required-field">Project</label>
               <div class="col-sm-4">
                   <select class="form-control" id="" name="">
                          <option value=""> Select Project</option>
						  <option value="">A1</option>
						  <option value="">B1</option>
                    </select>
              </div>
              
            
              </div>
                </div>
                </div>
        <div class="form-group">
			<div class="col-md-12">
			<div class="row">
			 <label for="" class="col-sm-2 col-form-label required-field">PI</label>
               <div class="col-sm-4">
                   <select class="form-control" id="" name="">
                          <option value=""> Select PI</option>
						  <option value="">A1</option>
						  <option value="">B1</option>
                    </select> 
              </div>
            </div>
           </div>
         </div>
   
      
   
			<div class="form-group m-t-25 m-b-5">
			<div class="col-md-12 text-center">
			<div class="row">
				 
				<button type="button" class="btn btn-view" onClick="btnNew();">Download</button>
				<button type="button" class="btn btn-view" onClick="btnResetInD();">Reset</button>
   			</div>
   			</div>
   			</div>
		</div><!-- End panel-body -->
	</div><!-- End panel-default -->
	</form>
 	 
    <iframe class="embed-responsive-item" name="btmfrmHRFinYearD" id="btmfrmHRFinYearD" 
	src="" width="100%" onload="resizeIframe(this)" scrolling="no" height="" frameborder="0"></iframe>
	 
</div><!-- End container-fluid -->
</body>
<script type="text/javascript">
      function resizeIframe(iframe) {
	    iframe.height = iframe.contentWindow.document.body.scrollHeight + "px";
	    window.requestAnimationFrame(() => resizeIframe(iframe));
	}
  </script>
</body>
</html>