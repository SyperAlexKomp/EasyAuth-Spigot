package ua.starman.easylogin.auther;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import ua.starman.easylogin.EasyAuth;
import ua.starman.easylogin.utils.Vars;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Auther implements Listener {
    public static File dataDir = new File("plugins/EasyAuth/player-data");
    static Plugin plugin = EasyAuth.getPlugin(EasyAuth.class);

    Vars vars = new Vars();

    public File getDataDir() {
        return dataDir;
    }



    private void loginCancel(Player player, Event event) {
        List<MetadataValue> metadataBlockedList = player.getMetadata("blocked");
        for (MetadataValue metadataBlocked : metadataBlockedList) {
            if (metadataBlocked.asBoolean()) {
                if (event instanceof Cancellable) {
                    ((Cancellable) event).setCancelled(true);
                }
            }

            List<MetadataValue> metadataNeedRegisterList = player.getMetadata("need_register");
            if (metadataNeedRegisterList.isEmpty() && metadataBlocked.asBoolean()) {
                player.sendMessage(vars.getPluginTab() + ChatColor.DARK_RED + "Enter /login <password>");
            } else if (!metadataNeedRegisterList.isEmpty() && metadataBlocked.asBoolean()){
                player.sendMessage(vars.getPluginTab() + ChatColor.DARK_RED + "Enter " +
                        "/register <password> <password>");
            }

        }
    }

    public static InetAddress getIPAddress(Player player) {
        try {
            return InetAddress.getByName(Objects.requireNonNull(player.getAddress()).getHostString());
        } catch (UnknownHostException e) {

            return null;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (PlayerData.check(player.getUniqueId(), dataDir)) {
            PlayerData playerData = PlayerData.get(player.getUniqueId(), dataDir);

            if (!playerData.ip.equals(getIPAddress(player))) {
                plugin.getLogger().info("Player " + player.getName() + " blocked");
                player.setMetadata("blocked", new FixedMetadataValue(plugin, true));
                player.sendMessage(vars.getPluginTab() + plugin.getConfig().getString("different_ip_message"));
            } else if (Duration.between(playerData.lastLogin, LocalDateTime.now()).toMinutes() >=
                    plugin.getConfig().getInt("unlogin_time")) {
                player.setMetadata("blocked", new FixedMetadataValue(plugin, true));
                player.sendMessage(vars.getPluginTab() + plugin.getConfig().getString("unlogin_time_message"));
            }

        } else {
            player.setMetadata("blocked", new FixedMetadataValue(plugin, true));
            player.setMetadata("need_register", new FixedMetadataValue(plugin, true));
            player.sendMessage(vars.getPluginTab() + plugin.getConfig().getString("register_message"));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        loginCancel(player, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        loginCancel(player, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        loginCancel(player, event);
    }


}
