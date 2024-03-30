package com.example.anan.AAChartCore.AAChartCoreLib.AATools;

import java.util.HashMap;
import java.util.Map;

public class AAGradientColor {
    public final static Map<String, Object> OceanBlue = oceanBlueColor();
    public final static Map<String, Object> Sanguine = sanguineColor();
    public final static Map<String, Object> LusciousLime = lusciousLimeColor();
    public final static Map<String, Object> PurpleLake = purpleLakeColor();
    public final static Map<String, Object> FreshPapaya = freshPapayaColor();
    public final static Map<String, Object> Ultramarine = ultramarineColor();
    public final static Map<String, Object> PinkSugar = pinkSugarColor();
    public final static Map<String, Object> LemonDrizzle = lemonDrizzleColor();
    public final static Map<String, Object> VictoriaPurple = victoriaPurpleColor();
    public final static Map<String, Object> SpringGreens = springGreensColor();
    public final static Map<String, Object> MysticMauve = mysticMauveColor();
    public final static Map<String, Object> ReflexSilver = reflexSilverColor();
    public final static Map<String, Object> NeonGlow = neonGlowColor();
    public final static Map<String, Object> BerrySmoothie = berrySmoothieColor();
    public final static Map<String, Object> NewLeaf = newLeafColor();
    public final static Map<String, Object> CottonCandy = cottonCandyColor();
    public final static Map<String, Object> PixieDust = pixieDustColor();
    public final static Map<String, Object> FizzyPeach = fizzyPeachColor();
    public final static Map<String, Object> SweetDream = sweetDreamColor();
    public final static Map<String, Object> Firebrick = firebrickColor();
    public final static Map<String, Object> WroughtIron = wroughtIronColor();
    public final static Map<String, Object> DeepSea = deepSeaColor();
    public final static Map<String, Object> CoastalBreeze = coastalBreezeColor();
    public final static Map<String, Object> EveningDelight = eveningDelightColor();


    private static Map<String, Object> oceanBlueColor() {
        return oceanBlueColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> sanguineColor() {
        return sanguineColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> lusciousLimeColor() {
        return lusciousLimeColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> purpleLakeColor() {
        return purpleLakeColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> freshPapayaColor() {
        return freshPapayaColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> ultramarineColor() {
        return ultramarineColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> pinkSugarColor() {
        return pinkSugarColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> lemonDrizzleColor() {
        return lemonDrizzleColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> victoriaPurpleColor() {
        return victoriaPurpleColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> springGreensColor() {
        return springGreensColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> mysticMauveColor() {
        return mysticMauveColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> reflexSilverColor() {
        return reflexSilverColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> neonGlowColor() {
        return neonGlowColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> berrySmoothieColor() {
        return berrySmoothieColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> newLeafColor() {
        return newLeafColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> cottonCandyColor() {
        return cottonCandyColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> pixieDustColor() {
        return pixieDustColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> fizzyPeachColor() {
        return fizzyPeachColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> sweetDreamColor() {
        return sweetDreamColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> firebrickColor() {
        return firebrickColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> wroughtIronColor() {
        return wroughtIronColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> deepSeaColor() {
        return deepSeaColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> coastalBreezeColor() {
        return coastalBreezeColor(AALinearGradientDirection.ToTop);
    }

    private static Map<String, Object> eveningDelightColor() {
        return eveningDelightColor(AALinearGradientDirection.ToTop);
    }

    /**
     * oceanBlueColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> oceanBlueColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#2E3192", "#1BFFFF");
    }

    /**
     * sanguineColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> sanguineColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#D4145A", "#FBB03B");
    }

    /**
     * lusciousLimeColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> lusciousLimeColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#009245", "#FCEE21");
    }

    /**
     * purpleLakeColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> purpleLakeColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#662D8C", "#ED1E79");
    }

    /**
     * freshPapayaColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> freshPapayaColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#ED1C24", "#FCEE21");
    }

    /**
     * ultramarineColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> ultramarineColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#00A8C5", "#FFFF7E");
    }

    /**
     * pinkSugarColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> pinkSugarColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#D74177", "#FFE98A");
    }

    /**
     * lemonDrizzleColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> lemonDrizzleColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#FB872B", "#D9E021");
    }

    /**
     * victoriaPurpleColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> victoriaPurpleColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#312A6C", "#852D91");
    }

    /**
     * springGreensColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> springGreensColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#009E00", "#FFFF96");
    }

    /**
     * mysticMauveColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> mysticMauveColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#B066FE", "#63E2FF");
    }

    /**
     * reflexSilverColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> reflexSilverColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#808080", "#E6E6E6");
    }

    /**
     * neonGlowColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> neonGlowColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#00FFA1", "#00FFFF");
    }

    /**
     * berrySmoothieColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> berrySmoothieColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#8E78FF", "#FC7D7B");
    }

    /**
     * newLeafColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> newLeafColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#00537E", "#3AA17E");
    }

    /**
     * cottonCandyColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> cottonCandyColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#FCA5F1", "#B5FFFF");
    }

    /**
     * pixieDustColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> pixieDustColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#D585FF", "#00FFEE");
    }

    /**
     * fizzyPeachColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> fizzyPeachColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#F24645", "#EBC08D");
    }

    /**
     * sweetDreamColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> sweetDreamColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#3A3897", "#A3A1FF");
    }

    /**
     * firebrickColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> firebrickColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#45145A", "#FF5300");
    }

    /**
     * wroughtIronColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> wroughtIronColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#333333", "#5A5454");
    }

    /**
     * deepSeaColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> deepSeaColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#4F00BC", "#29ABE2");
    }

    /**
     * coastalBreezeColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> coastalBreezeColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#00B7FF", "#FFFFC7");
    }

    /**
     * eveningDelightColor
     *
     * @param direction direction
     * @return Map
     */
    public static Map<String, Object> eveningDelightColor(AALinearGradientDirection direction) {
        return linearGradient(direction, "#93278F", "#00A99D");
    }

    /**
     * linearGradient
     *
     * @param startColor startColor
     * @param endColor   endColor
     * @return Map
     */
    public static Map<String, Object> linearGradient(String startColor,
                                                     String endColor) {
        return linearGradient(AALinearGradientDirection.ToTop, startColor, endColor);
    }

    /**
     * linearGradient
     *
     * @param direction  direction
     * @param startColor startColor
     * @param endColor   endColor
     * @return Map
     */
    public static Map<String, Object> linearGradient(
            AALinearGradientDirection direction,
            String startColor,
            String endColor
    ) {
        return linearGradient(direction, new Object[][]{
                {0, startColor},
                {1, endColor},
        });
    }

    /**
     * linearGradient
     *
     * @param direction direction
     * @param stopsArr  stopsArr
     * @return Map
     */
    public static Map<String, Object> linearGradient(
            AALinearGradientDirection direction,
            Object[][] stopsArr
    ) {
        Map linearGradientColorMap = new HashMap<String, Object>();
        linearGradientColorMap.put("linearGradient", linearGradientMap(direction));
        linearGradientColorMap.put("stops", stopsArr);
        return linearGradientColorMap;
    }

    /**
     * (0,0) ----------- (1,0)
     * |                   |
     * |                   |
     * |                   |
     * |                   |
     * |                   |
     * (0,1) ----------- (1,1)
     *
     * @param direction direction
     * @return Map
     */
    private static Map linearGradientMap(AALinearGradientDirection direction) {
        switch (direction) {
            case ToTop:
                return linearGradientMap(0, 1, 0, 0);
            case ToBottom:
                return linearGradientMap(0, 0, 0, 1);
            case ToLeft:
                return linearGradientMap(1, 0, 0, 0);
            case ToRight:
                return linearGradientMap(0, 0, 1, 0);
            case ToTopLeft:
                return linearGradientMap(1, 1, 0, 0);
            case ToTopRight:
                return linearGradientMap(0, 1, 1, 0);
            case ToBottomLeft:
                return linearGradientMap(1, 0, 0, 1);
            case ToBottomRight:
                return linearGradientMap(0, 0, 1, 1);
            default:
                return null;
        }
    }

    private static Map linearGradientMap(int x1, int y1, int x2, int y2) {
        Map linearGradientMap = new HashMap<String, Integer>();
        linearGradientMap.put("x1", x1);
        linearGradientMap.put("y1", y1);
        linearGradientMap.put("x2", x2);
        linearGradientMap.put("y2", y2);
        return linearGradientMap;
    }
}
