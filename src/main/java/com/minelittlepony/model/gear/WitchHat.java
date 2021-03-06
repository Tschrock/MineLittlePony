package com.minelittlepony.model.gear;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.minelittlepony.model.BodyPart;
import com.minelittlepony.model.capabilities.IModel;
import com.minelittlepony.pony.data.PonyWearable;
import com.minelittlepony.render.model.PonyRenderer;

import java.util.UUID;

public class WitchHat extends AbstractGear implements IStackable {

    private static final ResourceLocation WITCH_TEXTURES = new ResourceLocation("textures/entity/witch.png");

    private PonyRenderer witchHat;

    @Override
    public void renderPart(float scale, UUID interpolatorId) {
        witchHat.render(scale * 1.3F);
    }

    @Override
    public void init(float yOffset, float stretch) {
        witchHat = new PonyRenderer(this).size(64, 128);
        witchHat.around(HEAD_RP_X, HEAD_RP_Y + yOffset, HEAD_RP_Z)
                .tex(0, 64).box(-5, -6, -7, 10, 2, 10, stretch)
                .child(0).around(1.75F, -4, 2)
                    .tex(0, 76).box(-5, -5, -7, 7, 4, 7, stretch)
                    .rotate(-0.05235988F, 0, 0.02617994F)
                    .child(0).around(1.75F, -4, 2)
                        .tex(0, 87).box(-5, -4, -7, 4, 4, 4, stretch)
                        .rotate(-0.10471976F, 0, 0.05235988F)
                        .child(0).around(1.75F, -2, 2)
                            .tex(0, 95).box(-5, -2, -7, 1, 2, 1, stretch)
                            .rotate(-0.20943952F, 0, 0.10471976F);
    }


    @Override
    public boolean canRender(IModel model, Entity entity) {
        return model.isWearing(PonyWearable.HAT);
    }

    @Override
    public BodyPart getGearLocation() {
        return BodyPart.HEAD;
    }

    @Override
    public ResourceLocation getTexture(Entity entity) {
        return WITCH_TEXTURES;
    }

    @Override
    public float getStackingOffset() {
        return 0.7F;
    }
}
