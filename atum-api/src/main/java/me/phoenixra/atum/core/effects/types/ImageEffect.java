package me.phoenixra.atum.core.effects.types;


import me.phoenixra.atum.core.effects.BaseEffect;
import me.phoenixra.atum.core.effects.interfaces.EffectLocation;
import me.phoenixra.atum.core.effects.interfaces.EffectsManager;
import me.phoenixra.atum.core.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageEffect extends BaseEffect {
    private File imageFile = null;

    private boolean transparency = true;
    private File savePointsFile;

    private float size = (float) 1 / 40;

    private Vector rotation = null;
    private boolean orient = false;
    private Vector angularVelocity = new Vector(0,0,0);

    private int frameRate = 1;
    private int step = 0;
    private int rotationStep = 0;
    private int stepDelay = 0;

    private BufferedImage[] images = null;
    protected ImageLoadCallback imageLoadCallback;

    private boolean initialized;

    /**
     * Displays an image
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
    public ImageEffect(@NotNull EffectsManager effectsManager,
                 @NotNull EffectLocation origin,
                 @NotNull Particle particle,
                 long period,
                 int iterations) {

        super(effectsManager,origin,particle,period,iterations);
    }
    

    @Override
    public void onRun() {
        if(!initialized) {
            if (images == null && imageLoadCallback != null) {
                return;
            }
            if (images == null && imageFile != null) {
                loadImage();
                return;
            }
            if (images == null || images.length == 0) {
                cancel(false);
                return;
            }
            if(savePointsFile!=null) saveImagePoints(images[0]);
            initialized=true;
        }
        Location location = getOrigin().getCurrentLocation();
        if (images.length > 1) {
            if (stepDelay == frameRate) {
                step++;
                stepDelay = 0;
            }
            stepDelay++;
        }
        BufferedImage image = images[step];
        if (size != 1) {
            image = getScaledImage(image, (int) (image.getWidth() * size), (int) (image.getHeight() * size));
        }
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(thickness.getValue());
                if (rotation != null) {
                    MathUtils.rotateAroundX(v,rotation.getX() * MathUtils.degreesToRadians);
                    MathUtils.rotateAroundY(v,rotation.getY() * MathUtils.degreesToRadians);
                    MathUtils.rotateAroundZ(v,rotation.getZ() * MathUtils.degreesToRadians);
                }
                if (orient) {
                    MathUtils.rotateVectorByYawPitch(v, location.getYaw(), location.getPitch());
                }

                if (angularVelocity.getX() != 0) {
                    MathUtils.rotateAroundX(v,angularVelocity.getX() * rotationStep);
                }
                if (angularVelocity.getY() != 0) {
                    MathUtils.rotateAroundY(v,angularVelocity.getX() * rotationStep);
                }
                if (angularVelocity.getZ() != 0) {
                    MathUtils.rotateAroundZ(v,angularVelocity.getZ() * rotationStep);
                }

                int pixel = image.getRGB(x, y);
                if (transparency && (pixel >> 24) == 0) {
                    continue;
                }

                display(v, location, pixel);
            }
        }
        rotationStep++;
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }
    private void display(Vector v, Location location, int pixel) {
        Color color = new Color(pixel);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        displayParticle(getParticleType().getValue(), location.add(v), org.bukkit.Color.fromRGB(r, g, b), 1);
        location.subtract(v);

    }


    private static BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, Transparency.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    private void loadImage() {
        imageLoadCallback = i -> {
            images = i;
            imageLoadCallback = null;
        };
        getEffectsManager().loadImage(imageFile, imageLoadCallback);
    }
    private void saveImagePoints(BufferedImage image) {
        getEffectsManager().getPlugin().getScheduler().runAsync(()-> {
            FileConfiguration f = YamlConfiguration.loadConfiguration(savePointsFile);

            StringBuilder sb = new StringBuilder();
            sb.append(image.getWidth()).append("x").append(image.getHeight()).append("@");
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = image.getRGB(x, y);
                    if (transparency && (pixel >> 24) == 0) {
                        continue;
                    }
                    sb.append(pixel).append(":").append(x).append(",").append(y).append(";");

                }
            }
            f.set("PixelsArray", sb.toString());
            try {
                f.save(savePointsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            savePointsFile=null;
        });
    }




    /**
     * sets the image file
     */
    public void setImageFile(File imageFile){
        this.imageFile = imageFile;
    }
    /**
     * sets how often changes frame.
     * <p></p>
     * <b>This parameter useful only for .gif files</b>
     * <p></p>
     * default: 1
     */
    public void setFrameRate(int value){
        frameRate=value;
    }

    /**
     * sets image resolution multiplier
     * <p></p>
     * default: 1
     * @apiNote Useful if you want to display an image with high resolution
     */
    public void setSize(float value){
        size=value;
    }
    /**
     * sets rotation of an image
     * <p></p>
     * default: 0;0;0
     */
    public void setRotation(Vector value){
        rotation=value;
    }
    /**
     * sets rotation velocity
     * <p></p>
     * default: 0;0;0
     * @apiNote You can make animations using that parameter
     */
    public void setAngularVelocity(Vector value){
        angularVelocity=value;
    }
    /**
     * if false, displays transparent pixels as well
     * <p></p>
     * default: true
     */
    public void setTransparency(boolean value){
        transparency=value;
    }
    /**
     * if true, orients image by yaw and pitch of origin location
     * <p></p>
     * default: false
     */
    public void setOrient(boolean value){
        orient=value;
    }


    public void setSavePointsFile(File file){
        savePointsFile=file;
    }


    public interface ImageLoadCallback {
        void loaded(final BufferedImage[] images);
    }

}
