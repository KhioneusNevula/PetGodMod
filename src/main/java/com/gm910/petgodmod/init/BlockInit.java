package com.gm910.petgodmod.init;

import com.gm910.petgodmod.blocks.GodLogBlock;
import com.gm910.petgodmod.blocks.GodSeedBlock;
import com.gm910.petgodmod.main.Ref;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Ref.MODID);
	public static final RegistryObject<Block> GOD_LOG = BLOCKS.register("god_log", () -> new GodLogBlock("god_log"));
	public static final RegistryObject<Block> GOD_SEED = BLOCKS.register("god_seed",
			() -> new GodSeedBlock("god_seed"));

}
