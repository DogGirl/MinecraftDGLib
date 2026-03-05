package com.msdoggirl.dglib.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class GlowData implements IGlowData {

    private boolean headGlowing = false;
    private int headColor = 0xFFFFFFFF;
    private ResourceLocation headMask = null;

    private boolean bodyGlowing = false;
    private int bodyColor = 0xFFFFFFFF;
    private ResourceLocation bodyMask = null;

    private boolean rightArmGlowing = false;
    private int rightArmColor = 0xFFFFFFFF;
    private ResourceLocation rightArmMask = null;

    private boolean leftArmGlowing = false;
    private int leftArmColor = 0xFFFFFFFF;
    private ResourceLocation leftArmMask = null;

    private boolean rightLegGlowing = false;
    private int rightLegColor = 0xFFFFFFFF;
    private ResourceLocation rightLegMask = null;

    private boolean leftLegGlowing = false;
    private int leftLegColor = 0xFFFFFFFF;
    private ResourceLocation leftLegMask = null;

    @Override
    public boolean isHeadGlowing() { return headGlowing; }
    @Override
    public void setHeadGlowing(boolean enabled) { headGlowing = enabled; }
    @Override
    public int getHeadColor() { return headColor; }
    @Override
    public void setHeadColor(int color) { headColor = color; }
    @Override
    public ResourceLocation getHeadMask() { return headMask; }
    @Override
    public void setHeadMask(ResourceLocation mask) { headMask = mask; }

    @Override
    public boolean isBodyGlowing() { return bodyGlowing; }
    @Override
    public void setBodyGlowing(boolean enabled) { bodyGlowing = enabled; }
    @Override
    public int getBodyColor() { return bodyColor; }
    @Override
    public void setBodyColor(int color) { bodyColor = color; }
    @Override
    public ResourceLocation getBodyMask() { return bodyMask; }
    @Override
    public void setBodyMask(ResourceLocation mask) { bodyMask = mask; }

    @Override
    public boolean isRightArmGlowing() { return rightArmGlowing; }
    @Override
    public void setRightArmGlowing(boolean enabled) { rightArmGlowing = enabled; }
    @Override
    public int getRightArmColor() { return rightArmColor; }
    @Override
    public void setRightArmColor(int color) { rightArmColor = color; }
    @Override
    public ResourceLocation getRightArmMask() { return rightArmMask; }
    @Override
    public void setRightArmMask(ResourceLocation mask) { rightArmMask = mask; }

    @Override
    public boolean isLeftArmGlowing() { return leftArmGlowing; }
    @Override
    public void setLeftArmGlowing(boolean enabled) { leftArmGlowing = enabled; }
    @Override
    public int getLeftArmColor() { return leftArmColor; }
    @Override
    public void setLeftArmColor(int color) { leftArmColor = color; }
    @Override
    public ResourceLocation getLeftArmMask() { return leftArmMask; }
    @Override
    public void setLeftArmMask(ResourceLocation mask) { leftArmMask = mask; }

    @Override
    public boolean isRightLegGlowing() { return rightLegGlowing; }
    @Override
    public void setRightLegGlowing(boolean enabled) { rightLegGlowing = enabled; }
    @Override
    public int getRightLegColor() { return rightLegColor; }
    @Override
    public void setRightLegColor(int color) { rightLegColor = color; }
    @Override
    public ResourceLocation getRightLegMask() { return rightLegMask; }
    @Override
    public void setRightLegMask(ResourceLocation mask) { rightLegMask = mask; }

    @Override
    public boolean isLeftLegGlowing() { return leftLegGlowing; }
    @Override
    public void setLeftLegGlowing(boolean enabled) { leftLegGlowing = enabled; }
    @Override
    public int getLeftLegColor() { return leftLegColor; }
    @Override
    public void setLeftLegColor(int color) { leftLegColor = color; }
    @Override
    public ResourceLocation getLeftLegMask() { return leftLegMask; }
    @Override
    public void setLeftLegMask(ResourceLocation mask) { leftLegMask = mask; }

    @Override
    public void clearAll() {
        headGlowing = bodyGlowing = rightArmGlowing = leftArmGlowing = rightLegGlowing = leftLegGlowing = false;
        headColor = bodyColor = rightArmColor = leftArmColor = rightLegColor = leftLegColor = 0xFFFFFFFF;
        headMask = bodyMask = rightArmMask = leftArmMask = rightLegMask = leftLegMask = null;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putBoolean("HeadEnabled", headGlowing);
        nbt.putInt("HeadColor", headColor);
        if (headMask != null) nbt.putString("HeadMask", headMask.toString());

        nbt.putBoolean("BodyEnabled", bodyGlowing);
        nbt.putInt("BodyColor", bodyColor);
        if (bodyMask != null) nbt.putString("BodyMask", bodyMask.toString());

        nbt.putBoolean("RightArmEnabled", rightArmGlowing);
        nbt.putInt("RightArmColor", rightArmColor);
        if (rightArmMask != null) nbt.putString("RightArmMask", rightArmMask.toString());

        nbt.putBoolean("LeftArmEnabled", leftArmGlowing);
        nbt.putInt("LeftArmColor", leftArmColor);
        if (leftArmMask != null) nbt.putString("LeftArmMask", leftArmMask.toString());

        nbt.putBoolean("RightLegEnabled", rightLegGlowing);
        nbt.putInt("RightLegColor", rightLegColor);
        if (rightLegMask != null) nbt.putString("RightLegMask", rightLegMask.toString());

        nbt.putBoolean("LeftLegEnabled", leftLegGlowing);
        nbt.putInt("LeftLegColor", leftLegColor);
        if (leftLegMask != null) nbt.putString("LeftLegMask", leftLegMask.toString());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        headGlowing = nbt.getBoolean("HeadEnabled");
        headColor = nbt.getInt("HeadColor");
        if (nbt.contains("HeadMask")) headMask = new ResourceLocation(nbt.getString("HeadMask"));

        bodyGlowing = nbt.getBoolean("BodyEnabled");
        bodyColor = nbt.getInt("BodyColor");
        if (nbt.contains("BodyMask")) bodyMask = new ResourceLocation(nbt.getString("BodyMask"));

        rightArmGlowing = nbt.getBoolean("RightArmEnabled");
        rightArmColor = nbt.getInt("RightArmColor");
        if (nbt.contains("RightArmMask")) rightArmMask = new ResourceLocation(nbt.getString("RightArmMask"));

        leftArmGlowing = nbt.getBoolean("LeftArmEnabled");
        leftArmColor = nbt.getInt("LeftArmColor");
        if (nbt.contains("LeftArmMask")) leftArmMask = new ResourceLocation(nbt.getString("LeftArmMask"));

        rightLegGlowing = nbt.getBoolean("RightLegEnabled");
        rightLegColor = nbt.getInt("RightLegColor");
        if (nbt.contains("RightLegMask")) rightLegMask = new ResourceLocation(nbt.getString("RightLegMask"));

        leftLegGlowing = nbt.getBoolean("LeftLegEnabled");
        leftLegColor = nbt.getInt("LeftLegColor");
        if (nbt.contains("LeftLegMask")) leftLegMask = new ResourceLocation(nbt.getString("LeftLegMask"));
    }
}