package com.gm910.petgodmod.blocks;

import javax.annotation.Nullable;

import com.gm910.petgodmod.api.ServerPos;
import com.gm910.petgodmod.gods.GodData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class GodBlock extends BlockBase {

	public static enum SpellDataType {

	}

	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	public GodBlock(Properties properties, @Nullable String itemname) {
		super(properties, itemname);
		this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, Boolean.valueOf(false)));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {

		BlockState def = super.getStateForPlacement(context);
		BlockPos myPos = context.getClickedPos();
		Level ww = context.getLevel();
		BlockPos found = null;
		for (Direction dir : Direction.values()) {
			BlockPos p = myPos.relative(dir);
			if (ww.getBlockState(p).hasProperty(ACTIVATED) && ww.getBlockState(p).getValue(ACTIVATED)) {
				if (found != null) {
					if (ww instanceof ServerLevel) {
						GodData dat = GodData.load((ServerLevel) ww);
						if (dat.addBlock(new ServerPos(myPos, (ServerLevel) ww))) {
							return def.setValue(ACTIVATED, true);
						}
						((ServerLevel) ww).explode(null, myPos.getX(), myPos.getY(), myPos.getZ(), 4.0F,
								Explosion.BlockInteraction.BREAK);
						return Blocks.FIRE.defaultBlockState();
					} else {
						return def.setValue(ACTIVATED, true);
					}
				} else {
					found = p;
					def = def.setValue(ACTIVATED, true);
				}
			}
		}
		if (found == null) {
			return def.setValue(ACTIVATED, false);
		}
		return def;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir1, BlockState otherState, LevelAccessor ww,
			BlockPos myPos, BlockPos otherPos) {
		BlockState def = super.updateShape(state, dir1, otherState, ww, myPos, otherPos);
		BlockPos found = null;
		for (Direction dir : Direction.values()) {
			BlockPos p = myPos.relative(dir);
			if (ww.getBlockState(p).hasProperty(ACTIVATED) && ww.getBlockState(p).getValue(ACTIVATED)) {
				if (found != null) {
					if (ww instanceof ServerLevel) {
						GodData dat = GodData.load((ServerLevel) ww);
						if (dat.addBlock(new ServerPos(myPos, (ServerLevel) ww))) {
							return def.setValue(ACTIVATED, true);
						}
						((ServerLevel) ww).explode(null, myPos.getX(), myPos.getY(), myPos.getZ(), 4.0F,
								Explosion.BlockInteraction.BREAK);
						return Blocks.FIRE.defaultBlockState();
					} else {
						return def.setValue(ACTIVATED, true);
					}
				} else {
					found = p;
					def = def.setValue(ACTIVATED, true);
				}
			}
		}
		if (found == null) {
			return def.setValue(ACTIVATED, false);
		}
		return def;
	}

	@Override
	public void destroy(LevelAccessor w, BlockPos pos, BlockState state) {
		super.destroy(w, pos, state);
		if (w instanceof ServerLevel) {
			GodData dat = GodData.load((ServerLevel) w);
			dat.removeBlock(new ServerPos(pos, (ServerLevel) w));
		}
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> bb) {
		bb.add(ACTIVATED);
	}

}
