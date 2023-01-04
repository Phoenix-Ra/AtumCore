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
    void startEffect(Effect effect);

    /**
     * Finish an effect
     *
     * @param effect The effect
     */
    void finishEffect(Effect effect);

    /**
     * cancel effects with specified id.
     *
     * @param id An effect Id
     */
    void cancelEffectsByID(String id);

    /**
     * cancel all effects attached to this manager
     *
     */
    void cancelAllEffects();

    /**
     * get all effects with specified id
     *
     */
    List<Effect> getRunningEffectsByID(String id);


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
