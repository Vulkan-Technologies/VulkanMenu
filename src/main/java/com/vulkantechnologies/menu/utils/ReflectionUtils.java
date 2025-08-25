package com.vulkantechnologies.menu.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReflectionUtils {

    public static Class<?> getGenericType(Type type) {
        if (!(type instanceof ParameterizedType paramType))
            return null;
        Type[] typeArguments = paramType.getActualTypeArguments();
        if (typeArguments.length == 0)
            return null;
        Type typeArgument = typeArguments[0];
        if (typeArgument instanceof Class) {
            return (Class<?>) typeArgument;
        } else if (typeArgument instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) typeArgument).getRawType();
        }
        return null;
    }

}
