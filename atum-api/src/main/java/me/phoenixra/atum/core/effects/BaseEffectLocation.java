package me.phoenixra.atum.core.effects;

import lombok.Getter;
import me.phoenixra.atum.core.effects.interfaces.Effect;
import me.phoenixra.atum.core.effects.interfaces.EffectLocation;
import me.phoenixra.atum.core.effects.interfaces.ValueLimit;
import me.phoenixra.atum.core.effects.types.DynamicCircle;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BaseEffectLocation implements EffectLocation {
    @Getter @NotNull private final Location origin;
    @Getter @NotNull private Location currentLocation;
    @Getter @Nullable private Entity targetEntity;

    private double offsetX=0.0;
    private double offsetY=0.0;
    private double offsetZ=0.0;
    private float offsetYaw=0;
    private float offsetPitch=0;
    private boolean offsetApplied;

    private double velocityX=0.0;
    private double velocityY=0.0;
    private double velocityZ=0.0;
    private float velocityYaw=0;
    private float velocityPitch=0;
    private int velocityStep;

    private ValueLimit<Double> limitX;
    private ValueLimit<Double> limitY;
    private ValueLimit<Double> limitZ;
    private ValueLimit<Float> limitYaw;
    private ValueLimit<Float> limitPitch;

    public BaseEffectLocation(@NotNull Location location){
        this.currentLocation=location;

        origin = currentLocation.clone();
    }
    public BaseEffectLocation(@NotNull Entity targetEntity){
        this.targetEntity=targetEntity;
        currentLocation=targetEntity.getLocation();

        origin = currentLocation.clone();
    }

    @Override
    public @NotNull Location updateLocation() {
        if(targetEntity!=null){
            currentLocation=targetEntity.getLocation();
            currentLocation.add(offsetX+(velocityX*velocityStep),
                    offsetY + (velocityY*velocityStep),
                    offsetZ + (velocityZ*velocityStep));

            currentLocation.setYaw(currentLocation.getYaw() +
                    offsetYaw + (velocityYaw*velocityStep));
            currentLocation.setPitch(currentLocation.getPitch() +
                    offsetPitch + (velocityPitch*velocityStep));

        }else{
            if(!offsetApplied) {
                currentLocation.add(offsetX, offsetY, offsetZ);
                currentLocation.setYaw(currentLocation.getYaw() + offsetYaw);
                currentLocation.setPitch(currentLocation.getPitch() + offsetPitch);
            }else{
                currentLocation.add(velocityX, velocityY, velocityZ);
                currentLocation.setYaw(currentLocation.getYaw() + velocityYaw);
                currentLocation.setPitch(currentLocation.getPitch() + velocityPitch);
            }

        }
        applyLimits();

        velocityStep++;
        return currentLocation;
    }
    private void applyLimits(){
        if(limitX!=null){
            currentLocation.setX(limitX.limit(currentLocation.getX()));
        }
        if(limitY!=null){
            currentLocation.setY(limitY.limit(currentLocation.getY()));
        }
        if(limitZ!=null){
            currentLocation.setZ(limitZ.limit(currentLocation.getZ()));
        }
        if(limitYaw!=null){
            currentLocation.setYaw(limitYaw.limit(currentLocation.getYaw()));
        }
        if(limitPitch!=null){
            currentLocation.setPitch(limitPitch.limit(currentLocation.getPitch()));
        }
    }

    @Override
    public @NotNull EffectLocation setOffsetXYZ(double offsetX, double offsetY, double offsetZ) {
        this.offsetX=offsetX;
        this.offsetY=offsetY;
        this.offsetZ=offsetZ;
        return this;
    }

    @Override
    public @NotNull EffectLocation setOffsetYaw(float offsetYaw) {
        this.offsetYaw=offsetYaw;
        return this;
    }

    @Override
    public @NotNull EffectLocation setOffsetPitch(float offsetPitch) {
        this.offsetPitch=offsetPitch;
        return this;
    }

    @Override
    public @NotNull EffectLocation setVelocityXYZ(double velocityX, double velocityY, double velocityZ) {
        this.velocityX=velocityX;
        this.velocityY=velocityY;
        this.velocityZ=velocityZ;
        return this;
    }

    @Override
    public @NotNull EffectLocation setVelocityYaw(float velocityYaw) {
        this.velocityYaw=velocityYaw;
        return this;
    }

    @Override
    public @NotNull EffectLocation setVelocityPitch(float velocityPitch) {
        this.velocityPitch=velocityPitch;
        return this;
    }

    @Override
    public @NotNull EffectLocation setLimitXYZ(@Nullable ValueLimit<Double> limitX, @Nullable ValueLimit<Double> limitY, @Nullable ValueLimit<Double> limitZ) {
        this.limitX=limitX;
        this.limitY=limitY;
        this.limitZ=limitZ;
        return this;
    }

    @Override
    public @NotNull EffectLocation setLimitYaw(@Nullable ValueLimit<Float> limitYaw) {
        this.limitYaw=limitYaw;
        return this;
    }

    @Override
    public @NotNull EffectLocation setLimitPitch(@Nullable ValueLimit<Float> limitPitch) {
        this.limitPitch=limitPitch;
        return this;
    }

    @Override
    public EffectLocation clone() throws CloneNotSupportedException {
        try {
            BaseEffectLocation loc=(BaseEffectLocation) (super.clone());

            if(loc.targetEntity==null) loc.currentLocation = loc.origin.clone();

            if(loc.limitX!=null){
                loc.limitX = loc.limitX.clone();
            }
            if(loc.limitY!=null){
                loc.limitY = loc.limitY.clone();
            }
            if(loc.limitZ!=null){
                loc.limitZ = loc.limitZ.clone();
            }
            if(loc.limitYaw!=null){
                loc.limitYaw = loc.limitYaw.clone();
            }
            if(loc.limitPitch!=null){
                loc.limitPitch = loc.limitPitch.clone();
            }
            return loc;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
