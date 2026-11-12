package com.superfox231.nullfox.client.event;

import com.superfox231.nullfox.NullFox;
import com.superfox231.nullfox.entity.ModEntities;
import com.superfox231.nullfox.entity.NullFoxEntity;
import com.superfox231.nullfox.item.ModItems;
import com.superfox231.nullfox.util.NullFoxMemoryData;
import com.superfox231.nullfox.world.NullFoxJumpscareEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent; // Correct 1.20.4 import
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks; // Used to get server instance

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = NullFox.MODID)
public class WaffleFriesEvent {
    private static final Random RANDOM = new Random();

    // Event State Tracking
    private static boolean isEventActive = false;
    private static int timerTicks = 0;
    private static ServerPlayer targetPlayer = null;
    private static NullFoxEntity spawnedNullFox = null;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        // Run only on END phase to prevent ticking twice per game loop
        if (event.phase != TickEvent.Phase.END) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        // --- 1. Random Trigger Check ---
        if (!isEventActive) {
            // ~1 in 15,000 chance per tick (~12 minutes on average when playing)
            if (RANDOM.nextInt(15000) == 0 && !server.getPlayerList().getPlayers().isEmpty()) {
                ServerPlayer player = server.getPlayerList().getPlayers().get(0);
                startWaffleFriesEvent(player);
            }
            return;
        }

        // --- 2. Active Event Timer ---
        if (isEventActive && targetPlayer != null && spawnedNullFox != null && spawnedNullFox.isAlive()) {
            timerTicks++;

            // Warning callouts in chat at 10 seconds and 5 seconds remaining
            if (timerTicks == 200) {
                targetPlayer.sendSystemMessage(Component.literal("§4[??_Entity] §c10 seconds remaining, " + targetPlayer.getName().getString() + ". Where are my waffle fries?"));
            } else if (timerTicks == 300) {
                targetPlayer.sendSystemMessage(Component.literal("§4[??_Entity] §c5 SECONDS. RUN."));
            }

            // --- 3. Fail State: 20 Seconds Expired (400 ticks) ---
            if (timerTicks >= 400) {
                triggerJumpscareKill(targetPlayer, spawnedNullFox);
                resetEvent();
            }
        } else if (spawnedNullFox != null && !spawnedNullFox.isAlive()) {
            // Reset if entity was somehow killed or discarded early
            resetEvent();
        }
    }

    private static void startWaffleFriesEvent(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        BlockPos playerPos = player.blockPosition();

        // Step A: Search for nearby containers (Chests, Barrels) within a 20-block radius
        List<Container> nearbyContainers = new ArrayList<>();
        BlockPos.betweenClosedStream(playerPos.offset(-20, -5, -20), playerPos.offset(20, 5, 20)).forEach(pos -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof Container container) {
                nearbyContainers.add(container);
            }
        });

        // Step B: Seed Waffle Fries into a random container if available
        if (!nearbyContainers.isEmpty()) {
            Container chosenContainer = nearbyContainers.get(RANDOM.nextInt(nearbyContainers.size()));
            int slot = RANDOM.nextInt(chosenContainer.getContainerSize());
            chosenContainer.setItem(slot, new ItemStack(ModItems.WAFFLE_FRIES.get(), 1));
        } else {
            // Fallback: Drop waffle fries on the ground near the player if no chests exist
            player.spawnAtLocation(new ItemStack(ModItems.WAFFLE_FRIES.get(), 1));
        }

        // Step C: Spawn Null_Fox near the player
        NullFoxEntity nullFox = ModEntities.NULL_FOX.get().create(level);
        BlockPos spawnPos = playerPos.offset(RANDOM.nextInt(5) - 2, 0, RANDOM.nextInt(5) - 2);
        nullFox.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0.0F, 0.0F);
        level.addFreshEntity(nullFox);

        // Particle effect and creepy spawn sound
        level.sendParticles(ParticleTypes.PORTAL, nullFox.getX(), nullFox.getY() + 1.0, nullFox.getZ(), 40, 0.5, 1.0, 0.5, 0.1);
        level.playSound(null, spawnPos, SoundEvents.WARDEN_HEARTBEAT, SoundSource.HOSTILE, 1.5F, 0.4F);

        // Step D: Initialize State & Broadcast Chat Requirement
        isEventActive = true;
        timerTicks = 0;
        targetPlayer = player;
        spawnedNullFox = nullFox;

        NullFoxMemoryData memory = NullFoxMemoryData.get(level);
        String entityTag = memory.hasConfronted() ? "<§4Null_Fox§r>" : "<§4??_Entity§r>";
        player.sendSystemMessage(Component.literal(entityTag + " §cI require §lWAFFLE FRIES§r§c. You have 20 SECONDS to retrieve them from your storage."));
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        // --- 4. Success State: Handing over Waffle Fries ---
        if (isEventActive && event.getTarget() == spawnedNullFox && event.getEntity() instanceof ServerPlayer player) {
            ItemStack heldItem = player.getItemInHand(event.getHand());

            if (heldItem.is(ModItems.WAFFLE_FRIES.get())) {
                event.setCanceled(true);
                heldItem.shrink(1); // Consume the item

                ServerLevel level = player.serverLevel();

                // Reward Player: Spawn Trophy at feet
                player.spawnAtLocation(new ItemStack(ModItems.NULL_FOX_TROPHY.get(), 1));

                // Gratitude message & Vanish
                NullFoxMemoryData memory = NullFoxMemoryData.get(level);
                String entityTag = memory.hasConfronted() ? "<§4Null_Fox§r>" : "<§4??_Entity§r>";
                player.sendSystemMessage(Component.literal(entityTag + " §aRequirement satisfied. Take your trophy... for now."));

                // Particle Vanish Effect
                level.sendParticles(ParticleTypes.END_ROD, spawnedNullFox.getX(), spawnedNullFox.getY() + 1.0, spawnedNullFox.getZ(), 50, 0.5, 1.0, 0.5, 0.2);
                level.playSound(null, spawnedNullFox.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 0.5F);

                spawnedNullFox.discard();
                resetEvent();
            }
        }
    }

    private static void triggerJumpscareKill(ServerPlayer player, NullFoxEntity nullFox) {
        ServerLevel level = player.serverLevel();

        // 1. Calculate vector directly behind the player's back
        double lookAngle = Math.toRadians(player.getYRot());
        double backX = player.getX() + Math.sin(lookAngle) * 1.5;
        double backZ = player.getZ() - Math.cos(lookAngle) * 1.5;

        // 2. Teleport behind player
        nullFox.teleportTo(backX, player.getY(), backZ);
        nullFox.getLookControl().setLookAt(player, 180.0F, 180.0F);

        // 3. Audio / Visual Jumpscare
        level.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 2.0F, 0.1F);
        level.sendParticles(ParticleTypes.SQUID_INK, player.getX(), player.getY() + 1.0, player.getZ(), 100, 0.5, 1.0, 0.5, 0.5);
        NullFoxJumpscareEvent.triggerRandomJumpscare(player);

        // 4. One-shot kill with custom death message
        player.hurt(level.damageSources().fellOutOfWorld(), 9999.0F);
        eventServerMessage(level, "§4" + player.getName().getString() + " failed to feed Null_Fox waffle fries in time and was deleted from existence.");

        // 5. Clean up entity
        nullFox.discard();
    }

    private static void eventServerMessage(ServerLevel level, String text) {
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(text), false);
    }

    private static void resetEvent() {
        isEventActive = false;
        timerTicks = 0;
        targetPlayer = null;
        spawnedNullFox = null;
    }
}