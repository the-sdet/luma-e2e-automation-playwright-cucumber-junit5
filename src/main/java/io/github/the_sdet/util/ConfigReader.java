package io.github.the_sdet.util;

import io.github.the_sdet.logger.Log;

import java.util.Properties;

/**
 * This class helps in reading the config.properties file
 */
public class ConfigReader {
    /**
     * This method reads values from a property file
     *
     * @param propertyName Name of the property, which value needs to be retrieved
     * @return String value of the property
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public static String getPropertyValue(String propertyName) {
        Properties prop = new Properties();
        try {
            prop.load(ConfigReader.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (Exception e) {
            Log.error("Ops... Could NOT read the property...", e);
        }
        return prop.getProperty(propertyName);
    }

}
