package com.mycologycraft_devs.mycologycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExampleComplexDoubleMushroomBlockAlt extends ExampleDoubleMushroomBlock
{
	// This block behaves like ExampleDoubleMushroomBlock but has full collision
	public ExampleComplexDoubleMushroomBlockAlt(ResourceKey<ConfiguredFeature<?, ?>> feature, Properties properties)
	{

		super(feature, properties);
	}
	protected static final VoxelShape UPPER_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);
	protected static final VoxelShape LOWER_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			return LOWER_SHAPE;
		} else {
			return UPPER_SHAPE;
		}
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return getShape(state, level, pos, context);
	}
}
