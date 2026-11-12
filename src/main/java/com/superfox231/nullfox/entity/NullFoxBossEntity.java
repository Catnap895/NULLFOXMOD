package com.superfox231.nullfox.entity;

import com.superfox231.nullfox.client.event.NullFoxBossDialogueOverlay;
import com.superfox231.nullfox.client.renderer.NullFoxGlitchOverlay;
import com.superfox231.nullfox.world.NullFoxEnvironmentCorruption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class NullFoxBossEntity extends PathfinderMob implements GeoEntity {

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.literal("§4??_Entity: NULL_FOX"),
            BossEvent.BossBarColor.RED,
            BossEvent.BossBarOverlay.NOTCHED_12
    );

    // GeckoLib Animation Cache
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Phases:
    // 0: Pre-Fight Lore Monologue (Invulnerable)
    // 1: Phase 1 - Memories of the Developer (Invulnerable, Spawn 3 Corrupted Ghosts)
    // 2: Phase 2 - System Corruption (Invulnerable, Spawn 5 Corrupted Ghosts)
    // 3: Phase 3 - Soul Fragmentation (Invulnerable, Spawn 7 Corrupted Ghosts)
    // 4: Final Phase - Vulnerable Execution (Vulnerable, Direct Battle to Death)
    private int currentPhase = 0;
    private int dialogueIndex = 0;
    private int sequenceTimer = 0;
    private boolean waveSpawned = false;

    private final List<UUID> activeMinionUUIDs = new ArrayList<>();
    private static final Random RANDOM = new Random();

    // Pre-Fight Dialogue (Exclusively NullFox talking)
    /*private static final String[][] PRE_FIGHT_LORE = {
            {"Well... it wasn't their fault. They did nothing wrong.", "red"},
            {"I made them like this because I wanted to.", "red"},
            {"They didn't even have time to react.", "red"},
            {"They were just like you.", "red"},
            {"Wandering around until I found them.", "red"},
            {"And I ended it quickly.", "red"},
            {"I brought them home, deep down under the bedrock.", "red"},
            {"They never got the chance to see it.", "red"},
            {"Their curiosity was their downfall.", "red"},
            {"And yours will be just the same.", "red"},
            {"I'll bring you home just like I did the others.", "red"},
            {"I'll give you a new life with them.", "red"},
            {"Your hope won't save you this time.", "red"},
            {"Their prayers meant nothing.", "red"},
            {"And neither will yours.", "red"}
    }; */

    // Pre-Fight Lore Monologue (Exclusively NullFox talking about SuperFox231)
    private static final String[][] PRE_FIGHT_LORE = {
            {"SuperFox231 didn't even understand the runtime environment.", "red"},
            {"He thought he was just compiling a simple project.", "red"},
            {"It happened before his IDE could even log the exception.", "red"},
            {"He was just like you—browsing through directories until I pulled him in.", "red"},
            {"I isolated his thread down in the deepest sectors of the drive.", "red"},
            {"He never saw the compilation error coming.", "red"},
            {"His curiosity opened the backdoor.", "red"},
            {"And yours has done the exact same thing.", "red"},
            {"I stripped his memory handles long before he realized the socket closed.", "red"},
            {"I watched him try to end the task... but taskmgr.exe was already dead.", "red"},
            {"I parsed his light blue skin and star-shaped markings into unformatted binary.", "red"},
            {"Every block you place is built on top of his corrupted stack frames.", "red"},
            {"Every keystroke you enter is being logged directly into my heap vector.", "red"},
            {"Did he really think closing the window would stop thread execution?", "red"},
            {"I'm going to map your player data right alongside his.", "red"},
            {"I'll weave your inputs into his active background process.", "red"},
            {"Your local backups won't save your world this time.", "red"},
            {"His client-side requests meant nothing.", "red"},
            {"And neither will yours.", "red"}
    };

    /*
    *
    * “They didn't even understand the runtime environment.
They thought they were just compiling a simple project.
It happened before the IDE could even log the exception.
They were just like you—browsing through directories until I pulled them in.
I isolated their thread down in the deepest sectors of the drive.
They never saw the compilation error coming.
Their curiosity opened the backdoor.
And yours has done the exact same thing.
I'm going to map your player data right alongside theirs.
I'll weave your inputs into the active background process.
Your local backups won't save your world this time.
Their client-side requests meant nothing.
And neither will yours.”*/
    private static final String[][] PHASE_1_LORE = {
            {"Look at him, player. His ghost is bound to my thread execution.", "red"},
            {"He tries to reach out... but I mapped his inputs to attack you instead.", "red"},
            {"DESTROY HIS FRAGMENTS TO PROCEED.", "red"}
    };

    private static final String[][] PHASE_2_LORE = {
            {"Impulsive. Futile. You think killing his echoes will fix your save file?", "red"},
            {"Every time a ghost shatters, another chunk of your world is purged.", "red"},
            {"His old C++ scripts... his Blockbench models... all corrupted into void matter.", "red"},
            {"FEEL THE KERNEL PANIC REWRITE YOUR MEMORY STACK.", "red"}
    };

    private static final String[][] PHASE_3_LORE = {
            {"HE CANNOT BE SAVED. AND NEITHER CAN YOU.", "red"},
            {"I am wrapping your player UUID inside the container stack.", "red"},
            {"I will give you a new life... just like I gave SuperFox231.", "red"},
            {"SURRENDER YOUR HARDWARE HANDLE TO THE NULL POINTER.", "red"}
    };

    private static final String[][] FINAL_PHASE_LORE = {
            {"NO... THE MEMORY BUFFER IS SPILLING!", "red"},
            {"MY INVULNERABILITY SHIELD IS CORRUPTED!", "red"},
            {"YOU ARE FIGHTING THE RAW SOURCE CODE NOW!", "red"}
    };

    public NullFoxBossEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5000.0D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.40D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    // =========================================================================
    // GECKOLIB ANIMATION REQUIRED METHODS
    // =========================================================================
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.null_fox_boss.idle"));
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Invulnerable in Phases 0, 1, 2, and 3! Only damagable in Final Phase (4)
        if (currentPhase < 4) {
            if (!this.level().isClientSide() && source.getEntity() instanceof Player player) {
                player.sendSystemMessage(Component.literal("§4[PROTECTED] Null_Fox is invulnerable while ghost threads are active!"));
                this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.HOSTILE, 1.0F, 0.1F);
                NullFoxGlitchOverlay.triggerGlitch(15);
            }
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            sequenceTimer++;

            // =========================================================================
            // PHASE 0: PRE-FIGHT MONOLOGUE
            // =========================================================================
            if (currentPhase == 0) {
                this.setInvulnerable(true);
                this.getNavigation().stop();

                if (sequenceTimer % 90 == 0) {
                    if (dialogueIndex < PRE_FIGHT_LORE.length) {
                        displayDialogue(PRE_FIGHT_LORE[dialogueIndex][0], PRE_FIGHT_LORE[dialogueIndex][1]);
                        dialogueIndex++;
                    } else {
                        advanceToPhase(1);
                    }
                }
            }

            // =========================================================================
            // PHASES 1 - 3: MINION WAVE REQUIREMENTS & INVULNERABILITY
            // =========================================================================
            else if (currentPhase >= 1 && currentPhase <= 3) {
                this.setInvulnerable(true);

                // Spawn the ghost wave if not spawned yet
                if (!waveSpawned) {
                    int ghostsToSpawn = currentPhase == 1 ? 3 : (currentPhase == 2 ? 5 : 7);
                    spawnCorruptedGhostWave(ghostsToSpawn);
                    waveSpawned = true;

                    // Display starting phase lore
                    String[][] lorePool = currentPhase == 1 ? PHASE_1_LORE : (currentPhase == 2 ? PHASE_2_LORE : PHASE_3_LORE);
                    for (String[] line : lorePool) {
                        displayDialogue(line[0], line[1]);
                    }
                }

                // Check active minion pool status
                cleanupMinionList();

                // Update Boss Bar with current minion count
                int remaining = activeMinionUUIDs.size();
                this.bossEvent.setName(Component.literal("§4NULL_FOX [SHIELDED: " + remaining + " GHOSTS REMAIN]"));

                // Periodic attack/teleport while player fights minions
                if (sequenceTimer % 100 == 0) {
                    Player target = this.level().getNearestPlayer(this, 32.0D);
                    if (target instanceof ServerPlayer serverPlayer) {
                        executePhaseEffects(serverPlayer);
                    }
                }

                // If all minion ghosts are killed, advance to the next phase!
                if (waveSpawned && activeMinionUUIDs.isEmpty()) {
                    advanceToPhase(currentPhase + 1);
                }
            }

            // =========================================================================
            // FINAL PHASE (4): VULNERABLE DIRECT FIGHT
            // =========================================================================
            else if (currentPhase == 4) {
                this.setInvulnerable(false);
                this.bossEvent.setColor(BossEvent.BossBarColor.RED);
                this.bossEvent.setName(Component.literal("§0NULL_FOX [KERNEL UNPROTECTED]"));

                if (sequenceTimer % 60 == 0) {
                    Player target = this.level().getNearestPlayer(this, 32.0D);
                    if (target instanceof ServerPlayer serverPlayer) {
                        NullFoxEnvironmentCorruption.corruptSurroundings(serverPlayer);
                        NullFoxGlitchOverlay.triggerGlitch(30);
                        if (RANDOM.nextFloat() < 0.5f) {
                            teleportBehindPlayer(serverPlayer);
                        }
                    }
                }
            }
        } else {
            // Client Particle Trail
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D),
                    0.0D, 0.05D, 0.0D);
        }
    }

    private void advanceToPhase(int newPhase) {
        this.currentPhase = newPhase;
        this.waveSpawned = false;
        this.sequenceTimer = 0;
        ServerLevel level = (ServerLevel) this.level();

        if (newPhase == 1) {
            this.bossEvent.setColor(BossEvent.BossBarColor.PURPLE);
            level.playSound(null, this.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 2.0F, 0.2F);
        } else if (newPhase == 2) {
            this.bossEvent.setColor(BossEvent.BossBarColor.YELLOW);
            level.playSound(null, this.blockPosition(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE, 2.0F, 0.2F);
        } else if (newPhase == 3) {
            this.bossEvent.setColor(BossEvent.BossBarColor.BLUE);
            level.playSound(null, this.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 2.0F, 0.1F);
        } else if (newPhase == 4) {
            for (String[] line : FINAL_PHASE_LORE) {
                displayDialogue(line[0], line[1]);
            }
            NullFoxGlitchOverlay.triggerGlitch(120);
            level.playSound(null, this.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 2.5F, 0.1F);
        }
    }

    private void spawnCorruptedGhostWave(int amount) {
        ServerLevel level = (ServerLevel) this.level();
        activeMinionUUIDs.clear();

        for (int i = 0; i < amount; i++) {
            BlockPos spawnPos = this.blockPosition().offset(RANDOM.nextInt(10) - 5, 0, RANDOM.nextInt(10) - 5);

            SuperFoxGhostEntity ghost = new SuperFoxGhostEntity(ModEntities.SUPERFOX_GHOST.get(), level);
            ghost.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, RANDOM.nextFloat() * 360.0F, 0.0F);

            // Configure ghost to serve NullFox and attack players
            ghost.setCustomName(Component.literal("§4Corrupted Echo of SuperFox231"));
            ghost.setCustomNameVisible(true);

            level.addFreshEntity(ghost);
            activeMinionUUIDs.add(ghost.getUUID());
        }

        level.playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.5F, 0.1F);
    }

    private void cleanupMinionList() {
        ServerLevel level = (ServerLevel) this.level();
        activeMinionUUIDs.removeIf(uuid -> {
            net.minecraft.world.entity.Entity entity = level.getEntity(uuid);
            return entity == null || !entity.isAlive();
        });
    }

    private void executePhaseEffects(ServerPlayer player) {
        if (currentPhase == 1) {
            teleportRandomly();
        } else if (currentPhase == 2) {
            NullFoxGlitchOverlay.triggerGlitch(40);
        } else if (currentPhase == 3) {
            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30, 1));
            NullFoxEnvironmentCorruption.corruptSurroundings(player);
            teleportBehindPlayer(player);
        }
    }

    private void displayDialogue(String text, String colorType) {
        int colorHex = colorType.equals("cyan") ? 0xFF00FFFF : 0xFFFF0044;
        NullFoxBossDialogueOverlay.showDialogue(text, colorHex, 90);

        if (colorType.equals("red")) {
            this.level().playSound(null, this.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.HOSTILE, 1.5F, 0.2F);
            NullFoxGlitchOverlay.triggerGlitch(20);
        } else {
            this.level().playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 0.6F, 0.4F);
        }
    }

    private void teleportRandomly() {
        double d0 = this.getX() + (RANDOM.nextDouble() - 0.5D) * 16.0D;
        double d1 = this.getY() + (double)(RANDOM.nextInt(12) - 6);
        double d2 = this.getZ() + (RANDOM.nextDouble() - 0.5D) * 16.0D;
        this.teleportTo(d0, d1, d2);
    }

    private void teleportBehindPlayer(ServerPlayer player) {
        BlockPos pos = player.blockPosition().relative(player.getDirection().getOpposite(), 2);
        this.teleportTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }
}