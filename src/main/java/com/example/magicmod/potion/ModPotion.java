package com.example.magicmod.potion;

import com.example.magicmod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.example.magicmod.MagicMod;

/**
 * Registry class for custom potions added by MagicMod.
 * Handles the registration of all custom potions to the Forge registry.
 */
public class ModPotion {
    private ModPotion (){}
    /**
     * Deferred register for potions.
     * All potions will be registered under the "magicmod" namespace.
     */
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, MagicMod.MODID);

    /**
     * Mana Potion.
     * Applies the Mana Regeneration effect for 200 ticks (10 seconds) at level 0.
     * When consumed, grants the player experience points over time.
     */
    public static final RegistryObject<Potion> MANA_POTION = POTIONS.register("mana_potion",
            () -> new Potion( "mana_potion",new MobEffectInstance(ModEffects.MANA_REGENERATION.getHolder().get(), 200, 0)));

    /**
     * Registers all potions to the mod event bus.
     * This method should be called during mod initialization.
     *
     * @param busGroup The mod event bus group to register potions to
     */
    public static void registerItem(BusGroup busGroup) {
        POTIONS.register(busGroup);
    }

}

