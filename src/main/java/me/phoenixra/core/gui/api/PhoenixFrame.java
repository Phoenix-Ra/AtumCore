package me.phoenixra.core.gui.api;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PhoenixFrame {

    @Nullable @Getter private final PhoenixFrame parent;
    @NotNull @Getter private final Player viewer;
    @NotNull @Getter private final Set<PhoenixFrameComponent> components = ConcurrentHashMap.newKeySet();

    public PhoenixFrame(@Nullable PhoenixFrame parent, @NotNull Player viewer) {
        this.parent = parent;
        this.viewer = viewer;
    }

    @NotNull
    public abstract String getTitle();

    public abstract int getSize();

    public abstract void createComponents();

    public abstract void onClose();

    @Nullable
    public PhoenixFrameComponent getComponent(int slot, InventoryType inventoryType) {
        for (PhoenixFrameComponent c : getComponents()) {
            if (c.getSlot() == slot && c.getInventoryType() == inventoryType) {
                return c;
            }
        }
        return null;
    }

    public void add(@NotNull PhoenixFrameComponent c) {
        components.add(c);
    }

    public void clear() {
        components.clear();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PhoenixFrame) {
            PhoenixFrame otherFrame = (PhoenixFrame) other;
            return getSize() == otherFrame.getSize() && getTitle().equals(otherFrame.getTitle())
                    && getComponents().equals(otherFrame.getComponents());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode() + Integer.hashCode(getSize()) + getComponents().hashCode();
    }

}
