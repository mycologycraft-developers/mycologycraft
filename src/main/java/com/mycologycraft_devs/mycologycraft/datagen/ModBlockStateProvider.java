package com.mycologycraft_devs.mycologycraft.datagen;


import com.mycologycraft_devs.mycologycraft.block.ModBlocks;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
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

			BlockModelBuilder stemModel = models().singleTexture(stemModelName, parentTexture, stemTexture);
			BlockModelBuilder capModel = models().singleTexture(capModelName, parentTexture, capTexture);
			BlockModelBuilder insideModel = models().singleTexture(capInsideModelName, parentTexture, insideTexture);
			models().cubeAll(stemInventoryModelName, stemTexture);
			models().cubeAll(capInventoryModelName, capTexture);

			MultiPartBlockStateBuilder[] builders = {
				getMultipartBuilder(stemBlock.get()),
				getMultipartBuilder(capBlock.get())
			};
			BlockModelBuilder[] models = {stemModel, capModel};
			
			for (int i = 0; i < builders.length; i++) {
				builders[i]
					.part()
					.modelFile(models[i])
					.addModel()
					.condition(BlockStateProperties.NORTH, true)
					.end()
					.part()
					.modelFile(insideModel)
					.addModel()
					.condition(BlockStateProperties.NORTH, false)
					.end()
					
					.part()
					.modelFile(models[i])
					.uvLock(true)
					.rotationY(90)
					.addModel()
					.condition(BlockStateProperties.EAST, true)
					.end()
					.part()
					.modelFile(insideModel)
					.rotationY(90)
					.addModel()
					.condition(BlockStateProperties.EAST, false)
					.end()
					
					.part()
					.modelFile(models[i])
					.uvLock(true)
					.rotationY(180)
					.addModel()
					.condition(BlockStateProperties.SOUTH, true)
					.end()
					.part()
					.modelFile(insideModel)
					.rotationY(180)
					.addModel()
					.condition(BlockStateProperties.SOUTH, false)
					.end()
					
					.part()
					.modelFile(models[i])
					.uvLock(true)
					.rotationY(270)
					.addModel()
					.condition(BlockStateProperties.WEST, true)
					.end()
					.part()
					.modelFile(insideModel)
					.rotationY(270)
					.addModel()
					.condition(BlockStateProperties.WEST, false)
					.end()
					
					.part()
					.modelFile(models[i])
					.uvLock(true)
					.rotationX(270)
					.addModel()
					.condition(BlockStateProperties.UP, true)
					.end()
					.part()
					.modelFile(insideModel)
					.rotationX(270)
					.addModel()
					.condition(BlockStateProperties.UP, false)
					.end()
					
					.part()
					.modelFile(models[i])
					.uvLock(true)
					.rotationX(90)
					.addModel()
					.condition(BlockStateProperties.DOWN, true)
					.end()
					.part()
					.modelFile(insideModel)
					.rotationX(90)
					.addModel()
					.condition(BlockStateProperties.DOWN, false)
					.end();
			}
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
