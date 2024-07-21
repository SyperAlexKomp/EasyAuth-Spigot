package ua.starman.easylogin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ua.starman.easylogin.EasyAuth;
import ua.starman.easylogin.auther.PlayerData;
import ua.starman.easylogin.utils.Utils;
import ua.starman.easylogin.utils.Vars;

public class AuthCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PlayerData playerData = null;

        if (args.length == 0 && (sender instanceof Player)) {
            playerData = PlayerData.get(((Player) sender).getUniqueId());
        } else if (args.length > 0 && sender.isOp()) {
            Player player = sender.getServer().getPlayer(args[0]);
            if (player != null) {
                playerData = PlayerData.get(player.getUniqueId());
            } else {
                sender.sendMessage(Utils.parseMessage("This player is not registered"));
                return false;
            }
        } else {
            // TODO
            return false;
        }

        assert playerData != null;

        sender.sendMessage(
                ChatColor.BLUE + "------------",
                ChatColor.AQUA + "UUID: " + ChatColor.RESET + playerData.uuid,
                ChatColor.AQUA + "Nickname: " + ChatColor.RESET + playerData.name,
                ChatColor.AQUA + "IP: " + ChatColor.RESET + playerData.ip.toString(),
                ChatColor.AQUA + "Last log in time: " + ChatColor.RESET + playerData.lastLogin.toString(),
                ChatColor.BLUE + "------------"
        );
        return false;
    }
}
