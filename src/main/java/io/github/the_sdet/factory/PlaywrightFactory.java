package io.github.the_sdet.factory;

import com.microsoft.playwright.*;
import io.github.the_sdet.globalconstants.Device;
import io.github.the_sdet.logger.Log;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.the_sdet.util.ConfigReader.getPropertyValue;

/**
 * This class is responsible for managing playwright and Page instances
 */
@SuppressWarnings("all")
public class PlaywrightFactory {
    private static ThreadLocal<Playwright> threadLocalPlaywright = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> threadLocalBrowserContext = new ThreadLocal<>();
    private static ThreadLocal<Browser> threadLocalBrowser = new ThreadLocal<>();
    private static ThreadLocal<Page> threadLocalPage = new ThreadLocal<>();

    /**
     * This method returns the current playwright instance from the threadLocal
     *
     * @return current page instance
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public static Playwright getPlaywright() {
        return threadLocalPlaywright.get();
    }

    /**
     * This method returns the current browser instance from the threadLocal
     *
     * @return current browser instance
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public static BrowserContext getBrowserContext() {
        return threadLocalBrowserContext.get();
    }

    /**
     * This method returns the current browser context instance from the threadLocalPage
     *
     * @return current browser context instance
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public static Browser getBrowser() {
        return threadLocalBrowser.get();
    }

    /**
     * This method returns the current page instance from the threadLocalPage
     *
     * @return current page instance
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public static Page getPage() {
        List<Page> pages = threadLocalPage.get().context().pages();
        return pages.get(pages.size() - 1);
    }

    /**
     * This method closes the playwright instance
     *
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public static void closePlaywright() {
        if (threadLocalPage.get() != null) {
            threadLocalPage.get().context().browser().close();
            threadLocalPlaywright.get().close();
            Log.info("Browser Closed.");
        } else {
            Log.info("No browser to close.");
        }
    }

    /**
     * This method initializes the playwright and page instance making use of values from config.properties.
     * Takes care of execution environment, browser, device type, remote or local execution
     *
     * @author Pabitra Swain (pabitra.swain.work@gmail.com)
     */
    public static Page initializePage() {
        String executionType, browserName, deviceName;
        boolean headlessmode = false;
        try {
            deviceName = getPropertyValue("device");
            executionType = getPropertyValue("execution.type");
            browserName = getPropertyValue("browser");
            headlessmode = Boolean.parseBoolean(getPropertyValue("headless.mode"));
            Log.info("Headless mode is: " + headlessmode);
        } catch (Exception e) {
            e.printStackTrace();
            deviceName = "DESKTOP";
            executionType = "local";
            browserName = "chrome";
        }

        if (executionType.equals("remote")) {
            Map<String, String> env = new HashMap<>();
            env.put("SELENIUM_REMOTE_URL", getPropertyValue("remote.url"));
            threadLocalPlaywright.set(Playwright.create(new Playwright.CreateOptions().setEnv(env)));
            Log.info("Execution to be done on remote setup. " + env);
        } else {
            threadLocalPlaywright.set(Playwright.create());
            Log.info("Execution to be done on local setup.");
        }

        switch (browserName.toLowerCase()) {
            case "chrome":
                threadLocalBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(headlessmode).setSlowMo(50)));
                break;
            case "edge":
                threadLocalBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(headlessmode).setSlowMo(50)));
                break;
            case "firefox":
                threadLocalBrowser.set(getPlaywright().firefox().launch(new BrowserType.LaunchOptions().setHeadless(headlessmode).setSlowMo(50)));
                break;
            case "safari":
                threadLocalBrowser.set(getPlaywright().webkit().launch(new BrowserType.LaunchOptions().setHeadless(headlessmode).setSlowMo(50)));
                break;
            case "chromium":
            default:
                threadLocalBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(headlessmode).setSlowMo(50)));
        }
        switch (deviceName.toUpperCase()) {
            case "DESKTOP":
                if (headlessmode) {
                    threadLocalBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions()
                            .setViewportSize(Device.DESKTOP.width, Device.DESKTOP.height)));
                } else {
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int maxWidth = (int) screenSize.getWidth();
                    int maxHeight = (int) screenSize.getHeight();
                    Log.info("Desktop Viewport: " + maxWidth + " x " + maxHeight);
                    threadLocalBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions()
                            .setViewportSize(maxWidth, maxHeight)));
                }
                break;
            case "MOBILE":
                threadLocalBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions()
                        .setViewportSize(Device.MOBILE.width, Device.MOBILE.height)
                        .setDeviceScaleFactor(2.625)
                        .setIsMobile(true)
                        .setHasTouch(true)));
                break;
            case "TABLET":
                threadLocalBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions()
                        .setViewportSize(Device.TABLET.width, Device.TABLET.height)
                        .setDeviceScaleFactor(2.625)
                        .setIsMobile(true)
                        .setHasTouch(true)));
                break;
            default:
                threadLocalBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions().setViewportSize(Device.DESKTOP.width, Device.DESKTOP.height)));
        }

        threadLocalPage.set(getBrowserContext().newPage());
        threadLocalPage.get().setDefaultNavigationTimeout(60000);
        threadLocalPage.get().setDefaultTimeout(5000);
        Log.info("Browser launched !!.");
        return getPage();
    }
}