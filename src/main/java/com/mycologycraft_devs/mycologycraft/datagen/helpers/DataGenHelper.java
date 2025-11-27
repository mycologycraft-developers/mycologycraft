package com.mycologycraft_devs.mycologycraft.datagen.helpers;

import com.mycologycraft_devs.mycologycraft.MycologyCraft;

import net.minecraft.resources.ResourceLocation;

public class DataGenHelper {
	public static ResourceLocation asResource(String id) {
		return ResourceLocation.fromNamespaceAndPath(MycologyCraft.MODID, id);
	}
}
