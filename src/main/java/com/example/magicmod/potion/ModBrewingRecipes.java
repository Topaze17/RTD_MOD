package com.example.magicmod.potion;

import com.example.magicmod.item.register.ModItemRegister;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.brewing.BrewingRecipeRegisterEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import static com.example.magicmod.MagicMod.MODID;
/**
 * Registry class for custom brewing recipes added by MagicMod.
 * Handles the registration of potion brewing recipes.
 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModBrewingRecipes {
    private ModBrewingRecipes() {}

    /**
     * Registers all brewing recipes for custom potions.
     * Called during the brewing recipe registration event.
     *
     * @param event The brewing recipe register event
     */
    @SubscribeEvent
    public static void registerBrewingRecipes(BrewingRecipeRegisterEvent event) {
        // Gets the builder to add recipes to
        PotionBrewing.Builder builder = event.getBuilder();

        // Mana // Mana Potion: Awkward Potion + Mana Crystal Shard
        // Will add brewing recipes for all container potions (e.g. potion, splash potion, lingering potion)
        builder.addMix(
                // The initial potion to apply to
                Potions.AWKWARD,
                // The brewing ingredient. This is the item at the top of the brewing stand.
                ModItemRegister.MANA_CRYSTAL_SHARD.get(),
                // The resulting potion
                ModPotion.MANA_POTION.getHolder().get()
        );

        // Mana Supercharge Potion: Mana Potion + Sugar
        builder.addMix(
                // Start from the Mana Potion
                ModPotion.MANA_POTION.getHolder().get(),
                // Add Sugar for the supercharge effect
                Items.SUGAR,
                // Results in Mana Supercharge Potion
                ModPotion.MANA_SUPERCHARGE_POTION.getHolder().get()
        );
    }
}
