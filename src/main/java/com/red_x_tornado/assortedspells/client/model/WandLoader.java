package com.red_x_tornado.assortedspells.client.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public enum WandLoader implements IModelLoader<WandModel> {

	INSTANCE;

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		WandModel.WAND_MODEL_CACHE.clear();
	}

	@Override
	public WandModel read(JsonDeserializationContext ctx, JsonObject modelContents) {
		final String modelLoc = modelContents.get("model").getAsString();
		final BlockModel model;

		try (IResource res = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(modelLoc));
				InputStreamReader isr = new InputStreamReader(res.getInputStream())) {
			model = ModelLoaderRegistry.ExpandedBlockModelDeserializer.INSTANCE.fromJson(isr, BlockModel.class);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		return new WandModel(model);
	}
}