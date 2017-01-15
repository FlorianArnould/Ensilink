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
     * @return a 15% darker color
     */
    public static int darkerColor(int color){
        float HSVColor[] = new float[3];
        Color.colorToHSV(color, HSVColor);
        HSVColor[2] *= 0.85;
        return Color.HSVToColor(HSVColor);
    }
}
