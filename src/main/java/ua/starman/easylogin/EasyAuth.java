package ua.starman.easylogin;

import org.bukkit.plugin.java.JavaPlugin;
import ua.starman.easylogin.auther.AuthWorker;
import ua.starman.easylogin.commands.AuthCommand;
import ua.starman.easylogin.commands.LoginCommand;
import ua.starman.easylogin.commands.RegisterCommand;
import ua.starman.easylogin.events.AuthEvents;
import ua.starman.easylogin.utils.UpdateChecker;
import ua.starman.easylogin.utils.Utils;
import ua.starman.easylogin.utils.Vars;

import java.util.Objects;

public final class EasyAuth extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        Utils.checkDirs(Vars.dataDir);

        new UpdateChecker(this, 113725).checkUpdate();

        registerCommands();
        registerEvents();
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("register")).setExecutor(new RegisterCommand());
        Objects.requireNonNull(getCommand("login")).setExecutor(new LoginCommand());
        Objects.requireNonNull(getCommand("auth")).setExecutor(new AuthCommand());

    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new AuthEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
