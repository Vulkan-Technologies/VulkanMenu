package com.vulkantechnologies.menu.configuration.serializer.vulkan;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import com.vulkantechnologies.menu.model.adapter.CompactContext;
import com.vulkantechnologies.menu.model.component.MenuComponent;
import com.vulkantechnologies.menu.model.configuration.WrappedConstructor;
import com.vulkantechnologies.menu.model.configuration.WrappedConstructorParameter;
import com.vulkantechnologies.menu.registry.ComponentAdapterRegistry;
import com.vulkantechnologies.menu.registry.ComponentRegistry;
import com.vulkantechnologies.menu.registry.Registries;
import com.vulkantechnologies.menu.utils.ReflectionUtilsNew;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuComponentTypeSerializer<T extends MenuComponent> implements TypeSerializer<MenuComponent> {

    private static final Pattern TYPE_PATTERN = Pattern.compile("^\\[([a-z0-9_-]+)]");
    private final ComponentRegistry<T> registry;
    private final ComponentAdapterRegistry<T> adapterRegistry;

    @Override
    public T deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        if (node.virtual())
            return null;

        String raw = node.getString();
        if (raw == null || raw.isEmpty())
            throw new SerializationException("MenuComponent cannot be empty");

        // Get the action type
        String componentName = TYPE_PATTERN.matcher(raw)
                .results()
                .findFirst()
                .map(match -> match.group(1))
                .orElseThrow(() -> new SerializationException("No component type defined"));

        // Get action class
        Class<? extends T> componentType = this.registry
                .get(componentName)
                .orElseThrow(() -> new SerializationException("Unknown component type: " + componentName));

        raw = raw.replaceFirst(TYPE_PATTERN.pattern(), "");
        if (raw.startsWith(" "))
            raw = raw.substring(1);

        // Find a suitable constructor for the action
        Optional<WrappedConstructor> constructor = findSuitableConstructor(componentType);
        if (constructor.isEmpty())
            throw new SerializationException("No suitable constructor found for component: " + componentName);
        WrappedConstructor componentConstructor = constructor.get();

        // Create context
        CompactContext context = new CompactContext(raw, componentConstructor);

        // Parse arguments
        List<Object> constructorArguments = new ArrayList<>();
        String finalRaw = raw;
        for (WrappedConstructorParameter parameter : componentConstructor.parameters()) {
            Object argument = parseArgument(context, parameter, finalRaw);
            if (argument == null && !parameter.type().isAnnotationPresent(Nullable.class))
                throw new SerializationException("Argument cannot be null: " + parameter.type().getSimpleName());
            constructorArguments.add(argument);
        }

        try {
            return (T) componentConstructor.constructor().newInstance(constructorArguments.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate component: " + componentName, e);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable MenuComponent obj, @NotNull ConfigurationNode node) {
        throw new UnsupportedOperationException("Serialization is not supported");
    }

    private Object parseArgument(CompactContext context, WrappedConstructorParameter parameter, String raw) throws SerializationException {
        // Regular deserialization
        if (!parameter.hasGenericType()) {
            try {
                return parameter.deserializer().adapt(context);
            } catch (Exception e) {
                throw new SerializationException("Failed to deserialize argument: " + parameter.type().getSimpleName());
            }
        }

        // Generic deserialization
        if (parameter.type().equals(List.class))
            return Arrays.stream(raw.split(","))
                    .map(s -> {
                        try {
                            return parseArgument(context, WrappedConstructorParameter.of(parameter.genericType(), parameter.annotations(), parameter.deserializer()), s);
                        } catch (SerializationException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

        // TODO: Implement map, set, collections, ... deserialization

        throw new SerializationException("Unsupported generic type: " + parameter.genericType());
    }

    private Optional<WrappedConstructor> findSuitableConstructor(Class<?> componentClass) {
        Constructor<?>[] constructors = componentClass.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            Type[] parameterTypes = constructor.getGenericParameterTypes();

            boolean allMatch = true;
            for (Type parameterType : parameterTypes) {
                Class<?> rawType = getRawType(parameterType);
                Class<?> genericType = ReflectionUtilsNew.getGenericType(parameterType);

                boolean found = Registries.COMPACT_ADAPTER.findEntry(deserializer ->
                        matchesType(deserializer.type(), rawType, genericType)
                ).isPresent();

                if (!found) {
                    allMatch = false;
                    break;
                }
            }

            if (!allMatch) continue;

            // Valid constructor found
            Annotation[][] annotations = constructor.getParameterAnnotations();
            WrappedConstructor wrappedConstructor = new WrappedConstructor(componentClass, constructor);

            for (int i = 0; i < parameterTypes.length; i++) {
                Type parameterType = parameterTypes[i];
                Class<?> rawType = getRawType(parameterType);
                Class<?> genericType = ReflectionUtilsNew.getGenericType(parameterType);
                Annotation[] paramAnnotations = annotations[i];

                Registries.COMPACT_ADAPTER.findEntry(deserializer ->
                        matchesType(deserializer.type(), rawType, genericType)
                ).ifPresent(deserializer -> {
                    if (deserializer.type().equals(rawType)) {
                        wrappedConstructor.parameters().add(
                                WrappedConstructorParameter.of(rawType, paramAnnotations, deserializer)
                        );
                    } else {
                        wrappedConstructor.parameters().add(
                                WrappedConstructorParameter.of(rawType, paramAnnotations, deserializer, genericType)
                        );
                    }
                });
            }

            return Optional.of(wrappedConstructor);
        }

        return Optional.empty();
    }

    private boolean matchesType(Class<?> candidate, Class<?> raw, Class<?> generic) {
        return candidate.equals(raw)
               || candidate.equals(getWrapperType(raw))
               || candidate.equals(generic);
    }

    private Class<?> getRawType(Type type) {
        if (type instanceof ParameterizedType) {
            Type raw = ((ParameterizedType) type).getRawType();
            if (raw instanceof Class<?>) {
                return (Class<?>) raw;
            }
        } else if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return Object.class; // Fallback
    }

    private Class<?> getWrapperType(Class<?> type) {
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        if (type == short.class) return Short.class;
        if (type == byte.class) return Byte.class;
        if (type == char.class) return Character.class;
        if (type == boolean.class) return Boolean.class;
        return type;
    }

}
