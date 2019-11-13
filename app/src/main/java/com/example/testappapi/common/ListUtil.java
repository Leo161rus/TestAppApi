package com.example.testappapi.common;




import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ListUtil {
    public static <T> T[] toArray(Class<T> typeClass, @NotNull ArrayList<T> list) {
        T[] array = (T[]) Array.newInstance(typeClass, list.size());
        return list.toArray(array);
    }
}
