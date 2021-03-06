package scribee.cvchelper.util;

/**
 * Class to store constants used by the mod.
 */
public class Reference {
	/* MOD INFO */
	public static final String MOD_ID = "cvchelper";
	public static final String MOD_NAME = "CvC Helper";
	public static final String VERSION = "1.5";
	public static final String GUI_FACTORY_PATH = "scribee.cvchelper.gui.CvcHelperGuiFactory";
	
	/* SPECIAL CHARACTERS */
	public static final String S = String.valueOf('\u00a7'); // section symbol used by minecraft for text formatting
	// Death type symbols
	public static final String GRENADE = String.valueOf('\u926c'); // symbol for death by grenade
	public static final String FIRE = String.valueOf('\u9273'); // firebomb
	public static final String C4 = String.valueOf('\u9276'); // C4 exploding
	public static final String BONE = String.valueOf('\u9271') + String.valueOf('\u9272'); // fall damage
	// Weapon symbols
	public static final String HEADSHOT = String.valueOf('\u9270');
	public static final String SYMBOL_GRENADE = String.valueOf('\u9283');
	public static final String SYMBOL_FIREBOMB = String.valueOf('\u9279');
	public static final String SYMBOL_FLASHBANG = String.valueOf('\u9277');
	
	/* CONSTANTS */
	public static final int THROWABLE_COOLDOWN = 90; // TDM grenade cooldown in seconds
	public static final String NAME_GRENADE = "Frag Grenade";
	public static final String NAME_FLASHBANG = "Flashbang";
	public static final String NAME_FIREBOMB = "Firebomb";
	
}
