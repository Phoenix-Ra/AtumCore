package me.phoenixra.atum.core.utils;

import me.phoenixra.atum.core.config.Config;
import me.phoenixra.atum.core.config.serialization.impl.LocationSerialization;
import me.phoenixra.atum.core.exceptions.NotificationException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationUtils {
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
     * @throws NotificationException if failed to parse string to location
     */
    @NotNull
    public static Location getLocationFromString(@NotNull String preLoc) throws NotificationException{
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
            e.printStackTrace();
            throw new NotificationException("&cFailed to parse string to location! String: "+preLoc);
        }
    }
    /**
     * Get location from Config section
     * Yaw and Pitch are optional
     *
     * @param locationSection config section with location parameters
     * @return the location
     */
    @Nullable
    public static Location getLocationFromConfig(@NotNull Config locationSection)  {
        return LocationSerialization.deserializer().deserializeFromConfig(locationSection);
    }
    /**
     * Parse location to string
     * <p></p>
     * Pattern: world;X;Y;Z;Yaw;Pitch
     *
     * @param location The location
     * @param withCamera Ignores Yaw and Pitch values on false
     * @return the location
     */
    @NotNull
    public static String parseLocationToString(@NotNull Location location, boolean withCamera) {
        boolean b = location.getWorld() == null;
        if (withCamera) {
            return (b ? "" : location.getWorld().getName()+";")
                    +location.getX()+";"
                    +location.getY()+";"
                    +location.getZ()+";"
                    +location.getYaw()+";"
                    +location.getPitch();
        }
        return (b ? "" : location.getWorld().getName()+";")
                +location.getX()+";"
                +location.getY()+";"
                +location.getZ();
    }
    /**
     * Set location in config section
     *
     * @param location The location
     * @param withCamera Ignores Yaw and Pitch values on false
     * @return the config
     */
    @NotNull
    public static Config parseLocationToConfig(@NotNull Location location,
                                               boolean withCamera) {
        return LocationSerialization.serializer(withCamera).serializeToConfig(location);
    }

}
