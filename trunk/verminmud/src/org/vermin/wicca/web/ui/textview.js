/**
 * A text view component that understands VerminMUD color syntax and
 * provides scrolling.
 *
 * Tatu Tarvainen
 */
 
var TextView = Class.create();
TextView.prototype = {
  initialize: function(divId) {
    this.divId = divId;
  },
  
  append: function (txt) {
    var scroll = false;
    var node = $(this.divId);
    
    // check if the scrollbar is at the bottom
    if((node.clientHeight + node.scrollTop) == node.scrollHeight)
      scroll = true;
    
    // append the text
    var lines = txt.split("&.;");
    //var n = document.createElement("SPAN");
    for(var i = 0; i<lines.length; i++) {
      this.insertLine(node, lines[i]);
    }
    //node.appendChild(n);
    
    // if at bottom, remove old elements and scroll to bottom
    if(scroll) {
      while(node.childNodes.length > 100)
	node.removeChild(node.firstChild);
      node.scrollTop = 100000;
    }
  },
	
  insertLine: function(node, line) {
    
    var div = document.createElement("DIV");
    div.style.width = '100%';
    
    var fragments = line.split("&");
    var span = null;
    for(var f = 0; f < fragments.length; f++) {
      var fragment = fragments[f];
      if(fragment.match(/[Bbri]?[0-9]{0,2};/)) {
	span = this.createSpan(fragment);
	if(span) div.appendChild(span);
	var rest = fragment.substring(fragment.indexOf(";")+1);
	if(rest.length > 0)
	  (span ? span : div).appendChild(this.textify(rest));
	
      } else {
	(span ? span : div).appendChild(this.textify((f>0?"&":"")+fragment));
      }
    }
    node.appendChild(div);
  },
  
  textify: function(evil) {
    return document.createTextNode(evil);
  },
  
  createSpan: function(fragment) {
    if(fragment[0] == ";")
      return null;
    
    var ind = 0;
    var attr, fg, bg;
    
    var m = fragment.match(/([Bbri]?)([1-8])?([1-8])?/);
    attr = m[1];
    fg = m[2];
    bg = m[3];
    
    if(!fg) fg = "2";
    
    var bold = attr=='B';
    
    var span = document.createElement("SPAN");
    span.className = "fg" + fg + (bold ? "b" : "");
    if(bg) span.className += " bg"+bg;
    return span;		
  }	
};

	
