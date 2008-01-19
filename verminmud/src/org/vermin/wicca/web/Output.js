/**
 * WICCA Output component
 * Copyright (c) Vermin ry, 2005
 */

var Output = {
	
  put: function(txt) {
    //alert(txt);
    txt = new String(txt);
    var tv = this.getTextView();
    tv.append(txt);
  },
  
  showLink: function(title, url) {
    window.open(url, title);
  },

  getTextView: function() {
    if(!("textview" in this))
      this.textview = new TextView("game-output");
    return this.textview;
  }
  

};

wicca[2] = [Output, null, "put", "showLink"];

