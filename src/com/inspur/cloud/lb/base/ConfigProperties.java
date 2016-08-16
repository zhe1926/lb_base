package com.inspur.cloud.lb.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
	public static ConfigProperties instance = null;
	private Properties prop = new Properties();

	public static ConfigProperties getInstance() {
		if (instance == null) {
			instance = new ConfigProperties();
		}
		return instance;
	}

	private ConfigProperties() {
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(
				"lb_base.properties");
		try {
			this.prop.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				stream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getValue(String key) {
		return this.prop.getProperty(key, "");
	}
}