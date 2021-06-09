package com.red_x_tornado.assortedspells.util;

import static com.red_x_tornado.assortedspells.util.ResourceLocations.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

public class WandMaterialManager {

	private static final Map<ResourceLocation,WandCap> CAPS = new HashMap<>();
	private static final Map<ResourceLocation,WandRod> RODS = new HashMap<>();
	private static final Map<ResourceLocation,WandCore> CORES = new HashMap<>();

	static {
		register(new WandCap(mc("block/iron_block"), as("iron")));
		register(new WandCap(mc("block/gold_block"), as("gold")));
		register(new WandRod(mc("block/oak_log"), as("oak")));
		register(new WandRod(mc("block/diamond_block"), as("diamond")));
		register(new WandCore(as("oak_planks")));
	}

	public static void register(WandCap cap) {
		CAPS.put(cap.getId(), cap);
	}

	public static void register(WandRod rod) {
		RODS.put(rod.getId(), rod);
	}

	public static void register(WandCore core) {
		CORES.put(core.getId(), core);
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

		private final String langKey;

		protected WandMaterial(ResourceLocation id, String type) {
			this.id = id;
			langKey = id.getNamespace() + "." + type + "." + id.getPath();
		}

		public ResourceLocation getId() {
			return id;
		}

		public String getLangKey() {
			return langKey;
		}
	}

	public static class TexturedWandMaterial extends WandMaterial {

		private final ResourceLocation texture;

		protected TexturedWandMaterial(ResourceLocation texture, ResourceLocation id, String type) {
			super(id, type);
			this.texture = texture;
		}

		public ResourceLocation getTexture() {
			return texture;
		}
	}

	public static class WandCap extends TexturedWandMaterial {

		public WandCap(ResourceLocation texture, ResourceLocation id) {
			super(texture, id, "wand_cap");
		}
	}

	public static class WandRod extends TexturedWandMaterial {

		public WandRod(ResourceLocation texture, ResourceLocation id) {
			super(texture, id, "wand_rod");
		}
	}

	public static class WandCore extends WandMaterial {

		public WandCore(ResourceLocation id) {
			super(id, "wand_core");
		}
	}
}