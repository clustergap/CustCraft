package cn.mscraft.custcraft.Util;

import cn.mscraft.custcraft.CustCraft;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {
    private static ItemStack baffle;

    private static ItemStack button;

    public static ItemStack getBaffle() {
        if (baffle == null) {
            ItemStack item = ReturnMaterial.blackStainedGlassPaneMaterial();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("");
                    item.setItemMeta(meta);
            baffle = item;
        }
        return baffle;
    }

    public static ItemStack getButton() {
        if (button == null) {
            ItemStack item = new ItemStack(Material.SLIME_BALL);
            ItemMeta meta = item.getItemMeta();
            String itemName = CustCraft.instance.getConfig().getString("inventory.button").replaceAll("&", "§");
                    meta.setDisplayName(itemName);
            item.setItemMeta(meta);
            button = item;
        }
        return button;
    }

    public static boolean isSimilar(ItemStack a, ItemStack b) {
        if (a != null)
            return a.isSimilar(b);
        return false;
    }
}
