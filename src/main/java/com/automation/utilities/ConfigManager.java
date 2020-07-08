package com.automation.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.testng.Assert;


/**
 * This class provides Utility Helper functions for the Config Manager
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class ConfigManager
{
	private static Map<String, Properties> configs = new HashMap<String, Properties>();
	public static String configFileName = System.getProperty("user.dir")+"\\config.properties";
	/**
	 * Loads a config into memory.
	 * @param name - name of config to load
	 */
	private static void loadConfig(String name)
	{
		Properties prop = new Properties();
		// build file path
		//System.out.println("config file name >>"+configFileName);
		try
		{
			InputStream inputStream = new FileInputStream(configFileName);
		//	Assert.assertNotEquals(inputStream, null, "config file not found:" + configFileName);
			prop.load(inputStream);
			inputStream.close();
		}
		catch (IOException e)
		{
			Assert.fail(e.getMessage());
			return;
		}
		// put properties
		configs.put(name, prop);
	}

	/** 
	 * Remove config from memory.
	 * @param name - the name of the file minus the extension
	 */
	public static void unLoadConfig(String name)
	{
		if(configs.containsKey(name))
		{
			configs.remove(name);
		}
	}

	/**
	 * Get config. Will load the file if not loaded.
	 * @param name - name of the config to return
	 * @return - config 
	 */
	public static Properties getConfig(String name)
	{
		// if not loaded then load config
		if(!configs.containsKey(name))
		{
			loadConfig(name);
		}
		return configs.get(name);
	}

	/**
	 *  get property from config.
	 * @param configName - name of the config file to fetch
	 * @param propertyName - the property to return
	 * @return - the string value in the property
	 */
	public static String getProperty( String propertyName)
	{
		
		getConfig(configFileName);
		
		return getConfig(configFileName).getProperty(propertyName).toString();
	}

	/**
	 * sets a property for a config in memory.
	 * @param configName - name of the config file for this property
	 * @param propertyName - name of the property to set
	 * @param value - the value to set the property to
	 */
	public static void setProperty( String propertyName, String value)
	{
		// set property in memory
		getConfig(configFileName).setProperty(propertyName, value);
	}

	public static void main(String[] args){


		System.out.println( getProperty("password"));

	}

}
