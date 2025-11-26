package com.mycologycraft_devs.mycologycraft.datagen;


import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import com.mycologycraft_devs.mycologycraft.worldgen.ModBiomeModifiers;
import com.mycologycraft_devs.mycologycraft.worldgen.ModConfiguredFeatures;
import com.mycologycraft_devs.mycologycraft.worldgen.ModPlacedFeatures;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

//this function isnt gonna have much use to start with but is important for
//worldgen, armor trims, enchantments and more
public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {

		public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
							.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
							.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
							.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);
		//extend this function with .add to add new data, example from the tutorial project
		//.add(Registries.TRIM_MATERIAL, ModTrimMaterials::bootstrap)

		public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
				super(output, registries, BUILDER, Set.of(MycologyCraft.MODID));
		}
}
