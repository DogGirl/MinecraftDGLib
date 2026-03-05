package com.msdoggirl.dglib.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ColorPickerScreen extends Screen {

    private final List<ColorTab> tabs;
    private int currentTabIndex = 0;
    private ColorPickerWidget currentPicker;
    private final List<Integer> colors;
    private final boolean showPreview;
    @Nullable private final LivingEntity previewEntity;
    private final int previewXOffset;
    private final int previewYOffset;
    private final int previewScale;
    @Nullable private final Consumer<List<Integer>> customMultiPreviewRenderer;

    public ColorPickerScreen(
            Component title,
            List<ColorTab> tabs,
            boolean showPreview,
            @Nullable LivingEntity previewEntity,
            int previewXOffset, int previewYOffset, int previewScale,
            @Nullable Consumer<List<Integer>> customMultiPreviewRenderer
    ) {
        super(title);
        this.tabs = new ArrayList<>(tabs);
        this.colors = new ArrayList<>();
        for (var tab : tabs) {
            colors.add(tab.initialColor);
        }
        this.showPreview = showPreview;
        this.previewEntity = previewEntity;
        this.previewXOffset = previewXOffset;
        this.previewYOffset = previewYOffset;
        this.previewScale = previewScale;
        this.customMultiPreviewRenderer = customMultiPreviewRenderer;
    }

    @Override
    protected void init() {
        super.init();
        rebuildWidgets();
    }

    protected void rebuildWidgets() {
        clearWidgets();

        float uiScale = Math.min(width / 1920f, height / 1080f);
        uiScale = Math.max(0.75f, Math.min(1.5f, uiScale));

        int wheelSize = (int) (190 * uiScale);
        wheelSize = Math.max(120, Math.min(200, wheelSize));

        int wheelX = width / 2 - (wheelSize + (int)(130 * uiScale)) / 2;
        int wheelY = height / 2 - wheelSize / 2 + (int)(5 * uiScale);

        ColorTab currentTab = tabs.get(currentTabIndex);
        currentPicker = new ColorPickerWidget(wheelX, wheelY, wheelSize, colors.get(currentTabIndex), newColor -> {
            colors.set(currentTabIndex, newColor);
        });
        addRenderableWidget(currentPicker);

        int tabWidth = (int) (92 * uiScale);
        int tabHeight = (int) (22 * uiScale);
        int tabSpacing = (int) (4 * uiScale);
        int totalTabsWidth = tabs.size() * (tabWidth + tabSpacing) - tabSpacing;

        int tabStartX = width / 2 - totalTabsWidth / 2;
        int tabY = (int) (Math.max(20, height * 0.08f));

        for (int i = 0; i < tabs.size(); i++) {
            final int tabIdx = i;
            String label = tabs.get(i).name.getString();
            addRenderableWidget(Button.builder(Component.literal(label), b -> {
                currentTabIndex = tabIdx;
                rebuildWidgets();
            }).bounds(tabStartX + i * (tabWidth + tabSpacing), tabY, tabWidth, tabHeight).build());
        }

        int buttonWidth = (int) (100 * uiScale);
        int buttonHeight = (int) (20 * uiScale);
        int btnY = height - (int) (60 * uiScale);

        addRenderableWidget(Button.builder(Component.literal("Apply"), b -> {
            for (int i = 0; i < tabs.size(); i++) {
                tabs.get(i).onApply.accept(colors.get(i));
            }
            onClose();
        }).bounds(width / 2 - buttonWidth - 10, btnY, buttonWidth, buttonHeight).build());

        addRenderableWidget(Button.builder(Component.literal("Cancel"), b -> onClose())
                .bounds(width / 2 + 10, btnY, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        renderBackground(gui);
        super.render(gui, mouseX, mouseY, partialTick);

        if (showPreview) {
            float uiScale = Math.min(width / 1920f, height / 1080f);
            uiScale = Math.max(0.75f, Math.min(1.5f, uiScale));

            int margin = (int) (40 * uiScale);
            int previewX = width - margin - (int)(75 * uiScale) * 2;
            int previewY = height / 2;

            PoseStack pose = gui.pose();
            pose.pushPose();
            pose.translate(0, 0, 300);
            Lighting.setupForEntityInInventory();

            // ───────────────────────────────
            // Custom multi-renderer takes priority
            // ───────────────────────────────
            if (customMultiPreviewRenderer != null) {
                customMultiPreviewRenderer.accept(colors);  // Pass list of colors
            } else if (previewEntity != null) {
                // Fallback entity
                int previewScale = (int) (75 * uiScale);
                previewScale = Math.max(50, Math.min(110, previewScale));

                float rotYaw = (mouseX - width / 2f) * 0.25f;
                float rotPitch = (mouseY - height / 2f) * 0.15f;

                InventoryScreen.renderEntityInInventoryFollowsMouse(
                        gui, previewX, previewY, previewScale, rotYaw, rotPitch, previewEntity
                );
            }

            pose.popPose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static class ColorTab {
        public final Component name;
        public final int initialColor;
        public final Consumer<Integer> onApply;

        public ColorTab(Component name, int initialColor, Consumer<Integer> onApply) {
            this.name = name;
            this.initialColor = initialColor;
            this.onApply = onApply;
        }
    }

    // ───────────────────────────────
    // Static open methods
    // ───────────────────────────────

    public static void open(
            Component title,
            List<ColorTab> tabs
    ) {
        Minecraft.getInstance().setScreen(
                new ColorPickerScreen(title, tabs, false, null, 0, 0, 0, null)
        );
    }

    public static void openWithPlayerPreview(
            Component title,
            List<ColorTab> tabs
    ) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        Minecraft.getInstance().setScreen(
                new ColorPickerScreen(title, tabs, true, player, 0, 0, 75, null)
        );
    }

    public static void openWithCustomPreview(
            Component title,
            List<ColorTab> tabs,
            Consumer<List<Integer>> customRenderer
    ) {
        Minecraft.getInstance().setScreen(
                new ColorPickerScreen(title, tabs, true, null, 0, 0, 0, customRenderer)
        );
    }
}