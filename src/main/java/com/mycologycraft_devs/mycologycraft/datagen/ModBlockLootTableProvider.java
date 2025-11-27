package com.mycologycraft_devs.mycologycraft.datagen;


import java.util.Set;

import com.mycologycraft_devs.mycologycraft.block.ModBlocks;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

//this class configures loot tables for blocks. This is important, as without it blocks will not drop anything
public class ModBlockLootTableProvider extends BlockLootSubProvider {

    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }


    //for any simple blocks that drop themselves, use the dropSelf method below, anything more complicated
    //will require different or even custom functions
    @Override
    protected void generate() {

        dropSelf(ModBlocks.EXAMPLE_BLOCK.get());
        dropSelf(ModBlocks.MUSHROOM_SPAWNING_BLOCK.get());
    }

    //get all blocks that were registered in modblocks, converting it to an iteratable.
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
