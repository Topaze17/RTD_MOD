package com.example.magicmod.client;

import com.example.magicmod.MagicMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class Keybindings {
    public static final Keybindings INSTANCE = new Keybindings();

    private Keybindings() {}

    private final KeyMapping.Category MAGIC_KEYS_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MagicMod.MODID, "magic_casting_key"));

    public final KeyMapping magicCastingKey = new KeyMapping(
            "key." + MagicMod.MODID + ".magic_casting_key", // key name
            KeyConflictContext.IN_GAME, // type
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V, // default key
            MAGIC_KEYS_CATEGORY, // key category
            -1
    );

    public final KeyMapping magicPacketKey = new KeyMapping(
            "key." + MagicMod.MODID + ".magic_packet_key", // key name
            KeyConflictContext.IN_GAME, // type
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B, // default key
            MAGIC_KEYS_CATEGORY,
            -1
    );
}
