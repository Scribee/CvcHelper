package scribee.cvchelper.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import scribee.cvchelper.gui.GuiPosition;

public class ConfigHandler {
	public static Configuration config;
	
	public static final String CATEGORY_MODULES = "Module Settings";
	public static final String CATEGORY_HIDDEN = "Hidden Settings"; // not displayed for the user, used to store other info
	
	public static boolean enableStreaks;
	public static boolean enableGrenadeCountdown;
	public static boolean enableFirebombCountdown;
	public static boolean enableFlashbangCountdown;
	public static GuiPosition messagePos; // position on screen to print streak messages
	
	/**
	 * Create the config file. Called from the preInit event handler in the main mod class.
	 * 
	 * @param event - FML preinitialization event
	 */
	public static void init(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		
		System.out.println("Initialized cvchelper config file");
		
		syncConfig();
	}
	
	/**
	 * Creates fields if they don't exist, and saves the config file when changes have been made.
	 */
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
		
		enableFlashbangCountdown = config.getBoolean("Enable flashbang notification",
				CATEGORY_MODULES,
				true,
				"If true, a flashbang icon will be displayed when the flashbang can be reselected from the shop in TDM.");
		
		// The "Streak message position" field saves the ordinal value of the last GuiPosition set
		messagePos = GuiPosition.values()[config.getInt("Streak message position",
				CATEGORY_HIDDEN,
				GuiPosition.HOTBAR_ABOVE.ordinal(),
				0,
				14,
				"Location on the screen where streak messages will be displayed.")];
				
		if (config.hasChanged()) {
			config.save();
		}
	}
}
