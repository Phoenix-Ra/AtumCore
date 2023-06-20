package me.phoenixra.atum.core.misc;

import me.phoenixra.atum.core.AtumPlugin;

import java.util.function.Consumer;

public class AtumDebugger {
    private final AtumPlugin plugin;
    private final String taskId;
    private final String startInfo;
    private Consumer<Exception> onError = null;
    /**
     * Task Debugger, which can help u to
     * check the time required to execute the task
     *
     * @param plugin the plugin
     * @param taskId id of a task
     * @param startInfo info that is written on start debugging
     */
    public AtumDebugger(AtumPlugin plugin, String taskId, String startInfo){
        this.plugin = plugin;
        this.taskId = taskId;
        this.startInfo = startInfo;
    }

    /**
     * Set the error handler
     *
     * @param onError the error handler
     * @return the debugger
     */
    public AtumDebugger onError(Consumer<Exception> onError){
        this.onError = onError;
        return this;
    }

    /**
     * Start the task
     *
     */
    public void start(Runnable taskToDebug){
        if(!plugin.getConfigYml().isDebugEnabled()){
            try {
                taskToDebug.run();
                return;
            }catch (Exception exception){
                if(onError != null){
                    onError.accept(exception);
                }
            }
        }
        plugin.getLogger().info("&eDEBUG-> task with id "+taskId+" has been started\n"+startInfo);
        long debugTimer = System.currentTimeMillis();
        try{
            taskToDebug.run();
        }catch (Exception exception){
            plugin.getLogger().info("&eDEBUG-> task with id "+taskId+"&c FAILED WITH AN ERROR!");
            if(onError != null){
                onError.accept(exception);
            }
        }
        debugTimer = System.currentTimeMillis() - debugTimer;
        plugin.getLogger().info("&eDEBUG-> task with id "+taskId+"&a SUCCESS\n " +
                "&eIt took &6"+debugTimer+"&e milliseconds to execute");

    }

}
