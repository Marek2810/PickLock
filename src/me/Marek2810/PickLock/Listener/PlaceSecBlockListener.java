package me.Marek2810.PickLock.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.Marek2810.PickLock.PickLock;
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
			if ( PickLock.locks.getConfig().get("locks." + LockUtils.getLockID(fistHalfLoc) + ".location3") != null ) return;
			//add doors
			List<Location> locs = new ArrayList<Location>();
			locs.add(block.getLocation());
			locs.add(DoorsUtils.getSecondDoorHlaf(block));
			LockUtils.addLocationToLock(LockUtils.getLockID(fistHalfLoc), locs, 2);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDvere úspešne pridané k zámku."));
		}
		else if ( ChestsUtils.isDoubleChest(block)) {
			Location firstChestLoc = ChestsUtils.getSecondChest(block);
			if ( !(LockUtils.hasLock(firstChestLoc)) ) return;
			if ( !(LockUtils.hasLock(firstChestLoc)) ) return;
			if ( PickLock.locks.getConfig().get("locks." + LockUtils.getLockID(firstChestLoc) + ".location2") != null ) return; 
			//add chest
			List<Location> locs = new ArrayList<Location>();
			locs.add(block.getLocation());
			LockUtils.addLocationToLock(LockUtils.getLockID(firstChestLoc), locs, 1);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTruhla úspešne pridaná k zámku."));			
		}
	}
}
