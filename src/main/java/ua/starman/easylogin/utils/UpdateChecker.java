package ua.starman.easylogin.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
public class UpdateChecker {

    private final JavaPlugin plugin;
    private final int resourceId;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~").openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }

    public void checkUpdate() {
        this.getVersion(version -> {
            if (Float.parseFloat(this.plugin.getDescription().getVersion()) < Float.parseFloat(version)) {
                this.plugin.getLogger().info("Plugin is up to date!");
            } else {
                this.plugin.getLogger().info("There is a new update available " +
                        this.plugin.getDescription().getVersion() +
                        " -> " + version);
                this.plugin.getLogger().info("Check https://www.spigotmc.org/resources/easylogin.113725/");
            }
        });
    }
}