package me.phoenixra.atum.core.gui.baseframes;

import me.phoenixra.atum.core.utils.ItemBuilder;
import me.phoenixra.atum.core.gui.BaseGuiComponent;
import me.phoenixra.atum.core.gui.GuiDrawer;
import me.phoenixra.atum.core.gui.api.GuiClickType;
import me.phoenixra.atum.core.gui.api.GuiFrame;
import me.phoenixra.atum.core.gui.api.GuiComponent;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.phoenixra.atum.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ConfirmationFrame extends GuiFrame {

    private final Runnable listener;
    private final GuiDrawer guiDrawer;

    @Setter @Accessors(chain = true)
    private String confirm_name = StringUtils.format("&aConfirm action");
    @Setter @Accessors(chain = true)
    private String cancel_name = StringUtils.format("&cCancel action");
    @Setter @Accessors(chain = true)
    private String title = StringUtils.format("&6Action Confirmation");
    public ConfirmationFrame(@NotNull GuiDrawer guiDrawer, @Nullable GuiFrame parent, @NotNull Player viewer, @Nullable Runnable listener) {
        super(parent, viewer);
        this.listener = listener;
        this.guiDrawer = guiDrawer;
    }


    @Override
    public void createComponents() {
        GuiComponent confirm = new BaseGuiComponent.Builder(new ItemBuilder().setType(Material.LIME_WOOL)
                .setDisplayName(confirm_name)).withSlots(12).build();
        confirm.setListener(new GuiClickType(ClickType.LEFT), listener);
        add(confirm);

        GuiComponent returnC = new BaseGuiComponent.Builder(new ItemBuilder().setType(Material.RED_WOOL)
                .setDisplayName(cancel_name)).withSlots(14).build();
        returnC.setListener(new GuiClickType(ClickType.LEFT), () -> {
            if(getParent()==null) {
                getViewer().closeInventory();
                return;
            }
            guiDrawer.open(getParent(), !Bukkit.isPrimaryThread());
        });
        add(returnC);
    }

    @Override
    public void onClose() {

    }


    @Override
    public @NotNull String getTitle() {
        return StringUtils.format(title);
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
