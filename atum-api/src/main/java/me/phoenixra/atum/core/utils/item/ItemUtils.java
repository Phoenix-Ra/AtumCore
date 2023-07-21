package me.phoenixra.atum.core.utils.item;

import me.phoenixra.atum.core.config.Config;
import me.phoenixra.atum.core.config.serialization.impl.ItemSerialization;
import me.phoenixra.atum.core.utils.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemUtils {

    /**
     * Deserializes the item from config
     *
     * @param config The config section
     * @return The serialized item
     */
    public static ItemStack getFromConfig(Config config) {
        return ItemSerialization.deserializer().deserializeFromConfig(config);
    }

    /**
     * Serializes the item to a config
     *
     * @param item The item to serialize
     * @return The config section
     */
    public static Config parseToConfig(ItemStack item) {
        return ItemSerialization.serializer().serializeToConfig(item);
    }

    /**
     * Serializes the item to a base64
     *
     * @param item The item to serialize
     * @return The serialized item or null on error
     */
    @Nullable
    public static String serializeItemToBase64(@NotNull ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item.serialize());
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deserializes the item from a base64
     *
     * @param data The base64 value
     * @return The item or null on error
     */
    @Nullable
    public static ItemStack deserializeItemFromBase64(@NotNull String data) {
        if(StringUtils.isBlank(data)) return null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = ItemStack.deserialize((Map<String, Object>) dataInput.readObject());
            dataInput.close();
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Serializes the item array to a base64
     *
     * @param items The item array to serialize
     * @return The serialized item array or null on error
     */
    @Nullable
    public static String parseItemArrayToBase64(@NotNull List<ItemStack> items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.size());

            for (ItemStack item : items) {
                if (item != null) {
                    dataOutput.writeObject(item.serialize());
                } else {
                    dataOutput.writeObject(null);
                }
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deserializes the item array from a base64
     *
     * @param data The base64 value
     * @return The item array or null on error
     */
    @Nullable
    public static List<ItemStack> getItemArrayFromBase64(@NotNull String data) {
        if(StringUtils.isBlank(data)) return null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int Index = 0; Index < items.length; Index++) {
                Map<String, Object> stack = (Map<String, Object>) dataInput.readObject();

                if (stack != null) {
                    items[Index] = ItemStack.deserialize(stack);
                } else {
                    items[Index] = null;
                }
            }

            dataInput.close();
            return Arrays.stream(items).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ItemUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

}
