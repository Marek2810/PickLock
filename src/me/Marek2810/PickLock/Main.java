package me.Marek2810.PickLock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.PickLock.Commands.Hook;
import me.Marek2810.PickLock.Commands.Key;
import me.Marek2810.PickLock.Commands.LockCMD;
import me.Marek2810.PickLock.Commands.Picklock;
import me.Marek2810.PickLock.Files.DataManager;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {

    public static Main inst;   
    public static DataManager locks;
    public static DataManager playerData;
    public static Lock lock;
    public static Lockpick lockpick;
	public static LockCMD lockCMD;
	public static Hook hook;
	
	public static ConsoleCommandSender console;
	public static String logPrefix;
    
    public static Set<String> yamlKeys;    
    public static List<String> chests;
    public static List<String> doors;
    public static List<String> trapdoors;
    public static HashMap<String, Boolean> yamlIsLocked = new HashMap<String, Boolean>();
    public static HashMap<String, Integer> yamlKeyID = new HashMap<String, Integer>();
    public static HashMap<String, String> yamlKeyType = new HashMap<String, String>();
    public static HashMap<String, List<Object>> yamlLocations = new HashMap<String, List<Object>>();  
    public static ConfigurationSection messages;
    
    public static ConfigurationSection lockpickGui;
    public static ConfigurationSection lockpickOptions;
         
    @Override
    public void onEnable() {
    	inst = this;    	
    	console = this.getServer().getConsoleSender();
    	this.saveDefaultConfig();
    	locks = new DataManager(this, "locks.yml");
    	playerData = new DataManager(this, "data.yml");
    	lock = new Lock();
    	lockpick = new Lockpick();
		lockCMD = new LockCMD();
		hook = new Hook();
    	this.getCommand("picklock").setExecutor(new Picklock());
    	this.getCommand("key").setExecutor(new Key());
		this.getCommand("lock").setExecutor(new LockCMD());
		this.getCommand("hook").setExecutor(new Hook());
    	this.getServer().getPluginManager().registerEvents(lock, this);
    	this.getServer().getPluginManager().registerEvents(lockpick, this);     	
    	logPrefix = "&7[&6PickLock&7] ";
    	//loading block to locks
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading blocks abble to locks..."));
    	chests = this.getConfig().getStringList("locking.chests");
    	doors = this.getConfig().getStringList("locking.doors");
    	trapdoors = this.getConfig().getStringList("locking.trapdoors");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aLoaded blocks to lock."));
    	if (locks.getConfig().getConfigurationSection("locks") != null) {
    		//Loading locked blocks
    		console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading locked blocks..."));
        	yamlKeys = locks.getConfig().getConfigurationSection("locks").getKeys(false);
        	if (yamlKeys != null) {
        		for (String lockID : yamlKeys) {
	        		yamlIsLocked.put(lockID, locks.getConfig().getBoolean("locks." + lockID + ".locked") );
	        		yamlKeyID.put(lockID, locks.getConfig().getInt("locks." + lockID + ".keyID"));
	        		yamlKeyType.put(lockID, locks.getConfig().getString("locks." + lockID + ".keyType"));
	        		List<Object> locs = new ArrayList<Object>();
	        		for (int i = 1; i <= 4; i++ ) {
	        			Object get = locks.getConfig().get("locks." + lockID + ".location" + i);	        			
	        			if (get != null ) {
	        				locs.add(locks.getConfig().get("locks." + lockID + ".location" + i));
	        			}
	        			else {
	        				break;
	        			}	        			
	        		}
	        		yamlLocations.put(lockID, locs);
	        	}
            	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aLoaded locked blocks."));
	        }
        }
    	//lockpicking
    	//loading options
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading lockpicking options..."));
    	lockpickGui = this.getConfig().getConfigurationSection("lockpicking.hook-gui");
    	lockpickOptions = this.getConfig().getConfigurationSection("lockpicking.options");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aLockpicking options loaded."));
    	
    	//creating levels
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading levels..."));
    	lockpick.generateLevels();
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aLevels loaded."));
    	
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading messages..."));
    	messages = this.getConfig().getConfigurationSection("messages");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aMessages loaded."));
    	
    	
    }

    @Override
    public void onDisable() {

    }
}