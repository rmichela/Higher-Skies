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
		lowerChunk(event.getChunk());
	}
	
	public boolean lowerChunk(Chunk chunk) {
		// Note: Lowered chunks are marked with Brick at 0,0,0
		int drop = config.lowerWorldBy();
		
		if(chunk.getBlock(0, 0, 0).getType() != Material.BRICK && drop != 0) {
			
			// Drop the chunk
			for(int y = 0; y < 127 - drop; y++) {
				for(int x = 0; x < 16; x++) {
					for(int z = 0; z < 16; z++) {
						
						// Lay a bedrock floor
						if(y == 0 && x != 0 && z != 0) {
							chunk.getBlock(x, 0, z).setType(Material.BEDROCK);
							continue;
						}
						
						// Set the marker block
						if(y == 0 && x == 0 && z == 0) {
							chunk.getBlock(0, 0, 0).setType(Material.BRICK);
							continue;
						}
						
						// Cap the marker block
						if(y == 1 && x == 0 && z == 0) {
							chunk.getBlock(0, 1, 0).setType(Material.BEDROCK);
							continue;
						}
						
						// Drop all other blocks
						Block b = chunk.getBlock(x, y + drop, z);
						chunk.getBlock(x, y, z).setTypeIdAndData(b.getTypeId(), b.getData(), false);
					}
				}
			}
			return true;
		} else return false;
	}
}
