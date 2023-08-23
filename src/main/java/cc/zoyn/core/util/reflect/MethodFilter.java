package cc.zoyn.core.util.reflect;

import java.lang.reflect.Method;

public interface MethodFilter {
    boolean accept(Method paramMethod);
}