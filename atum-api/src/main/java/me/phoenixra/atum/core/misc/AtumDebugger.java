package me.phoenixra.atum.core.misc;

import me.phoenixra.atum.core.AtumPlugin;

public class AtumDebugger {
    private final String taskId;
    private final String startInfo;
    private final AtumPlugin plugin;
    private final Runnable taskToDebug;
    /**
     * Task Debugger, which can help u to
     * check the time required to execute the task
     *
     * @param plugin the plugin
     * @param taskId id of a task
     * @param startInfo info that is written on start debugging
     * @param taskToDebug the task
     */
    public AtumDebugger(AtumPlugin plugin, String taskId, String startInfo, Runnable taskToDebug){
        this.plugin = plugin;
        this.taskId = taskId;
        this.startInfo = startInfo;
        this.taskToDebug = taskToDebug;
    }
    /**
     * Start the task
     *
     */
    public void start(){
        if(!plugin.getConfigYml().isDebugEnabled()){
            taskToDebug.run();
            return;
        }
        plugin.getLogger().info("&eDEBUG-> task with id "+taskId+" has been started\n"+startInfo);
        long debugTimer = System.currentTimeMillis();
        try{
            taskToDebug.run();
        }catch (Exception exception){
            plugin.getLogger().info("&eDEBUG-> task with id "+taskId+"&c FAILED WITH AN ERROR!");
            throw exception;
        }
        debugTimer = System.currentTimeMillis() - debugTimer;
        plugin.getLogger().info("&eDEBUG-> task with id "+taskId+"&a SUCCESS\n " +
                "&eIt took &6"+debugTimer+"&e milliseconds to execute");

    }
}
