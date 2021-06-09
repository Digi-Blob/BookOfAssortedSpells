package com.red_x_tornado.assortedspells.item;

import java.util.function.Supplier;

import com.red_x_tornado.assortedspells.util.WandMaterialManager.WandRod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WandRodItem extends Item implements IWandRodItem {

	private final Supplier<WandRod> rod;

	public WandRodItem(Item.Properties props, Supplier<WandRod> rod) {
		super(props);
		this.rod = rod;
	}

	@Override
	public WandRod asRod(ItemStack stack) {
		return rod.get();
	}
}