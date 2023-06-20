package me.phoenixra.atum.core.config.serialization.impl;

import me.phoenixra.atum.core.AtumAPI;
import me.phoenixra.atum.core.config.Config;
import me.phoenixra.atum.core.config.ConfigType;
import me.phoenixra.atum.core.config.serialization.ConfigDeserializer;
import me.phoenixra.atum.core.config.serialization.ConfigSerializer;
import me.phoenixra.atum.core.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ItemSerialization {
    public static ConfigSerializer<ItemStack> serializer(){
        return item -> {
            Config config = AtumAPI.getInstance().createConfig(null, ConfigType.YAML);
            config.set("material", item.getType().name());
            config.set("amount", item.getAmount());
            ItemMeta meta = item.getItemMeta();
            if(meta!=null) {
                config.set("name", meta.getDisplayName());
                config.set("lore", meta.getLore());
            }

            return config;
        };
    }
    public static ConfigDeserializer<ItemStack> deserializer(){
        return config -> {
            Material material = Objects.requireNonNullElse(
                    Material.matchMaterial(config.getString("material")),
                    Material.BEDROCK
            );
            ItemBuilder builder = new ItemBuilder(material);
            builder.setAmount(config.getIntOrDefault("amount",1));
            String name = config.getStringOrNull("name");
            if(name!=null) {
                builder.setDisplayName(name);
            }
            String lore = config.getStringOrNull("lore");
            if(lore!=null) {
                builder.setLore(lore);
            }
            return builder.build();
        };
    }
}
