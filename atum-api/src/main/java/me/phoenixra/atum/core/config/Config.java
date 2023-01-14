package me.phoenixra.atum.core.config;

import me.phoenixra.atum.core.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface Config {

    /**
     * Convert the config into text
     * used in writing config
     * data into a file
     *
     * @return The plaintext.
     */
    String toPlaintext();

    /**
     * Get if the config contains a specified path
     *
     * @param path The path to check.
     * @return true if contains
     */
    boolean hasPath(@NotNull String path);

    /**
     * Get config keys.
     *
     * @param deep If keys from subsections should be added to that list as well
     * @return A list of the keys.
     */
    @NotNull
    List<String> getKeys(boolean deep);
    /**
     * Recurse config keys
     *
     * @param found The found keys.
     * @param root  The root.
     * @return The keys.
     */
    @NotNull
    default List<String> recurseKeys(@NotNull Set<String> found,
                                     @NotNull String root) {
        return new ArrayList<>();
    }

    @Nullable
    Object get(@NotNull String path);

    /**
     * Set an object in config
     * <p></p>
     * Set null to remove the config section
     * <p></p>
     * You can also set a {@link Config} object, so it will be a section
     *
     * @param path The path.
     * @param obj  The object.
     */
    void set(@NotNull String path,
             @Nullable Object obj);




    default int getInt(@NotNull String path) {
        return Objects.requireNonNullElse(getIntOrNull(path), 0);
    }
    default int getIntOrDefault(@NotNull String path,
                       int def) {
        return Objects.requireNonNullElse(getIntOrNull(path), def);
    }
    @Nullable
    Integer getIntOrNull(@NotNull String path);



    @NotNull
    default List<Integer> getIntList(@NotNull String path) {
        return Objects.requireNonNullElse(getIntListOrNull(path), new ArrayList<>());
    }
    @Nullable
    List<Integer> getIntListOrNull(@NotNull String path);



    default boolean getBool(@NotNull String path) {
        return Objects.requireNonNullElse(getBoolOrNull(path), false);
    }
    @Nullable
    Boolean getBoolOrNull(@NotNull String path);


    @NotNull
    default List<Boolean> getBoolList(@NotNull String path) {
        return Objects.requireNonNullElse(getBoolListOrNull(path), new ArrayList<>());
    }
    @Nullable
    List<Boolean> getBoolListOrNull(@NotNull String path);



    @NotNull
    default String getFormattedString(@NotNull String path){
        return StringUtils.format(getStringOrDefault(path, ""));
    }
    @NotNull
    default String getString(@NotNull String path) {
        return getStringOrDefault(path, "");
    }
    @NotNull
    default String getStringOrDefault(@NotNull String path, @NotNull String def) {
        return Objects.requireNonNullElse(getStringOrNull(path), def);
    }
    @Nullable
    String getStringOrNull(@NotNull String path);


    @NotNull
    default List<String> getFormattedStringList(@NotNull String path) {
        return StringUtils.format(
                Objects.requireNonNullElse(getStringListOrNull(path), new ArrayList<>())
        );
    }
    @NotNull
    default List<String> getStringList(@NotNull String path) {
        return Objects.requireNonNullElse(getStringListOrNull(path), new ArrayList<>());
    }
    @Nullable
    List<String> getStringListOrNull(@NotNull String path);



    default double getDouble(@NotNull String path) {
        return Objects.requireNonNullElse(getDoubleOrNull(path), 0.0);
    }
    default double getDoubleOrDefault(@NotNull String path, double def) {
        return Objects.requireNonNullElse(getDoubleOrNull(path), def);
    }
    @Nullable
    Double getDoubleOrNull(@NotNull String path);



    @NotNull
    default List<Double> getDoubleList(@NotNull String path) {
        return Objects.requireNonNullElse(getDoubleListOrNull(path), new ArrayList<>());
    }
    @Nullable
    List<Double> getDoubleListOrNull(@NotNull String path);


    /**
     * get the AtumConfig subsection
     * <p></p>
     * Works the same way as FileConfiguration#getConfigurationSection
     *
     * @param path  The path.
     */
    @Nullable
    Config getSubsection(@NotNull String path);

    /**
     * get all subsections of the key
     *
     * @param path  The path.
     * @return The found value, or empty {@link java.util.ArrayList} if not found.
     */
    @NotNull
    default List<? extends Config> getSubsectionList(@NotNull String path) {
        return Objects.requireNonNullElse(getSubsectionListOrNull(path), new ArrayList<>());
    }
    /**
     * get all subsections of the key
     *
     * @param path  The path.
     * @return The found value, or null
     */
    @Nullable
    List<? extends Config> getSubsectionListOrNull(@NotNull String path);


    @NotNull
    ConfigType getType();


    default Map<String, Object> toMap() {
        return new HashMap<>();
    }


    default ConfigurationSection toBukkit() {
        return new YamlConfiguration().createSection("temporary", this.toMap());
    }

}
