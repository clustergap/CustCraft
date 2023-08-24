package cc.zoyn.core.util.serializer;

import cc.zoyn.core.util.nms.NMSUtils;
import cn.mscraft.custcraft.CustCraft;
import cn.mscraft.custcraft.Util.LogUtil;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ItemSerializerUtils {
    private static Method WRITE_NBT;

    private static Method READ_NBT;

    public static String toBase64(List<ItemStack> items) {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
        Object localNBTTagList = null;
        try {
            localNBTTagList = NMSUtils.getNMSClass("NBTTagList").getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            LogUtil.send("" + e.getMessage());
        }
        try {
            for (ItemStack item : items) {
                Object localCraftItemStack = Class.forName("org.bukkit.craftbukkit." + NMSUtils.getVersion() + ".inventory.CraftItemStack").getMethod("asCraftCopy", new Class[] { ItemStack.class }).invoke(item, new Object[] { item });
                Object localNBTTagCompound = NMSUtils.getNMSClass("NBTTagCompound").getConstructor(new Class[0]).newInstance(new Object[0]);
                if (localCraftItemStack != null)
                    try {
                        Object nmsItem = NMSUtils.getNMSItem(item);
                        nmsItem.getClass().getMethod("save", new Class[] { NMSUtils.getNMSClass("NBTTagCompound") }).invoke(nmsItem, new Object[] { localNBTTagCompound });
                    } catch (NullPointerException localNullPointerException) {
                        LogUtil.send("" + localNullPointerException.getMessage());
                    }
                String version = NMSUtils.getVersion();
                int subVersion = Integer.parseInt(version.split("_")[1]);
                if (subVersion >= 14) {
                    localNBTTagList.getClass().getMethod("add", new Class[] { int.class, NMSUtils.getNMSClass("NBTBase") }).invoke(localNBTTagList, new Object[] { Integer.valueOf(0), localNBTTagCompound });
                    continue;
                }
                localNBTTagList.getClass().getMethod("add", new Class[] { NMSUtils.getNMSClass("NBTBase") }).invoke(localNBTTagList, new Object[] { localNBTTagCompound });
            }
        } catch (Exception e) {
            LogUtil.send("" + e.getMessage());
        }
        if (WRITE_NBT == null)
            try {
                WRITE_NBT = NMSUtils.getNMSClass("NBTCompressedStreamTools").getDeclaredMethod("a", new Class[] { NMSUtils.getNMSClass("NBTBase"), DataOutput.class });
                WRITE_NBT.setAccessible(true);
            } catch (Exception localException1) {
                throw new IllegalStateException("未找到写入方法", localException1);
            }
        try {
            WRITE_NBT.invoke(null, new Object[] { localNBTTagList, localDataOutputStream });
        } catch (Exception localException2) {
            throw new IllegalArgumentException("+ localNBTTagList + "+ localDataOutputStream, localException2);
        }
        return Base64Coder.encodeLines(localByteArrayOutputStream.toByteArray());
    }

    public static List<ItemStack> fromBase64(String paramString) {
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(paramString));
        Object localNBTTagList = readNbt(new DataInputStream(localByteArrayInputStream));
        try {
            ItemStack[] arrayOfItemStack = new ItemStack[((Integer)localNBTTagList.getClass().getMethod("size", new Class[0]).invoke(localNBTTagList, new Object[0])).intValue()];
            for (int i = 0; i < arrayOfItemStack.length; i++) {
                Object localNBTTagCompound = localNBTTagList.getClass().getMethod("get", new Class[] { int.class }).invoke(localNBTTagList, new Object[] { Integer.valueOf(i) });
                if (!((Boolean)localNBTTagCompound.getClass().getMethod("isEmpty", new Class[0]).invoke(localNBTTagCompound, new Object[0])).booleanValue()) {
                    String version = NMSUtils.getVersion();
                    int subVersion = Integer.parseInt(version.split("_")[1]);
                    if (subVersion >= 11) {
                        Constructor<?> constructor = NMSUtils.getNMSClass("ItemStack").getDeclaredConstructor(new Class[] { NMSUtils.getNMSClass("NBTTagCompound") });
                        constructor.setAccessible(true);
                        Object nmsItem = constructor.newInstance(new Object[] { localNBTTagCompound });
                        arrayOfItemStack[i] = (ItemStack)NMSUtils.getOBCClass("inventory.CraftItemStack").getMethod("asCraftMirror", new Class[] { NMSUtils.getNMSClass("ItemStack") }).invoke(nmsItem, new Object[] { nmsItem });
                    } else {
                        arrayOfItemStack[i] =
                                (ItemStack)Class.forName("org.bukkit.craftbukkit." + NMSUtils.getVersion() + ".inventory.CraftItemStack")
                                        .getMethod("asCraftMirror", new Class[] { NMSUtils.getNMSClass("ItemStack") }).invoke(localNBTTagCompound, new Object[] { NMSUtils.getNMSClass("ItemStack")
                                                .getMethod("createStack", new Class[] { NMSUtils.getNMSClass("NBTTagCompound") }).invoke(localNBTTagCompound, new Object[] { localNBTTagCompound }) });
                    }
                }
            }
            if (!CustCraft.info.isOldVersion()) {
                List<ItemStack> itemsList = new ArrayList<>(Arrays.asList(arrayOfItemStack));
                Collections.reverse(itemsList);
                return itemsList;
            }
            return new ArrayList<>(Arrays.asList(arrayOfItemStack));
        } catch (Exception e) {
            LogUtil.send("" + e.getMessage());
            return null;
        }
    }

    private static Object readNbt(DataInput paramDataInput) {
        if (READ_NBT == null)
            try {
                READ_NBT = NMSUtils.getNMSClass("NBTCompressedStreamTools").getDeclaredMethod("a", new Class[] { DataInput.class, int.class,
                        NMSUtils.getNMSClass("NBTReadLimiter") });
                READ_NBT.setAccessible(true);
            } catch (Exception localException1) {
                throw new IllegalStateException("未找到方法.", localException1);
            }
        try {
            Object limiter = NMSUtils.getNMSClass("NBTReadLimiter").getConstructor(new Class[] { long.class }).newInstance(new Object[] { Long.valueOf(Long.MAX_VALUE) });
            return READ_NBT.invoke(null, new Object[] { paramDataInput, Integer.valueOf(0), limiter });
        } catch (Exception localException2) {
            throw new IllegalArgumentException("无法从该位置读取数据" + paramDataInput, localException2);
        }
    }
}

