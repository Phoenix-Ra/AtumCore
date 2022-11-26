package me.phoenixra.core.gui.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;

public class PhoenixClickType {
    @Getter @Setter @Accessors(chain = true)
    private ClickType clickType;
    @Getter @Setter @Accessors(chain = true)
    private InventoryType inventoryType;
}
