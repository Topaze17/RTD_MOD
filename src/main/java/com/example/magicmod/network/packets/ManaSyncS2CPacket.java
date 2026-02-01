package com.example.magicmod.network.packets;


import com.example.magicmod.network.cache.ManaClientCache;
import net.minecraft.network.FriendlyByteBuf;
/**
 * a classic Packet class containing an encode and decode class
 * <p>
 * @author Topaze17
 */
public record ManaSyncS2CPacket(int mana, int maxMana)  {

    public static void encode(ManaSyncS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.mana());
        buf.writeVarInt(msg.maxMana());
    }

    public static ManaSyncS2CPacket decode(FriendlyByteBuf buf) {
        int mana = buf.readVarInt();
        int maxMana = buf.readVarInt();
        ManaClientCache.set(mana, maxMana);
        return new ManaSyncS2CPacket(mana, maxMana);
    }
}

