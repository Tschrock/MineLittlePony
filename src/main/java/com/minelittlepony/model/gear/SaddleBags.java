package com.minelittlepony.model.gear;

import com.minelittlepony.model.BodyPart;
import com.minelittlepony.model.capabilities.IModel;
import com.minelittlepony.model.capabilities.IModelPegasus;
import com.minelittlepony.pony.data.PonyWearable;
import com.minelittlepony.render.model.PlaneRenderer;

import java.util.UUID;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class SaddleBags extends AbstractGear {

    private PlaneRenderer leftBag;
    private PlaneRenderer rightBag;

    private PlaneRenderer strap;

    private boolean hangLow = false;

    float dropAmount = 0;


    private IModel model;

    @Override
    public void init(float yOffset, float stretch) {
        leftBag = new PlaneRenderer(this, 56, 19);
        rightBag = new PlaneRenderer(this, 56, 19);
        strap = new PlaneRenderer(this, 56, 19);

        float y = -0.5F;
        int x = 4;
        int z = -1;

        strap.offset(-x, y + 0.2F, z + 3).around(0, 4, 4)
        .tex(56, 31).top(0, 0, 0, 8, 1, stretch)
                    .top(0, 0, 1, 8, 1, stretch)
                  .south(0, 0, 2, 8, 1, stretch)
                  .north(0, 0, 0, 8, 1, stretch)
                .child(0).offset(0, -3, -0.305F).tex(56, 31)
                   .west( 4.0002F,  0, 0, 1, 3, stretch)  // 0.0001 is there
                   .west( 4.0002F, -1, 0, 1, 3, stretch)  // otherwise straps
                   .west(-4.0002F,  0, 0, 1, 3, stretch)  // clip into the body
                   .west(-4.0002F, -1, 0, 1, 3, stretch)
                .rotateAngleX = ROTATE_270;

        leftBag.offset(x, y, z).around(0, 4, 4)
                .tex(56, 25).south(0, 0, 0, 3, 6, stretch)
                .tex(59, 25).south(0, 0, 8, 3, 6, stretch)
                .tex(56, 19) .west(3, 0, 0, 6, 8, stretch)
                             .west(0, 0, 0, 6, 8, stretch)
                .child(0).offset(z, y, -x).tex(56, 16)
                                     .top(0, 0, -3, 8, 3, stretch)
              .tex(56, 22).flipZ().bottom(0, 6, -3, 8, 3, stretch)
                         .rotateAngleY = ROTATE_270;

        x += 3;

        rightBag.offset(-x, y, z).around(0, 4, 4).flip()
                .tex(56, 25).south(0, 0, 0, 3, 6, stretch)
                .tex(59, 25).south(0, 0, 8, 3, 6, stretch)
                .tex(56, 19).west(3, 0, 0, 6, 8, stretch)
                            .west(0, 0, 0, 6, 8, stretch)
                   .child(0).offset(z, y, x).tex(56, 16)
                            .flipZ().top(0, 0, -3, 8, 3, stretch)
             .tex(56, 22).flipZ().bottom(0, 6, -3, 8, 3, stretch)
                 .rotateAngleY = ROTATE_270;
    }

    @Override
    public void setLivingAnimations(IModel model, Entity entity) {
        this.model = model;

        hangLow = false;

        if (model instanceof IModelPegasus) {
            hangLow = model.canFly() && ((IModelPegasus)model).wingsAreOpen();
        }
    }

    @Override
    public void setRotationAndAngles(boolean rainboom, UUID interpolatorId, float move, float swing, float bodySwing, float ticks) {
        float pi = PI * (float) Math.pow(swing, 16);

        float mve = move * 0.6662f;
        float srt = swing / 10;

        bodySwing = MathHelper.cos(mve + pi) * srt;

        leftBag.rotateAngleX = bodySwing;
        rightBag.rotateAngleX = bodySwing;

        if (model instanceof IModelPegasus && model.isFlying()) {
            bodySwing = ((IModelPegasus)model).getWingRotationFactor(ticks) - ROTATE_270;
            bodySwing /= 10;
        }

        leftBag.rotateAngleZ = bodySwing;
        rightBag.rotateAngleZ = -bodySwing;

        dropAmount = hangLow ? 0.15F : 0;
    }

    public void sethangingLow(boolean veryLow) {
        hangLow = veryLow;
    }

    @Override
    public void renderPart(float scale, UUID interpolatorId) {
        dropAmount = model.getMetadata().getInterpolator(interpolatorId).interpolate("dropAmount", dropAmount, 3);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, dropAmount, 0);

        leftBag.render(scale);
        rightBag.render(scale);


        GlStateManager.popMatrix();
        strap.render(scale);
    }

    @Override
    public boolean canRender(IModel model, Entity entity) {
        return model.isWearing(PonyWearable.SADDLE_BAGS);
    }

    @Override
    public BodyPart getGearLocation() {
        return BodyPart.BODY;
    }

    @Override
    public ResourceLocation getTexture(Entity entity) {
        // use the default
        return null;
    }

}
