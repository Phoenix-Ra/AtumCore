package me.phoenixra.atum.core.gui.api;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class GuiComponent {
    private final HashMap<GuiClickType, Runnable> listeners = new HashMap<>();
    private final HashMap<GuiClickType, String> permissions = new HashMap<>();
    private final Set<GuiClickType> confirmationRequired = new HashSet<>();
    private @Nullable String lorePermission;

    @NotNull
    public abstract ItemStack getItem();

    public abstract int getSlot();
    public abstract InventoryType getInventoryType();

    @Nullable
    public ItemMeta getItemMeta() {
        return getItem().getItemMeta();
    }

    public void setItemMeta(@NotNull ItemMeta itemMeta) {
        getItem().setItemMeta(itemMeta);
    }

    public void setLorePermission(@Nullable String permission) {
        lorePermission = permission;
    }

    @Nullable
    public String getLorePermission() {
        return lorePermission;
    }

    public void setPermission(@NotNull GuiClickType click, @Nullable String permission) {
        permissions.put(click, permission);
    }
    @Nullable
    public String getPermission(@NotNull ClickType click) {
        for(Map.Entry<GuiClickType,String> entry : permissions.entrySet())
            if(entry.getKey().isEquals(click)) return entry.getValue();
        return null;
    }


    public GuiComponent setListener(@NotNull GuiClickType click, @Nullable Runnable listener) {
        listeners.put(click, listener);
        return this;
    }

    @Nullable
    public Runnable getListener(@NotNull ClickType click) {
        for(Map.Entry<GuiClickType,Runnable> entry : listeners.entrySet())
            if(entry.getKey().isEquals(click)) return entry.getValue();
        return null;
    }

    public void setConfirmationRequired(@NotNull GuiClickType click) {
        confirmationRequired.add(click);
    }

    public boolean isConfirmationRequired(@NotNull ClickType click) {
        for(GuiClickType clickType : confirmationRequired)
            if(clickType.isEquals(click)) return true;
       return false;
    }



}
