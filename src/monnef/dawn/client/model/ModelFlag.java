package monnef.dawn.client.model;

/*
 * Copyright (c) 2013 monnef.
 */

// Date: 12.4.2013 22:00:32
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFlag extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;

    public ModelFlag() {
        textureWidth = 128;
        textureHeight = 64;

        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 1, 32, 1);
        Shape1.setRotationPoint(4F, 0F, 0F);
        Shape1.setTextureSize(128, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 5, 0);
        Shape2.addBox(0F, 0F, 0F, 11, 8, 1);
        Shape2.setRotationPoint(-7F, 0F, 0F);
        Shape2.setTextureSize(128, 64);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Shape1.render(f5);
        Shape2.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}
