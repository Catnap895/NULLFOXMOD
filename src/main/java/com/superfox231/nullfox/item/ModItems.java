package com.superfox231.nullfox.item;

import com.superfox231.nullfox.NullFox;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NullFox.MODID);

    public static final DeferredItem<Item> SYSTEM_LOG = ITEMS.register("system_log",
            () -> new SystemLogItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WAFFLE_FRIES = ITEMS.register("waffle_fries",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8F).build())));

    // Trophy awarded for completing the event in time
    public static final DeferredItem<Item> NULL_FOX_TROPHY = ITEMS.register("null_fox_trophy",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> DIAGNOSTIC_TOOL = ITEMS.register("diagnostic_tool",
            () -> new DiagnosticToolItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}