package com.vulkantechnologies.menu.model.variable;


import org.jetbrains.annotations.NotNull;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@AllArgsConstructor
@Getter
public class MenuVariable<T> implements TagResolver.Single {

    private final String name;
    private final Class<T> type;
    private final CompactAdapter<T> adapter;
    private T value;

    public MenuVariable<T> value(String value) {
        this.value = this.adapter.deserialize(new CompactContext(value));
        return this;
    }

    @Override
    public @NotNull String key() {
        return "variable-" + this.name;
    }

    @Override
    public @NotNull Tag tag() {
        return Tag.preProcessParsed(this.value.toString());
    }
}
