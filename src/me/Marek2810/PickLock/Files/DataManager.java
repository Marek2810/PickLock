package me.Marek2810.PickLock.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Marek2810.PickLock.PickLock;

public class DataManager {
	
	private PickLock plugin;
	private FileConfiguration dataConfig = null;
	private File configFile = null;
	private String name;
	
	public DataManager(PickLock plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		saveDefaultConfig();
	}

	public void reloadConfig() {
		if (this.configFile == null)
			this.configFile = new File(this.plugin.getDataFolder(), name);
		
		this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
		
		InputStream defaultStream = this.plugin.getResource(name);
		if (defaultStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
			this.dataConfig.setDefaults(defaultConfig);
		}		
	}
	
	public FileConfiguration getConfig() {
		if (this.dataConfig == null)
			reloadConfig();
		
		return this.dataConfig;
	}
	
	public void saveConfig() {
		if (this.dataConfig == null && this.configFile == null)
			return;
		
		try {
			this.getConfig().save(this.configFile);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
		}		
	}
	
	public void saveDefaultConfig() {
		if (this.configFile == null) 
			this.configFile = new File(this.plugin.getDataFolder(), name);
		
		if (!this.configFile.exists()) {
			this.plugin.saveResource(name, false);
		}		
	}
}
