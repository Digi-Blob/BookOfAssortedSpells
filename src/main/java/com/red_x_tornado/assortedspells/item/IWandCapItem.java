package com.red_x_tornado.assortedspells.item;

import com.red_x_tornado.assortedspells.util.WandMaterialManager;

import net.minecraft.item.ItemStack;

public interface IWandCapItem {
	public WandMaterialManager.WandCap asCap(ItemStack stack);
}