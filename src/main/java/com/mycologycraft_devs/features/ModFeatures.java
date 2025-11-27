package com.mycologycraft_devs.features;

import com.mycologycraft_devs.features.referenceClasses.LakeFeatureAnnotated;
import com.mycologycraft_devs.mycologycraft.MycologyCraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ModFeatures {
	public static Feature<LakeFeatureAnnotated.Configuration> OVERWORLD_LAVA_LAKE_MAGMA_MYCELIUM;

	public static void bootstrap(BootstrapContext<Feature<?>> context) {
		OVERWORLD_LAVA_LAKE_MAGMA_MYCELIUM = register(context, registerKey("magma_mycelium_lava_lake"), new LakeFeatureAnnotated(LakeFeatureAnnotated.Configuration.CODEC));
	}



	public static ResourceKey<Feature<?>> registerKey(String name) {
		return ResourceKey.create(Registries.FEATURE, ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, name));
	}
	
	private static <FC extends FeatureConfiguration, F extends Feature<FC>> F register(BootstrapContext<Feature<?>> context,
																																												ResourceKey<Feature<?>> key,
																																												F feature) {
		context.register(key, feature);
		return feature;
	}
}
