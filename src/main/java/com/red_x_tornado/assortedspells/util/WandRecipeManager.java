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
import net.minecraft.util.ResourceLocation;

public class WandRecipeManager {

	private static final Map<ResourceLocation,IWandMaterialRecipe> CAP_RECIPES = new HashMap<>();
	private static final Map<ResourceLocation,IWandMaterialRecipe> CORE_RECIPES = new HashMap<>();
	private static final Map<ResourceLocation,IWandMaterialRecipe> ROD_RECIPES = new HashMap<>();

	public static void registerBuiltins() {
		registerCapRecipe(new SimpleWandMaterialRecipe(as("gold_cap"), Items.GOLD_INGOT, new ItemStack(ASItems.GOLD_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("iron_cap"), Items.IRON_INGOT, new ItemStack(ASItems.IRON_WAND_CAP.get())));
		registerCapRecipe(new SimpleWandMaterialRecipe(as("stone_cap"), Items.STONE, new ItemStack(ASItems.STONE_WAND_CAP.get())));

		registerCoreRecipe(new SimpleWandMaterialRecipe(as("magma_core"), Items.MAGMA_BLOCK, new ItemStack(ASItems.MAGMA_WAND_CORE.get())));
		registerCoreRecipe(new SimpleWandMaterialRecipe(as("coal_core"), Items.COAL, new ItemStack(ASItems.COAL_WAND_CORE.get())));
		registerCoreRecipe(new SimpleWandMaterialRecipe(as("coal_core_charcoal"), Items.CHARCOAL, new ItemStack(ASItems.COAL_WAND_CORE.get())));

		registerRodRecipe(new SimpleWandMaterialRecipe(as("oak_cap"), Items.OAK_LOG, new ItemStack(ASItems.OAK_WAND_ROD.get())));
		registerRodRecipe(new SimpleWandMaterialRecipe(as("bamboo_cap"), Items.BAMBOO, new ItemStack(ASItems.BAMBOO_WAND_ROD.get())));
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

		private final Item input;
		private final int inputCount;
		private final ItemStack output;

		public SimpleWandMaterialRecipe(ResourceLocation id, Item input, int inputCount, ItemStack output) {
			this.id = id;
			this.input = input;
			this.inputCount = inputCount;
			this.output = output;
		}

		public SimpleWandMaterialRecipe(ResourceLocation id, Item input, ItemStack output) {
			this(id, input, 1, output);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public boolean isApplicable(ItemStack stack) {
			return input == stack.getItem() && inputCount <= stack.getCount();
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