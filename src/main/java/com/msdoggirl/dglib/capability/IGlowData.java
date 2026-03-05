package com.msdoggirl.dglib.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGlowData extends INBTSerializable<CompoundTag> {

    boolean isHeadGlowing();
    void setHeadGlowing(boolean enabled);
    int getHeadColor();
    void setHeadColor(int colorARGB);
    ResourceLocation getHeadMask();
    void setHeadMask(ResourceLocation mask);

    boolean isBodyGlowing();
    void setBodyGlowing(boolean enabled);
    int getBodyColor();
    void setBodyColor(int colorARGB);
    ResourceLocation getBodyMask();
    void setBodyMask(ResourceLocation mask);

    boolean isRightArmGlowing();
    void setRightArmGlowing(boolean enabled);
    int getRightArmColor();
    void setRightArmColor(int colorARGB);
    ResourceLocation getRightArmMask();
    void setRightArmMask(ResourceLocation mask);

    boolean isLeftArmGlowing();
    void setLeftArmGlowing(boolean enabled);
    int getLeftArmColor();
    void setLeftArmColor(int colorARGB);
    ResourceLocation getLeftArmMask();
    void setLeftArmMask(ResourceLocation mask);

    boolean isRightLegGlowing();
    void setRightLegGlowing(boolean enabled);
    int getRightLegColor();
    void setRightLegColor(int colorARGB);
    ResourceLocation getRightLegMask();
    void setRightLegMask(ResourceLocation mask);

    boolean isLeftLegGlowing();
    void setLeftLegGlowing(boolean enabled);
    int getLeftLegColor();
    void setLeftLegColor(int colorARGB);
    ResourceLocation getLeftLegMask();
    void setLeftLegMask(ResourceLocation mask);

    void clearAll();
}