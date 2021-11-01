package com.gm910.petgodmod.api;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.mojang.math.Vector3d;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Position;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class ServerPos extends BlockPos {

	private ResourceLocation d;

	public ServerPos(GlobalPos source) {
		this(source.pos(), source.dimension().location());
	}

	public ServerPos(ServerPos source) {
		super(source);
		this.d = source.d;
	}

	public ServerPos(Entity source) {
		this(source.position(), source.level.dimension().location());
	}

	public ServerPos(BlockEntity source) {
		this(source.getBlockPos(), source.getLevel().dimension().location());
	}

	public ServerPos(Vector3d vec, ResourceLocation d) {
		super(new Vec3(vec.x, vec.y, vec.z));
		this.d = d;
	}

	public ServerPos(Position p_i50799_1_, ResourceLocation d) {
		super(p_i50799_1_);
		this.d = d;

	}

	public ServerPos(Vec3i source, ResourceLocation d) {
		super(source);
		this.d = d;
	}

	public ServerPos(Vector3d vec, ResourceKey<Level> d) {

		this(vec, d.location());
	}

	public ServerPos(Position p_i50799_1_, ResourceKey<Level> d) {
		this(p_i50799_1_, d.location());

	}

	public ServerPos(Vec3i source, ResourceKey<Level> d) {
		this(source, d.location());
	}

	public ServerPos(Vector3d vec, Level d) {
		this(vec, d.dimension());
	}

	public ServerPos(Position p_i50799_1_, Level d) {
		this(p_i50799_1_, d.dimension());

	}

	public ServerPos(Vec3 source, Level d) {
		this(source, d.dimension());
	}

	public ServerPos(int x, int y, int z, ResourceLocation d) {
		super(x, y, z);
		this.d = d;
	}

	public ServerPos(double x, double y, double z, ResourceLocation d) {
		super(x, y, z);
		this.d = d;
	}

	public ServerPos(int x, int y, int z, ResourceKey<Level> d) {
		this(x, y, z, d.location());
	}

	public ServerPos(double x, double y, double z, ResourceKey<Level> d) {
		this(x, y, z, d.location());
	}

	public ServerPos(int x, int y, int z, Level d) {
		this(x, y, z, d.dimension());
	}

	public ServerPos(double x, double y, double z, Level d) {
		this(x, y, z, d.dimension());
	}

	public ResourceLocation getD() {
		return d;
	}

	public ResourceKey<Level> getDKey() {
		return ResourceKey.create(Registry.DIMENSION_REGISTRY, d);
	}

	public ServerPos setDimension(ResourceLocation d) {
		return new ServerPos(this, d);
	}

	public ServerPos setDimension(ResourceKey<Level> d) {
		return new ServerPos(this, d);
	}

	public BlockPos getPos() {
		return new BlockPos(this);
	}

	public BlockPos castToPos() {
		return this;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof ServerPos)) {
			if (p_equals_1_ instanceof GlobalPos) {
				return this.getPos().equals(p_equals_1_)
						&& this.getD().equals(((GlobalPos) p_equals_1_).dimension().location());
			} else {
				return super.equals(p_equals_1_);
			}
		} else {
			return super.equals(p_equals_1_) && ((ServerPos) p_equals_1_).d == d;
		}
	}

	@Override
	public ServerPos above(int n) {
		return new ServerPos(super.above(n), d);
	}

	@Override
	public ServerPos above() {
		return above(1);
	}

	@Override
	public ServerPos below(int n) {
		return new ServerPos(super.below(n), d);
	}

	@Override
	public ServerPos below() {
		return below(1);
	}

	@Override
	public ServerPos north(int n) {
		return new ServerPos(super.north(n), d);
	}

	@Override
	public ServerPos north() {
		return new ServerPos(super.north(), d);
	}

	@Override
	public ServerPos south(int n) {
		return new ServerPos(super.south(n), d);
	}

	@Override
	public ServerPos south() {
		return new ServerPos(super.south(), d);
	}

	@Override
	public ServerPos east(int n) {
		return new ServerPos(super.east(n), d);
	}

	@Override
	public ServerPos east() {
		return new ServerPos(super.east(), d);
	}

	@Override
	public ServerPos west(int n) {
		return new ServerPos(super.west(n), d);
	}

	@Override
	public ServerPos west() {
		return new ServerPos(super.west(), d);
	}

	@Override
	public ServerPos relative(Direction facing, int n) {
		return new ServerPos(super.relative(facing, n), d);
	}

	@Override
	public ServerPos relative(Direction facing) {
		return new ServerPos(super.relative(facing), d);
	}

	@Override
	public ServerPos offset(double x, double y, double z) {
		return new ServerPos(super.offset(x, y, z), d);
	}

	@Override
	public ServerPos offset(int x, int y, int z) {
		return new ServerPos(super.offset(x, y, z), d);
	}

	@Override
	public ServerPos offset(Vec3i vec) {
		return new ServerPos(super.offset(vec), d);
	}

	@Override
	public String toShortString() {
		return super.toShortString() + ", d=" + d.toString();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ())
				.add("d", this.getD()).toString();
	}

	@Override
	public ServerPos immutable() {
		return this;
	}

	@Override
	public ServerPos subtract(Vec3i vec) {
		return new ServerPos(super.subtract(vec), d);
	}

	@Override
	public ServerPos rotate(Rotation rotationIn) {
		return new ServerPos(super.rotate(rotationIn), d);
	}

	/**
	 * Gets blockpos from nbt OR serverpos depending on whether the nbt is
	 * configured for a serverpos or blockpos
	 * 
	 * @param nbt
	 * @return
	 */
	public static BlockPos bpFromNBT(CompoundTag nbt) {

		if (nbt.contains("D")) {
			return new ServerPos(nbt.getInt("X"), nbt.getInt("Y"), nbt.getInt("Z"),
					new ResourceLocation(nbt.getString("D")));
		} else {
			return new BlockPos(nbt.getInt("X"), nbt.getInt("Y"), nbt.getInt("Z"));
		}
	}

	/**
	 * If returntype is not a serverpos, returns null;
	 */
	public static ServerPos fromNBT(CompoundTag nbt) {
		BlockPos pos = bpFromNBT(nbt);
		return pos instanceof ServerPos ? (ServerPos) pos : null;
	}

	/**
	 * Works for blockpos or serverpos
	 * 
	 * @param pos
	 * @return
	 */
	public static CompoundTag toNBT(Vec3i pos) {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("X", pos.getX());
		nbt.putInt("Y", pos.getY());
		nbt.putInt("Z", pos.getZ());
		if (pos instanceof ServerPos)
			nbt.putString("D", ((ServerPos) pos).getD().toString());
		return nbt;
	}

	public CompoundTag toNBT() {
		return ServerPos.toNBT(this);
	}

	public ServerLevel getWorld(MinecraftServer server) {
		return server.forgeGetWorldMap().entrySet().stream().filter((e) -> e.getKey().location().equals(d))
				.map((m) -> m.getValue()).findAny().orElse(null);
	}

	public boolean isClientInWorld(Minecraft mc) {
		return mc.level.dimension().location().equals(d);
	}

	public static Entity getEntityFromID(int en, ServerLevel server) {

		return server.getEntity(en);
	}

	public static <T> T serializeVec3d(Vector3d vec, DynamicOps<T> ops) {

		return ops.createList(
				Lists.newArrayList(ops.createDouble(vec.x), ops.createDouble(vec.y), ops.createDouble(vec.z)).stream());
	}

	public static <T> Vector3d deserializeVec3d(Dynamic<T> dyn) {
		List<Double> ls = dyn.asStream().map((d) -> d.asDouble(0)).collect(Collectors.toList());
		return new Vector3d(ls.get(0), ls.get(1), ls.get(2));
	}

}