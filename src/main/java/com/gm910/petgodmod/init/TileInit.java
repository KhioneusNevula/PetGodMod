package com.gm910.petgodmod.init;

import java.util.function.Supplier;

import com.gm910.petgodmod.blocks.tile.GodSeedTile;
import com.gm910.petgodmod.main.Ref;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileInit {

	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, Ref.MODID);
	public static final RegistryObject<BlockEntityType<GodSeedTile>> GOD_SEED = TILES.register("god_seed",
			from(GodSeedTile::new, BlockInit.GOD_SEED));

	public static <M extends BlockEntity> Supplier<BlockEntityType<M>> from(BlockEntitySupplier<M> tile,
			Supplier<? extends Block> block) {
		return () -> BlockEntityType.Builder.of(tile, block.get()).build(null);
	}

}
