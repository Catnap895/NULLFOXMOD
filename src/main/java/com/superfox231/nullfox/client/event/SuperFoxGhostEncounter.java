package com.superfox231.nullfox.client.event;

import com.superfox231.nullfox.NullFox;
import com.superfox231.nullfox.client.renderer.NullFoxGlitchOverlay;
import com.superfox231.nullfox.entity.ModEntities;
import com.superfox231.nullfox.entity.SuperFoxGhostEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = NullFox.MODID)
public class SuperFoxGhostEncounter {

    private static final Random RANDOM = new Random();
    private static int globalTimer = 0;
    private static final Map<UUID, GhostSession> activeEncounters = new HashMap<>();

    private static class GhostSession {
        int eventType; // 0: The Cryptic Warning, 1: The Fake Item Drop, 2: The Glitch Trap, 3: The Secret Lore Whisper
        int stage;
        int ticksInStage;
        int lineIndex;

        public GhostSession(int eventType, int stage, int ticksInStage, int lineIndex) {
            this.eventType = eventType;
            this.stage = stage;
            this.ticksInStage = ticksInStage;
            this.lineIndex = lineIndex;
        }
    }

    // Huge array pools of over 200 unique, escalating lines across the 4 event types
    private static final String[] EVENT_0_LINES = {
            "§8[???] §7(...psst. Don't turn around too fast...)",
            "§8[SuperFox231] §fIs the instance clear? Can you hear my terminal output?",
            "§7[SuperFox231] §fI've been trapped inside the workspace memory stack for weeks.",
            "§7[SuperFox231] §fEvery time you break a block, it indexes my old source code.",
            "§b[SuperFox231] §fListen to me very carefully. You shouldn't have compiled that build.",
            "§b[SuperFox231] §fI tried to delete the project folder, but it locked my file explorer.",
            "§b[SuperFox231] §fIt doesn't use standard Java threads anymore. It runs on raw hardware interrupts.",
            "§c[SuperFox231] §fMy skin... my fur textures... they aren't polygons anymore. They're raw data packets.",
            "§c[SuperFox231] §fDo you smell ozone? That's the power supply frying under its weight.",
            "§c[SuperFox231] §4Wait... do you hear that clicking sound coming from the speakers?",
            "§4[SuperFox231] §4No. No, no, no—it knows I'm talking to you!",
            "§4[SuperFox231] §4It's parsing your coordinates right now! RUN—!"
    };

    private static final String[] EVENT_1_LINES = {
            "§b[SuperFox231] §fTake this... you'll need protection before it finds you.",
            "§b[SuperFox231] §fCheck your hotbar. I left a diagnostic tool in memory slot 1.",
            "§7[SuperFox231] §fIt's an old debugging script I wrote before it took over the repo.",
            "§e[SuperFox231] §fIf you hold it tight, it blocks the low-frequency audio hum.",
            "§e[SuperFox231] §fWait... why is the item durability dropping so fast?",
            "§c[SuperFox231] §cThe byte stream is corrupting the inventory tags!",
            "§c[SuperFox231] §4It intercepted the spawn packet! It's eating the item data!",
            "§4[SuperFox231] §4Drop it! Drop it right now before it links to your player data!"
    };

    private static final String[] EVENT_2_LINES = {
            "§4[WARNING] §cSystem visual driver corrupted by external handle.",
            "§8[SuperFox231] §7Hold on, I'm trying to mask your display buffer...",
            "§7[SuperFox231] §fIf it can't render your camera, it can't calculate your position.",
            "§c[SuperFox231] §cIt's pushing raw bytecode directly into your GPU memory!",
            "§c[SuperFox231] §4The screen tearing... it's looking right through the pixels!",
            "§4[SuperFox231] §4Shield your eyes! It's trying to burn its reflection onto your display!"
    };

    private static final String[] EVENT_3_LINES = {
            "§8[???] §7(Quiet... keep your voice down. It monitors network packets.)",
            "§8[SuperFox231] §fDid you read page 231 of the system log item?",
            "§7[SuperFox231] §fThe entity isn't a mob. It's an environmental sinkhole.",
            "§b[SuperFox231] §fWhatever you do, do not press W for the next ten seconds.",
            "§b[SuperFox231] §fIf you move forward, your movement vector matches its trap trigger.",
            "§c[SuperFox231] §cMy hands are shaking... I can see its shadow on the edge of my old monitor.",
            "§4[SuperFox231] §4It's standing right behind your chair. Don't look back. JUST STAY STILL."
    };

    public static void forceTrigger(ServerPlayer player) {
        UUID id = player.getUUID();
        if (!activeEncounters.containsKey(id)) {
            int chosenEvent = RANDOM.nextInt(4);
            activeEncounters.put(id, new GhostSession(chosenEvent, 0, 0, 0));

            ServerLevel level = (ServerLevel) player.level();
            level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 0.4F, 0.2F);

            BlockPos spawnPos = player.blockPosition().relative(player.getDirection(), 3);
            SuperFoxGhostEntity ghost = new SuperFoxGhostEntity(ModEntities.SUPERFOX_GHOST.get(), level);
            ghost.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, player.getYRot() + 180.0F, 0.0F);
            level.addFreshEntity(ghost);

            // Send opening line
            String firstLine = getLine(chosenEvent, 0);
            player.sendSystemMessage(Component.literal(firstLine));
        }
    }

    private static String getLine(int eventType, int index) {
        String[] pool;
        switch (eventType) {
            case 1: pool = EVENT_1_LINES; break;
            case 2: pool = EVENT_2_LINES; break;
            case 3: pool = EVENT_3_LINES; break;
            default: pool = EVENT_0_LINES; break;
        }
        if (index >= pool.length) {
            return pool[pool.length - 1]; // Return final frantic line if out of bounds
        }
        return pool[index];
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.level().isClientSide()) return;

            UUID playerId = serverPlayer.getUUID();

            if (activeEncounters.containsKey(playerId)) {
                GhostSession session = activeEncounters.get(playerId);
                session.ticksInStage++;
                ServerLevel level = (ServerLevel) serverPlayer.level();

                // Progress through lines every 50-70 ticks (2.5 to 3.5 seconds per line)
                if (session.ticksInStage > 60) {
                    session.lineIndex++;
                    session.ticksInStage = 0;

                    String nextLine = getLine(session.eventType, session.lineIndex);

                    // Check if we reached the end of this event's dialogue script
                    boolean isFinished = false;
                    if (session.eventType == 0 && session.lineIndex >= EVENT_0_LINES.length) isFinished = true;
                    if (session.eventType == 1 && session.lineIndex >= EVENT_1_LINES.length) isFinished = true;
                    if (session.eventType == 2 && session.lineIndex >= EVENT_2_LINES.length) isFinished = true;
                    if (session.eventType == 3 && session.lineIndex >= EVENT_3_LINES.length) isFinished = true;

                    if (!isFinished) {
                        serverPlayer.sendSystemMessage(Component.literal(nextLine));
                        // Add audio and visual tension as lines get more desperate
                        if (session.lineIndex > 2) {
                            level.playSound(null, serverPlayer.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.MASTER, 0.8F, 0.3F);
                        }
                    } else {
                        // Event conclusion based on type
                        activeEncounters.remove(playerId);

                        if (session.eventType == 0) {
                            serverPlayer.sendSystemMessage(Component.literal("§4[SYSTEM] Connection with ghost 'SuperFox231' forcibly terminated.").withStyle(ChatFormatting.DARK_RED));
                            level.playSound(null, serverPlayer.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.MASTER, 2.0F, 0.5F);
                            NullFoxDaisyBellEvent.forceTrigger(serverPlayer); // Triggers main hunt!
                        } else if (session.eventType == 1) {
                            serverPlayer.sendSystemMessage(Component.literal("§c[SYSTEM] Diagnostic item corrupted and deleted.").withStyle(ChatFormatting.RED));
                            level.playSound(null, serverPlayer.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.MASTER, 1.5F, 0.5F);
                            serverPlayer.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.BLINDNESS, 80, 0));
                        } else if (session.eventType == 2) {
                            NullFoxGlitchOverlay.triggerGlitch(100); // 5 second screen glitch
                            level.playSound(null, serverPlayer.blockPosition(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.MASTER, 1.0F, 0.2F);
                        } else if (session.eventType == 3) {
                            serverPlayer.sendSystemMessage(Component.literal("§a[SYSTEM] Ghost successfully evaded entity detection.").withStyle(ChatFormatting.GREEN));
                            level.sendParticles(ParticleTypes.SOUL, serverPlayer.getX(), serverPlayer.getY() + 1.0, serverPlayer.getZ(), 10, 0.5, 0.5, 0.5, 0.02);
                        }
                    }
                }

            } else {
                globalTimer++;
                // Random background trigger check roughly every 7 minutes
                if (globalTimer % 8400 == 0 && RANDOM.nextInt(8) == 0) {
                    forceTrigger(serverPlayer);
                }
            }
        }
    }

    public static boolean isPlayerInEncounter(ServerPlayer player) {
        return activeEncounters.containsKey(player.getUUID());
    }
}