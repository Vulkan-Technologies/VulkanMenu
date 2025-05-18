package com.vulkantechnologies.menu.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.model.provider.ItemStackProvider;

public class ItemStackProviderRegistry {

    private final List<ItemStackProvider> providers = new ArrayList<>();

    public void register(ItemStackProvider provider) {
        providers.add(provider);
    }

    public void unregister(ItemStackProvider provider) {
        providers.remove(provider);
    }

    public Optional<ItemStackProvider> findByPrefix(String prefix) {
        return providers.stream()
                .filter(provider -> provider.prefix().equalsIgnoreCase(prefix))
                .findFirst();
    }

    @Unmodifiable
    public List<ItemStackProvider> providers() {
        return List.copyOf(providers);
    }
}
