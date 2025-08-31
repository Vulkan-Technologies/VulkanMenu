package com.vulkantechnologies.menu.model.adapter;

public interface CompactAdapter<T> {

    T deserialize(CompactContext context);

    String serialize(T object);

    Class<T> type();

}
