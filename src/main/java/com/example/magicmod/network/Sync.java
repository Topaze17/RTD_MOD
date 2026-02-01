package com.example.magicmod.network;

import com.example.magicmod.capabilities.ModCapabilities;
import net.minecraft.server.level.ServerPlayer;

/**

 * Class to make a layer on top of NetworkHandler
 * and give function to sync capabilities or other things that have an instance on
 * server side and client side.
 * <p>
 * @author Topaze17
 */

public final class Sync {
    private Sync() {}

    /**
     * a function to sync mana of a certain player.
     * @param player {@link ServerPlayer} instance representing the player who will get his mana sync
     */
    public static void syncManaTo(ServerPlayer player) {
        player.getCapability(ModCapabilities.MANA).ifPresent(m -> {
            NetworkHandler.sendManaSyncTo(player, m.getMana(), m.getMaxMana());
        });
    }
}


