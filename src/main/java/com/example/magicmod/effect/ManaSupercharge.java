package com.example.magicmod.effect;

import com.example.magicmod.capabilities.ModCapabilities;
import com.example.magicmod.capabilities.mana.Mana;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

/**
 * Mana Supercharge effect:
 * - Total duration: 800 ticks (40s)
 * - First 400 ticks: doubles max mana and blocks mana regeneration
 * - Last 400 ticks: restores max mana and continues to block regeneration
 *
 * Uses the modifier system: adds "effect_mana_supercharge" to the maxMana HashMap
 */
public class ManaSupercharge extends MobEffect {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String MODIFIER_ID = "effect_mana_supercharge";

    // Transition point (400 ticks remaining)
    private static final int REGEN_BLOCK_DURATION = 400;

    public ManaSupercharge(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

    /**
     * Apply effect: double base max mana using modifier system and block regeneration.
     */
    @Override
    public void onEffectStarted(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof ServerPlayer player) {
            // TODO: Remove logger before production
            LOGGER.info("ManaSupercharge.onEffectStarted for player: {}", player.getName().getString());

            player.getCapability(ModCapabilities.MANA).ifPresent(mana -> {
                int baseMana = mana.getMaxManaModifier(BASE_KEY);

                // Do NOT reapply the modifier (would increase maxMana without granting current mana).
                if (mana.isInRegenBlock() && !mana.hasMaxManaModifier(MODIFIER_ID)) {
                    LOGGER.info("ManaSupercharge.onEffectStarted - In regen-block phase and modifier absent, skipping reapplication");
                    return;
                }

                // This prevents overwriting a client-side maxMana display when reconnecting or in edge cases
                if (!mana.hasMaxManaModifier(MODIFIER_ID) && mana.getMaxMana() != baseMana) {
                    LOGGER.info("ManaSupercharge.onEffectStarted - Current max ({}) differs from base ({}), skipping modifier application to avoid display inconsistencies", mana.getMaxMana(), baseMana);
                    if (mana.isInRegenBlock()) mana.setInRegenBlock(true);
                    return;
                }

                // Check if modifier already exists (reconnection or already active)
                if (mana.hasMaxManaModifier(MODIFIER_ID)) {
                    LOGGER.info("ManaSupercharge modifier already active, skipping reapplication");
                    mana.setInRegenBlock(true);
                    return;
                }

                // TODO: Remove logger before production
                LOGGER.info("ManaSupercharge.onEffectStarted - BaseMana: {}, CurrentMaxMana: {}",
                    baseMana, mana.getMaxMana());

                mana.addMaxManaModifier(player, MODIFIER_ID, baseMana);
                mana.addMana(player, baseMana);

                mana.setInRegenBlock(true);

                // TODO: Remove logger before production
                LOGGER.info("ManaSupercharge.onEffectStarted - AFTER - Mana: {}, MaxMana: {}",
                    mana.getMana(), mana.getMaxMana());
            });
        }
    }

    /**
     * Transition behavior: at 400 ticks remaining restore max mana and clamp current mana.
     */
    @Override
    public boolean applyEffectTick(ServerLevel pServerLevel, LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof ServerPlayer player) {
            player.getCapability(ModCapabilities.MANA).ifPresent(mana -> {
                mana.removeMaxManaModifier(player, MODIFIER_ID);
            });
        }
        return true;
    }

    /**
     * Called when the effect is removed from an entity.
     * Remove modifier and cleanup state.
     */
    public static void onEffectRemoved(LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof ServerPlayer player) {
            // TODO: Remove logger before production
            LOGGER.info("ManaSupercharge.onEffectRemoved for player: {}", player.getName().getString());

            player.getCapability(ModCapabilities.MANA).ifPresent(mana -> {
                if (mana.hasMaxManaModifier(MODIFIER_ID)) {
                    mana.removeMaxManaModifier(player, MODIFIER_ID);
                }

                mana.setInRegenBlock(false);
            });
        }
    }

    /**
     * Determines whether the effect should be applied on this tick.
     * Returns true only at the transition point (400 ticks remaining)
     * to restore max mana before entering the regen block phase.
     */
    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration == REGEN_BLOCK_DURATION;
    }
}
