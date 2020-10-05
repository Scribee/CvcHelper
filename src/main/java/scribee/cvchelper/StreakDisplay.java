package scribee.cvchelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.util.EnumChatFormatting;
import scribee.cvchelper.util.Reference;

public class StreakDisplay {
    // Total number of kills the player has gotten since their last death
	private static int totalStreak = 0;
	// Message stating the player's current streak with the most recent weapon used
	private static String streakMessage = "";
	
	// Number of kills with each different weapon the player has gotten since their last death
	private static Map<String, Integer> weaponStreaks = new HashMap<String, Integer>();
	
	public static int getTotalStreak() {
		return totalStreak;
	}
	
	public static void resetStreaks() {
		totalStreak = 0;
    	streakMessage = "";
    	
    	weaponStreaks.forEach(new BiConsumer<String, Integer>() {
			@Override
			public void accept(String k, Integer v) {
				weaponStreaks.put(k, 0);
			}
		});
	}
	
    /**
     * Getter for currentStreak variable, which stores the message stating the current killstreak.
     * 
     * @return a formatted message stating the current weapon and streak.
     */	
	public static String getStreakMessage() {
		return streakMessage;
	}
	
	/**
     * Increments the killstreak counter for the specified weapon. If a key matching this weapon doesn't exist
     * for the weaponStreaks map yet, it will be created with an initial value of 1.
     * 
     * @param weapon - Gun characters used to indicate what weapon was used
     * @return streak message to send to the player
     */
    public static void updateWeaponStreaks(String weapon) {
    	int kills = 0;
    	totalStreak++;
    	
    	// If there was one, remove the headshot symbol to just get the characters that make up the gun
    	if (weapon.substring(weapon.length() - 1).equals(Reference.HEADSHOT)) {
    		weapon = weapon.substring(0, weapon.length() - 1);
    	}
    	
    	// If the weapon already exists as a key in the map, increment its value
    	if (weaponStreaks.containsKey(weapon)) {
    		kills = weaponStreaks.get(weapon) + 1;
    	}
    	// If the weapon doesn't exist in the map, the streak must be 1
    	else {
    		kills = 1;
    	}
    	
    	// Update the map
    	weaponStreaks.put(weapon, kills);
    	
    	// Update the streak message to be printed to the player
    	streakMessage = EnumChatFormatting.RESET + weapon + EnumChatFormatting.DARK_GREEN + " streak: " + EnumChatFormatting.RESET + kills + EnumChatFormatting.RESET;
    }
}
