package me.floppa.jokesmenu;

import me.floppa.jokesmenu.Commands.JokesMenuCommand;
import me.floppa.jokesmenu.Events.JokesEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
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
    String currentVersion = "0.7.1-beta";

    public String checkForNewVersion(Boolean getDownloadFile) throws IOException, ParseException {
        if(!getDownloadFile) {
            try {
                JSONArray array = getArrayWebSite(); // Для простоты

                if (array == null || array.isEmpty()) {
                    this.getLogger().warning("No version found, or Feed URL is bad.");
                    getLogger().info(array.toJSONString()); // Выводим для дебаггинга
                    return currentVersion; // Возвращаем т.к у нас инвалидный array
                } else {
                    return ((String)((JSONObject)array.get(array.size() - 1)).get("version")); // Вычесть из array 1 и получить последнюю version
                }
            } catch (Exception e) {
                getLogger().severe("Failed " + e); // Вывод для дебаггинга
                return currentVersion; // Вернуть текущую версию в случае ошибки
            }
        } else {
            // URL для получения JSON-данных
            JSONArray array = getArrayWebSite(); // Вводим в переменную array функцию т.к у нас может отличаться size и мы не вычтем 1
            return ((String)((JSONObject)array.get(array.size() - 1)).get("downloadfile")); // Просто выводим из последнего downloadfile
        }
    }

    public static JSONArray getArrayWebSite() throws IOException {
        URL url = new URL("https://user12233.github.io/versionMenu/info.json");
        URLConnection conn = url.openConnection();
        conn.setReadTimeout(5000);
        conn.addRequestProperty("User-Agent", "Menu Update Checker");
        conn.addRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Чтение ответа
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = reader.readLine();
        JSONArray array = (JSONArray)JSONValue.parse(line);
        reader.close();
        return array;
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
