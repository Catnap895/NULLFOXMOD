package com.superfox231.nullfox.client.renderer;

import com.superfox231.nullfox.NullFox;
import com.superfox231.nullfox.entity.SuperFoxGhostEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SuperFoxGhostRenderer extends GeoEntityRenderer<SuperFoxGhostEntity> {

    public SuperFoxGhostRenderer(EntityRendererProvider.Context renderManager) {
        // Automatically links to:
        // Model: assets/nullfox/geo/superfox_ghost.geo.json
        // Texture: assets/nullfox/textures/entity/superfox_ghost.png
        // Animation: assets/nullfox/animations/superfox_ghost.animation.json
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(NullFox.MODID, "superfox_ghost")));
        this.shadowRadius = 0.3F;
    }

    @Override
    public RenderType getRenderType(SuperFoxGhostEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        // Uses a translucent/ghostly render layer so it appears eerie and see-through
        return RenderType.entityTranslucent(texture);
    }
}