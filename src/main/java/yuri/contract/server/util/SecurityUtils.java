package yuri.contract.server.util;

import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecurityUtils {
    public static String MD5(String string) {
        return DigestUtils.md5DigestAsHex(string.getBytes());
    }

    public static String getToken(String account) {
        String result = "";
        for (int i = 0; i < account.length(); i++) {
            result = MD5(result + Math.random() + account.charAt(i));
        }
        return MD5(result);
    }

    public static Date getDate(String string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
        try {
            return simpleDateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
