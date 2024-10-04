package me.floppa.jokesmenu;

import me.floppa.jokesmenu.Commands.JokesMenuCommand;
import me.floppa.jokesmenu.Events.JokesEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public final class Jokesmenu extends JavaPlugin {
    String currentVersion = "0.7.0-beta";

    public String checkForNewVersion(Boolean getDownloadFile) throws IOException, ParseException {
        if(!getDownloadFile) {
            try {
                // URL для получения JSON-данных
                final String response = getString();

                JSONObject jsonResponse = null;

                // Парсинг JSON-ответа
                try {
                    jsonResponse = (JSONObject) JSONValue.parseWithException(response);
                    if (jsonResponse == null) {
                        System.out.println("Failed to parse JSON response.");
                        return currentVersion; // Возвращаем текущую версию, если не удалось распарсить
                    }
                } catch (ParseException e) {
                    getLogger().severe("Failed to parse: " + e.getMessage());
                }

                // Получение версии из объекта "info"
                assert jsonResponse != null;
                String version = (String) jsonResponse.get("version");
                if (version == null || version.isEmpty()) {
                    this.getLogger().warning("No version found, or Feed URL is bad.");
                    return currentVersion;
                } else {
                    // Преобразуем строку версии в число
                    return version;
                }
            } catch (Exception e) {
                getLogger().severe("Failed" + e);
                return currentVersion; // Вернуть текущую версию в случае ошибки
            }
        } else {
            JSONObject jsonresponse = (JSONObject) JSONValue.parseWithException(getString());
            return (String) jsonresponse.get("downloadfile");
        }
    }

    private static @NotNull String getString() throws IOException {
        URL url = new URL("https://user12233.github.io/versionMenu/info.json");
        URLConnection conn = url.openConnection();
        conn.setReadTimeout(5000);
        conn.addRequestProperty("User-Agent", "Menu Update Checker");
        conn.addRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Чтение ответа
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String line = reader.readLine();
        responseBuilder.append(line);
        reader.close();

        // Получаем полный ответ как строку
        return responseBuilder.toString();
    }

    private String getNewVersion() {
        try {
            return checkForNewVersion(false);
        } catch (IOException | ParseException e) {
            getLogger().severe("Failed to get new version.");
        }
        return currentVersion;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("jokesmenu")).setExecutor(new JokesMenuCommand());
        Bukkit.getPluginManager().registerEvents(new JokesEvents(), this);
        getLogger().info("JokesMenu has been enabled");
        String getNewVersion = getNewVersion();
        if(Objects.equals(getNewVersion, currentVersion)) {
            getLogger().info("Current version " + currentVersion);
        } else {
            try {
                getLogger().info("New version available: " + getNewVersion + " at " + checkForNewVersion(true));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("JokesMenu has been disabled");
    }
}
