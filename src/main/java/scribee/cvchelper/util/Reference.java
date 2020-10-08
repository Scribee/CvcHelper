package scribee.cvchelper.util;

/**
 * Class to store constants used by the mod.
 */
public class Reference {
	/* MOD INFO */
	public static final String MOD_ID = "cvchelper";
	public static final String MOD_NAME = "CvC Helper";
	public static final String VERSION = "1.2";
	
	/* SPECIAL CHARACTERS */
	public static final String S = String.valueOf('\u00a7'); // section symbol used by minecraft for text formatting
	// Death type symbols
	public static final String GRENADE = String.valueOf('\u926c'); // symbol for death by grenade
	public static final String FIRE = String.valueOf('\u9273'); // firebomb
	public static final String C4 = String.valueOf('\u9276'); // C4 exploding
	public static final String BONE = String.valueOf('\u9271') + String.valueOf('\u9272'); // fall damage
	// Weapon symbols
	public static final String HEADSHOT = String.valueOf('\u9270');
	
	/* CONSTANTS */
	public static final int GRENADE_COOLDOWN = 90; // TDM grenade cooldown in seconds
}
