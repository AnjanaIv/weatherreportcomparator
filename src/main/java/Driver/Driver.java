package Driver;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;

import Constants.FrameworkConstants;
import utilities.PropertyFileReader;




public class Driver {
private Driver() {
		
	}

	public static void initDriver() throws Exception {
		if(Objects.isNull(DriverManager.getTh_driver())) {
			System.setProperty("webdriver.chrome.driver", FrameworkConstants.getChromeDriverPath());
			DriverManager.setTh_driver(new ChromeDriver());
			DriverManager.getTh_driver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			DriverManager.getTh_driver().get(PropertyFileReader.get("url"));
		}

	}

	public static void quitDriver() {
		if(Objects.nonNull(DriverManager.getTh_driver())) {
			DriverManager.getTh_driver().quit();
			DriverManager.unload();
		}

	}

}
