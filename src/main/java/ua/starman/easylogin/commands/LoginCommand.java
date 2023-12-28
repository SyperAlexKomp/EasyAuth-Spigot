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

public class LoginCommand implements CommandExecutor {
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

    private static boolean checkPassword(String password, PlayerData playerData) {
        String true_pass;

        if (plugin.getConfig().getBoolean("encode")) {
            true_pass = encodeToSHA256(password);
        } else {
            true_pass = password;
        }

        return playerData.password.equals(true_pass);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            List<MetadataValue> metadataBlockedList = ((Player) sender).getMetadata("blocked");
            if (metadataBlockedList.isEmpty()) {
                sender.sendMessage(vars.getPluginTab() + ChatColor.GREEN +
                        "You are already authed!");
                return true;
            }
            for (MetadataValue metadataBlocked : metadataBlockedList) {
                if (metadataBlocked.asBoolean()) {
                    List<MetadataValue> metadataNeedRegisterList = ((Player) sender)
                            .getMetadata("need_register");
                    for (MetadataValue metadataNeedRegister : metadataNeedRegisterList) {
                        if (metadataNeedRegister.asBoolean()) {
                            sender.sendMessage(vars.getPluginTab() + ChatColor.DARK_RED +
                                    "You need to register!");
                            return false;
                        }
                    }

                    if (args.length > 0) {
                        PlayerData playerData = PlayerData.get(((Player) sender).getUniqueId(), Auther.dataDir);

                        if (checkPassword(args[0], playerData)) {
                            ((Player) sender).setMetadata("blocked",
                                    new FixedMetadataValue(plugin, false));

                            PlayerData playerDataNew = new PlayerData(((Player) sender).getUniqueId(),
                                    sender.getName(), args[0],
                                    Auther.getIPAddress(Objects.requireNonNull(((Player) sender).getPlayer())),
                                    LocalDateTime.now());

                            playerDataNew.save(Auther.dataDir);

                            sender.sendMessage(vars.getPluginTab() +
                                    plugin.getConfig().getString("login_done_message"));
                        }

                    } else  {
                        sender.sendMessage(vars.getPluginTab() + ChatColor.DARK_RED +
                                "Password not passed!");
                    }
                } else {
                    sender.sendMessage(vars.getPluginTab() + ChatColor.GREEN +
                            "You are already authed!");
                }
            }

        }
        return false;
    }
}
