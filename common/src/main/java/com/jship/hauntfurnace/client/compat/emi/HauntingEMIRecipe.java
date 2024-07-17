package com.jship.hauntfurnace.client.compat.emi;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import java.util.List;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HauntingEMIRecipe extends BasicEmiRecipe {
    private final HauntingRecipe recipe;

    HauntingEMIRecipe(HauntingRecipe recipe) {
        super(HauntFurnaceEMI.HAUNTING_CATEGORY, new ResourceLocation(HauntFurnace.MOD_ID, "/haunting"), 70, 18);
        this.recipe = recipe;
        this.inputs.add(EmiIngredient.of(recipe.getIngredients().get(0)));
        this.outputs.add(EmiStack.of(recipe.getResultItem()));
    }

    @Override
    public int getDisplayWidth() {
        return 82;
    }

    @Override
    public int getDisplayHeight() {
        return 38;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addFillingArrow(24, 5, 50 * this.recipe.getCookingTime()).tooltip((mx, my) -> {
            return List.of(ClientTooltipComponent.create(Component.translatable("emi.cooking.time", recipe.getCookingTime() / 20f).getVisualOrderText()));
        });
        widgets.addTexture(EmiTexture.EMPTY_FLAME, 1, 24);
        widgets.addAnimatedTexture(new EmiTexture(HauntFurnaceEMI.SPRITE_SHEET, 96, 114, 14, 14), 1, 24, 4000, false, true, true);
        widgets.addText(Component.translatable("emi.cooking.experience", recipe.getExperience()), 26, 28, -1, true);
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addSlot(outputs.get(0), 58, 0).recipeContext(this);
    }

}
