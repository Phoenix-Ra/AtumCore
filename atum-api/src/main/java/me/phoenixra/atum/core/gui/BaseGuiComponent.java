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
        public Builder withSlot(int slot,InventoryType inventoryType) {
            component.slot = slot;
            component.setInventoryType(inventoryType);
            return this;
        }

        public BaseGuiComponent build() {
            component.item=itemBuilder.getItem();
            return component;
        }
    }
}
