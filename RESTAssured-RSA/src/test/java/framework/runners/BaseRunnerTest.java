package framework.runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        monochrome = true,
        dryRun = false,
        features = "src/test/resources/features",
        glue = {"framework.stepDefinitions", "framework.hooks"},
        plugin = {
                "pretty",
                "html:target/reports/cucumber-report.html",
                "json:target/reports/cucumber-report.json"
        },
        tags = "@Regression"
)
public class BaseRunnerTest extends AbstractTestNGCucumberTests {
}
