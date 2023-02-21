package me.phoenixra.atum.core.effects.types;

import me.phoenixra.atum.core.effects.BaseEffect;
import me.phoenixra.atum.core.effects.interfaces.EffectLocation;
import me.phoenixra.atum.core.effects.interfaces.EffectsManager;
import me.phoenixra.atum.core.utils.effects.MathUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class SuperShape2D extends BaseEffect {
    private Particle particle;

    private double n1 = 0.3;
    private double n2 = 0.3;
    private double n3 = 0.3;
    private double amountOfAngles = 3;
    private double scaleA = 1;
    private double scaleB = 1;
    private double PiMultiply = 2;

    private double radius = 0.5;
    private double radiusAdd = 0;
    private double radiusMultiply = 1;

    private Vector rotation;


    private boolean drawAnimated=false;
    private int drawStepsPerTick=1;
    private int brushesAmount=1;
    private int currentDrawStep;

    private int particles;
    private double step;
    private boolean initialized;

    /**
     * Displays a 2D Shape via superShape formula
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
     */
    public SuperShape2D(@NotNull EffectsManager effectsManager,
                 @NotNull EffectLocation origin,
                 @NotNull Particle particle,
                 long period,
                 int iterations) {

        super(effectsManager,origin,particle,period,iterations);
    }
    @Override
    public void onRun() {
        if(!initialized){
            radius = (radius*radiusMultiply)+radiusAdd;
            particles = (int) (Math.PI * radius * PiMultiply / thickness.getValue());
            double step=Math.PI*PiMultiply/particles;
        }
        Location location = getOrigin().getCurrentLocation();

        if(radiusMultiply!=1.0||radiusAdd!=0 || PiMultiply!=1.0) {
            radius = (radius * radiusMultiply) + radiusAdd;
            particles = (int) (Math.PI * radius * PiMultiply / thickness.getValue());

            step = Math.PI * PiMultiply / particles;
        }

        Vector vector=new Vector();
        if(drawAnimated) {
            int brushStep=particles/brushesAmount;
            for(int brush = 0; brush<brushesAmount;brush++){
                for (int i = 0; i < drawStepsPerTick&&particles>currentDrawStep; i++) {
                    double formula = superShape(step * (currentDrawStep+brushStep*brush)) * radius;
                    vector.setX(formula * Math.sin(step * (currentDrawStep+brushStep*brush)));
                    vector.setZ(formula * Math.cos(step * (currentDrawStep+brushStep*brush)));
                    vector.setY(1);
                    if (rotation != null) {
                        vector.rotateAroundX(rotation.getX() * MathUtils.degreesToRadians);
                        vector.rotateAroundY(rotation.getY() * MathUtils.degreesToRadians);
                        vector.rotateAroundZ(rotation.getZ() * MathUtils.degreesToRadians);
                    }
                    displayParticle(particle, location.add(vector));
                    location.subtract(vector);
                    currentDrawStep++;
                }
            }
            if(particles<=currentDrawStep) {
                finish();
            }
        }else {
            for (int i = 0; i < particles; i++) {
                double formula = superShape(step * i) * radius;
                vector.setX(formula * Math.sin(step * i));
                vector.setZ(formula * Math.cos(step * i));
                vector.setY(1);
                if (rotation != null) {
                    vector.rotateAroundX(rotation.getX() * MathUtils.degreesToRadians);
                    vector.rotateAroundY(rotation.getY() * MathUtils.degreesToRadians);
                    vector.rotateAroundZ(rotation.getZ() * MathUtils.degreesToRadians);
                }
                displayParticle(particle, location.add(vector));
                location.subtract(vector);
            }
        }
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }

    private double superShape(double theta){
        double part1 = (1 / scaleA) * Math.cos((theta * amountOfAngles) / 4);
        part1 = Math.abs(part1);
        part1 = Math.pow(part1, n2);

        double part2 = (1 / scaleB) * Math.sin((theta * amountOfAngles) / 4);
        part2 = Math.abs(part2);
        part2 = Math.pow(part2, n3);

        double part3 = Math.pow(part1 + part2, 1 / n1);

        if (part3 == 0) {
            return 0;
        }

        return 1 / part3;
    }

    public void setRotation(Vector value) {
        rotation = value;
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
     * sets Pi multiplier(that affects on the ending draw Pi value )
     */
    public void setPiMultiply(double value) {
        PiMultiply = value;
    }

    /**
     * sets n1 formula value
     */
    public void setN1(double value) {
        n1 = value;
    }
    /**
     * sets n2 formula value
     */
    public void setN2(double value) {
        n2 = value;
    }
    /**
     * sets n3 formula value
     */
    public void setN3(double value) {
        n3 = value;
    }

    /**
     * sets m formula value (affects on amount of angles)
     */
    public void setAmountOfAngles(double value) {
        amountOfAngles = value;
    }

    /**
     * sets a formula value (affects on scale)
     */
    public void setScaleA(double value) {
        scaleA = value;
    }

    /**
     * sets b formula value (affects on scale)
     */
    public void setScaleB(double value) {
        scaleB = value;
    }

    public void setDrawAnimated(boolean value) {
        drawAnimated = value;
    }
    public void setDrawStepsPerTick(int value) {
        drawStepsPerTick = value;
    }
    public void setBrushesAmount(int value) {
        brushesAmount = value;
    }

}
