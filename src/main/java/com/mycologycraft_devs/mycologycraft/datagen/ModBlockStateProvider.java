package com.mycologycraft_devs.mycologycraft.datagen;


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

			bigMushroomBlocks(ModBlocks.EXAMPLE_MUSHROOM_STEM, ModBlocks.EXAMPLE_MUSHROOM_BLOCK);
    }

		private void bigMushroomBlocks(DeferredBlock<?> stemBlock, DeferredBlock<?> capBlock) {
			String stemModelName = stemBlock.getId().getPath();
			String capModelName = capBlock.getId().getPath();
			String capInsideModelName = capBlock.getId().getPath() + "_inside";
			String stemInventoryModelName = stemBlock.getId().getPath() + "_inventory";
			String capInventoryModelName = capBlock.getId().getPath() + "_inventory";

			ResourceLocation parentTexture = mcLoc("block/template_single_face");
			ResourceLocation stemTexture = blockTexture(stemBlock.get());
			ResourceLocation capTexture = blockTexture(capBlock.get());
			ResourceLocation insideTexture = DataGenHelper.asResource("block/" + capInsideModelName);

			blockItem(stemBlock, "_inventory");
			blockItem(capBlock, "_inventory");

			models().singleTexture(stemModelName, parentTexture, stemTexture);
			models().singleTexture(capModelName, parentTexture, capTexture);
			models().singleTexture(capInsideModelName, parentTexture, insideTexture);
			models().cubeAll(stemInventoryModelName, stemTexture);
			models().cubeAll(capInventoryModelName, capTexture);
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
