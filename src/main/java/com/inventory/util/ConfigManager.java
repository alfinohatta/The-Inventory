package com.inventory.util;

import java.io.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    
    public static void initialize() {
        properties = new Properties();
        
        // Set default values
        properties.setProperty("supabase.url", "https://your-project.supabase.co");
        properties.setProperty("supabase.api_key", "your-anon-key");
        properties.setProperty("count_non_usable_as_outgoing", "true");
        properties.setProperty("allow_negative_stock", "false");
        properties.setProperty("week_start_day", "1"); // 1 = Monday
        
        // Load existing config if available
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
                logger.info("Configuration loaded from file");
            } catch (IOException e) {
                logger.warn("Could not load existing config, using defaults", e);
            }
        }
        
        // Save default config if file doesn't exist
        if (!configFile.exists()) {
            saveConfig();
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key, "false"));
    }
    
    public static int getIntProperty(String key) {
        try {
            return Integer.parseInt(properties.getProperty(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveConfig();
    }
    
    public static void setProperty(String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
        saveConfig();
    }
    
    public static void setProperty(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
        saveConfig();
    }
    
    private static void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Inventory System Configuration - Supabase");
            logger.info("Configuration saved to file");
        } catch (IOException e) {
            logger.error("Failed to save configuration", e);
        }
    }
    
    public static boolean isCountNonUsableAsOutgoing() {
        return getBooleanProperty("count_non_usable_as_outgoing");
    }
    
    public static boolean isAllowNegativeStock() {
        return getBooleanProperty("allow_negative_stock");
    }
    
    public static String getSupabaseUrl() {
        return getProperty("supabase.url");
    }
    
    public static String getSupabaseApiKey() {
        return getProperty("supabase.api_key");
    }
} 