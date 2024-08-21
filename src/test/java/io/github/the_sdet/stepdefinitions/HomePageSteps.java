package io.github.the_sdet.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.the_sdet.pages.HomePage;
import org.junit.Assert;

import static io.github.the_sdet.factory.PlaywrightFactory.getPage;

public class HomePageSteps {

    HomePage homePage = new HomePage(getPage());

    @Given("Logo is displayed")
    public void logoIsDisplayedOnHomePage() {
        Assert.assertTrue("Logo Not Visible...",
                homePage.isVisible(homePage.getLogo()));
    }

    @Given("SearchBox is displayed")
    public void searchBoxIsDisplayedOnHomePage() {
        Assert.assertTrue("Search Box Not Visible...",
                homePage.isVisible(homePage.getSearchBox()));
    }

    @Given("Cart Icon is displayed")
    public void cartIconIsDisplayedOnHomePage() {
        Assert.assertTrue("Cart Icon Not Visible...",
                homePage.isVisible(homePage.getCartIcon()));
    }

    @Given("Menu Bar is displayed")
    public void menuBarIsDisplayed() {
        Assert.assertTrue("Menu Bar is NOT Displayed...",
                homePage.isVisible(homePage.getMenuBar()));
    }

    @Given("Menu Has These Options")
    public void menuHasTheseOptions(DataTable expectedParentMenuItems) {
        Assert.assertTrue("Menu Items Mismatch...",
                homePage.validateParentMenuItems(expectedParentMenuItems.asList()));
    }

    @Then("{string} Menu Item has child menu options as below")
    public void menuItemHasChildMenuOptionsAsBelow(String parentMenuItem, DataTable subMenuItems) {
        Assert.assertTrue("Menu Items Mismatch...",
                homePage.validateMenuItems(parentMenuItem, subMenuItems.asList()));
    }

    @Then("Hot Sellers Section is displayed")
    public void hotSellersSectionIsDisplayed() {
        Assert.assertTrue("Hot Seller Section is NOT displayed...",
                homePage.isVisible(homePage.getHotSellers()));
    }

    @And("Hot Seller Items are displayed")
    public void hotSellerItemsAreDisplayed(DataTable dataTable) {
        Assert.assertEquals("Hot Seller Products Mismatch...",
                dataTable.asList(), homePage.getHotSellerProducts());
    }
}