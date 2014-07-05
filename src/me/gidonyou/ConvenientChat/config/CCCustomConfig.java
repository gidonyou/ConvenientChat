package me.gidonyou.ConvenientChat.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.gidonyou.ConvenientChat.ConvenientChat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CCCustomConfig {

	ConvenientChat plugin;
	private String fileName;

	private static List<CCCustomConfig> allteam = new ArrayList<CCCustomConfig>();

	public CCCustomConfig(ConvenientChat instance, String name) {
		plugin = instance;
		this.fileName = name;
		allteam.add(this);
	}

	private FileConfiguration customConfig = null;
	private File customConfigFile = null;

	public void reload() {
		if (customConfigFile == null) {
			customConfigFile = new File(plugin.getDataFolder(), fileName);
		}
		customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

		// Look for defaults in the jar
		InputStream defConfigStream = plugin.getResource(fileName);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			customConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getCustomConfig() {
		if (customConfig == null) {
			reload();
		}
		return customConfig;
	}

	public void saveCustomConfig() {
		if (customConfig == null || customConfigFile == null) {
			return;
		}
		try {
			getCustomConfig().save(customConfigFile);
		} catch (IOException ex) {
			// TODO Fix This
			// getLogger().log(Level.SEVERE, "Could not save config to " +
			// customConfigFile, ex);
		}
	}

	public void saveDefaultConfig() {
		if (customConfigFile == null) {
			customConfigFile = new File(plugin.getDataFolder(), fileName);
		}
		if (!customConfigFile.exists()) {
			plugin.saveResource(fileName, false);
		}
	}

	public static List<CCCustomConfig> getAllTeam() {
		return allteam;
	}

	public static CCCustomConfig getConfig(String name) {
		for (CCCustomConfig config : allteam) {
			if (config.fileName.toLowerCase().trim() == name.toLowerCase()
					.trim())
				return config;
		}
		return null;
	}

}