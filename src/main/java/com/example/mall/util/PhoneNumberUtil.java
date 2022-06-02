
package com.example.mall.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PhoneNumberUtil {

    /**
     * 判断是否为11位电话号码
     *
     * @param phone
     * @return
     */
    public boolean isPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0-8])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public boolean isNotPhoneNumber(String phone) {
        return !isPhoneNumber(phone);
    }
}

