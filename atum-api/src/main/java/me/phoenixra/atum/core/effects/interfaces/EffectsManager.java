package me.phoenixra.atum.core.effects.interfaces;

import me.phoenixra.atum.core.AtumPlugin;
import me.phoenixra.atum.core.effects.types.ImageEffect;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public interface EffectsManager {

    /**
     * Start an effect
     *
     * @param effect The effect
     */
    void startEffect(@NotNull Effect effect);

    /**
     * Finish an effect
     *
     * @param effect The effect
     */
    void finishEffect(@NotNull Effect effect);

    /**
     * cancel effects with specified id.
     *
     * @param id An effect Id
     */
    void cancelEffectsByID(@NotNull String id);

    /**
     * cancel all effects attached to this manager
     *
     */
    void cancelAllEffects();

    /**
     * get all effects with specified id
     *
     * @param id the effect id
     * @return list of effects
     */
    @NotNull
    List<Effect> getRunningEffectsByID(@NotNull String id);

    /**
     * save the image in cache or get already saved
     *
     */
    void loadImage(@NotNull File file, @NotNull ImageEffect.ImageLoadCallback callback);

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
