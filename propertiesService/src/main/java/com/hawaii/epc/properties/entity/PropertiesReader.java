package com.hawaii.epc.properties.entity;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@ApplicationScoped
public class PropertiesReader {

    private Properties properties;

    private String propertiesFileName = "hawaii.properties";

    @PostConstruct
    public void init() {
        properties = new Properties();
        loadProperties(propertiesFileName);
    }

    private void loadProperties(String propertiesFileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("Properties file '" + propertiesFileName + "' not found in the classpath");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading properties file", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
