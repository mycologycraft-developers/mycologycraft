package com.mycologycraft_devs.mycologycraft.datagen;

import com.mycologycraft_devs.mycologycraft.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;


//this is used to provide tags to items, similar to the block tags
//it provides data relating to game mechanics so its important
public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        //purely for the sake of example, this would be how to add a sword tag to the
        //example item
        //custom tags are possible and usefull
        tag(ItemTags.SWORDS).add(ModItems.EXAMPLE_ITEM.get());
    }
}
