package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.annotation.Single;
import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;
import com.vulkantechnologies.menu.model.configuration.WrappedConstructorParameter;

public class CompactStringAdapter implements CompactAdapter<String> {

    public static final CompactStringAdapter INSTANCE = new CompactStringAdapter();

    @Override
    public String deserialize(CompactContext context) {
        if (context.hasConstructor() && context.constructor().parameters().size() > 1) {
            WrappedConstructorParameter parameter = context.constructor().parameters().get(context.index());
            if (parameter.hasAnnotation(Single.class))
                return context.popFirstArg();
        }

        return context.remainingArgs();
    }

    @Override
    public String serialize(String object) {
        return object;
    }

    @Override
    public Class<String> type() {
        return String.class;
    }
}
