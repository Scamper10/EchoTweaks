package net.echo.echotweaks;

import java.util.List;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class BigStickItem extends Item {
	public BigStickItem(Item.Settings settings) {
		super(settings);
	}

	public static final Identifier BASE_ATTACK_KNOCKBACK_ID = Identifier.of(EchoTweaks.MOD_ID, "base_attack_knockback");

	public static Item.Settings createSettings() {
		return new Item.Settings()
			.maxDamage(60)
			.component(DataComponentTypes.WEAPON, new WeaponComponent(1))
			.component(DataComponentTypes.TOOL, new ToolComponent(List.of(), 1.0F, 2, false))
			.attributeModifiers(BigStickItem.createAttributeModifiers());
	}

	public static AttributeModifiersComponent createAttributeModifiers() {
		return AttributeModifiersComponent.builder()
				.add(EntityAttributes.ATTACK_KNOCKBACK,
						new EntityAttributeModifier(BASE_ATTACK_KNOCKBACK_ID, 5.0, Operation.ADD_VALUE),
						AttributeModifierSlot.MAINHAND)
				.build();
	}
}
