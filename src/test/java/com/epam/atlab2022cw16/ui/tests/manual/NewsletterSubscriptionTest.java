package com.epam.atlab2022cw16.ui.tests.manual;

import com.epam.atlab2022cw16.ui.annotations.JiraTicketsLink;
import com.epam.atlab2022cw16.ui.application.constants.Constants.AlertMessageTexts;
import com.epam.atlab2022cw16.ui.application.pages.MyStoreHomepage;
import com.epam.atlab2022cw16.ui.application.modules.Alert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;

import static com.epam.atlab2022cw16.ui.utils.RandomEmailCreator.generateRandomEmail;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
@Tag("manual")
@JiraTicketsLink(id = 16322,
        description = "Checking the possibility to subscribe to the newsletter twice",
        url = "https://jira.epam.com/jira/browse/EPMFARMATS-16322")
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
        assertThat(myStoreHomepage.isPageTitleValid()).isTrue();
    }

    @Test
    @Order(2)
    public void subscribeNewsletterWithRandomGeneratedEmail() {
        MyStoreHomepage myStoreHomepage = new MyStoreHomepage(driver);
        myStoreHomepage.sendEmailToNewsletterField(generatedEmail)
                .pressSubmitNewsletterButton();
        Alert alert = myStoreHomepage.getPageElementAlert();
        assertThat(alert.isSuccess())
                .isTrue();
        assertThat(alert.getMessage())
                .contains(AlertMessageTexts.NEWSLETTER_SUCCESS_MESSAGE);
    }

    @Test
    @Order(3)
    public void subscribeNewsletterWithSameRandomGeneratedEmail() {
        MyStoreHomepage myStoreHomepage = new MyStoreHomepage(driver);
        myStoreHomepage.sendEmailToNewsletterField(generatedEmail)
                .pressSubmitNewsletterButton();
        Alert alert = myStoreHomepage.getPageElementAlert();
        assertThat(alert.isDanger())
                .isTrue();
        assertThat(alert.getMessage())
                .contains(AlertMessageTexts.NEWSLETTER_FAILURE_MESSAGE);
    }
}
