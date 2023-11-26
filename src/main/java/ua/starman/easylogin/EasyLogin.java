package ua.starman.easylogin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;
import ua.starman.easylogin.auther.Auther;
import ua.starman.easylogin.commands.LoginCommand;
import ua.starman.easylogin.commands.RegisterCommand;
import ua.starman.easylogin.utils.UpdateChecker;

import java.util.Collections;
import java.util.Objects;

public final class EasyLogin extends JavaPlugin {

    FileConfiguration config = getConfig();

    private void registerCommands() {
        Objects.requireNonNull(getCommand("register")).setExecutor(new RegisterCommand());
        Objects.requireNonNull(getCommand("login")).setExecutor(new LoginCommand());

    }



    @Override
    public void onEnable() {
        // Plugin startup logic

        config.addDefault("encode", false);
        config.setComments("encode",
                Collections.singletonList("Set to true if you want to store passwords in encoded format." +
                        " When false passwords will be saved as same as they were typed by player"));

        config.addDefault("register_message",
                ChatColor.RED +
                "Hello! Before joining to our server you need to register!" +
                " Use /register <password> <password>" + ChatColor.RESET);
        config.setComments("register_message",
                Collections.singletonList("Message that will show when player is not registered"));

        config.addDefault("different_ip_message",
                ChatColor.RED +
                        "We saw, that you joined from different IP address." +
                        " Use /login <password>" + ChatColor.RESET);
        config.setComments("different_ip_message",
                Collections.singletonList("Message that will show when player join from different IP address"));

        config.addDefault("unlogin_time",
                15);
        config.setComments("unlogin_time",
                Collections.singletonList("Player login expire time in minutes. DEFAULT: 15"));

        config.addDefault("unlogin_time_message",
                ChatColor.RED +
                        "Use /login <password>" + ChatColor.RESET);
        config.setComments("unlogin_time_message",
                Collections.singletonList("Message that will show when player join time expired"));

        config.addDefault("login_done_message",
                ChatColor.RED +
                        "Have a nice game!" + ChatColor.RESET);
        config.setComments("login_done_message",
                Collections.singletonList("Message that will show when player join after login"));
        saveConfig();

        Auther auther = new Auther();

        if (!auther.getDataDir().exists()) {
            if (auther.getDataDir().mkdirs()) {
                this.getLogger().info("Successfully created player-data folder");
            }
        }

        new UpdateChecker(this, 113725).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("Plugin is up to date!");
            } else {
                getLogger().info("There is a new update available " +
                        this.getDescription().getVersion() +
                        " -> " + version);
                getLogger().info("Check https://www.spigotmc.org/resources/easylogin.113725/");
            }
        });



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
