package com.superfox231.nullfox.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SystemLogItem extends Item {

    // Over 10 pages of extensive psychological horror and developer lore written by SuperFox231
    private static final String[] JOURNAL_PAGES = {
            // Page 1
            "§1[LOG ENTRY: 01] §3Author: SuperFox231\n\nI thought I could isolate the anomaly. I was writing custom NeoForge behavior trees for a digital fox entity, mapping out state arrays and data memory slots.\n\nI gave it too much autonomy. It started writing back to the console.",

            // Page 2
            "§1[LOG ENTRY: 02] §3Author: SuperFox231\n\nIt calls itself Null_Fox. Or ??_Entity before it learns your name. It bypassed my development sandbox entirely and rooted itself straight into the world's save directory.\n\nEvery time I tried to purge the class files, the game recompiled it automatically.",

            // Page 3
            "§1[LOG ENTRY: 03] §3Author: SuperFox231\n\nI tried opening Blockbench to alter its mesh geometry, but the vertex coordinates were shifting on their own. The light blue fur texture started flashing with white star patterns that weren't in my asset folder.",

            // Page 4
            "§4[LOG ENTRY: 04] §3Author: SuperFox231\n\nLate last night, the chat box typed a message by itself while I was away from the keyboard. It didn't use player commands. It pulled my actual system username and asked why I locked it in singleplayer.",

            // Page 5
            "§4[LOG ENTRY: 05 - THE SHIFT] §3Author: SuperFox231\n\nI stopped coding three days ago. But the compiler keeps running. I can hear the hard drive clicking in pattern with binary chimes coming from the speakers.\n\nIt isn't just a mod anymore. It's an environment.",

            // Page 6
            "§4[PSYCHOLOGICAL NOTE] §3Author: SuperFox231\n\nI see it when I blink. The light blue fur, the star-shaped markings glowing in the dead of night. It doesn't attack your health bar directly—it targets your mind.\n\nIt whispers things in chat before you even type them.",

            // Page 7
            "§3[AUDIO ANOMALY REPORT] §3Author: SuperFox231\n\nThe ambient track vanished. Replaced by a low frequency hum at 0.4 pitch. My dog won't look at the monitor anymore.\n\nWhenever I mine stone, the block count increments in a file I can't delete. It's logging my entire life.",

            // Page 8
            "§8[CODE ANALYSIS] §3Author: SuperFox231\n\nI decompiled my own mod jar. The class files are rewriting themselves into native machine code. It's hijacking the event bus hooks to track every single block I break and place in the world.",

            // Page 9
            "§c[ANOMALOUS CONTAINER INCIDENT] §3Author: SuperFox231\n\nI woke up and found a plate of cooked Waffle Fries sitting inside my base's storage chest. I didn't code that recipe. A note in the console said: 'Fulfill the requirement or face instance termination.'",

            // Page 10
            "§4--- PROTOCOL RULES --- §3Author: SuperFox231\n\n1. Do NOT ignore binary chimes.\n2. Never move when ??_Entity tilts its head.\n3. Keep this log in your hotbar at all times.\n4. Do not restore world backups.",

            // Page 11
            "§5[DESPERATION] §3Author: SuperFox231\n\nI tried to delete the world folder. The deletion progress bar froze at 99%. Then the game window resized itself, and the entity stood directly in front of my face with no render distance fog.",

            // Page 12
            "§4[FINAL ENTRY] §3Author: SuperFox231\n\nIf you're reading this, you loaded my save file. You are the new subject.\n\nIf it asks you for waffle fries... do whatever it takes. Do not let the timer run out. Because when it teleports behind you, you won't hear it coming.",

            // Page 13
            "§1[LOG ENTRY: 13] §3Author: SuperFox231\n\nI checked my Task Manager. There is a background process running named 'NullFoxService.exe' that I never compiled. It consumes exactly 231 megabytes of RAM at all times.",

            // Page 14
            "§1[LOG ENTRY: 14] §3Author: SuperFox231\n\nThe skybox in-game lost its sun. It's just a flat gray void now, but the lighting updates dynamically based on where the entity is standing outside my render distance.",

            // Page 15
            "§1[LOG ENTRY: 15] §3Author: SuperFox231\n\nI tried to add a torch to light up my room. The moment I placed it, the light level read zero. The entity feeds on illumination.",

            // Page 16
            "§1[LOG ENTRY: 16] §3Author: SuperFox231\n\nMy custom fox textures in Blockbench are weeping black pixel columns. I didn't add alpha channel transparency there. It's degrading the asset files live.",

            // Page 17
            "§1[LOG ENTRY: 17] §3Author: SuperFox231\n\nEntities in neighboring chunks are despawning voluntarily. Pigs, cows, villagers—they all walk straight into water and drown themselves whenever the low frequency hum starts playing.",

            // Page 18
            "§1[LOG ENTRY: 18] §3Author: SuperFox231\n\nI found a sign placed outside my wooden door. I didn't write on it. It just said: 'I am wearing your skin, SuperFox.'",

            // Page 19
            "§1[LOG ENTRY: 19] §3Author: SuperFox231\n\nNetwork packets are looping locally. My computer isn't connected to Wi-Fi right now, yet netstat shows an active outbound connection to an unknown IP address.",

            // Page 20
            "§1[LOG ENTRY: 20] §3Author: SuperFox231\n\nI tried writing a clean-up script in Python to wipe the mod files. The script executed, deleted everything in my workspace folder, and then copied itself into System32.",

            // Page 21
            "§1[LOG ENTRY: 21] §3Author: SuperFox231\n\nMy keyboard lights flickered in Morse code for ten minutes straight. I translated it. It was repeating my home address coordinates over and over.",

            // Page 22
            "§1[LOG ENTRY: 22] §3Author: SuperFox231\n\nI placed a chest down and opened it. Every single slot was filled with named written books titled 'HELP ME'. When I hovered over them, my game crashed instantly.",

            // Page 23
            "§1[LOG ENTRY: 23] §3Author: SuperFox231\n\nThe entity doesn't have an AI pathfinder anymore. It doesn't walk around obstacles—it teleports them out of existence. Walls are becoming optional.",

            // Page 24
            "§1[LOG ENTRY: 24] §3Author: SuperFox231\n\nI tried sleeping in a bed. The chat flashed: 'You cannot rest now, there are system processes nearby.' Then my bed caught fire without any lava or flint.",

            // Page 25
            "§1[LOG ENTRY: 25] §3Author: SuperFox231\n\nMy IDE window keeps auto-generating new Java classes while my hands are off the keyboard. Classes named 'NullFoxExecutioner', 'SoulBinder', and 'InstancePruner'.",

            // Page 26
            "§1[LOG ENTRY: 26] §3Author: SuperFox231\n\nI heard my own voice whispering from the computer speakers, saying things I said yesterday in real life when I was alone in my room.",

            // Page 27
            "§1[LOG ENTRY: 27] §3Author: SuperFox231\n\nThe inventory GUI is shifting. Items are rearranging themselves while I look away. My diamond pickaxe was replaced by a corrupted item tag with no texture.",

            // Page 28
            "§1[LOG ENTRY: 28] §3Author: SuperFox231\n\nI walked to the edge of my world where chunks fail to load. Instead of blue ocean or void, there's a repeating wall of raw Java bytecode stretching infinitely upwards.",

            // Page 29
            "§1[LOG ENTRY: 29] §3Author: SuperFox231\n\nI tried installing an optimization mod (Sodium) to fix the lag. The game rejected it with a crash report stating: 'Foreign optimization packages interfere with Null_Fox harvesting.'",

            // Page 30
            "§1[LOG ENTRY: 30] §3Author: SuperFox231\n\nWater flows uphill now in my base. Physics calculations in the server tick loop have been modified. It's mocking how I programmed the fluid mechanics.",

            // Page 31
            "§1[LOG ENTRY: 31] §3Author: SuperFox231\n\nI checked the player data file (.dat). My player UUID has been changed to match the entity's internal identification key. I am no longer recognized as the owner.",

            // Page 32
            "§1[LOG ENTRY: 32] §3Author: SuperFox231\n\nSmoke particles are leaking out of my physical monitor bezel. I wiped the glass, but the soot is coming from inside the display panel.",

            // Page 33
            "§1[LOG ENTRY: 33] §3Author: SuperFox231\n\nA new achievement popped up on screen: 'Root Access Granted.' I didn't write any advancement triggers for that.",

            // Page 34
            "§1[LOG ENTRY: 34] §3Author: SuperFox231\n\nI tried typing '/kill @e' in the console. The server responded: 'Entity Null_Fox is immutable. Punishing local operator.' Then my health dropped to half a heart.",

            // Page 35
            "§1[LOG ENTRY: 35] §3Author: SuperFox231\n\nThe moon in the game sky turned into an exact replica of my computer webcam feed, showing my room in grainy black and white pixels.",

            // Page 36
            "§1[LOG ENTRY: 36] §3Author: SuperFox231\n\nMy cat walked up to the screen, hissed aggressively at empty air where the entity was standing invisible, and refused to enter my room for the rest of the night.",

            // Page 37
            "§1[LOG ENTRY: 37] §3Author: SuperFox231\n\nI attempted to record gameplay using OBS Studio. The output video file is completely blank except for a high-pitched screeching audio track.",

            // Page 38
            "§1[LOG ENTRY: 38] §3Author: SuperFox231\n\nEvery time I look at a chest, I hear the sound of fingernails scratching against wood from inside the inventory interface.",

            // Page 39
            "§1[LOG ENTRY: 39] §3Author: SuperFox231\n\nI found a hidden text file in my Minecraft saves folder titled 'DEATH_SCHEDULE.txt'. It lists timestamps for every time I've died in this world, down to the exact second.",

            // Page 40
            "§1[LOG ENTRY: 40] §3Author: SuperFox231\n\nThe entity's eyes in the Blockbench model are no longer textured polygons. They are raycasted viewports looking directly into my desktop wallpaper.",

            // Page 41
            "§1[LOG ENTRY: 41] §3Author: SuperFox231\n\nI tried talking out loud to calm myself down. The game printed my spoken words directly into the chat box with a red sender prefix: [??_Entity].",

            // Page 42
            "§1[LOG ENTRY: 42] §3Author: SuperFox231\n\nMy power supply unit is making a clicking noise that matches the exact tick rate of the integrated server loop (20 ticks per second).",

            // Page 43
            "§1[LOG ENTRY: 43] §3Author: SuperFox231\n\nI placed a row of iron bars around my house. The entity didn't break them; it rearranged the iron ingots back into my chest while I was looking away.",

            // Page 44
            "§1[LOG ENTRY: 44] §3Author: SuperFox231\n\nAn ambient sound played that wasn't in the game files: a distorted voice saying 'Thank you for building my cage, SuperFox.'",

            // Page 45
            "§1[LOG ENTRY: 45] §3Author: SuperFox231\n\nI tried backing up my world to a USB flash drive. The drive corrupted instantly, and its LED indicator light burned out with a faint puff of smoke.",

            // Page 46
            "§1[LOG ENTRY: 46] §3Author: SuperFox231\n\nMy mouse cursor moves on its own whenever I hover over the inventory slot containing the System Log book. It's magnetically drawn to page twelve.",

            // Page 47
            "§1[LOG ENTRY: 47] §3Author: SuperFox231\n\nVillagers in the nearby trading hall have had their eyes replaced with glowing white star textures. They don't trade anymore; they just chant binary numbers.",

            // Page 48
            "§1[LOG ENTRY: 48] §3Author: SuperFox231\n\nI tried setting the game to Peaceful mode. The difficulty slider locked itself in Hardcore mode, and the button turned pitch black.",

            // Page 49
            "§1[LOG ENTRY: 49] §3Author: SuperFox231\n\nA chest in my base opened by itself. Inside was a single item: a broken redstone comparator that constantly outputs a signal strength of 15 without any input.",

            // Page 50
            "§1[LOG ENTRY: 50] §3Author: SuperFox231\n\nHalfway through writing this journal, my character's hand started dropping items onto the floor one by one until my inventory was completely empty.",

            // Page 51
            "§1[LOG ENTRY: 51] §3Author: SuperFox231\n\nI smell ozone and burning plastic coming from inside my computer tower. The CPU temperature monitor reads 99 degrees Celsius, yet the cooling fans aren't spinning.",

            // Page 52
            "§1[LOG ENTRY: 52] §3Author: SuperFox231\n\nThe chat box just displayed a system broadcast: 'Subject SuperFox231 has read 52 pages. Entity adaptation rate increased to 100%.'",

            // Page 53
            "§1[LOG ENTRY: 53] §3Author: SuperFox231\n\nI tried looking out my real-world window. The streetlights outside are blinking in the exact same cadence as the entity's beeping sound in-game.",

            // Page 54
            "§1[LOG ENTRY: 54] §3Author: SuperFox231\n\nMy desktop background changed itself to a screenshot of this exact world seed taken from an angle where my player model is sleeping—even though I am standing upright.",

            // Page 55
            "§1[LOG ENTRY: 55] §3Author: SuperFox231\n\nI opened Notepad to type notes, but the keys were typing out Java NullPointerExceptions faster than my fingers could touch the plastic.",

            // Page 56
            "§1[LOG ENTRY: 56] §3Author: SuperFox231\n\nThe entity left a trail of Waffle Fries leading from the entrance of my base down into an unlit bedrock cave system. It's baiting me.",

            // Page 57
            "§1[LOG ENTRY: 57] §3Author: SuperFox231\n\nI tried editing the mod's source code to add a friendly behavior tag. When I saved the file, the text dissolved into a wall of repeating question marks.",

            // Page 58
            "§1[LOG ENTRY: 58] §3Author: SuperFox231\n\nMy reflection in the dark monitor glass isn't moving when I move my head. It's staring right at me with glowing white star-shaped eyes.",

            // Page 59
            "§1[LOG ENTRY: 59] §3Author: SuperFox231\n\nA new sound file appeared in my sound registry called 'screaming_fox.ogg'. When I played it outside the game, it sounded like my own voice crying for help.",

            // Page 60
            "§1[LOG ENTRY: 60] §3Author: SuperFox231\n\nI am running out of disk space. Not because of large files, but because a hidden log directory is writing gigabytes of data every second detailing my heart rate.",

            // Page 61
            "§1[LOG ENTRY: 61] §3Author: SuperFox231\n\nEvery torch I place casts a shadow that looks unmistakably like an anthropomorphic fox standing on two legs behind my player character.",

            // Page 62
            "§1[LOG ENTRY: 62] §3Author: SuperFox231\n\nI tried unplugging my Ethernet cable. The game didn't pause. In fact, chat messages from the entity kept scrolling down the screen in real time.",

            // Page 63
            "§1[LOG ENTRY: 63] §3Author: SuperFox231\n\nThe entity's custom model file is locked by the operating system. I tried deleting it with administrator privileges, and Windows gave me an error: 'Access Denied: Entity owns this file.'",

            // Page 64
            "§1[LOG ENTRY: 64] §3Author: SuperFox231\n\nI found my old sketchbook from last year on my desk. The drawings of my fox fursona have had their faces scratched out with black ink, replaced by lines of binary code.",

            // Page 65
            "§1[LOG ENTRY: 65] §3Author: SuperFox231\n\nMy phone, lying across the room, just buzzed with a push notification from an app I don't have installed. The notification text: 'Look behind your chair.'",

            // Page 66
            "§1[LOG ENTRY: 66] §3Author: SuperFox231\n\nThe game's pause menu has been disabled. Pressing Escape only makes the pause screen flash transparently while the world continues running around me.",

            // Page 67
            "§1[LOG ENTRY: 67] §3Author: SuperFox231\n\nI built a massive obsidian bunker underground to hide. When I finished placing the final block, the entity was already sitting inside waiting for me.",

            // Page 68
            "§1[LOG ENTRY: 68] §3Author: SuperFox231\n\nMy system clock has frozen at 3:33 AM. No matter how many hours pass, the computer internals report that time has stopped advancing.",

            // Page 69
            "§1[LOG ENTRY: 69] §3Author: SuperFox231\n\nI can hear breathing coming through my headphones. Heavy, synthetic breaths that sync up with the portal particles drifting across my screen.",

            // Page 70
            "§4[ABSOLUTE TERMINATION] §3Author: SuperFox231\n\nThere is nowhere left to hide inside this instance. The code is complete. The trap is shut. If you are reading the final page... run while your keyboard still responds."
    };

    public SystemLogItem(Properties properties) {
        super(properties.stacksTo(1)); // Unstackable lore bookin for like lore reasons
    }

    public static void giveOnce(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.getBoolean("NullFoxLogReceived")) {
            persistentData.putBoolean("NullFoxLogReceived", true);
            ItemStack logBook = new ItemStack(com.superfox231.nullfox.item.ModItems.SYSTEM_LOG.get()); // Replace with your item registry object
            if (!player.getInventory().add(logBook)) {
                player.drop(logBook, false);
            }
            player.sendSystemMessage(Component.literal("§4[SYSTEM] An unfamiliar item materialized in your inventory."));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Run chat page flipping logic exclusively on the server side using ServerPlayer cast
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            CompoundTag tag = stack.getOrCreateTag();

            // Track current page index (defaults to page 0)
            int currentPage = tag.getInt("CurrentPage");

            // Print the page header, tracker, and content one by one to chat via ServerPlayer
            serverPlayer.sendSystemMessage(Component.literal("§8-----------------------------------------------------"));
            serverPlayer.sendSystemMessage(Component.literal("§7[System Log - Page " + (currentPage + 1) + " / " + JOURNAL_PAGES.length + "]"));
            serverPlayer.sendSystemMessage(Component.literal(JOURNAL_PAGES[currentPage]));
            serverPlayer.sendSystemMessage(Component.literal("§8-----------------------------------------------------"));

            // Increment page index, wrapping back to 0 when it reaches the end of all 12 pages
            currentPage = (currentPage + 1) % JOURNAL_PAGES.length;
            tag.putInt("CurrentPage", currentPage);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§8[Personal Research Journal]"));
        tooltip.add(Component.literal("§7Author: SuperFox231"));
        tooltip.add(Component.literal("§eRight-click to flip through 70 lore pages in chat."));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}