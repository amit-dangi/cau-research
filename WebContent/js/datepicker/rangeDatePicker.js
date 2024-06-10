function enableTxt(elem,from,to) {
	var id             = $(elem).attr("id");
    var fromdate       = from.split("/");
    var currentMonth   = fromdate[1]-1;
    var currentDate    = fromdate[0];
    var currentYear    = fromdate[2];
    var todate         = to.split("/");
    var tocurrentMonth = todate[1]-1;
    var tocurrentDate  = todate[0];
    var tocurrentYear  = todate[2];
    var yearRangeFrom=new Date().getFullYear()-100;
    var yearRangeTo=new Date().getFullYear()+100;   
    var newDate=new Date(currentYear, currentMonth, currentDate);
    var fromDate = $("#"+id).datepicker({
        changeMonth: true,
        numberOfMonths: 1,
        closeText: "X",
        showButtonPanel: true,
    	yearRange: yearRangeFrom+":"+yearRangeTo,
        minDate: newDate,
        firstDay: 1,
	    hideIfNoPrevNext: true,
	    showAnim: 'slideDown',
        dateFormat:'dd/mm/yy',
        changeYear: true,
        //showOn: "button",
       //  buttonImage: '../images/icon-calendar.gif',
       // buttonImageOnly: true,
        maxDate:new Date(tocurrentYear, tocurrentMonth, tocurrentDate),
        onClose: function (dateText, inst) {
            this.fixFocusIE = true;
            this.focus();
        },
        beforeShow: function(text, inst){        
	        var next_day = new Date(
	            inst.selectedYear,
	            inst.selectedMonth,
	            inst.selectedDay
	        );        
	        next_day.setDate(next_day.getDate()+1);
	      console.log(inst.selectedMonth);
	        console.log(next_day.getMonth());
	        if(inst.selectedMonth != next_day.getMonth())
	            return {numberOfMonths: 1};
	        else
	            return {numberOfMonths: 1};
	    }
    });
  
    $("#"+id).focus();
    $("#"+id).datepicker().datepicker( "show" );

}
$.datepicker._gotoToday = function(id) { 
    $(id).datepicker('setDate', new Date()).datepicker('hide').blur(); 
};