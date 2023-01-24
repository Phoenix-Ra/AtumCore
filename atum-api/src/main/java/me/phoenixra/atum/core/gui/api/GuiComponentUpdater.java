package me.phoenixra.atum.core.gui.api;

import me.phoenixra.atum.core.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public interface GuiComponentUpdater {
    ItemStack run(ItemBuilder component);
}
