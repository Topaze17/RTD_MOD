package com.example.magicmod.capabilities.mana;

/**
 * Base interface for mana data access.
 * <p>
 * This interface defines the core getters and setters for mana-related data.
 * Both gameplay and network interfaces inherit from this to ensure consistent access.
 * <p>
 */
public interface ManaData {
    /**
     * @return the current mana value
     */
    int getMana();

    /**
     * @return the maximum mana value
     */
    int getMaxMana();

    /**
     * @return true if mana regeneration is currently blocked
     */
    boolean isInRegenBlock();

    /**
     * Sets whether mana regeneration is blocked.
     *
     * @param value true to block regeneration, false to allow it
     */
    void setInRegenBlock(boolean value);
}
