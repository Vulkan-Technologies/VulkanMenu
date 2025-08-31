package com.vulkantechnologies.menu.configuration.serializer.vulkan;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;
import com.vulkantechnologies.menu.model.component.MenuComponent;
import com.vulkantechnologies.menu.model.configuration.WrappedConstructor;
import com.vulkantechnologies.menu.model.configuration.WrappedConstructorParameter;
import com.vulkantechnologies.menu.registry.ComponentAdapterRegistry;
import com.vulkantechnologies.menu.registry.ComponentRegistry;
import com.vulkantechnologies.menu.registry.Registries;
import com.vulkantechnologies.menu.utils.ReflectionUtils;

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
        final String originalRaw = raw;  // Keep original for error messages
        String componentName = TYPE_PATTERN.matcher(raw)
                .results()
                .findFirst()
                .map(match -> match.group(1))
                .orElseThrow(() -> new SerializationException(type, "No component type defined in: '" + originalRaw + "'"));

        // Get action class
        Class<? extends T> componentType = this.registry
                .get(componentName)
                .orElseThrow(() -> new SerializationException(type, "Unknown component type: " + componentName));

        // Remove the type prefix from the raw string
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
        for (int i = 0; i < componentConstructor.parameters().size(); i++) {
            WrappedConstructorParameter parameter = componentConstructor.parameters().get(i);
            try {
                Object argument = parseArgument(context, parameter, finalRaw);
                if (argument == null && !parameter.type().isAnnotationPresent(Nullable.class))
                    throw new SerializationException("Argument " + (i + 1) + " (" + parameter.type().getSimpleName() +
                                                     ") cannot be null for component: " + componentName);
                constructorArguments.add(argument);
            } catch (SerializationException e) {
                throw e; // Re-throw with original message
            } catch (Exception e) {
                throw new SerializationException(type, "Failed to parse argument " + (i + 1) + " (" +
                                                       parameter.type().getSimpleName() + ") for component " + componentName + ": " + e.getMessage());
            }
        }

        try {
            return (T) componentConstructor.constructor().newInstance(constructorArguments.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SerializationException(type, "Failed to instantiate component: " + componentName +
                                                   " with arguments: " + constructorArguments);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable MenuComponent obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        // Get the component name from annotation
        Class<?> componentClass = obj.getClass();
        if (!componentClass.isAnnotationPresent(ComponentName.class)) {
            throw new SerializationException("Component " + componentClass.getName() + " does not have @ComponentName annotation");
        }

        ComponentName componentName = componentClass.getAnnotation(ComponentName.class);
        String name = componentName.value();

        // Build the serialized string
        StringBuilder builder = new StringBuilder();
        builder.append("[")
                .append(name)
                .append("]")
                .append(" ");

        Class<? extends MenuComponent> componentType = obj.getClass();
        for (Field field : componentType.getDeclaredFields()) {
            field.trySetAccessible();
            Object value;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new SerializationException("Failed to access field " + field.getName() + " of component " + name);
            }

            // Get adapter for field type
            String serializedValue;
            try {
                serializedValue = deserialize(value);
            } catch (Exception e) {
                throw new SerializationException("Failed to serialize field " + field.getName() + " of component " + name + ": " + e.getMessage());
            }
            builder.append(serializedValue).append(" ");
        }

        if (!builder.isEmpty() && builder.charAt(builder.length() - 1) == ' ')
            builder.setLength(builder.length() - 1);

        node.set(builder.toString());
    }

    private <T> String deserialize(T value) throws SerializationException {
        if (value == null)
            return "null";

        Class<?> valueType = value.getClass();
        CompactAdapter<T> adapter = Registries.COMPACT_ADAPTER.findByClass(valueType)
                .map(a -> (CompactAdapter<T>) a)
                .orElseThrow(() -> new SerializationException("No adapter found for type: " + valueType.getName()));

        try {
            return adapter.serialize(value);
        } catch (Exception e) {
            throw new SerializationException("Failed to serialize value of type " + valueType.getName() + ": " + e.getMessage());
        }
    }

    private Object parseArgument(CompactContext context, WrappedConstructorParameter parameter, String raw) throws SerializationException {
        // Regular deserialization for non-generic types
        if (!parameter.hasGenericType()) {
            try {
                return parameter.deserializer().deserialize(context);
            } catch (Exception e) {
                throw new SerializationException(parameter.type(), "Failed to deserialize argument of type " + parameter.type().getSimpleName() + ": " + e.getMessage());
            }
        }

        // Generic deserialization
        if (parameter.type().equals(List.class)) {
            Class<?> elementType = parameter.genericType();
            String remaining = context.remainingArgs();

            if (remaining == null || remaining.isEmpty()) {
                return new ArrayList<>();
            }

            // Split by comma for list elements
            return Arrays.stream(remaining.split(","))
                    .map(String::trim)
                    .map(s -> {
                        try {
                            CompactContext elementContext = new CompactContext(s);
                            return parseArgument(elementContext,
                                    WrappedConstructorParameter.of(elementType, parameter.annotations(), parameter.deserializer()), s);
                        } catch (SerializationException e) {
                            throw new RuntimeException("Failed to parse list element: " + e.getMessage(), e);
                        }
                    })
                    .collect(Collectors.toList());
        }

        if (parameter.type().equals(Map.class)) {
            // Parse Map<K,V> - format: "key1=value1,key2=value2"
            String remaining = context.remainingArgs();
            if (remaining == null || remaining.isEmpty()) {
                return new HashMap<>();
            }

            Map<Object, Object> map = new HashMap<>();
            String[] entries = remaining.split(",");

            for (String entry : entries) {
                String[] keyValue = entry.split("=", 2);
                if (keyValue.length != 2) {
                    throw new SerializationException("Invalid map entry format: " + entry + ". Expected 'key=value'");
                }

                // For simplicity, assuming String keys and values
                // A full implementation would need to handle generic key/value types
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }

            return map;
        }

        if (parameter.type().equals(Set.class)) {
            // Parse Set<T> - similar to List but return as Set
            Class<?> elementType = parameter.genericType();
            String remaining = context.remainingArgs();

            if (remaining == null || remaining.isEmpty()) {
                return new HashSet<>();
            }

            return Arrays.stream(remaining.split(","))
                    .map(String::trim)
                    .map(s -> {
                        try {
                            CompactContext elementContext = new CompactContext(s);
                            return parseArgument(elementContext,
                                    WrappedConstructorParameter.of(elementType, parameter.annotations(), parameter.deserializer()), s);
                        } catch (SerializationException e) {
                            throw new RuntimeException("Failed to parse set element: " + e.getMessage(), e);
                        }
                    })
                    .collect(Collectors.toSet());
        }

        throw new SerializationException("Unsupported generic type: " + parameter.type().getName());
    }

    private Optional<WrappedConstructor> findSuitableConstructor(Class<?> componentClass) {
        Constructor<?>[] constructors = componentClass.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            Type[] parameterTypes = constructor.getGenericParameterTypes();

            boolean allMatch = true;
            for (Type parameterType : parameterTypes) {
                Class<?> rawType = getRawType(parameterType);
                Class<?> genericType = ReflectionUtils.getGenericType(parameterType);

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
                Class<?> genericType = ReflectionUtils.getGenericType(parameterType);
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