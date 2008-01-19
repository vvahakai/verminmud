package org.vermin.mudlib.outworld;

import java.util.EnumSet;

import org.vermin.mudlib.Reagent;

public enum TerrainType {
	
	SWAMP ("Swamp.", 
			"You are wading in a wet swamp. On every step you take, your shoes " +
			"sink deep in the murky insides of the swamp. You see fallen trees " +
			"languished bushes everywhere, laying in sickly gray water. The " +
			"smell of rotting plants and stale water almost makes you puke. " +
			"You wonder why the hell you ever got here.", 
			0, false,
			EnumSet.of(Reagent.WINTERGREEN, Reagent.BALLOON_FLOWER, Reagent.ASTRAGALUS, Reagent.HEARTSEASE)),
			
	LIGHT_FOREST ("Light forest.", 
			"This forest is delightfully light and easy to travel through. This " +
			"would surely be a great place for a picnic. The trees are mainly " +
			"birches, but you can spot many other deciduous trees. Your mood " +
			"is really rising up and you feel like taking your clothes off and " +
			"running around naked (My God, what kind of a pervert are you?).", 
			0, false,
			EnumSet.of(Reagent.CHICORY, Reagent.NIGELLA, Reagent.ARNICA, Reagent.ANGELICA)),
			
	DENSE_FOREST ("Dense forest.", 
			"The forest is really thick here, and you have to push the branches " +
			"constantly from your way in order to proceed. The sun is totally " +
			"blocked out by the tall spruces and it's actually quite dark down " +
			"here. The ground is all covered with spruce needles and you can " +
			"spot some really big cones on the ground. You bet this place is " +
			"full of squirrels.", 
			0, false,
			EnumSet.of(Reagent.ELDERBERRY, Reagent.BONESET, Reagent.WITCHHAZEL, Reagent.ARCHANGELICA)),
			
	JUNGLE ("Jungle.", 
			"This tropical rainforest is full of exotic plants and animals. " +
			"Unfortunately it is also full of pesky bloodsucking insects and " +
			"the air is so moist that your clothes get wet in no time. Most of " +
			"the trees look at least a mile tall and have bananas and other " +
			"fruits hanging from their branches. Too bad that they are all raw " +
			"at this time of the year.", 
			0, false,
			EnumSet.of(Reagent.BALLOON_FLOWER, Reagent.ASTRAGALUS, Reagent.NIGELLA)),
			
	GRASSLANDS ("Grasslands.", 
			"As far as you can see there is only grass and some bushes here. " +
			"This place bores the hell out of you. The only interesting things " +
			"that are here are the rabbits hopping around eating grass. This " +
			"place must be heaven to them with all this grass around and no " +
			"predators at all. You feel like kicking the nearest rabbit that " +
			"is munching grass almost between your feet.", 
			0, false,
			EnumSet.of(Reagent.LARKSPUR, Reagent.CATNIP, Reagent.CARDAMON, Reagent.HEARTSEASE)),
			
	HILLS ("Hills.", 
			"These hills are quite high. You are getting a bit tired of this " +
			"constant climbing up and down. The ground is quite arid and you " +
			"can see only few almost leafless bushes sticking out of the hard " +
			"soil. The wind produces strange howling noise in the slopes " +
			"between the hills.", 
			0, false,
			EnumSet.of(Reagent.GOATS_RUE, Reagent.BLACK_COHOSH)),
			
	BADLANDS ("Badlands.", 
			"This place has probably been a forest once but is has been ruined " +
			"by a strong evil force dwelling nearby. The landscape is barren " +
			"orange-reddish dust. Almost all of the trees have died and the " +
			"last few alive look like they are trying to crawl away with their " +
			"roots. You can sense the presence of evil even by yourself, " +
			"dreadfully strong.", 
			0, false,
			EnumSet.of(Reagent.BLOODROOT, Reagent.CARDAMON)),
			
	SMALL_RIVER("Small river", 
			"This is a little, peacefully flowing river. The water is cold and " +
			"very clear: you can see to the very bottom of the river, even in " +
			"the deeper places. You see many kinds of colorful fishes in the " +
			"water, swimming in their fishy purposes. This is not a bad place " +
			"at all, you just wish you would have taken your trousers off " +
			"before coming here.", 
			45, false,
			EnumSet.noneOf(Reagent.class)),
			
	DEEP_RIVER ("Deep river.", 
			"The river is flowing very fast here and you must hold on to rocks " +
			"that you wouldn't flush away with it. You really wish that you " +
			"would have taken those swimming lessons as as child.", 
			85, false,
			EnumSet.noneOf(Reagent.class)),
			
	DESERT ("Desert.", 
			"Endless sand dunes cover this area. There's no sight of water or " +
			"plants or actually ANYTHING but the sand. The only things that " +
			"break the landscape are your own footprints, travelling way back " +
			"across the sands. You are thirsty and you wish to get out of here " +
			"as soon as possible. This must be a hellish place for the animals " +
			"living here.", 
			0, false,
			EnumSet.of(Reagent.BLACK_COHOSH, Reagent.CARDAMON)),
			
	VOLCANIC ("Volcanic area.", 
			"This place is hot, rocky and full of boulders about twenty meters " +
			"high. You see lots of hot springs and geysers gushing water and " +
			"steam way up to the air. You can also see lots of pools full of " +
			"unspeakable ooze, in various colors of course. There is no signs " +
			"of any animals, but then again, what kind of an animal would " +
			"stand this suffocating stench of sulphur?", 
			0, false,
			EnumSet.noneOf(Reagent.class)),
			
	OCEAN ("Ocean.", 
			"The water is very deep and salty here. You can't see the bottom " +
			"or the surface and you wonder how long you can still hold your " +
			"breath. You feel that it would be a good idea to swim back to the " +
			"solid ground.", 
			99, false,
			EnumSet.of(Reagent.SEA_HOLLY)),
			
	TUNDRA ("Tundra.", 
			"This is a snow-covered flatland with almost no vegetation. Wind " +
			"is blowing hard straight to your face. You wish you would have " +
			"taken your mittens and your wooly hat with you. You can see some " +
			"animal tracks going around, probably a pack of wolves in search " +
			"of food.", 
			0, false,
			EnumSet.of(Reagent.ELDERBERRY)),
			
	ROAD ("Road.", 
			"You are on a very depressing looking cobblestone road. They say " +
			"that all roads lead to Rome, but you are quite sure that this one " +
			"doesn't.", 
			0, false,
			EnumSet.noneOf(Reagent.class)),
			
	BRIDGE ("Bridge.", 
			"You are on a very depressing looking stone bridge. Maybe there is " +
			"a troll under it?", 
			0, false,
			EnumSet.noneOf(Reagent.class)),
			
	RUINS ("Ruins.", 
			"You are standing in the ruins of what must have been a settlement " +
			"of some kind. The surroundings look very depressing. No one knows " +
			"who ruined these ruins.", 
			0, false,
			EnumSet.of(Reagent.ALLEVIL_ROOT)),
			
	LAKE ("Lake.", 
			"The water in this lake is warm and bright. You are enjoying " +
			"yourself in here though you are getting rather wet at the same " +
			"time.", 
			0, false,
			EnumSet.noneOf(Reagent.class)),
			
	FIELDS ("Fields.", 
			"Fields growing various local plants strech as far as the eye can " +
			"see. You see lots of farmers working on their agricultural duties.", 
			0, false,
			EnumSet.of(Reagent.CATNIP, Reagent.LARKSPUR, Reagent.CARDAMON)),
			
	SHORE ("Shore.", 
			"You are standing on the shore of a great ocean. It's very windy " +
			"here and the waves make a constant distant roar filling the air.", 
			0, false,
			EnumSet.of(Reagent.HEARTSEASE, Reagent.SEA_HOLLY)),
			
	SHALLOWS ("Shallows.", 
			"The water is very shallow here and you can clearly see the rocky " +
			"bottom of the sea. A lot of small fish seem to like this area; " +
			"you see schools of them swimming around.", 
			85, false,
			EnumSet.of(Reagent.SEA_HOLLY)),
			
	HIGHLANDS ("Highlands.", 
			"This is a very depressing highlands. You note that they are quite " +
			"low, actually.", 
			0, false,
			EnumSet.of(Reagent.GOATS_RUE, Reagent.BLOODROOT)),
			
	WATERFALLS ("Waterfalls.", 
			"This is a very depressing waterfalls. All the water seems to be " +
			"falling on you.", 
			0, false,
			EnumSet.of(Reagent.ANGELICA, Reagent.ARCHANGELICA, Reagent.SEA_HOLLY)),
			
	THE_LONE_MOUNTAIN ("The lone mountain.", 
			"",
			0, false,
			EnumSet.noneOf(Reagent.class)),
			
	CROSSING ("Crossing.", 
			"You are on a crossing of cobblestone roads. Where do you want to " +
			"go today?", 
			0, false,
			EnumSet.noneOf(Reagent.class)),
			
	TREETOPS ("Treetops.", 
			"You are flying in the treetops.", 0, false,
			EnumSet.noneOf(Reagent.class)),
			
	HILLTOPS ("Hill top.", 
			"You are on the top of the hill. You feel like proclaiming that " +
			"you are the king of it.", 
			0, false,
			EnumSet.of(Reagent.GOATS_RUE, Reagent.BLACK_COHOSH)),
			
	AIR ("Open air.", 
			"You are flying in open air. Whee!", 
			0, true,
			EnumSet.noneOf(Reagent.class)),
			
	PASSABLE_MOUNTAIN ("Passable mountains.", 
			"You are climbing in the mountains. Be careful not to fall.", 
			0, false,
			EnumSet.noneOf(Reagent.class)),
			
	WATER_BOTTOM ("Shallow bottom.", 
			"You are at the bottom of the shallow waters.", 
			100, false,
			EnumSet.noneOf(Reagent.class)),
			
	DEEP_WATER_BOTTOM ("Deep bottom.", 
			"You are at the bottom of the deep waters.", 
			100, false,
			EnumSet.noneOf(Reagent.class)),
			
	SEA_BOTTOM ("Sea bottom.", 
			"You are at the bottom of the sea.", 
			100, false,
			EnumSet.noneOf(Reagent.class)),
			
	UNDERWATER("Underwater.", 
			"You are swimming underwater.",
			100, false,
			EnumSet.noneOf(Reagent.class)),
			
	DEEP_UNDERWATER ("Deep underwater.", 
			"You swimming deep underwater.", 
			100, false,
			EnumSet.noneOf(Reagent.class)),
			
	HIGH_AIR ("High above the ground.", 
			"You are flying high above the ground.", 
			0, true,
			EnumSet.noneOf(Reagent.class)),
			
	ABOVE_CLOUDS ("Above the clouds.", 
			"You are flying above the clouds.", 
			0, true,
			EnumSet.noneOf(Reagent.class));
	
	
	TerrainType(String desc, String longdesc, int waterLevel, boolean requireAviation, EnumSet<Reagent> reagents) {
		this.desc = desc;
		this.longdesc = longdesc;
		this.waterLevel = waterLevel;
		this.requireAviation = requireAviation;
		this.reagents = reagents;
	}
	
	public final String desc;
	public final String longdesc;
	public final int waterLevel;
	public final boolean requireAviation;
	public final EnumSet<Reagent> reagents;
	
}
