package com.example.magicmod.capabilities.mana;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.server.level.ServerPlayer;

/**
 * Represents the Mana capability attached to a player.
 * <p>
 * This interface defines both getters/setters and helper methods
 * for modifying mana values, with optional server-side syncing.
 * <p>
 * @author Topaze17
 */
public interface IMana {

    /**
     * @return the current mana value
     */
    int getMana();

    /**
     * @return the maximum mana value
     */
    int getMaxMana();

    /**
     * Sets the current mana value.
     * <p>
     * Implementations should clamp this value between 0 and {@link #getMaxMana()}.
     *
     * @param value the new mana value
     */
    void setMana(int value);

    /**
     * Sets the maximum mana value.
     * <p>
     * Implementations may want to clamp current mana
     * if it exceeds the new maximum.
     *
     * @param value the new maximum mana
     */
    void setMaxMana(int value);

    /**
     * Adds mana to this capability and synchronizes
     * the result to the given {@link ServerPlayer}.
     * <p>
     * This method should only be called on the server.
     *
     * @param player the server-side player to sync with
     * @param value  the amount of mana to add
     */
    void addMana(ServerPlayer player, int value);

    /**
     * Adds maximum mana and synchronizes
     * the result to the given {@link ServerPlayer}.
     * <p>
     * This method should only be called on the server.
     *
     * @param player the server-side player to sync with
     * @param value  the amount of max mana to add
     */
    void addMaxMana(ServerPlayer player, int value);

    /**
     * Adds mana locally without performing any network sync.
     * <p>
     * Useful for internal logic or client-side updates
     * driven by server synchronization.
     *
     * @param value the amount of mana to add
     */
    void addMana(int value);

    /**
     * Adds maximum mana locally without performing any network sync.
     *
     * @param value the amount of max mana to add
     */
    void addMaxMana(int value);
}

