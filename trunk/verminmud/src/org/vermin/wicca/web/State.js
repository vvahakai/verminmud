
var State = {
  
  initialize: function() {
    this.sp = new FractionModel(50, 100);
    this.sp.setPreText("SP: ");
    this.spgauge = new Gauge($('spbar'), $('sptext'), this.sp);
    
    this.hp = new FractionModel(50,100);
    this.hp.setPreText("HP: ");
    this.hpgauge = new Gauge($('hpbar'), $('hptext'), this.hp);
  },
  
  update: function(hp, maxhp, sp, maxsp, exp) {
    this.sp.setMax(parseInt(new String(maxsp)));
    this.sp.setCurrent(parseInt(new String(sp)));
    this.hp.setMax(parseInt(new String(maxhp)));
    this.hp.setCurrent(parseInt(new String(hp)));
  }
}

wicca[3] = [State, null, "update"];
