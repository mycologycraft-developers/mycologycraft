package com.mycologycraft_devs.mycologycraft.datagen.grouped;

import com.mycologycraft_devs.mycologycraft.block.ModBlocks;
import com.mycologycraft_devs.mycologycraft.datagen.helpers.DataGenHelper;
import com.mycologycraft_devs.mycologycraft.datagen.helpers.LootTableProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.registries.DeferredBlock;

public class BigMushroomData {
	public static void GenerateBlocktags(BlockStateProvider provider) {
		bigMushroomBlocks(provider, ModBlocks.EXAMPLE_MUSHROOM_STEM, ModBlocks.EXAMPLE_MUSHROOM_BLOCK);
	}

	public static void GenerateLootTables(LootTableProvider provider) {
		provider.dropSelf(ModBlocks.EXAMPLE_MUSHROOM_STEM.get());
		provider.dropSelf(ModBlocks.EXAMPLE_MUSHROOM_BLOCK.get());
	}

	private static void bigMushroomBlocks(BlockStateProvider provider, DeferredBlock<?> stemBlock, DeferredBlock<?> capBlock) {
		String stemModelName = stemBlock.getId().getPath();
		String capModelName = capBlock.getId().getPath();
		String capInsideModelName = capBlock.getId().getPath() + "_inside";
		String stemInventoryModelName = stemBlock.getId().getPath() + "_inventory";
		String capInventoryModelName = capBlock.getId().getPath() + "_inventory";

		ResourceLocation parentTexture = provider.mcLoc("block/template_single_face");
		ResourceLocation stemTexture = provider.blockTexture(stemBlock.get());
		ResourceLocation capTexture = provider.blockTexture(capBlock.get());
		ResourceLocation insideTexture = DataGenHelper.asResource("block/" + capInsideModelName);

		provider.simpleBlockItem(stemBlock.get(), new ModelFile.UncheckedModelFile(DataGenHelper.asResource(stemInventoryModelName)));
		provider.simpleBlockItem(capBlock.get(), new ModelFile.UncheckedModelFile(DataGenHelper.asResource(capInventoryModelName)));

		BlockModelBuilder stemModel = provider.models().singleTexture(stemModelName, parentTexture, stemTexture);
		BlockModelBuilder capModel = provider.models().singleTexture(capModelName, parentTexture, capTexture);
		BlockModelBuilder insideModel = provider.models().singleTexture(capInsideModelName, parentTexture, insideTexture);
		provider.models().cubeAll(stemInventoryModelName, stemTexture);
		provider.models().cubeAll(capInventoryModelName, capTexture);

		MultiPartBlockStateBuilder[] builders = {
			provider.getMultipartBuilder(stemBlock.get()),
			provider.getMultipartBuilder(capBlock.get())
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
}
