package com.red_x_tornado.assortedspells.init;

import java.util.function.Supplier;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.block.FancyTorchBlock;
import com.red_x_tornado.assortedspells.block.FancyWallTorchBlock;
import com.red_x_tornado.assortedspells.block.ResearchTableBlock;
import com.red_x_tornado.assortedspells.block.WandBuilderBlock;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// Blocks
public class ASBlocks {

	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, BookOfAssortedSpells.MOD_ID);

	public static final RegistryObject<Block> RUNESTONE = REGISTRY.register("runestone", () -> new Block(props(Material.ROCK, SoundType.STONE, 1.5F, 6F)));
	public static final RegistryObject<Block> WAND_BUILDER = REGISTRY.register("wand_builder", () -> new WandBuilderBlock(props(Material.WOOD, SoundType.WOOD, 2F, 3F).notSolid().harvestTool(ToolType.AXE)));
	public static final RegistryObject<Block> RESEARCH_TABLE = REGISTRY.register("research_table", () -> new ResearchTableBlock(props(Material.WOOD, SoundType.WOOD, 2F, 3F).notSolid().harvestTool(ToolType.AXE)));
	public static final RegistryObject<Block> FANCY_TORCH = REGISTRY.register("fancy_torch", () -> new FancyTorchBlock(props(Material.MISCELLANEOUS, SoundType.WOOD).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel(state -> state.get(BlockStateProperties.LIT) ? 14 : 0), ParticleTypes.FLAME));
	public static final RegistryObject<Block> FANCY_WALL_TORCH = REGISTRY.register("fancy_wall_torch", () -> new FancyWallTorchBlock(props(Material.MISCELLANEOUS, SoundType.WOOD).doesNotBlockMovement().zeroHardnessAndResistance().setLightLevel(state -> state.get(BlockStateProperties.LIT) ? 14 : 0).lootFrom(FANCY_TORCH), ParticleTypes.FLAME));

	public static Block.Properties props(Material material, SoundType sound, float hardness, float resistance) {
		return props(material, sound).hardnessAndResistance(hardness, resistance);
	}

	public static Block.Properties props(Material material, SoundType sound) {
		return Block.Properties.create(material).sound(sound);
	}

	public static void registerItemBlocks() {
		// Take all the registered blocks and register item version of them.
		REGISTRY.getEntries().forEach(obj -> {
			final Supplier<? extends Item> item;

			if (obj != FANCY_TORCH && obj != FANCY_WALL_TORCH)
				item = () -> new BlockItem(obj.get(), ASItems.props());
			else if (obj == FANCY_TORCH)
				item = () -> new WallOrFloorItem(FANCY_TORCH.get(), FANCY_WALL_TORCH.get(), ASItems.props());
			else item = null;

			if (item != null)
				ASItems.REGISTRY.register(obj.getId().getPath(), item);
		});
	}
}