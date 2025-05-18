package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.annotation.Single;
import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;
import com.vulkantechnologies.menu.model.configuration.WrappedConstructorParameter;
import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;

public class CompactComponentWrapperAdapter implements CompactAdapter<ComponentWrapper> {

    public static final CompactComponentWrapperAdapter INSTANCE = new CompactComponentWrapperAdapter();

    @Override
    public ComponentWrapper adapt(CompactContext context) {
        if (context.hasConstructor() && context.constructor().parameters().size() > 1) {
            WrappedConstructorParameter parameter = context.constructor().parameters().get(context.index());
            if (parameter.hasAnnotation(Single.class))
                return new ComponentWrapper(context.popFirstArg());
        }

        return new ComponentWrapper(context.remainingArgs());
    }

    @Override
    public Class<ComponentWrapper> type() {
        return ComponentWrapper.class;
    }
}
