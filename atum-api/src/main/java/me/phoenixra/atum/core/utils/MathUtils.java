package me.phoenixra.atum.core.utils;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * MathUtils
 * <p></p>
 * Contains fast trigonometry methods
 * and other
 */
public class MathUtils {
    /**
     * Sin lookup table.
     */
    private static final double[] SIN_LOOKUP = new double[65536];
    static public final double degreesToRadians = Math.PI / 180;

    static {

        for (int i = 0; i < 65536; ++i) {
            SIN_LOOKUP[i] = Math.sin((double) i * 3.141592653589793D * 2.0D / 65536.0D);
        }
    }

    /**
     * Get sin from lookup table
     * it is significantly faster that Math#sin()
     *
     * @param radians the radians
     * @return result
     */
    public static double fastSin(final double radians) {
        float f = (float) radians;
        return SIN_LOOKUP[(int) (f * 10430.378F) & '\uffff'];
    }

    /**
     * Get cos from lookup table
     * it is significantly faster that Math#cos()
     *
     * @param radians the radians
     * @return result
     */
    public static double fastCos(final double radians) {
        float f = (float) radians;
        return SIN_LOOKUP[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }

    /**
     * Get tan from lookup table
     * it is significantly faster that Math#tan()
     *
     * @param radians the radians
     * @return result
     */
    public static double fastTan(final double radians) {
        float f = (float) radians;
        return SIN_LOOKUP[(int) (f * 10430.378F) & '\uffff'] /
                SIN_LOOKUP[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }

    /**
     * Bias the input value according to a curve.
     *
     * @param input The input value.
     * @param bias  The bias between -1 and 1, where higher values bias input values to lower output values.
     * @return The biased output.
     */
    public static double bias(final double input,
                              final double bias) {
        double k = Math.pow(1 - bias, 3);

        return (input * k) / (input * k - input + 1);
    }

    /**
     * Get random int inside bounds.
     * Uses ThreadLocalRandom
     *
     * @param min The min bound
     * @param max The max bound
     * @return result
     */
    public static int randInt(final int min,
                              final int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    /**
     * Get random int inside bounds.
     * Uses ThreadLocalRandom
     *
     * @param min The min bound
     * @param max The max bound
     * @return result
     */
    public static double randDouble(final double min,
                                    final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static Vector getYawAsVector(Location loc){
        Vector vector = new Vector();

        double rotX = loc.getYaw();

        double xz = fastCos(Math.toRadians(1));
        vector.setX(-xz * fastSin(Math.toRadians(rotX)));
        vector.setZ(xz * fastCos(Math.toRadians(rotX)));

        return vector;
    }
    public static final Vector rotateVectorByYawPitch(Vector v, float yawDegrees, float pitchDegrees) {
        double yaw = Math.toRadians(-1 * (yawDegrees + 90));
        double pitch = Math.toRadians(-pitchDegrees);

        double cosYaw = fastCos(yaw);
        double cosPitch = fastCos(pitch);
        double sinYaw = fastSin(yaw);
        double sinPitch = fastSin(pitch);

        double initialX, initialY, initialZ;
        double x, y, z;

        // Z_Axis rotation (Pitch)
        initialX = v.getX();
        initialY = v.getY();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        // Y_Axis rotation (Yaw)
        initialZ = v.getZ();
        initialX = x;
        z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;

        v.setX(x);
        v.setY(y);
        v.setZ(z);
        return v;
    }
    public static Vector vectorFromAngle(double angle){
        Vector vector=new Vector(0,0,0);
        vector.setX(fastCos(angle));
        vector.setY(fastSin(angle));
        return vector;
    }
    public static double angleFromVector(Vector vector){
        return Math.atan2(vector.getY(),vector.getX());
    }
    public static double getMagnitude(Vector vector){
        return Math.sqrt(Math.pow(vector.getX(),2)+Math.pow(vector.getY(),2)+Math.pow(vector.getZ(),2));
    }
    public static Vector setMagnitude(Vector vector, double newMagnitude){
        double currentMagnitude=getMagnitude(vector);
        vector.setX(vector.getX()*newMagnitude/currentMagnitude);
        vector.setY(vector.getY()*newMagnitude/currentMagnitude);
        vector.setZ(vector.getZ()*newMagnitude/currentMagnitude);
        return vector;
    }

    public static double getEpsilon() {
        return 1.0E-6;
    }
    public static boolean isNormalized(Vector vector) {
        return Math.abs(vector.lengthSquared() - 1.0) < getEpsilon();
    }

    @NotNull
    public static Vector rotateAroundX(Vector vector, double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double y = angleCos * vector.getY() - angleSin * vector.getZ();
        double z = angleSin * vector.getY() + angleCos * vector.getZ();
        return vector.setY(y).setZ(z);
    }

    @NotNull
    public static Vector rotateAroundY(Vector vector, double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * vector.getX() + angleSin * vector.getZ();
        double z = -angleSin * vector.getX() + angleCos * vector.getZ();
        return vector.setX(x).setZ(z);
    }

    @NotNull
    public static Vector rotateAroundZ(Vector vector, double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * vector.getX() - angleSin * vector.getY();
        double y = angleSin * vector.getX() + angleCos * vector.getY();
        return vector.setX(x).setY(y);
    }

    @NotNull
    public static Vector rotateAroundAxis(Vector vector, @NotNull Vector axis, double angle) throws IllegalArgumentException {
        Preconditions.checkArgument(axis != null, "The provided axis vector was null");
        return rotateAroundNonUnitAxis(vector,isNormalized(axis) ? axis : axis.clone().normalize(), angle);
    }

    @NotNull
    public static Vector rotateAroundNonUnitAxis(Vector vector, @NotNull Vector axis, double angle) throws IllegalArgumentException {
        Preconditions.checkArgument(axis != null, "The provided axis vector was null");
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        double x2 = axis.getX();
        double y2 = axis.getY();
        double z2 = axis.getZ();
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double dotProduct = vector.dot(axis);
        double xPrime = x2 * dotProduct * (1.0 - cosTheta) + x * cosTheta + (-z2 * y + y2 * z) * sinTheta;
        double yPrime = y2 * dotProduct * (1.0 - cosTheta) + y * cosTheta + (z2 * x - x2 * z) * sinTheta;
        double zPrime = z2 * dotProduct * (1.0 - cosTheta) + z * cosTheta + (-y2 * x + x2 * y) * sinTheta;
        return vector.setX(xPrime).setY(yPrime).setZ(zPrime);
    }
    private MathUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
