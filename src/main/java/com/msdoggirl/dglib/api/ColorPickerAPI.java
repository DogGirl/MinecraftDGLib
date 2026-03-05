package com.msdoggirl.dglib.api;

import com.msdoggirl.dglib.gui.ColorPickerScreen;

import net.minecraft.network.chat.Component;

import java.util.List;

public class ColorPickerAPI {

    // ───────────────────────────────────────────────
    // Multi-tab color picker methods
    // ───────────────────────────────────────────────

    public static void open(
            Component title,
            List<ColorPickerScreen.ColorTab> tabs
    ) {
        ColorPickerScreen.open(title, tabs);
    }


//    public static void openWithPlayerPreview(
//            Component title,
//            List<MultiColorPickerScreen.ColorTab> tabs
//    ) {
//        MultiColorPickerScreen.openWithPlayerPreview(title, tabs);
//    }


//    public static void openWithCustomPreview(
//            Component title,
//            List<MultiColorPickerScreen.ColorTab> tabs,
//            Consumer<List<Integer>> customRenderer
//    ) {
//        MultiColorPickerScreen.openWithCustomPreview(title, tabs, customRenderer);
//    }


}