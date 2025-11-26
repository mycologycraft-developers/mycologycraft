package com.mycologycraft_devs.mycologycraft.datagen;

import com.mycologycraft_devs.mycologycraft.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

//this provides datamaps, important as it actually adds functions such as
//compostability and functionality as a fuel to items

public class ModDataMapProvider  extends DataMapProvider {

    /**
     * Create a new provider.
     *
     * @param packOutput     the output location
     * @param lookupProvider a {@linkplain CompletableFuture} supplying the registries
     */
    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }


    //tags are added within this function
    @Override
    protected void gather() {
        //example method to make an item fuel
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(ModItems.EXAMPLE_ITEM.getId(), new FurnaceFuel(1200), false);

    }
}
