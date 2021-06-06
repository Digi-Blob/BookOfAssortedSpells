package com.red_x_tornado.assortedspells.init;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ASTileEntities {

	public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BookOfAssortedSpells.MOD_ID);
}