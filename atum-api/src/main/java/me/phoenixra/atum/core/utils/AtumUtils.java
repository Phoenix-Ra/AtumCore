package me.phoenixra.atum.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class AtumUtils {

    //to prevent java reflections usage
    private AtumUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }

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
}
