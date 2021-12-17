package com.red_x_tornado.assortedspells.tileentity.container;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.client.ClientEvents;
import com.red_x_tornado.assortedspells.init.ASContainers;
import com.red_x_tornado.assortedspells.item.IWandCapItem;
import com.red_x_tornado.assortedspells.item.IWandCoreItem;
import com.red_x_tornado.assortedspells.item.IWandRodItem;
import com.red_x_tornado.assortedspells.item.RuneItem;
import com.red_x_tornado.assortedspells.item.WandItem;
import com.red_x_tornado.assortedspells.network.msg.SpellSyncMessage;
import com.red_x_tornado.assortedspells.util.research.ResearchAttempt;
import com.red_x_tornado.assortedspells.util.research.ResearchInstance;
import com.red_x_tornado.assortedspells.util.spell.SpellType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.thread.EffectiveSide;

public class ResearchTableContainer extends Container {

	public static final ResourceLocation WAND_SLOT = new ResourceLocation("assortedspells", "item/wand_slot");

	private final IInventory researchTableInv;
	private final PlayerInventory playerInv;
	private final IWorldPosCallable worldPosCallable;
	private final SpellCapability caps;

	protected Slot wand;
	public final RuneSlot[] runeSlots = new RuneSlot[7];
	public int runeCount = 0;
	@Nullable
	public ResearchInstance selected;

	public ResearchTableContainer(int id, PlayerInventory playerInv, IInventory inv, IWorldPosCallable worldPosCallable) {
		super(ASContainers.RESEARCH_TABLE.get(), id);
		researchTableInv = inv;
		this.playerInv = playerInv;
		this.worldPosCallable = worldPosCallable;
		caps = SpellCapability.get(playerInv.player);
		setupSlots();
	}

	public ResearchTableContainer(int id, PlayerInventory playerInv, IWorldPosCallable worldPosCallable) {
		this(id, playerInv, new Inventory(9), worldPosCallable);
	}

	public SpellType[] getRuneSpellTypes() {
		int len = 0;
		for (RuneSlot slot : runeSlots)
			if (slot.isEnabled())
				len++;

		final SpellType[] ret = new SpellType[len];

		int i = 0;
		for (RuneSlot slot : runeSlots)
			if (slot.isEnabled())
				ret[i++] = slot.getHasStack() && slot.getStack().getItem() instanceof RuneItem ? ((RuneItem) slot.getStack().getItem()).getType() : null;

		return ret;
	}

	protected void setupSlots() {
		researchTableInv.openInventory(playerInv.player);
		//final boolean serverCheck = !playerInv.player.world.isRemote;

		// Wand
		addSlot(wand = new Slot(researchTableInv, 0, 80, 26) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof WandItem;
			}
			@Override
			public void putStack(ItemStack stack) {
				super.putStack(stack);
				onPlaceWand(stack);
			}
			@Override
			public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
				onTakeWand(thePlayer, stack);
				return super.onTake(thePlayer, stack);
			}
			@Override
			public int getSlotStackLimit() {
				return 1;
			}
		});

		wand.setBackground(PlayerContainer.LOCATION_BLOCKS_TEXTURE, WAND_SLOT);

		for (int i = 0; i < 7; i++)
			addSlot(runeSlots[i] = new RuneSlot(researchTableInv, i + 1, 16 + 21 * i, 51) {
				@Override
				public boolean isEnabled() {
					return slotNumber <= runeCount;
				}
				@Override
				public boolean isItemValid(ItemStack stack) {
					return stack.getItem() instanceof RuneItem;
				}
				@Override
				public int getSlotStackLimit() {
					return 1;
				}
			});

		// Player inventory
		for (int y = 0; y < 3; ++y)
			for (int x = 0; x < 9; ++x)
				addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 100 + y * 18));

		// Player hotbar
		for (int x = 0; x < 9; ++x)
			addSlot(new Slot(playerInv, x, 8 + x * 18, 158));
	}

	protected void onPlaceWand(ItemStack wand) {
		if (selected == null || wand.isEmpty()) return;

		if (selected.getResearch().getSpell().getId().toString().equals(WandItem.getResearchSpell(wand))) {
			final SpellType[] runes = WandItem.getRunes(wand);
			removeRunes(wand, runes);

			final boolean attempted = WandItem.isResearchAttempted(wand, selected.getResearch().getSpell());

			if (attempted && !playerInv.player.world.isRemote) {
				final ResearchAttempt attempt = selected.addAttempt(runes);
				WandItem.removeResearchAttempted(wand);
				if (attempt.isCorrect()) {
					caps.unlock(selected.getResearch().getSpell());
				}
				SpellSyncMessage.sync(playerInv.player, caps);
			} else WandItem.removeResearchAttempted(wand);
		}
	}

	protected void removeRunes(ItemStack wand, SpellType[] runes) {
		if (runes.length != 0) {
			for (int i = 0; i < runes.length; i++) {
				final ItemStack runeStack = new ItemStack(RuneItem.fromSpellType(runes[i]));
				if (runeSlots[i].isEnabled() && !runeSlots[i].getHasStack())
					runeSlots[i].putStack(runeStack);
				else {
					if (playerInv.addItemStackToInventory(runeStack)) {
						playerInv.markDirty();
					} else
						playerInv.player.entityDropItem(runeStack);
				}
			}
			WandItem.setRunes(wand, new SpellType[0]);
		}
	}

	protected void onTakeWand(PlayerEntity player, ItemStack wand) {
		if (selected != null && caps.getResearch().containsValue(selected) && WandItem.getRunes(wand).length == 0) {
			final SpellType[] runes = getRuneSpellTypes();
			for (SpellType type : runes)
				if (type == null) return;
			WandItem.setRunes(wand, runes);
			for (Slot s : runeSlots)
				s.putStack(ItemStack.EMPTY);
			WandItem.setResearchAttempted(wand, false, selected.getResearch().getSpell());
		}
	}

	public void onSelectionChange(@Nullable ResearchInstance selection) {
		selected = selection;
		runeCount = selection == null ? 0 : selection.getResearch().getKey().length;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		final Slot slot = getSlot(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			// If the slot is in the research table inventory...
			if (index < researchTableInv.getSizeInventory()) {
				// ... try to place it in the player inventory.
				// Note that inventorySlots is a list of all slots in the container.
				if (slot == wand)
					onTakeWand(playerIn, slotStack);
				if (!mergeItemStack(slotStack, researchTableInv.getSizeInventory(), inventorySlots.size() - 9, true))
					return ItemStack.EMPTY;
			} else if (slotStack.getItem() instanceof WandItem) { // It's a wand!
				if (!this.wand.getHasStack()) {
					wand.putStack(slotStack.copy());
					slotStack.shrink(1);
				}
			}
			else if (slotStack.getItem() instanceof RuneItem) {
				if (!mergeItemStack(slotStack, 1, runeCount + 1, false))
					return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			slot.onTake(playerIn, stack);
		}

		return stack;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return researchTableInv.isUsableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		researchTableInv.closeInventory(playerIn);
		super.onContainerClosed(playerIn);
		worldPosCallable.consume((world, pos) -> clearContainer(playerIn, world, researchTableInv));
	}

	private static class RuneSlot extends Slot {

		public RuneSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}