package com.example.magicmod.client;

import com.example.magicmod.MagicMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class KeyRegister {
    @Mod.EventBusSubscriber(modid = MagicMod.MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        private ClientModEvents() {}
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(Keybindings.INSTANCE.magicCastingKey);
            event.register(Keybindings.INSTANCE.magicPacketKey);
        }

    }
}
