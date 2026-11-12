package com.superfox231.nullfox.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.superfox231.nullfox.client.event.NullFoxDaisyBellEvent;
import com.superfox231.nullfox.client.event.SuperFoxGhostEncounter;
import com.superfox231.nullfox.client.renderer.NullFoxGlitchOverlay;
import com.superfox231.nullfox.entity.NullFoxBossEntity;
import com.superfox231.nullfox.world.NullFoxHorrorEvents;
import com.superfox231.nullfox.world.NullFoxJumpscareEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import com.superfox231.nullfox.entity.ModEntities;

public class NullFoxCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("nullfox")
                        .requires(source -> source.hasPermission(2)) // Level 2 permission (OP / Cheats Enabled)
                        .then(Commands.literal("trigger")
                                .then(Commands.argument("event_type", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            builder.suggest("daisy_bell");
                                            builder.suggest("psychosis");
                                            builder.suggest("ghost");
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            String eventType = StringArgumentType.getString(context, "event_type");

                                            if (eventType.equalsIgnoreCase("daisy_bell")) {
                                                NullFoxDaisyBellEvent.forceTrigger(player);
                                                context.getSource().sendSuccess(() -> Component.literal("§c[Debug] Triggered Daisy Bell event for " + player.getName().getString()), true);
                                            } else if (eventType.equalsIgnoreCase("psychosis")) {
                                                player.sendSystemMessage(Component.literal("§4[KERNEL PANIC] Manually forced psychosis sequence."));
                                                context.getSource().sendSuccess(() -> Component.literal("§c[Debug] Psychosis event triggered."), true);
                                            } else if (eventType.equalsIgnoreCase("ghost")) {
                                                SuperFoxGhostEncounter.forceTrigger(player);
                                                context.getSource().sendSuccess(() -> Component.literal("§c[Debug] Triggered SuperFox ghost encounter."), true);
                                            }
                                            else if (eventType.equalsIgnoreCase("boss")) {
                                                if (player.level() instanceof ServerLevel serverLevel) {
                                                    // Ensure ModEntities.NULL_FOX_BOSS uses .get()
                                                    NullFoxBossEntity boss = new NullFoxBossEntity(ModEntities.NULL_FOX_BOSS.get(), serverLevel);

                                                    boss.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                                                    serverLevel.addFreshEntity(boss);

                                                    player.sendSystemMessage(Component.literal("§4[Debug] Summoned Null_Fox Boss Entity."));
                                                }
                                            }
                                            else if (eventType.equalsIgnoreCase("glitch")) {
                                                NullFoxGlitchOverlay.triggerGlitch(120);
                                            }
                                            else if (eventType.equalsIgnoreCase("jumpscare")) {
                                                NullFoxJumpscareEvent.triggerRandomJumpscare(player);
                                            }
                                            else if (eventType.equalsIgnoreCase("ranHorror")) {
                                                NullFoxHorrorEvents.triggerForcedRebindEvent(player);
                                            }
                                            else {
                                                context.getSource().sendFailure(Component.literal("§4Unknown event. Options: daisy_bell, psychosis, ghost"));
                                            }

                                            return 1;
                                        })
                                )
                        )
        );
    }
}