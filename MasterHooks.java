package generic;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
//import runners.ExperimentalRunner;

public class MasterHooks extends BaseTest {

	public ScenarioContext scenarioContext;
	private ITestResult result;
	public BasePageObject defaultPage;
	// public ExperimentalRunner exprunner;
	public static ArrayList<String> failedTestCases = new ArrayList<>();

	@Before
	public void setUpTest(Scenario scenario) throws IOException {

		// *Set up driver instance as well as scenariocontext for sharing information
		// between steps.

		ScenarioContext.init();

		if (driver == null) {
			driver = getDriver();
			try {
				Set<String> set = driver.getWindowHandles();
				if (set.size() > 1) {
					driver.close();
					driver.quit();
					Exception e = new Exception();
					throw e;
				}
			} catch (Exception e) {
				driver = createDriver();
			}
		}

		String scenarioName = scenario.getName();
		Collection<String> tags = scenario.getSourceTagNames();

		System.out.println("[ RUNNING SCENARIO: ] " + scenarioName);
		System.out.println("[ TAGS: ]");
		for (String tag : tags) {
			System.out.print(tag + "  ");
		}
	}

	@AfterStep
	public void afterScenario(Scenario scenario) throws IOException {
		if (scenario.isFailed()) {

			String scenarioName = scenario.getName();

			System.out.println("[FAILED SCENARIO]: " + scenarioName);

			// add tags to the list for failed test cases
			Collection<String> tags = scenario.getSourceTagNames();
			for (String tag : tags) {
				if (tag.contains("OPTSPEND")) {
					failedTestCases.add(tag);
					break;
				}
			}
			// String tagString = tags.toString();
			// failedTestCases.add(tagString);

			try {
				// byte[] screenshot = ((TakesScreenshot)
				// driver).getScreenshotAs(OutputType.BYTES);
				// scenario.embed(screenshot, "image/png");
				// screenshot()
				screenshot(driver);
			} catch (WebDriverException wde) {
				System.err.println(wde.getMessage());
			} catch (ClassCastException cce) {
				cce.printStackTrace();
			}
		}
	}

	@Attachment(type = "image/png")
	public static byte[] screenshot(WebDriver driver) throws IOException/* throws IOException */ {
		byte[] screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

		Allure.getLifecycle().addAttachment(
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy_hh:mm:ss")), "image/png", "png",
				screen);
		return screen;
	}

	@After
	public void teardown(Scenario scenario) throws IOException {
		System.out.println("[ TEST RESULTS: ] " + scenario.getStatus());
		driver.close();
		driver.quit();
		driver = null;
		Runtime.getRuntime().exec("taskkill /F /IM ChromeDriver.exe");
	}

	/*
	 * @After public void tearDownAndScreenshotOnFailure(Scenario scenario) {
	 * 
	 * try {
	 * 
	 * // if(driver!=null&&scenario.getStatus()("undefined")) { // driver.quit(); //
	 * // // //THIS WILL FAIL NULL POINTER NEED TO INIT IN THE BEGINNING!!!!! // //
	 * FIGURE OUT WHERE THE RESULTS ARE BEING STORED!!!! // //ONCE THEY ARE SAVED //
	 * MOVE ON! // result.setStatus(ITestResult.FAILURE); // // // }
	 * 
	 * if (driver != null && scenario.isFailed()) { //
	 * scenario.embed(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES),
	 * "image/png"); // BasePageObject.captureScreenshot();
	 * driver.manage().deleteAllCookies(); driver.quit(); driver = null; } if
	 * (driver != null) { driver.manage().deleteAllCookies(); driver.quit(); driver
	 * = null; } } catch (Exception e) { System.out.
	 * println("Methods failed: tearDownAndScreenshotOnFailure, Exception: " +
	 * e.getMessage()); } }
	 */
}