package com.vulkantechnologies.menu.registry;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.Component;

public class ComponentRegistry<E extends Component> {

    private final Map<String, Class<? extends E>> entries = new ConcurrentHashMap<>();

    public void register(Class<? extends E> element) {
        if (!element.isAnnotationPresent(ComponentName.class))
            throw new IllegalArgumentException("Element %s does not have an ElementName annotation".formatted(element.getName()));

        ComponentName elementName = element.getAnnotation(ComponentName.class);
        entries.put(elementName.value(), element);
    }

    public Optional<Class<? extends E>> get(String key) {
        return Optional.ofNullable(entries.get(key));
    }

    public boolean contains(String key) {
        return entries.containsKey(key);
    }

    public void unregister(String key) {
        entries.remove(key);
    }

    public Map<String, Class<? extends E>> entries() {
        return Collections.unmodifiableMap(entries);
    }
}
