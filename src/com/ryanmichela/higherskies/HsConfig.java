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
