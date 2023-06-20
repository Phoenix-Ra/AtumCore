package me.phoenixra.atum.core.gui.api;

import me.phoenixra.atum.core.utils.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public interface GuiComponentUpdater {
    ItemStack run(ItemBuilder component);
}
