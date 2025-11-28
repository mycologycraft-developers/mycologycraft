package com.mycologycraft_devs.mycologycraft.datagen.grouped;

import com.mycologycraft_devs.mycologycraft.block.ModBlocks;
import com.mycologycraft_devs.mycologycraft.datagen.helpers.DataGenHelper;
import com.mycologycraft_devs.mycologycraft.datagen.helpers.LootTableProvider;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredBlock;

public class MushroomData {
	public static void GenerateBlocktags(BlockStateProvider provider) {
		mushroomBlocks(provider, ModBlocks.EXAMPLE_MUSHROOM_ONE);
	}

	public static void GenerateLootTables(LootTableProvider provider) {
		provider.dropSelf(ModBlocks.EXAMPLE_MUSHROOM_ONE.get());
	}

	private static void mushroomBlocks(BlockStateProvider provider, DeferredBlock<?> block) {
		ResourceLocation resourceLocation = DataGenHelper.asResource("block/" + block.getId().getPath());

		provider.models().cross(resourceLocation.toString(), resourceLocation);

		provider.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(resourceLocation));
	}
}
