package scribee.cvchelper.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class CvcHelperGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft mc) {

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return CvcHelperConfigGui.class; // class to instantiate to create the mod's configuration GUI
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null; // extra buttons to display in the "Options" menu in the pause screen, unused by this mod
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

}
