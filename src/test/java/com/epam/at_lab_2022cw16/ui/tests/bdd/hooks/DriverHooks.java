package com.epam.at_lab_2022cw16.ui.tests.bdd.hooks;

import com.epam.at_lab_2022cw16.ui.driver.WebDriverApp;
import com.epam.at_lab_2022cw16.ui.utils.EnvironmentUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

@Log4j2
public class DriverHooks {

    @Before
    public static void openBrowser() {
        WebDriver driver = new WebDriverApp()
                .getChrome()
                .defaultConfig()
                .windowsSize(new Dimension(1920, 1080))
                .build();
        EnvironmentUtils.setDriver(driver);
    }

    @After(order = 0)
    public static void closeBrowser() {
        Optional
                .ofNullable(EnvironmentUtils.getDriver())
                .ifPresent(webDriver -> {
                    webDriver.quit();
                    EnvironmentUtils.removeDriver();
                });
    }

    public static WebDriver getWebDriver() {
        return EnvironmentUtils.getDriver();
    }
}
