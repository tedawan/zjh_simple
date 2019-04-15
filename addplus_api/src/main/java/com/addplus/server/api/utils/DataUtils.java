package com.addplus.server.api.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述:数据处理工具类
 *
 * @author ljt
 * @version V1.0
 * @date 2017/9/23
 */
public class DataUtils {
    public final static BigDecimal bigDecimalSub = new BigDecimal("1");

    public final static BigDecimal bigDecimalPrecrnt = new BigDecimal("100"); // 计算百分比


    /**
     * 方法描述:多字符串判空工具类,为空或为空字符串即返回true
     *
     * @author ljt
     * @version V1.0
     * @date 2016/7/6 21:30
     */
    public static boolean isEmpty(String... strings) {
        if (StringUtils.isNoneBlank(strings)) {
            return false;
        }
        return true;
    }

    /**
     * 方法描述:多Integer判空工具类，为空或小于等于0即返回true
     *
     * @param integers
     * @return
     * @author ljt
     * @date 2016年7月20日 下午8:50:53
     * @version V1.0
     */
    public static boolean EmptyOrNegativeOrZero(Integer... integers) {
        boolean bool = false;
        for (Integer integer : integers) {
            if (integer == null || integer <= 0) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    public static boolean EmptyOrNegativeOrZero(Long... integers) {
        boolean bool = false;
        for (Long integer : integers) {
            if (integer == null || integer <= 0) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    /**
     * 方法描述:多Integer判空工具类，为空或小于0即返回true
     *
     * @param integers
     * @return
     * @author ljt
     * @date 2016年7月20日 下午8:50:53
     * @version V1.0
     */
    public static boolean EmptyOrNegative(Integer... integers) {
        boolean bool = false;
        for (Integer integer : integers) {
            if (integer == null || integer < 0) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    public static boolean EmptyOrNegative(Long... integers) {
        boolean bool = false;
        for (Long integer : integers) {
            if (integer == null || integer < 0) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    /**
     * 方法描述:List集合里面String转换成Integer
     *
     * @param list
     * @return
     * @author lijingtao
     * @date 2016年7月26日 上午9:31:06
     * @version V1.0
     */
    public static List<Integer> StringIdListToInteger(List<String> list) {
        return list.stream().map((x) -> new Integer(x.trim())).collect(Collectors.toList());
    }


    /**
     * 方法描述:Object 转 map
     *
     * @param thisObj
     * @return
     * @author ccc
     * @date 2016年8月11日 下午1:56:42
     */
    public static Map<String, String> objToMap(Object thisObj) {
        Map<String, String> map = new HashMap<String, String>();
        Class<?> c;
        try {
            c = Class.forName(thisObj.getClass().getName());
            Method[] m = c.getMethods();
            for (int i = 0; i < m.length; i++) {
                String method = m[i].getName();
                if (method.startsWith("get")) {
                    try {
                        Object value = m[i].invoke(thisObj);
                        if (value != null) {
                            String key = method.substring(3);
                            key = key.toLowerCase();
                            if (!"class".equals(key)) {
                                map.put(key, value.toString());
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 方法描述:double四舍五入保留小数位，返回double
     *
     * @param v     转换值
     * @param scale 保留的位数
     * @return
     * @author zhangjiehang
     * @date 2016年8月22日 上午9:10:36
     * @version V1.0
     */
    public static double roundToDouble(double v, int scale) {
        if (v == 0) {
            return v;
        }
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        try {
            BigDecimal bigDecimal = BigDecimal.valueOf(v).setScale(scale, BigDecimal.ROUND_HALF_UP);
            if (bigDecimal != null) {
                return bigDecimal.doubleValue();
            }
        } catch (Exception e) {
        }
        return 0d;
    }

    /**
     * 方法描述:double四舍五入保留小数位,返回String
     *
     * @param v     转换值
     * @param scale 保留的位数
     * @return
     * @author zhangjiehang
     * @date 2016年8月22日 上午9:10:36
     * @version V1.0
     */
    public static String roundToString(double v, int scale) {
        if (v == 0) {
            return "0";
        }
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(v).setScale(scale, BigDecimal.ROUND_HALF_UP);
        if (bigDecimal != null) {
            return bigDecimal.toString();
        }
        return null;
    }


    /**
     * 方法描述:string四舍五入保留小数位,返回String
     *
     * @param v     转换值
     * @param scale 保留的位数
     * @return
     * @author zhangjiehang
     * @date 2016年8月22日 上午9:10:36
     * @version V1.0
     */
    public static String roundToString(String v, int scale) {
        if (StringUtils.isBlank(v)) {
            return "0";
        }
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal bigDecimal = new BigDecimal(v).setScale(scale, BigDecimal.ROUND_HALF_UP);
        if (bigDecimal != null) {
            return bigDecimal.toString();
        }
        return null;
    }

    /**
     * 方法描述:
     *
     * @param nowRank 当前排名
     * @param allRank 总排名
     * @return
     * @throws @author zhangjiehang
     * @date 2016/9/24 16:40
     * @version V1.0
     */
    public static String calculateRankPercent(String nowRank, String allRank) {
        if (isEmpty(nowRank, allRank)) {
            return null;
        }
        BigDecimal now = new BigDecimal(nowRank);
        BigDecimal all = new BigDecimal(allRank);
        BigDecimal rank = all.subtract(now).add(bigDecimalSub).divide(all, 4, BigDecimal.ROUND_DOWN).multiply(bigDecimalPrecrnt).setScale(2);
        if (rank.toString().endsWith(".00")) {
            return rank.setScale(0).toString();
        }
        return rank.toString();

    }

    /**
     * 判断是否整数 true 是， false 不是
     */
    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()) && !"0".equals(s)) {
            return s.matches("^[0-9]*$");
        } else {
            return false;
        }
    }

    /**
     * 方法描述:截取list
     *
     * @param start 开始
     * @param end   介绍
     * @author ccc
     * @date 2016年08月29日 20:46:59
     */
    public static <T> List<T> subList(List<T> list, int start, int end) {
        List<T> childList = new ArrayList<T>();
        if (list != null) {
            if (list.size() < end) {
                end = list.size();
            }
            for (int i = start; i < end; i++) {
                childList.add(list.get(i));
            }
        }
        return childList;
    }

    /**
     * 方法描述: 去除List里面的重复数据，并保持顺序,传入对象需要在实体类重写
     *
     * @param memJsonList String list集合
     * @return List<T> 去重复的List集合
     * @throws @author zhangjiehang
     * @date 2016/10/18 11:44
     * @version V1.0
     */
    public static <T> List<T> removeDuplicateWithOrder(List<T> memJsonList) {
//        Set<T> set = new HashSet<T>();
//        List<T> newList = new ArrayList<T>();
//
//
//        for (Iterator iter = memJsonList.iterator(); iter.hasNext();)
//        {
//            Object element = iter.next();
//            if (set.add((T)element))
//                newList.add((T)element);
//        }
//        return newList;
        return memJsonList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 方法描述: 判断对象是否为空
     *
     * @param obs
     * @throws @author lanfeng
     * @date 2016/12/08 13:13
     * @version V1.0
     */
    public static boolean isEmptyObject(Object... obs) {
        boolean bool = false;
        for (Object obj : obs) {
            if (obj == null) {
                bool = true;
                break;
            } else if (obj instanceof String && StringUtils.isBlank(obj.toString())) {
                bool = true;
                break;
            }
        }
        return bool;
    }


    /**
     * 方法描述：生成一个范围在0到size中的5个不同的随机数数组
     *
     * @param size（大于5）
     * @return int[]
     * @throws Exception
     * @author caoyixiang
     * @date 2016/12/26 15:59
     * @version 1.0
     */
    public static int[] randRomNum(int size) {
        int s[] = new int[5];
        for (int i = 0; i < s.length; ) {
            s[i] = (int) (0 + Math.random() * size);
            if (checkBoolean(s, s[i], i)) {
                i++;
            }
        }
        return s;
    }

    private static boolean checkBoolean(int s[], int si, int inum) {
        for (int i = 0; i < s.length; i++) {
            if (s[i] == si && inum != i) {
                return false;
            }
        }
        return true;
    }

    public static Double parseDouble(String intStr) {
        if (isEmptyObject(intStr)) {
            return null;
        }
        try {
            return Double.parseDouble(intStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer parseInteger(String intStr) {
        if (isEmptyObject(intStr)) {
            return null;
        }
        try {
            return Integer.parseInt(intStr);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 方法描述：根据数据总数获取需要循环次数
     *
     * @param count 数据总数
     * @return loop 循环次数
     * @author lanfeng
     */
    public static int getSize(long count, int loop) {

        if (loop < 1) {
            return 0;
        }

        int size = 1;
        if (count > loop) {
            if (count % loop == 0) {
                size = (int) count / loop;
            } else {
                size = (int) count / loop + 1;
            }
        }
        return size;
    }


    /**
     * 方法描述:  生成订单
     *
     * @author yh
     * @date 2017年04月17日 19:21:10
     */

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }


    /**
     * 方法描述:根据规则切割字符串，返回切割后的数组
     *
     * @author gzx
     * @date 2017-06-14 11:28:28
     */
    public static String[] getStringArrayByRegex(String str, String regex) {
        if (DataUtils.isEmpty(str)) {
            return null;
        }
        return str.split(regex);
    }


    /**
     * 方法描述: 将url param String 转为 map
     *
     * @author fyq
     * @date 2017年07月26日 10:39:26
     */
    public static Map<String, String> getUrlParams(String param, String mosaic) {
        Map<String, String> map = new HashMap<>();
        if ("".equals(param) || null == param) {
            return map;
        }
        String[] params = param.split(mosaic);
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    public static Map<String, String> getUrlParams(String param) {
        return getUrlParams(param, "&");
    }


    /**
     * 方法描述:base64 编码数据
     *
     * @author gzx
     * @date 2017-08-25 13:58:22
     */
    public static String base64Encode(String data) {
        if (isEmpty(data)) {
            return "";
        }
        return Base64.decode(data).toString();
    }

    /**
     * description: double四舍五入两位小数
     * author: wlb
     * date: 18-1-8 下午4:54
     */
    public static double formatDouble(double d) {
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        return bg.doubleValue();
    }

    /**
     * 方法描述: 比较两个字符串 不是字符串转化为字符串 并对输入做相应判断
     */
    public static boolean stringCompare(Object one, Object another) {
        if (one == null || another == null) {
            return false;
        }
        return String.valueOf(one).equals(String.valueOf(another));
    }

    /**
     * 方法描述：用于支付宝经过json信息解析方法
     */
    public static Map<String, String> aliPayNotifyMap(String params) {
        Map<String, JSONArray> mapContex = JSON.parseObject(params, Map.class);
        Map<String, String> mapParam = new HashMap<>();
        for (Iterator iter = mapContex.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            JSONArray values = mapContex.get(name);
            String valueStr = "";
            for (int i = 0; i < values.size(); i++) {
                valueStr = (i == values.size() - 1) ? valueStr + values.getString(i)
                        : valueStr + values.getString(i) + ",";
            }
            mapParam.put(name, valueStr);
        }
        return mapParam;
    }

}
