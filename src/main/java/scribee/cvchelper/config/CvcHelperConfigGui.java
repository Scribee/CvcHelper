package scribee.cvchelper.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import scribee.cvchelper.util.Reference;

public class CvcHelperConfigGui extends GuiConfig {
	
	/**
	 * Constructor for the configuration screen for the mod.
	 * 
	 * @param previous - screen to go back to after this screen is exited
	 */
	public CvcHelperConfigGui(GuiScreen previous) {
		super(previous, getConfigElements(), Reference.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
	}
	
	/**
	 * Gets all the config elements belonging to each category.
	 * 
	 * @return List<DummyCategoryElement> - config elements for every category in this mod's config GUI.
	 */
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> categories = new ArrayList<IConfigElement>();
		
		categories.add(new DummyCategoryElement(ConfigHandler.CATEGORY_MODULES, "gui.config.modules", ModuleCategory.class));
		
		return categories;
	}
}
