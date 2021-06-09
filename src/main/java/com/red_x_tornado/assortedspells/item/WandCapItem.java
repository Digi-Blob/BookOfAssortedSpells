package com.red_x_tornado.assortedspells.item;

import java.util.function.Supplier;

import com.red_x_tornado.assortedspells.util.WandMaterialManager.WandCap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WandCapItem extends Item implements IWandCapItem {

	private final Supplier<WandCap> cap;

	public WandCapItem(Item.Properties props, Supplier<WandCap> cap) {
		super(props);
		this.cap = cap;
	}

	@Override
	public WandCap asCap(ItemStack stack) {
		return cap.get();
	}
}