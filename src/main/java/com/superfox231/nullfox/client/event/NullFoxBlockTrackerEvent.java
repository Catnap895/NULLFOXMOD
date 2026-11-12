package com.superfox231.nullfox.client.event;

import com.superfox231.nullfox.NullFox;
import com.superfox231.nullfox.util.NullFoxMemoryData;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.level.BlockEvent;

@Mod.EventBusSubscriber(modid = NullFox.MODID)
public class NullFoxBlockTrackerEvent {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            NullFoxMemoryData memory = NullFoxMemoryData.get(serverLevel);
            memory.addBlockActivity();
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            NullFoxMemoryData memory = NullFoxMemoryData.get(serverLevel);
            memory.addBlockActivity();
        }
    }
}