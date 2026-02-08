package com.example.magicmod.worldgen;

import com.example.magicmod.MagicMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * class specifying where to place new block in our mod world gen.
 * <p>
 * @author Topaze17
 */
public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_MANA_CRYSTAL =
            ResourceKey.create(
                    ForgeRegistries.Keys.BIOME_MODIFIERS,
                    Identifier.fromNamespaceAndPath(MagicMod.MODID, "add_mana_crystal_block")
            );
    public static final ResourceKey<BiomeModifier> ADD_FIRE_CRYSTAL =
            ResourceKey.create(
                    ForgeRegistries.Keys.BIOME_MODIFIERS,
                    Identifier.fromNamespaceAndPath(MagicMod.MODID, "add_fire_crystal_block")
            );

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(
                ADD_MANA_CRYSTAL,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(
                                placedFeatures.getOrThrow(ModPlacedFeatures.MANA_CRYSTAL_PLACED)
                        ),
                        GenerationStep.Decoration.UNDERGROUND_DECORATION
                )
        );
        context.register(
                ADD_FIRE_CRYSTAL,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(
                                placedFeatures.getOrThrow(ModPlacedFeatures.FIRE_CRYSTAL_PLACED)
                        ),
                        GenerationStep.Decoration.UNDERGROUND_DECORATION
                )
        );
    }
}
