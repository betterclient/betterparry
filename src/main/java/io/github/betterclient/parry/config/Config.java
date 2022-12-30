package io.github.betterclient.parry.config;

import com.google.gson.Gson;
import io.github.betterclient.parry.BetterParryFabric;
import io.github.betterclient.parry.BetterParryMod;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.client.ClientBrandRetriever;
import org.quiltmc.loader.api.QuiltLoader;

import java.io.IOException;
import java.nio.file.Files;

public class Config {
	public double multiplier = 0.50;
	public boolean shouldProiritirizeShield = false;
	public boolean animationVersion = false; //False for 1.7, true for 1.8(no swing)

	public static Config load() throws IOException {
		var isQuilt = ClientBrandRetriever.getClientModName().startsWith("quilt");
		var configFile = isQuilt ? QuiltLoader.getConfigDir().resolve("betterparry.json") : FabricLoader.INSTANCE.getConfigDir().resolve("betterparry.json");
		var gson = new Gson();
		if (!Files.exists(configFile)) {
			save(new Config());
		}
		return gson.fromJson(Files.newBufferedReader(configFile), Config.class);
	}

	public static void save(Config config) throws IOException {
		var configFile = QuiltLoader.getConfigDir().resolve("betterparry.json");
		var gson = new Gson();
		var writer = gson.newJsonWriter(Files.newBufferedWriter(configFile));
		writer.setIndent("    ");
		gson.toJson(gson.toJsonTree(config, Config.class), writer);
		writer.close();
	}
}
