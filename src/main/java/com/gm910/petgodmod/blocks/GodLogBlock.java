package com.gm910.petgodmod.blocks;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GodLogBlock extends GodBlock {
	private static final Direction[] DIRECTIONS = Direction.values();
	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;
	public static final BooleanProperty UP = BlockStateProperties.UP;
	public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
	public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap
			.copyOf(Util.make(Maps.newEnumMap(Direction.class), (p_55164_) -> {
				p_55164_.put(Direction.NORTH, NORTH);
				p_55164_.put(Direction.EAST, EAST);
				p_55164_.put(Direction.SOUTH, SOUTH);
				p_55164_.put(Direction.WEST, WEST);
				p_55164_.put(Direction.UP, UP);
				p_55164_.put(Direction.DOWN, DOWN);
			}));
	protected final VoxelShape[] shapeByIndex;

	public GodLogBlock(String itemname) {
		super(BlockBehaviour.Properties.of(Material.WOOD).explosionResistance(Blocks.OBSIDIAN.getExplosionResistance()),
				itemname);
		this.shapeByIndex = this.makeShapes(0.3125F);
		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false))
				.setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false))
				.setValue(WEST, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(false))
				.setValue(DOWN, Boolean.valueOf(false)));

	}

	private VoxelShape[] makeShapes(float p_55162_) {
		float f = 0.5F - p_55162_;
		float f1 = 0.5F + p_55162_;
		VoxelShape voxelshape = Block.box((double) (f * 16.0F), (double) (f * 16.0F), (double) (f * 16.0F),
				(double) (f1 * 16.0F), (double) (f1 * 16.0F), (double) (f1 * 16.0F));
		VoxelShape[] avoxelshape = new VoxelShape[DIRECTIONS.length];

		for (int i = 0; i < DIRECTIONS.length; ++i) {
			Direction direction = DIRECTIONS[i];
			avoxelshape[i] = Shapes.box(0.5D + Math.min((double) (-p_55162_), (double) direction.getStepX() * 0.5D),
					0.5D + Math.min((double) (-p_55162_), (double) direction.getStepY() * 0.5D),
					0.5D + Math.min((double) (-p_55162_), (double) direction.getStepZ() * 0.5D),
					0.5D + Math.max((double) p_55162_, (double) direction.getStepX() * 0.5D),
					0.5D + Math.max((double) p_55162_, (double) direction.getStepY() * 0.5D),
					0.5D + Math.max((double) p_55162_, (double) direction.getStepZ() * 0.5D));
		}

		VoxelShape[] avoxelshape1 = new VoxelShape[64];

		for (int k = 0; k < 64; ++k) {
			VoxelShape voxelshape1 = voxelshape;

			for (int j = 0; j < DIRECTIONS.length; ++j) {
				if ((k & 1 << j) != 0) {
					voxelshape1 = Shapes.or(voxelshape1, avoxelshape[j]);
				}
			}

			avoxelshape1[k] = voxelshape1;
		}

		return avoxelshape1;
	}

	public boolean propagatesSkylightDown(BlockState p_55166_, BlockGetter p_55167_, BlockPos p_55168_) {
		return false;
	}

	public VoxelShape getShape(BlockState p_55170_, BlockGetter p_55171_, BlockPos p_55172_,
			CollisionContext p_55173_) {
		return this.shapeByIndex[this.getAABBIndex(p_55170_)];
	}

	protected int getAABBIndex(BlockState p_55175_) {
		int i = 0;

		for (int j = 0; j < DIRECTIONS.length; ++j) {
			if (p_55175_.getValue(PROPERTY_BY_DIRECTION.get(DIRECTIONS[j]))) {
				i |= 1 << j;
			}
		}

		return i;
	}

	public BlockState getStateForPlacement(BlockPlaceContext p_51709_) {
		return this.getStateForPlacement(p_51709_.getLevel(), p_51709_.getClickedPos());
	}

	public BlockState getStateForPlacement(BlockGetter p_51711_, BlockPos p_51712_) {
		BlockState blockstate = p_51711_.getBlockState(p_51712_.below());
		BlockState blockstate1 = p_51711_.getBlockState(p_51712_.above());
		BlockState blockstate2 = p_51711_.getBlockState(p_51712_.north());
		BlockState blockstate3 = p_51711_.getBlockState(p_51712_.east());
		BlockState blockstate4 = p_51711_.getBlockState(p_51712_.south());
		BlockState blockstate5 = p_51711_.getBlockState(p_51712_.west());
		// TODO make this depend on the other block
		return this.defaultBlockState().setValue(DOWN, Boolean.valueOf(blockstate.getBlock() instanceof GodBlock))
				.setValue(UP, Boolean.valueOf(blockstate1.getBlock() instanceof GodBlock))
				.setValue(NORTH, Boolean.valueOf(blockstate2.getBlock() instanceof GodBlock))
				.setValue(EAST, Boolean.valueOf(blockstate3.getBlock() instanceof GodBlock))
				.setValue(SOUTH, Boolean.valueOf(blockstate4.getBlock() instanceof GodBlock))
				.setValue(WEST, Boolean.valueOf(blockstate5.getBlock() instanceof GodBlock));
	}

	public BlockState updateShape(BlockState p_51728_, Direction p_51729_, BlockState p_51730_, LevelAccessor p_51731_,
			BlockPos p_51732_, BlockPos p_51733_) {

		boolean flag = p_51730_.getBlock() instanceof GodBlock;
		return p_51728_.setValue(PROPERTY_BY_DIRECTION.get(p_51729_), Boolean.valueOf(flag));

	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51735_) {
		p_51735_.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}

}
