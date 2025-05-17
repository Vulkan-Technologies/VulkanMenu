package com.vulkantechnologies.menu.model.component;

public interface ComponentAdapter<T> {

    T parse(String raw);

}
