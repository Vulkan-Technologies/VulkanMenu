package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;
import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;
import com.vulkantechnologies.menu.model.wrapper.TitleWrapper;

public class CompactTitleWrapperAdapter implements CompactAdapter<TitleWrapper> {

    public static final CompactTitleWrapperAdapter INSTANCE = new CompactTitleWrapperAdapter();

    @Override
    public TitleWrapper adapt(CompactContext context) {
        String title;
        String subTitle = null;
        int fadeIn = 10;
        int stay = 70;
        int fadeOut = 20;

        String[] parts = context.raw().split(";");
        if (parts.length == 0)
            throw new IllegalArgumentException("Title wrapper requires at least one argument");

        title = parts[0];
        if (parts.length > 1)
            subTitle = parts[1];
        if (parts.length > 2)
            fadeIn = Integer.parseInt(parts[2]);
        if (parts.length > 3)
            stay = Integer.parseInt(parts[3]);
        if (parts.length > 4)
            fadeOut = Integer.parseInt(parts[4]);

        return new TitleWrapper(
                new ComponentWrapper(title),
                subTitle == null ? null : new ComponentWrapper(subTitle),
                fadeIn,
                stay,
                fadeOut
        );
    }

    @Override
    public Class<TitleWrapper> type() {
        return TitleWrapper.class;
    }
}
