package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

@Component
public class ElementManager {

    private static final String JSON_DIRECTORY_PATH = "src/test/java/objectRepository/";

    @Autowired
    private DriverManager driverManager;

    public By getLocatorFromJson(String elementKey) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File directory = new File(JSON_DIRECTORY_PATH);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null || files.length == 0) {
            throw new FileNotFoundException("No JSON files found in directory: " + JSON_DIRECTORY_PATH);
        }

        for (File file : files) {
            JsonNode rootNode = objectMapper.readTree(file);

            if (rootNode.has(elementKey)) {
                JsonNode elementNode = rootNode.get(elementKey);

                if (!elementNode.has("element_type") || !elementNode.has("element_value")) {
                    throw new IllegalArgumentException("Invalid JSON structure. Missing element_type or element_value.");
                }


                String elementValue = elementNode.get("element_value").asText();
                String elementType = elementNode.get("element_type").asText();

                switch (elementType.toLowerCase()) {
                    case "id":
                        return By.id(elementValue);
                    case "name":
                        return By.name(elementValue);
                    case "classname":
                        return By.className(elementValue);
                    case "css":
                        return By.cssSelector(elementValue);
                    case "xpath":
                        return By.xpath(elementValue);
                    default:
                        throw new IllegalArgumentException("Unsupported element type: " + elementType);
                }
            }
        }

        throw new NoSuchElementException("Element key '" + elementKey + "' not found in JSON files.");
    }

    public WebElement findElement(String key) throws Exception {
        WebDriver driver = driverManager.getDriver(System.getProperty("browser", "chrome"));
        return driver.findElement(getLocatorFromJson(key));
    }
}
