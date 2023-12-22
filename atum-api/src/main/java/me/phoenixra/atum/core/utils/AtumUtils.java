package me.phoenixra.atum.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class AtumUtils {


    public static List<Entity> getNearbyEntities(Location location, EntityType entityType, int radius) {
        ArrayList<Entity> arrayList = new ArrayList<>();
        for (Entity entity : location.getWorld().getEntities()) {
            if (entityType != null && !entity.getType().equals(entityType)) continue;
            if (!isInsideArea(location, entity.getLocation(), radius)) continue;
            arrayList.add(entity);
        }
        return arrayList;
    }

    public static boolean isInsideArea(Location center, Location point, int radius) {
        int x1 = center.getBlockX();
        int z1 = center.getBlockZ();
        int x2 = point.getBlockX();
        int z2 = point.getBlockZ();
        return x2 < x1 + radius && z2 < z1 + radius && x2 > x1 - radius && z2 > z1 - radius;
    }

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

    public static Class<?> getNMSClass(String pack, String name) {
        String className;

        if (getServerVersion() < 17) className = "net.minecraft.server" + getNMSVersion() + name;
        else className = "net.minecraft." + pack + '.' + name;

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new InternalError("Failed to get NMS class " + className + ". Probably, your currently using unsupported server version", e);
        }
    }

    public static int getServerVersion() {
        return Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].split("_")[1]);
    }

    public static String getNMSVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    private AtumUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
