package me.phoenixra.atum.core.nametag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;

public class NameTagManager {

    private static HashMap<Player,String> prefixes = new HashMap<>();

    public static void setPrefix(@NotNull Player target, @NotNull String prefix){
        for(Player p : Bukkit.getOnlinePlayers()) {
            Team team = p.getScoreboard().getTeam("nameTag-"+target.getName());
            if (team == null) {
                team = target.getScoreboard().registerNewTeam("nameTag-"+target.getName());
            }
            team.setPrefix(prefix);
            team.addEntry(target.getName());
        }
    }
    public static void setPrefix(@NotNull Player target, @NotNull Player source, @NotNull String prefix){
        Team team = source.getScoreboard().getTeam("nameTag-"+target.getName());
        if (team == null) {
            team = target.getScoreboard().registerNewTeam("nameTag-"+target.getName());
        }
        team.setPrefix(prefix);
        team.addEntry(target.getName());
    }

    public static void setColor(@NotNull Player target, @NotNull Player source, char color){
        Team team = source.getScoreboard().getTeam("nameTag-"+target.getName());
        if (team == null) {
            team = target.getScoreboard().registerNewTeam("nameTag-"+target.getName());
        }
        team.setColor(ChatColor.getByChar(color));
        team.addEntry(target.getName());
    }
}
