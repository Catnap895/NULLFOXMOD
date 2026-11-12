package com.superfox231.nullfox.entity;

import com.superfox231.nullfox.client.renderer.NullFoxGlitchOverlay;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SuperFoxGhostEntity extends PathfinderMob implements GeoEntity {

    // CRITICAL: Initialize the cache directly so it is never null
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int lifetimeTicks = 700; // 30 seconds lifespan

    public SuperFoxGhostEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setNoAi(false);
        this.setInvulnerable(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.superfox_ghost.idle"));
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache; // Will now return a valid, initialized cache!
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (this.random.nextFloat() < 0.4f) {
                this.level().addParticle(ParticleTypes.SOUL,
                        this.getRandomX(0.5D), this.getRandomY() + 0.5D, this.getRandomZ(0.5D),
                        0.0D, 0.03D, 0.0D);
            }
        } else {
            ServerLevel serverLevel = (ServerLevel) this.level();
            Player target = serverLevel.getNearestPlayer(this, 16.0D);

            if (target instanceof ServerPlayer serverPlayer) {
                this.getLookControl().setLookAt(serverPlayer, 30.0F, 30.0F);
                this.getNavigation().moveTo(serverPlayer, 1.0D);

                if (this.distanceToSqr(serverPlayer) < 6.25D) {
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false, false));
                    NullFoxGlitchOverlay.triggerGlitch(25);
                    serverLevel.playSound(null, this.blockPosition(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE, 0.8F, 0.5F);
                }
            }

            lifetimeTicks--;
            if (lifetimeTicks <= 0) {
                serverLevel.sendParticles(ParticleTypes.POOF, this.getX(), this.getY() + 1.0, this.getZ(), 20, 0.3, 0.6, 0.3, 0.02);
                this.discard();
            }
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false;
    }
}