package me.Marek2810.PickLock.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Marek2810.PickLock.PickLock;

public class LockUtils {
	
	public static boolean isLockable(String clicked) {
		if ( PickLock.chests.contains(clicked)
				|| PickLock.doors.contains(clicked)
				|| PickLock.trapdoors.contains(clicked) ) return true;
		return false;
	}

	public static void removeLock(String lockID) {
		PickLock.yamlIsLocked.remove(lockID);
		PickLock.yamlKeyID.remove(lockID);
		PickLock.yamlKeyType.remove(lockID);
		PickLock.yamlKeys.remove(lockID);
		PickLock.yamlLocations.remove(lockID);
		PickLock.locks.getConfig().set("locks." + lockID, null);
		PickLock.locks.saveConfig();
	}
	
	public static boolean isOwner(Player player, String lockID) {
		String UUID = player.getUniqueId().toString();
		String owner = PickLock.locks.getConfig().getString("locks." + lockID + ".owner");
		if (UUID.equals(owner)) return true;
		return false;
	}
	
	public static boolean hasLock(Location loc) {
		 Object check = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
		 for (Object o : PickLock.yamlLocations.keySet()) {
			 if(o == null) return false;	
			 String lockID = o.toString();
			 Object lock = PickLock.yamlLocations.get(lockID);
				if (  lock.toString().contains(check.toString()) ) return true;				
			 }
		 return false;
	 }
	
	public static String getLockID(Location loc){
		 Object check = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
		 for (Object o : PickLock.yamlLocations.keySet()) {
			 if(o == null) return null;	
			 Object lock = PickLock.yamlLocations.get(o.toString());			 
				if (  lock.toString().contains(check.toString()) ) {
					return o.toString();				
				}
			 }
		 return null;
	 }
	
	public static boolean isLocked(Location loc) {
		 if (!hasLock(loc)) return false;
		 String lockID = getLockID(loc);
		 Boolean isLocked = PickLock.yamlIsLocked.get(lockID);
		 if ( !isLocked ) return false;
		 return true;
	 }
	
	public static void makeLock(PlayerInteractEvent event, List<Location> locations) {
    	int lockID;			
		if (  PickLock.yamlKeys != null ) {
			lockID = PickLock.yamlKeys.size()+1;
			while (PickLock.yamlKeys.contains( Integer.toString(lockID) )){
				lockID++;
			}
		}
		else {
			lockID = 1;
		}
		String sLockID = Integer.toString(lockID);
    	Player player = (Player) event.getPlayer();	    	
    	int keyID = KeyUtils.setKey(player);
		String material = event.getClickedBlock().getType().toString();			
		//Owner
        PickLock.locks.getConfig().set("locks." + lockID + ".owner", player.getUniqueId().toString());
        //location
        List<Object> locs = new ArrayList<Object>();
        int i = 1;
        for (Location loc : locations) {
			locs.add(loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
			PickLock.locks.getConfig().set("locks." + lockID + ".location" + i, loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());	
			i++;
        }
        PickLock.yamlLocations.put(sLockID, locs);
        //material
        PickLock.locks.getConfig().set("locks." + lockID + ".material", material);	
        //key type
        PickLock.locks.getConfig().set("locks." + lockID + ".keyType", KeyUtils.getKeyType( event.getPlayer().getInventory().getItemInMainHand() ));
        //keyID
        PickLock.locks.getConfig().set("locks." + lockID + ".keyID", keyID);
        //locked
        PickLock.locks.getConfig().set("locks." + lockID + ".locked", true);
		PickLock.locks.saveConfig();		
		PickLock.yamlKeys = PickLock.locks.getConfig().getConfigurationSection("locks").getKeys(false);
    	PickLock.yamlIsLocked.put(sLockID, true);
    	PickLock.yamlKeyID.put(sLockID, keyID);   
    	PickLock.yamlKeyType.put(sLockID, KeyUtils.getKeyType( event.getPlayer().getInventory().getItemInMainHand() ));
    	return;
    }
	
	public static void addLocationToLock(String lockID, List<Location> locs, int lastLoc) {	
		List<Object> savedLocs = PickLock.yamlLocations.get(lockID);
		for (Location loc : locs) {
			lastLoc = lastLoc+1;
			savedLocs.add( loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() );
			PickLock.locks.getConfig().set("locks." + lockID + ".location" + lastLoc, loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
		}
		PickLock.locks.saveConfig();		
		PickLock.yamlLocations.remove(lockID);
		PickLock.yamlLocations.put(lockID, savedLocs);
	}
}