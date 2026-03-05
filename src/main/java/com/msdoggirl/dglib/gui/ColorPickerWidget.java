package com.msdoggirl.dglib.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class ColorPickerWidget extends AbstractWidget {

    private float hue = 0.0f;
    private float saturation = 1.0f;
    private float brightness = 1.0f;
    private float alpha = 1.0f;

    private final int wheelSize;
    private int wheelX, wheelY;
    private int sliderX, sliderY, sliderWidth, sliderHeight;

    public boolean draggingWheel = false;
    public boolean draggingSlider = false;

    private final Consumer<Integer> onColorChanged;

    public ColorPickerWidget(int x, int y, int size, int initialColor, Consumer<Integer> onColorChanged) {
        super(x, y, size + 120, size + 40, Component.empty());
        this.wheelSize = size;
        this.onColorChanged = onColorChanged;

        wheelX = x;
        wheelY = y;
        sliderWidth = 20;
        sliderHeight = size;
        sliderX = wheelX + size + 40;
        sliderY = wheelY;

        setColor(initialColor);
    }

    public void setColor(int color) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        float[] hsv = rgbToHsv(r / 255f, g / 255f, b / 255f);
        hue = hsv[0];
        saturation = hsv[1];
        brightness = hsv[2];
        alpha = a / 255f;

        if (onColorChanged != null) onColorChanged.accept(getCurrentColor());
    }

    public int getCurrentColor() {
        return hsvToARGB(hue, saturation, brightness, alpha);
    }

    @Override
    public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        drawColorWheel(gui);
        drawBrightnessSlider(gui);
    }

    private void drawColorWheel(GuiGraphics gui) {
        for (int px = 0; px < wheelSize; px++) {
            for (int py = 0; py < wheelSize; py++) {
                float dx = (px - wheelSize / 2f) / (wheelSize / 2f);
                float dy = (wheelSize / 2f - py) / (wheelSize / 2f);

                float dist = Mth.sqrt(dx * dx + dy * dy);

                if (dist <= 1.0f) {
                    float angle = (float) (Math.atan2(dy, dx) / (Math.PI * 2));
                    if (angle < 0) angle += 1.0f;
                    int color = hsvToARGB(angle, dist, brightness, alpha);
                    gui.fill(wheelX + px, wheelY + py, wheelX + px + 1, wheelY + py + 1, color);
                } else {
                    gui.fill(wheelX + px, wheelY + py, wheelX + px + 1, wheelY + py + 1, 0xFF202020);
                }
            }
        }

        // Selector dot
        double cx = wheelX + wheelSize / 2.0;
        double cy = wheelY + wheelSize / 2.0;
        double radius = saturation * (wheelSize / 2.0);
        double angleRad = hue * Math.PI * 2.0;

        int selX = (int) (cx + radius * Math.cos(angleRad));
        int selY = (int) (cy - radius * Math.sin(angleRad));

        gui.fill(selX - 6, selY - 6, selX + 7, selY + 7, 0xFFFFFFFF);
        gui.fill(selX - 5, selY - 5, selX + 6, selY + 6, 0xFF000000);
    }

    private void drawBrightnessSlider(GuiGraphics gui) {
        for (int py = 0; py < sliderHeight; py++) {
            float v = 1.0f - (py / (float) sliderHeight);
            int color = hsvToARGB(hue, saturation, v, alpha);
            gui.fill(sliderX, sliderY + py, sliderX + sliderWidth, sliderY + py + 1, color);
        }

        int selY = sliderY + (int) ((1.0f - brightness) * sliderHeight);
        gui.fill(sliderX - 6, selY - 4, sliderX + sliderWidth + 6, selY + 5, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != 0) return false;

        if (isInWheel(mx, my)) {
            draggingWheel = true;
            updateWheelFromMouse(mx, my);
            return true;
        }
        if (isInSlider(mx, my)) {
            draggingSlider = true;
            updateSliderFromMouse(my);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (draggingWheel) {
            updateWheelFromMouse(mx, my);
            return true;
        }
        if (draggingSlider) {
            updateSliderFromMouse(my);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        draggingWheel = draggingSlider = false;
        return false;
    }

    private boolean isInWheel(double mx, double my) {
        double cx = wheelX + wheelSize / 2.0;
        double cy = wheelY + wheelSize / 2.0;
        double dx = mx - cx;
        double dy = my - cy;
        return dx * dx + dy * dy <= (wheelSize / 2.0) * (wheelSize / 2.0);
    }

    private boolean isInSlider(double mx, double my) {
        return mx >= sliderX && mx <= sliderX + sliderWidth &&
                my >= sliderY && my <= sliderY + sliderHeight;
    }

    private void updateWheelFromMouse(double mx, double my) {
        double cx = wheelX + wheelSize / 2.0;
        double cy = wheelY + wheelSize / 2.0;

        double dx = mx - cx;
        double dy = cy - my;

        double angle = Math.atan2(dy, dx);
        hue = (float) (angle / (Math.PI * 2));
        if (hue < 0) hue += 1.0f;

        double dist = Math.sqrt(dx * dx + dy * dy);
        saturation = (float) Math.min(1.0, dist / (wheelSize / 2.0));

        notifyChange();
    }

    private void updateSliderFromMouse(double my) {
        brightness = 1.0f - (float) Mth.clamp((my - sliderY) / sliderHeight, 0.0, 1.0);
        notifyChange();
    }

    private void notifyChange() {
        if (onColorChanged != null) onColorChanged.accept(getCurrentColor());
    }

    private static int hsvToARGB(float h, float s, float v, float a) {
        int i = (int) (h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        return switch (i % 6) {
            case 0 -> rgb2argb(v, t, p, a);
            case 1 -> rgb2argb(q, v, p, a);
            case 2 -> rgb2argb(p, v, t, a);
            case 3 -> rgb2argb(p, q, v, a);
            case 4 -> rgb2argb(t, p, v, a);
            default -> rgb2argb(v, p, q, a);
        };
    }

    private static int rgb2argb(float r, float g, float b, float a) {
        return ((int) (a * 255 + 0.5f) << 24) |
                ((int) (r * 255 + 0.5f) << 16) |
                ((int) (g * 255 + 0.5f) << 8) |
                (int) (b * 255 + 0.5f);
    }

    private static float[] rgbToHsv(float r, float g, float b) {
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h = 0, s = 0, v = max;

        float d = max - min;
        s = max == 0 ? 0 : d / max;

        if (max == min) {
            h = 0;
        } else {
            if (max == r) h = (g - b) / d + (g < b ? 6 : 0);
            else if (max == g) h = (b - r) / d + 2;
            else h = (r - g) / d + 4;
            h /= 6;
        }

        return new float[]{h, s, v};
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}


}