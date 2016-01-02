package com.stuff.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private Properties p;
	private static Config instance = new Config();
	
	public static Config getInstance()
	{
		return instance;
	}
	
	private Config()
	{
		InputStream in = getClass().getResourceAsStream("/com/stuff/dashboard/conf.properties");
		p = new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			p = null;
		}
	}
	
	public String get(String key)
	{
		return p == null ? null : p.getProperty(key);
	}
	
	
	public static void main(String[] args)
	{
		String string = Config.getInstance().get("upload.dir");
		System.out.println(string);
	}
}
