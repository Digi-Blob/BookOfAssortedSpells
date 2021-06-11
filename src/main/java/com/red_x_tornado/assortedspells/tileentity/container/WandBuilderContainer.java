package com.red_x_tornado.assortedspells.tileentity.container;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.init.ASContainers;
import com.red_x_tornado.assortedspells.item.IWandCapItem;
import com.red_x_tornado.assortedspells.item.IWandCoreItem;
import com.red_x_tornado.assortedspells.item.IWandRodItem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class WandBuilderContainer extends Container {

	private final IInventory wandBuilderInv;
	private final PlayerInventory playerInv;
	public boolean showingPartTab = false;
	public int selectedPart = 0;

	public WandBuilderContainer(int id, PlayerInventory playerInv, IInventory inv) {
		super(ASContainers.WAND_BUILDER.get(), id);
		wandBuilderInv = inv;
		this.playerInv = playerInv;
		setupSlots();
	}

	public WandBuilderContainer(int id, PlayerInventory playerInv) {
		this(id, playerInv, new Inventory(7));
	}

	protected void setupSlots() {
		wandBuilderInv.openInventory(playerInv.player);
		final boolean serverCheck = !playerInv.player.world.isRemote;

		// Wand top cap
		addSlot(new Slot(wandBuilderInv, 0, 68, 16) {
			@Override
			public boolean isEnabled() {
				return serverCheck || !showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof IWandCapItem;
			}
		});
		// Wand rod
		addSlot(new Slot(wandBuilderInv, 1, 57, 38) {
			@Override
			public boolean isEnabled() {
				return serverCheck || !showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof IWandRodItem;
			}
		});
		// Wand core
		addSlot(new Slot(wandBuilderInv, 2, 35, 31) {
			@Override
			public boolean isEnabled() {
				return serverCheck || !showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof IWandCoreItem;
			}
		});
		// Wand bottom cap
		addSlot(new Slot(wandBuilderInv, 3, 46, 60) {
			@Override
			public boolean isEnabled() {
				return serverCheck || !showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof IWandCapItem;
			}
		});
		// Wand result
		addSlot(new Slot(wandBuilderInv, 4, 115, 38) {
			@Override
			public boolean isEnabled() {
				return serverCheck || !showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
			@Override
			public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
				onTakeWand(stack);
				return super.onTake(thePlayer, stack);
			}
		});
		// Material in
		addSlot(new Slot(wandBuilderInv, 5, 30, 40) {
			@Override
			public boolean isEnabled() {
				return serverCheck || showingPartTab;
			}
		});
		// Wand material out
		addSlot(new Slot(wandBuilderInv, 6, 124, 40) {
			@Override
			public boolean isEnabled() {
				return serverCheck || showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
			@Override
			public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
				onTakeWandMaterial(stack);
				return super.onTake(thePlayer, stack);
			}
		});

		// Player inventory
		for (int y = 0; y < 3; ++y)
			for (int x = 0; x < 9; ++x)
				addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 87 + y * 18));

		// Player hotbar
		for (int x = 0; x < 9; ++x)
			addSlot(new Slot(playerInv, x, 8 + x * 18, 145));
	}

	protected void onTakeWand(ItemStack wand) {
		
	}

	protected void onTakeWandMaterial(ItemStack mat) {
		
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return wandBuilderInv.isUsableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		wandBuilderInv.closeInventory(playerIn);
		super.onContainerClosed(playerIn);
	}
}