package com.mycologycraft_devs.mycologycraft.datagen;


import com.mycologycraft_devs.mycologycraft.block.ExampleDoubleMushroomBlock;
import com.mycologycraft_devs.mycologycraft.block.ModBlocks;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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
        crossBlockWithItem(ModBlocks.EXAMPLE_MUSHROOM_BLOCK);
        doubleCrossBlockWithItem(ModBlocks.EXAMPLE_DOUBLE_MUSHROOM_BLOCK);
				blockWithComplexModel(ModBlocks.EXAMPLE_COMPLEX_MUSHROOM_BLOCK, "example_complex_mushroom_block");
				doubleComplexBlockWithItem(ModBlocks.EXAMPLE_COMPLEX_DOUBLE_MUSHROOM_BLOCK);
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

    //registers a cross block with an item
    private void crossBlockWithItem(DeferredBlock<?> deferredBlock) {
        String name = deferredBlock.getId().getPath();
        ModelFile model = models()
        .cross(name, modLoc("block/" + name))
        .renderType("cutout");

        // simpleBlockWithItem(deferredBlock.get(), model);
        //item
        //not simpleblockitem you idiot
        //block and item separately
        simpleBlock(deferredBlock.get(), model);
        //item as 2d model
        itemModels().withExistingParent(name, "item/generated").texture("layer0", "mycologycraft:block/" + name);
    }

    //registers a double cross block with an item (like tall flowers)
    private void doubleCrossBlockWithItem(DeferredBlock<?> deferredBlock) {
        String name = deferredBlock.getId().getPath();
        ModelFile lower = crossModel(name + "_lower");
        ModelFile upper = crossModel(name + "_upper");

        getVariantBuilder(deferredBlock.get()) 
        .partialState().with(ExampleDoubleMushroomBlock.HALF, DoubleBlockHalf.LOWER).modelForState()
            .modelFile(lower).addModel()
        .partialState().with(ExampleDoubleMushroomBlock.HALF, DoubleBlockHalf.UPPER).modelForState()
            .modelFile(upper).addModel();

        //item
        itemModels().withExistingParent(name, "item/generated").texture("layer0", "mycologycraft:block/" + name + "_upper"); //just looks better in this case, figure this out later
    }

		//registers a double complex block with an item (tall plant but step is a fence and cap is a slab)
		private void doubleComplexBlockWithItem(DeferredBlock<?> deferredBlock) {
			String name = deferredBlock.getId().getPath();
			
			//var stemTexture = mcLoc("block/oak_planks"); //just an example texture, change as needed
			//var capTexture = mcLoc("block/oak_planks"); //just an example texture, change as needed

			var capTextureSide = modLoc("block/" + name + "_cap_side");
			var capTextureBottom = modLoc("block/" + name + "_cap_bottom");
			var capTextureTop = modLoc("block/" + name + "_cap_top");

			var stemTexture = modLoc("block/" + name + "_stem");
			
			ModelFile stem = models().fencePost(name + "_stem", stemTexture);
			ModelFile cap = models().slab(name + "_cap", capTextureSide, capTextureBottom, capTextureTop);


			getVariantBuilder(deferredBlock.get())
					.partialState().with(ExampleDoubleMushroomBlock.HALF, DoubleBlockHalf.LOWER).modelForState()
					.modelFile(stem).addModel()
					.partialState().with(ExampleDoubleMushroomBlock.HALF, DoubleBlockHalf.UPPER).modelForState()
					.modelFile(cap).addModel();

			//item
			itemModels().withExistingParent(name, "item/generated").texture("layer0", capTextureTop.toString()); //this will look like crap either way, we need separate texture made by hand or smth
		}

    //helper
		//cross
    ModelFile crossModel(String name) {
        return models()
        .cross(name, modLoc("block/" + name))
        .renderType("cutout");
    }


		private void blockWithComplexModel(DeferredBlock<?> deferredBlock, String modelName) {
			ModelFile modelFile = new ModelFile.UncheckedModelFile("mycologycraft:block/" + modelName);
			simpleBlockWithItem(deferredBlock.get(), modelFile);
		}


}

