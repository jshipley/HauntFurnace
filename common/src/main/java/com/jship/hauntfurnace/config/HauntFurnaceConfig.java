package com.jship.hauntfurnace.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@FieldDefaults(level=AccessLevel.PRIVATE)
@Accessors(fluent=true)
@UtilityClass
public class HauntFurnaceConfig {

    // Haunt furnace
    final boolean defaultHauntQuiet = false;
    @Getter @Setter
    boolean hauntQuiet = defaultHauntQuiet;

    final boolean defaultHauntVanillaFuels = true;
    @Getter @Setter
    boolean hauntVanillaFuels = defaultHauntVanillaFuels;

    final boolean defaultHauntCustomFuels = true;
    @Getter @Setter
    boolean hauntCustomFuels = defaultHauntCustomFuels;

    final int defaultHauntEnergyUsage = 10;
    @Getter @Setter
    int hauntEnergyUsage = defaultHauntEnergyUsage;

    final int defaultHauntEnergyCapacity = 1024;
    @Getter @Setter
    int hauntEnergyCapacity = defaultHauntEnergyCapacity;

    final int defaultHauntEnergyMaxInsert = 32;
    @Getter @Setter
    int hauntEnergyMaxInsert = defaultHauntEnergyMaxInsert;

    // Ender furnace
    final boolean defaultEnderQuiet = false;
    @Getter @Setter
    boolean enderQuiet = defaultEnderQuiet;

    final boolean defaultEnderVanillaFuels = true;
    @Getter @Setter
    boolean enderVanillaFuels = defaultEnderVanillaFuels;

    final boolean defaultEnderCustomFuels = true;
    @Getter @Setter
    boolean enderCustomFuels = defaultEnderCustomFuels;

    final int defaultEnderEnergyUsage = 10;
    @Getter @Setter
    int enderEnergyUsage = defaultEnderEnergyUsage;

    final int defaultEnderEnergyCapacity = 1024;
    @Getter @Setter
    int enderEnergyCapacity = defaultEnderEnergyCapacity;

    final int defaultEnderEnergyMaxInsert = 32;
    @Getter @Setter
    int enderEnergyMaxInsert = defaultEnderEnergyMaxInsert;

    public Screen createConfig(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder()
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
