package me.phoenixra.atum.core.utils;

import me.phoenixra.atum.core.exceptions.AtumException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class LocationUtils {
    //to prevent java reflections usage
    private LocationUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }

    /**
     * Get location from string
     * <p></p>
     * Pattern: world;X;Y;Z;Yaw;Pitch
     * <p></p>
     * Yaw and Pitch are optional
     *
     * @param preLoc The string to parse
     * @return the location
     * @throws AtumException if failed to parse string to location
     */
    @NotNull
    public static Location getLocationFromString(@NotNull String preLoc) throws AtumException{
        try {
            String[] setups = preLoc.split(";");
            double posX = Double.parseDouble(setups[1]);
            double posY = Double.parseDouble(setups[2]);
            double posZ = Double.parseDouble(setups[3]);
            Location loc = new Location(Bukkit.getWorld(setups[0]), posX, posY, posZ);
            if (setups.length > 4) {
                loc.setYaw(Float.parseFloat(setups[4]));
            }
            if (setups.length > 5) {
                loc.setPitch(Float.parseFloat(setups[5]));
            }

            return loc;
        }catch (Exception e){
            throw new AtumException(e);
        }
    }
    /**
     * Parse location to string
     * <p></p>
     * Pattern: world;X;Y;Z;Yaw;Pitch
     *
     * @param location The location
     * @param withCamera Ignores Yaw and Pitch values on false
     * @return the location
     * @throws AtumException if world of the location is null
     */
    @NotNull
    public static String parseLocationToString(@NotNull Location location, boolean withCamera) throws AtumException{
        if(location.getWorld()==null) {
            throw new AtumException("The world of the parsing location cannot be NULL");
        }
        if (withCamera) {
            return location.getWorld().getName()+";"
                    +location.getX()+";"
                    +location.getY()+";"
                    +location.getZ()+";"
                    +location.getYaw()+";"
                    +location.getPitch();
        }
        return location.getWorld().getName()+";"
                +location.getX()+";"
                +location.getY()+";"
                +location.getZ();
    }
}
