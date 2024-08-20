package io.github.the_sdet.stepdefinitions;

import io.cucumber.java.en.Given;
import io.github.the_sdet.pages.HomePage;
import org.junit.Assert;

import static io.github.the_sdet.cucumber.CucumberUtils.logToReport;
import static io.github.the_sdet.factory.PlaywrightFactory.getPage;

public class HomePageSteps {

    HomePage homePage = new HomePage(getPage());

    @Given("Logo is displayed on Home Page")
    public void logoIsDisplayedOnHomePage() {
        Assert.assertTrue("Logo Not Visible...", homePage.isLogoDisplayed());
        logToReport("Logo is Displayed...");
    }

    @Given("SearchBox is displayed on Home Page")
    public void searchBoxIsDisplayedOnHomePage() {
        Assert.assertTrue("Search Box Not Visible...", homePage.isSearchBoxDisplayed());
        logToReport("Search Box is Displayed...");
    }

    @Given("Cart Icon is displayed on Home Page")
    public void cartIconIsDisplayedOnHomePage() {
        Assert.assertTrue("Cart Icon Not Visible...", homePage.isCartIconDisplayed());
        logToReport("Cart Icon is Displayed...");
    }
}