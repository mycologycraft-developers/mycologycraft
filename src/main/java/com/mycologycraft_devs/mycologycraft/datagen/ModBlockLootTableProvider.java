package com.mycologycraft_devs.mycologycraft.datagen;


import java.util.Set;

import com.mycologycraft_devs.mycologycraft.block.ExampleDoubleMushroomBlock;
import com.mycologycraft_devs.mycologycraft.block.ModBlocks;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;

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
        dropSelf(ModBlocks.EXAMPLE_MUSHROOM_BLOCK.get());
        this.add(ModBlocks.EXAMPLE_DOUBLE_MUSHROOM_BLOCK.get(),
            (block) -> 
            createSinglePropConditionTable(
                block, 
                ExampleDoubleMushroomBlock.HALF,
                DoubleBlockHalf.LOWER)
        );
    }

    //get all blocks that were registered in modblocks, converting it to an iteratable.
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
