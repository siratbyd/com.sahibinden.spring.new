package utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.testng.Assert;

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
    private static final String TEST_CONFIG_FILE = Paths.get("src/test/resources/application.properties").toString();
    private static final Logger LOGGER = LogManager.getLogger(ConfigManager.class);
    private static Map<String, String> configMap = new HashMap<>();
    private static Properties properties = new Properties();

    private ConfigManager() {
    }

    public static synchronized String getProperty(String key) {
        if (configMap.isEmpty()) {
            try {
                // Application properties
                if (Files.exists(Paths.get(TEST_CONFIG_FILE))) {
                    Properties appProperties = new Properties();
                    appProperties.load(Files.newInputStream(Paths.get(TEST_CONFIG_FILE)));
                    configMap.putAll(appProperties.entrySet()
                            .stream()
                            .collect(Collectors.toMap(e -> e.getKey().toString(),
                                    e -> e.getValue().toString())));
                    LOGGER.info("Loaded application properties from: " + TEST_CONFIG_FILE);
                }

                // Configuration properties (eskiden kullanılan)
                if (Files.exists(Paths.get("application.properties"))) {
                    Properties configProperties = new Properties();
                    configProperties.load(new FileInputStream("application.properties"));
                    configMap.putAll(configProperties.entrySet()
                            .stream()
                            .collect(Collectors.toMap(e -> e.getKey().toString(),
                                    e -> e.getValue().toString())));
                    LOGGER.info("Loaded configuration properties");
                }
            } catch (IOException e) {
                LOGGER.error("Property yüklenirken hata oluştu", e);
                throw new RuntimeException(e);
            }
        }
        return configMap.getOrDefault(key, null);
    }
}
