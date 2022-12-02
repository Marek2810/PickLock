package me.Marek2810.PickLock.Commands.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Marek2810.PickLock.Commands.LockCommand;

public class PlayerListener implements Listener {
	
	@EventHandler
	public static void onPlayerQuit (PlayerQuitEvent event) {
		Player player = (Player) event.getPlayer();
		if (LockCommand.removingLock.containsKey(player)) LockCommand.removingLock.remove(player);		
		if (LockCommand.infoLock.containsKey(player)) LockCommand.infoLock.remove(player);
		if (LockCommand.adminRemovingLock.containsKey(player)) LockCommand.adminRemovingLock.remove(player);
		if (LockCommand.adminLock.containsKey(player)) LockCommand.adminLock.remove(player);
		if (LockCommand.adminUnlock.containsKey(player)) LockCommand.adminUnlock.remove(player);
		return;
	}
}
