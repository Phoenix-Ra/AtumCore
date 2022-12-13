package com.phoenixra.atum.core.gui.baseframes;

import com.phoenixra.atum.core.old.ItemBuilder;
import com.phoenixra.atum.core.gui.BaseFrameComponent;
import com.phoenixra.atum.core.gui.GuiDrawer;
import com.phoenixra.atum.core.gui.api.PhoenixClickType;
import com.phoenixra.atum.core.gui.api.PhoenixFrame;
import com.phoenixra.atum.core.gui.api.PhoenixFrameComponent;
import com.phoenixra.atum.core.utils.PhoenixUtils;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfirmationFrame extends PhoenixFrame {

    private final Runnable listener;
    private final GuiDrawer guiDrawer;

    @Setter @Accessors(chain = true)
    private String confirm_name = PhoenixUtils.colorFormat("&aConfirm action");
    @Setter @Accessors(chain = true)
    private String cancel_name = PhoenixUtils.colorFormat("&cCancel action");
    @Setter @Accessors(chain = true)
    private String title = PhoenixUtils.colorFormat("&6Action Confirmation");
    public ConfirmationFrame(@NotNull GuiDrawer guiDrawer, @Nullable PhoenixFrame parent, @NotNull Player viewer, @Nullable Runnable listener) {
        super(parent, viewer);
        this.listener = listener;
        this.guiDrawer = guiDrawer;
    }


    @Override
    public void createComponents() {
        PhoenixFrameComponent confirm = new BaseFrameComponent.Builder(new ItemBuilder().setType(Material.LIME_WOOL)
                .setDisplayName(cancel_name)).withSlot(12).build();
        confirm.setListener(new PhoenixClickType().setClickType(ClickType.LEFT), listener);
        add(confirm);

        PhoenixFrameComponent returnC = new BaseFrameComponent.Builder(new ItemBuilder().setType(Material.RED_WOOL)
                .setDisplayName(cancel_name)).withSlot(14).build();
        returnC.setListener(new PhoenixClickType().setClickType(ClickType.LEFT), () -> {
            if(getParent()==null) {
                getViewer().closeInventory();
                return;
            }
            guiDrawer.open(getParent());
        });
        add(returnC);
    }

    @Override
    public void onClose() {

    }


    @Override
    public @NotNull String getTitle() {
        return PhoenixUtils.colorFormat(title);
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
