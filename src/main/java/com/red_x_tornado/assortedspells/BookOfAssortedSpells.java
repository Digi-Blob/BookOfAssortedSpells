package com.red_x_tornado.assortedspells;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.capability.SpellCapabilityProvider;
import com.red_x_tornado.assortedspells.capability.SpellCapabilityStorage;
import com.red_x_tornado.assortedspells.init.ASBlocks;
import com.red_x_tornado.assortedspells.init.ASEntities;
import com.red_x_tornado.assortedspells.init.ASItems;
import com.red_x_tornado.assortedspells.init.ASTileEntities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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

		ASBlocks.registerItemBlocks();

		modBus.addListener(this::onCommonSetup);
	}

	void onCommonSetup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(SpellCapability.class, new SpellCapabilityStorage(),
				() -> {
					throw new UnsupportedOperationException("Color capability requires a player!");
				});
	}

	@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
	static class ForgeEvents {

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
	}
}