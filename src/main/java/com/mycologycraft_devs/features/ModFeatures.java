package com.mycologycraft_devs.features;

import com.mycologycraft_devs.features.referenceClasses.MyceliaLavaLake;
import com.mycologycraft_devs.mycologycraft.MycologyCraft;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(
		BuiltInRegistries.FEATURE,
		MycologyCraft.MODID
	);

	public static final DeferredHolder<Feature<?>, MyceliaLavaLake> OVERWORLD_LAVA_LAKE_MAGMA_MYCELIUM = FEATURES.register(
		"magma_mycelium_lava_lake",
		() -> new MyceliaLavaLake(MyceliaLavaLake.Configuration.CODEC)
	);

	//method to pass the register to the main mod class
	public static void register(IEventBus eventBus){
		FEATURES.register(eventBus);
	}
}
