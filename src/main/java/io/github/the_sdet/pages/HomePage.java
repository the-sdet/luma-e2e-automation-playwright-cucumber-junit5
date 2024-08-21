package io.github.the_sdet.pages;

import com.microsoft.playwright.Page;
import io.github.the_sdet.logger.Log;
import lombok.Getter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.the_sdet.cucumber.CucumberUtils.logFailureToReport;

@Getter
public class HomePage extends BasePage {
    public HomePage(Page page) {
        super(page);
    }

    String logo = "//a[@aria-label='store logo']/img";
    String searchBox = "#search";
    String cartIcon = ".minicart-wrapper a";
    String signInButton = "//a[contains(text(),'Sign In')]";
    String createAccountButton = "//a[contains(text(),'Create an Account')]";
    String menuBar = ".navigation";
    String menuOptionParent = "//ul[@role='menu' and contains(@style,'display: block;')]//span[contains(text(),\"v1\")] | //ul[@role='menu']//span[contains(text(),\"v1\")]";
    String menuOption = "//ul[@role='menu' and contains(@style,'display: block;')]//span[contains(text(),'v1')]";
    String hotSellers = "//h2[contains(text(),'Hot Sellers')]";
    String hotSellerItems = "//div[@class='product-item-details']";
    String hotSellerItemName = "//a[@class='product-item-link']";
    String hotSellerItemPrice = "//span[@data-price-type='finalPrice']";

    public String getMenuItem(String menuTitle) {
        String xpath = customizeXpath(menuOption, menuTitle);
        Log.info(xpath);
        return xpath;
    }

    /**
     * This method gets the locator of the parent menu items
     *
     * @param menuTitle String - menu title
     * @return Locator of the menu
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public String getParentMenuItem(String menuTitle) {
        return customizeXpath(menuOptionParent, menuTitle);
    }

    /**
     * This method gets all the hot seller products and their prices as Product Name → Price
     *
     * @return List of hot seller products and their prices
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public List<String> getHotSellerProducts() {
        List<String> hotSellerProducts = new ArrayList<>();
        for (int i = 1; i <= getElements(hotSellerItems).size(); i++) {
            String basePath = "(" + hotSellerItems + ")[" + i + "]";
            String productName = getElementTextContent(basePath + hotSellerItemName);
            String productPrice = getElementTextContent(basePath + hotSellerItemPrice);
            hotSellerProducts.add(productName + " -> " + productPrice);
        }
        return hotSellerProducts;
    }

    /**
     * This method validates the menu and submenu items
     *
     * @param expectedParentMenuItems List of Parent Menu Items
     * @return returns true if all the menu items are correctly displayed, else false
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public boolean validateParentMenuItems(List<String> expectedParentMenuItems) {
        boolean assertion = true;
        for (String menuItem : expectedParentMenuItems) {
            if (!waitAndCheckIsVisible(getParentMenuItem(menuItem), Duration.ofSeconds(5))) {
                assertion = false;
                logFailureToReport("Menu Bar doesn't have the option: " + menuItem);
            }
        }
        return assertion;
    }

    /**
     * This method validates the menu and submenu items
     *
     * @param parentMenuItemName Parent Menu Name
     * @param subMenuItems       Sub and Grand Sub Menu Items
     * @return returns true if all the menu items are correctly displayed, else false
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public boolean validateMenuItems(String parentMenuItemName, List<String> subMenuItems) {
        boolean assertion = true;
        Map<String, String[]> menuOptions = new HashMap<>();
        boolean hasGrandChildMenu = false;
        for (String subMenu : subMenuItems) {
            String[] options = subMenu.split("->");
            hasGrandChildMenu = options.length > 1;
            if (hasGrandChildMenu)
                menuOptions.put(options[0].trim(), options[1].trim().split(", "));
            else menuOptions.put(options[0].trim(), null);
        }

        for (Map.Entry<String, String[]> menuDetails : menuOptions.entrySet()) {
            String childMenuOption = menuDetails.getKey();

            hoverOverElement(getParentMenuItem(parentMenuItemName));
            if (!isVisible(getMenuItem(childMenuOption))) {
                assertion = false;
                logFailureToReport(parentMenuItemName + " doesn't have " + childMenuOption + " option under it...");
            }
            hoverOverElement(getMenuItem(childMenuOption));
            if (hasGrandChildMenu) {
                String[] grandChildMenuOptions = menuDetails.getValue();
                for (String grandChildMenuOption : grandChildMenuOptions) {
                    if (!isVisible(getMenuItem(grandChildMenuOption))) {
                        assertion = false;
                        logFailureToReport(parentMenuItemName + " -> " + childMenuOption + " doesn't have " + grandChildMenuOption + " option under it...");
                    }
                }
            }
        }
        return assertion;
    }
}