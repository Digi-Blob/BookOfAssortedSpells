package com.red_x_tornado.assortedspells.util;

import static com.red_x_tornado.assortedspells.util.ResourceLocations.as;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.init.ASItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class WandRecipeManager {

	private static final Map<ResourceLocation,IWandMaterialRecipe> CAP_RECIPES = new HashMap<>();
	private static final Map<ResourceLocation,IWandMaterialRecipe> CORE_RECIPES = new HashMap<>();
	private static final Map<ResourceLocation,IWandMaterialRecipe> ROD_RECIPES = new HashMap<>();

	public static void registerBuiltins() {
		registerCapRecipe(new SimpleWandMaterialRecipe(as("gold_cap"), Items.GOLD_INGOT, new ItemStack(ASItems.GOLD_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("iron_cap"), Items.IRON_INGOT, new ItemStack(ASItems.IRON_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("stone_cap"), Items.STONE, new ItemStack(ASItems.STONE_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("brown_mushroom_cap"), Items.BROWN_MUSHROOM_BLOCK, new ItemStack(ASItems.BROWN_MUSHROOM_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("nether_brick_cap"), Items.NETHER_BRICKS, new ItemStack(ASItems.NETHER_BRICK_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("netherite_cap"), Items.NETHERITE_INGOT, new ItemStack(ASItems.NETHERITE_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("packed_ice_cap"), Items.PACKED_ICE, new ItemStack(ASItems.PACKED_ICE_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("paper_cap"), Items.PAPER, new ItemStack(ASItems.PAPER_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("prismarine_cap"), Items.PRISMARINE, new ItemStack(ASItems.PRISMARINE_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("red_mushroom_cap"), Items.RED_MUSHROOM_BLOCK, new ItemStack(ASItems.RED_MUSHROOM_WAND_CAP.get())));

		registerCoreRecipe(new SimpleWandMaterialRecipe(as("magma_core"), Items.MAGMA_BLOCK, new ItemStack(ASItems.MAGMA_WAND_CORE.get())));
		registerCoreRecipe(new SimpleWandMaterialRecipe(as("coal_core"), Items.COAL, new ItemStack(ASItems.COAL_WAND_CORE.get())));
		registerCoreRecipe(new SimpleWandMaterialRecipe(as("coal_core_charcoal"), Items.CHARCOAL, new ItemStack(ASItems.COAL_WAND_CORE.get())));

		registerRodRecipe(new SimpleWandMaterialRecipe(as("oak_rod"), Items.OAK_LOG, new ItemStack(ASItems.OAK_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("bamboo_rod"), Items.BAMBOO, new ItemStack(ASItems.BAMBOO_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("acacia_rod"), Items.STRIPPED_ACACIA_LOG, new ItemStack(ASItems.ACACIA_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("basalt_rod"), Items.BASALT, new ItemStack(ASItems.BASALT_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("birch_rod"), Items.STRIPPED_BIRCH_LOG, new ItemStack(ASItems.BIRCH_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("blaze_rod"), Items.BLAZE_ROD, new ItemStack(ASItems.BLAZE_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("bone_rod"), Items.BONE, new ItemStack(ASItems.BONE_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("crimson_rod"), Items.STRIPPED_CRIMSON_STEM, new ItemStack(ASItems.CRIMSON_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("dark_oak_rod"), Items.STRIPPED_DARK_OAK_LOG, new ItemStack(ASItems.DARK_OAK_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("end_stone_rod"), Items.END_STONE, new ItemStack(ASItems.END_STONE_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("jungle_rod"), Items.STRIPPED_JUNGLE_LOG, new ItemStack(ASItems.JUNGLE_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("obsidian_rod"), Items.OBSIDIAN, new ItemStack(ASItems.OBSIDIAN_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("quartz_rod"), Items.QUARTZ, new ItemStack(ASItems.QUARTZ_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("sandstone_rod"), Items.SANDSTONE, new ItemStack(ASItems.SANDSTONE_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("spruce_rod"), Items.STRIPPED_SPRUCE_LOG, new ItemStack(ASItems.SPRUCE_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("warped_rod"), Items.STRIPPED_WARPED_STEM, new ItemStack(ASItems.WARPED_WAND_ROD.get())));
	}

	public static void registerCapRecipe(IWandMaterialRecipe recipe) {
		CAP_RECIPES.put(recipe.getId(), recipe);
	}

	public static void registerCoreRecipe(IWandMaterialRecipe recipe) {
		CORE_RECIPES.put(recipe.getId(), recipe);
	}

	public static void registerRodRecipe(IWandMaterialRecipe recipe) {
		ROD_RECIPES.put(recipe.getId(), recipe);
	}

	public static IWandMaterialRecipe getCapRecipe(ResourceLocation id) {
		return CAP_RECIPES.get(id);
	}

	public static IWandMaterialRecipe getCoreRecipe(ResourceLocation id) {
		return CORE_RECIPES.get(id);
	}

	public static IWandMaterialRecipe getRodRecipe(ResourceLocation id) {
		return ROD_RECIPES.get(id);
	}

	public static Collection<IWandMaterialRecipe> getCapRecipes() {
		return Collections.unmodifiableCollection(CAP_RECIPES.values());
	}

	public static Collection<IWandMaterialRecipe> getCoreRecipes() {
		return Collections.unmodifiableCollection(CORE_RECIPES.values());
	}

	public static Collection<IWandMaterialRecipe> getRodRecipes() {
		return Collections.unmodifiableCollection(ROD_RECIPES.values());
	}

	public static interface IWandMaterialRecipe {
		public boolean isApplicable(ItemStack stack);
		public ItemStack craft(ItemStack stack);
		public ResourceLocation getId();
	}

	public static class SimpleWandMaterialRecipe implements IWandMaterialRecipe {

		private final ResourceLocation id;

		private final Ingredient input;
		private final int inputCount;
		private final ItemStack output;

		public SimpleWandMaterialRecipe(ResourceLocation id, Ingredient input, int inputCount, ItemStack output) {
			this.id = id;
			this.input = input;
			this.inputCount = inputCount;
			this.output = output;
		}

		public SimpleWandMaterialRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
			this(id, input, 1, output);
		}

		public SimpleWandMaterialRecipe(ResourceLocation id, Item input, ItemStack output) {
			this(id, Ingredient.fromItems(input), output);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public boolean isApplicable(ItemStack stack) {
			return input.test(stack) && inputCount <= stack.getCount();
		}

		@Override
		public ItemStack craft(ItemStack stack) {
			return output.copy();
		}

		@Override
		public String toString() {
			return getId().toString() + ": " + inputCount + " * " + input + " -> " + output;
		}
	}

	public static class CachingRecipeMatcher {

		private IWandMaterialRecipe cache;
		private Collection<IWandMaterialRecipe> view;

		public CachingRecipeMatcher(Collection<IWandMaterialRecipe> view) {
			this.view = view;
		}

		@Nullable
		public IWandMaterialRecipe findMatching(ItemStack stack) {
			if (cache != null && cache.isApplicable(stack))
				return cache;

			for (IWandMaterialRecipe recipe : view)
				if (recipe.isApplicable(stack))
					return cache = recipe;

			return null;
		}
	}
}