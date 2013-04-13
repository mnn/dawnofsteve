/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.entity;

import monnef.core.utils.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.World;

import static monnef.core.utils.PlayerHelper.getPlayersHeadPositionVector;
import static monnef.core.utils.VectorUtils.multiplyVector;
import static monnef.core.utils.VectorUtils.shiftedVectorInDirection;

public class EntityKit extends Entity {
    public final static int WATCHER_TYPE = 10;
    private static final String TAG_TYPE = "kitType";
    public static final double DISTANCE_TO_SPAWN_FROM = 0.5;
    public static final double THROW_COEF = 0.3;
    public static final int NOT_USABLE_FOR_X_TICKS = 10;
    private EntityPlayer thrower;
    private int maximalTicks = -1; // dies without type

    public enum KitType {
        AMMO(Constants.KIT_LIFE_LEN), MEDPACK(Constants.KIT_LIFE_LEN), RATION(Constants.KIT_LIFE_LEN);
        public static KitType[] packs;

        static {
            packs = values();
        }

        private int secondsToLive;

        KitType(int secondsToLive) {
            this.secondsToLive = secondsToLive;
        }

        public int getSecondsToLive() {
            return secondsToLive;
        }

        private static class Constants {
            public static final int KIT_LIFE_LEN = 30;
        }
    }

    public EntityKit(World world, EntityPlayer player, ItemStack stack) {
        this(world);
        this.thrower = player;
        Vec3Pool pool = player.worldObj.getWorldVec3Pool();
        Vec3 direction = player.getLookVec();
        Vec3 t = shiftedVectorInDirection(pool, getPlayersHeadPositionVector(player), direction, DISTANCE_TO_SPAWN_FROM);
        this.setPosition(t.xCoord, t.yCoord, t.zCoord);
        Vec3 m = multiplyVector(pool, direction.normalize(), THROW_COEF);
        this.motionX = m.xCoord;
        this.motionY = m.yCoord;
        this.motionZ = m.zCoord;
    }

    public EntityKit(World par1World) {
        super(par1World);
        setSize(0.25f, 0.25f);
        this.yOffset = this.height / 2.0F;
    }

    public void setType(KitType type) {
        setType((byte) type.ordinal());
    }

    public void setType(byte type) {
        this.dataWatcher.updateObject(WATCHER_TYPE, type);
        refreshMaximalTicks();
    }

    private void refreshMaximalTicks() {
        maximalTicks = KitType.packs[getType().ordinal()].getSecondsToLive() * 20;
    }

    public KitType getType() {
        return KitType.packs[this.dataWatcher.getWatchableObjectByte(WATCHER_TYPE)];
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(WATCHER_TYPE, (byte) 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        byte type = tag.getByte(TAG_TYPE);
        setType(type);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setByte(TAG_TYPE, (byte) getType().ordinal());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (maximalTicks == -1) {
            // refresh type
            refreshMaximalTicks();
        }

        if (ticksExisted > maximalTicks) {
            this.setDead();
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.noClip = this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
        this.moveEntity(motionX, motionY, motionZ);

        float f = 0.98F;
        if (this.onGround) {
            f = 0.58800006F;
            int i = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

            if (i > 0) {
                f = Block.blocksList[i].slipperiness * 0.98F;
            }
        }
        this.motionX *= (double) f;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double) f;

        if (this.onGround) {
            this.motionY *= -0.5D;
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        super.onCollideWithPlayer(player);
        boolean used = false;
        if (ticksExisted < NOT_USABLE_FOR_X_TICKS) return;

        switch (getType()) {
            case AMMO:
                // TODO
                PlayerHelper.giveItemToPlayer(player, new ItemStack(Item.arrow));
                used = true;
                break;
            case MEDPACK:
                if (player.shouldHeal()) {
                    player.heal(4);
                    used = true;
                }
                break;
            case RATION:
                // TODO
                PlayerHelper.giveItemToPlayer(player, new ItemStack(Item.porkCooked));
                used = true;
                break;
        }

        if (used) {
            setDead();
        }
    }

    @Override
    public void setDead() {
        if (worldObj.isRemote) {
            worldObj.spawnParticle("smoke", posX, posY, posZ, 0, 0.001, 0);
        }
        super.setDead();
    }
}
