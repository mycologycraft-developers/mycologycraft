package com.mycologycraft_devs.mycologycraft.datagen;


import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

//this function isnt gonna have much use to start with but is important for
//worldgen, armor trims, enchantments and more
public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder();
    //extend this function with .add to add new data, example from the tutorial project
    //.add(Registries.TRIM_MATERIAL, ModTrimMaterials::bootstrap)

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(MycologyCraft.MODID));
    }
}
