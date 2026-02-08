package com.example.magicmod;

import com.example.magicmod.capabilities.ModCapabilities;
import com.example.magicmod.capabilities.mana.ManaProvider;
import com.example.magicmod.block.register.ModBlockRegister;
import com.example.magicmod.effect.ManaSupercharge;
import com.example.magicmod.effect.ModEffects;
import com.example.magicmod.item.register.ModItemRegister;
import com.example.magicmod.potion.ModPotion;
import com.example.magicmod.network.NetworkHandler;
import com.example.magicmod.network.Sync;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MagicMod.MODID)
public final class MagicMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "magicmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "magicmod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);



    // Creates a creative tab with the id "magicmod:example_tab" for the example items, that is placed after the combat tab
    /**
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example items to the tab. For your own tabs, this method is preferred over the event
            }).build());
     */
    /**public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB_2 = CREATIVE_MODE_TABS.register("mana_crystal_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> CRYSTAL_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(CRYSTAL_BLOCK_ITEM.get()); // Add the example items to the tab. For your own tabs, this method is preferred over the event
            }).build());*/

    public MagicMod(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();
        // Register the commonSetup method for modloading
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ModBlockRegister.RegisterBlock(modBusGroup);
        // Register the Deferred Register to the mod event bus so items get registered
        ModItemRegister.RegisterItem(modBusGroup);

        // Register the Deferred Register to the mod event bus so effects get registered
        ModEffects.registerEffect(modBusGroup);
        // Register to the mod event bus potions.
        ModPotion.registerPotion(modBusGroup);

        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modBusGroup);

        // Register the items to a creative tab
        BuildCreativeModeTabContentsEvent.BUS.addListener(MagicMod::addCreative);


        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        //register network handler to handle packet between server and client
        event.enqueueWork(NetworkHandler::register);
        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block items to the building blocks tab

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(ModBlockRegister.MANA_CRYSTAL_BLOCK_ITEM);
        event.accept(ModBlockRegister.FIRE_CRYSTAL_BLOCK_ITEM);
        event.accept(ModItemRegister.MANA_CRYSTAL_SHARD);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        private  ClientModEvents(){}
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
    /**
     * Class that represent event that append on server and how we want to add interaction if those event trigger
     * */
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerModEvents {
        /**
         * things that happen when the event to attach capabilities triggers (use to add our mod capabilities)
         * */
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent event) {
            if (event.getObject() instanceof Player) {
                ManaProvider provider = new ManaProvider();
                event.addCapability(Identifier.parse("magicmod:mana"), provider);
                event.addListener(provider::invalidate);
            }
        }
        /**
         * things that happen when the login event triggers
         * */
        @SubscribeEvent
        public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer sp) {
                // TODO: Remove debug logger before production
                LOGGER.info("=== Player Login === UUID: {}", sp.getUUID());

                Sync.syncManaTo(sp);
            }
        }

        @SubscribeEvent
        public static void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getEntity() instanceof ServerPlayer sp) {
                // TODO: Remove debug logger before production
                LOGGER.info("=== Player Logout === UUID: {}", sp.getUUID());

            }
        }

        /**
         * Detects when a mob effect is removed from an entity.
         * Used to cleanup the Mana Supercharge effect properly.
         * */
        @SubscribeEvent
        public static void onEffectRemoved(MobEffectEvent.Remove event) {
            if (event.getEffect() != null && event.getEffect().equals(ModEffects.MANA_SUPERCHARGE.getHolder().get())) {
                // TODO: Remove logger before production
                LOGGER.info("Mana Supercharge effect removed for entity: {}",
                    event.getEntity().getName().getString());
                ManaSupercharge.onEffectRemoved(event.getEntity());
            }
        }

        /**
         * Detects when a mob effect expires naturally on an entity.
         * Used to cleanup the Mana Supercharge effect when it runs out.
         * */
        @SubscribeEvent
        public static void onEffectExpired(MobEffectEvent.Expired event) {
            if (event.getEffectInstance() != null &&
                event.getEffectInstance().getEffect().equals(ModEffects.MANA_SUPERCHARGE.getHolder().get())) {
                // TODO: Remove logger before production
                LOGGER.info("Mana Supercharge effect expired for entity: {}",
                    event.getEntity().getName().getString());
                ManaSupercharge.onEffectRemoved(event.getEntity());
            }
        }
    }
}
