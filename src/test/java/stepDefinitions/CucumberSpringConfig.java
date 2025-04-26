package stepDefinitions;

import com.sahibinden.auto.spring.TestApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = TestApplication.class)
public class CucumberSpringConfig {
}
