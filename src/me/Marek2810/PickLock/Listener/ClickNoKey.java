package me.Marek2810.PickLock.Listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.PickLock.Utils.KeyUtils;
import me.Marek2810.PickLock.Utils.LockUtils;
import net.md_5.bungee.api.ChatColor;

public class ClickNoKey implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if ( !(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) ) return;
		if ( event.getClickedBlock() == null ) return;
       	Player player = (Player) event.getPlayer();
        String typeOfClickeBlock = event.getClickedBlock().getType().toString();	        
        if ( !(LockUtils.isLockable(typeOfClickeBlock)) ) return;
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (KeyUtils.isKey(itemInMainHand)) return;
        Location loc = event.getClickedBlock().getLocation();   
        if ( !(LockUtils.hasLock(loc)) ) return;
        if ( !(LockUtils.isLocked(loc)) ) return;
        
        String msg = PickLock.inst.getConfig().getString("messages.is-locked");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		event.setCancelled(true);
		return;   
	}
}
