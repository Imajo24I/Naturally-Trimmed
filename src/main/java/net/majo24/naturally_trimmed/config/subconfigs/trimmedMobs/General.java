package net.majo24.naturally_trimmed.config.subconfigs.trimmedMobs;

import net.majo24.naturally_trimmed.config.Config;

public class General {
    public TrimSystems enabledSystem = TrimSystems.RANDOM_TRIMS;
    public int noTrimsChance = 25;

    public enum TrimSystems {
        RANDOM_TRIMS,
        CUSTOM_TRIM_COMBINATIONS,
        NONE,
    }
}
