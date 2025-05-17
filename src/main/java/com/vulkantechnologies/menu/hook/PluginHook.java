package com.vulkantechnologies.menu.hook;

public interface PluginHook {

    void onSuccess();

    void onFailure();

    String pluginName();
}
