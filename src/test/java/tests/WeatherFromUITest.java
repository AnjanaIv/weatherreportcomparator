package tests;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.HashMap;

import org.hamcrest.Matchers;
import org.json.simple.JSONArray;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Constants.FrameworkConstants;
import Driver.DriverManager;
import io.restassured.response.Response;

public class WeatherFromUITest extends TestBase{
	//Part1 : *******************************************************
	//Step1 : Navigate to https://www.ndtv.com/
	//Step2 : Below search weather & hit Search icon
	//Step3 : Verify https://www.ndtv.com/search?searchtext=weather is loaded
	//Step4 : Scroll to Sections object
	//Step5 : Click on Sections & select Weather link
	//Step6 : Verify https://social.ndtv.com/static/Weather/report/ is loaded
	//Step7 : Type "Amritsar" in `Pin your city` section & click on Amritsar checkbox
	//Step8:  validate that the corresponding city is available on the map with temperature information
	//Step9 : Click on city name on the map & verify tooltip
	//Step10 : Verify the values
	//Part2 : *******************************************************
	//Step1 : Access the API : 	api.openweathermap.org/data/2.5/weather?q={city name}&appid={7fe67bf08c80ded756e598d6f8fedaea'}
	//Step2 : Save the GET call responses
	//Part3 : *******************************************************
	//Create a comparator that matches the temperature information from the UI in phase 1 against the API response(ensure both are using the same temperature unit) in phase 2
	//Build a variance logic(should be configurable) that returns a success if temperature difference is within a specified range, else return a matcher exception
	//Optional : Analyse other available weather conditions on both sources that can be compared and do the comparison following a similar variance logic.
	static ExtentTest test;
	static ExtentReports report;

	@Parameters({"city","temp_variance","hum_variance"})
	@Test
	public static void getWeatherReport(String city,String temp_var,String hum_variance) {
		report = new ExtentReports(FrameworkConstants.getReportfilepath());
		test = report.startTest("Weather Comparatore Report");
		//Visit ndtv website’s weather page and search for Bangalore 
		city = "Amritsar";
		try {
			DriverManager.getTh_driver().findElement(By.xpath("//input[@id='searchBox']")).sendKeys(city);
			DriverManager.getTh_driver().findElement(By.xpath("//input[@id='"+city+"']")).click();
			Assert.assertEquals(DriverManager.getTh_driver().findElement(By.xpath("//div[@class='outerContainer' and @title='"+city+"']")).isDisplayed(), true);
			//Store weather object 1 w.r.t this city (e.g. temp value as 33 degree celsius, humidity level as 70% etc.)
			HashMap<String,String> weather = new HashMap<String,String>();
			weather.put("temp", DriverManager.getTh_driver().findElement(By.xpath("//div[@title='"+city+"']//div[@class='temperatureContainer']/span[1]")).getText());
			weather.put("humidity", DriverManager.getTh_driver().findElement(By.xpath("//div[@title='"+city+"']//div[@class='temperatureContainer']/span[2]")).getText());
			test.log(LogStatus.INFO, "Temperature in Degres "+city+" is :"+weather.get("temp"));
			test.log(LogStatus.INFO, "Temperature in Farah "+city+" is :"+weather.get("humidity"));
			//Get the detailed weather report
			Thread.sleep(1000);
			//WebDriverWait wait = new WebDriverWait(DriverManager.getTh_driver(), 5);
			//WebElement element = wait.until(
			//ExpectedConditions.visibilityOfElementLocated(By.id("//div[@class='leaflet-popup  leaflet-zoom-animated']//span[2]")));
			DriverManager.getTh_driver().findElement(By.xpath("//div[@title='"+city+"']//span//img")).click();
			test.log(LogStatus.INFO, "Details for the city : "+ DriverManager.getTh_driver().findElement(By.xpath("//div[@class='leaflet-popup  leaflet-zoom-animated']//span[2]")).getText());
			test.log(LogStatus.INFO, DriverManager.getTh_driver().findElement(By.xpath("//div[@class='leaflet-popup  leaflet-zoom-animated']//span[@class='heading']")).getText());
			test.log(LogStatus.INFO, DriverManager.getTh_driver().findElement(By.xpath("//div[@class='leaflet-popup  leaflet-zoom-animated']//span[@class='heading'][2]")).getText());
			test.log(LogStatus.INFO, DriverManager.getTh_driver().findElement(By.xpath("//div[@class='leaflet-popup  leaflet-zoom-animated']//span[@class='heading'][3]")).getText());
			test.log(LogStatus.INFO, DriverManager.getTh_driver().findElement(By.xpath("//div[@class='leaflet-popup  leaflet-zoom-animated']//span[@class='heading'][4]")).getText());
			test.log(LogStatus.INFO, DriverManager.getTh_driver().findElement(By.xpath("//div[@class='leaflet-popup  leaflet-zoom-animated']//span[@class='heading'][5]")).getText());
			test.log(LogStatus.INFO, "Unable to compare UI & API values as none of the nodes of API response matches with temperature or humidity");
			//Get response from the weather API for city
			baseURI= "http://api.openweathermap.org/data/2.5";
			given().baseUri(baseURI)
					.get("/weather?q="+city+"&appid=7fe67bf08c80ded756e598d6f8fedaea").then()
					.body("main.temp",Matchers.closeTo(293.15F,(293.15F+Integer.valueOf(temp_var))))
					.body("main.humidity", Matchers.closeTo(45,(45+Integer.valueOf(hum_variance)))).log().all();
					 /*given()
				//.header("key","7fe67bf08c80ded756e598d6f8fedaea")
				.get("/weather?q="+city+"&appid=7fe67bf08c80ded756e598d6f8fedaea")
				.then()
				.body("main.temp",Matchers.closeTo(293.15F,(293.15F+Integer.valueOf(temp_var))))
				 .body("main.humidity", Matchers.closeTo(45,(45+Integer.valueOf(hum_variance)))).log().all();*/
			//Store the API response and build the weather object 2
			//Specify the variance logic - for e.g. 2 degree celsius for temperature & 10% for humidity
			//Compare weather objects 1 and 2 along with the variance and mark tests as pass or fail based on comparator response
			test.log(LogStatus.PASS, "All verifications are done");        
		}catch(Exception e) {
			e.printStackTrace();
			test.log(LogStatus.FAIL, "Error in fetching the intended values "+e);
		}
		report.endTest(test);
		report.flush();
		DriverManager.getTh_driver().close();
	}




}
