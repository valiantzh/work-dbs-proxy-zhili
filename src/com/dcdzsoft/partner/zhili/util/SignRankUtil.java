package com.dcdzsoft.partner.zhili.util;

import java.util.Arrays;
import java.util.Map;

import com.dcdzsoft.sda.security.SecurityUtils;
/**
 * <p>Title: 智能自助包裹柜系统</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2014</p>
 *
 * <p>Company: 杭州东城电子有限公司</p>
 *
 * @author 
 * @version 1.0
 */
public class SignRankUtil {
	/**
	 * 将Map里所有参数按照字段名的 ascii 码从小到大排序后使用 QueryString 的格式（即key1=value1&key2=value2…）和signName拼接而成string
	 * @param map
	 * @param signName
	 * @return String
	 */
	public static String signRank(Map<String, String>map,String signName){		
        String[] paramKeys = new String[map.size()];
        int index = 0;
        for(String key: map.keySet()){
            paramKeys[index++] = key;
        }
        Arrays.sort(paramKeys);      
        StringBuffer signBuff = new StringBuffer();
        index = 0;
        for(String key : paramKeys){
            String value = map.get(key);
            signBuff.append("&").append(key).append("=").append(value);        
        }
        signBuff.deleteCharAt(0);
        signBuff.append("&key=").append(signName);  
        signBuff.insert(0, "key="+signName+"&");
        String  signStr = SecurityUtils.md5(signBuff.toString());
        return signStr;
	}
}
