/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySplashBomb extends EntityThrowable {
    public EntitySplashBomb(World par1World) {
        super(par1World);
    }

    public EntitySplashBomb(World par1World, EntityLiving thrower) {
        super(par1World, thrower);
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entityHit != null) {
            movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 2);
        }

        if (worldObj.isRemote) {
            this.worldObj.spawnParticle("hugeexplosion", this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("largeexplode", this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
        } else {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        }

        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.04f;
    }

    @Override
    protected float func_70182_d() {
        return 1.0F;
    }
}
