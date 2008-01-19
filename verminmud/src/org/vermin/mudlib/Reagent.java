package org.vermin.mudlib;

public enum Reagent {

	ALLEVIL_ROOT ("Allevil root", "A black rubbery root with many small hairy sprouting appendages."),
	ANGELICA ("Angelica flowers", "These regular-looking white flowers are from rather simple plant that has glossy narrow leaves. The clusters of flowers are attached to long straws up to 3 feet above surface."),
	ARCHANGELICA ("Archangelica flowers", "These flowers of a strong-growing Archangelica plant are green with white petal edges."),
	ARNICA ("Arnica petals", "These soft, bright yellow petals have been collected from common Arnica flowers."),
	ASTRAGALUS ("Flower of Astragalus", "This flower with petals growing upwards is colored in shades of violet and purple and has been cut of an Astragalus plant."),
	BALLOON_FLOWER ("Balloon Flower", "A perfectly star-shaped violet flower with five-pointed white pistil."),
	BLACK_COHOSH ("Black Cohosh root", "A thick dark brown root of Black Cohosh."),
	BLOODROOT ("Root of Bloodroot", "A strong root of a plant called Bloodroot which sprouts beautiful white flowers. The center of the root is red and white and resembles the appearance of a blood-vein."),
	BONESET ("Boneset blossom", "A blossom containing several white, small Boneset flowers."),
	CARDAMON ("Cardamon pods", "A handful of light brown and green cardamon pods."),
	CATNIP ("Catnip straws", "Common looking plant straws from a Catnip plant."),
	CHICORY ("Chicory flower", "A purple flower with long pedals, each of which has one tall stamen growing upwards."),
	ELDERBERRY ("A cluster of Elderberries", "A cluster of almost transparent ice-blue Elderberries."),
	GOATS_RUE ("A blossom of Goat's Rue", "A blossom of Goat's Rue plant containing red, white and violet petals."),
	HEARTSEASE ("Heartsease flower", "A beautiful flower of Heartsease, colored in purple, yellow and white. There are striples of purple going outwards from the pistil."),
	LARKSPUR ("A Larkspur straw", "A long straw from a Larkspur plant, stripped-down from all the beautiful flowers."),
	NIGELLA ("The core of Nigella flower", "This core of a Nigella flower contains a pinsil and several curvy stamen."),
	SEA_HOLLY ("Flower of Sea Holly", "This is a sapphire blue flower of Sea Holly, which looks like a teasil or a thistle, with a star-like collar of spiky bracts at the base of each bloom."),
	WITCHHAZEL ("Witchhazel flower", "A yellow flower of Witchhazel with coils of long thin petals."),
	WINTERGREEN ("Wintergreen leaves", "A bunch of Wintergreen leaves. The leaves are heavy and have a mat-like form.");
	
	Reagent(String desc, String longdesc) {
		this.desc = desc;
		this.longdesc = longdesc;
	}
	
	public final String desc;
	
	public final String longdesc;

}
