package com.red_x_tornado.assortedspells.init;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.tileentity.WandBuilderTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ASTileEntities {

	public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BookOfAssortedSpells.MOD_ID);

	public static final RegistryObject<TileEntityType<? extends WandBuilderTileEntity>> WAND_BUILDER = REGISTRY.register("wand_builder", () ->
			TileEntityType.Builder.create(WandBuilderTileEntity::new, ASBlocks.WAND_BUILDER.get()).build(null));
}