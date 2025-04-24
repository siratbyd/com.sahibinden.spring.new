package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ConfigManager;

import utils.DriverManager;



import java.io.IOException;

import static base.AutomationMethods.*;
import static utils.ElementManager.findElement;

public class GeneralSteps {

    public static boolean isElementVisibilityWithSize(String elementKey) throws Exception {
        WebElement element = findElement(elementKey);
        return element.isDisplayed() && element.getSize().getHeight() > 0 && element.getSize().getWidth() > 0;
    }



    @Given("Go to url")
    public void go_to_url() throws Exception {
        String browser = System.getProperty("browser", "chrome"); // Default 'chrome' tarayıcısı

        // Tarayıcıyı parametre olarak DriverManager'a iletmek
        DriverManager.getDriver(browser).get(ConfigManager.getProperty("url"));
        waitForElementToBeVisible("CEREZLERI_KABUL_ET_BUTON",5);
//        if(isElementVisibilityWithSize("CEREZLERI_KABUL_ET_BUTON")){
//            click("CEREZLERI_KABUL_ET_BUTON");
//        }
    }

    @And("Click {string}")
    public void clickElement(String element) throws Exception {

        waitAndClick(element);
    }

    @And("Hover {string}")
    public void hover(String element) throws IOException {
        hoverOverElement(element);
    }

    @And("Switch new window")
    public void switchNewWindow() {
        switchToNewWindow();
    }

    @And("Assert that {string} element is visible")
    public void assert_that_element_is_visible(String element) throws Exception {
        boolean isVisible = isElementVisibilityWithSize(element);
        Assertions.assertTrue(isVisible, "Element is not visible: " + element);
    }

    @And("Wait {int} seconds")
    public void wait_seconds(int seconds) throws InterruptedException {
        System.out.println( "Waiting" + seconds + "seconds");
        Thread.sleep(seconds * 1000L);
        System.out.println("Waiting ended.");
    }


}
