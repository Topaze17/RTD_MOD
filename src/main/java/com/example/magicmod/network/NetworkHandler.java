package com.example.magicmod.network;


import com.example.magicmod.capabilities.ModCapabilities;
import com.example.magicmod.capabilities.mana.NetworkMana;
import com.example.magicmod.network.packets.ManaSyncS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

/**
 * Classic Class for netword handling
 * <p>
 * @author Topaze17
 */
public final class NetworkHandler {
    private NetworkHandler() {}

    private static final int PROTOCOL_VERSION = 1;
    //instance of the channel
    public static final SimpleChannel INSTANCE = ChannelBuilder
            .named(Identifier.fromNamespaceAndPath("magicmod", "main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel();

    private static int id = 0;

    /** register function to register messageBuilder for each packet
     * in the end if you have a packet you need a messageBuilder here
     * may change to payload if the forge is updated but currently not everything's is set up for it*/
    public static void register() {
        /*
         * a message builder for the packet of the mana if you another type of packet take this
         * as a template.
         */
        INSTANCE.messageBuilder(ManaSyncS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ManaSyncS2CPacket::encode)
                .decoder(ManaSyncS2CPacket::decode)
                .consumerMainThread(NetworkHandler::handleManaSync)
                .add();
    }

    /** A function to send mana specifically to something in this case the player */
    public static void sendManaSyncTo(net.minecraft.server.level.ServerPlayer player, int mana, int maxMana) {
        INSTANCE.send(new ManaSyncS2CPacket(mana, maxMana),PacketDistributor.PLAYER.with(player));
    }

    /**
     * ===== Handlers =====
     * for each packet we need a handlers function to explain
     * to the code what to do with the info transported on the packet.
     * */
    private static void handleManaSync(ManaSyncS2CPacket payload, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player == null) return;

            player.getCapability(ModCapabilities.MANA).ifPresent(m -> {
                NetworkMana mana = (NetworkMana) m;
                mana.setMaxMana(payload.maxMana());
                mana.setMana(payload.mana());
            });
        });
        ctx.setPacketHandled(true);
    }
}
