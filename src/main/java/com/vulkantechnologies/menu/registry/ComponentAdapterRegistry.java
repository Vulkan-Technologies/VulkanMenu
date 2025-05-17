package com.vulkantechnologies.menu.registry;

import java.util.HashMap;
import java.util.Map;

import com.vulkantechnologies.menu.model.Component;
import com.vulkantechnologies.menu.model.component.ComponentAdapter;

public class ComponentAdapterRegistry<T extends Component> {

    private final Map<Class<? extends T>, ComponentAdapter<T>> adapters = new HashMap<>();

    public <C extends T> void register(Class<C> type, ComponentAdapter<C> adapter) {
        this.adapters.put(type, (ComponentAdapter<T>) adapter);
    }

    public <C extends T> ComponentAdapter<C> getAdapter(Class<C> type) {
        return (ComponentAdapter<C>) this.adapters.get(type);
    }

    public <C extends T> boolean isRegistered(Class<C> type) {
        return this.adapters.containsKey(type);
    }

    public <C extends T> void unregister(Class<C> type) {
        this.adapters.remove(type);
    }

    public Map<Class<? extends T>, ComponentAdapter<T>> getAdapters() {
        return this.adapters;
    }
}
