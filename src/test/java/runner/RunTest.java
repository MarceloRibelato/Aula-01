package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"html:target/report.html", "json:target/report/cucumber.json"},
        features = {"src/test/resources/features"},
        glue = {"steps"},
        tags = "@regressivo")

public class RunTest {

    @AfterClass
    public static void report() throws IOException {
        if (System.getProperty("os.name").equals("Windows 10"))
            Runtime.getRuntime().exec("cmd.exe /c mvn cluecumber-report:reporting");
    }
}
