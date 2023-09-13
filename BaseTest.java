package generic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cucumber.api.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.generalnavigation.Login;
import pages.generalnavigation.Logout;

public class BaseTest {

	public static WebDriver driver;
	public Login page;
	public Logout logoutPage;
	public BasePageObject defaultPage;
	
	// private boolean assertCalled;
	// public SoftAssert testAssert;
	
	private static String browser;
	private static String platform;
	private static String driverPath;
	private static String url;
	private static String userName;
	private static String password;
	private static String driverVersion;

	public static String featureName;

	public WebDriver createDriver() {
		getReaderContext();
		browser = getBrowser();
		userName = getName();
		password = getPassword();
		String driverName = "";
		if (platform.equalsIgnoreCase("windows")) {
			driverName = ".exe";
		}

		if (browser.equalsIgnoreCase("msedge") || browser.equalsIgnoreCase("IE")) {

			// WebDriverManager.edgedriver().setup();
			// DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
			EdgeOptions cap = new EdgeOptions();
			cap.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			cap.setCapability("ignoreZoomSetting", true);
			cap.setCapability("ie.ensureCleanSession", true);
			/*
			 * cap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			 * System.setProperty("webdriver.ie.driver", driverPath + "IEDriverServer.exe");
			 */
			driver = new EdgeDriver(cap);

		} else if (browser.equalsIgnoreCase("chrome")) {
			/*
			 * driverName = "chromedriver" + driverName;
			 * System.setProperty("webdriver.chrome.driver", driverPath + driverName);
			 */
			
			//WebDriverManager.chromedriver().driverVersion(driverVersion).setup();
			
			//WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--silent");
			options.addArguments("start-maximized");
			options.addArguments("--disable-extensions");
		   
		   /*
		    * Added from a forum to see if worked
		    * options.addArguments("--no-proxy-server");
		    * options.addArguments("--no-sandbox");
		    * options.addArguments("--disable-extensions");
		    * options.addArguments("--dns-prefetch-disable");
		    * options.addArguments("--disable-gpu");
		    * options.addArguments("--force-device-scale-factor=1");
		    * options.setPageLoadStrategy(PageLoadStrategy.EAGER);
		    */
			
			
			/*
			 * options.addArguments("-incognito"); options.addArguments("-disable-cache");
			 * options.addArguments("--ignore-ssl-errors=yes");
			 * options.addArguments("--ignore-certificate-errors");
			 */
			options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			prefs.put("excludeSwitches", "enable-automation");
			options.setExperimentalOption("prefs", prefs);
			try {
				driver = new ChromeDriver(options);
			} catch (Exception e) {
				System.out.println("Message: " + e.getMessage());
			}
		} else if (browser.equalsIgnoreCase("Edge")) {
			// System.setProperty("webdriver.edge.driver", driverPath +
			// "MicrosoftWebDriver.exe");
			// WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		driver.manage().deleteAllCookies();
		// getClass(); disabled for testing purposes
		driver.navigate().to(url);
		driver.manage().window().maximize();
		
		//Used for extending driver timeout on pageloads
		//driver.manage().timeouts().pageLoadTimeout(600, TimeUnit.SECONDS);
		
		
		/*
		 * try { doLogin(); } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 */
		return driver;
	}

	public void getReaderContext() {
		final String propertyFilePath = "properties//Configuration.properties";
		try {
			Properties properties = new Properties();
			FileInputStream fi = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\main\\java\\properties\\Configuration.properties");
			try {
				properties.load(fi);

				// browser = properties.getProperty("browser");
				platform = properties.getProperty("platform");
				driverPath = properties.getProperty("driverPath");
				driverVersion = properties.getProperty("driverVersion");
				// url = properties.getProperty("url");
				// userName = properties.getProperty("userName");
				// password = properties.getProperty("password");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
		}
	}

	public WebDriver getDriver() {
		if (driver == null) {
			createDriver();
		}
		return this.driver;
	}

	public static void setName(String name) {
		BaseTest.userName = name;
	}

	public String getName() {
		String username = BaseTest.userName;
		return username;
	}

	public static void setPassword(String password) {
		BaseTest.password = password;
	}

	public String getPassword() {
		String pssword = BaseTest.password;
		return pssword;
	}

	public static void setBrowser(String browser) {
		BaseTest.browser = browser;
	}

	public String getBrowser() {
		String brows = BaseTest.browser;
		return brows;
	}

	public static void setURL(String URL) {
		BaseTest.url = URL;
	}

	public String getURL() {
		String Url = BaseTest.url;
		return Url;
	}

	public Iterator<Object[]> XMLReader(String fileName) {

		if (fileName != null) {
			if (!fileName.contains(".xml")) {
				fileName += ".xml";
			}
			try {
				File fXmlFile = new File(fileName);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				Node register;
				Node child;
				int index;

				doc.getDocumentElement().normalize();

				register = doc.getDocumentElement();

				register = register.getFirstChild();

				List<Object[]> data = new ArrayList<Object[]>();
				String[] aux;

				while (register != null) {
					if ((index = register.getChildNodes().getLength()) > 0) {
						index = (index / 2);
					}
					System.out.println(index);
					aux = new String[index];
					child = register.getFirstChild();
					index = 0;

					while (child != null) {
						if (!child.getNodeName().equals("#text")) {
							aux[index++] = child.getTextContent();
						}
						child = child.getNextSibling();
					}

					if (!register.getNodeName().equals("#text")) {
						data.add(aux);
					}

					register = register.getNextSibling();
				}

				return data.iterator();

			} catch (Exception e) {
			}
		}

		return null;
	}

	public Iterator<Object[]> XMLReaderByTag(String fileName, String tagName) {

		if (fileName != null) {
			if (!fileName.contains(".xml")) {
				fileName += ".xml";
			}

			try {
				File fXmlFile = new File(fileName);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				NodeList register;
				Node child;
				int index;
				int length;

				doc.getDocumentElement().normalize();

				register = doc.getElementsByTagName(tagName);

				List<Object[]> data = new ArrayList<Object[]>();
				String[] aux;

				if (register != null) {
					if ((length = register.item(0).getChildNodes().getLength()) > 0) {
						length = (length / 2);
					}
					System.out.println(length);
					aux = new String[length];
					index = 0;

					child = register.item(0).getFirstChild();
					while (child != null) {
						if (!child.getNodeName().equals("#text")) {
							aux[index++] = child.getTextContent();
						}
						child = child.getNextSibling();
					}

					// if(!register.getNodeName().equals("#text")) {
					data.add(aux);
					// }
					// register = register.getNextSibling();
				}

				return data.iterator();

			} catch (Exception e) {
			}
		}

		return null;
	}

	public static Iterator<Object[]> CSVReader(String fileName, String splitBy) {

		if (fileName != null) {
			if (!fileName.contains(".csv")) {
				fileName += ".csv";
			}
			try {
				List<Object[]> data = new ArrayList<Object[]>();
				BufferedReader br = null;
				String[] params;
				String line = "";
				br = new BufferedReader(new FileReader(fileName));
				while ((line = br.readLine()) != null) {
					params = line.split(splitBy);
					data.add(params);
				}
				br.close();
				return data.iterator();
			} catch (Exception e) {
				System.out.print(e.getMessage());
				System.out.print(e.getStackTrace().toString());

			}
		}
		return null;
	}
	public static void failedTestCaseListGenerator(Scenario scenario) {
		ArrayList<String> failedTestCases = new ArrayList<>();
		Collection<String> tags = scenario.getSourceTagNames();
		String tagString = tags.toString();
		failedTestCases.add(tagString);
	}
	
	/*
	 * private void doLogin() throws IOException { page = new Login(driver); try {
	 * page.refreshPage(); defaultPage = page.doLogin("fitsadm_emir", "fitsadm"); //
	 * defaultPage.refreshPage();
	 * 
	 * } catch (Exception e) { System.out.println(e.getMessage().toString()); } }
	 * 
	 * public void setUpTest(ITestResult result, Method methodName) throws
	 * IOException {
	 * 
	 * try { Set<String> set = driver.getWindowHandles(); if (set.size() > 1) {
	 * driver.quit(); Exception e = new Exception(); throw e; } } catch (Exception
	 * e) { driver = createDriver(); }
	 * 
	 * assertCalled = false; testAssert = new SoftAssert(); // driver.navigate().to(
	 * "javascript:document.getElementById('overridelink').click()");
	 * System.out.println("Currently Running: " + methodName.getName()); }
	 * 
	 * protected void assertTrue(boolean vlr, String msg) {
	 * testAssert.assertTrue(vlr, msg); }
	 * 
	 * protected void assertFalse(boolean vlr, String msg) {
	 * testAssert.assertFalse(vlr, msg); }
	 * 
	 * protected void assertAll() { assertCalled = true; testAssert.assertAll(); }
	 */

}
