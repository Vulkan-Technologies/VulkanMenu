package com.vulkantechnologies.menu.model.configuration;

import java.lang.annotation.Annotation;

import org.jetbrains.annotations.Nullable;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;

public record WrappedConstructorParameter(Class<?> type, Annotation[] annotations, CompactAdapter<?> deserializer,
                                          @Nullable Class<?> genericType) {

    public boolean hasGenericType() {
        return genericType != null;
    }

    public boolean hasAnnotations() {
        return annotations != null && annotations.length > 0;
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        if (annotations == null)
            return false;

        for (Annotation a : annotations) {
            if (a.annotationType() == annotation)
                return true;
        }

        return false;
    }

    public static WrappedConstructorParameter of(Class<?> type, Annotation[] annotations, CompactAdapter<?> deserializer) {
        return new WrappedConstructorParameter(type, annotations, deserializer, null);
    }

    public static WrappedConstructorParameter of(Class<?> type, Annotation[] annotations, CompactAdapter<?> deserializer, Class<?> genericType) {
        return new WrappedConstructorParameter(type, annotations, deserializer, genericType);
    }
}
