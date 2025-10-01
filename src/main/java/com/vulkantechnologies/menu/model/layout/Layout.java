package com.vulkantechnologies.menu.model.layout;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.supplier.ItemStackSupplier;

@ConfigSerializable
public record Layout(String namespace, ItemStackSupplier supplier, List<Integer> slots, @Nullable String pageVariable) {

}
