//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mycologycraft_devs.features.referenceClasses;

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

import java.util.Arrays;

import static java.lang.Math.abs;


public class MyceliaLavaLake extends Feature<MyceliaLavaLake.Configuration> {
	private static final BlockState AIR;

	public MyceliaLavaLake(Codec<MyceliaLavaLake.Configuration> codec) {
		super(codec);
	}

	public boolean place(FeaturePlaceContext<MyceliaLavaLake.Configuration> context) {
		BlockPos origin = context.origin();
		WorldGenLevel level = context.level();
		RandomSource random = context.random();
		Configuration config = (Configuration)context.config();
		if (origin.getY() <= level.getMinBuildHeight() + 4) {
			return false;
		} else {
			origin = origin.below(4);

			//the main grid for generating the lava lake
			boolean[] grid = new boolean[2048];

			//the secondary grid for generating our expanded barrier of mycelia
			boolean[] outerMycelium = new boolean[2048];

			//our custom air list, for referencing where blocks have already been replaced with air
			boolean[] airList = new boolean[4096];
			Arrays.fill(airList, false);

			int spots = random.nextInt(4) + 4;


			//this block seems to create a general shape that matches the lava lakes
			for(int j = 0; j < spots; ++j) {
				double xr = random.nextDouble() * (double)6.0F + (double)3.0F;
				double yr = random.nextDouble() * (double)4.0F + (double)2.0F;
				double zr = random.nextDouble() * (double)6.0F + (double)3.0F;
				double xp = random.nextDouble() * ((double)16.0F - xr - (double)2.0F) + (double)1.0F + xr / (double)4.0F;
				double yp = random.nextDouble() * ((double)8.0F - yr - (double)4.0F) + (double)2.0F + yr / (double)2.0F;
				double zp = random.nextDouble() * ((double)16.0F - zr - (double)2.0F) + (double)1.0F + zr / (double)4.0F;

				for(int xx = 1; xx < 15; ++xx) {
					for(int zz = 1; zz < 15; ++zz) {
						for(int yy = 1; yy < 7; ++yy) {
							double xd = ((double)xx - xp) / (xr / (double)2.0F);
							double yd = ((double)yy - yp) / (yr / (double)2.0F);
							double zd = ((double)zz - zp) / (zr / (double)2.0F);
							double d = xd * xd + yd * yd + zd * zd;
							if (d < (double)1.0F) {
								grid[(xx * 16 + zz) * 8 + yy] = true;
							}
						}
					}
				}
			}

			//creating a secondary shape to use to expand the barrier so we can spawn more mycelium
			for(int j = 0; j < spots; ++j) {
				double xr = random.nextDouble() * (double)6.0F + (double)3.0F;
				double yr = random.nextDouble() * (double)4.0F + (double)2.0F;
				double zr = random.nextDouble() * (double)6.0F + (double)3.0F;
				double xp = random.nextDouble() * ((double)16.0F - xr - (double)2.0F) + (double)1.0F + xr / (double)8.0F;
				double yp = random.nextDouble() * ((double)8.0F - yr - (double)4.0F) + (double)2.0F + yr / (double)4.0F;
				double zp = random.nextDouble() * ((double)16.0F - zr - (double)2.0F) + (double)1.0F + zr / (double)8.0F;

				for(int xx = 1; xx < 15; ++xx) {
					for(int zz = 1; zz < 15; ++zz) {
						for(int yy = 1; yy < 7; ++yy) {
							double xd = ((double)xx - xp) / (xr / (double)2.0F);
							double yd = ((double)yy - yp) / (yr / (double)2.0F);
							double zd = ((double)zz - zp) / (zr / (double)2.0F);
							double d = xd * xd + yd * yd + zd * zd;
							if (d < (double)4F) {
								outerMycelium[(xx * 16 + zz) * 8 + yy] = true;
							}
						}
					}
				}
			}

			BlockState fluid = config.fluid().getState(random, origin);


			//im still... not exactly sure what this does
			//it seems to do nothing?
			for(int xx = 0; xx < 16; ++xx) {
				for(int zz = 0; zz < 16; ++zz) {
					for(int yy = 0; yy < 8; ++yy) {
						boolean flag = !grid[(xx * 16 + zz) * 8 + yy] && (xx < 15 && grid[((xx + 1) * 16 + zz) * 8 + yy] || xx > 0 && grid[((xx - 1) * 16 + zz) * 8 + yy] || zz < 15 && grid[(xx * 16 + zz + 1) * 8 + yy] || zz > 0 && grid[(xx * 16 + (zz - 1)) * 8 + yy] || yy < 7 && grid[(xx * 16 + zz) * 8 + yy + 1] || yy > 0 && grid[(xx * 16 + zz) * 8 + (yy - 1)]);
						if (flag) {
							BlockState blockState = level.getBlockState(origin.offset(xx, yy, zz));
							if (yy >= 4 && blockState.liquid()) {
								return false;
							}

							if (yy < 4 && !blockState.isSolid() && level.getBlockState(origin.offset(xx, yy, zz)) != fluid) {
								return false;
							}
						}
					}
				}
			}


			//a bit of custom code to create an open area above our custom lava pit
			for(int xx = 0; xx < 16; ++xx) {
				for (int zz = 0; zz < 16; ++zz) {
					for (int yy = 0; yy < 9; ++yy) {
						//if above the floor level
						if (yy>=5){
							//if at the ceiling level and the random dosent hit (keeps the ceiling from being flat)
							if (yy!=8 || random.nextInt(2) != 0) {
								//if inside our radius of 12 (using abs and offset to set this properly, yay replicube helped)
								if (abs(xx - 8) + abs(yy - 8) + abs(zz - 8) < 12) {
									BlockPos blockpos1 = origin.offset(xx, yy, zz);
									airList[(xx * 16 + zz) * 8 + yy] = true;
									level.setBlock(blockpos1, AIR, 2);
								}
							}
						}
					}
				}
			}


			//this clears out some area above our lava where its indented, it also writes to the air block list so it can be referenced
			for(int xx = 0; xx < 16; ++xx) {
				for(int zz = 0; zz < 16; ++zz) {
					for(int yy = 0; yy < 8; ++yy) {
						if (grid[(xx * 16 + zz) * 8 + yy]) {
							BlockPos placePos = origin.offset(xx, yy, zz);
							if (this.canReplaceBlock(level.getBlockState(placePos))) {
								boolean flag1 = yy >= 4;
								airList[(xx * 16 + zz) * 8 + yy] = true;
								level.setBlock(placePos, flag1 ? AIR : fluid, 2);
								if (flag1) {
									airList[(xx * 16 + zz) * 8 + yy] = true;
									level.scheduleTick(placePos, AIR.getBlock(), 0);
									this.markAboveForPostProcessing(level, placePos);
								}
							}
						}
					}
				}
			}

			//this creates the initial barrier
			BlockState barrier = config.barrier().getState(random, origin);
			if (!barrier.isAir()) {
				for(int xx = 0; xx < 16; ++xx) {
					for(int zz = 0; zz < 16; ++zz) {
						for(int yy = 0; yy < 8; ++yy) {
							boolean check =
									//not grid at pos (x * 16 + z) * 8 + y (OH GOD OH FUCK THEY ARE DOING FUCKY SHIT TO AVOID 3D ARRAYS)
									//if not at grid origin
									!grid[(xx * 16 + zz) * 8 + yy]
											//and x is less than 15 and the grid position with x+1 is lava
									&& (xx < 15 && grid[((xx + 1) * 16 + zz) * 8 + yy]
											//or x is greater than 0 and the grid position with x-1 is lava
									|| xx > 0 && grid[((xx - 1) * 16 + zz) * 8 + yy]
											//or z is less than 15 and the grid position with z+1 is lava
									|| zz < 15 && grid[(xx * 16 + zz + 1) * 8 + yy]
											//or z is greater than 0 nd the grid position with z-1 is lava
									|| zz > 0 && grid[(xx * 16 + (zz - 1)) * 8 + yy]
											//or y is greater than 7 and the grid position with y+1 is lava
									|| yy < 7 && grid[(xx * 16 + zz) * 8 + yy + 1]
											//or y is less than 0 and the grid position with y-1 is lava
									|| yy > 0 && grid[(xx * 16 + zz) * 8 + (yy - 1)]);

							if (check && (yy < 4 || random.nextInt(2) != 0)) {
								BlockState blockstate = level.getBlockState(origin.offset(xx, yy, zz));
								if (blockstate.isSolid() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
									BlockPos barrierPos = origin.offset(xx, yy, zz);
									level.setBlock(barrierPos, barrier, 2);
									this.markAboveForPostProcessing(level, barrierPos);
								}
							}
						}
					}
				}
			}


			//this creates our expanded barrier using the second grid setup
			//it will create both the top and bottom layer
			BlockState mycelia = config.mycelia().getState(random, origin);
			if (!barrier.isAir()) {
				for(int xx = 0; xx < 16; ++xx) {
					for(int zz = 0; zz < 16; ++zz) {
						for(int yy = 0; yy < 8; ++yy) {
							boolean check =
									//not grid at pos (x * 16 + z) * 8 + y (OH GOD OH FUCK THEY ARE DOING FUCKY SHIT TO AVOID 3D ARRAYS)
									//if not at grid origin
									!outerMycelium[(xx * 16 + zz) * 8 + yy]
											//and x is less than 15 and the grid position with x+1 is lava
											&& !(xx < 15 && grid[((xx + 1) * 16 + zz) * 8 + yy]
											//or x is greater than 0 and the grid position with x-1 is lava
											|| xx > 0 && grid[((xx - 1) * 16 + zz) * 8 + yy]
											//or z is less than 15 and the grid position with z+1 is lava
											|| zz < 15 && grid[(xx * 16 + zz + 1) * 8 + yy]
											//or z is greater than 0 nd the grid position with z-1 is lava
											|| zz > 0 && grid[(xx * 16 + (zz - 1)) * 8 + yy]
											//or y is greater than 7 and the grid position with y+1 is lava
											|| yy < 7 && grid[(xx * 16 + zz) * 8 + yy + 1]
											//or y is less than 0 and the grid position with y-1 is lava
											|| yy > 0 && grid[(xx * 16 + zz) * 8 + (yy - 1)]);

							if (!check) {
								BlockState blockstate = level.getBlockState(origin.offset(xx, yy, zz));
								BlockState above = level.getBlockState(origin.offset(xx, yy+1, zz));

								//fill the whole area of our custom barrier with our barrier block
								if (blockstate.isSolid() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE) && !above.is(Blocks.CAVE_AIR) && !airList[(xx * 16 + zz) * 8 + yy]) {
									BlockPos myceliumPos = origin.offset(xx, yy, zz);
									level.setBlock(myceliumPos, barrier, 2);
									this.markAboveForPostProcessing(level, myceliumPos);
								}

								//replace the topmost layer with our mycelium
								if (blockstate.isSolid() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE) && (above.is(Blocks.CAVE_AIR) || above.is(Blocks.AIR) || airList[(xx * 16 + zz) * 8 + yy])) {
									BlockPos myceliumPos = origin.offset(xx, yy, zz);
									level.setBlock(myceliumPos, mycelia, 2);
									this.markAboveForPostProcessing(level, myceliumPos);
								}

							}
						}
					}
				}
			}

			//this is only used for water based lakes, but shall be left in case we want to do something with it
			if (fluid.getFluidState().is(FluidTags.WATER)) {
				for(int k2 = 0; k2 < 16; ++k2) {
					for(int k3 = 0; k3 < 16; ++k3) {
						int i4 = 4;
						BlockPos blockpos2 = origin.offset(k2, 4, k3);
						if (((Biome)level.getBiome(blockpos2).value()).shouldFreeze(level, blockpos2, false) && this.canReplaceBlock(level.getBlockState(blockpos2))) {
							level.setBlock(blockpos2, Blocks.ICE.defaultBlockState(), 2);
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

	public static record Configuration(BlockStateProvider fluid, BlockStateProvider barrier, BlockStateProvider mycelia) implements FeatureConfiguration {
		public static final Codec<MyceliaLavaLake.Configuration> CODEC = RecordCodecBuilder.create((p_190962_) -> p_190962_.group(BlockStateProvider.CODEC.fieldOf("fluid").forGetter(Configuration::fluid), BlockStateProvider.CODEC.fieldOf("barrier").forGetter(Configuration::barrier), BlockStateProvider.CODEC.fieldOf("mycelia").forGetter(Configuration::mycelia)).apply(p_190962_, Configuration::new));
	}
}
