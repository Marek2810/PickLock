package me.Marek2810.PickLock.Listener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.Marek2810.PickLock.Utils.ChatUtils;
import me.Marek2810.PickLock.Utils.ChestsUtils;
import me.Marek2810.PickLock.Utils.DoorsUtils;
import me.Marek2810.PickLock.Utils.LockUtils;
import net.md_5.bungee.api.ChatColor;

public class PlaceSecBlockListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getBlock() == null) return;
		Block block = event.getBlock();
		if ( !(LockUtils.isLockable(block.getType().toString())) ) return;
		Player player = event.getPlayer();
		if ( DoorsUtils.isDoubleDoor(block) ) {
			Location fistHalfLoc = DoorsUtils.getSecondDoor(block).getLocation();
			if ( !(LockUtils.hasLock(fistHalfLoc)) ) return;
			event.setCancelled(true);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("cannot-be-placed-door")));
		}
		else if ( ChestsUtils.isDoubleChest(block)) {
			Location firstChestLoc = ChestsUtils.getSecondChest(block);
			if ( !(LockUtils.hasLock(firstChestLoc)) ) return;
				event.setCancelled(true);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("cannot-be-placed-chest")));
		}
	}
}
