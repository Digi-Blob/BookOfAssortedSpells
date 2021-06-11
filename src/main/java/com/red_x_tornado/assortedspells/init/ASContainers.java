package com.red_x_tornado.assortedspells.init;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.tileentity.container.WandBuilderContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ASContainers {

	public static final DeferredRegister<ContainerType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, BookOfAssortedSpells.MOD_ID);

	public static final RegistryObject<ContainerType<? extends WandBuilderContainer>> WAND_BUILDER = REGISTRY.register("wand_builder",
			() -> IForgeContainerType.create((windowId,playerInv,extraData) -> new WandBuilderContainer(windowId, playerInv)));
}