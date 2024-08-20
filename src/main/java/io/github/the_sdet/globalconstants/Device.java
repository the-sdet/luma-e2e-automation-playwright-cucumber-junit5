package io.github.the_sdet.globalconstants;

/**
 * This enum provides us a predefined viewport (height and width) of the devices to be used in test
 *
 * @author Pabitra Swain (pabitra.swain.work@gmail.com)
 */
public enum Device {
    DESKTOP(1366, 768),
    MOBILE(545, 588),
    TABLET(1024, 1366);

    public final int width;
    public final int height;

    Device(int width, int height) {
        this.width = width;
        this.height = height;
    }
}