package me.phoenixra.atum.core.effects.interfaces;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EffectLocation extends Cloneable{
    /**
     * get original location (i.e starting loc)
     *
     * @return The location
     */
    @NotNull
    Location getOrigin();

    /**
     * get targeted entity
     *
     * @return The entity
     */
    @Nullable
    Entity getTargetEntity();

    /**
     * get current effect location
     *
     * @return The location
     */
    Location getCurrentLocation();

    /**
     * updates the current effect location
     *
     * @return The location
     */
    Location updateLocation();


    /**
     * set offset x,y,z
     *
     * @param offsetX the X value
     * @param offsetY the Y value
     * @param offsetZ the Z value
     * @return this
     */
    EffectLocation setOffsetXYZ(double offsetX, double offsetY, double offsetZ);

    /**
     * set offset yaw
     *
     * @param offsetYaw the value
     * @return this
     */
    EffectLocation setOffsetYaw(float offsetYaw);

    /**
     * set offset pitch
     *
     * @param offsetPitch the value
     * @return this
     */
    EffectLocation setOffsetPitch(float offsetPitch);


    /**
     * set velocity x,y,z
     *
     * @param velocityX the X value
     * @param velocityY the Y value
     * @param velocityZ the Z value
     * @return this
     */
    EffectLocation setVelocityXYZ(double velocityX,double velocityY,double velocityZ);

    /**
     * set velocity yaw
     *
     * @param velocityYaw the value
     * @return this
     */
    EffectLocation setVelocityYaw(float velocityYaw);

    /**
     * set velocity pitch
     *
     * @param velocityPitch the value
     * @return this
     */
    EffectLocation setVelocityPitch(float velocityPitch);


    EffectLocation clone();

}
