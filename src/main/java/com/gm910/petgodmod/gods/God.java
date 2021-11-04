package com.gm910.petgodmod.gods;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gm910.petgodmod.api.ServerPos;
import com.gm910.petgodmod.blocks.GodBlock;

import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Nameable;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class God extends CapabilityProvider<God> implements Nameable, CommandSource, INBTSerializable<CompoundTag> {

	private String name;
	private ServerLevel world;
	private ServerPos center;
	private Map<ServerPos, GodBlock> allBlocks = new HashMap<>();
	private GodData data;

	public God() {
		this("");
	}

	public God(String name) {
		super(God.class);
		this.name = name;
	}

	public GodData getData() {
		return data;
	}

	public void setData(GodData data) {
		this.data = data;
	}

	public ServerLevel getWorld() {
		return world;
	}

	protected void setWorld(ServerLevel world) {
		this.world = world;
	}

	public BlockPos getCenter() {
		return center;
	}

	protected void setCenter(ServerPos center) {
		this.center = center;
	}

	public GodBlock getBlockType(ServerPos pos) {
		return null; // TODO block type
	}

	public boolean tryAddBlock(ServerPos pos) {
		// TODO do this

		return false;
	}

	public void updateBlockStructure() {
		// TODO
	}

	public void removeBlock(ServerPos pos) {
		this.allBlocks.remove(pos);
	}

	protected void addBlock(ServerPos pos, GodBlock type) {
		this.allBlocks.put(pos, type);
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String getGodName() {
		return this.name;
	}

	public God init(ServerLevel world, BlockPos center) {
		this.world = world;
		this.center = new ServerPos(center, world);

		return this;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag data = new CompoundTag();
		data.putString("Name", name);
		data.put("Center", center.toNBT());
		return data;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.name = nbt.getString("Name");
		this.center = ServerPos.fromNBT(nbt.getCompound("Center"));
	}

	public static God fromNBT(CompoundTag nbt) {
		God god = new God();
		god.deserializeNBT(nbt);
		return god;
	}

	@Override
	public void sendMessage(Component p_80166_, UUID p_80167_) {
	}

	@Override
	public boolean acceptsSuccess() {
		return false;
	}

	@Override
	public boolean acceptsFailure() {
		return false;
	}

	@Override
	public boolean shouldInformAdmins() {
		return false;
	}

	@Override
	public Component getName() {
		return new TextComponent(name);
	}

}
