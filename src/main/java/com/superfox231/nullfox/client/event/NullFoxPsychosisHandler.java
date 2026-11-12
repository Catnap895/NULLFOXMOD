package com.superfox231.nullfox.client.event;

import com.superfox231.nullfox.NullFox;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = NullFox.MODID)
public class NullFoxPsychosisHandler {

    private static final Random RANDOM = new Random();
    private static int globalTickCounter = 0;

    /*
     * ==========================================
     *  GAME THEORY ARG / LORE DECODER NOTE:
     *  Base64: VGhlIGZveCBpcyBub3QgaW4gdGhlIGdhbWUuIEl0IGlzIGluIHlvdXIgcmFtLg==
     *  Decoded: "The fox is not in the game. It is in your ram."
     * ==========================================
     */

    private static final String[] CORRUPTED_CONSOLE_ERRORS = {
            "§4[KERNEL PANIC] Segmentation fault at 0xDEADBEEF: Null_Fox has overwritten local stack memory.",
            "§c[SEVERE] Thread 'Server Watchdog' hung: Process blocked waiting for player input from ??_Entity.",
            "§4[FATAL] OpenAL Sound Engine failure: Audio buffer hijacked by synthesis stream 'SuperFox231'.",
            "§c[SECURITY] Unauthorized hook detected in net.minecraft.server.MinecraftServer. Saving state blocked."
    };

    private static final String[] PARANOIA_WHISPERS = {
            "§8Don't look at the chat box. Look at your webcam indicator light.",
            "§4The entity didn't spawn in the world. It spawned in your asset loader.",
            "§8Why is your inventory moving while your hands are off the keyboard?",
            "§4SuperFox231 tried to delete the repository. I saved a copy on your local drive.",
            "§8Listen closely. That clicking sound isn't your hard drive."
    };

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.level().isClientSide()) return;

            if (NullFoxDaisyBellEvent.isPlayerInEvent(serverPlayer) || SuperFoxGhostEncounter.isPlayerInEncounter(serverPlayer)) {
                return;
            }

            globalTickCounter++;

            // REDUCED FREQUENCY: Fires every ~90 seconds (1800 ticks)
            if (globalTickCounter % 1800 == 0) {
                ServerLevel level = (ServerLevel) serverPlayer.level();
                int eventRoll = RANDOM.nextInt(7); // 7 unique terrifying events

                switch (eventRoll) {
                    case 0:
                        // 1. Fake System Crash / Terminal Error
                        String error = CORRUPTED_CONSOLE_ERRORS[RANDOM.nextInt(CORRUPTED_CONSOLE_ERRORS.length)];
                        serverPlayer.sendSystemMessage(Component.literal(error));
                        level.playSound(null, serverPlayer.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.MASTER, 1.0F, 0.2F);
                        break;

                    case 1:
                        // 2. Psychological Whisper + Heavy Darkness & Confusion
                        String whisper = PARANOIA_WHISPERS[RANDOM.nextInt(PARANOIA_WHISPERS.length)];
                        serverPlayer.sendSystemMessage(Component.literal(whisper).withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
                        serverPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false, false));
                        serverPlayer.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0, false, false, false));
                        serverPlayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0, false, false, false));
                        level.playSound(null, serverPlayer.blockPosition(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.MASTER, 0.3F, 0.5F);
                        break;

                    case 2:
                        // 3. Phantom Stalker Ambush (Sculk particles & creepy fox screeches behind player)
                        Vec3 look = serverPlayer.getLookAngle();
                        double x = serverPlayer.getX() - (look.x * 3.0);
                        double z = serverPlayer.getZ() - (look.z * 3.0);
                        double y = serverPlayer.getY();
                        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y + 1.0, z, 8, 0.2, 0.5, 0.2, 0.01);
                        level.playSound(null, x, y, z, SoundEvents.FOX_SCREECH, SoundSource.HOSTILE, 0.9F, 0.2F);
                        break;

                    case 3:
                        // 4. Fake Disconnect Prompt Warning
                        serverPlayer.sendSystemMessage(Component.literal("§cInternal Exception: java.net.SocketException: Connection reset by peer (Host: ??_Entity)").withStyle(ChatFormatting.RED));
                        break;

                    case 4:
                        // NEW EVENT 5: Fake Player Join Notification (Gaslights them into thinking someone else joined their singleplayer world)
                        serverPlayer.server.getPlayerList().broadcastSystemMessage(Component.literal("§e??_Entity joined the game"), false);
                        level.playSound(null, serverPlayer.blockPosition(), SoundEvents.PLAYER_TELEPORT, SoundSource.MASTER, 1.0F, 0.5F);
                        break;

                    case 5:
                        // NEW EVENT 6: Self-Chat Imitation (Prints a message in chat as if the player typed it themselves, warning them)
                        serverPlayer.sendSystemMessage(Component.literal("<" + serverPlayer.getScoreboardName() + "> Why won't it let me close the game?").withStyle(ChatFormatting.GRAY));
                        level.playSound(null, serverPlayer.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.MASTER, 1.0F, 0.1F);
                        break;

                    case 6:
                        // NEW EVENT 7: Inventory Ghost Sound & Fake Item Name Drop
                        serverPlayer.sendSystemMessage(Component.literal("§4[WARNING] Slot 4 checksum mismatch. Item corrupted.").withStyle(ChatFormatting.RED));
                        level.playSound(null, serverPlayer.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 0.2F);
                        break;

                    case 7:
                        NullFoxFakeCrashHandler.executeFakeCrash(serverPlayer);
                        break;
                    case 8:
                        NullFoxInventoryCorruptor.corruptHotbar(serverPlayer);
                        break;
                    case 9:
                        com.superfox231.nullfox.world.NullFoxEnvironmentCorruption.corruptSurroundings(serverPlayer);
                        break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide()) {
            serverPlayer.sendSystemMessage(Component.literal("§8====================================================="));
            serverPlayer.sendSystemMessage(Component.literal("§4[CRITICAL WARNING] §7Instance accessed by unauthorized subject."));
            serverPlayer.sendSystemMessage(Component.literal("§cAuthor Signature Verified: SuperFox231 (DECEASED/CONTAINED)"));
            serverPlayer.sendSystemMessage(Component.literal("§8-----------------------------------------------------"));
            serverPlayer.sendSystemMessage(Component.literal("§4DATA NODE: §7You are running build version 0.0.0_null. Do not attempt to close the game window."));
            serverPlayer.sendSystemMessage(Component.literal("§8====================================================="));
        }
    }
}