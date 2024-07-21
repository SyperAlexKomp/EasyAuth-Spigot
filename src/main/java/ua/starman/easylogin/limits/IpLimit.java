package ua.starman.easylogin.limits;

import ua.starman.easylogin.utils.Vars;

public class IpLimit {
    public static boolean working = Vars.plugin.getConfig().getBoolean("limits.ip.working");
}
