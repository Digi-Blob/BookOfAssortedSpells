package com.red_x_tornado.assortedspells.client;

import java.util.Arrays;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.client.gui.ResearchTableScreen;
import com.red_x_tornado.assortedspells.client.gui.WandBuilderScreen;
import com.red_x_tornado.assortedspells.client.model.WandLoader;
import com.red_x_tornado.assortedspells.init.ASBlocks;
import com.red_x_tornado.assortedspells.init.ASContainers;
import com.red_x_tornado.assortedspells.item.WandItem;
import com.red_x_tornado.assortedspells.network.ASNetworkManager;
import com.red_x_tornado.assortedspells.network.msg.SpellSelectionMessage;
import com.red_x_tornado.assortedspells.tileentity.container.ResearchTableContainer;
import com.red_x_tornado.assortedspells.util.research.MatchType;
import com.red_x_tornado.assortedspells.util.research.ResearchAttempt;
import com.red_x_tornado.assortedspells.util.research.ResearchInstance;
import com.red_x_tornado.assortedspells.util.spell.SpellInstance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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
				int startIndex = caps.findSpellInQuickSpells(caps.getSelected().getSpell());
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
		public static void onClientSetup(FMLClientSetupEvent event) {
			ScreenManager.registerFactory(ASContainers.WAND_BUILDER.get(), WandBuilderScreen::new);
			ScreenManager.registerFactory(ASContainers.RESEARCH_TABLE.get(), ResearchTableScreen::new);
			RenderTypeLookup.setRenderLayer(ASBlocks.FANCY_TORCH.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ASBlocks.FANCY_WALL_TORCH.get(), RenderType.getCutout());
		}
		@SubscribeEvent
		public static void registerLoaders(ModelRegistryEvent event) {
			ModelLoaderRegistry.registerLoader(new ResourceLocation(BookOfAssortedSpells.MOD_ID, "wand"), WandLoader.INSTANCE);
		}
		@SubscribeEvent
		public static void addSprites(TextureStitchEvent.Pre event) {
			if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE))
				event.addSprite(ResearchTableContainer.WAND_SLOT);
		}
	}

	public static void handleSync(SpellCapability newCaps) {
		final SpellCapability caps = SpellCapability.get(Minecraft.getInstance().player);
		caps.copyFrom(newCaps);
		refreshResearch(caps);
	}

	public static void refreshResearchAttempts() {
		if (Minecraft.getInstance().currentScreen instanceof ResearchTableScreen) {
			final ResearchTableScreen screen = (ResearchTableScreen) Minecraft.getInstance().currentScreen;
			screen.refreshResearchAttempts();
		}
	}

	public static void refreshResearch(SpellCapability caps) {
		if (Minecraft.getInstance().currentScreen instanceof ResearchTableScreen) {
			final ResearchTableScreen screen = (ResearchTableScreen) Minecraft.getInstance().currentScreen;
			screen.populateResearch();
			if (screen.getContainer().selected != null) {
				final ResearchInstance res = caps.getResearch().get(screen.getContainer().selected.getResearch().getSpell());
				if (res != null) {
					screen.getContainer().selected = res;
				} else {
					screen.getContainer().selected.getAttempts().add(new ResearchAttempt(
							Arrays.stream(screen.getContainer().getRuneSpellTypes())
							.map(r -> new ResearchAttempt.RunePair(r, MatchType.EXACT))
							.toArray(ResearchAttempt.RunePair[]::new)));
				}
				screen.refreshResearchAttempts();
			}
		}
	}
}