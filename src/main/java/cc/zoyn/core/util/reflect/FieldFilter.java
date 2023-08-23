package cc.zoyn.core.util.reflect;

import java.lang.reflect.Field;

public interface FieldFilter {
    boolean accept(Field paramField);
}
