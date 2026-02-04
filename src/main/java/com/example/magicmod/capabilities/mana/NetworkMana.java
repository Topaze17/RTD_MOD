package com.example.magicmod.capabilities.mana;

public interface NetworkMana {
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
