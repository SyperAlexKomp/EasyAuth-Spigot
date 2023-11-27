package ua.starman.easylogin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import ua.starman.easylogin.EasyAuth;
import ua.starman.easylogin.auther.Auther;
import ua.starman.easylogin.auther.PlayerData;
import ua.starman.easylogin.utils.Vars;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class RegisterCommand implements CommandExecutor {
    static Plugin plugin = EasyAuth.getPlugin(EasyAuth.class);
    private final Vars vars = new Vars();

    private static String encodeToSHA256(String originalString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            List<MetadataValue> metadataList = ((Player) sender).getMetadata("need_register");
            if (metadataList.isEmpty()) {
                sender.sendMessage(vars.getPluginTab() + ChatColor.GREEN +
                        "You are already authed!");
                return true;
            }
            for (MetadataValue need_register : metadataList) {
                if (need_register.asBoolean()) {
                    if (args.length > 1) {
                        if (args[0].equals(args[1])) {
                            String password;

                            if (plugin.getConfig().getBoolean("encode")) {
                                password = encodeToSHA256(args[0]);
                            } else {
                                password = args[0];
                            }

                            PlayerData playerData = new PlayerData(((Player) sender).getUniqueId(),
                                    sender.getName(), password,
                                    Auther.getIPAddress(Objects.requireNonNull(((Player) sender).getPlayer())),
                                    LocalDateTime.now());
                            playerData.save(Auther.dataDir);

                            ((Player) sender).setMetadata("blocked",
                                    new FixedMetadataValue(plugin, false));
                            ((Player) sender).setMetadata("need_register",
                                    new FixedMetadataValue(plugin, false));

                            sender.sendMessage(vars.getPluginTab() +
                                    plugin.getConfig().getString("login_done_message"));
                        } else {
                            sender.sendMessage(vars.getPluginTab() + ChatColor.DARK_RED +
                                    "Passwords must be same!" + ChatColor.RESET);
                        }
                    } else {
                        sender.sendMessage(vars.getPluginTab() + ChatColor.DARK_RED +
                                "You need to confirm password!" + ChatColor.RESET);
                    }
                } else {
                    sender.sendMessage(ChatColor.GREEN + "You are already registered!" +
                            ChatColor.RESET);
                }
            }
        }
        return false;
    }
}
