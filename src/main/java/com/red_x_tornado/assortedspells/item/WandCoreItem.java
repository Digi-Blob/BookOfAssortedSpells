package com.red_x_tornado.assortedspells.item;

import java.util.function.Supplier;

import com.red_x_tornado.assortedspells.util.WandMaterialManager.WandCore;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WandCoreItem implements IWandCoreItem {

	private final Supplier<WandCore> core;

	public WandCoreItem(Item.Properties props, Supplier<WandCore> core) {
		this.core = core;
	}

	@Override
	public WandCore asCore(ItemStack stack) {
		return core.get();
	}
}