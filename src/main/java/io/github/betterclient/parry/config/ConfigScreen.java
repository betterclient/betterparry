package io.github.betterclient.parry.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.button.ButtonWidget;
import net.minecraft.text.Text;

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
	public void render(GuiGraphics matrices, int mouseX, int mouseY, float delta) {
		super.renderBackground(matrices, mouseX, mouseY, delta);

		assert client != null;
		matrices.drawText(client.textRenderer, "BetterParry mod settings!", width / 2 - (client.textRenderer.getWidth("BetterParry mod settings!") / 2), 50, -1, false);

		config.shouldProiritirizeShield = widget.isChecked();

		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected void init() {
		buttonWidget = ButtonWidget.builder(Text.literal("Animation Version: " + (config.animationVersion ? "1.8" : "1.7")), buttonWidget1 -> {
			buttonWidget.setMessage(Text.literal("Animation Version: " + (config.animationVersion ? "1.7" : "1.8")));
			config.animationVersion = !config.animationVersion;
		}).width(200).position(width / 2 - 100, 100).build();

		widget = CheckboxWidget.builder(Text.literal("Should Prioritize Shield"), client.textRenderer).checked(config.shouldProiritirizeShield).position(width / 2 - 100, 130).maxWidth(200).checkListener((checkboxWidget, bl) -> config.shouldProiritirizeShield = bl).build();

		sliderWidget = new SliderWidget(width / 2 - 100, 160, 200, 20, Text.literal("Multiplier"), config.multiplier) {
			@Override protected void updateMessage() {} //Don't update the message
			@Override protected void applyValue() { config.multiplier = value; }
		};

		this.addDrawableSelectableElement(buttonWidget);
		this.addDrawableSelectableElement(widget);
		this.addDrawableSelectableElement(sliderWidget);
	}
}
