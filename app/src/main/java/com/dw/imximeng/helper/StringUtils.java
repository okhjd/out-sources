package com.dw.imximeng.helper;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hjd
 * @time 2017-11-03 14:58
 */

public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static Pattern IMG_URL = Pattern
            .compile(".*?(gif|jpeg|png|jpg|bmp)");

    private final static Pattern URL = Pattern
            .compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");
    /**
     * 手机号码及电话号码验证
     *
     * @param string
     * @return
     */
    public static boolean isMobileOrPhone(String string) {
        if (isEmpty(string)) {
            return false;
        }
        if (string.length() != 11) {
            if (isPhone(string)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (isPhone(string) || isMobile(string)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断字符串是否为empty
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)||"null".equals(str))
            return true;
        return false;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 判断字符串是否为数字与字母的组合
     * @param psd   字符串
     * @return      true：数字与字母的组合
     */
    public static boolean ispsd(String psd) {
        Pattern p = Pattern
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
        Matcher m = p.matcher(psd);

        return m.matches();
    }

    /***
     * 计算两个时间差，返回的是的天
     *
     * @author 火蚁 2015-2-9 下午4:50:06
     *
     * @return long
     * @param dete1
     * @param date2
     * @return
     */
    public static long calDateDifferentDay(String dete1, String date2) {

        long diff = 0;

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = dateFormater2.get().parse(dete1);
            d2 = dateFormater2.get().parse(date2);

            // 毫秒ms
            diff = d2.getTime() - d1.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return diff / 1000 / 60 / 60 / 24;
    }
    /**
     * 价格格式化 如123 123.1 123.11
     * @param money
     * @return
     */
    public static double setMoney(double money){
        double priceMoney;
        String s1 = String.valueOf(money);
        String s2 = s1.substring(s1.indexOf('.') + 1);
        if (s2.length()==1&&s1.substring(s1.length()-1, s1.length()).equals("0")){
            priceMoney=Double.parseDouble(s1.substring(0,s1.indexOf(".")));
        }else {
            priceMoney=money;
        }
        return priceMoney;
    }
    public static String conversionTimeFormat(String sdate){
        return dateFormater4.get().format(toDate(sdate));
    }
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM/dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater4 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd HH:mm");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    public static Date toDate1(String sdate) {
        return toDate(sdate, dateFormater2.get());
    }

    public static Date conversionTime(String sdate) {
        return toDate(sdate, dateFormater4.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateString(Date date) {
        return dateFormater.get().format(date);
    }

    public static String getDateString1(Date date) {
        return dateFormater3.get().format(date);
    }

    public static String getDateString(String sdate){
        return getDateString1(toDate1(sdate));
    }

    /**
     * 以逗号分割字符串
     * @param string
     * @return
     */
    public static String[] splitStringWhitCommas(String string){
        return string.split(",");
    }
    /**
     * 判断是否为一个合法的url地址
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        return URL.matcher(str).matches();
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    /**
     * 字符串相加，避免null字符串
     *
     * @param strings
     * @return
     */
    public static String stringsIsEmpty(String... strings) {
        String result = "";
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null) {
                strings[i] = "";
                continue;
            }
            result += strings[i];
        }
        return result;
    }
}
