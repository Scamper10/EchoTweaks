package net.echo.echotweaks.config;

import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

@Environment(EnvType.CLIENT)
public class EchoConfigScreen extends Screen {

	private final EchoConfig config = EchoConfig.getConfig();

	private final int
		VERTICAL_PADDING = 5
	  , TITLE_VERTICAL_PADDING = 10
	  ,	LABEL_LEFT_MARGIN = 30
	  , WIDGET_RIGHT_MARGIN = LABEL_LEFT_MARGIN
	  , WHITE = 0xFFFFFFFF;

	
	public CyclingButtonWidget<DefaultPlatformBehavior> platformModeCycler;
	public TextFieldWidget platformSizeInput;
	public BlockInput
		platformBlockInput
	  ,	platformCenterBlockInput;

	public final Screen parent;

	protected EchoConfigScreen(Screen parent) {
		super(Text.translatable("text.echotweaks.config.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		platformModeCycler = CyclingButtonWidget.builder(
			(DefaultPlatformBehavior value) -> Text.translatable(value.getKey())
			, config.platformNoArgBehavior
		)
		.values(DefaultPlatformBehavior.values())
		.omitKeyText()
		.build(
			Text.empty()
		  ,	(btn, value) -> {
				config.platformNoArgBehavior = value;
				EchoConfig.save();
		  	}
		);
		addDrawableChild(platformModeCycler);

		platformSizeInput = new TextFieldWidget(
			textRenderer, 150, 20
		  ,	Text.empty()
		);
		platformSizeInput.setText(((Integer)config.defaultPlatformSize).toString());
		platformSizeInput.setChangedListener(value -> {
			StringBuilder digitsOnly = new StringBuilder();
			for(char c : value.toCharArray()) {
				if(!Character.isDigit(c)) continue;
				
				if(digitsOnly.length() >= 3) {
					digitsOnly.setCharAt(2, c);
					continue;
				}

				digitsOnly.append(c);
			} 
			String filteredValue = digitsOnly.toString();
			
			if(!filteredValue.contentEquals(value)) {
				platformSizeInput.setText(filteredValue);
			}
			
			if(filteredValue.isEmpty() || filteredValue.contentEquals(((Integer)config.defaultPlatformSize).toString())) return;
			config.defaultPlatformSize = Integer.parseInt(filteredValue);
			EchoConfig.save();
		});
		addDrawableChild(platformSizeInput);

		platformBlockInput = new BlockInput(
			config.customPlatformBlock
		  ,	value -> {
				String namespacedId = Identifier.of(value).toString();
				config.customPlatformBlock = namespacedId;
				EchoConfig.save();
			}
		);
		addDrawableChild(platformBlockInput);
		
		platformCenterBlockInput = new BlockInput(
			config.customPlatformCenterBlock
		  ,	value -> {
				String namespacedId = value.isEmpty() ? value : Identifier.of(value).toString();
				config.customPlatformCenterBlock = namespacedId;
				EchoConfig.save();
			}
		  ,	true
		);
		addDrawableChild(platformCenterBlockInput);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		decideVisibility();

		super.render(context, mouseX, mouseY, delta);

		Integer y = TITLE_VERTICAL_PADDING/2;

		context.drawHorizontalLine(0, width, y, WHITE);

		y += 1 + TITLE_VERTICAL_PADDING;
		context.drawCenteredTextWithShadow(textRenderer, title, width/2, y, WHITE);
		y += textRenderer.fontHeight + TITLE_VERTICAL_PADDING;
		context.drawHorizontalLine(0, width, y, WHITE);
		
		y += 1 + TITLE_VERTICAL_PADDING;
		
		y += drawPair(context, "platformNoArgBehavior", platformModeCycler, y);
		y += drawPair(context, "defaultPlatformSize", platformSizeInput, y);
		y += drawPair(context, "customPlatformBlock", platformBlockInput, y);
		y += drawPair(context, "customPlatformCenterBlock", platformCenterBlockInput, y);
	}

	@Override
	public void close() {
		EchoConfig.save();
		this.client.setScreen(parent);
	}

	public int drawPair(DrawContext context, String keySuffix, ClickableWidget widget, Integer y) {
		if(!widget.visible) return 0;

		final int labelYOffset = (widget.getHeight()+1-textRenderer.fontHeight)/2;
		
		widget.setPosition(width-widget.getWidth()-WIDGET_RIGHT_MARGIN, y);
		widget.visible = true;

		context.drawText(
				textRenderer
			  ,	Text.translatable("text.echotweaks.config.label." + keySuffix)
			  ,	LABEL_LEFT_MARGIN, y+labelYOffset
			  ,	WHITE
			  ,	true
		);

		return widget.getHeight() + VERTICAL_PADDING;
	}

	private void decideVisibility() {
		platformSizeInput.visible = config.platformNoArgBehavior != DefaultPlatformBehavior.VOID_WORLD;
		platformBlockInput.visible = config.platformNoArgBehavior == DefaultPlatformBehavior.CUSTOM;
		platformCenterBlockInput.visible = platformSizeInput.visible && config.defaultPlatformSize > 0;
	}

	private void setInvalid(boolean invalid) {
		if(invalid) {
			// disallow exit
			// possibly add discard changes screen
			return;
		}

		// re-allow exit and save
	}

	private class BlockInput extends TextFieldWidget {
		
		private final int
			INVALID_ACTIVE_COLOR = 0xFFE01111
		,	INVALID_INACTTIVE_COLOR = 0xFFA00F0F;

		private boolean invalid = false;

		BlockInput(String initialText, Consumer<String> onChange, boolean canBeEmpty) {
			super(textRenderer, 150, 20, Text.empty());
			this.setText(initialText);
			this.setChangedListener(value -> {
				boolean
					isEmpty = value.isEmpty()
				  ,	isBlock;

				try {
					isBlock = Registries.BLOCK.containsId(Identifier.of(value));
				} catch (InvalidIdentifierException e) {
					isBlock = false;
				}

				if(isEmpty && !canBeEmpty || !isEmpty && !isBlock) {
					setWidgetInvalid(true);
					return;
				}
				
				setWidgetInvalid(false);
				onChange.accept(value);
			});
		}
		BlockInput(String initialText, Consumer<String> onChange) {
			this(initialText, onChange, false);
		}

		@Override
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
			super.renderWidget(context, mouseX, mouseY, deltaTicks);

			int color = invalid ?
				isActive() ?
					INVALID_ACTIVE_COLOR
				  :	INVALID_INACTTIVE_COLOR
			  :	DEFAULT_EDITABLE_COLOR;
			context.drawStrokedRectangle(getX(), getY(), getWidth(), getHeight(), color);

		}

		public void setWidgetInvalid(boolean invalid) {
			this.invalid = invalid;
			setInvalid(invalid);
			setEditableColor(invalid ? INVALID_ACTIVE_COLOR : DEFAULT_EDITABLE_COLOR);
		}
	}
}
