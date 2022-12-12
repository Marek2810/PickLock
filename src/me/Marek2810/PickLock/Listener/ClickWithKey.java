package me.Marek2810.PickLock.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.PickLock.Utils.ChestsUtils;
import me.Marek2810.PickLock.Utils.DoorsUtils;
import me.Marek2810.PickLock.Utils.FormatUtils;
import me.Marek2810.PickLock.Utils.KeyUtils;
import me.Marek2810.PickLock.Utils.LockUtils;
import net.md_5.bungee.api.ChatColor;

public class ClickWithKey implements Listener {

	@EventHandler
	public void onKeyClick(PlayerInteractEvent event) {
		if ( !(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) ) return;
		if ( event.getClickedBlock() == null ) return;
       	Player player = (Player) event.getPlayer();
        String typeOfClickeBlock = event.getClickedBlock().getType().toString();	        
        if ( !(LockUtils.isLockable(typeOfClickeBlock)) ) return;
        Location loc = event.getClickedBlock().getLocation();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        String formatedLoc = FormatUtils.getLocation(loc);
        if (KeyUtils.isKey(player.getInventory().getItemInMainHand())) {   			
        	//Have lock
			if ( LockUtils.hasLock(event.getClickedBlock().getLocation() ) ) {
				event.setCancelled(true);
				ItemMeta meta = itemInMainHand.getItemMeta();
				boolean hasLore = meta.hasLore();
				//Check if key used
				if (hasLore) {
					List<String> lore = meta.getLore();
					String lore1 = lore.get(1);
                    int keyID = Integer.parseInt(lore1.replaceAll("[^0-9]", ""));
                    String lockID = LockUtils.getLockID(event.getClickedBlock().getLocation());
                    if (lockID == null ) {
                    	String msg = PickLock.inst.getConfig().getString("messages.error");
                    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    	return;
                    }	                   
                    //Check if key is correct
                    if ( KeyUtils.isGoodKey(lockID, keyID, KeyUtils.getKeyType(itemInMainHand)) ) {
                    	//Is locked?
                    	if ( LockUtils.isLocked(event.getClickedBlock().getLocation()) ) {	                            		
                    		//unlock
                    		PickLock.yamlIsLocked.replace(lockID, false);
                    		PickLock.locks.getConfig().set("locks." + lockID + ".locked", false);
                    		PickLock.locks.saveConfig();
                    		String msg = PickLock.inst.getConfig().getString("messages.on-unlock");
                    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    		PickLock.console.sendMessage(ChatColor.translateAlternateColorCodes('&',
								PickLock.logPrefix + "&aHráč &6" + player.getName() + " &aodomkol zámok &6ID: &e" 
								+ lockID + " &ana súradniciach &6" + formatedLoc + "&a."));
                    		return;
                    	}
                    	else {
                    		//lock
                    		PickLock.yamlIsLocked.replace(lockID, true);
                    		PickLock.locks.getConfig().set("locks." + lockID + ".locked", true);
                    		PickLock.locks.saveConfig();
                    		String msg = PickLock.inst.getConfig().getString("messages.on-lock");
                    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    		PickLock.console.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    			PickLock.logPrefix + "&aHráč &6" + player.getName() + " &azamkol zámok &6ID: &e" 
								+ lockID + " &ana súradniciach &6" + formatedLoc + "&a."));
                    		return;
                    	}
                    }
                    else {
    					//Wrong key
                    	String msg = PickLock.inst.getConfig().getString("messages.wrong-key");
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						return;
    				}
				}
				else {
					//Wrong key (no lore)
					String msg = PickLock.inst.getConfig().getString("messages.wrong-key");
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
					return;
				}
			}	        			
			//Not having a lock
			else {
				event.setCancelled(true);
				ItemMeta meta = itemInMainHand.getItemMeta();
				boolean hasLore = meta.hasLore();
				//Is key used?
				if (hasLore) {
					String msg = PickLock.inst.getConfig().getString("messages.key-is-used");
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    				return;
				}
				else {
					//Make a lock	        					
					ArrayList<Location> lockLocations = new ArrayList<Location>();		    					
					lockLocations.add( event.getClickedBlock().getLocation() );		    					
    				if (PickLock.chests.contains( event.getClickedBlock().getType().toString() )) {
    					if ( ChestsUtils.isDoubleChest( event.getClickedBlock() ) ) {		
    						lockLocations.add( ChestsUtils.getSecondChest(event.getClickedBlock()) );
    					}
    				}
    				else if (PickLock.doors.contains( event.getClickedBlock().getType().toString() )) {
    					lockLocations.add( DoorsUtils.getSecondDoorHlaf( event.getClickedBlock()) );	
    					if ( DoorsUtils.isDoubleDoor( event.getClickedBlock() ) ) {
    						Block secDoorBlock = DoorsUtils.getSecondDoor( event.getClickedBlock() );
    						lockLocations.add( secDoorBlock.getLocation() );
    						lockLocations.add( DoorsUtils.getSecondDoorHlaf( secDoorBlock ) );
    					}			    					
    				}
    				LockUtils.makeLock(event, lockLocations);	                                                                       
                    String msg = PickLock.inst.getConfig().getString("messages.on-first-lock");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    PickLock.console.sendMessage(ChatColor.translateAlternateColorCodes('&',
						PickLock.logPrefix + "&aHráč &6" + player.getName() + " &anasadil zámok &6ID: &e" 
						+ LockUtils.getLockID(event.getClickedBlock().getLocation()) + " &ana súradniciach &6" + formatedLoc + "&a."));		                        
                   return;
				}
			}    		
        }
	}
}