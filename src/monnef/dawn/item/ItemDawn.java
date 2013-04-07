package monnef.dawn.item;

import monnef.dawn.DawnOfSteve;
import monnef.dawn.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemDawn extends Item {
    public ItemDawn(int id) {
        super(id);
        setCreativeTab(DawnOfSteve.CreativeTab);
    }

    @Override
    public void updateIcons(IconRegister register) {
        this.iconIndex = register.registerIcon(getIconName());
    }

    private String getIconName() {
        String id = getUnlocalizedName();
        id = id.substring(id.indexOf('.') + 1);
        return Reference.ModName + ":" + id;
    }
}
