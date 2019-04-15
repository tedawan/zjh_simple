package com.addplus.server.consumer.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class SortUtil
{

    private final static Comparator<String> comparator = (String str1, String str2) -> (str1.compareTo(str2));

    public static Object[] sortMapByKey(Map<String, String> map) throws Exception {
        Optional<Map<String, String>> optional = Optional.ofNullable(map);
        if (!optional.isPresent())
        {
            return new Object[] {};
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(comparator);
        sortMap.putAll(optional.get());
        Object[] res = new Object[sortMap.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : sortMap.entrySet())
        {
            if (entry.getValue() instanceof String[]){
                String[] param = (String[])((Map.Entry)entry).getValue();
                res[i] = param[0];
            }else {
                if ("null".equals(entry.getValue())) {
                    res[i] = null;
                } else {
                    res[i] = entry.getValue();
                }
            }
            i++;
        }
        return res;
    }
    
}
