package com.red_x_tornado.assortedspells.init;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.item.RuneItem;
import com.red_x_tornado.assortedspells.item.TomeItem;
import com.red_x_tornado.assortedspells.item.WandCapItem;
import com.red_x_tornado.assortedspells.item.WandCoreItem;
import com.red_x_tornado.assortedspells.item.WandItem;
import com.red_x_tornado.assortedspells.item.WandRodItem;
import com.red_x_tornado.assortedspells.util.WandMaterialManager;
import com.red_x_tornado.assortedspells.util.spell.SpellType;

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
	public static final RegistryObject<Item> ACACIA_WAND_ROD = REGISTRY.register("acacia_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.ACACIA_ROD));
	public static final RegistryObject<Item> BASALT_WAND_ROD = REGISTRY.register("basalt_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.BASALT_ROD));
	public static final RegistryObject<Item> BIRCH_WAND_ROD = REGISTRY.register("birch_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.BIRCH_ROD));
	public static final RegistryObject<Item> BLAZE_WAND_ROD = REGISTRY.register("blaze_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.BLAZE_ROD));
	public static final RegistryObject<Item> BONE_WAND_ROD = REGISTRY.register("bone_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.BONE_ROD));
	public static final RegistryObject<Item> CRIMSON_WAND_ROD = REGISTRY.register("crimson_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.CRIMSON_ROD));
	public static final RegistryObject<Item> DARK_OAK_WAND_ROD = REGISTRY.register("dark_oak_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.DARK_OAK_ROD));
	public static final RegistryObject<Item> END_STONE_WAND_ROD = REGISTRY.register("end_stone_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.END_STONE_ROD));
	public static final RegistryObject<Item> JUNGLE_WAND_ROD = REGISTRY.register("jungle_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.JUNGLE_ROD));
	public static final RegistryObject<Item> OBSIDIAN_WAND_ROD = REGISTRY.register("obsidian_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.OBSIDIAN_ROD));
	public static final RegistryObject<Item> QUARTZ_WAND_ROD = REGISTRY.register("quartz_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.QUARTZ_ROD));
	public static final RegistryObject<Item> SANDSTONE_WAND_ROD = REGISTRY.register("sandstone_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.SANDSTONE_ROD));
	public static final RegistryObject<Item> SPRUCE_WAND_ROD = REGISTRY.register("spruce_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.SPRUCE_ROD));
	public static final RegistryObject<Item> WARPED_WAND_ROD = REGISTRY.register("warped_wand_rod", () -> new WandRodItem(props(), () -> WandMaterialManager.WARPED_ROD));

	public static final RegistryObject<Item> MAGMA_WAND_CORE = REGISTRY.register("magma_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.MAGMA_CORE));
	public static final RegistryObject<Item> COAL_WAND_CORE = REGISTRY.register("coal_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.COAL_CORE));
	public static final RegistryObject<Item> BLAZE_WAND_CORE = REGISTRY.register("blaze_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.BLAZE_CORE));
	public static final RegistryObject<Item> BLUE_ICE_WAND_CORE = REGISTRY.register("blue_ice_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.BLUE_ICE_CORE));
	public static final RegistryObject<Item> CHORUS_FRUIT_WAND_CORE = REGISTRY.register("chorus_fruit_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.CHORUS_FRUIT_CORE));
	public static final RegistryObject<Item> CRYING_OBSIDIAN_WAND_CORE = REGISTRY.register("crying_obsidian_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.CRYING_OBSIDIAN_CORE));
	public static final RegistryObject<Item> DIAMOND_WAND_CORE = REGISTRY.register("diamond_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.DIAMOND_CORE));
	public static final RegistryObject<Item> EYE_OF_ENDER_WAND_CORE = REGISTRY.register("eye_of_ender_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.EYE_OF_ENDER_CORE));
	public static final RegistryObject<Item> GLOWSTONE_WAND_CORE = REGISTRY.register("glowstone_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.GLOWSTONE_CORE));
	public static final RegistryObject<Item> HEART_OF_THE_SEA_WAND_CORE = REGISTRY.register("heart_of_the_sea_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.HEART_OF_THE_SEA_CORE));
	public static final RegistryObject<Item> HONEY_WAND_CORE = REGISTRY.register("honey_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.HONEY_CORE));
	public static final RegistryObject<Item> NAUTILUS_SHELL_WAND_CORE = REGISTRY.register("nautilus_shell_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.NAUTILUS_SHELL_CORE));
	public static final RegistryObject<Item> NETHER_STAR_WAND_CORE = REGISTRY.register("nether_star_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.NETHER_STAR_CORE));
	public static final RegistryObject<Item> PRISMARINE_WAND_CORE = REGISTRY.register("prismarine_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.PRISMARINE_CORE));
	public static final RegistryObject<Item> REDSTONE_WAND_CORE = REGISTRY.register("redstone_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.REDSTONE_CORE));
	public static final RegistryObject<Item> SEA_PICKLE_WAND_CORE = REGISTRY.register("sea_pickle_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.SEA_PICKLE_CORE));
	public static final RegistryObject<Item> WHEAT_WAND_CORE = REGISTRY.register("wheat_wand_core", () -> new WandCoreItem(props(), () -> WandMaterialManager.WHEAT_CORE));

	public static final RegistryObject<Item> AIR_RUNE = REGISTRY.register("air_rune", () -> new RuneItem(props().maxStackSize(16), SpellType.AIR));
	public static final RegistryObject<Item> ARCANE_RUNE = REGISTRY.register("arcane_rune", () -> new RuneItem(props().maxStackSize(16), SpellType.ARCANE));
	public static final RegistryObject<Item> COSMIC_RUNE = REGISTRY.register("cosmic_rune", () -> new RuneItem(props().maxStackSize(16), SpellType.COSMIC));
	public static final RegistryObject<Item> DARK_RUNE = REGISTRY.register("dark_rune", () -> new RuneItem(props().maxStackSize(16), SpellType.DARK));
	public static final RegistryObject<Item> EARTH_RUNE = REGISTRY.register("earth_rune", () -> new RuneItem(props().maxStackSize(16), SpellType.EARTH));
	public static final RegistryObject<Item> FIRE_RUNE = REGISTRY.register("fire_rune", () -> new RuneItem(props().maxStackSize(16), SpellType.FIRE));
	public static final RegistryObject<Item> LIGHT_RUNE = REGISTRY.register("light_rune", () -> new RuneItem(props().maxStackSize(16), SpellType.LIGHT));
	public static final RegistryObject<Item> WATER_RUNE = REGISTRY.register("water_rune", () -> new RuneItem(props().maxStackSize(16), SpellType.WATER));

	public static Item.Properties props() {
		return new Item.Properties().group(BookOfAssortedSpells.TAB);
	}
}