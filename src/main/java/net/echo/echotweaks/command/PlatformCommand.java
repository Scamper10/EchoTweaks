/*
 * existing block handling?
 * 
 * config?
 * 	  map items -> blocks (eg water bucket to water)
 * 
 * reconsider survival mode access - maybe some new item (sources block from offhand) instead
 */


package net.echo.echotweaks.command;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.echo.echotweaks.command.ModCommands.sendFeedback;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.echo.echotweaks.config.DefaultPlatformBehavior;
import net.echo.echotweaks.config.EchoConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PlatformCommand {

	private static final String
		MAIN_BLOCK_ARG		= "main"
	  ,	USE_HELD_INPUT		= "$use_held"
	  ,	SIZE_ARG			= "size"
	  ,	CENTER_BLOCK_ARG	= "center";

	private static final String TRANSLATE_PREFIX = "commands.echotweaks.platform.";

	private static final SimpleCommandExceptionType
		INVALID_ITEM	= new SimpleCommandExceptionType(Text.translatable(TRANSLATE_PREFIX.concat("fail.invalid_item")))
	  ,	SET_FAIL		= new SimpleCommandExceptionType(Text.translatable(TRANSLATE_PREFIX.concat("fail.not_set")))
	  ,	NONE_FILLED		= new SimpleCommandExceptionType(Text.translatable(TRANSLATE_PREFIX.concat("fail.none_filled")));
	
	private static final String
		SET_SUCCESS	= TRANSLATE_PREFIX.concat("success.one_filled")
	  ,	FILL_SOME	= TRANSLATE_PREFIX.concat("success.some_filled")
	  ,	FILL_ALL	= TRANSLATE_PREFIX.concat("success.all_filled");

	public static void register() {
		ModCommands.register("platform", (argBuilder, registryAccess) -> {
			return argBuilder
				.requires(source -> source.hasPermissionLevel(2))
				.executes(context -> executeNoArg(context))
				.then(CommandManager.argument(MAIN_BLOCK_ARG, BlockStateArgumentType.blockState(registryAccess))
					.executes(context -> execute(context, false, false, false))
					.then(buildSizeArgTree(registryAccess, false))
				)
				.then(CommandManager.literal(USE_HELD_INPUT)
					.executes(context -> execute(context, true, false, false))
					.then(buildSizeArgTree(registryAccess, true))
				);
		});
	}

	private static RequiredArgumentBuilder<ServerCommandSource, Integer> buildSizeArgTree(CommandRegistryAccess registryAccess, boolean useHeld) {
		return CommandManager.argument(SIZE_ARG, IntegerArgumentType.integer(0))
			.executes(context -> execute(context, useHeld, true, false))
			.then(CommandManager.argument(CENTER_BLOCK_ARG, BlockStateArgumentType.blockState(registryAccess))
				.executes(context -> execute(context, useHeld, true, true))
			);
	}

	// Does not use execute() because of spaghetti between ( boolean | _ArgumentType | blockState ) in method arguments
	private static int executeNoArg(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final EchoConfig conf = EchoConfig.getConfig();
		final DefaultPlatformBehavior behavior = conf.platformNoArgBehavior;
		final int size = conf.defaultPlatformSize;
		final PlayerEntity player = context.getSource().getPlayerOrThrow();
		final BlockPos pos = getPosUnder(player);
		switch(behavior) {
			default: case DefaultPlatformBehavior.USE_HELD: {
				final BlockState
					main = getHeldBlock(player)
				  ,	center = getBlockFromString(conf.customPlatformCenterBlock);
				return handleUnknownSize(context, pos, main, size, center);
			}
			case DefaultPlatformBehavior.VOID_WORLD: {
				final BlockState
					cobble = Blocks.COBBLESTONE.getDefaultState()
				  ,	stone = Blocks.STONE.getDefaultState();
				return handleMany(context, pos, stone, 16, cobble);
			}
			case DefaultPlatformBehavior.CUSTOM: {
				final BlockState
					main = getBlockFromString(conf.customPlatformBlock)
				  ,	center = getBlockFromString(conf.customPlatformCenterBlock);
				return handleUnknownSize(context, pos, main, size, center);
			}
		}
	}

	private static int execute(CommandContext<ServerCommandSource> context, boolean useHeld, boolean givenSize, boolean givenCenter) throws CommandSyntaxException {
		final EchoConfig conf = EchoConfig.getConfig();
		final PlayerEntity player = context.getSource().getPlayerOrThrow();
		final BlockPos pos = getPosUnder(player);

		final BlockState main = useHeld ?
			getHeldBlock(player)
		  :	BlockStateArgumentType.getBlockState(context, MAIN_BLOCK_ARG).getBlockState();

		final int size = givenSize ?
			IntegerArgumentType.getInteger(context, SIZE_ARG)
		  :	conf.defaultPlatformSize;

		final BlockState center = givenCenter ?
			BlockStateArgumentType.getBlockState(context, CENTER_BLOCK_ARG).getBlockState()
		  :	null;

		return handleUnknownSize(context, pos, main, size, center);
	}

	private static int handleSingle(CommandContext<ServerCommandSource> context, BlockPos pos, BlockState block) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		boolean success = setSingle(source, pos, block);
		if(success) {
			sendFeedback(source, true, SET_SUCCESS);
			return SINGLE_SUCCESS;
		}
		throw SET_FAIL.create();
	}
	private static boolean setSingle(ServerCommandSource source, BlockPos pos, BlockState block) {
		return source.getWorld().setBlockState(pos, block, Block.NOTIFY_LISTENERS | Block.SKIP_BLOCK_ADDED_CALLBACK);
	}

	private static int handleMany(CommandContext<ServerCommandSource> context, BlockPos pos, BlockState main, int size, BlockState center) throws CommandSyntaxException {
		final ServerCommandSource source = context.getSource();
		final int
			totalPossible = (int) Math.pow((size * 2) + 1, 2)
		  ,	totalSet = fillMany(source, pos, main, size, center);

		if(totalSet == 0) throw NONE_FILLED.create();
		if(totalSet == totalPossible) {
			sendFeedback(source, true, FILL_ALL, totalPossible);
			return totalPossible;
		}
		sendFeedback(source, true, FILL_SOME, totalSet, totalPossible);
		return totalSet;
	}
	private static int fillMany(ServerCommandSource source, BlockPos origin, BlockState main, int size, BlockState center) {
		int successCount = 0;
		for(int i = -size; i <= size; i++) {
			for(int j = -size; j <= size; j++) {
				boolean success;
				if(center != null && i == 0 && j == 0) success = setSingle(source, origin, center);
				else success = setSingle(source, origin.add(i, 0, j), main);

				if(success) successCount++;
			}
		}
		return successCount;
	}

	private static int handleUnknownSize(CommandContext<ServerCommandSource> context, BlockPos pos, BlockState main, int size, BlockState center) throws CommandSyntaxException {
		if(size == 0) return handleSingle(context, pos, main);
		return handleMany(context, pos, main, size, center);
	}

	private static BlockState getBlockFromString(String id) {
		return Registries.BLOCK.get(Identifier.of(id)).getDefaultState();
	}

	private static BlockState getHeldBlock(PlayerEntity player) throws CommandSyntaxException {
		final Item item = player.getWeaponStack().getItem();
		if(!(item instanceof BlockItem blockItem)) throw INVALID_ITEM.create();
		return blockItem.getBlock().getDefaultState();
	}

	private static BlockPos getPosUnder(PlayerEntity player) {
		return player.getBlockPos().down();
	}
}
