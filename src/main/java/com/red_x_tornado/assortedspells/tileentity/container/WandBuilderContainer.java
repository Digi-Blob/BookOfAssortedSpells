package com.red_x_tornado.assortedspells.tileentity.container;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.init.ASContainers;
import com.red_x_tornado.assortedspells.init.ASItems;
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
import net.minecraft.nbt.CompoundNBT;

public class WandBuilderContainer extends Container {

	private final IInventory wandBuilderInv;
	private final PlayerInventory playerInv;
	public boolean showingPartTab = false;
	public int selectedPart = 0;

	protected Slot topCap;
	protected Slot rod;
	protected Slot core;
	protected Slot bottomCap;
	protected Slot wandOut;

	protected Slot material;
	protected Slot materialOut;

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
		addSlot(topCap = new Slot(wandBuilderInv, 0, 68, 16) {
			@Override
			public boolean isEnabled() {
				return serverCheck || !showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof IWandCapItem;
			}
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				onWandSlotChange();
			}
		});
		// Wand rod
		addSlot(rod = new Slot(wandBuilderInv, 1, 57, 38) {
			@Override
			public boolean isEnabled() {
				return serverCheck || !showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof IWandRodItem;
			}
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				onWandSlotChange();
			}
		});
		// Wand core
		addSlot(core = new Slot(wandBuilderInv, 2, 35, 31) {
			@Override
			public boolean isEnabled() {
				return serverCheck || !showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof IWandCoreItem;
			}
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				onWandSlotChange();
			}
		});
		// Wand bottom cap
		addSlot(bottomCap = new Slot(wandBuilderInv, 3, 46, 60) {
			@Override
			public boolean isEnabled() {
				return serverCheck || !showingPartTab;
			}
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof IWandCapItem;
			}
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				onWandSlotChange();
			}
		});
		// Wand result
		addSlot(wandOut = new Slot(wandBuilderInv, 4, 115, 38) {
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
		addSlot(material = new Slot(wandBuilderInv, 5, 30, 40) {
			@Override
			public boolean isEnabled() {
				return serverCheck || showingPartTab;
			}
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				onMaterialSlotChange();
			}
		});
		// Wand material out
		addSlot(materialOut = new Slot(wandBuilderInv, 6, 124, 40) {
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

	protected void onWandSlotChange() {
		final ItemStack topCapStack = this.topCap.getStack();
		final ItemStack rodStack = this.rod.getStack();
		final ItemStack coreStack = this.core.getStack();
		final ItemStack bottomCapStack = this.bottomCap.getStack();

		if (!topCapStack.isEmpty() && !rodStack.isEmpty() && !coreStack.isEmpty() && !bottomCapStack.isEmpty()) {
			final IWandCapItem top = (IWandCapItem) topCapStack.getItem();
			final IWandRodItem rod = (IWandRodItem) rodStack.getItem();
			final IWandCoreItem core = (IWandCoreItem) coreStack.getItem();
			final IWandCapItem bottom = (IWandCapItem) bottomCapStack.getItem();

			final ItemStack wand = new ItemStack(ASItems.WAND.get());
			final CompoundNBT mats = wand.getOrCreateChildTag("wandMaterials");
			mats.putString("topCap", top.asCap(topCapStack).getId().toString());
			mats.putString("rod", rod.asRod(rodStack).getId().toString());
			mats.putString("core", core.asCore(coreStack).getId().toString());
			mats.putString("bottomCap", bottom.asCap(bottomCapStack).getId().toString());

			wandOut.putStack(wand);

			return;
		}

		wandOut.putStack(ItemStack.EMPTY);
	}

	protected void onMaterialSlotChange() {
		
	}

	protected void onTakeWand(ItemStack wand) {
		topCap.putStack(ItemStack.EMPTY);
		rod.putStack(ItemStack.EMPTY);
		core.putStack(ItemStack.EMPTY);
		bottomCap.putStack(ItemStack.EMPTY);
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