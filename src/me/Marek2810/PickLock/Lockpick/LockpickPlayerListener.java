package me.Marek2810.PickLock.Lockpick;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Marek2810.PickLock.PickLock;

public class LockpickPlayerListener implements Listener {
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
		UUID playerUUID = (UUID) event.getPlayer().getUniqueId();
		if ( !(PickLock.playerData.getConfig().contains("players." + playerUUID) )) {
    		PickLock.playerData.getConfig().set("players." + playerUUID + ".xp", 0.0); 
    		PickLock.playerData.saveConfig();
    	}
		LockpickUtils.playerPickXP.put(playerUUID, PickLock.playerData.getConfig().getDouble("players." + playerUUID + ".xp"));		
	}    
	
	
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
		LockpickUtils.playerPickXP.remove(event.getPlayer().getUniqueId());
    }
	
}
