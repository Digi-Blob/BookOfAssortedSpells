package com.red_x_tornado.assortedspells.util;

public enum SpellDifficulty {

	SIMPLE("assortedspells.difficulty.simple"),
	EASY("assortedspells.difficulty.easy"),
	COMPLICATED("assortedspells.difficulty.complicated"),
	IN_DEPTH("assortedspells.difficulty.in_depth"),
	VERY_DIFFICULT("assortedspells.difficulty.very_difficult"),
	INSANE("assortedspells.difficulty.insane");

	private final String langKey;

	private SpellDifficulty(String langKey) {
		this.langKey = langKey;
	}

	public String getLangKey() {
		return langKey;
	}
}