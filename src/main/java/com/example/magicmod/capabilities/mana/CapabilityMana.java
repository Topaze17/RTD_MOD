package com.example.magicmod.capabilities.mana;

import net.minecraft.server.level.ServerPlayer;

/**
 * Represents the Mana capability attached to a player.
 * <p>
 * This interface defines helper methods for modifying mana values
 * with server-side syncing. It extends ManaData for exposing the necessary for gameplay implementation.
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
     * @param value  the amount of mana to add (can be negative for consumption)
     */
    void addMana(ServerPlayer player, int value);

    /**
     * Adds or updates a max mana modifier from a specific source.
     * <p>
     * Use this to manage temporary bonuses/debuffs from armor, effects, buffs, etc.
     * Each source has a unique ID and can be added/removed independently.
     * <p>
     * Naming convention: category_source
     * - Armor: "armor_<slot>" (armor_helmet, armor_chestplate, armor_leggings, armor_boots)
     * - Effects: "effect_<name>" (effect_mana_supercharge, effect_mana_boost)
     * - Buffs: "buff_<name>" (buff_mana_aura, buff_wizard_presence)
     * - Items: "item_<name>" (item_magic_ring, item_staff_of_power)
     * <p>
     * This method should only be called on the server.
     *
     * @param player   the server-side player to sync with
     * @param sourceId unique identifier for this bonus source
     * @param value    the modifier amount
     */
    void addMaxManaModifier(ServerPlayer player, String sourceId, int value);

    /**
     * Removes a max mana modifier from a specific source.
     * <p>
     * This method should only be called on the server.
     *
     * @param player   the server-side player to sync with
     * @param sourceId unique identifier for the bonus source to remove
     */
    void removeMaxManaModifier(ServerPlayer player, String sourceId);
}
