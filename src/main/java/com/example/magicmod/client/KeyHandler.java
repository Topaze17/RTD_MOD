package com.example.magicmod.client;

import com.example.magicmod.MagicMod;
import com.example.magicmod.network.NetworkHandler;
import com.example.magicmod.network.packets.MagicCastingC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public final class KeyHandler {
    @Mod.EventBusSubscriber(modid = MagicMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        private  ClientForgeEvents(){}

        private static final Component MAGIC_KEY_PRESSED =
                Component.translatable("message." + MagicMod.MODID + ".magic_key_pressed");
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null && event.getKey() == Keybindings.INSTANCE.magicCastingKey.getKey().getValue() && Keybindings.INSTANCE.magicCastingKey.isDown()) {
                Keybindings.INSTANCE.magicCastingKey.consumeClick();
                minecraft.player.displayClientMessage(MAGIC_KEY_PRESSED, true);
            }

            if (minecraft.player != null && event.getKey() == Keybindings.INSTANCE.magicPacketKey.getKey().getValue() && Keybindings.INSTANCE.magicPacketKey.isDown()) {
                Keybindings.INSTANCE.magicPacketKey.consumeClick();
                NetworkHandler.sendMagicCastToServer(new MagicCastingC2SPacket());
            }
        }
    }
}
