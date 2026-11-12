package com.superfox231.nullfox.client.event;

import com.superfox231.nullfox.NullFox;
import com.superfox231.nullfox.client.renderer.NullFoxRenderer;
import com.superfox231.nullfox.client.renderer.SuperFoxGhostRenderer;
import com.superfox231.nullfox.client.renderer.NullFoxBossRenderer;
import com.superfox231.nullfox.entity.ModEntities;
import com.superfox231.nullfox.entity.NullFoxBossEntity;
import com.superfox231.nullfox.entity.NullFoxEntity;
import com.superfox231.nullfox.entity.SuperFoxGhostEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod.EventBusSubscriber(modid = NullFox.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.NULL_FOX.get(), NullFoxEntity.createAttributes().build());
        event.put(ModEntities.SUPERFOX_GHOST.get(), SuperFoxGhostEntity.createAttributes().build());
        event.put(ModEntities.NULL_FOX_BOSS.get(), NullFoxBossEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SUPERFOX_GHOST.get(), SuperFoxGhostRenderer::new);

        // CRITICAL FIX: Register your boss entity renderer here so it isn't null!
        event.registerEntityRenderer(ModEntities.NULL_FOX_BOSS.get(), NullFoxBossRenderer::new);
    }
}