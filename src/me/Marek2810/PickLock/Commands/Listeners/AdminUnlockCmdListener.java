package me.Marek2810.PickLock.Commands.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.PickLock.Commands.LockCommand;
import me.Marek2810.PickLock.Utils.FormatUtils;
import me.Marek2810.PickLock.Utils.LockUtils;
import net.md_5.bungee.api.ChatColor;

public class AdminUnlockCmdListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onClick(PlayerInteractEvent event ) {
		if ( !(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) ) return;
		if ( event.getClickedBlock() == null ) return;
       	Player player = (Player) event.getPlayer();
       	if (LockCommand.adminUnlock.get(player) == null || !(LockCommand.adminUnlock.get(player)) ) return;
        String typeOfClickeBlock = event.getClickedBlock().getType().toString();	
        Location loc = event.getClickedBlock().getLocation();
        if ( !(LockUtils.isLockable(typeOfClickeBlock)) ) return;
        event.setCancelled(true);
        if ( LockUtils.hasLock(event.getClickedBlock().getLocation()) ) {
			if ( !(LockUtils.isLocked(event.getClickedBlock().getLocation())) ) {	                            		
        		//lock
				String lockID = LockUtils.getLockID( event.getClickedBlock().getLocation() );
				PickLock.yamlIsLocked.replace(lockID, true);
        		PickLock.locks.getConfig().set("locks." + lockID + ".locked", false);
        		PickLock.locks.saveConfig();
        		String msg = PickLock.inst.getConfig().getString("messages.on-lock");
        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        		LockCommand.adminLock.remove(player);
        		PickLock.console.sendMessage(ChatColor.translateAlternateColorCodes('&',
						PickLock.logPrefix + "&cAdmin &6" + player.getName() + " &aodomkol zámok &6ID: &e" 
						+ lockID + " &ana súradniciach &6" + FormatUtils.getLocation(loc) + "&a."));
        		event.setCancelled(true);                 		
        		return;
        	}
        	else {
        		//locked
        		String msg = PickLock.inst.getConfig().getString("messages.is-locked");
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
				LockCommand.adminLock.remove(player);
				event.setCancelled(true);
				return;
        	}
		}
		else {
			//not locked
			String msg = PickLock.inst.getConfig().getString("messages.no-lock");
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			LockCommand.adminLock.remove(player);
			event.setCancelled(true);
			return;
		}	        				        			
	}
		
	}

	

