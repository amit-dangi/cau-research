/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function validateNumber(key)
{
    //getting key code of pressed key
    var keycode = (key.which) ? key.which : key.keyCode;
    //comparing pressed keycodes
    if (!(keycode == 8 || keycode == 12) && (keycode < 13 || keycode > 13) && (keycode < 48 || keycode > 57))
    {
        return false;
    } else {
        return true;
    }
}
//Function to allow only alpha numeric to textbox
function validatealphanumeric(key) {
    var keycode = (key.which) ? key.which : key.keyCode;
//comparing pressed keycodes
    if (!(keycode == 8 || keycode == 12) && (keycode < 13 || keycode > 13) && (keycode < 32 || keycode > 33) && (keycode < 65 || keycode > 90) && (keycode < 97 || keycode > 122)) {
        return false;
    } else {
        return true;
    }
}
function alphaNumericPattern() {
    var pattern = /^([a-zA-Z\u0080-\u024F]+(?:. |-| |'))*[a-zA-Z\u0080-\u024F\.\']*$/
    return pattern;
}


function alphaNumericWithSpacialCharPattern() {
    var pattern = /^([a-z - A-Z\u0080-\u024F]+(?:. |-| |'))*[a-z A-Z\u0080-\u024F\.\']*$/
    return pattern;
}
function numbervalidation() {
//    var numberpattern = /^[0-9]/;
    var numberpattern = /^[0-9]\d*$/;
    return numberpattern;
}
function doubleValidation() {
    var pattern = /^[0-9]*\.[0-9]{1,2}$/;
    return pattern;
}

function Phonevalidation() {
    var phonepattern = /^\d{10}$/;
    return phonepattern;
}
function  ifscCodeValidation() {
    //var ifscCodePattern = /^[^\s]{4}\d{7}$/;
    var ifscCodePattern = /^[A-Za-z]{4}[0]{1}[a-zA-Z0-9]{6}$/;
    return ifscCodePattern;
}
function PanNumberValidation() {
    var numberpattern = /^([a-zA-Z]{5})(\d{4})([a-zA-Z]{1})$/;
    return numberpattern;
}
function EmailValidation() {
//    var numberpattern = /^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$/;
    var numberpattern = /^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/;
    return numberpattern;
}

function alphabetsWithSpace(){
    
}
//function PFNumber() {
////    var numberpattern = /^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$/;
//     var numberpattern = /^([a-zA-Z]{2})(\d{12})$/;
//    return numberpattern;
//}
function Category() {
	var pattern = /^([a-zA-Z\u0080-\u024F]+(?:. |-| |'))*[a-zA-Z\u0080-\u024F\.\']*$/;
	return pattern;
}

function validateFileSize(component,maxSize,msg_id,msg)
{
      if(component.files[0]!=undefined)
      {
         size = component.files[0].size;
      }
   if(size!=undefined && size>maxSize)
   {
      document.getElementById(msg_id).innerHTML=msg;
      component.value="";
      component.style.backgroundColor="#eab1b1";
      component.style.border="thin solid #000000";
      component.focus();
      return false;
   }
   else
   {
      return true;
   }
}


function validateEmail(email) {
	   var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	   return re.test(email);
	 }

function acceptOnlyCharacters(text){
	 var regex=/^[a-zA-Z-,]+(\s{0,1}[a-zA-Z-, ])*$/;
	 return regex.test(text);
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

function getAge(dateString) {
	var dateString=dateString.split("/");
	dateString=dateString[2]+"/"+dateString[1]+"/"+dateString[0];
	var today = new Date();
    var birthDate = new Date(dateString);
    var age = today.getFullYear() - birthDate.getFullYear();
    var m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
}
function validatedMobileNo(sText,fieldName) {
    
	// var regex = new RegExp("^[0-9a-zA-Z]+$");
	 var regex = new RegExp("^[0-9]*$");
     var IsNumber=true;
     var text=sText.value;
     var v = 0;
     if(!regex.test(text)){
     	  showerr(sText,fieldName+" must be numeric.","block");
           sText.focus();
           IsNumber=false;
     }
     if(v!="") {
         if (v > 0) {
         	IsNumber= true;
         } else {
         	
           showerr(sText,fieldName+" must be greater than zero(s)","block");
           sText.focus();
           IsNumber=false;
         }
       } 
     
     return IsNumber;
	
	
  }