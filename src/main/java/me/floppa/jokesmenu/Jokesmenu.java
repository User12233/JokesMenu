package me.floppa.jokesmenu;

import me.floppa.jokesmenu.Commands.JokesMenuCommand;
import me.floppa.jokesmenu.Events.JokesEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public final class Jokesmenu extends JavaPlugin {
    String currentVersion = "1.0 BETA";

    public String checkForNewVersion() {
        try {
            // URL для получения JSON-данных
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
            String response = responseBuilder.toString();

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
    }


    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("jokesmenu")).setExecutor(new JokesMenuCommand());
        Bukkit.getPluginManager().registerEvents(new JokesEvents(), this);
        getLogger().info("JokesMenu has been enabled");
        String getNewVersion = checkForNewVersion();
        if(Objects.equals(getNewVersion, currentVersion)) {
            getLogger().info("Current version " + currentVersion);
        } else {
            getLogger().info("New version available: " + getNewVersion);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("JokesMenu has been disabled");
    }
}
