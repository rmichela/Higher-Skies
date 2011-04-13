package com.ryanmichela.higherskies;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.bukkit.plugin.Plugin;

public class HsConfig {
	private Plugin plugin;
	
	public HsConfig(Plugin plugin) {
		this.plugin = plugin;
		
		// Initialize config if necessary
		if(!plugin.getDataFolder().exists()) {
			try {
				plugin.getDataFolder().mkdir();
				
				OutputStreamWriter out = new OutputStreamWriter(
						new FileOutputStream(
							new File(plugin.getDataFolder(), "config.yml")));
				out.write(defaultConfig());
				out.close();
				plugin.getServer().getLogger().info("[Higher Skies] Initializing config.");
				
				plugin.getConfiguration().load();
				
			} catch(Exception e) {
				plugin.getServer().getLogger().severe("[Higher Skies] Failed to initialize config.");
			}
		}
	}
	
	public int lowerWorldBy() {
		int lower = plugin.getConfiguration().getInt("lowerWorldBy", 0);
		if(lower < 0) lower = 0;
		if(lower >127) lower = 127;
		return lower;
	}
	
	public String defaultConfig() {
		return
		"lowerWorldBy: 0\n";
	}
}
