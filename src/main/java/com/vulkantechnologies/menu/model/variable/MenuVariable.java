package com.vulkantechnologies.menu.model.variable;


import org.jetbrains.annotations.NotNull;

import lombok.Data;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Data
public class MenuVariable implements TagResolver.Single {

    private final String name;
    private final Class<?> type;
    private Object value;


    @Override
    public @NotNull String key() {
        return "variable-" + this.name;
    }

    @Override
    public @NotNull Tag tag() {
        return Tag.preProcessParsed(this.value.toString());
    }
}
