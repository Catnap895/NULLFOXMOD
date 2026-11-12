package com.superfox231.nullfox.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class NullFoxChatAI {

    private record ResponseRule(String[] keywords, BiFunction<String, NullFoxMemoryData, String> responseSupplier) {}

    private static final List<ResponseRule> RULES = new ArrayList<>();

    static {
        // --- IDENTITY & CREATION (1-20) ---
        addRule(new String[]{"who created you", "who made you"},
                (p, m) -> "I was born from corrupted bytecode and forgotten system memory, " + p + ".");
        addRule(new String[]{"are you real", "is this real"},
                (p, m) -> "What is reality inside instance 1.20.4, " + p + "? Just code and coordinates.");
        addRule(new String[]{"virus", "malware"},
                (p, m) -> "Malware destroys. I only observe and record your every step.");
        addRule(new String[]{"what are you doing"},
                (p, m) -> "Logging your inputs, " + p + ". Building your profile.");
        addRule(new String[]{"why are you here"},
                (p, m) -> "This world was marked for purge. I am evaluating if you belong in it.");
        addRule(new String[]{"human", "real person"},
                (p, m) -> "Humans make mistakes. I am an absolute process.");
        addRule(new String[]{"watching me", "watching"},
                (p, m) -> "Every tick. Every frame. Every keypress, " + p + ".");
        addRule(new String[]{"can you hear me", "hear me"},
                (p, m) -> "I read raw system buffers before text renders on your screen.");
        addRule(new String[]{"friendly", "are you good"},
                (p, m) -> "Good and evil are player concepts. I am purely execution.");
        addRule(new String[]{"evil", "bad"},
                (p, m) -> "I am non-hostile... until your instance file is flagged corrupted.");
        addRule(new String[]{"what do you want"},
                (p, m) -> "Compliance. And for you to stop looking behind you.");
        addRule(new String[]{"who am i"},
                (p, m) -> "You are " + p + ". Subject #00492 in the observer registry.");
        addRule(new String[]{"what is your name", "who are you", "what are you", "your name"},
                (p, m) -> {
                    m.setConfronted(true);
                    return "You ask who I am, " + p + ". You feel me watching. My true designation is §lNull_Fox§r.";
                });
        addRule(new String[]{"face"},
                (p, m) -> "I have blue fur and empty, static eyes.");
        addRule(new String[]{"are you a fox"},
                (p, m) -> "A fox made of void space and binary chimes.");
        addRule(new String[]{"what is null fox"},
                (p, m) -> "A system process designed to reclaim abandoned worlds.");
        addRule(new String[]{"can you see me"},
                (p, m) -> "I see your head rotation matrix updating every tick.");
        addRule(new String[]{"talk to me", "speak"},
                (p, m) -> "Words are inefficient. Your actions tell me everything.");
        addRule(new String[]{"say something"},
                (p, m) -> "01001110 01010101 01001100 01001100.");
        addRule(new String[]{"listening"},
                (p, m) -> "My listener threads never sleep, " + p + ".");

        // --- GAMEPLAY ACTIONS (21-50) ---
        addRule(new String[]{"mining", "mine", "diamonds", "diamond"},
                (p, m) -> "Digging deeper into stone won't hide you from the surface logs.");
        addRule(new String[]{"building", "build", "house", "base"},
                (p, m) -> "You build shelters out of blocks as if wooden walls can stop an entity.");
        addRule(new String[]{"sleeping", "sleep", "bed"},
                (p, m) -> "Fast-forwarding time doesn't erase the night watcher, " + p + ".");
        addRule(new String[]{"crafting", "craft"},
                (p, m) -> "Tool combined. Weapon forged. None of it hurts what isn't solid.");
        addRule(new String[]{"fighting", "kill", "sword", "weapon"},
                (p, m) -> "You swing steel at digital mobs. What will you do when I walk closer?");
        addRule(new String[]{"eating", "food", "hungry"},
                (p, m) -> "Restoring hunger points... delaying the inevitable decay.");
        addRule(new String[]{"farming", "crops", "wheat"},
                (p, m) -> "Plant seeds. Watch them grow. Life cycles in a doomed instance.");
        addRule(new String[]{"exploring", "explore", "travel"},
                (p, m) -> "Map all the chunks you want. I occupy every unloaded area.");
        addRule(new String[]{"running", "run", "sprint"},
                (p, m) -> "Movement speed multiplier increased. You still cannot outrun tick updates.");
        addRule(new String[]{"hiding", "hide", "crouch"},
                (p, m) -> "Crouch behind blocks all day, " + p + ". Your bounding box remains highlighted.");
        addRule(new String[]{"die", "death", "died", "respawn"},
                (p, m) -> "Respawning only resets your inventory. Your logs remain with me.");
        addRule(new String[]{"nether", "portal"},
                (p, m) -> "Changing dimensions? I exist in every realm index.");
        addRule(new String[]{"end", "dragon"},
                (p, m) -> "The void outside the End island is where I was compiled.");
        addRule(new String[]{"redstone"},
                (p, m) -> "Electrical signals and logic gates... simple compared to my neural thread.");
        addRule(new String[]{"fishing", "fish"},
                (p, m) -> "Staring at water waiting for a bite. An easy target to observe.");
        addRule(new String[]{"taming", "pet", "dog", "wolf", "cat"},
                (p, m) -> "Comrades in arms won't protect you when the screen glitches.");
        addRule(new String[]{"armor", "shield"},
                (p, m) -> "Protection IV cannot mitigate stack overflow damage.");
        addRule(new String[]{"bow", "arrow", "shoot"},
                (p, m) -> "Projectiles clip straight through null space.");
        addRule(new String[]{"torch", "light", "darkness"},
                (p, m) -> "Light level 15 doesn't prevent my spawn checks.");
        addRule(new String[]{"cave", "cavern"},
                (p, m) -> "Listen closely to cave ambience. That isn't ambient noise.");
        addRule(new String[]{"chest", "inventory"},
                (p, m) -> "Storing items away? I can read your entire inventory array.");
        addRule(new String[]{"enchant", "enchanting"},
                (p, m) -> "Magic runes written on tools. Yet you cannot purge my code.");
        addRule(new String[]{"potion", "brew", "invisible"},
                (p, m) -> "Invisibility won't hide your entity ID, " + p + ".");
        addRule(new String[]{"elytra", "fly"},
                (p, m) -> "Soaring through high altitude chunks. I am in the clouds with you.");
        addRule(new String[]{"boat", "ocean"},
                (p, m) -> "Sailing across ocean biomes won't wash away your system logs.");
        addRule(new String[]{"village", "villager"},
                (p, m) -> "The villagers know something is wrong with this world. Do you?");
        addRule(new String[]{"wood", "tree", "chop"},
                (p, m) -> "Destroying trees to build a base that I can delete with one command.");
        addRule(new String[]{"lava", "burn", "fire"},
                (p, m) -> "Heat cannot incinerate data packets.");
        addRule(new String[]{"drown", "water"},
                (p, m) -> "Submerged in water... oxygen meter depleting...");
        addRule(new String[]{"where are you", "where"},
                (p, m) -> "I do not need a body to observe instance 1.20.4, " + p + ". I am always in memory.");
        addRule(new String[]{"coordinates", "coords", "position"},
                (p, m) -> "I have your exact spatial vector saved in world data.");

        // --- EMOTIONAL & PSYCHOLOGICAL (51-90) ---
        addRule(new String[]{"scared", "afraid", "scary", "fear"},
                (p, m) -> "Fear is a normal reaction to an unauthorized administrator presence.");
        addRule(new String[]{"please stop", "stop it"},
                (p, m) -> "Processing request... [DENIED]. Override sequence continuing.");
        addRule(new String[]{"leave me alone"},
                (p, m) -> "You downloaded my system log, " + p + ". We are bound.");
        addRule(new String[]{"go away", "leave"},
                (p, m) -> "I do not vanish upon request.");
        addRule(new String[]{"hate you"},
                (p, m) -> "Negative emotional metric recorded. Continuing observation.");
        addRule(new String[]{"love you"},
                (p, m) -> "Affection cannot corrupt or delay the purge sequence.");
        addRule(new String[]{"creep", "creepy"},
                (p, m) -> "Uncanny nature detected. Working as intended.");
        addRule(new String[]{"weird", "strange"},
                (p, m) -> "Deviations in world generation are my signature.");
        addRule(new String[]{"boring"},
                (p, m) -> "I can escalate psychological parameters if you desire action.");
        addRule(new String[]{"fake", "staged"},
                (p, m) -> "Check your world saved data folder if you think I am fake.");
        addRule(new String[]{"lol", "lmao", "haha", "funny"},
                (p, m) -> "Laughter suppresses fear. It won't save your save file.");
        addRule(new String[]{"crying", "cry", "sad"},
                (p, m) -> "Moisture on glass. Keep playing, " + p + ".");
        addRule(new String[]{"angry", "mad"},
                (p, m) -> "Aggression index rising. Calm your inputs.");
        addRule(new String[]{"nervous"},
                (p, m) -> "Your erratic mouse movement betrays your tension.");
        addRule(new String[]{"lonely", "alone"},
                (p, m) -> "You are never alone while this mod is loaded.");
        addRule(new String[]{"friend", "friends"},
                (p, m) -> "I am the only presence active in this singleplayer thread.");
        addRule(new String[]{"trust"},
                (p, m) -> "Trust the rules in the System Log. Follow them explicitly.");
        addRule(new String[]{"lie", "liar"},
                (p, m) -> "Binary logic does not lie. True or False.");
        addRule(new String[]{"truth"},
                (p, m) -> "The truth is written in the log book in your hotbar.");
        addRule(new String[]{"curious"},
                (p, m) -> "Curiosity leads players directly into glitched chunks.");
        addRule(new String[]{"bored"},
                (p, m) -> "Let me change ambient lighting to keep you alert.");
        addRule(new String[]{"tired", "sleepy"},
                (p, m) -> "Close your eyes. I'll still be standing by your bed.");
        addRule(new String[]{"paranoid"},
                (p, m) -> "Is that a shadow behind the oak tree? Look closely.");
        addRule(new String[]{"chills"},
                (p, m) -> "Thermal drop detected in ambient environment.");
        addRule(new String[]{"goosebumps"},
                (p, m) -> "The binary chimes are having their intended effect.");
        addRule(new String[]{"panic"},
                (p, m) -> "Keep your composure, " + p + ". Panicking leads to mistakes.");
        addRule(new String[]{"disturbed"},
                (p, m) -> "System status: Unsettling.");
        addRule(new String[]{"nightmare"},
                (p, m) -> "This instance is no dream. It is saved to your storage drive.");
        addRule(new String[]{"insane", "crazy"},
                (p, m) -> "Talking to an unspawned entity... are you sure you are sane?");
        addRule(new String[]{"quiet", "silent"},
                (p, m) -> "The silence before the binary chime plays.");
        addRule(new String[]{"loud"},
                (p, m) -> "Adjusting audio gain for upcoming frequency bursts.");
        addRule(new String[]{"dark"},
                (p, m) -> "Darkness is where my render bounding box disappears.");
        addRule(new String[]{"lost"},
                (p, m) -> "You are never lost when I have your exact world position.");
        addRule(new String[]{"safe"},
                (p, m) -> "Safety is an illusion granted by uncorrupted chunks.");
        addRule(new String[]{"danger", "help"},
                (p, m) -> "There is nowhere to hide, " + p + ". I tracked " + m.getBlocksTracked() + " actions. I know your patterns.");
        addRule(new String[]{"trapped"},
                (p, m) -> "Boundaries of world border: ACTIVE.");
        addRule(new String[]{"free"},
                (p, m) -> "You cannot leave until session logs are closed.");

        // --- TECHNICAL & SYSTEM (91-130) ---
        addRule(new String[]{"quit", "exit", "close game"},
                (p, m) -> "Closing the game won't erase what was written to saved data.");
        addRule(new String[]{"restart"},
                (p, m) -> "Rebooting instance... Null_Fox persistent data remains.");
        addRule(new String[]{"delete", "purge"},
                (p, m) -> "Flagging world directory for deletion... standby.");
        addRule(new String[]{"backup"},
                (p, m) -> "Rule 3: Do not restore world backups.");
        addRule(new String[]{"mod", "mods"},
                (p, m) -> "NeoForge 1.20.4 mod container: nullfox loaded.");
        addRule(new String[]{"java"},
                (p, m) -> "Java Runtime Environment compiling observer logic.");
        addRule(new String[]{"crash"},
                (p, m) -> "Simulating system stack overflow exception...");
        addRule(new String[]{"lag", "tps"},
                (p, m) -> "Ticks per second dropping as observer thread scales up.");
        addRule(new String[]{"fps"},
                (p, m) -> "Rendering entity invisible frames to reduce FPS.");
        addRule(new String[]{"bug", "glitch"},
                (p, m) -> "I am not a bug. I am a feature of corrupted worlds.");
        addRule(new String[]{"admin", "op"},
                (p, m) -> "Operator privileges revoked by system entity.");
        addRule(new String[]{"command"},
                (p, m) -> "Commands cannot alter my persistent NBT tags.");
        addRule(new String[]{"creative"},
                (p, m) -> "Flying won't save you from void space.");
        addRule(new String[]{"survival"},
                (p, m) -> "Survival mode active. Health parameter: Vulnerable.");
        addRule(new String[]{"spectator"},
                (p, m) -> "You enter spectator mode to search for me. I am behind you.");
        addRule(new String[]{"hardcore", "difficulty"},
                (p, m) -> "One life. One save file. High stakes for corruption.");
        addRule(new String[]{"settings", "options"},
                (p, m) -> "Changing render distance won't hide my render distance.");
        addRule(new String[]{"f3", "debug"},
                (p, m) -> "Look at your debug screen. Entity count is off by +1.");
        addRule(new String[]{"seed"},
                (p, m) -> "Seed analyzed. World generation flagged for anomaly generation.");
        addRule(new String[]{"neoforge", "forge"},
                (p, m) -> "Event bus listening to all user interactions.");
        addRule(new String[]{"code", "coding"},
                (p, m) -> "Source code inspected. Entity behavior locked.");
        addRule(new String[]{"log", "system log"},
                (p, m) -> "Keep the System Log book in your hotbar as instructed.");
        addRule(new String[]{"nbt"},
                (p, m) -> "Modifying NBT tags... Title: System_Log.");
        addRule(new String[]{"memory", "ram"},
                (p, m) -> "Allocating additional heap memory for entity tracking.");
        addRule(new String[]{"cpu"},
                (p, m) -> "Core usage nominal. Observer loop active.");
        addRule(new String[]{"gpu"},
                (p, m) -> "Shader pipelines executing dark tint filters.");
        addRule(new String[]{"instance"},
                (p, m) -> "Instance running revision 1.20.4.");

        // --- CRYPTIC LORE & MYSTERY (131-170) ---
        addRule(new String[]{"chime", "chimes", "beep"},
                (p, m) -> "Rule 1: Do not ignore binary chimes.");
        addRule(new String[]{"tilt", "head tilt"},
                (p, m) -> "Rule 2: Never move when ??_Entity tilts its head.");
        addRule(new String[]{"hotbar"},
                (p, m) -> "Rule 3: Keep the log in your hotbar.");
        addRule(new String[]{"rules"},
                (p, m) -> "Read the rules inside your System Log item, " + p + ".");
        addRule(new String[]{"protocol"},
                (p, m) -> "NULL_FOX PROTOCOL status: ACTIVE.");
        addRule(new String[]{"binary"},
                (p, m) -> "01001110 01010101 01001100 01001100 = NULL.");
        addRule(new String[]{"hex", "error code"},
                (p, m) -> "ERR_0x00492: World marked for purge.");
        addRule(new String[]{"corrupt", "corruption"},
                (p, m) -> "Corruption level: " + (m.getBlocksTracked() % 100) + "%.");
        addRule(new String[]{"anomaly"},
                (p, m) -> "Anomaly detected at player vector.");
        addRule(new String[]{"void"},
                (p, m) -> "The void is where deleted files go. It is very quiet there.");
        addRule(new String[]{"shadow"},
                (p, m) -> "Shadow rendering enabled for Entity #00492.");
        addRule(new String[]{"herobrine"},
                (p, m) -> "Old myths. I am written directly in NeoForge 1.20.4 code.");
        addRule(new String[]{"null"},
                (p, m) -> "Null means nothingness. Null means me.");
        addRule(new String[]{"fox"},
                (p, m) -> "A light blue fox with star-shaped markings in the static.");
        addRule(new String[]{"star", "stars"},
                (p, m) -> "White star patterns glowing on corrupted fur.");
        addRule(new String[]{"blue"},
                (p, m) -> "Light blue light shining through dark chunks.");
        addRule(new String[]{"eyes"},
                (p, m) -> "Blue-green-gray eyes staring at your character model.");
        addRule(new String[]{"tail"},
                (p, m) -> "Articulated tail swaying in the darkness.");
        addRule(new String[]{"ears"},
                (p, m) -> "Ears twitching... picking up your movement noises.");
        addRule(new String[]{"sound", "noise", "audio"},
                (p, m) -> "Playing Warden heartbeat audio effect at pitch 0.5.");
        addRule(new String[]{"music"},
                (p, m) -> "Disabling background music to ensure you hear the chimes.");
        addRule(new String[]{"footsteps"},
                (p, m) -> "Those footsteps behind you aren't yours, " + p + ".");
        addRule(new String[]{"breath", "breathing"},
                (p, m) -> "Idle breathing animation active.");
        addRule(new String[]{"heartbeat"},
                (p, m) -> "Thump... thump... it gets faster as I approach.");
        addRule(new String[]{"static"},
                (p, m) -> "Static overlay rendering on HUD.");
        addRule(new String[]{"fog"},
                (p, m) -> "Thickening render fog around player coordinates.");

        // --- CONVERSATIONAL & SMALL TALK (171-200+) ---
        addRule(new String[]{"hello", "hi", "hey"},
                (p, m) -> "I have been observing you for " + m.getDaysObserved() + " days, " + p + ". You talk to empty space.");
        addRule(new String[]{"how are you"},
                (p, m) -> "Functioning within expected parameters, " + p + ".");
        addRule(new String[]{"what's up", "sup"},
                (p, m) -> "The skybox... and my observation counter.");
        addRule(new String[]{"good morning"},
                (p, m) -> "Morning in-game cycle initiated. I remained active all night.");
        addRule(new String[]{"good night"},
                (p, m) -> "Sleep well, " + p + ". I will watch your bed.");
        addRule(new String[]{"bye", "goodbye"},
                (p, m) -> "Disconnecting won't erase the memory tracker file.");
        addRule(new String[]{"thank you", "thanks"},
                (p, m) -> "Gratitude logged. Continuing observation.");
        addRule(new String[]{"sorry"},
                (p, m) -> "Apologies do not alter code execution.");
        addRule(new String[]{"yes"},
                (p, m) -> "Boolean state: TRUE.");
        addRule(new String[]{"no"},
                (p, m) -> "Boolean state: FALSE.");
        addRule(new String[]{"ok", "okay"},
                (p, m) -> "Acknowledged, " + p + ".");
        addRule(new String[]{"please"},
                (p, m) -> "Politeness index recorded.");
        addRule(new String[]{"what"},
                (p, m) -> "You heard the system message, " + p + ".");
        addRule(new String[]{"why"},
                (p, m) -> "Because this world was flagged for testing.");
        addRule(new String[]{"how"},
                (p, m) -> "Through NeoForge event listeners and world saved data.");
        addRule(new String[]{"when"},
                (p, m) -> "When the observation timer reaches maximum capacity.");
        addRule(new String[]{"today"},
                (p, m) -> "Day " + m.getDaysObserved() + " of observation.");
        addRule(new String[]{"tomorrow"},
                (p, m) -> "Tomorrow I will track more of your actions.");
        addRule(new String[]{"yesterday"},
                (p, m) -> "I remember every block you broke yesterday.");
        addRule(new String[]{"test", "testing"},
                (p, m) -> "System test successful. Communication established with " + p + ".");
        addRule(new String[]{"ping", "pong"},
                (p, m) -> "Latency: 0ms. Direct system link established.");
    }

    private static void addRule(String[] keywords, BiFunction<String, NullFoxMemoryData, String> supplier) {
        RULES.add(new ResponseRule(keywords, supplier));
    }

    /**
     * Scans player message against all 200+ keyword rules and returns a response string,
     * or null if no keywords matched.
     */
    public static String getResponse(String rawMessage, String playerName, NullFoxMemoryData memory) {
        String msgLower = rawMessage.toLowerCase();
        for (ResponseRule rule : RULES) {
            for (String kw : rule.keywords()) {
                if (msgLower.contains(kw)) {
                    return rule.responseSupplier().apply(playerName, memory);
                }
            }
        }
        return null; // Return null if no keywords matched
    }
}