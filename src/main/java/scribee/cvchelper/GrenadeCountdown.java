package scribee.cvchelper;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class GrenadeCountdown {
	private static final int COOLDOWN_SECONDS = 90;

	private static boolean onCooldown = false;
	private static int cooldownTicks = COOLDOWN_SECONDS * 20; // 20 ticks per second

	public static void startCountdown() {
		onCooldown = true;
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		// Make sure to only execute once per real game tick, during the end phase of the client tick
		if (onCooldown && event.phase == TickEvent.Phase.END) {
			cooldownTicks--;
			
			if (cooldownTicks < 0) {
				onCooldown = false;
				cooldownTicks = COOLDOWN_SECONDS * 20;
			}
		}
	}
	
	public static String getNadeMessage() {
		if (!onCooldown) {
			return String.valueOf('\u926c');
		}
		else {
			return "";
		}
	}
}
