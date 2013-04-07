package monnef.dawn.item;

import monnef.core.utils.PlayerHelper;
import monnef.dawn.DawnOfSteve;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class ItemGun extends ItemDawn {
    private static final String AMMO_LEFT_TAG = "ammoLeft";
    private static final String COOLDOWN_TAG = "cooldown";

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
    }

    public void initGun(AmmoRequirement ammo, int clipSize, float maxDistance, int damagePerBullet) {
        requiresAmmo = ammo;
        this.clipSize = clipSize;
        setMaxDamage(clipSize * itemDmgCoef);
        this.maxDistance = maxDistance;
        this.damagePerBullet = damagePerBullet;
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
                // TODO fire
                if (DawnOfSteve.proxy.isServer()) {
                    MovingObjectPosition res = PlayerHelper.rayTrace(player, maxDistance);
                    if (res != null) {
                        if (res.typeOfHit == EnumMovingObjectType.ENTITY) {
                            res.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(player), damagePerBullet);
                        } // TODO block hit
                    }
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

    private void initNBT(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            setAmmoLeft(stack, clipSize);
            setCoolDown(stack, 0);
        }
    }

    private int getAmmoLeft(ItemStack stack) {
        return stack.getTagCompound().getInteger(AMMO_LEFT_TAG);
    }

    private void setAmmoLeft(ItemStack stack, int count) {
        stack.getTagCompound().setInteger(AMMO_LEFT_TAG, count);
        int dmgToShow = clipSize * itemDmgCoef - count * itemDmgCoef;
        if (dmgToShow <= 0) dmgToShow = 1;
        stack.setItemDamage(dmgToShow);
    }

    private int getCoolDown(ItemStack stack) {
        return stack.getTagCompound().getInteger(COOLDOWN_TAG);
    }

    private void setCoolDown(ItemStack stack, int count) {
        stack.getTagCompound().setInteger(COOLDOWN_TAG, count);
    }

    private boolean coolDownActive(ItemStack stack) {
        return getCoolDown(stack) > 0;
    }
}
