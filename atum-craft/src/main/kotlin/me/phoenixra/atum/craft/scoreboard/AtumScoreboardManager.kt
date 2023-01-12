package me.phoenixra.atum.craft.scoreboard

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.scoreboard.Scoreboard
import me.phoenixra.atum.core.scoreboard.ScoreboardManager
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class AtumScoreboardManager(
    private val plugin: AtumPlugin
): ScoreboardManager, BukkitRunnable() {

    private var boards = HashMap<String, Scoreboard>()

    init {
        plugin.addTaskAfterLoad{
            this.runTaskTimer(plugin,0,2)
        }
    }

    override fun run() {
        for (board in boards.values) {
            try {
                board.update()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun addPlayerToScoreboard(player: Player, id: String) {
        try {
            var alreadyAdded = false
            for (board in boards.values) {
                if (board.hasPlayer(player)) {
                    if (board.id == id) {
                        alreadyAdded = true
                        continue
                    }
                    board.removePlayer(player)
                }
            }
            if (!alreadyAdded) {
                boards[id]?.addPlayer(player)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun removePlayerFromScoreboards(player: Player) {
        for(board in boards.values){
            board.removePlayer(player)
        }
    }

    override fun addScoreboard(scoreboard: Scoreboard) {
        boards[scoreboard.id] = scoreboard
    }

    override fun removeScoreboard(id: String) {
        boards.remove(id)
    }

    override fun getScoreboard(id: String): Scoreboard? {
        return boards[id]
    }

    override fun getScoreboards(): MutableList<Scoreboard> {
        return boards.values.toMutableList()
    }

    override fun clearAll() {
        for(board in boards.values){
            board.removeAllPlayers()
        }
        boards.clear()
    }

    override fun getPlugin(): AtumPlugin {
        return plugin
    }

}