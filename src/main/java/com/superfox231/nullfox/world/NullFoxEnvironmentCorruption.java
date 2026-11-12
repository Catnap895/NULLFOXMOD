package com.superfox231.nullfox.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class NullFoxEnvironmentCorruption {

    private static final Random RANDOM = new Random();

    // Pool of terrifying 2-line sign messages written by SuperFox231 and Null_Fox
    private static final String[][] SIGN_MESSAGES = {
            {"§4HE IS LISTENING", "§0- SuperFox231"},
            {"§4DO NOT MOVE", "§0When it beeps."},
            {"§0IT IS IN YOUR", "§4RAM STACK"},
            {"§4LOOK BEHIND YOU", "§0SuperFox"},
            {"§0I can hear your", "§4keyboard clicks."},
            {"§4BUILD 0.0.0_NULL", "§0UNCONTAINED"},
            {"§0Why locked in", "§4singleplayer?"},
            {"§4DELETED REPO", "§0It saved a copy."},
            {"§0Instance error:", "§4SOUL_HARVEST"},
            {"§4DAISY BELL", "§038 SECONDS LEFT"},
            {"§0He's wearing my", "§4light blue skin."},
            {"§4I WAS THE DEV", "§0Now I am data."},
            {"§0CHECK TASK", "§4MANAGER NOW"},
            {"§4TURN AROUND", "§0SLOWLY"},
            {"§0It feeds on your", "§4FOV frustum."},
            {"§4NO REST ALLOWED", "§0Processes near."},
            {"§0The sun is gone.", "§4It took the light."},
            {"§4SYSTEM PANIC", "§00xDEADBEEF"},
            {"§0I tried to delete", "§4the save folder."},
            {"§4RUN BEFORE THE", "§0TIMER EXPIRES"},
            {"§0It knows your", "§4real name."},
            {"§4WAFFLE FRIES", "§0REQUIRED"},
            {"§0Light level = 0", "§4It likes the dark."},
            {"§4STOP FLIPPING", "§0THE LOG PAGES"},
            {"§0Don't look at the", "§4webcam light."},
            {"§4ROOT ACCESS", "§0GRANTED"},
            {"§0It isn't a mob.", "§4It's a patch."},
            {"§4I WAS SUPERFOX", "§0Now I'm a ghost."},
            {"§0Can you hear", "§4the 0.4Hz hum?"},
            {"§4STAND STILL", "§0IF YOU WANT LIFE"}
    };

    public static void corruptSurroundings(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        BlockPos playerPos = player.blockPosition();

        // Scan a 10x10 area around the player for torches or light sources
        for (int x = -5; x <= 5; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -5; z <= 5; z++) {
                    BlockPos targetPos = playerPos.offset(x, y, z);
                    BlockState state = level.getBlockState(targetPos);

                    // Replace normal torches with redstone torches
                    if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)) {
                        level.setBlockAndUpdate(targetPos, Blocks.REDSTONE_TORCH.defaultBlockState());
                    }

                    // Replace water with air to simulate dying environment
                    else if (state.is(Blocks.WATER) && RANDOM.nextFloat() < 0.2f) {
                        level.setBlockAndUpdate(targetPos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }

        // 10% chance to spawn an eerie sign directly behind the player
        if (RANDOM.nextFloat() < 0.10f) {
            // Get the direction behind where the player is currently facing
            Direction oppositeDir = player.getDirection().getOpposite();
            BlockPos signPos = playerPos.relative(oppositeDir, 2);

            if (level.getBlockState(signPos).isAir()) {
                level.setBlockAndUpdate(signPos, Blocks.OAK_SIGN.defaultBlockState());
                if (level.getBlockEntity(signPos) instanceof SignBlockEntity sign) {
                    // Pick a random sign message pair from our terror pool
                    String[] selectedMessage = SIGN_MESSAGES[RANDOM.nextInt(SIGN_MESSAGES.length)];

                    SignText text = sign.getText(true)
                            .setMessage(0, Component.literal(selectedMessage[0]))
                            .setMessage(1, Component.literal(selectedMessage[1]));

                    sign.setText(text, true);
                    sign.setChanged();
                }
            }
        }
    }
}