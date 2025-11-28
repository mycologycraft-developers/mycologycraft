package com.mycologycraft_devs.mycologycraft;

import com.mycologycraft_devs.mycologycraft.block.ModBlocks;
import com.mycologycraft_devs.mycologycraft.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MycologyCraft.MODID);

    //this function constructs the creative tab
    public static final Supplier<CreativeModeTab> MYCOLOGYCRAFT_CREATIVE_TAB = CREATIVE_MODE_TAB.register("mycology_items",
            () -> CreativeModeTab.builder().icon(()->new ItemStack(Items.RED_MUSHROOM)).title(Component.translatable("creativetab.mycologycraft.mycology_items"))
                    .displayItems((ItemDisplayParameters, output) -> {

                        //follow this template for adding items/blocks to the creative menu
                        output.accept(ModItems.EXAMPLE_ITEM);
                        output.accept(ModBlocks.EXAMPLE_BLOCK);
                        output.accept(ModBlocks.MUSHROOM_SPAWNING_BLOCK);
                        output.accept(ModBlocks.FEATURELESS_DOUBLE_MUSHROOM_BLOCK);
                    }).build());

    //    Alternate method that can be used to add an item from elsewhere
    //    This is from the project template and will need modification to make work
    //    But its probably worth keeping around
    //
    //    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    //        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
    //            event.accept(EXAMPLE_BLOCK_ITEM);
    //        }
    //    }

    //method to pass the creative tabs data to the main class
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
