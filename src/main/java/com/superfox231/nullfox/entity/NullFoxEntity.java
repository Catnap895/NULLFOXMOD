package com.superfox231.nullfox.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;

public class NullFoxEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final Random RANDOM = new Random();

    // 1. Define synced data accessors
    private static final EntityDataAccessor<Boolean> IS_BEEPING =
            SynchedEntityData.defineId(NullFoxEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_TWITCHING =
            SynchedEntityData.defineId(NullFoxEntity.class, EntityDataSerializers.BOOLEAN);

    private int twitchCooldown = 0;

    // RawAnimation matching your Blockbench animation names
    private static final RawAnimation BEEP_ANIMATION = RawAnimation.begin().thenPlay("animation.null_fox.beep");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.null_fox.idle");
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("animation.null_fox.walk");
    private static final RawAnimation TWITCH_ANIMATION = RawAnimation.begin().thenPlay("animation.null_fox.twitch");

    public NullFoxEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setCustomName(Component.literal("??_Entity"));
        this.setCustomNameVisible(true);
    }

    // 2. Register synced data defaults
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_BEEPING, false);
        this.entityData.define(IS_TWITCHING, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100000.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void tick() {
        super.tick();

        // Handle random spontaneous twitching on the server side when idle and standing still
        if (!this.level().isClientSide() && !this.isWalking() && !this.isBeeping()) {
            if (twitchCooldown > 0) {
                twitchCooldown--;
                if (twitchCooldown == 0) {
                    this.setTwitching(false);
                }
            } else {
                // Every ~4 seconds on average, roll a chance to trigger a twitch burst
                if (RANDOM.nextInt(80) == 0) {
                    this.setTwitching(true);
                    twitchCooldown = 30; // Lasts 30 ticks (1.5 seconds)
                }
            }
        }
    }

    // Helper method to check if the mob is currently walking/moving
    public boolean isWalking() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    // --- GeckoLib Animation Controller ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, event -> {
            if (this.isBeeping()) {
                return event.setAndContinue(BEEP_ANIMATION);
            }
            if (this.isWalking()) {
                return event.setAndContinue(WALK_ANIMATION);
            }
            if (this.isTwitching()) {
                return event.setAndContinue(TWITCH_ANIMATION);
            }

            // Default smooth idle animation
            return event.setAndContinue(IDLE_ANIMATION);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new NullFoxBehaviorGoal(this));
    }

    public boolean isBeeping() {
        return this.entityData.get(IS_BEEPING);
    }

    public void setBeeping(boolean beeping) {
        this.entityData.set(IS_BEEPING, beeping);
    }

    public boolean isTwitching() {
        return this.entityData.get(IS_TWITCHING);
    }

    public void setTwitching(boolean twitching) {
        this.entityData.set(IS_TWITCHING, twitching);
    }
}