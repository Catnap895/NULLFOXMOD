package com.superfox231.nullfox.client.event;

import com.superfox231.nullfox.NullFox;
import com.superfox231.nullfox.util.NullFoxChatAI;
import com.superfox231.nullfox.util.NullFoxMemoryData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.TickEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = NullFox.MODID)
public class NullFoxWatcherEvent {
    private static int tickCounter = 0;
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;

            // Every ~3 minutes (3600 ticks), trigger ambient psychological horror on online players
            if (tickCounter >= 3600) {
                tickCounter = 0;

                for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                    ServerLevel serverLevel = player.serverLevel();
                    NullFoxMemoryData memory = NullFoxMemoryData.get(serverLevel);

                    // Increment observation days over time
                    memory.incrementDays();

                    // Randomly select a psychological horror manifestation
                    int horrorEventId = RANDOM.nextInt(3);

                    if (horrorEventId == 0) {
                        // 1. Phantom Audio: Play a faint heartbeat or cave ambience right at the player's position
                        serverLevel.playSound(null, player.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.HOSTILE, 0.5F, 0.4F);
                        player.sendSystemMessage(Component.literal("§8[Client] §7A low frequency hum vibrates through your speakers..."));
                    }
                    else if (horrorEventId == 1) {
                        // 2. Phantom Glitch Particles: Spawn smoke & portal dust right behind the player's head
                        double offsetX = (RANDOM.nextDouble() - 0.5) * 2.0;
                        double offsetZ = (RANDOM.nextDouble() - 0.5) * 2.0;
                        serverLevel.sendParticles(ParticleTypes.PORTAL, player.getX() + offsetX, player.getY() + 1.0, player.getZ() + offsetZ, 15, 0.1, 0.1, 0.1, 0.05);
                        player.sendSystemMessage(Component.literal("§4[??_Entity] §cI am closer than your render distance allows, " + player.getName().getString() + "."));
                    }
                    else if (horrorEventId == 2) {
                        // 3. Fake System Warning
                        player.sendSystemMessage(Component.literal("§4[System Warning] §cInstance memory index verified. Total tracked blocks: " + memory.getBlocksTracked()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String playerName = player.getName().getString();

        String rawMessage = event.getMessage().getString();
        ServerLevel serverLevel = player.serverLevel();
        NullFoxMemoryData memory = NullFoxMemoryData.get(serverLevel);

        // Scan message against your 200+ keyword responses
        String aiResponse = NullFoxChatAI.getResponse(rawMessage, playerName, memory);

        if (aiResponse != null) {
            event.setCanceled(true); // Suppress player chat message

            String entityTag = memory.hasConfronted() ? "<§4Null_Fox§r>" : "<§4??_Entity§r>";
            player.sendSystemMessage(Component.literal(entityTag + " " + aiResponse));
        }
    }
}