package net.skds.core.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.skds.core.SKDSCoreConfig;

public class SKDSCoreConfigScreen extends Screen {
    private static final int LABEL_X_OFFSET = 150;
    private static final int CONTROL_WIDTH = 150;

    private final Screen parent;
    private CycleButton<PerformancePreset> presetButton;
    private EditBox minBlockUpdatesBox;
    private EditBox timeoutBox;
    private Component statusMessage = CommonComponents.EMPTY;

    public SKDSCoreConfigScreen(Screen parent) {
        super(Component.literal("SKDS Core Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int top = Math.max(32, this.height / 4 - 10);

        presetButton = this.addRenderableWidget(CycleButton.builder(PerformancePreset::getDisplayName)
                .withValues(PerformancePreset.values())
                .withInitialValue(SKDSCoreConfig.getPerformancePreset())
                .create(centerX, top, CONTROL_WIDTH, 20,
                        Component.translatable("skds_core.config.performancePreset"),
                        (button, value) -> updateManualFieldState()));

        minBlockUpdatesBox = createNumberBox(centerX, top + 28,
                Component.translatable("skds_core.config.minBlockUpdates"),
                String.valueOf(SKDSCoreConfig.getManualMinBlockUpdates()));
        timeoutBox = createNumberBox(centerX, top + 56,
                Component.translatable("skds_core.config.timeout"),
                String.valueOf(SKDSCoreConfig.getManualTimeoutCutoff()));

        this.addRenderableWidget(Button.builder(Component.literal("Defaults"), button -> loadDefaults())
                .bounds(centerX - 154, this.height - 28, 100, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.literal("Save"), button -> saveAndClose())
                .bounds(centerX - 50, this.height - 28, 100, 20)
                .build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
                .bounds(centerX + 54, this.height - 28, 100, 20)
                .build());

        updateManualFieldState();
    }

    private EditBox createNumberBox(int x, int y, Component message, String value) {
        EditBox box = new EditBox(this.font, x, y, CONTROL_WIDTH, 20, message);
        box.setValue(value);
        box.setHint(message);
        this.addRenderableWidget(box);
        return box;
    }

    private void updateManualFieldState() {
        boolean custom = presetButton.getValue() == PerformancePreset.CUSTOM;
        minBlockUpdatesBox.active = custom;
        timeoutBox.active = custom;
    }

    private void loadDefaults() {
        presetButton.setValue(SKDSCoreConfig.COMMON.performancePreset.getDefault());
        minBlockUpdatesBox.setValue(String.valueOf(SKDSCoreConfig.COMMON.minBlockUpdates.getDefault()));
        timeoutBox.setValue(String.valueOf(SKDSCoreConfig.COMMON.timeoutCutoff.getDefault()));
        updateManualFieldState();
        statusMessage = Component.literal("Defaults loaded. Press Save to apply.");
    }

    private void saveAndClose() {
        try {
            SKDSCoreConfig.setPerformancePreset(presetButton.getValue());
            SKDSCoreConfig.setManualMinBlockUpdates(parseInt(minBlockUpdatesBox.getValue(), 0, 1_000_000));
            SKDSCoreConfig.setManualTimeoutCutoff(parseInt(timeoutBox.getValue(), 0, 50));
            SKDSCoreConfig.save();
            this.minecraft.setScreen(parent);
        } catch (NumberFormatException ex) {
            statusMessage = Component.literal("Enter whole numbers for every field.");
        }
    }

    private static int parseInt(String text, int min, int max) {
        return Mth.clamp(Integer.parseInt(text.trim()), min, max);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int top = Math.max(32, this.height / 4 - 10);
        int labelX = centerX - LABEL_X_OFFSET;

        guiGraphics.drawCenteredString(this.font, this.title, centerX, 12, 0xFFFFFF);
        guiGraphics.drawString(this.font, Component.translatable("skds_core.config.performancePreset"), labelX, top + 6, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, Component.translatable("skds_core.config.minBlockUpdates"), labelX, top + 34, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, Component.translatable("skds_core.config.timeout"), labelX, top + 62, 0xFFFFFF, false);

        if (presetButton.getValue() != PerformancePreset.CUSTOM) {
            guiGraphics.drawCenteredString(this.font,
                    Component.literal("Manual values are ignored unless preset is CUSTOM."),
                    centerX, top + 92, 0xC0C0C0);
        }

        if (!statusMessage.getString().isEmpty()) {
            guiGraphics.drawCenteredString(this.font, statusMessage, centerX, this.height - 40, 0xFF8080);
        }
    }
}
