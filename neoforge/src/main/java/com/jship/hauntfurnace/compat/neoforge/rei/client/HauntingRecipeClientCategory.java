package com.jship.hauntfurnace.compat.neoforge.rei.client;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.jship.hauntfurnace.compat.neoforge.rei.BurningSoulFireWidget;

import me.shedaniel.math.Dimension;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.SimpleDisplayRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.network.chat.Component;

public class HauntingRecipeClientCategory implements DisplayCategory<HauntingRecipeClientDisplay> {
    private CategoryIdentifier<HauntingRecipeClientDisplay> identifier;
    private EntryStack<?> logo;
    private String categoryName;
    private double defaultCookingTime;
    
    public HauntingRecipeClientCategory(CategoryIdentifier<HauntingRecipeClientDisplay> identifier, EntryStack<?> logo, String categoryName, double defaultCookingTime) {
        this.identifier = identifier;
        this.logo = logo;
        this.categoryName = categoryName;
        this.defaultCookingTime = defaultCookingTime;
    }
    
    @Override
    public List<Widget> setupDisplay(HauntingRecipeClientDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        DecimalFormat df = new DecimalFormat("###.##");
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
        widgets.add(new BurningSoulFireWidget(new Rectangle(new Point(startPoint.x + 1, startPoint.y + 20), new Dimension(14, 14)))
                .animationDurationMS(10000));
        if (display.cookTime().isPresent() && display.xp().isPresent()) {
            widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5),
                    Component.translatable("category.rei.cooking.time&xp", df.format(display.xp().getAsDouble()), df.format(display.cookTime().getAsDouble() / 20d))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
        }
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 8))
                .animationDurationTicks(display.cookTime().orElse(defaultCookingTime)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1))
                .entries(display.getInputEntries().get(0))
                .markInput());
        return widgets;
    }
    
    @Override
    public DisplayRenderer getDisplayRenderer(HauntingRecipeClientDisplay display) {
        return SimpleDisplayRenderer.from(Collections.singletonList(display.getInputEntries().get(0)), display.getOutputEntries());
    }
    
    @Override
    public int getDisplayHeight() {
        return 49;
    }
    
    @Override
    public CategoryIdentifier<HauntingRecipeClientDisplay> getCategoryIdentifier() {
        return identifier;
    }
    
    @Override
    public Renderer getIcon() {
        return logo;
    }
    
    @Override
    public Component getTitle() {
        return Component.translatable(categoryName);
    }
    
}
