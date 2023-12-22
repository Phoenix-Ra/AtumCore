package me.phoenixra.atum.core.config.serialization.impl;

import me.phoenixra.atum.core.AtumAPI;
import me.phoenixra.atum.core.config.Config;
import me.phoenixra.atum.core.config.ConfigType;
import me.phoenixra.atum.core.config.serialization.ConfigDeserializer;
import me.phoenixra.atum.core.config.serialization.ConfigSerializer;
import me.phoenixra.atum.core.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerialization {

    public static ConfigSerializer<Location> serializer(boolean withCamera){
        return location -> {
            Config config = AtumAPI.getInstance().createConfig(null, ConfigType.YAML);

            if(location.getWorld() != null) {
                config.set("world", location.getWorld().getName());
            }
            config.set("x", location.getX());
            config.set("y", location.getY());
            config.set("z", location.getZ());
            if(withCamera) {
                config.set("yaw", location.getYaw());
                config.set("pitch", location.getPitch());
            }
            return config;
        };
    }
    public static ConfigDeserializer<Location> deserializer(){
        return conf -> {
            String world = conf.getStringOrNull("world");

            return new Location(
                    world == null ? null : Bukkit.getServer().getWorld(world),
                    conf.getDouble("x"),
                    conf.getDouble("y"),
                    conf.getDouble("z"),
                    conf.getInt("yaw"),
                    conf.getInt("pitch")
            );
        };
    }
}
