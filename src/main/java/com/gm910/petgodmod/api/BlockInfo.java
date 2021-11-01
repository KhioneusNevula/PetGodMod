package com.gm910.petgodmod.api;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;

public class BlockInfo implements INBTSerializable<CompoundTag> {

	private BlockState state;
	private CompoundTag tile = new CompoundTag();

	public BlockInfo() {
	}

	public BlockInfo(CompoundTag nbt) {
		this.deserializeNBT(nbt);
		if (state == null)
			throw new IllegalArgumentException("State of block is unreadable in nbt tag");
	}

	public BlockInfo(BlockState state) {
		this.state = state;
	}

	public BlockInfo(BlockState state, BlockEntity tile) {
		this.state = state;
		this.tile = tile.serializeNBT();
	}

	public BlockInfo(BlockState state, CompoundTag tile) {
		this.state = state;
		this.tile = tile;
	}

	public BlockInfo(Level world, BlockPos pos) {
		this.state = world.getBlockState(pos);
		this.tile = world.getBlockEntity(pos).serializeNBT();
	}

	public BlockState getState() {
		return state;
	}

	public BlockEntity createTile(BlockPos worldPos) {
		return BlockEntity.loadStatic(worldPos, state, tile);
	}

	public BlockInfo withState(BlockState state) {
		return new BlockInfo(state);
	}

	public BlockInfo withTile(BlockEntity te) {
		return new BlockInfo(state, te);
	}

	/**
	 * Places block and returns the info of the block that was previously there;
	 * null if this isn't possible
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	public BlockInfo place(Level world, BlockPos pos) {
		BlockState state1 = world.getBlockState(pos);
		if (!world.setBlock(pos, this.state, 2)) {
			return null;
		}
		BlockEntity tile1 = world.getBlockEntity(pos);
		if (this.tile != null) {
			world.setBlockEntity(this.createTile(pos));
		}
		return new BlockInfo(state1, tile1);
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.put("State", NbtUtils.writeBlockState(state));
		if (tile != null)
			nbt.put("Tile", tile);

		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.state = NbtUtils.readBlockState(nbt.getCompound("State"));
		if (nbt.contains("Tile"))
			this.tile = nbt.getCompound("Tile");

	}

	public <T> T serialize(DynamicOps<T> ops) {
		return ops.createString(this.serializeNBT().getAsString());
	}

	public static BlockInfo fromDynamic(Dynamic<?> op) {
		String data = op.asString("");
		try {
			CompoundTag nbt = NbtUtils.snbtToStructure(data);
			return new BlockInfo(nbt);
		} catch (CommandSyntaxException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		return this.serializeNBT().equals(((BlockInfo) obj).serializeNBT());
	}

	@Override
	public String toString() {
		return this.serializeNBT().toString();
	}

	public static BlockInfo fromString(String s) {
		try {
			return new BlockInfo(NbtUtils.snbtToStructure(s));
		} catch (CommandSyntaxException e) {
		}
		return null;
	}

}