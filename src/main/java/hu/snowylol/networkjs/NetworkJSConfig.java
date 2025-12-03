package hu.snowylol.networkjs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class NetworkJSConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue ENABLE_LOGGING;

    private static ModConfig activeConfig;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("general");
        ENABLE_LOGGING = builder
            .comment("Enable informational NetworkJS logging (errors always log)")
            .define("enableLogging", true);
        builder.pop();

        SPEC = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(Type.COMMON, SPEC);
    }

    public static void onLoad(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            activeConfig = event.getConfig();
        }
    }

    public static void onReload(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            activeConfig = event.getConfig();
        }
    }

    public static void save() {
        if (activeConfig != null) {
            activeConfig.save();
        }
    }
}
