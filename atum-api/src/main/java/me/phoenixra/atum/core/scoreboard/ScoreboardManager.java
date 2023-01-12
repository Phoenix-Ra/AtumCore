package me.phoenixra.atum.core.scoreboard;


import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.exceptions.AtumException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ScoreboardManager {
    /**
     * Adds the player to the scoreboard
     * and removes him from all other scoreboards
     *
     * @param player The player
     * @param id The id
     * @throws AtumException if scoreboard not found
     */
    void addPlayerToScoreboard(@NotNull Player player, @NotNull String id) throws AtumException;

    /**
     * remove player from all scoreboards of this manager
     *
     * @param player The player
     */
    void removePlayerFromScoreboards(@NotNull Player player);

    /**
     * add the scoreboard to this manager
     *
     * @param scoreboard the board
     */
    void addScoreboard(@NotNull Scoreboard scoreboard);

    /**
     * remove the scoreboard from this manager
     *
     * @param id the id
     */
    void removeScoreboard(@NotNull String id);

    /**
     * Get the scoreboard added to the manager
     *
     * @param id The id
     * @return The scoreboard or null if not found
     */
    @Nullable
    Scoreboard getScoreboard(@NotNull String id);

    /**
     * Get added scoreboards
     *
     * @return The list
     */
    @NotNull
    List<Scoreboard> getScoreboards();

    /**
     * clear all added scoreboards
     *
     */
    void clearAll();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
