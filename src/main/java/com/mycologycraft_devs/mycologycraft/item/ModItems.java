package com.mycologycraft_devs.mycologycraft.item;

import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // Create a Deferred Register to hold Items which will all be registered under the "mycologycraft" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MycologyCraft.MODID);

    //create a simple food item (from example package)
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // example of how to register a separate item for a block
    //public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    //method to pass the register to the main mod class
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
