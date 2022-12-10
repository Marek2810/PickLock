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
			player.sendMessage(ChatColor.GREEN + "Double doorz");
			Location fistHalfLoc = DoorsUtils.getSecondDoor(block).getLocation();
			if ( LockUtils.hasLock(fistHalfLoc) ) {
				player.sendMessage("yup");
				if (PickLock.locks.getConfig().get("locks." + LockUtils.getLockID(fistHalfLoc) + ".location3") == null ) {
					//add doors
					player.sendMessage("doors true");
					List<Location> locs = new ArrayList<Location>();
					locs.add(block.getLocation());
					locs.add(DoorsUtils.getSecondDoorHlaf(block));
					LockUtils.addLocationToLock(LockUtils.getLockID(fistHalfLoc), locs, 2);
					player.sendMessage(ChatColor.GREEN + "doors done");
				}
			}
		}
		else if ( ChestsUtils.isDoubleChest(block)) {
			player.sendMessage(ChatColor.GREEN + "Double chest");
			Location firstChestLoc = ChestsUtils.getSecondChest(block);
			if ( LockUtils.hasLock(firstChestLoc) ) {
				player.sendMessage("yup");
				if (PickLock.locks.getConfig().get("locks." + LockUtils.getLockID(firstChestLoc) + ".location2") == null ) {
					//add chest
					player.sendMessage("chest true");
					List<Location> locs = new ArrayList<Location>();
					locs.add(block.getLocation());
					LockUtils.addLocationToLock(LockUtils.getLockID(firstChestLoc), locs, 1);
					player.sendMessage(ChatColor.GREEN + "chests done");
				}
			}
		}
	}
}
