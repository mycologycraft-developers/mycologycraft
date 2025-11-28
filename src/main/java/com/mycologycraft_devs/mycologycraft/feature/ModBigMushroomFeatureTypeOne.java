package com.mycologycraft_devs.mycologycraft.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mycologycraft_devs.mycologycraft.block.custom.BigMushroomBlock;
import com.mycologycraft_devs.mycologycraft.feature.ModBigMushroomFeatureTypeOne.Configuration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ModBigMushroomFeatureTypeOne extends Feature<Configuration> {
	public ModBigMushroomFeatureTypeOne(Codec<Configuration> codec) {
			super(codec);
	}

	protected void placeMushroomBlock(final LevelAccessor level, final BlockPos.MutableBlockPos blockPos, final BlockState newState) {
		BlockState currentState = level.getBlockState(blockPos);
		if (currentState.isAir()) {
			this.setBlock(level, blockPos, newState);
		}
	}

	protected int getStemHeight(RandomSource random, Configuration config) {
			return random.nextInt((config.maxHeight - config.minHeight) + 1) + config.minHeight;
	}

	protected int getStemRadius(RandomSource random, Configuration config) {
			return random.nextInt((config.maxRadius - config.minRadius) + 1) + config.minRadius;
	}

	protected boolean isValidPosition(
			LevelAccessor level,
			BlockPos pos,
			int minHeight,
			int maxHeight,
			int radius,
			BlockPos.MutableBlockPos mutablePos
	) {
			int i = pos.getY();
			if (i >= level.getMinBuildHeight() + 1 && i + maxHeight + 1 < level.getMaxBuildHeight()) {
					BlockState blockstate = level.getBlockState(pos.below());
					if (!isDirt(blockstate) && !blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
							return false;
					} else {
							for (int j = minHeight; j <= maxHeight; j++) {
									for (int l = -radius; l <= radius; l++) {
											for (int i1 = -radius; i1 <= radius; i1++) {
													BlockState blockstate1 = level.getBlockState(mutablePos.setWithOffset(pos, l, j, i1));
													if (!blockstate1.isAir() && !blockstate1.is(BlockTags.LEAVES)) {
															return false;
													}
											}
									}
							}

							return true;
					}
			} else {
					return false;
			}
	}

	protected int getStemRadiusForHeight(final int radius, final int yo) {
		return yo <= 3 ? 0 : radius;
	}

	// ==================================================

	private boolean is_in_circle(int dx, int dz, int radius) {
		double distance_squared = Math.pow(dx, 2) + Math.pow(dz, 2);
		return distance_squared <= Math.pow(radius + 0.5, 2);
	}

	private boolean is_in_circle_border(int dx, int dz, int radius) { return is_in_circle_border(dx, dz, radius, 0.5); }
	private boolean is_in_circle_border(int dx, int dz, int radius, double borderThickness) {
		double distance = Math.pow(Math.pow(dx, 2) + Math.pow(dz, 2), 0.5);

		double outer_radius = radius + 0.5;
		double inner_radius = radius - borderThickness - 0.5;

		return distance <= outer_radius && distance > inner_radius;
	}

	// ==================================================

	protected void placeStem(
			LevelAccessor level,
			RandomSource random,
			BlockPos pos,
			Configuration config,
			int maxHeight,
			BlockPos.MutableBlockPos mutablePos
	) {
		for (int i = 0; i < maxHeight; i++) {
			mutablePos.set(pos).move(Direction.UP, i);
			if (!level.getBlockState(mutablePos).isSolidRender(level, mutablePos)) {
				BlockState state = config.stemProvider.getState(random, pos);

				state = state
					.setValue(BigMushroomBlock.UP, i != maxHeight - 1)
					.setValue(BigMushroomBlock.DOWN, i != 0);
				
				this.setBlock(level, mutablePos, state);
			}
		}
	}
	protected void makeCap(final LevelAccessor level,
			final RandomSource random,
			final BlockPos origin,
			final int height,
			final int radius,
			final BlockPos.MutableBlockPos blockPos,
			final Configuration config) {
		int top_radius = radius - 1;
		for(int dx = -top_radius; dx <= top_radius; ++dx) {
			for(int dz = -top_radius; dz <= top_radius; ++dz) {
				if (is_in_circle(dx, dz, top_radius)) {
					blockPos.setWithOffset(origin, dx, height, dz);
					BlockState state = config.capProvider.getState(random, origin);
					state = state.setValue(BigMushroomBlock.DOWN, false);

					this.placeMushroomBlock(level, blockPos, state);
				}
			}
		}

		for(int dx = -radius; dx <= radius; ++dx) {
			for(int dz = -radius; dz <= radius; ++dz) {
				if (is_in_circle_border(dx, dz, radius)) {
					blockPos.setWithOffset(origin, dx, height-1, dz);
					BlockState state = config.capProvider.getState(random, origin);

					state = state
						.setValue(BigMushroomBlock.NORTH, dz < 0)
						.setValue(BigMushroomBlock.EAST, dx > 0)
						.setValue(BigMushroomBlock.SOUTH, dz > 0)
						.setValue(BigMushroomBlock.WEST, dx < 0);

					this.placeMushroomBlock(level, blockPos, state);
				}
			}
		}
	}

	/**
	* Places the given feature at the given location.
	* During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated, that they can safely generate into.
	*
	* @param context A context object with a reference to the level and the position
	*                the feature is being placed at
	*/
	@Override
	public boolean place(final FeaturePlaceContext<Configuration> context) {
		 WorldGenLevel worldgenlevel = context.level();
		 BlockPos blockpos = context.origin();
		 RandomSource randomsource = context.random();
		 Configuration config = context.config();
		 int height = this.getStemHeight(randomsource, config);
		 int radius = this.getStemRadius(randomsource, config);
		 BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		 if (!this.isValidPosition(worldgenlevel, blockpos, config.minHeight, height, radius, blockpos$mutableblockpos)) {
				 return false;
		 } else {
				 this.makeCap(worldgenlevel, randomsource, blockpos, height, radius, blockpos$mutableblockpos, config);
				 this.placeStem(worldgenlevel, randomsource, blockpos, config, height, blockpos$mutableblockpos);
				 return true;
		 }
	}


	public static record Configuration(
			BlockStateProvider stemProvider,
			BlockStateProvider capProvider,
			int minRadius,
			int maxRadius,
			int minHeight,
			int maxHeight) implements FeatureConfiguration {
		public static final Codec<Configuration> CODEC = RecordCodecBuilder.create((i) -> {
			return i.group(
				BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(Configuration::stemProvider),
				BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(Configuration::capProvider),
				Codec.INT.fieldOf("min_radius").forGetter(Configuration::minRadius),
				Codec.INT.fieldOf("max_radius").forGetter(Configuration::maxRadius),
				Codec.INT.fieldOf("min_height").forGetter(Configuration::minHeight),
				Codec.INT.fieldOf("max_height").forGetter(Configuration::maxHeight)
			).apply(i, Configuration::new);
		});
	}
}
