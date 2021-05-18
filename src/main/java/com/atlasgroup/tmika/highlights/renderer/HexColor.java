package com.atlasgroup.tmika.highlights.renderer;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HexColor {

    private static Random random = new Random();

    private HexColor() {}

    public static String random() {
        final String[] letters = "0123456789ABCDEF".split("");
        return IntStream.range(0, 6)
                .mapToObj(i -> letters[Math.round(random.nextFloat() * 15)])
                .collect(Collectors.joining("", "#", ""));
    }
}
