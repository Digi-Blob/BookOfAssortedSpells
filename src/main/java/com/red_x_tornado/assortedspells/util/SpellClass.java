package com.red_x_tornado.assortedspells.util;

public enum SpellClass {

	SUPPORT("assortedspells.class.support"),
	UTILITY("assortedspells.class.utility"),
	ATTACK("assortedspells.class.attack");

	private final String langKey;

	private SpellClass(String langKey) {
		this.langKey = langKey;
	}

	public String getLangKey() {
		return langKey;
	}
}