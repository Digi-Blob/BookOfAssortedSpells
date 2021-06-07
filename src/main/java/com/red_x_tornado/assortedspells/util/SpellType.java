package com.red_x_tornado.assortedspells.util;

public enum SpellType {

	AIR("assortedspells.type.air"),
	ARCANE("assortedspells.type.arcane"),
	COSMIC("assortedspells.type.cosmic"),
	DARK("assortedspells.type.dark"),
	EARTH("assortedspells.type.earth"),
	FIRE("assortedspells.type.fire"),
	LIGHT("assortedspells.type.light"),
	WATER("assortedspells.type.water");

	private final String langKey;

	private SpellType(String langKey) {
		this.langKey = langKey;
	}

	public String getLangKey() {
		return langKey;
	}
}