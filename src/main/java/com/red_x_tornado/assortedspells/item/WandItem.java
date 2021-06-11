package com.red_x_tornado.assortedspells.item;

import static com.red_x_tornado.assortedspells.util.ResourceLocations.as;

import java.util.List;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.client.gui.TomeScreen;
import com.red_x_tornado.assortedspells.util.WandMaterialManager;
import com.red_x_tornado.assortedspells.util.WandMaterialManager.WandCap;
import com.red_x_tornado.assortedspells.util.WandMaterialManager.WandCore;
import com.red_x_tornado.assortedspells.util.WandMaterialManager.WandRod;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;

public class WandItem extends Item {

	public WandItem(Item.Properties props) {
		super(props);
	}

	public static WandCap getTopCap(ItemStack stack) {
		final ResourceLocation id = maybeGet(stack, "topCap");
		if (id == null) return WandMaterialManager.IRON_CAP;
		final WandCap cap = WandMaterialManager.getCap(id);
		if (cap == null) return WandMaterialManager.IRON_CAP;
		return cap;
	}

	public static WandRod getRod(ItemStack stack) {
		final ResourceLocation id = maybeGet(stack, "rod");
		if (id == null) return WandMaterialManager.OAK_ROD;
		final WandRod rod = WandMaterialManager.getRod(id);
		if (rod == null) return WandMaterialManager.OAK_ROD;
		return rod;
	}

	public static WandCap getBottomCap(ItemStack stack) {
		final ResourceLocation id = maybeGet(stack, "bottomCap");
		if (id == null) return WandMaterialManager.IRON_CAP;
		final WandCap cap = WandMaterialManager.getCap(id);
		if (cap == null) return WandMaterialManager.IRON_CAP;
		return cap;
	}

	public static WandCore getCore(ItemStack stack) {
		final ResourceLocation id = maybeGet(stack, "core");
		if (id == null) return WandMaterialManager.COAL_CORE;
		final WandCore core = WandMaterialManager.getCore(id);
		if (core == null) return WandMaterialManager.COAL_CORE;
		return core;
	}

	@Nullable
	private static ResourceLocation maybeGet(ItemStack stack, String key) {
		if (!stack.hasTag() || !stack.getTag().contains("wandMaterials", Constants.NBT.TAG_COMPOUND)) return null;
		final CompoundNBT mats = stack.getTag().getCompound("wandMaterials");
		return mats.contains(key, Constants.NBT.TAG_STRING) ? new ResourceLocation(mats.getString(key)) : null;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		final WandCap topCap = getTopCap(stack);
		final WandRod rod = getRod(stack);
		final WandCap bottomCap = getBottomCap(stack);
		final WandCore core = getCore(stack);

		if (topCap != null)
			tooltip.add(new TranslationTextComponent(topCap.getLangKey()).mergeStyle(TextFormatting.GRAY));
		if (rod != null)
			tooltip.add(new TranslationTextComponent(rod.getLangKey()).mergeStyle(TextFormatting.GRAY));
		if (core != null)
			tooltip.add(new TranslationTextComponent(core.getLangKey()).mergeStyle(TextFormatting.GRAY));
		if (bottomCap != null)
			tooltip.add(new TranslationTextComponent(bottomCap.getLangKey()).mergeStyle(TextFormatting.GRAY));

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.isSneaking()) {
			if (worldIn.isRemote)
				DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TomeScreen::open);
			return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
		}

		final ItemStack stack = playerIn.getHeldItem(handIn);
		final SpellCapability caps = SpellCapability.get(playerIn);

		if (caps.getSelected() == null)
			playerIn.sendStatusMessage(new TranslationTextComponent("assortedspells.message.nospells"), true);

		else if (caps.getSelected().canCast(caps, stack)) {
			caps.castSelected(stack);
			return ActionResult.resultSuccess(stack);
		}

		return ActionResult.resultPass(stack);
	}
}