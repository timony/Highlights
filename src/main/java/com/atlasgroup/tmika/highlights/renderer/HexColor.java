package com.atlasgroup.tmika.highlights.renderer;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class HexColor {

    public static final String HEX_TEMPLATE = "#%02x%02x%02x";
    private static final int MIN = 100;
    private static final int MAX = 255;

    private HexColor() {}

    public static String random() {
        final var localRandom = ThreadLocalRandom.current();
        final var color = new Color(localRandom.nextInt(MIN, MAX), localRandom.nextInt(MIN, MAX), localRandom.nextInt(MIN, MAX));
        return String.format(HEX_TEMPLATE, color.getRed(), color.getGreen(), color.getBlue());
    }
}
