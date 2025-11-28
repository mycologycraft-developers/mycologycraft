package com.mycologycraft_devs.mycologycraft.worldgen;

import java.util.List;

import com.mycologycraft_devs.mycologycraft.MycologyCraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.*;

public class ModPlacedFeatures {

	public static final ResourceKey<PlacedFeature> MAGMA_MYCELIUM_LAVA_LAKE_KEY = registerKey("magma_mycelium_lava_lake");

	public static void bootstrap(BootstrapContext<PlacedFeature> context) {
		var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

		register(
				context,
				MAGMA_MYCELIUM_LAVA_LAKE_KEY,
				configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_LAVA_LAKE_MAGMA_MYCELIUM),
				List.of(
					RarityFilter.onAverageOnceEvery(9),
					InSquarePlacement.spread(),
					HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.top())),
					EnvironmentScanPlacement.scanningFor(
							Direction.DOWN,
							BlockPredicate.allOf(BlockPredicate.not(BlockPredicate.ONLY_IN_AIR_PREDICATE), BlockPredicate.insideWorld(new BlockPos(0, -5, 0))),
							32
					),
					SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -5),
					BiomeFilter.biome()
				)
		);
	}

	private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> magmaMyceliumLavaLakeKey, Holder.Reference<ConfiguredFeature<?,?>> orThrow, RarityFilter rarityFilter, InSquarePlacement spread, HeightRangePlacement of, EnvironmentScanPlacement environmentScanPlacement, SurfaceRelativeThresholdFilter of1, BiomeFilter biome) {
	}

	public static ResourceKey<PlacedFeature> registerKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, name));
	}
	
	private static void register(BootstrapContext<PlacedFeature> context,
																ResourceKey<PlacedFeature> key,
																Holder<ConfiguredFeature<?, ?>> configuration,
																List<PlacementModifier> modifiers) {
		context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
	}
}
