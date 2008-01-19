
var Chat = {
	
	channelView: {},
	
	message: function(who, channel, text) {
		window.alert(who+" ["+channel+"]: "+text);	
	},
	
	tell: function(src, tgt, text) {
		window.alert(src+" told "+tgt+": "+text);
	},
	
	getChannelTab: function(name) {
		var view = this.channelView[name];
		if(view != null)
			return view;
			
                var chatTabs = $("chat-tabs");
                var chatOutput = $("chat-output");
                
                var div = document.createElement
		view = new TextView("chat_"+name); // FIXME: create DIV
		this.channelView[name] = view;
		return view;
	}

};

wicca[1] = [Chat, null, "message", "tell"];
