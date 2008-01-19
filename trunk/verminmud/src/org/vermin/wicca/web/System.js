
var System = {
  rawEval: function (str) {
    eval(str);
  },
  
  showErrorMessage: function (str) {
    alert("Server error: "+str);
  },
	
  loginFailure: function() {
    alert("Incorrect username or password.");
  },
	
  loginAccepted: function() {
    $("login-form").style.display = 'none';
  }
}

wicca[0] = [System, null, null, "showErrorMessage", null, "loginFailure", "rawEval", "loginAccepted"];
