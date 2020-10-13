package scribee.cvchelper.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import scribee.cvchelper.util.Reference;

public class ModuleCategory extends CategoryEntry {
	public ModuleCategory(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
	}
	
	@Override
	protected GuiScreen buildChildScreen() {
		List<IConfigElement> elements = new ArrayList<IConfigElement>();

		elements.addAll((new ConfigElement(ConfigHandler.config.getCategory(ConfigHandler.CATEGORY_MODULES.toLowerCase()))).getChildElements());
		
		System.out.println("Module category config elements: " + elements.toString());
		
		return new GuiConfig(this.owningScreen,
				elements,
				Reference.MOD_ID,
				ConfigHandler.CATEGORY_MODULES,
				false,
				false,
				GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()),
				ConfigHandler.CATEGORY_MODULES);
	}
}
