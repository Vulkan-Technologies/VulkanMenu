package com.vulkantechnologies.menu.registry;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;

public class CompactAdapterRegistry {

    private final Map<Class<?>, CompactAdapter<?>> adapters = new ConcurrentHashMap<>();

    public void register(CompactAdapter<?> adapter) {
        this.adapters.put(adapter.type(), adapter);
    }

    public <T> CompactAdapter<T> get(Class<T> type) {
        return (CompactAdapter<T>) this.adapters.get(type);
    }

    public <T> boolean contains(Class<T> type) {
        return this.adapters.containsKey(type);
    }

    public <T> void unregister(Class<T> type) {
        this.adapters.remove(type);
    }

    public Optional<CompactAdapter<?>> findEntry(Predicate<CompactAdapter<?>> predicate) {
        return this.adapters.values()
                .stream()
                .filter(predicate)
                .findFirst();
    }

    @Unmodifiable
    public Map<Class<?>, CompactAdapter<?>> getAdapters() {
        return Map.copyOf(this.adapters);
    }


}
