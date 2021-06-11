package com.red_x_tornado.assortedspells;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.capability.SpellCapabilityProvider;
import com.red_x_tornado.assortedspells.capability.SpellCapabilityStorage;
import com.red_x_tornado.assortedspells.command.ASCommand;
import com.red_x_tornado.assortedspells.init.ASBlocks;
import com.red_x_tornado.assortedspells.init.ASContainers;
import com.red_x_tornado.assortedspells.init.ASEntities;
import com.red_x_tornado.assortedspells.init.ASItems;
import com.red_x_tornado.assortedspells.init.ASTileEntities;
import com.red_x_tornado.assortedspells.network.ASNetworkManager;
import com.red_x_tornado.assortedspells.network.msg.SpellSyncMessage;
import com.red_x_tornado.assortedspells.util.WandRecipeManager;
import com.red_x_tornado.assortedspells.util.spell.Spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Woah, a main mod class! I haven't seen one of these in minutes!
 */
@Mod(BookOfAssortedSpells.MOD_ID)
public class BookOfAssortedSpells {

	public static final String MOD_ID = "assortedspells";

	public static final Logger LOGGER = LogManager.getLogger("BookOfAssortedSpells");

	public static final ItemGroup TAB = new ItemGroup(MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ASItems.WAND.get());
		}
	};

	public BookOfAssortedSpells() {
		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ASBlocks.REGISTRY.register(modBus);
		ASEntities.REGISTRY.register(modBus);
		ASItems.REGISTRY.register(modBus);
		ASTileEntities.REGISTRY.register(modBus);
		ASContainers.REGISTRY.register(modBus);

		ASBlocks.registerItemBlocks();

		modBus.addListener(this::onCommonSetup);

		Spells.FREEZE.getClazz(); // Classload Spells.
	}

	void onCommonSetup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(SpellCapability.class, new SpellCapabilityStorage(),
				() -> {
					throw new UnsupportedOperationException("Color capability requires a player!");
				});

		ASNetworkManager.init();
		WandRecipeManager.registerBuiltins();
	}

	@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
	static class ForgeEvents {

		@SubscribeEvent
		static void registerCommands(RegisterCommandsEvent event) {
			event.getDispatcher().register(ASCommand.create());
		}

		@SubscribeEvent
		static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof PlayerEntity) {
				final SpellCapabilityProvider provider = new SpellCapabilityProvider((PlayerEntity) event.getObject());
				event.addListener(provider);
				event.addCapability(SpellCapabilityProvider.ID, provider);
			}
		}

		@SubscribeEvent
		static void onPlayerClone(PlayerEvent.Clone event) {
			final SpellCapability old = SpellCapability.get(event.getOriginal());
			final SpellCapability newCap = SpellCapability.get(event.getPlayer());

			newCap.copyFrom(old);
		}

		@SubscribeEvent
		static void onPlayerTick(TickEvent.PlayerTickEvent event) {
			if (event.phase != TickEvent.Phase.START) return;

			final SpellCapability caps = SpellCapability.get(event.player);
			caps.tick();
		}

		@SubscribeEvent
		static void onPlayerJoin(PlayerLoggedInEvent event) {
			SpellSyncMessage.sync(event.getPlayer());
		}

		@SubscribeEvent
		static void onPlayerRespawn(PlayerRespawnEvent event) {
			SpellSyncMessage.sync(event.getPlayer());
		}

		@SubscribeEvent
		static void onPlayerChangeDimension(PlayerChangedDimensionEvent event) {
			SpellSyncMessage.sync(event.getPlayer());
		}
	}
}