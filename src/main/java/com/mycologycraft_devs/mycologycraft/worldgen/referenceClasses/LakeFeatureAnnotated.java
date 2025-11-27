//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mycologycraft_devs.mycologycraft.worldgen.referenceClasses;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;


public class LakeFeatureAnnotated extends Feature<Configuration> {
	private static final BlockState AIR;

	public LakeFeatureAnnotated(Codec<Configuration> codec) {
		super(codec);
	}

	public boolean place(FeaturePlaceContext<Configuration> context) {
		BlockPos blockpos = context.origin();
		WorldGenLevel worldgenlevel = context.level();
		RandomSource randomsource = context.random();
		Configuration lakefeature$configuration = (Configuration)context.config();
		if (blockpos.getY() <= worldgenlevel.getMinBuildHeight() + 4) {
			return false;
		} else {
			blockpos = blockpos.below(4);
			boolean[] aboolean = new boolean[2048];
			int i = randomsource.nextInt(4) + 4;
			//this block seems to create a general shape that matches the lava lakes
			for(int j = 0; j < i; ++j) {
				double d0 = randomsource.nextDouble() * (double)6.0F + (double)3.0F;
				double d1 = randomsource.nextDouble() * (double)4.0F + (double)2.0F;
				double d2 = randomsource.nextDouble() * (double)6.0F + (double)3.0F;
				double d3 = randomsource.nextDouble() * ((double)16.0F - d0 - (double)2.0F) + (double)1.0F + d0 / (double)2.0F;
				double d4 = randomsource.nextDouble() * ((double)8.0F - d1 - (double)4.0F) + (double)2.0F + d1 / (double)2.0F;
				double d5 = randomsource.nextDouble() * ((double)16.0F - d2 - (double)2.0F) + (double)1.0F + d2 / (double)2.0F;

				for(int l = 1; l < 15; ++l) {
					for(int i1 = 1; i1 < 15; ++i1) {
						for(int j1 = 1; j1 < 7; ++j1) {
							double d6 = ((double)l - d3) / (d0 / (double)2.0F);
							double d7 = ((double)j1 - d4) / (d1 / (double)2.0F);
							double d8 = ((double)i1 - d5) / (d2 / (double)2.0F);
							double d9 = d6 * d6 + d7 * d7 + d8 * d8;
							if (d9 < (double)1.0F) {
								aboolean[(l * 16 + i1) * 8 + j1] = true;
							}
						}
					}
				}
			}
			//end of block

			BlockState blockstate1 = lakefeature$configuration.fluid().getState(randomsource, blockpos);


			//this appears to prevent overwriting liquids
			for(int k1 = 0; k1 < 16; ++k1) {
				for(int k = 0; k < 16; ++k) {
					for(int l2 = 0; l2 < 8; ++l2) {
						boolean flag = !aboolean[(k1 * 16 + k) * 8 + l2] && (k1 < 15 && aboolean[((k1 + 1) * 16 + k) * 8 + l2] || k1 > 0 && aboolean[((k1 - 1) * 16 + k) * 8 + l2] || k < 15 && aboolean[(k1 * 16 + k + 1) * 8 + l2] || k > 0 && aboolean[(k1 * 16 + (k - 1)) * 8 + l2] || l2 < 7 && aboolean[(k1 * 16 + k) * 8 + l2 + 1] || l2 > 0 && aboolean[(k1 * 16 + k) * 8 + (l2 - 1)]);
						if (flag) {
							BlockState blockstate3 = worldgenlevel.getBlockState(blockpos.offset(k1, l2, k));
							if (l2 >= 4 && blockstate3.liquid()) {
								return false;
							}

							if (l2 < 4 && !blockstate3.isSolid() && worldgenlevel.getBlockState(blockpos.offset(k1, l2, k)) != blockstate1) {
								return false;
							}
						}
					}
				}
			}


			//
			for(int l1 = 0; l1 < 16; ++l1) {
				for(int i2 = 0; i2 < 16; ++i2) {
					for(int i3 = 0; i3 < 8; ++i3) {
						if (aboolean[(l1 * 16 + i2) * 8 + i3]) {
							BlockPos blockpos1 = blockpos.offset(l1, i3, i2);
							if (this.canReplaceBlock(worldgenlevel.getBlockState(blockpos1))) {
								boolean flag1 = i3 >= 4;
								worldgenlevel.setBlock(blockpos1, flag1 ? AIR : blockstate1, 2);
								if (flag1) {
									worldgenlevel.scheduleTick(blockpos1, AIR.getBlock(), 0);
									this.markAboveForPostProcessing(worldgenlevel, blockpos1);
								}
							}
						}
					}
				}
			}

			//this creates a barrier, gonna tinker here
			BlockState blockstate2 = lakefeature$configuration.barrier().getState(randomsource, blockpos);
			if (!blockstate2.isAir()) {
				for(int j2 = 0; j2 < 16; ++j2) {
					for(int j3 = 0; j3 < 16; ++j3) {
						for(int l3 = 0; l3 < 8; ++l3) {
							boolean flag2 = !aboolean[(j2 * 16 + j3) * 8 + l3] && (j2 < 15 && aboolean[((j2 + 1) * 16 + j3) * 8 + l3] || j2 > 0 && aboolean[((j2 - 1) * 16 + j3) * 8 + l3] || j3 < 15 && aboolean[(j2 * 16 + j3 + 1) * 8 + l3] || j3 > 0 && aboolean[(j2 * 16 + (j3 - 1)) * 8 + l3] || l3 < 7 && aboolean[(j2 * 16 + j3) * 8 + l3 + 1] || l3 > 0 && aboolean[(j2 * 16 + j3) * 8 + (l3 - 1)]);
							if (flag2 && (l3 < 4 || randomsource.nextInt(2) != 0)) {
								BlockState blockstate = worldgenlevel.getBlockState(blockpos.offset(j2, l3, j3));
								if (blockstate.isSolid() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
									BlockPos blockpos3 = blockpos.offset(j2, l3, j3);
									worldgenlevel.setBlock(blockpos3, blockstate2, 2);
									this.markAboveForPostProcessing(worldgenlevel, blockpos3);
								}
							}
						}
					}
				}
			}

			if (blockstate1.getFluidState().is(FluidTags.WATER)) {
				for(int k2 = 0; k2 < 16; ++k2) {
					for(int k3 = 0; k3 < 16; ++k3) {
						int i4 = 4;
						BlockPos blockpos2 = blockpos.offset(k2, 4, k3);
						if (((Biome)worldgenlevel.getBiome(blockpos2).value()).shouldFreeze(worldgenlevel, blockpos2, false) && this.canReplaceBlock(worldgenlevel.getBlockState(blockpos2))) {
							worldgenlevel.setBlock(blockpos2, Blocks.ICE.defaultBlockState(), 2);
						}
					}
				}
			}

			return true;
		}
	}

	private boolean canReplaceBlock(BlockState state) {
		return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
	}

	static {
		AIR = Blocks.CAVE_AIR.defaultBlockState();
	}

	public static record Configuration(BlockStateProvider fluid, BlockStateProvider barrier) implements FeatureConfiguration {
		public static final Codec<Configuration> CODEC = RecordCodecBuilder.create((p_190962_) -> p_190962_.group(BlockStateProvider.CODEC.fieldOf("fluid").forGetter(Configuration::fluid), BlockStateProvider.CODEC.fieldOf("barrier").forGetter(Configuration::barrier)).apply(p_190962_, Configuration::new));
	}
}
