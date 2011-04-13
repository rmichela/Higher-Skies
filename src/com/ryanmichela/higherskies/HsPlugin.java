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
		getServer().getLogger().info("[Higher Skies] Enabled. Processing loaded chunks...");
		
		HsWorldListener listener = new HsWorldListener(config, this);
		getServer().getPluginManager().registerEvent(Type.CHUNK_LOAD, listener, Priority.Normal, this);
		
		// Drop the initially loaded world
		for(World w : getServer().getWorlds()) {
			for(Chunk c : w.getLoadedChunks()) {
				listener.lowerChunk(c);
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
