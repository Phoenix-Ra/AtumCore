package me.phoenixra.atum.core.config.wrapper;

import me.phoenixra.atum.core.config.LoadableConfig;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LoadableConfigWrapper extends ConfigWrapper<LoadableConfig> implements LoadableConfig {
    protected LoadableConfigWrapper(@NotNull final LoadableConfig handle) {
        super(handle);
    }
    @Override
    public void createFile(boolean forceResourceLoad) {
        getHandle().createFile(forceResourceLoad);
    }

    @Override
    public void reload() throws IOException {
        getHandle().reload();
    }

    @Override
    public void save() throws IOException {
        getHandle().save();
    }

    @Override
    public File getFile() {
        return getHandle().getFile();
    }

    @Override
    public String getResourcePath() {
        return getHandle().getResourcePath();
    }

    @Override
    public List<String> getHeader() {
        return getHandle().getHeader();
    }

    @Override
    public void setExtraHeaderText(List<String> list) {
        getHandle().setExtraHeaderText(list);
    }

    @Override
    public @NotNull YamlConfiguration toBukkit() {
        return getHandle().toBukkit();
    }
}
