package ua.starman.easylogin.limits;

import ua.starman.easylogin.utils.Vars;

public class TimeLimit {
    public static boolean working = Vars.plugin.getConfig().getBoolean("limits.time.working");
    public static int time = Vars.plugin.getConfig().getInt("limits.time.login_time");
}
