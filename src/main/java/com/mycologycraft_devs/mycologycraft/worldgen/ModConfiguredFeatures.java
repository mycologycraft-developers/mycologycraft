package com.mycologycraft_devs.mycologycraft.worldgen;

import com.mycologycraft_devs.mycologycraft.block.ModBlocks;
import com.mycologycraft_devs.mycologycraft.datagen.helpers.DataGenHelper;
import com.mycologycraft_devs.mycologycraft.feature.ModBigMushroomFeatureTypeOne;
import com.mycologycraft_devs.mycologycraft.feature.ModFeatures;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ModConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> BIG_MUSHROOM_TYPE_ONE = registerKey("big_mushroom_type_one");

	public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		register(context, BIG_MUSHROOM_TYPE_ONE, ModFeatures.BIG_MUSHROOM_TYPE_ONE.get(), new ModBigMushroomFeatureTypeOne.Configuration(
			BlockStateProvider.simple(ModBlocks.EXAMPLE_MUSHROOM_STEM.get()),
			BlockStateProvider.simple(ModBlocks.EXAMPLE_MUSHROOM_BLOCK.get()),
			3, 5, 4, 6
		));
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, DataGenHelper.asResource(name));
	}
	
	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
																																												ResourceKey<ConfiguredFeature<?, ?>> key,
																																												F feature,
																																												FC configuration) {
		context.register(key, new ConfiguredFeature<>(feature, configuration));
	}
}
