package com.superfox231.nullfox.client.renderer;

import com.superfox231.nullfox.NullFox;
import com.superfox231.nullfox.entity.NullFoxBossEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NullFoxBossRenderer extends GeoEntityRenderer<NullFoxBossEntity> {

    public NullFoxBossRenderer(EntityRendererProvider.Context renderManager) {
        // Automatically links to:
        // Model: assets/nullfox/geo/null_fox_boss.geo.json
        // Texture: assets/nullfox/textures/entity/null_fox_boss.png
        // Animation: assets/nullfox/animations/null_fox_boss.animation.json
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(NullFox.MODID, "null_fox_boss")));
        this.shadowRadius = 0.7F;
    }

    @Override
    public RenderType getRenderType(NullFoxBossEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        // Uses translucent render type to allow ghostly glitch flashes and entity transparency
        return RenderType.entityTranslucent(texture);
    }
}