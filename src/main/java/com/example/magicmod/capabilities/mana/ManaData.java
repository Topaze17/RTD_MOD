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
     * Get the value of a specific max mana modifier.
     *
     * @param sourceId unique identifier for the modifier
     * @return the modifier value, or 0 if not present
     */
    int getMaxManaModifier(String sourceId);

    /**
     * Check if a specific modifier is active.
     *
     * @param sourceId unique identifier for the modifier
     * @return true if the modifier exists and is non-zero
     */
    boolean hasMaxManaModifier(String sourceId);

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
