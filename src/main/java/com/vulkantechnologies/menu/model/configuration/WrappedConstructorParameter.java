package com.vulkantechnologies.menu.model.configuration;

import org.jetbrains.annotations.Nullable;

import com.vulkantechnologies.menu.model.CompactAdapter;

public record WrappedConstructorParameter(Class<?> type, CompactAdapter<?> deserializer,
                                          @Nullable Class<?> genericType) {

    public boolean hasGenericType() {
        return genericType != null;
    }

    public static WrappedConstructorParameter of(Class<?> type, CompactAdapter<?> deserializer) {
        return new WrappedConstructorParameter(type, deserializer, null);
    }

    public static WrappedConstructorParameter of(Class<?> type, CompactAdapter<?> deserializer, Class<?> genericType) {
        return new WrappedConstructorParameter(type, deserializer, genericType);
    }
}
