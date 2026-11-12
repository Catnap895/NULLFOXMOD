package com.superfox231.nullfox.client.event;

import com.superfox231.nullfox.NullFox;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

@Mod.EventBusSubscriber(modid = NullFox.MODID)
public class NullFoxDaisyBellEvent {

    private static final Random RANDOM = new Random();
    private static int globalTimer = 0;

    private static final Map<UUID, EventData> activeEvents = new HashMap<>();

    private static class EventData {
        boolean isWarningPhase;
        int ticksRemaining;
        double lockedX;
        double lockedY;
        double lockedZ;

        public EventData(boolean isWarningPhase, int ticksRemaining, double x, double y, double z) {
            this.isWarningPhase = isWarningPhase;
            this.ticksRemaining = ticksRemaining;
            this.lockedX = x;
            this.lockedY = y;
            this.lockedZ = z;
        }
    }

    public static boolean isPlayerInEvent(ServerPlayer player) {
        return activeEvents.containsKey(player.getUUID());
    }

    public static void forceTrigger(ServerPlayer player) {
        UUID playerId = player.getUUID();
        // 160 ticks = 8 seconds warning phase to hide
        activeEvents.put(playerId, new EventData(true, 160, player.getX(), player.getY(), player.getZ()));

        // Loud, jarring warning sound to force player attention
        ServerLevel level = (ServerLevel) player.level();
        level.playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.WARDEN_HEARTBEAT, SoundSource.MASTER, 2.0F, 0.5F);

        player.sendSystemMessage(Component.literal("§4[CRITICAL] A music box starts playing nearby... HIDE NOW!").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.level().isClientSide()) return;

            UUID playerId = serverPlayer.getUUID();

            if (activeEvents.containsKey(playerId)) {
                EventData data = activeEvents.get(playerId);
                data.ticksRemaining--;

                if (data.isWarningPhase) {
                    // --- PHASE 1: THE 8-SECOND WARNING TO HIDE ---
                    // Send big flashing title warning so they can't miss it
                    serverPlayer.connection.send(new net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket(5, 40, 5));
                    serverPlayer.connection.send(new net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket(Component.literal("§4FIND A HIDEAWAY").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)));
                    serverPlayer.connection.send(new net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket(Component.literal("§cIt is approaching. 8 seconds.").withStyle(ChatFormatting.YELLOW)));

                    serverPlayer.displayClientMessage(
                            Component.literal("§4[!] FIND A HIDEAWAY IMMEDIATELY. [!]").withStyle(ChatFormatting.RED, ChatFormatting.BOLD),
                            true
                    );

                    // When warning ends, start the 38-second locking song
                    if (data.ticksRemaining <= 0) {
                        ServerLevel level = (ServerLevel) serverPlayer.level();

                        SoundEvent daisyBellSound = SoundEvent.createVariableRangeEvent(new ResourceLocation(NullFox.MODID, "daisy_bell"));
                        level.playSound(null, serverPlayer.blockPosition(), daisyBellSound, SoundSource.RECORDS, 3.0F, 1.0F);

                        serverPlayer.sendSystemMessage(Component.literal("§5[AUDIO LOG] The song begins. DO NOT MOVE.").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));

                        // 760 ticks = 38 seconds song lockdown. Lock their exact current coordinates.
                        activeEvents.put(playerId, new EventData(false, 760, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()));
                    }

                } else {
                    // --- PHASE 2: THE 38-SECOND LOCKDOWN & KILL CHECK ---
                    serverPlayer.connection.send(new net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket(0, 25, 0));
                    serverPlayer.connection.send(new net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket(Component.literal("§4DO NOT MOVE").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD)));
                    serverPlayer.connection.send(new net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket(Component.literal("§cAny movement is fatal.").withStyle(ChatFormatting.RED)));

                    serverPlayer.displayClientMessage(
                            Component.literal("§4[!] DO NOT MOVE. IT IS HUNTING YOU. [!]").withStyle(ChatFormatting.RED, ChatFormatting.BOLD),
                            true
                    );

                    // Strict coordinate check (X, Y, and Z) to catch walking, jumping, or climbing
                    double currentX = serverPlayer.getX();
                    double currentY = serverPlayer.getY();
                    double currentZ = serverPlayer.getZ();

                    double distanceMovedSq = (currentX - data.lockedX) * (currentX - data.lockedX) +
                            (currentY - data.lockedY) * (currentY - data.lockedY) +
                            (currentZ - data.lockedZ) * (currentZ - data.lockedZ);

                    if (distanceMovedSq > 0.03D) {
                        // DEATH PUNISHMENT: They moved while the song played!
                        ServerLevel level = (ServerLevel) serverPlayer.level();

                        level.playSound(null, serverPlayer.blockPosition(), net.minecraft.sounds.SoundEvents.WARDEN_DEATH, SoundSource.MASTER, 3.0F, 0.2F);
                        serverPlayer.hurt(level.damageSources().generic(), Float.MAX_VALUE);
                        serverPlayer.sendSystemMessage(Component.literal("§4[KERNEL PANIC] Subject moved during transmission. Instance terminated by ??_Entity."));

                        activeEvents.remove(playerId);
                        return;
                    }

                    // Paralyze them so they physically cannot walk away easily
                    // serverPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 255, false, false, false));
                    // serverPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0, false, false, false));

                    // When the full 38 seconds elapse safely
                    if (data.ticksRemaining <= 0) {
                        activeEvents.remove(playerId);
                        serverPlayer.sendSystemMessage(Component.literal("§7[SYSTEM] The music stops. The entity loses your signal.").withStyle(ChatFormatting.DARK_GRAY));
                    }
                }
            } else {
                // Background random trigger check
                globalTimer++;
                if (globalTimer % 15000 == 0 && RANDOM.nextInt(8) == 0) {
                    forceTrigger(serverPlayer);
                }
            }
        }
    }
}