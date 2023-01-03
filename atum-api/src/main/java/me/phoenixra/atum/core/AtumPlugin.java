package me.phoenixra.atum.core;

import me.phoenixra.atum.core.command.AtumCommand;
import me.phoenixra.atum.core.config.ConfigManager;
import me.phoenixra.atum.core.config.ConfigType;
import me.phoenixra.atum.core.config.LoadableConfig;
import me.phoenixra.atum.core.events.EventManager;
import me.phoenixra.atum.core.gui.GuiController;
import me.phoenixra.atum.core.schedule.Scheduler;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
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
    private final GuiController guiController;


    private final Logger logger;


    @Getter @Nullable
    private final LoadableConfig configYml;
    @Getter @Nullable
    private final LoadableConfig langYml;

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
        this.guiController = atumAPI.createGuiController(this);

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

    protected void addTaskOnEnable(Runnable task){
        onEnableTasks.add(task);
    }
    protected void addTaskOnDisable(Runnable task){
        onDisableTasks.add(task);
    }
    protected void addTaskOnLoad(Runnable task){
        onLoadTasks.add(task);
    }
    protected void addTaskOnReload(Runnable task){
        onReloadTasks.add(task);
    }
    protected void addTaskAfterLoad(Runnable task){
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
    protected LoadableConfig createLang() {
        try {
            return getAtumAPI()
                    .createLoadableConfig(this, "lang", "", ConfigType.YAML,true);
        }catch (NullPointerException ex){
            this.getLogger().severe("Failed to load 'lang.yml' from the plugin resources");
        }
        return null;
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
        }
        return null;
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



}
