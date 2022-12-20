package io.github.betterclient.parry.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {
	public Config config;
	public CheckboxWidget widget;
	public SliderWidget sliderWidget;
	private ButtonWidget buttonWidget;

	public ConfigScreen(Config config) {
		super(Text.empty());
		this.config = config;
	}

	@Override
	@SuppressWarnings("all")
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.renderBackground(matrices);

		assert client != null;
		client.textRenderer.draw(matrices, "BetterParry mod settings!", width / 2 - (client.textRenderer.getWidth("BetterParry mod settings!") / 2), 50, -1);

		config.shouldProiritirizeShield = widget.isChecked();

		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected void init() {
		buttonWidget = ButtonWidget.builder(Text.literal("Animation Version: " + (config.animationVersion ? "1.8" : "1.7")), buttonWidget1 -> {
			buttonWidget.setMessage(Text.literal("Animation Version: " + (config.animationVersion ? "1.7" : "1.8")));
			config.animationVersion = !config.animationVersion;
		}).width(200).position(width / 2 - 100, 100).build();

		widget = new CheckboxWidget(width / 2 - 100, 130, 200, 20, Text.literal("Should Prioritize Shield"), config.shouldProiritirizeShield) {
			@Override
			public void onPress() {
				config.shouldProiritirizeShield = isChecked();
				super.onPress();
			}
		};

		sliderWidget = new SliderWidget(width / 2 - 100, 160, 200, 20, Text.literal("Multiplier"), config.multiplier) {
			@Override protected void updateMessage() {} //Don't update the message
			@Override protected void applyValue() { config.multiplier = value; }
		};

		addDrawableChild(buttonWidget);
		addDrawableChild(widget);
		addDrawableChild(sliderWidget);
	}

	@Override
	@SuppressWarnings("all") //Fuck the warning
	public List<ClickableWidget> getButtons() {
		try {
			Field drawablesField = getClass().getField("drawables");
			List<ClickableWidget> widgets = new ArrayList<>();

			for (Drawable drawable : (List<Drawable>) drawablesField.get(this)) {
				if(drawable instanceof ClickableWidget)
					widgets.add((ClickableWidget) drawable);
			}
			return widgets;
		} catch (Exception ignored) { }

		return null;
	}
	@Override
	public ItemRenderer getItemRenderer() {
		return this.itemRenderer;
	}
	@Override
	public TextRenderer getTextRenderer() {
		return this.textRenderer;
	}
	@Override
	public MinecraftClient getClient() {
		return this.client;
	}
}
