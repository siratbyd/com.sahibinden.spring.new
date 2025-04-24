package utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigManager {
    private static final String TEST_CONFIG_FILE = Paths.get("src/test/resources/cucumber.properties").toString();
    private static final Logger LOGGER = LogManager.getLogger(ConfigManager.class);
    private static Map<String, String> configMap = new HashMap<>();
    private static Properties properties = new Properties();

    private ConfigManager() {
    }
    public static synchronized String getConfigProperty(String key) {
        if (configMap.isEmpty()) {
            Properties properties = new Properties();
            try {
                properties.load(Files.newInputStream(Paths.get(TEST_CONFIG_FILE)));
                configMap = new HashMap<>(properties.entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey().toString(),
                        e -> e.getValue().toString())));
                LOGGER.debug("Loaded config properties : " + TEST_CONFIG_FILE);
            } catch (Exception e) {
                LOGGER.error(e);
                throw new RuntimeException(e);
            }
        }
        return configMap.get(key);
    }

    static {
        try{
            FileInputStream file = new FileInputStream("configuration.properties");
            properties.load(file);
            file.close();
        } catch (IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    public static String getProperty(String keyword){
        return properties.getProperty(keyword);
    }
}
