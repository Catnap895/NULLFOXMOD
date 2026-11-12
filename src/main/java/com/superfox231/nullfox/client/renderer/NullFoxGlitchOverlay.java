package com.superfox231.nullfox.client.renderer;

import com.superfox231.nullfox.NullFox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = NullFox.MODID, value = Dist.CLIENT)
public class NullFoxGlitchOverlay {

    private static int glitchTicksRemaining = 0;
    private static final Random RANDOM = new Random();

    // Pool of terrifying pseudo-code memory dumps and hex lines flashed during severe glitches
    private static final String[] MEMORY_DUMPS = {
            "EXCEPTION: 0xC0000005 ACCESS_VIOLATION",
            "NULL_FOX INJECTED INTO THREAD 0x231",
            "SUPERFOX_SKULL_CONTAINER OVERWRITTEN",
            "READ FAULT AT 0xDEADBEEF [PROTECTED]",
            "WARNING: FRAMEBUFFER HIJACKED BY ?_ENTITY",
            "java.lang.NullPointerException: Cannot read field 'soul'",
            "THE COMPILER CANNOT SAVE YOU NOW",
            "INSTANCE MAPPED TO LOCAL RAM SECTOR 404"
    };

    public static void triggerGlitch(int durationTicks) {
        glitchTicksRemaining = durationTicks;
    }

    public static boolean isGlitching() {
        return glitchTicksRemaining > 0;
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (glitchTicksRemaining > 0) {
            glitchTicksRemaining--;
            Minecraft mc = Minecraft.getInstance();
            GuiGraphics graphics = event.getGuiGraphics();
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            // 1. Heavy Chromatic / Static Tearing Lines (Expanded density)
            for (int i = 0; i < 28; i++) {
                int y = RANDOM.nextInt(screenHeight);
                int h = RANDOM.nextInt(12) + 1;
                int color;
                float roll = RANDOM.nextFloat();
                if (roll < 0.3f) {
                    color = 0xFF550000; // Deep dried blood red
                } else if (roll < 0.6f) {
                    color = 0xFF00FFCC; // Sickly cyan code static
                } else {
                    color = 0xFF000000; // Total void black tear
                }
                graphics.fill(0, y, screenWidth, y + h, color);
            }

            // 2. Sudden Inverted Color Flashes (Simulating display driver crash)
            if (RANDOM.nextFloat() < 0.15f) {
                graphics.fill(0, 0, screenWidth, screenHeight, 0x44FFFFFF); // Whiteout flash
            }

            // 3. Flash Corrupted Hex / Memory Dump Strings across the viewport
            if (RANDOM.nextFloat() < 0.45f) {
                String randomDump = MEMORY_DUMPS[RANDOM.nextInt(MEMORY_DUMPS.length)];
                int textX = RANDOM.nextInt(Math.max(1, screenWidth - 200));
                int textY = RANDOM.nextInt(Math.max(1, screenHeight - 50));
                graphics.drawString(mc.font, randomDump, textX, textY, 0xFFFF0033, true);
            }

            // 4. Heavy Vignette / Peripheral Shadowing (Tunnel vision effect)
            graphics.fill(0, 0, screenWidth, 15, 0xFF000000);
            graphics.fill(0, screenHeight - 15, screenWidth, screenHeight, 0xFF000000);
            graphics.fill(0, 0, 15, screenHeight, 0xFF000000);
            graphics.fill(screenWidth - 15, 0, screenWidth, screenHeight, 0xFF000000);
        }
    }
}