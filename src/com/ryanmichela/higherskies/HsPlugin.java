//Copyright (C) 2011  Ryan Michela
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

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
		
		int drop = config.lowerWorldBy();
		
		if(drop != 0) {
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
		}
		
		getServer().getLogger().info("[Higher Skies] Complete!.");
		
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}
}
