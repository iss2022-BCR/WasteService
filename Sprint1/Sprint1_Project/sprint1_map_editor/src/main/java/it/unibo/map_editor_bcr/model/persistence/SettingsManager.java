package it.unibo.map_editor_bcr.model.persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import it.unibo.map_editor_bcr.model.settings.LogLevel;
import it.unibo.map_editor_bcr.model.settings.Settings;

public class SettingsManager {
	private static SettingsManager instance = null;
	private static Settings settings;
	
	private SettingsManager()
	{
		settings = new Settings();
	}

	public static synchronized SettingsManager getInstance()
	{
		if(instance == null)
			instance = new SettingsManager();
		return instance;
	}

	public String getRoomMapFile() {return settings.getRoomMapFile();}
	public void setRoomMapFile(String roomMapFile) {settings.setRoomMapFile(roomMapFile);}
	public String getMapConfigFile() {return settings.getMapConfigFile();}
	public void setMapConfigFile(String mapConfigFile) {settings.setMapConfigFile(mapConfigFile);}
	public LogLevel getLogLevel() {return settings.getLogLevel();}
	public void setLogLevel(boolean toStdOutput, boolean toFile, String directory) {settings.setLogLevel(toStdOutput, toFile, directory);}
	public boolean isConfirmations() {return settings.isConfirmations();}
	public void setConfirmations(boolean confirmations) {settings.setConfirmations(confirmations);}
	public boolean isDarkTheme() {return settings.isDarkTheme();}
	public void setDarkTheme(boolean darkTheme) {settings.setDarkTheme(darkTheme);}
	public String getCoordinateColor() {return settings.getCoordinateColor();}
	public void setCoordinateColor(String coordinateColor) {settings.setCoordinateColor(coordinateColor);}
	
	public void loadSettings(String filename) {
		try (FileReader reader = new FileReader(filename)) {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).create(); // need this since we use a static class
			
			settings = gson.fromJson(reader, Settings.class);
			
			// the file exist, but it's empty => settings get loaded as null
			if (settings == null)
			{
				System.out.println("Settings file missing, it will be recreated with default values.");
				this.resetSettings(filename);
			}
			
			/*System.out.println("Impostazioni caricate");
			settings.toString();*/
		} catch (NullPointerException | IOException | JsonIOException | JsonSyntaxException e) {
			System.out.println("Settings file corrupted or missing, it will be recreated with default values.");
			this.resetSettings(filename);
		}
	}
	
	private void saveSettings(Settings s, String filename) {
		try(FileWriter writer = new FileWriter(filename)) {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setPrettyPrinting().create();
			gson.toJson(s, writer);
	        
			/*System.out.println("Impostazioni salvate");
	        settings.toString();*/
		} catch (JsonIOException | IOException e) {
			System.out.println("An error has occurred while saving the settings file. It will be restored with default values.");
			this.resetSettings(filename);
		}
	}
	public void saveSettings(String filename) {
		this.saveSettings(settings, filename);
	}
	
	public void resetSettings(String filename) {
		
		try(FileWriter writer = new FileWriter(filename)) {
			settings = new Settings();

			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).setPrettyPrinting().create();
			gson.toJson(settings, writer);
			
			/*System.out.println("Impostazioni predefinite ripristinate");
			settings.toString();*/
		} catch (JsonIOException | IOException e) {
			System.out.println("An error has occurred during the restoring of the settings file.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Theme: " + (this.isDarkTheme() ? "dark" : "light") + "\n");
		// TO-DO

		return sb.toString();
	}
}
