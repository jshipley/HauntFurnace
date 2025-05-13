package com.jship.hauntfurnace.config;

import com.google.gson.GsonBuilder;
import com.jship.hauntfurnace.HauntFurnace;

import dev.architectury.platform.Platform;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Accessors(fluent=true)
public class HauntFurnaceConfig {
    public static ConfigClassHandler<HauntFurnaceConfig> HANDLER = ConfigClassHandler.createBuilder(HauntFurnaceConfig.class)
        .id(HauntFurnace.id("haunt_furnace_config"))
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .setPath(Platform.getConfigFolder().resolve("haunt_furnace.json5"))
            .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
            .setJson5(true)
            .build())
        .build();

    // Haunt furnace
    public static final boolean defaultHauntQuiet = false;
    @Getter @Setter @SerialEntry
    public static boolean hauntQuiet = defaultHauntQuiet;

    public static final boolean defaultHauntVanillaFuels = true;
    @Getter @Setter @SerialEntry
    public static boolean hauntVanillaFuels = defaultHauntVanillaFuels;

    public static final boolean defaultHauntCustomFuels = true;
    @Getter @Setter @SerialEntry
    public static boolean hauntCustomFuels = defaultHauntCustomFuels;

    public static final int defaultHauntEnergyUsage = 10;
    @Getter @Setter @SerialEntry
    public static int hauntEnergyUsage = defaultHauntEnergyUsage;

    public static final int defaultHauntEnergyCapacity = 1024;
    @Getter @Setter @SerialEntry
    public static int hauntEnergyCapacity = defaultHauntEnergyCapacity;

    public static final int defaultHauntEnergyMaxInsert = 32;
    @Getter @Setter @SerialEntry
    public static int hauntEnergyMaxInsert = defaultHauntEnergyMaxInsert;

    // Ender furnace
    public static final boolean defaultEnderQuiet = false;
    @Getter @Setter @SerialEntry
    public static boolean enderQuiet = defaultEnderQuiet;

    public static final boolean defaultEnderVanillaFuels = true;
    @Getter @Setter @SerialEntry
    public static boolean enderVanillaFuels = defaultEnderVanillaFuels;

    public static final boolean defaultEnderCustomFuels = true;
    @Getter @Setter @SerialEntry
    public static boolean enderCustomFuels = defaultEnderCustomFuels;

    public static final int defaultEnderEnergyUsage = 10;
    @Getter @Setter @SerialEntry
    public static int enderEnergyUsage = defaultEnderEnergyUsage;

    public static final int defaultEnderEnergyCapacity = 1024;
    @Getter @Setter @SerialEntry
    public static int enderEnergyCapacity = defaultEnderEnergyCapacity;

    public static final int defaultEnderEnergyMaxInsert = 32;
    @Getter @Setter @SerialEntry
    public static int enderEnergyMaxInsert = defaultEnderEnergyMaxInsert;

    public static Screen createConfig(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder()
                .save(() -> HANDLER.save())
                .title(Component.literal("Haunt Furnace"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("Haunt Furnace"))
                        .group(OptionGroup.createBuilder()
                            .name(Component.translatable("block.hauntfurnace.haunt_furnace"))
                            .option(Option.<Boolean>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.haunt_quiet"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.haunt_quiet.desc")))
                                    .binding(defaultHauntQuiet, HauntFurnaceConfig::hauntQuiet, HauntFurnaceConfig::hauntQuiet)
                                    .controller(TickBoxControllerBuilder::create)
                                    .build())
                            .option(Option.<Boolean>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.haunt_vanilla_fuels"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.haunt_vanilla_fuels.desc")))
                                    .binding(defaultHauntVanillaFuels, HauntFurnaceConfig::hauntVanillaFuels, HauntFurnaceConfig::hauntVanillaFuels)
                                    .controller(TickBoxControllerBuilder::create)
                                    .build())
                            .option(Option.<Boolean>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.haunt_custom_fuels"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.haunt_custom_fuels.desc")))
                                    .binding(defaultHauntCustomFuels, HauntFurnaceConfig::hauntCustomFuels, HauntFurnaceConfig::hauntCustomFuels)
                                    .controller(TickBoxControllerBuilder::create)
                                    .build())
                            .option(Option.<Integer>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.haunt_energy_usage"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.haunt_energy_usage.desc", 1, 100_000)))
                                    .binding(defaultHauntEnergyUsage, HauntFurnaceConfig::hauntEnergyUsage, HauntFurnaceConfig::hauntEnergyUsage)
                                    .controller(opt -> IntegerFieldControllerBuilder.create(opt).range(1, 100_000))
                                    .build())
                            .option(Option.<Integer>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.haunt_energy_capacity"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.haunt_energy_capacity.desc", 1, 1_000_000)))
                                    .binding(defaultHauntEnergyCapacity, HauntFurnaceConfig::hauntEnergyCapacity, HauntFurnaceConfig::hauntEnergyCapacity)
                                    .controller(opt -> IntegerFieldControllerBuilder.create(opt).range(1, 1_000_000))
                                    .build())
                            .option(Option.<Integer>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.haunt_energy_max_insert"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.haunt_energy_max_insert.desc", 1, 100_000)))
                                    .binding(defaultHauntEnergyMaxInsert, HauntFurnaceConfig::hauntEnergyMaxInsert, HauntFurnaceConfig::hauntEnergyMaxInsert)
                                    .controller(opt -> IntegerFieldControllerBuilder.create(opt).range(1, 100_000))
                                    .build())
                            .build())
                        .group(OptionGroup.createBuilder()
                            .name(Component.translatable("block.hauntfurnace.ender_furnace"))
                            .option(Option.<Boolean>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.ender_quiet"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.ender_quiet.desc")))
                                    .binding(defaultEnderQuiet, HauntFurnaceConfig::enderQuiet, HauntFurnaceConfig::enderQuiet)
                                    .controller(TickBoxControllerBuilder::create)
                                    .build())
                            .option(Option.<Boolean>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.ender_vanilla_fuels"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.ender_vanilla_fuels.desc")))
                                    .binding(defaultEnderVanillaFuels, HauntFurnaceConfig::enderVanillaFuels, HauntFurnaceConfig::enderVanillaFuels)
                                    .controller(TickBoxControllerBuilder::create)
                                    .build())
                            .option(Option.<Boolean>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.ender_custom_fuels"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.ender_custom_fuels.desc")))
                                    .binding(defaultEnderCustomFuels, HauntFurnaceConfig::enderCustomFuels, HauntFurnaceConfig::enderCustomFuels)
                                    .controller(TickBoxControllerBuilder::create)
                                    .build())
                            .option(Option.<Integer>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.ender_energy_usage"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.ender_energy_usage.desc", 1, 100_000)))
                                    .binding(defaultEnderEnergyUsage, HauntFurnaceConfig::enderEnergyUsage, HauntFurnaceConfig::enderEnergyUsage)
                                    .controller(opt -> IntegerFieldControllerBuilder.create(opt).range(1, 100_000))
                                    .build())
                            .option(Option.<Integer>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.ender_energy_capacity"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.ender_energy_capacity.desc", 1, 1_000_000)))
                                    .binding(defaultEnderEnergyCapacity, HauntFurnaceConfig::enderEnergyCapacity, HauntFurnaceConfig::enderEnergyCapacity)
                                    .controller(opt -> IntegerFieldControllerBuilder.create(opt).range(1, 1_000_000))
                                    .build())
                            .option(Option.<Integer>createBuilder()
                                    .name(Component.translatable("config.hauntfurnace.ender_energy_max_insert"))
                                    .description(OptionDescription
                                            .of(Component.translatable("config.hauntfurnace.ender_energy_max_insert.desc", 1, 100_000)))
                                    .binding(defaultEnderEnergyMaxInsert, HauntFurnaceConfig::enderEnergyMaxInsert, HauntFurnaceConfig::enderEnergyMaxInsert)
                                    .controller(opt -> IntegerFieldControllerBuilder.create(opt).range(1, 100_000))
                                    .build())
                            .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}
