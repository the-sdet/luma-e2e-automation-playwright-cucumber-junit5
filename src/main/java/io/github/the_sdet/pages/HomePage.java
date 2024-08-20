package io.github.the_sdet.pages;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage {
    public HomePage(Page page) {
        super(page);
    }

    String logo = "//a[@aria-label='store logo']/img";
    String searchBox = "#search";
    String cartIcon = ".minicart-wrapper a";

    public boolean isLogoDisplayed() {
        return isVisible(logo);
    }

    public boolean isSearchBoxDisplayed() {
        return isVisible(searchBox);
    }

    public boolean isCartIconDisplayed() {
        return isVisible(cartIcon);
    }
}
