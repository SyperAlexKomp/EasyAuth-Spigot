package ua.starman.easylogin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ua.starman.easylogin.EasyAuth;
import ua.starman.easylogin.auther.Auther;
import ua.starman.easylogin.auther.PlayerData;
import ua.starman.easylogin.utils.Vars;

public class AuthCommand implements CommandExecutor {
    static Plugin plugin = EasyAuth.getPlugin(EasyAuth.class);
    private final Vars vars = new Vars();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            PlayerData playerData = PlayerData.get(((Player) sender).getUniqueId(), Auther.dataDir);

            sender.sendMessage(ChatColor.BLUE + "------------" + ChatColor.AQUA +
                    "\nUUID: " + ChatColor.RESET + playerData.uuid + ChatColor.AQUA +
                    "\nIP: " + ChatColor.RESET + playerData.ip.toString() + ChatColor.AQUA +
                    "\nLast login: " + ChatColor.RESET + playerData.lastLogin.toString() + ChatColor.BLUE +
                    "\n------------");
        }
        return false;
    }
}
