package me.Marek2810.PickLock.Utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FormatUtils {

	public static String getLocation (Location loc) {
		return loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
	}
	
	public static String UUIDtoName(String sUUID) {
		return Bukkit.getServer().getOfflinePlayer(UUID.fromString(sUUID)).getName();
	}
}