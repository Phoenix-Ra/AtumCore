package me.phoenixra.atum.core.gui;

import me.phoenixra.atum.core.gui.api.GuiComponentUpdater;
import me.phoenixra.atum.core.utils.item.ItemBuilder;
import me.phoenixra.atum.core.gui.api.GuiComponent;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseGuiComponent extends GuiComponent {

    @NotNull
    private ItemStack item;
    BaseGuiComponent(@NotNull ItemStack item){
        this.item=item;
    }

    @Setter @Accessors(chain = true)
    private InventoryType inventoryType=InventoryType.CHEST;
    private ArrayList<Integer> slots;

    @Override
    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public ArrayList<Integer> getSlots() {
        return slots;
    }

    @Override
    public InventoryType getInventoryType() {
        return inventoryType;
    }

    @Override
    public void update() {
        if(updater == null) return;
        updaterTimer++;
        if(updaterTimer >= updaterPeriod) {
            item = updater.run(new ItemBuilder(item));
            updaterTimer = 0;
        }
    }

    public static class Builder {
        private final BaseGuiComponent component;
        private final ItemBuilder itemBuilder;

        public Builder(@NotNull ItemBuilder itemBuilder) {
            this.itemBuilder = itemBuilder;
            component = new BaseGuiComponent(itemBuilder.getItem());
        }

        public Builder withUpdater(int period, GuiComponentUpdater updater){
            component.updater = updater;
            component.updaterPeriod = period;
            return this;
        }
        public Builder withSlots(int ... slots) {
            component.slots = new ArrayList<>();
            for(int slot : slots){
                component.slots.add(slot);
            }
            return this;
        }
        public Builder withSlots(List<Integer> slots) {
            component.slots = new ArrayList<>();
            component.slots.addAll(slots);
            return this;
        }
        public Builder withSlots(InventoryType inventoryType, int ... slots) {
            component.slots = new ArrayList<>();
            for(int slot : slots){
                component.slots.add(slot);
            }
            component.setInventoryType(inventoryType);
            return this;
        }
        public Builder withSlots(InventoryType inventoryType, List<Integer> slots) {
            component.slots = new ArrayList<>();
            component.slots.addAll(slots);
            component.setInventoryType(inventoryType);
            return this;
        }

        public BaseGuiComponent build() {
            component.item=itemBuilder.getItem();
            return component;
        }
    }
}
