package me.phoenixra.atum.core.utils.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.phoenixra.atum.core.tuples.Pair;
import me.phoenixra.atum.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public final class ItemBuilder {
    @NotNull
    private ItemStack itemStack;

    public ItemBuilder() {
        this.itemStack = new ItemStack(Material.AIR);
    }

    public ItemBuilder(@NotNull Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Set type
     *
     * @param material the material
     * @return This builder
     */
    @NotNull
    public final ItemBuilder setType(@NotNull Material material) {
        this.itemStack.setType(material);
        return this;
    }

    /**
     * Set Amount
     *
     * @param amount the amount of item
     * @return This builder
     */
    @NotNull
    public final ItemBuilder setAmount(int amount) {
        getItem().setAmount(amount);
        return this;
    }

    /**
     * Make the item unbreakable
     *
     * @return This builder
     */
    @NotNull
    public final ItemBuilder makeUnbreakable() {
        ItemMeta itemMeta = getItem().getItemMeta();
        itemMeta.setUnbreakable(true);
        getItem().setItemMeta(itemMeta);
        return this;
    }



    /**
     * Set Custom Owner head
     *
     * @param value the player name
     * @return This builder
     */
    @NotNull
    public final ItemBuilder setOwner(@NotNull String value) {
        getItem().setType(Objects.requireNonNull(Material.SKULL_ITEM));
        SkullMeta skullMeta = (SkullMeta) getItem().getItemMeta();
        skullMeta.setOwner(value);
        getItem().setItemMeta(skullMeta);
        return this;
    }

    /**
     * Set Custom Owner head
     *
     * @param value the looong string value of a custom head
     * @return This builder
     */
    @NotNull
    public final ItemBuilder setCustomOwner(@NotNull String value) {
        getItem().setType(Objects.requireNonNull(Material.SKULL_ITEM));
        SkullMeta skullMeta = (SkullMeta) getItem().getItemMeta();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));
        try {
            Field field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullMeta, gameProfile);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        getItem().setItemMeta(skullMeta);
        return this;
    }

    /**
     * Set Leather Color
     * <p></p>
     * Use it only with a Leather Armor
     *
     * @param color the color
     * @return This builder
     */
    @NotNull
    public final ItemBuilder setLeatherColor(@NotNull Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) getItem().getItemMeta();
        leatherArmorMeta.setColor(color);
        getItem().setItemMeta(leatherArmorMeta);
        return this;
    }
    public final ItemBuilder setDurability(int durability) {
        getItem().setDurability((short) durability);
        return this;
    }

    /**
     * Set display name of an item
     *
     * @param displayName The displayName
     * @return This builder
     */
    @NotNull
    public final ItemBuilder setDisplayName(@Nullable String displayName) {
        ItemMeta itemMeta = getItem().getItemMeta();
        if (displayName != null) {
            itemMeta.setDisplayName(StringUtils.format(displayName));
        } else {
            itemMeta.setDisplayName(null);
        }
        getItem().setItemMeta(itemMeta);
        return this;
    }

    /**
     * Set the lore of an item
     * <p></p>
     * This method formats the list as well
     *
     * @param lore the lore list
     * @return This builder
     */
    @NotNull
    public final ItemBuilder setLore(@Nullable List<String> lore) {
        ItemMeta itemMeta = getItem().getItemMeta();
        if (lore != null) {
            ArrayList<String> arrayList = new ArrayList<>();

            for (String string : lore) {
                arrayList.add(StringUtils.format(string));
            }
            itemMeta.setLore(arrayList);
        } else {
            itemMeta.setLore(null);
        }
        getItem().setItemMeta(itemMeta);
        return this;
    }

    /**
     * Set the lore of an item
     * <p></p>
     * This method formats the input as well
     *
     * @param input string values
     * @return This builder
     */
    @NotNull
    public final ItemBuilder setLore(@Nullable String... input) {
        ItemMeta itemMeta = getItem().getItemMeta();
        if (input != null) {
            List<String> list = new ArrayList<>();
            for (String string : input) {
                if (string.contains("\n"))
                    list.addAll(StringUtils.format(Arrays.asList(string.split("\n"))));
                else
                    list.add(StringUtils.format(string));
            }
            itemMeta.setLore(list);
        } else {
            itemMeta.setLore(null);
        }
        getItem().setItemMeta(itemMeta);
        return this;
    }

    /**
     * add item flags
     *
     * @param input flags
     * @return This builder
     */
    @NotNull
    public final ItemBuilder addItemFlags(ItemFlag... input) {
        ItemMeta itemMeta = getItem().getItemMeta();
        itemMeta.addItemFlags(input);
        getItem().setItemMeta(itemMeta);
        return this;
    }

    /**
     * Adds all ItemFlags, to hide everything
     *
     * @return This builder
     */
    @NotNull
    public final ItemBuilder hideAllAttributes() {
        ItemMeta itemMeta = getItem().getItemMeta();
        for (ItemFlag flag : ItemFlag.values()) {
            itemMeta.addItemFlags(flag);
        }
        getItem().setItemMeta(itemMeta);
        return this;
    }

    /**
     * Add enchant
     *
     * @param enchantment the enchantment type
     * @param level the enchantment level
     * @return This builder
     */
    @NotNull
    public final ItemBuilder addEnchant(@NotNull Enchantment enchantment, int level) {
        getItem().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Adds LUCK enchant with flag HIDE_ENCHANTS
     *<p></p>
     * Useful method for Gui menus
     *
     * @return This builder
     */
    @NotNull
    public final ItemBuilder addFakeEnchant() {
        getItem().addUnsafeEnchantment(Enchantment.LUCK, 1);
        addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Clear custom Item Meta settings
     *
     * @return This builder
     */
    @NotNull
    public final ItemBuilder clearItemMeta(){
        getItem().setItemMeta(Bukkit.getItemFactory().getItemMeta(getItem().getType()));
        return this;
    }

    /**
     * Get the current item
     *
     * @return The item
     */
    @NotNull
    public ItemStack getItem() {
        return this.itemStack;
    }

    /**
     * builds the itemStack
     *
     * @return The item
     */
    @NotNull
    public ItemStack build() {
        return this.itemStack.clone();
    }
}
