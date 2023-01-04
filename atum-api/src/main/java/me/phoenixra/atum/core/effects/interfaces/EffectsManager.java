package me.phoenixra.atum.core.effects.interfaces;

import me.phoenixra.atum.core.AtumPlugin;
import org.jetbrains.annotations.NotNull;

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
     */
    @NotNull
    List<Effect> getRunningEffectsByID(@NotNull String id);


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
