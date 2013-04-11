/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.item;

import monnef.dawn.DawnOfSteve;
import monnef.dawn.network.NetworkHelper;
import monnef.dawn.network.packet.SpawnParticlePacket;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

import static monnef.core.utils.PlayerHelper.EntityHitResult;
import static monnef.core.utils.PlayerHelper.addVector;
import static monnef.core.utils.PlayerHelper.getEntityPositionVector;
import static monnef.core.utils.PlayerHelper.getPlayersHeadPositionVector;
import static monnef.core.utils.PlayerHelper.multiplyVector;
import static monnef.core.utils.PlayerHelper.rayTraceBlock;
import static monnef.core.utils.PlayerHelper.rayTraceEntity;
import static monnef.dawn.network.packet.SpawnParticlePacket.SpawnType;

public class ItemGun extends ItemDawn implements IItemGun {
    private static final String AMMO_LEFT_TAG = "ammoLeft";
    private static final String COOLDOWN_TAG = "cooldown";
    public static final int SMOKE_EFFECT_VISIBILITY = 50;

    enum AmmoRequirement {
        NONE, BULLETS_SMALL
    }

    private AmmoRequirement requiresAmmo;
    private int clipSize;
    private float maxDistance;
    private int damagePerBullet;
    protected int cdAfterFire = 20;
    protected int cdAfterNoAmmo = 10;
    private final static int itemDmgCoef = 100;

    public ItemGun(int id) {
        super(id);
        setMaxStackSize(1);
        setFull3D();
    }

    public void initGun(AmmoRequirement ammo, int clipSize, float maxDistance, int damagePerBullet) {
        requiresAmmo = ammo;
        this.clipSize = clipSize;
        setMaxDamage(clipSize * itemDmgCoef);
        this.maxDistance = maxDistance;
        this.damagePerBullet = damagePerBullet;
    }

    enum HitTypeEnum {
        AIR, ENTITY, TILE
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        initNBT(stack);

        if (!coolDownActive(stack)) {
            int ammo = getAmmoLeft(stack);
            if (ammo <= 0) {
                player.addChatMessage("no ammo");
                setCoolDown(stack, cdAfterNoAmmo);
            } else {
                player.addChatMessage((DawnOfSteve.proxy.isServer() ? "S" : "C") + " firing! " + stack.getItemDamage());
                if (DawnOfSteve.proxy.isServer()) {
                    MovingObjectPosition blockHit = rayTraceBlock(player, maxDistance);
                    EntityHitResult entityHit = rayTraceEntity(player, maxDistance);
                    HitTypeEnum hitType = HitTypeEnum.AIR;
                    if (blockHit != null) hitType = HitTypeEnum.TILE;
                    if (entityHit != null) {
                        if (hitType == HitTypeEnum.AIR) hitType = HitTypeEnum.ENTITY;
                        else {
                            Vec3 playerPosition = getEntityPositionVector(player);
                            double blockDist = blockHit.hitVec.distanceTo(playerPosition);
                            double entDist = entityHit.hitVector.distanceTo(playerPosition);
                            if (blockDist < entDist) hitType = HitTypeEnum.TILE;
                            else hitType = HitTypeEnum.ENTITY;
                        }
                    }

                    Vec3 smokePos = null;
                    switch (hitType) {
                        case ENTITY:
                            entityHit.entity.attackEntityFrom(DamageSource.causePlayerDamage(player), damagePerBullet);
                            smokePos = entityHit.hitVector;
                            break;

                        case TILE:
                            player.worldObj.setBlock(blockHit.blockX, blockHit.blockY, blockHit.blockZ, Block.glowStone.blockID);
                            smokePos = blockHit.hitVec;
                            break;

                        case AIR:
                            //Vec3 shift = PlayerHelper.calculatePlayerLookMultiplied(player, maxDistance);
                            //smokePos = PlayerHelper.getPlayersHeadPositionVector(player).addVector(shift.xCoord, shift.yCoord, shift.zCoord);
                            break;

                        default:
                            throw new RuntimeException("No hit?");
                    }

                    if (smokePos != null) {
                        smokePos = addVector(smokePos, multiplyVector(player.worldObj.getWorldVec3Pool(), player.getLookVec().normalize(), -0.1));
                        NetworkHelper.sendToAllAround(player, SMOKE_EFFECT_VISIBILITY, new SpawnParticlePacket(SpawnType.BULLET_SMOKE, player.dimension, smokePos).makePacket());
                    }

                    Vec3 gunSmoke = addVector(getPlayersHeadPositionVector(player), multiplyVector(player.worldObj.getWorldVec3Pool(), player.getLookVec().normalize(), 1));
                    NetworkHelper.sendToAllAround(player, SMOKE_EFFECT_VISIBILITY, new SpawnParticlePacket(SpawnType.GUNPOWDER_SMOKE, player.dimension, gunSmoke).makePacket());
                }
                ammo--;
                setAmmoLeft(stack, ammo);
                setCoolDown(stack, cdAfterFire);
            }
        }

        return stack;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
        super.onUpdate(stack, world, entity, par4, par5);

        initNBT(stack);
        if (coolDownActive(stack)) {
            setCoolDown(stack, getCoolDown(stack) - 1);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        initNBT(stack);
        String info = String.format("%d / %d", getAmmoLeft(stack), clipSize);
        list.add(info);
    }

    @Override
    public void initNBT(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            setAmmoLeft(stack, clipSize);
            setCoolDown(stack, 0);
        }
    }

    @Override
    public int getAmmoLeft(ItemStack stack) {
        return stack.getTagCompound().getInteger(AMMO_LEFT_TAG);
    }

    @Override
    public void setAmmoLeft(ItemStack stack, int count) {
        stack.getTagCompound().setInteger(AMMO_LEFT_TAG, count);
        int dmgToShow = clipSize * itemDmgCoef - count * itemDmgCoef;
        if (dmgToShow <= 0) dmgToShow = 1;
        stack.setItemDamage(dmgToShow);
    }

    @Override
    public int getCoolDown(ItemStack stack) {
        return stack.getTagCompound().getInteger(COOLDOWN_TAG);
    }

    @Override
    public void setCoolDown(ItemStack stack, int count) {
        stack.getTagCompound().setInteger(COOLDOWN_TAG, count);
    }

    @Override
    public boolean coolDownActive(ItemStack stack) {
        return getCoolDown(stack) > 0;
    }

    @Override
    public AmmoRequirement getRequiresAmmo() {
        return requiresAmmo;
    }

    @Override
    public int getClipSize() {
        return clipSize;
    }

    @Override
    public float getMaxDistance() {
        return maxDistance;
    }
}
