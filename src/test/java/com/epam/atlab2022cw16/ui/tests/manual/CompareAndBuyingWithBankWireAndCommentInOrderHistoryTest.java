package com.epam.atlab2022cw16.ui.tests.manual;

import com.epam.atlab2022cw16.ui.annotations.JiraTicketsLink;
import com.epam.atlab2022cw16.ui.application.models.User;
import com.epam.atlab2022cw16.ui.application.pages.ComparisonPage;
import com.epam.atlab2022cw16.ui.application.pages.MyAccountPage;
import com.epam.atlab2022cw16.ui.application.pages.MyStoreHomepage;
import com.epam.atlab2022cw16.ui.application.pages.OrderAddressPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderBankWirePaymentPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderConfirmationPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderHistoryPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderPaymentPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderShippingPage;
import com.epam.atlab2022cw16.ui.application.pages.OrderSummaryPage;
import com.epam.atlab2022cw16.ui.application.modules.Alert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;

import static com.epam.atlab2022cw16.ui.application.constants.Constants.AlertMessageTexts.ORDER_HISTORY_PAGE_DANGER_MESSAGE;
import static com.epam.atlab2022cw16.ui.application.constants.Constants.AlertMessageTexts.ORDER_HISTORY_PAGE_SUCCESS_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
@Tag("manual")
@JiraTicketsLink(id = {16299, 16331},
        description = "Test check authorization, adding to comparison, creating order and adding comment to order",
        url = {"https://jira.epam.com/jira/browse/EPMFARMATS-16299#",
                "https://jira.epam.com/jira/browse/EPMFARMATS-16331"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompareAndBuyingWithBankWireAndCommentInOrderHistoryTest extends AbstractBaseTest {

    private static final String TOTAL_ORDER_PRICE = "$59.96";
    private static final String USER_MESSAGE = "Please, send me photo of it!";
    private final WebDriver driver = getWebDriver();

    @Test
    @Order(1)
    public void openHomepageTest() {
        MyStoreHomepage homepage = new MyStoreHomepage(driver).openPage();
        assertThat(homepage.isPageTitleValid()).isTrue();
    }

    @Test
    @Order(2)
    public void loginTest() {
        User user = User.create().setEmail("mofrekoiquemma-6157@yopmail.com").setPassword("12345").build();
        MyAccountPage myAccountPage = new MyStoreHomepage(driver)
                .clickSignInButton()
                .inputEmail(user.getEmail())
                .inputPassword(user.getPassword())
                .proceedToMyAccountPage();
        assertThat(myAccountPage.isPageTitleValid())
                .isTrue();
    }

    @Test
    @Order(3)
    public void addToComparisonTest() {
        ComparisonPage comparisonPage = new MyAccountPage(driver)
                .clickWomenCatalogButton()
                .addToCompareItemByID(5)
                .addToCompareItemByID(6)
                .clickCompareButton();
        assertThat(comparisonPage.getNumberOfComparableItems())
                .isEqualTo(2);
    }

    @Test
    @Order(4)
    public void deleteFromComparisonTest() {
        ComparisonPage comparisonPage = new ComparisonPage(driver);
        comparisonPage.addToCartItemByID(5)
                .clickContinueShoppingButton()
                .deleteItemFromComparisonByID(5)
                .deleteItemFromComparisonByID(6);
        assertThat(comparisonPage.getNumberOfComparableItems())
                .isEqualTo(0);
    }

    @Test
    @Order(5)
    public void setProductQuantityTest() {
        OrderSummaryPage orderSummaryPage = new ComparisonPage(driver).clickViewSoppingCartButton();
        assertThat(orderSummaryPage.getSummaryProductsQuantity())
                .isEqualTo("1 Product");

        orderSummaryPage.setProductQuantity("qwe");
        assertThat(orderSummaryPage.getSummaryProductsQuantity())
                .isEqualTo("1 Product");

        orderSummaryPage.setProductQuantity("-9");
        assertThat(orderSummaryPage.getSummaryProductsQuantity())
                .isEqualTo("1 Product");

        orderSummaryPage.setProductQuantity("2");
        assertThat(orderSummaryPage.getSummaryProductsQuantity())
                .isEqualTo("2 Products");
    }

    @Test
    @Order(6)
    public void shippingAgreementCheckboxTest() {
        new OrderSummaryPage(driver).clickProceedToCheckoutButtonCommon();
        new OrderAddressPage(driver).clickProceedToCheckoutButtonCommon();
        OrderShippingPage orderShippingPage = new OrderShippingPage(driver);
        orderShippingPage.clickProceedToCheckoutButtonCommon();
        assertThat(orderShippingPage.isFancyboxDisplayed())
                .isTrue();

        orderShippingPage.closeFancybox()
                .changingCheckboxState();
    }

    @Test
    @Order(7)
    public void paymentMethodTitleTest() {
       new OrderShippingPage(driver).clickProceedToCheckoutButtonCommon();
        assertThat(new OrderPaymentPage(driver).isPageTitleValid())
                .isTrue();
    }

    @Test
    @Order(8)
    public void bankWirePaymentTitleTest() {
        OrderBankWirePaymentPage orderBankWirePaymentPage = new OrderPaymentPage(driver).chooseBankWirePayment();
        assertThat(orderBankWirePaymentPage.isPageTitleValid())
                .isTrue();

        assertThat(orderBankWirePaymentPage.getTotalPriceInformation())
                .isEqualTo(TOTAL_ORDER_PRICE);
    }

    @Test
    @Order(9)
    public void orderConfirmationPageTest() {
        List<String> bankAccountInformation = Arrays.asList("Pradeep Macharla", "xyz", "RTP");
        OrderConfirmationPage orderConfirmationPage =
                new OrderBankWirePaymentPage(driver).clickPaymentIConfirmMyOrderButton();
        assertThat(orderConfirmationPage.isPageTitleValid())
                .isTrue();
        assertThat(orderConfirmationPage.getConfirmationText())
                .isEqualTo("Your order on My Store is complete.");
        assertThat(orderConfirmationPage.getAmountInformation())
                .isEqualTo(TOTAL_ORDER_PRICE);
        assertThat(orderConfirmationPage.getBankAccountInformation())
                .isEqualTo(bankAccountInformation);
    }

    @Test
    @Order(10)
    public void emptyCommentMessageTest() {
        MyAccountPage myAccountPage = new OrderConfirmationPage(driver).openMyAccountPage();
        assertThat(myAccountPage.isPageTitleValid())
                .isTrue();

        OrderHistoryPage ordersHistoryPage = myAccountPage.clickOrderHistoryButton();
        assertThat(ordersHistoryPage.isPageTitleValid())
                .isTrue();

        Alert alert = ordersHistoryPage
                .showLastOrderDetails()
                .clickSendButton()
                .getPageElementAlert();
        assertThat(alert.isDisplayed())
                .isTrue();
        assertThat(alert.isDanger())
                .isTrue();
        assertThat(alert.getMessage())
                .contains(ORDER_HISTORY_PAGE_DANGER_MESSAGE);
    }

    @Test
    @Order(11)
    public void nonEmptyCommentMessageTest() {
        OrderHistoryPage ordersHistoryPage = new OrderHistoryPage(driver)
                .selectProductFromDropdownByID("5")
                .setMessageText(USER_MESSAGE)
                .clickSendButton();
        Alert alert = ordersHistoryPage.getPageElementAlert();
        assertThat(alert.isDisplayed())
                .isTrue();
        assertThat(alert.isSuccess())
                .isTrue();
        assertThat(alert.getMessage())
                .contains(ORDER_HISTORY_PAGE_SUCCESS_MESSAGE);
        assertThat(ordersHistoryPage.getMessageText())
                .isEqualTo(USER_MESSAGE);
    }
}
