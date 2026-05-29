//TODO
// literally the whole original purpose of this
// make bubble columns transfer through it

package net.echo.echotweaks.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class IronMeshBlock extends Block implements Waterloggable {
	public static final MapCodec<IronMeshBlock> CODEC = createCodec(IronMeshBlock::new);  
	public static final int MAX_DISTANCE = 2;
	public static final IntProperty DISTANCE = IntProperty.of("distance", 0, MAX_DISTANCE); //TODO make this work
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

	public static Settings createSettings() {
		return Settings.create()
			.requiresTool()
			.hardness(5)
			.resistance(5)
			.nonOpaque(); //TODO figure out how to properly do this
	}
	public IronMeshBlock(Settings settings) {
		super(settings);
		setDefaultState(stateManager.getDefaultState()
			.with(DISTANCE, MAX_DISTANCE)
			.with(WATERLOGGED, false)
			.with(FACING, Direction.NORTH)
		);
	}
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{DISTANCE, WATERLOGGED, FACING});
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

		return getDefaultState()
			.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
			.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER)
			;
	}
	protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if(!world.isClient()) {
			world.scheduleBlockTick(pos, this, 1);
		}
	}

	protected FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public static int calculateDistance(BlockView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if(blockState.get(WATERLOGGED))
			return 0;
		
		int i = MAX_DISTANCE;
		BlockPos.Mutable checkingPos = pos.mutableCopy();
		for(Direction direction : Direction.Type.HORIZONTAL) {
			BlockState checkingBlockState = world.getBlockState(checkingPos.set(pos, direction));
			//TODO
			// if adjacent block has supporting top edge
			// return 0;

			if (!checkingBlockState.isOf(ModBlocks.IRON_MESH)) 
				continue;

			i = Math.min(i, checkingBlockState.get(DISTANCE) + 1);
			if (i == 1)
				break;
		}

		return i;
	}


	public MapCodec<IronMeshBlock> getCodec() {
		return CODEC;
	}
}
