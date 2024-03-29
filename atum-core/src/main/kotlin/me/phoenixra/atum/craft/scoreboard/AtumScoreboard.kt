package me.phoenixra.atum.craft.scoreboard

import me.phoenixra.atum.core.placeholders.InjectablePlaceholder
import me.phoenixra.atum.core.placeholders.context.PlaceholderContext
import me.phoenixra.atum.core.utils.StringUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

class AtumScoreboard(
    private val id: String,
    private var displayName: MutableList<String>,
    private var scores: MutableList<String>
): me.phoenixra.atum.core.scoreboard.Scoreboard {
    var injections = mutableListOf<InjectablePlaceholder>()

    var update = 0
    var players = HashMap<Player, MutableList<String>>()
    var sb = HashMap<Player, Scoreboard>()
    override fun addInjectablePlaceholder(placeholders: MutableIterable<InjectablePlaceholder>) {
        injections.addAll(placeholders);
    }

    override fun removeInjectablePlaceholder(placeholders: MutableIterable<InjectablePlaceholder>) {
        injections.removeAll(placeholders);
    }

    override fun clearInjectedPlaceholders() {
        injections.clear();
    }

    override fun getPlaceholderInjections(): MutableList<InjectablePlaceholder> {
        return injections;
    }


    override fun update() {
        for (key in players.keys) {
            try {
                if (!key.isOnline) continue
                val objective = sb[key]!!.getObjective(id)
                ++update
                if (update >= displayName.size) {
                    update = 0
                }
                objective!!.displayName = displayName[update]
                for (i in scores.size downTo 1) {
                    val old = players[key]!![i - 1]
                    val s = StringBuilder(StringUtils.formatWithPlaceholders(
                        scores[scores.size - i],
                        PlaceholderContext(key,null,this, mutableListOf())
                    ))
                    if (s.toString() == old || old.isBlank()) continue
                    if (s.toString().trim().isEmpty()) {
                        s.append(" ".repeat(i))
                    }
                    sb[key]!!.resetScores(old)
                    players[key]!![i - 1] = s.toString()
                    val score = objective.getScore(s.toString())
                    score.score = i - 1
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun addPlayer(player: Player) {
        try {
            sb[player] = Bukkit.getScoreboardManager().newScoreboard
            players[player] = ArrayList()
            val objective = sb[player]!!.registerNewObjective(
                id, "dummy", StringUtils.format(
                    displayName[0]
                )
            )
            objective.displaySlot = DisplaySlot.SIDEBAR
            for (i in scores.size downTo 1) players[player]!!.add(
                StringUtils.formatWithPlaceholders(
                    scores[scores.size - i],
                    PlaceholderContext(player,null,this, mutableListOf())
                )
            )
            for (i in scores.size downTo 1) {
                val s = StringBuilder(StringUtils.formatWithPlaceholders(
                    scores[scores.size - i],
                    PlaceholderContext(player,null,this, mutableListOf())
                ))
                if (s.toString().trim().isEmpty()) {
                    s.append(" ".repeat(i))
                }
                objective.getScore(s.toString()).score = i - 1
                players[player]!![i - 1] = s.toString()
            }
            player.scoreboard = sb[player]!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun removePlayer(p: Player) {
        players.remove(p)
        sb.remove(p)
        if (p.isOnline) {
            p.scoreboard = Bukkit.getScoreboardManager().newScoreboard
        }
    }

    override fun removeAllPlayers() {
        for (player in players.keys) {
            if (player.isOnline) {
                player.scoreboard = Bukkit.getScoreboardManager().newScoreboard
            }
        }
        players.clear()
        sb.clear()
    }


    override fun hasPlayer(player: Player): Boolean {
        return player.scoreboard === sb[player]
    }

    override fun getId(): String {
        return id
    }
}