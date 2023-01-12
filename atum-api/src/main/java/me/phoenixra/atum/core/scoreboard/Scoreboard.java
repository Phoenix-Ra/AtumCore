package me.phoenixra.atum.core.scoreboard;

import me.phoenixra.atum.core.placeholders.TextReplacer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Scoreboard {

    /**
     * update the scoreboard
     *
     */
    void update();

    /**
     * add player to the scoreboard
     *
     * @param player the player
     */
    void addPlayer(@NotNull Player player);

    /**
     * remove player from the scoreboard
     *
     * @param player the player
     */
    void removePlayer(@NotNull Player player);

    /**
     * remove all players from the scoreboard
     *
     */
    void removeAllPlayers();

    /**
     * true if the scoreboard displayed for the player
     *
     * @param player the player
     */
    boolean hasPlayer(@NotNull Player player);

    /**
     * Get the text replacer of the scoreboard
     *
     * @return The text replacer
     */
    @NotNull
    TextReplacer getReplacer();

    /**
     * Get the scoreboard's id
     *
     * @return The id
     */
    @NotNull
    String getId();
}
