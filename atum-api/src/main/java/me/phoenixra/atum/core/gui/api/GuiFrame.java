package me.phoenixra.atum.core.gui.api;

import me.phoenixra.atum.core.exceptions.AtumException;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GuiFrame {

    @Nullable @Getter private final GuiFrame parent;
    @NotNull @Getter private final Player viewer;
    @NotNull @Getter private final Set<GuiComponent> components = ConcurrentHashMap.newKeySet();

    public GuiFrame(@Nullable GuiFrame parent, @NotNull Player viewer) {
        this.parent = parent;
        this.viewer = viewer;
    }

    @NotNull
    public abstract String getTitle();

    public abstract int getSize();

    public abstract void createComponents() throws AtumException;

    public abstract void onClose();

    public void updateComponents(){
        for(GuiComponent component : components){
            component.update();
        }
    }

    @Nullable
    public GuiComponent getComponent(int slot, InventoryType inventoryType) {
        for (GuiComponent c : getComponents()) {
            if (c.getSlots().contains(slot) && c.getInventoryType() == inventoryType) {
                return c;
            }
        }
        return null;
    }

    public void add(@NotNull GuiComponent c) {
        components.add(c);
    }

    public void clear() {
        components.clear();
    }



    @Override
    public boolean equals(Object other) {
        if (other instanceof GuiFrame otherFrame) {
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
