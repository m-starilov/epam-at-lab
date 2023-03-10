package com.epam.atlab2022cw16.ui.steps;

import com.epam.atlab2022cw16.ui.application.constants.Constants.Color;
import com.epam.atlab2022cw16.ui.application.models.Product;
import com.epam.atlab2022cw16.ui.application.pages.EveningDressesCatalogPage;
import com.epam.atlab2022cw16.ui.application.pages.SummerDressesCatalogPage;
import com.epam.atlab2022cw16.ui.application.pages.TShirtsCatalogPage;
import com.epam.atlab2022cw16.ui.application.pages.WomenCatalogPage;
import com.epam.atlab2022cw16.ui.application.modules.ProductBlock;
import com.epam.atlab2022cw16.ui.utils.EnvironmentUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class CatalogPageStepDefinitions {

    private final WebDriver driver = EnvironmentUtils.getDriver();

    @When("I open WomenCatalogPage")
    public void openCatalogPage() {
        new WomenCatalogPage(driver).openPage();
    }

    @When("the user click on dropdown list Sort by and select sort {string}")
    public void sortCatalogByParam(String param) {
        new WomenCatalogPage(driver).sortByType(param);
    }

    @Then("all products are displayed on the page according sort {string}")
    public void isDisplaySortedProductList(String param) {
        Assertions.assertTrue(sortByParam(param, new WomenCatalogPage(driver)));
    }

    @When("the user apply {string} filter")
    public void applyFilter(String colorFilter) {
        new WomenCatalogPage(driver).filterByColor(Color.valueOf(colorFilter).getColorHex());
    }

    @Then("applied {string} filter marked in the filter block by {string} red color.")
    public void isChangedBorderColor(String filterColor, String borderColor) {
        String actualColor = new WomenCatalogPage(driver).getBorderColorFilterByColor(Color.valueOf(filterColor).getColorHex());
        Assertions.assertEquals(Color.valueOf(borderColor).getColorRGB(), actualColor);
    }

    @Then("all products filtered by {string} color")
    public void isFilteredByColor(String colorFilter) {
        List<List<String>> productsColors = new WomenCatalogPage(driver).getItemsInCatalog().stream().map(Product::getProductColor).collect(Collectors.toList());
        Assertions.assertTrue(productsColors.stream().allMatch(colorList -> {
            boolean isContained = false;
            for (String color : colorList) {
                isContained = color.contains(Color.valueOf(colorFilter).getColorRGB());
                if (isContained) {
                    break;
                }
            }
            return isContained;
        }));
    }

    @Then("the breadcrumbs on the filter page contains the name of the applied {string} filter.")
    public void isContainedTheNameOfTheAppliedFilterInBreadcrumbs(String filterName) {
        List<String> breadCrumbsLevels = Arrays.asList(new WomenCatalogPage(driver).getBreadCrumbs());
        String nameFilter = Color.valueOf(filterName).name();
        Assertions.assertTrue(breadCrumbsLevels.stream().anyMatch(p -> p.contains(nameFilter)));
    }

    @When("the user apply another one price filter")
    public void applyAnotherOnePriceFilter() {
        new WomenCatalogPage(driver).filterByPrice();
    }

    @Then("the page displays products that match the price filter")
    public void shouldDisplayProductsThatMatchThePriceFilter() {
        List<Double> minMaxPrice = new WomenCatalogPage(driver).getMinMaxPrice();
        List<Double> productsPrice = new WomenCatalogPage(driver).getItemsInCatalog().stream().map(Product::getProductPrice).collect(Collectors.toList());
        Assertions.assertEquals(productsPrice.size(), (productsPrice.stream().filter(p -> p > minMaxPrice.get(0) && p < minMaxPrice.get(1)).count()));
    }

    @And("filters type {string}, {string} and filters name are displayed in the Enabled filters: on the left side of the page.")
    public void shouldDisplayFiltersTypeAndFiltersName(String price, String color) {
        List<String> selectedFilters = new ArrayList<>();
        selectedFilters.add(price + (new WomenCatalogPage(driver).getPriceFilterRange().trim()));
        selectedFilters.add(color);
        List<String> filterNamesFromBlock = new WomenCatalogPage(driver).findAppliedFilters();
        Assertions.assertTrue(selectedFilters.size() == filterNamesFromBlock.size() && selectedFilters.containsAll(filterNamesFromBlock));
    }

    @When("the user remove all filters")
    public void removeAllFilters() {
        new WomenCatalogPage(driver).removeAllFilters().removePriceFilter();
    }

    @Then("the page displays products sorted by the condition from first step {string}.")
    public void displayProductsSortedByTheConditionFromFirstStep(String param) {
        Assertions.assertTrue(sortByParam(param, new WomenCatalogPage(driver)));
    }

    @When("I add to Compare product with id {int}")
    public void addItemsToCompare(int id) {
        new WomenCatalogPage(driver).addToCompareItemByID(id);
    }

    @When("I click to Compare button")
    public void clickToCompareButton() {
        new WomenCatalogPage(driver).clickCompareButton();
    }

    @When("I change items view to List")
    public void userChangeItemsViewToList() {
        new SummerDressesCatalogPage(driver).switchToListView();
        log.info("Items view changed to list");
    }

    @Then("items displayed in List view with \"Add to cart\", \"More\" buttons and \"Add to Wishlist\", \"Add to compare\" links")
    public void itemsDisplayedInListViewWithButtonsAndLinks() {
        SummerDressesCatalogPage summerDressesCatalogPage = new SummerDressesCatalogPage(driver);
        List<ProductBlock> productList = summerDressesCatalogPage.getProductsList();
        assertTrue(productList.size() > 0);
        assertEquals(productList.size(), summerDressesCatalogPage.getListOfAddToCartButtonsNumberValue());
        assertEquals(productList.size(), summerDressesCatalogPage.getMoreButtonsNumberValue());
        assertEquals(productList.size(), summerDressesCatalogPage.getAddToWishListButtonsNumberValue());
        assertEquals(productList.size(), summerDressesCatalogPage.getAddToCompareButtonsValue());
    }

    @When("I add item {int} to Wishlist")
    public void userAddItemsToWishlist(int numberOfItem) {
        SummerDressesCatalogPage catalogPage = new SummerDressesCatalogPage(driver);
        List<ProductBlock> productList = catalogPage.getProductsList();
        productList.get(numberOfItem - 1).addToWishListButtonClick();
    }

    @And("in {int} link outline heart icon changed to solid")
    public void inLinkOutlineHeartIconChangedToSolid(int number) {
        List<ProductBlock> productList = new SummerDressesCatalogPage(driver).getProductsList();
        assertTrue(productList.get(number - 1).isAddToWishlistSolidButtonDisplayed());
    }

    @When("I go to Wishlist page (My account>My Wishlists)")
    public void userGoToWishlistPage() {
        new SummerDressesCatalogPage(driver).proceedToMyAccountPage()
                .proceedToWishlist();
    }

    @Then("infobox is displayed")
    public void infoboxIsDisplayed() {
        assertTrue(new SummerDressesCatalogPage(driver).infoBoxIsDisplayed());
    }

    @When("I close infobox")
    public void userCloseInfobox() {
        new SummerDressesCatalogPage(driver).closeInfoBox();
    }

    @When("I go to Wishlist page \\(My account>My Wishlists)")
    public void userGoToWishlistPageFromCatalog() {
        new SummerDressesCatalogPage(driver).proceedToMyAccountPage().proceedToWishlist();
    }

    @When("I chose \"Evening Dresses\" from site top menu \\(Women>Dresses>Evening Dresses)")
    public void userChoseFromSiteTopMenuEveningDresses() {
        new WomenCatalogPage(driver).proceedToEveningDressesPage();
    }

    @And("in \"Add to Wishlist\" link outline heart icon for item {int} changed to solid")
    public void inLinkOutlineHeartIconForItemChangedToSolid(int numberOfItem) {
        List<ProductBlock> productList = new SummerDressesCatalogPage(driver).getProductsList();
        assertTrue(productList.get(numberOfItem - 1).isAddToWishlistSolidButtonDisplayed());
    }

    @Then("page with Evening Dresses opened")
    public void pageWithEveningDressesOpened() {
        assertTrue(new EveningDressesCatalogPage(driver).isPageTitleValid());
    }

    @When("I put one item {string} in a cart")
    public void putOneItemInACart(String productName) {
        new WomenCatalogPage(driver).addProductToCart(productName);
    }

    @Then("alert is displayed with a message Product successfully added to your shopping cart")
    public void alertIsDisplayedWithAMessage() {
        assertTrue(new WomenCatalogPage(driver).isProductAddedTitleVisible());
    }

    @And("item  {string} displayed in a cart")
    public void itemDisplayedInACart(String productName) {
        assertTrue(new WomenCatalogPage(driver).isProductAddedToCart(productName));
    }

    @When("I press Proceed to checkout button to Order Summary Page")
    public void pressProceedToCheckoutButton() {
        new WomenCatalogPage(driver).proceedToCheckout();
    }

    @Then("page with Summer dresses opened")
    public void pageWithSummerDressesOpened() {
        assertTrue(new SummerDressesCatalogPage(driver).isPageTitleValid());
    }

    @And("in \"Add to Wishlist\" link outline heart icon changed to solid")
    public void inLinkOutlineHeartIconChangedToSolid() {
        List<ProductBlock> productList = new WomenCatalogPage(driver).getProductsList();
        assertTrue(productList.get(0).isAddToWishlistSolidButtonDisplayed());
    }

    @Then("page with T-shirts opened")
    public void pageWithTShirtsOpened() {
        assertTrue(new TShirtsCatalogPage(driver).isPageTitleValid());
    }

    /**
     * There is a bugs.
     * Expected result for price:desc must be:
     * [50.99, 30.5, 28.98, 27.0, 26.0, 16.51, 16.4]
     * But actual result is:
     * [16.4, 16.51, 26.0, 27.0, 28.98, 30.5, 50.99]
     *
     * Expected result for name:desc must be:
     * [Printed Summer Dress, Printed Summer Dress, Printed Dress, Printed Dress, Printed Chiffon Dress, Faded Short Sleeve T-shirts, Blouse]
     * But actual result is:
     * [Blouse, Faded Short Sleeve T-shirts, Printed Chiffon Dress, Printed Dress, Printed Dress, Printed Summer Dress, Printed Summer Dress]
     */
    private static boolean sortByParam(String param, WomenCatalogPage catalog) {
        boolean isEquals;
        if (param.contains("name")) {
            List<String> actualProductsNames = catalog.getItemsInCatalog().stream().map(Product::getProductName).collect(Collectors.toList());
            List<String> expectedProductsNames;
            if (param.contains("desc")) {
                expectedProductsNames = Arrays.asList("Blouse", "Faded Short Sleeve T-shirts", "Printed Chiffon Dress", "Printed Dress", "Printed Dress", "Printed Summer Dress", "Printed Summer Dress");
            } else {
                expectedProductsNames = Arrays.asList("Blouse", "Faded Short Sleeve T-shirts", "Printed Chiffon Dress", "Printed Dress", "Printed Dress", "Printed Summer Dress", "Printed Summer Dress");
            }
            isEquals = expectedProductsNames.equals(actualProductsNames);
        } else {
            List<Double> actualProductsPrices = catalog.getItemsInCatalog().stream().map(Product::getProductPrice).collect(Collectors.toList());
            List<Double> expectedProductsPrices;
            if (param.contains("desc")) {
                expectedProductsPrices = Arrays.asList(16.4, 16.51, 26.0, 27.0, 28.98, 30.5, 50.99);
            } else {
                expectedProductsPrices = Arrays.asList(16.4, 16.51, 26.0, 27.0, 28.98, 30.5, 50.99);
            }
            isEquals = expectedProductsPrices.equals(actualProductsPrices);
        }
        return isEquals;
    }

}
