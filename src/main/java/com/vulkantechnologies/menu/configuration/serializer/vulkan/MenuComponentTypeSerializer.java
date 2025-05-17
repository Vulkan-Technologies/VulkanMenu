package com.vulkantechnologies.menu.configuration.serializer.vulkan;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import com.vulkantechnologies.menu.model.Component;
import com.vulkantechnologies.menu.registry.ComponentAdapterRegistry;
import com.vulkantechnologies.menu.registry.ComponentRegistry;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuComponentTypeSerializer<T extends Component> implements TypeSerializer<Component> {

    private static final Pattern TYPE_PATTERN = Pattern.compile("^\\[([a-z0-9_-]+)]");
    private final ComponentRegistry<T> registry;
    private final ComponentAdapterRegistry<T> adapterRegistry;

    @Override
    public T deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        if (node.virtual())
            return null;

        String raw = node.getString();
        if (raw == null || raw.isEmpty())
            throw new SerializationException("Component cannot be empty");

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

        try {
            Constructor<? extends Component> constructor = (Constructor<? extends Component>) componentType.getConstructors()[0];
            if (constructor.getParameterCount() == 0)
                return (T) constructor.newInstance();
            else if (constructor.getParameterCount() != 1)
                throw new SerializationException("Component type " + componentName + " does not have a constructor that takes a String");

            Class<?> parameterType = constructor.getParameterTypes()[0];
            if (parameterType.isAssignableFrom(String.class))
                return (T) constructor.newInstance(raw);
            else if (parameterType.isAssignableFrom(Integer.class))
                return (T) constructor.newInstance(Integer.valueOf(raw));
            else if (parameterType.isAssignableFrom(Double.class))
                return (T) constructor.newInstance(Double.valueOf(raw));
            else if (parameterType.isAssignableFrom(Boolean.class))
                return (T) constructor.newInstance(Boolean.valueOf(raw));
            else if (parameterType.isAssignableFrom(Long.class))
                return (T) constructor.newInstance(Long.valueOf(raw));
            else if (parameterType.isAssignableFrom(Float.class))
                return (T) constructor.newInstance(Float.valueOf(raw));
            else
                throw new SerializationException("Component type " + componentName + " does not have a constructor that takes a String");
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new SerializationException("Component type " + componentName + " does not have a constructor that takes a String");
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Component obj, @NotNull ConfigurationNode node) throws SerializationException {
        throw new UnsupportedOperationException("Serialization is not supported");
    }


}
