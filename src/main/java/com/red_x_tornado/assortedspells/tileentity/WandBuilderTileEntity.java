package com.red_x_tornado.assortedspells.tileentity;

import com.red_x_tornado.assortedspells.init.ASTileEntities;
import com.red_x_tornado.assortedspells.tileentity.container.WandBuilderContainer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class WandBuilderTileEntity extends TileEntity implements INamedContainerProvider, INameable {

	/**
	 * Slots:<br>
	 * 0 -&gt; Wand top cap<br>
	 * 1 -&gt; Wand rod<br>
	 * 2 -&gt; Wand core<br>
	 * 3 -&gt; Wand bottom cap<br>
	 * 4 -&gt; Wand result<br>
	 * 5 -&gt; Material input<br>
	 * 6 -&gt; Wand material output<br>
	 */
	private Inventory inventory = new Inventory(7);

	public WandBuilderTileEntity(TileEntityType<? extends WandBuilderTileEntity> type) {
		super(type);
	}

	public WandBuilderTileEntity() {
		this(ASTileEntities.WAND_BUILDER.get());
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		inventory.read(nbt.getList("inventory", Constants.NBT.TAG_COMPOUND));
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		nbt.put("inventory", inventory.write());
		return nbt;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
		return new WandBuilderContainer(windowId, playerInv, inventory);
	}

	@Override
	public ITextComponent getDisplayName() {
		return getName();
	}

	@Override
	public ITextComponent getName() {
		return new TranslationTextComponent("block.assortedspells.wand_builder");
	}
}