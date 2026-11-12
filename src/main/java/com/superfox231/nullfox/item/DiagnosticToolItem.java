package com.superfox231.nullfox.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiagnosticToolItem extends Item {

    public DiagnosticToolItem(Properties properties) {
        super(properties.durability(100));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            // Apply short-term resistance and blindness suppression
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1, false, false, false));

            level.playSound(null, serverPlayer.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.8F, 1.5F);
            serverPlayer.sendSystemMessage(Component.literal("§a[DIAGNOSTIC] Suppressing local thread memory interrupts...").withStyle(ChatFormatting.GREEN));

            // Damage the tool slightly on use
            stack.hurtAndBreak(15, serverPlayer, (p) -> p.broadcastBreakEvent(hand));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§8[EXPERIMENTAL DEBUG SCRIPT]"));
        tooltip.add(Component.literal("§7Author: SuperFox231"));
        tooltip.add(Component.literal("§cWarning: Vulnerable to byte-stream corruption."));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}