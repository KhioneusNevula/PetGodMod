package com.gm910.petgodmod.gods;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.gm910.petgodmod.api.GMNBT;
import com.gm910.petgodmod.api.ServerPos;
import com.gm910.petgodmod.main.Ref;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class GodData extends SavedData {
	public static final String FILE_ID = Ref.MODID + "_gods";
	private static final String TAG_NAME = "Gods";
	private final Map<String, God> gods = new TreeMap<>();
	private MinecraftServer server;

	public God createGod(ServerLevel world, BlockPos center) {

		String name = generateUnusedName(new Random(world.getSeed()));
		God god = new God(name);
		god.init(world, center);
		addGod(god);
		return god;

	}

	protected GodData addGod(God god) {
		this.gods.put(god.getGodName(), god);

		return this;
	}

	public God removeGod(String god) {
		return this.gods.remove(god);
	}

	public GodData removeGod(God god) {
		this.gods.remove(god.getGodName(), god);
		return this;
	}

	/**
	 * Removes a block from the god which has the appropriate structure there
	 * 
	 * @param pos
	 * @return
	 */
	public boolean removeBlock(ServerPos pos) {
		// TODO complete this
		return false;
	}

	/**
	 * Adds block to the god appropriate there
	 * 
	 * @param pos
	 * @return whether the block can be added; returns true if the block is already
	 *         right there
	 */
	public boolean addBlock(ServerPos pos) {
		// TODO complete this
		return false;
	}

	public God getGod(String name) {
		return gods.get(name);
	}

	public String generateUnusedName(Random rand) {
		String name = "";
		int threshhold = 60;
		while (gods.keySet().contains(name)) {

			name = NameBuilder.genName(rand);

			threshhold--;
			if (threshhold < 0) {
				throw new RuntimeException("Issue generating god; somehow, all generated names are already used");
			}
		}

		return name;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.put(TAG_NAME, GMNBT.makeMap(gods, (p) -> {
			CompoundTag t = new CompoundTag();
			t.putString("Name", p.getFirst());
			t.put("Data", p.getSecond().serializeNBT());
			return t;
		}));

		return tag;
	}

	public static GodData load(CompoundTag tag) {
		GodData new_ = new GodData();
		new_.gods.clear();
		new_.gods.putAll(GMNBT.createMap((ListTag) tag.get(TAG_NAME), (em) -> {
			CompoundTag e = (CompoundTag) em;
			return Pair.of(e.getString("Name"), God.fromNBT(tag.getCompound("Data")));
		}));
		return new_;

	}

	public static GodData load(Level world) {
		return load(world.getServer());
	}

	public static GodData load(MinecraftServer server) {
		GodData dat = server.overworld().getDataStorage().computeIfAbsent(GodData::load, GodData::new, FILE_ID);
		dat.server = server;
		return dat;
	}

}
