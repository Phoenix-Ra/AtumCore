package me.phoenixra.atum.core.config.serialization.impl;

import me.phoenixra.atum.core.AtumAPI;
import me.phoenixra.atum.core.config.Config;
import me.phoenixra.atum.core.config.ConfigType;
import me.phoenixra.atum.core.config.serialization.ConfigDeserializer;
import me.phoenixra.atum.core.config.serialization.ConfigSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerialization {

    public static ConfigSerializer<Location> serializer(boolean withCamera){
        return obj -> {
            Config config = AtumAPI.getInstance().createConfig(null, ConfigType.YAML);
            if(obj.getWorld() != null) {
                config.set("world", obj.getWorld().getName());
            }
            config.set("x", obj.getX());
            config.set("y", obj.getY());
            config.set("z", obj.getZ());
            if(withCamera) {
                config.set("yaw", obj.getYaw());
                config.set("pitch", obj.getPitch());
            }
            return config;
        };
    }
    public static ConfigDeserializer<Location> deserializer(){
        return obj -> {
            String world = obj.getStringOrNull("world");

            return new Location(
                    world == null ? null : Bukkit.getServer().getWorld(world),
                    obj.getDouble("x"),
                    obj.getDouble("y"),
                    obj.getDouble("z"),
                    obj.getInt("yaw"),
                    obj.getInt("pitch")
            );
        };
    }
}
