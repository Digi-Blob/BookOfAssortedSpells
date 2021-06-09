package com.red_x_tornado.assortedspells.item;

import com.red_x_tornado.assortedspells.client.gui.TomeScreen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class TomeItem extends Item {

	public TomeItem(Item.Properties props) {
		super(props);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.isSneaking()) {
			if (worldIn.isRemote)
				DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TomeScreen::open);
			return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
		}

		return ActionResult.resultPass(playerIn.getHeldItem(handIn));
	}
}