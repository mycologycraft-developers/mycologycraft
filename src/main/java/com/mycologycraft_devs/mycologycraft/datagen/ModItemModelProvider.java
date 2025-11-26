package com.mycologycraft_devs.mycologycraft.datagen;

import com.mycologycraft_devs.mycologycraft.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


//this provides models for items
//KEEP IN MIND this will likely be pretty helper method heavy
//As a lot of vanilla functions for this are private (for some reason)
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }


    //register item models here
    @Override
    protected void registerModels() {
        //this will register a generic item model generated from a sprite, more complex
        //items models will require more stuff
        basicItem(ModItems.EXAMPLE_ITEM.get());
    }
}
