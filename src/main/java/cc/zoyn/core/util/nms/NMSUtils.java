package cc.zoyn.core.util.nms;

import cc.zoyn.core.util.reflect.ReflectionUtils;
import cn.mscraft.custcraft.CustCraft;
import cn.mscraft.custcraft.Util.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class NMSUtils {
    private static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    private static Field playerConnectionField;

    private static Method sendPacketMethod;

    private static Method asNMSCopyMethod;

    private static Method asBukkitCopyMethod;

    private static Method stringAsIChatBaseComponentMethod;

    private static Method craftBukkitEntityPlayerGetHandleMethod;

    static {
        try {
            playerConnectionField = ReflectionUtils.getFieldByFieldName(getNMSClass("EntityPlayer"), "playerConnection");
            sendPacketMethod = ReflectionUtils.getMethod(getNMSClass("PlayerConnection"), "sendPacket", new Class[] { getNMSClass("Packet") });
            asNMSCopyMethod = ReflectionUtils.getMethod(getOBCClass("inventory.CraftItemStack"), "asNMSCopy", new Class[] { ItemStack.class });
            asBukkitCopyMethod = ReflectionUtils.getMethod(getOBCClass("inventory.CraftItemStack"), "asBukkitCopy", new Class[] { getNMSClass("ItemStack") });
            stringAsIChatBaseComponentMethod = ReflectionUtils.getMethod(getNMSClass("IChatBaseComponent$ChatSerializer"), "a", new Class[] { String.class });
            craftBukkitEntityPlayerGetHandleMethod = ReflectionUtils.getMethod(getOBCClass("entity.CraftPlayer"), "getHandle", new Class[0]);
        } catch (NoSuchMethodException|NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static String getVersion() {
        return version;
    }

    public static Class<?> getOBCClass(String className) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getBukkitItem(Object nmsItem) {
        if (asBukkitCopyMethod == null)
            try {
                asNMSCopyMethod = ReflectionUtils.getMethod(getOBCClass("inventory.CraftItemStack"), "asNMSCopy", new Class[] { ItemStack.class });
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            return ReflectionUtils.invokeMethod(asBukkitCopyMethod, null, new Object[] { nmsItem });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getNMSItem(ItemStack itemStack) {
        Validate.notNull(itemStack);
        if (asNMSCopyMethod == null) {
            Class<?> craftItemStack = getOBCClass("inventory.CraftItemStack");
            try {
                asNMSCopyMethod = ReflectionUtils.getMethod(craftItemStack, "asNMSCopy", new Class[] { ItemStack.class });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            return ReflectionUtils.invokeMethod(asNMSCopyMethod, null, new Object[] { itemStack });
        } catch (Exception e) {
            e.printStackTrace();
            return itemStack;
        }
    }

    public static Class<?> getNMSClass(String className) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + className);
        } catch (Exception e) {
            LogUtil.send("" + e.getMessage());
            return null;
        }
    }

    public static void sendPacket(Player player, Object packet) {
        Object entityPlayer = getNMSPlayer(player);
        if (playerConnectionField == null)
            try {
                playerConnectionField = ReflectionUtils.getFieldByFieldName(getNMSClass("EntityPlayer"), "playerConnection");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        if (sendPacketMethod == null)
            try {
                sendPacketMethod = ReflectionUtils.getMethod(getNMSClass("PlayerConnection"), "sendPacket", new Class[] { getNMSClass("Packet") });
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        try {
            Object playerConnection = playerConnectionField.get(entityPlayer);
            ReflectionUtils.invokeMethod(sendPacketMethod, playerConnection, new Object[] { packet });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getNMSPlayer(Player player) {
        try {
            return ReflectionUtils.invokeMethod(craftBukkitEntityPlayerGetHandleMethod, player, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return player;
        }
    }

    public static Object stringToIChatBaseComponent(String text) {
        if (stringAsIChatBaseComponentMethod == null)
            try {
                stringAsIChatBaseComponentMethod = ReflectionUtils.getMethod(getNMSClass("IChatBaseComponent$ChatSerializer"), "a", new Class[] { String.class });
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            return ReflectionUtils.invokeMethod(stringAsIChatBaseComponentMethod, text, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class Validate {
        public static void notNull(ItemStack itemStack) {
        }
    }
}

