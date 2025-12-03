package hu.snowylol.networkjs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(NetworkJS.MODID)
public class NetworkJSForge {
    public NetworkJSForge() {
        NetworkJS.init();

        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStopping);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        NetworkJSCommand.register(event.getDispatcher());
    }

    private void onServerStarting(ServerStartingEvent event) {
        NetworkJS.LOGGER.info("Server starting - NetworkJS ready");
        NetworkJS.checkSingleplayerAndWarn();
    }

    private void onServerStopping(ServerStoppingEvent event) {
        NetworkJS.disableRegistry();
        NetworkJS.LOGGER.info("Server stopping - NetworkJS registry disabled");
    }
}
