package com.superfox231.nullfox.client.event;

import com.superfox231.nullfox.NullFox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = NullFox.MODID, value = Dist.CLIENT)
public class NullFoxBossDialogueOverlay {
    private static String currentText = "";
    private static int textColor = 0xFFFF0044; // Default neon red
    private static int displayTicks = 0;
    private static final Random RANDOM = new Random();

    public static void showDialogue(String text, int color, int durationTicks) {
        currentText = text;
        textColor = color;
        displayTicks = durationTicks;
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (displayTicks > 0) {
            displayTicks--;
            Minecraft mc = Minecraft.getInstance();
            GuiGraphics graphics = event.getGuiGraphics();

            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            // Calculate center position
            int textWidth = mc.font.width(currentText);
            int x = (screenWidth - textWidth) / 2;
            int y = (screenHeight / 2) - 40;

            // Apply frantic offset jitter when rendering to look glitchy
            int offsetX = (RANDOM.nextFloat() < 0.2f) ? RANDOM.nextInt(5) - 2 : 0;
            int offsetY = (RANDOM.nextFloat() < 0.2f) ? RANDOM.nextInt(3) - 1 : 0;

            // Render text shadow / glow backlayer
            graphics.drawString(mc.font, currentText, x + offsetX + 1, y + offsetY + 1, 0xFF000000, false);

            // Render main dialogue text (Cyan: 0xFF00FFFF or Red: 0xFFFF0044)
            graphics.drawString(mc.font, currentText, x + offsetX, y + offsetY, textColor, true);
        }
    }
}
