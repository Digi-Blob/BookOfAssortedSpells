package com.red_x_tornado.assortedspells.init;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ASBlocks {

	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, BookOfAssortedSpells.MOD_ID);

	public static final RegistryObject<Block> RUNESTONE = REGISTRY.register("runestone", () -> new Block(props(Material.ROCK, SoundType.STONE, 1.5F, 6F)));

	public static Block.Properties props(Material material, SoundType sound, float hardness, float resistance) {
		return Block.Properties.create(material).sound(sound).hardnessAndResistance(hardness, resistance);
	}

	public static void registerItemBlocks() {
		// Take all the registered blocks and register item version of them.
		REGISTRY.getEntries().forEach(obj -> ASItems.REGISTRY.register(obj.getId().getPath(), () -> new BlockItem(obj.get(), ASItems.props())));
	}
}