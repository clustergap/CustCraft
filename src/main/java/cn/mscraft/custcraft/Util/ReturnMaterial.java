package cn.mscraft.custcraft.Util;

import cn.mscraft.custcraft.CustCraft;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ReturnMaterial {
    public static Material craftingTableMaterial() {
        if (CustCraft.info.isOldMaterialVersion()) {
            String material = "WORKBENCH";
            Material type = Material.valueOf(material.substring(material.indexOf(":") + 1).toUpperCase());
            return type;
        }
        return Material.CRAFTING_TABLE;
    }

    public static ItemStack blackStainedGlassPaneMaterial() {
        if (CustCraft.info.isOldMaterialVersion()) {
            String material = "STAINED_GLASS_PANE";
            Material type = Material.valueOf(material.substring(material.indexOf(":") + 1).toUpperCase());
            ItemStack itemStack = new ItemStack(type, 1, (short)15);
            return itemStack;
        }
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        return item;
    }
}