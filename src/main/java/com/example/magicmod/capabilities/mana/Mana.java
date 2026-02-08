package com.example.magicmod.capabilities.mana;

import com.example.magicmod.network.Sync;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class Mana implements CapabilityMana, NetworkMana {
    private int mana;
    private final Map<String, Integer> maxManaModifiers;
    private boolean isInRegenBlock;

    public static final String BASE_KEY = "base";

    public Mana(int mana, int maxMana) {
        this.mana = mana;
        this.maxManaModifiers = new HashMap<>();
        this.maxManaModifiers.put(BASE_KEY, maxMana);
        this.isInRegenBlock = false;
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public int getMaxMana() {
        return maxManaModifiers.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public void setMana(int value) {
        int currentMax = getMaxMana();
        mana = Math.max(0, Math.min(value, currentMax));
    }

    @Override
    public void setMaxMana(int value) {
        maxManaModifiers.put(BASE_KEY, Math.max(0, value));
        mana = Math.min(getMaxMana(), mana);
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
    public void addMaxManaModifier(ServerPlayer player, String sourceId, int value) {
        if (player.isCreative() || player.isSpectator()) return;

        if (BASE_KEY.equals(sourceId)) {
            throw new IllegalArgumentException("Cannot modify base max mana. Base mana is set at initialization only.");
        }

        maxManaModifiers.put(sourceId, value);

        int newMax = getMaxMana();
        if (mana > newMax) {
            mana = newMax;
        }

        Sync.syncManaTo(player);
    }

    @Override
    public void removeMaxManaModifier(ServerPlayer player, String sourceId) {
        if (player.isCreative() || player.isSpectator()) return;

        if (BASE_KEY.equals(sourceId)) {
            throw new IllegalArgumentException("Cannot remove base max mana.");
        }

        maxManaModifiers.remove(sourceId);

        int newMax = getMaxMana();
        if (mana > newMax) {
            mana = newMax;
        }

        Sync.syncManaTo(player);
    }

    @Override
    public int getMaxManaModifier(String sourceId) {
        return maxManaModifiers.getOrDefault(sourceId, 0);
    }

    @Override
    public boolean hasMaxManaModifier(String sourceId) {
        return maxManaModifiers.containsKey(sourceId) && maxManaModifiers.get(sourceId) != 0;
    }

    public Map<String, Integer> getMaxManaModifiers() {
        return new HashMap<>(maxManaModifiers);
    }

    public void setMaxManaModifiers(Map<String, Integer> modifiers) {
        maxManaModifiers.clear();
        maxManaModifiers.putAll(modifiers);

        if (!maxManaModifiers.containsKey(BASE_KEY)) {
            maxManaModifiers.put(BASE_KEY, 1000);
        }

        mana = Math.min(getMaxMana(), mana);
    }
}
