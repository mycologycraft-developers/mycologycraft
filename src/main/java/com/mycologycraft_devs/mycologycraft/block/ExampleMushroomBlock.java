package com.mycologycraft_devs.mycologycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExampleMushroomBlock extends MushroomBlock
{
	protected static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public ExampleMushroomBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, Properties properties)
	{
		super(feature, properties);
	}
}
