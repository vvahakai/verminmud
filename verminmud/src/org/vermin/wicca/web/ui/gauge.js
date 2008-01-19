var FractionModel = Class.create();
FractionModel.prototype = {
   initialize: function(current, max) {
      this.current = current;
      if(max < 1) {
         max = 1;
      }
      this.max = max;
      this.listeners = [];
      this.preText = "";
   },
   
   setPreText: function(txt) {
    this.preText = txt;
  },
   
   setCurrent: function(current) {
    this.current = current;
    this.fireEvents();
  },
   
   setMax: function(max) {
      this.max = max;
      this.fireEvents();
   },
   
   addListener: function(listener) {
      this.listeners.push(listener);
   },
   
   getText: function() {
      return this.preText + this.current+" / "+this.max; 
   },
   
   getPercent: function() {
      return (100*this.current/this.max);
   },
   
   fireEvents: function() {
      for(var i=0; i<this.listeners.length; i++) {
         this.listeners[i](this);
      }
   }
};

var Gauge = Class.create();
Gauge.prototype = {
   initialize: function(bar, text, model) {
      this.bar = bar;
      this.text = text;
      this.model = model;
      this.model.addListener(this.redraw.bind(this));
   },
   
   redraw: function(model) {
      this.bar.style.width = model.getPercent()+"%";
      this.text.firstChild.nodeValue = model.getText();
   }
};

