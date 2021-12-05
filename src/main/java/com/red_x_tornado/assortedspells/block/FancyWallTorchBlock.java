package com.red_x_tornado.assortedspells.block;

import static net.minecraft.state.properties.BlockStateProperties.LIT;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FancyWallTorchBlock extends WallTorchBlock {

	public FancyWallTorchBlock(Properties properties, IParticleData particleData) {
		super(properties, particleData);
		setDefaultState(getDefaultState().with(BlockStateProperties.LIT, true));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.LIT);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		final ItemStack stack = player.getHeldItem(handIn);
		final boolean lit = state.get(LIT);

		if (!lit && (stack.getItem() == Items.TORCH || stack.getItem() == Items.FLINT_AND_STEEL)) {
			if (!worldIn.isRemote)
				worldIn.setBlockState(pos, state.with(LIT, true));
			return ActionResultType.SUCCESS;
		} else if (lit && (stack.getItem() == Items.WATER_BUCKET || stack.getItem() == Items.WET_SPONGE || stack.getItem() == Items.POTION)) {
			if (!worldIn.isRemote)
				worldIn.setBlockState(pos, state.with(LIT, false));
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.get(LIT))
			super.animateTick(stateIn, worldIn, pos, rand);
	}
}