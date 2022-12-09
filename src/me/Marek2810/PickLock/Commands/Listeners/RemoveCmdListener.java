package me.Marek2810.PickLock.Commands.Listeners;

import org.bukkit.Location;
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

public class RemoveCmdListener implements Listener{

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if ( !(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) ) return;
		if ( event.getClickedBlock() == null ) return;
       	Player player = (Player) event.getPlayer();
       	if ( LockCommand.removingLock.get(player) == null || !(LockCommand.removingLock.get(player)) ) return;
        String typeOfClickeBlock = event.getClickedBlock().getType().toString();	
        Location loc = event.getClickedBlock().getLocation();
        if ( !(LockUtils.isLockable(typeOfClickeBlock)) ) return;
        event.setCancelled(true);
		if ( LockUtils.hasLock(loc) ) {							
			String lockID = LockUtils.getLockID( loc );
			if (LockUtils.isOwner(player, lockID)) {
//			if (UUID.equals(player.getUniqueId().toString())) {							
				LockUtils.removeLock(lockID);					
				String msg = PickLock.inst.getConfig().getString("messages.remove-lock");
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));					
				PickLock.console.sendMessage(ChatColor.translateAlternateColorCodes('&',
						PickLock.logPrefix + "&aHráč &6" + player.getName() + " &aodstránil zámok &6ID: &e" 
						+ lockID + " &ana súradniciach &6" + Format.getLocation(loc) + "&a."));
				LockCommand.removingLock.remove(player);								
				return;
			}
			else {
				String msg = PickLock.inst.getConfig().getString("messages.remove-lock-not-owned");
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));							
				LockCommand.removingLock.remove(player);
				return;	
			}
		}
        event.setCancelled(true);
		String msg = PickLock.inst.getConfig().getString("messages.no-lock");
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		LockCommand.removingLock.remove(player);		
		return;		
	}
}