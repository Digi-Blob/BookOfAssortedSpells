package com.red_x_tornado.assortedspells.init;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.item.TomeItem;
import com.red_x_tornado.assortedspells.item.WandCapItem;
import com.red_x_tornado.assortedspells.item.WandCoreItem;
import com.red_x_tornado.assortedspells.item.WandItem;
import com.red_x_tornado.assortedspells.item.WandRodItem;
import com.red_x_tornado.assortedspells.util.WandMaterialManager;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ASItems {

	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BookOfAssortedSpells.MOD_ID);

	public static final RegistryObject<Item> WAND = REGISTRY.register("wand", () -> new WandItem(props().maxStackSize(1)));
	public static final RegistryObject<Item> TOME = REGISTRY.register("tome", () -> new TomeItem(props().maxStackSize(1)));

	public static final RegistryObject<Item> IRON_WAND_CAP = REGISTRY.register("iron_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.IRON_CAP));
	public static final RegistryObject<Item> STONE_WAND_CAP = REGISTRY.register("stone_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.STONE_CAP));
	public static final RegistryObject<Item> GOLD_WAND_CAP = REGISTRY.register("gold_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.GOLD_CAP));
	public static final RegistryObject<Item> BROWN_MUSHROOM_WAND_CAP = REGISTRY.register("brown_mushroom_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.BROWN_MUSHROOM_CAP));
	public static final RegistryObject<Item> NETHER_BRICK_WAND_CAP = REGISTRY.register("nether_brick_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.NETHER_BRICK_CAP));
	public static final RegistryObject<Item> NETHERITE_WAND_CAP = REGISTRY.register("netherite_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.NETHERITE_CAP));
	public static final RegistryObject<Item> PACKED_ICE_WAND_CAP = REGISTRY.register("packed_ice_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.PACKED_ICE_CAP));
	public static final RegistryObject<Item> PAPER_WAND_CAP = REGISTRY.register("paper_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.PAPER_CAP));
	public static final RegistryObject<Item> PRISMARINE_WAND_CAP = REGISTRY.register("prismarine_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.PRISMARINE_CAP));
	public static final RegistryObject<Item> RED_MUSHROOM_WAND_CAP = REGISTRY.register("red_mushroom_wand_cap", () -> new WandCapItem(props(), () -> WandMaterialManager.RED_MUSHROOM_CAP));

	public static final RegistryObject<Item> OAK_WAND_ROD = REGISTRY.register("oak_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.OAK_ROD));
	public static final RegistryObject<Item> BAMBOO_WAND_ROD = REGISTRY.register("bamboo_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.BAMBOO_ROD));

	public static final RegistryObject<Item> MAGMA_WAND_CORE = REGISTRY.register("magma_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.MAGMA_CORE));
	public static final RegistryObject<Item> COAL_WAND_CORE = REGISTRY.register("coal_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.COAL_CORE));

	public static Item.Properties props() {
		return new Item.Properties().group(BookOfAssortedSpells.TAB);
	}
}