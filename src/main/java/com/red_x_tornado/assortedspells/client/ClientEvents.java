package com.red_x_tornado.assortedspells.client;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.client.model.WandLoader;
import com.red_x_tornado.assortedspells.item.WandItem;
import com.red_x_tornado.assortedspells.network.ASNetworkManager;
import com.red_x_tornado.assortedspells.network.msg.SpellSelectionMessage;
import com.red_x_tornado.assortedspells.util.SpellInstance;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class ClientEvents {

	@EventBusSubscriber(modid = BookOfAssortedSpells.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.FORGE)
	public static class Forge {
		public static int wrap(int index, int dir, SpellInstance[] quick) {
			int newIndex = index;

			do {
				newIndex = newIndex + dir;
				if (newIndex < 0) newIndex = quick.length - 1;
				else if (newIndex >= quick.length) newIndex = 0;

				if (quick[newIndex] == null)
					continue;
				else break;
			} while (newIndex != index);

			return newIndex;
		}

		@SubscribeEvent
		public static void onScroll(InputEvent.MouseScrollEvent event) {
			final PlayerEntity player = Minecraft.getInstance().player;
			if (!player.isCrouching()) return;

			final ItemStack stack = player.getHeldItemMainhand();

			if (stack.getItem() instanceof WandItem) {
				final SpellCapability caps = SpellCapability.get(player);

				final int direction = event.getScrollDelta() < 0 ? -1 : 1;
				int startIndex = caps.quickSpellIndexFromSelection();
				// We need to fix this in case we don't have a quick spell selected.
				if (startIndex == -1)
					startIndex = direction == -1 ? 1 : caps.getQuickSpells().length - 1;

				final int index = wrap(startIndex, direction, caps.getQuickSpells());
				final SpellInstance sel = caps.getQuickSpells()[index];
				if (sel == null) { // Quick spell list might be empty.
					player.sendStatusMessage(new StringTextComponent("You don't have any quick spells."), true);
					event.setCanceled(true);
					return;
				}

				player.sendStatusMessage(new TranslationTextComponent(sel.getSpell().getLangKey()), true);
				caps.select(sel.getSpell());
				ASNetworkManager.get().sendToServer(new SpellSelectionMessage(sel.getSpell().getId()));

				event.setCanceled(true);
			}
		}
	}

	@EventBusSubscriber(modid = BookOfAssortedSpells.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
	public static class Mod {
		@SubscribeEvent
		public static void registerLoaders(ModelRegistryEvent event) {
			ModelLoaderRegistry.registerLoader(new ResourceLocation(BookOfAssortedSpells.MOD_ID, "wand"), WandLoader.INSTANCE);
		}
	}

	public static void handleSync(SpellCapability newCaps) {
		final SpellCapability caps = SpellCapability.get(Minecraft.getInstance().player);
		caps.copyFrom(newCaps);
	}
}