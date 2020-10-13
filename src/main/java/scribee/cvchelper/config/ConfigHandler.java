package scribee.cvchelper.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {
	public static Configuration config;
	
	public static final String CATEGORY_HIDDEN = "Hidden";
	public static final String CATEGORY_MODULES = "Module Settings";
	
	public static boolean enableStreaks = true;
	public static boolean enableGrenadeCountdown = true;
	public static boolean enableFirebombCountdown = true;
	
	public static void init(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		
		System.out.println("Initialized config file");
		
		syncConfig();
	}
	
	public static void syncConfig() {
		enableStreaks = config.getBoolean("Enable streak counter",
				CATEGORY_MODULES,
				true,
				"If true, killstreaks for each weapon will be counted and displayed on screen in CvC games.");
		
		enableGrenadeCountdown = config.getBoolean("Enable grenade notification",
				CATEGORY_MODULES,
				true,
				"If true, a grenade icon will be displayed when the frag grenade can be reselected from the shop in TDM.");
		
		enableFirebombCountdown = config.getBoolean("Enable firebomb notification",
				CATEGORY_MODULES,
				true,
				"If true, a firebomb icon will be displayed when the firebomb can be reselected from the shop in TDM.");
		
		if (config.hasChanged()) {
			config.save();
			System.out.println("Saved config file");
		}
	}
}
