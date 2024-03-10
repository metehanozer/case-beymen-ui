package org.mt.config;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton design config manager
 */
public class ConfigManager {

    private static volatile ConfigManager instance = null;
    private Properties properties;

    private ConfigManager() {
    }

    @Step("Config manager get instance")
    public synchronized static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                    instance.loadProperties();
                }
            }
        }
        return instance;
    }

    @Step("Load all config")
    private void loadProperties() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("app.config"));
        } catch (IOException ex) {
            throw new Error("Exception while load app.config: " + ex.getMessage());
        }
    }

    private String getProperty(String key) {
        var value = properties.getProperty(key);
        Allure.addAttachment("Property", value);
        return value;
    }

    @Step("Get integer config")
    public Integer getInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    @Step("Get integer list config")
    public List<Integer> getIntList(String key) {
        return Stream.of(getProperty(key).split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
    }

    @Step("Get string config")
    public String getStr(String key) {
        return getProperty(key);
    }

    @Step("Get string list config")
    public List<String> getStrList(String key) {
        return Arrays.asList(getProperty(key).split(","));
    }

    @Step("Set config")
    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

}
