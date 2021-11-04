package com.gm910.petgodmod.events;

import com.gm910.petgodmod.main.Ref;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Ref.MODID)
public class PGMEvents {

	/*
	 * @SubscribeEvent public static void l(LivingUpdateEvent event) { if
	 * (event.getEntityLiving().getServer() != null && event.getEntityLiving()
	 * instanceof Player) { if (event.getEntity().tickCount % 30 == 0) {
	 * event.getEntity().sendMessage(new
	 * TextComponent(NameBuilder.genName(event.getEntity().level.random)),
	 * UUID.randomUUID()); } } }
	 */

	@SubscribeEvent
	public static void nnn(BlockEvent.NeighborNotifyEvent event) {

	}

}
