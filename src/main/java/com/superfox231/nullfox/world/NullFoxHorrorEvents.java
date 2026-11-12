package com.superfox231.nullfox.world;

import com.superfox231.nullfox.entity.ModEntities;
import com.superfox231.nullfox.entity.SuperFoxGhostEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class NullFoxHorrorEvents {

    /**
     * Triggers the psychological horror event on a specific player.
     * Sends system intercept messages, plays eerie audio, and silently spawns a stalking ghost behind them.
     */
    public static void triggerForcedRebindEvent(ServerPlayer player) {
        if (player.level().isClientSide()) return;

        ServerLevel level = (ServerLevel) player.level();
        RandomSource random = level.getRandom();

        // 1. Send narrative system intercept messages simulating a compromised thread
        player.sendSystemMessage(Component.literal("§4[System Error]: Thread allocation failure on player UUID."));
        player.sendSystemMessage(Component.literal("§b[SuperFox231]: ...Why are you still moving? Stop walking that way, it's watching your input stream—"));
        player.sendSystemMessage(Component.literal("§4[Null_Fox]: OVERRIDE: Input mapping reallocated to sector 0x231."));

        // 2. Play unsettling hard-drive scratching and heartbeat audio cues
        level.playSound(null, player.blockPosition(), SoundEvents.SCULK_CLICKING, SoundSource.HOSTILE, 1.5F, 0.1F);
        level.playSound(null, player.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.HOSTILE, 1.0F, 0.2F);

        // 3. Spawn the stalking ghost entity directly behind the player's back
        BlockPos behindPos = player.blockPosition().relative(player.getDirection().getOpposite(), 4);
        SuperFoxGhostEntity ghost = new SuperFoxGhostEntity(ModEntities.SUPERFOX_GHOST.get(), level);

        ghost.moveTo(behindPos.getX() + 0.5D, player.getY(), behindPos.getZ() + 0.5D, player.getYRot(), 0.0F);

        // Give it a corrupted terminal display name
        ghost.setCustomName(Component.literal("§4??_Process_0x231"));
        ghost.setCustomNameVisible(true);

        level.addFreshEntity(ghost);
    }
}