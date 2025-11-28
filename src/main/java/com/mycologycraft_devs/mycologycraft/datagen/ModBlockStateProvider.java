package com.mycologycraft_devs.mycologycraft.datagen;


import com.mycologycraft_devs.mycologycraft.block.FeaturelessDoubleMushroomBlock;
import com.mycologycraft_devs.mycologycraft.block.ModBlocks;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
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
        blockWithItem(ModBlocks.MUSHROOM_SPAWNING_BLOCK);

        featurelessDoubleMushroom(ModBlocks.FEATURELESS_DOUBLE_MUSHROOM_BLOCK);
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

    private void featurelessDoubleMushroom(DeferredBlock<?> block) {

        String name = block.getId().getPath();

        // generate three cross models (small, stalk, cap)
        ModelFile small = models().cross(name + "_stage0", modLoc("block/" + name + "_cap")).renderType("cutout");
        ModelFile stalk = models().cross(name + "_stage1", modLoc("block/" + name + "_cap")).renderType("cutout");
        ModelFile cap   = models().cross(name + "_stage2", modLoc("block/" + name + "_stalk")).renderType("cutout");

        // Build blockstate with variants for STAGE property
        getVariantBuilder(block.get())
            .partialState().with(FeaturelessDoubleMushroomBlock.GROWTH_STAGE, 0)
                .modelForState().modelFile(small).addModel()
            .partialState().with(FeaturelessDoubleMushroomBlock.GROWTH_STAGE, 1)
                .modelForState().modelFile(stalk).addModel()
            .partialState().with(FeaturelessDoubleMushroomBlock.GROWTH_STAGE, 2)
                .modelForState().modelFile(cap).addModel();

        // Item model (for inventory) stage0
        // simpleBlockItem(block.get(), small);
        itemModels().withExistingParent(name, "item/generated").texture("layer0", "mycologycraft:block/" + name + "_cap");
    }

}
