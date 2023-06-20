package me.phoenixra.atum.craft.scoreboard

import me.phoenixra.atum.core.AtumPlugin
import me.phoenixra.atum.core.scoreboard.Scoreboard
import me.phoenixra.atum.core.scoreboard.ScoreboardManager
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class AtumScoreboardManager(
    private val plugin: AtumPlugin
): ScoreboardManager {

    private var boards = HashMap<String, Scoreboard>()

    private var taskEnabled = false
    private var  task: BukkitTask? = null

    override fun run() {
        for (board in boards.values) {
            try {
                board.update()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun enable(value: Boolean) {
        taskEnabled = value
        val localTask = task
        if(taskEnabled) {
            if(localTask == null || localTask.isCancelled){
                task = plugin.scheduler.runTimer(0, 2, this)
            }
        }else{
            if(localTask != null && !localTask.isCancelled){
                localTask.cancel()
            }
            task = null
        }
    }

    override fun isEnabled(): Boolean {
        return taskEnabled
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