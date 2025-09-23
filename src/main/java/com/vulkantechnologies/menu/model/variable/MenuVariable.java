package com.vulkantechnologies.menu.model.variable;

import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Getter
public class MenuVariable<T> implements TagResolver.Single {

    public static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");

    private final String name;
    private final Class<T> type;
    private final CompactAdapter<T> adapter;
    private T value;

    public MenuVariable(String name, Class<T> type, CompactAdapter<T> adapter, T value) {
        if (!isValidVariableName(name)) {
            throw new IllegalArgumentException("Invalid variable name: " + name + ". Must match pattern: " + VARIABLE_NAME_PATTERN.pattern());
        }
        this.name = name;
        this.type = type;
        this.adapter = adapter;
        this.value = value;
    }

    public MenuVariable<T> value(String value) {
        this.value = this.adapter.adapt(new CompactContext(value));
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

    public static boolean isValidVariableName(String name) {
        return name != null && VARIABLE_NAME_PATTERN.matcher(name).matches();
    }
}
