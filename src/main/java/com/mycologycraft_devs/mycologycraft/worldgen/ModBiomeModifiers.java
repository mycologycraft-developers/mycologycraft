package com.mycologycraft_devs.mycologycraft.worldgen;

import com.mycologycraft_devs.mycologycraft.MycologyCraft;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifiers {

	public static final ResourceKey<BiomeModifier> OVERWORLD_LAVA_LAKE_MAGMA_MYCELIUM = registerKey("magma_mycelium_lava_lake");

	public static void bootstrap(BootstrapContext<BiomeModifier> context) {
		var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
		var biomes = context.lookup(Registries.BIOME);

		context.register(OVERWORLD_LAVA_LAKE_MAGMA_MYCELIUM, new BiomeModifiers.AddFeaturesBiomeModifier(
				biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
				HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MAGMA_MYCELIUM_LAVA_LAKE_KEY)),
				GenerationStep.Decoration.LAKES
		));
	}

	public static ResourceKey<BiomeModifier> registerKey(String name) {
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, name));
	}
}
