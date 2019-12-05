package yuri.contract.server.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.DigestUtils;
import yuri.contract.server.util.constant.Constant;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SecurityUtils {

    private static SecretKey generateKey() {
        String stringKey = Constant.SALT;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        log.info("encodedKey:{}", encodedKey);
        log.info("encodedBase64URLSafeString:{}", encodedKey);
        return Keys.hmacShaKeyFor(encodedKey);
    }

    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generateKey();
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
    }

    public static String createJWT(String name, long expirationTime) throws Exception {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", name);
        SecretKey key = generateKey();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setSubject(name)
                .signWith(key);
        if (expirationTime >= 0) {
            long expTime = nowMillis + expirationTime;
            Date exp = new Date(expTime);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static String getToken(String account)  {
        try {
            return createJWT(account, 60 * 60 * 1000);
        } catch (Exception e) {
            return null;
        }
    }
}
