package com.red_x_tornado.assortedspells.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;

import net.minecraft.util.ResourceLocation;

public class WandMaterialManager {

	private static final Map<ResourceLocation,WandCap> CAPS = new HashMap<>();
	private static final Map<ResourceLocation,WandRod> RODS = new HashMap<>();
	private static final Map<ResourceLocation,WandCore> CORES = new HashMap<>();

	static {
		register(new WandCap(mc("block/iron_block"), builtin("iron")));
		register(new WandCap(mc("block/gold_block"), builtin("gold")));
		register(new WandRod(mc("block/oak_log"), builtin("oak")));
		register(new WandRod(mc("block/diamond_block"), builtin("diamond")));
		register(new WandCore(mc("block/oak_planks"), builtin("oak_planks")));
	}

	private static ResourceLocation mc(String path) {
		return new ResourceLocation("minecraft", path);
	}

	private static ResourceLocation builtin(String path) {
		return new ResourceLocation(BookOfAssortedSpells.MOD_ID, path);
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

		private final ResourceLocation texture;
		private final ResourceLocation id;

		private final String langKey;

		protected WandMaterial(ResourceLocation texture, ResourceLocation id, String type) {
			this.texture = texture;
			this.id = id;
			langKey = id.getNamespace() + "." + type + "." + id.getPath();
		}

		public ResourceLocation getTexture() {
			return texture;
		}

		public ResourceLocation getId() {
			return id;
		}

		public String getLangKey() {
			return langKey;
		}
	}

	public static class WandCap extends WandMaterial {

		public WandCap(ResourceLocation texture, ResourceLocation id) {
			super(texture, id, "wand_cap");
		}
	}

	public static class WandRod extends WandMaterial {

		public WandRod(ResourceLocation texture, ResourceLocation id) {
			super(texture, id, "wand_rod");
		}
	}

	public static class WandCore extends WandMaterial {

		public WandCore(ResourceLocation texture, ResourceLocation id) {
			super(texture, id, "wand_core");
		}
	}
}