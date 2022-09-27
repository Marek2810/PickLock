package me.Marek2810.PickLock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import me.Marek2810.PickLock.Commands.LockCMD;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.PickLock.Commands.Key;
import me.Marek2810.PickLock.Commands.Picklock;
import me.Marek2810.PickLock.Files.DataManager;

public class Main extends JavaPlugin implements Listener {

    public static Main inst;   
    public static DataManager data;
    public static DataManager playerData;
    public static Lock lock;
    public static Lockpick lockpick;
	public static LockCMD lockCMD;
    
    public static Set<String> yamlKeys;    
    public static List<String> chests;
    public static List<String> doors;
    public static List<String> trapdoors;
    public static HashMap<String, Boolean> yamlIsLocked = new HashMap<String, Boolean>();
    public static HashMap<String, Integer> yamlKeyID = new HashMap<String, Integer>();
    public static HashMap<String, List<Object>> yamlLocations = new HashMap<String, List<Object>>();  
    
    @Override
    public void onEnable() {
    	inst = this;    	
    	data = new DataManager(this, "locks.yml");
    	playerData = new DataManager(this, "data.yml");
    	lock = new Lock();
    	lockpick = new Lockpick();
		lockCMD = new LockCMD();
    	this.getCommand("picklock").setExecutor(new Picklock());
    	this.getCommand("kluc").setExecutor(new Key());
		this.getCommand("zamok").setExecutor(new LockCMD());
    	this.getServer().getPluginManager().registerEvents(lock, this);
    	this.getServer().getPluginManager().registerEvents(lockpick, this); 
    	this.saveDefaultConfig();
    	chests = this.getConfig().getStringList("locking.chests");
    	doors = this.getConfig().getStringList("locking.doors");
    	trapdoors = this.getConfig().getStringList("locking.trapdoors");
    	if (data.getConfig().getConfigurationSection("locks") != null) {
        	yamlKeys = data.getConfig().getConfigurationSection("locks").getKeys(false);
        	if (yamlKeys != null) {
        		for (String lockID : yamlKeys) {
	        		yamlIsLocked.put(lockID, data.getConfig().getBoolean("locks." + lockID + ".locked") );
	        		yamlKeyID.put(lockID, data.getConfig().getInt("locks." + lockID + ".keyID"));
	        		List<Object> locs = new ArrayList<Object>();
	        		for (int i = 1; i <= 4; i++ ) {
	        			Object get = data.getConfig().get("locks." + lockID + ".location" + i);	        			
	        			if (get != null ) {
	        				locs.add(data.getConfig().get("locks." + lockID + ".location" + i));
	        			}
	        			else {
	        				break;
	        			}	        			
	        		}
	        		yamlLocations.put(lockID, locs);
	        	}        		
	        }
        }
    }

    @Override
    public void onDisable() {

    }
}