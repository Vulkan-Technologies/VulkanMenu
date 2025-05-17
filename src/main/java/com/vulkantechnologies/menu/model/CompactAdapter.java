package com.vulkantechnologies.menu.model;

public interface CompactAdapter<T> {

    T adapt(String value);

    Class<T> type();

}
