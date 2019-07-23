/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月22日
 */
package cn.withmes.springboot.singlepointjwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Map;

/**
 * ClassName: JWTUtil
 *
 * @author leegoo
 * @Description: jwt 工具类
 * @date 2019年07月22日
 */
public class JWTUtil {

    public static String generatorToken(Map<String, Object> payload) {

        try {
            String token = Jwts.builder().setPayload(new ObjectMapper().writeValueAsString(payload))
                    .signWith(SignatureAlgorithm.HS256, createKey())
                    .compact();
            return token;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static SecretKeySpec createKey() {
        byte[] bin = DatatypeConverter.parseBase64Binary("leegookey");
        return new SecretKeySpec(bin, SignatureAlgorithm.HS256.getJcaName());
    }

    public static Claims parseToken(String key) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(createKey()).parseClaimsJws(key);
        return claimsJws.getBody();
    }
}
