package io.github.the_sdet.pages;

import com.microsoft.playwright.Page;
import io.github.the_sdet.web.PlaywrightUtils;

/**
 * This class is the parent class of every page classe available in the project.
 * This is supposed to hold all the generic elements and their respective methods that is visible and can be used from any of the pages.
 */
@SuppressWarnings("all")
public class BasePage extends PlaywrightUtils {
    protected BasePage(Page page) {
        super(page);
    }
}