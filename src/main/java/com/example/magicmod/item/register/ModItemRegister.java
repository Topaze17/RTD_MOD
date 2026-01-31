package com.example.magicmod.item.register;

import com.example.magicmod.MagicMod;
import com.example.magicmod.block.register.ModBlockRegister;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemRegister {

    // Create a Deferred Register to hold Items which will all be registered under the "magicmod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MagicMod.MODID);

    public static final RegistryObject<Item> MANA_CRYSTAL_SHARD =
            ITEMS.register("mana_crystal_shard",
                    () -> new Item(new Item.Properties().setId(ITEMS.key("mana_crystal_shard"))));
    public static void RegisterItem(BusGroup busGroup) {
        ITEMS.register(busGroup);
    }
}
