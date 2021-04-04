package Driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {
	private DriverManager() {

	}

	private static final ThreadLocal<WebDriver> th_driver = new ThreadLocal<WebDriver>();

	public static WebDriver getTh_driver() {
		return th_driver.get();
	}

	public static void setTh_driver(WebDriver driverRef) {
		th_driver.set(driverRef);
	}

	public static void unload() {
		th_driver.remove();
	}


}
