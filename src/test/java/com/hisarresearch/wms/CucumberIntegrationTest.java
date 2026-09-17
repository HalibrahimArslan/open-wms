package com.hisarresearch.wms;

import com.hisarresearch.wms.web.rest.SpringIntegrationTest;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features")
public class CucumberIntegrationTest extends SpringIntegrationTest {
}
