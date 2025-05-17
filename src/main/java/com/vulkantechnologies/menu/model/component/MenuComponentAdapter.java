package com.vulkantechnologies.menu.model.component;

public interface MenuComponentAdapter<T extends MenuComponent> {

    T parse(String raw);

}
