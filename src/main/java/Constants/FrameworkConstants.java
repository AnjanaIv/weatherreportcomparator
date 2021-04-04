package Constants;

import java.io.File;

public class FrameworkConstants {
	private final static String ChromeDriverPath = System.getProperty("user.dir") +File.separator+"drivers"+File.separator+"chromedriver.exe";
	private final static String PropertyFilePath = System.getProperty("user.dir") +File.separator+"src/test/resources/config"+File.separator+"config.properties";
	private final static String ReportFilePath = System.getProperty("user.dir")+File.separator+"output"+File.separator+"ExtentReportResults.html";
	public static String getChromeDriverPath() {
		return ChromeDriverPath;
	}

	public static String getPropertyfilepath() {
		return PropertyFilePath;
	}

	public static String getReportfilepath() {
		return ReportFilePath;
	}


}
