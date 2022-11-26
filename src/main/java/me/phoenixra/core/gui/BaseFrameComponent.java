package me.phoenixra.core.gui;

import com.cryptomorin.xseries.XMaterial;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.phoenixra.core.ItemBuilder;
import me.phoenixra.core.gui.api.PhoenixFrameComponent;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseFrameComponent extends PhoenixFrameComponent {

    @NotNull
    private ItemStack item;

    @Setter @Accessors(chain = true)
    private InventoryType inventoryType=InventoryType.CHEST;
    private int slot;

    private BaseFrameComponent() {
        item = new ItemStack(Material.STONE);
        slot = 0;
    }

    public BaseFrameComponent(@Nullable String displayName, @Nullable List<String> lore, @NotNull XMaterial material,
                              int slot) {
        this(displayName, lore, material.parseItem(), slot);
    }

    public BaseFrameComponent(@Nullable String displayName, @Nullable List<String> lore, @Nullable ItemStack item,
                              int slot) {
        if (item == null) {
            item = XMaterial.STONE.parseItem();
        }
        this.item = item;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            itemMeta.setLore(lore);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(itemMeta);
        }
        this.slot = slot;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public static class Builder {
        private final BaseFrameComponent component = new BaseFrameComponent();
        private ItemBuilder itemBuilder;

        public Builder(@NotNull ItemBuilder item) {
            itemBuilder = new ItemBuilder(Material.STONE);
        }

        public Builder withSlot(int slot) {
            component.slot = slot;
            return this;
        }
        public Builder withInventoryType(InventoryType inventoryType) {
            component.setInventoryType(inventoryType);
            return this;
        }

        public BaseFrameComponent build() {
            if (itemBuilder.getItem().getItemMeta() != null) {
                component.setItemMeta(itemBuilder.getItem().getItemMeta());
            }
            return component;
        }
    }
}
