package com.red_x_tornado.assortedspells.item;

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
}