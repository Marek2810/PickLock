package me.Marek2810.PickLock.Utils;

import org.bukkit.entity.Player;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.PickLock.Commands.LockCommand;

public class ChatUtils {
	
	public static String getMessage(String msgType) {
		return PickLock.inst.getConfig().getString("messages." + msgType);
	}

	public static boolean activeCommandActions(Player player) {
		if ( !(LockCommand.removingLock.containsKey(player)) && !(LockCommand.infoLock.containsKey(player))
				&& !(LockCommand.adminRemovingLock.containsKey(player)) && !(LockCommand.adminLock.containsKey(player))
				&& !(LockCommand.adminUnlock.containsKey(player)) ) return false;
		return true;
	}	
	
}
