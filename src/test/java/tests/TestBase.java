package tests;

import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import Driver.Driver;


public class TestBase {

	protected TestBase() {

	}

	@BeforeMethod
	protected void setUp() throws Exception {
		Driver.initDriver();
	}

	@AfterMethod
	protected void tearDown() {
		Driver.quitDriver();
	}


}

