package com.wdk.sports.util;


import java.math.BigInteger;

/**
 * 进制转换器工具类
 *
 */
public class HexConverter {

    /**
     * 将整数转换为指定位数的16进制数
     * @param n
     * @param size
     * @return
     */
    public static String intToHex(int n,int size) {
        StringBuffer s = new StringBuffer();
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            s = s.append(b[n%16]);
            n = n/16;
        }
        a = s.reverse().toString();
        a  = add_zore(a,size);
        return a;
    }
    public static String add_zore(String str, int size){
        if (str.length()<size){
            str= "0"+str;
            str=add_zore(str,size);
            return str;
        }else {
            return str;
        }
    }
    public static String xiaoDuan( String hex ){
        return hex.substring( 2 ) + hex.substring( 0,2 ) ;
    }


    /**
     * 取反 + 1
     * @param hex
     * @return  取反+1之后的值
     */
    public static String backAndAddOne( String hex ){
        byte[] bytes = HexConverter.hexTobytes(hex);

        byte temp;
        for(int i=0;i<bytes.length;i++){
            temp = bytes[i];
            bytes[i] = (byte) (~temp);
        }

        // 取反后的16进制数
        String hex1 = HexConverter.byte2Hex(bytes);
        // +1 操作
        String hexString = Long.toHexString(Long.parseLong(hex1, 16) + Long.parseLong("1", 16));

        return hexString ;
    }

    /**
     * @Description: 16进制字符串转10进制
     * @Param: [hex]
     * @return: java.lang.String
     * @Author: Liang Shan
     * @Date: 2019/9/24 0024
     */
    public static int hexStringToDecimal(String hex) {
        Integer result = Integer.valueOf(hex, 16);
        return result;
    }

    /**
     * @Description: 16进制字符串转2进制字符串
     * @Param: [hex]
     * @return: java.lang.String
     * @Author: Liang Shan
     * @Date: 2019/9/24 0024
     */
    public static String hexStringToBinaryString(String hex) {
        Integer temp = Integer.valueOf(hex, 16);
        String result = Integer.toBinaryString(temp);
        return result;
    }

    /**
     * @Description: 10进制转16进制字符串
     * @Param: []
     * @return: java.lang.String
     * @Author: Liang Shan
     * @Date: 2019/9/24 0024
     */
    public static String decimalToHexString(Integer decimal) {
        String result = Integer.toHexString(decimal);
        return result;
    }

    /**
     * @Description: 10进制转2进制字符串
     * @Param: []
     * @return: java.lang.String
     * @Author: Liang Shan
     * @Date: 2019/9/24 0024
     */
    public static String decimalToBinaryString(Integer decimal) {
        String result = Integer.toBinaryString(decimal);
        return result;
    }

    /**
     * @Description: 2进制字符串转10进制
     * @Param: [hex]
     * @return: java.lang.String
     * @Author: Liang Shan
     * @Date: 2019/9/24 0024
     */
    public static Integer binaryStringToDecimal(String binary) {
        Integer result = Integer.valueOf(binary, 2);
        return result;
    }

    /**
     * @Description: 2进制字符串转16进制字符串
     * @Param: []
     * @return: java.lang.String
     * @Author: Liang Shan
     * @Date: 2019/9/24 0024
     */
    public static String binaryStringToHexString(String binary) {
        Integer temp = Integer.valueOf(binary, 2);
        String result = Integer.toHexString(temp);
        return result;
    }

    /**
     * @Description: byte[]转字符串
     * @Param: [bytes]
     * @return: java.lang.String
     * @Author: Liang Shan
     * @Date: 2019/9/20 0020
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xff);
            if (temp.length() == 1) {
                // 得到的一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * @Description: byte[]转String[]
     * @Param: [bytes]
     * @return: java.lang.String[]
     * @Author: Liang Shan
     * @Date: 2019/9/20 0020
     */
    public static String[] bytesToStrings(byte[] bytes) {
        String[] strings = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            String temp = Integer.toHexString(bytes[i] & 0xff);
            if (temp.length() == 1) {
                strings[i] = '0' + temp;
            } else {
                strings[i] = temp;
            }
        }
        return strings;
    }


    /**
     * 16进制字符串转为byte[]
     * @param hex
     * @return
     */
    public static byte[] hexTobytes(String hex) {
        if (hex.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hex.length() / 2];
            int j = 0;
            for(int i = 0; i < hex.length(); i+=2) {
                result[j++] = (byte)Integer.parseInt(hex.substring(i,i+2), 16);
            }
            return result;
        }
    }
}
