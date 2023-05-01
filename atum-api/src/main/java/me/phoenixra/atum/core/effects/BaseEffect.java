package me.phoenixra.atum.core.effects;

import lombok.Getter;
import me.phoenixra.atum.core.effects.annotations.EffectVar;
import me.phoenixra.atum.core.effects.interfaces.Effect;
import me.phoenixra.atum.core.effects.interfaces.EffectLocation;
import me.phoenixra.atum.core.effects.interfaces.EffectVariable;
import me.phoenixra.atum.core.effects.interfaces.EffectsManager;
import me.phoenixra.atum.core.utils.effects.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class BaseEffect implements Effect {
    @Getter private String id = UUID.randomUUID().toString();
    @Getter @NotNull private final EffectsManager effectsManager;

    @Getter boolean isAsync;

    @Getter private BaseEffect copy;

    @Getter @NotNull private EffectLocation origin;
    @Getter @Nullable private EffectLocation target;

    @Getter private boolean finished;
    @Getter private boolean runningManually;

    @Getter protected long delay = 0;
    @Getter protected long period = 1;
    @Getter protected int iterations = 1;

    @Getter protected int repeats = 0;
    @Getter protected int repeatDelay = 0;
    private int repeatDelayCount;

    @Getter private double displayRange=100;

    @EffectVar @Getter private EffectVariable<Particle> particleType     = new BaseEffectVariable<>(Particle.REDSTONE);
    @EffectVar @Getter private EffectVariable<Color> particleColor       = new BaseEffectVariable<>(Color.WHITE);
    @EffectVar @Getter private EffectVariable<Material> particleMaterial = new BaseEffectVariable<>(Material.BARRIER);
    @EffectVar @Getter private EffectVariable<Float> particleSpeed       = new BaseEffectVariable<>(0.0f);
    @EffectVar @Getter private EffectVariable<Integer> particleCount     = new BaseEffectVariable<>(1);
    @EffectVar @Getter private EffectVariable<Double> particleOffsetX     = new BaseEffectVariable<>(0.0);
    @EffectVar @Getter private EffectVariable<Double> particleOffsetY     = new BaseEffectVariable<>(0.0);
    @EffectVar @Getter private EffectVariable<Double> particleOffsetZ     = new BaseEffectVariable<>(0.0);

    @EffectVar @Getter protected EffectVariable<Double> thickness        = new BaseEffectVariable<>(1.0);


    private List<EffectVariable<?>> variables;

    public BaseEffect(@NotNull EffectsManager effectsManager,@NotNull EffectLocation origin, @NotNull Particle particle, long period, int iterations){
        this.effectsManager = effectsManager;
        this.origin = origin;

        this.particleType.setValue(particle);
        this.period=period;
        this.iterations=iterations;

        loadVariables();
    }
    public BaseEffect(@NotNull EffectsManager effectsManager, @NotNull EffectLocation origin){
        this(effectsManager, origin, Particle.REDSTONE,1,0);
    }

    @Override
    public void run() {
        try {
            if (finished) return;
            //copy before first changes of variables
            if (repeats != 0 && copy == null && !runningManually) {
                copy = cloneWithSameId();
            }
            onRun();

            for (EffectVariable<?> variable : variables) {
                variable.runTask(this);
            }
            //repeater
            if (!runningManually) {
                if (repeats > 0 && repeatDelay != -1) {
                    repeatDelayCount--;
                    if (repeatDelayCount <= 0) {
                        BaseEffect craftEffect = copy.cloneWithSameId();
                        craftEffect.repeats = 0;
                        effectsManager.startEffect(craftEffect);

                        repeats--;
                        repeatDelayCount = repeatDelay;
                    }

                } else if (repeats == -1 && repeatDelay != -1) {
                    repeatDelayCount--;
                    if (repeatDelayCount <= 0) {
                        BaseEffect craftEffect = copy.cloneWithSameId();
                        craftEffect.repeats = 0;
                        effectsManager.startEffect(craftEffect);
                        repeatDelayCount = repeatDelay;
                    }
                }
            }

            if (iterations == -1) {
                return;
            }
            iterations--;
            if (iterations < 1) {
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
            cancel(false);
        }

    }
    protected abstract void onRun();

    @Override
    public final void finish() {
        finished = true;
        effectsManager.finishEffect(this);
        onFinish();
        //repeat
        if (runningManually) return;

        if (repeats > 0 && repeatDelay == -1) {
            BaseEffect baseEffect = copy.cloneWithSameId();
            baseEffect.repeats = repeats - 1;
            effectsManager.startEffect(baseEffect);
        } else if (repeats == -1) {
            BaseEffect baseEffect = copy.cloneWithSameId();
            effectsManager.startEffect(baseEffect);
        }
    }
    protected abstract void onFinish();

    @Override
    public final void cancel(boolean callFinished) {
        if (callFinished) {
            finish();
        } else {
            finished = true;
            if(!isRunningManually()) {
                effectsManager.finishEffect(this);
            }
        }
    }


    @Override
    public void displayParticle(@NotNull Particle effect, @NotNull Location location) {
        displayParticle(effect, location, particleColor.getValue(), particleCount.getValue());
    }
    @Override
    public void displayParticle(@NotNull Particle particle, @NotNull Location location, Color color, int amount) {
        ParticleUtils.display(particle, location, particleOffsetX.getValue(), particleOffsetY.getValue(),
                particleOffsetZ.getValue(), particleSpeed.getValue(), amount,
                color, particleMaterial.getValue(), displayRange);
    }
    @Override
    public void displayParticle(@NotNull Particle particle, @NotNull Location location, Color color, float particleOffsetX,
                                float particleOffsetY,
                                float particleOffsetZ, int amount) {
        ParticleUtils.display(particle, location, particleOffsetX, particleOffsetY, particleOffsetZ, particleSpeed.getValue(), amount,
                color, particleMaterial.getValue(), displayRange);
    }

    @Override
    public final void runManually(boolean manually) {
        runningManually=manually;
    }


    @Override
    public final void setTarget(@Nullable EffectLocation location) {
        target = location;
    }


    @Override
    public final void setOrigin(@NotNull EffectLocation location) {
        origin = location;
    }

    /**
     * set repeat delay
     */
    public void setRepeatDelay(int value) {
        repeatDelay = value;
        repeatDelayCount = value;
    }

    /**
     * set amount of effect repeats.
     */
    public void setRepeats(int value) {
        repeats = value;

    }

    /**
     * sets thickness that in most cases determines vector step,
     * per which particles will be displayed
     */
    public void setThickness(EffectVariable<Double> value) {
        thickness = value;
    }

    /**
     * set default particle type
     */
    public final void setParticleType(EffectVariable<Particle> value){
        particleType=value;

    }

    /**
     * set default color of displaying particles
     */
    public void setParticleColor(EffectVariable<Color> color) {
        particleColor = color;

    }

    /**
     * set default particle material
     */
    public final void setParticleMaterial(EffectVariable<Material> value) {
        particleMaterial=value;

    }

    /**
     * set default particle count
     */
    public final void setParticleCount(EffectVariable<Integer> value) {
        particleCount=value;

    }
    /**
     * set default particle speed
     */
    public final void setParticleSpeed(EffectVariable<Float> value) {
        particleSpeed=value;

    }
    /**
     * set default particle offsetX
     */
    public final void setParticleOffsetX(EffectVariable<Double> value) {
        particleOffsetX=value;

    }
    /**
     * set default particle offsetY
     */
    public final void setParticleOffsetY(EffectVariable<Double> value) {
        particleOffsetY=value;

    }
    /**
     * set default particle offsetZ
     */
    public final void setParticleOffsetZ(EffectVariable<Double> value) {
        particleOffsetZ=value;

    }
    /**
     * set display range of effect
     * default: 100
     */
    public final void setDisplayRange(double value) {
        displayRange=value;

    }


    private void loadVariables(){
        variables=new ArrayList<>();
        try {
            for (Field field : getClass().getDeclaredFields()) {

                if (!field.isAnnotationPresent(EffectVar.class) ||
                        !field.getType().equals(EffectVariable.class)) continue;

                field.setAccessible(true);
                variables.add(((EffectVariable<?>) field.get(this)));
            }
            for (Field field : getClass().getSuperclass().getDeclaredFields()) {

                if (!field.isAnnotationPresent(EffectVar.class) ||
                        !field.getType().equals(EffectVariable.class)) continue;

                field.setAccessible(true);
                variables.add(((EffectVariable<?>) field.get(this)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public final BaseEffect clone() {
        try {
            BaseEffect baseEffect = (BaseEffect) (super.clone());
            baseEffect.id = UUID.randomUUID().toString();

            if(baseEffect.origin!=null){
                baseEffect.origin=origin.clone();
            }
            if(baseEffect.target!=null){
                baseEffect.target=target.clone();
            }
            cloneVariables(baseEffect);
            onClone(baseEffect);

            baseEffect.loadVariables();
            return baseEffect;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public final BaseEffect cloneWithSameId() {
        BaseEffect baseEffect = clone();
        baseEffect.id = id;
        return baseEffect;
    }
    private void cloneVariables(BaseEffect cloned) throws CloneNotSupportedException{
        cloned.particleType = particleType.clone();
        cloned.particleColor = particleColor.clone();
        cloned.particleMaterial = particleMaterial.clone();
        cloned.particleSpeed = particleSpeed.clone();
        cloned.particleCount = particleCount.clone();
        cloned.particleOffsetX = particleOffsetX.clone();
        cloned.particleOffsetY = particleOffsetY.clone();
        cloned.particleOffsetZ = particleOffsetZ.clone();
        cloned.thickness = thickness.clone();

    }

    protected abstract void onClone(BaseEffect cloned);

}
