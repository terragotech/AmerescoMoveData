package com.amereco;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesReader {
	private static Properties properties = null;

	public PropertiesReader() {

	}

	private void init() {
		FileInputStream inputStream = null;
		try {
			properties = new Properties();
			String path = "./src/main/resources/main.properties";
//			String path = "./resources/main.properties";
			inputStream = new FileInputStream(path);
			properties.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Properties getProperties() {
		if (properties == null) {
			PropertiesReader propertyReader = new PropertiesReader();
			propertyReader.init();
		}
		return properties;
	}

}
