package com.phoenixra.atum.core.gui.baseframes;

import com.phoenixra.atum.core.old.ItemBuilder;
import com.phoenixra.atum.core.gui.BaseFrameComponent;
import com.phoenixra.atum.core.gui.GuiDrawer;
import com.phoenixra.atum.core.gui.SkullSkin;
import com.phoenixra.atum.core.gui.api.PhoenixClickType;
import com.phoenixra.atum.core.gui.api.PhoenixFrame;
import com.phoenixra.atum.core.utils.PhoenixUtils;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WarningFrame extends PhoenixFrame {
	private final String message;
	private final GuiDrawer guiDrawer;
	@Setter @Accessors(chain = true)
	private String title = PhoenixUtils.colorFormat("&cAction Warning");
	public WarningFrame(@NotNull GuiDrawer guiDrawer, @Nullable PhoenixFrame parent, @NotNull Player viewer, @NotNull String message) {
		super(parent, viewer);
		this.message = message;
		this.guiDrawer = guiDrawer;
	}

	@Override
	public void createComponents() {
		for (int slot = 0; slot < 9; slot++) {
			add(new BaseFrameComponent.Builder(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).
					setDisplayName("")).withSlot(slot).build());
		}
		BaseFrameComponent component = new BaseFrameComponent.Builder(new ItemBuilder().
				setCustomOwner(SkullSkin.WARNING.getSkin()).setDisplayName("&eWarning!").
				setLores(" ","&7Action has been cancelled","","&7warning message:","&8&o"+message, "","&aL Click to return"))
				.withSlot(13).build();
		component.setListener(new PhoenixClickType().setClickType(ClickType.LEFT),()->{
			if(getParent()==null) {
				getViewer().closeInventory();
				return;
			}
			guiDrawer.open(getParent());
		});
		add(component);
	}

	@Override
	public void onClose() {

	}

	@Override
	public @NotNull String getTitle() {
		return title;
	}

	@Override
	public int getSize() {
		return 3 * 9;
	}


}
