package me.phoenixra.atum.core.utils;

import me.phoenixra.atum.core.config.Config;
import me.phoenixra.atum.core.exceptions.AtumException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
     * Get location from Config section
     * Yaw and Pitch are optional
     *
     * @param locationSection config section with location parameters
     * @return the location
     * @throws AtumException if failed to parse config section to location
     */
    @NotNull
    public static Location getLocationFromConfig(@NotNull Config locationSection) throws AtumException{
        if(!locationSection.hasPath("world")){
            throw new AtumException("Failed to get location from Configuration Section!" +
                    " 'world' wasn't specified");
        }
        if(!locationSection.hasPath("x")){
            throw new AtumException("Failed to get location from Configuration Section!" +
                    " 'x' wasn't specified");
        }
        if(!locationSection.hasPath("y")){
            throw new AtumException("Failed to get location from Configuration Section!" +
                    " 'y' wasn't specified");
        }
        if(!locationSection.hasPath("z")){
            throw new AtumException("Failed to get location from Configuration Section!" +
                    " 'z' wasn't specified");
        }
        try {
            String worldName = locationSection.getString("world");
            World world = Bukkit.getWorld(worldName);
            if(world==null){
                throw new AtumException("Failed to get location from Configuration Section!" +
                        " world '"+worldName+"' not found");
            }
            double posX = locationSection.getDouble("x");
            double posY = locationSection.getDouble("y");
            double posZ = locationSection.getDouble("z");
            Location loc = new Location(world, posX, posY, posZ);
            loc.setYaw((float) locationSection.getDouble("yaw"));
            loc.setPitch((float) locationSection.getDouble("pitch"));

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
    /**
     * Save location to config section
     *
     * @param config the config section
     * @param location The location
     * @param withCamera Ignores Yaw and Pitch values on false
     * @throws AtumException if world of the location is null
     */
    public static void saveLocationToConfig(@NotNull Config config,
                                            @NotNull Location location,
                                            boolean withCamera) throws AtumException{
        if(location.getWorld()==null) {
            throw new AtumException("The world of the parsing location cannot be NULL");
        }
        config.set("world",location.getWorld().getName());
        config.set("x",location.getX());
        config.set("y",location.getY());
        config.set("z",location.getZ());
        if(withCamera){
            config.set("yaw",location.getYaw());
            config.set("pitch",location.getPitch());
        }
    }

}
