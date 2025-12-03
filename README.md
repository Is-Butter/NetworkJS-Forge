# NetworkJS

A powerful KubeJS addon that enables internet connectivity for your Minecraft server scripts.

> **Note**: This is my first Java mod! I'm still learning, so feedback and contributions are very welcome. :D

## Features

- **HTTP/FETCH Support** - Make HTTP(S) requests from your KubeJS scripts with synchronous or async calls
- **Server Utilities** - Send colored messages to players and gather server info without extra addons
- **Easy Integration** - Works seamlessly with KubeJS using both global functions and class access
- **Always-On Access** - Network helpers are available immediately without commands, toggles, or registry steps
- **Configurable Logging** - Verbose logging can be toggled in config or via the `/networkjs logging` command

## Installation

1. Download the latest jar from the [releases page](https://github.com/SSnowly/NetworkJS/releases)
2. Place the jar in your `mods` folder
3. Make sure you have [KubeJS](https://github.com/KubeJS-Mods/KubeJS) installed
4. Restart your server

## API Reference

NetworkJS exposes the same helpers as both global functions and legacy classes, so you can choose the style that best fits your KubeJS scripts.

### Global Functions

```javascript
// Quick GET
const response = fetch('https://api.example.com/data');

// POST/PUT/PATCH/DELETE with options
const created = fetch('https://api.example.com/data', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer token' },
    body: JSON.stringify({ key: 'value' })
});

// Async variant returns a CompletableFuture<FetchResponse>
fetchAsync('https://api.example.com/data', {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ enabled: true })
}).then(response => {
    console.log('Status', response.getStatus());
});
```

#### Fetch options (object)

- `method` – HTTP verb (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`, or any valid method). Defaults to `GET`.
- `headers` – Map/object of header key/value pairs. `Content-Type` is auto-detected from the header when present (defaults to `application/json`).
- `body` – Any value that can be stringified; used as the request body when the method accepts one.

> **Important**: All fetch fields are coerced to Java strings before building the HTTP request to avoid Rhino `ConsString` casting issues. Non-string methods, header keys/values, and bodies are passed through `String.valueOf()`/`toString()`, so ensure your objects implement a meaningful string representation if you rely on custom types.

#### Fetch response helpers

- `getStatus()` / `status` – Numeric HTTP status code.
- `getStatusText()` – Status text message.
- `isOk()` – `true` when the request returned a 2xx status.
- `getHeaders()` – Map of response headers.
- `getText()` / `text()` – Response body as a string.
- `json()` – Parse the body into a JSON element (throws if invalid JSON).
- `json(Class<T>)` – Parse directly into a typed object.

### Server utilities (global class `Server`)

```javascript
// Broadcast a colored message to everyone
Server.sendRawMessage('&aWelcome to the server!');

// Direct message a single player by name
Server.sendRawMessageToPlayer('PlayerName', '&bHello there!');

// Query server state
const playerCount = Server.getPlayerCount();
const playerNames = Server.getPlayerNames(); // returns string[]
```

### Legacy classes (still available)

```javascript
// Class-based synchronous fetch
const response = FetchBinding.fetch('https://api.example.com');
console.log('Status:', response.getStatus());
console.log('Body:', response.text());

// Explicit options with typed helper
const options = new FetchOptions('POST', { 'Content-Type': 'application/json' }, JSON.stringify({ data: 'value' }));
const created = FetchBinding.fetch('https://api.example.com', options);

// Async using CompletableFuture
const future = FetchBinding.fetchAsync('https://api.example.com');
future.thenApply(resp => resp.json());
```

### Complete API Exports

The following classes and functions are available globally in your KubeJS scripts:

| Export | Type | Description |
|--------|------|-------------|
| `fetch()` | Function | Perform synchronous HTTP/HTTPS requests |
| `fetchAsync()` | Function | Perform asynchronous HTTP/HTTPS requests |
| `Server` | Class | Broadcast messages and query server state |
| `FetchBinding` | Class | Legacy fetch wrapper (sync/async) |
| `FetchOptions` | Class | Options bag for HTTP method/headers/body |
| `FetchResponse` | Class | Response wrapper with text/json helpers |

## Configuration & Commands

- **Logging toggle**: `config/networkjs-common.toml` contains `enableLogging` (default `true`) to turn verbose logs on or off.
- **Status command**: `/networkjs status` (op level 2) reports network availability and current logging mode.
- **Runtime logging toggle**: `/networkjs logging enable|disable` flips `enableLogging` without restarting and saves it back to the config file.

## Example Usage

### Server Status API
```javascript
// Create a simple API endpoint using fetch
ServerEvents.loaded(event => {
    const statusData = {
        online: true,
        players: Server.getPlayerCount(),
        playerList: Server.getPlayerNames()
    };
    
    // Post to your status API
    fetchAsync('https://your-api.com/server-status', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(statusData)
    });
});
```

## Requirements

- Minecraft 1.20.1
- Forge 47.3.0+
- KubeJS 2001.6.5+

## Building

```bash
git clone https://github.com/SSnowly/NetworkJS.git
cd NetworkJS
./gradlew build
```

The built jar will be in `build/libs/networkjs-1.20.1-{version}.jar`

## Releasing

Automated releases are published through GitHub Actions whenever a version tag that begins with `r` is pushed (for example, `r1.0.0`). Follow these steps to cut a new release:

1. Update `gradle.properties` with the new `mod_version` value if it changed.
2. Build locally with `./gradlew build` and verify the artifact in `build/libs`.
3. Create a tag that starts with `r` and push it to GitHub:

   ```bash
   git tag r1.0.0
   git push origin r1.0.0
   ```

4. The workflow `.github/workflows/release.yml` will run automatically to:
   - Build the project on GitHub Actions.
   - Generate the changelog from commits since the previous tag.
   - Publish a GitHub Release and upload the built JAR from `build/libs`.

The Release page will contain the changelog and the compiled mod ready for download.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

As a new Java developer, I'm always looking to improve! Feel free to:

- Open issues for bugs or feature requests
- Submit pull requests with improvements
- Share feedback on the code structure
- Help with documentation

## Support

- Open an [issue](https://github.com/SSnowly/NetworkJS/issues) for bugs
- Check the [KubeJS Wiki](https://kubejs.com/) for general KubeJS help

---

Made with <3 by ME!
