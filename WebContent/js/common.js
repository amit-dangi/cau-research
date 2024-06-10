  function LTrim( value ) {
    var re = /\s*((\S+\s*)*)/;
    return value.replace(re, "$1");
  }
  function RTrim( value ) {
    var re = /((\s*\S+)*)\s*/;
    return value.replace(re, "$1");
  }
  function trim( value ) {
    return LTrim(RTrim(value));
  }

var W3CDOM = (document.getElementsByTagName && document.createElement);

function showerr(obj,msg,display) {
  errorstring = '';
  firstError = null;
  if (obj.hasError) return;
  if (W3CDOM) {
    obj.className += ' error';
    obj.onchange = removeError;
    var spanTag = document.createElement("span");
    spanTag.id ="txt_"+obj.name;
    spanTag.innerHTML=msg;
    //spanTag.className="label.error";
    spanTag.className="errmessage";
    spanTag.style.display=display;
    //spanTag.appendChild(document.createTextNode(msg));
    obj.parentNode.appendChild(spanTag);
    obj.hasError = spanTag;
  } else {
    errorstring += obj.name + ': ' + msg + '\n';
    obj.hasError = true;
    obj.focus();
  }
  if (firstError)
    firstError.focus();
}

function removeError() {
  this.className = this.className.substring(0,this.className.lastIndexOf(' '));
  this.parentNode.removeChild(this.hasError);
  this.hasError = null;
  this.onchange = null;
}


function revertBack(){
	  setTimeout(function () {
	   displaySuccessMessages("errMsg", "No Data Found.", "");
	     }, 1000);
	  clearSuccessMessageAfterFiveSecond("errMsg");
	 }

function manageSessInsideChildPack(url,data){
	//alert(url+"|"+data);
	var rtn;
	$.ajax({
	    type: 'POST',
	    url:  url,
	    dataType : "json",
	    data: {	action : 1,
	    		userData : data,
	    	  },

	   	async: false,
	    success: function (data)
	    {
	    	//alert(data.flag);
	    	if(data.flag != 'Y') {
	    		rtn = "false";
	    	} else {
	    		rtn = "true";
	    	}
	    },
	    error: function(error) {
	    	alert("error");
	    }
	});
	return rtn;
}

function rmvSessInsideChildPack(url){
	//alert(url);
	$.ajax({
	    type: 'POST',
	    url:  url,
	    data: {	action : 2},
	    async: true,
	    success: function (response)
	    {
			//alert("success");
	    },
	    error: function(xhr, status, error) {
	    	alert("common.js [rmvSessInsideChildPack] : "+xhr.responseText);
	    }
	});
}