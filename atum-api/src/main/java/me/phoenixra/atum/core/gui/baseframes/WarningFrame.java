package me.phoenixra.atum.core.gui.baseframes;

import me.phoenixra.atum.core.utils.ItemBuilder;
import me.phoenixra.atum.core.gui.BaseGuiComponent;
import me.phoenixra.atum.core.gui.GuiDrawer;
import me.phoenixra.atum.core.gui.SkullSkin;
import me.phoenixra.atum.core.gui.api.GuiClickType;
import me.phoenixra.atum.core.gui.api.GuiFrame;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.phoenixra.atum.core.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

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
		for (int slot = 0; slot < 9; slot++) {
			add(new BaseGuiComponent.Builder(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).
					setDisplayName("")).withSlot(slot).build());
		}
		BaseGuiComponent component = new BaseGuiComponent.Builder(new ItemBuilder().
				setCustomOwner(SkullSkin.WARNING.getSkin()).setDisplayName("&eWarning!").
				setLores(" ","&7Action has been cancelled","","&7warning message:","&8&o"+message, "","&aL Click to return"))
				.withSlot(13).build();
		component.setListener(new GuiClickType().setClickTypes(Arrays.asList(ClickType.LEFT)),()->{
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
