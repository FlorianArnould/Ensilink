package fr.ensicaen.lbssc.ensilink;

import android.graphics.Color;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Class to modify colors
 */
public final class ColorCreator {

    /**
     *
     * @param color the color to modify
     * @return a 10% darker color
     */
    public static int darkerColor(int color){
        float HSVColor[] = new float[3];
        Color.colorToHSV(color, HSVColor);
        HSVColor[2] *= 0.9;
        return Color.HSVToColor(HSVColor);
    }

    /**
     *
     * @param color the color to modify
     * @return the same color but 50% transparent
     */
    public static int semiTransparentColor(int color){
        return Color.argb(125, Color.red(color), Color.green(color), Color.blue(color));
    }
}
