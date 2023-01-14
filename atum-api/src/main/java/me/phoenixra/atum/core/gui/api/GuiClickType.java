package me.phoenixra.atum.core.gui.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiClickType {
    @Getter @NotNull
    private List<ClickType> clickTypes = new ArrayList<>();

    public GuiClickType(@NotNull ClickType type){
        clickTypes.add(type);
    }
    public GuiClickType(ClickType ... types){
        clickTypes.addAll(Arrays.asList(types));
    }
    public GuiClickType(@NotNull List<ClickType> types){
        clickTypes = types;
    }
    public boolean isEquals(@NotNull ClickType click){
        return  clickTypes.contains(click);
    }
}
