package com.example.magicmod.network;


import com.example.magicmod.capabilities.ModCapabilities;
import com.example.magicmod.capabilities.mana.NetworkMana;
import com.example.magicmod.network.packets.MagicCastingC2SPacket;
import com.example.magicmod.network.packets.ManaSyncS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

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

        INSTANCE.messageBuilder(MagicCastingC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(MagicCastingC2SPacket::encode)
                .decoder(MagicCastingC2SPacket::decode)
                .consumerMainThread(NetworkHandler::handleMagicCasting)
                .add();
    }

    public static void sendMagicCastToServer(MagicCastingC2SPacket packet) {
        INSTANCE.send(packet, PacketDistributor.SERVER.noArg());
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

    private static void handleMagicCasting(MagicCastingC2SPacket payload, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;

            ServerLevel level = player.level();

            // for now simply spawn a ghast fireball in front of the player
            EntityType<?> entity = ForgeRegistries.ENTITY_TYPES.getValue(Identifier.fromNamespaceAndPath("minecraft", "fireball"));

            /*List<Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>>> entities =
                    ForgeRegistries.ENTITY_TYPES.getEntries()
                        .stream()
                        .filter(e -> e.getValue().getBaseClass().isAssignableFrom(LivingEntity.class))
                        .toList();

            for (var e : entities) {
                System.out.println("\n" + e);
            }*/
            if (entity == null) return;

            Vec3 playerEyePos = player.getEyePosition();
            BlockPos entityPos = new BlockPos((int) playerEyePos.x, (int) playerEyePos.y, (int) playerEyePos.z);
                    /*.relative(player.getDirection())
                    .offset(player.getDirection().getUnitVec3i());*/

            entity.spawn(level, entityPos, EntitySpawnReason.MOB_SUMMONED);


            //EntityType<?> type = new LargeFireball(level, player, player.getEyePosition(), 1);


        });
    }
}
