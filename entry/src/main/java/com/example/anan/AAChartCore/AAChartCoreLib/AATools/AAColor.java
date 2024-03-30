package com.example.anan.AAChartCore.AAChartCoreLib.AATools;

public class AAColor {
    public final static String Black = blackColor();
    public final static String DarkGray = darkGrayColor();
    public final static String LightGray = lightGrayColor();
    public final static String White = whiteColor();
    public final static String Gray = grayColor();
    public final static String Red = redColor();
    public final static String Green = greenColor();
    public final static String Blue = blueColor();
    public final static String Cyan = cyanColor();
    public final static String Yellow = yellowColor();
    public final static String Magenta = magentaColor();
    public final static String Orange = orangeColor();
    public final static String Purple = purpleColor();
    public final static String Brown = brownColor();
    public final static String Clear = clearColor();

    /**
     * rgbaColor
     *
     * @param red   red
     * @param green green
     * @param blue  blue
     * @param alpha alpha
     * @return String
     */
    public static String rgbaColor(
            Integer red,
            Integer green,
            Integer blue,
            Float alpha
    ) {
        return "rgba(" + red + "," + green + "," + blue + "," + alpha + ")";
    }


    private static String blackColor() {
        return "black";
    }

    private static String darkGrayColor() {
        return "darkGray";
    }

    private static String lightGrayColor() {
        return "lightGray";
    }

    private static String whiteColor() {
        return "white";
    }

    private static String grayColor() {
        return "gray";
    }

    private static String redColor() {
        return "red";
    }

    private static String greenColor() {
        return "green";
    }

    private static String blueColor() {
        return "blue";
    }

    private static String cyanColor() {
        return "cyan";
    }

    private static String yellowColor() {
        return "yellow";
    }

    private static String magentaColor() {
        return "magenta";
    }

    private static String orangeColor() {
        return "orange";
    }

    private static String purpleColor() {
        return "purple";
    }

    private static String brownColor() {
        return "brown";
    }

    private static String clearColor() {
        return "clear";
    }


}
