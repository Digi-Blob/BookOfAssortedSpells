package com.red_x_tornado.assortedspells.util.spell;

public enum SpellDifficulty {

	SIMPLE("assortedspells.difficulty.simple"),
	EASY("assortedspells.difficulty.easy"),
	COMPLICATED("assortedspells.difficulty.complicated"),
	INSANE("assortedspells.difficulty.insane");

	private final String langKey;

	private SpellDifficulty(String langKey) {
		this.langKey = langKey;
	}

	public String getLangKey() {
		return langKey;
	}
}