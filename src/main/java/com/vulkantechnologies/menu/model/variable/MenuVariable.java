package com.vulkantechnologies.menu.model.variable;


import lombok.Data;

@Data
public class MenuVariable {

    private final String name;
    private final Class<?> type;
    private Object value;


}
