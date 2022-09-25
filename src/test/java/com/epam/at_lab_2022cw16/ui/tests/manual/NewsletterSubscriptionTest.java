package com.epam.at_lab_2022cw16.ui.tests.manual;

import com.epam.at_lab_2022cw16.ui.constants.AlertMessageTexts;
import com.epam.at_lab_2022cw16.ui.constants.PageTitles;
import com.epam.at_lab_2022cw16.ui.page.MyStoreHomepage;
import com.epam.at_lab_2022cw16.ui.page.pageElements.Alert;
import com.epam.at_lab_2022cw16.ui.utils.TestListener;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import static com.epam.at_lab_2022cw16.ui.utils.RandomEmailCreator.generateRandomEmail;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(TestListener.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewsletterSubscriptionTest extends AbstractBaseTest {
    private static final WebDriver driver = getWebDriver();
    private static String generatedEmail;

    @BeforeAll
    public static void createEmail() {
        generatedEmail = generateRandomEmail();
    }

    @Test
    @Order(1)
    public void openHomepage() {
        MyStoreHomepage myStoreHomepage = new MyStoreHomepage(driver);
        myStoreHomepage.openPage();
        assertThat(myStoreHomepage.getTitle())
                .contains(PageTitles.HOME.getPageTitle());
    }

    @Test
    @Order(2)
    public void subscribeNewsletterWithRandomGeneratedEmail() {
        MyStoreHomepage myStoreHomepage = new MyStoreHomepage(driver);
        myStoreHomepage.sendEmailToNewsletterField(generatedEmail)
                .pressSubmitNewsletterButton();
        Alert alert = myStoreHomepage.getNewsletterAlert();
        assertThat(alert.isSuccess())
                .isTrue();
        assertThat(alert.getMessage())
                .contains(AlertMessageTexts.NEWSLETTER_SUCCESS_MESSAGE.getAlertMessageText());
    }

    @Test
    @Order(3)
    public void subscribeNewsletterWithSameRandomGeneratedEmail() {
        MyStoreHomepage myStoreHomepage = new MyStoreHomepage(driver);
        myStoreHomepage.sendEmailToNewsletterField(generatedEmail)
                .pressSubmitNewsletterButton();
        Alert alert = myStoreHomepage.getNewsletterAlert();
        assertThat(alert.isDanger())
                .isTrue();
        assertThat(alert.getMessage())
                .contains(AlertMessageTexts.NEWSLETTER_FAILURE_MESSAGE.getAlertMessageText());
    }
}
