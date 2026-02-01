package com.example.magicmod.network.cache;

/**
 * a class to cache Mana to keep diplaying it even
 * if we are dead as forge drop capabilities on death
 * <p>
 * @author Topaze17
 */
public final class ManaClientCache {
    private ManaClientCache() {}

    public static int mana = 0;
    public static int maxMana = 0;
    public static boolean hasData = false;

    public static void set(int m, int mm) {
        mana = m;
        maxMana = mm;
        hasData = true;
    }
}
