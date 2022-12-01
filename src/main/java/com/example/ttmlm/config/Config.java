package com.example.ttmlm.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.example.ttmlm.TTMLM;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public class Config {
    public static final ForgeConfigSpec commonSpec;
    public static final CommonConfig COMMON;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static void load(ForgeConfigSpec spec, String path){
        TTMLM.LOGGER.info("Loading config file: " + path);
        final CommentedFileConfig configFile = CommentedFileConfig.builder(new File(path))
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        TTMLM.LOGGER.info("Built config file:" + path);

        configFile.load();
        TTMLM.LOGGER.info("Loaded config file:" + path);

    }
}
