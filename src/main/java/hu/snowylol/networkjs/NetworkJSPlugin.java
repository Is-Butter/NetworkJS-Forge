package hu.snowylol.networkjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import hu.snowylol.networkjs.FetchBinding.FetchOptions;

import java.util.Map;

public class NetworkJSPlugin extends KubeJSPlugin {

    @Override
    public void registerBindings(BindingsEvent bindings) {
        NetworkJS.logInfo("Registering NetworkJS bindings...");

        bindings.add("fetch", new FetchFunction(false));
        bindings.add("fetchAsync", new FetchFunction(true));

        bindings.add("FetchBinding", FetchBinding.class);
        bindings.add("FetchOptions", FetchOptions.class);
        bindings.add("FetchResponse", FetchBinding.FetchResponse.class);
        bindings.add("Server", ServerBinding.class);

        NetworkJS.logInfo("NetworkJS bindings registered successfully");
    }

    /**
     * Normalizes script-provided fetch arguments into safe Java types before delegating to {@link FetchBinding}.
     * <p>
     * Rhino often wraps string literals in {@code ConsString}, which can trigger {@link ClassCastException}s when
     * passed directly to OkHttp. To avoid that, this method stringifies the URL, HTTP method, header keys/values,
     * and leaves the body as a raw object so {@link FetchOptions} can convert it using {@link Object#toString()} when
     * constructing the request body.
     */
    private static Object handleFetch(String url, Object options, boolean async) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL is required for fetch calls");
        }

        if (options instanceof Map<?, ?> opts) {
            @SuppressWarnings("unchecked")
            Map<String, Object> optMap = (Map<String, Object>) opts;

            String method = String.valueOf(optMap.getOrDefault("method", "GET"));

            Map<String, String> headers = Map.of();
            Object rawHeaders = optMap.get("headers");
            if (rawHeaders instanceof Map<?, ?> headerMap) {
                java.util.HashMap<String, String> normalized = new java.util.HashMap<>();
                for (Map.Entry<?, ?> entry : headerMap.entrySet()) {
                    normalized.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
                headers = normalized;
            }

            Object rawBody = optMap.getOrDefault("body", null);

            FetchOptions javaOptions = new FetchOptions(method, headers, rawBody);
            return async ? FetchBinding.fetchAsync(url, javaOptions) : FetchBinding.fetch(url, javaOptions);
        }

        return async ? FetchBinding.fetchAsync(url) : FetchBinding.fetch(url);
    }

    private static class FetchFunction extends BaseFunction {
        private final boolean async;

        FetchFunction(boolean async) {
            this.async = async;
        }

        @Override
        public Object call(Context context, Scriptable scope, Scriptable thisObj, Object[] args) {
            String url = args.length > 0 ? String.valueOf(args[0]) : null;
            Object options = args.length > 1 ? args[1] : null;
            return handleFetch(url, options, async);
        }
    }
}
