package net.echo.echotweaks.mixin;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

@Mixin(ZombieEntity.class)
public abstract class PlayerHeadZombies extends HostileEntity {
	private PlayerHeadZombies(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Unique private static final int INVERSE_HEAD_CHANCE = 10_000;

	@Inject(
		method="initialize"
		, at = @At(
			value = "INVOKE"
			, target = "Lnet/minecraft/entity/mob/ZombieEntity;applyAttributeModifiers(F)V"
		)
	)
	private void tryGiveHead( // hehe
		  ServerWorldAccess world
		, LocalDifficulty difficulty, SpawnReason spawnReason
		, @Nullable EntityData entityData
		, CallbackInfoReturnable<@Nullable EntityData> cir
	) {
		Random random = world.getRandom();
		if(!shouldGiveHead(random))
			return;
		
		List<? extends PlayerEntity> players = world.getPlayers();
		int playerCount = players.size();
		if(playerCount < 1)
			return;

		int chosenIndex = random.nextInt(playerCount);
		PlayerEntity player = players.get(chosenIndex);

		ItemStack headStack = new ItemStack(Blocks.PLAYER_HEAD.asItem());
		headStack.set(DataComponentTypes.PROFILE, ProfileComponent.ofStatic(player.getGameProfile()));

		equipStack(EquipmentSlot.HEAD, headStack);
		setEquipmentDropChance(EquipmentSlot.HEAD, 1); // always drop
	}

	@Unique private boolean shouldGiveHead(Random random) { // still hehe
		return getEquippedStack(EquipmentSlot.HEAD).isEmpty()
			&& random.nextInt(INVERSE_HEAD_CHANCE) == 0;
	}
}
