package hu.snowylol.networkjs;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class NetworkJS {
    public static final String MODID = "networkjs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        logInfo("NetworkJS initialized - network access enabled");
    }

    public static boolean loggingEnabled() {
        return NetworkJSConfig.ENABLE_LOGGING.get();
    }

    public static void logInfo(String message, Object... args) {
        if (loggingEnabled()) {
            LOGGER.info(message, args);
        }
    }

    public static void logWarn(String message, Object... args) {
        if (loggingEnabled()) {
            LOGGER.warn(message, args);
        }
    }
}
