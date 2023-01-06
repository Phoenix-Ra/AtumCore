package me.phoenixra.atum.core.gui.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

public class GuiClickType {
    @Getter @Setter @Accessors(chain = true)
    private List<ClickType> clickTypes;

    public boolean isEquals(ClickType click){
        return  clickTypes.contains(click);
    }
}
