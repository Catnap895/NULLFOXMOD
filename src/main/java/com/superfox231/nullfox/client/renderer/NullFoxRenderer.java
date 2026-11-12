package com.superfox231.nullfox.client.renderer;

import com.superfox231.nullfox.client.model.NullFoxModel;
import com.superfox231.nullfox.entity.NullFoxEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NullFoxRenderer extends GeoEntityRenderer<NullFoxEntity> {
    public NullFoxRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NullFoxModel());
        this.shadowRadius = 0.3f;
    }
}