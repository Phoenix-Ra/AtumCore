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
import me.phoenixra.atum.core.scoreboard.ScoreboardManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public abstract class AtumPlugin extends JavaPlugin {

    private final AtumAPI atumAPI;

    private final Scheduler scheduler;
    private final EventManager eventManager;
    private final ConfigManager configManager;
    private final ScoreboardManager scoreboardManager;
    private final GuiController guiController;
    private final EffectsManager effectsManager;


    private final Logger logger;


    private final LoadableConfig configYml;
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

        if(!getName().equals("AtumCore")){
            AtumPlugin corePlugin = getCorePlugin();
            if(this.getAtumAPIVersion() != corePlugin.getAtumAPIVersion()){
                logger.info("&cYour server uses an unsupported Atum API version!");
                logger.info("&cPlugin supports:&e "+this.getAtumAPIVersion());
                logger.info("&cYour server have:&e "+corePlugin.getAtumAPIVersion());
                logger.info(
                        String.format("&eDownload the AtumCore v2.%s &eor use this plugin at your own risk",
                        corePlugin.getAtumAPIVersion())
                );
            }
        }

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

        this.handleEnable();
        this.onEnableTasks.forEach(Runnable::run);

        this.getScheduler().runLater(1,this::afterLoad);

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
        if(scoreboardManager.isEnabled()) {
            scoreboardManager.enable(true);
        }
    }


    protected abstract void handleEnable();
    protected abstract void handleDisable();

    /**
     * Get the AtumAPI version the plugin uses
     * <p>Pattern:</p>
     * <p>2.6.5 = 6</p>
     * <p>2.15.0 = 15</p>
     *
     * If the supported API version and
     * the version the server have are different
     * the warning will be sent
     *
     * @return The version
     */
    protected abstract int getAtumAPIVersion();

    /**
     * Method with empty implementation,
     * that is called on plugin load.
     * <p></p>
     * Override if needed
     *
     */
    protected void handleLoad() {
    }

    /**
     * Method with empty implementation,
     * that is called on plugin reload
     * <p></p>
     * Override if needed
     *
     */
    protected void handleReload() {

    }

    /**
     * Method with empty implementation,
     * that is called after plugin has been loaded and enabled
     * <p></p>
     * Override if needed
     *
     */
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
     * AtumAPI instance
     *
     * @return The scheduler
     */
    public AtumAPI getAtumAPI() {
        return atumAPI;
    }

    /**
     * Get Scheduler
     * <p></p>
     * Works via Bukkit#getScheduler
     * but with more convenient methods
     *
     * @return The scheduler
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Get Scoreboard Manager
     *
     * @return The scoreboard manager
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Get Config Manager
     *
     * @return The config manager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Get Scoreboard Manager
     *
     * @return The scoreboard manager
     */
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    /**
     * Get Gui Controller
     *
     * @return The gui controller
     */
    public GuiController getGuiController(){
        return guiController;
    }

    /**
     * Get Effects Manager
     *
     * @return The effects manager
     */
    public EffectsManager getEffectsManager() {
        return effectsManager;
    }

    /**
     * Get ConfigYml
     *
     * @return The config
     */
    public LoadableConfig getConfigYml() {
        return configYml;
    }

    /**
     * Get LangYml
     *
     * @return The langYml
     */
    public LangYml getLangYml() {
        return langYml;
    }

    /**
     * Get colored plugin name
     *
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
