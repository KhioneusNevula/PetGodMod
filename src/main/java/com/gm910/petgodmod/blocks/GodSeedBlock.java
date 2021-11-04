package com.gm910.petgodmod.blocks;

import java.util.Random;

import com.gm910.petgodmod.blocks.tile.GodSeedTile;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class GodSeedBlock extends GodBlock implements EntityBlock {

	public GodSeedBlock(String itemname) {
		super(BlockBehaviour.Properties.of(Material.WOOD), itemname);
	}

	@Override
	public void onPlace(BlockState p_60566_, Level w, BlockPos pos, BlockState p_60569_, boolean p_60570_) {
		w.getBlockTicks().scheduleTick(pos, this, 1);

		super.onPlace(p_60566_, w, pos, p_60569_, p_60570_);
	}

	@Override
	public void tick(BlockState state, ServerLevel w, BlockPos pos, Random rand) {
		super.tick(state, w, pos, rand);
		if (state.getValue(ACTIVATED).booleanValue()) {
			return;
		}
		GodSeedTile gst = (GodSeedTile) w.getBlockEntity(pos);
		gst.generateGod();
		if (gst.hasGod()) {
			w.setBlock(pos, state.setValue(ACTIVATED, true), 1);
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		return new GodSeedTile(p_153215_, p_153216_);
	}

}
