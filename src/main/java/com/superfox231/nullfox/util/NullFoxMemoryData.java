package com.superfox231.nullfox.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class NullFoxMemoryData extends SavedData {
    private int daysObserved = 0;
    private int blocksPlacedOrBroken = 0;
    private boolean hasConfrontedPlayer = false;

    // Factory for saving/loading data in world files
    public static SavedData.Factory<NullFoxMemoryData> factory() {
        return new SavedData.Factory<>(NullFoxMemoryData::new, NullFoxMemoryData::load, null);
    }

    public static NullFoxMemoryData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(factory(), "nullfox_memory");
    }

    public static NullFoxMemoryData load(CompoundTag tag) {
        NullFoxMemoryData data = new NullFoxMemoryData();
        data.daysObserved = tag.getInt("DaysObserved");
        data.blocksPlacedOrBroken = tag.getInt("BlocksTracked");
        data.hasConfrontedPlayer = tag.getBoolean("HasConfronted");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("DaysObserved", this.daysObserved);
        tag.putInt("BlocksTracked", this.blocksPlacedOrBroken);
        tag.putBoolean("HasConfronted", this.hasConfrontedPlayer);
        return tag;
    }

    public int getDaysObserved() { return daysObserved; }
    public void incrementDays() { this.daysObserved++; setDirty(); }

    public int getBlocksTracked() { return blocksPlacedOrBroken; }
    public void addBlockActivity() { this.blocksPlacedOrBroken++; setDirty(); }

    public boolean hasConfronted() { return hasConfrontedPlayer; }
    public void setConfronted(boolean confronted) { this.hasConfrontedPlayer = confronted; setDirty(); }
}