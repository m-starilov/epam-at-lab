package com.epam.at_lab_2022cw16.ui.tests.manual;

import com.epam.at_lab_2022cw16.ui.constants.AlertMessageTexts;
import com.epam.at_lab_2022cw16.ui.constants.PageTitles;
import com.epam.at_lab_2022cw16.ui.model.User;
import com.epam.at_lab_2022cw16.ui.page.AuthenticationPage;
import com.epam.at_lab_2022cw16.ui.page.MyAccountPage;
import com.epam.at_lab_2022cw16.ui.page.MyStoreHomepage;
import com.epam.at_lab_2022cw16.ui.page.OrderSummaryPage;
import com.epam.at_lab_2022cw16.ui.page.WomenCatalogPage;
import com.epam.at_lab_2022cw16.ui.page.pageElements.Alert;
import com.epam.at_lab_2022cw16.ui.utils.TestListener;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(TestListener.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShoppingCartSignOutTest extends AbstractBaseTest {

    private final WebDriver driver = getWebDriver();
    private final User validUser = new User("rescue-rangers@mail.com", "010203");

    @Test
    @Order(1)
    public void openHomePageTest() {
        MyStoreHomepage homePage = new MyStoreHomepage(driver).openPage();
        assertThat(homePage.getTitle()).isEqualTo(PageTitles.HOME.getPageTitle());
    }

    @Test
    @Order(2)
    public void openAuthenticationPageTest() {
        AuthenticationPage authPage = new MyStoreHomepage(driver).clickSignInButton();
        assertThat(authPage.getTitle()).isEqualTo(PageTitles.LOGIN.getPageTitle());
        assertThat(authPage.isCreateAccountFormVisible()).isTrue();
        assertThat(authPage.isLoginFormVisible()).isTrue();
    }

    @Test
    @Order(3)
    public void signInWithEmptyCredentialsTest() {
        AuthenticationPage authPage = new AuthenticationPage(driver);
        authPage.proceedToMyAccountPage();
        Alert alert = authPage.getPageElementAlert();
        assertThat(alert.isDisplayed()).isTrue();
        assertThat(alert.isDanger()).isTrue();
        assertThat(alert.getMessage()).contains(AlertMessageTexts.EMAIL_REQUIRED.getAlertMessageText());
    }

    @Test
    @Order(4)
    public void signInWithNotRegisteredEmailTest() {
        User user = new User("rescuerangers@mail.com", "010203");
        AuthenticationPage authPage = new AuthenticationPage(driver);
        authPage.inputEmail(user.getUsername());
        authPage.inputPassword(user.getPassword());
        authPage.proceedToMyAccountPage();
        Alert alert = authPage.getPageElementAlert();
        assertThat(alert.isDisplayed()).isTrue();
        assertThat(alert.isDanger()).isTrue();
        assertThat(alert.getMessage()).contains(AlertMessageTexts.AUTH_FAIL.getAlertMessageText());
    }

    @Test
    @Order(5)
    public void signInWithWrongPassTest() {
        User user = new User("rescue-rangers@mail.com", "0102030");
        AuthenticationPage authPage = new AuthenticationPage(driver);
        authPage.inputEmail(user.getUsername());
        authPage.inputPassword(user.getPassword());
        authPage.proceedToMyAccountPage();
        Alert alert = authPage.getPageElementAlert();
        assertThat(alert.isDisplayed()).isTrue();
        assertThat(alert.isDanger()).isTrue();
        assertThat(alert.getMessage()).contains(AlertMessageTexts.AUTH_FAIL.getAlertMessageText());
    }

    @Test
    @Order(6)
    public void signInTest() {
        AuthenticationPage authPage = new AuthenticationPage(driver);
        authPage.inputEmail(validUser.getUsername());
        authPage.inputPassword(validUser.getPassword());
        MyAccountPage myAccountPage = authPage.proceedToMyAccountPage();
        assertThat(myAccountPage.getTitle()).isEqualTo(PageTitles.MY_ACCOUNT.getPageTitle());
    }

    @Test
    @Order(7)
    public void addItemToCart() {
        WomenCatalogPage womenCatalogPage = new MyAccountPage(driver).clickWomenCatalogButton();
        womenCatalogPage.addOneItemToCart();
        assertThat(womenCatalogPage.isProductAddedTitleVisible()).isTrue();
    }

    @Test
    @Order(8)
    public void openTheCartTest() {
        WomenCatalogPage womenCatalogPage = new WomenCatalogPage(driver);
        OrderSummaryPage orderSummaryPage = womenCatalogPage.proceedToCheckout();
        assertThat(orderSummaryPage.getNavigationPageTitle()).isEqualTo(PageTitles.YOUR_CART.getPageTitle());
        assertThat(orderSummaryPage.isProductVisible()).isTrue();
    }

    @Test
    @Order(9)
    public void signOutTest() {
        OrderSummaryPage orderSummaryPage = new OrderSummaryPage(driver);
        orderSummaryPage.clickSignOutButton();
        assertThat(orderSummaryPage.getAlertMessage())
                .isEqualTo(AlertMessageTexts.EMPTY_CART_TEXT.getAlertMessageText());
        assertThat(orderSummaryPage.isAccountVisible()).isFalse();
    }

    @Test
    @Order(10)
    public void signInAfterSignOutTest() {
        OrderSummaryPage orderSummaryPage = new OrderSummaryPage(driver);
        AuthenticationPage authPage = orderSummaryPage.clickSignInButton();
        authPage.inputEmail(validUser.getUsername());
        authPage.inputPassword(validUser.getPassword());
        MyAccountPage myAccountPage = authPage.proceedToMyAccountPage();
        assertThat(myAccountPage.getTitle()).isEqualTo(PageTitles.MY_ACCOUNT.getPageTitle());
    }

    @Test
    @Order(10)
    public void goToCartTest() {
        MyAccountPage myAccountPage = new MyAccountPage(driver);
        assertThat(myAccountPage.isMiniCartEmpty()).isTrue();
        OrderSummaryPage orderSummaryPage = myAccountPage.clickToMiniCart();
        assertThat(orderSummaryPage.getAlertMessage())
                .isEqualTo(AlertMessageTexts.EMPTY_CART_TEXT.getAlertMessageText());
    }
}