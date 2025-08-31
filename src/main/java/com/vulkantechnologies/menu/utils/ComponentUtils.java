package com.vulkantechnologies.menu.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@UtilityClass
public class ComponentUtils {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();


    public static String legacyToMiniMessage(String legacy) {
        if (legacy == null || legacy.isEmpty())
            return "";

        if (!legacy.contains("&") && !legacy.contains("§"))
            return legacy;

        return MINI_MESSAGE.serialize(LEGACY_SERIALIZER.deserialize(legacy));
    }

}
