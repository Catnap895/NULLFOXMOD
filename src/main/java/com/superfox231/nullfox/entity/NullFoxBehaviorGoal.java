package com.superfox231.nullfox.entity;

import com.superfox231.nullfox.item.ModItems;
import com.superfox231.nullfox.item.SystemLogItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class NullFoxBehaviorGoal extends Goal {
    private final NullFoxEntity mob;
    private Player targetPlayer;
    private int phaseTimer = 0;
    private boolean hasDroppedBook = false;

    public NullFoxBehaviorGoal(NullFoxEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Find the closest player within 16 blocks
        this.targetPlayer = this.mob.level().getNearestPlayer(this.mob, 16.0D);
        return this.targetPlayer != null;
    }

    @Override
    public void start() {
        this.phaseTimer = 0;
        this.hasDroppedBook = false;
        this.mob.setBeeping(false);
    }

    @Override
    public void tick() {
        if (this.targetPlayer == null || !this.targetPlayer.isAlive()) return;

        // 1. Always stare directly at the player
        this.mob.getLookControl().setLookAt(this.targetPlayer, 30.0F, 30.0F);
        this.phaseTimer++;

        double distance = this.mob.distanceToSqr(this.targetPlayer);

        // 2. Walk slowly toward the player if farther than 4 blocks
        if (distance > 16.0D) {
            this.mob.getNavigation().moveTo(this.targetPlayer, 1.0D);
            this.mob.setBeeping(false);
        } else {
            // Stop moving close up, lock position, and trigger binary behavior
            this.mob.getNavigation().stop();
            this.mob.setBeeping(true);

            // Play binary chime sound effect and send warning at phase 20 (~1 second in)
            if (this.phaseTimer == 20 && !this.mob.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) this.mob.level();

                // Play a creepy note/glitch sound (e.g., sculk shriek or block ambient)
                serverLevel.playSound(null, this.mob.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.HOSTILE, 1.0F, 0.5F);

                // Send eerie message to target player
                this.targetPlayer.sendSystemMessage(Component.literal("§4[??_Entity] §coi01001110 01010101 01001100... Take it."));
            }

            // 3. Drop the System_Log book at phase 60 (~3 seconds in)
            if (this.phaseTimer == 60 && !this.hasDroppedBook && !this.mob.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) this.mob.level();

                // Drop System_Log item at the mob's feet
                ItemStack logBook = new ItemStack(ModItems.SYSTEM_LOG.get());
                ItemEntity itemEntity = new ItemEntity(serverLevel, this.mob.getX(), this.mob.getY(), this.mob.getZ(), logBook);
                serverLevel.addFreshEntity(itemEntity);

                this.hasDroppedBook = true;
            }

            // 4. Vanish into smoke particles and remove entity at phase 100 (~5 seconds total)
            if (this.phaseTimer >= 100 && !this.mob.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) this.mob.level();

                // Spawn portal/smoke vanish particles
                serverLevel.sendParticles(ParticleTypes.PORTAL, this.mob.getX(), this.mob.getY() + 1.0D, this.mob.getZ(), 30, 0.5, 1.0, 0.5, 0.1);
                serverLevel.playSound(null, this.mob.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 0.2F);

                // Disappear permanently
                this.mob.discard();
            }
        }
    }

    @Override
    public void stop() {
        this.mob.setBeeping(false);
        this.phaseTimer = 0;
    }
}