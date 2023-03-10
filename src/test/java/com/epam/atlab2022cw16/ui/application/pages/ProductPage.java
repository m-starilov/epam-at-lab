package com.epam.atlab2022cw16.ui.application.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends AbstractBasePage {

    @FindBy(xpath = "//h1[@itemprop='name']")
    private WebElement productTitle;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getProductTitle() {
        return productTitle.getText().trim();
    }
}
