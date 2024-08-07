package net.majo24.naturally_trimmed.config.subconfigs;

import net.majo24.naturally_trimmed.config.subconfigs.trimmedMobs.CustomTrimCombinations;
import net.majo24.naturally_trimmed.config.subconfigs.trimmedMobs.General;
import net.majo24.naturally_trimmed.config.subconfigs.trimmedMobs.RandomTrims;

public class TrimmedMobs {
    public General general = new General();
    public RandomTrims randomTrims = new RandomTrims();
    public CustomTrimCombinations trimCombinations = new CustomTrimCombinations();
}
