package com.gm910.petgodmod.api;

import java.util.List;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class ServerTools {

	public static Entity getEntityFromUUID(UUID en, ServerLevel world) {
		Entity e = world.getEntity(en);
		if (e != null)
			return e;
		return null;
	}

	public static Entity getEntityFromUUID(UUID en, MinecraftServer server) {
		for (ServerLevel world : server.forgeGetWorldMap().values()) {
			Entity e = getEntityFromUUID(en, world);
			if (e != null)
				return e;
		}
		return null;
	}

	/**
	 * Gets entities in the general area. Does not register entities trapped in
	 * anima.
	 * 
	 * @param en
	 * @param world
	 * @param pos
	 * @param range
	 * @return
	 */
	public static Entity getEntityFromUUID(UUID en, Level world, BlockPos pos, double range) {
		List<Entity> list = world.getEntitiesOfClass(Entity.class, (new AABB(pos)).inflate(range),
				(e) -> e.getUUID().equals(en));
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

}
