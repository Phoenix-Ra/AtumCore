package me.phoenixra.core.gui.api;

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

public abstract class PhoenixFrameComponent {
    private final HashMap<PhoenixClickType, Runnable> listeners = new HashMap<>();
    private final HashMap<PhoenixClickType, String> permissions = new HashMap<>();
    private final Set<PhoenixClickType> confirmationRequired = new HashSet<>();
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

    public void setPermission(@NotNull PhoenixClickType click, @Nullable String permission) {
        permissions.put(click, permission);
    }

    @Nullable
    public String getPermission(@NotNull ClickType click, @NotNull InventoryType inventoryType) {
        for(Map.Entry<PhoenixClickType,String> entry : permissions.entrySet())
            if(entry.getKey().getClickType()==click && entry.getKey().getInventoryType()==inventoryType) return entry.getValue();
        return null;
    }

    public PhoenixFrameComponent setListener(@NotNull PhoenixClickType click, @Nullable Runnable listener) {
        listeners.put(click, listener);
        return this;
    }

    @Nullable
    public Runnable getListener(@NotNull ClickType click,@NotNull InventoryType inventoryType) {
        for(Map.Entry<PhoenixClickType,Runnable> entry : listeners.entrySet())
            if(entry.getKey().getClickType()==click && entry.getKey().getInventoryType()==inventoryType) return entry.getValue();
        return null;
    }

    public void setConfirmationRequired(@NotNull PhoenixClickType click) {
        confirmationRequired.add(click);
    }

    public boolean isConfirmationRequired(@NotNull ClickType click,@NotNull InventoryType inventoryType) {
        for(PhoenixClickType clickType : confirmationRequired)
            if(clickType.getClickType()==click && clickType.getInventoryType()==inventoryType) return true;
       return false;
    }



}
