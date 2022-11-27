package me.phoenixra.core.gui.baseframes;

import com.cryptomorin.xseries.XMaterial;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.phoenixra.core.ItemBuilder;
import me.phoenixra.core.gui.BaseFrameComponent;
import me.phoenixra.core.gui.GuiDrawer;
import me.phoenixra.core.gui.api.PhoenixClickType;
import me.phoenixra.core.gui.api.PhoenixFrame;
import me.phoenixra.core.gui.SkullSkin;
import me.phoenixra.core.utils.PhoenixUtils;
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
			add(new BaseFrameComponent.Builder(new ItemBuilder(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(true)).
					setDisplayName("")).withSlot(slot).build());
		}
		BaseFrameComponent component = new BaseFrameComponent.Builder(new ItemBuilder().
				setCustomOwner(SkullSkin.WARNING.getSkin()).setDisplayName("&eWarning!").
				setLores(" ","&7Action has been cancelled","","&7warning message:","&7&o"+message, "","&aL Click to return"))
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
