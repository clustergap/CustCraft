package cc.zoyn.core.util.reflect;

import java.lang.reflect.Constructor;

public interface ConstructorFilter {
    boolean accept(Constructor<?> paramConstructor);
}
