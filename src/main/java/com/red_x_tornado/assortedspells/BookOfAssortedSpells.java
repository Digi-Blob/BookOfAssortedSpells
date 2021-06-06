package com.red_x_tornado.assortedspells;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.red_x_tornado.assortedspells.init.ASBlocks;
import com.red_x_tornado.assortedspells.init.ASEntities;
import com.red_x_tornado.assortedspells.init.ASItems;
import com.red_x_tornado.assortedspells.init.ASTileEntities;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
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
	}

	@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
	static class ForgeEvents {

		static void attachCapabilities() {
			
		}
	}
}