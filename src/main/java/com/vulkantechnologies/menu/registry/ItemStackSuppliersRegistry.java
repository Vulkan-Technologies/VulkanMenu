package com.vulkantechnologies.menu.registry;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.model.supplier.ItemStackSupplier;

public class ItemStackSuppliersRegistry {

    private final Set<ItemStackSupplier> suppliers = new HashSet<>();

    public void register(ItemStackSupplier supplier) {
        suppliers.add(supplier);
    }

    public void unregister(ItemStackSupplier supplier) {
        suppliers.remove(supplier);
    }

    public Optional<ItemStackSupplier> findByNamespace(String namespace) {
        return suppliers.stream()
                .filter(supplier -> supplier.namespace().equalsIgnoreCase(namespace))
                .findFirst();
    }

    @Unmodifiable
    public Set<ItemStackSupplier> suppliers() {
        return Set.copyOf(suppliers);
    }
}
