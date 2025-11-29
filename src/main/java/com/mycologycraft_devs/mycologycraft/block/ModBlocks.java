package com.mycologycraft_devs.mycologycraft.block;

import java.util.function.Supplier;

import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import com.mycologycraft_devs.mycologycraft.item.ModItems;

import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "mycologycraft" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MycologyCraft.MODID);

    // Creates a new Block with the id "mycologycraft:example_block", combining the namespace and path
    //if you want to register a block and item seperately use Blocks.register here, ommit the supplier, and in the item class
    //register a blockitem that references the block
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = registerBlock("example_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));

    //public static final DeferredBlock<MushroomBlock> EXAMPLE_MUSHROOM_BLOCK = registerBlock("example_mushroom_block",
    public static final DeferredBlock<MushroomBlock> EXAMPLE_MUSHROOM_BLOCK = registerBlock("example_mushroom_block",
            () -> new MushroomBlock(
                TreeFeatures.HUGE_BROWN_MUSHROOM,
                BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM)
            ));



    //helper method that will call to register a block and then register an item for the block using the helper method below
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        //setup a variable for your block to be added to the deferred register list
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        //use the register block item function below to register an item for said block using the set up block above
        registerBlockItem(name, toReturn);
        //return the block to register so it can be added to the list
        return toReturn;
    }

    //helper method to register an item for the block, saves a little complexity for simple blocks
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    

    //method to pass the register to the main mod class
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

}
