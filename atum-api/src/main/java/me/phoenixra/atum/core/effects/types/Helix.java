package me.phoenixra.atum.core.effects.types;


import me.phoenixra.atum.core.effects.BaseEffect;
import me.phoenixra.atum.core.effects.interfaces.EffectLocation;
import me.phoenixra.atum.core.effects.interfaces.EffectsManager;
import me.phoenixra.atum.core.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Helix extends BaseEffect {


    private double radius = 0.5;


    private int particle_draw_per_tick=4;
    private int current_draw_step;
    private double radiusFunctionIncrementer=0.5;
    private Vector rotation;


    private int particles;
    private double step;
    private double stepY;
    private boolean initialized=false;

    /**
     * Displays helix effect
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
     *     <li>Standard: <b>thickness:</b> 0.06; <b>radius:</b> 0.8; <b>radiusFunctionIncrementer:</b> 0.5; <b>ParticleDrawPerTick:</b> 4</li>
     *     <li>2D Helix: <b>thickness:</b> 0.06; <b>radius:</b> 0.8; <b>radiusFunctionIncrementer:</b> 0.5; <b>ParticleDrawPerTick:</b> 4; <b>rotationX:</b> 0; <b>rotationZ:</b> 90</li>
     * </ul>
     */
    public Helix(@NotNull EffectsManager effectsManager,
                 @NotNull EffectLocation origin,
                 @NotNull Particle particle,
                 long period,
                 int iterations) {

        super(effectsManager,origin,particle,period,iterations);
    }

    @Override
    public void onRun() {
        if(getTarget()==null) {
            cancel(false);
            return;
        }
        if(particle_draw_per_tick*(current_draw_step-1)>=particles){
            cancel(true);
            return;
        }
        Location startLocation = getOrigin().updateLocation();
        Location endLocation = getTarget().updateLocation();

        if(!initialized) {
            particles = (int) (Math.PI * radius * 2 / thickness.getValue());
            step=Math.PI*2/particles;
            stepY=startLocation.distance(endLocation)/particles;
            initialized=true;
        }
        draw(startLocation);
        current_draw_step++;
    }


    private void draw(Location start) {
        Vector v = new Vector();
        double radius;
        int i_step=current_draw_step*particle_draw_per_tick;
        for (int i = 0; i < particle_draw_per_tick; i++) {
            radius=this.radius*MathUtils.fastSin(Math.PI*i_step/particles+radiusFunctionIncrementer);
            v.setX(radius*MathUtils.fastSin(step*i_step));
            v.setZ(radius*MathUtils.fastCos(step*i_step));
            v.setY(i_step*stepY);
            if(rotation!=null){
                v.rotateAroundX(rotation.getX() *  MathUtils.degreesToRadians);
                v.rotateAroundY(rotation.getY() *  MathUtils.degreesToRadians);
                v.rotateAroundZ(rotation.getZ() *  MathUtils.degreesToRadians);
            }
            displayParticle(getParticleType().getValue(), start.add(v));
            start.subtract(v);

            v.setX(radius*MathUtils.fastSin(step*i_step-Math.PI));
            v.setZ(radius*MathUtils.fastCos(step*i_step-Math.PI));
            v.setY(i_step*stepY);
            if(rotation!=null){
                v.rotateAroundX(rotation.getX() *  MathUtils.degreesToRadians);
                v.rotateAroundY(rotation.getY() *  MathUtils.degreesToRadians);
                v.rotateAroundZ(rotation.getZ() *  MathUtils.degreesToRadians);
            }
            displayParticle(getParticleType().getValue(), start.add(v));
            start.subtract(v);
            i_step++;
        }

    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }



    /**
     * sets starting radius of helix
     */
    public void setRadius(double value) {
        radius = value;
    }

    /**
     * sets amount of particles that will be drawn per onTick() call
     */
    public void setParticleDrawPerTick(int value) {
        particle_draw_per_tick = value;
    }

    /**
     * radius changes like sinusoid function.
     * You can use this method to add value into sin(x + c) formula. x is default formula, c is that incrementer
     */
    public void setRadiusFunctionIncrementer(double value) {
        radiusFunctionIncrementer = value;
    }

    /**
     * helix rotation
     */
    public void setRotation(Vector value) {
        rotation = value;
    }
}
