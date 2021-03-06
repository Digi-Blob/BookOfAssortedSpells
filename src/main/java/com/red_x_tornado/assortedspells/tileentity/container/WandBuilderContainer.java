package com.red_x_tornado.assortedspells.tileentity.container;

import com.red_x_tornado.assortedspells.init.ASContainers;
import com.red_x_tornado.assortedspells.init.ASItems;
import com.red_x_tornado.assortedspells.item.IWandCapItem;
import com.red_x_tornado.assortedspells.item.IWandCoreItem;
import com.red_x_tornado.assortedspells.item.IWandRodItem;
import com.red_x_tornado.assortedspells.network.ASNetworkManager;
import com.red_x_tornado.assortedspells.network.msg.WandBuilderMaterialMessage;
import com.red_x_tornado.assortedspells.util.WandRecipeManager;
import com.red_x_tornado.assortedspells.util.WandRecipeManager.CachingRecipeMatcher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class WandBuilderContainer extends Container {

	private final IInventory wandBuilderInv;
	private final PlayerInventory playerInv;
	public boolean showingPartTab = false;
	public byte selectedPart = 0;

	protected Slot topCap;
	protected Slot rod;
	protected Slot core;
	protected Slot bottomCap;
	protected Slot wandOut;

	protected Slot material;
	protected Slot materialOut;

	protected final CachingRecipeMatcher caps = new CachingRecipeMatcher(WandRecipeManager.getCapRecipes());
	protected final CachingRecipeMatcher rods = new CachingRecipeMatcher(WandRecipeManager.getRodRecipes());
	protected final CachingRecipeMatcher cores = new CachingRecipeMatcher(WandRecipeManager.getCoreRecipes());

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

	public void setSelectedPart(byte value) {
		selectedPart = value;
		onMaterialSlotChange(); // It didn't really, but this is the best way to reuse logic.
		if (playerInv.player.world.isRemote)
			ASNetworkManager.get().sendToServer(new WandBuilderMaterialMessage(value));
	}

	protected void onMaterialSlotChange() {
		final ItemStack material = this.material.getStack();

		if (!material.isEmpty()) {
			final WandRecipeManager.IWandMaterialRecipe recipe;

			if (selectedPart == 0) // Rod
				recipe = rods.findMatching(material);
			else if (selectedPart == 1) // Core
				recipe = cores.findMatching(material);
			else if (selectedPart == 2) // Cap
				recipe = caps.findMatching(material);
			else recipe = null;

			if (recipe != null) {
				final ItemStack stack = recipe.craft(material);
				materialOut.putStack(stack);
				return;
			}
		}

		materialOut.putStack(ItemStack.EMPTY);
	}

	protected void onTakeWand(ItemStack wand) {
		topCap.decrStackSize(1);
		rod.decrStackSize(1);
		core.decrStackSize(1);
		bottomCap.decrStackSize(1);

		topCap.onSlotChanged();
		rod.onSlotChanged();
		core.onSlotChanged();
		bottomCap.onSlotChanged();
	}

	protected void onTakeWandMaterial(ItemStack mat) {
		material.decrStackSize(1);
		material.onSlotChanged();
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

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		final Slot slot = getSlot(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			// If the slot is in the wand builder inventory...
			if (index < wandBuilderInv.getSizeInventory()) {
				// ... try to place it in the player inventory.
				// Note that inventorySlots is a list of all slots in the container.
				if (!mergeItemStack(slotStack, wandBuilderInv.getSizeInventory(), inventorySlots.size() - wandBuilderInv.getSizeInventory(), true))
					return ItemStack.EMPTY;
			} else {
				if (!mergeItemStack(slotStack, 0, wandBuilderInv.getSizeInventory(), false))
					return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			slot.onTake(playerIn, slotStack);
		}

		return stack;
	}
}