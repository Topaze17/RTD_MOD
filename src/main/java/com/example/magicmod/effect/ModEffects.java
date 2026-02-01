package com.example.magicmod.effect;

import com.example.magicmod.MagicMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registry class for custom mob effects added by MagicMod.
 * Handles the registration of all custom effects to the Forge registry.
 */
public class ModEffects {
    private ModEffects (){}
    /**
     * Deferred register for mob effects.
     * All effects will be registered under the "magicmod" namespace.
     */
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MagicMod.MODID);

    /**
     * Mana Regeneration effect.
     * A neutral effect that grants experience points to players over time.
     * Color: 0x36ebab (cyan)
     */
    public static final RegistryObject<MobEffect> MANA_REGENERATION = MOB_EFFECTS.register("mana_regeneration",
            () -> new ManaRegeneration(MobEffectCategory.NEUTRAL, 0x36ebab));

    /**
     * Registers all mob effects to the mod event bus.
     * This method should be called during mod initialization.
     *
     * @param busGroup The mod event bus group to register effects to
     */
    public static void registerEffect(BusGroup busGroup) {
        MOB_EFFECTS.register(busGroup);
    }
}
