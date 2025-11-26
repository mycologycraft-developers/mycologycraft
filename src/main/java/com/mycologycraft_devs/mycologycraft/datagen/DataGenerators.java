package com.mycologycraft_devs.mycologycraft.datagen;


//THIS CLASS IS INCREDIBLY IMPORTANT
//It's used to generate the json files that tell the game how to render, tag, drop, etc. every item
//you can create json files manually if it would be faster, but for the most part use the datagen
//when you change something around here, run the DataGen configuration to generate the data

import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MycologyCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {


    private static String modID = MycologyCraft.MODID;
    //this is where the other data generators will be registered
    //FOR SOME REASON the way this is done is horribly inconsistant
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFIleHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();


        //loot table provider
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        //mod recipe provider
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));

        //mod block tags provider
        BlockTagsProvider blockTagsProvider = new ModBlockTagProvider(packOutput, lookupProvider, modID, existingFIleHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);

        //mod item tags provider
        generator.addProvider(event.includeServer(), new ModItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), modID, existingFIleHelper));

        //mod item model provider
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, modID, existingFIleHelper));

        //mod block state provider
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, modID, existingFIleHelper));

        //mod data map provider
        generator.addProvider(event.includeServer(), new ModDataMapProvider(packOutput, lookupProvider));

        //mod data pack provider
        generator.addProvider(event.includeServer(), new ModDatapackProvider(packOutput, lookupProvider));

    }
}
