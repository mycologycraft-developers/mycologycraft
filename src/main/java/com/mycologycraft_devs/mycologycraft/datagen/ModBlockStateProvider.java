package com.mycologycraft_devs.mycologycraft.datagen;


import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import com.mycologycraft_devs.mycologycraft.block.ModBlocks;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

//this class creates block states and the associated model data, for the most part you will be using the
//blockWithItem helper method but this can be quite dynamic dependent on the block function
public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }


    //add stuff within this method to register block states and models.
    @Override
    protected void registerStatesAndModels() {
      blockWithItem(ModBlocks.EXAMPLE_BLOCK);

    	blockItem(ModBlocks.EXAMPLE_MUSHROOM_STEM, "_inventory");
    	blockItem(ModBlocks.EXAMPLE_MUSHROOM_BLOCK, "_inventory");

			models().singleTexture("example_mushroom_stem", ResourceLocation.parse("minecraft:block/template_single_face"), ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, "block/example_mushroom_stem"));
			models().singleTexture("example_mushroom_block", ResourceLocation.parse("minecraft:block/template_single_face"), ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, "block/example_mushroom_block"));
			models().singleTexture("example_mushroom_block_inside", ResourceLocation.parse("minecraft:block/template_single_face"), ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, "block/example_mushroom_block_inside"));
			models().cubeAll("example_mushroom_stem_inventory", ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, "block/example_mushroom_stem"));
			models().cubeAll("example_mushroom_block_inventory", ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, "block/example_mushroom_block"));
    }

    //registers a cube block with an item
    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    //registers a more advanced block item based on the way it renders in world
    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("mycologycraft:block/" + deferredBlock.getId().getPath()));
    }

    //extension of the above method that allows you to specify data if that modifies the blocks rendering
    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("mycologycraft:block/" + deferredBlock.getId().getPath() + appendix));
    }
}
