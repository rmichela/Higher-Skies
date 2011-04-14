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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.Plugin;

public class HsWorldListener extends WorldListener{

	private HsConfig config;
	private Plugin plugin;
	
	public HsWorldListener(HsConfig config, Plugin plugin) {
		this.config = config;
		this.plugin = plugin;
	}
	
	@Override
	public void onChunkLoad(ChunkLoadEvent event) {
		if(chunkNeedsLowering(event.getChunk())) {
			lowerChunkImpl(event.getChunk());
		}
	}
	
	public boolean lowerChunk(Chunk chunk) {
		if(chunkNeedsLowering(chunk)) {
			lowerChunkImpl(chunk);
			return true;
		} else return false;
	}
	
	private boolean chunkNeedsLowering(Chunk chunk) {
		// Note: Lowered chunks are marked with Brick at 0,0,0
		int drop = config.lowerWorldBy();
		return chunk.getBlock(0, 0, 0).getType() != Material.BRICK && drop != 0;
	}
	
	private void lowerChunkImpl(Chunk chunk) {
		// Note: Lowered chunks are marked with Brick at 0,0,0
		int drop = config.lowerWorldBy();			
			
		// Drop the chunk
		for(int y = 0; y < 127; y++) {
			for(int x = 0; x < 16; x++) {
				for(int z = 0; z < 16; z++) {
					
					// Set the marker block
					if(y == 0 && x == 0 && z == 0) {
						chunk.getBlock(0, 0, 0).setType(Material.BRICK);
						continue;
					}
					
					// Lay a bedrock floor
					if(y == 0) {
						chunk.getBlock(x, 0, z).setType(Material.BEDROCK);
						continue;
					}

					// Cap the marker block
					if(y == 1 && x == 0 && z == 0) {
						chunk.getBlock(0, 1, 0).setType(Material.BEDROCK);
						continue;
					}
					
					// Drop all blocks
					if(y < 127 - drop) {						
						Block b = chunk.getBlock(x, y + drop, z);
						if(b.getType() == Material.WATER || b.getType() == Material.LAVA) {
							chunk.getBlock(x, y, z).setType(Material.AIR);
						} else {
							chunk.getBlock(x, y, z).setTypeIdAndData(b.getTypeId(), b.getData(), false);
						}
						continue;
					}
					
					// Fill in the remainder with air
					if( y >= 127 - drop) {
						chunk.getBlock(x, y, z).setType(Material.AIR);
						continue;
					}
				}
			}
		}
		chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());		
	}
}
