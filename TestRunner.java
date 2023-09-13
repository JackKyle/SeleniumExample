package runners;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import generic.BaseTest;
import generic.MasterHooks;

import java.util.ArrayList;

import org.testng.annotations.*;

//import com.cucumber.listener.Reporter;
//import cucumber.api.junit.Cucumber;

//@RunWith(Cucumber.class)

@CucumberOptions(features = { "src/test/java/featureFiles/" }, glue = { "stepDefinitions",
		"generic" },tags = {"not @ContractualSpend", "not @Exclude","not @Excluded", "not @ExistingIssue", "not @ToBeDone", "not @FalsePositive", "not @EditHistory"}, monochrome = true, strict = true, plugin = { "pretty",
				"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm", "html:target/cucumber"
				,"json:target/cucumber.json",
		})

public class TestRunner {
	private TestNGCucumberRunner testNGCucumberRunner;

	@Parameters({ "userName", "password", "browser", "url" })
	@BeforeClass
	public void setUpT(String userName, String password, String browser, String url) {
		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());

		BaseTest.setName(userName);
		BaseTest.setPassword(password);
		BaseTest.setBrowser(browser);
		BaseTest.setURL(url);
	}

	@Test(dataProvider = "features")
	public void feature(PickleEventWrapper eventwrapper, CucumberFeatureWrapper cucumberFeature) throws Throwable {
		// testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
		testNGCucumberRunner.runScenario(eventwrapper.getPickleEvent());
	}

	@DataProvider // (parallel=true)
	public Object[][] features() {
		// return testNGCucumberRunner.provideFeatures();
		return testNGCucumberRunner.provideScenarios();
	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() throws Exception {
		ArrayList<String> finalFailedTestCasesList = new ArrayList<>();
		String listOfFailedTestCasesInAString=new String();
		// Remove any duplicate values, that exist from the list
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("-------------------------LIST OF FAILED TEST CASES:-------------------------");
		System.out.println(" ");

		for (String tag : MasterHooks.failedTestCases) {
			if (!finalFailedTestCasesList.contains(tag)) {
				finalFailedTestCasesList.add(tag);
				listOfFailedTestCasesInAString+=tag +", ";
			}
		}
		System.out.println(listOfFailedTestCasesInAString);		
		System.out.println(" ");
		System.out.println("---------------------------------------------------------------------------------");
		
		testNGCucumberRunner.finish();
	}
}