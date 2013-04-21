/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.entity;

import monnef.core.utils.EntityHelper;
import monnef.core.utils.VectorUtils;
import monnef.dawn.network.NetworkHelper;
import monnef.dawn.network.packet.SpawnParticlePacket;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySplashBomb extends EntityThrowable {
    private static final double EXPLOSION_VISIBILITY = 16 * 15;

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

        if (!this.worldObj.isRemote) {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
            NetworkHelper.sendToAllAround(this, EXPLOSION_VISIBILITY, new SpawnParticlePacket(SpawnParticlePacket.SpawnType.BOMB_EXPLOSION, this.dimension, VectorUtils.getEntityPositionVector(this)).makePacket());
            this.setDead();
            EntityHelper.pushEntitiesBack(worldObj, movingobjectposition.hitVec, 0.5f, 5, 5, 0.15f, getThrower());
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
