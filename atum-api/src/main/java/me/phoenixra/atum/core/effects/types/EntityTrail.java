package me.phoenixra.atum.core.effects.types;

import me.phoenixra.atum.core.effects.BaseEffect;
import me.phoenixra.atum.core.effects.interfaces.EffectLocation;
import me.phoenixra.atum.core.effects.interfaces.EffectsManager;
import me.phoenixra.atum.core.effects.libs.OpenSimplex2S;
import me.phoenixra.atum.core.utils.MathUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class EntityTrail extends BaseEffect {
    private Particle particleRotating= Particle.REDSTONE;
    private Color particleRotatingColor= Color.RED;
    private double particleRotatingRadius=0.5;
    private double particleRotatingSpeed=0.1;

    private boolean rotatingParticlesEnabled=false;
    private boolean particleRotatingNoise=false;
    private boolean particleNoise=false;
    private int ticksPassed;


    /**
     * Displays an Entity trail
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
    public EntityTrail(@NotNull EffectsManager effectsManager,
                         @NotNull EffectLocation origin,
                         @NotNull Particle particle,
                         long period,
                         int iterations) {

        super(effectsManager,origin,particle,period,iterations);
    }

    @Override
    public void onRun() {
        Entity entity = getOrigin().getTargetEntity();
        if(entity==null){
            cancel(false);
            return;
        }
        Location loc = entity.getLocation().subtract(entity.getVelocity());
        int particles = (int)(loc.distance(entity.getLocation())/thickness.getValue());

        if(particles==0){
            float noise=0;
            if(particleNoise||particleRotatingNoise) {
                noise = OpenSimplex2S.noise3_ImproveXZ(0, 0, entity.getLocation().getY(), particleRotatingRadius);
            }
            displayParticle(getParticleType().getValue(),particleNoise?entity.getLocation().add(entity.getLocation().toVector().normalize().multiply(noise)):entity.getLocation());
            if(rotatingParticlesEnabled) drawRotatingParticles(entity.getLocation()
                    ,entity.getVelocity().normalize(),noise);

        }else {
            //here I can't just save particles amount, because Velocity is not a constant value.
            Vector step =  new Vector(entity.getVelocity().getX()/particles,entity.getVelocity().getY()/particles,entity.getVelocity().getZ()/particles);
            float noise=0;
            for(int i=0; i<particles; i++){
                if(particleNoise||particleRotatingNoise) {
                    noise = OpenSimplex2S.noise3_ImproveXZ(0, 0, loc.getY(), particleRotatingRadius);
                }
                loc.add(step);
                displayParticle(getParticleType().getValue(),particleNoise?loc.add(loc.toVector().normalize().multiply(noise)):loc);
                if(rotatingParticlesEnabled) drawRotatingParticles(loc
                        ,entity.getVelocity().normalize(),noise);
            }
        }
        ticksPassed++;


    }



    private void drawRotatingParticles(Location start, Vector velocity, float noise) {
        double radius= particleRotatingNoise?noise:1*particleRotatingRadius;
        Vector v = new Vector();

        double step=Math.PI*2*particleRotatingSpeed*ticksPassed;

        v.setX(radius * MathUtils.fastSin(step));
        v.setZ(radius * MathUtils.fastCos(step));
        //effect angle
        v.add(velocity);
        displayParticle(particleRotating,start.add(v),particleRotatingColor,1);
        start.subtract(v);

        v.setX(velocity.getX()+radius * MathUtils.fastSin(step-Math.PI));
        v.setZ(velocity.getZ()+radius * MathUtils.fastCos(step-Math.PI));
        displayParticle(particleRotating,start.add(v),particleRotatingColor,1);
        start.subtract(v);

    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }

    /**
     * set whether noise of central particles is activated
     */
    public void setParticleNoise(boolean value){
        this.particleNoise=value;
    }

    /**
     * set whether rotating particles is activated
     */
    public void setParticleRotatingState(boolean value){
        this.rotatingParticlesEnabled=value;
    }
    /**
     * set particle type of rotating particles
     */
    public void setParticleRotatingType(Particle particle){
        this.particleRotating=particle;
    }
    /**
     * set particle color of rotating particles
     */
    public void setParticleRotatingColor(Color value){
        this.particleRotatingColor=value;
    }
    /**
     * set radius of rotating particles
     */
    public void setParticleRotatingRadius(double value){
        this.particleRotatingRadius=value;
    }
    /**
     * set rotation speed of rotating particles
     */
    public void setParticleRotatingSpeed(double value){
        this.particleRotatingSpeed=value;
    }/**
     * set whether noise of radius of rotating particles is activated
     */
    public void setParticleRotatingNoise(boolean value){
        this.particleRotatingNoise=value;
    }

}
