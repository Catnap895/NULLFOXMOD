package com.superfox231.nullfox.client.event;

import com.superfox231.nullfox.NullFox;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class NullFoxFakeCrashHandler {

    public static void executeFakeCrash(ServerPlayer player) {
        if (player.level().isClientSide()) return;

        // Apply blindness and weakness to freeze the player in place
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 140, 255, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 140, 255, false, false, false));

        // Play loud crash/corrupt sound effects
        player.level().playSound(null, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.MASTER, 2.0F, 0.1F);
        player.level().playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.MASTER, 2.0F, 0.2F);

        // Print fake stack trace directly into chat
        player.sendSystemMessage(Component.literal("§4[FATAL EXCEPTION] Unhandled thread interrupt in com.superfox231.nullfox.InstancePruner"));
        player.sendSystemMessage(Component.literal("§c  at com.superfox231.nullfox.NullFoxEntity.harvest(NullFoxEntity.java:231)"));
        player.sendSystemMessage(Component.literal("§c  at com.superfox231.nullfox.SoulBinder.bindPlayer(SoulBinder.java:404)"));
        player.sendSystemMessage(Component.literal("§c  at net.minecraft.server.MinecraftServer.tickChildren(MinecraftServer.java:872)"));
        player.sendSystemMessage(Component.literal("§4[SYSTEM] Purging player process: " + player.getScoreboardName() + "..."));

        // Trigger full screen glitch overlay
        com.superfox231.nullfox.client.renderer.NullFoxGlitchOverlay.triggerGlitch(120);
    }
}