package com.superfox231.nullfox.world;

import com.superfox231.nullfox.entity.ModEntities;
import com.superfox231.nullfox.entity.NullFoxBossEntity;
import com.superfox231.nullfox.entity.NullFoxEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

public class NullFoxJumpscareEvent {

    /**
     * Executes the face-flash psychological jumpscare directly on the target player.
     */
    public static void triggerRandomJumpscare(ServerPlayer player) {
        if (player.level().isClientSide()) return;

        ServerLevel level = (ServerLevel) player.level();

        Vec3 lookVec = player.getLookAngle();
        Vec3 eyePos = player.getEyePosition();

        // Position entity 1.0 block in front of the player's camera
        Vec3 spawnPos = eyePos.add(lookVec.scale(1.0D));

        NullFoxEntity flashEntity = new NullFoxEntity(ModEntities.NULL_FOX.get(), level);

        // Position entity so eye-level matches camera vector and face points back at player
        flashEntity.moveTo(
                spawnPos.x,
                spawnPos.y - 1.5D,
                spawnPos.z,
                player.getYRot() + 180.0F,
                -player.getXRot()
        );

        flashEntity.setCustomName(Component.literal("§4[0x231: SYSTEM_CRASH]"));
        flashEntity.setCustomNameVisible(false);
        flashEntity.setNoAi(true); // Freeze AI so it stays locked in screen frame

        // Force add entity to server level
        level.addFreshEntity(flashEntity);

        // Instant screeching audio combination right at eye level
        level.playSound(null, player.blockPosition(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE, 3.0F, 0.2F);
        level.playSound(null, player.blockPosition(), SoundEvents.WARDEN_ROAR, SoundSource.HOSTILE, 2.0F, 0.4F);

        // Short darkness burst to disorient the player after the flash
        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20, 0, false, false, false));

        // Keep entity alive for 15 ticks (~0.75 sec) to allow full render before despawning
        level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + 15, () -> {
            if (flashEntity.isAlive()) {
                level.sendParticles(ParticleTypes.POOF,
                        flashEntity.getX(), flashEntity.getY() + 1.0, flashEntity.getZ(),
                        15, 0.2, 0.4, 0.2, 0.05);
                flashEntity.discard();
            }
        }));
    }
}