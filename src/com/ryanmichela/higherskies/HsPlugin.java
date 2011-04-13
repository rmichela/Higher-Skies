package com.ryanmichela.higherskies;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

public class HsPlugin extends JavaPlugin {

	private HsConfig config;
	
	@Override
	public void onLoad() {
		config = new HsConfig(this);
	}

	@Override
	public void onEnable() {
		getServer().getLogger().info("[Higher Skies] Enabled. Initializing worlds...");
		
		HsWorldListener listener = new HsWorldListener(config, this);
		getServer().getPluginManager().registerEvent(Type.CHUNK_LOAD, listener, Priority.Normal, this);
		
		int loadedChunks = 0;
		int processedChunks = 0;
		for(World w : getServer().getWorlds()) {
			loadedChunks += w.getLoadedChunks().length;
		}
		
		// Drop the initially loaded world
		for(World w : getServer().getWorlds()) {
			int i = 0;
			for(Chunk c : w.getLoadedChunks()) {
				if(listener.lowerChunk(c)) i++;
				if(i > 10) {
					getServer().getLogger().info("[Higher Skies] Saving " + ((processedChunks * 100) / loadedChunks) + "%");
					w.save();
					i = 0;
				}
				processedChunks++;
			}
			w.save();
		}
		
		getServer().getLogger().info("[Higher Skies] Complete!.");
		
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}
}
