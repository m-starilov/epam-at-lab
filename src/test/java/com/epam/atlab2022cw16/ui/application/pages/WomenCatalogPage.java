package com.epam.atlab2022cw16.ui.application.pages;

import org.openqa.selenium.WebDriver;

public class WomenCatalogPage extends AbstractCatalogPage {
    private static final String PAGE_TITLE = "WOMEN";

    private static final String BASE_URL = "http://automationpractice.com/index.php?id_category=3&controller=category";

    public WomenCatalogPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public WomenCatalogPage openPage() {
        driver.get(BASE_URL);
        return this;
    }

    @Override
    public boolean isPageTitleValid() {
        return getSummary().equals(PAGE_TITLE);
    }
}
