package com.red_x_tornado.assortedspells.client.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.red_x_tornado.assortedspells.item.WandItem;
import com.red_x_tornado.assortedspells.util.WandMaterialManager.WandCap;
import com.red_x_tornado.assortedspells.util.WandMaterialManager.WandRod;

import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class WandModel extends BlockModel implements IModelGeometry<WandModel> {

	static final Map<WandModelKey,IBakedModel> WAND_MODEL_CACHE = new HashMap<>();

	@SuppressWarnings("deprecation")
	public WandModel(BlockModel model) {
		super(model.getParentLocation(), model.getElements(), model.textures, model.isAmbientOcclusion(), model.getGuiLight(), model.getAllTransforms(), Collections.emptyList());
	}

	@Override
	public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
		return bakeModel(bakery, this, spriteGetter, modelTransform, modelLocation, false);
	}

	@Override
	public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		return getTextures(modelGetter, missingTextureErrors);
	}

	@Override
	public ItemOverrideList getItemOverrideList(ModelBakery modelBakeryIn, BlockModel modelIn) {
		return getOverrideList();
	}

	@Override
	public ItemOverrideList getOverrides(ModelBakery modelBakeryIn, BlockModel modelIn, Function<RenderMaterial, TextureAtlasSprite> textureGetter) {
		return getOverrideList();
	}

	protected ItemOverrideList getOverrideList() {
		return new ItemOverrideList() {
			@SuppressWarnings("deprecation")
			@Override
			public IBakedModel getOverrideModel(IBakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity) {
				final WandCap topCap = WandItem.getTopCap(stack);
				final WandRod rod = WandItem.getRod(stack);
				final WandCap bottomCap = WandItem.getBottomCap(stack);

				final WandModelKey key = new WandModelKey(
						topCap.getTexture(),
						rod.getTexture(),
						bottomCap.getTexture());

				return WAND_MODEL_CACHE.computeIfAbsent(key, k -> {
					textures.put("cap1", Either.left(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, topCap.getTexture())));
					textures.put("rod", Either.left(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, rod.getTexture())));
					textures.put("cap2", Either.left(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, bottomCap.getTexture())));

					final IBakedModel bakedModel = bakeModel(null, WandModel.this,
							RenderMaterial::getSprite, ModelRotation.X0_Y0, parentLocation, false);
					return bakedModel;
				});
			}
		};
	}
}