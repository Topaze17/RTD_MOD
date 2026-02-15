package com.example.magicmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;

public record MagicCastingC2SPacket() {

    public static void encode(MagicCastingC2SPacket msg, FriendlyByteBuf buffer) {}

    public static MagicCastingC2SPacket decode(FriendlyByteBuf buffer) {
        return new MagicCastingC2SPacket();
    }

}
