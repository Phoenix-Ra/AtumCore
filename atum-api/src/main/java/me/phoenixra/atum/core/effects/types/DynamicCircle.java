package me.phoenixra.atum.core.effects.types;

import me.phoenixra.atum.core.effects.BaseEffect;
import me.phoenixra.atum.core.effects.interfaces.EffectLocation;
import me.phoenixra.atum.core.effects.interfaces.EffectsManager;
import me.phoenixra.atum.core.effects.libs.OpenSimplex2S;
import me.phoenixra.atum.core.utils.effects.MathUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class DynamicCircle extends BaseEffect {

    private double radius = 0.5;

    private double radiusAdd = 0;
    private double radiusMultiply = 1;

    private double YAdd = 0;
    private double currentY=0;

    private double noiseAmplitudeAdd = 0;
    private double noiseAmplitudeMultiply = 1;
    private double noisePointMultiplier=0.5;
    private double noiseSpeed=0.05;


    private Vector rotation = null;


    private boolean noise=false;
    private double timer=0;
    /**
     * Displays a Dynamic Circle
     *
     * @param effectsManager <i>Sets manager where effect runnable will be saved<i/>
     *                       <p><p/>
     * @param period         <i>Sets update rate (in Ticks)<i/>
     *                       <p><p/>
     * @param iterations     <i>Sets amount of run() calls.<i/>
     *                       <p></p>
     *                       <i>'-1' -> uses runTask() than runTaskTimer()<i/>
     *                       <p></p>
     *                       <i>'-2' -> infinite, can be stopped only manually.<i/>
     * @apiNote parameters examples:
     * <ul>
     *     <li>Explosion: <b>thickness:</b> 0.1; <b>radius:</b> 0.3; <b>radiusMultiplier:</b> 1.3; <b>iterations:</b> 10</li>
     *     <li>Cool effect: <b>thickness:</b> 0.1; <b>radius:</b> 0.3; <b>radiusMultiplier:</b> 1.15; <b>iterations:</b> 30; <b>noisePointMultiplier:</b> 0.2; <b>repeats:</b> -1; <b>repeatDelay:</b> -1</li>
     * </ul>
     */
    public DynamicCircle(@NotNull EffectsManager effectsManager,
                         @NotNull EffectLocation origin,
                         @NotNull Particle particle,
                         long period,
                         int iterations) {

        super(effectsManager,origin,particle,period,iterations);
    }

    @Override
    public void onRun() {
        Location location = getOrigin().updateLocation();

        currentY+=YAdd;
        location.add(0,currentY,0);

        radius = (radius*radiusMultiply)+radiusAdd;
        int particles = (int) (Math.PI * radius * 2 / thickness.getValue());
        double step=Math.PI*2/particles;
        Vector vector=new Vector();
        for (int i = 0; i < particles; i++) {
            vector.setX(radius*Math.sin(step*i));
            vector.setZ(radius*Math.cos(step*i));
            //set to 0, to make rotation weird:D
            vector.setY(1);
            if(noise) {
                double noise = noiseAmplitudeMultiply* OpenSimplex2S.noise3_ImproveXZ(0, vector.getX()*noisePointMultiplier, timer,vector.getZ()*noisePointMultiplier) + noiseAmplitudeAdd;
                vector.add(vector.clone().normalize().multiply(noise));
            }
            if (rotation != null) {
                vector.rotateAroundX(rotation.getX() *  MathUtils.degreesToRadians);
                vector.rotateAroundY(rotation.getY() *  MathUtils.degreesToRadians);
                vector.rotateAroundZ(rotation.getZ() *  MathUtils.degreesToRadians);
            }
            displayParticle(getParticleType().getValue(), location.add(vector),getParticleColor().getValue(),1);
            location.subtract(vector);
        }
        if(noise) timer+=noiseSpeed;
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }



    /**
     * sets starting radius of a sphere
     */
    public void setRadius(double value) {
        radius = value;
    }

    /**
     * sets multiplier of radius for every onTick() call
     */
    public void setRadiusMultiplier(double value) {
        radiusMultiply = value;
    }

    /**
     * sets incrementer of a radius for every onTick() call (increments after multiplying)
     */
    public void setRadiusIncrementer(double value) {
        radiusAdd = value;
    }

    /**
     * sets incrementer of a height for every onTick() call
     */
    public void setHeightIncrementer(double value) {
        YAdd = value;
    }


    /**
     * sets multiplier of noise amplitude. formula: (noise*multiplier)+incrementer
     */
    public void setNoiseAmplitudeMultiply(double value) {
        noiseAmplitudeMultiply = value;
    }

    /**
     * sets adder of noise amplitude. formula: (noise*multiplier)+adder
     */
    public void setNoiseAmplitudeAdder(double value) {
        noiseAmplitudeAdd = value;
    }

    /**
     * that setting affects work of noise. Less value - smoother noise effect will be.  Default: 0.5
     */
    public void setNoisePointMultiplier(double value) {
        noisePointMultiplier = value;
    }

    public void setNoiseSpeed(double value) {
        noiseSpeed = value;
    }


    /**
     * sets rotation of a circle
     * <p></p>
     * default: 0;0;0
     */
    public void setRotation(Vector value){
        rotation=value;
    }

    public void setNoiseEnabled(boolean value){
        noise=value;
    }
}
