package com.red_x_tornado.assortedspells.util;

import static com.red_x_tornado.assortedspells.util.ResourceLocations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.init.ASItems;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class WandMaterialManager {

	private static final Map<ResourceLocation,WandCap> CAPS = new HashMap<>();
	private static final Map<ResourceLocation,WandRod> RODS = new HashMap<>();
	private static final Map<ResourceLocation,WandCore> CORES = new HashMap<>();

	public static final WandCap IRON_CAP = register(new WandCap(mc("block/iron_block"), as("iron"), ASItems.IRON_WAND_CAP));
	public static final WandCap STONE_CAP = register(new WandCap(mc("block/stone"), as("stone"), ASItems.STONE_WAND_CAP));
	public static final WandCap GOLD_CAP = register(new WandCap(mc("block/gold_block"), as("gold"), ASItems.GOLD_WAND_CAP));
	public static final WandCap BROWN_MUSHROOM_CAP = register(new WandCap(mc("block/brown_mushroom_block"), as("brown_mushroom"), ASItems.BROWN_MUSHROOM_WAND_CAP));
	public static final WandCap NETHER_BRICK_CAP = register(new WandCap(mc("block/nether_bricks"), as("nether_brick"), ASItems.NETHER_BRICK_WAND_CAP));
	public static final WandCap NETHERITE_CAP = register(new WandCap(mc("block/netherite_block"), as("netherite"), ASItems.NETHERITE_WAND_CAP));
	public static final WandCap PACKED_ICE_CAP = register(new WandCap(mc("block/packed_ice"), as("packed_ice"), ASItems.PACKED_ICE_WAND_CAP));
	public static final WandCap PAPER_CAP = register(new WandCap(as("block/paper"), as("paper"), ASItems.PAPER_WAND_CAP));
	public static final WandCap PRISMARINE_CAP = register(new WandCap(mc("block/prismarine_bricks"), as("prismarine"), ASItems.PRISMARINE_WAND_CAP));
	public static final WandCap RED_MUSHROOM_CAP = register(new WandCap(mc("block/red_mushroom_block"), as("red_mushroom"), ASItems.RED_MUSHROOM_WAND_CAP));

	public static final WandRod OAK_ROD = register(new WandRod(mc("block/oak_log"), as("oak"), ASItems.OAK_WAND_ROD));
	public static final WandRod BAMBOO_ROD = register(new WandRod(mc("block/bamboo_stalk"), as("bamboo"), ASItems.BAMBOO_WAND_ROD));
	public static final WandRod ACACIA_ROD = register(new WandRod(mc("block/stripped_acacia_log"), as("acacia"), ASItems.ACACIA_WAND_ROD));
	public static final WandRod BASALT_ROD = register(new WandRod(mc("block/basalt_side"), as("basalt"), ASItems.BASALT_WAND_ROD));
	public static final WandRod BIRCH_ROD = register(new WandRod(mc("block/stripped_birch_log"), as("birch"), ASItems.BIRCH_WAND_ROD));
	public static final WandRod BLAZE_ROD = register(new WandRod(as("block/blaze"), as("blaze"), ASItems.BLAZE_WAND_ROD));
	public static final WandRod BONE_ROD = register(new WandRod(mc("block/bone_block_side"), as("bone"), ASItems.BONE_WAND_ROD));
	public static final WandRod CRIMSON_ROD = register(new WandRod(mc("block/stripped_crimson_stem"), as("crimson"), ASItems.CRIMSON_WAND_ROD));
	public static final WandRod DARK_OAK_ROD = register(new WandRod(mc("block/stripped_dark_oak_log"), as("dark_oak"), ASItems.DARK_OAK_WAND_ROD));
	public static final WandRod END_STONE_ROD = register(new WandRod(mc("block/end_stone"), as("end_stone"), ASItems.END_STONE_WAND_ROD));
	public static final WandRod JUNGLE_ROD = register(new WandRod(mc("block/stripped_jungle_log"), as("jungle"), ASItems.JUNGLE_WAND_ROD));
	public static final WandRod OBSIDIAN_ROD = register(new WandRod(mc("block/obsidian"), as("obsidian"), ASItems.OBSIDIAN_WAND_ROD));
	public static final WandRod QUARTZ_ROD = register(new WandRod(mc("block/quartz_block_bottom"), as("quartz"), ASItems.QUARTZ_WAND_ROD));
	public static final WandRod SANDSTONE_ROD = register(new WandRod(mc("block/sandstone_top"), as("sandstone"), ASItems.SANDSTONE_WAND_ROD));
	public static final WandRod SPRUCE_ROD = register(new WandRod(mc("block/stripped_spruce_log"), as("spruce"), ASItems.SPRUCE_WAND_ROD));
	public static final WandRod WARPED_ROD = register(new WandRod(mc("block/stripped_warped_stem"), as("warped"), ASItems.WARPED_WAND_ROD));

	public static final WandCore MAGMA_CORE = register(new WandCore(as("magma"), ASItems.MAGMA_WAND_CORE));
	public static final WandCore COAL_CORE = register(new WandCore(as("coal"), ASItems.COAL_WAND_CORE));

	public static WandCap register(WandCap cap) {
		CAPS.put(cap.getId(), cap);
		return cap;
	}

	public static WandRod register(WandRod rod) {
		RODS.put(rod.getId(), rod);
		return rod;
	}

	public static WandCore register(WandCore core) {
		CORES.put(core.getId(), core);
		return core;
	}

	@Nullable
	public static WandCap getCap(ResourceLocation id) {
		return CAPS.get(id);
	}

	@Nullable
	public static WandRod getRod(ResourceLocation id) {
		return RODS.get(id);
	}

	@Nullable
	public static WandCore getCore(ResourceLocation id) {
		return CORES.get(id);
	}

	public static class WandMaterial {

		private final ResourceLocation id;
		private final Supplier<Item> item;

		private final String langKey;

		protected WandMaterial(ResourceLocation id, Supplier<Item> item, String type) {
			this.id = id;
			this.item = item;
			langKey = id.getNamespace() + "." + type + "." + id.getPath();
		}

		public ResourceLocation getId() {
			return id;
		}

		public Item getItem() {
			return item.get();
		}

		public String getLangKey() {
			return langKey;
		}
	}

	public static class TexturedWandMaterial extends WandMaterial {

		private final ResourceLocation texture;

		protected TexturedWandMaterial(ResourceLocation texture, ResourceLocation id, Supplier<Item> item, String type) {
			super(id, item, type);
			this.texture = texture;
		}

		public ResourceLocation getTexture() {
			return texture;
		}
	}

	public static class WandCap extends TexturedWandMaterial {

		public WandCap(ResourceLocation texture, ResourceLocation id, Supplier<Item> item) {
			super(texture, id, item, "wand_cap");
		}
	}

	public static class WandRod extends TexturedWandMaterial {

		public WandRod(ResourceLocation texture, ResourceLocation id, Supplier<Item> item) {
			super(texture, id, item, "wand_rod");
		}
	}

	public static class WandCore extends WandMaterial {

		public WandCore(ResourceLocation id, Supplier<Item> item) {
			super(id, item, "wand_core");
		}
	}
}