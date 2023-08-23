package cc.zoyn.core.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtils {
    public static Field getFieldByFieldName(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = null;
        if (hasField(clazz, fieldName))
            field = clazz.getField(fieldName);
        return field;
    }

    public static Field getFieldByFieldName(String classPath, String fieldName) throws ClassNotFoundException, NoSuchFieldException {
        return getFieldByFieldName(Class.forName(classPath), fieldName);
    }

    public static Object getValueByFieldName(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field = getFieldByFieldName(obj.getClass(), fieldName);
        Object value = null;
        if (field != null)
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        return value;
    }

    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Constructor<?> constructor = null;
        if (hasConstructor(clazz, parameterTypes))
            constructor = clazz.getDeclaredConstructor(parameterTypes);
        return constructor;
    }

    public static Constructor<?> getConstructor(String classPath, Class<?>... parameterTypes) throws ClassNotFoundException, NoSuchMethodException {
        return getConstructor(Class.forName(classPath), parameterTypes);
    }

    public static Object instantiateObject(Constructor<?> constructor, Object... arguments) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance(arguments);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        if (hasMethod(clazz, methodName, parameterTypes))
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        return null;
    }

    public static Method getMethod(String classPath, String methodName, Class<?>... parameterTypes) throws ClassNotFoundException, NoSuchMethodException {
        return getMethod(Class.forName(classPath), methodName, parameterTypes);
    }

    public static Object invokeMethod(Method method, Object object, Object... arguments) throws InvocationTargetException, IllegalAccessException {
        Object o;
        if (method.isAccessible()) {
            o = method.invoke(object, arguments);
        } else {
            method.setAccessible(true);
            o = method.invoke(object, arguments);
            method.setAccessible(false);
        }
        return o;
    }

    public static Object invokeMethod(String methodName, Object object, Object... arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?>[] classes = new Class[0];
        for (int i = 0; i < arguments.length; i++)
            classes[i] = arguments[i].getClass();
        Method method = getMethod(object.getClass(), methodName, classes);
        return invokeMethod(method, object, arguments);
    }

    public static boolean hasField(Class<?> clazz, String fieldName) {
        boolean has;
        try {
            clazz.getDeclaredField(fieldName);
            has = true;
        } catch (NoSuchFieldException e) {
            has = false;
        }
        return has;
    }

    public static boolean hasField(Class<?> clazz, FieldFilter filter) {
        boolean has = false;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (filter.accept(field)) {
                has = true;
                break;
            }
        }
        return has;
    }

    public static boolean hasField(String classPath, String fieldName) throws ClassNotFoundException {
        return hasField(Class.forName(classPath), fieldName);
    }

    public static boolean hasConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        boolean has;
        try {
            clazz.getDeclaredConstructor(parameterTypes);
            has = true;
        } catch (NoSuchMethodException e) {
            has = false;
        }
        return has;
    }

    public static boolean hasConstructor(Class<?> clazz, ConstructorFilter filter) {
        boolean has = false;
        Constructor[] arrayOfConstructor = (Constructor[])clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : arrayOfConstructor) {
            if (filter.accept(constructor)) {
                has = true;
                break;
            }
        }
        return has;
    }

    public static boolean hasConstructor(String classPath, Class<?>... parameterTypes) throws ClassNotFoundException {
        return hasConstructor(Class.forName(classPath), parameterTypes);
    }

    public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        boolean has;
        try {
            clazz.getDeclaredMethod(methodName, parameterTypes);
            has = true;
        } catch (NoSuchMethodException e) {
            has = false;
        }
        return has;
    }

    public static boolean hasMethod(Class<?> clazz, MethodFilter filter) {
        boolean has = false;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (filter.accept(method)) {
                has = true;
                break;
            }
        }
        return has;
    }

    public static boolean hasMethod(String classPath, String methodName, Class<?>... parameterTypes) throws ClassNotFoundException {
        return hasMethod(Class.forName(classPath), methodName, parameterTypes);
    }
}
