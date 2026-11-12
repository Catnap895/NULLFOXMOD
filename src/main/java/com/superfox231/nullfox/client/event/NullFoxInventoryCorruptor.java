package com.superfox231.nullfox.client.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class NullFoxInventoryCorruptor {

    private static final Random RANDOM = new Random();

    public static void corruptHotbar(ServerPlayer player) {
        if (player.level().isClientSide()) return;

        int selectedSlot = player.getInventory().selected;
        ItemStack currentItem = player.getInventory().getItem(selectedSlot);

        if (!currentItem.isEmpty()) {
            // Rename the currently held item to a corrupted system message
            String[] corruptedNames = {
                    "§4[CORRUPTED_DATA]",
                    "§0??_ENTITY_TAG",
                    "§8[DO_NOT_HOLD]",
                    "§cNULL_POINTER_EXCEPTION",
                    "§4SUPERFOX_SOUL_CONTAINER"
            };

            String chosenName = corruptedNames[RANDOM.nextInt(corruptedNames.length)];
            currentItem.setHoverName(Component.literal(chosenName));

            // Play break sound effect to disorient the player
            player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.8F, 0.2F);
            player.sendSystemMessage(Component.literal("§4[WARN] Slot " + (selectedSlot + 1) + " allocation table compromised."));
        }
    }
}