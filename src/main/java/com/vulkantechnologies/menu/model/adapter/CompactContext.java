package com.vulkantechnologies.menu.model.adapter;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.model.configuration.WrappedConstructor;

import lombok.Getter;

public class CompactContext {

    @Getter
    private final String raw;
    private final List<String> arguments;
    @Getter
    private final @Nullable WrappedConstructor constructor;
    @Getter
    private final int argumentCount;

    public CompactContext(String raw) {
        this(raw, null);
    }

    public CompactContext(String raw, @Nullable WrappedConstructor constructor) {
        this.raw = raw;
        this.constructor = constructor;
        this.arguments = new ArrayList<>();

        String[] args = raw.split(" ");
        if (args.length == 0)
            this.arguments.add(raw);
        else {
            for (String arg : args) {
                if (!arg.isEmpty())
                    this.arguments.add(arg);
            }
        }

        this.argumentCount = this.arguments.size();
    }

    public boolean hasConstructor() {
        return this.constructor != null;
    }

    public String popFirstArg() {
        return !this.arguments.isEmpty() ? this.arguments.removeFirst() : null;
    }

    public String popLastArg() {
        return !this.arguments.isEmpty() ? this.arguments.removeLast() : null;
    }

    public String getFirstArg() {
        return !this.arguments.isEmpty() ? this.arguments.getFirst() : null;
    }

    public String getLastArg() {
        return !this.arguments.isEmpty() ? this.arguments.getLast() : null;
    }

    public boolean isLastArg() {
        return !this.arguments.isEmpty();
    }

    public String remainingArgs() {
        return String.join(" ", this.arguments);
    }

    public int index() {
        return this.arguments.size() - 1;
    }

    @Unmodifiable
    public List<String> arguments() {
        return List.copyOf(this.arguments);
    }
}
