package com.red_x_tornado.assortedspells.item;

import com.red_x_tornado.assortedspells.capability.SpellCapability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class WandItem extends Item {

	public WandItem(Item.Properties props) {
		super(props);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		final ItemStack stack = playerIn.getHeldItem(handIn);
		final SpellCapability caps = SpellCapability.get(playerIn);

		if (caps.getSelected().canCast(caps, stack)) {
			caps.castSelected(stack);
			return ActionResult.resultSuccess(stack);
		}

		return ActionResult.resultPass(stack);
	}
}