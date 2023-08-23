package cn.mscraft.custcraft.Command;

public class SafeThis {
    public static String get(String[] array, int index, String def) {
        if (array != null &&
                index > -1 &&
                index < array.length)
            return array[index];
        return def;
    }
}
