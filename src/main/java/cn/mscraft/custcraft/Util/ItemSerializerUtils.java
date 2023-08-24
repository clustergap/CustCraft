package cn.mscraft.custcraft.Util;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ItemSerializerUtils {

    public static String toNbtString(List<ItemStack> itemStacks) {
        ItemStack[] array = new ItemStack[itemStacks.size()];
        ReadWriteNBT nbt = NBT.itemStackArrayToNBT(itemStacks.toArray(array));
        return nbt.toString();
    }

    public static List<ItemStack> fromNbtString(String nbtString) {
        ReadWriteNBT nbt = NBT.parseNBT(nbtString);
        return Arrays.asList(NBT.itemStackArrayFromNBT(nbt));
    }
}
