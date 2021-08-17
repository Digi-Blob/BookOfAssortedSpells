package com.red_x_tornado.assortedspells.item;

import com.red_x_tornado.assortedspells.init.ASItems;
import com.red_x_tornado.assortedspells.util.spell.SpellType;

import net.minecraft.item.Item;

public class RuneItem extends Item {

	private final SpellType type;

	public RuneItem(Item.Properties props, SpellType type) {
		super(props);
		this.type = type;
	}

	public SpellType getType() {
		return type;
	}

	public static Item fromSpellType(SpellType type) {
		switch (type) {
		case AIR: return ASItems.AIR_RUNE.get();
		case ARCANE: return ASItems.ARCANE_RUNE.get();
		case COSMIC: return ASItems.COSMIC_RUNE.get();
		case DARK: return ASItems.DARK_RUNE.get();
		case EARTH: return ASItems.EARTH_RUNE.get();
		case FIRE: return ASItems.FIRE_RUNE.get();
		case LIGHT: return ASItems.LIGHT_RUNE.get();
		case WATER: return ASItems.WATER_RUNE.get();
		default: return null;
		}
	}
}