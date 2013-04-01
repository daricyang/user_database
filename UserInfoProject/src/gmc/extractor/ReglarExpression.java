/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Pok
 */
public class ReglarExpression {

    /**
     * 正则匹配
     *
     * @param regex 正则表达式
     * @param source 待匹配的文本
     * @return content 返回匹配结果——字符串
     */
    public static String Reglar(String regex, String source) {
        StringBuilder sb = new StringBuilder();
        final List<String> list = new ArrayList<String>();
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(source);
        while (ma.find()) {
            list.add(ma.group());
        }
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * 正则匹配
     *
     * @param regex
     * @param source
     * @return resList 返回结果列表
     */
    public static List<String> ReglarArray(String regex, String source) {
        final List<String> list = new ArrayList<String>();
        final Pattern pa = Pattern.compile(regex);
        final Matcher ma = pa.matcher(source);
        while (ma.find()) {
            list.add(ma.group());
        }
        return list;
    }
}
