package com.example.magicmod.capabilities.mana;

import com.example.magicmod.network.Sync;
import net.minecraft.server.level.ServerPlayer;

public class Mana implements IMana {
    private int mana;
    private int maxMana;
    public Mana() {
        this(100,100);
    }
    public Mana(int mana, int maxMana) {
        this.mana = mana;
        this.maxMana = maxMana;
    }
    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public int getMaxMana() {
        return maxMana;
    }

    @Override
    public void setMana(int value) {
        mana = Math.max(0, Math.min(value, maxMana));
    }

    @Override
    public void setMaxMana(int value) {
        maxMana = Math.max(0, value);
    }

    @Override
    public void addMana(ServerPlayer player, int value) {
        if (player.isCreative() || player.isSpectator()) return;
        setMana(mana + value);
        Sync.syncManaTo(player);
    }

    @Override
    public void addMaxMana(ServerPlayer player, int value) {
        if (player.isCreative() || player.isSpectator()) return;
        setMaxMana(maxMana + value);
        Sync.syncManaTo(player);
    }
    @Override
    public void addMana(int value) {
        setMana(mana + value);
    }

    @Override
    public void addMaxMana(int value) {
        setMaxMana(maxMana + value);
    }
}

