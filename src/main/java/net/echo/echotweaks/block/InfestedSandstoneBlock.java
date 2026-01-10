/* TODO
 * make light level configurable
 * make real textures
 * use harming pots to 'cleanse'
 * add loot table
 * chance for silverfish to inhabit nearby pure sandstone instead of die
 */

package net.echo.echotweaks.block;

import java.util.Optional;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.InfestedBlock;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class InfestedSandstoneBlock extends InfestedBlock {
	public static final IntProperty DECAY = IntProperty.of("decay", 0, 2);

	InfestedSandstoneBlock(AbstractBlock.Settings settings) {
		super(Blocks.SANDSTONE, settings);
		setDefaultState(getDefaultState()
			.with(DECAY, 0)
		);
	}

	public static AbstractBlock.Settings createBlockSettings() {
		// Hardness, Blast Resistance are handled in super constructor
		return AbstractBlock.Settings.create()
			.ticksRandomly();
	}
	public static Optional<Item.Settings> createItemSettings() {
		return ModBlocks.ItemSettingsHelper.DEFAULT_ITEM;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder
			.add(DECAY);
	}

	// for harming potion
	// @Override
	// protected boolean hasRandomTicks(BlockState state) {
	// 	return state.get(HAS_SILVERFISH);
	// }

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		// if(world.getLightLevel(pos) > 9) return; // light level inside block always 0, use surroundings?
		
		int age = state.get(DECAY);
		if(age == 2) {
			this.transmute(world, pos, state);
			return;
		}

		world.setBlockState(pos, state.with(DECAY, age + 1));
	}

	private void transmute(ServerWorld world, BlockPos pos, BlockState oldState) {

		createKillEffects(world, pos, oldState);

		BlockState sand = Blocks.SAND.getDefaultState();
		world.setBlockState(pos, sand);
	}

	private void createKillEffects(ServerWorld world, BlockPos pos, BlockState state) {
		world.playSound(null, pos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
		world.playSound(null, pos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS);
		Vec3d center = pos.toCenterPos();
		int count = 20;
		double spread = 0.25;
		double speed = 0.025;
		world.spawnParticles(ParticleTypes.POOF, center.getX(), center.getY(), center.getZ(), count, spread, spread, spread, speed);
	}
}
