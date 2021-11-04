package com.gm910.petgodmod.api;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3d;
import com.mojang.serialization.Dynamic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class GMNBT {

	public static <T> ListTag makeList(Iterable<T> iter, Function<T, Tag> serializer) {
		ListTag ls = new ListTag();
		for (T ob : iter) {
			ls.add(serializer.apply(ob));
		}
		return ls;
	}

	/**
	 * Gets a list nbt without the hassle of figuring out what its "type" is and all
	 * that; note that it MUST be an actual listnbt or this will throw a
	 * classcastexception
	 * 
	 * @param nbt
	 * @param name
	 * @return
	 */
	public static ListTag getList(CompoundTag nbt, String name) {
		return (ListTag) nbt.get(name);
	}

	public static ListTag makePosList(Iterable<? extends Vec3i> iter) {
		return makeList(iter, (s) -> (ServerPos.toNBT(s)));
	}

	public static ListTag makeUUIDList(Iterable<UUID> iter) {
		return makeList(iter, (s) -> {
			CompoundTag tg = new CompoundTag();
			tg.putUUID("ID", s);
			return tg;
		});
	}

	public static ListTag makeUUIDListFromEntities(Iterable<? extends Entity> iter) {
		return makeList(iter, (s) -> {
			CompoundTag tg = new CompoundTag();
			tg.putUUID("ID", s.getUUID());
			return tg;
		});
	}

	/**
	 * Returns list of CompoundTags with integers
	 * 
	 * @param iter
	 * @return
	 */
	public static ListTag makeIDListFromEntities(Iterable<? extends Entity> iter) {
		return makeList(iter, (s) -> {
			CompoundTag tg = new CompoundTag();
			tg.putInt("ID", s.getId());
			return tg;
		});
	}

	/**
	 * Returns list of Ints
	 * 
	 * @param iter
	 * @return
	 */
	public static ListTag makeIntListFromEntities(Iterable<? extends Entity> iter) {
		return makeList(iter, (s) -> {
			return IntTag.valueOf(s.getId());
		});
	}

	/**
	 * Returns list of Ints
	 * 
	 * @param iter
	 * @return
	 */
	public static IntArrayTag makeIntArrayFromEntities(Iterable<? extends Entity> iter) {
		List<Integer> ls = new ArrayList<>();
		for (Entity e : iter) {
			ls.add(e.getId());
		}
		return new IntArrayTag(ls);
	}

	public static <T extends Entity> List<T> createEntityListFromUUIDs(ListTag ls, MinecraftServer server) {
		return createList(ls, (b) -> {
			return (T) ServerTools.getEntityFromUUID(((CompoundTag) b).getUUID("ID"), server);
		});
	}

	public static <T extends Entity> List<T> createEntityListFromUUIDs(ListTag ls, ServerLevel server) {
		return createList(ls, (b) -> {
			return (T) ServerTools.getEntityFromUUID(((CompoundTag) b).getUUID("ID"), server);
		});
	}

	public static List<Entity> createEntityListFromIDs(CollectionTag<?> ls, ServerLevel server) {
		return createList(ls, (b) -> {
			if (b instanceof NumericTag) {
				return ServerPos.getEntityFromID(((NumericTag) b).getAsInt(), server);
			} else {
				return ServerPos.getEntityFromID(((CompoundTag) b).getInt("ID"), server);
			}

		});
	}

	public static List<UUID> createUUIDList(ListTag ls) {
		return createList(ls, (b) -> {
			return ((CompoundTag) b).getUUID("ID");
		});
	}

	public static void forEach(ListTag ls, Consumer<Tag> cons) {
		for (Tag base : ls) {
			cons.accept(base);
		}
	}

	public static <R extends Tag, T> List<T> createList(CollectionTag<R> ls, Function<R, T> func) {
		List<T> lws = new ArrayList<>();
		if (ls == null) {
			throw new IllegalArgumentException("Null TagTagList");
		}
		for (R b : ls) {
			lws.add(func.apply(b));
		}
		return lws;
	}

	public static <T extends Vec3i> List<T> createPosList(ListTag ls) {
		return createList(ls, (b) -> (T) ServerPos.fromNBT((CompoundTag) b));
	}

	public static List<BlockPos> createServerPosList(ListTag ls) {
		return createList(ls, (b) -> ServerPos.fromNBT((CompoundTag) b));
	}

	public static <T, K> Map<T, K> createMap(ListTag ls, Function<Tag, T> keyFunc, Function<Tag, K> valFunc) {
		Map<T, K> lws = new HashMap<>();

		for (Tag b : ls) {

			lws.put(keyFunc.apply(b), valFunc.apply(b));
		}
		return lws;
	}

	public static <T, K> Map<T, K> createMap(ListTag ls, Function<Tag, Pair<T, K>> func) {
		Map<T, K> lws = new HashMap<>();

		for (Tag b : ls) {

			Pair<T, K> p = func.apply(b);

			lws.put(p.getFirst(), p.getSecond());
		}
		return lws;
	}

	public static <T, K> ListTag makeMap(Map<T, K> map, Function<Pair<T, K>, Tag> func) {
		ListTag list = new ListTag();
		for (T key : map.keySet()) {
			list.add(func.apply(Pair.of(key, map.get(key))));
		}
		return list;
	}

	public static CompoundTag writeVec3d(Vector3d vec) {
		CompoundTag nbt = new CompoundTag();
		nbt.putDouble("X", vec.x);
		nbt.putDouble("Y", vec.y);
		nbt.putDouble("Z", vec.z);
		return nbt;
	}

	public static Vector3d readVec3d(CompoundTag nbt) {
		return new Vector3d(nbt.getDouble("X"), nbt.getDouble("Y"), nbt.getDouble("Z"));
	}

	public static Dynamic<Tag> makeDynamic(Tag data) {
		return new Dynamic<>(NbtOps.INSTANCE, data);
	}

	public static CompoundTag rectangleToTag(Rectangle rect) {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("X", (int) rect.getMinX());
		nbt.putInt("Y", (int) rect.getMinY());
		nbt.putInt("W", (int) rect.getWidth());
		nbt.putInt("H", (int) rect.getHeight());
		return nbt;
	}

	public static Rectangle rectangleFromTag(CompoundTag nbt) {
		return new Rectangle(nbt.getInt("X"), nbt.getInt("Y"), nbt.getInt("W"), nbt.getInt("H"));
	}

}