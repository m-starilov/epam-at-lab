package com.epam.atlab2022cw16.ui.steps;

import com.epam.atlab2022cw16.ui.application.constants.Constants;
import com.epam.atlab2022cw16.ui.application.pages.OrderAddressPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderBankWirePaymentPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderConfirmationPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderPaymentPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderShippingPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderSummaryPage;
import com.epam.atlab2022cw16.ui.utils.EnvironmentUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderPageStepDefinitions {

    private final WebDriver driver = EnvironmentUtils.getDriver();
    private final List<String> bankAccountInformation = Arrays.asList("Pradeep Macharla", "xyz", "RTP");
    static ThreadLocal<String> orderReferenceThreadLocal = new ThreadLocal<>();
    private final static String oldOrderProductName = "Faded Short Sleeve T-shirts";

    @Then("I see {string} in cart")
    public void checkNumberOfItemsInCart(String numberOfItems) {
        assertThat(new OrderSummaryPage(driver).getSummaryProductsQuantity())
                .isEqualTo(numberOfItems);
    }

    @When("I enter {string} to Qty field")
    public void setQuantity(String quantity) {
        new OrderSummaryPage(driver).setProductQuantity(quantity);
    }

    @When("I click Proceed to checkout button at Summary page")
    public void clickProceedToCheckoutSummary() {
        new OrderSummaryPage(driver).clickProceedToCheckoutButtonCommon();
    }

    @When("I click Proceed to checkout button at Address page")
    public void clickProceedToCheckoutAddress() {
        new OrderAddressPage(driver).clickProceedToCheckoutButtonCommon();
    }

    @When("I click Proceed to checkout button at Shipping page")
    public void clickProceedToCheckoutShipping() {
        new OrderShippingPage(driver).clickProceedToCheckoutButtonCommon();
    }

    @Then("I see modal window is displayed")
    public void checkModalWindowDisplayed() {
        assertThat(new OrderShippingPage(driver).isFancyboxDisplayed())
                .isTrue();
    }

    @When("I close modal window")
    public void closeModalWindow() {
        new OrderShippingPage(driver).closeFancybox();
    }

    @When("I change checkbox state")
    public void changeCheckbox() {
        new OrderShippingPage(driver).changingCheckboxState();
    }

    @When("I choose Pay by bank wire method")
    public void payByBankWire() {
        new OrderPaymentPage(driver).chooseBankWirePayment();
    }

    @When("I click I confirm my order button")
    public void confirmMyOrder() {
        new OrderBankWirePaymentPage(driver).clickPaymentIConfirmMyOrderButton();
    }

    @Then("I see {string} text is displayed")
    public void checkOrderCompletion(String text) {
        assertThat(new OrderConfirmationPage(driver).getConfirmationText())
                .isEqualTo(text);
    }

    @Then("I see amount {string}")
    public void checkAmount(String amount) {
        assertThat(new OrderConfirmationPage(driver).getAmountInformation())
                .isEqualTo(amount);
    }

    @Then("I see correct bank information")
    public void checkBankInformation() {
        assertThat(new OrderConfirmationPage(driver).getBankAccountInformation())
                .isEqualTo(bankAccountInformation);
    }

    @When("I proceed to My Account page")
    public void proceedToMyAccountPage() {
        new OrderConfirmationPage(driver).openMyAccountPage();
    }

    @Then("Cart page has Empty cart alert message")
    public void getAlertMessage() {
        OrderSummaryPage orderSummaryPage = new OrderSummaryPage(driver);
        assertThat(orderSummaryPage.getPageElementAlert().getMessage())
                .isEqualTo(Constants.AlertMessageTexts.EMPTY_CART_TEXT);
    }

    @And("The {int} reordered item is in the cart")
    public void isReorderedItemsInTheCart(int quantity) {
        OrderSummaryPage orderSummaryPage = new OrderSummaryPage(driver);
        List<String> itemsFromCurrentOrder = orderSummaryPage.getAddedProductNames();
        assertThat(oldOrderProductName).contains(itemsFromCurrentOrder.get(0));
        assertEquals(quantity, orderSummaryPage.getSummaryProductsQuantityAsInt());
    }

    @And("Previews items is deleted")
    public void isDeletedPreviewsItems() {
        assertThat(new OrderSummaryPage(driver).getAddedProductNames().size())
                .isEqualTo(1);
    }

    @When("I return to Order history page")
    public void returnToOrderHistoryPage() {
        new OrderSummaryPage(driver).openMyAccountPage().clickOrderHistoryButton();
    }

    @And("added product {string} displayed on the page")
    public void addedProductDisplayedOnThePage(String productName) {
        List<String> addedProductNames = new OrderSummaryPage(driver).getAddedProductNames();
        assertEquals(1, addedProductNames.size());
        assertTrue(addedProductNames.contains(productName));
    }

    @And("A message {string}  displayed")
    public void isDisplayedMessage(String message) {
        Assertions.assertEquals(message, new OrderConfirmationPage(driver).getConfirmationText());
    }

    @And("order reference is on the page")
    public void orderReferenceIsOnThePage() {
        orderReferenceThreadLocal.set(new OrderConfirmationPage(driver).getOrderReverence());
        Assertions.assertEquals(9, orderReferenceThreadLocal.get().length());
    }

    @When("I press Back to orders")
    public void pressBackToOrders() {
        new OrderConfirmationPage(driver).clickBackToOrdersButton();
    }
}
