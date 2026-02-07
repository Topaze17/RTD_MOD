package com.example.magicmod.capabilities.mana;

import net.minecraft.server.level.ServerPlayer;

/**
 * Represents the Mana capability attached to a player.
 * <p>
 * This interface defines helper methods for modifying mana values
 * with server-side syncing. It extends ManaData for core data access.
 * <p>
 * @author Topaze17
 */
public interface CapabilityMana extends ManaData {

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
}
