package ua.starman.easylogin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import ua.starman.easylogin.auther.AuthWorker;
import ua.starman.easylogin.auther.PlayerData;
import ua.starman.easylogin.utils.Utils;
import ua.starman.easylogin.utils.Vars;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class RegisterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            List<MetadataValue> metadataList = ((Player) sender).getMetadata("auth_register");
            if (metadataList.isEmpty()) {
                sender.sendMessage(Utils.parseMessage(ChatColor.GREEN + "You are already authed!"));
                return true;
            }
            for (MetadataValue need_register : metadataList) {
                if (need_register.asBoolean()) {
                    if (args.length > 1) {
                        if (args[0].equals(args[1])) {
                            String password;

                            if (Objects.equals(Vars.encode, "SHA256")) {
                                password = Utils.encodeToSHA256(args[0]);
                            } else {
                                password = args[0];
                            }

                            PlayerData playerData = new PlayerData(((Player) sender).getUniqueId(),
                                    sender.getName(), password,
                                    AuthWorker.getIPAddress(Objects.requireNonNull(((Player) sender).getPlayer())),
                                    LocalDateTime.now());
                            playerData.save();

                            ((Player) sender).setMetadata("auth_block",
                                    new FixedMetadataValue(Vars.plugin, false));
                            ((Player) sender).setMetadata("auth_register",
                                    new FixedMetadataValue(Vars.plugin, false));

                            sender.sendMessage(Utils.parseMessage(ChatColor.GREEN + "Have a nice game!"));
                        } else {
                            sender.sendMessage(Utils.parseMessage(ChatColor.RED + "Passwords must be same!" + ChatColor.RESET));
                        }
                    } else {
                        sender.sendMessage(Utils.parseMessage(ChatColor.RED + "You need to confirm password!" + ChatColor.RESET));
                    }
                } else {
                    sender.sendMessage(Utils.parseMessage(ChatColor.GREEN + "You are already registered!" + ChatColor.RESET));
                }
            }
        }
        return false;
    }
}
