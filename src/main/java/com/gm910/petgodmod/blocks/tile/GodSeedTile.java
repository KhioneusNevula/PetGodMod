package com.gm910.petgodmod.blocks.tile;

import java.util.UUID;

import com.gm910.petgodmod.gods.God;
import com.gm910.petgodmod.gods.GodData;
import com.gm910.petgodmod.init.TileInit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GodSeedTile extends BlockEntity {

	private String godName;

	public GodSeedTile(BlockPos p_155229_, BlockState p_155230_) {
		super(TileInit.GOD_SEED.get(), p_155229_, p_155230_);
	}

	public void generateGod() {
		if (this.level.isClientSide)
			return;
		GodData dat = GodData.load(this.level);
		God god = dat.createGod((ServerLevel) this.level, this.worldPosition);

		this.godName = god.getGodName();
		((ServerLevel) this.level).players().forEach((e) -> e.sendMessage(god.getDisplayName(), UUID.randomUUID()));
	}

	public void destroyGod() {
		if (this.level.isClientSide)
			return;
		if (this.godName == null)
			return;

	}

	public boolean hasGod() {
		return godName != null;
	}

	public God getGod() {
		if (this.level.isClientSide)
			return null;
		return GodData.load((ServerLevel) this.level).getGod(godName);
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		if (godName != null) {
			nbt.putString("god", godName);
		}
		return super.save(nbt);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		if (nbt.contains("god")) {
			this.godName = nbt.getString("god");
		}
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.save(super.getUpdateTag());
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		this.load(tag);
	}

}
