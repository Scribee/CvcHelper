package scribee.cvchelper;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import scribee.cvchelper.util.Reference;

/**
 * Notifies the player when they're able to reselect the grenade in TDM games.
 */
public class GrenadeCountdown {
	private boolean inGame = false;
	private boolean onCooldown = false; // if true, the player cannot select a grenade yet
	private int cooldownTicks = 0;
	
	/**
	 * Default constructor for a new GrenadeCountdown.
	 */
	public GrenadeCountdown() {
		
	}

	/**
	 * Updates onCooldown to begin the countdown.
	 */
	public void startCountdown() {
		inGame = true;
		onCooldown = true;
		cooldownTicks = Reference.GRENADE_COOLDOWN * 20; // there should be 20 ticks per second
	}

	/**
	 * Called at the beginning and end of every tick on the client, used to count ticks until the grenade can be selected.
	 * 
	 * @param event - Client tick event
	 */
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		// Make sure to only execute once per real game tick, during the end phase of the client tick
		if (onCooldown && event.phase == TickEvent.Phase.END) {
			// When the countdown is finished
			if (cooldownTicks <= 0) {
				onCooldown = false;
			}
			else {
				cooldownTicks--;
			}
		}
	}

	/**
	 * Getter method for the message that should be displayed to the player. If the player is able to reselect the grenade, a
	 * grenade symbol will be returned. Otherwise, "" is returned.
	 * 
	 * @return String - message to be displayed to the user notifying them that they can reselect the grenade
	 */
	public String getNadeMessage() {
		if (!onCooldown && inGame) {
			return Reference.GRENADE;
		}
		else {
			return "";
		}
	}
	
	/**
	 * Resets the countdown when the player is no longer in game, to stop displaying the grenade notification.
	 */
	public void resetCounter() {
		onCooldown = false;
		inGame = false;
	}
}
