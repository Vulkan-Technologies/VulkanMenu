package com.vulkantechnologies.menu.model.adapter;

public interface CompactAdapter<T> {

    T adapt(CompactContext context);

    Class<T> type();

}
