package io.github.betterclient.parry.mixin;

import io.github.betterclient.parry.BetterParryMod;
import io.github.betterclient.parry.config.Config;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {
	@Inject(method = "stop", at = @At("HEAD"))
	public void onClose(CallbackInfo ci) { //SaveConfig
		try {
			Config.save(BetterParryMod.getBetterParryMod().config);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
