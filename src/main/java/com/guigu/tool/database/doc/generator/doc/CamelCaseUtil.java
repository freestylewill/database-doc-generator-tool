package com.guigu.tool.database.doc.generator.doc;

import org.apache.commons.lang3.StringUtils;

/**
 * 驼峰与下划线之间互转工具类
 * 
 * @author：bood
 * @since：2020/2/9
 */
public class CamelCaseUtil {

    private static final char SEPARATOR = '_';

    public CamelCaseUtil() {
    }

    /**
     * @param s: 名称
     * @description: 转下划线
     * @return：java.lang.String
     * @author：ray
     * @create：2020/2/9
     */
    public static String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * @param s: 名称
     * @description: 转驼峰
     * @return：java.lang.String
     * @author：ray
     * @create：2020/2/9
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @param s: 名称
     * @description: 转大写驼峰
     * @return：java.lang.String
     * @author：ray
     * @create：2020/2/9
     */
    public static String toCapitalizeCamelCase(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}
