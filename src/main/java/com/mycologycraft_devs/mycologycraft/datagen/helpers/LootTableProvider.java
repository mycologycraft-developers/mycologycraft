package com.mycologycraft_devs.mycologycraft.datagen.helpers;

import java.util.Set;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public abstract class LootTableProvider extends BlockLootSubProvider {
	protected LootTableProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, Provider registries) {
		super(explosionResistant, enabledFeatures, registries);
	}

	public void dropSelf(Block block) {
		super.dropSelf(block);
	}
}
