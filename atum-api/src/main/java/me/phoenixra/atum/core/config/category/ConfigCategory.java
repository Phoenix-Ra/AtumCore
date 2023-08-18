package me.phoenixra.atum.core.config.category;

import me.phoenixra.atum.core.AtumAPI;
import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.config.Config;
import me.phoenixra.atum.core.config.ConfigType;
import me.phoenixra.atum.core.tuples.PairRecord;
import me.phoenixra.atum.core.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public abstract class ConfigCategory {
    private final AtumPlugin plugin;
    private final ConfigType configType;
    private final String id;
    private final String directory;
    private final boolean supportSubFolders;
    /**
     * Config Category class
     *
     * @param plugin            the plugin
     * @param configType        the config type
     * @param id                the category id
     * @param directory         the directory path
     * @param supportSubFolders if accept configs from subFolders
     */
    public ConfigCategory(@NotNull AtumPlugin plugin,
                          @NotNull ConfigType configType,
                          @NotNull String id,
                          @NotNull String directory,
                          boolean supportSubFolders) {
        this.plugin = plugin;
        this.configType = configType;
        this.id = id;
        this.directory = directory;
        this.supportSubFolders = supportSubFolders;
    }
    /**
     * Config Category class
     *
     * @param plugin            the plugin
     * @param id                the category id
     * @param directory         the directory path
     * @param supportSubFolders if accept configs from subFolders
     */
    public ConfigCategory(@NotNull AtumPlugin plugin,
                          @NotNull String id,
                          @NotNull String directory,
                          boolean supportSubFolders) {
        this(plugin, ConfigType.YAML, id, directory, supportSubFolders);
    }

    /**
     * Reload the config category
     */
    public final void reload() {
        beforeReload();
        clear();
        File dir = new File(plugin.getDataFolder(), directory);
        if (!dir.exists()) {
            loadDefaults();
        }
        for (PairRecord<String, File> entry : FileUtils.loadFiles(dir, supportSubFolders)) {
            Config conf = AtumAPI.getInstance().createLoadableConfig(
                    getPlugin(),
                    entry.getSecond().getName().split("\\.")[0],
                    directory,
                    configType,
                    false
            );
            acceptConfig(entry.getFirst(), conf);
        }
        afterReload();
    }

    private void loadDefaults() {
        for (String path : FileUtils.getAllPathsInResourceFolder(plugin, directory)) {
            try {
                File file = new File(plugin.getDataFolder(), path);
                if (!file.getName().contains(".")) {
                    file.mkdir();
                    plugin.getLogger().info("Dir: " + path + " | " + file.getName());
                    continue;
                }
                plugin.getLogger().info("File: " + path + " | " + file.getName());
                var stream = plugin.getResource(path);
                if (stream == null) continue;
                Files.copy(stream, Path.of(file.toURI()), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Clear the saved data
     */
    protected abstract void clear();

    /**
     * Accept the config
     */
    protected abstract void acceptConfig(@NotNull String id, @NotNull Config config);

    /**
     * Called before category reload
     * <p></p>
     * Override to add implementation
     */
    public void beforeReload() {
    }

    /**
     * Called after category reload
     * <p></p>
     * Override to add implementation
     */
    public void afterReload() {
    }


    /**
     * Get the ID of a category
     *
     * @return The id
     */
    public final @NotNull String getId() {
        return id;
    }

    /**
     * Get the directory path
     *
     * @return The directory
     */
    public final @NotNull String getDirectory() {
        return directory;
    }


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    public final @NotNull AtumPlugin getPlugin() {
        return plugin;
    }
}
