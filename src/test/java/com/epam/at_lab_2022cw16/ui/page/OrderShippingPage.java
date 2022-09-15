package com.epam.at_lab_2022cw16.ui.page;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Log4j2
public class OrderShippingPage extends AbstractOrderPage {

    @FindBy(xpath = "//div[@id='center_column']//button")
    private WebElement proceedToCheckoutButton;

    @FindBy(xpath = "//input[@id='cgv']")
    private WebElement agreeToTheTermsCheckbox;

    private final By fancyboxCloseButton = By.xpath("//a[@title='Close']");

    public OrderShippingPage(WebDriver driver) {
        super(driver);
    }

    public OrderShippingPage changingCheckboxState() {
        agreeToTheTermsCheckbox.click();
        log.info("Agreement checkbox status is changed");
        return this;
    }

    public boolean isFancyboxDisplayed() {
        return driverWait()
                .until(ExpectedConditions.presenceOfElementLocated(fancyboxCloseButton))
                .isDisplayed();
    }

    public OrderShippingPage closeFancybox() {
        if (isFancyboxDisplayed()) {
            findElement(fancyboxCloseButton).click();
            log.info("Modal message You must agree to the terms of service before continuing. is closed");
        }
        return this;
    }

    public OrderPaymentPage clickProceedToCheckoutButton() {
        proceedToCheckoutButton.click();
        log.info("Go to Payment information");
        return new OrderPaymentPage(driver);
    }
}