package ua.starman.easylogin.utils;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import ua.starman.easylogin.EasyAuth;

import java.io.File;
import java.util.Objects;

public class Vars {
    public static Plugin plugin = EasyAuth.getPlugin(EasyAuth.class);
    public static String pluginTab = plugin.getConfig().getString("plugin.tab");
    public static File dataDir = new File(Objects.requireNonNull(plugin.getConfig().getString("data.dir")));
    public static String encode = plugin.getConfig().getString("data.method");
    public static Integer login_time = plugin.getConfig().getInt("limits.login_time");
}
