package me.phoenixra.atum.core.config.wrapper;

import me.phoenixra.atum.core.config.Config;
import me.phoenixra.atum.core.config.ConfigType;
import me.phoenixra.atum.core.placeholders.InjectablePlaceholder;
import me.phoenixra.atum.core.placeholders.context.PlaceholderContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigWrapper<T extends Config> implements Config {

    private final T handle;

    /**
     * Create a config wrapper.
     *
     * @param handle The config that is being wrapped.
     */
    protected ConfigWrapper(@NotNull final T handle) {
        this.handle = handle;
    }

    @Override
    public String toPlaintext() {
        return handle.toPlaintext();
    }

    @Override
    public boolean hasPath(@NotNull String path) {
        return handle.hasPath(path);
    }

    @Override
    public @NotNull List<String> getKeys(boolean deep) {
        return handle.getKeys(deep);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return handle.get(path);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object obj) {
        handle.set(path,obj);
    }

    @Override
    public @Nullable Integer getIntOrNull(@NotNull String path) {
        return handle.getIntOrNull(path);
    }

    @Override
    public @Nullable List<Integer> getIntListOrNull(@NotNull String path) {
        return handle.getIntListOrNull(path);
    }

    @Override
    public @Nullable Boolean getBoolOrNull(@NotNull String path) {
        return handle.getBoolOrNull(path);
    }

    @Override
    public @Nullable List<Boolean> getBoolListOrNull(@NotNull String path) {
        return handle.getBoolListOrNull(path);
    }

    @Override
    public @Nullable String getStringOrNull(@NotNull String path) {
        return handle.getStringOrNull(path);
    }

    @Override
    public @Nullable List<String> getStringListOrNull(@NotNull String path) {
        return handle.getStringListOrNull(path);
    }

    @Override
    public @Nullable Double getDoubleOrNull(@NotNull String path) {
        return handle.getDoubleOrNull(path);
    }

    @Override
    public @Nullable List<Double> getDoubleListOrNull(@NotNull String path) {
        return handle.getDoubleListOrNull(path);
    }

    @Override
    public @Nullable Config getSubsection(@NotNull String path) {
        return handle.getSubsection(path);
    }

    @Override
    public @Nullable List<? extends Config> getSubsectionListOrNull(@NotNull String path) {
        return handle.getSubsectionListOrNull(path);
    }

    @Override
    public double getEvaluated(@NotNull String path, @NotNull PlaceholderContext context) {
        return handle.getEvaluated(path,context);
    }

    @Override
    public @NotNull ConfigType getType() {
        return handle.getType();
    }

    @Override
    public void addInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
        handle.addInjectablePlaceholder(placeholders);
    }

    @Override
    public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
        handle.removeInjectablePlaceholder(placeholders);
    }

    @Override
    public void clearInjectedPlaceholders() {
        handle.clearInjectedPlaceholders();
    }

    @Override
    public @NotNull List<InjectablePlaceholder> getPlaceholderInjections() {
        return handle.getPlaceholderInjections();
    }

    public T getHandle(){
        return handle;
    }

}
