package com.example.magicmod.capabilities.mana;

import com.example.magicmod.network.Sync;
import net.minecraft.server.level.ServerPlayer;

public class Mana implements CapabilityMana, NetworkMana {
    private int mana;
    private int maxMana;
    private boolean isInRegenBlock;

    public Mana() {
        this(100,100);
    }
    public Mana(int mana, int maxMana) {
        this.mana = mana;
        this.maxMana = maxMana;
        this.isInRegenBlock = false;
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
        mana = Math.min(maxMana, mana);
    }

    @Override
    public boolean isInRegenBlock() {
        return isInRegenBlock;
    }

    @Override
    public void setInRegenBlock(boolean value) {
        isInRegenBlock = value;
    }

    @Override
    public void addMana(ServerPlayer player, int value) {
        if (player.isCreative() || player.isSpectator()) return;

        if (value > 0 && isInRegenBlock) return;

        setMana(mana + value);
        Sync.syncManaTo(player);
    }

    @Override
    public void addMaxMana(ServerPlayer player, int value) {
        if (player.isCreative() || player.isSpectator()) return;
        setMaxMana(maxMana + value);
        Sync.syncManaTo(player);
    }
}

