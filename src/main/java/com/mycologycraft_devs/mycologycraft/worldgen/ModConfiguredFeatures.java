package com.mycologycraft_devs.mycologycraft.worldgen;

import com.mycologycraft_devs.mycologycraft.MycologyCraft;

import com.mycologycraft_devs.mycologycraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ModConfiguredFeatures {

	public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_LAVA_LAKE_MAGMA_MYCELIUM = registerKey("magma_mycelium_lava_lake");

	public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		register(context, OVERWORLD_LAVA_LAKE_MAGMA_MYCELIUM, Feature.LAKE,
				new LakeFeature.Configuration(BlockStateProvider.simple(Blocks.LAVA.defaultBlockState()), BlockStateProvider.simple(ModBlocks.EXAMPLE_BLOCK.get())));
	}



	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, name));
	}
	
	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
																																												ResourceKey<ConfiguredFeature<?, ?>> key,
																																												F feature,
																																												FC configuration) {
		context.register(key, new ConfiguredFeature<>(feature, configuration));
	}
}
