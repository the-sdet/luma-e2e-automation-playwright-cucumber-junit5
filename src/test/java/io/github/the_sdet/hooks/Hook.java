package io.github.the_sdet.hooks;

import com.microsoft.playwright.Page;
import io.cucumber.java.*;
import io.github.the_sdet.logger.Log;

import java.io.File;
import java.util.*;

import static io.github.the_sdet.cucumber.CucumberUtils.getFeatureNameFromScenario;
import static io.github.the_sdet.cucumber.CucumberUtils.setCurrentScenario;
import static io.github.the_sdet.factory.PlaywrightFactory.*;
import static io.github.the_sdet.util.ConfigReader.getPropertyValue;
import static io.github.the_sdet.util.ReportingUtils.parseDataForReport;

@SuppressWarnings("unused")
public class Hook {
    private static final int TEST_PASSED = 1;
    private static final int TEST_FAILED = 5;
    static String featureName;
    static HashSet<String> features = new HashSet<>();
    static HashMap<String, List<Integer>> resultSet = new HashMap<>();

    @BeforeAll
    public static void beforeAll() {
        Log.info("-----------------------------------------------------------------------------");
        Log.info("Test Started!");
        Log.info("Test Rail Update: " + getPropertyValue("testrail.update.results"));
        Log.info("Test Rail Run ID: " + getPropertyValue("testrail.runid"));
    }

    @AfterAll
    public static void afterAll() {
        closePlaywright();

        parseDataForReport(resultSet);
        String home = System.getProperty("user.dir");
        Log.info("------------------------------------------------------");
        Log.info("Reports Generated...");
        Log.info("------------------------------------------------------");
        Log.info("Cucumber: " + home + "\\testReports\\CucumberReport.html");
        Log.info("Time Line: " + home + "\\testReports\\timelineReport\\index.html");
        Log.info("Extent: " + home + "\\testReports\\ExtentReport.html");
        Log.info("Extent: " + home + "\\testReports\\ExtentReport.pdf");
        Log.info("Custom HTML Report: " + home + "\\testReports\\AutomationReport.html");
        Log.info("Test Complete!");
        Log.info("-----------------------------------------------------------------------------");
    }

    @Before
    public void beforeTest(Scenario scenario) {
        String currentFeatureName = getFeatureNameFromScenario(scenario, false);
        if (!features.contains(currentFeatureName)) {
            featureName = currentFeatureName;
            features.add(featureName);
            closePlaywright();
            Page page = initializePage();
            page.navigate(getPropertyValue(getPropertyValue("active.env") + ".url"));
        }
        setCurrentScenario(scenario);
    }

    @After
    public void afterTest(Scenario scenario) {
        String key = getFeatureNameFromScenario(scenario, false);
        ArrayList<Integer> resultIDsInt = new ArrayList<>();
        ArrayList<Integer> testcaseIds = new ArrayList<>();

        String FILEPATH = "";
        File file = new File(FILEPATH);
        if (resultSet.containsKey(getFeatureNameFromScenario(scenario, false))) {
            int passCount = resultSet.get(key).get(0);
            int failCount = resultSet.get(key).get(1);
            if (scenario.isFailed()) {
                resultSet.get(key).set(1, (failCount + 1));
            } else {
                resultSet.get(key).set(0, (passCount + 1));
            }
        } else {
            List<Integer> value = new ArrayList<>(Arrays.asList(0, 0));
            if (scenario.isFailed()) {
                value.set(1, 1);
            } else {
                value.set(0, 1);
                resultSet.put(key, value);
            }
        }
        Page activePage = getBrowserContext().pages().get(getBrowserContext().pages().size() - 1);
        if (getPropertyValue("screenshot.option").equals("only.fail")) {
            if (scenario.isFailed()) {
                Log.info("Scenario failed. Taking Screenshot.");
                scenario.attach(activePage.screenshot(new Page.ScreenshotOptions().setFullPage(true)), "image/png", "Attached Image");
            }
        } else if (getPropertyValue("screenshot.option").equals("only.pass")) {
            if (!scenario.isFailed()) {
                Log.info("Scenario passed. Taking Screenshot.");
                scenario.attach(activePage.screenshot(new Page.ScreenshotOptions().setFullPage(true)), "image/png", "Attached Image");
            }
        } else {
            Log.info("Taking Screenshot.");
            scenario.attach(activePage.screenshot(new Page.ScreenshotOptions().setFullPage(true)), "image/png", "Attached Image");
        }
    }
}