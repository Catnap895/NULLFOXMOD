package com.superfox231.nullfox.entity;

import com.superfox231.nullfox.NullFox;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static software.bernie.example.registry.EntityRegistry.ENTITIES;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, NullFox.MODID);

    public static final Supplier<EntityType<NullFoxEntity>> NULL_FOX =
            ENTITY_TYPES.register("null_fox", () -> EntityType.Builder.of(NullFoxEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f) // Entity hitbox dimensions (fox size / tall entity)
                    .build("null_fox"));
    public static final Supplier<EntityType<SuperFoxGhostEntity>> SUPERFOX_GHOST =
            ENTITY_TYPES.register("superfox_ghost", () -> EntityType.Builder.of(SuperFoxGhostEntity::new, MobCategory.MISC)
                    .sized(0.6f, 1.8f)
                    .build("superfox_ghost"));
    public static final Supplier<EntityType<NullFoxBossEntity>> NULL_FOX_BOSS = ENTITIES.register("null_fox_boss",
            () -> EntityType.Builder.of(NullFoxBossEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F)
                    .fireImmune()
                    .build("null_fox_boss")
    );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}