package com.superfox231.nullfox.client.model;

import com.superfox231.nullfox.NullFox;
import com.superfox231.nullfox.entity.NullFoxEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NullFoxModel extends GeoModel<NullFoxEntity> {
    @Override
    public ResourceLocation getModelResource(NullFoxEntity animatable) {
        return new ResourceLocation(NullFox.MODID, "geo/null_fox.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NullFoxEntity animatable) {
        return new ResourceLocation(NullFox.MODID, "textures/entity/null_fox.png");
    }

    @Override
    public ResourceLocation getAnimationResource(NullFoxEntity animatable) {
        return new ResourceLocation(NullFox.MODID, "animations/null_fox.animation.json");
    }
}