package com.mycologycraft_devs.mycologycraft.datagen;

import com.mycologycraft_devs.mycologycraft.block.ModBlocks;
import com.mycologycraft_devs.mycologycraft.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;


//this class can generate recipies
//theres some weird nuances to this, which i will explain throughout

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }


    //create recipes within this
    protected void buildRecipes(RecipeOutput recipeOutput) {
        //this is a smelting recipe, put in an item thats in the list to get the items in the recipies below
        //it requires a list, feel free to look into changing that hin the helper methods
        List<ItemLike> EXAMPLE_SMELTABLES = List.of(Items.BEEHIVE, Items.BONE);

        oreSmelting(recipeOutput, EXAMPLE_SMELTABLES, RecipeCategory.MISC, ModItems.EXAMPLE_ITEM.get(), 0.25f, 200, "example");
        oreBlasting(recipeOutput, EXAMPLE_SMELTABLES, RecipeCategory.MISC, ModItems.EXAMPLE_ITEM.get(), 0.25f, 200, "example");

        //example shaped recipe, follow the notes below
        //the RecipeCategory tells it where to put it in the vanilla recipe book
        //the block/item is the output
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.EXAMPLE_BLOCK.get())
                //create a pattern as follows, with letters for the items and spaces for empty space
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                //define the letters you use here to represent items, repeat the define for more types
                .define('E', ModItems.EXAMPLE_ITEM.get())
                //unlockedby determines what unlocks this recipie in the book
                //here its holding the example item
                //the name (afaik) is unimportant
                .unlockedBy("has_crystalfragment", has(ModItems.EXAMPLE_ITEM))
                //save(recipeOutput) is supposed to be enough, but ive had issues not defining a place to save it
                //just provide a save location like below to be safe
                .save(recipeOutput,"mycologycraft:example_item_to_block");

        //example for shapeless recipies, much more self explanatory
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXAMPLE_ITEM.get(), 9)
                .requires(ModBlocks.EXAMPLE_BLOCK.get())
                .unlockedBy("has_CrystalShardBlock", has(ModBlocks.EXAMPLE_BLOCK)).save(recipeOutput,"alkahistorycraft:crystal_block_to_frag");
    }

    //helper functions for smelting/blasting recipes
    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }
}
