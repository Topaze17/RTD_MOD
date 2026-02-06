package com.example.magicmod.datagen;

import com.example.magicmod.MagicMod;
import com.example.magicmod.worldgen.ModBiomeModifiers;
import com.example.magicmod.worldgen.ModConfiguredFeatures;
import com.example.magicmod.worldgen.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;


import java.util.concurrent.CompletableFuture;


public class ModDatapackEntries extends DatapackBuiltinEntriesProvider {
    public static final ResourceKey<Registry<BiomeModifier>> BIOME_MODIFIERS_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath("forge", "biome_modifier")
    );
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(BIOME_MODIFIERS_KEY, ModBiomeModifiers::bootstrap);

    public ModDatapackEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, MagicMod.MODID);
    }
}
