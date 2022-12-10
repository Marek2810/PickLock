package me.Marek2810.PickLock.Commands.Listeners;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.PickLock.Commands.LockCommand;
import me.Marek2810.PickLock.Utils.Format;
import me.Marek2810.PickLock.Utils.LockUtils;
import net.md_5.bungee.api.ChatColor;

public class InfoCmdListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if ( !(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) ) return;
		if ( event.getClickedBlock() == null ) return;
       	Player player = (Player) event.getPlayer();
       	if ( LockCommand.infoLock.get(player) == null || !(LockCommand.infoLock.get(player)) ) return;
        String typeOfClickeBlock = event.getClickedBlock().getType().toString();	
        Location loc = event.getClickedBlock().getLocation();
        if ( !(LockUtils.isLockable(typeOfClickeBlock)) ) return;
        if ( LockUtils.hasLock(loc) ) {
			event.setCancelled(true);
			String lockID = LockUtils.getLockID(loc);
			ConfigurationSection lock = PickLock.locks.getConfig().getConfigurationSection("locks." + lockID);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
					"&7------------ \n"
					+ "&aLockID: &e" + lockID + "\n"
					+ "&aOwner: &e" 
						+ Format.UUIDtoName(lock.getString("owner")) 
						+ "\n" 
					+ "&aKey type: &e" + lock.getString("keyType") + "\n" 
					+ "&aKey ID: &e" + lock.getInt("keyID") + "\n"
					+ "&aLocked: &e" + lock.getBoolean("locked") + "\n"
					+ "&7------------"));				
			PickLock.console.sendMessage(ChatColor.translateAlternateColorCodes('&',
					PickLock.logPrefix + "&aHráč &6" + player.getName() + " &azistil informácie o zámku &6ID: &e"
					+ lockID + " &ana súradniciach &6" + Format.getLocation(loc) + "&a."));
			LockCommand.infoLock.remove(player);
			return;
		}		
		event.setCancelled(true);	
		String msg = PickLock.inst.getConfig().getString("messages.no-lock");
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		LockCommand.infoLock.remove(player);
		return;		
	}
}