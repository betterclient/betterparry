package io.github.betterclient.parry;

import com.mojang.blaze3d.platform.InputUtil;
import io.github.betterclient.parry.config.Config;
import io.github.betterclient.parry.config.ConfigScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBind;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BetterParryMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("BetterParry");
	public KeyBind bind = new KeyBind("Open Settings", InputUtil.KEY_RIGHT_SHIFT_CODE, "BetterParry");

	private static BetterParryMod betterParryMod;
	public Config config;

	public static BetterParryMod getBetterParryMod() {
		return betterParryMod;
	}
	public static List<Item> items = new java.util.ArrayList<>();

	public BetterParryMod() {betterParryMod = this;}

	@Override
	public void onInitialize(ModContainer mod) {
		for(Field f : Items.class.getFields()) {
			Object output = null;
			try {
				output = f.get(null);
			} catch (IllegalAccessException ignored) { }

			if(output instanceof Item) {
				items.add((Item) output);
			}
		}

		try {
			config = Config.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		KeyBindingHelper.registerKeyBinding(bind);
		LOGGER.info("Starting Mod! {}", mod.metadata().name());

		items.forEach((item) -> {
			if (item instanceof SwordItem) {
				ModelPredicateProviderRegistry.register (item, new Identifier("parrying"), (stack, world, entity, i) -> (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack) ? 1.0F: 0.0F);
			}
		});

		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			if(bind.isPressed())
				MinecraftClient.getInstance().setScreen(new ConfigScreen(config));
		});
	}
}
