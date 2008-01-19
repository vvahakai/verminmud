
var BattleIcons = Class.create();
BattleIcons.prototype = {
   initialize: function() {
      var imageSrcs = {
         "attack_1" : "graf/icons/attack_1.png",
         "attack_2" : "graf/icons/attack_2.png",
         "attack_3" : "graf/icons/attack_3.png",
         "attack_4" : "graf/icons/attack_4.png",
         "attack_5" : "graf/icons/attack_5.png",
         "attack_6" : "graf/icons/attack_6.png",
         "attack_7" : "graf/icons/attack_6.png",
         "attack_8" : "graf/icons/attack_6.png",
         "attack_9" : "graf/icons/attack_6.png",
         "attack_failed" : "graf/icons/attack_failed.png",
         "block" : "graf/icons/block.png",
         "damage_taken" : "graf/icons/damage_taken.png",
         "no_action" : "graf/icons/no_action.png",
         "death" : "graf/icons/death.png"
      };

      this.images = {};
      for(i in imageSrcs) {
         var img = document.createElement("IMG");
         img.src = imageSrcs[i];
         this.images[i] = img;
      }
   },
   
   getIcon: function(name) {
      return this.images[name].cloneNode(false);
   }
}

var Battle = {
  initialize: function() {
    this.icons = new BattleIcons();
    this.playerDisplay = document.getElementById('player_battle_display');
    this.enemyDisplay = document.getElementById('enemy_battle_display');
		
    this.inBattle = false;
    this.clear();
  },
	
  attack: function(attack, aType, rType) {
    attack = parseInt(""+attack);
    aType = ""+aType;
    rType = ""+rType;

    if(this.inBattle == false) {
      this.inBattle = true;
      this.clear();
    }
		
    switch(aType) {
      case "hit":
      var img = this.mapImage(attack);
      this.buffer(this.playerDisplay, img);
      break;
      case "fail":
      this.buffer(this.playerDisplay, this.icons.getIcon("attack_failed"));
      this.buffer(this.enemyDisplay, this.icons.getIcon("no_action"));
      break;
    }
		
    switch(rType) {
      case "take":
      this.buffer(this.enemyDisplay, this.icons.getIcon("damage_taken"));
      break;
      case "prevent":
      this.buffer(this.enemyDisplay, this.icons.getIcon("block"));
      break;
    }
  },
	
  defend: function(attack, aType, rType) {
    attack = parseInt(""+attack);
    aType = ""+aType;
    rType = ""+rType;

    if(this.inBattle == false) {
      this.inBattle = true;
      this.clear();
    }

    switch(aType) {
      case "hit":
      var img = this.mapImage(attack);
      this.buffer(this.enemyDisplay, img);
      break;
      case "fail":
      this.buffer(this.enemyDisplay, this.icons.getIcon("attack_failed"));
      this.buffer(this.playerDisplay, this.icons.getIcon("no_action"));
      break;
    }
		
    switch(rType) {
      case "take":
      this.buffer(this.playerDisplay, this.icons.getIcon("damage_taken"));
      break;
      case "prevent":
      this.buffer(this.playerDisplay, this.icons.getIcon("block"));
      break;
    }
  },
	
  die: function(who) {
    who = ""+who;
    switch(who) {
      case "enemy":
      this.buffer(this.enemyDisplay, this.icons.getIcon("death"));
      this.buffer(this.playerDisplay, this.icons.getIcon("no_action"));				
      break;
      case "friend":
      this.buffer(this.playerDisplay, this.icons.getIcon("death"));
      this.buffer(this.enemyDisplay, this.icons.getIcon("no_action"));				
      break;
      case "player":
      this.buffer(this.playerDisplay, this.icons.getIcon("death"));
      this.buffer(this.enemyDisplay, this.icons.getIcon("no_action"));				
      break;
    }
  },
	
  mapImage: function(prec) {
    if(prec < 3) {
      return this.icons.getIcon("attack_1");
    } else if(prec < 6) {
      return this.icons.getIcon("attack_2");
    } else if(prec < 10) {
      return this.icons.getIcon("attack_3");
    } else if(prec < 17) {
      return this.icons.getIcon("attack_4");
    } else if(prec < 25) {
      return this.icons.getIcon("attack_5");
    } else {
      return this.icons.getIcon("attack_6");
    }	
  },
	
  end: function() {
    this.inBattle = false;
  },
	
  clear: function() {
    var empty = this.icons.getIcon("no_action");
    empty.style.visibility = 'hidden';
		
    for(var i=0; i<13; i++) {
      this.buffer(this.playerDisplay, empty.cloneNode(false));
      this.buffer(this.enemyDisplay, empty.cloneNode(false));
    }
  },
	
  buffer: function(b, img) {
    while(b.childNodes.length >= 13) {
      b.removeChild(b.firstChild);
    }
    b.appendChild(img);

    for(var i=0; i<b.childNodes.length; i++) {
      var opacity = (i+1)/14;
      b.childNodes[i].style.filter = "alpha(opacity:"+opacity+")";
      b.childNodes[i].style.opacity = opacity;
    }
  }
}

wicca[4] = [Battle, null, "attack", "defend", "end", "die"];
