package com.vulkantechnologies.menu.model.configuration;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WrappedConstructor {

    private final Class<?> declaringClass;
    private final Constructor<?> constructor;
    private final List<WrappedConstructorParameter> parameters = new ArrayList<>();

}

