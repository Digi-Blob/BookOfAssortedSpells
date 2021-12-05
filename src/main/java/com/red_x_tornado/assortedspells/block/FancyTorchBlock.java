package com.red_x_tornado.assortedspells.block;

import static net.minecraft.state.properties.BlockStateProperties.LIT;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FancyTorchBlock extends TorchBlock {

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(6, 0, 6, 10, 12, 10);

	public FancyTorchBlock(Properties properties, IParticleData particleData) {
		super(properties, particleData);
		setDefaultState(getDefaultState().with(LIT, true));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(LIT);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
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
		if (stateIn.get(LIT)) {
			final double d0 = pos.getX() + 0.5D;
			final double d1 = pos.getY() + 0.9D;
			final double d2 = pos.getZ() + 0.5D;
			worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
			worldIn.addParticle(this.particleData, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
}