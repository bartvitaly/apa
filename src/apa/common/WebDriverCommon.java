package apa.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverCommon {

	public WebDriver driver;
	final Actions action;
	final static long TIMEOUT = 60;
	final static Logger logger = Logger.getLogger(WebDriverCommon.class);

	/**
	 * This method is a constructor and defines driver object
	 * 
	 * @param driver
	 * 
	 */
	public WebDriverCommon() {
		String chromePath = (new File("driver/chromedriver.exe")).getAbsolutePath();
		System.setProperty("webdriver.chrome.driver", chromePath);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-infobars"); // , "--headless", "--window-size=1920x1080"
		driver = new ChromeDriver(options);
		this.action = new Actions(driver);
		logger.debug("Wait for page to load");
		waitForPageLoaded(driver);
	}

	/**
	 * This method takes a page screenshot
	 * 
	 * @param driver
	 * 
	 */
	public void takeScreenshot() {
		String fileName = File.separator + "test-output" + File.separator + System.currentTimeMillis()
				+ "_screenshot.png";
		logger.info("Taking screenshot.");
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(Common.canonicalPath() + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method waits till a page loads
	 * 
	 * @param driver
	 * 
	 */
	static public void waitForPageLoaded(WebDriver driver) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		for (int i = 0; i < tabs.size(); i++) {
			driver.switchTo().window(tabs.get(i));
			ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
				}
			};
			org.openqa.selenium.support.ui.Wait<WebDriver> wait = new WebDriverWait(driver, TIMEOUT);
			wait.until(expectation);
		}
	}

	/**
	 * This method is to wait till an element appears
	 * 
	 * @param driver
	 * @param by
	 * @param timeout
	 * 
	 */
	static WebElement waitForElement(WebDriver driver, final By by, long timeout) {
		org.openqa.selenium.support.ui.Wait<WebDriver> wait = new WebDriverWait(driver, timeout);

		driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

		ExpectedCondition<WebElement> expectation = new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(by);
			}
		};

		try {
			return wait.until(expectation);
		} catch (Throwable error) {
			return null;
		}
	}

	/**
	 * This method is to wait for a list of elements appear on the page
	 * 
	 * @param driver
	 * @param by
	 * @param timeout
	 * 
	 */
	static ArrayList<WebElement> waitForElements(WebDriver driver, final By by, long timeout) {

		org.openqa.selenium.support.ui.Wait<WebDriver> wait = new WebDriverWait(driver, timeout);

		driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

		ExpectedCondition<ArrayList<WebElement>> expectation = new ExpectedCondition<ArrayList<WebElement>>() {
			public ArrayList<WebElement> apply(WebDriver driver) {
				return (ArrayList<WebElement>) driver.findElements(by);
			}
		};

		try {
			return wait.until(expectation);
		} catch (Throwable error) {
			return null;
		}
	}

	/**
	 * This method is to wait till an element disappears
	 * 
	 * @param driver
	 * @param by
	 * @param timeout
	 * 
	 */
	static boolean waitForElementNotShown(WebDriver driver, final By by, int timeout) {

		org.openqa.selenium.support.ui.Wait<WebDriver> wait = new WebDriverWait(driver, timeout);

		driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return !driver.findElement(by).isDisplayed();
			}
		};

		try {
			return wait.until(expectation);
		} catch (Throwable error) {
			return false;
		}
	}

	/**
	 * This method is to click on element using WebElement or By object
	 * 
	 * @param obj
	 * @throws InterruptedException
	 */
	public void click(Object obj) {
		logger.info("Clicking an element '" + obj + "' on page '" + driver.getCurrentUrl() + "'");
		WebElement element = getElement(obj);
		try {
			action.moveToElement(element).build().perform();
			action.click(element).build().perform();
			waitForPageLoaded(driver);
		} catch (Exception e) {
			takeScreenshot();
			logger.error("An element '" + obj + "' was not clicked on page '" + driver.getCurrentUrl() + "'");
		}
	}

	/**
	 * This method is to click on element using WebElement or By object and wait for
	 * page to load
	 * 
	 * @param obj
	 * @throws InterruptedException
	 */
	public void click_wait(Object obj) {
		click(obj);
		logger.info("Waiting for page to load '" + driver.getCurrentUrl() + "'");
		try {
			waitForPageLoaded(driver);
		} catch (Exception e) {
			takeScreenshot();
			logger.error("An element '" + obj + "' was not clicked on page '" + driver.getCurrentUrl() + "'");
		}
	}

	/**
	 * This method is to click and wait till another element sappears
	 * 
	 * @param driver
	 * @param by
	 * @param timeout
	 * @throws InterruptedException
	 * 
	 */
	public void click_wait(By elementToClick, By elementToWait) throws InterruptedException {
		int i = 0;
		while (!present(elementToWait) && i < 10) {
			click(elementToClick);
			i++;
		}
	}

	/**
	 * This method is to type text into element using WebElement or By object
	 * 
	 * @param obj
	 * @param text
	 */
	public void type(Object obj, Object text) {
		text = String.valueOf(text);
		logger.info(
				"Filling a text '" + text + "' into the field '" + obj + "' on page '" + driver.getCurrentUrl() + "'");
		WebElement element = getElement(obj);
		try {
			element.clear();
			element.sendKeys((String) text);
		} catch (Exception e) {
			takeScreenshot();
			logger.error("A text '" + text + "' was not filled in the element '" + obj + "' on page '"
					+ driver.getCurrentUrl() + "'");
		}
	}

	/**
	 * This method is to select an option in element using WebElement or By object
	 * 
	 * @param by
	 * @param text
	 */
	public void select(Object obj, String text) {
		logger.info("Selecting a value '" + text + "' from select field '" + obj + "' on page '"
				+ driver.getCurrentUrl() + "'");
		WebElement element = getElement(obj);
		Select selectBox = new Select(element);
		selectBox.selectByValue(text);
	}

	/**
	 * This method is to get a text of element using WebElement or By object
	 * 
	 * @param obj
	 * @return WebElement
	 */
	String getText(Object obj) {
		logger.info("Getting text from element '" + obj + "' on page '" + driver.getCurrentUrl() + "'");
		WebElement element = getElement(obj);
		return element.getText().toString();
	}

	/**
	 * This method is to select a text from unconventional select box element using
	 * WebElement or By object
	 * 
	 * @param by
	 * @param by
	 * @param text
	 * @throws InterruptedException
	 */
	public void select(By by, By byChild, String text) throws InterruptedException {
		click(by);
		click(byChild);
	}

	/**
	 * This method is to get an element using WebElement or By object with user
	 * defined timeout
	 * 
	 * @param obj
	 * @param timeout
	 * @return WebElement
	 */
	WebElement getElement(Object obj, long timeout) {
		logger.debug("Getting element '" + obj + "' on page '" + driver.getCurrentUrl() + "'");
		if (obj.getClass().getName().contains(By.class.getName())) {
			return waitForElement(driver, (By) obj, timeout);
		}
		return (WebElement) obj;
	}

	/**
	 * This method is to get an element using WebElement or By object with default
	 * timeout
	 * 
	 * @param obj
	 * @return WebElement
	 */
	WebElement getElement(Object obj) {
		WebElement element = getElement(obj, TIMEOUT);
		return element;
	}

	/**
	 * This method is to get an element using By object with user defined timeout
	 * 
	 * @param by
	 * @return WebElement
	 */
	WebElement getElement(By by, int timeout) {
		logger.debug("Getting element '" + by + "' on page '" + driver.getCurrentUrl() + "'");
		return waitForElement(driver, by, timeout);
	}

	/**
	 * This method is to check that an element exist on the page
	 * 
	 * @param by
	 * 
	 */
	public Boolean present(final By by) {
		if (getElement(by, 0) != null) {
			return true;
		}
		return false;
	}

	/**
	 * This method is to get an element using By object with default timeout
	 * 
	 * @param by
	 * @return WebElement
	 */
	public WebElement getElement(By by) {
		return getElement(by, TIMEOUT);
	}

	/**
	 * This method is to get the list of elements using By object with default
	 * timeout
	 * 
	 * @param by
	 * @return List
	 */
	public ArrayList<WebElement> getElements(By by) {
		return waitForElements(driver, by, TIMEOUT);
	}

	/**
	 * This method checks an attribute of an element
	 * 
	 * @param element
	 * @param attribute
	 * @param value
	 * @return Boolean
	 */
	public boolean checkAttribute(Object obj, String attribute, Object value, boolean throwError) {
		value = String.valueOf(value);
		logger.debug(
				"Verify an attribute '" + attribute + "' equals value '" + value + "' for an object '" + obj + "'");
		String attributeActual = getAttribute(obj, attribute);
		if (attribute.equals("text")) {
			attributeActual = getText(obj);
		}

		if (!(attributeActual.contains((String) value)) && throwError) { // ||
																			// ((String)
																			// value).contains(attributeActual)
			logger.error("Verification of an attribute '" + attribute + "' failed. Expected value '" + value
					+ "', actual is '" + attributeActual + "' for an object '" + obj + "'");
			return false;
		}
		logger.info("Verification of an attribute '" + attribute + "' passed. Expected value '" + value
				+ "', actual is '" + attributeActual + "' for an object '" + obj + "'");
		return true;
	}

	/**
	 * This method retrieves an attribute of an element
	 * 
	 * @param element
	 * @param attribute name
	 * @return String
	 */
	public String getAttribute(Object obj, String attribute) {
		WebElement element = getElement(obj);
		if (element == null) {
			return "";
		}
		if (attribute.equals("text")) {
			return getText(element);
		}
		String attributeValue = element.getAttribute(attribute);
		if (attributeValue == null) {
			attributeValue = "";
		}

		return attributeValue;
	}

}
