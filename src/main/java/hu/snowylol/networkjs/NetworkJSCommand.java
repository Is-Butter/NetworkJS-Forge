package hu.snowylol.networkjs;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class NetworkJSCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("networkjs")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("status")
                .executes(context -> {
                    String loggingState = NetworkJS.loggingEnabled() ? "enabled" : "disabled";
                    Component message = Component.literal(ChatFormatting.GREEN + "[NetworkJS] Network access is enabled. Logging is " + loggingState + ".");
                    context.getSource().sendSuccess(() -> message, false);
                    return 1;
                }))
            .then(Commands.literal("logging")
                .then(Commands.literal("enable")
                    .executes(context -> setLogging(context.getSource(), true)))
                .then(Commands.literal("disable")
                    .executes(context -> setLogging(context.getSource(), false))))
        );
    }

    private static int setLogging(CommandSourceStack source, boolean enable) {
        NetworkJSConfig.ENABLE_LOGGING.set(enable);
        NetworkJSConfig.save();

        String status = enable ? "enabled" : "disabled";
        Component feedback = Component.literal(ChatFormatting.YELLOW + "[NetworkJS] Logging " + status + ".");
        source.sendSuccess(() -> feedback, true);
        return 1;
    }
}
