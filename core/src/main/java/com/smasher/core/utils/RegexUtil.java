package com.smasher.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author moyu
 * @date 2017/3/17
 */
public class RegexUtil {

    /**
     * 昵称的规则,只能包含字母,数字,汉字,长度位不超过2~12位
     */
    private static final String NICKNAME_REGEX = "[a-zA-Z0-9\u4e00-\u9f5a]{2,12}";

    /**
     * Email规则
     */
    private static final String EMAIL_REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    /**
     * 正则验证Email
     *
     * @param input input
     * @return boolean
     */
    public static boolean matchEmail(String input) {
        return input.matches(EMAIL_REGEX);
    }

    /**
     * 正则验证NickName
     *
     * @param input input
     * @return boolean
     */
    public static boolean matchNickName(String input) {
        return input.matches(NICKNAME_REGEX);
    }

    /**
     * 正则验证
     *
     * @param input input
     * @param regex regex
     * @return boolean
     */
    public static boolean matchRegex(String input, String regex) {
        return input.matches(regex);
    }

    /**
     * 过滤标点符号，只允许字母、数字和汉字
     *
     * @param str str
     * @return String
     * @throws PatternSyntaxException
     */
    public static String filterPunctuation(String str) throws PatternSyntaxException {
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
