package com.crawler.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static Properties prop = new Properties();
	static{
		InputStream in = Config.class.getResourceAsStream("/rabbitmq.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getProperty(String key){
		return prop.getProperty(key);
	}
	public static int getIntProperty(String key){
		return Integer.parseInt(prop.getProperty(key).trim());
	}
}
