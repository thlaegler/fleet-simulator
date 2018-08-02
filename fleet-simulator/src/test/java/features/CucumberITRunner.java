package features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

// plugin "io.cucumber.pro.JsonReporter:all"
@RunWith(Cucumber.class)
@CucumberOptions(monochrome = false, glue = "features", features = "src/test/resources/features",
    plugin = {"pretty", "html:target/site/cucumber", "json:target/site/cucumber.json",
        "junit:target/site/cucumber.xml",
        "com.cucumber.listener.ExtentCucumberFormatter:target/site/cucumber-extent/index.html"})
public class CucumberITRunner {

  // @AfterClass
  // public static void teardown() {
  // Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
  // Reporter.setSystemInfo("user", System.getProperty("user.name"));
  // Reporter.setSystemInfo("os", "Mac OSX");
  // Reporter.setTestRunnerOutput("Sample test runner output message");
  // }
}
