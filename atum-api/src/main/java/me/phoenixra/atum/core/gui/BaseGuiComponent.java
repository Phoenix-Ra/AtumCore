package me.phoenixra.atum.core.gui;

import me.phoenixra.atum.core.utils.ItemBuilder;
import me.phoenixra.atum.core.gui.api.GuiComponent;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseGuiComponent extends GuiComponent {

    @NotNull
    private ItemStack item;

    @Setter @Accessors(chain = true)
    private InventoryType inventoryType=InventoryType.CHEST;
    private int slot;

    private BaseGuiComponent() {
        item = new ItemStack(Material.STONE);
        slot = 0;
    }

    public BaseGuiComponent(@Nullable String displayName, @Nullable List<String> lore, @NotNull Material material,
                            int slot) {
        this(displayName, lore, new ItemStack(material), slot);
    }

    public BaseGuiComponent(@Nullable String displayName, @Nullable List<String> lore, @Nullable ItemStack item,
                            int slot) {
        if (item == null) {
            item = new ItemStack(Material.STONE);
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
        private final BaseGuiComponent component = new BaseGuiComponent();
        private final ItemBuilder itemBuilder;

        public Builder(@NotNull ItemBuilder item) {
            itemBuilder = item;
        }

        public Builder withSlot(int slot) {
            component.slot = slot;
            return this;
        }
        public Builder withInventoryType(InventoryType inventoryType) {
            component.setInventoryType(inventoryType);
            return this;
        }

        public BaseGuiComponent build() {
            component.item=itemBuilder.getItem();
            return component;
        }
    }
}
