package me.phoenixra.atum.core.utils.effects;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class MathUtils {
    static public final double degreesToRadians = Math.PI / 180;

    
    public static Color[][] sortImageArray(String data){
        int imageWidth= Integer.parseInt(data.split("@")[0].split("x")[0]);
        int imageHeight= Integer.parseInt(data.split("@")[0].split("x")[1]);
        Color[][] out = new Color[imageWidth][imageHeight];
        for(String s : data.split(";")){
            int x=Integer.parseInt(s.split(":")[1].split(",")[0]);
            int y=Integer.parseInt(s.split(":")[1].split(",")[1]);
            java.awt.Color color;
            if(s.contains("@")) {
                color = new java.awt.Color(Integer.parseInt(s.split("@")[1].split(":")[0]));
            }else color = new java.awt.Color(Integer.parseInt(s.split(":")[0]));

            out[x][y] = Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
        }
        return out;
    }
    public static Location getCentralLocation(Location ... locs){
        World world=locs[0].getWorld();
        double x= 0,y = 0,z = 0;
        int dots=0;
        for(Location loc : locs){
            if(loc.getWorld()!=world) continue;
            x+=loc.getX();
            y+=loc.getY();
            z+=loc.getZ();
            dots++;
        }
        return new Location(world,x/dots,y/dots,z/dots);

    }

    public static Vector getYawAsVector(Location loc){
        Vector vector = new Vector();

        double rotX = loc.getYaw();

        double xz = Math.cos(Math.toRadians(1));
        vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
        vector.setZ(xz * Math.cos(Math.toRadians(rotX)));

        return vector;
    }
    public static final Vector rotateVectorByYawPitch(Vector v, float yawDegrees, float pitchDegrees) {
        double yaw = Math.toRadians(-1 * (yawDegrees + 90));
        double pitch = Math.toRadians(-pitchDegrees);

        double cosYaw = Math.cos(yaw);
        double cosPitch = Math.cos(pitch);
        double sinYaw = Math.sin(yaw);
        double sinPitch = Math.sin(pitch);

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
        vector.setX(Math.cos(angle));
        vector.setY(Math.sin(angle));
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
}
