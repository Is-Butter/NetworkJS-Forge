package hu.snowylol.networkjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import hu.snowylol.networkjs.FetchBinding.FetchOptions;
import dev.latvian.mods.rhino.Callable;

import java.util.Map;

public class NetworkJSPlugin extends KubeJSPlugin {

    @Override
    public void registerBindings(BindingsEvent bindings) {
        NetworkJS.logInfo("Registering NetworkJS bindings...");

        bindings.add("fetch", (Callable) (context, scope, thisObj, args) -> {
            String url = args.length > 0 ? String.valueOf(args[0]) : null;
            Object options = args.length > 1 ? args[1] : null;
            return handleFetch(url, options, false);
        });

        bindings.add("fetchAsync", (Callable) (context, scope, thisObj, args) -> {
            String url = args.length > 0 ? String.valueOf(args[0]) : null;
            Object options = args.length > 1 ? args[1] : null;
            return handleFetch(url, options, true);
        });

        bindings.add("FetchBinding", FetchBinding.class);
        bindings.add("FetchOptions", FetchOptions.class);
        bindings.add("FetchResponse", FetchBinding.FetchResponse.class);
        bindings.add("Server", ServerBinding.class);

        NetworkJS.logInfo("NetworkJS bindings registered successfully");
    }

    private Object handleFetch(String url, Object options, boolean async) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL is required for fetch calls");
        }

        if (options instanceof Map<?, ?> opts) {
            @SuppressWarnings("unchecked")
            Map<String, Object> optMap = (Map<String, Object>) opts;
            FetchOptions javaOptions = new FetchOptions(
                (String) optMap.getOrDefault("method", "GET"),
                (Map<String, String>) optMap.getOrDefault("headers", Map.of()),
                optMap.getOrDefault("body", null)
            );
            return async ? FetchBinding.fetchAsync(url, javaOptions) : FetchBinding.fetch(url, javaOptions);
        }

        return async ? FetchBinding.fetchAsync(url) : FetchBinding.fetch(url);
    }
}
