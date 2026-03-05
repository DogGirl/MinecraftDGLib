package com.msdoggirl.dglib;

import net.minecraft.resources.ResourceLocation;

public class PlayerGlowState {

    public boolean headGlowing = false;
    public boolean bodyGlowing = false;
    public boolean rightArmGlowing = false;
    public boolean leftArmGlowing = false;
    public boolean rightLegGlowing = false;
    public boolean leftLegGlowing = false;

    public ResourceLocation headGlowTexture;
    public ResourceLocation bodyGlowTexture;
    public ResourceLocation rightArmGlowTexture;
    public ResourceLocation leftArmGlowTexture;
    public ResourceLocation rightLegGlowTexture;
    public ResourceLocation leftLegGlowTexture;

    public int headGlowColor = 0xFFFFFFFF;
    public int bodyGlowColor = 0xFFFFFFFF;
    public int rightArmGlowColor = 0xFFFFFFFF;
    public int leftArmGlowColor = 0xFFFFFFFF;
    public int rightLegGlowColor = 0xFFFFFFFF;
    public int leftLegGlowColor = 0xFFFFFFFF;

    public void clear() {
        headGlowing = bodyGlowing = rightArmGlowing = leftArmGlowing = rightLegGlowing = leftLegGlowing = false;
        headGlowTexture = bodyGlowTexture = rightArmGlowTexture = leftArmGlowTexture = rightLegGlowTexture = leftLegGlowTexture = null;
        headGlowColor = bodyGlowColor = rightArmGlowColor = leftArmGlowColor = rightLegGlowColor = leftLegGlowColor = 0xFFFFFFFF;
    }
}