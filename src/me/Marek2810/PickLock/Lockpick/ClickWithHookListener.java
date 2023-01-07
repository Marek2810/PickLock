package me.Marek2810.PickLock.Lockpick;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import me.Marek2810.PickLock.Utils.ChatUtils;
import me.Marek2810.PickLock.Utils.LockUtils;
import me.Marek2810.RoleEngine.Utils.CharacterUtils;
import net.md_5.bungee.api.ChatColor;

public class ClickWithHookListener implements Listener {
	
	@EventHandler
	public void onHookClick(PlayerInteractEvent event) {
		if ( !(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) ) return;
		if ( event.getClickedBlock() == null ) return;
       	Player player = (Player) event.getPlayer();
       	if (ChatUtils.activeCommandActions(player)) return;     
        if (LockpickUtils.isHook(player.getInventory().getItemInMainHand())) {   
        	if (CharacterUtils.activeChar.get(player) == null) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMusíš si zvloiť postavu aby si mohol lockpickovať."));
				return;
			}
        	String typeOfClickeBlock = event.getClickedBlock().getType().toString();
            if ( !(LockUtils.isLockable(typeOfClickeBlock)) ) return;
        	Location loc = event.getClickedBlock().getLocation();
        	String lockID = LockUtils.getLockID(loc);
        	if ( !(LockUtils.hasLock(loc)) ) return;
        	if ( !(LockUtils.isLocked(loc)) ) return;
        	if ( !(LockpickUtils.canBeLockpicked(lockID)) ) return;  
        	event.setCancelled(true);
        	Inventory inv = LockpickUtils.setPins(LockpickUtils.createInv(player.getInventory().getItemInMainHand()), lockID);
        	player.closeInventory();
        	LockpickUtils.invs.add(inv);
        	LockpickUtils.invPins.put(inv, LockpickUtils.getPins(lockID));
        	LockpickUtils.lastPin.put(player, 0);
        	player.openInventory(inv);
        	LockpickGUIlistener.lockpicking.put(player, lockID);
        }      
	}
}