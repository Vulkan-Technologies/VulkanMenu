package com.vulkantechnologies.menu.configuration;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import lombok.Getter;

@ConfigSerializable
@Getter
public class MenuConfiguration {

    private String title;
    private int size;
    private @Nullable String openCommand;
}
