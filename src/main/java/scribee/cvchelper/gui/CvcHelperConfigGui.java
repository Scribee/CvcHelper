package scribee.cvchelper.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import scribee.cvchelper.config.ConfigHandler;
import scribee.cvchelper.config.ModuleCategory;
import scribee.cvchelper.util.Reference;

public class CvcHelperConfigGui extends GuiConfig {
	
	public CvcHelperConfigGui(GuiScreen previous) {
		super(previous, getConfigElements(), Reference.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
	}
	
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> categories = new ArrayList<IConfigElement>();
		
		categories.add(new DummyCategoryElement(ConfigHandler.CATEGORY_MODULES, "gui.config.modules", ModuleCategory.class));
		
		return categories;
	}
}
