package com.example.magicmod.capabilities.mana;
/**
 * Represents the Mana capability for network operations.
 * <p>
 * This interface defines setters for modifying mana values
 * during network synchronization. It extends ManaData for core data access.
 * <p>
 * @author Topaze17
 */
public interface NetworkMana extends ManaData {
    /**
     * Sets the current mana value.
     * <p>
     * Implementations should clamp this value between 0 and MaxMana.
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
}
