package me.phoenixra.atum.core;

import me.phoenixra.atum.core.command.AtumCommand;
import me.phoenixra.atum.core.config.ConfigManager;
import me.phoenixra.atum.core.config.ConfigType;
import me.phoenixra.atum.core.config.LoadableConfig;
import me.phoenixra.atum.core.config.base.LangYml;
import me.phoenixra.atum.core.effects.interfaces.EffectsManager;
import me.phoenixra.atum.core.events.EventManager;
import me.phoenixra.atum.core.gui.GuiController;
import me.phoenixra.atum.core.schedule.Scheduler;
import lombok.Getter;
import me.phoenixra.atum.core.scoreboard.ScoreboardManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public abstract class AtumPlugin extends JavaPlugin {
    @Getter
    private final AtumAPI atumAPI;

    @Getter
    private final Scheduler scheduler;
    @Getter
    private final EventManager eventManager;
    @Getter
    private final ConfigManager configManager;
    @Getter
    private final ScoreboardManager scoreboardManager;
    @Getter
    private final GuiController guiController;
    @Getter
    private final EffectsManager effectsManager;


    private final Logger logger;


    @Getter
    private final LoadableConfig configYml;
    @Getter
    private final LangYml langYml;

    private final List<Runnable> onEnableTasks = new ArrayList<>();
    private final List<Runnable> onDisableTasks = new ArrayList<>();
    private final List<Runnable> onReloadTasks = new ArrayList<>();
    private final List<Runnable> onLoadTasks = new ArrayList<>();
    private final List<Runnable> afterLoadTasks = new ArrayList<>();


    protected AtumPlugin() {
        if(AtumAPI.getInstance()==null){
            AtumAPI.Instance.set(loadAPI());
        }
        atumAPI = AtumAPI.getInstance();

        this.logger = atumAPI.createLogger(this);

        this.getLogger().info("Initializing "  + this.getName());

        this.scheduler = atumAPI.createScheduler(this);
        this.eventManager = atumAPI.createEventManager(this);
        this.configManager = atumAPI.createConfigManager(this);
        this.scoreboardManager = atumAPI.createScoreboardManager(this);
        this.guiController = atumAPI.createGuiController(this);
        this.effectsManager = atumAPI.createEffectsManager(this);

        configYml = createConfig();
        langYml = createLang();

        atumAPI.addPlugin(this);

    }


    @Override
    public final void onEnable() {
        this.getLogger().info("");
        this.getLogger().info("Loading " + this.getName());


        getEventManager().registerListener(guiController);
        this.loadListeners().forEach(listener -> getEventManager().registerListener(listener));

        this.loadPluginCommands().forEach(AtumCommand::register);

        this.getScheduler().runLater(1,this::afterLoad);

        this.handleEnable();
        this.onEnableTasks.forEach(Runnable::run);

        this.getLogger().info("");
    }



    @Override
    public final void onDisable() {
        super.onDisable();

        this.getEventManager().unregisterAllListeners();
        this.getScheduler().cancelAll();

        this.handleDisable();
        this.onDisableTasks.forEach(Runnable::run);

        this.getLogger().info("Cleaning up...");

        atumAPI.removePlugin(this);
    }


    @Override
    public final void onLoad() {
        super.onLoad();

        this.handleLoad();
        this.onLoadTasks.forEach(Runnable::run);
    }



    public final void afterLoad() {


        this.handleAfterLoad();
        this.afterLoadTasks.forEach(Runnable::run);

        this.reload();

        this.getLogger().info("Loaded " + this.getName());
    }



    public final void reload() {
        this.getScheduler().cancelAll();
        this.getConfigManager().reloadAllConfigs();

        this.handleReload();
        this.onReloadTasks.forEach(Runnable::run);
    }


    protected abstract void handleEnable();
    protected abstract void handleDisable();
    protected void handleLoad() {
    }
    protected void handleReload() {

    }
    protected void handleAfterLoad() {
    }

    public void addTaskOnEnable(Runnable task){
        onEnableTasks.add(task);
    }
    public void addTaskOnDisable(Runnable task){
        onDisableTasks.add(task);
    }
    public void addTaskOnLoad(Runnable task){
        onLoadTasks.add(task);
    }
    public void addTaskOnReload(Runnable task){
        onReloadTasks.add(task);
    }
    public void addTaskAfterLoad(Runnable task){
        afterLoadTasks.add(task);
    }


    /**
     * All commands to be registered.
     *
     * @return A list of all commands
     */
    protected List<AtumCommand> loadPluginCommands() {
        return new ArrayList<>();
    }
    /**
     * All listeners to be registered.
     *
     * @return A list of all listeners.
     */
    protected List<Listener> loadListeners()  {
        return new ArrayList<>();
    }

    /**
     * Load an API. Use it if you want to implement
     * your own AtumAPI class.
     * <p></p>
     *
     * @return AtumAPI class
     */
    protected AtumAPI loadAPI(){
        return AtumAPI.getInstance();
    }

    @NotNull
    @Override
    public Logger getLogger() {
        return logger;
    }


    /**
     * Tries to load the lang.yml from the resources
     * <p>
     * Override if needed.
     *
     * @return lang.yml.
     */
    protected LangYml createLang() {
        try {
            return new LangYml(this,"lang",ConfigType.YAML,true);
        }catch (NullPointerException ex){
            this.getLogger().severe("Failed to load 'lang.yml' from the plugin resources");
            return new LangYml(this,"lang",ConfigType.YAML,false);
        }
    }

    /**
     * Tries to load the config.yml from the resources
     * <p>
     * Override if needed.
     *
     * @return config.yml.
     */
    protected LoadableConfig createConfig() {
        try {
            return getAtumAPI()
                    .createLoadableConfig(this, "config", "", ConfigType.YAML,true);
        }catch (NullPointerException ex){
            this.getLogger().severe("Failed to load 'config.yml' from the plugin resources");

            return getAtumAPI()
                    .createLoadableConfig(this, "config", "", ConfigType.YAML,true);
        }
    }
    /**
     * The {@link JavaPlugin} method
     * <p></p>
     * The AtumPlugin doesn't support it.
     *
     * @deprecated Use the Atum config system.
     * @see AtumPlugin#getConfigManager()
     */
    @NotNull
    @Override
    @Deprecated
    public final FileConfiguration getConfig() {
        this.getLogger().warning("Call to Bukkit config method in atum plugin!");

        return Objects.requireNonNull(getConfigYml()).toBukkit();
    }

    /**
     * The {@link JavaPlugin} method
     * <p></p>
     * The AtumPlugin doesn't support it.
     *
     * @deprecated Use the Atum config system.
     * @see AtumPlugin#getConfigManager()
     */
    @Override
    @Deprecated
    public final void saveConfig() {
        this.getLogger().warning("Call to Bukkit config method in atum plugin!");

        super.saveConfig();
    }

    /**
     * The {@link JavaPlugin} method
     * <p></p>
     * The AtumPlugin doesn't support it.
     *
     * @deprecated Use the Atum config system.
     * @see AtumPlugin#getConfigManager()
     */
    @Override
    @Deprecated
    public final void saveDefaultConfig() {
        this.getLogger().warning("Call to Bukkit config method in atum plugin!");
        super.saveDefaultConfig();
    }

    /**
     * The {@link JavaPlugin} method
     * <p></p>
     * The AtumPlugin doesn't support it.
     *
     * @deprecated Use the Atum config system.
     * @see AtumPlugin#getConfigManager()
     */
    @Override
    @Deprecated
    public final void reloadConfig() {
        this.getLogger().warning("Call to default Bukkit method in atum plugin!");

        super.reloadConfig();
    }

    /**
     * Get colored plugin name
     * <p></p>
     * Override to make your plugin name look unique
     * @return The colored name of a plugin
     */
    public String getColoredName(){
        return getName();
    }



    public AtumPlugin getCorePlugin(){
        return atumAPI.getPluginByName("AtumCore");
    }
}
