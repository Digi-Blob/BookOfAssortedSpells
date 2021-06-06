package com.red_x_tornado.assortedspells.init;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;

import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ASEntities {

	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, BookOfAssortedSpells.MOD_ID);
}