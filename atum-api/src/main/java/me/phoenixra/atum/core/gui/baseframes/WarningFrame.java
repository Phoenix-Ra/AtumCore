package me.phoenixra.atum.core.gui.baseframes;

import me.phoenixra.atum.core.utils.item.ItemBuilder;
import me.phoenixra.atum.core.gui.BaseGuiComponent;
import me.phoenixra.atum.core.gui.GuiDrawer;
import me.phoenixra.atum.core.gui.SkullSkin;
import me.phoenixra.atum.core.gui.api.GuiClickType;
import me.phoenixra.atum.core.gui.api.GuiFrame;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.phoenixra.atum.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WarningFrame extends GuiFrame {
	private final String message;
	private final GuiDrawer guiDrawer;
	@Setter @Accessors(chain = true)
	private String title = StringUtils.format("&cAction Warning");
	public WarningFrame(@NotNull GuiDrawer guiDrawer, @Nullable GuiFrame parent, @NotNull Player viewer, @NotNull String message) {
		super(parent, viewer);
		this.message = message;
		this.guiDrawer = guiDrawer;
	}

	@Override
	public void createComponents() {
		add(new BaseGuiComponent.Builder(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).
				setDisplayName("")).withSlots(0,1,2,3,4,5,6,7,8).build());
		BaseGuiComponent component = new BaseGuiComponent.Builder(new ItemBuilder().
				setCustomOwner(SkullSkin.WARNING.getSkin()).setDisplayName("&eWarning!").
				setLore(" ","&7Action has been cancelled","","&7warning message:","&8&o"+message, "","&aL Click to return"))
				.withSlots(13).build();
		component.setListener(new GuiClickType(ClickType.LEFT),()->{
			if(getParent()==null) {
				getViewer().closeInventory();
				return;
			}
			guiDrawer.open(getParent(),!Bukkit.isPrimaryThread());
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
