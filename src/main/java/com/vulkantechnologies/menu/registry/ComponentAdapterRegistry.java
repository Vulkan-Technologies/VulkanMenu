package com.vulkantechnologies.menu.registry;

import java.util.HashMap;
import java.util.Map;

import com.vulkantechnologies.menu.model.component.MenuComponent;
import com.vulkantechnologies.menu.model.component.MenuComponentAdapter;

public class ComponentAdapterRegistry<T extends MenuComponent> {

    private final Map<Class<? extends T>, MenuComponentAdapter<T>> adapters = new HashMap<>();

    public <C extends T> void register(Class<C> type, MenuComponentAdapter<C> adapter) {
        this.adapters.put(type, (MenuComponentAdapter<T>) adapter);
    }

    public <C extends T> MenuComponentAdapter<C> getAdapter(Class<C> type) {
        return (MenuComponentAdapter<C>) this.adapters.get(type);
    }

    public <C extends T> boolean isRegistered(Class<C> type) {
        return this.adapters.containsKey(type);
    }

    public <C extends T> void unregister(Class<C> type) {
        this.adapters.remove(type);
    }

    public Map<Class<? extends T>, MenuComponentAdapter<T>> getAdapters() {
        return this.adapters;
    }
}
