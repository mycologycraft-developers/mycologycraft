package com.mycologycraft_devs.mycologycraft.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExampleMushroomBlock extends MushroomBlock
{
	public static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0); //this is actually used in super
	public ExampleMushroomBlock(ResourceKey<ConfiguredFeature<?, ?>> feature, Properties properties)
	{
		super(feature, properties);
	}
}
