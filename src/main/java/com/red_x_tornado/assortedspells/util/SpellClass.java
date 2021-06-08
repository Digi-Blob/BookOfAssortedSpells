package com.red_x_tornado.assortedspells.util;

/**
 * Represents the three possible spell classes.
 * @see #SUPPORT
 * @see #UTILITY
 * @see #ATTACK
 */
public enum SpellClass {

	/**
	 * Spells with this class are for support purposes, such as healing.
	 */
	SUPPORT("assortedspells.class.support"),

	/**
	 * Spells with this class are for utility purposes, such as smelting items, crafting, or toggling that lever that's just out of reach.
	 */
	UTILITY("assortedspells.class.utility"),

	/**
	 * Spells with this class are for attacking purposes, such as fireballs, lightning, a levitating sword spell, etc.
	 */
	ATTACK("assortedspells.class.attack");

	private final String langKey;

	private SpellClass(String langKey) {
		this.langKey = langKey;
	}

	public String getLangKey() {
		return langKey;
	}
}