package hu.snowylol.networkjs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NetworkJS.MODID)
public class NetworkJSForge {
    public NetworkJSForge() {
        NetworkJSConfig.register();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(NetworkJSConfig::onLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(NetworkJSConfig::onReload);

        NetworkJS.init();

        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void onServerStarting(ServerStartingEvent event) {
        NetworkJS.logInfo("Server starting - NetworkJS ready");
    }

    private void registerCommands(RegisterCommandsEvent event) {
        NetworkJSCommand.register(event.getDispatcher());
    }
}
