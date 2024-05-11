package br.com.joaotadeu.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/br/com/joaotadeu/features/Parodify.feature",
        glue = "br.com.joaotadeu.steps.steps_parodify",
        plugin = {"pretty", "html: logs/report-html", "json: logs/report.json","json:target/cucumber.json","junit:target/cucumber.xml"},
        tags = "@Parodify"
)
public class Runner {

}