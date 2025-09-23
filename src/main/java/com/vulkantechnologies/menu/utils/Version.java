package com.vulkantechnologies.menu.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Version {
    UNDEFINED(-1, -1),
    V_1_7_10(5, -1),

    V_1_8(47, -1),

    V_1_9(107, -1),
    V_1_9_1(108, -1),
    V_1_9_2(109, -1),
    /**
     * 1.9.3 or 1.9.4 as they have the same protocol version.
     */
    V_1_9_3(110, -1),
    V_1_10(210, -1),
    V_1_11(315, -1),
    /**
     * 1.11.1 or 1.11.2 as they have the same protocol version.
     */
    V_1_11_1(316, -1),
    V_1_12(335, -1),
    V_1_12_1(338, -1),
    V_1_12_2(340, -1),

    V_1_13(393, 4),
    V_1_13_1(401, 4),
    V_1_13_2(404, 4),

    V_1_14(477, 4),
    V_1_14_1(480, 4),
    V_1_14_2(485, 4),
    V_1_14_3(490, 4),
    V_1_14_4(498, 4),

    V_1_15(573, 5),
    V_1_15_1(575, 5),
    V_1_15_2(578, 5),

    V_1_16(735, 5),
    V_1_16_1(736, 5),
    V_1_16_2(751, 6),
    V_1_16_3(753, 6),
    /**
     * 1.16.4 or 1.16.5 as they have the same protocol version.
     */
    V_1_16_4(754, 6),

    V_1_17(755, 7),
    V_1_17_1(756, 7),

    /**
     * 1.18 or 1.18.1 as they have the same protocol version.
     */
    V_1_18(757, 8),
    V_1_18_2(758, 9),

    V_1_19(759, 10),
    V_1_19_1(760, 10),
    V_1_19_3(761, 10),
    V_1_19_4(762, 12),
    /**
     * 1.20 and 1.20.1 have the same protocol version.
     */
    V_1_20(763, 15),
    V_1_20_2(764, 18),
    /**
     * 1.20.3 and 1.20.4 have the same protocol version.
     */
    V_1_20_3(765, 26),
    V_1_20_4(765, 26),
    /**
     * 1.20.5 and 1.20.6 have the same protocol version.
     */
    V_1_20_5(766, 41),

    /**
     * 1.21 and 1.21.1 have the same protocol version.
     */
    V_1_21(767, 48),
    /**
     * 1.21.2 and 1.21.3 have the same protocol version.
     */
    V_1_21_2(768, 57),
    V_1_21_4(769, 61),
    V_1_21_5(770, 71),
    V_1_21_6(771, 75),
    V_1_21_7(772, 82),
    V_1_21_8(773, 87);

    private static final Map<Integer, Version> VERSION_MAP;
    private static final Version MAX;
    public static final Version CURRENT;

    static {
        Version[] values = values();

        VERSION_MAP = new HashMap<>();
        MAX = values[values.length - 1];

        Version last = null;
        for (Version version : values) {
            version.previous = last;
            last = version;
            VERSION_MAP.put(version.protocolVersion(), version);
        }

        String rawVersion = Bukkit.getMinecraftVersion();
        rawVersion = rawVersion.replace(".", "_");
        CURRENT = Version.valueOf("V_" + rawVersion);
    }

    private final int protocolVersion;
    private final int packFormat;
    private Version previous;

    public boolean more(Version another) {
        return this.protocolVersion > another.protocolVersion;
    }

    public boolean moreOrEqual(Version another) {
        return this.protocolVersion >= another.protocolVersion;
    }

    public boolean less(Version another) {
        return this.protocolVersion < another.protocolVersion;
    }

    public boolean lessOrEqual(Version another) {
        return this.protocolVersion <= another.protocolVersion;
    }

    public boolean fromTo(Version min, Version max) {
        return this.protocolVersion >= min.protocolVersion && this.protocolVersion <= max.protocolVersion;
    }

    public boolean isSupported() {
        return this != UNDEFINED;
    }

    public static Version getMin() {
        return V_1_7_10;
    }

    public static Version getMax() {
        return MAX;
    }

    public static Version of(int protocolVersion) {
        return VERSION_MAP.getOrDefault(protocolVersion, UNDEFINED);
    }

}