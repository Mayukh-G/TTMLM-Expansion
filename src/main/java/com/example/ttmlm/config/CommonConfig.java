package com.example.ttmlm.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public static ForgeConfigSpec.BooleanValue oreSwitch;
    public static ForgeConfigSpec.BooleanValue structSwitch;
    public static ForgeConfigSpec.BooleanValue mobSwitch;

    CommonConfig(ForgeConfigSpec.Builder server){
        server.push("OreConfig");
        server.comment("Turns the mod ore generation on or off");
        oreSwitch = server.define("oreswitch", true);
        server.pop();

        server.push("MobConfig");
        server.comment("Decides whether to replace vanilla mobs with mod variants");
        mobSwitch = server.define("mobswitch", true);
        server.pop();

        server.push("StructureConfig");
        server.comment("Turns the generation of mod structures on or off");
        structSwitch = server.define("structswitch", true);
        server.pop();
    }
}
