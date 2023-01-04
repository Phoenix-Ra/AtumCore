package me.phoenixra.atum.core.effects.interfaces;

import me.phoenixra.atum.core.utils.effects.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Effect extends Cloneable{
    String id = UUID.randomUUID().toString();

    /**
     * updates an effect
     */
    void run();

    /**
     * completes an effect
     */
    default void finish(){
        getEffectsManager().finishEffect(this);
    }

    /**
     * cancels an effect
     * @param callFinished whether to call complete() method
     */
    default void cancel(boolean callFinished){
       if(callFinished) finish();
    }

    /**
     * returns effect update delay in ticks
     */
    int getDelay();

    /**
     * returns effect update period in ticks
     */
    int getPeriod();
    /**
     * returns amount of iterations left
     * @apiNote
     * <p></p>
     * <i>'-1' -> uses runTask() than runTaskTimer()<i/>
     * <p></p>
     * <i>'-2' -> infinite, can be stopped only manually.<i/>
     */
    int getIterations();

    /**
     * get the second location of an effect (for specific effect types)
     */
    @Nullable
    EffectLocation getTarget();
    /**
     * sets the second location of an effect (for specific effect types)
     */
    void setTarget(@Nullable EffectLocation location);

    /**
     * returns an original location of effect
     */
    @NotNull
    EffectLocation getOrigin();
    /**
     * sets an original location of effect
     */
    void setOrigin(@NotNull EffectLocation location);

    /**
     * is effect finished?
     */
    boolean isFinished();

    /**
     * Does effect updates asynchronously?
     */
    boolean isAsync();

    /**
     * display the particle
     *
     * @param particle the particle type
     * @param location the location where to display
     */
    default void displayParticle(@NotNull Particle particle, @NotNull Location location) {
        displayParticle(particle, location, null, 1);
    }

    /**
     * display the particle
     *
     * @param particle the particle type
     * @param location the location where to display
     * @param color the color of a particle
     * @param amount the amount of particles
     */
    default void displayParticle(Particle particle,
                                 Location location,
                                 Color color,
                                 int amount) {
        ParticleUtils.display(particle, location, 0, 0, 0, 0, amount,
                color, null, 100);
    }

    /**
     * display the particle
     *
     * @param particle the particle type
     * @param location the location where to display
     * @param color the color of the particle
     * @param offsetX the offset X of the particle (can be useful with >1 particles amount)
     * @param offsetY the offset Y of the particle (can be useful with >1 particles amount)
     * @param offsetZ the offset Z of the particle (can be useful with >1 particle amount)
     * @param amount the amount of particles
     */
    default void displayParticle(Particle particle,
                                 Location location,
                                 Color color,
                                 float offsetX,
                                 float offsetY,
                                 float offsetZ,
                                 int amount) {

        ParticleUtils.display(particle, location, offsetX, offsetY, offsetZ, 0, amount,
                color, null, 100);
    }

    /**
     * display the particle
     *
     * @param particle the particle type
     * @param location the location where to display
     * @param color the color of the particle
     * @param offsetX the offset X of the particle (can be useful with >1 particles amount)
     * @param offsetY the offset Y of the particle (can be useful with >1 particles amount)
     * @param offsetZ the offset Z of the particle (can be useful with >1 particle amount)
     * @param amount the amount of particles
     * @param speed the speed of the particle
     * @param material the material of the particle
     * @param range the display range of the particle
     */
    default void displayParticle(Particle particle,
                                 Location location,
                                 Color color,
                                 float offsetX,
                                 float offsetY,
                                 float offsetZ,
                                 int amount,
                                 float speed,
                                 Material material,
                                 double range) {

        ParticleUtils.display(particle, location, offsetX, offsetY, offsetZ, speed, amount,
                color, material, range);
    }


    Effect clone();
    /**
     * get effects manager the effect attached to
     *
     * @return The effects manager
     */
    EffectsManager getEffectsManager();

    /**
     * get effect id
     *
     * @return The effect id
     */
    default String getId(){
        return id;
    }
}
