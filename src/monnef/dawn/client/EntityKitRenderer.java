/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import monnef.dawn.client.model.ModelAmmoBox;
import monnef.dawn.client.model.ModelMedpack;
import monnef.dawn.client.model.ModelRation;
import monnef.dawn.entity.EntityKit;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class EntityKitRenderer extends Render {
    ModelAmmoBox box = new ModelAmmoBox();
    ModelMedpack medpack = new ModelMedpack();
    ModelRation rations = new ModelRation();

    private float U = 1f / 16;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float f1) {
        renderKit((EntityKit) entity, x, y, z, f, f1);
    }

    public void renderKit(EntityKit entity, double x, double y, double z, float f, float f1) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y + 1.5, z);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        switch (entity.getType()) {
            case AMMO:
                loadTexture("/box.png");
                box.render(entity, f, f1, 0, 0, 0, U);
                break;
            case MEDPACK:
                loadTexture("/medpack.png");
                medpack.render(entity, f, f1, 0, 0, 0, U);
                break;
            case RATION:
                loadTexture("/ration.png");
                rations.render(entity, f, f1, 0, 0, 0, U);
                break;
            default:
                throw new RuntimeException("unknown kit");
        }

        GL11.glPopMatrix();
    }
}
