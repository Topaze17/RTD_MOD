package com.example.magicmod.effect;

import com.example.magicmod.capabilities.ModCapabilities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;



/**
 * Custom mob effect that regenerates mana by granting experience points to players.
 * This effect gives 1 XP point per tick to any player affected by it.
 * The effect is categorized as NEUTRAL and displays with a cyan color (0x36ebab).
 */
public class ManaRegeneration extends MobEffect {
    /**
     * Constructor for the ManaRegeneration effect.
     *
     * @param pCategory The effect category (BENEFICIAL, HARMFUL, or NEUTRAL)
     * @param pColor The color of the effect icon in hexadecimal format
     */
    public ManaRegeneration(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    /**
     * Applies the effect's tick behavior.
     * Grants 1 mana point to the player each time this method is called.
     * Any blocking of mana regeneration (such as regen block phases) is handled internally by the Mana capability.
     *
     * @param pServerLevel The server level where the effect is being applied
     * @param pLivingEntity The living entity affected by this effect
     * @param pAmplifier The amplifier level of the effect
     * @return true if the effect was successfully applied
     */
    @Override
    public boolean applyEffectTick(ServerLevel pServerLevel, LivingEntity pLivingEntity, int pAmplifier) {

        if (pLivingEntity instanceof ServerPlayer player) {
            player.getCapability(ModCapabilities.MANA).ifPresent(m -> m.addMana( player, 1));
        }

        return true;
    }

    /**
     * Determines whether the effect should be applied on this tick.
     *
     * @param pDuration The duration remaining on the effect
     * @param pAmplifier The amplifier level of the effect
     * @return true to apply the effect every tick
     */
    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}
